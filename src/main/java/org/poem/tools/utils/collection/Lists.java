package org.poem.tools.utils.collection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * lists extend java.utils.ArrayList
 *
 * @since java
 */
public final class Lists<E> extends ArrayList<E> {

    /**
     * 返回一个空的列表
     *
     * @param <T>
     * @return
     */
    public static <T> List<T> empty() {
        return new ArrayList<>();
    }


    /**
     * 构造一个新的list
     *
     * @param args 数组
     * @param <T>  数据类型
     * @return
     */
    public static <T> List<T> asList(T... args) {
        if (null == args) {
            return Lists.empty();
        }
        List<T> newList = new ArrayList<>(args.length);
        newList.addAll(Arrays.asList(args));
        return newList;
    }
}
