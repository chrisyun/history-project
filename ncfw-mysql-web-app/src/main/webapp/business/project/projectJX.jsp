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
	String fpid = request.getParameter("fpid")==null?"":request.getParameter("fpid");
	String openType = request.getParameter("openType")==null?"":request.getParameter("openType");
	
	String sbDept = "";
	if("ZB".equals(org.getProvinceNumber()) && "DW".equals(org.getOrgtype())) {
		sbDept = user.getUsername();
	} else {
		sbDept = org.getOrgsbname()==null?"":org.getOrgsbname();
	}
%>
<html>
<head>
	<title>项目结项申报</title>
	<link rel="stylesheet" type="text/css" href="<%=basePath%>foundation/ext/ext-plugins/lovcombo-1.0/css/Ext.ux.form.LovCombo.css"/>
	<script type="text/javascript" src="<%=basePath%>foundation/ext/ext-plugins/lovcombo-1.0/js/Ext.ux.form.LovCombo.js"></script>
	<script type="text/javascript" src="<%=basePath%>foundation/ext/ext-plugins/TreeField.js"></script>
	<script type="text/javascript" src="<%=basePath%>foundation/ext/ext-plugins/SearchField.js"></script>
	<link rel="stylesheet" type="text/css" href="<%=basePath%>foundation/ext/ext-plugins/file-upload.css" />
	<script type="text/javascript" src="<%=basePath%>foundation/ext/ext-plugins/FileUploadField.js"></script>
	<script type="text/javascript" src="<%=basePath%>business/file/fileManager.js"></script>
	<script type="text/javascript" src="<%=basePath%>business/project/FinProject.js"></script>
	<script type="text/javascript" src="./Minds.js"></script>
	
	<script type="text/javascript">

	var orgType = '<%=org.getOrgtype() %>';
	var pid = '<%=pid %>';
	var user = '<%=user.getUserid()%>';
	var fpid = '<%=fpid%>';
	var openType = '<%=openType%>';
	var roleId	= '<%=roleId%>';
	var proprovincecode = '<%=user.getProvincenumber()%>';
	var sbDeptName = '<%=sbDept%>';
	
	var fileMan = new FileManager("fileListPanel",fpid,"t_fin_project","view","common");
    //数据字典
    var DataDict = {
    	flowStatusArray				: <ncframework:dictWrite dicttypeId="FLOW_STATUS" type="array"/>,
    	projectStatusArray			: <ncframework:dictWrite dicttypeId="PROJECT_STATUS" type="array"/>,
    	projectTypeArray			: <ncframework:dictWrite dicttypeId="PROJECT_TYPE" type="array"/>,
    	activityNameArray			: <ncframework:dictWrite dicttypeId="FLOW_ACTIVITYNAME" type="array"/>,
    	projectZTypeArray			: <ncframework:dictWrite dicttypeId="PROJECT_ZTYPE" type="array"/>,
    	projectSBTypeArray			: <ncframework:dictWrite dicttypeId="PROJECT_SB_TYPE" type="array"/>,
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
		FinProject.queryFinProjectBypid(pid,fpid);

		//显示意见
		searchMinds(fpid,"t_fin_project","minds-div");
	}

	function initPageShow() {
		Ext.getCmp('sborg').setValue(sbDeptName);
		var issp = Ext.getCmp('issp').getValue();
		if((issp == "YES" && openType == "pendingJX")
				|| openType == "draft") {
			//用户为起草人
			if(user == Ext.getCmp('creater').getValue()) {
				flowBaseInfo.hide();
				Ext.getCmp('pstatus').setValue('XMJXZ');
				Ext.getCmp('curActivityName').setValue('DRAFT');
			} else {
				Ext.getCmp('sp_ok_Btn').show();
				Ext.getCmp('sp_not_Btn').show();
				Ext.getCmp('return_Btn').show();
				Ext.getCmp('sb_Btn').hide();
			}
		} else if(issp == "YES") {
			Ext.getCmp('fileUploadButton').hide();
			Ext.getCmp('sb_Btn').hide();
		} else {
			flowBaseInfo.hide();
			Ext.getCmp('fileUploadButton').hide();
			Ext.getCmp('sb_Btn').hide();
		}

		if(openType == "searchStore" 
			&& roleId == 'projectsp' 
				&& 'ZB' == proprovincecode) {
			Ext.getCmp('up_pro_Btn').show();
		}
	}

	function uploadCommon() {
		FileConstant.fileManager = fileMan;
		FileConstant.showUploadWindow();
	}
    </script>
