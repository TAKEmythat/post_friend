package com.cwy.post_friend.frame.controller;

import com.alibaba.fastjson2.JSON;
import com.cwy.post_friend.frame.annotation.reponse.Response;
import com.cwy.post_friend.frame.annotation.request.RequestBody;
import com.cwy.post_friend.frame.annotation.request.RequestMapping;
import com.cwy.post_friend.frame.annotation.request.RequestParam;
import com.cwy.post_friend.frame.bean.ControllerChain;
import com.cwy.post_friend.frame.bean.Handler;
import com.cwy.post_friend.frame.enum_.RequestMode;
import com.cwy.post_friend.frame.factory.RequestMap;
import com.cwy.post_friend.frame.view.InternalResourceViewResolver;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

/**
 * 基础的分配器/任何请求应当来到这个Controller层
 * 再由该层 根据 RequestMapping 分发方法
 *
 * @projectName: post_friend
 * @package: com.cwy.post_friend.controller
 * @className: DispatcherServlet
 * @author: LGJ
 * @description: TODO
 * @date: 2023/12/24 15:22
 * @version: 1.0
 */

public class DispatcherServlet extends HttpServlet {
    private final HashMap<String, Handler> urlMapping = new HashMap<>();

    private int contextPathLength = 0;

    private InternalResourceViewResolver internalResourceViewResolver = null;

    @Override
    public void init() throws ServletException {
        //计算context长度
        contextPathLength = getServletContext().getContextPath().length();

        // 获得map路径映射
        RequestMap requestMap = RequestMap.newInstance();
        // 注册路径映射
        registerMap(requestMap);

        // 视图解析器
        String prefix = getServletConfig().getInitParameter("prefix");
        String suffix = getServletConfig().getInitParameter("suffix");
        internalResourceViewResolver = new InternalResourceViewResolver(prefix, suffix);
        Map<String, Object> requestMapping = requestMap.getRequestMapping();
        System.out.println("requestMapping = " + requestMapping);
    }

    /**
     * 将给的RequestMap映射 注册到本类的属性urlMapping中备用
     *
     * @param requestMap
     */
    private void registerMap(RequestMap requestMap) {
        Map<String, Object> requestMapping = requestMap.getRequestMapping();

        requestMapping.forEach(new BiConsumer<String, Object>() {
            @Override
            public void accept(String url_part1, Object o) {
                ControllerChain o1 = (ControllerChain) o;

                Object obj = o1.getController();
                Class<?> clazz = obj.getClass();

                // 遍历方法 拿到注解RequestMapping上的路径
                Method[] declaredMethods = clazz.getDeclaredMethods();
                for (Method declaredMethod : declaredMethods) {
                    RequestMapping requestMappingAnnotation = declaredMethod.getAnnotation(RequestMapping.class);
                    if (requestMappingAnnotation != null) {
                        // 拼接请求路径
                        String url_part2 = requestMappingAnnotation.value();
                        String url = url_part1 + url_part2;

                        RequestMode mode = requestMappingAnnotation.mode();
                        Handler handler = new Handler(obj, declaredMethod, mode);
                        // 用于参数和位置的map
                        Map<String, Integer> paramClassNameMap = handler.getParamClassNameMap();

                        Parameter[] parameters = declaredMethod.getParameters();
                        for (int i = 0; i < parameters.length; i++) {
                            // req和resp存简单类名，其他的存形参名称
                            if (parameters[i].getType() == HttpServletRequest.class ||
                                    parameters[i].getType() == HttpServletResponse.class) {
                                paramClassNameMap.put(parameters[i].getType().getSimpleName(), i);
                                continue;
                            } else {
                                RequestParam requestParamAnnotation = parameters[i].getAnnotation(RequestParam.class);
                                if (requestParamAnnotation != null) {
                                    String paramName = requestParamAnnotation.value();
                                    paramClassNameMap.put(paramName, i);
                                    continue;
                                }

                                RequestBody requestBodyAnnotation = parameters[i].getAnnotation(RequestBody.class);
                                if (requestBodyAnnotation != null) {
                                    paramClassNameMap.put(RequestBody.class.getSimpleName(), i);
                                }
                            }

                        }

                        paramClassNameMap.forEach(new BiConsumer<String, Integer>() {
                            @Override
                            public void accept(String s, Integer integer) {
                                System.out.println(s + integer);
                            }
                        });
                        urlMapping.put(url, handler);
                    }
                }
            }
        });
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 请求的路径
        // http://localhost:8080/post_friend_war_exploded/3324?name=hmj
        // requestURI = /post_friend_war_exploded /3324
        //  request.getRequestURI();


        String requestURI = request.getRequestURI().substring(contextPathLength);

        Handler handler = urlMapping.get(requestURI);
        if (handler == null) {
            // 没有匹配的处理器
            response.getWriter().write("404 Not Found");
            return;
        }

        // 检测请求方法
        String method = request.getMethod();
        RequestMode requestMode = handler.getRequestMode();
        if (!requestMode.name().equals(method)) {
            response.getWriter().write("405 Method Not Allowed");
            return;
        }

        // 获得request的param
        Map<String, String[]> requestParameterMap = request.getParameterMap();

        // 方法需要的参数
        Map<String, Integer> paramClassNameMap = handler.getParamClassNameMap();
        // 准备存入参数列表
        Object[] paramObjects = new Object[paramClassNameMap.size()];


        // 准备参数
        requestParameterMap.forEach(new BiConsumer<String, String[]>() {
            @Override
            public void accept(String key, String[] strings) {
                // 拼接前端传来的string 比如复选框那些多个String的内容
                StringBuffer stringBuffer = new StringBuffer();
                for (String s : strings) {
                    stringBuffer.append(s);
                    stringBuffer.append(",");
                }
                String value = stringBuffer.substring(0, stringBuffer.length() - 1);

                // 如果方法需要的参数中没有当前遍历的这个参数
                if (!paramClassNameMap.containsKey(key)) {
                    return;
                }

                // 准备参数
                paramObjects[paramClassNameMap.get(key)] = value;
            }
        });

        // 插入请求和响应
        if (paramClassNameMap.containsKey(HttpServletRequest.class.getSimpleName())) {
            paramObjects[paramClassNameMap.get(HttpServletRequest.class.getSimpleName())] = request;
        }
        if (paramClassNameMap.containsKey(HttpServletResponse.class.getSimpleName())) {
            paramObjects[paramClassNameMap.get(HttpServletResponse.class.getSimpleName())] = response;
        }

        // 封装请求体
        if (paramClassNameMap.containsKey(RequestBody.class.getSimpleName())){
            int requestBodyIndex = paramClassNameMap.get(RequestBody.class.getSimpleName());
            Parameter parameter = handler.getMethod().getParameters()[requestBodyIndex];
            Class<?> paramType = parameter.getType();

            // 获得请求体参数
            BufferedReader reader = request.getReader();

            StringBuffer json = new StringBuffer();
            String temp;
            while ((temp = reader.readLine())!=null){
                json.append(temp);
            }
            Object obj = JSON.parseObject(json.toString(), paramType);
            paramObjects[requestBodyIndex] = obj;
        }


        try {
            String result = (String) handler.invoke(paramObjects);
            Response annotation = handler.getMethod().getAnnotation(Response.class);
            if (annotation != null) {
                PrintWriter out = response.getWriter();
                out.print(result);
                out.flush();
                return;
            }
            internalResourceViewResolver.render(result, request, response);
            return;
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
