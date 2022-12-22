package middleware;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;

import middleware.annotation.RPC;

@SupportedAnnotationTypes("middleware.annotation.RemoteMethod")
@SupportedSourceVersion(SourceVersion.RELEASE_17)
public class MiddlewareProcessor extends AbstractProcessor {

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for(TypeElement annotation : annotations) {
            Set<? extends Element> annotatedElements = roundEnv.getElementsAnnotatedWith(annotation);
            Map<Boolean, List<Element>> annotatedMethods = annotatedElements.stream().collect(
                Collectors.partitioningBy(element -> 
                    element.getEnclosingElement().getKind().isInterface() && (element.getEnclosingElement().getAnnotation(RPC.class) != null)
                )
            );

            List<Element> interfaceMethods = annotatedMethods.get(true);
            List<Element> otherMethods = annotatedMethods.get(false);
            otherMethods.forEach(element -> 
                processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, "RemoteMethod may only be applied to a method inside an interface with an @RPC annotation.", element)
            );

            
        }
        return false;
    }
    
}
