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
    private final Map<String, Object> ordinaryBeans = new HashMap<>();
    private final Map<String, Object> configBeans = new HashMap<>();

    public void insertOrdinaryBeans(String name, Object o) {
        ordinaryBeans.put(name, o);
    }

    public void insertConfigBeans(String name, Object o) {
        configBeans.put(name, o);
    }

    public Map<String, Object> getOrdinaryBeans() {
        return ordinaryBeans;
    }

    public Map<String, Object> getConfigBeans() {
        return configBeans;
    }

    public Object getBean(String name) {
        return ordinaryBeans.get(name);
    }

    public Object getConfigBean(String name) {
        return configBeans.get(name);
    }
}
