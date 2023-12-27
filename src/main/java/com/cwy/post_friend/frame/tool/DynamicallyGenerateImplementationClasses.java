package com.cwy.post_friend.frame.tool;

import com.cwy.post_friend.frame.bean.JavaFileObject;
import com.cwy.post_friend.frame.bean.XMLObject;
import com.cwy.post_friend.frame.proxy.DaoProxy;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;

/**
 * 动态生成接口实现类的工具类
 *
 * @Classname DynamicallyGenerateImplementationClasses
 * @Description TODO
 * @Author stomach medicine
 * @Version 1.0.0
 * @Create 2023-12-25 8:22
 * @Since 1.0.0
 */

public class DynamicallyGenerateImplementationClasses {
    private DynamicallyGenerateImplementationClasses() {
    }

    public static JavaFileObject generateJavaFileObject(String className) throws URISyntaxException {
        return new JavaFileObject(className, "");
    }

    /**
     * 生成类文件的代理类文件
     * 1. 生成 JavaFileObject 对象通过类文件
     * 2. 创建目标接口类文件的代理类文件
     * 3. 创建
     *
     * @param className 目标类文件名
     * @return
     * @throws URISyntaxException     ...
     * @throws IOException            ...
     * @throws ClassNotFoundException ...
     * @throws InterruptedException   ...
     */
    public static Class<?> generateImplementationClass(String className, String proxyMethod, XMLObject xmlObject) throws URISyntaxException,
            IOException,
            ClassNotFoundException, InterruptedException {
        Class<?> clazz = null;
        JavaFileObject generateJavaFile = null;
        JavaFileObject javaFileObject = generateJavaFileObject(className);
        String generateFileName =
                javaFileObject.getCompleteFileName().substring(0,
                        javaFileObject.getCompleteFileName().lastIndexOf("."))
                        + "$Impl.java";
        javaFileObject.setFile(new File(generateFileName));
        // 创建源文件
        javaFileObject.newFile();
        // 在这一步使用我们自己原生写的代理工具类去创建我们的代理类
        if (proxyMethod.equals("DAO")) {
            javaFileObject.implementInterfaceClass(Class.forName(className));
            DaoProxy daoProxy = new DaoProxy(javaFileObject, xmlObject);
            generateJavaFile = daoProxy.generateJavaFileObjectByXMLObject();
            clazz = compileJavaFile(generateJavaFile);
        }
        // 删除源文件
        javaFileObject.deleteFile();
        return clazz;
    }

    public static Class<?> compileJavaFile(JavaFileObject javaFileObject) throws ClassNotFoundException,
            IOException, InterruptedException {
        FileOutputStream fos = new FileOutputStream(javaFileObject.getFile());
        fos.write(javaFileObject.getJavaCode().toString().getBytes());
        String classPath = javaFileObject.getFile().getAbsolutePath().substring(0,
                javaFileObject.getFile().getAbsolutePath().indexOf(javaFileObject.
                        getClassPathClassName().replace(".", "\\")));
        String fileName = javaFileObject.getFile().getAbsolutePath();
        Process exec1 = Runtime.getRuntime().exec("javac " + fileName,
                new String[]{"CLASSPATH=" + classPath});
        exec1.waitFor();
        fos.close();
        String packageName = javaFileObject.getPackageName().replace(".", "\\");
        String absolutePath =
                javaFileObject.getFile().getAbsolutePath().
                        substring(javaFileObject.getFile().
                                getAbsolutePath().indexOf(packageName)).
                replace("\\", ".");
        absolutePath = absolutePath.substring(0, absolutePath.lastIndexOf("."));
        return Class.forName(absolutePath);
    }
}
