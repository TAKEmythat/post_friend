package com.cwy.post_friend.frame.tool;

import com.cwy.post_friend.frame.exception.RollbackTransactionException;
import com.cwy.post_friend.frame.exception.SubmitTransactionException;

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
            threadLocalConnections.set(getDefaultConnection());
            connection = threadLocalConnections.get();
        }
        return connection;
    }

    public static Connection getConnection(String url, String username, String password) throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }

    public void openTransaction() throws SQLException {
        Connection connection = threadLocalConnections.get();
        if (connection == null) {
            threadLocalConnections.set(JDBCUtil.getDefaultConnection());
            connection = threadLocalConnections.get();
        }
        connection.setAutoCommit(false);
    }

    public void submitTransaction() throws SubmitTransactionException, SQLException {
        Connection connection = threadLocalConnections.get();
        if (connection == null) {
            throw new SubmitTransactionException("事务提交错误");
        }
        connection.commit();
    }

    public void rollbackTransaction() throws RollbackTransactionException, SQLException {
        Connection connection = threadLocalConnections.get();
        if (connection == null) {
            throw new RollbackTransactionException("事务回滚错误");
        }
        connection.rollback();
    }

    private static Connection getDefaultConnection() throws SQLException {
        return JDBCUtil.getConnection(
                "jdbc:mysql://localhost:3306/post_friend", "root",
                "123456c");
    }
}
