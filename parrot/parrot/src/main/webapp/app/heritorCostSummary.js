var summaryPageUrl = sys.path + "/heritor/getHeritorList.jmt";
var exportUrl = sys.path + "/heritor/exportHeritorCostExcel.jmt";

Ext.onReady(function() {
	Ext.QuickTips.init();

	/** ****************query panel start*************** */
	var query_attr = {
		title : "查询列表",
		region : "north",
		height : 70,
		labelWidth : 100
	};
	
	var query_form = new FormUtil(query_attr);
	var provinceCb = getComboBox("query_area", "申报省份", 160, area, false);
	query_form.addComp(provinceCb, 0.3, true);
	
	query_form.addComp(getQueryBtn(onSubmitQueryHandler), 0.3, true);

	Ext.getCmp("show_query_area").setValue(user.org);
	
	/** ****************query panel end*************** */

	/** ****************summary Grid panel start************ */
	var moneyRender = function(remark, metadata, record, rowIndex, colIndex) {
		return heritorMoney;
	};
	
	var recordArray = [
			getRecord(null, "id", "string"),
			getRecord(null, "projectId", "string"),
			getRecord("姓名", "name", "string", 80, true),
			getRecord("身份证号", "idCard", "string", 200, false),
			getRecord("项目编码", "projectCode", "string", 80, true),
			getRecord("项目名称", "projectName", "string", 200, true),
			getRecord("金额（万元）", "money", "string", 120, true, moneyRender)];
	var summary_grid = new GridUtil(summaryPageUrl, recordArray);
	
	var bBar = new Array();
	bBar.push({
		text : "合计（万元）:",
		xtype : "tbtext"
	});
	bBar.push({
		xtype : "textfield",
		id : "summaryTotal",
		width : 100,
		readOnly : true
	});
	if(userPermission.EXCEL) {
		bBar.push("-");
		bBar.push({
			text : "费用导出",
			iconCls : "silk-excel",
			minWidth : Configuration.minBtnWidth,
			handler : exportHandler
		});
	}
	
	summary_grid.setBottomBar(bBar);
	
	var grid_attr = {
		title : "国家级代表性传承人补助费汇总列表",
		region : "center"
	};
	summary_grid.setGridPanel(grid_attr);
	
	var recordArrayDie = [
   			getRecord(null, "id", "string"),
   			getRecord(null, "projectId", "string"),
   			getRecord("姓名", "name", "string", 80, true),
   			getRecord("身份证号", "idCard", "string", 200, false),
   			getRecord("项目编码", "projectCode", "string", 80, true),
   			getRecord("项目名称", "projectName", "string", 200, true),
   			getRecord("去世时间", "dieDate", "string", 120, true)];
   	var summary_grid_die = new GridUtil(summaryPageUrl, recordArrayDie);

   	var grid_attr_die = {
   		title : "已去世代表性传承人名单",
   		height : 150,
   		region : "south"
   	};
   	summary_grid_die.setGridPanel(grid_attr_die);
	
	/** ****************summary Grid panel end************ */

	var grid = summary_grid.getGrid();
	summarystore = summary_grid.getStore();
	summarystore_die = summary_grid_die.getStore();
	
	summarystore.addListener("load", function(store, records) {
		var totalMoney = 0;
		totalMoney = records.length * heritorMoney;
		Ext.getCmp("summaryTotal").setValue(totalMoney);
	});
	
	function onSubmitQueryHandler() {

		var province = Ext.getCmp("show_query_area").getValue();
		summarystore.baseParams.province = province;
		summarystore.baseParams.isDie = "NO";
		summarystore.load();
		
		summarystore_die.baseParams.province = province;
		summarystore_die.baseParams.isDie = "YES";
		summarystore_die.load();
	}
	
	function exportHandler() {
		var province = Ext.getCmp("show_query_area").getValue();

		var excelUrl = exportUrl + "?province=" + province
				+ "&heritorMoney=" + heritorMoney +
				"&key=" + Math.random();
		
		document.getElementById("excelFrame").src = excelUrl;
	}

	var viewport = new Ext.Viewport({
				border : false,
				layout : "border",
				items : [query_form.getPanel(), 
				         summary_grid.getGrid(), 
				         summary_grid_die.getGrid()]
			});

	onSubmitQueryHandler();
	completePage();
});
