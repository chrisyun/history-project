<%@ page language="java" contentType="text/html; charset=UTF-8" isErrorPage="true" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
response.setStatus(HttpServletResponse.SC_OK);
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>HTTP 404</title>
    
	<style type="text/css">
	   	body {
			background-color: #D2E0F1;
		}
		
		#notFound {
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
  			<div id ="notFound">
				对不起，您访问的页面不存在！
			</div>
    	</td></tr>
  	</table>
  </body>
</html>
