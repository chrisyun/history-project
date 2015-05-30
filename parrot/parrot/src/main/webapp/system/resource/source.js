var sourcePageUrl = sys.path + "/resource/getSourcePage.jmt";
var sourceSubmitUrl = sys.path + "/resource/saveOrModifySource.jmt";
var sourceDeleteUrl = sys.path + "/resource/deleteSource.jmt";
var sourceTreeUrl = sys.path + "/resource/getLeftTree.jmt";

var pageSize = 20;
var currentActiveNode;

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

	query_form.addComp(getText("query_code", "资源编码", 120), 0.23, true);
	query_form.addComp(getText("query_name", "资源名称", 120), 0.23, true);
	query_form.addComp(getQueryBtn(onSubmitQueryHandler), 0.3, true);
	/** ****************query panel end*************** */

	/** ****************Grid panel start************ */
	var recordArray = [ getRecord(null, "sortNum", "string"),
			getRecord(null, "parentSource", "string"),
			getRecord("资源编码", "sourceCode", "string", 100, true),
			getRecord("资源名称", "sourceName", "string", 150, false),
			getRecord("是否叶子节点", "isleaf", "string", 70, true, yesOrNoRender),
			getRecord("图标样式", "iconsCls", "string", 70, false, iconClsRender),
			getRecord("是否显示", "isShow", "string", 70, true, yesOrNoRender),
			getRecord("菜单访问入口", "sourceUrl", "string", 200, false) ];
	var grid_rs = new GridUtil(sourcePageUrl, recordArray, pageSize);

	var grid_Bar = getCommonBar(null, modifyHandler, deleteHandler, userPermission);
	grid_rs.setBottomBar(grid_Bar);

	var listeners = {
		celldblclick : function(thisGrid, rowIndex, columnIndex, ev) {
			if(userPermission.MODIFY == "true") {
				modifyHandler();
			}
		}
	};
	grid_rs.setListeners(listeners);

	var grid_attr = {
		title : "资源列表",
		region : "center"
	};
	grid_rs.setGridPanel(grid_attr);
	/** ****************Grid panel end************ */

	/** ****************tree panel start************ */
	var loader = new Ext.tree.TreeLoader( {
		url : sourceTreeUrl
	});
	loader.on("beforeload", function(loader, node) {
		loader.baseParams.pid = node.id;
		loader.baseParams.isSourcePanel = "YES";
	});

	var tree = new Ext.tree.TreePanel( {
		title : "资源面板(右键添加)",
		region : 'west',
		autoScroll : true,
		collapsible : true,
		margins : "2 0 0 0",
		width : 230,
		border : true,
		useArrows : true,
		rootVisible : true,
		split : true,
		loader : loader,
		root : new Ext.tree.AsyncTreeNode( {
			text : "资源树",
			id : "0",
			leaf : false
		})
	});

	tree.on("click", function(node) {
		currentActiveNode = node;
		store.baseParams = {
			"pid" : node.id
		};
		store.reload( {
			params : {
				start : 0,
				limit : pageSize
			}
		});
	});
	if(userPermission.SAVE == "true") {
		var menu = new Ext.menu.Menu( {
			items : [ {
				text : "增加子资源",
				iconCls : "silk-add",
				handler : saveHandler
			} ]
		});
		tree.on("contextmenu", function(node, ev) {
			ev.preventDefault();
			node.select();
			currentActiveNode = node;
			menu.showAt(ev.getXY());
		});
	}
	tree.getRootNode().expand(false, false);
	/** ****************tree panel start************ */

	var grid = grid_rs.getGrid();
	var store = grid_rs.getStore();

	/** ****************org window start************ */
	var source_form = new FormUtil( {
		id : "sourceForm",
		collapsible : false,
		labelWidth : 100,
		border : false
	});
	source_form.addComp(getText("sourceCode", "资源编码", 150), 0.5, false);
	source_form.addComp(getText("sourceName", "资源名称", 150), 0.5, false);
	source_form.addComp(getText("sourceUrl", "菜单访问入口", 150), 0.5, true);
	source_form.addComp(getComboBox("iconsCls", "菜单图标", 150, iconCls, false),
			0.5, true);
	source_form.addComp(getComboBox("isShow", "是否显示", 150, yesOrNo, false),
			0.5, false);
	source_form.addComp(getComboBox("isleaf", "是否叶子节点", 150, yesOrNo, false),
			0.5, false);
	source_form.addComp(getNumberText("sortNum", "排序号", 150), 0.5, true);
	source_form.addComp(getHidden("opType"), 0, true);
	source_form.addComp(getHidden("parentSource"), 0, true);

	var source_win = new WindowUtil( {
		width : 560,
		height : 220
	}, source_form.getPanel(), saveOrModify);

	/** ****************org window start************ */

	function saveHandler() {
		var pid = currentActiveNode.id;

		source_win.show("新增资源");

		source_form.getForm().reset();
		source_form.findById("opType").setValue(Configuration.opType.SAVE);
		source_form.findById("parentSource").setValue(pid);

		setTextEditable(source_form.findById("sourceCode"));
	}

	function modifyHandler() {
		if (grid.getSelectionModel().getCount() != 1) {
			Ext.Msg.alert("提示信息", "请选择一条记录!");
			return;
		}

		source_win.show("修改资源");
		source_form.getForm().reset();
		// 将主键置为不可编辑
		setTextReadOnly(source_form.findById("sourceCode"));

		var selectedRecord = grid.getSelectionModel().getSelected();
		var values = {
			sourceCode : selectedRecord.get("sourceCode"),
			sourceName : selectedRecord.get("sourceName"),
			isleaf : selectedRecord.get("isleaf"),
			iconsCls : selectedRecord.get("iconsCls"),
			sourceUrl : selectedRecord.get("sourceUrl"),
			parentSource : selectedRecord.get("parentSource"),
			isShow : selectedRecord.get("isShow"),
			sortNum : selectedRecord.get("sortNum"),
			opType : Configuration.opType.MODIFY
		};
		source_form.getForm().setValues(values);
	}

	function saveOrModify() {
		var form = source_form.getForm();

		if (!form.isValid()) {
			Ext.Msg.alert("提示信息", "请填写完整的资源信息!");
			return;
		}

		var isLeaf = source_form.findById("show_isleaf").getValue();
		var url = source_form.findById("sourceUrl").getValue();
		if (Ext.isEmpty(url) && isLeaf == Configuration.yOrN.YES) {
			Ext.Msg.alert("提示信息", "叶子节点必须填写菜单访问入口地址!");
			return;
		}

		var ajaxClass = new AjaxUtil(sourceSubmitUrl);

		var callBack_obj = new Object();
		callBack_obj.grid = grid;
		callBack_obj.win = source_win;
		callBack_obj.form = source_form;

		ajaxClass.submit(form, null, true, callBack_obj, function(obj) {
			obj.grid.getStore().reload();
			var thisForm = obj.form;
			var opType = thisForm.findById("opType").getValue();
			var pid = thisForm.findById("parentSource").getValue();

			if (currentActiveNode != null
					&& currentActiveNode.parentNode != null) {
				try {
					var refreshNode = currentActiveNode.parentNode;
					tree.getLoader().load(refreshNode);
					refreshNode.expand();
				} catch (e) {
				}
			} else {
				tree.getLoader().load(tree.getRootNode());
				tree.getRootNode().expand();
			}

			if (opType == Configuration.opType.SAVE) {
				obj.form.getForm().reset();
				thisForm.findById("opType").setValue(opType);
				thisForm.findById("parentSource").setValue(pid);
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
		var selectedRecordArray = grid.getSelectionModel().getSelections();

		Ext.Msg.confirm("确认提示", "确定删除选定的记录?", function(btn) {
			if (btn == "yes") {
				var ids = "";
				for ( var i = 0; i < selectedRecordArray.length; i++) {
					ids += selectedRecordArray[i].get("sourceCode") + ",";
				}

				var params = {
					ids : ids
				};

				var ajaxClass = new AjaxUtil(sourceDeleteUrl);
				ajaxClass.request(params, true, null, function(obj) {
					grid.getStore().reload();
					try {
						tree.getLoader().load(currentActiveNode);
						currentActiveNode.expand();
					} catch (e) {
					}
				});
			}
		});
	}

	function onSubmitQueryHandler() {
		var thisForm = query_form.getForm();
		var store = grid.getStore();

		var name = Ext.getCmp("query_name").getValue();
		var code = Ext.getCmp("query_code").getValue();

		store.baseParams.sourceCode = code;
		store.baseParams.sourceName = name;
		store.baseParams.pid = "";
		
		store.load( {
			params : {
				start : 0,
				limit : grid_rs.pageSize
			}
		});
	}

	var viewport = new Ext.Viewport( {
		border : false,
		layout : "border",
		items : [ tree, {
			border : false,
			region : "center",
			layout : "border",
			split : true,
			items : [ query_form.getPanel(), grid_rs.getGrid() ]
		} ]
	});

	grid.getStore().reload( {
		params : {
			pid : "0",
			start : 0,
			limit : grid_rs.pageSize
		}
	});
	completePage();
});
