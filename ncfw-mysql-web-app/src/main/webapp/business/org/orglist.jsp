<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="com.talkweb.ncfw.action.OrgAction" %>
<%@page import="com.talkweb.ncfw.entity.Org"%>
<%@ include file="/foundation/ext/jsp/taglib.jsp"%>
<%@ include file="/foundation/ext/jsp/workspace.jsp"%>
<%
	Org org = SecurityHelper.getCurrentUser().getOrg();
	User user = SecurityHelper.getCurrentUser().getUser();
	String roleId = user.getRoleid();
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>组织数据管理</title>
<script type="text/javascript" src="org.js"></script>
    <script type="text/javascript">
	var pageSize = Common.config.pageSize || 15;
	var queryOrgListUrl = sys.basePath + 'orgAction!queryOrg.do';
	var getOrgNodesUrl		= sys.basePath + "orgAction!createOrgTree.do";
	var DataDict = {
   		isShowArray : <ncframework:dictWrite dicttypeId="ORG_ISSHOW" type="array"/>,
		orgTypeArray : <ncframework:dictWrite dicttypeId="ORG_TYPE" type="array"/>,
		orgLevelArray: <ncframework:dictWrite dicttypeId="ORG_LEVEL" type="array"/>,
   		provinceCodeArray	: <ncframework:dictWrite dicttypeId="COMMON_PROVINCE" type="array"/>
   	};
	var LocalRenderer = {
   		isShow : function(showType){
   			return Common.renderer.dictRender (DataDict.isShowArray, showType);
   		},
 		orgType : function(orgType){
   			return Common.renderer.dictRender (DataDict.orgTypeArray, orgType);
   		},
		operateProvince	: function(value){
			return Common.renderer.dictRender(DataDict.provinceCodeArray, value);
		},
		orgLevel	: function(value){
			return Common.renderer.dictRender(DataDict.orgLevelArray, value);
		},
		provinceCode	: function (value) {
			return Common.renderer.dictRender (DataDict.provinceCodeArray, value);
   		}
   	};

	var orgRoot				= '<%=org.getParentorg()%>';
	var orgRootName			= LocalRenderer.provinceCode('<%=org.getProvinceNumber()%>');

	if('<%= roleId %>' == 'sysadmin') {
		orgRootName = '组织树';
		orgRoot = '<%= OrgAction.ALLORGROOT%>';
	}

	function onDeleteOrg(){
		var selectionModel = orgGrid.getSelectionModel();
		var count = selectionModel.getCount();
		if(count<1){
			Ext.Msg.alert("友情提示","请选择一条记录");
			return;
		}
		
		Ext.Msg.confirm("确认消息框", "此操作将删除组织信息，是否继续？", function(btn){
	 		if(btn == 'yes'){
				var record = selectionModel.getSelected();
				var orgid = record.get("orgid");
				Common.showProcessMsgBox('数据删除中，请稍后...');
			    Ext.Ajax.request({
					url: sys.basePath + 'orgAction!deleteOrg.do',
					timeout: 300000,
					params:{'org.orgid':orgid},
					success: function(response, options){
						Common.hideProcessMsgBox();
						var result = Ext.util.JSON.decode(response.responseText);
						if(result.success){
							Ext.Msg.show({
				                title : '成功提示',
				                msg : result.msg,
				                buttons : Ext.Msg.OK,
				                icon: Ext.MessageBox.INFO,
				                fn: function(){
				                	orgStore.reload();
				                }
				            });
				         } else {
				         	Ext.Msg.show({
				                title : '失败提示',
				                msg : result.msg,
				                buttons : Ext.Msg.OK,
				                icon: Ext.MessageBox.ERROR,
				                fn: function(){
				                }
				            });
				         }
					},
					failure:function(){Ext.MessageBox.hide();return;}
				});
		 	}
		});	
	}
	
	function submitSearchOrgHandler(){
		var thisForm = queryFormPanel.getForm();
		if (!thisForm.isValid()) {
			return;
		}
		orgStore.baseParams = {
			'orgname': Ext.getCmp("query.orgName").getValue().trim(),
			'provinceNumber': Ext.getCmp("query.provinceCode").getValue()
		};
    	orgStore.reload({params:{start: 0, limit: orgPagingBar.pageSize}});
	
	}
	
    </script>
