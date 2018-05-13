package org.poem.core.print;

import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

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

    public static void printUnrecognizOption(String group , String command, Options options) {
        HelpFormatter formatter = new HelpFormatter();
        printMsg(group + "\n");
        formatter.printHelp( command, options);
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
