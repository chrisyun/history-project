var dtPageUrl = sys.path + "/dd/getDtPage.jmt";
var dtSubmitUrl = sys.path + "/dd/saveOrModifyDt.jmt";
var dtDeleteUrl = sys.path + "/dd/deleteDt.jmt";

var ddListUrl = sys.path + "/dd/getDdList.jmt";
var ddSubmitUrl = sys.path + "/dd/saveOrModifyDd.jmt";
var ddDeleteUrl = sys.path + "/dd/deleteDd.jmt";

var pageSize = 10;
var typeId;

Ext.onReady(function() {
	Ext.QuickTips.init();

	/** ****************query panel start*************** */
	var query_attr = {
		title : "查询列表",
		region : "north",
		height : 80,
		labelWidth : 120
	};
	var query_form = new FormUtil(query_attr);

	query_form.addComp(getText("query_ename", "字典类型编码", 120), 0.25, true);
	query_form.addComp(getText("query_cname", "字典类型名称", 120), 0.25, true);
	query_form.addComp(getQueryBtn(onSubmitQueryHandler), 0.3, true);
	/** ****************query panel end*************** */

	/** ****************dtGrid panel start************ */
	var recordArray_dt = [ getRecord("字典类型编码", "ename", "string", 70, true),
			getRecord("字典类型名称", "cname", "string", 100, true),
			getRecord("描述", "typeDesc", "string", 300) ];
	var grid_dt = new GridUtil(dtPageUrl, recordArray_dt, pageSize);

	var dt_Bar = getCommonBar(dtSaveHandler, dtModifyHandler, dtDeleteHandler, userPermission);
	grid_dt.setBottomBar(dt_Bar);

	var dt_listeners = {
		celldblclick : function(thisGrid, rowIndex, columnIndex, ev) {
			if(userPermission.SAVE == 'true') {
				dtModifyHandler();
			}
		},
		cellclick : function(thisGrid, rowIndex, columnIndex, ev) {
			var record = thisGrid.getStore().getAt(rowIndex);
			typeId = record.get("ename");
			
			grid_dd.getStore().baseParams.typeId = typeId;
			grid_dd.getStore().load();
		}
	};
	grid_dt.setListeners(dt_listeners);

	var grid_dt_attr = {
		title : "字典类型列表",
		height : 200,
		region : "center"
	};
	grid_dt.setGridPanel(grid_dt_attr);
	/** ****************dtGrid panel end************ */

	/** ****************ddGrid panel start************ */
	var recordArray_dd = [ getRecord("字典编码", "dname", "string", 70),
			getRecord("字典名称", "dvalue", "string", 100),
			getRecord("排序号", "sortNum", "string", 300),
			getRecord(null, "typeId", "string", null) ];
	var grid_dd = new GridUtil(ddListUrl, recordArray_dd);

	var dd_Bar = getCommonBar(ddSaveHandler, ddModifyHandler, ddDeleteHandler, userPermission);
	grid_dd.setBottomBar(dd_Bar);

	var dd_listeners = {
		celldblclick : function(thisGrid, rowIndex, columnIndex, ev) {
			if(userPermission.SAVE == 'true') {
				ddModifyHandler();
			}
		}
	};
	grid_dd.setListeners(dd_listeners);

	var grid_dd_attr = {
		title : "字典数据列表",
		height : 200,
		margins : "20 0 0 0",
		region : "south"
	};
	grid_dd.setGridPanel(grid_dd_attr);
	
	/** ****************ddGrid panel end************ */

	var dt_grid = grid_dt.getGrid();
	var dd_grid = grid_dd.getGrid();

	/**
	 * 数据字典类型删除方法
	 */
	function dtDeleteHandler() {

		if (dt_grid.getSelectionModel().getCount() == 0) {
			Ext.Msg.alert("提示信息", "请至少选择一条记录!");
			return;
		}
		var selectedRecordArray = dt_grid.getSelectionModel().getSelections();

		Ext.Msg.confirm("确认提示", "确定删除选定的记录?", function(btn) {
			if (btn == 'yes') {
				var ids = "";
				for ( var i = 0; i < selectedRecordArray.length; i++) {
					ids += selectedRecordArray[i].get("ename") + ",";
				}

				var params = {
					ids : ids
				};

				var ajaxClass = new AjaxUtil(dtDeleteUrl);
				ajaxClass.request(params, true, null, function(obj) {
					grid_dt.getStore().reload();
					grid_dd.getStore().removeAll();
				});
			}
		});
	}

	/**
	 * 数据字典项删除方法
	 */
	function ddDeleteHandler() {
		if (dd_grid.getSelectionModel().getCount() == 0) {
			Ext.Msg.alert("提示信息", "请至少选择一条记录!");
			return;
		}
		var selectedRecordArray = dd_grid.getSelectionModel().getSelections();

		Ext.Msg.confirm("确认提示", "确定删除选定的记录?", function(btn) {
			if (btn == 'yes') {
				var ids = "";
				for ( var i = 0; i < selectedRecordArray.length; i++) {
					ids += selectedRecordArray[i].get("dname") + ",";
				}

				var params = {
					ids : ids,
					typeId : typeId
				};

				var ajaxClass = new AjaxUtil(ddDeleteUrl);
				ajaxClass.request(params, true, null, function(obj) {
					grid_dd.getStore().reload();
				});
			}
		});
	}

	var dt_form = new FormUtil( {
		id : "dtForm",
		labelWidth : 110,
		collapsible : false,
		border : false
	});

	dt_form.addComp(getText("ename", "字典类型编码", 180), 1.0, false);
	dt_form.addComp(getText("cname", "字典类型名称", 180), 1.0, false);
	dt_form.addComp(getHidden("dtFormType"), 0, true);
	dt_form.addComp(getTextArea("typeDesc", "描述", 180, 60), 1.0, true);

	var dt_win = new WindowUtil( {
		width : 370,
		height : 220
	}, dt_form.getPanel(), dtSaveOrModify);

	function dtSaveHandler() {
		dt_win.show("新增数据字典类型");
		
		setTextEditable(dt_form.findById("ename"));
		
		dt_form.getForm().reset();
		dt_form.findById("dtFormType").setValue(
				Configuration.opType.SAVE);
	}

	function dtModifyHandler() {
		if (dt_grid.getSelectionModel().getCount() != 1) {
			Ext.Msg.alert("提示信息", "请选择一条记录!");
			return;
		}

		dt_win.show("修改数据字典类型");
		dt_form.getForm().reset();

		// 将主键置为不可编辑
		setTextReadOnly(dt_form.findById("ename"));

		var selectedRecord = dt_grid.getSelectionModel().getSelected();
		var values = {
			ename : selectedRecord.get("ename"),
			cname : selectedRecord.get("cname"),
			typeDesc : selectedRecord.get("typeDesc"),
			dtFormType : Configuration.opType.MODIFY
		};
		dt_form.getForm().setValues(values);
	}

	function dtSaveOrModify() {
		var dtForm = dt_form.getForm();

		if (!dtForm.isValid()) {
			Ext.Msg.alert("提示信息", "请填写完整的字典类型信息!");
			return;
		}

		var ajaxClass = new AjaxUtil(dtSubmitUrl);

		var opType = dt_form.findById("dtFormType").getValue();

		var params = {
			opType : opType
		};

		var callBack_obj = new Object();
		callBack_obj.grid = grid_dt;
		callBack_obj.win = dt_win;

		ajaxClass.submit(dtForm, params, true, callBack_obj, function(obj) {
			obj.grid.getStore().reload();
			obj.win.hide();
		});
	}

	var dd_form = new FormUtil( {
		id : "ddForm",
		collapsible : false,
		border : false
	});

	dd_form.addComp(getText("dname", "字典编码", 150), 1.0, false);
	dd_form.addComp(getText("dvalue", "字典名称", 150), 1.0, false);
	dd_form.addComp(getNumberText("sortNum", "排序号", 150), 1.0, true);
	dd_form.addComp(getHidden("ddFormType"), 0, true);
	dd_form.addComp(getHidden("typeId"), 0, true);

	var dd_win = new WindowUtil( {
		width : 300,
		height : 180
	}, dd_form.getPanel(), ddSaveOrModify);

	function ddSaveHandler() {
		if (Ext.isEmpty(typeId)) {
			Ext.Msg.alert("提示信息", "请选择数据字典类型!");
			return;
		}

		dd_win.show("新增数据字典项");
		
		setTextEditable(dd_form.findById("dname"));
		
		dd_form.getForm().reset();
		dd_form.findById("ddFormType").setValue(
				Configuration.opType.SAVE);
		dd_form.findById("typeId").setValue(typeId);
	}

	function ddModifyHandler() {
		if (dd_grid.getSelectionModel().getCount() != 1) {
			Ext.Msg.alert("提示信息", "请选择一条记录!");
			return;
		}

		var ddForm = dd_form.getPanel();

		dd_win.show("修改数据字典项");
		ddForm.getForm().reset();

		// 将主键置为不可编辑
		setTextReadOnly(ddForm.findById("dname"));

		var selectedRecord = dd_grid.getSelectionModel().getSelected();
		var values = {
			dname : selectedRecord.get("dname"),
			dvalue : selectedRecord.get("dvalue"),
			typeId : selectedRecord.get("typeId"),
			sortNum : selectedRecord.get("sortNum"),
			ddFormType : Configuration.opType.MODIFY
		};
		ddForm.getForm().setValues(values);
	}

	function ddSaveOrModify() {
		var ddForm = dd_form.getForm();

		if (!ddForm.isValid()) {
			Ext.Msg.alert("提示信息", "请填写完整的字典数据信息!");
			return;
		}

		var ajaxClass = new AjaxUtil(ddSubmitUrl);

		var opType = dd_form.findById("ddFormType").getValue();

		var params = {
			opType : opType
		};

		var callBack_obj = new Object();
		callBack_obj.grid = grid_dd;
		callBack_obj.win = dd_win;
		callBack_obj.form = dd_form;

		ajaxClass.submit(ddForm, params, true, callBack_obj, function(obj) {
			obj.grid.getStore().reload();
			var thisForm = obj.form;
			var opType = thisForm.findById("ddFormType").getValue();

			if (opType == Configuration.opType.SAVE) {
				thisForm.findById("dname").setValue("");
				thisForm.findById("dvalue").setValue("");
				thisForm.findById("sortNum").setValue("");
			} else {
				obj.win.hide();
			}
		});
	}

	/**
	 * 查询按钮提交方法
	 */
	function onSubmitQueryHandler() {
		var thisForm = query_form.getForm();
		var store = grid_dt.getStore();

		var cname = Ext.getCmp("query_cname").getValue();
		var ename = Ext.getCmp("query_ename").getValue();
		
		store.baseParams.ename = ename;
		store.baseParams.cname = cname;
		
		store.load( {
			params : {
				start : 0,
				limit : grid_dt.pageSize
			}
		});
	}

	var viewport = new Ext.Viewport( {
		border : false,
		layout : "border",
		items : [ query_form.getPanel(), dt_grid, dd_grid ]
	});

	onSubmitQueryHandler();
	completePage();
});