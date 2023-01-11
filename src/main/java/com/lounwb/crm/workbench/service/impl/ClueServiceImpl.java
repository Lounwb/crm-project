package com.lounwb.crm.workbench.service.impl;

import com.lounwb.crm.utils.DateTimeUtil;
import com.lounwb.crm.utils.SqlSessionUtil;
import com.lounwb.crm.utils.UUIDUtil;
import com.lounwb.crm.workbench.dao.*;
import com.lounwb.crm.workbench.domain.*;
import com.lounwb.crm.workbench.service.ClueService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @author Lounwb
 */
public class ClueServiceImpl implements ClueService {

    //线索相关表
    private ClueDao clueDao = SqlSessionUtil.getSqlSession().getMapper(ClueDao.class);
    private ClueActivityRelationDao clueActivityRelationDao = SqlSessionUtil.getSqlSession().getMapper(ClueActivityRelationDao.class);
    private ClueRemarkDao clueRemarkDao = SqlSessionUtil.getSqlSession().getMapper(ClueRemarkDao.class);
    //客户相关表
    private CustomerDao customerDao = SqlSessionUtil.getSqlSession().getMapper(CustomerDao.class);
    private CustomerRemarkDao customerRemarkDao = SqlSessionUtil.getSqlSession().getMapper(CustomerRemarkDao.class);
    //联系人相关表
    private ContactsDao contactsDao = SqlSessionUtil.getSqlSession().getMapper(ContactsDao.class);
    private ContactsRemarkDao contactsRemarkDao = SqlSessionUtil.getSqlSession().getMapper(ContactsRemarkDao.class);
    private ContactsActivityRelationDao contactsActivityRelationDao = SqlSessionUtil.getSqlSession().getMapper(ContactsActivityRelationDao.class);
    //交易相关表
    private TranDao tranDao = SqlSessionUtil.getSqlSession().getMapper(TranDao.class);
    private TranHistoryDao tranHistoryDao = SqlSessionUtil.getSqlSession().getMapper(TranHistoryDao.class);
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

    @Override
    public boolean unbind(String id) {
        boolean flag = true;

        int count = clueActivityRelationDao.unbind(id);

        if(count != 1){
            flag = false;
        }

        return flag;
    }

    @Override
    public boolean bind(String cid, String[] aids) {
        boolean flag = true;
        for(String aid:aids){
            ClueActivityRelation car = new ClueActivityRelation();
            car.setId(UUIDUtil.getUUID());
            car.setClueId(cid);
            car.setActivityId(aid);

            int count = clueActivityRelationDao.bind(car);
            if(count != 1){
                flag = false;
            }
        }
        return flag;
    }

