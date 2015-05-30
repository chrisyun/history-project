var User = {
	inputForm		: null,		//录入表单
	inputWindow		: null,		//录入对话框
	fieldAnchor		: Common.config.fieldAnchor,
	windowWidth 	: document.body.clientWidth > 660 ? 660 : document.body.clientWidth - 10,
	windowHeight 	: document.body.clientHeight > 286 ? 286 : document.body.clientHeight - 10,
	optype			: null,
	getInputForm	: function () {
		if (this.inputForm != null) {
			return this.inputForm;
		}
		this.inputForm = new Ext.FormPanel({
			id: 'inputForm',
			region: 'center',
			bodyStyle: 'padding:5px; overflow-x:visible;overflow-y:scroll',
			autoScroll: true,
	        frame: true,
	        labelAlign: 'right',
	        labelWidth: 86,	        
         	items: [{
        		xtype: 'panel',
        		//title: '基本信息',
        		autoHeight: true,
            	items: [{
	            	layout: 'column',border: false, labelSeparator: ':',
	                defaults:{layout: 'form', border: false, columnWidth: 0.5},
	                items: [{
	                	xtype: 'panel',
	                	id: 'useridPanel',
	                	items:[{
	                		xtype: 'textfield', id: 'userid', name: 'user.userid', anchor: this.fieldAnchor,
	                		fieldLabel: Common.config.redStar + '登录名', readOnly: false, allowBlank: false
	                	}]
	                },{
	                	items:[{
	                		xtype: 'textfield', id: 'username', name: 'user.username', anchor: this.fieldAnchor,
	                		fieldLabel: Common.config.redStar + '姓名', readOnly: false, allowBlank: false
	                	}]
	                },{
	                	items:[{
	                		xtype: 'combo', id: 'gender', hiddenName: 'user.gender', anchor: this.fieldAnchor,
	                		fieldLabel: '用户性别', readOnly: false, allowBlank: true,editable: false,
			                mode: 'local', triggerAction: 'all', valueField: 'value', displayField: 'text',
			                readOnly: false, resizable: true, value: 1,
			                store: new Ext.data.SimpleStore({
								fields: ['value', 'text'], data: DataDict.sexArray
							})
	                	}]
	                },{
	                	items:[{
	                		xtype: 'treefield', id: 'citynumber', hiddenName: 'user.citynumber', anchor: this.fieldAnchor,
	                		fieldLabel: Common.config.redStar + '所属组织', readOnly: true, allowBlank: false,
	                		valueField: 'id', displayField: 'text', border: false, listHeight: 200,
		   					tree: new Ext.tree.TreePanel({
		   	   					border: false, rootVisible: true, split: false, autoHeight: false, 
		   	   					frame: false, autoScroll: true, useArrows: false, containerScroll: true,
		   	   					id:'user-deptTree', root : new Ext.tree.AsyncTreeNode({text: orgRootName,id: orgRoot, leaf: false, expanded: false}),
		   	   					loader: new Ext.tree.TreeLoader({
		   	   	   					dataUrl : sys.basePath + "orgAction!createOrgTree.do",
			   	   	   				listeners : {
			   	   	   	   				beforeload: function(thisLoader, node, callback){
			   	   	   	   					thisLoader.baseParams.parentId = node.id
				   	   	   	   			}
			   	   	   	   			}
			   	   	   	   		}),
			   	   	   	   		listeners : {
			   	   	   	   			beforeload  : function(node){
		   	   	   	   					Ext.getCmp('user-deptTree').currentNodeId = node.id;
		   	   	   	   					return true; 
		   	   	   	   				},
		   	   	   	   				click : function (node, e) {
		   	   	   	   					var provinceCode 	= '';
		   	   	   	   					if (node.attributes && node.attributes.attributes && node.attributes.attributes.provinceCode) {
		   	   	   	   						provinceCode = node.attributes.attributes.provinceCode;
		   	   	   	   					}
		   	   	   	   					Ext.getCmp('provincenumber').setValue(provinceCode);
		   	   	   	   				}
		  	   	   				}
			   	   			})
	                	}]
	                },{
	                	items:[{
	                		xtype: 'combo', id: 'provincenumber', hiddenName: 'user.provincenumber', anchor: this.fieldAnchor,
	                		fieldLabel: Common.config.redStar + '所属省份', allowBlank: false,editable: false,
			                mode: 'local', triggerAction: 'all', valueField: 'value', displayField: 'text',
			                readOnly: false, resizable: true,
			                store: new Ext.data.SimpleStore({
								fields: ['value', 'text'], data: DataDict.provinceCodeArray
							})
	                	}]
	                },{
	                	items:[{
	                		xtype: 'combo', id: 'status', hiddenName: 'user.status', anchor: this.fieldAnchor,
	                		fieldLabel: '用户状态', readOnly: false, allowBlank: true,editable: false,
			                mode: 'local', triggerAction: 'all', valueField: 'value', displayField: 'text',
			                readOnly: false, resizable: true, value: 'enable',
			                store: new Ext.data.SimpleStore({
								fields: ['value', 'text'], data: DataDict.statusArray
							})
	                	}]
	                },{
	                	items:[{
	                		xtype: 'textfield', id: 'mobile', name: 'user.mobile', anchor: this.fieldAnchor,
	                		fieldLabel: '联系电话', readOnly: false, allowBlank: true
	                	}]
	                },{
	                	items:[ {
	                		xtype: 'textfield', inputType: 'password', id: 'newpwd', name: 'newpwd', anchor: this.fieldAnchor,
	                		fieldLabel: '密码', readOnly: false, allowBlank: true
	                	}]
	                },{
	                	items:[{
	                		xtype: 'textfield', id: 'mail', name: 'user.mail', vtype: 'email', anchor: this.fieldAnchor,
	                		fieldLabel: Common.config.redStar + '邮箱地址', readOnly: false, allowBlank: false
	                	}]
	                },{
	                	items:[{
	                		xtype: 'textfield', inputType: 'password', id: 'newpwdconfirm', name: 'newpwdconfirm', anchor: this.fieldAnchor,
	                		fieldLabel: '密码确认', readOnly: false, allowBlank: true, vtype:'password', vtypeText:'两次密码不一致', confirmTo:'newpwd'
	                	}]
	                },{
	                	columnWidth: 1.0,
	                	items:[{
	                		xtype: 'combo', id: 'roleid', hiddenName: 'user.roleid', anchor: '97.1%',
	                		fieldLabel: Common.config.redStar + '用户角色', readOnly: false, allowBlank: false,
	                		triggerAction: 'all', resizable: true, lazyInit: true, editable: false,
	                		store: new Ext.data.Store({
		    	                url: sys.basePath + 'rights/roleAction!queryRole.do?currentTransferRoleId=' + currentTransferRoleId,
			                	reader:new Ext.data.JsonReader({
								},
								Ext.data.Record.create([
									{name: 'roleId', type: 'string'},
									{name: 'roleName', type: 'string'}
								]))
			                }),
			                mode: 'remote',
			                triggerAction: 'all',
			                valueField: 'roleId',
			                displayField: 'roleName'
	                	}]
	                }]
            	}]
         	}]
		});
		return this.inputForm;
	},
	showInputWindow : function (title) {
		if (this.inputWindow == null) {
			var inputForm = User.getInputForm();
			this.inputWindow = new Ext.Window({ //定义对话框
				title: '用户信息录入',
				closeAction : 'hide',
				modal : true,
				shadow : true,
				closable : true,
				layout : 'fit',
				width : this.windowWidth,
				height : this.windowHeight,
				items:[inputForm],
				buttons : [{
					text :'保存',
					iconCls: 'icon_save',
					scope: this,
					handler: this.onSaveUser
				},{
					text :'关闭',
					iconCls: 'icon_close',
					scope: this,
					handler: function(){ this.inputWindow.hide();}
				}]
			});
		}
		this.inputWindow.show();
		this.inputWindow.setTitle(title || "用户信息录入");
	},
	onSaveUser : function () {
		var thisForm = User.getInputForm().getForm();
		if (!thisForm.isValid()) {
			return;
		}
		var newpwd 				= thisForm.findField("newpwd");
		var newpwdconfirmCmp 	= thisForm.findField("newpwdconfirm");
		if (newpwd.getValue() != newpwdconfirmCmp.getValue()) {
			newpwdconfirmCmp.markInvalid("密码确认与新输入的密码不一致!");
			return;
		}

		if (!Ext.isEmpty(thisForm.findField('userid').getValue())) {
			thisForm.findField('userid').setValue(thisForm.findField('userid').getValue().toUpperCase());
		}
		var params = {
			optype : User.optype
		};
		if (!Ext.isEmpty(newpwd.getValue())) {
			params['user.userpwd'] = newpwd.getValue();
		}
		
		Common.showProcessMsgBox("信息保存中，请稍后...");
		thisForm.submit({
			url: sys.basePath + "userAction!inputUser.do",
			params : {optype : User.optype},
            success: function(form, action){
            	Common.hideProcessMsgBox();
                Ext.topShow.msg('成功提示', action.result.msg);
                User.inputWindow.hide();
                dataGrid.getStore().reload();
            },
            failure : function (form, action) {
            	Common.hideProcessMsgBox();
            	var failMsg = Ext.isEmpty(action.result) ? "用户信息保存失败." : action.result.msg;
        	 	Ext.Msg.show({
   	                title: '失败提示',
   	                msg: failMsg || '用户信息保存失败.',
   	                width: 180,
   	                modal: false,
   	                buttons : Ext.Msg.OK,
   	                icon: Ext.MessageBox.ERROR
   	            });
            }
        });
	},
	onAdd : function () {		//新增
		User.optype	= Common.optype.add;
		User.showInputWindow("新增用户");
		Ext.getCmp('useridPanel').show();
		User.getInputForm().doLayout(true);
		if(!currentActiveNode){
			return;
		}
		User.getInputForm().getForm().reset();
		var orgvalue = {text: currentActiveNode.attributes.text, id: currentActiveNode.attributes.id};
		Ext.getCmp("citynumber").setValue(orgvalue);
		if (currentActiveNode.attributes && currentActiveNode.attributes.attributes) {
			var provinceCode = currentActiveNode.attributes.attributes.provinceCode;
			Ext.getCmp('provincenumber').setValue(provinceCode);
			//Ext.getCmp('company_id').setValue(User.getCompanyId({node : currentActiveNode}));
		}
       // $("twinpanelChooser").setValue(null);
	},
	onUpdate : function () {	//修改
		if (dataGrid.getSelectionModel().getCount() != 1) {
            Ext.Msg.alert("提示信息", "请选择一条记录!");
            return;
        }
		User.optype	= Common.optype.update;
		var selectedRecord = dataGrid.getSelectionModel().getSelected();
		
		User.showInputWindow("修改用户信息");
		Ext.getCmp('useridPanel').hide();
		User.getInputForm().doLayout(true);
		Common.showProcessMsgBox('表单数据初始化中，请稍后...');
		var thisForm = User.getInputForm().getForm();
		thisForm.reset();
		var values = {
			userid	:	selectedRecord.get("userid"),					username	:	selectedRecord.get("username"),
			mobile	:	selectedRecord.get("mobile"),					mail		:	selectedRecord.get("mail"),		
			gender	:	selectedRecord.get("gender"),					status		:	selectedRecord.get("status"),		
			provincenumber	:	selectedRecord.get("provincenumber"),	roleid	:	selectedRecord.get("roleid")
		};
		thisForm.setValues(values);
		var orgvalue = {text: selectedRecord.get("cityname"), id: selectedRecord.get("citynumber")};
		Ext.getCmp("citynumber").setValue(orgvalue);
		Common.hideProcessMsgBox();
	},
	onDelete : function () {	//删除用户
		var selectedRecordArray = dataGrid.getSelectionModel().getSelections();
		if(selectedRecordArray.length < 1){
     		Ext.Msg.alert("提示信息", "请选择要删除的记录!");
     		return;
      	}
		User.optype	= Common.optype.del;
        Ext.Msg.confirm("确认提示", "确定删除选定的记录?", function(btn){
         	if(btn == 'yes'){
              	var userids = "";
              	for(var i = 0;i < selectedRecordArray.length;i++){
              		userids += selectedRecordArray[i].get("userid") + ",";
        	    }
        	    if(userids.length > 0){
        	    	userids = userids.substring(0, userids.length - 1);
        	    }
        	    
        	    var spflag = Ext.getCmp("isSpflag");
              	var params;
              	if(spflag) {
              		var userMails = "";
             	    for(var i = 0;i < selectedRecordArray.length;i++){
                   		userMails += selectedRecordArray[i].get("mail") + ",";
             	    }
             	    if(userMails.length > 0){
             	    	userMails = userMails.substring(0, userMails.length - 1);
             	    }
             	    
             	    params = {isSpflag:'yes',userids: userids,mails:userMails};
              	} else {
              		params = {userids: userids};
              	}
        	    
				Common.showProcessMsgBox('数据删除中，请稍后...');
				Ext.Ajax.request({
					method:'POST', 
					timeout:0,
					url : sys.basePath + 'userAction!deleteUserBatch.do',
					params : params,
					success : function(response, options) {
						var result = Ext.util.JSON.decode(response.responseText);
						Ext.MessageBox.hide();
						if (result.success) {
					 		Ext.topShow.msg("成功提示", result.msg);
					 		dataGrid.getStore().reload();
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
		   	                msg: '删除数据时发生异常.',
		   	                width: 180,
		   	                modal: false,
		   	                buttons : Ext.Msg.OK,
		   	                icon: Ext.MessageBox.ERROR
		   	            });
					}
				});
         	}
        });	
	},
	/**
	 * 启用
	 */
	enable : function () {
		var selectedRecordArray = dataGrid.getSelectionModel().getSelections();
		if(selectedRecordArray.length < 1){
     		Ext.Msg.alert("提示信息", "请选择需启用的用户记录!");
     		return;
      	}
		var userids = "";
      	for(var i = 0;i < selectedRecordArray.length;i++){
      		userids += selectedRecordArray[i].get("userid") + ",";
	    }
      	if(userids.length > 0){
	    	userids = userids.substring(0, userids.length - 1);
	    }
      	
      	var spflag = Ext.getCmp("isSpflag");
      	var params;
      	if(spflag) {
      		var userMails = "";
     	    for(var i = 0;i < selectedRecordArray.length;i++){
           		userMails += selectedRecordArray[i].get("mail") + ",";
     	    }
     	    if(userMails.length > 0){
     	    	userMails = userMails.substring(0, userMails.length - 1);
     	    }
     	    
     	    params = {isSpflag:'yes',userids: userids,mails:userMails};
      	} else {
      		params = {userids: userids};
      	}
	    
		Common.ajaxRequest({
			url: sys.basePath + 'userAction!enableUser.do', params: params,
			proccessMsg: '用户启用处理中, 请稍后...', successHandler: function () {dataGrid.getStore().reload();}
		});
	},
	/**
	 * 禁用
	 */
	disable : function () {
		var selectedRecordArray = dataGrid.getSelectionModel().getSelections();
		if(selectedRecordArray.length < 1){
     		Ext.Msg.alert("提示信息", "请选择需启用的用户记录!");
     		return;
      	}
		var userids = "";
      	for(var i = 0;i < selectedRecordArray.length;i++){
      		userids += selectedRecordArray[i].get("userid") + ",";
	    }
	    if(userids.length > 0){
	    	userids = userids.substring(0, userids.length - 1);
	    }
		Common.ajaxRequest({
			url: sys.basePath + 'userAction!disableUser.do', params: {userids: userids},
			proccessMsg: '用户禁用处理中, 请稍后...', successHandler: function () {dataGrid.getStore().reload();}
		});
	}
};

//自定义password验证函数
Ext.apply(Ext.form.VTypes,{
    password:function(val,field){
       if(field.confirmTo){
           var pwd=Ext.get(field.confirmTo);
           return (val==pwd.getValue());
       }
       return true;
    }
});