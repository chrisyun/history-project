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
	<title>国家文化创新工程项目</title>
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
		if(openType == 'pending') {
			fileMan.editMode = 'edit';
		} else if(openType == 'pendingZQ' || openType == 'pendingJX') {
			Ext.getCmp('fileUploadButton').hide();
		} else if(openType == 'searchList') {
			Ext.getCmp('fileUploadButton').hide();
			Project.gotoUrl = sys.basePath + 'business/project/projectList.jsp';
		} else if(openType == 'searchStore') {
			Ext.getCmp('fileUploadButton').hide();
			Project.gotoUrl = sys.basePath + 'business/project/projectStore.jsp';
		} else {
			//新建页面
			fileMan.editMode = 'edit';
		}
		
		flowBaseInfo.hide();
		
		if(!Ext.isEmpty(pid)) {
			Project.queryProject(pid);

			//增加中期检查
			Project.queryMedProjectId(pid);
			//增加结项检查
			Project.queryFinProjectId(pid);

			//显示意见
			searchMinds(pid,"t_project","minds-div");
		} else {
			//新建
			Project.generateProjectNum("GJWHCXGCXM");
			Ext.getCmp('sborg').setValue(sbDeptName);
		}

		if(roleId != 'projectsp' || 'ZB' != proprovincecode) {
			Ext.getCmp('pw_panel').hide();
		}
	}

	function initPageShow() {
		//待办打开
		if(openType == 'pending') {
			//非起草环节
			if(Ext.getCmp('curActivityName').getValue() != 'DRAFT') {
				flowBaseInfo.show();
				Ext.getCmp('sb_Btn').hide();
				Ext.getCmp('sp_ok_Btn').show();
				Ext.getCmp('sp_not_Btn').show();
				Ext.getCmp('return_Btn').show();
			} else {
				Ext.getCmp('return_Btn').show();
			}
		} else if(openType == 'pendingZQ' || openType == 'pendingJX') {
			Ext.getCmp('sb_Btn').hide();
		} else if(openType == 'searchList') {
			//申报项目查询打开
			flowBaseInfo.show();
			Ext.getCmp('return_Btn').show();
			Ext.getCmp('sb_Btn').hide();
		} else if(openType == 'searchStore') {
			//项目成果库查询打开
			flowBaseInfo.show();
			Ext.getCmp('return_Btn').show();
			Ext.getCmp('sb_Btn').hide();

			//如果是起草人，则显示中间检查按钮和结项按钮
			if(Ext.getCmp('creater').getValue() == user 
					&& Ext.getCmp('curActivityName').getValue() == "FINISH"
					&& Ext.getCmp('pstatus').getValue() != "XMJX") {
				Ext.getCmp('zq_pro_Btn').show();
				Ext.getCmp('jx_pro_Btn').show();
			}

			//如果是文化部用户，并且为审批人
			if(roleId == 'projectsp' && 'ZB' == proprovincecode) {
				Ext.getCmp('up_pro_Btn').show();
			}
			
		} 
	}
    </script>
