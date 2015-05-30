var pcPageUrl = sys.path + "/resever/getReseverCostPage.jmt";

var getUsersByProvince = sys.path + "/user/getUserList.jmt";
var getReseverByProvince = sys.path + "/resever/getReseverByProvince.jmt";

var pageSize = 20;
var pcstore;

Ext.onReady(function() {
	Ext.QuickTips.init();

	/** ****************query panel start*************** */
	var query_attr = {
		title : "查询列表",
		region : "north",
		height : 130,
		labelWidth : 100
	};
	
	var query_form = new FormUtil(query_attr);
	
	var userStore = new Ext.data.Store({
		url : getUsersByProvince,
		listeners : {
			"beforeload" : function(thisStore) {
				var province = query_form.findById("show_query_area").getValue();
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
	
	var reseverStore = new Ext.data.Store({
		url : getReseverByProvince,
		listeners : {
			"beforeload" : function(thisStore) {
				var province = query_form.findById("show_query_area").getValue();
				thisStore.baseParams.province = province;
			}
		},
		reader : new Ext.data.JsonReader({}, Ext.data.Record
						.create([{
									name : 'id',
									type : 'string'
								}, {
									name : 'name',
									type : 'string'
								}]))
	});
	
	
	var provinceCb = getComboBox("query_area", "申报省份", 160, area, false);
	provinceCb.listeners = {
		"change" : function(field, newValue, oldValue) {
			query_form.findById("show_query_creater").setValue();
			query_form.findById("show_query_reseverId").setValue();
			userStore.load();
			reseverStore.load();
		}	
	};
	query_form.addComp(provinceCb, 0.3, true);
	
	var reseverCb = getComboBox("query_reseverId", "文化生态保护区", 300, null);
	reseverCb.valueField = "id";
	reseverCb.displayField = "name";
	reseverCb.lazyInit = true;
	reseverCb.mode = "remote";
	reseverCb.store = reseverStore;
	query_form.addComp(reseverCb, 0.4, true);
	query_form.addComp(getText("query_name", "保护区名称",160), 0.3, true);
	
	query_form.addComp(getComboBox("query_year", "申报年份", 160, year, false),
			0.3, true);
	var userCb = getComboBox("query_creater", "申报单位", 300, null);
	userCb.valueField = "userId";
	userCb.displayField = "userName";
	userCb.lazyInit = true;
	userCb.mode = "remote";
	userCb.store = userStore;
	query_form.addComp(userCb, 0.4, true);
	query_form.addComp(getText("query_createrName", "申报单位名称",160), 0.3, true);
	
	query_form.addComp(getComboBox("query_activity", "当前环节", 160, 
			[["","全部"],["申报环节","申报环节"],["起草暂存环节","起草暂存环节"],["省厅审批环节","省厅审批环节"],
			 ["非遗司审批环节","非遗司审批环节"],["审批完成已归档","审批完成已归档"]], false),
			0.3, true);
	query_form.addComp(getDateText("query_startTime", "开始日期", 90), 0.19, true);
	query_form.addComp(getDateText("query_endTime", "结束日期", 90), 0.25, true);
	
	query_form.addComp(getQueryBtn(onSubmitQueryHandler), 0.2, true);
	/** ****************query panel end*************** */

	/** ****************rc Grid panel start************ */
	var recordArray = [
			getRecord(null, "id", "string"),
			getRecord("文化生态保护区名称", "reseverName", "string", 200, true),
			getRecord("申报年份", "year", "string", 60, true),
			getRecord("申报地区或单位", "createrName", "string", 150, true),
			getRecord("已拨金额(万元)", "givenMoney", "string", 100, true),
			getRecord("申报金额(万元)", "applyMoney", "string", 100, true),
			getRecord("申报时间", "applyTime", "string", 150, true),
			getRecord("当前环节", "activity", "string", 100, true),
			getRecord("审批结果", "flowStatus", "string", 80, true, flowStatusRender),
			getRecord("申报省份", "province", "string", 60, true, areaRender)];
	var pc_grid = new GridUtil(pcPageUrl, recordArray, pageSize);
	
	pc_grid.setBottomBar();
	
	var listeners = {
		celldblclick : function(thisGrid, rowIndex, columnIndex, ev) {
			var selectedRecord = grid.getSelectionModel().getSelected();
			var id = selectedRecord.get("id");
			openDoneFlow(id, 'resever');
		}
	};
	pc_grid.setListeners(listeners);
	
	var grid_attr = {
		title : "文化生态保护区补助费用列表（双击查看详情）",
		region : "center"
	};
	pc_grid.setGridPanel(grid_attr);
	/** ****************pc Grid panel end************ */

	var grid = pc_grid.getGrid();
	pcstore = pc_grid.getStore();

	function onSubmitQueryHandler() {

		var pid = Ext.getCmp("show_query_reseverId").getValue();
		var name = Ext.getCmp("query_name").getValue();
		
		var activity = Ext.getCmp("show_query_activity").getValue();
		var province = Ext.getCmp("show_query_area").getValue();
		var userId = Ext.getCmp("show_query_creater").getValue();
		var userName = Ext.getCmp("query_createrName").getValue();
		var startTime = Ext.getCmp("query_startTime").getValue();
		var year = Ext.getCmp("show_query_year").getValue();
		if(! Ext.isEmpty(startTime)) {
			startTime = startTime.format("Y-m-d");
		}
		var endTime = Ext.getCmp("query_endTime").getValue();
		if(! Ext.isEmpty(endTime)) {
			endTime = endTime.format("Y-m-d");
		}
		
		pcstore.baseParams.activity = activity;
		pcstore.baseParams.year = year;
		pcstore.baseParams.name = name;
		pcstore.baseParams.pid = pid;
		pcstore.baseParams.province = province;
		pcstore.baseParams.userId = userId;
		pcstore.baseParams.userName = userName;
		pcstore.baseParams.startTime = startTime;
		pcstore.baseParams.endTime = endTime;

		pcstore.load({
					params : {
						start : 0,
						limit : pc_grid.pageSize
					}
				});
	}

	var viewport = new Ext.Viewport({
				border : false,
				layout : "border",
				items : [query_form.getPanel(), pc_grid.getGrid()]
			});

	onSubmitQueryHandler();
	completePage();
});

function refreshGrid() {
	pcstore.reload();
}

function openDoneFlow(id, type) {
	var url = sys.path + "/wf/cm/openDoneOrDoingProcess.jmt?pid=" 
		+ id + "&flowType=" + type;
	var height = window.screen.availHeight - 50;
	var width = window.screen.availWidth - 50;
	
	window.open (url, "flowPage", 
			"height="+height+", width="+width+", top=0, left=0, toolbar=no, menubar=no, scrollbars=yes,resizable=no,location=no, status=yes");
}
