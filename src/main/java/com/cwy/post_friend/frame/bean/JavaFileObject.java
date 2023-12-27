package com.cwy.post_friend.frame.bean;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * @Classname JavaFileObejct
 * @Description TODO
 * @Author stomach medicine
 * @Version 1.0.0
 * @Create 2023-12-26 10:38
 * @Since 1.0.0
 */

public class JavaFileObject {
    // 包名，例如：com.xxx
    private String packageName;
    // 类文件
    private File file;
    // 完整路径的文件名，例如：E:\\code\\xxx.java，后缀是一定带有 .java 的
    private String completeFileName;
    // 导入的其他依赖或包名，例如：com.xxx.*
    private List<String> importClass = new ArrayList<>();
    // 类路径的文件名，例如：com.xxx.*，后缀什么都没带
    private String classPathClassName;
    // 类名，例如：User.class -> User
    private String className;
    // 实现的接口名
    private List<String> implementedInterface = new ArrayList<>();
    // 字段列表，例如：private Object a = 19
    private List<String> fieldList = new ArrayList<>();
    // 方法列表，例如：public String register() {return "register 成功"}
    private List<String> methodList = new ArrayList<>();
    // Java 文件的所有代码
    private StringBuilder javaCode = new StringBuilder();

    public JavaFileObject(File file) {
        try {
            this.file = file;
            this.completeFileName = file.getAbsolutePath();
            URL resource = Thread.currentThread().getContextClassLoader().getResource("");
            assert resource != null;
            URI uri = resource.toURI();
            File dir = new File(uri);
            if (this.completeFileName.contains(dir.getAbsolutePath())) {
                this.classPathClassName = completeFileName.substring(dir.getAbsolutePath().length() + 1);
                this.classPathClassName = classPathClassName.substring(0, classPathClassName.lastIndexOf("."));
                this.classPathClassName = this.classPathClassName.replace("\\", ".");
            }
            this.packageName = this.classPathClassName.substring(0, this.classPathClassName.lastIndexOf("."));
            this.className = this.classPathClassName.substring(this.classPathClassName.lastIndexOf(".") + 1);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public void addInterfaces(String... interfaces) {
        this.implementedInterface.addAll(Arrays.stream(interfaces).toList());
    }

    public JavaFileObject(String completeFileName) {
        this(new File(completeFileName));
    }

    public JavaFileObject(String classPathClassName, Object... null_) throws URISyntaxException {
        this(new File(Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource(classPathClassName.replace(".", "\\") + ".class")).toURI()));
    }

    public void addImportClass(String... importClass) {
        this.importClass.addAll(Arrays.stream(importClass).toList());
    }

    public void addFields(String... fields) {
        this.fieldList.addAll(Arrays.stream(fields).toList());
    }

    public void addMethods(String... methods) {
        this.methodList.addAll(Arrays.stream(methods).toList());
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JavaFileObject that = (JavaFileObject) o;
        return Objects.equals(file, that.file) && Objects.equals(completeFileName, that.completeFileName) && Objects.equals(classPathClassName, that.classPathClassName) && Objects.equals(className, that.className) && Objects.equals(fieldList, that.fieldList) && Objects.equals(methodList, that.methodList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(file, completeFileName, classPathClassName, className, fieldList, methodList);
    }

    @Override
    public String toString() {
        return "JavaFileObject{" +
                "packageName='" + packageName + '\'' +
                ", importClass=" + importClass +
                ", file=" + file +
                ", completeFileName='" + completeFileName + '\'' +
                ", classPathClassName='" + classPathClassName + '\'' +
                ", className='" + className + '\'' +
                ", fieldList=" + fieldList +
                ", methodList=" + methodList +
                ", javaCode=" + javaCode +
                '}';
    }

    /**
     * 创建文件
     *
     * @throws IOException ...
     */
    public void newFile() throws IOException {
        if (!file.exists()) {
            file.createNewFile();
        }
        javaCode.append("$package$\n$import$\n$class0$\n$field$\n$methods$\n$class1$");
    }

    /**
     * 删除文件
     */
    public void deleteFile() {
        file.delete();
    }

    /**
     * 解析接口并且填充到我们这个对象上
     *
     * @param interfaceClass 传过来的接口参数
     */
    public void implementInterfaceClass(Class<?> interfaceClass) {
        Method[] interfaceClassMethods = interfaceClass.getDeclaredMethods();
        List<String> methodsList = new ArrayList<>();
        for (Method method : interfaceClassMethods) {
            boolean isPublic;
            isPublic = Modifier.isPublic(method.getModifiers());
            if (isPublic) {
                StringBuilder methods = new StringBuilder();
                // 生成函数的头
                methods.append("public ").append(method.getReturnType().getName()).append(" ").append(method.getName()).append("(");
                Parameter[] parameters = method.getParameters();
                for (int i = 0; i < parameters.length; i++) {
                    Class<?> type = parameters[i].getType();
                    String name = parameters[i].getName();
                    if (i == parameters.length - 1) {
                        methods.append(type.getName()).append(" ").append(name);
                    } else {
                        methods.append(type.getName()).append(" ").append(name).append(", ");
                    }
                }
                methods.append(")\n{\n");
                methods.append("$fillCode$\n");
                methods.append("\n}\n");
                methodsList.add(methods.toString());
            }
        }
        for (String method : methodsList) {
            this.addMethods(method);
        }
        compoundJavaCode();
    }

    /**
     * 整合代码到内存中
     */
    public void compoundJavaCode() {
        String p0 = "$package$";
        String i0 = "$import$";
        String c0 = "$class0$";
        String f0 = "$field$";
        String m0 = "$methods$";
        String c1 = "$class1$";
        StringBuilder interfaces = new StringBuilder();
        // 导入类
        List<String> anInterface = getImplementedInterface();
        String replace = file.getAbsolutePath().replace("\\", ".");
        String substring = replace.substring(0, replace.lastIndexOf("."));
        int i = substring.indexOf(classPathClassName);
        String substring1 = substring.substring(i);
        if (!substring1.equals(classPathClassName)) {
            anInterface.add(classPathClassName);
        }
        for (String s : anInterface) {
            interfaces.append(s);
        }
        // 替换 package
        javaCode.replace(javaCode.indexOf(p0),
                javaCode.indexOf(p0) + p0.length(), "package " + packageName + ";\n");
        StringBuilder sb = new StringBuilder();
        // 替换 import
        for (String importS : importClass) {
            sb.append(importS).append("\n");
        }
        javaCode.replace(javaCode.indexOf(i0),
                javaCode.indexOf(i0) + i0.length(), sb + "\n");
        StringBuilder s = new StringBuilder().append("public class ").
                append(file.getName(), 0, file.getName().lastIndexOf("."));
        if (interfaces.length() > 1) {
            s.append(" implements ").append(interfaces);
        }
        // 替换 class0
        javaCode.replace(javaCode.indexOf(c0),
                javaCode.indexOf(c0) + c0.length(), s + " {");
        // 替换 field
        sb = new StringBuilder();
        for (String field : fieldList) {
            sb.append(field).append("\n");
        }
        javaCode.replace(javaCode.indexOf(f0),
                javaCode.indexOf(f0) + f0.length(), sb.toString());
        // 替换 methods
        sb = new StringBuilder();
        for (String method : methodList) {
            sb.append(method).append("\n");
        }
        javaCode.replace(javaCode.indexOf(m0),
                javaCode.indexOf(m0) + m0.length(), sb.toString());
        // 结尾的花括号 "}"
        javaCode.replace(javaCode.indexOf(c1),
                javaCode.indexOf(c1) + c1.length(), "}\n");
    }

    /**
     * 输出代码到文件中
     *
     * @throws IOException ...
     */
    public void outputFile() throws IOException {
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(javaCode.toString().getBytes());
        fos.close();
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public List<String> getImportClass() {
        return importClass;
    }

    public void setImportClass(List<String> importClass) {
        this.importClass = importClass;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public String getCompleteFileName() {
        return completeFileName;
    }

    public void setCompleteFileName(String completeFileName) {
        this.completeFileName = completeFileName;
    }

    public String getClassPathClassName() {
        return classPathClassName;
    }

    public void setClassPathClassName(String classPathClassName) {
        this.classPathClassName = classPathClassName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public List<String> getFieldList() {
        return fieldList;
    }

    public void setFieldList(List<String> fieldList) {
        this.fieldList = fieldList;
    }

    public List<String> getMethodList() {
        return methodList;
    }

    public void setMethodList(List<String> methodList) {
        this.methodList = methodList;
    }

    public StringBuilder getJavaCode() {
        return javaCode;
    }

    public List<String> getImplementedInterface() {
        return implementedInterface;
    }

    public void setImplementedInterface(List<String> implementedInterface) {
        this.implementedInterface = implementedInterface;
    }

    public void setJavaCode(StringBuilder javaCode) {
        this.javaCode = javaCode;
    }
}
