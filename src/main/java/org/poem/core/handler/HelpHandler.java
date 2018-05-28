package org.poem.core.handler;


import org.poem.config.ShellMethodTargetRegistrar;
import org.poem.core.annotation.ShellComponent;
import org.poem.core.annotation.ShellMethod;
import org.poem.core.annotation.ShellOptions;
import org.poem.core.bean.ShellMethodTarget;
import org.poem.core.print.ShellPrint;
import org.poem.tools.utils.collection.CollectionUtils;
import org.poem.tools.utils.collection.Lists;
import org.poem.tools.utils.collection.Maps;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@ShellComponent(name = "help")
@Service(value = "help")
public class HelpHandler  {


    private static Map<String, List<ShellMethodTarget>> commands;

    static {
        commands = ShellMethodTargetRegistrar.getCommands();
    }

    @ShellMethod(name = "all")
    public void all(@ShellOptions(detail = "参数") String[]  args) {
        //帮助
        for (String s : commands.keySet()) {
            ShellPrint.printTargetMethod(s,commands.get(s));
        }
    }


    private void group(String group){
        for (String s : commands.keySet()) {
            if(s.equalsIgnoreCase(group)) {
                ShellPrint.printTargetMethod(s, commands.get(s));
            }
        }
    }

    private void method(String group, String method){
        Map<String, List<ShellMethodTarget>> newMap = Maps.emptys();
        newMap.put(group,Lists.empty());
        List<ShellMethodTarget> shellMethodTargets = newMap.getOrDefault(group,Lists.empty());
        for (ShellMethodTarget shellMethodTarget : shellMethodTargets) {
            if(shellMethodTarget.getName().equalsIgnoreCase(method)){
                shellMethodTargets.add(shellMethodTarget);
            }
        }
        if(CollectionUtils.isNotEmpty(shellMethodTargets)){
            ShellPrint.printTargetMethod(group, shellMethodTargets);
        }
    }
}
