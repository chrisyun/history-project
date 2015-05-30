var userExtendPageUrl = sys.path + "/project/dw/getProjectDWPage.jmt";
var userExtendSubmitUrl = sys.path + "/project/dw/saveOrModifyProjectDW.jmt";
var getDWProjectUrl = sys.path + "/project/getProjectOfDW.jmt";
var getDWExcelUrl = sys.path + "/project/dw/getProjectDWExcel.jmt";

var pageSize = 20;

Ext.onReady(function() {
	Ext.QuickTips.init();

	/** ****************query panel start*************** */
	var query_attr = {
		title : "查询列表",
		region : "north",
		height : 70,
		labelWidth : 70
	};
	var query_form = new FormUtil(query_attr);
	query_form.addComp(getComboBox("query_area", "所属省份", 160, area, false),
			0.23, true);
	query_form.addComp(getText("query_code", "单位编码", 120), 0.23, true);
	query_form.addComp(getText("query_name", "承担单位", 120), 0.23, true);
	query_form.addComp(getQueryBtn(onSubmitQueryHandler), 0.2, true);
	/** ****************query panel end*************** */

	/** ****************userExtend Grid panel start************ */
	var recordArray = [
			getRecord(null, "id", "string"),
			getRecord(null, "orgCode", "string"),
			getRecord(null, "postalCode", "string"),
			getRecord(null, "companyCtf", "string"),
			getRecord(null, "bankAccount", "string"),
			getRecord("单位编码", "userId", "string", 100, true),
			getRecord("承担单位名称", "userName", "string", 150, false),
			getRecord("联系地址", "address", "string", 200, false),
			getRecord("单位负责人", "contacts", "string", 80, false),
			getRecord("联系电话", "phone", "string", 80),
			getRecord("开户名称", "bankName", "string", 100),
			getRecord("开户银行", "bank", "string", 80, false)];
	var userExtend_grid = new GridUtil(userExtendPageUrl, recordArray, pageSize);
	
	var bar = new Array();
	if(userPermission.MODIFY == "true") {
		bar.push("-");
		bar.push({
			text : "修改",
			iconCls : "silk-edit",
			minWidth : Configuration.minBtnWidth,
			handler : modifyHandler
		});
	}
	bar.push("-");
	bar.push({
		text : "费用导出",
		iconCls : "silk-excel",
		minWidth : Configuration.minBtnWidth,
		handler : exportHandler
	});
	
	
	userExtend_grid.setBottomBar(bar);
	
	var listeners = {
		celldblclick : function(thisGrid, rowIndex, columnIndex, ev) {
			if(userPermission.MODIFY == "true") {
				modifyHandler();
			}
		}
	};
	userExtend_grid.setListeners(listeners);

	var grid_attr = {
		title : "非遗项目承担单位列表",
		region : "center"
	};
	userExtend_grid.setGridPanel(grid_attr);
	/** ****************userExtend Grid panel end************ */
	var grid = userExtend_grid.getGrid();
	var store = userExtend_grid.getStore();

	/** ****************org window start************ */
	var userExtend_form = new FormUtil( {
		title : "承担单位信息",
		id : "userExtendForm",
		collapsible : false,
		border : false,
		labelWidth : 120
	});
	userExtend_form.addComp(getText("userId", "单位编码", 520), 1.0, true);
	userExtend_form.addComp(getText("userName", "项目承担单位", 400), 0.7, true);
	userExtend_form.addComp(getButton({
		text : "修改项目承担单位信息",
		handler : function() {
			
			var pid = userExtend_form.findById("userId").getValue();
			
			if(Ext.isEmpty(pid)) {
				return ;
			}
			
			var url = sys.path + "/system/user/user.jsp?userSid="+pid; 
			
			parent.window.setActivate(url, {
				id : "USER",
				text : "修改项目承担单位信息",
				attributes : {
					iconCls : "tree-user"
				}
			});
		}
	}), 0.3, true);
	
	
	
	userExtend_form.addComp(getText("address", "联系地址", 180), 0.5, false);
	userExtend_form.addComp(getText("postalCode", "邮政编码", 180), 0.5, true);
	userExtend_form.addComp(getText("contacts", "申报项目负责人", 180), 0.5, false);
	userExtend_form.addComp(getText("phone", "联系电话", 180), 0.5, false);
	userExtend_form.addComp(getText("bankName", "开户名称", 520), 1.0, false);
	userExtend_form.addComp(getText("bank", "开户银行", 520), 1.0, false);
	userExtend_form.addComp(getText("bankAccount", "开户账号", 520), 1.0, false);
	userExtend_form.addComp(getTextArea("companyCtf", "申报单位具备资质", 520, 70, 1000), 1.0, false);
	
	userExtend_form.addComp(getHidden("id"), 0, true);
	userExtend_form.addComp(getHidden("opType"), 0, true);
	
	var project_recordArray = [
 	      	getRecord("项目类别", "type", "string", 150, true, projectTypeRender),
			getRecord("项目编码", "code", "string", 100, false),
			getRecord("项目名称", "name", "string", 200, true)];
	var project_grid = new GridUtil(getDWProjectUrl, project_recordArray);
	project_grid.setGridPanel({
		title : "承担项目列表",
		collapsible : false,
		autoScroll : true,
		border : false
	});
	
	var tab = new Ext.TabPanel( {
		activeTab : 0,
		resizeTabs : true,
		border : false,
		minTabWidth : 140,
		items : [ userExtend_form.getPanel() , 
					project_grid.getGrid()]
	});
	
	var userExtend_win = new WindowUtil( {
		width : 700,
		height : 400
	}, {
		xtype : "panel",
		layout : "fit",
		border : false,
		items : [ tab ]
	}, saveOrModify);
	/** *****************userextend window start************ */
	function modifyHandler() {
		if (grid.getSelectionModel().getCount() != 1) {
			Ext.Msg.alert("提示信息", "请选择一条记录!");
			return;
		}

		userExtend_win.show("修改项目承担单位信息");
		userExtend_form.getForm().reset();

		setTextReadOnly(userExtend_form.findById("userId"));
		setTextReadOnly(userExtend_form.findById("userName"));
		
		var selectedRecord = grid.getSelectionModel().getSelected();
		var values = {
			userId : selectedRecord.get("userId"),
			userName : selectedRecord.get("userName"),
			address : selectedRecord.get("address"),
			postalCode : selectedRecord.get("postalCode"),
			contacts : selectedRecord.get("contacts"),
			phone : selectedRecord.get("phone"),
			bankName : selectedRecord.get("bankName"),
			bank : selectedRecord.get("bank"),
			bankAccount : selectedRecord.get("bankAccount"),
			companyCtf : selectedRecord.get("companyCtf"),
			id : selectedRecord.get("id")
		};
		
		project_grid.getStore().load({
			params : {
				userId : values.userId
			}
		});
		
		userExtend_form.getForm().setValues(values);
		if(Ext.isEmpty(values.id)) {
			userExtend_form.findById("opType").setValue(Configuration.opType.SAVE);
		} else {
			userExtend_form.findById("opType").setValue(Configuration.opType.MODIFY);
		}
	}

	function saveOrModify() {
		var form = userExtend_form.getForm();

		if (!form.isValid()) {
			Ext.Msg.alert("提示信息", "请填写完整的承担单位信息!");
			return;
		}

		var callBack_obj = new Object();
		callBack_obj.grid = grid;
		callBack_obj.win = userExtend_win;
		callBack_obj.form = userExtend_form;
		var ajaxClass = new AjaxUtil(userExtendSubmitUrl);
		ajaxClass.submit(form, null, true, callBack_obj, function(
				obj) {
			obj.grid.getStore().reload();
			obj.win.hide();
		});
	}
	
	function exportHandler() {
		var name = Ext.getCmp("query_name").getValue();
		var code = Ext.getCmp("query_code").getValue();
		var orgCode = Ext.getCmp("show_query_area").getValue();
		
		var excelUrl = getDWExcelUrl + "?name=" + name
				+ "&code=" + code + "&orgCode=" + orgCode +
		"&key=" + Math.random();
		
		document.getElementById("excelFrame").src = excelUrl;
	}
	
	function onSubmitQueryHandler() {
		var thisForm = query_form.getForm();
		var store = grid.getStore();

		var name = Ext.getCmp("query_name").getValue();
		var code = Ext.getCmp("query_code").getValue();
		var orgCode = Ext.getCmp("show_query_area").getValue();

		store.baseParams.userName = name;
		store.baseParams.userId = code;
		store.baseParams.orgCode = orgCode;
		
		store.load( {
			params : {
				start : 0,
				limit : userExtend_grid.pageSize
			}
		});
	}

	var viewport = new Ext.Viewport( {
		border : false,
		layout : "border",
		items : [ query_form.getPanel(), userExtend_grid.getGrid() ]
	});

	onSubmitQueryHandler();

	completePage();
});
