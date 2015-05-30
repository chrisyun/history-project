var heritorPageUrl = sys.path + "/heritor/getHeritorPage.jmt";
var heritorSubmitUrl = sys.path + "/heritor/saveOrModifyHeritor.jmt";
var heritorDeleteUrl = sys.path + "/heritor/deleteHeritor.jmt";

var getProjectByProvince = sys.path + "/project/getProjectByProvince.jmt";

var pageSize = 20;

Ext.onReady(function() {
	Ext.QuickTips.init();

	/** ****************query panel start*************** */
	var query_attr = {
		title : "查询列表",
		region : "north",
		height : 100,
		labelWidth : 70
	};

	var query_form = new FormUtil(query_attr);

	query_form.addComp(getText("query_name", "传承人姓名",160), 0.23, true);
	query_form.addComp(getComboBox("query_area", "所属省份", 160, area, false), 0.23, true);
	query_form.addComp(getText("query_pName", "项目名称",160), 0.23, true);
	query_form.addComp(getComboBox("query_isDie", "是否去世", 160, yesOrNo,false), 0.23, true);
	query_form.addComp(getComboBox("query_gender", "性别", 160, gender,false), 0.23, true);
	query_form.addComp(getComboBox("query_minority", "民族", 160, minority,false), 0.23, true);
	query_form.addComp(getText("query_batchNum", "批次号",160), 0.23, true);
	query_form.addComp(getQueryBtn(onSubmitQueryHandler), 0.23, true);
	/** ****************query panel end*************** */

	/** ****************heritor Grid panel start************ */

	var recordArray = [
			getRecord(null, "id", "string"),
			getRecord(null, "projectId", "string"),
			getRecord(null, "remark", "string"),
			getRecord(null, "birthDate", "string"),
			getRecord(null, "minority", "string"),
			getRecord("省份", "province", "string", 80, true, areaRender),
			getRecord("项目编码", "projectCode", "string", 80, true),
			getRecord("项目名称", "projectName", "string", 200, true),
			getRecord("传承人", "name", "string", 100, true),
			getRecord("性别", "gender", "string", 50, true, genderRender),
			getRecord("身份证号码", "idCard", "string", 200, false),
			getRecord("批次", "batchNum", "string", 80, true),
			getRecord("是否去世", "isDie", "string", 80, true, yesOrNoRender),
			getRecord("去世时间", "dieDate", "string", 120, true)];
	var heritor_grid = new GridUtil(heritorPageUrl, recordArray, pageSize);
	
	var grid_Bar = getCommonBar(saveHandler, modifyHandler, deleteHandler, userPermission);
	heritor_grid.setBottomBar(grid_Bar);

	var listeners = {
		celldblclick : function(thisGrid, rowIndex, columnIndex, ev) {
			if(userPermission.MODIFY == "true") {
				modifyHandler();
			}
		}
	};
	heritor_grid.setListeners(listeners);

	var grid_attr = {
		title : "传承人列表",
		region : "center"
	};
	heritor_grid.setGridPanel(grid_attr);
	/** ****************heritor Grid panel end************ */

	var grid = heritor_grid.getGrid();
	var store = heritor_grid.getStore();

	/** ****************heritor window start************ */
	var heritor_form = new FormUtil({
		id : "heritorForm",
		collapsible : false,
		labelWidth : 90,
		border : false
	});
	
	
	var projectStore = new Ext.data.Store({
		url : getProjectByProvince,
		listeners : {
			"beforeload" : function(thisStore) {
				var province = heritor_form.findById("show_province").getValue();
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
	
	heritor_form.addComp(getText("name", "传承人", 180), 0.5, false);
	heritor_form.addComp(getComboBox("gender", "性别", 180, gender),0.5, true);
	heritor_form.addComp(getText("idCard", "身份证号码", 180), 0.5, false);
	heritor_form.addComp(getComboBox("minority", "民族", 180, minority), 0.5, false);
	
	var provinceCb = getComboBox("province", "所属省份", 180, area);
	provinceCb.listeners = {
		"change" : function(field, newValue, oldValue) {
			heritor_form.findById("show_projectId").setValue();
			projectStore.load();
		}
	};
	heritor_form.addComp(provinceCb, 0.5, false);
	
	heritor_form.addComp(getText("batchNum", "批次", 180), 0.5, false);
	var projectCb = getComboBox("projectId", "传承人项目", 340, null);
	projectCb.valueField = "id";
	projectCb.displayField = "name";
	projectCb.lazyInit = true;
	projectCb.mode = "remote";
	projectCb.store = projectStore;
	heritor_form.addComp(projectCb, 0.7, true);
	heritor_form.addComp(getButton({
		text : "查询项目信息",
		handler : function() {
			var pid = heritor_form.findById("show_projectId").getValue();
			
			if(Ext.isEmpty(pid)) {
				return ;
			}
			var url = sys.path + "/app/project.jsp?pid=" + pid; 
			
			parent.window.setActivate(url, {
				id : "PROJECTINFO",
				text : "项目信息管理",
				attributes : {
					iconCls : ""
				}
			});
		}
	}), 0.3, true);
	
	heritor_form.addComp(getDateText("birthDate", "出生日期", 180, null), 0.5, true);
	heritor_form.addComp(getComboBox("isDie", "是否去世", 180, yesOrNo), 0.5, false);
	heritor_form.addComp(getDateText("dieDate", "去世时间", 180, null), 0.5, true);
	
	heritor_form.addComp(getTextArea("remark", "备注", 500, 60), 1.0, true);

	heritor_form.addComp(getHidden("opType"), 0, true);
	heritor_form.addComp(getHidden("id"), 0, true);
	
	var heritor_win = new WindowUtil({
		width : 650,
		height : 320
	}, heritor_form.getPanel(), saveOrModify);

	/** ****************heritor window end************ */

	function saveHandler() {
		heritor_win.show("新增传承人");

		heritor_form.getForm().reset();
		heritor_form.findById("opType").setValue(
				Configuration.opType.SAVE);
		
		heritor_form.findById("show_province").setValue(user.org);
	}
	
	function modifyHandler() {
		if (grid.getSelectionModel().getCount() != 1) {
			Ext.Msg.alert("提示信息", "请选择一条记录!");
			return;
		}

		heritor_win.show("修改传承人信息");

		heritor_form.getForm().reset();

		var selectedRecord = grid.getSelectionModel().getSelected();
		var values = {
			id : selectedRecord.get("id"),
			name : selectedRecord.get("name"),
			idCard : selectedRecord.get("idCard"),
			gender : selectedRecord.get("gender"),
			batchNum : selectedRecord.get("batchNum"),
			remark : selectedRecord.get("remark"),
			isDie : selectedRecord.get("isDie"),
			dieDate : selectedRecord.get("dieDate"),
			projectId : selectedRecord.get("projectId"),
			province : selectedRecord.get("province"),
			birthDate : selectedRecord.get("birthDate"),
			minority : selectedRecord.get("minority"),
			opType : Configuration.opType.MODIFY
		};
		heritor_form.getForm().setValues(values);
		heritor_form.findById("show_projectId").setRawValue(
				selectedRecord.get("projectName"));
	}
	
	function saveOrModify() {
		var form = heritor_form.getForm();

		if (!form.isValid()) {
			Ext.Msg.alert("提示信息", "请填写完整的传承人信息!");
			return;
		}

		var ajaxClass = new AjaxUtil(heritorSubmitUrl);

		var callBack_obj = new Object();
		callBack_obj.grid = grid;
		callBack_obj.win = heritor_win;
		callBack_obj.form = heritor_form;

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

				var ajaxClass = new AjaxUtil(heritorDeleteUrl);
				ajaxClass.request(params, true, null, function(obj) {
					grid.getStore().reload();
				});
			}
		});
	}
	
	function onSubmitQueryHandler() {

		var isDie = Ext.getCmp("show_query_isDie").getValue();
		var name = Ext.getCmp("query_name").getValue();
		var area = Ext.getCmp("show_query_area").getValue();
		var pName = Ext.getCmp("query_pName").getValue();
		var batchNum = Ext.getCmp("query_batchNum").getValue();
		
		var gender = Ext.getCmp("show_query_gender").getValue();
		var minority = Ext.getCmp("show_query_minority").getValue();
		
		store.baseParams.name = name;
		store.baseParams.batchNum = batchNum;
		store.baseParams.projectName = pName;
		store.baseParams.isDie = isDie;
		store.baseParams.province = area;
		store.baseParams.gender = gender;
		store.baseParams.minority = minority;
		
		store.load({
					params : {
						start : 0,
						limit : heritor_grid.pageSize
					}
				});
	}

	var viewport = new Ext.Viewport({
				border : false,
				layout : "border",
				items : [query_form.getPanel(), heritor_grid.getGrid()]
			});

	onSubmitQueryHandler();
	completePage();
});
