package com.spring.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * 事务管理器:
 *    开启事务
 *    提交事务
 *    回滚操作
 *    关闭连接，释放资源
 */
@Component("transactionManager")
public class TransactionManager {

    @Autowired
    ConnectionUtils connectionUtils;

    //开启事务
    public void beginTransaction() throws SQLException {
        Connection connection = connectionUtils.getConnection();
        connection.setAutoCommit(false);
    }
    //提交事务
    public void commit() throws SQLException {
        connectionUtils.getConnection().commit();
    }
    //回滚事务
    public void rollback() {
        try {
            connectionUtils.getConnection().rollback();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    //关闭事务
    public void close() {
        try {
            connectionUtils.getConnection().close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
