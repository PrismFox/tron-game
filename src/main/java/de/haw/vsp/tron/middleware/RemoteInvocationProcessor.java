package de.haw.vsp.tron.middleware;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.ExecutableType;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;

import de.haw.vsp.tron.middleware.annotation.RemoteImplementation;
import de.haw.vsp.tron.middleware.annotation.RemoteInterface;


@SupportedAnnotationTypes({"de.haw.vsp.tron.middleware.annotation.RemoteInterface", "de.haw.vsp.tron.middleware.annotation.RemoteImplementation"})
@SupportedSourceVersion(SourceVersion.RELEASE_11)
public class RemoteInvocationProcessor extends AbstractProcessor {

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for(TypeElement annotation : annotations) {
            Set<? extends Element> annotatedElements = roundEnv.getElementsAnnotatedWith(annotation);
            Map<Boolean, List<Element>> annotatedMethods = annotatedElements.stream().collect(
                Collectors.partitioningBy(element -> 
                    element.getKind().isInterface()
                )
            );

            List<Element> interfaces = annotatedMethods.get(true);
            List<Element> otherTypes = annotatedMethods.get(false);
            otherTypes.forEach(element -> 
                processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, "RemoteInterface may only be applied to an Interface.", element)
            );

            if(interfaces.isEmpty()) {
                continue;
            }
            TypeElement interf = (TypeElement) interfaces.get(0);

