package com.lounwb.crm.workbench.dao;


import com.lounwb.crm.workbench.domain.TranHistory;

import java.util.List;

public interface TranHistoryDao {
    int save(TranHistory history);

    List<TranHistory> getHistoryListByTranId(String tranId);
}
