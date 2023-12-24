package com.cwy.post_friend.frame.controller;

import com.cwy.post_friend.frame.annotation.request.RequestMapping;
import com.cwy.post_friend.frame.bean.Handler;
import com.cwy.post_friend.frame.factory.RequestMap;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.List;
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
@WebServlet(value = "/*", loadOnStartup = 3)
public class DispatcherServlet extends HttpServlet {
    private final HashMap<String,Handler> urlMapping = new HashMap<>();

    @Override
    public void init() throws ServletException {
//        获得map路径映射
        RequestMap requestMap = RequestMap.newInstance();
//        注册路径映射
        registerMap(requestMap);
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
                Class<?> clazz = o.getClass();

//              遍历方法 拿到注解RequestMapping上的路径
                Method[] declaredMethods = clazz.getDeclaredMethods();
                for (Method declaredMethod : declaredMethods) {
                    RequestMapping requestMappingAnnotation = declaredMethod.getAnnotation(RequestMapping.class);
                    if (requestMappingAnnotation != null) {
                        String url_part2 = requestMappingAnnotation.value();
                        String url = url_part1 + url_part2;

                        Handler handler = new Handler(o, declaredMethod);
//                        用于参数和位置的map
                        List<String> paramClassNameList = handler.getParamClassNameList();

                        Parameter[] parameters = declaredMethod.getParameters();
                        for (int i = 0; i < parameters.length; i++) {
                            paramClassNameList.add(parameters.getClass().getSimpleName());
                        }

                        urlMapping.put(url,handler);
                    }
                }
            }
        });
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//     请求的路径
//        http://localhost:8080/post_friend_war_exploded/3324?name=hmj
//        path = /3324
        String path = request.getPathInfo();

        Handler handler = urlMapping.get(path);


    }
}
