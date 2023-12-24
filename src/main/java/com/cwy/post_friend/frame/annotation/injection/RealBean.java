package com.cwy.post_friend.frame.annotation.injection;

import java.lang.annotation.*;

/**
 * @Classname RealBean
 * @Description TODO
 * @Author stomach medicine
 * @Version 1.0.0
 * @Create 2023-12-24 12:24
 * @Since 1.0.0
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented
public @interface RealBean {
    String value() default "";
}
