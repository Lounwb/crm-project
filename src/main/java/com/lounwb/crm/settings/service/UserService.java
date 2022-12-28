package com.lounwb.crm.settings.service;

import com.lounwb.crm.exceptions.LoginException;
import com.lounwb.crm.settings.domain.User;

public interface UserService {
    User login(String loginAct, String loginPwd, String ip) throws LoginException;
}
