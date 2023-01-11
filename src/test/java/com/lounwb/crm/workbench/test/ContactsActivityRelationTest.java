package com.lounwb.crm.workbench.test;

import com.lounwb.crm.utils.SqlSessionUtil;
import com.lounwb.crm.utils.UUIDUtil;
import com.lounwb.crm.workbench.dao.ContactsActivityRelationDao;
import com.lounwb.crm.workbench.domain.ContactsActivityRelation;
import org.junit.Assert;
import org.junit.Test;

public class ContactsActivityRelationTest {
    private ContactsActivityRelationDao contactsActivityRelationDao = SqlSessionUtil.getSqlSession().getMapper(ContactsActivityRelationDao.class);
    @Test
    public void testSave(){
        ContactsActivityRelation contactsActivityRelation = new ContactsActivityRelation();
        contactsActivityRelation.setId(UUIDUtil.getUUID());
        contactsActivityRelation.setActivityId(UUIDUtil.getUUID());
        contactsActivityRelation.setId(UUIDUtil.getUUID());
        contactsActivityRelationDao.save(contactsActivityRelation);
        //Assert.assertEquals(1, );
    }
}
