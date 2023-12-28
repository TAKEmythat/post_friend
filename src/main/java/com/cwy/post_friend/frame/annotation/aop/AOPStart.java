package com.cwy.post_friend.frame.annotation.aop;

import java.lang.annotation.*;
import java.lang.reflect.Method;

/**
 * @Classname AOPStart
 * @Description TODO
 * @Author stomach medicine
 * @Version 1.0.0
 * @Create 2023-12-27 14:21
 * @Since 1.0.0
 */

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AOPStart {
    String value() default "";
}
