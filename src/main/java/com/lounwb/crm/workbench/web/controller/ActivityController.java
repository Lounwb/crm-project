package com.lounwb.crm.workbench.web.controller;

import com.lounwb.crm.settings.domain.User;
import com.lounwb.crm.settings.service.UserService;
import com.lounwb.crm.settings.service.impl.UserServiceImpl;
import com.lounwb.crm.utils.*;
import com.lounwb.crm.vo.PaginationVO;
import com.lounwb.crm.workbench.domain.Activity;
import com.lounwb.crm.workbench.domain.ActivityRemark;
import com.lounwb.crm.workbench.service.ActivityService;
import com.lounwb.crm.workbench.service.impl.ActivityServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 市场活动控制器,负责处理市场活动相关业务
 * @author Lounwb
 * @version 1.0
 */

@WebServlet({"/workbench/activity/getUserList.do",
        "/workbench/activity/save.do",
        "/workbench/activity/pageList.do",
        "/workbench/activity/delete.do",
        "/workbench/activity/getUserListAndActivity.do",
        "/workbench/activity/update.do",
        "/workbench/activity/detail.do",
        "/workbench/activity/getRemarkListByAid.do",
        "/workbench/activity/deleteRemark.do",
        "/workbench/activity/saveRemark.do",
        "/workbench/activity/updateRemark.do"})
public class ActivityController extends HttpServlet {


    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String path = request.getServletPath();

