package com.lounwb.crm.workbench.test;

import com.lounwb.crm.utils.ServiceFactory;
import com.lounwb.crm.utils.SqlSessionUtil;
import com.lounwb.crm.workbench.dao.ClueActivityRelationDao;
import com.lounwb.crm.workbench.dao.ClueDao;
import com.lounwb.crm.workbench.domain.Clue;
import com.lounwb.crm.workbench.domain.ClueActivityRelation;
import com.lounwb.crm.workbench.service.ClueService;
import com.lounwb.crm.workbench.service.impl.ClueServiceImpl;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class ClueTest {
    private ClueActivityRelationDao clueActivityRelationDao = SqlSessionUtil.getSqlSession().getMapper(ClueActivityRelationDao.class);
    @Test
    public void testGetClueById(){
        String clueId = "7d1d569aec4e4d45a6a51c669e27b498";
        ClueDao clueDao = SqlSessionUtil.getSqlSession().getMapper(ClueDao.class);
        Clue c = clueDao.getById(clueId);

        Assert.assertEquals("qd.com", c.getEmail());
    }
    @Test
    public void testGETCustomerByName(){
        String name = "";
    }
    @Test
    public void testSaveCustomer(){

    }
    @Test
    public void testGetListByClueId(){
        String clueId = "24927dca2fe14cc3b5da0727827e3469";
        List<ClueActivityRelation> list = clueActivityRelationDao.getListByClueId(clueId);
        for (ClueActivityRelation clueActivityRelation : list) {
            System.out.println(clueActivityRelation.getActivityId());
        }
    }
}
