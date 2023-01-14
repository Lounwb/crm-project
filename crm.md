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

3. 实际项目开发中添加和修改的顺序

   在实际项目开发中，一定是按照先做添加，再做修改的这种顺序所以，为了节省开发时间，修改操作一般都是copy添加操作

4. 后端为前端提供数据的两种方式

   ![](https://raw.githubusercontent.com/Lounwb/imgbed-picgo-repo/master/blogimg/202301131810574.png)

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


6. 市场活动备注动态生成的删除按钮单击后无反应

![](https://raw.githubusercontent.com/Lounwb/imgbed-picgo-repo/master/blogimg/202301051603089.png)

```javascript
html += '<a onclick="deleteRemark('+n.id+')"></a>'

//更正后
onclick='<a onclick="deleteRemark(\''+n.id+'\')"></a>'
//(\''+n.id+'\') \'转义跟外层'配对，不然套在""里面会被当成字符串
//里面''是拼接字符串，是在""里面的字符串
```

**动态生成的元素所触发的方法，参数必须套用在字符串当中，即'onclick="deleteRemark(n.id)"'。**

7. 市场活动备注删除后内容追加的问题

​	![](https://raw.githubusercontent.com/Lounwb/imgbed-picgo-repo/master/blogimg/202301051622847.png)

​	因为市场活动备注展现的时候使用的是before方法，所以删除后调用showRemarkList()后会在原来的备注后面追加新查询的备注。所以说需要查询前需要清空

### 线索（潜在客户）模块

1. 在模态窗口中按下回车会自动刷新页面清空内容（默认行为）

​	

```javascript
$("#aname").keydown(function (event) {
	if(event.keyCode == 13){
        alert("查询并展现市场活动类表")
        //禁用模态窗口默认的回车行为
        return false;
	}
})
```

### 交易模块

1. [解决]Tomcat 9.0.70版本在监听器上使用ResourceBundle出现异常，导致无法正常启动服务器

   ```java
   //处理Stage2Possibility.properties文件
           //解析文件
   ResourceBundle bundle = ResourceBundle.getBundle("Stage2Possibility");
   Enumeration<String> keys = bundle.getKeys();
   //将文件中键值对关系处理成java中的键值对关系 Map<stage:String, possibility:String> pMap
   Map<String, String> pMap = new HashMap<>();
   while (keys.hasMoreElements()) {
      tring key = keys.nextElement();
      String value = bundle.getString(key);
      pMap.put(key, value);
   }
   application.setAttribute("pMap", pMap);
   ```

   使用Tomcat 10.0.x版本可以正常启动服务器，但是需要重构项目，所以没写。

   原因：idea资源文件不自动生成到classes里面

   过程：

   ​	早上启动服务器发现不能正常启动，因为昨天晚上写的内容没测试，所以考虑写的内容有Bug.(下图是报错信息)

   ​	![](https://raw.githubusercontent.com/Lounwb/imgbed-picgo-repo/master/blogimg/202301131325443.png)

   ​	百度后，csdn说我昨天没关服务器，今天打开服务器tomcat检测到两个线程冲突，所以需要关闭。关闭后仍然不能解决问题.

   ​	![](https://raw.githubusercontent.com/Lounwb/imgbed-picgo-repo/master/blogimg/202301131327615.png)

   查看Tomcat Localhost Log以后发现找不到我的properties，一开始还以为是tomcat9不支持，使用tomcat10以后能后正常使用，还以为是版本问题。结果百度了一下说classes里面没有resources文件，所以找不到。

   ![](https://raw.githubusercontent.com/Lounwb/imgbed-picgo-repo/master/blogimg/202301131330374.png)

   复制过来以后完美解决问题。

2. 

## 项目开发中的设计方法

### 登录验证模块

1. 数据库表设计的时候不使用主键自增

   原因：使用主键自增会降低速度，因为主键使用auto_increment，插入新字段的时候需要查询数据库表中最大的id，然后id+1最为新记录的id，所以涉及到查询会降低速度。

2. 数据库表设计的时候不使用外键而是约定外键

   如果使用假设B表的activity_id是外键关联A表的Id字段，如果b插入数据需要查询A表中是否有该id。由于涉及到查询，所以会降低速度。

   我们采用约定好外键的方式，就是说在B表中仍然有Activity_id字段，A表中有id字段，我们默认activity_id关联id，使用的时候只需要Join表即可

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

   - 

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
//或者可以直接触发事件的方式 <button onclick="...."></button>
```

6. 关于文本域textarea的使用

```html
<!--
关于文本域textarea：
（1）一定是要以标签对的形式来呈现,正常状态下标签对要紧紧的挨着
（2）textarea虽然是以标签对的形式来呈现的，但是它也是属于表单元素范畴
我们所有的对于textarea的取值和赋值操作，应该统一使用val()方法（而不是html()方法）
-->
<textarea class="form-control" rows="3" id="edit-description"></textarea>
<!-- 对于(1)不能出现<textarea .... />的形式 -->
<!-- 对于(1)不能出现<textarea>          </textarea>的形式 -->
<!-- 对于(2)textarea没有value属性，但是文本域中的内容用.val（）方法来拿->
```

7. 执行增加删除修改后展示记录条数

   ```javascript
   /*
   * $("#activityPage").bs_pagination('getOption', 'currentPage'):
   *     操作后停留在当前页
   *
   * $("#activityPage").bs_pagination('getOption', 'rowsPerPage')
   *     操作后维持已经设置好的每页展现的记录数
   *
   * 这两个参数不需要我们进行任何的修改操作
   *  直接使用即可
   *
   * */
   ```

8. 填充内容的多种使用方法

![](https://raw.githubusercontent.com/Lounwb/imgbed-picgo-repo/master/blogimg/202301051152147.png)

- 使用$("activityBody").html(html)
  - 缺点：如果activityBody里面还有其他元素则无法使用
- 获取前面的div，然后在其后面追加html，使用append
- 获取后面的div，然后在其前面追加html，使用before：$("#remarkDiv").before(html)

9. 日期拾取器(DataTimePicker)的使用

   在需要使用选择日期的input里面class 添加time

   ```javascript
   $(".time").datetimepicker({
       minView: "month",
       language:  'zh-CN',
       format: 'yyyy-mm-dd',
       autoclose: true,
       todayBtn: true,
       pickerPosition: "top-left"
   })
   ```

   

### 线索（潜在客户）模块

1. 在sql语句中不要在=左右加空格![](https://raw.githubusercontent.com/Lounwb/imgbed-picgo-repo/master/blogimg/202301091845295.png)

​	来源于alibaba的规范

### 交易模块

1. 自动联想和自动补全功能组件

   ```javascript
   $("#create-customerName").typeahead({
       source: function (query, process) {
           $.get(
               "workbench/transaction/getCustomerName.do",
               {"name": query},
               function (data) {
                   //alert(data);
                   /*
   						String..
   							data
   								[{客户名称1},{2},{3}]
   
   						 */
   
                   process(data);
               },
               "json"
           );
       },
       delay: 500
   });
   ```

2. 键值对的数据的使用

   ```javascript
   /*
   
   			关于阶段和可能性
   				是一种一一对应的关系
   				一个阶段对应一个可能性
   				我们现在可以将阶段和可能性想象成是一种键值对之间的对应关系
   				以阶段为key，通过选中的阶段，触发可能性value
   
   				stage               possibility
   				key					value
   				01资质审查			10
   				02需求分析			25
   				03价值建议			40
   				...
   				...
   				07成交				100
   				08..				0
   				09..				0
   
   				对于以上的数据，通过观察得到结论
   				（1）数据量不是很大
   				（2）这是一种键值对的对应关系
   
   				如果同时满足以上两种结论，那么我们将这样的数据保存到数据库表中就没有什么意义了
   				如果遇到这种情况，我们需要用到properties属性文件来进行保存
   				stage2Possibility.properties
   				01资质审查=10
   				02需求分析=20
   				....
   				
   				但是properties文件对中文支持很差，不能直接使用01资质审查=10，而是采用汉字转换成unicode码的方式来存储，也就是01\u8D44\u8D28\u5BA1\u67E5=10
   
   				stage2Possibility.properties这个文件表示的是阶段和键值对之间的对应关系
   				将来，我们通过stage，以及对应关系，来取得可能性这个值
   				这种需求在交易模块中需要大量的使用到
   
   				所以我们就需要将该文件解析在服务器缓存中
   				application.setAttribute(stage2Possibility.properties文件内容)
   
   
   		 */
   ```

   

   ```properties
   01\u8D44\u8D28\u5BA1\u67E5=10
   02\u9700\u6C42\u5206\u6790=25
   03\u4EF7\u503C\u5EFA\u8BAE=40
   04\u786E\u5B9A\u51B3\u7B56\u8005=60
   05\u63D0\u6848/\u62A5\u4EF7=80
   06\u8C08\u5224/\u590D\u5BA1=90
   07\u6210\u4EA4=100
   08\u4E22\u5931\u7684\u7EBF\u7D22=0
   09\u56E0\u7ADE\u4E89\u4E22\u5931\u5173\u95ED=0
   ```

   
