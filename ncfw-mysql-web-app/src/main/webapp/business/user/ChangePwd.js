var ChangePwd = {
	inputForm		: null,		//密码修改表单
	inputWindow		: null,		//密码修改对话框
	fieldAnchor		: Common.config.fieldAnchor,
	onChangePwd		: function () {
		if (dataGrid.getSelectionModel().getCount() != 1) {
            Ext.Msg.alert("提示信息", "请选择一条记录!");
            return;
        }
        ChangePwd.showInputWindow();
        var selectedRecord = dataGrid.getSelectionModel().getSelected();
        var values = {
        	userid	: selectedRecord.get("userid")
        };
        var thisForm = ChangePwd.getInputForm().getForm();
        thisForm.reset();
        thisForm.setValues(values);
	},
	getInputForm	: function () {
		if (this.inputForm != null) {
			return this.inputForm;
		}
		this.inputForm = new Ext.FormPanel({
			id: 'changePwdInputForm',
			region: 'center',
			autoScroll: true,
	        frame: true,
	        labelAlign: 'right',
	        labelWidth: 66,	        
         	items: [{
            	items: [{
	            	layout: 'column',border: false, labelSeparator: ':',
	                defaults:{layout: 'form', border: false, columnWidth: 1.0},
	                items: [{
	                	items:[{
	                		xtype: 'hidden', id: 'pwd.userid', name: 'userid'
	                	}]
	                },{
	                	items:[ {
	                		xtype: 'textfield', inputType: 'password', id: 'pwd.newpwd', name: 'newpwd', anchor: this.fieldAnchor,
	                		fieldLabel: Common.config.redStar + '新密码', readOnly: false, allowBlank: false
	                	}]
	                },{
	                	items:[ {
	                		xtype: 'textfield', inputType: 'password', id: 'pwd.newpwdconfirm', name: 'newpwdconfirm', anchor: this.fieldAnchor,
	                		fieldLabel: Common.config.redStar + '密码确认', readOnly: false, allowBlank: false
	                	}]
	                }]
            	}]
         	}]
		});
		return this.inputForm;
	},
	showInputWindow : function () {
		var windowWidth = document.body.clientWidth > 460 ? 460 : document.body.clientWidth - 10;
		var windowHeight = document.body.clientHeight > 160 ? 160 : document.body.clientHeight - 10;
		if (this.inputWindow == null) {
			var inputForm = ChangePwd.getInputForm();
			this.inputWindow = new Ext.Window({ //定义对话框
				title: '密码修改',
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
					handler: this.onSavePwd
				},{
					text :'关闭',
					iconCls: 'icon_close',
					scope: this,
					handler: function(){ this.inputWindow.hide();}
				}]
			});
		}
		this.inputWindow.show();
	},
	onSavePwd : function () {
		var thisForm = ChangePwd.getInputForm().getForm();
		if (!thisForm.isValid()) {
			return;
		}
		var newpwd 				= thisForm.findField("pwd.newpwd");
		var newpwdconfirmCmp 	= thisForm.findField("pwd.newpwdconfirm");
		if (newpwd.getValue() != newpwdconfirmCmp.getValue()) {
			newpwdconfirmCmp.markInvalid("密码确认与新输入的密码不一致!");
			return;
		}
		Common.showProcessMsgBox("信息保存中，请稍后...");
		thisForm.submit({
			url: sys.basePath + "userAction!updateUserPwd.do",
            success: function(form, action){
            	Common.hideProcessMsgBox();
                Ext.topShow.msg('成功提示', '密码修改成功.');
                //ChangePwd.inputWindow.hide();
            }
        });
	}
};