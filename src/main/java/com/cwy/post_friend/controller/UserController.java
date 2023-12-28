package com.cwy.post_friend.controller;

import com.cwy.post_friend.bean.User;
import com.cwy.post_friend.frame.annotation.injection.RealBean;
import com.cwy.post_friend.frame.annotation.ordinary.Controller;
import com.cwy.post_friend.frame.annotation.reponse.Response;
import com.cwy.post_friend.frame.annotation.request.RequestBody;
import com.cwy.post_friend.frame.annotation.request.RequestMapping;
import com.cwy.post_friend.frame.annotation.request.RequestParam;
import com.cwy.post_friend.frame.controller.DispatcherServlet;
import com.cwy.post_friend.frame.enum_.RequestMode;
import com.cwy.post_friend.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * @Classname UserController
 * @Description TODO
 * @Author stomach medicine
 * @Version 1.0.0
 * @Create 2023-12-24 13:46
 * @Since 1.0.0
 */

@Controller
@RequestMapping(value = "/user")
public class UserController extends DispatcherServlet {
    @RealBean("UserServiceImpl")
    private UserService userService;

    @RequestMapping(value = "/index",mode = RequestMode.POST)
    @Response
    public String index(HttpServletRequest request, HttpServletResponse response, @RequestBody User user, @RequestParam("name") String name) {
//        方法传参除了req和resp以外，需要写requestParam来获得request.getParameterMap的内容，不写则会报错
        System.out.println(request);
        System.out.println(response);
        System.out.println("name = " + name);
        System.out.println("user = " + user);
        request.setAttribute("user",user);
        userService.register(2);
        return "index";
    }
}
