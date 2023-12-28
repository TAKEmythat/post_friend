package com.cwy.post_friend.service.impl;

import com.cwy.post_friend.bean.User;
import com.cwy.post_friend.dao.UserDao;
import com.cwy.post_friend.frame.annotation.aop.*;
import com.cwy.post_friend.frame.annotation.injection.RealBean;
import com.cwy.post_friend.frame.annotation.ordinary.Service;
import com.cwy.post_friend.frame.enum_.InsertMethod;
import com.cwy.post_friend.service.UserService;

/**
 * @Classname UserServiceImpl
 * @Description TODO
 * @Author stomach medicine
 * @Version 1.0.0
 * @Create 2023-12-24 14:10
 * @Since 1.0.0
 */

@Service
@Journal
public class UserServiceImpl implements UserService {
    @RealBean("UserDao")
    protected UserDao userDao;

    @Transaction
    @AOPCatch("com.cwy.post_friend.service.impl.UserServiceImpl.test2")
    @AOPStart("com.cwy.post_friend.service.impl.UserServiceImpl.test0")
    @AOPEnd("com.cwy.post_friend.service.impl.UserServiceImpl.test1")
    @Override
    public void register(int i) {
        System.out.println("正在注册用户");
        System.out.println("userDao = " + userDao);
        Object o = userDao.deleteUserByID(1);
        System.out.println("i = " + o);
    }

    public void test0(String a, String b) {
        System.out.println("Start");
    }

    public void test1() {
        System.out.println("End");
    }

    public void test2(InsertMethod i, Service s) {
        System.out.println("我报错了");
    }
}
