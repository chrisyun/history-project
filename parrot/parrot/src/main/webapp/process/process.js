var tpanel,contentPanel,bPanel,upload;
var processHandlerUrl = sys.path + "/wf/cm/handlerProcess.jmt";
var processDeleteUrl = sys.path + "/wf/cm/deleteProcess.jmt";
var exportWordUrl = sys.path + "/wf/cm/exportProcessWord.jmt";

Ext.onReady(function() {
	Ext.QuickTips.init();
	
	var thtml = "<table width=\"100%\" height=\"100%\">" +
   					"<tr valign=\"middle\" height=\"60%\">" +
   						"<td align=\"center\" colspan=\"2\" id=\"title\" style=\"font-size: 18px;\"></td>" +
   					"</tr>" +
   					"<tr height=\"20%\">" +
   						"<td style=\"font-size: 13px;color: red;\" width=\"70%\">&nbsp;&nbsp;&nbsp;&nbsp;注：带<em class=\"required\">*</em>为必填项</td>" +
   						"<td id=\"activityName\" style=\"font-size: 13px;\"></td>" +
   					"</tr>" +
   				"</table>";
	
	
	var loadingUrl = sys.path + "/resources/images/local/waiting.gif";
	var html = "<table width=\"100%\" height=\"100%\" id='pageLoading'>" +
				"<tr valign=\"middle\" height=\"100%\">" +
					"<td width=\"100%\" align=\"center\">" +
						"<font style=\"font-size: 14px;\">表单数据加载中...</font><br>" +
						"<img src='" + loadingUrl + "'>" +
					"</td>" + 
				"</tr></table>";
	
	//设置按钮：保存草稿、导出word、作废流程
	var barArray = new Array();
	
	if(processInfo.openType == "done" 
		&& userPermission.MODIFY) {
		
		barArray.push("-");
		barArray.push({
			text : "修改数据",
			handler : function() {
				var processmModifyUrl = sys.path + "/wf/modifyProcess.jmt";
				
				//表单验证
				try {
					if(! VerificationForm()) {
						return ;
					}
				} catch(e) {}
				
				var params = {
						flowId : processInfo.flowInstId,
						flowType : processInfo.flowKey,
						creater : processInfo.creater,
						curActivity : processInfo.activityName,
						pid : processInfo.pid,
						flowAtts : upload.getUploadAttIds()
					};
					
					var form = getFormValues();
					
					for ( var attr in form) {
						params[attr] = form[attr];
					}
					
					var ajax = new AjaxUtil(processmModifyUrl);
					ajax.request(params, true, null, function(){
						Ext.Msg.show( {
							buttons : Ext.Msg.OK,
							maxWidth : 200,
							minWidth : 300,
							msg : "<div style='margin: 5 5 8 15px;font-size: 15px;'>流程数据修改成功！</div>",
							title : "提示信息",
							icon : Ext.Msg.WARNING,
							fn : function(btn) {
								closeThisWindow();
							}
						} );
					});
			}
		});
	}
	
	if((processInfo.openType == "todo"
		&& user.userId == processInfo.creater)
		|| ((processInfo.openType == "done" 
			|| processInfo.openType == "doing")
			&& userPermission.MODIFY)) {
		barArray.push("-");
		barArray.push({
			text : "删除流程",
			handler : function() {
				
				var params = {
					id : processInfo.flowInstId,
					flowType : processInfo.flowKey,
					pid : processInfo.pid
				};
				
				if(Ext.isEmpty(params.id) 
						|| Ext.isEmpty(params.flowType) 
						|| Ext.isEmpty(params.pid)) {
					Ext.Msg.alert("提示信息", "参数有误，无法删除!");
					return ;
				}
				
				Ext.Msg.confirm("确认提示", "确定删除这条申请流程?", function(btn) {
					if (btn == "yes") {
						var ajaxClass = new AjaxUtil(processDeleteUrl);
						ajaxClass.request(params, true, null, function(obj) {
							Ext.Msg.show( {
								buttons : Ext.Msg.OK,
								maxWidth : 200,
								minWidth : 300,
								msg : "<div style='margin: 5 5 8 15px;font-size: 15px;'>流程删除成功！</div>",
								title : "提示信息",
								icon : Ext.Msg.WARNING,
								fn : function(btn) {
									closeThisWindow();
								}
							} );
						});
					}
				});
			}
		});
	}
	barArray.push("-");
	
	tpanel = new Ext.Panel({
		height : 75,
		split : true,
		margins : "0 0 0 2",
		region : "north",
		tbar : barArray,
		html : thtml
	});
	
	contentPanel = new Ext.Panel({
		split : true,
		layout : "fit",
		height : 500,
		margins : "0 0 0 2",
		region : "center",
		html : html
	});
	
	//附件上传
	var attViewType = "read";
	if(processInfo.openType == "create" 
		|| (processInfo.openType == "todo" 
			&& user.userId == processInfo.creater)
		|| (processInfo.openType == "done" 
			&& userPermission.MODIFY)) {
		attViewType = "edit";
	}
	upload = new UploadUtil("flowAtt", attViewType);
	
	var attArray = new Array();
	if(attViewType == "edit") {
		attArray.push("-");
		attArray.push({
			text : "附件上传",
			handler : function() {
				upload.show("上传附件");
			}
		});
		attArray.push("-");
	}
	
	var attPanel = upload.initShowPanel({
		title : "附件信息",
		split : true,
		region : "center",
		collapsible : true,
		height : 90,
		autoScroll : true,
		autoHeight : false,
		autoWidth : false
	}, "");
	
	if(attArray.length > 0) {
		attPanel.add(new Ext.Toolbar(attArray));
	}
	
	
	var btn = new Array();
	if(processInfo.openType == "todo"
		|| processInfo.openType == "create") {
		for ( var i = 0; i < nextStep.length; i++) {
			btn.push(getNextActivityBtn(nextStep[i], submitProcessInfo));
		}
	}
	
	if(processInfo.openType == "create" 
		|| (user.userId == processInfo.creater && processInfo.openType == "todo")) {
		btn.push({
			text : "保存草稿",
			iconCls : "silk-tick",
			handler : function() {
				handlerProcess("保存草稿");
			}
		});
	}
	if(processInfo.openType == "done" 
		|| (processInfo.openType == "todo" && user.userId != processInfo.creater)) {
		btn.push({
			text : "导出WORD",
			handler : function() {
				var wordUrl = exportWordUrl + "?pid=" + processInfo.pid
				+ "&flowType=" + processInfo.flowKey + "&num=" + Math.random();
		
				document.getElementById("wordFrame").src = wordUrl;
			}
		});
	}
	
	
	btn.push({
		text : "关闭",
		iconCls : "slik-close",
		handler : function() {
			closeThisWindow();
		}
	});
	
	//流程意见
	var processMinds = new ProcessMinds(processInfo.flowInstId,
					processInfo.activityName, processInfo.openType);
	
	bPanel = new Ext.Panel({
		region : "south",
		split : true,
		height : 230,
		margins : "0 0 0 2",
		layout : "border",
		border : false,
		items : [attPanel, processMinds.getMindPanel()],
		buttonAlign : "center",
		buttons : btn
	});
		
	var viewport = new Ext.Viewport({
		border : false,
		layout : "border",
		items : [tpanel, contentPanel, bPanel]
	});
	
	//设置title
	setTitle(processInfo.title);
	Ext.getDom("activityName").innerHTML = "当前环节：" + processInfo.activityName;
	completePage();
	
	try {
		startProcessHandler();
	} catch(e) {
		alert(e);
	}
	
	// 非新建和待办打开，将所有的控件置为disabled
	if(processInfo.openType != "create" 
		&& !(processInfo.openType == "todo" 
			&& user.userId == processInfo.creater)
		&& !(processInfo.openType == "done" 
			&& userPermission.MODIFY)) {
		
		var fieldArray = contentPanel.findByType("textfield");
		for(var i=0; i < fieldArray.length; i++) {
			fieldArray[i].disable();
		}
		fieldArray = contentPanel.findByType("textarea");
		for(var i=0; i < fieldArray.length; i++) {
			fieldArray[i].enable();
			fieldArray[i].on("change", function(textArea, newValue, oldValue){
				textArea.setValue(oldValue);
				Ext.Msg.alert("提示信息", "该输入项不允许修改!");
			});
			
			
		}
	}
	
	//提交流程处理
	function submitProcessInfo(nextActivity) {
		//表单验证
		if(!(processInfo.openType == "todo" 
			&& user.userId != processInfo.creater)) {
			try {
				if(! VerificationForm()) {
					return ;
				}
			} catch(e) {}
		}
		
		handlerProcess(nextActivity);
	}
	
	function handlerProcess(nextActivity) {
		var minds = processMinds.getMind();
		
		var params = {
			mindsId : minds.id,
			minds : minds.mind,
			flowId : processInfo.flowInstId,
			flowType : processInfo.flowKey,
			nextStep : nextActivity,
			creater : processInfo.creater,
			curActivity : processInfo.activityName,
			pid : processInfo.pid,
			flowAtts : upload.getUploadAttIds()
		};
		
		var form = getFormValues();
		
		for ( var attr in form) {
			params[attr] = form[attr];
		}
		
		var ajax = new AjaxUtil(processHandlerUrl);
		ajax.request(params, true, null, function(){
			Ext.Msg.show( {
				buttons : Ext.Msg.OK,
				maxWidth : 200,
				minWidth : 300,
				msg : "<div style='margin: 5 5 8 15px;font-size: 15px;'>流程数据处理成功！</div>",
				title : "提示信息",
				icon : Ext.Msg.WARNING,
				fn : function(btn) {
					closeThisWindow();
				}
			} );
		});
	}
	
	
});


function setTitle(processTitle) {
	Ext.getDom("title").innerHTML = processTitle;
	processInfo.title = processTitle;
}

function formLoadingComplate() {
	Ext.getDom("pageLoading").style.display = "none";
}

function addContentPanel(panel) {
	contentPanel.add(panel);
	contentPanel.doLayout();
}

function reLoadAtts(ids) {
	upload.loadAtt(ids);
}

function closeThisWindow() {
	try {
		window.opener.refreshGrid();
	} catch(e) {}
	window.close();
}

