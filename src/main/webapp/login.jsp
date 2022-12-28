<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";
%>
<!DOCTYPE html>
<html>
<head>
	<base href="<%=basePath%>">
<meta charset="UTF-8">
<link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet" />
<script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
<script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
	<script>
		$(function () {
			//页面加载完毕后，自动聚焦用户名文本块
			$("#loginAct").focus()
			//为登录按钮绑定事件，执行登陆操作
			$("#submitBtn").click(function () {
				login()
			})
			//为密码输入框绑定敲键盘事件
			//event:获取到用户敲的键

			$("#loginPwd,#loginAct").keydown(function (event){
				//在密码框按下回车
				//如果未输入用户名则焦点聚焦到用户名输入框
				//并且提示 请输入用户名
				if (event.keyCode == 13) {
					if("" == $("#loginAct").val()){
						$("#msg").html("请输入用户名")
						$("#loginAct").focus()
					}else if("" == $("#loginPwd").val()){
						$("#msg").html("请输入密码")
						$("#loginPwd").focus()
					}else {
						//验证登录
						login()
					}
				}
			})
		})
		//验证登录
		function login() {
			//获取账号密码
			//将文本中的左右空格去掉，使用$.trim()
			var loginAct = $.trim($("#loginAct").val())
			var loginPwd = $.trim($("#loginPwd").val())

			if (loginAct == "" || loginPwd == "") {
				$("#msg").html("用户名密码不能为空")
				return false
			}
			//后台验证登录操作
			$.ajax({
				type : "POST",
				url : "settings/user/login.do",
				dataType : "json",
				data : {
					"loginAct" : loginAct,
					"loginPwd" : loginPwd
				},
				success : function (data) {
					//data {"success":true}
					//登陆成功跳转到工作台的欢迎页
					if (data.success) {
						window.location.href = "workbench/index.jsp"
					}else {
						$("#msg").html(data.msg)
					}
					//登录失败
					//data {"success":true,"msg":"密码错误"}
				}
			})
		}
	</script>
</head>
<body>
	<div style="position: absolute; top: 0px; left: 0px; width: 60%;">
		<img src="image/IMG_7114.JPG" style="width: 100%; height: 90%; position: relative; top: 50px;">
	</div>
	<div id="top" style="height: 50px; background-color: #3C3C3C; width: 100%;">
		<div style="position: absolute; top: 5px; left: 0px; font-size: 30px; font-weight: 400; color: white; font-family: 'times new roman'">CRM &nbsp;<span style="font-size: 12px;">&copy;2017&nbsp;动力节点</span></div>
	</div>
	<div style="position: absolute; top: 120px; right: 100px;width:450px;height:400px;border:1px solid #D5D5D5">
		<div style="position: absolute; top: 0px; right: 60px;">
			<div class="page-header">
				<h1>登录</h1>
			</div>
			<form action="workbench/index.jsp" class="form-horizontal" role="form">
				<div class="form-group form-group-lg">
					<div style="width: 350px;">
						<input class="form-control" type="text" placeholder="用户名" id="loginAct">
					</div>
					<div style="width: 350px; position: relative;top: 20px;">
						<input class="form-control" type="password" placeholder="密码" id="loginPwd">
					</div>
					<div class="checkbox"  style="position: relative;top: 30px; left: 10px;">
							<span id="msg" style="color: red"></span>
					</div>
					<!--
						注意：按钮写在form表单中，默认的行为就是提交表单
							一定要将按钮的类型设置为button
							按钮所触发的行为应该是由我们自己手动写js代码来决定
					-->
					<button type="button" id="submitBtn" class="btn btn-primary btn-lg btn-block"  style="width: 350px; position: relative;top: 45px;">登录</button>
				</div>
			</form>
		</div>
	</div>
</body>
</html>