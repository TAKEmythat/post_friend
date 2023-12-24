package com.cwy.post_friend.frame.annotation.config;

import java.lang.annotation.*;

/**
 * @Classname Allocation
 * @Description TODO
 * @Author stomach medicine
 * @Version 1.0.0
 * @Create 2023-12-23 22:13
 * @Since 1.0.0
 */

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Allocation {
    String value() default "";
}
