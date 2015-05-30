var rolePageUrl = sys.path + "/role/getRolePage.jmt";
var roleSubmitUrl = sys.path + "/role/saveOrModifyRole.jmt";
var roleDeleteUrl = sys.path + "/role/deleteRole.jmt";
var roleSaveAuthUrl = sys.path + "/role/saveAuth.jmt";

var getSourceUrl = sys.path + "/resource/getSourceCodeByRole.jmt";
var getSourceTreeUrl = sys.path + "/resource/getSourceTree.jmt";

var getUserUrl = sys.path + "/user/getUserPageByRole.jmt";

var pageSize = 20;

var role_grid;
var sysMenuTree;
var navMenuTree;

var roleCode;

var member_grid;
var member_Window;

Ext.onReady(function() {
	Ext.QuickTips.init();

	/** ****************query panel start*************** */
	var query_attr = {
		title : "查询列表",
		region : "north",
		height : 80,
		labelWidth : 70
	};
	var query_form = new FormUtil(query_attr);

	query_form.addComp(getText("query_code", "角色编码", 120), 0.3, true);
	query_form.addComp(getText("query_name", "角色名称", 120), 0.3, true);
	query_form.addComp(getQueryBtn(onSubmitQueryHandler), 0.3, true);
	/** ****************query panel end*************** */

	/** ****************role Grid panel start************ */
	var remarkRender = function(remark, metadata, record, rowIndex, colIndex) {
		return Render.detailRender(remark, role_grid.cm, colIndex);
	};

	var authRender = function(value) {
		return '<div align="center"><a class="link" href="javascript: onAuth();">授权</a></div>';
	};
	var userRender = function(value) {
		return '<div align="center"><a class="link" href="javascript: onRoleMember();">成员列表</a></div>';
	};

	var recordArray = [
			getRecord("角色编码", "roleCode", "string", 100, true),
			getRecord("角色名称", "roleName", "string", 100, true),
			getRecord("是否禁用", "isDisabled", "string", 50, true, yesOrNoRender),
			getRecord("描述", "roleDesc", "string", 200, false, remarkRender),
			getRecord("授权", "authColumn", "string", 50, false, authRender),
			getRecord("角色成员", "roleUserColumn", "string", 70, false, userRender)];
	role_grid = new GridUtil(rolePageUrl, recordArray, pageSize);

	var grid_Bar = getCommonBar(saveHandler, modifyHandler, deleteHandler, userPermission);
	role_grid.setBottomBar(grid_Bar);

	var listeners = {
		celldblclick : function(thisGrid, rowIndex, columnIndex, ev) {
			if(userPermission.MODIFY == "true") {
				modifyHandler();
			}
		}
	};
	role_grid.setListeners(listeners);

	var grid_attr = {
		title : "角色列表",
		region : "center"
	};
	role_grid.setGridPanel(grid_attr);
	/** ****************role Grid panel end************ */
	var grid = role_grid.getGrid();
	var store = role_grid.getStore();

	/** ****************org window start************ */
	var role_form = new FormUtil({
				id : "roleForm",
				collapsible : false,
				labelWidth : 80,
				border : false
			});
	role_form.addComp(getText("roleCode", "角色编码", 180), 1.0, false);
	role_form.addComp(getText("roleName", "角色名称", 180), 1.0, false);
	role_form.addComp(getTextArea("roleDesc", "描述", 180, 60), 1.0, true);
	role_form.addComp(getComboBox("isDisabled", "是否禁用", 180, yesOrNo, false),
			1.0, false);

	role_form.addComp(getHidden("opType"), 0, true);

	var role_win = new WindowUtil({
				width : 330,
				height : 250
			}, role_form.getPanel(), saveOrModify);

	/** ****************org window start************ */

	function saveHandler() {
		role_win.show("新增角色");

		role_form.getForm().reset();
		role_form.findById("opType").setValue(Configuration.opType.SAVE);

		setTextEditable(role_form.findById("roleCode"));
	}

	function modifyHandler() {
		if (grid.getSelectionModel().getCount() != 1) {
			Ext.Msg.alert("提示信息", "请选择一条记录!");
			return;
		}

		role_win.show("修改角色");
		role_form.getForm().reset();

		// 将主键置为不可编辑
		setTextReadOnly(role_form.findById("roleCode"));

		var selectedRecord = grid.getSelectionModel().getSelected();
		var values = {
			roleCode : selectedRecord.get("roleCode"),
			roleName : selectedRecord.get("roleName"),
			roleDesc : selectedRecord.get("roleDesc"),
			isDisabled : selectedRecord.get("isDisabled"),
			opType : Configuration.opType.MODIFY
		};
		role_form.getForm().setValues(values);
	}

	function saveOrModify() {
		var form = role_form.getForm();

		if (!form.isValid()) {
			Ext.Msg.alert("提示信息", "请填写完整的角色信息!");
			return;
		}

		var ajaxClass = new AjaxUtil(roleSubmitUrl);

		var callBack_obj = new Object();
		callBack_obj.grid = grid;
		callBack_obj.win = role_win;
		callBack_obj.form = role_form;

		ajaxClass.submit(form, null, true, callBack_obj, function(obj) {
					obj.grid.getStore().reload();
					var thisForm = obj.form;
					var opType = thisForm.findById("opType").getValue();

					if (opType == Configuration.opType.SAVE) {
						thisForm.getForm().reset();
						thisForm.findById("opType").setValue(opType);
					} else {
						obj.win.hide();
					}
				});
	}

	function deleteHandler() {
		if (grid.getSelectionModel().getCount() == 0) {
			Ext.Msg.alert("提示信息", "请至少选择一条记录!");
			return;
		}
		var selectedRecordArray = grid.getSelectionModel().getSelections();

		Ext.Msg.confirm("确认提示", "确定删除选定的记录?", function(btn) {
					if (btn == "yes") {
						var ids = "";
						for (var i = 0; i < selectedRecordArray.length; i++) {
							ids += selectedRecordArray[i].get("roleCode") + ",";
						}

						var params = {
							ids : ids
						};

						var ajaxClass = new AjaxUtil(roleDeleteUrl);
						ajaxClass.request(params, true, null, function(obj) {
									grid.getStore().reload();
								});
					}
				});
	}

	function onSubmitQueryHandler() {
		var thisForm = query_form.getForm();
		var store = grid.getStore();

		var name = Ext.getCmp("query_name").getValue();
		var code = Ext.getCmp("query_code").getValue();

		store.baseParams.roleName = name;
		store.baseParams.roleCode = code;

		store.reload({
					params : {
						start : 0,
						limit : role_grid.pageSize
					}
				});
	}

	/** **********************Auth panel*********************** */

	var loader = new Ext.tree.TreeLoader({
				url : getSourceTreeUrl,
				uiProviders : {
					"checkBox" : Ext.ux.TreeCheckNodeUI
				}
			});

	loader.on("beforeload", function(loader, node) {
				loader.baseParams.pid = node.id;
			});

	sysMenuTree = new Ext.tree.TreePanel({
				border : false,
				useArrows : true,
				rootVisible : false,
				autoScroll : true,
				loader : loader,
				title : "系统菜单",
				iconCls : "silk-settings",
				root : new Ext.tree.AsyncTreeNode({
							id : 'SYS',
							expanded : true,
							leaf : false
						})
			});

	navMenuTree = new Ext.tree.TreePanel({
				border : false,
				useArrows : true,
				rootVisible : false,
				autoScroll : true,
				loader : loader,
				title : "应用菜单",
				iconCls : "silk-nav",
				root : new Ext.tree.AsyncTreeNode({
							id : 'NAV',
							expanded : true,
							leaf : false
						})
			});
	
	function saveAuthHandler() {

		if (Ext.isEmpty(roleCode)) {
			Ext.Msg.alert("提示信息", "请先选择需要授权的角色!");
			return;
		}

		var resIds = "";

		var rootNode = sysMenuTree.getNodeById("SYS");
		var checkNodes = rootNode.getUI().getCheckedNodes(rootNode);
		for (var i = 0; i < checkNodes.length; i++) {

//			if (!Ext.isEmpty(checkNodes[i].attributes.targetUrl)) {
				resIds += checkNodes[i].id + ",";
//			}
		}

		var rootNode = navMenuTree.getNodeById("NAV");
		var checkNodes = rootNode.getUI().getCheckedNodes(rootNode);
		for (var i = 0; i < checkNodes.length; i++) {

//			if (!Ext.isEmpty(checkNodes[i].attributes.targetUrl)) {
				resIds += checkNodes[i].id + ",";
//			}
		}
		
		var ajax = new AjaxUtil(roleSaveAuthUrl);
		ajax.request({
					roleCode : roleCode,
					ids : resIds
				}, true, null, function() {
				});
	}
	
	var authTbar = new Array();
	if(userPermission.AUTH == "true") {
		authTbar.push("-");
		authTbar.push({
			id : "save-nav",
			text : "保存",
			iconCls : "silk-save",
			minWidth : Configuration.btnWidth.MIN,
			handler : saveAuthHandler
		});
		authTbar.push("-");
	}
	
	var authTab = new Ext.TabPanel({
				border : false,
				activeTab : 0,
				resizeTabs : true,
				tabWidth : 100,
				deferredRender : false,
				tbar : authTbar,
				items : [sysMenuTree, navMenuTree]
			});

	var viewport = new Ext.Viewport({
				border : false,
				layout : "border",
				items : [{
							border : false,
							region : "center",
							layout : "border",
							split : true,
							items : [query_form.getPanel(), role_grid.getGrid()]
						}, {
							region : 'east',
							title : "角色授权面板",
							collapsible : true,
							split : true,
							layout : 'fit',
							width : 300,
							margins : "2 0 0 0",
							border : true,
							items : authTab
						}]
			});

	onSubmitQueryHandler();

	/** ***********************User Member********************** */

	var member_recordArray = [
			getRecord("用户编号", "userId", "string", 100, true),
			getRecord("用户名称", "userName", "string", 100, true),
			getRecord("邮箱", "cumail", "string", 120),
			getRecord("默认角色", "defaultRole", "string", 70, true,
					defaultRoleRender),
			getRecord("是否显示", "isShow", "string", 70, true, yesOrNoRender),
			getRecord("是否禁用", "isDisabled", "string", 70, true, yesOrNoRender)];
	member_grid = new GridUtil(getUserUrl, member_recordArray, pageSize);
	member_grid.setBottomBar();

	member_grid.setGridPanel({
				collapsible : false,
				border : false,
				split : false
			});
	
	var member_query = new Ext.ux.form.SearchField({
			id : "member_queryField",
			width : 160,
			emptyText : "请输入查询关键字"
		});
			
	member_Window = new Ext.Window({
		title : "角色成员列表",
		closeAction : 'hide',
		modal : true,
		shadow : true,
		closable : true,
		layout : 'fit',
		tbar : [{
			xtype : 'tbbutton',
			id : 'member_queryType',
			text : '编号/账号',
			minWidth : 100,
			menu : new Ext.menu.Menu({
				width : 100,
				items : [{
							text : "编号/账号",
							handler : function() {
								Ext.getCmp("member_queryType").setText("编号/账号");
							}
						}, {
							text : "姓名",
							handler : function() {
								Ext.getCmp("member_queryType").setText("姓名");
							}
						}]
			})
		},member_query],
		width : 680,
		height : 420,
		items : [member_grid.getGrid()]
	});

	member_query.onTrigger2Click = function() {
		var queryType = Ext.getCmp("member_queryType").getText();

		var memStore = member_grid.getStore();

		switch (queryType) {
			case "姓名" :
				var name = Ext.getCmp("member_queryField").getValue();
				memStore.baseParams.userName = name;
				memStore.baseParams.userAccount = "";
				break;
			case "编号/账号" :
				var account = Ext.getCmp("member_queryField").getValue();
				memStore.baseParams.userAccount = account;
				memStore.baseParams.userName = "";
				break;
		}
		
		memStore.baseParams.roleCode = roleCode;
		memStore.load({
					params : {
						start : 0,
						limit : member_grid.pageSize
					}
				});
	};
	completePage();	
});

