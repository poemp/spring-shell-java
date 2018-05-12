package org.poem.core;


import org.poem.api.Runner;
import org.springframework.boot.ApplicationArguments;
import org.springframework.stereotype.Component;

import java.util.Scanner;

/**
 * 执行器
 */
@Component
public class CommandShellRunner implements Runner {
    /**
     * 输入
     */
    Scanner sc = new Scanner(System.in);
    /**
     *
     * @param args
     */
    @Override
    public void run(ApplicationArguments args) {
        executor();
    }

    private void executor(){
        while (true){
            System.err.print("输入：");
            String commandLine = sc.nextLine();  //读取字符串型输入
            System.err.println(commandLine);
        }
    }
}
