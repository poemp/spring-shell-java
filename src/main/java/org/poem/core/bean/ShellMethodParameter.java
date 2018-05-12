package org.poem.core.bean;


public class ShellMethodParameter {

    /**
     * 参数名称
     */
    private final String name;

    /**
     * 方法的参数的类型
     */
    private final Class clazz;


    public ShellMethodParameter(String name,Class clazz) {
        this.clazz = clazz;
        this.name = name;
    }

    public Class getClazz() {
        return clazz;
    }

    public String getName() {
        return name;
    }
}
