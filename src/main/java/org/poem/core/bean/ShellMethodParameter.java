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

    /**
     * 描述
     */
    private final String detail;


    public ShellMethodParameter(String name, Class clazz, String detail) {
        this.name = name;
        this.clazz = clazz;
        this.detail = detail;
    }

    public String getName() {
        return name;
    }

    public Class getClazz() {
        return clazz;
    }

    public String getDetail() {
        return detail;
    }
}
