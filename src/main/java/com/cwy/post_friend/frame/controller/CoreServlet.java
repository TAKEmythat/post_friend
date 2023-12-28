package com.cwy.post_friend.frame.controller;

import com.cwy.post_friend.frame.exception.AnnotationException;
import com.cwy.post_friend.frame.annotation.aop.*;
import com.cwy.post_friend.frame.annotation.config.Allocation;
import com.cwy.post_friend.frame.annotation.config.ScanningPackage;
import com.cwy.post_friend.frame.annotation.injection.RealBean;
import com.cwy.post_friend.frame.annotation.ordinary.Component;
import com.cwy.post_friend.frame.annotation.ordinary.Controller;
import com.cwy.post_friend.frame.annotation.ordinary.Dao;
import com.cwy.post_friend.frame.annotation.ordinary.Service;
import com.cwy.post_friend.frame.annotation.request.RequestMapping;
import com.cwy.post_friend.frame.bean.ControllerChain;
import com.cwy.post_friend.frame.enum_.RequestMode;
import com.cwy.post_friend.frame.factory.BeanFactory;
import com.cwy.post_friend.frame.factory.RequestMap;
import com.cwy.post_friend.frame.tool.ScanDirectoryHasAnnotation;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;

import java.io.File;
import java.lang.reflect.Field;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

/**
 * 整体流程：
 * 1. 扫描配置类，注入到 BeanFactory
 * 2. 扫描配置类的包，扫描包下的普通组件，注入到 BeanFactory
 * 3. 通过 BeanFactory 然后注入
 *
 * @Classname CoreServlet
 * @Description TODO
 * @Author stomach medicine
 * @Version 1.0.0
 * @Create 2023-12-23 22:11
 * @Since 1.0.0
 */

public class CoreServlet extends HttpServlet {
    private final BeanFactory beanFactory = BeanFactory.newInstance();
    private final RequestMap requestMap = new RequestMap();
    private final Logger logger = Logger.getLogger("com.cwy.post_friend.frame.controller.CoreServlet");

