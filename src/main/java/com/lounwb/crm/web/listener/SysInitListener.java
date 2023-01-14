package com.lounwb.crm.web.listener;


import com.lounwb.crm.settings.domain.DicValue;
import com.lounwb.crm.settings.service.DicService;
import com.lounwb.crm.settings.service.impl.DicServiceImpl;
import com.lounwb.crm.utils.ServiceFactory;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@WebListener
public class SysInitListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent event) {
        ServletContext application = event.getServletContext();

        DicService ds = (DicService) ServiceFactory.getService(new DicServiceImpl());
        /*
            需要7个List,
            map.put("appellationList",dvList1);
            map.put("clueStateList",dvList2);
            ...
         */
        Map<String, List<DicValue>> map = ds.getAll();
        for(String key:map.keySet()){
            application.setAttribute(key,map.get(key));
        }

        //处理Stage2Possibility.properties文件
        //解析文件
        ResourceBundle bundle = ResourceBundle.getBundle("Stage2Possibility");
        Enumeration<String> keys = bundle.getKeys();
        //将文件中键值对关系处理成java中的键值对关系 Map<stage:String, possibility:String> pMap
        Map<String, String> pMap = new HashMap<>();
        while (keys.hasMoreElements()) {
            String key = keys.nextElement();
            String value = bundle.getString(key);

            pMap.put(key, value);
        }
        application.setAttribute("pMap", pMap);
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {

    }
}
