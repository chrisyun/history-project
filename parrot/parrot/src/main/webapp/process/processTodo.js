var todoPageUrl = sys.path + "/wf/cm/getPendingPage.jmt";

var getUsersByProvince = sys.path + "/user/getUserList.jmt";

var pageSize = 20;
var todostore;

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
	
	query_form.addComp(getComboBox("query_flowType", "流程类型", 160, flowTypeDD, false),
			0.3, true);
	
	var provinceCb = getComboBox("query_area", "申报省份", 160, area, false);
	provinceCb.listeners = {
		"change" : function(field, newValue, oldValue) {
			query_form.findById("show_query_creater").setValue();
			userStore.load();
		}	
	};
	query_form.addComp(provinceCb, 0.3, true);
	var userCb = getComboBox("query_creater", "申报单位", 160, null);
	userCb.valueField = "userId";
	userCb.displayField = "userName";
	userCb.lazyInit = true;
	userCb.mode = "remote";
	userCb.store = userStore;
	query_form.addComp(userCb, 0.3, true);
	
	query_form.addComp(getComboBox("query_flowStatus", "审批状态", 160, flowStatusDD, false),
			0.3, true);
	query_form.addComp(getText("query_createrName", "申报单位名称",160), 0.3, true);
	query_form.addComp(getText("query_pName", "项目名称",160), 0.3, true);
	
	
	query_form.addComp(getDateText("query_startTime", "开始日期", 160), 0.3, true);
	query_form.addComp(getDateText("query_endTime", "结束日期", 160), 0.3, true);
	
	query_form.addComp(getQueryBtn(onSubmitQueryHandler), 0.3, true);
	/** ****************query panel end*************** */

	/** ****************todo Grid panel start************ */
	var openRender = function(name, metadata, record, rowIndex, colIndex) {
		var id = record.get("id");
		var link = "<a href=\"javascript:openTodoFlow('"+id+"');\">" + name + "</a>";

		return link;
	};
	
	var recordArray = [
			getRecord(null, "id", "string"),
			getRecord("流程类型", "flowType", "string", 150, true, flowTypeRender),
			getRecord("项目名称", "pName", "string", 200),
			getRecord("当前环节", "activity", "string", 100, true),
			getRecord("申报地区或单位", "createrName", "string", 200, true, openRender),
			getRecord("上一环节审批情况", "flowStatus", "string", 150, true, flowStatusRender),
			getRecord("申报时间", "createTime", "string", 100, true),
			getRecord("申报省份", "province", "string", 100, true, areaRender)];
	var todo_grid = new GridUtil(todoPageUrl, recordArray, pageSize);
	
	var listeners = {
		celldblclick : function(thisGrid, rowIndex, columnIndex, ev) {
			showHandler();
		}
	};
	todo_grid.setListeners(listeners);
	
	todo_grid.setBottomBar();

	var grid_attr = {
		title : "待办列表（双击表格或单击申报单位链接打开待办）",
		region : "center"
	};
	todo_grid.setGridPanel(grid_attr);
	/** ****************todo Grid panel end************ */

	var grid = todo_grid.getGrid();
	todostore = todo_grid.getStore();
	
	function showHandler() {
		if (grid.getSelectionModel().getCount() != 1) {
			Ext.Msg.alert("提示信息", "请选择一条记录!");
			return;
		}
		var selectedRecord = grid.getSelectionModel().getSelected();
		
		var id = selectedRecord.get("id");
		openTodoFlow(id);
	}
	
	
	function onSubmitQueryHandler() {

		var flowType = Ext.getCmp("show_query_flowType").getValue();
		var flowStatus = Ext.getCmp("show_query_flowStatus").getValue();
		var province = Ext.getCmp("show_query_area").getValue();
		var creater = Ext.getCmp("show_query_creater").getValue();
		var createrName = Ext.getCmp("query_createrName").getValue();
		var pName = Ext.getCmp("query_pName").getValue();
		var startTime = Ext.getCmp("query_startTime").getValue();
		if(! Ext.isEmpty(startTime)) {
			startTime = startTime.format("Y-m-d");
		}
		var endTime = Ext.getCmp("query_endTime").getValue();
		if(! Ext.isEmpty(endTime)) {
			endTime = endTime.format("Y-m-d");
		}
		
		todostore.baseParams.flowType = flowType;
		todostore.baseParams.flowStatus = flowStatus;
		todostore.baseParams.province = province;
		todostore.baseParams.creater = creater;
		todostore.baseParams.createrName = createrName;
		todostore.baseParams.pName = pName;
		todostore.baseParams.startTime = startTime;
		todostore.baseParams.endTime = endTime;

		todostore.load({
					params : {
						start : 0,
						limit : todo_grid.pageSize
					}
				});
	}

	var viewport = new Ext.Viewport({
				border : false,
				layout : "border",
				items : [query_form.getPanel(), todo_grid.getGrid()]
			});

	onSubmitQueryHandler();
	completePage();
});

function refreshGrid() {
	todostore.reload();
}

function openTodoFlow(id) {
	var url = sys.path + "/wf/cm/openTodoProcess.jmt?id=" + id;
	var height = window.screen.availHeight - 50;
	var width = window.screen.availWidth - 50;
	
	window.open (url, "flowPage", 
			"height="+height+", width="+width+", top=0, left=0, toolbar=no, menubar=no, scrollbars=yes,resizable=no,location=no, status=yes");
	//window.open (url, "flowPage");
}
