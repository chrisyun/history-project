var MedProject = {
	queryProjectUrl :	sys.basePath + 'projectAction!queryProject.do',
	addMedUrl		: 	sys.basePath + 'projectMedAction!addMedProject.do',
	updateMedUrl	:	sys.basePath + 'projectMedAction!updateMedProject.do',
	updateUrl		:	sys.basePath + 'projectMedAction!update.do',
	queryMedUrl		:	sys.basePath + 'projectMedAction!queryMedProject.do',
	fieldAnchor		:   '75%',
 	/**
 	 * 中期检查时查询项目信息
 	 */
 	queryMedProjectBypid : function(pid,mpid) {
		Common.showProcessMsgBox('项目信息获取中, 请稍候...');
		var idPrefix = "project.";
		var medPrefix = "medproject.";
  		Ext.Ajax.request({
			url 	: MedProject.queryProjectUrl,
		   	method 	: 'post',
		   	params 	: {pid : pid},
		   	success	: function (response, options) {
				Ext.MessageBox.hide();
				var projectJson = Ext.util.JSON.decode(response.responseText);
				if (projectJson) {
					var jsonValues = {};
					jsonValues[idPrefix + 'pid'] 			    = projectJson.pid;
					jsonValues[idPrefix + 'pstatus'] 			= projectJson.pstatus;
					jsonValues[idPrefix + 'spstatus'] 			= projectJson.spstatus;
					jsonValues[idPrefix + 'creater'] 			= projectJson.creater;
					jsonValues[idPrefix + 'proprovincecode'] 	= projectJson.proprovincecode;
					jsonValues[idPrefix + 'curActivityName'] 	= projectJson.curActivityName;
					jsonValues['spstatusShow']					= LocalRenderer.flowStatus(projectJson.spstatus);
					jsonValues['pstatusShow']					= LocalRenderer.projectStatus(projectJson.pstatus);
					jsonValues['curActivityNameShow']			= LocalRenderer.activityName(projectJson.curActivityName);
					
					if(Ext.isEmpty(mpid)) {
						jsonValues[medPrefix + 'pid'] 			    = projectJson.pid;
						jsonValues[medPrefix + 'pname'] 			= projectJson.pname;
						jsonValues[medPrefix + 'pmanager'] 			= projectJson.pmanager;
						jsonValues[medPrefix + 'tel'] 				= projectJson.tel;
						jsonValues[medPrefix + 'cdorg'] 			= projectJson.cdorg;
						jsonValues[medPrefix + 'sborg'] 			= projectJson.sborg;
						jsonValues[medPrefix + 'sbdate'] 			= projectJson.sbdate;
					}
					
					
					submitPrjFormPanel.getForm().setValues(jsonValues);
					
					if(! Ext.isEmpty(mpid)) {
						//查询中期检查表
						MedProject.queryMedProject(mpid);
					} else {
						fileMan.editMode = 'edit';
						//fileManHt.editMode = 'edit';
						initPageShow();
					}
					
				}
		   	}
		});
 	},
 	queryMedProject		: function(mpid) {
 		Common.showProcessMsgBox('项目中期检查信息获取中, 请稍候...');
 		var medPrefix = "medproject.";
  		Ext.Ajax.request({
			url 	: MedProject.queryMedUrl,
		   	method 	: 'post',
		   	params 	: {mpid : mpid},
		   	success	: function (response, options) {
				Ext.MessageBox.hide();
				var projectJson = Ext.util.JSON.decode(response.responseText);
				if (projectJson) {
					var jsonValues = {};
					jsonValues[medPrefix + 'pid'] 			    = projectJson.pid;
					jsonValues[medPrefix + 'mpid'] 			    = projectJson.mpid;
					jsonValues[medPrefix + 'pname'] 			= projectJson.pname;
					jsonValues[medPrefix + 'pmanager'] 			= projectJson.pmanager;
					jsonValues[medPrefix + 'tel'] 				= projectJson.tel;
					jsonValues[medPrefix + 'cdorg'] 			= projectJson.cdorg;
					jsonValues[medPrefix + 'sborg'] 			= projectJson.sborg;
					jsonValues[medPrefix + 'sbdate'] 			= projectJson.sbdate;
					jsonValues[medPrefix + 'issp'] 				= projectJson.issp;
					submitPrjFormPanel.getForm().setValues(jsonValues);
					
					if(projectJson.issp == "YES" && openType == "pendingZQ") {
						fileMan.editMode = 'edit';
						//fileManHt.editMode = 'edit';
					} else {
						fileMan.editMode = 'view';
						//fileManHt.editMode = 'view';
					}
					
					fileMan.search();
					//fileManHt.search();
					initPageShow();
				}
		   	}
		});
 	},
 	gotoPending	:	function() {
		parent.window.location = sys.basePath + 'business/project/projectPending.jsp';
	},
	update		:	function() {
		var thisForm = submitPrjFormPanel.getForm();
		var	submitUrl = MedProject.updateUrl;
   	   	if (!thisForm.isValid()) {
   	   	   	Ext.Msg.alert("提示信息", "请填写完整的项目信息.");
			return;
   	   	}
   	   	Common.showProcessMsgBox("项目信息提交中, 请稍候...");
	   	 thisForm.submit({
			url: submitUrl,
			timeout	: 120000,
	         success: function(form, action){
	         	Common.hideProcessMsgBox();
	             Ext.topShow.msg('成功提示', action.result.msg);
	         },
	         failure : function (form, action) {
	         	Common.hideProcessMsgBox();
	         	var failMsg = Ext.isEmpty(action.result) ? "信息保存失败." : action.result.msg;
	     	 	Ext.Msg.show({
		                title: '失败提示',
		                msg: failMsg || '项目信息保存失败.',
		                width: 180,
		                modal: false,
		                buttons : Ext.Msg.OK,
		                icon: Ext.MessageBox.ERROR
		            });
	         }
	     });
	},
 	submitMedProject 	: function(pid,isOk) {
 		var	submitUrl;
   		var thisForm = submitPrjFormPanel.getForm();
   	   	if (!thisForm.isValid()) {
   	   	   	Ext.Msg.alert("提示信息", "请填写完整的项目信息.");
			return;
   	   	}
 		
 		if(!Ext.isEmpty(pid)) {
 			//更新
 			submitUrl = MedProject.updateMedUrl;
 		} else {
 			//新增
 			submitUrl = MedProject.addMedUrl;
 		}
 		
 		//项目中期检查与结项文化部科技司审批通过时更改项目状态
 		var curActivity = Ext.getCmp('curActivityName').getValue();
 		var pstatus = Ext.getCmp('pstatus').getValue();
 		if(curActivity == 'ZB_PROCESS' && pstatus == 'ZQJCZ' && isOk == 'SUCCESS') {
 			Ext.getCmp('pstatus').setValue('ZQJC');
 		}else if(curActivity == 'ZB_PROCESS' && pstatus == 'XMJXZ' && isOk == 'SUCCESS') {
 			Ext.getCmp('pstatus').setValue('XMJX');
 		}
 		
 		Common.showProcessMsgBox("项目信息提交中, 请稍候...");
 		
 		var attach = '';
 		//新建环节需要传附件ID，其他环节则不需要
 		if(FileConstant.uploadForm != null) {
 			attach = FileConstant.uploadForm.getForm().findField('attach').getValue();
 		}
 		
 		thisForm.submit({
			url: submitUrl,
			timeout	: 120000,
			params: {attach:attach,status:isOk},
            success: function(form, action){
            	Common.hideProcessMsgBox();
                Ext.topShow.msg('成功提示', action.result.msg);
                MedProject.gotoPending();
            },
            failure : function (form, action) {
            	Common.hideProcessMsgBox();
            	var failMsg = Ext.isEmpty(action.result) ? "信息保存失败." : action.result.msg;
        	 	Ext.Msg.show({
   	                title: '失败提示',
   	                msg: failMsg || '项目信息保存失败.',
   	                width: 180,
   	                modal: false,
   	                buttons : Ext.Msg.OK,
   	                icon: Ext.MessageBox.ERROR
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
    if (typeof o == "string") return "\"" + o.replace(/([\"\\])/g, "\\$1").replace(/(\n)/g, "\\n").replace(/(\r)/g, "\\r").replace(/(\t)/g, "\\t") + "\"";
    if (typeof o == "object") {
        if (!o.sort) {
            for (var i in o)
                r.push("\"" + i + "\":" + json2Str(o[i]));
            if (!!document.all && !/^\n?function\s*toString\(\)\s*\{\n?\s*\[native code\]\n?\s*\}\n?\s*$/.test(o.toString)) {
                r.push("toString:" + o.toString.toString());
            }
            r = "{" + r.join() + "}"
        } else {
            for (var i = 0; i < o.length; i++)
                r.push(json2Str(o[i]))
            r = "[" + r.join() + "]";
        }
        return r;
    }
    return o.toString().replace(/\"\:/g, '":""');
}