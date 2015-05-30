var FinProject = {
	queryProjectUrl : sys.basePath + 'projectAction!queryProject.do',
	addFinUrl : sys.basePath + 'projectFinAction!addFinProject.do',
	updateFinUrl : sys.basePath + 'projectFinAction!updateFinProject.do',
	updateUrl : sys.basePath + 'projectFinAction!update.do',
	queryFinUrl : sys.basePath + 'projectFinAction!queryFinProject.do',
	fieldAnchor : '75%',
	/**
	 * 结项时查询项目信息
	 */
	queryFinProjectBypid : function(pid, fpid) {
		Common.showProcessMsgBox('项目信息获取中, 请稍候...');
		var idPrefix = "project.";
		var medPrefix = "finproject.";
		Ext.Ajax
				.request( {
					url : FinProject.queryProjectUrl,
					method : 'post',
					params : {
						pid : pid
					},
					success : function(response, options) {
						Ext.MessageBox.hide();
						var projectJson = Ext.util.JSON
								.decode(response.responseText);
						if (projectJson) {
							var jsonValues = {};
							jsonValues[idPrefix + 'pid'] = projectJson.pid;
							jsonValues[idPrefix + 'pstatus'] = projectJson.pstatus;
							jsonValues[idPrefix + 'spstatus'] = projectJson.spstatus;
							jsonValues[idPrefix + 'creater'] = projectJson.creater;
							jsonValues[idPrefix + 'proprovincecode'] = projectJson.proprovincecode;
							jsonValues[idPrefix + 'curActivityName'] = projectJson.curActivityName;
							jsonValues['spstatusShow'] = LocalRenderer
									.flowStatus(projectJson.spstatus);
							jsonValues['pstatusShow'] = LocalRenderer
									.projectStatus(projectJson.pstatus);
							jsonValues['curActivityNameShow'] = LocalRenderer
									.activityName(projectJson.curActivityName);

							if (Ext.isEmpty(fpid)) {
								jsonValues[medPrefix + 'pid'] = projectJson.pid;
								jsonValues[medPrefix + 'pname'] = projectJson.pname;
								jsonValues[medPrefix + 'pmanager'] = projectJson.pmanager;
								jsonValues[medPrefix + 'tel'] = projectJson.tel;
								jsonValues[medPrefix + 'cdorg'] = projectJson.cdorg;
								jsonValues[medPrefix + 'sborg'] = projectJson.sborg;
								jsonValues[medPrefix + 'sbdate'] = projectJson.sbdate;
							}

							submitPrjFormPanel.getForm().setValues(jsonValues);

							if (!Ext.isEmpty(fpid)) {
								// 查询中期检查表
								FinProject.queryFinProject(fpid);
							} else {
								fileMan.editMode = 'edit';
								initPageShow();
							}

						}
					}
				});
	},
	update : function() {
		var thisForm = submitPrjFormPanel.getForm();
		var submitUrl = FinProject.updateUrl;
		if (!thisForm.isValid()) {
			Ext.Msg.alert("提示信息", "请填写完整的项目信息.");
			return;
		}
		Common.showProcessMsgBox("项目信息提交中, 请稍候...");
		thisForm.submit( {
			url : submitUrl,
			timeout : 120000,
			success : function(form, action) {
				Common.hideProcessMsgBox();
				Ext.topShow.msg('成功提示', action.result.msg);
			},
			failure : function(form, action) {
				Common.hideProcessMsgBox();
				var failMsg = Ext.isEmpty(action.result) ? "信息保存失败."
						: action.result.msg;
				Ext.Msg.show( {
					title : '失败提示',
					msg : failMsg || '项目信息保存失败.',
					width : 180,
					modal : false,
					buttons : Ext.Msg.OK,
					icon : Ext.MessageBox.ERROR
				});
			}
		});
	},
	queryFinProject : function(fpid) {
		Common.showProcessMsgBox('项目结项信息获取中, 请稍候...');
		var medPrefix = "finproject.";
		Ext.Ajax.request( {
			url : FinProject.queryFinUrl,
			method : 'post',
			params : {
				fpid : fpid
			},
			success : function(response, options) {
				Ext.MessageBox.hide();
				var projectJson = Ext.util.JSON.decode(response.responseText);
				if (projectJson) {
					var jsonValues = {};
					jsonValues[medPrefix + 'pid'] = projectJson.pid;
					jsonValues[medPrefix + 'fpid'] = projectJson.fpid;
					jsonValues[medPrefix + 'pname'] = projectJson.pname;
					jsonValues[medPrefix + 'pmanager'] = projectJson.pmanager;
					jsonValues[medPrefix + 'tel'] = projectJson.tel;
					jsonValues[medPrefix + 'cdorg'] = projectJson.cdorg;
					jsonValues[medPrefix + 'sborg'] = projectJson.sborg;
					jsonValues[medPrefix + 'sbdate'] = projectJson.sbdate;
					jsonValues[medPrefix + 'issp'] = projectJson.issp;
					submitPrjFormPanel.getForm().setValues(jsonValues);

					if (projectJson.issp == "YES" && openType == "pendingJX") {
						fileMan.editMode = 'edit';
					} else {
						fileMan.editMode = 'view';
					}

					fileMan.search();
					initPageShow();
				}
			}
		});
	},
	gotoPending : function() {
		parent.window.location = sys.basePath + 'business/project/projectPending.jsp';
	},
	submitFinProject : function(pid, isOk) {
		var submitUrl;
		var thisForm = submitPrjFormPanel.getForm();
		if (!thisForm.isValid()) {
			Ext.Msg.alert("提示信息", "请填写完整的项目信息.");
			return;
		}

		if (!Ext.isEmpty(pid)) {
			// 更新
			submitUrl = FinProject.updateFinUrl;
		} else {
			// 新增
			submitUrl = FinProject.addFinUrl;
		}

		// 项目中期检查与结项文化部科技司审批通过时更改项目状态
		var curActivity = Ext.getCmp('curActivityName').getValue();
		var pstatus = Ext.getCmp('pstatus').getValue();
		if (curActivity == 'ZB_PROCESS' && pstatus == 'ZQJCZ' && isOk == 'SUCCESS') {
			Ext.getCmp('pstatus').setValue('ZQJC');
		} else if (curActivity == 'ZB_PROCESS' && pstatus == 'XMJXZ'
				&& isOk == 'SUCCESS') {
			Ext.getCmp('pstatus').setValue('XMJX');
		}
		
		Common.showProcessMsgBox("项目信息提交中, 请稍候...");

		var attach = '';
		// 新建环节需要传附件ID，其他环节则不需要
		if (FileConstant.uploadForm != null) {
			attach = FileConstant.uploadForm.getForm().findField('attach').getValue();
		}

		thisForm.submit( {
			url : submitUrl,
			timeout : 120000,
			params : {
				attach : attach,
				status : isOk
			},
			success : function(form, action) {
				Common.hideProcessMsgBox();
				Ext.topShow.msg('成功提示', action.result.msg);
				FinProject.gotoPending();
			},
			failure : function(form, action) {
				Common.hideProcessMsgBox();
				var failMsg = Ext.isEmpty(action.result) ? "信息保存失败."
						: action.result.msg;
				Ext.Msg.show( {
					title : '失败提示',
					msg : failMsg || '项目信息保存失败.',
					width : 180,
					modal : false,
					buttons : Ext.Msg.OK,
					icon : Ext.MessageBox.ERROR
				});
			}
		});
}
};

function json2Str(o) {
	if (o == undefined) {
		return "";
	}
	var r = [];
	if (typeof o == "string")
		return "\""
				+ o.replace(/([\"\\])/g, "\\$1").replace(/(\n)/g, "\\n")
						.replace(/(\r)/g, "\\r").replace(/(\t)/g, "\\t") + "\"";
	if (typeof o == "object") {
		if (!o.sort) {
			for ( var i in o)
				r.push("\"" + i + "\":" + json2Str(o[i]));
			if (!!document.all
					&& !/^\n?function\s*toString\(\)\s*\{\n?\s*\[native code\]\n?\s*\}\n?\s*$/
							.test(o.toString)) {
				r.push("toString:" + o.toString.toString());
			}
			r = "{" + r.join() + "}"
		} else {
			for ( var i = 0; i < o.length; i++)
				r.push(json2Str(o[i]))
			r = "[" + r.join() + "]";
		}
		return r;
	}
	return o.toString().replace(/\"\:/g, '":""');
}