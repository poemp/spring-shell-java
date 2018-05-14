package org.poem.shell;

import org.poem.core.annotation.ShellComponent;
import org.poem.core.annotation.ShellMethod;
import org.poem.core.annotation.ShellOptions;

@ShellComponent(name = "test")
public class TestCommand {

    @ShellMethod(name = "method",detail = "答应用户的名字")
    public void method(@ShellOptions(detail = "需要打印的用户名字") String name){

    }

    @ShellMethod(name = "method1",detail = "")
    public void method1(@ShellOptions(detail = "124564512adf15a4sf") String name){

    }

    @ShellMethod(name = "method2",detail = "")
    public void method2(@ShellOptions(detail = "4542512sdf2das1g54dagf") String name){

    }


    @ShellMethod(name = "method3",detail = "")
    public void method3(@ShellOptions(detail = "dfgsdbgsdhewrsgsdf5145321541") String name){

    }


    @ShellMethod(name = "method4")
    public void method4(@ShellOptions(detail = "sdgnjksdbgnjkisdbgfsd") String name){

    }


    @ShellMethod(name = "method5",detail = "drgmkldnagkila")
    public void method5(@ShellOptions(detail = "drgmkldnagkila") String name){

    }
}
