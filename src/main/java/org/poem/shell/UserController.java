package org.poem.shell;

import org.poem.core.annotation.ShellComponent;
import org.poem.core.annotation.ShellMethod;
import org.poem.core.annotation.ShellOptions;

@ShellComponent
public class UserController {

    @ShellMethod(name = "update" , detail = "修改用户的名字.")
    public void update(@ShellOptions(detail = "修改的id") String id,
                       @ShellOptions(detail = "修改的名字") String name){

    }

    @ShellMethod(name = "all", detail = " 查询全部.")
    public void selectAll(){

    }
}
