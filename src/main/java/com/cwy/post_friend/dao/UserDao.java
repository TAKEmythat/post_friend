package com.cwy.post_friend.dao;

import com.cwy.post_friend.frame.annotation.ordinary.Dao;

/**
 * @Classname UserDao
 * @Description TODO
 * @Author stomach medicine
 * @Version 1.0.0
 * @Create 2023-12-24 14:11
 * @Since 1.0.0
 */

@Dao
public interface UserDao {
    default void register() {
        System.out.println("数据库正在注册用户");
    }
}
