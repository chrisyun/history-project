var oneForm,twoForm,threeForm,mainTab,budgetStore;

function startProcessHandler() {
	
	var getReseverByUser = sys.path + "/resever/getReseverByUser.jmt";
	var loadFormUrl = sys.path + "/resever/getReseverCostById.jmt";
	var loadBaseInfoUrl = sys.path + "/resever/getReseverDW.jmt";
	var getBudgetUrl = sys.path + "/wf/cm/getBudget.jmt";
	
	oneForm = new FormUtil({
		iconCls : "",
		title : "基本信息",
		border : false,
		id : "oneForm",
		autoScroll : true,
		collapsible : false,
		labelWidth : 220
	});
	
	var reseverStore = new Ext.data.Store({
		url : getReseverByUser,
		autoLoad : true,
		reader : new Ext.data.JsonReader({}, Ext.data.Record
						.create([{
									name : "id",
									type : "string"
								}, {
									name : "name",
									type : "string"
								}]))
	});
	
	var ajaxInfo = new AjaxUtil(loadBaseInfoUrl);
	
	var reseverCb = getComboBox("reseverId", "文化生态保护区名称", 300, null);
	reseverCb.valueField = "id";
	reseverCb.displayField = "name";
	reseverCb.lazyInit = true;
	reseverCb.mode = "remote";
	reseverCb.store = reseverStore;
	reseverCb.listeners = {
		"change" : function(field, newValue, oldValue) {
			ajaxInfo.getData({
				reseverId : newValue
			}, null, function(scope, data) {
				
				var values = new Object();
				
				if(Ext.isEmpty(data)) {
					values.address = "";
					values.postalCode = "";
					values.contacts = "";
					values.phone = "";
				} else {
					values.address = data.address;
					values.postalCode = data.postalCode;
					values.contacts = data.contacts;
					values.phone = data.phone;
				}
				
				oneForm.getForm().setValues(values);
			});
		}	
	};
	
	
	
	
	oneForm.addComp(reseverCb, 1.0, false);
	oneForm.addComp(getText("createrName", "建设实施单位名称", 300), 0.45, false);
	
	oneForm.addComp(getComboBox("year", "申报年份", 300, year), 0.5, false);
	oneForm.addComp(getText("address", "联系地址", 300), 0.45, false);
	oneForm.addComp(getText("postalCode", "邮政编码", 300), 0.5, true);
	oneForm.addComp(getText("contacts", "单位负责人", 300), 0.45, false);
	oneForm.addComp(getText("phone", "联系电话", 300), 0.5, false);
	
	twoForm = new FormUtil({
		iconCls : "",
		title : "详细信息",
		border : false,
		id : "twoForm",
		autoScroll : true,
		collapsible : false,
		labelWidth : 250
	});
	
	twoForm.addComp(getTextArea("target", "建设总体目标及本年度目标", 800, 60, 1000), 1.0, false);
	twoForm.addComp(getTextArea("appReason", "补助申请理由", 800, 60, 1000), 1.0, false);
	twoForm.addComp(getTextArea("content", "补助资金使用内容", 800, 60, 1000), 1.0, false);

	var cm = new Ext.grid.ColumnModel({
		defaults : {
			sortable : true
		},
		columns : [ {
			id : "detail",
			header : "当年中央财政拨款支出明细",
			dataIndex : "detail",
			width : 200,
			editor : new Ext.form.ComboBox({
				listClass: "x-combo-list-small",
				typeAhead: true,
				store : budgetArray,
				triggerAction : "all"
			})
		}, {
			id : "money",
			header : "金额（万元）",
			dataIndex : "money",
			width : 100,
			editor : new Ext.form.TextField({
				vtype : "money",
				vtypeText : Validate.money
			})
		}, {
			id : "remark",
			header : "说明",
			dataIndex : "remark",
			width : 350,
			editor : new Ext.form.TextField({
				maxLength : 100
			})
		} ]
	});

	budgetStore = new Ext.data.Store({
		autoLoad : true,
		url : getBudgetUrl,
		listeners : {
			"load" : function(store, records) {
				var totalMoney = 0;
				for(var i=0; i<records.length; i++) {
					var money = parseFloat(records[i].get("money"));
					
					totalMoney = parseFloat(totalMoney) + money;
				}
				
				Ext.getCmp("budgetTotal").setValue(totalMoney);
			}
		},
		baseParams : {
			pid : processInfo.pid,
			flowType : processInfo.flowKey
		},
		reader : new Ext.data.JsonReader({}, Ext.data.Record.create([ {
			name : "id",
			type : "string"
		}, {
			name : "pid",
			type : "string"
		}, {
			name : "type",
			type : "string"
		}, {
			name : "detail",
			type : "string"
		}, {
			name : "remark",
			type : "string"
		}, {
			name : "money",
			type : "string"
		} ]))
	});

	var budgetGrid = new Ext.grid.EditorGridPanel({
		region:"center",
		store : budgetStore,
		margins : "2 2 2 2",
		cm : cm,
		height : 110,
		title : "项目支出明细预算（单击表格进行修改）",
		loadMask : {
			msg : "数据获取中,请稍候..."
		},
		clicksToEdit : 1,
		listeners : {
			"validateedit" : function(e) {
				if(e.field == "money") {
					var total = Ext.getCmp("budgetTotal").getValue();
					total = parseFloat(total) - parseFloat(e.originalValue) + parseFloat(e.value);
					Ext.getCmp("budgetTotal").setValue(total);
				}
			},
			"beforeedit" : function(e) {
				
				if(e.field == 'detail') {
					return false;
				}
				
				if(processInfo.openType != "create" 
					&& !(processInfo.openType == "todo" 
						&& user.userId == processInfo.creater)
					&& !(processInfo.openType == "done" 
						&& userPermission.MODIFY)) {
					return false;
				}
				
				return true;
			}
		},
		bbar : [ {
			text : "合计（万元）:",
			xtype : "tbtext"
		}, {
			xtype : "textfield",
			id : "budgetTotal",
			width : 70,
			readOnly : true
		} ]
	});
	
	threeForm = new FormUtil({
		region:"west",
		iconCls : "",
		id : "budgetForm",
		border : false,
		autoScroll : true,
		collapsible : false,
		split : true,
		width : 600,
		labelWidth : 185
	});
	threeForm.addComp(getMoneyText("givenMoney", "当年中央财政拨款金额（万元）", 360), 1.0, false);
	threeForm.addComp(getMoneyText("applyMoney", "申请中央财政拨款金额（万元）", 360), 1.0, false);
	threeForm.addComp(getTextArea("budget", "预算测算依据及说明", 360, 120, 1000), 1.0, false);
	
	var budgetForm = new Ext.Panel({
		title : "金额及预算明细",
		border : false,
		collapsible : false,
		id : "threeForm",
		bodyStyle : "padding-top: 7px;",
		margins : "2 0 0 0",
		layout : "border",
		collapsible : false,
		items : [ threeForm.getPanel(), budgetGrid ]
	});
	
	mainTab = new Ext.TabPanel( {
		activeTab : 0,
		margins : "2 0 2 0",
		resizeTabs : true,
		border : false,
		minTabWidth : 120,
		buttonAlign : "center",
		buttons : [{
			id : "lastStep",
			text : "上一步",
			hidden : true,
			minWidth : 200,
			handler : function() {
				var activateId = mainTab.getActiveTab().getItemId();
				
				if(activateId == "twoForm") {
					mainTab.activate("oneForm");
					Ext.getCmp("lastStep").hide();
				} else if(activateId == "threeForm") {
					mainTab.activate("twoForm");
					Ext.getCmp("nextStep").show();
				}
			}
		}, {
			text : "下一步",
			id : "nextStep",
			minWidth : 200,
			handler : function() {
				var activateId = mainTab.getActiveTab().getItemId();
				
				if(activateId == "oneForm") {
					mainTab.activate("twoForm");
					Ext.getCmp("lastStep").show();
				} else if(activateId == "twoForm") {
					mainTab.activate("threeForm");
					Ext.getCmp("nextStep").hide();
				}
			}
		}],
		items : [ oneForm.getPanel(), twoForm.getPanel(), budgetForm]
	});
	
	addContentPanel(mainTab);
	formLoadingComplate();
	
	oneForm.findById("createrName").setDisabled(true);
	
	//非新建时load表单数据
	if(processInfo.openType != "create") {
		var ajax = new AjaxUtil(loadFormUrl);
		ajax.getData({
			id : processInfo.pid
		}, null, function(scope, data) {
			oneForm.getForm().reset();
			oneForm.getForm().setValues(data);
			twoForm.getForm().reset();
			twoForm.getForm().setValues(data);
			threeForm.getForm().reset();
			threeForm.getForm().setValues(data);
			
			oneForm.findById("show_reseverId").setRawValue(data.reseverName);
			
			if(!Ext.isEmpty(data.reseverId)) {
				oneForm.findById("show_reseverId").setDisabled(true);
			}
			
			reLoadAtts(data.attids);
		});
	} else {
		oneForm.findById("createrName").setValue(user.userName);
	}
}

