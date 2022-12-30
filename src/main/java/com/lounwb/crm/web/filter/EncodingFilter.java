package com.lounwb.crm.web.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

/**
 * 解决乱码过滤器
 * @author Lounwb
 * @version 1.0
 */
@WebFilter("*.do")
public class EncodingFilter implements Filter {
    @Override
    public void doFilter(ServletRequest req, ServletResponse rep, FilterChain chain) throws IOException, ServletException {
        //过滤post请求中文参数乱码
        req.setCharacterEncoding("UTF-8");
        //过滤响应流响应中文乱码
        rep.setContentType("text/html;charset=utf-8");

        //请求放行
        chain.doFilter(req, rep);
    }
}
