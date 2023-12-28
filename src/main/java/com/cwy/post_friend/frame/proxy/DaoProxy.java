package com.cwy.post_friend.frame.proxy;

import com.cwy.post_friend.frame.bean.JavaFileObject;
import com.cwy.post_friend.frame.bean.XMLLabel;
import com.cwy.post_friend.frame.bean.XMLObject;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Dao 层的动态代理对象
 *
 * @Classname DaoProxy
 * @Description TODO
 * @Author stomach medicine
 * @Version 1.0.0
 * @Create 2023-12-24 14:58
 * @Since 1.0.0
 */
public class DaoProxy {
    private JavaFileObject javaFileObject;
    private XMLObject xmlObject;

    /**
     * 通过对象的 xmlObject 属性生成 javaFileObject 对象
     *
     * @return 代码填充好的 javaFileObject 对象
     */
    public JavaFileObject generateJavaFileObjectByXMLObject() {
        javaFileObject.addImportClass("import com.cwy.post_friend.frame.tool.JDBCUtil;\n");
        javaFileObject.addImportClass("import java.util.ArrayList;\n");
        javaFileObject.addImportClass("import java.sql.*;\n");
        javaFileObject.addImportClass("import java.util.Arrays;\n");
        javaFileObject.addImportClass("import java.util.List;\n");
        generateUpdateCode();
        generateQueryCode();
        injectCodeIntoMemory();
        return javaFileObject;
    }

    /**
     * 注入全部代码到内存中
     */
    public void injectCodeIntoMemory() {
        StringBuilder javaCode = javaFileObject.getJavaCode();
        List<String> fieldList = javaFileObject.getFieldList();
        List<String> methodList = javaFileObject.getMethodList();
        StringBuilder importClassCode = new StringBuilder();
        int importSign = javaCode.indexOf("\n") + 1;
        StringBuilder fieldCode = new StringBuilder();
        StringBuilder methodCode = new StringBuilder();
        Set<String> importClass = javaFileObject.getImportClass();
        // 清空函数区
        javaCode.replace(javaCode.indexOf("{") + 1, javaCode.lastIndexOf("}"), "\n\n");
        javaCode.append("       ");
        // 导包
        for (String importCode : importClass) {
            importClassCode.append(importCode);
        }
        javaCode.replace(importSign, importSign + 1, importClassCode.toString());
        int fieldSign = javaCode.indexOf("{") + 1;
        // 注入属性
        for (String field : fieldList) {
            fieldCode.append(field);
        }
        javaCode.replace(fieldSign, fieldSign + 1, fieldCode.toString());
        int methodSign = javaCode.lastIndexOf("}");
        // 注入函数
        for (String method : methodList) {
            methodCode.append(method);
        }
        javaCode.replace(methodSign - 1, methodSign, methodCode.toString());
    }

    /**
     * 生成添加数据的 SQL 语句
     * 例如：INSERT INTO xxx VALUES(***)
     */
    private void generateUpdateCode() {
        List<XMLLabel> xmlLabels = xmlObject.getXmlLabels();
        List<String> methodList = javaFileObject.getMethodList();
        // 循环遍历标签对象
        xmlLabels.forEach(xmlLabel -> {
            boolean injection = false;
            int methodIndex = 999999;
            Map<String, String> attributes = xmlLabel.getAttributes();
            String labelName = xmlLabel.getLabelName();
            // 如果是添加标签
            if (labelName.equals("insert")) {
                for (int i = 0; i < methodList.size(); i++) {
                    if (methodList.get(i).contains(attributes.get("id"))) {
                        injection = true;
                        methodIndex = i;
                    }
                }
            } else if (labelName.equals("delete")) {
                for (int i = 0; i < methodList.size(); i++) {
                    if (methodList.get(i).contains(attributes.get("id"))) {
                        injection = true;
                        methodIndex = i;
                    }
                }
            } else if (labelName.equals("update")) {
                for (int i = 0; i < methodList.size(); i++) {
                    if (methodList.get(i).contains(attributes.get("id"))) {
                        injection = true;
                        methodIndex = i;
                    }
                }
            }
            if (injection) {
                StringBuilder sb = new StringBuilder();
                int argSize = 0;
                String[] methodArgs = new String[0];
                String method = methodList.get(methodIndex);
                int arg0 = method.indexOf("(");
                int arg1 = method.indexOf(")");
                if (arg1 - arg0 != 1) {
                    argSize = 1;
                    String methodArg = method.substring(arg0, arg1);
                    methodArgs = methodArg.split(",");
                    for (int i = 0; i < methodArgs.length; i++) {
                        methodArgs[i] = methodArgs[i].trim();
                    }
                    if (methodArgs.length > 1) {
                        argSize = methodArgs.length;
                    }
                }
                if (argSize == 1) {
                    sb.append("preparedStatement.setObject(").
                            append(1).append(", ").
                            append(methodArgs[0].
                                    substring(methodArgs[0].
                                            indexOf(" ") + 1)).append(");\n");
                }
                for (int i = 0; i < argSize && argSize > 1; i++) {
                    sb.append("preparedStatement.setObject(").
                            append(i + 1).append(", ").
                            append(methodArgs[i].
                                    substring(methodArgs[i].
                                            indexOf(" ") + 1)).append(");\n");
                }
                String insertCode = "int i = 0;" +
                        "try{\nConnection connection = JDBCUtil.getConnection();\n" +
                        "PreparedStatement preparedStatement = connection.prepareStatement(\""
                        + xmlLabel.getValue().replace("\n", "") +
                        "\");\n" + sb + "\n" +
                        "i = preparedStatement.executeUpdate();"+
                        "}catch(Exception e){ e.printStackTrace(); }"+
                        "return i;";
                String s = methodList.get(methodIndex);
                String replace = s.replace("$fillCode$", insertCode);
                methodList.remove(methodIndex);
                methodList.add(replace);
            }
        });
    }

