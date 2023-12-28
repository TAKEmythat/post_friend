package com.cwy.post_friend.frame.tool;

import com.cwy.post_friend.frame.bean.JavaFileObject;
import com.cwy.post_friend.frame.bean.XMLObject;
import com.cwy.post_friend.frame.enum_.InsertMethod;
import com.cwy.post_friend.frame.proxy.DaoProxy;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.net.URISyntaxException;
import java.util.List;

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
     * 生成继承类的类对象
     *
     * @param javaFileObject Java 文件类
     * @return 返回代理继承后的类对象
     * @throws IOException ...
     */
    public static Class<?> generateInheritanceClass(JavaFileObject javaFileObject, boolean allMethod,
                                                    InsertMethod insertMethod, Method... methods)
            throws IOException,
            ClassNotFoundException, InterruptedException {
        Class<?> clazz = null;
        File file = null;
        if (!javaFileObject.getFile().getAbsolutePath().contains("$Extend.java")) {
            file = new File(javaFileObject.getCompleteFileName().substring(0,
                    javaFileObject.getCompleteFileName().lastIndexOf(".")) + "$Extend.java");
        } else {
            file = javaFileObject.getFile();
        }
        javaFileObject.setFile(file);
        javaFileObject.setJavaCode(new StringBuilder());
        // 生成继承的文件
        javaFileObject.newFile();
        // 编写继承文件的代码
        if (allMethod) {
            if (insertMethod.equals(InsertMethod.JOURNAL)) {
                clazz = Class.forName(javaFileObject.getClassPathClassName());
                clazz = generateCodeForInheritanceClasses(javaFileObject, clazz, InsertMethod.JOURNAL);
            }
        } else {
            if (insertMethod.equals(InsertMethod.TRANSACTION)) {
                replaceFunctionAndGenerateClasses(javaFileObject, InsertMethod.TRANSACTION, methods[0]);
            }
        }
        // 删除继承的文件
        javaFileObject.deleteFile();
        return clazz;
    }

    /**
     * 将文件中的函数替换掉
     *
     * @param javaFileObject java 文件对象
     * @param insertMethod   插入的代码是什么类型
     * @param method         被替换函数
     * @return 返回类
     */
    public static Class<?> replaceFunctionAndGenerateClasses(JavaFileObject javaFileObject,
                                                             InsertMethod insertMethod, Method method) throws IOException, ClassNotFoundException, InterruptedException {
        List<String> methodList = javaFileObject.getMethodList();
        for (int i = 0; i < methodList.size(); i++) {
            int i1 = methodList.get(i).indexOf(method.getName());
            if (i1 == -1 || methodList.get(i).contains("//AFFAIR")) {
                continue;
            }
            String methodName = methodList.get(i).
                    substring(i1, methodList.get(i).indexOf("("));
            if (insertMethod.equals(InsertMethod.TRANSACTION)) {
                if (methodName.equals(method.getName())) {
                    String s = methodList.get(i);
                    String start = s.substring(0, s.indexOf("{") + 1);
                    String content = s.substring(s.indexOf("{") + 1, s.lastIndexOf("}"));
                    methodList.remove(i);
                    StringBuilder sb = new StringBuilder();
                    sb.append(start);
                    sb.append("try {\ncom.cwy.post_friend.frame.tool.JDBCUtil.openTransaction();");
                    sb.append(content);
                    sb.append("com.cwy.post_friend.frame.tool.JDBCUtil.submitTransaction();\n}\n");
                    sb.append("catch(Exception e) {com.cwy.post_friend.frame.tool.JDBCUtil.rollbackTransaction();\n}");
                    sb.append("//AFFAIR\n");
                    sb.append("}");
                    methodList.add(sb.toString());
                }
            }
        }
        javaFileObject.compoundJavaCode();
        javaFileObject.outputFile();
        String classPath = javaFileObject.getCompleteFileName().
                substring(0, javaFileObject.getCompleteFileName().
                        indexOf(javaFileObject.getClassPathClassName().
                                replace(".", "\\")));
        Process exec = Runtime.getRuntime().exec("javac " + javaFileObject.getFile().getAbsolutePath(),
                new String[]{"CLASSPATH=" + classPath});
        exec.waitFor();
        Class<?> clazz = Class.forName(javaFileObject.getClassPathClassName() + "$Extend");
        return clazz;
    }

    /**
     * 生成继承类的代码
     *
     * @param javaFileObject Java 文件对象
     */
    public static Class<?> generateCodeForInheritanceClasses(JavaFileObject javaFileObject, Class<?> clazz,
                                                             InsertMethod insertMethod, Method... methods)
            throws IOException, InterruptedException, ClassNotFoundException {
        FileOutputStream fos = new FileOutputStream(javaFileObject.getFile());
        Method[] declaredMethods = clazz.getDeclaredMethods();
        boolean addLogger = false;
        // 添加函数
        for (Method method : declaredMethods) {
            StringBuilder methodCode = new StringBuilder();
            int modifiers = method.getModifiers();
            if (Modifier.isPublic(modifiers)) {
                methodCode.append("public ");
            }
            String returnType = method.getReturnType().getTypeName();
            methodCode.append(returnType).append(" ");
            String methodName = method.getName();
            methodCode.append(methodName).append("(");
            Parameter[] parameters = method.getParameters();
            for (int i = 0; i < parameters.length; i++) {
                String parameterTypeName = parameters[i].getType().getTypeName();
                methodCode.append(parameterTypeName).append(" ");
                String name = parameters[i].getName();
                methodCode.append(name);
                if (i != parameters.length - 1) {
                    methodCode.append(", ");
                }
            }
            methodCode.append(") {").append("\n");
            if (!addLogger && insertMethod.equals(InsertMethod.JOURNAL)) {
                javaFileObject.addImportClass("import java.text.SimpleDateFormat;\n");
                javaFileObject.addImportClass("import java.util.Date;\n");
                javaFileObject.addImportClass("import java.util.logging.Logger;\n");
                javaFileObject.addFields("private static final Logger logger =" +
                        " Logger.getLogger(\"" + javaFileObject.getClassPathClassName() + "$Extend" + "\");\n");
                addLogger = true;
            }
            if (insertMethod.equals(InsertMethod.JOURNAL)) {
                methodCode.append("logger.info(\"程序在运行 \" + new SimpleDateFormat(\"yyyy-MM-dd HH:mm:ssss\").format" +
                        "(new Date()));\n");
                Parameter[] parameters1 = method.getParameters();
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < parameters1.length; i++) {
                    sb.append(parameters1[i].getName());
                    if (i != parameters1.length - 1) {
                        sb.append(", ");
                    }
                }
                methodCode.append("super.").append(method.getName()).append("(").append(sb).append(")").append(";\n");
            }
            methodCode.append("}\n");
            javaFileObject.addMethods(String.valueOf(methodCode));
        }
        javaFileObject.compoundJavaCode();
        javaFileObject.outputFile();
        fos.close();
        return compileInheritanceClass(javaFileObject);
    }

    public static Class<?> compileInheritanceClass(JavaFileObject javaFileObject) throws IOException, InterruptedException, ClassNotFoundException {
        String replace = javaFileObject.getClassPathClassName().replace(".", "\\");
        String substring = javaFileObject.getCompleteFileName().substring(0, javaFileObject.getCompleteFileName().indexOf(replace));
        Process exec = Runtime.getRuntime().exec("javac " + javaFileObject.getFile().getAbsolutePath(),
                new String[]{"CLASSPATH=" + substring});
        exec.waitFor();
        return Class.forName(javaFileObject.getClassPathClassName() + "$Extend");
    }

    /**
     * 生成类文件的代理类文件
     * 1. 生成 JavaFileObject 对象通过类文件
     * 2. 创建目标接口类文件的代理类文件
     *
     * @param className 目标类文件名
     * @return 返回一个生成的类
     * @throws URISyntaxException     ...
     * @throws IOException            ...
     * @throws ClassNotFoundException ...
     * @throws InterruptedException   ...
     */
    public static Class<?> generateImplementationClass(String className, String proxyMethod, XMLObject xmlObject)
            throws URISyntaxException, IOException, ClassNotFoundException, InterruptedException {
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
