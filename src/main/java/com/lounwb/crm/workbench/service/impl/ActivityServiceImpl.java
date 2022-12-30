package com.lounwb.crm.workbench.service.impl;

import com.lounwb.crm.utils.SqlSessionUtil;
import com.lounwb.crm.workbench.dao.ActivityDao;
import com.lounwb.crm.workbench.domain.Activity;
import com.lounwb.crm.workbench.service.ActivityService;

public class ActivityServiceImpl implements ActivityService {
    private ActivityDao activityDao = SqlSessionUtil.getSqlSession().getMapper(ActivityDao.class);


    @Override
    public boolean save(Activity activity) {
        boolean flag = true;

        int count = activityDao.save(activity);

        if(count != 1){
            flag = false;
        }

        return flag;
    }
}
