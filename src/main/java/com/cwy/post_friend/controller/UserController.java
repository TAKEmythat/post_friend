package com.cwy.post_friend.controller;

import com.cwy.post_friend.frame.annotation.injection.RealBean;
import com.cwy.post_friend.frame.annotation.ordinary.Controller;
import com.cwy.post_friend.frame.annotation.reponse.Response;
import com.cwy.post_friend.frame.annotation.request.RequestMapping;
import com.cwy.post_friend.service.UserService;

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
public class UserController {
    @RealBean("UserServiceImpl")
    private UserService userService;

    @Response()
    public String index() {
        userService.register();
        return "a";
    }
}
