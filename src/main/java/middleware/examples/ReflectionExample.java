package middleware.examples;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ReflectionExample {
    private List<String> validMethods = new ArrayList<>();

    public void registerMethod(String className, String methodName) {
        validMethods.add(String.format("%s.%s", className, methodName));
    }

    public Object callMethod(String classname, String methodName, Object... args) throws ClassNotFoundException, NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        if(validMethods.contains(String.format("%s.%s", classname, methodName)));
        Class c = Class.forName(classname);
        Class[] paramTypes = Arrays.asList(args).stream().map(e -> e.getClass()).toArray(Class[]::new);
        Method m = c.getDeclaredMethod(methodName, paramTypes);
        Object result = m.invoke(null, args);
        return result;
    }
}
