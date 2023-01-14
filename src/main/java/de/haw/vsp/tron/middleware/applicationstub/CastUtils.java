package de.haw.vsp.tron.middleware.applicationstub;

import java.lang.reflect.Array;
import java.util.HashMap;



public class CastUtils {
    public static <K, T> java.util.Map<K, T> convertMapToCorrectType(java.util.Map<K, Object[]> map, Class<T> c) {
        java.util.Map<K, T> resultMap = new HashMap<>();
        map.entrySet().stream().forEach(e -> resultMap.put(e.getKey(), deepCast(e.getValue(), c)));   
        return resultMap;
    }

    public static <T> T deepCast(Object[] input, Class<T> clazz) {
        T result = (T) Array.newInstance(clazz, Array.getLength(input));
        for (int i = 0; i < Array.getLength(input); i++) {
            Object element = Array.get(input, i);
            if (element.getClass().isArray()) {
                Array.set(result, i, deepCast((Object[]) element, clazz.getComponentType()));
            } else {
                Array.set(result, i, clazz.cast(element));
            }
        }
        return result;
    }
    
}