    @Override
    public boolean convert(String clueId, Tran t, String createBy) {
        boolean flag = true;
        String creatTime = DateTimeUtil.getSysTime();
        Clue clue = clueDao.getById(clueId);

        String company = clue.getCompany();

        //添加客户信息
        Customer customer = customerDao.getCustomerByName(company);
        if(customer == null){
            customer = new Customer();
            customer.setId(UUIDUtil.getUUID());
            customer.setAddress(clue.getAddress());
            customer.setWebsite(clue.getWebsite());
            customer.setPhone(clue.getPhone());
            customer.setOwner(clue.getOwner());
            customer.setNextContactTime(clue.getNextContactTime());
            customer.setName(company);
            customer.setDescription(clue.getDescription());
            customer.setCreateBy(clue.getCreateBy());
            customer.setCreateTime(creatTime);
            customer.setContactSummary(clue.getContactSummary());

            int count1 = customerDao.save(customer);
            if(count1 != 1){
                flag = false;
            }
        }
        //添加联系人信息
        Contacts contacts = new Contacts();
        contacts.setId(UUIDUtil.getUUID());
        contacts.setSource(clue.getSource());
        contacts.setOwner(clue.getOwner());
        contacts.setNextContactTime(clue.getNextContactTime());
        contacts.setMphone(clue.getMphone());
        contacts.setJob(clue.getJob());
        contacts.setFullname(clue.getFullname());
        contacts.setEmail(clue.getEmail());
        contacts.setDescription(clue.getDescription());
        contacts.setCustomerId(customer.getId());
        contacts.setCreateTime(creatTime);
        contacts.setCreateBy(createBy);
        contacts.setAppellation(clue.getAppellation());
        contacts.setAddress(clue.getAddress());

        int count2 = contactsDao.save(contacts);
        if(count2 != 1){
            flag = false;
        }

        List<ClueRemark> clueRemarkList = clueRemarkDao.getListByClueId(clueId);
        for (ClueRemark clueRemark : clueRemarkList) {
            String noteContent = clueRemark.getNoteContent();

            CustomerRemark customerRemark = new CustomerRemark();
            customerRemark.setId(UUIDUtil.getUUID());
            customerRemark.setCreateBy(createBy);
            customerRemark.setCreateTime(creatTime);
            customerRemark.setCustomerId(customer.getId());
            customerRemark.setEditBy("0");
            customerRemark.setNoteContent(noteContent);
            int count3 = customerRemarkDao.save(customer);
            if (count3 != 1) {
                flag = false;
            }

            ContactsRemark contactsRemark = new ContactsRemark();
            contactsRemark.setId(UUIDUtil.getUUID());
            contactsRemark.setCreateBy(createBy);
            contactsRemark.setCreateTime(creatTime);
            contactsRemark.setContactsId(contacts.getId());
            contactsRemark.setEditBy("0");
            contactsRemark.setNoteContent(noteContent);
            int count4 = contactsRemarkDao.save(customer);
            if (count4 != 1) {
                flag = false;
            }
        }
        //线索市场活动关系->联系人市场活动关系
        List<ClueActivityRelation> clueActivityRelationList = clueActivityRelationDao.getListByClueId(clueId);
        for (ClueActivityRelation clueActivityRelation : clueActivityRelationList) {
            String activityId = clueActivityRelation.getActivityId();

            ContactsActivityRelation contactsActivityRelation = new ContactsActivityRelation();
            contactsActivityRelation.setId(UUIDUtil.getUUID());
            contactsActivityRelation.setContactsId(contacts.getId());
            contactsActivityRelation.setActivityId(activityId);

            int count5 = contactsActivityRelationDao.save(contactsActivityRelation);

            if (count5 != 1) {
                flag = false;
            }
        }
        //创建交易
        if(t != null){
            t.setSource(clue.getSource());
            t.setOwner(clue.getOwner());
            t.setNextContactTime(clue.getNextContactTime());
            t.setDescription(clue.getDescription());
            t.setCustomerId(customer.getId());
            t.setContactsId(contacts.getId());
            int count6 = tranDao.save(t);
            if (count6 != 1) {
                flag = false;
            }
            //创建交易历史
            TranHistory history = new TranHistory();
            history.setId(UUIDUtil.getUUID());
            history.setCreateBy(createBy);
            history.setCreateTime(creatTime);
            history.setExpectedDate(t.getExpectedDate());
            history.setMoney(t.getMoney());
            history.setStage(t.getStage());
            history.setTranId(t.getId());

            int count7 = tranHistoryDao.save(history);
            if (count7 != 1) {
                flag = false;
            }
        }
        //删除线索备注
        for (ClueRemark clueRemark : clueRemarkList) {
            int count8 = clueRemarkDao.delete(clueRemark);
            if(count8 != 1){
                flag = false;
            }
        }
        //删除线索和市场的关联关系
        for (ClueActivityRelation clueActivityRelation : clueActivityRelationList) {
            int count9 = clueActivityRelationDao.delete(clueActivityRelation);
            if(count9 != 1){
                flag = false;
            }
        }
        //删除线索
        int count10 = clueDao.delete(clueId);
        if(count10 != 1){
            flag = false;
        }
        return flag;
    }


}