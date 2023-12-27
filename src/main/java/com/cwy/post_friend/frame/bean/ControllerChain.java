package com.cwy.post_friend.frame.bean;

import com.cwy.post_friend.frame.enum_.RequestMode;

import java.util.Objects;

/**
 * @Classname ControllerChain
 * @Description TODO
 * @Author stomach medicine
 * @Version 1.0.0
 * @Create 2023-12-24 14:03
 * @Since 1.0.0
 */

public class ControllerChain {
    // 控制器对象，即类上有 @Controller 注解的类
    private Object controller;
    // 控制器对象所要匹配的请求方式，例如：GET，POST，PUT....
    private RequestMode requestMode;
    // 控制器对象的唯一标识名字，例如：UserController -> UserController
    private String controllerName;

    public ControllerChain(Object controller, RequestMode requestMode, String controllerName) {
        this.controller = controller;
        this.requestMode = requestMode;
        this.controllerName = controllerName;
    }

    public String getControllerName() {
        return controllerName;
    }

    public void setControllerName(String controllerName) {
        this.controllerName = controllerName;
    }

    public ControllerChain(Object controller, RequestMode requestMode) {
        this.controller = controller;
        this.requestMode = requestMode;
    }

    public Object getController() {
        return controller;
    }

    public void setController(Object controller) {
        this.controller = controller;
    }

    public RequestMode getRequestMode() {
        return requestMode;
    }

    public void setRequestMode(RequestMode requestMode) {
        this.requestMode = requestMode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ControllerChain that = (ControllerChain) o;
        return Objects.equals(controller, that.controller) && requestMode == that.requestMode;
    }

    @Override
    public int hashCode() {
        return Objects.hash(controller, requestMode);
    }

    @Override
    public String toString() {
        return "ControllerChain{" +
                "controller=" + controller +
                ", requestMode=" + requestMode +
                '}';
    }
}
