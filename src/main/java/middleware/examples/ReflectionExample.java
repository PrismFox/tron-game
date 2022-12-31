package middleware.examples;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ReflectionExample {
    private List<String> validMethods = new ArrayList<>();
    private Map<String, Object> instances = new HashMap<>();
    private final String uuid = "test";

    public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        new ReflectionExample();
    }

    public ReflectionExample() throws ClassNotFoundException, NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        registerInstance(null, null);


 
        registerMethod(this.getClass().getCanonicalName(), "putIntIntoArray");
        registerInstance(uuid, this);

        int[] result = (int[]) callMethod(this.getClass().getCanonicalName(), uuid, "putIntIntoArray", Integer.valueOf(5), Integer.valueOf(7));
        System.out.println(String.format("%s: [%d, %d] Length: %d", result.getClass().getName(), result[0], result[1], result.length));
    }

    public int[] putIntIntoArray(Integer i1, Integer i2) {
        return new int[] {i1, i2};
    }

    public void registerMethod(String className, String methodName) {
        validMethods.add(String.format("%s.%s", className, methodName));
    }

    public void registerInstance(String objectUUID, Object instance) {
        instances.put(objectUUID, instance);
    }

    public Object callMethod(String classname, String objectUUID, String methodName, Object... args) throws ClassNotFoundException, NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        if(!validMethods.contains(String.format("%s.%s", classname, methodName))) throw new IllegalArgumentException(String.format("Method %s not registered for class %s", methodName, classname));
        if(!instances.containsKey(objectUUID)) throw new IllegalArgumentException(String.format("Object with UUID %s not registered", objectUUID));
        Class c = Class.forName(classname);
        Class[] paramTypes = Arrays.asList(args).stream().map(e -> e.getClass()).toArray(Class[]::new);
        Method m = c.getDeclaredMethod(methodName, paramTypes);
        Object result = m.invoke(instances.get(objectUUID), args);
        return result;
    }
}
