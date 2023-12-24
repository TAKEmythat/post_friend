package com.cwy.post_friend.frame.factory;

import java.util.HashMap;
import java.util.Map;

/**
 * 用来对应 url 与对应组件关系
 *
 * @Classname RequestMap
 * @Description TODO
 * @Author stomach medicine
 * @Version 1.0.0
 * @Create 2023-12-24 13:51
 * @Since 1.0.0
 */

public class RequestMap {
    private static final RequestMap requestMap = new RequestMap();
    private static final Map<String, Object> requestMapping = new HashMap<>();

    public static RequestMap newInstance() {
        return requestMap;
    }

    public Map<String, Object> getRequestMapping(){
        return requestMapping;
    }

    public void insertRequestMapping(String url, Object controller) {
        requestMapping.put(url, controller);
    }

    public Object getController(String url) {
        return requestMapping.get(url);
    }



}
