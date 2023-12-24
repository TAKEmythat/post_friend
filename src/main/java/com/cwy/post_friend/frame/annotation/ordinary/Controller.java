package com.cwy.post_friend.frame.annotation.ordinary;

import java.lang.annotation.*;

/**
 * @Classname Controller
 * @Description TODO
 * @Author stomach medicine
 * @Version 1.0.0
 * @Create 2023-12-24 12:12
 * @Since 1.0.0
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface Controller {
    String value() default "";
}
