package com.cwy.post_friend.frame.annotation.ordinary;

import java.lang.annotation.*;

/**
 * @Classname Dao
 * @Description TODO
 * @Author stomach medicine
 * @Version 1.0.0
 * @Create 2023-12-24 11:30
 * @Since 1.0.0
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface Dao {
    String value() default "";
}
