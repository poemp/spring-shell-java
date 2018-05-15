package org.poem.core;


import org.apache.commons.cli.ParseException;
import org.poem.api.Runner;
import org.poem.config.ApplicationContext;
import org.poem.config.ShellCommandParse;
import org.poem.config.ShellMethodTargetRegistrar;
import org.poem.core.bean.ShellMethodTarget;
import org.poem.core.enums.ActionEnums;
import org.poem.core.exception.ShellCommandException;
import org.poem.core.lang.SObject;
import org.poem.core.print.ShellPrint;
import org.poem.tools.utils.string.StringUtils;
import org.springframework.boot.ApplicationArguments;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

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
            pw.print("\n输入:");
            pw.flush();
            String commandLine = sc.nextLine();  //读取字符串型输入
            if (StringUtils.isNotBlank(commandLine)) {
                try {
                    ShellCommandParse parse = new ShellCommandParse(commands.get(getGroupName(commandLine)));
                    Object[] args = parse.getParameterValue(commandLine);
                    String beanName = getGroupName(commandLine);
                    if(commands.keySet().contains(beanName)){
                        executor(parse.getCurrentMethod(), args);
                    }else{

                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                } catch (ShellCommandException e) {
                    e.printStackTrace();
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
            } else {
                throw new ShellCommandException("for example: testCommand method [-p]");
            }
        }
        return "";
    }

    /**
     * 执行器
     *
     * @param shellMethodTarget
     * @param parameters
     */
    private void executor( ShellMethodTarget shellMethodTarget, Object[] parameters) {
        try {
            Object clsObj = shellMethodTarget.getBean();
            SObject result;
            Method method = shellMethodTarget.getMethod();
            String name = method.getReturnType().getSimpleName();
            if (!"void".equals(name)) {
                result = (SObject) ReflectionUtils.invokeMethod(method, clsObj, parameters);
                ShellPrint.printResult(result);
            } else {
                ReflectionUtils.invokeMethod(method, clsObj, parameters);
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