</head>
  <body>
	<ext:ext onReady="initPage();">
	  	<ext:viewport layout="border" otherProperties="split: true">
	  		<ext:items>
	  		<ext:tabPanel region="center" border="false" frame="false" otherProperties="activeTab: 0">
	  			<ext:items>
	  				<ext:panel title="组织管理" layout="border" border="false" iconCls="icon_plugin">
	  					<ext:items>
	  						<%-- 组织树 --%>
		  				<ext:treePanel var="orgTree" id="orgTree" title="组织机构树" border="true" rootVisible="true" autoScroll="true" 
		  					width="230" useArrows="false" otherProperties="split: true" region="west" margins="3 0 0 0" 
		      				root='new Ext.tree.AsyncTreeNode({id: orgRoot, text : orgRootName, leaf: false, expanded: true })' 
		      				loader="new Ext.tree.TreeLoader({dataUrl : getOrgNodesUrl, listeners: { beforeload : function (thisLoader, node, callback ) {
		      						thisLoader.baseParams.parentId = node.id;
		      					}}
		      				})" collapsible="true">
		   				</ext:treePanel>
		   				<ext:panel var="orgPanel" layout="border" border="false" otherProperties="split: true" region="center"
	   						margins="3 0 0 0">
	   						<ext:items>
	  						<%-------------------------------------------------------------------------------------------------
								定义查询条件面板  
							-------------------------------------------------------------------------------------------------%>
							<ext:formPanel var="queryFormPanel" id="queryFormPanel" title="查询条件" collapsible="true" 
								labelAlign="left" frame="true" region="north" height="86" border="false" 
								labelWidth="66" otherProperties="split: true" margins="3 0 0 0">
								<ext:items>
									<ext:container layout="column" defaults="{width: 230, layout: 'form'}" lazyInit="true">
										<ext:items>
											<ext:container lazyInit="true">
												<ext:items>
													<ext:textField id="query.orgName" fieldLabel="组织名称" width="120">
													</ext:textField>
												</ext:items>
											</ext:container>
											<ext:container lazyInit="true">
												<ext:items>
													<ext:comboBox id="query.provinceCode"
														otherProperties="fieldLabel: '所属省份'"
														displayField="text" mode="local" triggerAction="all"
														selectOnFocus="true" forceSelection="true" width="100"
														valueField="value" value="" editable="false" allowBlank="true"
														store="new Ext.data.SimpleStore({
															fields : ['value', 'text'],
															data : Common.config.nullArray.concat(DataDict.provinceCodeArray)
														})">
													</ext:comboBox>
												</ext:items>
											</ext:container>
										
											<ext:container layout="table" lazyInit="true"
												defaults="{layout: 'form', border:false, columnWidth: 1.0}">
												<ext:items>
													<ext:button text="查询" iconCls="icon_search" style="margin-right:20px;" 
														handler="submitSearchOrgHandler" otherProperties="minWidth: Common.config.buttonWidth.defaultButton">
													</ext:button>
												</ext:items>
											</ext:container>
										</ext:items>
									</ext:container>
								</ext:items>
							</ext:formPanel>
							
							<%-------------------------------------------------------------------------------------------------
								定义字典类型列表  
							-------------------------------------------------------------------------------------------------%>
							<ext:gridPanel var="orgGrid" id="orgGrid" title="组织列表" region="center" iconCls="icon_grid" 
								border="true" viewConfig="{forceFit: true}">
								<%-- 定义org数据源 --%>
								<ext:store var="orgStore" remoteSort="true" sortInfo="{field: 'sort', direction: 'ASC'}"
									otherProperties="url: queryOrgListUrl">
									<ext:jsonReader root="data" totalProperty="totalProperty"
										record="Ext.data.Record.create([
											{name : 'orgid',type : 'string', mapping: 'orgid'},
											{name : 'orgname',type : 'string', mapping: 'orgname'},
											{name : 'orglevel',type : 'string', mapping: 'orglevel'},
											{name : 'isshow',type : 'string', mapping: 'isshow'},
											{name : 'orgtype',type : 'string', mapping: 'orgtype'},
											{name : 'parentorg',type : 'string', mapping: 'parentorg'},
											{name : 'sort',type : 'string', mapping: 'sort'},
											{name : 'isshow',type : 'string', mapping: 'isshow'},
											{name : 'orgsbname',type : 'string', mapping: 'orgsbname'},
											{name : 'provinceNumber',type : 'string', mapping: 'provinceNumber'}
										])">
									</ext:jsonReader>
								</ext:store>
								<%-- 定义org列表的列头 --%>
								<ext:columnModel id="orgCm">
									<ext:rowNumberer></ext:rowNumberer>
									<%--<ext:checkboxSelectionModel var="orgSm"></ext:checkboxSelectionModel>--%>
									<ext:columnModel header="组织编号" dataIndex="orgid" width="50" sortable="true" 
										listFlag="false" lazyInit="true"></ext:columnModel>
									<ext:columnModel header="组织名称" dataIndex="orgname" width="220" 
										sortable="true" listFlag="false" lazyInit="true"></ext:columnModel>
									<ext:columnModel header="组织类型" dataIndex="orgtype" width="120" 
										sortable="true" listFlag="false" lazyInit="true" renderer="LocalRenderer.orgType"></ext:columnModel>
									<ext:columnModel header="是否显示" dataIndex="isshow" width="90" 
										sortable="true" listFlag="false" lazyInit="true" renderer="LocalRenderer.isShow"></ext:columnModel>
									<ext:columnModel header="所属省份" dataIndex="provinceNumber" width="100" 
										sortable="true" listFlag="false" lazyInit="true" renderer="LocalRenderer.provinceCode"></ext:columnModel>
										
								</ext:columnModel>
								<%-- 定义字典类型列表的分页工具栏 --%>
								<ext:pagingToolbar key="bbar" var="orgPagingBar" displayInfo="true" store="orgStore" otherProperties="pageSize: pageSize">
									<ext:items>
										<ext:toolbarSeparator></ext:toolbarSeparator>
										
									</ext:items>
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
	  	<ext:window var="systemMenuWindow" id="systemMenuWindow"
			title="组织信息" width="460" autoHeight="true" 
		 	layout="fit" shadow="true" closeAction="hide"
			modal="true" closable="true" resizable="false">
			<ext:items>
			<ext:formPanel var="orgForm" id="orgForm" region="center" autoHeight="true"
				labelAlign="right" frame="true" border="false"
				labelWidth="96" buttonAlign="left" otherProperties="split: true, url:editOrgUrl">
				<ext:items>
					<ext:panel layout="form" defaults="{layout: 'form', anchor: '96%'}">
						<ext:items>
							<ext:hidden id="org.parentorg" name="org.parentorg"></ext:hidden>
							<ext:textField id="parentName" name="parentName" fieldLabel="上级组织" disabled="true" />
							
							<ext:hidden id="org.orgid" name="org.orgid" value="1"></ext:hidden>
							<ext:textField id="org.orgname" name="org.orgname" allowBlank="false"
								otherProperties="fieldLabel: Common.config.redStar + '组织名称'" maxLength="50">
							</ext:textField>
							
							<ext:comboBox id="provinceCode"
								otherProperties="fieldLabel: Common.config.redStar + '所属省份'"
								displayField="text" mode="local" triggerAction="all"
								selectOnFocus="true" forceSelection="true" width="130"
								valueField="value" value="" editable="false" allowBlank="true"
								hiddenName="org.provinceNumber"
								store="new Ext.data.SimpleStore({
									fields : ['value', 'text'],
									data : Common.config.nullArray.concat(DataDict.provinceCodeArray)
								})">
							</ext:comboBox>
							
							<ext:comboBox otherProperties="fieldLabel: Common.config.redStar + '组织性质'" 
								hiddenName="org.orgtype" mode="local" triggerAction="all" 
								valueField="value" displayField="text" readOnly="true" resizable="true"
								store="new Ext.data.SimpleStore({
									fields : ['value', 'text'],
									data : DataDict.orgTypeArray
								})">
							</ext:comboBox>
							
							<ext:comboBox otherProperties="fieldLabel: Common.config.redStar + '组织级别'" 
								hiddenName="org.orglevel" allowBlank="false" readOnly="true" resizable="true"
								mode="local" triggerAction="all" valueField="value" displayField="text"
								store="new Ext.data.SimpleStore({
									fields: ['value', 'text'], data: DataDict.orgLevelArray
								})">
							</ext:comboBox>
							
							<ext:comboBox fieldLabel="是否显示" value="SHOW"
								hiddenName="org.isshow"
								store="new Ext.data.SimpleStore({
									fields: ['value', 'text'], data: DataDict.isShowArray
								})"
								mode="local" triggerAction="all" valueField="value" displayField="text"
								readOnly="true" resizable="true">
							</ext:comboBox>
							
							<ext:textField id="org.sort" name="org.sort" fieldLabel="排序号" maxLength="5">
							</ext:textField>
							<ext:textField id="org.orgsbname" name="org.orgsbname" fieldLabel="部门申报名称">
							</ext:textField>
						</ext:items>
					</ext:panel>
				</ext:items>
			</ext:formPanel>
			</ext:items>
			<ext:buttons>
				<ext:button text="保存" iconCls="icon_save" handler="onSave"
					otherProperties="minWidth: Common.config.buttonWidth.defaultButton">
				</ext:button>
				<ext:button text="关闭" iconCls="icon_close" handler="onClose"
					otherProperties="minWidth: Common.config.buttonWidth.defaultButton">
				</ext:button>
			</ext:buttons>
		</ext:window>
	  </ext:ext>
  </body>
  
</html>
    