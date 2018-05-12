package org.poem.tools.utils.collection;

import java.util.Map;

/**
 * commons utils
 */
public final class CollectionUtils extends org.apache.commons.collections.CollectionUtils {


    public static Boolean isNotEmpty(Map<String, Object> loaderClass) {
        return loaderClass != null && !loaderClass.isEmpty();
    }
}
