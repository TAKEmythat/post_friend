package com.cwy.post_friend.frame.factory;

import java.util.HashMap;
import java.util.Map;

/**
 * @Classname BeanFactory
 * @Description TODO
 * @Author stomach medicine
 * @Version 1.0.0
 * @Create 2023-12-23 22:07
 * @Since 1.0.0
 */

public class BeanFactory {
    private Map<String, Object> ordinaryBeans = new HashMap<>();
    private Map<String, Object> configBeans = new HashMap<>();

    void insertOrdinaryBeans(String name, Object o) {
        ordinaryBeans.put(name, o);
    }

    void insertConfigBeans(String name, Object o) {
        configBeans.put(name, o);
    }
}
