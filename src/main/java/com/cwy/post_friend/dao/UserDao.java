package com.cwy.post_friend.dao;

import com.cwy.post_friend.bean.User;
import com.cwy.post_friend.frame.annotation.ordinary.Dao;

/**
 * @Classname UserDao
 * @Description TODO
 * @Author stomach medicine
 * @Version 1.0.0
 * @Create 2023-12-24 14:34
 * @Since 1.0.0
 */

@Dao
public interface UserDao {
    Integer insertUser(Integer id, String name);

    Integer deleteUserByID(int id);

    Integer update(int id, User user);

    Object selectByID(int id);
}
