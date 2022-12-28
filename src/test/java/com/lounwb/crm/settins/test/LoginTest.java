package com.lounwb.crm.settins.test;

import com.lounwb.crm.utils.MD5Util;
import org.junit.Test;

public class LoginTest {

    @Test
    public void testSecurity(){
        String pwd = "#sj@!,vnx(2";
        String md5 = MD5Util.getMD5(pwd);
        System.out.println(md5);
    }
}
