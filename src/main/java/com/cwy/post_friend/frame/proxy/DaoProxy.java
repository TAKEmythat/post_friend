package com.cwy.post_friend.frame.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Dao 层的动态代理对象
 *
 * @Classname DaoProxy
 * @Description TODO
 * @Author stomach medicine
 * @Version 1.0.0
 * @Create 2023-12-24 14:58
 * @Since 1.0.0
 */
public class DaoProxy implements InvocationHandler {
    private Object object;
    private final Map<String, String> idAndSQLField = new HashMap<>();

    public DaoProxy(Object object) {
        this.object = object;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return null;
    }
}