function VerificationForm() {
	
	if(!oneForm.getForm().isValid()
			|| !twoForm.getForm().isValid()
			|| !threeForm.getForm().isValid()) {
		Ext.Msg.alert("提示信息", "请填写完整的费用申报信息，带<em class=\"required\">*</em>为必填项!");
		return false;
	}
	
	return true;
}

function getFormValues() {
	
	var obj = new Object();
	
	var form1 = oneForm.getForm().getValues(false);
	mainTab.activate("twoForm");
	var form2 = twoForm.getForm().getValues(false);
	mainTab.activate("threeForm");
	var form3 = threeForm.getForm().getValues(false);
	
	for ( var attr in form1) {
		obj["rc_" + attr] = form1[attr];
	}
	for ( var attr in form2) {
		obj["rc_" + attr] = form2[attr];
	}
	for ( var attr in form3) {
		obj["rc_" + attr] = form3[attr];
	}
	
	obj.rc_reseverName = oneForm.findById("show_reseverId").getRawValue();
	
	var budgetJson = new Array();
	
	budgetStore.each(function(record) {
		
		var id = record.get("id");
		var money = record.get("money");
		
		if((Ext.isEmpty(id) && !Ext.isEmpty(money) && money > 0)
				|| !Ext.isEmpty(id)) {
			budgetJson.push(record.data); 
		}
	});
	
	if(budgetJson.length > 0) {
		obj.budgetJson = Ext.encode(budgetJson);
	}
	
	return obj;
}

