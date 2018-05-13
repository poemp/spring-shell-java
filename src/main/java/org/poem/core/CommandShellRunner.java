package org.poem.core;


import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.ParseException;
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
import org.springframework.stereotype.Component;

import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

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
                String[] args = commandLine.split("\\s+");
                String method = args[0];
                List<ShellMethodTarget> shellMethodTargets = Collecommands.values();
                if (ActionEnums.HELP.equals(method)) {
                    shellMethodTargets = Collections.addAll(commands.values());
                }
                ShellCommandParse parse = new ShellCommandParse(commands.getOrDefault(method, Lists.empty()));
                try {
                    CommandLine line = parser.parse(parse.createParse().getSearchOpts(), args);
                    String[] as = line.getArgs();
                } catch (ParseException e) {
                    ShellPrint.printMsg(e.getMessage());
                    ShellPrint.printUnrecognizOption(method, args[1], parse.createParse().getSearchOpts());
                }
            } else {
                System.err.println("\n");
            }
        }
    }


    /**
     * 执行器
     *
     * @param bean       bean名字
     * @param methodName 方法
     * @param parameters 参数
     */
    private void executor(String bean, String methodName, List<Object> parameters) {
        try {
            if (!org.springframework.util.StringUtils.isEmpty(bean)) {
                Object clsObj = ApplicationContext.getBean(bean);
                if (null == clsObj) {
                    LoggerUtils.info("任务:" + bean + "指定的类未被Spring找到!");
                    return;
                }
                SObject result;
                Class clazz = clsObj.getClass();
                //得到指定的方法
                Method method = clazz.getDeclaredMethod(methodName);
                if (null == method) {
                    LoggerUtils.info("任务:" + methodName + "指定的方法未被Spring找到!");
                    return;
                }
                if (CollectionUtils.isNotEmpty(parameters)) {
                    //执行方法
                    result = (SObject) method.invoke(clsObj, parameters);
                } else {
                    //执行方法
                    result = (SObject) method.invoke(clsObj);
                }
                ShellPrint.printResult(result);
            }
        } catch (NoSuchMethodException e) {
            ShellPrint.printMsg(e.getMessage());
        } catch (IllegalAccessException e) {
            ShellPrint.printMsg(e.getMessage());
        } catch (IllegalArgumentException e) {
            ShellPrint.printMsg(e.getMessage());
        } catch (InvocationTargetException e) {
            ShellPrint.printMsg(e.getMessage());
        } finally {
            //打印数据
            ShellPrint.printMsg("/n");
        }
    }
}
