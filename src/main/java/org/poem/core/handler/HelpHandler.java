package org.poem.core.handler;


import org.poem.api.Hanlder;
import org.poem.config.ShellCommandParse;
import org.poem.config.ShellMethodTargetRegistrar;
import org.poem.core.annotation.ShellComponent;
import org.poem.core.annotation.ShellMethod;
import org.poem.core.bean.ShellMethodTarget;
import org.poem.core.print.ShellPrint;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@ShellComponent(name = "help")
@Service(value = "help")
public class HelpHandler extends Hanlder {


    private static Map<String, List<ShellMethodTarget>> commands;

    static {
        commands = ShellMethodTargetRegistrar.getCommands();
    }

    @Override
    @ShellMethod
    public void defaultMethod() {
        //帮助
        for (String s : commands.keySet()) {
            ShellPrint.printTargetMethod(s,commands.get(s));
        }
    }
}