    private void generateQueryCode() {
        List<XMLLabel> xmlLabels = xmlObject.getXmlLabels();
        List<String> methodList = javaFileObject.getMethodList();
        // 循环遍历标签对象
        xmlLabels.forEach(xmlLabel -> {
            boolean injection = false;
            int methodIndex = 999999;
            Map<String, String> attributes = xmlLabel.getAttributes();
            String labelName = xmlLabel.getLabelName();
            // 如果是添加标签
            if (labelName.equals("select")) {
                for (int i = 0; i < methodList.size(); i++) {
                    if (methodList.get(i).contains(attributes.get("id"))) {
                        injection = true;
                        methodIndex = i;
                    }
                }
            }
            if (injection) {
                StringBuilder sb = new StringBuilder();
                int argSize = 0;
                String[] methodArgs = new String[0];
                String method = methodList.get(methodIndex);
                int arg0 = method.indexOf("(");
                int arg1 = method.indexOf(")");
                if (arg1 - arg0 != 1) {
                    argSize = 1;
                    String methodArg = method.substring(arg0, arg1);
                    methodArgs = methodArg.split(",");
                    for (int i = 0; i < methodArgs.length; i++) {
                        methodArgs[i] = methodArgs[i].trim();
                    }
                    if (methodArgs.length > 1) {
                        argSize = methodArgs.length;
                    }
                }
                if (argSize == 1) {
                    sb.append("preparedStatement.setObject(").
                            append(1).append(", ").
                            append(methodArgs[0].
                                    substring(methodArgs[0].
                                            indexOf(" ") + 1)).append(");\n");
                }
                for (int i = 0; i < argSize && argSize > 1; i++) {
                    sb.append("preparedStatement.setObject(").
                            append(i + 1).append(", ").
                            append(methodArgs[i].
                                    substring(methodArgs[i].
                                            indexOf(" ") + 1)).append(");\n");
                }
                StringBuilder result = new StringBuilder();
                for (int i = 0; i < 20; i++) {
                    result.append("Object object").append(i + 1).append(" = resultSet.getObject(").append(i + 1).append(
                            ")" + ";\n").append("list.add(object").append(i + 1).append(");");
                }
                String insertCode = "List<Object> objects = new ArrayList<>();\n" +
                        "try{" +
                        "Connection connection = JDBCUtil.getConnection();\n" +
                        "PreparedStatement preparedStatement = connection.prepareStatement(\""
                        + xmlLabel.getValue().replace("\n", "") +
                        "\");\n" + sb +
                        "ResultSet resultSet = preparedStatement.executeQuery();\n" +
                        "while (resultSet.next()) {\n" +
                        "List<Object> list = new ArrayList<>();\n" +
                        "try {" +
                        result + "}" +
                        "catch(Exception e){\n" +
                        "objects.addAll(list);}}\n}" +
                        "catch(Exception e) { e.printStackTrace(); }" +
                        "return objects;\n";
                String s = methodList.get(methodIndex);
                String replace = s.replace("$fillCode$", insertCode);
                methodList.remove(methodIndex);
                methodList.add(replace);
            }
        });
    }

    public DaoProxy(JavaFileObject javaFileObject, XMLObject xmlObject) {
        this.javaFileObject = javaFileObject;
        this.xmlObject = xmlObject;
    }

    public DaoProxy() {
    }

    public JavaFileObject getJavaFileObject() {
        return javaFileObject;
    }

    public void setJavaFileObject(JavaFileObject javaFileObject) {
        this.javaFileObject = javaFileObject;
    }

    public XMLObject getXmlObject() {
        return xmlObject;
    }

    public void setXmlObject(XMLObject xmlObject) {
        this.xmlObject = xmlObject;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DaoProxy daoProxy = (DaoProxy) o;
        return Objects.equals(javaFileObject, daoProxy.javaFileObject) && Objects.equals(xmlObject, daoProxy.xmlObject);
    }

    @Override
    public int hashCode() {
        return Objects.hash(javaFileObject, xmlObject);
    }

    @Override
    public String toString() {
        return "DaoProxy{" +
                "javaFileObject=" + javaFileObject +
                ", xmlObject=" + xmlObject +
                '}';
    }
}
