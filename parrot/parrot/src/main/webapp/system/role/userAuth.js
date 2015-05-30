var userRolePageUrl = sys.path + "/user/getUserPageByRole.jmt";
var userNotInRolePageUrl = sys.path + "/user/getUserPageNotInRole.jmt";
var userdeleteRoleUrl = sys.path + "/user/deleteUserRole.jmt";
var usersaveRoleUrl = sys.path + "/user/saveUserRole.jmt";

var getAllRoleUrl = sys.path + "/role/getAllRole.jmt";

var pageSize = 20;
var roleCode;

Ext.onReady(function() {
	Ext.QuickTips.init();
	/** *******************query Panel start********************* */
	var query_attr = {
		title : "查询列表",
		region : "north",
		height : 80,
		labelWidth : 70
	};
	var query_form = new FormUtil(query_attr);

	query_form.addComp(getText("query_code", "用户编码", 120), 0.23, true);
	query_form.addComp(getText("query_name", "用户姓名", 120), 0.23, true);

	var role = {
		xtype : "combo",
		id : "show_roleCode",
		hiddenName : "roleCode",
		triggerAction : "all",
		resizable : true,
		editable : false,
		lazyInit : true,
		mode : "remote",
		width : 120,
		emptyText : "---请先选择角色---",
		fieldLabel : "角色列表",
		valueField : "roleCode",
		displayField : "roleName",
		store : new Ext.data.Store({
					url : getAllRoleUrl,
					reader : new Ext.data.JsonReader({}, Ext.data.Record
									.create([{
												name : "roleCode",
												type : "string"
											}, {
												name : "roleName",
												type : "string"
											}]))
				})
	};
	query_form.addComp(role, 0.23, false);
	query_form.addComp(getQueryBtn(onSubmitQueryHandler), 0.3, true);
	/** *******************query Panel end*********************** */

	/** *******************User Role Grid start********************* */
	var recordArray = [
			getRecord("用户编码", "userId", "string", 100, true),
			getRecord("姓名", "userName", "string", 100, true),
			getRecord("邮箱", "cumail", "string", 120),
			getRecord("默认角色", "defaultRole", "string", 70, true,
					defaultRoleRender),
			getRecord("是否显示", "isShow", "string", 70, true, yesOrNoRender),
			getRecord("是否禁用", "isDisabled", "string", 70, true, yesOrNoRender)];
	var member_grid = new GridUtil(userRolePageUrl, recordArray, pageSize);
	
	var removeBar = null;
	if(userPermission.REMOVEAUTH == "true") {
		removeBar = {
			text : "角色删除用户",
			iconCls : "silk-delete",
			handler : removeUsersHandler
		};
	}
	member_grid.setBottomBar(removeBar);

	member_grid.setGridPanel({
				title : "角色成员列表",
				region : "center"
			});

	var notIn_grid = new GridUtil(userNotInRolePageUrl, recordArray, pageSize);
	var saveBar = null;
	if(userPermission.SAVEAUTH == "true") {
		saveBar = {
			text : "角色增加用户",
			iconCls : "silk-add",
			minWidth : Configuration.minBtnWidth,
			handler : saveUsersHandler
		};
	}
	notIn_grid.setBottomBar(saveBar);

	notIn_grid.setGridPanel({
				title : "非角色成员列表",
				height : 200,
				region : "south"
			});

	/** *******************User Role Grid end********************* */
	var grid_member = member_grid.getGrid();
	var store_member = member_grid.getStore();

	var grid_notIn = notIn_grid.getGrid();
	var store_notIn = notIn_grid.getStore();

	/** *******************org Tree Grid start********************* */
	doStore = function(node) {
		var thisForm = query_form.getForm();

		if (!thisForm.isValid()) {
			Ext.Msg.alert("提示信息", "请先选择角色!");
			return;
		}

		roleCode = Ext.getCmp("show_roleCode").getValue();

		store_member.baseParams = {
			"roleCode" : roleCode,
			"orgCode" : node.attributes.pid
		};
		store_member.reload({
					params : {
						start : 0,
						limit : pageSize
					}
				});

		store_notIn.baseParams = {
			"roleCode" : roleCode,
			"orgCode" : node.attributes.pid
		};
		store_notIn.reload({
					params : {
						start : 0,
						limit : pageSize
					}
				});
		store_notIn.removeAll();
	};
	/** *******************org Tree Grid end********************* */
	function onSubmitQueryHandler() {
		var thisForm = query_form.getForm();

		if (!thisForm.isValid()) {
			Ext.Msg.alert("提示信息", "请先选择角色!");
			return;
		}

		var name = Ext.getCmp("query_name").getValue();
		var code = Ext.getCmp("query_code").getValue();
		roleCode = Ext.getCmp("show_roleCode").getValue();
		
		store_member.baseParams.orgCode = "";
		store_member.baseParams.userName = name;
		store_member.baseParams.userAccount = code;
		store_member.baseParams.roleCode = roleCode;

		store_member.load({
					params : {
						start : 0,
						limit : member_grid.pageSize
					}
				});
		store_notIn.removeAll();		
	}

	function saveUsersHandler() {
		if (grid_notIn.getSelectionModel().getCount() == 0) {
			Ext.Msg.alert("提示信息", "请至少选择一条记录!");
			return;
		}
		
		if (Ext.isEmpty(roleCode)) {
			Ext.Msg.alert("提示信息", "未选中角色!");
			return;
		}

		var selectedRecordArray = grid_notIn.getSelectionModel()
				.getSelections();

		var ids = "";
		for (var i = 0; i < selectedRecordArray.length; i++) {
			ids += selectedRecordArray[i].get("userId") + ",";
		}

		var params = {
			userIds : ids,
			roleCode : roleCode
		};

		var ajaxClass = new AjaxUtil(usersaveRoleUrl);
		ajaxClass.request(params, true, null, function(obj) {
					member_grid.getStore().reload();
					notIn_grid.getStore().reload();
				});
	}

	function removeUsersHandler() {
		if (grid_member.getSelectionModel().getCount() == 0) {
			Ext.Msg.alert("提示信息", "请至少选择一条记录!");
			return;
		}

		if (Ext.isEmpty(roleCode)) {
			Ext.Msg.alert("提示信息", "未选中角色!");
			return;
		}

		var selectedRecordArray = grid_member.getSelectionModel()
				.getSelections();

		Ext.Msg.confirm("确认提示", "确定删除选定的记录?", function(btn) {
					if (btn == "yes") {
						var ids = "";
						for (var i = 0; i < selectedRecordArray.length; i++) {
							ids += selectedRecordArray[i].get("userId") + ",";
						}

						var params = {
							userIds : ids,
							roleCode : roleCode
						};

						var ajaxClass = new AjaxUtil(userdeleteRoleUrl);
						ajaxClass.request(params, true, null, function(obj) {
									member_grid.getStore().reload();
									notIn_grid.getStore().reload();
								});
					}
				});
	}

	var viewport = new Ext.Viewport({
				border : false,
				layout : "border",
				items : [orgTree, {
					border : false,
					region : "center",
					layout : "border",
					split : true,
					items : [query_form.getPanel(), member_grid.getGrid(),
							notIn_grid.getGrid()]
				}]
			});
	completePage();	
});