var logPageUrl = sys.path + "/log/getLogPage.jmt";
var exportUrl = sys.path + "/log/exportLogExcel.jmt";

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

	query_form.addComp(getDateText("query_startTime", "开始日期", 120, new Date()
							.add(Date.MONTH, -1)), 0.2, true);
	query_form.addComp(getDateText("query_endTime", "结束日期", 120, new Date()
							.add(Date.DAY, 1)), 0.2, true);
	query_form.addComp(getComboBox("query_module", "模块", 120, module, false),
			0.2, true);
	query_form.addComp(getComboBox("query_result", "操作结果", 120, handleResult,
					false), 0.2, true);
	query_form.addComp(getQueryBtn(onSubmitQueryHandler), 0.2, true);
	/** ****************query panel end*************** */

	/** ****************log Grid panel start************ */
	var msgRender = function(remark, metadata, record, rowIndex, colIndex) {
		return Render.detailRender(remark, log_grid.cm, colIndex);
	};

	var recordArray = [
			getRecord(null, "id", "string"),
			getRecord(null, "parameters", "string"),
			getRecord("模块名称", "moduleName", "string", 200, false, moduleRender),
			getRecord("方法名称", "methodName", "string", 150, true),
			getRecord("操作人", "userId", "string", 70, true),
			getRecord("操作时间", "executeTime", "string", 150, true),
			getRecord("操作结果", "handleResult", "string", 50, true,
					handleResultRender),
			getRecord("错误信息", "errorMsg", "string", 200, false, msgRender)];
	var log_grid = new GridUtil(logPageUrl, recordArray, pageSize);
	
	var bar = null;
	if(userPermission.EXPORT == "true") {
		bar = ["-",{
					text : "日志导出",
					iconCls : "silk-excel",
					minWidth : Configuration.minBtnWidth,
					handler : exportHandler
				}];
	}
				
	log_grid.setBottomBar(bar);

	var listeners = {
		celldblclick : function(thisGrid, rowIndex, columnIndex, ev) {
			showHandler();
		}
	};
	log_grid.setListeners(listeners);

	var grid_attr = {
		title : "日志列表",
		region : "center"
	};
	log_grid.setGridPanel(grid_attr);
	/** ****************log Grid panel end************ */

	var grid = log_grid.getGrid();
	var store = log_grid.getStore();

	/** ****************log window start************ */
	var log_form = new FormUtil({
				id : "logForm",
				collapsible : false,
				labelWidth : 100,
				border : false
			});

	log_form.addComp(getComboBox("moduleName", "模块名称", 180, module, true), 0.5,
			true);
	log_form.addComp(getText("methodName", "方法名称", 180), 0.5, true);
	log_form.addComp(getTextArea("parameters", "方法参数", 500, 60), 1.0, true);
	log_form.addComp(getText("userId", "操作人", 180), 0.5, true);
	log_form.addComp(getText("executeTime", "操作时间", 180), 0.5, true);
	log_form.addComp(getComboBox("handleResult", "操作结果", 180, handleResult,
					true), 0.5, true);
	log_form.addComp(getTextArea("errorMsg", "错误信息", 500, 60), 1.0, true);

	var log_win = new WindowUtil({
				width : 650,
				height : 340
			}, log_form.getPanel(), null);

	/** ****************log window end************ */

	function showHandler() {
		if (grid.getSelectionModel().getCount() != 1) {
			Ext.Msg.alert("提示信息", "请选择一条记录!");
			return;
		}

		// 不显示保存按钮
		log_win.hideSaveBtn();

		log_win.show("日志详情");
		log_form.getForm().reset();

		var selectedRecord = grid.getSelectionModel().getSelected();
		var values = {
			moduleName : selectedRecord.get("moduleName"),
			parameters : selectedRecord.get("parameters"),
			handleResult : selectedRecord.get("handleResult"),
			errorMsg : selectedRecord.get("errorMsg"),
			userId : selectedRecord.get("userId"),
			methodName : selectedRecord.get("methodName"),
			executeTime : selectedRecord.get("executeTime")
		};
		log_form.getForm().setValues(values);
	}

	function exportHandler() {
		var startTime = Ext.getCmp("query_startTime").getValue()
				.format("Y-m-d");
		var endTime = Ext.getCmp("query_endTime").getValue().format("Y-m-d");
		var moduleName = Ext.getCmp("show_query_module").getValue();
		var handleResult = Ext.getCmp("show_query_result").getValue();

		var excelUrl = exportUrl + "?moduleName=" + moduleName
				+ "&handleResult=" + handleResult +
		"&startTime=" + startTime + "&endTime=" + endTime;
		
		document.getElementById("excelFrame").src = excelUrl;
	}

	function onSubmitQueryHandler() {
		var startTime = Ext.getCmp("query_startTime").getValue()
				.format("Y-m-d");
		var endTime = Ext.getCmp("query_endTime").getValue().format("Y-m-d");
		var moduleName = Ext.getCmp("show_query_module").getValue();
		var handleResult = Ext.getCmp("show_query_result").getValue();

		store.baseParams.moduleName = moduleName;
		store.baseParams.handleResult = handleResult;
		store.baseParams.startTime = startTime;
		store.baseParams.endTime = endTime;

		store.load({
					params : {
						start : 0,
						limit : log_grid.pageSize
					}
				});
	}

	var viewport = new Ext.Viewport({
				border : false,
				layout : "border",
				items : [query_form.getPanel(), log_grid.getGrid()]
			});

	onSubmitQueryHandler();
	completePage();
});
