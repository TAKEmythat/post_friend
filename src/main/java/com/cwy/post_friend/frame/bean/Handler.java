package com.cwy.post_friend.frame.bean;

import java.lang.reflect.Method;
import java.util.*;

/**
 * c
 * @projectName: post_friend
 * @package: com.cwy.post_friend.frame.bean
 * @className: Handler
 * @author: LGJ
 * @description: TODO
 * @date: 2023/12/24 16:25
 * @version: 1.0
 */
public class Handler {
    private Object target;
    private Method method;

    private Map<String,Integer> paramClassNameMap;

    public Handler(Object target, Method method) {
        this.target = target;
        this.method = method;
        this.paramClassNameMap = new HashMap<>();
    }


    public Object getTarget() {
        return target;
    }

    public void setTarget(Object target) {
        this.target = target;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public Map<String, Integer> getParamClassNameMap() {
        return paramClassNameMap;
    }

    public void setParamClassNameMap(Map<String, Integer> paramClassNameMap) {
        this.paramClassNameMap = paramClassNameMap;
    }
}
