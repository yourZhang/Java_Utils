package com.spring.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Component("connectionUtils")
public class ConnectionUtils {

    @Autowired
    private DataSource dataSource;

    //缓存当前数据库连接，让业务层及持久层共享一个Connection连接对象
    private ThreadLocal<Connection> map = new ThreadLocal<Connection>();

    //获取数据库连接
    public Connection getConnection() throws SQLException {
        Connection connection = map.get();
        /**
         * 或数据库连接对象，如果没有则从连接池中获取一个新的
         * 如果有，直接返回
         */
        if (connection == null){
            connection = dataSource.getConnection();
            //要防止多次获取连接对象
            map.set(connection);
        }
        return connection;
    }
}
