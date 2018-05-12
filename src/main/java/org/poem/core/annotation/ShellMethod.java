package org.poem.core.annotation;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Documented
public @interface ShellMethod {

    /**
     * 方法的名字
     * 如果没有，则是方法名字
     * @return
     */
    String name() default "";

    /**
     * 描述
     * @return
     */
    String detail() default "";


}
