package com.lounwb.crm.workbench.service.impl;

import com.lounwb.crm.utils.SqlSessionUtil;
import com.lounwb.crm.vo.PaginationVO;
import com.lounwb.crm.workbench.dao.ActivityDao;
import com.lounwb.crm.workbench.dao.ActivityRemarkDao;
import com.lounwb.crm.workbench.domain.Activity;
import com.lounwb.crm.workbench.service.ActivityService;

import java.util.List;
import java.util.Map;

public class ActivityServiceImpl implements ActivityService {
    private ActivityDao activityDao = SqlSessionUtil.getSqlSession().getMapper(ActivityDao.class);
    private ActivityRemarkDao activityRemarkDao = SqlSessionUtil.getSqlSession().getMapper(ActivityRemarkDao.class);


    @Override
    public boolean save(Activity activity) {
        boolean flag = true;

        int count = activityDao.save(activity);

        if(count != 1){
            flag = false;
        }

        return flag;
    }

    @Override
    public PaginationVO<Activity> pageList(Map<String, Object> map) {
        //取total
        int total = activityDao.getTotalByCondition(map);
        //取dataList
        List<Activity> dataList = activityDao.getActivityListByCondition(map);
        //将total和pageList封装到vo中
        PaginationVO<Activity> vo = new PaginationVO<>();
        vo.setTotal(total);
        vo.setDataList(dataList);
        //返回vo
        return vo;
    }

    @Override
    public boolean delete(String[] ids) {
        boolean flag = true;
        //查询出需要删除的备注的数量
        int count1 = activityRemarkDao.getCountByAids(ids);
        //删除备注，返回实际删除的数量
        int count2 = activityRemarkDao.deleteByAids(ids);
        //比对
        if(count1 != count2){
            return false;
        }
        //删除市场活动
        int count3 = activityDao.delete(ids);

        if(count3 != ids.length){
            return false;
        }
        return flag;
    }

}
