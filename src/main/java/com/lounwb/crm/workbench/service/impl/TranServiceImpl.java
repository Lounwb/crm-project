package com.lounwb.crm.workbench.service.impl;

import com.lounwb.crm.utils.DateTimeUtil;
import com.lounwb.crm.utils.SqlSessionUtil;
import com.lounwb.crm.utils.UUIDUtil;
import com.lounwb.crm.workbench.dao.CustomerDao;
import com.lounwb.crm.workbench.dao.TranDao;
import com.lounwb.crm.workbench.dao.TranHistoryDao;
import com.lounwb.crm.workbench.domain.Customer;
import com.lounwb.crm.workbench.domain.Tran;
import com.lounwb.crm.workbench.domain.TranHistory;
import com.lounwb.crm.workbench.service.TranService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Lounwb
 */
public class TranServiceImpl implements TranService {
    private TranDao tranDao = SqlSessionUtil.getSqlSession().getMapper(TranDao.class);
    private TranHistoryDao tranHistoryDao = SqlSessionUtil.getSqlSession().getMapper(TranHistoryDao.class);
    private CustomerDao customerDao = SqlSessionUtil.getSqlSession().getMapper(CustomerDao.class);

    @Override
    public boolean save(Tran t, String customerName) {
        boolean flag = true;
        Customer customer = customerDao.getCustomerByName(customerName);
        if(customer == null){
            customer = new Customer();
            customer.setId(UUIDUtil.getUUID());
            customer.setName(customerName);
            customer.setCreateBy(t.getCreateBy());
            customer.setCreateTime(DateTimeUtil.getSysTime());
            customer.setContactSummary(t.getContactSummary());
            customer.setNextContactTime(t.getNextContactTime());
            customer.setOwner(t.getOwner());
            //添加客户
            int count1 = customerDao.save(customer);
            if (count1 != 1) {
                flag = false;
            }
        }
        t.setCustomerId(customer.getId());
        int count2 = tranDao.save(t);
        if (count2 != 1) {
            flag = false;
        }
        TranHistory history = new TranHistory();
        history.setId(UUIDUtil.getUUID());
        history.setTranId(t.getId());
        history.setStage(t.getStage());
        history.setMoney(t.getMoney());
        history.setExpectedDate(t.getExpectedDate());
        history.setCreateTime(DateTimeUtil.getSysTime());
        history.setCreateBy(t.getCreateBy());
        int count3 = tranHistoryDao.save(history);
        if (count3 != 1) {
            flag = false;
        }
        return flag;
    }

    @Override
    public Tran detail(String id) {
        Tran t = tranDao.detail(id);

        return t;
    }

    @Override
    public List<TranHistory> getHistoryListByTranId(String tranId) {
        List<TranHistory> thList = tranHistoryDao.getHistoryListByTranId(tranId);
        return thList;
    }

    @Override
    public boolean changeStage(Tran t) {
        boolean flag = true;

        //改变交易阶段
        int count1 = tranDao.changeStage(t);
        if(count1!=1){

            flag = false;

        }
        //交易阶段改变后，生成一条交易历史
        TranHistory th = new TranHistory();
        th.setId(UUIDUtil.getUUID());
        th.setCreateBy(t.getEditBy());
        th.setCreateTime(DateTimeUtil.getSysTime());
        th.setExpectedDate(t.getExpectedDate());
        th.setMoney(t.getMoney());
        th.setTranId(t.getId());
        //添加交易历史
        int count2 = tranHistoryDao.save(th);
        if(count2!=1){

            flag = false;

        }

        return flag;
    }

    @Override
    public Map<String, Object> getCharts() {
        //取得total
        int total = tranDao.getTotal();

        //取得dataList
        List<Map<String,Object>> dataList = tranDao.getCharts();

        //将total和dataList保存到map中
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("total", total);
        map.put("dataList", dataList);

        //返回map
        return map;
    }
}
