package com.lounwb.crm.web.filter;

import com.lounwb.crm.settings.domain.User;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 检验是否登录，访问内部资源如果未登录则跳转到登录页面
 * @author Lounwb
 * @version 1.0
 */
@WebFilter({"*.do","*.jsp"})
public class LoginFilter implements Filter {
    @Override
    public void doFilter(ServletRequest req, ServletResponse rep, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) rep;

        String path = request.getServletPath();

        if ("/settings/user/login.do".equals(path) || "/login.jsp".equals(path)) {
            chain.doFilter(req, rep);
        }else {
            User user = (User) request.getSession().getAttribute("user");
            //如果user不为空,说明已登录
            if (user != null) {
                chain.doFilter(req, rep);
            }else {
                //重定向到登录页
                response.sendRedirect(request.getContextPath() + "/login.jsp");
            }
        }
    }
}
