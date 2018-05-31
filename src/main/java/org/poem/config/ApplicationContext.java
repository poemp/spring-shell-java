package org.poem.config;


import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;

@Service
public class ApplicationContext implements ApplicationContextAware, ApplicationListener {

    private static org.springframework.context.ApplicationContext applicationContext;

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
        this.applicationContext = applicationContext;
    }


    @Override
    public void onApplicationEvent(ApplicationEvent event) {

    }
}