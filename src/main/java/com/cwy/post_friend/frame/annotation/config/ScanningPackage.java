package com.cwy.post_friend.frame.annotation.config;

import java.lang.annotation.*;

/**
 * @Classname ScanningPackage
 * @Description TODO
 * @Author stomach medicine
 * @Version 1.0.0
 * @Create 2023-12-23 22:16
 * @Since 1.0.0
 */

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ScanningPackage {
    String[] value() default "";
}
