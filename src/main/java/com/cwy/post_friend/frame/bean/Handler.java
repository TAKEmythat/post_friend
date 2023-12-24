package com.cwy.post_friend.frame.bean;

import com.cwy.post_friend.frame.enum_.RequestMode;

import java.lang.reflect.InvocationTargetException;
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

    private RequestMode requestMode;

    private Map<String,Integer> paramClassNameMap;

    public Handler(Object target, Method method,RequestMode requestMode) {
        this.target = target;
        this.method = method;
        this.requestMode = requestMode;
        this.paramClassNameMap = new HashMap<>();
    }

    public RequestMode getRequestMode() {
        return requestMode;
    }

    public void setRequestMode(RequestMode requestMode) {
        this.requestMode = requestMode;
    }

    public Object invoke(Object[] param) throws InvocationTargetException, IllegalAccessException {
        return method.invoke(target, param);
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
