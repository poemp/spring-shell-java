package org.poem.api;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;

public interface Runner extends ApplicationRunner {

    /**
     * 执行器
     */
    public void run(ApplicationArguments args);
}
