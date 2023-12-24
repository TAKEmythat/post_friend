package com.cwy.post_friend.dao.impl;

import com.cwy.post_friend.bean.User;
import com.cwy.post_friend.dao.UserDao;
import com.cwy.post_friend.frame.annotation.ordinary.Dao;

/**
 * @Classname UserDaoImpl
 * @Description TODO
 * @Author stomach medicine
 * @Version 1.0.0
 * @Create 2023-12-24 22:08
 * @Since 1.0.0
 */

@Dao
public class UserDaoImpl implements UserDao {
    @Override
    public int insertUser(User user) {
        return 0;
    }

    @Override
    public int deleteUserByID(int id) {
        return 0;
    }

    @Override
    public int update(int id, User user) {
        return 0;
    }

    @Override
    public User selectByID(int id) {
        return null;
    }
}
