<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/foundation/ext/jsp/taglib.jsp"%>
<%@ include file="/foundation/ext/jsp/workspace.jsp"%>
<html>
<head>
	<title>附件上传demo</title>
	<script type="text/javascript" src="./fileManager.js"></script>
	<link rel="stylesheet" type="text/css" href="<%=basePath%>foundation/ext/ext-plugins/file-upload.css" />
	<script type="text/javascript" src="<%=basePath%>foundation/ext/ext-plugins/FileUploadField.js"></script>
	<script type="text/javascript">
   	function initPage () {
   		FileManage.fileListPanelId = 'fileListPanel';
   		FileManage.editMode = 'edit';
   		FileManage.showUploadWindow();
   	}
    </script>
</head>
  <body>
    <ext:ext onReady="">
	  	<ext:viewport layout="border" otherProperties="split: true">
	  		<ext:items>
	  		<ext:tabPanel region="center" border="false" frame="false" otherProperties="activeTab: 0">
	  			<ext:items>
  				<ext:panel title="用户注册" layout="fit" border="false" iconCls="icon_plugin"  var="mainPanel">
  					<ext:items>
  						<ext:fieldSet lazyInit="true" xtype="fieldset" autoHeight="true" title="文件信息" buttonAlign="right">
							<ext:items>
								<ext:panel xtype="panel" lazyInit="true" id="fileListPanel" layout="column"  html="<div id='fileListPanel_text'></div>"
									defaults="{columnWidth: .5, layout: 'form'}"  width="500" height="200">
								</ext:panel>
							</ext:items>
							<ext:buttons>
								<ext:button text="文件上传" iconCls="icon_add" style="margin-right:20px;" 
								handler="initPage" otherProperties="minWidth: Common.config.buttonWidth.defaultButton">
								</ext:button>
							</ext:buttons>
						</ext:fieldSet>
  					</ext:items>
  				</ext:panel>
	  			</ext:items>
	  		</ext:tabPanel>
	  		</ext:items>
	  	</ext:viewport>
    </ext:ext>
  </body>
  
</html>
