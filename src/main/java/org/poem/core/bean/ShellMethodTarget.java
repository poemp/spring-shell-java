package org.poem.core.bean;

import java.lang.reflect.Method;
import java.util.Map;

public class ShellMethodTarget {

    /**
     * 方法
     */
    private final Method method;

    /**
     * java bean
     */
    private final Object bean;

    /**
     * name
     */
    private final String name;

    /**
     * 描述
     */
    private final String detail;

    /**
     * 方法的参数
     */
    private final Map<String ,ShellMethodParameter> methodParameterMap;



    public ShellMethodTarget(Method method, Object bean, String name, String detail, Map<String, ShellMethodParameter> methodParameterMap) {
        this.method = method;
        this.bean = bean;
        this.name = name;
        this.detail = detail;
        this.methodParameterMap = methodParameterMap;
    }

    public Map<String, ShellMethodParameter> getMethodParameterMap() {
        return methodParameterMap;
    }

    public Method getMethod() {
        return method;
    }

    public Object getBean() {
        return bean;
    }

    public String getName() {
        return name;
    }

    public String getDetail() {
        return detail;
    }
}
