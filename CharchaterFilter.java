package com.travel.web.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 解决全站乱码问题，处理所有的请求
 */
/*
 * 放行注册，登录，手机号姓名验证
 * */
@WebFilter(value = "/*", initParams = {@WebInitParam(name = "whiteName", value = "register,SmsGetCode,LoginByCode,getLoginCode,LoginByPwd,SmsGetCode,findByPhone,findByUsername")})
public class CharchaterFilter implements Filter {
    //=白名单
    String[] split;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        String whiteName = filterConfig.getInitParameter("whiteName");
        split = whiteName.split(",");
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse rep, FilterChain filterChain) throws IOException, ServletException {

        //获取接口请求参数的action
//        String action = request.getParameter("action");

        //将父接口转为子接口
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) rep;
        //获取请求方法
        String method = request.getMethod();
        System.out.println(method + "请求方式===============================================================================================");
        //解决post请求中文数据乱码问题
        if (method.equalsIgnoreCase("post")) {
            request.setCharacterEncoding("utf-8");
        }
        System.out.println("编码过滤");
        //处理响应乱码
        response.setContentType("application/json;charset=utf-8");
        filterChain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }
}
