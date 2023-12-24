package com.cwy.post_friend.frame.controller;

import com.cwy.post_friend.exception.frame.AnnotationException;
import com.cwy.post_friend.frame.annotation.config.Allocation;
import com.cwy.post_friend.frame.annotation.config.ScanningPackage;
import com.cwy.post_friend.frame.annotation.ordinary.Component;
import com.cwy.post_friend.frame.annotation.ordinary.Controller;
import com.cwy.post_friend.frame.annotation.ordinary.Dao;
import com.cwy.post_friend.frame.annotation.ordinary.Service;
import com.cwy.post_friend.frame.factory.BeanFactory;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;

import java.io.File;
import java.lang.annotation.Annotation;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
    private final BeanFactory beanFactory = new BeanFactory();

    @Override
    public void init() throws ServletException {
        super.init();
        try {
            scanConfigurationClass();
        } catch (AnnotationException | URISyntaxException | ClassNotFoundException |
                 InstantiationException |
                 IllegalAccessException e) {
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
        List<Class<?>> classList = scanBeans(dir, Allocation.class);
        for (Class<?> clazz : classList) {
            beanFactory.insertConfigBeans(clazz.getSimpleName(), clazz.newInstance());
            scanOrdinaryBeanWhitConfiguration();
            Map<String, Object> ordinaryBeans = beanFactory.getOrdinaryBeans();
            System.out.println("ordinaryBeans = " + ordinaryBeans);
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
                    List<Class<?>> classList = scanBeans(packageDir, Component.class, Dao.class, Service.class, Controller.class);
                    // 循环添加普通 Bean 到 BeanFactory 中
                    for (Class<?> clazz0 : classList) {
                        beanFactory.insertOrdinaryBeans(clazz0.getSimpleName(), clazz0.newInstance());
                    }
                }
            }
        }
    }

    public void injection() {

    }

    /**
     * 遍历整个目录，查找任意类文件中是否有这些注解
     *
     * @param dir         查找的目录
     * @param annotations 查找的注解
     * @return 返回符合条件的类
     * @throws AnnotationException 注解错误
     */
    public List<Class<?>> scanBeans(File dir, Class<?>... annotations) throws AnnotationException,
            ClassNotFoundException {
        List<Class<?>> classList0 = new ArrayList<>();
        URL resource = Thread.currentThread().getContextClassLoader().getResource("");
        String fileName = null;
        // 为了得出项目根路径
        if (resource != null) {
            fileName = resource.getFile();
            fileName = fileName.replace("/", "\\");
            fileName = fileName.substring(1);
        }
        List<File> fileList = new ArrayList<>();
        List<Class<?>> classList = new ArrayList<>();
        if (annotations.length == 0) {
            throw new AnnotationException("There are no annotations available");
        }
        // 扫描该目录下的所有文件
        scanDir(dir, fileList);
        // 遍历文件
        if (fileList.size() > 0) {
            for (File file : fileList) {
                String absolutePath = file.getAbsolutePath();
                assert fileName != null;
                // 得出类文件，并且加载类文件
                if (absolutePath.contains(fileName)) {
                    String f = absolutePath.substring(fileName.length());
                    f = f.substring(0, f.indexOf("."));
                    f = f.replace("\\", ".");
                    Class<?> clazz = Class.forName(f);
                    classList.add(clazz);
                }
            }
        }
        // 扫描注解
        for (Class<?> annotation : annotations) {
            // 遍历类
            if (classList.size() > 0) {
                for (Class<?> clazz : classList) {
                    Annotation[] annotations0 = clazz.getDeclaredAnnotations();
                    // 遍历类上面的注解
                    for (Annotation a : annotations0) {
                        // 如果类上面的注解与我们指定的注解相同
                        if (a.annotationType().equals(annotation)) {
                            classList0.add(clazz);
                        }
                    }
                }
            }
        }
        return classList0;
    }


    /**
     * 递归这个目录，将所有文件保存到 fileList
     *
     * @param dir      被递归的目录
     * @param fileList 被保存的 fileList
     */
    public void scanDir(File dir, List<File> fileList) {
        if (dir.isDirectory()) {
            File[] files = dir.listFiles();
            if (files != null && files.length >= 1) {
                for (File file : files) {
                    scanDir(file, fileList);
                }
            }
        } else {
            fileList.add(dir);
        }
    }
}
