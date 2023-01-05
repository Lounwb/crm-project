package com.lounwb.crm.workbench.dao;

import com.lounwb.crm.workbench.domain.ActivityRemark;

import java.util.List;

/**
 * 市场活动备注Dao
 * @author Lounwb
 * @version 1.0
 */
public interface ActivityRemarkDao {
    int getCountByAids(String[] ids);

    int deleteByAids(String[] ids);

    List<ActivityRemark> getRemarkListByAid(String activityId);

    int deleteById(String id);

    int saveRemark(ActivityRemark ar);

    int updateRemark(ActivityRemark ar);
}
