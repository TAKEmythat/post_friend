package com.cwy.post_friend.frame.annotation.ordinary;

import jdk.jfr.Relational;

import java.lang.annotation.*;

/**
 * @Classname Service
 * @Description TODO
 * @Author stomach medicine
 * @Version 1.0.0
 * @Create 2023-12-24 11:29
 * @Since 1.0.0
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface Service {
    String value() default "";
}
