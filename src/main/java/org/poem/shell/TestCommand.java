package org.poem.shell;

import org.poem.core.annotation.ShellComponent;
import org.poem.core.annotation.ShellMethod;
import org.poem.core.annotation.ShellOptions;

import java.io.FileNotFoundException;

@ShellComponent(name = "test")
public class TestCommand {
    @ShellMethod(name = "method2", detail = "测试方法第二")
    public void method2(@ShellOptions(detail = "4542512sdf2das1g54dagf") String name,
                        @ShellOptions(detail = "4542512sdf2das1g54dagf") String dsfds,
                        @ShellOptions(detail = "Ip地址") String ip,
                        @ShellOptions(detail = "端口号") String port,
                        @ShellOptions(detail = "使用的年限") String age) throws FileNotFoundException {
        throw  new FileNotFoundException(name+ "  " + dsfds + " " + ip + " "+ port+ " "+ age);
    }
}
