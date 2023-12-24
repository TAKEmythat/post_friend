package com.cwy.post_friend.frame.annotation.request;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER})
@Documented
public @interface RequestParam {
    String value();
}
