var pcPageUrl = sys.path + "/project/getProjectCostPage.jmt";

var getUsersByProvince = sys.path + "/user/getUserList.jmt";
var getProjectByProvince = sys.path + "/project/getProjectByProvince.jmt";

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
	
	var projectStore = new Ext.data.Store({
		url : getProjectByProvince,
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
	
	
	var provinceCb = getComboBox("query_area", "申报省份", 130, area, false);
	provinceCb.listeners = {
		"change" : function(field, newValue, oldValue) {
			query_form.findById("show_query_creater").setValue();
			query_form.findById("show_query_projectId").setValue();
			userStore.load();
			projectStore.load();
		}	
	};
	query_form.addComp(provinceCb, 0.23, true);
	
	var projectCb = getComboBox("query_projectId", "非遗项目", 250, null);
	projectCb.valueField = "id";
	projectCb.displayField = "name";
	projectCb.lazyInit = true;
	projectCb.mode = "remote";
	projectCb.store = projectStore;
	query_form.addComp(projectCb, 0.35, true);
	query_form.addComp(getText("query_name", "非遗项目名称",130), 0.23, true);
	query_form.addComp(getDateText("query_startTime", "开始日期", 75), 0.19, true);
	
	query_form.addComp(getComboBox("query_Type", "项目类别", 130, projectTypeDD, false),
			0.23, true);
	var userCb = getComboBox("query_creater", "申报单位", 250, null);
	userCb.valueField = "userId";
	userCb.displayField = "userName";
	userCb.lazyInit = true;
	userCb.mode = "remote";
	userCb.store = userStore;
	query_form.addComp(userCb, 0.35, true);
	query_form.addComp(getText("query_createrName", "申报单位名称",130), 0.23, true);
	query_form.addComp(getDateText("query_endTime", "结束日期", 75), 0.19, true);
	
	
	query_form.addComp(getComboBox("query_year", "申报年份", 130, year, false),
			0.23, true);
	query_form.addComp(getComboBox("query_isUn", "联合国非遗项目", 250, yesOrNo, false),
			0.35, true);
	query_form.addComp(getComboBox("query_activity", "当前环节", 130, 
			[["","全部"],["申报环节","申报环节"],["起草暂存环节","起草暂存环节"],["省厅审批环节","省厅审批环节"],
			 ["非遗司审批环节","非遗司审批环节"],["审批完成已归档","审批完成已归档"]], false),
			0.25, true);
	
	query_form.addComp(getQueryBtn(onSubmitQueryHandler), 0.17, true);
	/** ****************query panel end*************** */

	/** ****************pc Grid panel start************ */
	var projectNameRender = function(name, metadata, record, rowIndex, colIndex) {
		var isUn = record.get("isUnProject");
		
		if(isUn == "YES") {
			name = "★ " + name;
		}
		return name;
	};
	
	var recordArray = [
			getRecord(null, "id", "string"),
			getRecord(null, "isUnProject", "string"),
			getRecord("项目类别", "type", "string", 80, true, projectTypeRender),
			getRecord("项目编码", "code", "string", 80, true),
			getRecord("项目名称", "projectName", "string", 200, true, projectNameRender),
			getRecord("申报年份", "year", "string", 80, true),
			getRecord("申报地区或单位", "createrName", "string", 150, true),
			getRecord("金额(万元)", "money", "string", 80, true),
			getRecord("申报时间", "applyTime", "string", 150, true),
			getRecord("当前环节", "activity", "string", 100, true),
			getRecord("审批结果", "flowStatus", "string", 80, true, flowStatusRender),
			getRecord("申报省份", "province", "string", 80, true, areaRender)];
	var pc_grid = new GridUtil(pcPageUrl, recordArray, pageSize);
	
	pc_grid.setBottomBar();
	
	var listeners = {
		celldblclick : function(thisGrid, rowIndex, columnIndex, ev) {
			var selectedRecord = grid.getSelectionModel().getSelected();
			var id = selectedRecord.get("id");
			openDoneFlow(id, 'project');
		}
	};
	pc_grid.setListeners(listeners);
	
	var grid_attr = {
		title : "非遗项目费用列表（双击查看详情）",
		region : "center"
	};
	pc_grid.setGridPanel(grid_attr);
	/** ****************pc Grid panel end************ */

	var grid = pc_grid.getGrid();
	pcstore = pc_grid.getStore();

	function onSubmitQueryHandler() {

		var projectType = Ext.getCmp("show_query_Type").getValue();
		var isUnProject = Ext.getCmp("show_query_isUn").getValue();
		
		var pid = Ext.getCmp("show_query_projectId").getValue();
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
		
		pcstore.baseParams.year = year;
		pcstore.baseParams.activity = activity;
		pcstore.baseParams.name = name;
		pcstore.baseParams.pid = pid;
		pcstore.baseParams.isUnProject = isUnProject;
		pcstore.baseParams.projectType = projectType;
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