</head>
  <body>
    <ext:ext onReady="initPage();">
	  	<ext:viewport layout="border" otherProperties="split: true">
	  		<ext:items>
  				<ext:panel layout="border" border="false" iconCls="icon_plugin" region="center">
  					<ext:items>
	   					<%-- 列表面板 --%>
	   					<ext:panel var="mainPanel" layout="border" border="false" otherProperties="split: true" region="center"
	   						margins="3 0 0 0" otherProperties="buttonAlign: 'center'">
	   						<ext:items>
								<ext:formPanel var="submitPrjFormPanel" id="submitPrjFormPanel" collapsible="false"
									labelAlign="right" frame="true" region="center" border="false" autoScroll="true"
									labelWidth="200" otherProperties="split: true"  autoWidth="true">
									<ext:items>
										<ext:fieldSet layout="column" defaults="{columnWidth: 1, layout: 'form'}" lazyInit="true" 
											xtype="fieldset" autoHeight="true" title="基本信息" collapsible="true" var="projectBaseInfo" 
											frame="true">
											<ext:items>
												<ext:hidden name="project.pid" id="pid"></ext:hidden>
												<ext:hidden name="project.creater" id="creater"></ext:hidden>
												<ext:hidden name="project.proprovincecode" id="proprovincecode"></ext:hidden>
												
												<ext:hidden name="finproject.pid" id="_pid"></ext:hidden>
												<ext:hidden name="finproject.fpid" id="fpid"></ext:hidden>
												<ext:hidden name="finproject.issp" id="issp"></ext:hidden>
												
												<ext:panel lazyInit="true">
													<ext:items>
														<ext:textField id="pname" name="finproject.pname"  fieldLabel="项目名称" allowBlank="true" otherProperties="anchor: FinProject.fieldAnchor">
														</ext:textField>
													</ext:items>
												</ext:panel>
												<ext:panel lazyInit="true">
													<ext:items>
														<ext:textField id="cdorg" name="finproject.cdorg" fieldLabel="承担单位<span class='red'>(必填)</span>"  allowBlank="false" otherProperties="anchor: FinProject.fieldAnchor">
														</ext:textField>
													</ext:items>
												</ext:panel>
												
												<ext:container layout="column" defaults="{width: 465, layout: 'form'}" lazyInit="true">
													<ext:items>
														<ext:panel lazyInit="true">
															<ext:items>
																<ext:textField id="pmanager" name="finproject.pmanager"  width="300" fieldLabel="项目负责人<span class='red'>(必填)</span>"  allowBlank="false" otherProperties="anchor: FinProject.fieldAnchor">
																</ext:textField>
															</ext:items>
														</ext:panel>
														<ext:panel lazyInit="true">
															<ext:items>
																<ext:textField id="tel" name="finproject.tel" width="285" fieldLabel="联系电话<span class='red'>(必填)</span>"  allowBlank="false" otherProperties="anchor: FinProject.fieldAnchor">
																</ext:textField>
															</ext:items>
														</ext:panel>
													</ext:items>
												</ext:container>
												
												
												<ext:panel lazyInit="true">
													<ext:items>
														<ext:textField id="sborg" name="medproject.sborg" fieldLabel="申报部门<span class='red'>(必填)</span>"  allowBlank="false" readOnly="true" otherProperties="anchor: Project.fieldAnchor">
														</ext:textField>
													</ext:items>
												</ext:panel>
												<ext:panel lazyInit="true">
													<ext:items>
														<ext:dateField fieldLabel="申报日期<span class='red'>(必填)</span>"
															name="finproject.sbdate" 
															id="sbdate" width="200" format="Y-m-d"
															allowBlank="true" cls="float_left" itemCls="float_left"
															clearCls="allow_float" emptyText="请选择日期"
															otherProperties="shadow : true,editable: false ,maxValue: new Date(), 
															value: new Date().add(Date.MONTH,0)">
														</ext:dateField>
													</ext:items>
												</ext:panel>
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
													handler="uploadCommon" otherProperties="minWidth: Common.config.buttonWidth.defaultButton">
												</ext:button>
											</ext:buttons>
										</ext:fieldSet>
										
										<ext:fieldSet layout="column" defaults="{columnWidth: 1, layout: 'form'}" lazyInit="true" 
											xtype="fieldset" autoHeight="true" title="流程意见" collapsible="true" frame="true">
											<ext:items>
												<ext:container layout="column" defaults="{layout: 'form'}" lazyInit="true">
													<ext:items>
														<ext:panel lazyInit="true" html="<div id='minds-div'><div>"></ext:panel>
														<ext:container lazyInit="true">
															<ext:items>
																<ext:textArea  fieldLabel='填写意见' id='minds' name="minds" width="640" height="70"></ext:textArea>
															</ext:items>
														</ext:container>
													</ext:items>
												</ext:container>
											</ext:items>
										</ext:fieldSet>
										
										<ext:fieldSet layout="column" defaults="{columnWidth: 1, layout: 'form'}" lazyInit="true" 
											xtype="fieldset" autoHeight="true" title="审批信息" collapsible="true" var="flowBaseInfo" 
											frame="true">
											<ext:items>
												<ext:container layout="column" defaults="{width: 450, layout: 'form'}" lazyInit="true">
													<ext:items>
														<ext:container lazyInit="true">
															<ext:items>
																<ext:textField  fieldLabel='当前环节' id='curActivityNameShow' width="200" readOnly="true"></ext:textField>
																<ext:hidden id="curActivityName" name="project.curActivityName"></ext:hidden>
															</ext:items>
														</ext:container>
														<ext:container lazyInit="true">
															<ext:items>
																<ext:textField  fieldLabel='项目状态' id='pstatusShow' width="200" readOnly="true"></ext:textField>
																<ext:hidden id="pstatus" name="project.pstatus"></ext:hidden>
															</ext:items>
														</ext:container>
														<ext:container lazyInit="true">
															<ext:items>
																<ext:textField  fieldLabel='审批状态' id='spstatusShow' width="200" readOnly="true"></ext:textField>
																<ext:hidden id="spstatus" name="project.spstatus"></ext:hidden>
															</ext:items>
														</ext:container>
													</ext:items>
												</ext:container>
											</ext:items>
										</ext:fieldSet>
									</ext:items>
								</ext:formPanel>
	   						</ext:items>
	   						
	   						<ext:buttons>
								<ext:button id="sb_Btn" text="项目结项申报" handler="function() {FinProject.submitFinProject(fpid,'NONSP');}" iconCls="icon_sure" otherProperties="minWidth: Common.config.buttonWidth.defaultButton"></ext:button>
								<ext:button id="sp_ok_Btn" text="审批通过" hidden="true" handler="function() {FinProject.submitFinProject(fpid,'SUCCESS');}" iconCls="icon_sure" otherProperties="minWidth: Common.config.buttonWidth.defaultButton"></ext:button>
								<ext:button id="sp_not_Btn" text="审批不通过" hidden="true" handler="function() {FinProject.submitFinProject(fpid,'FAILURE');}" iconCls="icon_unUse" otherProperties="minWidth: Common.config.buttonWidth.defaultButton"></ext:button>
								<ext:button id="up_pro_Btn" text="修改" hidden="true" handler="function() { FinProject.update();}" iconCls="icon_sure" otherProperties="minWidth: Common.config.buttonWidth.defaultButton"></ext:button>
								<ext:button id="return_Btn" text="返回" hidden="true" handler="function() { FinProject.gotoPending();}" iconCls="icon_close" otherProperties="minWidth: Common.config.buttonWidth.defaultButton"></ext:button>
							</ext:buttons>
	   					</ext:panel>
   					</ext:items>
   				</ext:panel>
	  			</ext:items>
	  	</ext:viewport>
    </ext:ext>
  </body>
  
</html>
