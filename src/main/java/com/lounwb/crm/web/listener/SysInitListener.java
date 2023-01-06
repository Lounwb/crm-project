package com.lounwb.crm.web.listener;


import com.lounwb.crm.settings.domain.DicValue;
import com.lounwb.crm.settings.service.DicService;
import com.lounwb.crm.settings.service.impl.DicServiceImpl;
import com.lounwb.crm.utils.ServiceFactory;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.util.List;
import java.util.Map;

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
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {

    }
}
