<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page language="java" pageEncoding="UTF-8"%>
<%@page import="com.talkweb.security.SecurityHelper"%>
<%@page import="com.talkweb.ncfw.entity.Org"%>
<%@page import="com.talkweb.ncfw.action.OrgAction"%>
<%@ include file="/foundation/ext/jsp/taglib.jsp"%>
<%@ include file="/foundation/ext/jsp/workspace.jsp"%>
<html>
<%
	User user = SecurityHelper.getCurrentUser().getUser();
	String curProvince = user.getProvincenumber();
%>
<head>
	<title>用户管理</title>
	<link rel="stylesheet" type="text/css" href="<%=basePath%>foundation/ext/ext-plugins/lovcombo-1.0/css/Ext.ux.form.LovCombo.css"/>
	<script type="text/javascript" src="<%=basePath%>foundation/ext/ext-plugins/lovcombo-1.0/js/Ext.ux.form.LovCombo.js"></script>
	<script type="text/javascript" src="<%=basePath%>foundation/ext/ext-plugins/TreeField.js"></script>
	<script type="text/javascript" src="<%=basePath%>foundation/ext/ext-plugins/SearchField.js"></script>
	<script type="text/javascript" src="./ChangePwd.js"></script>
	<script type="text/javascript" src="./User.js"></script>
	
	<script type="text/javascript">
	
    var listUserUrl   	  	= sys.basePath + "userAction!listUser.do";
    var curProvince 		= '<%=curProvince%>';
    var pageSize = Common.config.pageSize || 15;

    //数据字典
    var DataDict = {
    	sexArray			: <ncframework:dictWrite dicttypeId="COMMON_USER_SEX" type="array"/>,
   		statusArray			: <ncframework:dictWrite dicttypeId="SYS_USER_STATUS" type="array"/>,
   		provinceCodeArray	: <ncframework:dictWrite dicttypeId="COMMON_PROVINCE" type="array"/>
   	};

   	//本地转化
   	var LocalRenderer = {
   		sex				: function (sex) {
			return Common.renderer.dictRender (DataDict.sexArray, sex);
   		},
   		status			: function (status) {
			return Common.renderer.dictRender (DataDict.statusArray, status);
   		},
   		provincenumber	: function (value) {
			return Common.renderer.dictRender (DataDict.provinceCodeArray, value);
   		}
   	};

   	//提交查询表单
   	var onSubmitQueryHandler = function () {
   	   	var store = dataGrid.getStore();
		store.baseParams = {provincenumber:curProvince, status:'disable'};
   	 	store.reload({params:{start: 0, limit: pagingToolbar.pageSize}});
   	};

	//初始化页面信息
	function initPage () {
		onSubmitQueryHandler();
	}
    </script>
</head>
  <body>
    <ext:ext onReady="initPage();">
	  	<ext:viewport layout="border" otherProperties="split: true">
	  		<ext:items>
	  		<ext:tabPanel region="center" border="false" frame="false" otherProperties="activeTab: 0">
	  			<ext:items>
  				<ext:panel title="注册用户审核" layout="border" border="false" iconCls="icon_plugin">
  					<ext:items>
	   					<%-- 列表面板 --%>
	   					<ext:panel var="userListPanel" layout="border" border="false" otherProperties="split: true" region="center"
	   						margins="3 0 0 0">
	   						<ext:items>
	   							<ext:hidden id="isSpflag" name="isSpflag" value="yes"></ext:hidden>
	   							<%-------------------------------------------------------------------------------------------------
									定义数据列表  
								-------------------------------------------------------------------------------------------------%>
								<ext:gridPanel var="dataGrid" id="dataGrid" region="center" iconCls="icon_grid" 
									border="true" otherProperties="split: true" iconCls="icon_grid" selModel="sm" loadMask="true"
									viewConfig="{forceFit: true}">
									<ext:tbar>
										<ext:toolbarSeparator></ext:toolbarSeparator>
										<ext:toolbarButton id="deleteBtn" text="审核不通过" iconCls="icon_delete" handler="User.onDelete"
											otherProperties="minWidth: Common.config.buttonWidth.minButton">
										</ext:toolbarButton>
										<ext:toolbarButton id="enableBtn" text="审核通过" iconCls="icon_sure" handler="User.enable"
											otherProperties="minWidth: Common.config.buttonWidth.minButton">
										</ext:toolbarButton>
									</ext:tbar>
									<%-- 定义数据源--%>
									<ext:store var="dataStore" remoteSort="true" sortInfo="{field: 'ordernumber', direction: 'ASC'}"
										otherProperties="url: listUserUrl" >
										<ext:jsonReader root="data" totalProperty="totalProperty"
											record="Ext.data.Record.create([
												{name : 'userid', type : 'string', mapping: 'userid'},
												{name : 'username', type : 'string', mapping: 'username'},
												{name : 'citynumber', type : 'string', mapping: 'citynumber'},
												{name : 'cityname', type : 'string', mapping: 'cityname'},
												{name : 'mobile', type : 'string', mapping: 'mobile'},
												{name : 'mail', type : 'string', mapping: 'mail'},
												{name : 'gender', type : 'string', mapping: 'gender'},
												{name : 'status', type : 'string', mapping: 'status'},
												{name : 'roleid', type : 'string', mapping: 'roleid'},
												{name : 'provincenumber', type : 'string', mapping: 'provincenumber'},
												{name : 'remark', type : 'string', mapping: 'remark'}
											])">
										</ext:jsonReader>
									</ext:store>
									<%-- 定义列表的列头--%>
									<ext:columnModel id="cm">
										<ext:rowNumberer></ext:rowNumberer>										
										<ext:checkboxSelectionModel var="sm"></ext:checkboxSelectionModel>,
										{header : '登录名', width: 150, dataIndex: 'userid'},
										{header : '姓名', width: 150, dataIndex: 'username'},
										{header : '性别', width: 50, dataIndex: 'gender', renderer: LocalRenderer.sex},
										{header : '联系电话', width: 100, dataIndex: 'mobile'}, 
										{header : '邮箱', width: 180, dataIndex: 'mail'},
										{header : '用户状态', width: 70, dataIndex: 'status', renderer: LocalRenderer.status},
										{header : '所属组织', width: 100, dataIndex: 'cityname'},
										{header : '省份', width: 100, dataIndex: 'provincenumber', renderer: LocalRenderer.provincenumber}
									</ext:columnModel>
									<%-- 定义列表的分页工具栏--%>
									<ext:pagingToolbar key="bbar" displayInfo="true"
										store="dataStore" var="pagingToolbar" otherProperties="pageSize: pageSize">
										<ext:plugins>new Ext.ux.Page.pageSize({beforeText : '显示', afterText : '条' })</ext:plugins>
									</ext:pagingToolbar>
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