</head>
  <body>
    <ext:ext onReady="initPage();">
	  	<ext:viewport layout="border" otherProperties="split: true">
	  		<ext:items>
	  		<ext:tabPanel region="center" border="false" frame="false" otherProperties="activeTab: 0" var="tabPanel">
	  			<ext:items>
  				<ext:panel title="国家文化创新工程项目" layout="border" border="false" iconCls="icon_plugin">
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
											xtype="fieldset" autoHeight="true" title="申报须知" collapsible="true" frame="true">
											<ext:html><span style="font-size: 13px;">&nbsp;&nbsp;&nbsp;&nbsp;为确保项目申报规范，准确，请注意以下事项：<br>
													  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;1、若申报过程中发现屏幕右侧滚动条无法显示，请调整计算机屏幕分辨率至1024*768或1366*768。<br>      
     	 											  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;2、首先请填写项目基本信息。项目编号由系统自动生成无需填写；承担单位请填写本单位的全称，必须与单位行政公章名称一致；共建部门只需重点项目填写，为项目所在地人民政府（须报经同意，并在纸质申请书中签署意见，加盖公章）；申报部门由系统自动生成无需填写；申报日期为项目申报时的日期。<br>
    												  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;3、项目基本信息填写完成后，请下载国家文化创新工程申报书，<a href="<%=basePath%>business/国家文化创新工程项目申报书.doc" target="blank">点击此处下载</a>。认真填写《国家文化创新工程项目申报书》。<br>
     												  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;4、填写完成后，点击文件上传按钮，在弹出的文件上传界面，上传填写好的《国家文化创新工程项目申报书》及相关附件，相关附件文件大小不得超过10M ；申报书的文件名为单位名称。<br>
     												  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;5、流程意见中填写意见一栏可以根据申报项目实际情况填写需提醒资料审核部门注意的事项，也可以选择不填写。最后点击项目申报按钮进行申报。<br>
     												  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;6、在线申报完成后，请将填写好的 《国家文化创新工程项目申报书》打印后，签字并加盖相应公章一式三份寄送相应资料审核部门。文化部各司局、各直属单位，国家文化创新研究中心，原文化部直属高等艺术院校寄送到文化部科技司，其他单位寄送到所在省、自治区、直辖市文化厅（局）。 </span></ext:html>
										</ext:fieldSet>
									
									
									
										<ext:fieldSet layout="column" defaults="{columnWidth: 1, layout: 'form'}" lazyInit="true" 
											xtype="fieldset" autoHeight="true" title="项目基本信息" collapsible="true" var="projectBaseInfo" 
											frame="true">
											<ext:items>
												<ext:hidden name="project.pid" id="pid"></ext:hidden>
												<ext:hidden name="project.creater" id="creater"></ext:hidden>
												<ext:hidden name="project.proprovincecode" id="proprovincecode"></ext:hidden>
												<ext:hidden name="project.pmtype" id="pmtype" value="GJWHCXGC"></ext:hidden>
												
												<ext:container lazyInit="true">
													<ext:items>
														<ext:textField id="pname" name="project.pname" fieldLabel="项目名称<span class='red'>(必填)</span>" allowBlank="false" otherProperties="anchor: Project.fieldAnchor">
														</ext:textField>
													</ext:items>
												</ext:container>
														
												<ext:container lazyInit="true">
													<ext:items>
														<ext:comboBox id="psbtype" hiddenName="project.psbtype"
															fieldLabel="申报类别<span class='red'>(必填)</span>"
															displayField="text" mode="local" triggerAction="all"
															selectOnFocus="true" forceSelection="true" width="300"
															valueField="value" value="" editable="false" allowBlank="false"
															store="new Ext.data.SimpleStore({
																fields : ['value', 'text'],
																data : Common.config.nullArray.concat(DataDict.projectSBTypeArray)
															})">
														</ext:comboBox>
													</ext:items>
												</ext:container>
												
												<ext:panel lazyInit="true">
													<ext:items>
														<ext:textField id="pslnumber" name="project.pslnumber" readOnly="true" fieldLabel="项目编号" allowBlank="true" otherProperties="anchor: Project.fieldAnchor">
														</ext:textField>
													</ext:items>
												</ext:panel>
												<ext:panel lazyInit="true">
													<ext:items>
														<ext:textField id="sborg" name="project.sborg" fieldLabel="申报部门<span class='red'>(必填)</span>"  allowBlank="false" readOnly="true" otherProperties="anchor: Project.fieldAnchor">
														</ext:textField>
													</ext:items>
												</ext:panel>
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
												
												<ext:container lazyInit="true" id="pw_panel" otherProperties="hidden:false">
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
								<ext:button id="sb_Btn" text="项目申报" handler="function() {Project.submintProject(pid,'NONSP');}" iconCls="icon_sure" otherProperties="minWidth: Common.config.buttonWidth.defaultButton"></ext:button>
								<ext:button id="sp_ok_Btn" text="审批通过" hidden="true" handler="function() {Project.submintProject(pid,'SUCCESS');}" iconCls="icon_sure" otherProperties="minWidth: Common.config.buttonWidth.defaultButton"></ext:button>
								<ext:button id="zq_pro_Btn" text="项目中期检查申报" hidden="true" handler="function() {Project.projectMed(pid);}" iconCls="icon_sure" otherProperties="minWidth: Common.config.buttonWidth.defaultButton"></ext:button>
								<ext:button id="sp_not_Btn" text="审批不通过" hidden="true" handler="function() {Project.submintProject(pid,'FAILURE');}" iconCls="icon_unUse" otherProperties="minWidth: Common.config.buttonWidth.defaultButton"></ext:button>
								<ext:button id="jx_pro_Btn" text="项目结项申报" hidden="true" handler="function() {Project.projectFin(pid);}" iconCls="icon_sure" otherProperties="minWidth: Common.config.buttonWidth.defaultButton"></ext:button>
								<ext:button id="up_pro_Btn" text="修改" hidden="true" handler="function() {Project.updateProject();}" iconCls="icon_sure" otherProperties="minWidth: Common.config.buttonWidth.defaultButton"></ext:button>
								<ext:button id="return_Btn" text="返回" hidden="true" handler="function() { Project.gotoPending();}" iconCls="icon_close" otherProperties="minWidth: Common.config.buttonWidth.defaultButton"></ext:button>
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
