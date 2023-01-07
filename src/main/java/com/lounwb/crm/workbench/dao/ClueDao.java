package com.lounwb.crm.workbench.dao;


import com.lounwb.crm.workbench.domain.Clue;

public interface ClueDao {

    int save(Clue c);

    Clue detail(String id);
}
