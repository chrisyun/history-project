<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page language="java" import="com.talkweb.ncframework.pub.utils.tree.TreeUtils" pageEncoding="UTF-8"%>
<%@ include file="/foundation/ext/jsp/taglib.jsp"%>
<%@ include file="/foundation/ext/jsp/workspace.jsp"%>
<html>
<head>
	<link rel="stylesheet" type="text/css" href="<%=basePath%>foundation/ext/ext-plugins/lovcombo-1.0/css/Ext.ux.form.LovCombo.css"/>
	<script type="text/javascript" src="<%=basePath%>foundation/ext/ext-plugins/MultiSelectTree.js"></script>
	<script type="text/javascript" src="<%=basePath%>foundation/ext/ext-plugins/TwinPanelController.js"></script>
	<script type="text/javascript" src="<%=basePath%>foundation/ext/ext-plugins/RadioGroupGetValue.js"></script>
	<script type="text/javascript" src="<%=basePath%>foundation/ext/ext-plugins/Tree3.js"></script>
	<script type="text/javascript" src="<%=basePath%>foundation/ext/ext-plugins/SearchField.js"></script>
	<script type="text/javascript" src="<%=basePath%>foundation/ext/ext-plugins/lovcombo-1.0/js/Ext.ux.form.LovCombo.js"></script>
	<script type="text/javascript" src="./RoleUser.js"></script>
	<script type="text/javascript">
	var listRoleUrl  			= sys.basePath + 'rights/roleAction!listRole.do';
	var queryRoleUrl  			= sys.basePath + 'rights/roleAction!queryRole.do';
	var inputRoleUrl     		= sys.basePath + 'rights/roleAction!inputRole.do';
	var deleteRoleUrl   		= sys.basePath + 'rights/roleAction!deleteRole.do';
	var getSystemMenuTree3Url 	= sys.basePath + "rights/systemMenuAction!createMenuTree3.do";
	var getRoleAuthMenuDataUrl 	= sys.basePath + 'authAction!queryMenu.do';
    var treeRootId			  	= "<%=TreeUtils.treeRootId%>";

	var saveRoleAuthDataUrl	    = sys.basePath + 'authAction!saveRoleAuthData.do';

	var pageSize = 15;
	var currentRoleId;			//当前角色ID
	var currentRoleName;        //当前角色名称
	var currentRoleId_menu;		//当前角色ID-菜单 
	var currentActiveNode;		//当前活动节点
	var currentOptype;			//当前操作类型

	var LocalRenderer = {
		operate : function (value) {
			return '<div align="center"><a class="link" href="javascript: onAuth();">授权</a></div>';
		},
		userlist : function (value) {
			return '<div align="center">'
				+ 	'<a class="link" href="javascript: RoleUser.viewUserList(\'' + value + '\');">成员列表</a>' 
				+ '</div>';
		},
		remark	: function (remark, metadata, record, rowIndex, colIndex) {
			return Common.renderer.detailRender(remark, cm, colIndex);
		}
	};

	//角色授权动作
	function onAuth () {
		var roleRecord 	= systemRoleGrid.getSelectionModel().getSelected();
		var roleId 		= roleRecord.get("roleId");
		var roleName 	= roleRecord.get("roleName");
		currentRoleName = roleName;
		authPanel.setTitle("授权面板（当前角色：" + roleName + "）");
		currentRoleId 	= roleId;
		var currentAcitiveAuthTree = getCurrentAcitiveAuthTree();
		if (currentAcitiveAuthTree == menuTree3) {//菜单授权三态树
			currentRoleId_menu = roleId;
			initMenuAuthData();
		}
	}

	//初始化菜单授权数据
	function initMenuAuthData () {
		//获取角色权限信息
 		Ext.Ajax.request({
			url: getRoleAuthMenuDataUrl,
			timeout: 300000,
			params:{roleid: currentRoleId},
			success: function(response, options){
				var menuArray = Ext.util.JSON.decode(response.responseText);
				changeAuthMenuTree3NodesChecked(menuArray);
			},
			failure:function(){return;}
		});
	}
	
	//更改菜单权限三态树勾选状态
	function changeAuthMenuTree3NodesChecked(menuArray){
		var rootNode 	= menuTree3.getNodeById(treeRootId);
	    var checkNodes 	= rootNode.getUI().getCheckedNodes(rootNode);
	    //先设置所有叶子节点为未选择状态
	    for(var i = 0;i < checkNodes.length;i++){
	         checkNodes[i].getUI().setChecked(checkNodes[i], false);
	    }
	    if(!Ext.isEmpty(menuArray)){
		    //勾选以获得权限的叶子节点
		    for(var i = 0;i < menuArray.length;i++){
   				var menuNode = menuTree3.getNodeById(menuArray[i].menuId);
		        if(!Ext.isEmpty(menuNode)){
			        if(menuNode.isLeaf()){
		        		menuNode.getUI().setChecked(menuNode, true);
			        }
			    }
		    }
	    }
	}

	//获取当前活动的授权树面板
	function getCurrentAcitiveAuthTree () {
		return authTabPanel.getActiveTab();
	}

	//保存角色授予的菜单数据
	function saveRoleAuthMenuData () {
		var rootNode 	= menuTree3.getNodeById(treeRootId);
	    var checkNodes 	= rootNode.getUI().getCheckedNodes(rootNode);
	    var resIds 		= "";
	    var resNames 	= "";
	    for(var i = 0;i < checkNodes.length;i++){
	    	resIds 		+= checkNodes[i].id + ",";
	    	resNames 	+= checkNodes[i].text + ",";
	    }
	    submitRoleAuthData(currentRoleId_menu, resIds, 'menu');
	}

	function submitRoleAuthData (roleId, resIds, resType) {
		if (Ext.isEmpty(roleId)) {
			Ext.Msg.alert("提示信息", "请先选择角色！");
			return;
		}
		Common.showProcessMsgBox('数据保存中，请稍后...');
	 	Ext.Ajax.request({
   			url : saveRoleAuthDataUrl,
   		   	method : 'POST',
   		   	params: {
   		   		'roleId': roleId, 
   		   		'resIds': resIds,
   		   		'resType': resType
   		   	},
   		   	success: function (response, options) {
   		   		Common.hideProcessMsgBox();
   				var result = Ext.util.JSON.decode(response.responseText);
   		   		if (result.success) {
   		        	Ext.topShow.msg('成功提示', result.msg);
   			   	} else {
   			   		Ext.Msg.show({
       	                title: '失败提示',
       	                msg: result.msg,
       	                width: 180,
       	                modal: false,
       	                buttons : Ext.Msg.OK,
       	                icon: Ext.MessageBox.ERROR
       	            });
       			}
   		   	},
   		   	failure: function () {
				Ext.MessageBox.hide();
   			   	Ext.Msg.show({
   	                title: '失败提示',
   	                msg: '保存授权信息时发生异常.',
   	                width: 180,
   	                modal: false,
   	                buttons : Ext.Msg.OK,
   	                icon: Ext.MessageBox.ERROR
   	            });
   		   	}
   		});
	}

	//ajax请求对象
   	function ExtAjax(url, params, grid){
   		var extObject = 
   			Ext.Ajax.request({
   				url: url,
   				timeout: 60000,
   				params: params,
   				success: function(response, options){
   		   			Common.hideProcessMsgBox();
   					var result = Ext.util.JSON.decode(response.responseText);
   					if(result.success){
   						if(grid){
							grid.getStore().reload();
		               	}
   						Ext.Msg.show({
   			                title : '成功提示',
   			                msg : result.msg,
   			                buttons : Ext.Msg.OK,
   			                icon: Ext.MessageBox.INFO,
   			                fn: function(){}
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
   				failure:function(){Common.hideProcessMsgBox();return;}
   			});
   	    return extObject;
   	}

	/**
	 * 新建角色
	 */
	function onAdd () {
		var thisForm = roleForm.getForm();
		currentOptype = Common.optype.add;
		systemRoleWindow.show();
		systemRoleWindow.setTitle("新增角色");
		thisForm.reset();
        Role.initTransferRoles('');
	}
	/**
	 * 修改角色
	 */
	function onModify () {
		var thisForm = roleForm.getForm();
		currentOptype = Common.optype.update;
		if (!systemRoleGrid.getSelectionModel().hasSelection()) {
            Ext.Msg.alert("提示:", "请选择要修改的记录!");
            return;
        }
        if (systemRoleGrid.getSelectionModel().getSelections().length > 1) {
            Ext.Msg.alert("提示:", "请选择一条记录进行修改!");
            return;
        }
        var record = systemRoleGrid.getSelectionModel().getSelected();
        var values = [
        	{id: 'roleId', 				value: record.get("roleId")},
        	{id: 'roleName', 			value: record.get("roleName")},
        	{id: 'remark', 				value: record.get("remark")},
        	{id: 'transferRolesLimit', 	value: record.get("transferRolesLimit")}
        ];
        systemRoleWindow.show();
        systemRoleWindow.setTitle("修改角色");
        thisForm.setValues(values);
        Role.initTransferRoles(record.get("transferRolesLimit"));
	}

	/**
	 * 删除角色
	 */
	function onDelete () {
		if (!systemRoleGrid.getSelectionModel().hasSelection()) {
            Ext.Msg.alert("提示:", "请选择要删除的记录!");
            return;
        }
        
		Ext.Msg.confirm("确认提示", "角色删除后无法恢复, 是否继续?",function(btn){
	 		if(btn == 'yes'){
	 			var selects = systemRoleGrid.getSelectionModel().getSelections();
	    		var ajaxParams = {'roleId': selects[0].get("roleId")};
	    		Common.showProcessMsgBox('正在删除系统角色，请稍后...');
	    		ExtAjax(deleteRoleUrl, ajaxParams, systemRoleGrid);
	 		}
		});
	}

	/**
	 * 保存角色
	 */
	/**
	 * 保存用户组
	 */
	function onSaveRole () {
		var thisForm = roleForm.getForm();
		if(!thisForm.isValid()){
			return;
		}
		Common.showProcessMsgBox();
        thisForm.submit({
			timeout: 60000,
		 	success: function(form, action) {
   		   		Common.hideProcessMsgBox();
		 		systemRoleGrid.getStore().reload();
		 		systemRoleWindow.hide();
	        	Ext.topShow.msg('成功提示', action.result.msg);
			},
			failure: function(form, action) {
   		   		Common.hideProcessMsgBox();
	           	Ext.Msg.show({
	                title : '失败提示',
	                msg : action.result.msg,
	                buttons : Ext.Msg.OK,
	                icon: Ext.MessageBox.ERROR
	            });
			}
        });
	}

	
	var Role = {
		transferRolesLimit 				: null,
		roleStore 			: null
	};
    Role = {
   		getRoleStore : function () { 
			if (this.roleStore == null) {
				this.roleStore = new Ext.data.Store({
					url: sys.basePath + 'rights/roleAction!queryRoleForAuth.do',
					autoLoad : false, remoteSort: false,
					reader: new Ext.data.JsonReader({
						id: 'roleId'
					},
					Ext.data.Record.create([
			       		{name: 'roleId', type: 'string', mapping: 'roleId'},
			       		{name: 'roleName', type: 'string', mapping: 'roleName'}
					]))
				});
			}
			return this.roleStore;
		},
		initTransferRoles : function (transferRolesLimit) {
			Role.transferRolesLimit = transferRolesLimit;
			if (Role.transferRolesLimit == null) {
				return;
			}
			this.roleStore.addListener('load', function (thisStore, records, options) {
				if (records == null || records.length == 0) {
					return;
				}
				Ext.getCmp('transferRolesLimit').setValue(Role.transferRolesLimit);
			});
			if (Role.getRoleStore().getCount() > 0) {
				Ext.getCmp('transferRolesLimit').setValue(Role.transferRolesLimit);
			} else {
				this.loadRoleStore();
			}
		},
		loadRoleStore : function () {
			Role.getRoleStore().load();
		}
   	};

	function onClose () {
		systemRoleWindow.hide();
		roleForm.getForm().reset();
	}
	
	//初始化页面信息
	function initPage () {
		var store = systemRoleGrid.getStore();
		store.load({params:{start: 0, limit: gridPagingBar.pageSize}});

		authTabPanel.addListener("tabchange", function (thisTabPanel, panel) {
			if (Ext.isEmpty(currentRoleId)) {
				return;
			}
			if (panel == menuTree3) {
				if (currentRoleId_menu != currentRoleId) {
					currentRoleId_menu = currentRoleId;
					initMenuAuthData();
				}
			}
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
	  				<ext:panel title="系统角色管理" layout="border" border="false" iconCls="icon_plugin">
	  					<ext:items>
  						<%-------------------------------------------------------------------------------------------------
							角色列表
						-------------------------------------------------------------------------------------------------%>
							<ext:gridPanel var="systemRoleGrid" title="系统角色列表" region="center" border="true" loadMask="true" 
								otherProperties="split: true" enableColumnMove="false" margins="3 0 0 0" 
								 cm="cm" autoExpandColumn="remarkCm"
								sm="roleSm" stripeRows="true" iconCls="icon_grid"  viewConfig="{forceFit: true}"
								listeners="{cellclick : function ( thisGrid, rowIndex, columnIndex, ev ) {}}">
								<ext:tbar>
									<ext:toolbarButton text="新建" iconCls="icon_add" handler="onAdd"
										otherProperties="minWidth: Common.config.buttonWidth.minButton">
									</ext:toolbarButton>
									<ext:toolbarSeparator></ext:toolbarSeparator>
									<ext:toolbarButton text="修改" iconCls="icon_edit" handler="onModify"
										otherProperties="minWidth: Common.config.buttonWidth.minButton">
									</ext:toolbarButton>
									<ext:toolbarSeparator></ext:toolbarSeparator>
									<ext:toolbarButton text="删除" iconCls="icon_delete" handler="onDelete"
										otherProperties="minWidth: Common.config.buttonWidth.minButton">
									</ext:toolbarButton>
								</ext:tbar>
								<%-- 定义数据源--%>
								<ext:store var="datastore" remoteSort="true"
										sortInfo="{field: 'roleName', direction: 'ASC'}"
										otherProperties="url: listRoleUrl">
									<ext:jsonReader root="data" totalProperty="totalProperty"
										record="Ext.data.Record.create([
											{name : 'roleId',type : 'string', mapping: 'roleId'},
											{name : 'roleName',type : 'string', mapping: 'roleName'},
											{name : 'remark',type : 'string', mapping: 'remark'},
											{name : 'transferRolesLimit',type : 'string', mapping: 'transferRolesLimit'}
										])">
									</ext:jsonReader>
								</ext:store>
								<%-- 定义列表的列头--%>
								<ext:columnModel id="cm" var="cm">
									<ext:rowNumberer></ext:rowNumberer>
									<ext:checkboxSelectionModel var="roleSm" singleSelect="true"></ext:checkboxSelectionModel>,
									{header: '角色名称',width: 60,dataIndex: 'roleName'},
			        				{header: '描述',width: 120, id: 'remarkCm', dataIndex: 'remark', renderer: LocalRenderer.remark},
			        				{header: '操作',width: 50,dataIndex: 'roleId', renderer: LocalRenderer.operate},
			        				{header: '成员列表',width: 80,dataIndex: 'roleId', renderer: LocalRenderer.userlist}
								</ext:columnModel>
								<%-- 定义列表的分页工具栏--%>
								<ext:pagingToolbar key="bbar" displayInfo="true" store="datastore" otherProperties="pageSize: pageSize" 
									var="gridPagingBar">
								</ext:pagingToolbar>
							</ext:gridPanel>
							<ext:panel var="authPanel" layout="border" width="320" region="east" margins="3 0 0 3" title="授权面板">
							<ext:items>
					  		<ext:tabPanel var="authTabPanel" region="center" border="false" frame="false"  otherProperties="activeTab: 0">
					  			<ext:items>
		  						<%-------------------------------------------------------------------------------------------------
									定义系统菜单资源树  
								-------------------------------------------------------------------------------------------------%>
				  				<ext:treePanel var="menuTree3" id="menuTree3" title="系统菜单" border="true" rootVisible="true" autoScroll="true"
				  					useArrows="true" margins="3 0 0 0" region="center"
				  					otherProperties="split:true,imgSrc: sys.basePath + 'foundation/ext/images/'" 
				  					loader="new Ext.tree.TreeLoader({dataUrl: getSystemMenuTree3Url, uiProviders:{'checkBox' : Ext.ux.TreeCheckNodeUI} })">
				      				<ext:asyncTreeNode key="root" text="系统菜单" leaf="false" expanded="true"
				      					otherProperties="id: treeRootId" uiProvider="Ext.ux.TreeCheckNodeUI"></ext:asyncTreeNode>
				      				<ext:tbar>
				      					<ext:button text="保存" iconCls="icon_save" handler="saveRoleAuthMenuData"
				      						otherProperties="minWidth: Common.config.buttonWidth.minButton">
				      					</ext:button>
				      				</ext:tbar>
				   				</ext:treePanel>
				   				
				   				</ext:items>
				   			</ext:tabPanel>
				   			</ext:items>
				   			</ext:panel>
	  					</ext:items>
	  				</ext:panel>
	  			</ext:items>
	  		</ext:tabPanel>
	  		</ext:items>
  		</ext:viewport>
  		<ext:window var="systemRoleWindow" id="systemRoleWindow"
			title="新建角色" width="560" autoHeight="true" 
			 layout="fit" shadow="true" closeAction="hide"
			modal="true" closable="true" resizable="false">
		<ext:items>
		<ext:formPanel var="roleForm" id="roleForm" region="center" autoHeight="true"
			labelAlign="right" frame="true" border="false" style="padding: 0 0 0 0px;"
			labelWidth="86" buttonAlign="left" otherProperties="split: true, url: inputRoleUrl">
			<ext:items>
				<ext:panel layout="form">
					<ext:items>
						<ext:hidden id="roleId" name="role.roleId"></ext:hidden>
						<ext:textField id="roleName" name="role.roleName" 
							fieldLabel="<span class=required>*</span>角色名称" otherProperties="anchor: Common.config.fieldAnchor">
						</ext:textField>
						<ext:comboBox id="transferRolesLimit" hiddenName="role.transferRolesLimit" fieldLabel="可传递的角色" 
							otherProperties="anchor: Common.config.fieldAnchor, store: Role.getRoleStore()"
							mode="remote" triggerAction="all" valueField="roleId" displayField="roleName"
							readOnly="true" resizable="true" lazyInit="true" xtype="lovcombo">
						</ext:comboBox>
						<ext:textArea id="remark" name="role.remark" allowBlank="true" otherProperties="anchor: Common.config.fieldAnchor"
							fieldLabel="角色描述" maxLength="250" grow="true"></ext:textArea>
					</ext:items>
				</ext:panel>
			</ext:items>
		</ext:formPanel>
		</ext:items>
		<ext:buttons>
			<ext:button text="保存" iconCls="icon_save" handler="onSaveRole"
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
