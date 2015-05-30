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
%>
<html>
<head>
	<title>用户管理</title>
	<link rel="stylesheet" type="text/css" href="<%=basePath%>foundation/ext/ext-plugins/lovcombo-1.0/css/Ext.ux.form.LovCombo.css"/>
	<script type="text/javascript" src="<%=basePath%>foundation/ext/ext-plugins/lovcombo-1.0/js/Ext.ux.form.LovCombo.js"></script>
	<script type="text/javascript" src="<%=basePath%>foundation/ext/ext-plugins/TreeField.js"></script>
	<script type="text/javascript" src="<%=basePath%>foundation/ext/ext-plugins/SearchField.js"></script>
	<script type="text/javascript" src="./ChangePwd.js"></script>
	<script type="text/javascript" src="./User.js"></script>
	
	<script type="text/javascript">
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

	var orgRoot				= '<%=org.getParentorg()%>';
	var orgRootName			= LocalRenderer.provincenumber('<%=org.getProvinceNumber()%>');

	if('<%= roleId %>' == 'sysadmin') {
		orgRootName = '组织树';
		orgRoot = '<%= OrgAction.ALLORGROOT%>';
	}
	
	var currentTransferRoleId	= '<%=roleId%>';
	
    var listUserUrl   	  	= sys.basePath + "userAction!listUser.do";
    var getOrgNodesUrl		= sys.basePath + "orgAction!createOrgTree.do";
    var currentActiveNode;
        
    var pageSize = Common.config.pageSize || 15;


   	//提交查询表单
   	var onSubmitQueryHandler = function () {
   	   	var thisForm = queryFormPanel.getForm();
   	   	if (!thisForm.isValid()) {
			return;
   	   	}
   	   	var store = dataGrid.getStore();
   	 	var queryType 		= Ext.getCmp("query.QueryType").getText();
		if (store.baseParams == null) {
			store.baseParams	= {};
		}

		store.baseParams.useridLike = '';
		store.baseParams.usernameLike = '';
		store.baseParams.mailLike = '';
		store.baseParams.mobileLike = '';
		
		switch (queryType) {
			case "登录名" 	: 	store.baseParams.useridLike 	= Ext.getCmp("query.SearchField").getValue(); 	break;
			case "姓名" 		: 	store.baseParams.usernameLike 	= Ext.getCmp("query.SearchField").getValue(); 	break;
			case "邮箱" 		: 	store.baseParams.mailLike		= Ext.getCmp("query.SearchField").getValue(); 	break;
			case "联系电话" 	: 	store.baseParams.mobileLike		= Ext.getCmp("query.SearchField").getValue(); 	break;
		}
		store.baseParams.citynumber = '';
   	 	store.reload({params:{start: 0, limit: pagingToolbar.pageSize}});
   	};

   	//重置查询表单
   	var onResetQueryHandler = function () {
   		var thisForm = queryFormPanel.getForm();
   		thisForm.reset();
   	}

   	var searchField = new Ext.ux.form.SearchField({id : 'query.SearchField', width: 160, emptyText:'请输入查询关键字', hideLabel: true});
   	searchField.onTrigger2Click = onSubmitQueryHandler;

	//初始化页面信息
	function initPage () {
		//onSubmitQueryHandler();
		orgTree.on("click", function(node){
			currentActiveNode = node;
			var thisForm = queryFormPanel.getForm();
			var store = dataGrid.getStore();
	   	 	store.baseParams = {
	   	 		citynumber	: node.id
	   	   	};
	   	 	store.reload({params:{start: 0, limit: pagingToolbar.pageSize}});
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
  				<ext:panel title="用户管理" layout="border" border="false" iconCls="icon_plugin">
  					<ext:items>
	  					<%-- 组织树 --%>
		  				<ext:treePanel var="orgTree" id="query.orgTree" title="组织机构树" border="true" rootVisible="true" autoScroll="true" 
		  					width="230" useArrows="false" otherProperties="split: true" region="west" margins="3 0 0 0" 
		      				root='new Ext.tree.AsyncTreeNode({id: orgRoot, text : orgRootName, leaf: false, expanded: true})' 
		      				loader="new Ext.tree.TreeLoader({dataUrl : getOrgNodesUrl, listeners: { beforeload : function (thisLoader, node, callback ) {
		      						thisLoader.baseParams.parentId = node.id
		      					}}
		      				})" collapsible="true">
		   				</ext:treePanel>
	   					<%-- 列表面板 --%>
	   					<ext:panel var="userListPanel" layout="border" border="false" otherProperties="split: true" region="center"
	   						margins="3 0 0 0">
	   						<ext:items>
	   							<%-------------------------------------------------------------------------------------------------
									定义查询条件面板  
								-------------------------------------------------------------------------------------------------%>
								<ext:formPanel var="queryFormPanel" id="queryFormPanel" title="查询条件" collapsible="true" 
									labelAlign="left" frame="true" region="north" height="62" border="false" 
									labelWidth="58" otherProperties="split: true">
									<ext:items>
										<ext:container layout="column" defaults="{width: 260, layout: 'form'}" lazyInit="true">
											<ext:items>
												<ext:container lazyInit="true" xtype="panel" width="120">
													<ext:items>
														{xtype: 'tbbutton', id: 'query.QueryType', text : '登录名', minWidth: 100, menu : 
														new Ext.menu.Menu({
															width: 100,
															items : [{
																id: 'query.QueryTypeID',
																text : '登录名',
																handler: function () {
																	var QueryType = Ext.getCmp("query.QueryType");
																	QueryType.setText(Ext.getCmp("query.QueryTypeID").text);
																}
															},{
																id: 'query.QueryTypeCn',
																text : '姓名',
																handler: function () {
																	var QueryType = Ext.getCmp("query.QueryType");
																	QueryType.setText(Ext.getCmp("query.QueryTypeCn").text);
																}
															},{
																id: 'query.QueryTypeMobile',
																text : '联系电话',
																handler: function () {
													  				var QueryType = Ext.getCmp("query.QueryType");
													  				QueryType.setText(Ext.getCmp("query.QueryTypeMobile").text);
																}
															},{
																id: 'query.QueryTypeMail',
																text : '邮箱',
																handler: function () {
													  				var QueryType = Ext.getCmp("query.QueryType");
													  				QueryType.setText(Ext.getCmp("query.QueryTypeMail").text);
																}
															}]
														})}
													</ext:items>
												</ext:container>
												<ext:container lazyInit="true">
													<ext:items>
													searchField
													</ext:items>
												</ext:container>
											</ext:items>
										</ext:container>
										
									</ext:items>
								</ext:formPanel>
	   							<%-------------------------------------------------------------------------------------------------
									定义数据列表  
								-------------------------------------------------------------------------------------------------%>
								<ext:gridPanel var="dataGrid" id="dataGrid" region="center" iconCls="icon_grid" 
									border="true" otherProperties="split: true" iconCls="icon_grid" selModel="sm" loadMask="true"
									viewConfig="{forceFit: true}">
									<ext:tbar>
										<ext:toolbarSeparator></ext:toolbarSeparator>
										<ext:toolbarButton id="addBtn" text="新增" iconCls="icon_add" handler="User.onAdd"
											otherProperties="minWidth: Common.config.buttonWidth.minButton">
										</ext:toolbarButton>
										<ext:toolbarButton id="updateBtn" text="修改" iconCls="icon_edit" handler="User.onUpdate"
											otherProperties="minWidth: Common.config.buttonWidth.minButton">
										</ext:toolbarButton>
										<ext:toolbarButton id="deleteBtn" text="删除" iconCls="icon_delete" handler="User.onDelete"
											otherProperties="minWidth: Common.config.buttonWidth.minButton">
										</ext:toolbarButton>
										<ext:toolbarButton id="updatePwdBtn" text="密码修改" iconCls="icon_edit" handler="ChangePwd.onChangePwd"
											otherProperties="minWidth: Common.config.buttonWidth.minButton">
										</ext:toolbarButton>
										<ext:toolbarButton id="enableBtn" text="启用" iconCls="icon_sure" handler="User.enable"
											otherProperties="minWidth: Common.config.buttonWidth.minButton">
										</ext:toolbarButton>
										<ext:toolbarButton id="disableBtn" text="禁用" iconCls="icon_unUse" handler="User.disable"
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
										{header : '登录名', width: 120, dataIndex: 'userid'},
										{header : '姓名', width: 150, dataIndex: 'username'},
										{header : '性别', width: 50, dataIndex: 'gender', renderer: LocalRenderer.sex},
										{header : '联系电话', width: 100, dataIndex: 'mobile'}, 
										{header : '邮箱', width: 180, dataIndex: 'mail'},
										{header : '用户状态', width: 70, dataIndex: 'status', renderer: LocalRenderer.status},
										{header : '所属组织', width: 150, dataIndex: 'cityname'},
										{header : '省份', width: 70, dataIndex: 'provincenumber', renderer: LocalRenderer.provincenumber}
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
