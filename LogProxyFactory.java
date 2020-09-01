package com.travel.utils;


import org.apache.commons.io.FileUtils;

import java.io.File;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

/**
 * 目标需求 : 为业务层所有方法加入日志记录功能。要求记录方法执行时间，实参、抛出异常信息。并将信息输出到日志文件。
 * 需要记录的日志信息 :
 *    方法名称
 *    方法所在类
 *    方法执行时间
 *    方法参数
 *    方法中的异常
 *    将日志信息输出到文件中！
 * 采用什么技术 : 动态代理
 *     JDK动态代理
 *     CGLib动态代理
 *
 * 写代理对象生产的工厂LogProxyFactory
 */
public class LogProxyFactory {
    /**
     * 定义方法:创建一个目标对象的代理对象，并且对这个对象的所有的方法加入功能增强！
     *
     * 一个明确 : 这个方法要做什么事情
     *
     * 方法的三要素:
     *     方法名称 : createProxyObject
     *     方法参数 : 目标对象
     *     方法返回值 : 代理对象，增强后的对象
     */
    public static Object createProxyObject(Object target){
        //JDK 动态代理对象、
        /**
         * 参数：
         *    参数1 : loader  定义代理类的类加载器
         *    参数2 : interfaces  代理类要实现的接口列表
         *    参数3 : 指派方法调用的调用处理程序
         * 返回：
         * 一个带有代理类的指定调用处理程序的代理实例，它由指定的类加载器定义，并实现指定的接口
         */
        //参数1 : loader  定义代理类的类加载器
        ClassLoader classLoader = target.getClass().getClassLoader();
        //参数2 : interfaces  代理类要实现的接口列表
        Class<?>[] interfaces = target.getClass().getInterfaces();
        //参数3 : 指派方法调用的调用处理程序
        InvocationHandler handler = new InvocationHandler() {
            /**
             * 拦截器:拦截运行所有目标对的方法，加点料
             * @param proxy 目标类
             * @param method 目标类中的方法
             * @param args 方法参数
             * @return 方法的返回值
             */
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                /**
                 * 日志功能
                 *    方法名称
                 *    方法所在类
                 *    方法执行时间
                 *    方法参数
                 *    方法中的异常
                 *
                 *    将日志信息输出到文件中！
                 */
                //方法执行之前
                Object invoke = null;
                //方法名称
                String methodName = method.getName();
                //方法所在类
                String classname = target.getClass().getName();
                //方法执行时间
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:hh:ss");
                String time = sdf.format(new Date());
                //方法参数
                String argsString = Arrays.toString(args);
                //方法出了异常
                String exceptionMessage = "";
                try {

                    //调用方法
                    invoke = method.invoke(target, args);

                } catch (Exception e) {
                    //方法出了异常
                    exceptionMessage = e.getCause().getMessage();
                }
                //将日志信息输出到文件中！
                StringBuilder sb = new StringBuilder();
                sb.append("[方法所在类:" + classname + "]")
                        .append("[方法名称:" + methodName + "]")
                        .append("[方法执行时间:" + time + "]")
                        .append("[方法参数:" + argsString + "]")
                        .append("[方法异常信息:" + exceptionMessage + "]");

                //一天生成一个文本文件
                SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
                String time1 = sdf1.format(new Date());

                File file = new File("D:\\log\\log_" + time1 + ".log");
                FileUtils.writeStringToFile(file, sb.toString() + "\r\n", "utf-8", true);
                //方法执行之后
                return invoke;
            }
        };

        Object proxyInstance = Proxy.newProxyInstance(classLoader, interfaces, handler);
        //CGLib动态代理
        //Object proxy = Enhancer.create(null, null);
        return proxyInstance;
    }
}

