package org.poem.config;


import org.poem.core.annotation.ShellComponent;
import org.poem.core.annotation.ShellMethod;
import org.poem.core.bean.ShellMethodParameter;
import org.poem.core.bean.ShellMethodTarget;
import org.poem.tools.utils.collection.Maps;
import org.poem.tools.utils.string.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.util.ReflectionUtils;

import javax.annotation.PostConstruct;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * 方法注册
 */
public class ShellMethodTargetRegistrar {


    private ApplicationContext applicationContext;

    /**
     * 全局的数据
     */
    private static final Map<String, ShellMethodTarget> commands = new HashMap<>();

    @Autowired
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    /**
     * 注册方法
     */
    @PostConstruct
    public void register() {
        //获取全部的类的信息
        Map<String, Object> commandBeans = applicationContext.getBeansWithAnnotation(ShellComponent.class);
        //获取方法参数名
        LocalVariableTableParameterNameDiscoverer discoverer = new LocalVariableTableParameterNameDiscoverer();
        for (Object bean : commandBeans.values()) {
            Class<?> clazz = bean.getClass();
            ReflectionUtils.doWithMethods(clazz, new ReflectionUtils.MethodCallback() {
                @Override
                public void doWith(Method method) throws IllegalArgumentException, IllegalAccessException {
                    ShellMethod shellMapping = method.getAnnotation(ShellMethod.class);
                    String name = shellMapping.name();
                    if (StringUtils.isEmpty(name)) {
                        name = method.getName();
                    }
                    String detail = shellMapping.detail();
                    Map<String, ShellMethodParameter> methodParameterMap = Maps.emptys();
                    String[] param = discoverer.getParameterNames(method);
                    Class[] paramClazzs = method.getParameterTypes();
                    for (int i = 0 ; i < param.length; i++){
                        methodParameterMap.put("--" + param[i], new ShellMethodParameter(param[i],paramClazzs[i]));
                    }
                    ShellMethodTarget target = new ShellMethodTarget(method, bean, name, detail, methodParameterMap);
                    commands.put(name, target);
                }
            }, new ReflectionUtils.MethodFilter() {
                @Override
                public boolean matches(Method method) {
                    return method.getAnnotation(ShellMethod.class) != null;
                }
            });
        }
    }
}
