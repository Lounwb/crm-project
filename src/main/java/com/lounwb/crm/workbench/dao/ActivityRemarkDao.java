package com.lounwb.crm.workbench.dao;

/**
 * 市场活动备注Dao
 * @author Lounwb
 * @version 1.0
 */
public interface ActivityRemarkDao {
    int getCountByAids(String[] ids);

    int deleteByAids(String[] ids);
}
