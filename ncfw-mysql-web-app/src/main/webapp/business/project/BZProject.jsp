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
	String pid = request.getParameter("pid")==null?"":request.getParameter("pid");
	String openType = request.getParameter("openType")==null?"":request.getParameter("openType");
	
	String sbDept = "";
	if("ZB".equals(org.getProvinceNumber()) && "DW".equals(org.getOrgtype())) {
		sbDept = user.getUserid();
	} else {
		sbDept = org.getOrgsbname()==null?"":org.getOrgsbname();
	}
%>
<html>
<head>
	<title>项目信息</title>
	<link rel="stylesheet" type="text/css" href="<%=basePath%>foundation/ext/ext-plugins/lovcombo-1.0/css/Ext.ux.form.LovCombo.css"/>
	<script type="text/javascript" src="<%=basePath%>foundation/ext/ext-plugins/lovcombo-1.0/js/Ext.ux.form.LovCombo.js"></script>
	<script type="text/javascript" src="<%=basePath%>foundation/ext/ext-plugins/TreeField.js"></script>
	<script type="text/javascript" src="<%=basePath%>foundation/ext/ext-plugins/SearchField.js"></script>
	<link rel="stylesheet" type="text/css" href="<%=basePath%>foundation/ext/ext-plugins/file-upload.css" />
	<script type="text/javascript" src="<%=basePath%>foundation/ext/ext-plugins/FileUploadField.js"></script>
	<script type="text/javascript" src="<%=basePath%>business/file/fileManager.js"></script>
	<script type="text/javascript" src="./Project.js"></script>
	<script type="text/javascript" src="./Minds.js"></script>
	
	
	<script type="text/javascript">

	var orgType = '<%=org.getOrgtype() %>';
	var pid = '<%=pid %>';
	var openType = '<%=openType%>';
	var user = '<%=user.getUserid()%>';
	var roleId	= '<%=roleId%>';
	var proprovincecode = '<%=user.getProvincenumber()%>';
	var sbDeptName = '<%=sbDept%>';
	
	var fileMan = new FileManager("fileListPanel",pid,"t_project","view","common");
    //数据字典
    var DataDict = {
    	flowStatusArray				: <ncframework:dictWrite dicttypeId="FLOW_STATUS" type="array"/>,
    	projectStatusArray			: <ncframework:dictWrite dicttypeId="PROJECT_STATUS" type="array"/>,
    	projectTypeArray			: <ncframework:dictWrite dicttypeId="PROJECT_TYPE" type="array"/>,
    	activityNameArray			: <ncframework:dictWrite dicttypeId="FLOW_ACTIVITYNAME" type="array"/>,
    	pwProjectTypeArray			: <ncframework:dictWrite dicttypeId="PW_PROJECT_TYPE" type="array"/>,
    	projectZTypeArray			: <ncframework:dictWrite dicttypeId="PROJECT_ZTYPE" type="array"/>,
    	projectSBTypeArray			: <ncframework:dictWrite dicttypeId="PROJECT_SB_TYPE" type="array"/>,
    	projectNameArray			: <ncframework:dictWrite dicttypeId="GJCXPROJECT_NAME" type="array"/>,
   		provinceCodeArray			: <ncframework:dictWrite dicttypeId="COMMON_PROVINCE" type="array"/>
   	};

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
   		projectZType			: function (projectZType) {
			return Common.renderer.dictRender (DataDict.projectZTypeArray, projectZType);
   		},
   		projectSBType			: function (projectSBType) {
			return Common.renderer.dictRender (DataDict.projectSBTypeArray, projectSBType);
   		},
   		provincenumber			: function (value) {
			return Common.renderer.dictRender (DataDict.provinceCodeArray, value);
   		}
   	};

	//初始化页面信息
	function initPage () {
		fileMan.editMode = 'edit';
		
		if(!Ext.isEmpty(pid)) {
			Project.queryProject(pid);
			//增加中期检查
			Project.queryMedProjectId(pid);
			//增加结项检查
			Project.queryFinProjectId(pid);
			Ext.getCmp('add_Btn').hide();
			Ext.getCmp('update_Btn').show();
			Ext.getCmp('pmtype').setDisabled(true);
		} else {
			Ext.getCmp('isInput').setValue("yes");
		}
		
		
		//Project.generateProjectNum("GJWHCXGCXM");
		Ext.getCmp('sborg').setValue(sbDeptName);
	}
	
	function doSBNumber(type) {
		Project.generateProjectNum(type);
	}
	
	
	function initPageShow() {
	}
    </script>
