package org.poem.config;


import org.poem.core.annotation.ShellComponent;
import org.poem.core.annotation.ShellMethod;
import org.poem.core.annotation.ShellOptions;
import org.poem.core.bean.ShellMethodParameter;
import org.poem.core.bean.ShellMethodTarget;
import org.poem.core.exception.ShellParameterException;
import org.poem.tools.utils.collection.Lists;
import org.poem.tools.utils.collection.Maps;
import org.poem.tools.utils.logger.LoggerUtils;
import org.poem.tools.utils.string.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.util.ReflectionUtils;

import javax.annotation.PostConstruct;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

/**
 * 方法注册
 * @author poem
 */
public class ShellMethodTargetRegistrar {


    private ApplicationContext applicationContext;

    /**
     * 所有的方法都在这儿
     * 对应的是 group - 方法的参数
     */
    private static final Map<String, List<ShellMethodTarget>> commands = Maps.emptys();

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
        for (String beanName : commandBeans.keySet()) {
            String bName = beanName;
            Object bean = commandBeans.get(beanName);
            Class<?> clazz = bean.getClass();
            ShellComponent shellOptions = clazz.getAnnotation(ShellComponent.class);
            if (shellOptions != null && StringUtils.isNoneBlank(shellOptions.name())) {
                bName = shellOptions.name();
            }
            Map<String, Method> methods = Maps.emptys();
            List<ShellMethodTarget> shellMethodTargets = Lists.empty();
            ReflectionUtils.doWithMethods(clazz, new ReflectionUtils.MethodCallback() {
                @Override
                public void doWith(Method method) throws IllegalArgumentException, IllegalAccessException {
                    ShellMethod shellMapping = method.getAnnotation(ShellMethod.class);
                    String name = shellMapping.name();
                    if (StringUtils.isEmpty(name)) {
                        name = method.getName();
                    }
                    if (methods.get(name) != null) {
                        throw new IllegalArgumentException(
                                String.format("Illegal registration for command '%s': Attempt to register both '%s' and '%s'", name, methods.get(name), method));
                    }
                    methods.put(name, method);
                    String detail = shellMapping.detail();
                    List<ShellMethodParameter> methodParameterMap = Lists.empty();
                    String[] param = discoverer.getParameterNames(method);
                    this.validateShortName(beanName, method, param);
                    Annotation[][] annotateds = method.getParameterAnnotations();
                    Annotation[] annotations;
                    Class[] paramClazzs = method.getParameterTypes();
                    if (param.length != paramClazzs.length && annotateds.length != paramClazzs.length) {
                        try {
                            throw new ShellParameterException(
                                    String.format("Illegal registration for command '%s': All Paramter Must Has Option:@ShellOptions", name)
                            );
                        } catch (ShellParameterException e) {
                            LoggerUtils.error(e.getMessage());
                            e.printStackTrace();
                        }
                    } else {
                        for (int i = 0; i < param.length; i++) {
                            annotations = annotateds[i];
                            methodParameterMap.add(new ShellMethodParameter(param[i], paramClazzs[i], join("", annotations)));
                        }
                        ShellMethodTarget target = new ShellMethodTarget(method, bean, name, detail, methodParameterMap);
                        shellMethodTargets.add(target);
                    }
                }

                /**
                 * 自定义方法
                 * @param param
                 */
                private void validateShortName(String name, Method method, String[] param) {
                    Map<String, String> mSets = Maps.emptys();
                    String shortName;
                    if (null != param && param.length > 0) {
                        for (String s : param) {
                            shortName = StringUtils.shortName(s);
                            if (mSets.keySet().contains(shortName)) {
                                throw new IllegalArgumentException(
                                        String.format("Illegal registration for class  '%s' ," +
                                                " method '%s': Attempt to register both parameter option '-%s'(%s) and '-%s'(%s)", name, method, shortName, mSets.get(shortName), shortName, s));
                            }
                            mSets.put(shortName, s);
                        }
                    }
                }
            }, new ReflectionUtils.MethodFilter() {
                @Override
                public boolean matches(Method method) {
                    return method.getAnnotation(ShellMethod.class) != null;
                }
            });
            commands.put(bName, shellMethodTargets);
        }
    }

    private static String join(String split, Annotation... arg) {
        StringBuffer stringBuffer = new StringBuffer();
        if (null == arg || arg.length == 0) {
            return null;
        }
        for (Annotation s : arg) {
            if (s instanceof ShellOptions) {
                ShellOptions shellOptions = (ShellOptions) s;
                stringBuffer.append(shellOptions.detail()).append(split);
            }
        }
        String options = stringBuffer.toString();
        return options.endsWith(split) ? options.trim() : options;
    }

    /**
     * 获取命令
     *
     * @return
     */
    public static Map<String, List<ShellMethodTarget>> getCommands() {
        return commands;
    }
}
