var userPageUrl = sys.path + "/user/getUserPage.jmt";
var userSubmitUrl = sys.path + "/user/saveOrModifyUser.jmt";
var userDeleteUrl = sys.path + "/user/deleteUser.jmt";
var userDisabledUrl = sys.path + "/user/disabledUser.jmt";
var userSaveRoleUrl = sys.path + "/user/saveRoleByUser.jmt";
var userGetUrl = sys.path + "/user/getUserById.jmt";

var getUserRoleUrl = sys.path + "/role/getUserRole.jmt";
var pageSize = 20;

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
	query_form.addComp(getText("query_code", "用户编码", 120), 0.23, true);
	query_form.addComp(getText("query_name", "姓名", 120), 0.23, true);
	query_form.addComp(getText("query_mail", "邮箱", 120), 0.23, true);
	query_form.addComp(getQueryBtn(onSubmitQueryHandler), 0.3, true);
	/** ****************query panel end*************** */

	/** ****************user Grid panel start************ */
	var recordArray = [
			getRecord(null, "orgName", "string"),
			getRecord(null, "orgCode", "string"),
			getRecord(null, "sortNum", "string"),
			getRecord(null, "userAccount", "string"),
			getRecord(null, "sn", "string"),
			getRecord("用户编码", "userId", "string", 100, true),
			getRecord("姓名", "userName", "string", 100, true),
			getRecord("性别", "gender", "string", 70, true, genderRender),
			getRecord("邮箱", "cumail", "string", 120),
			getRecord("电话", "phone", "string", 70),
			getRecord("默认角色", "defaultRole", "string", 70, true,
					defaultRoleRender),
			getRecord("是否显示", "isShow", "string", 70, true,
					yesOrNoRender),
			getRecord("是否禁用", "isDisabled", "string", 70, true,
					yesOrNoRender) ];
	var user_grid = new GridUtil(userPageUrl, recordArray, pageSize);
	user_grid.setBottomBar();
	
	var top_Bar = new Array();
	if(userPermission.MODIFY == "true") {
		top_Bar.push("-");
		top_Bar.push({
			text : "修改",
			iconCls : "silk-edit",
			minWidth : Configuration.minBtnWidth,
			handler : modifyHandler
		});
	}
	if(userPermission.REMOVE == "true") {
		top_Bar.push("-");
		top_Bar.push({
			text : "删除",
			iconCls : "silk-delete",
			minWidth : Configuration.minBtnWidth,
			handler : deleteHandler
		});
	}
	if(userPermission.DISABLED == "true") {
		top_Bar.push("-");
		top_Bar.push({
			text : "启用",
			iconCls : "silk-tick",
			minWidth : Configuration.minBtnWidth,
			handler : unDisabledHandler
		});
		top_Bar.push("-");
		top_Bar.push({
			text : "禁用",
			iconCls : "silk-cross",
			minWidth : Configuration.minBtnWidth,
			handler : disabledHandler
		});
	}
	if(userPermission.CHANGEPWD == "true") {
		top_Bar.push("-");
		top_Bar.push({
			text : "修改密码",
			iconCls : "tree-pwd",
			minWidth : Configuration.minBtnWidth,
			handler : pwdHandler
		});
	}
	if(userPermission.AUTH == "true") {
		top_Bar.push("-");
		top_Bar.push({
			text : "用户授权",
			iconCls : "tree-auth",
			minWidth : Configuration.minBtnWidth,
			handler : roleHandler
		});
	}
	user_grid.setTopBar(top_Bar);

	var listeners = {
		celldblclick : function(thisGrid, rowIndex, columnIndex, ev) {
			if(userPermission.MODIFY == "true") {
				modifyHandler();
			}
		}
	};
	user_grid.setListeners(listeners);

	var grid_attr = {
		title : "用户列表",
		region : "center"
	};
	user_grid.setGridPanel(grid_attr);
	/** ****************user Grid panel end************ */
	var grid = user_grid.getGrid();
	var store = user_grid.getStore();
	/** ****************org tree setting start************ */
	if(userPermission.SAVE == "true") {
		menuTop = new Ext.menu.Menu( {
			items : [ {
				text : "增加用户",
				iconCls : "silk-add",
				handler : saveHandler
			} ]
		});
		menu = menuTop;
	}

	doStore = function(node) {
		store.baseParams = {
			"orgCode" : node.attributes.pid
		};
		store.reload( {
			params : {
				start : 0,
				limit : user_grid.pageSize
			}
		});
	};

	/** ****************org tree setting end************ */

	/** ****************org window start************ */
	var user_form = new FormUtil( {
		id : "userForm",
		collapsible : false,
		labelWidth : 100,
		border : false
	});
	user_form.addComp(getText("userId", "用户编码", 150), 0.5, true);
	user_form.addComp(getText("sn", "姓", 150), 0.5, true);
	user_form.addComp(getText("userAccount", "账号", 150), 0.5, false);
	user_form.addComp(getText("userName", "姓名", 150), 0.5, false);
	user_form.addComp(getComboBox("gender", "性别", 150, gender, false),
			0.5, true);
	user_form.addComp(getComboBox("defaultRole", "默认角色", 150,
			defaultRole, false), 0.5, false);
	user_form.addComp(getMailText("cumail", "邮箱", 442), 1.0, false);
	user_form.addComp(getOrgTreeSelect("orgCode", 442, false), 1.0,
			false);
	user_form.addComp(getText("phone", "电话", 150), 0.5, true);
	user_form.addComp(getPwdText("userPwd", "密码", 150), 0.5, true);
	user_form.addComp(
			getComboBox("isShow", "是否显示", 150, yesOrNo, false), 0.5,
			false);
	user_form.addComp(getRPwdText("userrPwd", "重复密码", 150, "userPwd"),
			0.5, true);
	user_form.addComp(getComboBox("isDisabled", "是否禁用", 150, yesOrNo,
			false), 0.5, false);
	user_form.addComp(getNumberText("sortNum", "排序号", 150), 0.5, true);
	user_form.addComp(getHidden("opType"), 0, true);

	var user_win = new WindowUtil( {
		width : 600,
		height : 338
	}, user_form.getPanel(), saveOrModify);
	/** *****************org window start************ */

	/** *****************role window start************ */
	var role_form = new FormUtil( {
		id : "roleForm",
		collapsible : false,
		labelWidth : 30,
		border : false
	});
	var role_win = new WindowUtil( {
		width : 450,
		height : 300
	}, role_form.getPanel(), saveUserRoleHandler);

	function saveUserRoleHandler() {
		var thisForm = role_form.getPanel();

		var roleArray = thisForm.findByType("checkbox");
		var roleIds = "";
		for ( var i = 0; i < roleArray.length; i++) {
			if (roleArray[i].getValue()) {
				roleIds += roleArray[i].getId() + ",";
			}
		}

		var selectedRecord = grid.getSelectionModel().getSelected();
		var userId = selectedRecord.get("userId");

		var ajaxClass = new AjaxUtil(userSaveRoleUrl);
		ajaxClass.request( {
			userId : userId,
			roleCodes : roleIds
		}, true, role_win, function(obj) {
			role_win.hide();
		});
	}

	function roleHandler() {
		if (grid.getSelectionModel().getCount() != 1) {
			Ext.Msg.alert("提示信息", "请选择一条记录!");
			return;
		}
		var selectedRecord = grid.getSelectionModel().getSelected();
		var userId = selectedRecord.get("userId");

		var thisForm = role_form.getPanel();
		thisForm.removeAll(true);

		var ajaxClass = new AjaxUtil(getUserRoleUrl);
		ajaxClass.getData( {
			userId : userId
		}, null, function(obj, relationArray) {
			for ( var i = 0; i < relationArray.length; i++) {
				var role = relationArray[i].object;
				var cb = getCheckbox(role.roleCode, role.roleName,
						relationArray[i].relevance);
				role_form.addComp(cb, 0.5, true);
			}
			thisForm.doLayout();
			role_win.show("用户授权");
		});
	}

	/** *****************role window end************ */
	function saveHandler() {
		user_win.show("新增用户");
		
		user_form.getForm().reset();
		
		// 将主键置为不可编辑，用户编码系统自动生成
		setTextReadOnly(user_form.findById("userId"));
		
		user_form.findById("opType")
				.setValue(Configuration.opType.SAVE);
		Ext.getCmp("show_orgCode").setValue(currentActiveNode);
	}

	function modifyHandler() {
		if (grid.getSelectionModel().getCount() != 1) {
			Ext.Msg.alert("提示信息", "请选择一条记录!");
			return;
		}

		user_win.show("修改用户");
		user_form.getForm().reset();

		// 将主键置为不可编辑
		setTextReadOnly(user_form.findById("userId"));
		var selectedRecord = grid.getSelectionModel().getSelected();
		var orgCode = selectedRecord.get("orgCode");
		var orgName = selectedRecord.get("orgName");
		var values = {
			userId : selectedRecord.get("userId"),
			userName : selectedRecord.get("userName"),
			userAccount : selectedRecord.get("userAccount"),
			sn : selectedRecord.get("sn"),
			gender : selectedRecord.get("gender"),
			cumail : selectedRecord.get("cumail"),
			phone : selectedRecord.get("phone"),
			defaultRole : selectedRecord.get("defaultRole"),
			isShow : selectedRecord.get("isShow"),
			isDisabled : selectedRecord.get("isDisabled"),
			sortNum : selectedRecord.get("sortNum"),
			opType : Configuration.opType.MODIFY
		};
		user_form.getForm().setValues(values);

		var orgNode = {
			text : orgName,
			attributes : {
				pid : orgCode
			}
		};
		Ext.getCmp("show_orgCode").setValue(orgNode);
	}

	function saveOrModify() {
		var form = user_form.getForm();

		var newpwd = form.findField("userPwd");
		var newpwdconfirmCmp = form.findField("userrPwd");
		if (newpwd.getValue() != newpwdconfirmCmp.getValue()) {
			newpwdconfirmCmp.markInvalid(Validate.rpwd);
			return;
		}

		if (!form.isValid()) {
			Ext.Msg.alert("提示信息", "请填写完整的用户信息!");
			return;
		}

		var callBack_obj = new Object();
		callBack_obj.grid = grid;
		callBack_obj.win = user_win;
		callBack_obj.form = user_form;
		var ajaxClass = new AjaxUtil(userSubmitUrl);
		ajaxClass.submit(form, null, true, callBack_obj, function(
				obj) {
			obj.grid.getStore().reload();
			var thisForm = obj.form;
			var opType = thisForm.findById("opType").getValue();

			if (opType == Configuration.opType.SAVE) {
				//obj.form.getForm().reset();
				thisForm.findById("opType").setValue(opType);
				Ext.getCmp("show_orgCode").setValue(currentActiveNode);
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
		var selectedRecordArray = grid.getSelectionModel()
				.getSelections();

		Ext.Msg.confirm("确认提示", "确定删除选定的记录?", function(btn) {
			if (btn == "yes") {
				var ids = "";
				for ( var i = 0; i < selectedRecordArray.length; i++) {
					ids += selectedRecordArray[i].get("userId") + ",";
				}

				var params = {
					ids : ids
				};

				var ajaxClass = new AjaxUtil(userDeleteUrl);
				ajaxClass.request(params, true, null, function(obj) {
					grid.getStore().reload();
				});
			}
		});
	}

	function unDisabledHandler() {
		if (grid.getSelectionModel().getCount() == 0) {
			Ext.Msg.alert("提示信息", "请至少选择一条记录!");
			return;
		}
		var selectedRecordArray = grid.getSelectionModel()
				.getSelections();

		var ids = "";
		for ( var i = 0; i < selectedRecordArray.length; i++) {
			ids += selectedRecordArray[i].get("userId") + ",";
		}

		var params = {
			ids : ids,
			isDisabled : Configuration.yOrN.NO
		};

		var ajaxClass = new AjaxUtil(userDisabledUrl);
		ajaxClass.request(params, true, null, function(obj) {
			grid.getStore().reload();
		});
	}

	function disabledHandler() {
		if (grid.getSelectionModel().getCount() == 0) {
			Ext.Msg.alert("提示信息", "请至少选择一条记录!");
			return;
		}
		var selectedRecordArray = grid.getSelectionModel()
				.getSelections();

		Ext.Msg.confirm("确认提示", "确定禁用选定的用户?", function(btn) {
			if (btn == "yes") {
				var ids = "";
				for ( var i = 0; i < selectedRecordArray.length; i++) {
					ids += selectedRecordArray[i].get("userId") + ",";
				}

				var params = {
					ids : ids,
					isDisabled : Configuration.yOrN.YES
				};

				var ajaxClass = new AjaxUtil(userDisabledUrl);
				ajaxClass.request(params, true, null, function(obj) {
					grid.getStore().reload();
				});
			}
		});

	}

	/** ****************pwd panel start************ */

	var Pwd_Obj = new UserPwd();
	function pwdHandler() {
		if (grid.getSelectionModel().getCount() != 1) {
			Ext.Msg.alert("提示信息", "请选择一名用户!");
			return;
		}
		var selectedRecord = grid.getSelectionModel().getSelected();
		var user_id = selectedRecord.get("userId");
		Pwd_Obj.show(user_id);
	}

	function onSubmitQueryHandler() {
		var thisForm = query_form.getForm();
		var store = grid.getStore();

		var name = Ext.getCmp("query_name").getValue();
		var code = Ext.getCmp("query_code").getValue();
		var mail = Ext.getCmp("query_mail").getValue();

		store.baseParams.userName = name;
		store.baseParams.userAccount = code;
		store.baseParams.cumail = mail;
		store.baseParams.orgCode = "";
		
		store.load( {
			params : {
				start : 0,
				limit : user_grid.pageSize
			}
		});
	}

	var viewport = new Ext.Viewport( {
		border : false,
		layout : "border",
		items : [ orgTree, {
			border : false,
			region : "center",
			layout : "border",
			split : true,
			items : [ query_form.getPanel(), user_grid.getGrid() ]
		} ]
	});

	grid.getStore().reload( {
		params : {
			start : 0,
			limit : user_grid.pageSize,
			orgCode : "ROOT"
		}
	});

	completePage();
	
	var thisUrl = window.location.href;
	if(thisUrl.indexOf("createUser") > 0) {
		saveHandler();
	}
	
	var args = getRequestArguments();
	var userSid = args['userSid'];
	if(userSid != null && !Ext.isEmpty(userSid)) {
		openModifyHandler(userSid);
	}
	
	function openModifyHandler(userSid) {
		user_win.show("修改用户信息");
		user_form.getForm().reset();
		
		var ajax = new AjaxUtil(userGetUrl);
		ajax.getData({
			id : userSid
		},null,function(scope, data){
			data.opType = Configuration.opType.MODIFY;
			
			user_form.getForm().setValues(data);
					
			var orgNode = {
				text : data.orgName,
				attributes : {
					pid : data.orgCode
				}
			};
			Ext.getCmp("show_orgCode").setValue(orgNode);
		});
	}
	
});
