<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page language="java" pageEncoding="UTF-8"%>
<%@page import="com.talkweb.security.SecurityHelper"%>
<%@page import="com.talkweb.ncfw.entity.Org"%>
<%@page import="com.talkweb.ncfw.action.OrgAction"%>
<%@ include file="/foundation/ext/jsp/taglib.jsp"%>
<%@ include file="/foundation/ext/jsp/workspace.jsp"%>
<%
	Org org = SecurityHelper.getCurrentUser().getOrg();
	User user = SecurityHelper.getCurrentUser().getUser();
	String roleId = user.getRoleid();
	String proprovincecode = user.getProvincenumber();
%>
<html>
<head>
	<title>流程开启控制</title>
	<link rel="stylesheet" type="text/css" href="<%=basePath%>foundation/ext/ext-plugins/lovcombo-1.0/css/Ext.ux.form.LovCombo.css"/>
	<script type="text/javascript" src="<%=basePath%>foundation/ext/ext-plugins/lovcombo-1.0/js/Ext.ux.form.LovCombo.js"></script>
	<script type="text/javascript" src="<%=basePath%>foundation/ext/ext-plugins/TreeField.js"></script>
	<script type="text/javascript" src="<%=basePath%>foundation/ext/ext-plugins/SearchField.js"></script>
	<link rel="stylesheet" type="text/css" href="<%=basePath%>foundation/ext/ext-plugins/file-upload.css" />
	<script type="text/javascript" src="<%=basePath%>foundation/ext/ext-plugins/FileUploadField.js"></script>
	
	
	<script type="text/javascript">

	var listUrl   	  	= sys.basePath + "switchAction!searchSwitchList.do";
	var updateUrl       = sys.basePath + "switchAction!updateStatus.do";
	var roleId			= '<%=roleId%>';
	var proprovincecode = '<%=proprovincecode%>';
	
    //数据字典
    var DataDict = {
    	projectTypeArray			: <ncframework:dictWrite dicttypeId="PROJECT_TYPE" type="array"/>,
    	sbStatusArray				: <ncframework:dictWrite dicttypeId="SB_STATUS" type="array"/>
   	};

   	//本地转化
   	var LocalRenderer = {
   		projectType				: function (projectType) {
			return Common.renderer.dictRender (DataDict.projectTypeArray, projectType);
   		},
   		sbStatus				: function (sbStatus) {
			return Common.renderer.dictRender (DataDict.sbStatusArray, sbStatus);
   		}
   	};

	//初始化页面信息
	function initPage () {
		if((roleId != 'sysadmin' || roleId != 'projectsp') && proprovincecode != 'ZB') {
			Ext.getCmp('sb_start_Btn').hide();
			Ext.getCmp('sb_finish_Btn').hide();
		}
		var store = dataGrid.getStore();
		store.load();
	}

	function startSb() {
		var selectedRecordArray = dataGrid.getSelectionModel().getSelections();
		if(selectedRecordArray.length < 1){
     		Ext.Msg.alert("提示信息", "请选择项目类别!");
     		return;
      	}

		var flowids = "";
      	for(var i = 0;i < selectedRecordArray.length;i++){
      		flowids += selectedRecordArray[i].get("flowid") + ",";
	    }
      	if(flowids.length > 0){
      		flowids = flowids.substring(0, flowids.length - 1);
	    }
		
      	updateStatus(flowids,"start");
	}

	function finishSb() {
		var selectedRecordArray = dataGrid.getSelectionModel().getSelections();
		if(selectedRecordArray.length < 1){
     		Ext.Msg.alert("提示信息", "请选择项目类别!");
     		return;
      	}

		var flowids = "";
      	for(var i = 0;i < selectedRecordArray.length;i++){
      		flowids += selectedRecordArray[i].get("flowid") + ",";
	    }
      	if(flowids.length > 0){
      		flowids = flowids.substring(0, flowids.length - 1);
	    }
		
      	updateStatus(flowids,"finish");
	}

	function updateStatus(flowids,status) {
		var params = {flowids: flowids,status: status};
	    
		Common.ajaxRequest({
			url: updateUrl, params: params,
			proccessMsg: '数据处理中, 请稍后...', successHandler: function () {dataGrid.getStore().load();}
		});
	}
	
    </script>
</head>
  <body>
    <ext:ext onReady="initPage();">
	  	<ext:viewport layout="border" otherProperties="split: true">
	  		<ext:items>
	  		<ext:tabPanel region="center" border="false" frame="false" otherProperties="activeTab: 0">
	  			<ext:items>
	  				<ext:panel title="项目申报设置" layout="border" border="false" iconCls="icon_plugin">
	  					<ext:items>
	   					<%-- 列表面板 --%>
	   					<ext:panel var="pendingListPanel" layout="border" border="false" otherProperties="split: true" region="center"
	   						margins="3 0 0 0">
	   						<ext:items>
	   						<%-------------------------------------------------------------------------------------------------
									定义数据列表  
								-------------------------------------------------------------------------------------------------%>
								<ext:gridPanel var="dataGrid" id="dataGrid" region="center" iconCls="icon_grid" title="项目类型列表"
									border="true" otherProperties="split: true" iconCls="icon_grid" loadMask="true" 
									viewConfig="{forceFit: true}" selModel="sm">
									<ext:tbar>
										<ext:toolbarSeparator></ext:toolbarSeparator>
										<ext:toolbarButton id="sb_start_Btn" text="允许申报" iconCls="icon_sure" handler="startSb"
											otherProperties="minWidth: Common.config.buttonWidth.minButton">
										</ext:toolbarButton>
										<ext:toolbarSeparator></ext:toolbarSeparator>
										<ext:toolbarButton id="sb_finish_Btn" text="结束申报" iconCls="icon_unUse" handler="finishSb"
											otherProperties="minWidth: Common.config.buttonWidth.minButton">
										</ext:toolbarButton>
									</ext:tbar>
									<%-- 定义数据源--%>
									<ext:store var="dataStore" remoteSort="true" otherProperties="url: listUrl" >
										<ext:jsonReader root="data" totalProperty="totalProperty"
											record="Ext.data.Record.create([
												{name : 'flowid', type : 'string', mapping: 'flowid'},
												{name : 'status', type : 'string', mapping: 'status'}
											])">
										</ext:jsonReader>
									</ext:store>
									<%-- 定义列表的列头--%>
									<ext:columnModel id="cm">
										<ext:rowNumberer></ext:rowNumberer>
										<ext:checkboxSelectionModel var="sm"></ext:checkboxSelectionModel>,
										{header : '项目类别', width: 100, dataIndex: 'flowid', renderer: LocalRenderer.projectType},
										{header : '申报状态', width: 200, dataIndex: 'status', renderer: LocalRenderer.sbStatus}
									</ext:columnModel>
								</ext:gridPanel>
	   						</ext:items>
	   					</ext:panel>
	  					</ext:items>
	  				
		  			</ext:panel>
		  		</ext:items>
		  	</ext:tabPanel>
		  	</ext:items>
	  	</ext:viewport>
    </ext:ext>
  </body>
  
</html>
