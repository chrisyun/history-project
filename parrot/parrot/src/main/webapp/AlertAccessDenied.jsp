<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    <title>Access Denied</title>
	<style>
	<!--
	body {
		background-color: #D2E0F1;
	}
	.error {
    	width: 400px;
    	height : 100px;
    	border: 1px solid black;
    	font-size : 13pt;
    	background-color: yellow;
    	text-align: left;
    	margin-top: 50px;
    	padding: 15px 50px;
	}
	-->
	</style>

  </head>
  
  <body>
  	<table align="center" width="100%">
  		<tr><td align="center"> 
  			<div class="error">
    			访问被拒绝：${requestScope['SPRING_SECURITY_403_EXCEPTION'].message}<br/>
    			您无权限访问本页面，请联系管理员为您分配相应的访问权限！
    		</div>
    	</td></tr>
  	</table>
   
  </body>
</html>
