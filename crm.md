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
   $("#createActivityModal").modal("hide");4.
   ```


4. [未解决]根据信息条件查询市场活动时出现一条记录也没有的问题

   复制老师的源代码后bug没了，不同点在于老师的startDate和endDate没有模糊查询（确实不能用模糊查询）但是把老师的代码加上模糊查询后依旧正常能用，但是对比自己和老师的代码后除了这点其余没有任何区别。

   ![](https://raw.githubusercontent.com/Lounwb/imgbed-picgo-repo/master/blogimg/202212311825211.png)

   ```xml
   <select id="getTotalByCondition" resultType="int">
           select
               count(*)
           from
               tbl_activity a
           join
               tbl_user u
           on
               a.owner = u.id
           <where>
               <if test="name != null and name != ''">
                   a.name like '%' #{name} '%'
               </if>
               <if test="owner != null and owner != ''">
                   and u.name like '%' #{owner} '%'
               </if>
               <if test="startDate != null and startDate != ''">
                   and a.startDate &gt; '%' #{startDate} '%'
               </if>
               <if test="endDate != null and endDate != ''">
                   and a.endDate &lt; '%' #{endDate} '%'
               </if>
           </where>
   </select>
       <select id="getActivityListByCondition" resultType="Activity">
           select
               a.id,
               a.name,
               u.name as owner,
               a.startDate,
               a.endDate
           from
               tbl_activity a
           join
               tbl_user u
           on
               a.owner = u.id
           <where>
               <if test="name != null and name != ''">
                   a.name like '%' #{name} '%'
               </if> 
               <if test="owner != null and owner != ''">
                   and u.name like '%' #{owner} '%'
               </if>
               <if test="startDate != null and startDate != ''">
                   and a.startDate &gt; '%'#{startDate} '%'
               </if>
               <if test="endDate != null and endDate != ''">
                   and a.endDate &lt; '%' #{endDate} '%'
               </if>
           </where>
           order by
               a.createTime desc
           limit
               #{skipCount},#{pageSize}
    <select>
   ```

5. 查询市场活动时，在查询栏中输入条件后，不执行查询而点击下一页会自动带入条件查询

   ![](https://raw.githubusercontent.com/Lounwb/imgbed-picgo-repo/master/blogimg/202212311944828.png)

   ```html
   <!-- 解决方法：使用隐藏域 -->
   <input type="hidden" id="hidden-name"/>
   <input type="hidden" id="hidden-owner"/>
   <input type="hidden" id="hidden-startDate"/>
   <input type="hidden" id="hidden-endDate"/>
   ```

   ```javascript
   //存到隐藏域中
   $("#hidden-name").val($("#search-name").val)
   $("#hidden-owner").val($("#search-owner").val)
   $("#hidden-startDate").val($("#search-startDate").val)
   $("#hidden-endDate").val($("#search-endDate").val)
   
   //查询前，将隐藏域中保存的信息取出来，重新赋予到搜索框中
   $("#search-name").val($.trim($("#hidden-name").val()))
   $("#search-owner").val($.trim($("#hidden-owner").val()))
   $("#search-startDate").val($.trim($("#hidden-startDate").val()))
   $("#search-endDate").val($.trim($("#hidden-endDate").val()))
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

3. VO和Map的选择

   当后端信息复用率高使用VO，新建一个VO类，复用率低使用Map

 4. 动态SQL，条件查询可能只是用一部分的条件，所以不能查询语句不能全部写上

    ```xml
    <select id="getActivityListByCondition" resultType="com.lounwb.crm.workbench.domain.Activity">
            select
                a.id,
                a.name,
                u.name as owner,
                a.startDate,
                a.endDate
            from
                tbl_activity a
            join
                tbl_user u
            on
                a.owner = u.id
            <where>
                <if test="name != null and name != ''">
                    a.name like '%'#{name}'%'
                </if>
                <if test="owner != null and owner != ''">
                    and u.name like '%'#{owner}'%'
                </if>
            </where>
    </select>
    ```


5. 动态生成的元素，是不能够以普通绑定事件的形式来进行操作

![](https://raw.githubusercontent.com/Lounwb/imgbed-picgo-repo/master/blogimg/202212312004497.png)

```javascript
//以下这种做法是不行的
		/*$("input[name=xz]").click(function () {

			alert(123);

		})*/

		//因为动态生成的元素，是不能够以普通绑定事件的形式来进行操作的
		/*

			动态生成的元素，我们要以on方法的形式来触发事件

			语法：
				$(需要绑定元素的有效的外层元素).on(绑定事件的方式,需要绑定的元素的jquery对象,回调函数)

		 */
$("#activityBody").on("click",$("input[name=xz]"),function () {

    		$("#qx").prop("checked",$("input[name=xz]").length==$("input[name=xz]:checked").length);

})
```
