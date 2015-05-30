var reseverPageUrl = sys.path + "/resever/getReseverPage.jmt";
var reseverSubmitUrl = sys.path + "/resever/saveOrModifyResever.jmt";
var reseverDeleteUrl = sys.path + "/resever/deleteResever.jmt";

var getUsersByProvince = sys.path + "/user/getUserList.jmt";

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

	query_form.addComp(getText("query_name", "保护区名称",160), 0.23, true);
	query_form.addComp(getText("query_userName", "建设单位",160), 0.23, true);
	query_form.addComp(getComboBox("query_area", "所属省份", 160, area, false),
			0.23, true);
	query_form.addComp(getQueryBtn(onSubmitQueryHandler), 0.2, true);
	/** ****************query panel end*************** */

	/** ****************resever Grid panel start************ */

	var recordArray = [
			getRecord(null, "id", "string"),
			getRecord(null, "userId", "string"),
			getRecord("省份", "province", "string", 80, true, areaRender),
			getRecord("生态区序号", "serNo", "string", 80, true),
			getRecord("文化生态保护区", "name", "string", 200, false),
			getRecord("设立批复时间", "buildTime", "string", 100, true),
			getRecord("规划批复时间", "replyTime", "string", 100, true),
			getRecord("保护区范围", "area", "string", 250, true),
			getRecord("建设单位", "userName", "string", 100, true)];
	var resever_grid = new GridUtil(reseverPageUrl, recordArray, pageSize);
	
	var grid_Bar = getCommonBar(saveHandler, modifyHandler, deleteHandler, userPermission);
	resever_grid.setBottomBar(grid_Bar);

	var listeners = {
		celldblclick : function(thisGrid, rowIndex, columnIndex, ev) {
			if(userPermission.MODIFY == "true") {
				modifyHandler();
			}
		}
	};
	resever_grid.setListeners(listeners);

	var grid_attr = {
		title : "文化生态保护区列表",
		region : "center"
	};
	resever_grid.setGridPanel(grid_attr);
	/** ****************resever Grid panel end************ */

	var grid = resever_grid.getGrid();
	var store = resever_grid.getStore();

	/** ****************resever window start************ */
	var resever_form = new FormUtil({
		id : "reseverForm",
		collapsible : false,
		labelWidth : 110,
		border : false
	});
	
	resever_form.addComp(getText("serNo", "序号", 180), 0.5, false);
	var userStore = new Ext.data.Store({
		url : getUsersByProvince,
		listeners : {
			"beforeload" : function(thisStore) {
				var province = resever_form.findById("show_province").getValue();
				thisStore.baseParams.area = province;
			}
		},
		reader : new Ext.data.JsonReader({}, Ext.data.Record
						.create([{
									name : 'userId',
									type : 'string'
								}, {
									name : 'userName',
									type : 'string'
								}]))
	});
	
	var provinceCb = getComboBox("province", "所属省份", 180, area);
	provinceCb.listeners = {
		"change" : function(field, newValue, oldValue) {
			resever_form.findById("show_userId").setValue();
			userStore.load();
		}	
	};
	
	resever_form.addComp(provinceCb, 0.5, false);
	
	var userCb = getComboBox("userId", "建设单位", 180, null);
	userCb.valueField = "userId";
	userCb.displayField = "userName";
	userCb.lazyInit = true;
	userCb.mode = "remote";
	userCb.store = userStore;
	
	resever_form.addComp(userCb, 0.5, false);
	
	resever_form.addComp(getText("name", "保护区名称", 480), 1.0, false);
	resever_form.addComp(getText("area", "保护区范围", 480), 1.0, false);
	resever_form.addComp(getText("buildTime", "设立批复时间", 180), 0.5, false);
	resever_form.addComp(getText("replyTime", "总体规划批复时间", 180), 0.5, false);

	resever_form.addComp(getHidden("opType"), 0, true);
	resever_form.addComp(getHidden("id"), 0, true);
	
	var resever_win = new WindowUtil({
		width : 650,
		height : 240
	}, resever_form.getPanel(), saveOrModify);

	/** ****************resever window end************ */

	function saveHandler() {
		resever_win.show("新增文化生态保护区");

		resever_form.getForm().reset();
		resever_form.findById("opType").setValue(
				Configuration.opType.SAVE);
		
		resever_form.findById("show_province").setValue(user.org);
	}
	
	function modifyHandler() {
		if (grid.getSelectionModel().getCount() != 1) {
			Ext.Msg.alert("提示信息", "请选择一条记录!");
			return;
		}

		resever_win.show("修改文化生态保护区");

		resever_form.getForm().reset();

		var selectedRecord = grid.getSelectionModel().getSelected();
		var values = {
			id : selectedRecord.get("id"),
			name : selectedRecord.get("name"),
			buildTime : selectedRecord.get("buildTime"),
			area : selectedRecord.get("area"),
			userId : selectedRecord.get("userId"),
			province : selectedRecord.get("province"),
			replyTime : selectedRecord.get("replyTime"),
			serNo : selectedRecord.get("serNo"),
			opType : Configuration.opType.MODIFY
		};
		resever_form.getForm().setValues(values);
		resever_form.findById("show_userId").setRawValue(
				selectedRecord.get("userName"));
	}
	
	function saveOrModify() {
		var form = resever_form.getForm();

		if (!form.isValid()) {
			Ext.Msg.alert("提示信息", "请填写完整的保护区信息!");
			return;
		}

		var ajaxClass = new AjaxUtil(reseverSubmitUrl);

		var callBack_obj = new Object();
		callBack_obj.grid = grid;
		callBack_obj.win = resever_win;
		callBack_obj.form = resever_form;

		ajaxClass.submit(form, null, true, callBack_obj, function(
				obj) {
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
		var selectedRecordArray = grid.getSelectionModel()
				.getSelections();

		Ext.Msg.confirm("确认提示", "确定删除选定的记录?", function(btn) {
			if (btn == "yes") {
				var ids = "";
				for ( var i = 0; i < selectedRecordArray.length; i++) {
					ids += selectedRecordArray[i].get("id") + ",";
				}

				var params = {
					ids : ids
				};

				var ajaxClass = new AjaxUtil(reseverDeleteUrl);
				ajaxClass.request(params, true, null, function(obj) {
					grid.getStore().reload();
				});
			}
		});
	}
	
	function onSubmitQueryHandler() {

		var name = Ext.getCmp("query_name").getValue();
		var area = Ext.getCmp("show_query_area").getValue();
		var userName = Ext.getCmp("query_userName").getValue();
		
		store.baseParams.resverName = name;
		store.baseParams.userName = userName;
		store.baseParams.province = area;

		store.load({
					params : {
						start : 0,
						limit : resever_grid.pageSize
					}
				});
	}

	var viewport = new Ext.Viewport({
				border : false,
				layout : "border",
				items : [query_form.getPanel(), resever_grid.getGrid()]
			});

	onSubmitQueryHandler();
	completePage();
});
