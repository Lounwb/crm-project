package com.lounwb.crm.workbench.dao;

import com.lounwb.crm.workbench.domain.Customer;

public interface CustomerDao {

    Customer getCustomerByName(String company);

    int save(Customer customer);
}