            String className = interf.getQualifiedName().toString();
            Map<String, List<String>> methodMap = interf.getEnclosedElements().stream()
                .filter(e -> e.getKind() == ElementKind.METHOD)
                .collect(Collectors.toMap(
                    method -> ((Element) method).getSimpleName().toString(),
                    method -> Stream.concat(
                            Stream.of(((ExecutableType) method.asType()).getReturnType().toString()), 
                            ((ExecutableType) method.asType()).getParameterTypes().stream().map(e -> e.toString())
                        ).collect(Collectors.toList())
                ));
            try {
                if(interf.getAnnotation(RemoteInterface.class) != null) {
                    if(interf.getAnnotation(RemoteImplementation.class) != null) {
                        processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, "An Interface may not be simultaneously annotated as RemoteInterface and RemoteImplementation.", interf);
                    }
                    writeRPCInvokerFile(className, methodMap);
                } else if(interf.getAnnotation(RemoteImplementation.class) != null) {
                    writeImplWrapperFile(className, methodMap);
                } // else exception
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }
        return false;
    }

    private void writeImplWrapperFile(String className, Map<String, List<String>> methodMap) throws IOException {
        String packageName = null;
        int lastDot = className.lastIndexOf('.');
        if(lastDot > 0) {
            packageName = className.substring(0, lastDot);
        }

        String simpleClassName = className.substring(lastDot + 1);
        String implWrapperClassName = className + "ImplWrapper";
        String implWrapperSimpleClassName = implWrapperClassName.substring(lastDot + 1);

        JavaFileObject implWrapperFile = processingEnv.getFiler().createSourceFile(implWrapperClassName);

        try (PrintWriter out = new PrintWriter(implWrapperFile.openWriter())) {
            if(packageName != null) {
                out.print("package ");
                out.print(packageName);
                out.println(";");
                out.println();
            }

            out.println("import org.springframework.stereotype.Component;");
            out.println("import org.springframework.beans.factory.annotation.Autowired;");
            out.println("import javax.annotation.PostConstruct;");
            out.println("import de.haw.vsp.tron.middleware.applicationstub.IImplRegistry;");
            out.println();

            out.println("@Component");
            out.print("public class ");
            out.print(implWrapperSimpleClassName);
            out.println(" {");
            out.println();

            out.println("    @Autowired");
            out.println("    private IImplRegistry implRegistry;");
            out.println();

            out.println("    @Autowired");
            out.print("    private ");
            out.print(className);
            out.println(" providedImpl;");
            out.println();

            methodMap.entrySet().forEach(method -> {
                List<String> typeList = new ArrayList<>(method.getValue());
                String returnType = typeList.remove(0);
                String methodName = method.getKey();

                out.print("    private Object ");
                out.print(methodName);
                out.println("Wrapper(Object... args) {");

                out.print("        ");
                if(!returnType.equals("void")) {
                    out.print("Object result = ");
                }
                out.print("providedImpl.");
                out.print(methodName);
                out.print("(");

                for(int i = 0; i < typeList.size(); i++) {
                    out.print("(");
                    out.print(typeList.get(i));
                    out.printf(") args[%d]", i);
                    if(i < typeList.size()-1) {
                        out.print(", ");
                    }
                }
                out.println(");");

                out.print("        return ");
                if(returnType.equals("void")) {
                    out.println("null;");
                } else {
                    out.println("result;");
                }
                out.println("    }");
                out.println();
            });

            out.println("    @PostConstruct");
            out.println("    private void registerWrapperMethods() {");

            methodMap.entrySet().stream().forEach(method -> {
                List<String> typeList = new ArrayList<>(method.getValue());
                typeList.remove(0);

                String methodName = method.getKey();

                out.print("        implRegistry.registerImplementation(\"");
                out.print(className);
                out.print(" ");
                out.print(methodName);
                typeList.forEach(param -> {
                    out.print(" ");
                    out.print(param);
                });
                out.print("\", this::");
                out.print(methodName);
                out.println("Wrapper);");
            });
            out.println("    }");

            out.println("}");
        }
    }

    private void writeRPCInvokerFile(String className, Map<String, List<String>> methodMap) throws IOException {
        String packageName = null;
        int lastDot = className.lastIndexOf('.');
        if(lastDot > 0) {
            packageName = className.substring(0, lastDot);
        }

        String simpleClassName = className.substring(lastDot + 1);
        String rpcInvokerClassName = className + "RPCInvokerImpl";
        String rpcInvokerSimpleClassName = rpcInvokerClassName.substring(lastDot + 1);

        JavaFileObject rpcInvokerFile = processingEnv.getFiler().createSourceFile(rpcInvokerClassName);

        try (PrintWriter out = new PrintWriter(rpcInvokerFile.openWriter())) {
            if(packageName != null) {
                out.print("package ");
                out.print(packageName);
                out.println(";");
                out.println();
            }
            out.println("import org.springframework.stereotype.Component;");
            out.println("import org.springframework.beans.factory.annotation.Autowired;");
            out.println("import de.haw.vsp.tron.middleware.clientstub.IClientStub;");

            out.println("@Component");
            out.print("public class ");
            out.print(rpcInvokerSimpleClassName);
            out.print(" implements ");
            out.print(simpleClassName);
            out.println(" {");
            out.println();

            out.println("    @Autowired");
            out.println("    private IClientStub clientStub;");
            out.println();
            
            methodMap.entrySet().stream().forEach(method -> {
                String methodName = method.getKey();
                List<String> typeList = new ArrayList<>(method.getValue());
                String returnType = typeList.remove(0);

                out.println("    @Override");
                out.print("    public ");
                out.print(returnType);
                out.print(" ");
                out.print(methodName);
                out.print("(");
                
                for(int i = 0; i < typeList.size(); i++) {
                    out.print(typeList.get(i));
                    out.printf(" arg%d", i);
                    if(i < typeList.size()-1) {
                        out.print(", ");
                    }
                }

                out.println(") {");

                out.print("        ");
                if(!returnType.equals("void")) {
                    out.print(returnType);
                    out.print(" result = (");
                    out.print(returnType);
                    out.print(") ");
                }

                out.print("clientStub.invokeSynchronously(\"");
                out.print(className);
                out.print(" ");
                out.print(methodName);
                typeList.forEach(param -> {
                    out.print(" ");
                    out.print(param);
                });
                out.print("\"");

                if(typeList.size() > 0) {
                    out.print(", ");
                }

                for(int i = 0; i < typeList.size(); i++) {
                    out.printf("arg%d", i);
                    if(i < typeList.size() - 1) {
                        out.print(", ");
                    }
                }
                out.println(");");

                if(!returnType.equals("void")) {
                    out.println("        return result;");
                }
                out.println("    }");
                out.println();
            });
            out.println("}"); 
        }
    }
    
}
