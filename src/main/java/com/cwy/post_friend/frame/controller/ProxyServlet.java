package com.cwy.post_friend.frame.controller;

import com.cwy.post_friend.frame.bean.JavaFileObject;
import com.cwy.post_friend.frame.enum_.InsertMethod;
import com.cwy.post_friend.frame.exception.AnnotationException;
import com.cwy.post_friend.frame.annotation.aop.*;
import com.cwy.post_friend.frame.annotation.ordinary.Component;
import com.cwy.post_friend.frame.annotation.ordinary.Controller;
import com.cwy.post_friend.frame.annotation.ordinary.Dao;
import com.cwy.post_friend.frame.annotation.ordinary.Service;
import com.cwy.post_friend.frame.bean.XMLObject;
import com.cwy.post_friend.frame.factory.BeanFactory;
import com.cwy.post_friend.frame.tool.DynamicallyGenerateImplementationClasses;
import com.cwy.post_friend.frame.tool.ScanDirectoryHasAnnotation;
import com.cwy.post_friend.frame.tool.XMLAnalysis;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

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
                 IOException | SAXException | InstantiationException | IllegalAccessException |
                 InterruptedException e) {
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
        URL resource = Thread.currentThread().getContextClassLoader().getResource("");
        assert resource != null;
        URI uri = resource.toURI();
        File dir = new File(uri);
        List<Class<?>> classList = ScanDirectoryHasAnnotation.scanBeans(dir, Component.class,
                Controller.class, Dao.class, Service.class);
        // 循环所有类
        if (classList.size() > 0) {
            for (Class<?> clazz : classList) {
                Method[] clazzDeclaredMethods = clazz.getDeclaredMethods();
                JavaFileObject javaFileObject = new JavaFileObject(clazz.getName(), "");
                // 如果类上有日志相关的注解的话，生成代理的 Service
                if (clazz.getDeclaredAnnotation(Journal.class) != null) {
                    Class<?> clazz0 = DynamicallyGenerateImplementationClasses.generateInheritanceClass(javaFileObject,
                            true, InsertMethod.JOURNAL);
                    beanFactory.insertOrdinaryBeans(clazz.getSimpleName(), clazz0.newInstance());
                }
                // 如果函数上有 AOP 相关的注解的话，生成代理的 Service
                for (Method method : clazzDeclaredMethods) {
                    Transaction transaction = method.getDeclaredAnnotation(Transaction.class);
                    AOPStart aopStart = method.getDeclaredAnnotation(AOPStart.class);
                    AOPEnd aopEnd = method.getDeclaredAnnotation(AOPEnd.class);
                    AOPCatch aopCatch = method.getDeclaredAnnotation(AOPCatch.class);
                    // 添加事务开启、关闭、回滚代码
                    if (transaction != null) {
                        DynamicallyGenerateImplementationClasses.
                                generateInheritanceClass(javaFileObject, false, InsertMethod.TRANSACTION, method);
                    }
                    // 添加前置代码
                    if (aopStart != null) {
                    }
                    // 添加后置代码
                    if (aopEnd != null) {
                    }
                    // 添加错误处理代码
                    if (aopCatch != null) {
                    }
                }
                // 检查是否为接口，并且还需要是 Dao
                if (clazz.isInterface() && clazz.getDeclaredAnnotation(Dao.class) != null) {
                    String clazzName = clazz.getName();
                    XMLObject xmlObject = XMLAnalysis.getXmlObject(clazzName.replace(".", "\\") + ".xml");
                    Class<?> clazz0 = DynamicallyGenerateImplementationClasses.
                            generateImplementationClass(clazzName, "DAO", xmlObject);
                    beanFactory.insertOrdinaryBeans(clazzName.substring(clazzName.lastIndexOf(".") + 1),
                            clazz0.newInstance());
                }
            }
        }
    }
}
