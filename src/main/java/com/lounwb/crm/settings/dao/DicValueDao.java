package com.lounwb.crm.settings.dao;

import com.lounwb.crm.settings.domain.DicValue;

import java.util.List;

/**
 * @author Lounwb
 */
public interface DicValueDao {
    List<DicValue> getListByCode(String code);
}
