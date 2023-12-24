package com.cwy.post_friend.frame.controller;

import com.cwy.post_friend.exception.frame.AnnotationException;
import com.cwy.post_friend.frame.annotation.ordinary.Component;
import com.cwy.post_friend.frame.annotation.ordinary.Controller;
import com.cwy.post_friend.frame.annotation.ordinary.Dao;
import com.cwy.post_friend.frame.annotation.ordinary.Service;
import com.cwy.post_friend.frame.factory.BeanFactory;
import com.cwy.post_friend.frame.proxy.DaoProxy;
import com.cwy.post_friend.frame.tool.ScanDirectoryHasAnnotation;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;

import java.io.File;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

/**
 * 负责创建动态代理对象
 * 1. 扫描该项目目录下所有有指定注解的类
 * 2. 如果这些类是接口则直接创建代理类，并注入到 BeanFactory
 *
 * @Classname ProxyServlet
 * @Description TODO
 * @Author stomach medicine
 * @Version 1.0.0
 * @Create 2023-12-24 14:38
 * @Since 1.0.0
 */

public class ProxyServlet extends HttpServlet {
    private final BeanFactory beanFactory = BeanFactory.newInstance();

    @Override
    public void init() throws ServletException {
        try {
            // 扫描项目中有指定注解的类
            List<Class<?>> classList = scanningProjectAllClassHasAnnotation();
        } catch (URISyntaxException | AnnotationException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        super.init();
    }

    /**
     * 扫描整个项目有指定注解的类
     *
     * @return 返回指定注解的类
     * @throws URISyntaxException     找不到资源的错误
     * @throws AnnotationException    没有填写注解的错误
     * @throws ClassNotFoundException 找不到类的错误
     */
    public List<Class<?>> scanningProjectAllClassHasAnnotation() throws URISyntaxException, AnnotationException, ClassNotFoundException {
        URL resource = Thread.currentThread().getContextClassLoader().getResource("");
        assert resource != null;
        URI uri = resource.toURI();
        File dir = new File(uri);
        List<Class<?>> classList = ScanDirectoryHasAnnotation.scanBeans(dir, Component.class,
                Controller.class, Dao.class, Service.class);
        // 循环所有类，检查是否为接口
        if (classList.size() > 0) {
            for (Class<?> clazz : classList) {
                // 如果是接口，则生成代理类
                if (clazz.isInterface()) {
                    // 如果是 Dao
                    if (clazz.getDeclaredAnnotation(Dao.class) != null) {
                        String name = clazz.getName();
                        System.out.println("name = " + name);
                        Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
                                clazz.getInterfaces(), new DaoProxy(clazz));
                    }
                }
            }
        }
        return null;
    }
}
