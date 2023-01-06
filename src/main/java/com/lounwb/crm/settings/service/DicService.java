package com.lounwb.crm.settings.service;


import com.lounwb.crm.settings.domain.DicValue;

import java.util.List;
import java.util.Map;

/**
 * @author Lounwb
 */
public interface DicService {
    Map<String, List<DicValue>> getAll();
}
