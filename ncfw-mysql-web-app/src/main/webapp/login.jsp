<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
			
	String msg = "";
	String info = request.getParameter("info");
	if(info != null && info.equals("AuthenticationFailure")) {
		msg = (String) request.getSession().getAttribute("msg");
		if(msg == null || msg.equals("")) {
			msg = "用户名或者密码错误";
		}
	}
%>
<html xmlns="http://www.w3.org/1999/xhtml" lang="zh-CN">
	<head>
		<title>登录</title>
		<style type="text/css">
		html {
			border: 0;
			height: 100%;
		}
		
		body {
			margin: 0;
			padding: 0;
			height: 100%;
			background: #f9f9f9;
			font-size: 12px;
			font-family: Arial;
		}
		
		input {
			width: 160px;
			height: 18px;
			line-height: 18px;
			border: 1px solid #5e5e5e;
			padding: 3px;
		}
		
		.box {
			width: 100%;
			height: 100%;
			padding-top: 40px;
		}
		
		.box td {
			vertical-align: middle;
		}
		
		.cdiv {
			width: 1004px;
			height: 479px;
			margin: 0 auto;
			background: url(<%=basePath%>foundation/ext/images/bg.jpg) no-repeat center center;
			color: #fff;
			border: solid 2px #e5e5e5;
		}
		
		.cdiv .login {
			height: 100px;
			width: 280px;
			float: left;
			color: #000;
			font-weight: bold;
			padding: 200px 0 0 150px;
		}
		
		.cdiv .login a {
			color: #666666;
			text-decoration: underline;
			font-weight: normal;
		}
		
		.cdiv .foot {
			margin: 510px 0 0 0;
			text-align: center;
			color: #333333;
		}
		</style>
		
		<script type="text/javascript">
		function subForm() {
			var uname = document.getElementById("j_username").value;
			var pwd = document.getElementById("j_password").value;
			var code = document.getElementById("validateCode").value;	

			if(uname == null || uname == "") {
				alert("请输入用户名！");
				return ;
			} else if(pwd == null || pwd == "") {
				alert("请输入密码！");
				return ;
			} else if(code == null || code == "") {
				alert("请输入验证码！");
				return ;
			}

			document.getElementById('loginForm').submit();
		}

		function refCode() {
			document.getElementById('codeImg').src = '<%=basePath%>security/jcaptcha?random='+Math.random();
		}

		document.onkeydown = function(event){
			var event 	= event || window.event;
			var keyCode = event.keyCode;
			if (keyCode == 13) {	//回车键
				subForm();
			}
		}
		
		</script>
		
		
	</head>

	<body>
		<form method="post" id="loginForm" action="<%=path%>/j_spring_security_check">
			<table class="box">
				<tr>
					<td>
						<div class="cdiv">
							<div class="login">
								<table width="99%" border="0" cellspacing="14" cellpadding="0">
									<tr>
										<td align="right">
											用户名
										</td>
										<td align="left">
											<input type="text" name="j_username" id="j_username" />
										</td>
									</tr>
									<tr>
										<td align="right">
											密 &nbsp;&nbsp;&nbsp;&nbsp;码
										</td>
										<td align="left">
											<input type="password" name="j_password" id="j_password" />
										</td>
									</tr>
									<tr>
										<td align="right">
											验证码
										</td>
										<td align="left">
											<input type="text" name="validateCode" id="validateCode" style="width: 80px;"/>&nbsp;
											<img src="<%=basePath%>security/jcaptcha" width="70" align="middle" alt="看不清点击刷新"
												height="25" id="codeImg" onclick="refCode();" style="cursor: pointer;"/>
										</td>
									</tr>
									<tr>
										<td>
											&nbsp;
										</td>
										<td>
											<table width="100%" border="0" cellspacing="0"
												cellpadding="0">
												<tr>
													<td>
														<img src="<%=basePath%>foundation/ext/images/sub.gif" onclick="subForm();" width="56" height="20" style="cursor: pointer;" />
													</td>
													<td></td>
													<td>
														&nbsp;
													</td>
												</tr>
											</table>
										</td>
									</tr>
									<tr>
										<td>&nbsp;</td>
										<td style="color: red;"><%=msg %></td>
									</tr>
								</table>
							</div>

						</div>
					</td>
					</tr>
			</table>
		</form>
	</body>
</html>