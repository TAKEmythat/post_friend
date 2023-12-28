package com.cwy.post_friend.frame.tool;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.cwy.post_friend.frame.exception.RollbackTransactionException;
import com.cwy.post_friend.frame.exception.SubmitTransactionException;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.logging.Logger;

/**
 * @Classname JDBCUtil
 * @Description TODO
 * @Author stomach medicine
 * @Version 1.0.0
 * @Create 2023-12-26 15:20
 * @Since 1.0.0
 */

public class JDBCUtil {
    private static final ThreadLocal<Connection> threadLocalConnections =
            new ThreadLocal<>();
    private static final DataSource dataSource;
    private static final Logger logger = Logger.getLogger("com.cwy.post_friend.frame.tool.JDBCUtil");

    static {
        try {
            Properties properties = new Properties();
            properties.load(Thread.currentThread().
                    getContextClassLoader().getResourceAsStream("druid/druid.properties"));
            ResourceBundle resourceBundle = ResourceBundle.getBundle("druid.druid");
            dataSource = DruidDataSourceFactory.createDataSource(properties);
            Class.forName(resourceBundle.getString("druid.driverClassName"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private JDBCUtil() {
    }

    public static Connection getConnection() throws SQLException {
        Connection connection = dataSource.getConnection();
        threadLocalConnections.set(connection);
        return connection;
    }

    public static Connection getConnection(String url, String username, String password) throws SQLException {
        Connection connection = DriverManager.getConnection(url, username, password);
        threadLocalConnections.set(connection);
        return connection;
    }

    public static void openTransaction() {
        try {
            logger.info("开启事务");
            Connection connection = threadLocalConnections.get();
            if (connection == null) {
                connection = getConnection();
            }
            threadLocalConnections.set(connection);
            connection.setAutoCommit(false);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void submitTransaction() {
        try {
            logger.info("提交事务");
            Connection connection = threadLocalConnections.get();
            if (connection == null) {
                throw new SubmitTransactionException("事务提交错误");
            }
            connection.commit();
        } catch (SQLException | SubmitTransactionException e) {
            throw new RuntimeException(e);
        }
    }

    public static void rollbackTransaction() {
        try {
            logger.info("事务回滚");
            Connection connection = threadLocalConnections.get();
            if (connection == null) {
                throw new RollbackTransactionException("事务回滚错误");
            }
            connection.rollback();
        } catch (RollbackTransactionException | SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
