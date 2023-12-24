package com.cwy.post_friend.frame.annotation.request;

import com.cwy.post_friend.frame.enum_.RequestMode;

import java.lang.annotation.*;

/**
 * @Classname RequestMapping
 * @Description TODO
 * @Author stomach medicine
 * @Version 1.0.0
 * @Create 2023-12-24 13:41
 * @Since 1.0.0
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface RequestMapping {
    String value() default "";
    RequestMode mode() default RequestMode.GET;
}
