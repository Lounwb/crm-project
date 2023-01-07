package com.lounwb.crm.workbench.service;

import com.lounwb.crm.workbench.domain.Clue;

/**
 * @author Lounwb
 */
public interface ClueService {

    boolean save(Clue c);

    Clue detail(String id);
}
