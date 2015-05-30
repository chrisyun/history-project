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
	<title>待办工作</title>
	<link rel="stylesheet" type="text/css" href="<%=basePath%>foundation/ext/ext-plugins/lovcombo-1.0/css/Ext.ux.form.LovCombo.css"/>
	<script type="text/javascript" src="<%=basePath%>foundation/ext/ext-plugins/lovcombo-1.0/js/Ext.ux.form.LovCombo.js"></script>
	<script type="text/javascript" src="<%=basePath%>foundation/ext/ext-plugins/TreeField.js"></script>
	<script type="text/javascript" src="<%=basePath%>foundation/ext/ext-plugins/SearchField.js"></script>
	<script type="text/javascript" src="./Project.js"></script>
	
	<script type="text/javascript">

	var orgType = '<%=org.getOrgtype() %>';
	var proprovincecode = '<%=user.getProvincenumber()%>';
	
    var listUrl   	  	= sys.basePath + "projectAction!searchPendingList.do";
        
    var pageSize = Common.config.pageSize || 15;

    //数据字典
    var DataDict = {
    	flowStatusArray			: <ncframework:dictWrite dicttypeId="FLOW_STATUS" type="array"/>,
    	projectStatusArray			: <ncframework:dictWrite dicttypeId="PROJECT_STATUS" type="array"/>,
    	projectTypeArray			: <ncframework:dictWrite dicttypeId="PROJECT_TYPE" type="array"/>,
    	activityNameArray			: <ncframework:dictWrite dicttypeId="FLOW_ACTIVITYNAME" type="array"/>,
   		dwCodeArray	: <ncframework:dictWrite dicttypeId="DW_CODE" type="array"/>,
   		orgSearchTypeArray	: <ncframework:dictWrite dicttypeId="ORG_SEARCH_TYPE" type="array"/>,
   		tsPnameArray	: <ncframework:dictWrite dicttypeId="TSPROJECT_NAME" type="array"/>,
   		provinceCodeArray	: <ncframework:dictWrite dicttypeId="COMMON_PROVINCE" type="array"/>
   	};

    var projectNameList = <ncframework:dictWrite dicttypeId="GJCXPROJECT_NAME" type="array"/>;
    projectNameList = projectNameList.concat(DataDict.tsPnameArray);
	
   	//本地转化
   	var LocalRenderer = {
   		flowStatus				: function (flowStatus) {
			return Common.renderer.dictRender (DataDict.flowStatusArray, flowStatus);
   		},
   		projectStatus			: function (projectStatus) {
			return Common.renderer.dictRender (DataDict.projectStatusArray, projectStatus);
   		},
   		projectType				: function (projectType) {
			return Common.renderer.dictRender (DataDict.projectTypeArray, projectType);
   		},
   		activityName			: function (activityName) {
			return Common.renderer.dictRender (DataDict.activityNameArray, activityName);
   		},
   		dwCodeArray			: function (value) {
			return Common.renderer.dictRender (DataDict.dwCodeArray, value);
   		},
   		provincenumber			: function (value) {
			return Common.renderer.dictRender (DataDict.provinceCodeArray, value);
   		}
   	};

   	//提交查询表单
   	var onSubmitQueryHandler = function () {
   	   	var store = dataGrid.getStore();

   		var startDate = Ext.getCmp("query.sbDateBegin").getValue();
		var endDate = Ext.getCmp("query.sbDateEnd").getValue();
		startDate = (startDate == "") ? "" : startDate.format("Y-m-d");
		endDate = (endDate == "") ? "" : endDate.format("Y-m-d");
		if(startDate != "" && endDate != ""){
			if(startDate > endDate){
				Ext.getCmp("query.sbDateEnd").markInvalid("结束日期应该晚于开始日期！");
				return;
			}
		}

   	   	
   	 	store.baseParams = {
   	 		'pname': Ext.getCmp("query.projectName").getValue().trim(),
 			'dept': Ext.getCmp("query.dept").getValue(),
 			'pmtype': Ext.getCmp("query.projectType").getValue(),
 			'pstatus': Ext.getCmp("query.projectStatus").getValue(),
 			'spstatus': Ext.getCmp("query.flowStatus").getValue(),
 			'startDate': startDate,
 			'endDate': endDate
 		};
   	 	store.reload({params:{start: 0, limit: pagingToolbar.pageSize}});
   	};

   	//重置查询表单
   	var onResetQueryHandler = function () {
   		var thisForm = queryFormPanel.getForm();
   		thisForm.reset();
   	}

	//初始化页面信息
	function initPage () {
		if('ZF' != orgType || 'ZB' != proprovincecode) {
			Ext.getCmp("pCodeCon").hide();
			Ext.getCmp("pCodeCon1").hide();
		}
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
  				<ext:panel title="待办工作列表" layout="border" border="false" iconCls="icon_plugin">
  					<ext:items>
	   					<%-- 列表面板 --%>
	   					<ext:panel var="pendingListPanel" layout="border" border="false" otherProperties="split: true" region="center"
	   						margins="3 0 0 0">
	   						<ext:items>
	   							<%-------------------------------------------------------------------------------------------------
									定义查询条件面板  
								-------------------------------------------------------------------------------------------------%>
								<ext:formPanel var="queryFormPanel" id="queryFormPanel" title="查询条件" collapsible="true" 
									labelAlign="left" frame="true" region="north" height="115" border="false" 
									labelWidth="80" otherProperties="split: true" margins="3 0 0 0">
									<ext:items>
										<ext:container layout="column" defaults="{width: 380, layout: 'form'}" lazyInit="true">
											<ext:items>
												<ext:container lazyInit="true">
													<ext:items>
														<ext:dateField fieldLabel="申报时间"
															name="query.sbDateBegin"
															id="query.sbDateBegin" width="100" format="Y-m-d"
															allowBlank="true" cls="float_left" itemCls="float_left"
															clearCls="allow_float" emptyText="请选择开始日期"
															otherProperties="shadow : true,editable: false ,maxValue: new Date(), 
															value: new Date().add(Date.MONTH,-12)">
														</ext:dateField>
														,{xtype: 'label', text: '至', cls: 'float_left',itemCls: 'float_left', style: 'font-size: 12px; margin: 3 3 0 4px;display:inline;'}
														<ext:dateField hideLabel="true"
															name="query.sbDateEnd"
															id="query.sbDateEnd" width="100" format="Y-m-d"
															allowBlank="true" cls="float_left" itemCls="float_left"
															clearCls="allow_float" emptyText="请选择结束日期"
															otherProperties="shadow : true,editable: false,
															maxValue: new Date(), value: new Date()">
														</ext:dateField>
													</ext:items>
												</ext:container>
											
											
												<ext:container lazyInit="true">
													<ext:items>
														<ext:comboBox id="query.projectName" triggerAction="all"
															fieldLabel='项目名称' allowBlank="true" width="180"
															displayField="text" mode="local" editable="true"
															valueField="text" value=""
															store="new Ext.data.SimpleStore({
																fields : ['value', 'text'],
																data : Common.config.nullArray.concat(projectNameList)
															})">
														</ext:comboBox>
													</ext:items>
												</ext:container>
												
												<ext:container lazyInit="true">
													<ext:items>
														<ext:comboBox id="query.projectType" triggerAction="all"
															fieldLabel='项目类型' allowBlank="true" width="180"
															displayField="text" mode="local" editable="false"
															selectOnFocus="true" forceSelection="true" valueField="value" value=""
															store="new Ext.data.SimpleStore({
																fields : ['value', 'text'],
																data : Common.config.nullArray.concat(DataDict.projectTypeArray)
															})">
														</ext:comboBox>
													</ext:items>
												</ext:container>
												
												<ext:container lazyInit="true">
													<ext:items>
														<ext:comboBox id="query.projectStatus" triggerAction="all"
															fieldLabel='项目状态' allowBlank="true" width="180"
															displayField="text" mode="local" editable="false"
															selectOnFocus="true" forceSelection="true" valueField="value" value=""
															store="new Ext.data.SimpleStore({
																fields : ['value', 'text'],
																data : Common.config.nullArray.concat(DataDict.projectStatusArray)
															})">
														</ext:comboBox>
													</ext:items>
												</ext:container>
												
												
												<ext:panel lazyInit="true" id="pCodeCon" width="280">
													<ext:items>
														<ext:comboBox id="query.orgType" triggerAction="all"
															fieldLabel='申报人部门' displayField="text" mode="local"
															selectOnFocus="true" forceSelection="true" width="180"
															valueField="value" value="" editable="false" allowBlank="true"
															listeners="{select : function ( field, record, index) {setOrgSel(field, record, index);}}"
															store="new Ext.data.SimpleStore({
																fields : ['value', 'text'],
																data : Common.config.nullArray.concat(DataDict.orgSearchTypeArray)
															})" >
														</ext:comboBox>
													</ext:items>
												</ext:panel>
												
												<ext:panel lazyInit="true" width="180" id="pCodeCon1">
													<ext:items>
														<ext:comboBox id="query.dept" triggerAction="all"
															displayField="text" mode="local" hideLabel="true"
															selectOnFocus="true" forceSelection="true" width="180"
															valueField="value" value="" editable="false" allowBlank="true"
															store="new Ext.data.SimpleStore({
																fields : ['value', 'text'],
																data : Common.config.nullArray.concat([])
															})">
														</ext:comboBox>
													</ext:items>
												</ext:panel>
												
												<ext:container lazyInit="true">
													<ext:items>
														<ext:comboBox id="query.flowStatus" triggerAction="all"
															fieldLabel='流程状态' allowBlank="true" width="180"
															displayField="text" mode="local" editable="false"
															selectOnFocus="true" forceSelection="true" valueField="value" value=""
															store="new Ext.data.SimpleStore({
																fields : ['value', 'text'],
																data : Common.config.nullArray.concat(DataDict.flowStatusArray)
															})">
														</ext:comboBox>
													</ext:items>
												</ext:container>
												
												<ext:container layout="table" lazyInit="true"
													defaults="{layout: 'form', border:false, columnWidth: 1.0}">
													<ext:items>
														<ext:button text="查询" iconCls="icon_search" style="margin-right:20px;" 
															handler="onSubmitQueryHandler" otherProperties="minWidth: Common.config.buttonWidth.defaultButton">
														</ext:button>
													</ext:items>
												</ext:container>
											</ext:items>
										</ext:container>
									</ext:items>
								</ext:formPanel>
	   						
	   							<%-------------------------------------------------------------------------------------------------
									定义数据列表  
								-------------------------------------------------------------------------------------------------%>
								<ext:gridPanel var="dataGrid" id="dataGrid" region="center" iconCls="icon_grid" title="待办列表(<span style='color:blue;'>双击查看详情</span>)"
									border="true" otherProperties="split: true" iconCls="icon_grid" loadMask="true" viewConfig="{forceFit: true}"
								listeners="{celldblclick : function ( thisGrid, rowIndex, columnIndex, ev ) {Project.showPendingWindow('pending');}}">
									<%-- 定义数据源--%>
									<ext:store var="dataStore" remoteSort="true" sortInfo="{field: 'creattime', direction: 'DESC'}"
										otherProperties="url: listUrl" >
										<ext:jsonReader root="data" totalProperty="totalProperty"
											record="Ext.data.Record.create([
												{name : 'pid', type : 'string', mapping: 'pid'},
												{name : 'pname', type : 'string', mapping: 'pname'},
												{name : 'pmtype', type : 'string', mapping: 'pmtype'},
												{name : 'pstatus', type : 'string', mapping: 'pstatus'},
												{name : 'spstatus', type : 'string', mapping: 'spstatus'},
												{name : 'creater', type : 'string', mapping: 'creater'},
												{name : 'pmanager', type : 'string', mapping: 'pmanager'},
												{name : 'proprovincecode', type : 'string', mapping: 'proprovincecode'},
												{name : 'curActivityName', type : 'string', mapping: 'curActivityName'},
												{name : 'pslnumber', type : 'string', mapping: 'pslnumber'},
												{name : 'creattime', type : 'string', mapping: 'creattime'}
											])">
										</ext:jsonReader>
									</ext:store>
									<%-- 定义列表的列头--%>
									<ext:columnModel id="cm">
										<ext:rowNumberer></ext:rowNumberer>,
										{header : '项目名称', width: 100, dataIndex: 'pname',sortable:true},
										{header : '项目编号', width: 100, dataIndex: 'pslnumber'},
										{header : '项目类别', width: 100, dataIndex: 'pmtype', renderer: LocalRenderer.projectType,sortable:true},
										{header : '项目状态', width: 40, dataIndex: 'pstatus', renderer: LocalRenderer.projectStatus,sortable:true}, 
										{header : '审批状态', width: 40, dataIndex: 'spstatus', renderer: LocalRenderer.flowStatus},
										{header : '当前处理环节', width: 80, dataIndex: 'curActivityName', renderer: LocalRenderer.activityName},
										{header : '申报单位', width: 80, dataIndex: 'creater'},
										{header : '申报省份', width: 40, dataIndex: 'proprovincecode', renderer: LocalRenderer.provincenumber,sortable:true},
										{header : '申报时间', width: 50, dataIndex: 'creattime',sortable:true}
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
