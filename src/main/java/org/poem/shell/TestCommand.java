package org.poem.shell;

import org.poem.core.annotation.ShellComponent;
import org.poem.core.annotation.ShellMethod;
import org.poem.core.annotation.ShellOptions;

@ShellComponent(name = "test")
public class TestCommand {

    @ShellMethod(name = "method",detail = "答应用户的名字")
    public void method(@ShellOptions(detail = "需要打印的用户名字") String name){

    }
}
