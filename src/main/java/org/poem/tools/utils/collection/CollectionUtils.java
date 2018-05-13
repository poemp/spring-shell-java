package org.poem.tools.utils.collection;

import java.util.Map;

/**
 * commons utils
 */
public final class CollectionUtils extends org.apache.commons.collections.CollectionUtils {


    public static Boolean isNotEmpty(Map<String, Object> loaderClass) {
        return loaderClass != null && !loaderClass.isEmpty();
    }


    public static String join(String ... args){
        return  CollectionUtils.join(",", args);
    }

    public static String join(String split, String ... arg){
        StringBuffer stringBuffer = new StringBuffer();
        if(null == arg || arg.length  == 0){
            return null;
        }
        for (String s : arg) {
            stringBuffer.append(s).append(split);
        }
        return  stringBuffer.substring(0,stringBuffer.length() -1 );
    }

}
