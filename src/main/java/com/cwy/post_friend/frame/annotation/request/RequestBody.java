package com.cwy.post_friend.frame.annotation.request;

import java.lang.annotation.*;

/**
 * 将请求体封装对象
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
@Documented
public @interface RequestBody {
}
