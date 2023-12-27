package com.cwy.post_friend.frame.annotation.aop;

import java.lang.annotation.*;

/**
 * @Classname Transaction
 * @Description TODO
 * @Author stomach medicine
 * @Version 1.0.0
 * @Create 2023-12-27 14:18
 * @Since 1.0.0
 */

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Transaction {
    String value() default "";
}
