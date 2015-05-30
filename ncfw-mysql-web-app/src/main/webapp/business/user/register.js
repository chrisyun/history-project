var Register = {
	inputForm		: null,		//录入表单
	//inputWindow		: null,		//录入对话框
	fieldAnchor		: Common.config.fieldAnchor,
	windowWidth 	: document.body.clientWidth > 660 ? 660 : document.body.clientWidth - 10,
	windowHeight 	: document.body.clientHeight > 286 ? 286 : document.body.clientHeight - 10,
    showRegister	: function() {
		if (this.inputForm != null) {
			return this.inputForm;
		}
		this.inputForm = new Ext.FormPanel({
			id: 'inputForm',
			region: 'center',
			border:false,
			width : 800,
			height : 350,
			bodyStyle: 'overflow-x:visible;',
			//autoScroll: false,
	        //frame: true,
	        labelAlign: 'right',
	        labelWidth: 100,
	        buttonAlign:'center',  
	        buttons: [{
				text :'注册',
				scope: this,
				handler: this.onSaveUser
			},{
				text :'返回登陆页面',
				scope: this,
				handler: function(){ window.location = sys.basePath;}
			}],
	        items: [{
	        	xtype: 'panel',
	        	layout : 'column',
	        	border: false, 
	        	labelSeparator: ':',
	        	defaults:{layout: 'form', border: false,columnWidth: 0.5},
	        	items: [{
                	xtype: 'panel',
                	id: 'useridPanel',
                	layout : 'form',
                	bodyStyle:'padding-top:9px;padding-bottom:6px;',
                	items:[{
                		xtype: 'textfield', id: 'userid', name: 'user.userid', anchor: this.fieldAnchor,
                		fieldLabel: Common.config.redStar + '登录名', readOnly: false, allowBlank: false
                	}]
                },{
            		xtype: 'label',
            		html: "<div style='padding-top:9px;padding-bottom:6px;text-align:left;'>登录名为申报单位的汉字全称，名称必须与单位行政公章名称一致。字数不得超过20，若全称超过20，请与文化部信息中心联系。</div>"
            	}]
            },{
            	xtype: 'panel',
	        	layout : 'column',
	        	border: false, 
	        	labelSeparator: ':',
	        	defaults:{layout: 'form', border: false,columnWidth: 0.5},
	        	items: [{
                	xtype: 'panel',
                	layout : 'form',
                	bodyStyle:'padding-top:9px;padding-bottom:6px;text-align:left;',
                	items:[{
                		xtype: 'textfield', id: 'username', name: 'user.username', anchor: this.fieldAnchor,
                		fieldLabel: Common.config.redStar + '姓名', readOnly: false, allowBlank: false
                	}]
                },{
            		xtype: 'label',
            		html: "<div style='padding-top:9px;padding-bottom:6px;text-align:left;'>姓名为申报单位管理员真实姓名，文字类型应为汉字，字数不得超过20。</div>"
            	}]
        	},{
            	xtype: 'panel',
	        	layout : 'column',
	        	border: false, 
	        	labelSeparator: ':',
	        	defaults:{layout: 'form', border: false,columnWidth: 0.5},
	        	items: [{
                	xtype: 'panel',
                	layout : 'form',
                	bodyStyle:'padding-top:9px;padding-bottom:6px;text-align:left;',
                	items:[{
                		xtype: 'textfield', id: 'mobile', name: 'user.mobile', anchor: this.fieldAnchor,
                		fieldLabel: '联系电话', readOnly: false, allowBlank: true
                	}]
                },{
            		xtype: 'label',
            		html: "<div style='padding-top:9px;padding-bottom:6px;text-align:left;'>联系电话为申报单位管理员电话座机或者手机。</div>"
            	}]
        	},{
            	xtype: 'panel',
	        	layout : 'column',
	        	border: false, 
	        	labelSeparator: ':',
	        	defaults:{layout: 'form', border: false,columnWidth: 0.5},
	        	items: [{
                	xtype: 'panel',
                	layout : 'form',
                	bodyStyle:'padding-top:9px;padding-bottom:6px;text-align:left;',
                	items:[{
                		xtype: 'treefield', id: 'citynumber', hiddenName: 'user.citynumber', anchor: '97.1%',
                		fieldLabel: Common.config.redStar + '所属组织', readOnly: true, allowBlank: false,
                		valueField: 'id', displayField: 'text', border: false, listHeight: 200,
	   					tree: new Ext.tree.TreePanel({
	   	   					border: false, rootVisible: true, split: false, autoHeight: false, 
	   	   					frame: false, autoScroll: true, useArrows: false, containerScroll: true,
	   	   					id:'user-deptTree', root : new Ext.tree.AsyncTreeNode({text: orgRootName,id: orgRoot, leaf: false, expanded: false}),
	   	   					loader: new Ext.tree.TreeLoader({
	   	   	   					dataUrl : sys.basePath + "orgAction!createOrgTree.do?orgtype=DW&notShow=yes",
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
	   	   	   	   					//Ext.getCmp('provinceshowName').setValue(LocalRenderer.provincenumber(provinceCode));
	   	   	   	   					Ext.getCmp('provincenumber').setValue(provinceCode);
	   	   	   	   				}
	  	   	   				}
		   	   			})
                	}]
                },{
            		xtype: 'label',
            		html: "<div style='padding-top:9px;padding-bottom:6px;text-align:left;'>所属组织为申报单位所属的行政区域。</div>"
            	}]
        	},{
            	xtype: 'panel',
	        	layout : 'column',
	        	border: false, 
	        	labelSeparator: ':',
	        	defaults:{layout: 'form', border: false,columnWidth: 0.5},
	        	items: [{
                	xtype: 'panel',
                	layout : 'form',
                	bodyStyle:'padding-top:9px;padding-bottom:6px;text-align:left;',
                	items:[{
                		xtype: 'textfield', id: 'mail', name: 'user.mail', vtype: 'email', anchor: this.fieldAnchor,
                		fieldLabel: Common.config.redStar + '邮箱地址', readOnly: false, allowBlank: false
                	}]
                },{
            		xtype: 'label',
            		html: "<div style='padding-top:9px;padding-bottom:6px;text-align:left;'>邮箱地址为申报单位管理员的邮箱。</div>"
            	}]
        	},{
            	xtype: 'panel',
	        	layout : 'column',
	        	border: false, 
	        	labelSeparator: ':',
	        	defaults:{layout: 'form', border: false,columnWidth: 0.5},
	        	items: [{
                	xtype: 'panel',
                	layout : 'form',
                	bodyStyle:'padding-top:9px;padding-bottom:6px;text-align:left;',
                	items:[{
                		xtype: 'textfield', inputType: 'password', id: 'newpwd', name: 'newpwd', anchor: this.fieldAnchor,
                		fieldLabel: Common.config.redStar + '密码', readOnly: false, allowBlank: false
                	}]
                },{
            		xtype: 'label',
            		html: "<div style='padding-top:9px;padding-bottom:6px;text-align:left;'>&nbsp;</div>"
            	}]
        	},{
            	xtype: 'panel',
	        	layout : 'column',
	        	border: false, 
	        	labelSeparator: ':',
	        	defaults:{layout: 'form', border: false,columnWidth: 0.5},
	        	items: [{
                	xtype: 'panel',
                	layout : 'form',
                	bodyStyle:'padding-top:9px;padding-bottom:6px;text-align:left;',
                	items:[{
                		xtype: 'textfield', inputType: 'password', id: 'newpwdconfirm', name: 'newpwdconfirm', anchor: this.fieldAnchor,
                		fieldLabel: Common.config.redStar + '密码确认', readOnly: false, allowBlank: true, vtype:'password', vtypeText:'两次密码不一致', confirmTo:'newpwd'
                	}]
                },{
            		xtype: 'label',
            		html: "<div style='padding-top:9px;padding-bottom:6px;text-align:left;'>&nbsp;</div>"
            	},{
            		xtype:'hidden',name:"user.provincenumber",id:"provincenumber"
            	},{
            		xtype:'hidden',name:"user.gender",id:"gender",value:"1"
            	}]
        	}]
		});
		/**
		if (this.inputWindow == null) {
			this.inputWindow = new Ext.Window({ //定义对话框
				//title: '新用户注册',
				//closeAction : 'hide',
				//modal : true,
				shadow : true,
				closable : false,
				draggable : false,
				layout : 'fit',
				//contentEl : 'reg-form',
				width : 800,
				height : 300,
				items:[this.inputForm],
				buttons : [{
					text :'注册',
					iconCls: 'icon_save',
					scope: this,
					handler: this.onSaveUser
				},{
					text :'返回主页',
					iconCls: 'icon_close',
					scope: this,
					handler: function(){ window.location = sys.basePath;}
				}]
			});
		}*/
		this.inputForm.getForm().reset();
		this.inputForm.render("reg-form");
		//this.inputWindow.show();
//		this.inputForm().doLayout(true);
	}, 
	onSaveUser	: function() {
		var thisForm = this.inputForm.getForm();
		if (!thisForm.isValid()) {
			return;
		}
		var newpwd 				= thisForm.findField("newpwd");
		var newpwdconfirmCmp 	= thisForm.findField("newpwdconfirm");
		if (newpwd.getValue() != newpwdconfirmCmp.getValue()) {
			newpwdconfirmCmp.markInvalid("密码确认与新输入的密码不一致!");
			return;
		}
		
		var orgCode = thisForm.findField("citynumber");
		if(orgCode.getValue() == '0') {
			orgCode.markInvalid("请选择正确的所属组织!");
			return ;
		}
		
		Common.showProcessMsgBox("信息保存中，请稍后...");
		thisForm.submit({
			url: sys.basePath + "userAction!register.do",
			params : {optype : Common.optype.add},
            success: function(form, action){
            	Common.hideProcessMsgBox();
            	thisForm.reset();
                //Ext.topShow.msg('成功提示', action.result.msg);
                Ext.Msg.show({
   	                title: '成功提示',
   	                //msg: '您的注册信息已经提交审核，我们会将审核结果发送至您的邮箱，请注意查收。',
   	                msg: '注册成功，请登录。',
   	                width: 180,
   	                modal: false,
   	                buttons : Ext.Msg.OK
   	            });
            },
            failure : function (form, action) {
            	Common.hideProcessMsgBox();
            	var failMsg = Ext.isEmpty(action.result) ? "用户注册失败." : action.result.msg;
        	 	Ext.Msg.show({
   	                title: '失败提示',
   	                msg: failMsg || '用户注册失败.',
   	                width: 180,
   	                modal: false,
   	                buttons : Ext.Msg.OK,
   	                icon: Ext.MessageBox.ERROR
   	            });
            }
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