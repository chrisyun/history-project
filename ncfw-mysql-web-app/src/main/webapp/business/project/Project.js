var Project = {
	projectNumUrl		:	sys.basePath + 'projectAction!getCurNumber.do',
	queryProjectUrl		:	sys.basePath + 'projectAction!queryProject.do',
	addProjectUrl		:	sys.basePath + 'projectAction!addProject.do',
	inputProjectUrl		:	sys.basePath + 'projectAction!addInputProject.do',
	updateProjectUrl	:	sys.basePath + 'projectAction!submitProject.do',
	deleteUrl			:	sys.basePath + 'projectAction!deleteProject.do',
	upProjectUrl		:	sys.basePath + 'projectAction!updateProject.do',
	gotoUrl				:	sys.basePath + 'business/project/projectPending.jsp',
	updateStatusUrl		:	sys.basePath + 'projectAction!updateProjectStatus.do',
	projectMedIdsUrl	:	sys.basePath + 'projectMedAction!searchMedProjectIds.do',
	projectFinIdsUrl	:	sys.basePath + 'projectFinAction!searchFinProjectIds.do',
	fieldAnchor			:   '75%',
	showPendingWindow	: 	function(type) {
		if (dataGrid.getSelectionModel().getCount() != 1) {
	        Ext.Msg.alert("提示信息", "请选择一条记录!");
	        return;
	    }
	    
	    var selectedRecord = dataGrid.getSelectionModel().getSelected();
	    
	    var pid = selectedRecord.get("pid");
	    var ptype = selectedRecord.get("pmtype");
	    var pstatus	= selectedRecord.get("pstatus");
	    
	    var projectUrl = sys.basePath + 'business/project/';
	    
	    if(type == 'pending' && pstatus == 'ZQJCZ') {
	    	type = "pendingZQ";
	    } else if(type == 'pending' && pstatus == 'XMJXZ') {
	    	type = "pendingJX";
	    } else if(type == 'searchInput') {
	    	projectUrl += "BZProject.jsp";
	    	ptype = '';
	    }
	    
    	if(ptype == "GJWHCXGC") {
	    	projectUrl += "GJWHCXGC.jsp";
	    }else if(ptype == "WHBKJCX") {
	    	projectUrl += "WHBKJCXXM.jsp";
	    }else if(ptype == "GJWHKJTS") {
	    	projectUrl += "GJWHKJTSJHXM.jsp";
	    }else if(ptype == "WHBCXJ") {
	    	projectUrl += "WHBCXJ.jsp";
	    }else if(ptype == "BZHKY") {
	    	projectUrl += "BZHKYXM.jsp";
	    }else if(ptype == "BZHLX") {
	    	projectUrl += "";
	    }else if(ptype == "GJRKXYJ") {
	    	projectUrl += "";
	    }else if(ptype == "GJJHJ") {
	    	projectUrl += "";
	    }else if(ptype == "KJJCXGZ") {
	    	projectUrl += "";
	    }
    	
	    projectUrl += "?pid="+pid+"&openType="+type;
	    
	    window.open(projectUrl,'newwindow','height=600,width=1000,top=50,left=50,toolbar=no,menubar=no,scrollbars=no, resizable=no,location=no, status=no')
	    
//	    window.location = projectUrl;
	},
	generateProjectNum	:	function(pinying) {
		Common.showProcessMsgBox('自动生成项目编号中，请稍后...');
		Ext.Ajax.request({
			method:'POST', 
			timeout:0,
			url : Project.projectNumUrl,
			params : {
				pinying : pinying
			},
			success : function(response, options) {
				var result = Ext.util.JSON.decode(response.responseText);
				Ext.MessageBox.hide();
				if (result.success) {
					Ext.getCmp('pslnumber').setValue(result.msg);
   			   	} else {
   			   		Ext.Msg.show({
       	                title: '失败提示',
       	                msg: result.msg,
       	                width: 180,
       	                modal: false,
       	                buttons : Ext.Msg.OK,
       	                icon: Ext.MessageBox.ERROR
       	            });
       			}
			},
			failure : function() {
				Ext.MessageBox.hide();
   			   	Ext.Msg.show({
   	                title: '失败提示',
   	                msg: '生成项目编号时发生异常.',
   	                width: 180,
   	                modal: false,
   	                buttons : Ext.Msg.OK,
   	                icon: Ext.MessageBox.ERROR
   	            });
			}
		});
 	},
 	queryMedProjectId : function(pid) {
 		Ext.Ajax.request({
			method:'POST', 
			timeout:0,
			url : Project.projectMedIdsUrl,
			params : {
				pid : pid
			},
			success : function(response, options) {
				var result = Ext.util.JSON.decode(response.responseText);
				for(var i=1;i<result.length+1;i++) {
					var mpid = result[i-1].mpid;
					var newZQPanel = new Ext.Panel({
						layout:"fit",
						title : "项目中期检查[" + i +"]",
						iconCls:"icon_plugin",
						id:"zq_panel_"+i,
						border:false,
						html : "<iframe src='"+ sys.basePath + "business/project/projectZQ.jsp?mpid=" + mpid +"&pid="+pid+"&openType="+openType+"' width='100%' height='100%' frameborder='0'></iframe>"
					});

					tabPanel.add("zq_panel_"+i);
					if(result[i-1].issp == "YES") {
						tabPanel.activate("zq_panel_"+i);
					}
				}
				
			},
			failure : function() {
   			   	Ext.Msg.show({
   	                title: '失败提示',
   	                msg: '查询项目相关信息发生异常.',
   	                width: 180,
   	                modal: false,
   	                buttons : Ext.Msg.OK,
   	                icon: Ext.MessageBox.ERROR
   	            });
			}
		});
 	},
 	queryFinProjectId : function(pid) {
 		Ext.Ajax.request({
			method:'POST', 
			timeout:0,
			url : Project.projectFinIdsUrl,
			params : {
				pid : pid
			},
			success : function(response, options) {
				var result = Ext.util.JSON.decode(response.responseText);
				for(var i=1;i<result.length+1;i++) {
					var fpid = result[i-1].fpid;
					var newJXPanel = new Ext.Panel({
						layout:"fit",
						title : "项目结项检查",
						iconCls:"icon_plugin",
						id:"jx_panel_"+i,
						border:false,
						html : "<iframe src='"+ sys.basePath + "business/project/projectJX.jsp?fpid=" + fpid +"&pid="+pid+"&openType="+openType+"' width='100%' height='100%' frameborder='0'></iframe>"
					});

					tabPanel.add("jx_panel_"+i);
					if(result[i-1].issp == "YES") {
						tabPanel.activate("jx_panel_"+i);
					}
				}
				
			},
			failure : function() {
   			   	Ext.Msg.show({
   	                title: '失败提示',
   	                msg: '查询项目相关信息发生异常.',
   	                width: 180,
   	                modal: false,
   	                buttons : Ext.Msg.OK,
   	                icon: Ext.MessageBox.ERROR
   	            });
			}
		});
 	},
 	queryProject	: function(pid) {
		Common.showProcessMsgBox('项目信息获取中, 请稍候...');
		var idPrefix = "project.";
  		Ext.Ajax.request({
			url 	: Project.queryProjectUrl,
		   	method 	: 'post',
		   	params 	: {pid : pid},
		   	success	: function (response, options) {
				Ext.MessageBox.hide();
				var projectJson = Ext.util.JSON.decode(response.responseText);
				if (projectJson) {
					var jsonValues = {};
					jsonValues[idPrefix + 'pid'] 			    = projectJson.pid;
					jsonValues[idPrefix + 'pname'] 				= projectJson.pname;
					jsonValues[idPrefix + 'pmtype'] 			= projectJson.pmtype;
					jsonValues[idPrefix + 'pstatus'] 			= projectJson.pstatus;
					jsonValues[idPrefix + 'spstatus'] 			= projectJson.spstatus;
					jsonValues[idPrefix + 'creattime'] 			= projectJson.creattime;
					jsonValues[idPrefix + 'creater'] 			= projectJson.creater;
					jsonValues[idPrefix + 'pmanager'] 			= projectJson.pmanager;
					jsonValues[idPrefix + 'tel'] 				= projectJson.tel;
					jsonValues[idPrefix + 'email'] 				= projectJson.email;
					jsonValues[idPrefix + 'proprovincecode'] 	= projectJson.proprovincecode;
					jsonValues[idPrefix + 'curActivityName'] 	= projectJson.curActivityName;
					jsonValues[idPrefix + 'psecret'] 			= projectJson.psecret;
					jsonValues[idPrefix + 'pslnumber'] 			= projectJson.pslnumber;
					jsonValues[idPrefix + 'plxnumber'] 			= projectJson.plxnumber;
					jsonValues[idPrefix + 'psbtype'] 			= projectJson.psbtype;
					jsonValues[idPrefix + 'ptype'] 				= projectJson.ptype;
					jsonValues[idPrefix + 'cdorg'] 				= projectJson.cdorg;
					jsonValues[idPrefix + 'sborg'] 				= projectJson.sborg;
					jsonValues[idPrefix + 'sbdate'] 			= projectJson.sbdate;
					jsonValues[idPrefix + 'gjorg'] 				= projectJson.gjorg;
					jsonValues[idPrefix + 'jsorg'] 				= projectJson.jsorg;
					jsonValues[idPrefix + 'hyorg'] 				= projectJson.hyorg;
					jsonValues[idPrefix + 'tjorg'] 				= projectJson.tjorg;
					jsonValues[idPrefix + 'pwtype'] 			= projectJson.pwtype;
					jsonValues[idPrefix + 'isInput'] 			= projectJson.isInput;
					jsonValues['spstatusShow']					= LocalRenderer.flowStatus(projectJson.spstatus);
					jsonValues['pstatusShow']					= LocalRenderer.projectStatus(projectJson.pstatus);
					jsonValues['curActivityNameShow']			= LocalRenderer.activityName(projectJson.curActivityName);
					submitPrjFormPanel.getForm().setValues(jsonValues);
					initPageShow();
				}
		   	}
		});
  		fileMan.search();
 	},
 	updateProject	: function() {
 		var submitUrl = Project.upProjectUrl;
 		var thisForm = submitPrjFormPanel.getForm();
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
 	submintProject	: function(pid,isOk) {
 		var	submitUrl;
   		var thisForm = submitPrjFormPanel.getForm();
   	   	if (!thisForm.isValid()) {
   	   	   	Ext.Msg.alert("提示信息", "请填写完整的项目信息.");
			return;
   	   	}
 		
 		if(!Ext.isEmpty(pid)) {
 			//更新
 			submitUrl = Project.updateProjectUrl;
 		} else {
 			//新增
 			if(!Ext.isEmpty(isOk) && isOk == "input") {
 				submitUrl = Project.inputProjectUrl;
 			} else {
 				submitUrl = Project.addProjectUrl;
 			}
 		}
 		
 		//项目中期检查与结项文化部科技司审批通过时更改项目状态
 		/*
 		var curActivity = Ext.getCmp('curActivityName').getValue();
 		var pstatus = Ext.getCmp('pstatus').getValue();
 		if(curActivity == 'ZB_PROCESS' && pstatus == 'ZQJCZ' && isOk == 'SUCCESS') {
 			Ext.getCmp('pstatus').setValue('ZQJC');
 		}else if(curActivity == 'ZB_PROCESS' && pstatus == 'XMJXZ' && isOk == 'SUCCESS') {
 			Ext.getCmp('pstatus').setValue('XMJX');
 		}*/
 		
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
                Project.gotoPending();
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
 	gotoPending	:	function() {
 		if(window.opener == null) {
 			window.location = Project.gotoUrl;
 		} else {
 	 		window.close();
 	 		window.opener.onSubmitQueryHandler();
 		}
	},
	updateProjectStatus	:	function() {
		var selectedRecordArray = dataGrid.getSelectionModel().getSelections();
		if(selectedRecordArray.length < 1){
     		Ext.Msg.alert("提示信息", "请选择可以立项的项目!");
     		return;
      	}
		
		for(var i = 0;i < selectedRecordArray.length;i++){
			var spStatus = selectedRecordArray[i].get("spstatus");
			var curActivity = selectedRecordArray[i].get("curActivityName");
			var pStatus = selectedRecordArray[i].get("pstatus");
			if(spStatus != 'SUCCESS' || curActivity != 'FINISH' || pStatus != 'SB') {
				Ext.Msg.alert("提示信息", "您选择的项目不符合立项条件（需申报流程审批通过并且项目状态为[申报中]）");
	     		return;
			}
		}
		
		
		var pids = "";
      	for(var i = 0;i < selectedRecordArray.length;i++){
      		pids += selectedRecordArray[i].get("pid") + ",";
	    }
      	if(pids.length > 0){
      		pids = pids.substring(0, pids.length - 1);
	    }
      	
      	params = {pids: pids};
	    
		Common.ajaxRequest({
			url: Project.updateStatusUrl, params: params,
			proccessMsg: '项目状态更改中, 请稍后...', successHandler: function () {dataGrid.getStore().reload();}
		});
	},
	deleteProject	:	function() {
		var selectedRecordArray = dataGrid.getSelectionModel().getSelections();
		if(selectedRecordArray.length < 1){
     		Ext.Msg.alert("提示信息", "请选择需要删除的项目!");
     		return;
      	}
		
		
		var pids = "";
      	for(var i = 0;i < selectedRecordArray.length;i++){
      		pids += selectedRecordArray[i].get("pid") + ",";
	    }
      	if(pids.length > 0){
      		pids = pids.substring(0, pids.length - 1);
	    }
      	
      	params = {pids: pids};
	    
		Common.ajaxRequest({
			url: Project.deleteUrl, params: params,
			proccessMsg: '项目删除中, 请稍后...', successHandler: function () {dataGrid.getStore().reload();}
		});
	},
	projectMed	:	function(pid) {
		/*
		var projectUrl = sys.basePath + 'business/project/projectZQ.jsp?pid='+pid;
		window.location = projectUrl;
		*/
		var newZQPanel = new Ext.Panel({
			layout:"fit",
			title : "项目中期检查申报",
			iconCls:"icon_plugin",
			border:false,
			html: "<iframe src='"+ sys.basePath + "business/project/projectZQ.jsp?openType=draft&pid="+pid+"' width='100%' height='100%' frameborder='0'></iframe>"
		});

		tabPanel.add(newZQPanel);
		tabPanel.activate(newZQPanel);
	},
	projectFin	:	function(pid) {
		var newJXPanel = new Ext.Panel({
			layout:"fit",
			title : "项目结项申报",
			iconCls:"icon_plugin",
			border:false,
			html: "<iframe src='"+ sys.basePath + "business/project/projectJX.jsp?openType=draft&pid="+pid+"' width='100%' height='100%' frameborder='0'></iframe>"
		});

		tabPanel.add(newJXPanel);
		tabPanel.activate(newJXPanel);
	}
};

var PWProject = {
		selForm		: 	null,
		selWindow	:	null,
		
		getSelForm	:	function() {
			if (this.selForm != null) {
				return this.selForm;
			}
			
			this.selForm = new Ext.FormPanel({
				id: 'selForm',
				region: 'center',
				autoScroll: true,
		        frame: true,
		        labelAlign: 'right',
		        labelWidth: 80,	        
		     	items: [{
		        	items: [{
		            	layout: 'column',border: false, labelSeparator: ':',
		                defaults:{layout: 'form', border: false, columnWidth: 1.0},
		                items: [{
		                	items:[{
		                		xtype: 'hidden', id: 'pids', name: 'pids'
		                	}]
		                },{
		                	items:[{
		                		xtype: 'combo', id: 'pwtype1', hiddenName: 'pwtype', anchor: "95%",
		                		fieldLabel: '项目分类', readOnly: false, allowBlank: true,editable: false,
				                mode: 'local', triggerAction: 'all', valueField: 'value', displayField: 'text',
				                readOnly: false, resizable: true,
				                store: new Ext.data.SimpleStore({
									fields: ['value', 'text'], data: DataDict.pwProjectTypeArray
								})
		                	}]
		                }]
		        	}]
		     	}]
			});
			
			return this.selForm;
		},
		showSelWindow	:	function() {
			if (this.selWindow == null) {
				var windowWidth = document.body.clientWidth > 400 ? 400 : document.body.clientWidth - 10;
				var windowHeight = document.body.clientHeight > 150 ? 150 : document.body.clientHeight - 10;
				
				var inputForm = this.getSelForm();
				
				this.selWindow = new Ext.Window({ //定义对话框
					title: '项目分类',
					closeAction : 'hide',
					modal : true,
					shadow : true,
					closable : true,
					layout : 'fit',
					width : windowWidth,
					height : windowHeight,
					items:[inputForm],
					buttons : [{
						text :'保存',
						iconCls: 'icon_save',
						scope: this,
						handler: this.updateType
					},{
						text :'关闭',
						iconCls: 'icon_close',
						scope: this,
						handler: function(){this.selWindow.hide();}
					}]
				});

			}
			
			this.selWindow.show();
		},
		
		choosePwType	:	function() {
			var selectedRecordArray = dataGrid.getSelectionModel().getSelections();
			if(selectedRecordArray.length < 1){
		 		Ext.Msg.alert("提示信息", "请选择需要分类的项目!");
		 		return;
		  	}
			
			var pids = "";
		  	for(var i = 0;i < selectedRecordArray.length;i++){
		  		pids += selectedRecordArray[i].get("pid") + ",";
		    }
		  	if(pids.length > 0){
		  		pids = pids.substring(0, pids.length - 1);
		    }
		  	
		  	params = {pids: pids};
		  	
		  	PWProject.showSelWindow();
		  	PWProject.selForm.getForm().setValues(params);
		},
		
		updateType		:	function() {
			var thisForm = PWProject.getSelForm().getForm();
			if (!thisForm.isValid()) {
				return;
			}
			
			Common.showProcessMsgBox("信息保存中，请稍后...");
			thisForm.submit({
				url		: 	sys.basePath + 'projectAction!updatePWType.do',
	            success	: 	function(form, action){
	            	Common.hideProcessMsgBox();
	                Ext.topShow.msg('成功提示', '项目分类成功.');
	                //ChangePwd.inputWindow.hide();
	            }
	        });
		}
}

var ProjectStatus = {
		statusForm		: 	null,
		statusWindow	:	null,
		showWindow	:	function() {
			if (this.statusWindow == null) {
				var windowWidth = document.body.clientWidth > 400 ? 400 : document.body.clientWidth - 10;
				var windowHeight = document.body.clientHeight > 150 ? 150 : document.body.clientHeight - 10;
				
				this.statusForm = new Ext.FormPanel({
					id: 'statusForm',
					region: 'center',
					autoScroll: true,
			        frame: true,
			        labelAlign: 'right',
			        labelWidth: 80,	        
			     	items: [{
			        	items: [{
			            	layout: 'column',border: false, labelSeparator: ':',
			                defaults:{layout: 'form', border: false, columnWidth: 1.0},
			                items: [{
			                	items:[{
			                		xtype: 'hidden', id: 'pids', name: 'pids'
			                	}]
			                },{
			                	items:[{
			                		xtype: 'combo', id: 'pro_status1', hiddenName: 'pro_status', anchor: "95%",
			                		fieldLabel: '项目状态', readOnly: false, allowBlank: true,editable: false,
					                mode: 'local', triggerAction: 'all', valueField: 'value', displayField: 'text',
					                readOnly: false, resizable: true,
					                store: new Ext.data.SimpleStore({
										fields: ['value', 'text'], data: DataDict.projectStatusArray
									})
			                	}]
			                }]
			        	}]
			     	}]
				});
				
				this.statusWindow = new Ext.Window({ //定义对话框
					title: '项目状态',
					closeAction : 'hide',
					modal : true,
					shadow : true,
					closable : true,
					layout : 'fit',
					width : windowWidth,
					height : windowHeight,
					items:[this.statusForm],
					buttons : [{
						text :'保存',
						iconCls: 'icon_save',
						scope: this,
						handler: this.updateStatus
					},{
						text :'关闭',
						iconCls: 'icon_close',
						scope: this,
						handler: function(){this.statusWindow.hide();}
					}]
				});

			}
			
			this.statusWindow.show();
		},
		
		chooseStatus :	function() {
			var selectedRecordArray = dataGrid.getSelectionModel().getSelections();
			if(selectedRecordArray.length < 1){
		 		Ext.Msg.alert("提示信息", "请选择需要更改状态的项目!");
		 		return;
		  	}
			
			var pids = "";
		  	for(var i = 0;i < selectedRecordArray.length;i++){
		  		pids += selectedRecordArray[i].get("pid") + ",";
		    }
		  	if(pids.length > 0){
		  		pids = pids.substring(0, pids.length - 1);
		    }
		  	
		  	params = {pids: pids};
		  	
		  	ProjectStatus.showWindow();
		  	ProjectStatus.statusForm.getForm().setValues(params);
		},
		
		updateStatus		:	function() {
			var thisForm = ProjectStatus.statusForm.getForm();
			if (!thisForm.isValid()) {
				return;
			}
			
			Common.showProcessMsgBox("信息保存中，请稍后...");
			thisForm.submit({
				url		: 	sys.basePath + 'projectAction!modifyProjectStatus.do',
	            success	: 	function(form, action){
	            	Common.hideProcessMsgBox();
	                Ext.topShow.msg('成功提示', '项目修改状态成功.');
	                dataGrid.getStore().reload();
	            }
	        });
		}
}

function setOrgSel(field, record, index) {
	var deptEmp = Ext.getCmp("query.dept");
	var store = deptEmp.store;
	store.removeAll();
	
	var newValue = field.getValue();
	
	if(newValue == 'ZS') {
		store.loadData(Common.config.nullArray.concat([['9','文化部直属单位']]));
	} else if(newValue == 'YX') {
		store.loadData(Common.config.nullArray.concat([['38','原文化部直属高等艺术院校']]));
	} else if(newValue == 'SJ') {
		store.loadData(Common.config.nullArray.concat([['39','文化部各司局']]));
	} else if(newValue == 'SF') {
		store.loadData(Common.config.nullArray.concat(DataDict.dwCodeArray));
	}
}

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