</head>
  <body>
    <ext:ext onReady="initPage();">
	  	<ext:viewport layout="border" otherProperties="split: true">
	  		<ext:items>
	  		<ext:tabPanel region="center" border="false" frame="false" otherProperties="activeTab: 0" var="tabPanel">
	  			<ext:items>
  				<ext:panel title="项目信息" layout="border" border="false" iconCls="icon_plugin">
  					<ext:items>
	   					<%-- 列表面板 --%>
	   					<ext:panel var="mainPanel" layout="border" border="false" otherProperties="split: true" region="center"
	   						margins="3 0 0 0" otherProperties="buttonAlign: 'center'">
	   						<ext:items>
								<ext:formPanel var="submitPrjFormPanel" id="submitPrjFormPanel" collapsible="false"
									labelAlign="right" frame="true" region="center" border="false" autoScroll="true" autoWidth="true"
									labelWidth="200" otherProperties="split: true">
									<ext:items>
										<ext:fieldSet layout="column" defaults="{columnWidth: 1, layout: 'form'}" lazyInit="true" 
											xtype="fieldset" autoHeight="true" title="项目基本信息" collapsible="true" var="projectBaseInfo" 
											frame="true">
											<ext:items>
												<ext:hidden name="project.pid" id="pid"></ext:hidden>
												<ext:hidden name="project.isInput" id="isInput"></ext:hidden>
												
												<ext:container lazyInit="true">
													<ext:items>
														<ext:comboBox id="pmtype" hiddenName="project.pmtype"
															fieldLabel="项目类别<span class='red'>(必填)</span>"
															displayField="text" mode="local" triggerAction="all"
															selectOnFocus="true" forceSelection="true" width="500"
															valueField="value" value="" editable="false" allowBlank="false"
															store="new Ext.data.SimpleStore({
																fields : ['value', 'text'],
																data : Common.config.nullArray.concat(DataDict.projectTypeArray)
															})" listeners="{change : function ( field, newValue, oldValue ) {doSBNumber(newValue);}}">
														</ext:comboBox>
													</ext:items>
												</ext:container>
												
												
												<ext:container lazyInit="true">
													<ext:items>
														<ext:textField id="pname" name="project.pname"  width="480" fieldLabel="项目名称<span class='red'>(必填)</span>" allowBlank="false" otherProperties="anchor: Project.fieldAnchor">
														</ext:textField>
													</ext:items>
												</ext:container>
														
												<ext:container layout="column" defaults="{width: 480, layout: 'form'}" lazyInit="true">
													<ext:items>
														<ext:panel lazyInit="true">
															<ext:items>
																<ext:textField id="pslnumber" name="project.pslnumber" readOnly="false" fieldLabel="项目编号<span class='red'>(必填)</span>" allowBlank="false" otherProperties="anchor: Project.fieldAnchor">
																</ext:textField>
															</ext:items>
														</ext:panel>
														<ext:container lazyInit="true">
															<ext:items>
																<ext:comboBox id="psbtype" hiddenName="project.psbtype"
																	fieldLabel="申报类别"
																	displayField="text" mode="local" triggerAction="all"
																	selectOnFocus="true" forceSelection="true" width="200"
																	valueField="value" value="" editable="false" allowBlank="true"
																	store="new Ext.data.SimpleStore({
																		fields : ['value', 'text'],
																		data : Common.config.nullArray.concat(DataDict.projectSBTypeArray)
																	})">
																</ext:comboBox>
															</ext:items>
														</ext:container>
													</ext:items>
												</ext:container>
												
												<ext:container layout="column" defaults="{width: 480, layout: 'form'}" lazyInit="true">
													<ext:items>
														<ext:panel lazyInit="true">
															<ext:items>
																<ext:textField id="pmanager" name="project.pmanager"  width="250" fieldLabel="项目负责人<span class='red'>(必填)</span>"  allowBlank="false" otherProperties="anchor: Project.fieldAnchor">
																</ext:textField>
															</ext:items>
														</ext:panel>
														<ext:panel lazyInit="true">
															<ext:items>
																<ext:textField id="tel" name="project.tel" width="280" fieldLabel="联系电话<span class='red'>(必填)</span>"  allowBlank="false" otherProperties="anchor: Project.fieldAnchor">
																</ext:textField>
															</ext:items>
														</ext:panel>
													</ext:items>
												</ext:container>
												
												<ext:container layout="column" defaults="{width: 480, layout: 'form'}" lazyInit="true">
													<ext:items>
															<ext:panel lazyInit="true">
																<ext:items>
																	<ext:textField id="sborg" name="project.sborg" fieldLabel="申报部门<span class='red'>(必填)</span>"  allowBlank="false" otherProperties="anchor: Project.fieldAnchor">
																	</ext:textField>
																</ext:items>
															</ext:panel>
															<ext:panel lazyInit="true">
															<ext:items>
																<ext:textField id="creater" name="project.creater" width="280" fieldLabel="申报单位<span class='red'>(必填)</span>"  allowBlank="false" otherProperties="anchor: Project.fieldAnchor">
																</ext:textField>
															</ext:items>
														</ext:panel>
													</ext:items>
												</ext:container>
												
												
												<ext:panel lazyInit="true">
													<ext:items>
														<ext:textField id="gjorg" name="project.gjorg" fieldLabel="共建部门"  allowBlank="true" otherProperties="anchor: Project.fieldAnchor">
														</ext:textField>
													</ext:items>
												</ext:panel>
												
												<ext:panel lazyInit="true">
													<ext:items>
														<ext:textField id="cdorg" name="project.cdorg" fieldLabel="承担单位<span class='red'>(必填)</span>"  allowBlank="false" otherProperties="anchor: Project.fieldAnchor">
														</ext:textField>
													</ext:items>
												</ext:panel>
												
												<ext:container lazyInit="true">
													<ext:items>
														<ext:comboBox id="proprovincecode" hiddenName="project.proprovincecode"
															fieldLabel="项目所属省份<span class='red'>(必填)</span>"
															displayField="text" mode="local" triggerAction="all" width="300"
															valueField="value" editable="false" allowBlank="false"
															store="new Ext.data.SimpleStore({
																fields : ['value', 'text'],
																data : Common.config.nullArray.concat(DataDict.provinceCodeArray)
															})">
														</ext:comboBox>
													</ext:items>
												</ext:container>
												
												
												<ext:panel lazyInit="true">
													<ext:items>
														<ext:dateField fieldLabel="申报日期<span class='red'>(必填)</span>"
															name="project.sbdate" 
															id="sbdate" width="200" format="Y-m-d"
															allowBlank="true" cls="float_left" itemCls="float_left"
															clearCls="allow_float" emptyText="请选择日期"
															otherProperties="shadow : true,editable: false ,maxValue: new Date(), 
															value: new Date().add(Date.MONTH,0)">
														</ext:dateField>
													</ext:items>
												</ext:panel>
												
												<ext:container lazyInit="true">
													<ext:items>
														<ext:comboBox id="pwtype" hiddenName="project.pwtype"
															fieldLabel="评委分类"
															displayField="text" mode="local" triggerAction="all"
															selectOnFocus="true" forceSelection="true" width="500"
															valueField="value" value="" editable="false"
															store="new Ext.data.SimpleStore({
																fields : ['value', 'text'],
																data : Common.config.nullArray.concat(DataDict.pwProjectTypeArray)
															})">
														</ext:comboBox>
													</ext:items>
												</ext:container>
												
											</ext:items>
										</ext:fieldSet>
										
										
										<ext:fieldSet layout="column" defaults="{columnWidth: 1, layout: 'form'}" lazyInit="true" 
											xtype="fieldset" autoHeight="true" title="附件信息" collapsible="true" var="fileBaseInfo" 
											frame="true" otherProperties="buttonAlign: 'left'">
											<ext:items>
												<ext:panel xtype="panel" lazyInit="true" id="fileListPanel" layout="column"  html="<div id='fileListPanel_text'></div>"
													defaults="{columnWidth: .5, layout: 'form'}">
												</ext:panel>
											</ext:items>
											<ext:buttons>
												<ext:button text="文件上传" iconCls="icon_add" style="margin-right:20px;" id="fileUploadButton"
													handler="FileConstant.showUploadWindow" otherProperties="minWidth: Common.config.buttonWidth.defaultButton">
												</ext:button>
											</ext:buttons>
										</ext:fieldSet>
									</ext:items>
								</ext:formPanel>
	   						</ext:items>
	   						
	   						<ext:buttons>
								<ext:button id="add_Btn" text="新增" handler="function() {Project.submintProject(pid,'input');}" iconCls="icon_sure" otherProperties="minWidth: Common.config.buttonWidth.defaultButton"></ext:button>
								<ext:button id="update_Btn" text="修改" hidden="true" handler="function() {Project.updateProject();}" iconCls="icon_sure" otherProperties="minWidth: Common.config.buttonWidth.defaultButton"></ext:button>
								<ext:button id="return_Btn" text="返回" handler="function() {  Project.gotoPending();}" iconCls="icon_close" otherProperties="minWidth: Common.config.buttonWidth.defaultButton"></ext:button>
							</ext:buttons>
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
