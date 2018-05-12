package org.poem.core.annotation;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.lang.annotation.*;


@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Component
@Service

public @interface ShellComponent {

    /**
     * shell 的名字
     * @return 名字
     */
    String name() default  "";
}
