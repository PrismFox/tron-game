package middleware.examples;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class MethodRefExample {
    
    private Map<String, Function<Object[], Object>> methodMap = new HashMap<>();
    private final String uuid = "12345";

    public static void main(String[] args) {
        new MethodRefExample();
    }

    public MethodRefExample() {
        String methodString = String.format("%s_%s::putIntIntoArray", getClass().getCanonicalName(), uuid);
        registerMethod(methodString, this::putIntIntoArrayWrapper);
        int[] result = (int[]) callMethod(methodString, Integer.valueOf(5), Integer.valueOf(7));
        System.out.println(String.format("%s: [%d, %d] Length: %d", result.getClass().getName(), result[0], result[1], result.length));
    }

    public int[] putIntIntoArray(int i1, int i2) {
        return new int[] {i1, i2};
    }

    private Object putIntIntoArrayWrapper(Object[] args) {
        int i1 = (int) args[0];
        int i2 = (int) args[1];
        Object result = putIntIntoArray(i1, i2);
        return result;
    }

    public void registerMethod(String methodName, Function<Object[], Object> method) {
        methodMap.put(methodName, method);
    }

    public Object callMethod(String methodName, Object... args) {
        Function<Object[], Object> method = methodMap.get(methodName);
        Object result = method.apply(args);
        return result;
    }
}