    @Override
    public void init() throws ServletException {
        super.init();
        try {
            // 扫描配置类，并且扫包，进行注入到 BeanFactory
            scanConfigurationClass();
            // 依赖注入，通过 BeanFactory
            injection();
            // 输入注入后的对象
            Map<String, Object> ordinaryBeans = beanFactory.getOrdinaryBeans();
            logger.info("注入的对象：" + ordinaryBeans.toString());
        } catch (AnnotationException | URISyntaxException | ClassNotFoundException | InstantiationException |
                 IllegalAccessException | NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 扫描配置类
     */
    public void scanConfigurationClass() throws AnnotationException, URISyntaxException,
            ClassNotFoundException, InstantiationException, IllegalAccessException {
        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        URL resource = contextClassLoader.getResource("");
        assert resource != null;
        URI uri = resource.toURI();
        File dir = new File(uri);
        List<Class<?>> classList = ScanDirectoryHasAnnotation.scanBeans(dir, Allocation.class);
        for (Class<?> clazz : classList) {
            beanFactory.insertConfigBeans(clazz.getSimpleName(), clazz.newInstance());
            scanOrdinaryBeanWhitConfiguration();
            Map<String, Object> ordinaryBeans = beanFactory.getOrdinaryBeans();
        }
    }

    /**
     * 扫描配置类上的配置的包，扫描配置的包
     * 扫描包下的所有普通 Bean，然后存储到 BeanFactory 中
     */
    public void scanOrdinaryBeanWhitConfiguration() throws URISyntaxException, AnnotationException,
            ClassNotFoundException, InstantiationException, IllegalAccessException {
        Map<String, Object> configBeans = beanFactory.getConfigBeans();
        Set<Map.Entry<String, Object>> configEntry = configBeans.entrySet();
        // 遍历该配置 Bean
        for (Map.Entry<String, Object> entry : configEntry) {
            Object configBean = entry.getValue();
            Class<?> clazz = configBean.getClass();
            ScanningPackage scanningPackage =
                    clazz.getDeclaredAnnotation(ScanningPackage.class);
            // 扫描配置类的注解配置
            if (scanningPackage != null) {
                String[] packages = scanningPackage.value();
                // 扫描包配置注解上的 value
                for (String s : packages) {
                    s = s.replace(".", "\\");
                    URL resource = Thread.currentThread().getContextClassLoader().getResource(s);
                    assert resource != null;
                    URI uri = resource.toURI();
                    File packageDir = new File(uri);
                    List<Class<?>> classList = ScanDirectoryHasAnnotation.scanBeans(packageDir, Component.class, Dao.class,
                            Service.class, Controller.class);
                    // 循环添加普通 Bean 到 BeanFactory 中
                    for (Class<?> clazz0 : classList) {
                        if ((!clazz0.isInterface()) && clazz0.getDeclaredAnnotation(AOPEnd.class) == null
                                && clazz0.getDeclaredAnnotation(AOPStart.class) == null
                                && clazz0.getDeclaredAnnotation(AOPCatch.class) == null
                                && clazz0.getDeclaredAnnotation(Journal.class) == null
                                && clazz0.getDeclaredAnnotation(Transaction.class) == null) {
                            beanFactory.insertOrdinaryBeans(clazz0.getSimpleName(), clazz0.newInstance());
                        }
                    }
                    List<Class<?>> controllerList = ScanDirectoryHasAnnotation.scanBeans(packageDir, RequestMapping.class);
                    // 循环添加 Controller 组件到 RequestMapping 中
                    for (Class<?> clazz0 : controllerList) {
                        Object controller = clazz0.newInstance();
                        Field[] clazz0DeclaredFields = clazz0.getDeclaredFields();
                        // 再将 Controller 依赖注入到 ControllerChain 中
                        RequestMapping requestMappingAnnotation = clazz0.getDeclaredAnnotation(RequestMapping.class);
                        String value = requestMappingAnnotation.value();
                        RequestMode mode = requestMappingAnnotation.mode();
                        ControllerChain controllerChain = new ControllerChain(controller, mode, clazz0.getSimpleName());
                        requestMap.insertRequestMapping(value, controllerChain);
                    }
                }
            }
        }
    }

    /**
     * 依赖注入 Bean
     */
    public void injection() throws IllegalAccessException, InstantiationException, NoSuchFieldException {
        Map<String, Object> ordinaryBeans = beanFactory.getOrdinaryBeans();
        Set<Map.Entry<String, Object>> beanFactoryEntry = ordinaryBeans.entrySet();
        Map<String, Object> requestMapping = requestMap.getRequestMapping();
        Set<Map.Entry<String, Object>> requestMapEntry = requestMapping.entrySet();
        // 循环遍历普通 Bean
        for (Map.Entry<String, Object> entry : beanFactoryEntry) {
            Object value = entry.getValue();
            Class<?> clazz = value.getClass();
            Field[] clazzDeclaredFields = clazz.getDeclaredFields();
            Class<?> superclass = clazz.getSuperclass();
            // 如果有父类，证明很大概率是代理类，需要获取这个类的字段，
            // 然后通过字段给子类对象赋值，达到给子类对象赋值的目的
            if (superclass != null) {
                Field[] declaredFields = superclass.getDeclaredFields();
                for (Field field : declaredFields) {
                    RealBean realBeanAnnotation = field.getDeclaredAnnotation(RealBean.class);
                    if (realBeanAnnotation != null) {
                        field.setAccessible(true);
                        field.set(value, beanFactory.getBean(realBeanAnnotation.value()));
                    }
                }
            }
            // 循环遍历字段，并为对象的属性赋值，实现依赖注入
            for (Field field : clazzDeclaredFields) {
                RealBean realBeanAnnotation = field.getDeclaredAnnotation(RealBean.class);
                if (realBeanAnnotation != null) {
                    // 设置可写权限
                    field.setAccessible(true);
                    String realBeanName = realBeanAnnotation.value();
                    Object bean = beanFactory.getBean(realBeanName);
                    field.set(value, bean);
                }
            }
        }
        // 循环遍历 Request 的 Controller 进行注入
        for (Map.Entry<String, Object> entry : requestMapEntry) {
            String controllerName = "";
            String url = entry.getKey();
            Object controllerChain = entry.getValue();
            Class<?> controllerChainClass = controllerChain.getClass();
            Field controllerChainClassController = controllerChainClass.getDeclaredField("controller");
            Field[] controllerChainClassDeclaredFields = controllerChainClass.getDeclaredFields();
            // 循环遍历 ControllerChain 的字段，为对象的属性赋值
            for (Field field : controllerChainClassDeclaredFields) {
                if (field.getName().equals("controllerName")) {
                    field.setAccessible(true);
                    controllerName = (String) field.get(controllerChain);
                    break;
                }
            }
            Object realController = beanFactory.getBean(controllerName);
            Field[] realControllerFields = realController.getClass().getDeclaredFields();
            // 循环遍历 Controller 的字段，为对象的属性赋值
            for (Field field : realControllerFields) {
                RealBean realBeanAnnotation = field.getDeclaredAnnotation(RealBean.class);
                if (realBeanAnnotation != null) {
                    field.setAccessible(true);
                    String value = realBeanAnnotation.value();
                    Object bean = beanFactory.getBean(value);
                    field.set(realController, bean);
                }
            }
            for (Field field : controllerChainClassDeclaredFields) {
                if (field.getName().equals("controllerName")) {
                    controllerChainClassController.setAccessible(true);
                    controllerChainClassController.set(controllerChain, realController);
                    requestMap.insertRequestMapping(url, controllerChain);
                    break;
                }
            }
        }
    }
}
