var attPageUrl = sys.path + "/att/getAttsPage.jmt";

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
			.add(Date.MONTH, -1)), 0.18, true);
	query_form.addComp(getDateText("query_endTime", "结束日期", 120), 0.18, true);
	query_form.addComp(getText("query_user", "上传用户", 120), 0.2, true);
	query_form.addComp(getText("query_type", "文件类型", 120), 0.2, true);
	query_form.addComp(getQueryBtn(onSubmitQueryHandler), 0.2, true);
	/** ****************query panel end*************** */

	/** ****************log Grid panel start************ */
	var store = new Ext.data.Store({
		url : attPageUrl,
		remoteSort : true,
		reader : new Ext.data.JsonReader({
			totalProperty : "total",
			root : "reslutSet"
		}, Ext.data.Record.create([ {
			name : "id",
			type : "string",
			mapping : "id"
		}, {
			name : "fileName",
			type : "string",
			mapping : "fileName"
		}, {
			name : "fileType",
			type : "string",
			mapping : "fileType"
		}, {
			name : "uploadTime",
			type : "string",
			mapping : "uploadTime"
		}, {
			name : "uploader",
			type : "string",
			mapping : "uploader"
		} ]))
	});

	var dataview = new Ext.DataView({
		store : store,
		loadingText : "数据加载中...",
		emptyText : "没有可查看的文件",
		tpl : new Ext.XTemplate('<ul>', '<tpl for=".">', '<li class="att">',
				'<img width="64" height="64" src="' + sys.path
						+ '/resources/images/fileType/{fileType}.ico" />',
				'<strong>{fileName}</strong>',
				'<span>{uploader} - {uploadTime}</span>', '</li>', '</tpl>',
				'</ul>'),
		id : "attManager",
		itemSelector : "li.att",
		overClass : "over-hover",
		multiSelect : true,
		autoScroll : true,
		listeners : {
			"dblclick" : function(view, index, node) {
				if (userPermission.DOWNLOAD != "true") {
					return;
				}

				var record = view.getRecord(node);
				var id = record.data.id;

				var att = new Object();
				att.id = id;
				var downUrl = getDownloadUrl(att);
				window.open(downUrl);
			}

		}
	});

	var deleteBtn = null;
	if (userPermission.DELETE == "true") {
		deleteBtn = [ "-", {
			text : "删除",
			iconCls : "silk-delete",
			handler : deleteHandler
		} ];
	}

	var pageBar = new Ext.PagingToolbar({
		store : store,
		pageSize : pageSize,
		items : deleteBtn,
		displayInfo : true,
		displayMsg : "当前显示{0}-{1}条,共{2}条",
		emptyMsg : "没有找到相关记录",
		emptyMsg : "没有找到相关记录",
		firstText : "第一页",
		prevText : "上一页",
		nextText : "下一页",
		lastText : "最后页",
		refreshText : "刷新",
		afterPageText : "页,共{0}页",
		beforePageText : "当前第"
	});

	function deleteHandler() {
		var count = dataview.getSelectionCount();
		if (count == 0) {
			Ext.Msg.alert("提示信息", "请选择一条记录!");
			return;
		}

		var selectedRecordArray = dataview.getSelectedRecords();
		Ext.Msg.confirm("确认提示", "确定删除选定的记录?", function(btn) {
			if (btn == "yes") {
				var ids = "";
				for ( var i = 0; i < selectedRecordArray.length; i++) {
					ids += selectedRecordArray[i].get("id") + ",";
				}

				var params = {
					ids : ids
				};

				var ajaxClass = new AjaxUtil(removeAttsUrl);
				ajaxClass.request(params, true, null, function(obj) {
					store.reload();
				});
			}
		});
	}

	var attPanel = new Ext.Panel({
		title : "文件视图",
		layout : "fit",
		region : "center",
		collapsible : true,
		iconCls : "silk-grid",
		items : dataview,
		split : true,
		bbar : pageBar
	});

	function onSubmitQueryHandler() {

		var startTime = Ext.getCmp("query_startTime").getValue();
		var endTime = Ext.getCmp("query_endTime").getValue();
		var uploader = Ext.getCmp("query_user").getValue();
		var fileType = Ext.getCmp("query_type").getValue();

		store.baseParams.fileType = fileType;
		store.baseParams.uploader = uploader;
		
		if(!Ext.isEmpty(startTime)) {
			startTime = startTime.format("Y-m-d");
		}
		store.baseParams.startTime = startTime;
		if(!Ext.isEmpty(endTime)) {
			endTime = endTime.format("Y-m-d");
		}
		store.baseParams.endTime = endTime;
		
		store.load({
			params : {
				start : 0,
				limit : pageSize
			}
		});
	}

	var viewport = new Ext.Viewport({
		border : false,
		layout : "border",
		items : [ query_form.getPanel(), attPanel ]
	});

	onSubmitQueryHandler();
	completePage();
});
