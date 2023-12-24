package com.cwy.post_friend.frame.annotation.ordinary;

import java.lang.annotation.*;

/**
 * @Classname Component
 * @Description TODO
 * @Author stomach medicine
 * @Version 1.0.0
 * @Create 2023-12-24 11:01
 * @Since 1.0.0
 */

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Component {
    String value() default "";
}