function onAuth() {
	var pageGrid = role_grid.getGrid();

	var roleRecord = pageGrid.getSelectionModel().getSelected();
	roleCode = roleRecord.get("roleCode");

	var ajax = new AjaxUtil(getSourceUrl);
	ajax.getData({
				roleCode : roleCode
			}, pageGrid, function(obj, menuArray) {
				onAuth2Tree(menuArray, sysMenuTree, "SYS");
				onAuth2Tree(menuArray, navMenuTree, "NAV");
			});

}

function onAuth2Tree(menuArray, tree, rootId) {
	var rootNode = tree.getNodeById(rootId);
	var checkNodes = rootNode.getUI().getCheckedNodes(rootNode);

	// 先设置所有叶子节点为未选择状态
	for (var i = 0; i < checkNodes.length; i++) {
		checkNodes[i].getUI().setChecked(checkNodes[i], false);
	}

	if (!Ext.isEmpty(menuArray)) {
		// 勾选以获得权限的叶子节点
		for (var i = 0; i < menuArray.length; i++) {
			var menuNode = tree.getNodeById(menuArray[i]);
			if (!Ext.isEmpty(menuNode)) {
				if (menuNode.isLeaf()) {
					menuNode.getUI().setChecked(menuNode, true);
				}
			}
		}
	}
}

function onRoleMember() {
	var pageGrid = role_grid.getGrid();

	var roleRecord = pageGrid.getSelectionModel().getSelected();
	roleCode = roleRecord.get("roleCode");

	member_Window.show();
	
	var memStore = member_grid.getStore();
	memStore.baseParams.userAccount = "";
	memStore.baseParams.userName = "";
	memStore.baseParams.roleCode = roleCode;
	
	memStore.load({
				params : {
					start : 0,
					limit : member_grid.pageSize
				}
			});
}
