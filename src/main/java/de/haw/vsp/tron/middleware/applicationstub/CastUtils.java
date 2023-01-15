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
        T result = (T) Array.newInstance(clazz.getComponentType(), Array.getLength(input)); // [[25, 32]], Integer[][] // [25, 32], Integer[]
        for (int i = 0; i < Array.getLength(input); i++) { // 
            Object element = Array.get(input, i);           //[25, 32], Integer[][]
            if (element.getClass().isArray()) {
                Array.set(result, i, deepCast((Object[]) element, clazz.getComponentType()));   //[25, 32], Integer[]
            } else {
                Array.set(result, i, clazz.getComponentType().cast(element)); // Integer
            }
        }
        return result;
    }
    
}
