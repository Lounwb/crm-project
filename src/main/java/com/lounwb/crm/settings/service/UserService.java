package com.lounwb.crm.settings.service;

import com.lounwb.crm.exceptions.LoginException;
import com.lounwb.crm.settings.domain.User;

import java.util.List;

public interface UserService {
    User login(String loginAct, String loginPwd, String ip) throws LoginException;

    List<User> getUserList();
}
