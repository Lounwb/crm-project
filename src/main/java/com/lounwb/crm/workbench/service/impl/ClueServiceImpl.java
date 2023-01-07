package com.lounwb.crm.workbench.service.impl;

import com.lounwb.crm.utils.DateTimeUtil;
import com.lounwb.crm.utils.SqlSessionUtil;
import com.lounwb.crm.utils.UUIDUtil;
import com.lounwb.crm.workbench.dao.*;
import com.lounwb.crm.workbench.domain.*;
import com.lounwb.crm.workbench.service.ClueService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Author 北京动力节点
 */
public class ClueServiceImpl implements ClueService {

    //线索相关表
    private ClueDao clueDao = SqlSessionUtil.getSqlSession().getMapper(ClueDao.class);
    private ClueActivityRelationDao clueActivityRelationDao = SqlSessionUtil.getSqlSession().getMapper(ClueActivityRelationDao.class);
    private ClueRemarkDao clueRemarkDao = SqlSessionUtil.getSqlSession().getMapper(ClueRemarkDao.class);


    @Override
    public boolean save(Clue c) {
        boolean flag = true;

        int count = clueDao.save(c);

        if (count != 1) {
            flag = false;
        }
        return flag;
    }

    @Override
    public Clue detail(String id) {
        Clue c = clueDao.detail(id);
        return c;
    }
}






