        if ("/workbench/activity/getUserList.do".equals(path)) {
            getUserList(request, response);
        }else if("/workbench/activity/save.do".equals(path)){
            save(request, response);
        }else if("/workbench/activity/pageList.do".equals(path)){
            pageList(request, response);
        }else if("/workbench/activity/delete.do".equals(path)){
            delete(request, response);
        }else if("/workbench/activity/getUserListAndActivity.do".equals(path)){
            getUserListAndActivity(request, response);
        }else if("/workbench/activity/update.do".equals(path)){
            update(request, response);
        }else if("/workbench/activity/detail.do".equals(path)){
            detail(request, response);
        }else if("/workbench/activity/getRemarkListByAid.do".equals(path)){
            getRemarkListByAid(request, response);
        }else if("/workbench/activity/deleteRemark.do".equals(path)){
            deleteRemark(request, response);
        }else if("/workbench/activity/saveRemark.do".equals(path)){
            saveRemark(request, response);
        }else if("/workbench/activity/updateRemark.do".equals(path)){
            updateRemark(request, response);
        }
    }

    private void updateRemark(HttpServletRequest request, HttpServletResponse response) {
        /*
         "id" : id,
        "noteContent" : $.trim($("#noteContent").val())
        */
        String id = request.getParameter("id");
        String noteContent = request.getParameter("noteContent");
        String editTime = DateTimeUtil.getSysTime();
        String editBy = ((User) request.getSession().getAttribute("user")).getName();
        String editFlag = "1";

        ActivityRemark ar = new ActivityRemark();

        ar.setId(id);
        ar.setNoteContent(noteContent);
        ar.setEditTime(editTime);
        ar.setEditBy(editBy);
        ar.setEditFlag(editFlag);

        ActivityService ac = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        boolean flag = ac.updateRemark(ar);
        //{"success":true,"ar":{备注}}
        Map<String, Object> map = new HashMap<>();
        map.put("success", flag);
        map.put("ar", ar);
        PrintJson.printJsonObj(response, map);
    }

    private void saveRemark(HttpServletRequest request, HttpServletResponse response) {
        /*
        "noteContent" : $.trim($("#remark").val()),
					"activityId" : "${a.id}"
         */
        String noteContent = request.getParameter("noteContent");
        String activityId = request.getParameter("activityId");
        String id = UUIDUtil.getUUID();
        String createTime = DateTimeUtil.getSysTime();
        String createBy = ((User) request.getSession().getAttribute("user")).getName();
        String editFlag = "0";

        ActivityRemark ar = new ActivityRemark();
        ar.setNoteContent(noteContent);
        ar.setActivityId(activityId);
        ar.setId(id);
        ar.setCreateTime(createTime);
        ar.setCreateBy(createBy);
        ar.setEditFlag(editFlag);

        ActivityService ac = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        boolean flag = ac.saveRemark(ar);
        //{"success":true,"ar":{备注}}
        Map<String, Object> map = new HashMap<>();
        map.put("success",flag);
        map.put("ar", ar);
        PrintJson.printJsonObj(response, map);
    }

    private void deleteRemark(HttpServletRequest request, HttpServletResponse response) {
        String id = request.getParameter("id");
        ActivityService ac = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        boolean flag = ac.deleteRemark(id);
        PrintJson.printJsonFlag(response, flag);
    }

    private void getRemarkListByAid(HttpServletRequest request, HttpServletResponse response) {
        String activityId = request.getParameter("activityId");
        ActivityService ac = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        List<ActivityRemark> arList = ac.getRemarkListByAid(activityId);
        PrintJson.printJsonObj(response, arList);
    }

    private void detail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id = request.getParameter("id");
        ActivityService ac = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        Activity a  = ac.detail(id);
        request.setAttribute("a", a);
        request.getRequestDispatcher("/workbench/activity/detail.jsp").forward(request, response);
    }

    private void update(HttpServletRequest request, HttpServletResponse response) {
        /*
            {
                "id" : $.trim($("#edit-id").val()),
                "owner" : $.trim($("#edit-owner").val()),
                "name" : $.trim($("#edit-name").val()),
                "startDate" : $.trim($("#edit-startDate").val()),
                "endDate" : $.trim($("#edit-endDate").val()),
                "cost" : $.trim($("#edit-cost").val()),
                "description" : $.trim($("#edit-description").val())
            }
         */
        String id = request.getParameter("id");
        String owner = request.getParameter("owner");
        String name = request.getParameter("name");
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        String cost = request.getParameter("cost");
        String description = request.getParameter("description");
        String editTime = DateTimeUtil.getSysTime();
        String editBy = ((User)request.getSession().getAttribute("user")).getName();

        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        Activity a = new Activity();
        a.setId(id);
        a.setOwner(owner);
        a.setName(name);
        a.setStartDate(startDate);
        a.setEndDate(endDate);
        a.setCost(cost);
        a.setDescription(description);
        a.setEditTime(editTime);
        a.setEditBy(editBy);

        boolean flag = as.update(a);

        PrintJson.printJsonFlag(response, flag);
    }

    private void getUserListAndActivity(HttpServletRequest request, HttpServletResponse response) {
        String id = request.getParameter("id");
        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        Map<String, Object> map = as.getUserListAndActivity(id);
        PrintJson.printJsonObj(response, map);
    }

    private void delete(HttpServletRequest request, HttpServletResponse response) {
        String[] ids = request.getParameterValues("id");
        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        boolean flag = as.delete(ids);
        PrintJson.printJsonFlag(response, flag);
    }

    private void pageList(HttpServletRequest request, HttpServletResponse response) {
        String name = request.getParameter("name");
        String owner = request.getParameter("owner");
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        String pageNoStr = request.getParameter("pageNo");
        String pageSizeStr = request.getParameter("pageSize");
        //页数
        int pageNo = Integer.valueOf(pageNoStr);
        //每页展示的记录数
        int pageSize = Integer.valueOf(pageSizeStr);
        //计算出略过的记录数
        int skipCount = (pageNo - 1) * pageSize;

        Map<String, Object> map = new HashMap<>();
        map.put("name", name);
        map.put("owner", owner);
        map.put("startDate", startDate);
        map.put("endDate", endDate);
        map.put("skipCount", skipCount);
        map.put("pageSize", pageSize);

        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        PaginationVO<Activity> vo = as.pageList(map);
        PrintJson.printJsonObj(response, vo);
    }

    private void save(HttpServletRequest request, HttpServletResponse response) {
        /*
            {
                "owner" : $.trim($("#create-owner").val()),
                "name" : $.trim($("#create-name").val()),
                "startDate" : $.trim($("#create-startDate").val()),
                "endDate" : $.trim($("#create-endDate").val()),
                "cost" : $.trim($("#create-cost").val())
            }
         */
        String id = UUIDUtil.getUUID();
        String owner = request.getParameter("owner");
        String name = request.getParameter("name");
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        String cost = request.getParameter("cost");
        String description = request.getParameter("description");
        String createTime = DateTimeUtil.getSysTime();
        String createBy = ((User)request.getSession().getAttribute("user")).getName();

        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        Activity activity = new Activity();
        activity.setId(id);
        activity.setOwner(owner);
        activity.setName(name);
        activity.setStartDate(startDate);
        activity.setEndDate(endDate);
        activity.setCost(cost);
        activity.setDescription(description);
        activity.setCreateTime(createTime);
        activity.setCreateBy(createBy);

        boolean flag = as.save(activity);

        PrintJson.printJsonFlag(response, flag);
    }

    private void getUserList(HttpServletRequest request, HttpServletResponse response) {
        UserService us = (UserService) ServiceFactory.getService(new UserServiceImpl());
        List<User> uList = us.getUserList();

        PrintJson.printJsonObj(response, uList);
    }


}
