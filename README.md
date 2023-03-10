## 基于Servlet+Mybatis+Bootstrap的客户关系管理系统CRM

### 逻辑介绍

- 用户可以通过市场活动来获取潜在客户,潜在客户可能在不久的将来为用户所在公司提供一笔利益，这笔利益在项目中称为交易(商机)
- 将一名潜在客户定义成一个线索，这是本项目的核心业务，潜在客户可以与公司取得联系，成为公司的伙伴，在这个过程中，潜在客户就变成了一位联系人，而潜在客户所在的公司，就成为了本公司的客户，进行贸易往来，而在线索转换中，也可以同时建立一笔交易，因为往往潜在客户都是通过第一笔交易而成为公司的客户的
- 在市场活动、线索、联系人、客户、交易的详细页面中，均有对应的备注，用户可在相应页面进行适当备注（类似于备忘录）



### 功能介绍

1、市场活动

- 完善了市场活动的全部细节，包括市场活动的添加、修改、删除（支持批量）、查询（支持模糊查询，多条件动态查询），数据的分页展示，市场备注的增删改

2、线索

- 实现了创建线索，多条件动态查询线索，分页展示，修改和删除等功能与市场活动大体一致，就没有实现了，大体思路是差不多的，不过在删线索的时候应该连带着删除线索与它对应市场活动的关系。
- 线索与市场活动的关联在线索详细页中，通过搜索市场活动的名称(模糊查询)进行关联

- 说明一下线索模块的转换细节
  - 前台传给后台一个线索的id，可以查询到具体的线索信息
  - 根据该条线索可以取得客户名（公司名），若为空，那么新添客户
  - 一个客户（公司）可以有多个联系人，故也应向数据库中新添联系人（联系人是在线索创建的时候写入的，故在线索写入时，需要向数据库中查询是否有该联系人的姓名，若已有该姓名，那么此条线索不能创建），这里发现了b站视频教程中的一个bug，原来视频中是没有这条逻辑的，那么如果新添了两条线索，联系人的姓名均是aaa的话，那么数据库中就会出现两条大致一样的记录，仅uuid不同）
  - 线索转换后会被删除，连带着线索的备注一同删除，此时需要将线索的备注转换为联系人备注和客户的备注
  - 将线索与市场活动的关系转换为联系人和市场活动的关系（多对多的转换），因为一个线索，可能同时来自多个市场活动（在线索详细页面可以关联市场活动）,而一个市场活动，也会对应着许多条线索，因此是多对多关系。
  - 若有交易记录，那么建立一笔交易，同时建立一条交易历史，（可在交易信息详细页中查看）
  - 删除线索备注、删除线索和市场活动的关系，删除该条线索。

3、交易

- 在交易详细页中，用户可点击图标变更交易阶段（资质审查,需求分析...成交,丢失的线索,因竞争丢失），当时跟着老师敲，变更逻辑均写在jsp页面中，以达到异步刷新的效果，而这样写代码繁多，后期也不易于维护，我觉得更好的方式是将9个交易阶段写死，点击变更图标的时候向后台发请求，然后重新刷新页面，这样省事多了。
- 在交易详细页下方可查看该笔交易从创建以来的各个阶段历史信息。



## 应该注意的细节

- 对于数据字典的处理（即一些形式固定，不会改动的数据，而页面展示经常要用到的，放入全局作用域中存储），相当于一个缓存机制吧。

  - ```
    com.scnu.crm.web.listener.SystemInitListener
    public class SystemInitListener implements ServletContextListener
    web容器中,servlet,filter,listener都不是spring容器管理的,因此无法通过注入的方式获取对象
    如:
        @Autowired
        private DicService ds;
    采用这种方式,项目启动的时候就会报错
    原因:
        Listener的生命周期是由servlet容器(Tomcat)管理的,项目启动的时候SystemInitListener是由servlet容器实例化并调用
        contextInitialized(ServletContextEvent sce)方法,但servlet容器不认识@Autoried注解,因此报错
    
    但此处需要将数据字典放在ServletContext里面,就采取最原始的方法,先得到spring容器,然后通过getBean来获取
    (WebApplicationContextUtils.getWebApplicationContext(application)).getBean(xxx.class)
    ```

- 设置拦截器后，拦截器会拦截静态资源(js,css,html,jpg)等，而不拦截jsp，在springmvc.xml中配置拦截器解决了静态资源的拦截问题

  - ```xml
    <mvc:interceptors>
        <mvc:interceptor>
            <mvc:mapping path="/**"/>
            <mvc:exclude-mapping path="/settings/user/login.do"/>
            <mvc:exclude-mapping path="/**/*.js"/>
            <mvc:exclude-mapping path="/**/*.css"/>
            <mvc:exclude-mapping path="/**/*.png"/>
            <mvc:exclude-mapping path="/**/*.jpg"/>
            <!--声明拦截器对象-->
            <bean class="com.scnu.crm.interceptor.LoginInterceptor"/>
        </mvc:interceptor>
    </mvc:interceptors>
    ```

