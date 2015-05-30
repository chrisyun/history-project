<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/foundation/ext/jsp/taglib.jsp"%>
<%@ include file="/foundation/ext/jsp/workspace.jsp"%>
<html>
<head>
	<title>密码修改</title>
	<script type="text/javascript" src="./ChangePwd.js"></script>
	<script type="text/javascript">
    
	//初始化页面信息
	function initPage () {
		ChangePwd.showInputWindow();
		//mainPanel.add(ChangePwd.getInputForm());
	}
    </script>
</head>
  <body>
    <ext:ext onReady="initPage();">
	  	<ext:viewport layout="border" otherProperties="split: true">
	  		<ext:items>
	  		<ext:tabPanel region="center" border="false" frame="false" otherProperties="activeTab: 0">
	  			<ext:items>
  				<ext:panel title="密码修改" layout="fit" border="false" iconCls="icon_plugin"  var="mainPanel"></ext:panel>
	  			</ext:items>
	  		</ext:tabPanel>
	  		</ext:items>
	  	</ext:viewport>
    </ext:ext>
  </body>
  
</html>
