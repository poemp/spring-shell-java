package org.poem.core.print;

import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.poem.core.bean.ShellMethodParameter;
import org.poem.core.bean.ShellMethodTarget;
import org.poem.core.lang.SObject;
import org.poem.tools.utils.collection.Lists;
import org.poem.tools.utils.string.StringUtils;

import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

public class ShellPrint {

    /**
     * 打印帮助信息
     *
     * @param command
     * @param options
     */
    public static void printHelp(String command, Options options) {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(command, options);
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
     * @param group
     * @param command
     * @param options
     */
    public static void printUnrecognizOption(String group, String command, Options options) {
        HelpFormatter formatter = new HelpFormatter();
        printMsg(group + "\n");
        formatter.printHelp(command, options);
    }


    /**
     * 打印结果
     *
     * @param result
     * @param <T>
     */
    public static <T extends SObject> void printResult(T result) {
        PrintWriter pw = new PrintWriter(System.out);
        pw.print(result.sToString() + "\n");
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
    public static void printTragetMethod(String group, List<ShellMethodTarget> shellMethodTargets) {
        StringBuilder stringBuilder = new StringBuilder();
        if(null != shellMethodTargets){
            for (ShellMethodTarget shellMethodTarget : shellMethodTargets) {
                stringBuilder
                        .append(group)
                        .append(" ")
                        .append(shellMethodTarget.getName())
                        .append("\n")
                        .append(getParameter(shellMethodTarget.getMethodParameterMap()))
                        .append("\n");
            }
        }
        printMsg(stringBuilder.toString());
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
    private static String getParameter(Map<String, ShellMethodParameter> stringShellMethodParameterMap) {
        StringBuilder stringBuilder = new StringBuilder();
        for (ShellMethodParameter shellMethodParameter : stringShellMethodParameterMap.values()) {
            stringBuilder
                    .append("     -")
                    .append(shortName(shellMethodParameter.getName()))
                    .append(",--")
                    .append(shellMethodParameter.getName())
                    .append(" ")
                    .append("<")
                    .append(shellMethodParameter.getName().toUpperCase())
                    .append(">")
                    .append("   ")
                    .append(shellMethodParameter.getDetail())
                    .append("\n");
        }
        return stringBuilder.toString();
    }
}
