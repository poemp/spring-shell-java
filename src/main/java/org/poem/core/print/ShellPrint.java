package org.poem.core.print;

import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.poem.core.lang.SObject;

import java.io.PrintWriter;

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
    public static void printHelp(String group,String command, Options options) {
        HelpFormatter formatter = new HelpFormatter();
        printMsg("\n"+ group);
        formatter.printHelp(command, options);
    }

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
}
