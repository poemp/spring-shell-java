package org.poem.core;


import org.apache.commons.cli.*;
import org.poem.api.Runner;
import org.poem.config.ApplicationContext;
import org.poem.config.ShellCommandParse;
import org.poem.config.ShellMethodTargetRegistrar;
import org.poem.core.bean.ShellMethodTarget;
import org.poem.core.enums.ActionEnums;
import org.poem.core.lang.SObject;
import org.poem.core.print.ShellPrint;
import org.poem.tools.utils.collection.CollectionUtils;
import org.poem.tools.utils.collection.Lists;
import org.poem.tools.utils.logger.LoggerUtils;
import org.poem.tools.utils.string.StringUtils;
import org.springframework.boot.ApplicationArguments;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import java.io.PrintWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.Executable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 执行器
 */
@Component
public class CommandShellRunner implements Runner {
    /**
     * 输入
     */
    Scanner sc = new Scanner(System.in);

    private Map<String, List<ShellMethodTarget>> commands;

    private CommandLineParser parser = new DefaultParser();


    public CommandShellRunner() {
        commands = ShellMethodTargetRegistrar.getCommands();
    }

    /**
     * @param applicationArguments
     */
    @Override
    public void run(ApplicationArguments applicationArguments) {
        PrintWriter pw = new PrintWriter(System.out);
        while (true) {
            pw.print("输入:");
            pw.flush();
            String commandLine = sc.nextLine();  //读取字符串型输入
            if (StringUtils.isNotBlank(commandLine)) {
                ShellCommandParse parse = new ShellCommandParse(commands.get(getGroupName(commandLine)));
                try {
                    Object[] args = parse.getParameterValue(commandLine);
                    String beanName = getGroupName(commandLine);
                    String method = getCommand(commandLine);
                    executor(beanName,method,Arrays.asList(args),parse.getParameterTypes());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } else {
                System.err.println("\n");
            }
        }
    }


    /**
     * 获取参数
     *
     * @param commandLine
     * @return
     */
    private String getCommand(String commandLine) {
        if (StringUtils.isNoneBlank(commandLine)) {
            List<String> cpmmands = Arrays.asList(commandLine.split("\\s+"));
            if (cpmmands.size() >= 2) {
                return cpmmands.get(1);
            }
        }
        return "";
    }

    /**
     * 获取当前的分组
     *
     * @param commandLine
     * @return
     */
    private String getGroupName(String commandLine) {
        if (StringUtils.isNoneBlank(commandLine)) {
            List<String> cpmmands = Arrays.asList(commandLine.split("\\s+"));
            if (cpmmands.size() >= 1) {
                return cpmmands.get(0);
            }
        }
        return "";
    }

    /**
     * 执行器
     *
     * @param bean       bean名字
     * @param methodName 方法
     * @param parameters 参数
     */
    private void executor(String bean, String methodName, List<Object> parameters, Class<?> ... parameterTypes) {
        try {
            if (!org.springframework.util.StringUtils.isEmpty(bean)) {
                Object clsObj = ApplicationContext.getBean(bean);
                SObject result;
                Class clazz = clsObj.getClass();
                //得到指定的方法
                Method method = clazz.getMethod(methodName,parameterTypes);
                if (null == method) {
                    LoggerUtils.info("任务:" + methodName + "指定的方法未被Spring找到!");
                    return;
                }
                String name = method.getReturnType().getSimpleName();
                if(!"void".equals(name)){
                    result = (SObject) ReflectionUtils.invokeMethod(method, clsObj, parameters);
                    ShellPrint.printResult(result);
                }
            }
        } catch (NoSuchMethodException | IllegalArgumentException e) {
            e.printStackTrace();
            ShellPrint.printMsg(e.getMessage());
        } finally {
            //打印数据
            ShellPrint.printMsg("\n");
        }
    }
}
