package com.lounwb.crm.workbench.test;

import com.lounwb.crm.utils.ServiceFactory;
import com.lounwb.crm.utils.UUIDUtil;
import com.lounwb.crm.workbench.domain.Activity;
import com.lounwb.crm.workbench.service.ActivityService;
import com.lounwb.crm.workbench.service.impl.ActivityServiceImpl;
import org.junit.Assert;
import org.junit.Test;

public class ActivityTest {
    @Test
    public void testSave(){
        //单元测试测业务层逻辑
        Activity a = new Activity();
        a.setId(UUIDUtil.getUUID());
        a.setName("宣传推广会");
        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        Assert.assertEquals(true, as.save(a));
    }
}
