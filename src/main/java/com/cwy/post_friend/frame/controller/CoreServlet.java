package com.cwy.post_friend.frame.controller;

import com.cwy.post_friend.exception.frame.AnnotationException;
import com.cwy.post_friend.frame.factory.BeanFactory;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * 整体流程：
 * 1. 扫描配置类
 * 2. 扫描配置类的包
 *
 * @Classname CoreServlet
 * @Description TODO
 * @Author stomach medicine
 * @Version 1.0.0
 * @Create 2023-12-23 22:11
 * @Since 1.0.0
 */

public class CoreServlet extends HttpServlet {
    private BeanFactory beanFactory = new BeanFactory();

    @Override
    public void init() throws ServletException {
        super.init();
        scanConfigurationClass();
    }

    /**
     * 扫描配置类
     */
    public void scanConfigurationClass() {
        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        URL resource = contextClassLoader.getResource("");
        String fileName = resource.getFile();
        File file = new File(fileName);
    }

    /**
     * 遍历整个目录，查找任意类文件中是否有这些注解
     *
     * @param file        查找的目录
     * @param annotations 查找的注解
     * @return 返回文件名，就是符合条件的类文件名
     * @throws AnnotationException 注解错误
     */
    public List<String> scanBeans(File file, Class<?>... annotations) throws AnnotationException {
        List<File> fileList = new ArrayList<>();
        if (annotations.length == 1) {
            throw new AnnotationException("There are no annotations available");
        }
        scanDir(file, fileList);
        for (Class<?> annotation : annotations) {
        }
    }

    /**
     * 递归这个目录或文件，如果是文件则返回，如果是目录
     *
     * @param file 被递归的文件或目录
     * @return
     */
    public void scanDir(File file, List<File> fileList) {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null && files.length > 1) {
                scanDir(files[0], fileList);
            }
        } else {
            fileList.add(file);
        }
    }
}
