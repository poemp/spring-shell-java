package org.poem.core.annotation;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER})
@Documented
public @interface ShellOptions {

    /**
     * 参数的详细描述
     * @return
     */
    public String detail() ;
}
