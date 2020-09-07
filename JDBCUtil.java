package com.web.utils;


import java.sql.*;
import java.util.ResourceBundle;

/**
 * @program: study_java
 * @description:
 * @author: xiaozhang6666
 * @create: 2020-08-10 09:21
 **/
public class JDBCUtil {

    // 声明数据库配置变量
    private static String driver;
    private static String url;
    private static String username;
    private static String password;

    // 加载jdbc.properties配置文件，给变量赋值
    static {
        // SE阶段 new Properties.load();

        // sun公司提供，专门加载src目录下的properties类型文件，不需要手写扩展名
        ResourceBundle jdbc = ResourceBundle.getBundle("jdbc");
        driver = jdbc.getString("jdbc.driver");
        url = jdbc.getString("jdbc.url");
        username = jdbc.getString("jdbc.username");
        password = jdbc.getString("jdbc.password");
    }


    // 1.注册驱动【保证只注册一次】
    static {
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("注册驱动失败...");
        }
    }

    // 2.获取连接
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }

    // 3.释放资源
    public static void release(ResultSet resultSet, Statement statement, Connection connection) {
        // 关闭ResultSet
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        // 关闭Statement
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        // 关闭Connection
        if (connection != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

    // 方法重载
    public static void release(Statement statement, Connection connection) {
        release(null, statement, connection);
    }
}
