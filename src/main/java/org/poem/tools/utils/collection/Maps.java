package org.poem.tools.utils.collection;

import java.util.HashMap;
import java.util.Map;

public class Maps {

    public static <K, V> Map<K,V> emptys(){
        return new HashMap<>();
    }
    
    public static Boolean isNotEmpty(Map<String, Object> loaderClass) {
        return loaderClass != null && !loaderClass.isEmpty();
    }
}
