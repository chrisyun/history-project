<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>页面打开出错</title>
    
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
				<h2>对不起，流程页面打开出错，无法找到相关的流程.</h2><br/>
			   	<hr width="100%">
			</div>
    	</td></tr>
  	</table>
  </body>
</html>