package org.poem.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author poem
 */
@Configuration
public class CommandShellConfig {

    /**
     * init all
     * @return
     */
    @Bean
    public ShellMethodTargetRegistrar methodTargetRegistrar(){
        return new ShellMethodTargetRegistrar();
    }
}
