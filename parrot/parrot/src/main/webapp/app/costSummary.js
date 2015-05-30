var summaryPageUrl = sys.path + "/heritor/summaryAllCost.jmt";
var exportUrl = sys.path + "/heritor/exportAllSummaryExcel.jmt";

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
	
	query_form.addComp(getComboBox("query_year", "申报年份", 160, year, false),
			0.3, true);
	var provinceCb = getComboBox("query_area", "申报省份", 160, area, false);
	query_form.addComp(provinceCb, 0.3, true);
	
	query_form.addComp(getQueryBtn(onSubmitQueryHandler), 0.3, true);
	
	var now = new Date(); 
	Ext.getCmp("show_query_year").setValue(now.getFullYear());
	Ext.getCmp("show_query_area").setValue(user.org);
	
	/** ****************query panel end*************** */

	/** ****************summary Grid panel start************ */
	var recordArray = [
			getRecord("项目名称", "name", "string", 300, true),
			getRecord("金额（万元）", "money", "string", 100, true)];
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
		title : "国家非物质文化遗产保护专项资金申报汇总表",
		region : "center"
	};
	summary_grid.setGridPanel(grid_attr);
	/** ****************summary Grid panel end************ */

	var grid = summary_grid.getGrid();
	summarystore = summary_grid.getStore();
	
	summarystore.addListener("load", function(store, records) {
		var totalMoney = 0;
		for(var i=0; i<records.length; i++) {
			var money = parseFloat(records[i].get("money"));
			
			totalMoney = parseFloat(totalMoney) + money;
		}
		Ext.getCmp("summaryTotal").setValue(totalMoney);
	});
	
	function onSubmitQueryHandler() {

		var province = Ext.getCmp("show_query_area").getValue();
		var year = Ext.getCmp("show_query_year").getValue();
		
		summarystore.baseParams.year = year;
		summarystore.baseParams.province = province;
		summarystore.baseParams.heritorMoney = heritorMoney;

		summarystore.load();
	}
	
	function exportHandler() {
		var province = Ext.getCmp("show_query_area").getValue();
		var year = Ext.getCmp("show_query_year").getValue();
		
		var excelUrl = exportUrl + "?province=" + province
				+ "&heritorMoney=" + heritorMoney
				+ "&year=" + year +
				"&key=" + Math.random();
		
		document.getElementById("excelFrame").src = excelUrl;
	}

	var viewport = new Ext.Viewport({
				border : false,
				layout : "border",
				items : [query_form.getPanel(), summary_grid.getGrid()]
			});

	onSubmitQueryHandler();
	completePage();
});
