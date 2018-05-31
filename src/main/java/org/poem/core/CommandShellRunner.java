package org.poem.core;


import org.apache.commons.cli.ParseException;
import org.poem.api.Runner;
import org.poem.config.ShellCommandParse;
import org.poem.config.ShellMethodTargetRegistrar;
import org.poem.core.bean.ShellMethodTarget;
import org.poem.core.enums.ActionEnums;
import org.poem.core.exception.ShellCommandException;
import org.poem.core.print.ShellPrint;
import org.poem.tools.utils.logger.LoggerUtils;
import org.poem.tools.utils.string.StringUtils;
import org.springframework.boot.ApplicationArguments;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.lang.reflect.UndeclaredThrowableException;
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
        ShellPrint.printFirstMessage();
        while (true) {
            pw.print("\n输入>:");
            pw.flush();
            String commandLine = sc.nextLine();  //读取字符串型输入
            if (StringUtils.isNotBlank(commandLine)) {
                if (ActionEnums.EXIT.getAction().equalsIgnoreCase(commandLine)) {
                    System.exit(0);
                }
                try {
                    this.validate(commandLine);
                    ShellCommandParse parse = new ShellCommandParse(commands.get(getGroupName(commandLine)));
                    Object[] args = parse.getParameterValue(commandLine);
                    if(parse.getCurrentMethod().getBean().getClass().getName().equals("org.poem.core.handler.HelpHandler")){
                        args = new Object[1];
                    }
                    String command = getCommand(commandLine);
                    if(StringUtils.isNotBlank(command)){
                        args[0] = command;
                    }
                    executor(parse.getCurrentMethod(), args);
                } catch (ParseException e) {
                    //参数转换异常
                    LoggerUtils.error(e);
                    ShellPrint.printMsg(e.getMessage());
                } catch (ShellCommandException e) {
                    //输入的命令异常
                    LoggerUtils.error(e);
                    ShellPrint.printMsg(e.getMessage());
                }catch (Exception e){
                    //调用的方法中出现异常
                    if(e instanceof UndeclaredThrowableException){
                        LoggerUtils.error(e);
                        ShellPrint.printMsg("错误信息："+ ((UndeclaredThrowableException) e).getUndeclaredThrowable().getLocalizedMessage());
                    }
                    else{
                        LoggerUtils.error(e);
                        ShellPrint.printMsg(e.getMessage());
                    }
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
     * 获取方法名字
     *
     * @return
     */
    private Boolean getMethodList(String command) {
        for (List<ShellMethodTarget> shellMethodTargets : commands.values()) {
            for (ShellMethodTarget shellMethodTarget : shellMethodTargets) {
                if (shellMethodTarget.getName().equals(command)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 验证参数名
     *
     * @param commandLine 命令
     * @return
     * @throws ShellCommandException
     */
    private void validate(String commandLine) throws ShellCommandException {
        String groupName = getGroupName(commandLine);
        String command = getCommand(commandLine);
        //分组名称不正确
        if (StringUtils.isEmpty(groupName) || !commands.keySet().contains(groupName)) {
            if (!ActionEnums.HELP.equals(groupName.toUpperCase())) {
                throw new ShellCommandException("[" + groupName + "] 不是内部命令.");
            }
        }
        if (StringUtils.isEmpty(command) || !getMethodList(command)) {
            if (!ActionEnums.HELP.equals(groupName.toUpperCase())) {
                throw new ShellCommandException("[" + groupName + " " + command + "]不是内部命令.");
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
    private void executor(ShellMethodTarget shellMethodTarget, Object[] parameters) {
        try {
            Object clsObj = shellMethodTarget.getBean();
            Method method = shellMethodTarget.getMethod();
            String name = method.getReturnType().getSimpleName();
            if (!"void".equals(name)) {
                Object result = ReflectionUtils.invokeMethod(method, clsObj, parameters);
                ShellPrint.printResult(result);
            } else {
                    ReflectionUtils.invokeMethod(method, clsObj, parameters);
            }
        } catch (IllegalArgumentException e) {
            LoggerUtils.error(e);
            ShellPrint.printMsg(e.getMessage());
        } catch (Exception e) {
            LoggerUtils.error(e);
            if (e instanceof UndeclaredThrowableException) {
                ShellPrint.printMsg("错误信息：" + ((UndeclaredThrowableException) e).getUndeclaredThrowable().getLocalizedMessage());
            } else {
                ShellPrint.printMsg(e.getMessage());
            }
        } finally {
            //打印数据
            ShellPrint.printMsg("\n");
        }
    }
}
