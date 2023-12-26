package com.cwy.post_friend.frame.proxy;

import com.cwy.post_friend.frame.bean.XMLObject;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

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
    private Object target;
    private XMLObject xmlObject;

    public DaoProxy(Object target, XMLObject xmlObject) {
        this.target = target;
        this.xmlObject = xmlObject;
    }

    public DaoProxy() {
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        String name = method.getName();
        System.out.println("name = " + name);
        return null;
    }

    public Object getTarget() {
        return target;
    }

    public void setTarget(Object target) {
        this.target = target;
    }

    public XMLObject getXmlObject() {
        return xmlObject;
    }

    public void setXmlObject(XMLObject xmlObject) {
        this.xmlObject = xmlObject;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DaoProxy daoProxy = (DaoProxy) o;
        return Objects.equals(target, daoProxy.target) && Objects.equals(xmlObject, daoProxy.xmlObject);
    }

    @Override
    public int hashCode() {
        return Objects.hash(target, xmlObject);
    }

    @Override
    public String toString() {
        return "DaoProxy{" +
                "target=" + target +
                ", xmlObject=" + xmlObject +
                '}';
    }
}
