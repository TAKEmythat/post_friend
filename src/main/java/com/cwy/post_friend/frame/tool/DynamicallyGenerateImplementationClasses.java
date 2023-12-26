package com.cwy.post_friend.frame.tool;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.*;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

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

    /**
     * 生成 className 的实现类
     *
     * @param className 格式：`com.xxx.*`
     * @return 返回生成后的类名
     * @throws URISyntaxException     找不到资源错误
     * @throws IOException            资源连接错误
     * @throws ClassNotFoundException 找不到类错误
     */
    public static Class<?> generateImplementationClass(String className) throws URISyntaxException, IOException, ClassNotFoundException, InterruptedException {
        className = className.replace(".", "/") + ".class";
        URL resource = Thread.currentThread().getContextClassLoader().getResource(className);
        assert resource != null;
        URI uri = resource.toURI();
        File classfile = new File(uri);
        String path = classfile.getParent();
        String generateFileName =
                path + "\\" + classfile.getName().substring(0, classfile.getName().lastIndexOf(".")) +
                        "Impl$.java";
        File file = new File(generateFileName);
        // 生成源文件
        if (!file.exists()) {
            boolean newFile = file.createNewFile();
        }
        String achieveClassName = writeClassIntoJavaFile(className.replace("/", ".").
                substring(0, className.lastIndexOf(".")), generateFileName);
        // 删除源文件
        boolean delete = file.delete();
        return Class.forName(achieveClassName);
    }

    /**
     * 将指定的接口类文件内容写入 Java 文件中，并实现类加载
     *
     * @param className 全限定类名
     * @param fileName  资源管理器文件全名
     * @return 返回生成后的实现类的全限定类名
     * @throws ClassNotFoundException 找不到类错误
     * @throws IOException            资源连接错误
     */
    public static String writeClassIntoJavaFile(String className, String fileName) throws ClassNotFoundException,
            IOException, InterruptedException {
        JavaCompiler javaCompiler = ToolProvider.getSystemJavaCompiler();
        StringBuilder classCode = new StringBuilder();
        Class<?> clazz = Class.forName(className);
        Method[] declaredMethods = clazz.getDeclaredMethods();
        String packageName = "package " + clazz.getPackageName() + ";\n";
        classCode.append(packageName);
        String className0 = "public class " + fileName.substring(fileName.lastIndexOf("\\") + 1,
                fileName.lastIndexOf(".")) + " implements " +
                className.substring(className.lastIndexOf(".") + 1) + " {\n";
        classCode.append(className0);
        for (Method method : declaredMethods) {
            StringBuilder methodAll = new StringBuilder();
            int modifiers = method.getModifiers();
            methodAll.append(Modifier.isPublic(modifiers) ? "public " : "");
            Class<?> returnType = method.getReturnType();
            String returnTypeName = returnType.getName();
            methodAll.append(returnTypeName).append(" ");
            String methodName = method.getName();
            methodAll.append(methodName).append("(");
            Parameter[] parameters = method.getParameters();
            for (int i = 0; i < parameters.length; i++) {
                StringBuilder methodParameters = new StringBuilder();
                Class<?> parameterType = parameters[i].getType();
                String parameterTypeName = parameterType.getTypeName();
                methodParameters.append(parameterTypeName).append(" ");
                String name = parameters[i].getName();
                methodParameters.append(name);
                if (i != parameters.length - 1) {
                    methodParameters.append(", ");
                }
                methodAll.append(methodParameters);
            }
            String parameterEnd = ")";
            methodAll.append(parameterEnd);
            methodAll.append("{\nreturn null;\n}\n");
            classCode.append(methodAll);
        }
        String end = "\n}\n";
        classCode.append(end);
        File file = new File(fileName);
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(classCode.toString().getBytes());
        String cutting = className.replace(".", "\\").substring(0, className.lastIndexOf("."));
        String classPath = fileName.substring(0, fileName.indexOf(cutting) - 1);
        Process exec1 = Runtime.getRuntime().exec("javac " + fileName,
                new String[]{"CLASSPATH=" + classPath});
        int run = javaCompiler.run(null, null, null, fileName);
        String implementSubclassesName =
                className.substring(0, className.lastIndexOf(".")) + "." +
                        fileName.substring(fileName.lastIndexOf("\\") + 1
                                , fileName.lastIndexOf("."));
        fos.close();
        return implementSubclassesName;
    }
}
