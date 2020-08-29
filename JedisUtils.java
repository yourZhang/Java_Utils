package com.travel.utils;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.ResourceBundle;

/*
 *   jedis工具类
 * */
public class JedisUtils {

    private static JedisPool jedisPool;

    private static String host;
    private static Integer port;
    private static Integer maxTotal;
    private static Integer maxIdle;

    // 初始化连接池
    static {
        try {

//            ResourceBundle jedis = ResourceBundle.getBundle("jedis");


            // 读取配置文件给变量赋值
            // 获取类加载读取 jedis.properties 获取io流
            InputStream is = JedisUtils.class.getClassLoader().getResourceAsStream("jedis.properties");
            // 创建properties对象 加载io流
            Properties properties = new Properties();
            properties.load(is);
            // 给变量赋值
            host = properties.getProperty("jedis.host");
            port = Integer.parseInt(properties.getProperty("jedis.port"));
            maxTotal = Integer.parseInt(properties.getProperty("jedis.maxTotal"));
            maxIdle = Integer.parseInt(properties.getProperty("jedis.maxIdle"));

            // 创建连接池配置对象
            JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
            jedisPoolConfig.setMaxTotal(maxTotal);
            jedisPoolConfig.setMaxIdle(maxIdle);
            // 创建连接池对象
            jedisPool = new JedisPool(jedisPoolConfig, host, port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 提供获取jedis连接的方法
    public static Jedis getJedis() {
        return jedisPool.getResource();
    }
}
