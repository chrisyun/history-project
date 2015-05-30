var orgPageUrl = sys.path + "/org/getOrgPage.jmt";
var orgSubmitUrl = sys.path + "/org/saveOrModifyOrg.jmt";
var orgDeleteUrl = sys.path + "/org/deleteOrg.jmt";
var orgQueryUrl = sys.path + "/org/getOrg.jmt";

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

	query_form.addComp(getText("query_code", "组织编码",120), 0.23, true);
	query_form.addComp(getText("query_name", "组织名称",120), 0.23, true);
	query_form.addComp(getComboBox("query_type", "组织类型",
					120, orgType, false), 0.23, true);
	query_form.addComp(getQueryBtn(onSubmitQueryHandler), 0.3, true);
	/** ****************query panel end*************** */

	
	/** ****************org Grid panel start************ */
	var recordArray = [
			getRecord(null, "orgId", "string"),
			getRecord(null, "parentOrg", "string"),
			getRecord(null, "orgName", "string"),
			getRecord(null, "orgCode", "string"),
			getRecord(null, "sortNum", "string"),
			getRecord("组织编码", "orgCode", "string", 100, true),
			getRecord("组织名称", "showName", "string", 150, true),
			getRecord("组织类型", "orgType", "string", 70, true,orgTypeRender),
			getRecord("组织邮箱", "orgMail", "string", 120),
			getRecord("所属区域", "area", "string", 70, true, areaRender),
			getRecord("是否显示", "isShow", "string", 70, true,yesOrNoRender),
			getRecord("是否禁用", "isDisabled", "string", 70, true,yesOrNoRender) ];
	var org_grid = new GridUtil(orgPageUrl, recordArray, pageSize);

	var grid_Bar = getCommonBar(saveHandler, modifyHandler, deleteHandler, userPermission);
	org_grid.setBottomBar(grid_Bar);

	var listeners = {
		celldblclick : function(thisGrid, rowIndex, columnIndex, ev) {
			if(userPermission.MODIFY == "true") {
				modifyHandler();
			}
		}
	};
	org_grid.setListeners(listeners);

	var grid_attr = {
		title : "组织列表",
		region : "center"
	};
	org_grid.setGridPanel(grid_attr);
	/** ****************org Grid panel end************ */

	var grid = org_grid.getGrid();
	var store = org_grid.getStore();

	/** ****************org tree setting start************ */
	var menuArray = new Array();
	
	if(userPermission.SAVE == "true") {
		menuArray.push({
			text : "增加子组织",
			iconCls : "silk-add",
			handler : saveHandler
		});
		menuTop = new Ext.menu.Menu( {
			items : [menuArray[0]]
		});
	}
	if(userPermission.REMOVE == "true") {
		menuArray.push({
			text : "删除组织",
			iconCls : "silk-delete",
			handler : deleteMenuHandler
		});
	}
	if(userPermission.MODIFY == "true") {
		menuArray.push({
			text : "修改组织",
			iconCls : "silk-edit",
			handler : modifyMenuHandler
		});
	}
	
	if(menuArray.length == 2) {
		menu = new Ext.menu.Menu( {
			items : [menuArray[0],"-",menuArray[1]]
		});
	} else if(menuArray.length == 3) {
		menu = new Ext.menu.Menu( {
			items : [menuArray[0],"-",menuArray[1],"-",menuArray[2]]
		});
	}
	
	doStore = function(node) {
		store.baseParams = {
			"pid" : node.attributes.pid
		};
		store.reload( {
			params : {
				start : 0,
				limit : org_grid.pageSize
			}
		});
	};
	
	/** ****************org tree setting end************ */
	

	/** ****************org window start************ */
	var org_form = new FormUtil( {
		id : "orgForm",
		collapsible : false,
		labelWidth : 100,
		border : false
	});
	org_form.addComp(getText("orgName", "组织名称", 150), 0.5, false);
	org_form.addComp(getText("orgCode", "组织编码", 150), 0.5, false);
	org_form.addComp(getText("showName", "组织显示名称", 150), 0.5, false);

	org_form.addComp(
			getComboBox("orgType", "组织类型", 150, orgType, false), 0.5,
			false);

	org_form.addComp(getMailText("orgMail", "组织邮箱", 150), 0.5, true);

	org_form.addComp(getOrgTreeSelect("parentOrg", 150, false), 0.5,false);

	org_form.addComp(
			getComboBox("isShow", "是否显示", 150, yesOrNo, false), 0.5,
			false);

	org_form.addComp(getComboBox("isDisabled", "是否禁用", 150, yesOrNo,
			false), 0.5, false);

	org_form.addComp(getComboBox("area", "所属区域", 150, area, false),
			0.5, false);

	org_form.addComp(getNumberText("sortNum", "排序号", 150), 0.5, true);

	org_form.addComp(getHidden("opType"), 0, true);
	org_form.addComp(getHidden("orgId"), 0, true);

	var org_win = new WindowUtil( {
		width : 600,
		height : 300
	}, org_form.getPanel(), saveOrModify);
	
	Ext.getCmp("show_parentOrg").addListener("select",function(treefield,node){
		if(node.attributes.attributes != null 
			&& !Ext.isEmpty(node.attributes.attributes.area)) {
			Ext.getCmp("show_area").setValue(node.attributes.attributes.area);
		}
	});
	
	
	/** ****************org window start************ */

	function saveHandler() {
		org_win.show("新增组织");

		org_form.getForm().reset();
		org_form.findById("opType").setValue(
				Configuration.opType.SAVE);

		Ext.getCmp("show_parentOrg").setValue(currentActiveNode);
		if(currentActiveNode.attributes.attributes != null 
			&& !Ext.isEmpty(currentActiveNode.attributes.attributes.area)) {
			Ext.getCmp("show_area").setValue(currentActiveNode.attributes.attributes.area);
		}
		
		// 自动将上级区域信息传递给下级组织？
		// 将所属组织设置为不可选
		// var parentOrgSel =
		// org_form.getFormPanel().findById("isDisabled_show");
		// parentOrgSel.el.dom.readOnly = true;
	}

	function modifyHandler() {
		if (grid.getSelectionModel().getCount() != 1) {
			Ext.Msg.alert("提示信息", "请选择一条记录!");
			return;
		}

		org_win.show("修改组织");

		org_form.getForm().reset();

		// 将主键置为不可编辑
		// var codeText = form.findById("orgCode");
		// codeText.el.dom.readOnly = true;

		var selectedRecord = grid.getSelectionModel().getSelected();
		var parentOrg = selectedRecord.get("parentOrg");
		var values = {
			orgId : selectedRecord.get("orgId"),
			orgName : selectedRecord.get("orgName"),
			orgCode : selectedRecord.get("orgCode"),
			showName : selectedRecord.get("showName"),
			orgType : selectedRecord.get("orgType"),
			orgMail : selectedRecord.get("orgMail"),
			isShow : selectedRecord.get("isShow"),
			isDisabled : selectedRecord.get("isDisabled"),
			area : selectedRecord.get("area"),
			sortNum : selectedRecord.get("sortNum"),
			opType : Configuration.opType.MODIFY
		};
		org_form.getForm().setValues(values);

		// 获取所属组织名称
		if (!Ext.isEmpty(parentOrg)) {
			var ajax = new AjaxUtil(orgQueryUrl);

			ajax.getData( {
				orgCode : parentOrg
			}, org_form, function(obj, data) {
				var orgName = data.orgName;
				var parentOrgNode = {
					text : orgName,
					attributes : {
						pid : parentOrg
					}
				};
				Ext.getCmp("show_parentOrg").setValue(parentOrgNode);
			});
		}
	}

	function saveOrModify() {
		var form = org_form.getForm();

		if (!form.isValid()) {
			Ext.Msg.alert("提示信息", "请填写完整的组织信息!");
			return;
		}

		var ajaxClass = new AjaxUtil(orgSubmitUrl);

		var callBack_obj = new Object();
		callBack_obj.grid = grid;
		callBack_obj.win = org_win;
		callBack_obj.form = org_form;

		ajaxClass.submit(form, null, true, callBack_obj, function(
				obj) {
			obj.grid.getStore().reload();
			var thisForm = obj.form;
			var opType = thisForm.findById("opType").getValue();

			if (currentActiveNode != null
					&& currentActiveNode.parentNode != null) {
				var refreshNode = currentActiveNode.parentNode;
				orgTree.getLoader().load(refreshNode);
				refreshNode.expand();
			} else {
				orgTree.getLoader().load(tree_root);
				tree_root.expand();
			}

			if (opType == Configuration.opType.SAVE) {
				thisForm.getForm().reset();
				thisForm.findById("opType").setValue(opType);
				Ext.getCmp("show_parentOrg")
						.setValue(currentActiveNode);
			} else {
				obj.win.hide();
			}
		});
	}

	function modifyMenuHandler() {
		org_win.show("修改组织");
		org_form.getForm().reset();

		var parentNode = currentActiveNode.parentNode;
		var orgId = currentActiveNode.id;

		var ajax = new AjaxUtil(orgQueryUrl);

		ajax.getData( {
			orgId : orgId
		}, org_form, function(obj, data) {
			var values = {
				orgId : data.orgId,
				orgName : data.orgName,
				orgCode : data.orgCode,
				showName : data.showName,
				orgType : data.orgType,
				orgMail : data.orgMail,
				isShow : data.isShow,
				isDisabled : data.isDisabled,
				area : data.area,
				sortNum : data.sortNum,
				opType : Configuration.opType.MODIFY
			};
			org_form.getForm().setValues(values);
			Ext.getCmp("show_parentOrg").setValue(parentNode);
		});
	}

	function deleteMenuHandler() {
		Ext.Msg.confirm("确认提示", "确定删除选定的记录?", function(btn) {
			if (btn == "yes") {
				var ids = currentActiveNode.id + ",";

				var params = {
					ids : ids
				};

				var ajaxClass = new AjaxUtil(orgDeleteUrl);
				ajaxClass.request(params, true, null, function(obj) {
					grid.getStore().reload();
					var refreshNode = currentActiveNode.parentNode;
					orgTree.getLoader().load(refreshNode);
					refreshNode.expand();
				});
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
					ids += selectedRecordArray[i].get("orgId") + ",";
				}

				var params = {
					ids : ids
				};

				var ajaxClass = new AjaxUtil(orgDeleteUrl);
				ajaxClass.request(params, true, null, function(obj) {
					grid.getStore().reload();
					
					//避免该节点已经被删导致无法加载的问题
					try {
						orgTree.getLoader().load(currentActiveNode);
						currentActiveNode.expand();
					} catch(e) {}
					
				});
			}
		});
	}

	function onSubmitQueryHandler() {
		var store = grid.getStore();
		
		var name = Ext.getCmp("query_name").getValue();
		var code = Ext.getCmp("query_code").getValue();
		var type = Ext.getCmp("show_query_type").getValue();

		store.baseParams.orgCode = code;
		store.baseParams.orgName = name;
		store.baseParams.orgType = type;
		store.baseParams.pid = "";
		
		store.load( {
			params : {
				start : 0,
				limit : org_grid.pageSize
			}
		});
	}

	var viewport = new Ext.Viewport( {
		border : false,
		layout : "border",
		items : [
				orgTree,
				{
					border : false,
					region : "center",
					layout : "border",
					split : true,
					items : [ query_form.getPanel(),
							org_grid.getGrid() ]
				} ]
	});

	grid.getStore().reload( {
		params : {
			start : 0,
			limit : org_grid.pageSize,
			pid : "ROOT"
		}
	});
	completePage();
});
