package com.lounwb.crm.workbench.service;

import com.lounwb.crm.vo.PaginationVO;
import com.lounwb.crm.workbench.domain.Activity;
import com.lounwb.crm.workbench.domain.ActivityRemark;

import java.util.List;
import java.util.Map;

/**
 * @author Lounwb
 * @version 1.0
 */
public interface ActivityService {

    boolean save(Activity activity);

    PaginationVO<Activity> pageList(Map<String, Object> map);

    boolean delete(String[] ids);

    Map<String, Object> getUserListAndActivity(String id);

    boolean update(Activity a);

    Activity detail(String id);

    List<ActivityRemark> getRemarkListByAid(String activityId);

    boolean deleteRemark(String id);

    boolean saveRemark(ActivityRemark ar);

    boolean updateRemark(ActivityRemark ar);

    List<Activity> getActivityListByClueId(String clueId);

    List<Activity> getActivityListByNameAndNotByClueId(Map<String, String> map);

    List<Activity> getActivityListByName(String aname);

}
