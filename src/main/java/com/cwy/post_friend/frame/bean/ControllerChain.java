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
    private Object controller;
    private RequestMode requestMode;

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
