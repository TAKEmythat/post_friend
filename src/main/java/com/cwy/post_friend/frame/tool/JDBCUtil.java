package com.cwy.post_friend.frame.tool;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @Classname JDBCUtil
 * @Description TODO
 * @Author stomach medicine
 * @Version 1.0.0
 * @Create 2023-12-26 15:20
 * @Since 1.0.0
 */

public class JDBCUtil {
    private static final ThreadLocal<Connection> threadLocalConnections = new ThreadLocal<>();

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private JDBCUtil() {
    }

    public static Connection getConnection() throws SQLException {
        Connection connection = threadLocalConnections.get();
        if (connection == null) {
            threadLocalConnections.set(
                    JDBCUtil.getConnection("jdbc:mysql://localhost:3306/post_friend", "root",
                    "123456c"));
            connection = threadLocalConnections.get();
        }
        return connection;
    }

    public static Connection getConnection(String url, String username, String password) throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }
}
