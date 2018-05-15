package org.poem.shell;

import org.poem.core.annotation.ShellComponent;
import org.poem.core.annotation.ShellMethod;
import org.poem.core.annotation.ShellOptions;

@ShellComponent(name = "test")
public class TestCommand {
    @ShellMethod(name = "method2", detail = "")
    public void method2(@ShellOptions(detail = "4542512sdf2das1g54dagf") String name,
                        @ShellOptions(detail = "4542512sdf2das1g54dagf") String dsfds,
                        @ShellOptions(detail = "Ip地址") String ip,
                        @ShellOptions(detail = "端口号") String port,
                        @ShellOptions(detail = "使用的年限") String age) {

    }
}
