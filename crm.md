# crm项目笔记

## 项目约定

1. 开发顺序

   前端人员开发原型html→修改成为jsp页面→请求到Controller→交给Service代理去做→调用Dao层(dao是动态代理)→编写后端Mapper

2. 常见标识

```javascript
/*
	add/create:跳转到添加页，或者打开添加操作的模态窗口
	save:执行添加操作
	edit:跳转到修改页，或者打开修改操作的模态窗口
	update:执行修改操作
	get:执行查询操作	find/select/query/...
	特殊操作 login等
*/
```

## 项目中出现的问题

### 登录验证模块

1. 解决前端拿不到登录异常信息，但是后端能正常抛出异常的问题

   ```java
   try{
       session = SqlSessionUtil.getSqlSession();
       obj = method.invoke(target, args);
       session.commit();
   }catch(Exception e){
       session.rollback();
       e.printStackTrace();
       //处理的是什么异常，继续往上抛什么异常
       throw e.getCause();
   }finally{
       SqlSessionUtil.myClose(session);
   }
   ```

2. 123

### 市场活动模块

1. 解决目前在市场活动页，当关闭服务器后重新点击活动页会出现小窗内加载登录页面的问题

   ![](https://raw.githubusercontent.com/Lounwb/imgbed-picgo-repo/master/blogimg/202212291748578.png)

   ```javascript
   //解决原理：如果当前登录页面不是顶层，则设置为顶层
   if(window.top != window){
   	window.top.location = window.location
   }
   ```

2. 解决在javascript代码中编写el表达式失效问题

   因为el表达式需要使用$符号，而jQeury库中定义 $ = jQuery，所以不能直接使用

   ```javascript
   //取得当前登录用户的id
   //在js中使用el表达式，el表达式一定要套用在字符串中
   var id = "${user.id}"
   $("#create-owner").val(id)
   
   /*
   	想要实现默认选中的功能使用，使用$("#grade").val(2)即可
   	<select id='grade'>
   		<option val='1'>本科</option>
   		<option val='2'>硕士</option>
   		<option val='3'>博士</option>
   	</select>
   */
   ```

3. 解决创建市场活动时已经保存一次活动后重新打开会保存原来的数据

   ![](https://raw.githubusercontent.com/Lounwb/imgbed-picgo-repo/master/blogimg/202212301206273.png)

   ```javascript
   //清空添加操作模态窗口中的数据
   //提交表单
   //$("#activityAddForm").submit();
   
   /*
   注意：
   我们拿到了form表单的jquery对象
   对于表单的jquery对象，提供了submit()方法让我们提交表单
   但是表单的jquery对象，没有为我们提供reset()方法让我们重置表单（坑：idea为我们提示了有reset()方法）
   虽然jquery对象没有为我们提供reset方法，但是原生js为我们提供了reset方法
   所以我们要将jquery对象转换为原生dom对象
   jquery对象转换为dom对象：jquery对象[下标]
   dom对象转换为jquery对象：$(dom)
   */
   $("#activityAddForm")[0].reset();
   
   //关闭添加操作的模态窗口
   $("#createActivityModal").modal("hide");
   ```

   

## 项目开发中的设计方法

### 登录验证模块

### 市场活动模块

1. 业务逻辑处理过程中，如果出现问题应当使用抛出自定义异常的形式

   ![](https://raw.githubusercontent.com/Lounwb/imgbed-picgo-repo/master/blogimg/202212301210537.png)

   ```java
   ...
   int count = activityDao.save(activity);
   
   if(count != 1){
       throw new FailedInsertException("活动创建失败");
   }
   
   ....
   ```

2. 对于关系型数据库，做前端分页相关操作的基础组件是pageNo和pageSize

   pageNo:页码

   pageSize:每页展示的记录数

   ​		
