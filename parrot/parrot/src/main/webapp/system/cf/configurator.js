var cfGetAllUrl = sys.path + "/cf/getConfigName.jmt";
var cfGetConfigUrl = sys.path + "/cf/getConfig.jmt";
var cfmodifyConfigUrl = sys.path + "/cf/modifyConfig.jmt";

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

	var configNames = {
		xtype : "combo",
		id : "show_configPath",
		hiddenName : "configPath",
		triggerAction : "all",
		resizable : true,
		lazyInit : true,
		mode : "remote",
		width : 300,
		emptyText : "---请先选择配置文件---",
		fieldLabel : "配置文件",
		valueField : "name",
		displayField : "name",
		store : new Ext.data.Store({
			url : cfGetAllUrl,
			reader : new Ext.data.JsonReader({}, Ext.data.Record
							.create([{
										name : 'name',
										type : 'string'
									}]))
		})
	};
    
	query_form.addComp(configNames, 0.4, false);
	query_form.addComp(getQueryBtn(onSubmitQueryHandler), 0.3, true);
	/** ****************query panel end*************** */

	/** ****************Grid panel start************** */
	var saveBtn = "";
	if(userPermission.MODIFY == "true") {
		saveBtn = {
			id : "save-nav",
			text : "保&nbsp;&nbsp;&nbsp;&nbsp;存",
			iconCls : "silk-save",
			width : 300,
			handler : saveCfHandler
		};
	}
	
	var propsGrid = new Ext.grid.PropertyGrid({
		region : "center",
		iconCls : "silk-grid",
		collapsible : true,
		title: "配置项",
		loadMask : true,
    	width : 400,
		buttonAlign : "center",
		buttons : [saveBtn]
	});
	
	var tipsPanel = new Ext.Panel({
		title : "提示",
		region : "east",
		collapsible : true,
		margins : "0 0 0 2",
		width : 300,
		html : "<div class='tips'>1、部分应用服务器修改配置文件后会自动重启。<br>2、配置文件如果没有配置监听，需要重启应用才能生效。</div>"
	});
	
	/** ****************Grid panel end************** */
	var viewport = new Ext.Viewport({
				border : false,
				layout : "border",
				items : [query_form.getPanel(),propsGrid,tipsPanel]
			});
	
	function saveCfHandler() {
		var source = propsGrid.getSource();
		
		var name = Ext.getCmp("show_configPath").getValue();
		if(Ext.isEmpty(name)) {
			Ext.Msg.alert("提示信息", "请选择配置文件!");
			return;
		}
		
		source.name = name;
		
		var ajax = new AjaxUtil(cfmodifyConfigUrl);
		ajax.request(source, true);
	}
			
	function onSubmitQueryHandler() {
		var thisForm = query_form.getForm();

		if (!thisForm.isValid()) {
			Ext.Msg.alert("提示信息", "请选择配置文件!");
			return;
		}
		
		var name = Ext.getCmp("show_configPath").getValue();
		var ajax = new AjaxUtil(cfGetConfigUrl);
		ajax.getData({"name":name}, propsGrid, function(obj, data) {
			propsGrid.setSource(data);
		});
	}			

	completePage();	
});
