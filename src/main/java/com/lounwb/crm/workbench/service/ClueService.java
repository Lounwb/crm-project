package com.lounwb.crm.workbench.service;

import com.lounwb.crm.workbench.domain.Clue;
import com.lounwb.crm.workbench.domain.Tran;

import java.util.Map;

/**
 * @author Lounwb
 */
public interface ClueService {

    boolean save(Clue c);

    Clue detail(String id);

    boolean unbind(String id);

    boolean bind(String cid, String[] aids);

    boolean convert(String clueId, Tran t, String createBy);
}
