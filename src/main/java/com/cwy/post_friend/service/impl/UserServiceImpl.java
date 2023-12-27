package com.cwy.post_friend.service.impl;

import com.cwy.post_friend.dao.UserDao;
import com.cwy.post_friend.frame.annotation.injection.RealBean;
import com.cwy.post_friend.frame.annotation.ordinary.Service;
import com.cwy.post_friend.service.UserService;

/**
 * @Classname UserServiceImpl
 * @Description TODO
 * @Author stomach medicine
 * @Version 1.0.0
 * @Create 2023-12-24 14:10
 * @Since 1.0.0
 */

//@Service
public class UserServiceImpl implements UserService {
    @RealBean("UserDao")
    private UserDao userDao;

    @Override
    public void register() {
        System.out.println("正在注册用户");
        Object i = userDao.deleteUserByID(1);
        System.out.println("i = " + i);
    }
}
