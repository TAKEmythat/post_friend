package com.cwy.post_friend.frame.controller;

import com.cwy.post_friend.exception.frame.AnnotationException;
import com.cwy.post_friend.frame.annotation.ordinary.Component;
import com.cwy.post_friend.frame.annotation.ordinary.Controller;
import com.cwy.post_friend.frame.annotation.ordinary.Dao;
import com.cwy.post_friend.frame.annotation.ordinary.Service;
import com.cwy.post_friend.frame.bean.XMLObject;
import com.cwy.post_friend.frame.factory.BeanFactory;
import com.cwy.post_friend.frame.proxy.DaoProxy;
import com.cwy.post_friend.frame.tool.DynamicallyGenerateImplementationClasses;
import com.cwy.post_friend.frame.tool.ScanDirectoryHasAnnotation;
import com.cwy.post_friend.frame.tool.XMLAnalysis;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
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
            // 扫描并代理项目中有指定注解的类
            scanningProjectAllClassHasAnnotation();
        } catch (URISyntaxException | AnnotationException | ClassNotFoundException | ParserConfigurationException |
                 IOException | SAXException | InstantiationException | IllegalAccessException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        super.init();
    }

    /**
     * 扫描整个项目有指定注解的类
     *
     * @throws URISyntaxException     找不到资源的错误
     * @throws AnnotationException    没有填写注解的错误
     * @throws ClassNotFoundException 找不到类的错误
     */
    public void scanningProjectAllClassHasAnnotation() throws URISyntaxException,
            AnnotationException, ClassNotFoundException, ParserConfigurationException, IOException, SAXException, InstantiationException, IllegalAccessException, InterruptedException {
        List<Class<?>> clazzList = new ArrayList<>();
        URL resource = Thread.currentThread().getContextClassLoader().getResource("");
        assert resource != null;
        URI uri = resource.toURI();
        File dir = new File(uri);
        List<Class<?>> classList = ScanDirectoryHasAnnotation.scanBeans(dir, Component.class,
                Controller.class, Dao.class, Service.class);
        // 循环所有类，检查是否为接口
        if (classList.size() > 0) {
            for (Class<?> clazz : classList) {
                // 如果是 Dao
                if (clazz.isInterface() && clazz.getDeclaredAnnotation(Dao.class) != null) {
                    String clazzName = clazz.getName();
                    XMLObject xmlObject = XMLAnalysis.getXmlObject(clazzName.replace(".", "\\") + ".xml");
                    Class<?> clazz0 = DynamicallyGenerateImplementationClasses.generateImplementationClass(clazzName);
                    DaoProxy daoProxy = new DaoProxy(clazz0, xmlObject);
                    Object o = Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
                            clazz0.getInterfaces(), daoProxy);
                    beanFactory.insertOrdinaryBeans(clazzName.substring(clazzName.lastIndexOf(".") + 1),
                            clazz.cast(o));
                }
            }
        }
    }
}
