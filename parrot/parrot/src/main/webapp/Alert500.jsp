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
    
    <title>HTTP 500</title>
    
	<style type="text/css">
	   	body {
			background-color: #D2E0F1;
		}
		
		#exception {
            margin-top: 50px;
			padding: 15px 50px;
			width: 800px;
			font-size : 16px;
			border: 1px solid black;
			background-color: yellow;
			text-align: center;
		}
		
		a{
			font-weight:bold;
			font-family:"宋体";
			font-size:18px;
		}
		
		hr {
			border-width: 2px;
			border-color: black;
		}
    </style>

  </head>
  
  <body>
  	<table align="center" width="100%">
  		<tr><td align="center"> 
  			<div id="exception">
				<h2>程序发生了错误，有可能该页面正在调试或者是设计上的缺陷.</h2><br/>
			   	<hr width="100%">
			</div>
    	</td></tr>
  	</table>
  </body>
</html>
