package com.lounwb.crm.settings.web.controller;

import com.lounwb.crm.settings.domain.User;
import com.lounwb.crm.settings.service.UserService;
import com.lounwb.crm.settings.service.impl.UserServiceImpl;
import com.lounwb.crm.utils.MD5Util;
import com.lounwb.crm.utils.PrintJson;
import com.lounwb.crm.utils.ServiceFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 *  用户登录页
 * @author Lounwb
 * @version 1.0
 */
@WebServlet({"/settings/user/login.do","/settings/user/delete.do"})
public class UserController extends HttpServlet {

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String path = request.getServletPath();
        if ("/settings/user/login.do".equals(path)) {
            login(request, response);
        }else if("/settings/user/delete.do".equals(path)){

        }
    }
    private void login(HttpServletRequest request, HttpServletResponse response){
        //{"loginAct" : loginAct,"loginPwd" : loginPwd}
        String loginAct = request.getParameter("loginAct");
        String loginPwd = request.getParameter("loginPwd");
        //将密码明文形式转换成MD5密文形式
        loginPwd = MD5Util.getMD5(loginPwd);
        //接受浏览器端的ip地址
        String ip = request.getRemoteAddr();
        //未来业务层开发，统一使用代理类形态的接口对象
        UserService us = (UserService) ServiceFactory.getService(new UserServiceImpl());
        try{
            User user = us.login(loginAct, loginPwd,ip);
            request.getSession().setAttribute("user", user);
            //data {"success":true}
            PrintJson.printJsonFlag(response, true);
        }catch (Exception e){
            //走到这里说明业务层验证登录失败,Controller抛出异常
            String msg = e.getMessage();
            //Controller处理需要给ajax提供信息
            /*
                //{"success":true,"msg":"密码错误"}
                两种处理手段
                1.使用Map,然后转换成JSON串
                2.创建VO(信息类，一串信息)
                    private boolean success;
                    private String msg;
                选择：对于展现的信息将来还会大量使用，就创建VO类，使用方便
                    如果对于展现的信息只有在这个需求中使用，只使用Map即可
            */
            Map<String, Object> map = new HashMap<>();
            map.put("success", false);
            map.put("msg", msg);
            PrintJson.printJsonObj(response, map);
        }
    }
}
