package org.poem.config;


import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;

/**
 * @author poem
 */
@Service
public class ApplicationContext implements ApplicationContextAware, ApplicationListener {

    private static org.springframework.context.ApplicationContext applicationContext = null;

    /**
     * 获取bean
     *
     * @param name
     * @return
     */
    public static Object getBean(String name) {
        return applicationContext.getBean(name);
    }

    @Override
    public void setApplicationContext(org.springframework.context.ApplicationContext applicationContext) throws BeansException {
        ApplicationContext.applicationContext = applicationContext;
    }


    @Override
    public void onApplicationEvent(ApplicationEvent event) {

    }
}