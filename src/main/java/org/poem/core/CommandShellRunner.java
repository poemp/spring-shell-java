package org.poem.core;


import org.apache.commons.cli.*;
import org.poem.api.Runner;
import org.poem.config.ApplicationContext;
import org.poem.config.ShellCommandParse;
import org.poem.config.ShellMethodTargetRegistrar;
import org.poem.core.bean.ShellMethodTarget;
import org.poem.core.enums.ActionEnums;
import org.poem.core.exception.ShellCommandException;
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
                if(ActionEnums.HELP.getAction().equals(commandLine)){
                    //帮助
                    for (String s : commands.keySet()) {
                        ShellCommandParse parse = new ShellCommandParse(commands.get(s));
                        parse.printHelp(s);
                    }
                }else{
                    try {
                        ShellCommandParse parse = new ShellCommandParse(commands.get(getGroupName(commandLine)));
                        Object[] args = parse.getParameterValue(commandLine);
                        String beanName = getGroupName(commandLine);
                        executor(beanName,parse.getCurrentMethod(),args);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    } catch (ShellCommandException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                System.err.println("\n");
            }
        }
    }

    /**
     * 获取当前的分组
     *
     * @param commandLine
     * @return
     */
    private String getGroupName(String commandLine) throws ShellCommandException {
        if (StringUtils.isNoneBlank(commandLine)) {
            List<String> cpmmands = Arrays.asList(commandLine.split("\\s+"));
            if (cpmmands.size() >= 1) {
                return cpmmands.get(0);
            }else{
                throw new ShellCommandException("testCommand method [-p]");
            }
        }
        return "";
    }

    /**
     * 执行器
     * @param bean
     * @param method
     * @param parameters
     */
    private void executor(String bean, Method method,  Object[] parameters) {
        try {
            if (!org.springframework.util.StringUtils.isEmpty(bean)) {
                Object clsObj = ApplicationContext.getBean(bean);
                SObject result;
                String name = method.getReturnType().getSimpleName();
                if(!"void".equals(name)){
                    result = (SObject) ReflectionUtils.invokeMethod(method, clsObj, parameters);
                    ShellPrint.printResult(result);
                }else{
                    ReflectionUtils.invokeMethod(method, clsObj, parameters);
                }
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            ShellPrint.printMsg(e.getMessage());
        } finally {
            //打印数据
            ShellPrint.printMsg("\n");
        }
    }
}
