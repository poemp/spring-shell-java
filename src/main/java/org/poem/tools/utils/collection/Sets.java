package org.poem.tools.utils.collection;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * 扩展子HashSet
 *
 * @param <E>
 */
public final class Sets<E> extends HashSet<E> {

    /**
     * 返回一个空的set
     *
     * @param <T> 数据类型
     * @return 返回一个空的set
     */
    public static <T> Set<T> empty() {
        return new HashSet<>();
    }


    /**
     * 创建一个新的Set
     * @param args 放入set的数组
     * @param <T> 数据类型
     * @return 返回一个新的set
     */
    public static <T> Set<T> newHashSet(T ... args){
        if(args == null){
            return  Sets.empty();
        }
        Set<T> newSet = new HashSet<>(args.length);
        newSet.addAll(Arrays.asList(args));
        return  newSet;
    }


    /**
     * 把新的不定长数据变成set
     * @param args 放入的数据
     * @param <T> 数据类型
     * @return 新构造的set数据
     */
    public  <T> Set<T> asHashSet(T ... args){
        return Sets.newHashSet(args);
    }
}
