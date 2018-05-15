package org.poem.config;

import org.apache.commons.cli.*;
import org.poem.core.bean.ShellMethodParameter;
import org.poem.core.bean.ShellMethodTarget;
import org.poem.core.enums.ActionEnums;
import org.poem.core.print.ShellPrint;
import org.poem.tools.utils.collection.CollectionUtils;
import org.poem.tools.utils.collection.Lists;
import org.poem.tools.utils.string.StringUtils;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class ShellCommandParse {

    private Collection<ShellMethodTarget> commands;

    private final Options searchOpts = new Options();

    private CommandLineParser parser = new DefaultParser();

    private List<String> longArgName = Lists.empty();

    private List<String> shortArgName = Lists.empty();

    private List<Class> parameterTypes = Lists.empty();

    private int parameterCount = 0;

    private String groupName;

    private ShellMethodTarget shellMethodTarget;

    public ShellCommandParse(Collection<ShellMethodTarget> commands) {
        this.commands = CollectionUtils.isNotEmpty(commands) ? commands : Lists.empty();
    }

    /**
     * 段参数
     *
     * @param name
     * @return
     */
    private String shortName(String name) {
        return StringUtils.isNotBlank(name) ? name.substring(0, 1) : "";
    }


    /**
     *
     * @param command
     * @return
     */
    @SuppressWarnings({"deprecation", "static-access"})
    private ShellCommandParse createParse(String command) {
        if (!this.commands.isEmpty()) {
            //当前的类下的所有的方法
            for (ShellMethodTarget shellMethodTarget : this.commands) {
                Option option;
               Options s =  new Options();
                if(this.shellMethodTarget == null && ActionEnums.DEFAULTMETHOD.getAction().equals(shellMethodTarget.getName())){
                    this.shellMethodTarget = shellMethodTarget;
                }
                for (ShellMethodParameter shellMethodParameter : shellMethodTarget.getMethodParameterMap().values()) {
                    String shortName = this.shortName(shellMethodParameter.getName());
                    if (command.equals(shellMethodTarget.getName())) {
                        parameterCount += 1;
                        longArgName.add(shellMethodParameter.getName());
                        shortArgName.add(shortName);
                         this.shellMethodTarget = shellMethodTarget;
                        parameterTypes.add(shellMethodParameter.getClazz());
                    }
                    String detail = shellMethodParameter.getDetail();
                    option = OptionBuilder
                            .withLongOpt(shellMethodParameter.getName())
                            .withDescription(detail)
                            .withArgName(shellMethodParameter.getName().toUpperCase())
                            .hasArg().create(shortName);
                    if("".equals(command)){
                        s.addOption(option);
                    }else{
                        searchOpts.addOption(option);
                    }
                }
                if("".equals(command)){
                    ShellPrint.printHelp(groupName,shellMethodTarget.getName(),s);
                }
            }
        }
        return this;
    }

    /**
     * 获取当前的方法
     * @return
     */
    public ShellMethodTarget getCurrentMethod(){
        return  this.shellMethodTarget;
    }
    /**
     * 获取值
     *
     * @param commandLine
     * @return
     * @throws ParseException
     */
    public Object[] getParameterValue(String commandLine) throws ParseException {
        //必须先执行，再执行下面的数据
        List<String> wordsForArgs = wordsForArguments(getGroupName(commandLine), wordsForArguments(getCommand(commandLine), sanitizeInput(commandLine)));
        Options options = this.createParse(getCommand(commandLine)).getSearchOpts();
        CommandLine line = parser.parse(options, wordsForArgs.toArray(new String[wordsForArgs.size()]));
        Object[] objects = new Object[this.parameterCount];
        for (int i = 0, length = longArgName.size(); i < length; i++) {
            Object o = objects[i];
            if (Objects.isNull(o)) {
                objects[i] = line.getParsedOptionValue(longArgName.get(i));
            }
        }

        for (int i = 0, length = shortArgName.size(); i < length; i++) {
            Object o = objects[i];
            if (Objects.isNull(o)) {
                objects[i] = line.getParsedOptionValue(shortArgName.get(i));
            }
        }
        return objects;
    }

    private Options getSearchOpts() {
        return searchOpts;
    }

    /**
     * 取得参数
     *
     * @param command
     * @param words
     * @return
     */
    private List<String> wordsForArguments(String command, List<String> words) {
        int wordsUsedForCommandKey = command.split("\\s+").length;
        if(wordsUsedForCommandKey > words.size()){
            return Lists.empty();
        }
        List<String> args = words.subList(wordsUsedForCommandKey, words.size());
        int last = args.size() - 1;
        if (last >= 0 && "".equals(args.get(last))) {
            args.remove(last);
        }
        return args;
    }

    /**
     * 把一条命令解析成方法
     *
     * @param commandLine
     * @return
     */
    private List<String> sanitizeInput(String commandLine) {
        List<String> words = Arrays.asList(commandLine.split("\\s+"));
        List<String> wordLsit = Lists.empty();
        if (CollectionUtils.isNotEmpty(words)) {
            for (String word : words) {
                wordLsit.add(word.replaceAll("^\\n+|\\n+$", "").replaceAll("\\n+", " "));
            }
        }
        return wordLsit;
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
}
