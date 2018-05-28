package org.poem.core.print;

import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.poem.core.bean.ShellMethodParameter;
import org.poem.core.bean.ShellMethodTarget;
import org.poem.core.enums.ActionEnums;
import org.poem.core.lang.SObject;
import org.poem.tools.utils.string.StringUtils;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class ShellPrint {

    private static final String spilt = " ";
    private static final String action = "用法: ";
    private static final String methodDetail = "说明：";
    private static final String detail = "选项：";
    /**
     * 打印帮助信息
     */
    public static void printFirstMessage() {
        printMsg("启动完成.");
        printMsg("--------------------------------------------------");
        printMsg("信息：");
        printMsg("      1、使用 help 查看帮助信息以及相应的命令的用法");
        printMsg("      2、使用 exit 可以安全退出当前应用");
        printMsg("--------------------------------------------------");
    }

    /**
     * 打印帮助信息
     *
     * @param command
     * @param options
     */
    public static void printHelp(String group, String command, Options options) {
        HelpFormatter formatter = new HelpFormatter();
        printMsg("\n" + group);
        formatter.printHelp(command, options);
    }


    /**
     * 打印结果
     *
     * @param result
     */
    public static void printResult(Object result) {
        PrintWriter pw = new PrintWriter(System.out);
        //如果是数组
        if (result instanceof ArrayList) {
            List<Object> list = (List<Object>) result;
            for (Object o : list) {
                if (o instanceof SObject) {
                    pw.print(((SObject) result).sToString());
                } else {
                    pw.print(result);
                }
            }
        } else {
            if (result instanceof SObject) {
                pw.print(((SObject) result).sToString());
            } else {
                pw.print(result);
            }
        }
        pw.flush();
    }

    /**
     * 打印
     *
     * @param message
     */
    public static void printMsg(String message) {
        PrintWriter pw = new PrintWriter(System.out);
        pw.print(message + "\n");
        pw.flush();
    }


    /**
     * 打印帮助信息
     *
     * @param group              组
     * @param shellMethodTargets 方法
     */
    public static void printTargetMethod(String group, List<ShellMethodTarget> shellMethodTargets) {
        StringBuilder stringBuilder = new StringBuilder();
        if (null != shellMethodTargets) {
            for (ShellMethodTarget shellMethodTarget : shellMethodTargets) {
                if(ActionEnums.HELP.getAction().equalsIgnoreCase(group)){
                    continue;
                }
                stringBuilder.append(action).append(group).append(spilt).append(shellMethodTarget.getName()).append(spilt).append(getPara(shellMethodTarget.getMethodParameterMap()));//参数;
                if(StringUtils.isNotBlank(shellMethodTarget.getDetail())){
                    stringBuilder.append("\n");
                    stringBuilder.append(methodDetail).append(shellMethodTarget.getDetail());
                }
                String par = getParameter(shellMethodTarget.getMethodParameterMap(), action);
                if(StringUtils.isNotBlank(par)){
                    stringBuilder.append("\n");
                    stringBuilder
                            .append(detail)
                            .append("\n")
                            .append(par);//参数详细介绍
                }
                stringBuilder.append("\n");
            }
        }
        printMsg(stringBuilder.toString());
    }

    /**
     * 格式花显示
     *
     * @param length
     * @return
     */
    private static String getEmpty(int length) {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < length; i++) {
            stringBuffer.append(spilt);
        }
        return stringBuffer.toString();
    }

    /**
     * 获取参数
     *
     * @param stringShellMethodParameterMap
     * @return
     */
    public static String getPara(List<ShellMethodParameter> stringShellMethodParameterMap) {
        StringBuilder stringBuilder = new StringBuilder();
        for (ShellMethodParameter shellMethodParameter : stringShellMethodParameterMap) {
            stringBuilder.append("[")
                    .append("-")
                    .append(shortName(shellMethodParameter.getName()))
                    .append(",")
                    .append("--")
                    .append(shellMethodParameter.getName())
                    .append("]");
        }
        return stringBuilder.toString();
    }

    /**
     * 段参数
     *
     * @param name
     * @return
     */
    private static String shortName(String name) {
        return StringUtils.isNotBlank(name) ? name.substring(0, 1) : "";
    }

    /**
     * 参数获取
     *
     * @param stringShellMethodParameterMap
     * @return
     */
    private static String getParameter(List<ShellMethodParameter> stringShellMethodParameterMap, String action) {
        StringBuilder stringBuilder = new StringBuilder();
        for (ShellMethodParameter shellMethodParameter : stringShellMethodParameterMap) {

            stringBuilder
                    .append(getEmpty(action.length()))
                    .append(String.format("%-3s,%-10s %-10s %-100s",
                            "-" + shortName(shellMethodParameter.getName()),
                            "--" + shellMethodParameter.getName(),
                            "<" + shellMethodParameter.getName().toUpperCase() + ">",
                            shellMethodParameter.getDetail()))
                    .append("\n");
        }
        return stringBuilder.toString();
    }
}
