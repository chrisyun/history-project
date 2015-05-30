<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>Session TimeOut</title>
    
	<style type="text/css">
	   	body {
			background-color: #D2E0F1;
		}
		
		#sessionOut {
            margin-top: 50px;
			padding: 15px 50px;
			width: 500px;
			font-size : 13pt;
			border: 1px solid black;
			background-color: yellow;
			text-align: center;
		}
		
		a{
			font-weight:bold;
			font-family:"宋体";
			font-size:18px;
		}

    </style>

  </head>
  
  <body>
  	<table align="center" width="100%">
  		<tr><td align="center"> 
  			<div id ="sessionOut">
				会话超时，系统自动超时退出<br>
				请重新<a href="<%=basePath %>">登录</a>系统！
			</div>
    	</td></tr>
  	</table>
  </body>
  
<script type="text/javascript">

if (self != top){
	window.top.location = window.location;
}

</script>
</html>
