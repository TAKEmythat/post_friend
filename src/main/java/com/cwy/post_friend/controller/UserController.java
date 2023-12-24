package com.cwy.post_friend.controller;

import com.cwy.post_friend.frame.annotation.injection.RealBean;
import com.cwy.post_friend.frame.annotation.ordinary.Controller;
import com.cwy.post_friend.frame.annotation.reponse.Response;
import com.cwy.post_friend.frame.annotation.request.RequestMapping;
import com.cwy.post_friend.frame.annotation.request.RequestParam;
import com.cwy.post_friend.frame.controller.DispatcherServlet;
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

    @Response()
    @RequestMapping(value = "/index")
    public String index(HttpServletRequest request, HttpServletResponse response,@RequestParam("name") String name) {
        userService.register();
        return "a";
    }
}
