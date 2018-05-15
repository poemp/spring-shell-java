package org.poem.core.print;

import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.poem.core.bean.ShellMethodParameter;
import org.poem.core.bean.ShellMethodTarget;
import org.poem.core.lang.SObject;
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
    public static void printTargetMethod(String group, List<ShellMethodTarget> shellMethodTargets) {
        StringBuilder stringBuilder = new StringBuilder();
        String action = "使用方法: ";
        if(null != shellMethodTargets){
            for (ShellMethodTarget shellMethodTarget : shellMethodTargets) {
                stringBuilder.append(action)
                        .append(group)
                        .append(" ")
                        .append(shellMethodTarget.getName())
                        .append(" ")
                        .append(getPara(shellMethodTarget.getMethodParameterMap()))
                        .append("\n")
                        .append(getParameter(shellMethodTarget.getMethodParameterMap(),action))
                        .append("\n");
            }
        }
        printMsg(stringBuilder.toString());
    }

    private static String getEmpty(int length){
        StringBuffer stringBuffer = new StringBuffer();
        for(int i = 0 ; i < length ;i++){
            stringBuffer.append(" ");
        }
        return stringBuffer.toString();
    }
    /**
     * 获取参数
     * @param stringShellMethodParameterMap
     * @return
     */
    public static String getPara(Map<String, ShellMethodParameter> stringShellMethodParameterMap){
        StringBuilder stringBuilder = new StringBuilder();
        for (ShellMethodParameter shellMethodParameter : stringShellMethodParameterMap.values()) {
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
    private static String getParameter(Map<String, ShellMethodParameter> stringShellMethodParameterMap,String action ) {
        StringBuilder stringBuilder = new StringBuilder();
        for (ShellMethodParameter shellMethodParameter : stringShellMethodParameterMap.values()) {
            stringBuilder
                    .append(getEmpty(action.length()))
                    .append(" -")
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
