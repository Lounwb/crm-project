package com.lounwb.crm.workbench.service;

import com.lounwb.crm.vo.PaginationVO;
import com.lounwb.crm.workbench.domain.Activity;

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
}
