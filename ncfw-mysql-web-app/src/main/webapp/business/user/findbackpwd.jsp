<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/foundation/ext/jsp/taglib.jsp"%>
<%@ include file="/foundation/ext/jsp/workspace.jsp"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<title>密码找回</title>
	<link rel="stylesheet" type="text/css" href="<%=basePath%>foundation/ext/css/style.css" />
	<script type="text/javascript">

	var inputForm = new Ext.FormPanel({
		id: 'inputForm',
		region: 'center',
		border:false,
		width : 700,
		height : 250,
		bodyStyle: 'overflow-x:visible;',
        labelAlign: 'right',
        labelWidth: 150,
        buttonAlign:'center',  
        buttons : [{
			text :'确定',
			scope: this,
			handler: findPwd
		},{
			text :'返回主页',
			scope: this,
			handler: function(){ window.location = sys.basePath;}
		}],
        items: [{
    		xtype: 'panel',
    		//title: '基本信息',
    		autoHeight: true,
        	items: [{
            	layout: 'column',border: false, labelSeparator: ':',
                defaults:{layout: 'form', border: false, columnWidth: 0.5,bodyStyle:'padding-top:10px;padding-bottom:7px;'},
                items: [{
                	columnWidth: 1.0,
                	items:[{
                		xtype: 'combo', id: 'type', hiddenName: 'findtype',displayField: 'text',
                		fieldLabel: '信息类型', value: 'userid',triggerAction: 'all', valueField: 'value',
                		readOnly: false, resizable: true,mode: 'local',
		                store: new Ext.data.SimpleStore({
							fields: ['value', 'text'], data: [['userid','用户名'],['mail','邮箱']]
						})
                	}]
                },{
	             	items:[{
	             		xtype: 'textfield', id: 'findkey', name: 'findkey', anchor: Common.config.fieldAnchor,
	             		fieldLabel: Common.config.redStar + '关键信息', readOnly: false, allowBlank: false
	               	}]
	            },{
	            	columnWidth: 1.0,
	            	html : '<br><strong style="color: red;padding-left:90px;">2012年度申报工作中已注册用户请填写您注册的用户名或邮箱，我们会将密码发送至您的邮箱，请注意查收。<br><div style="color: red;padding-left:90px;">未参加2012年度申报，本单位用户名已被注册，请与文化部信息中心联系，电话：01059881182，联系人：邢波。</div></strong>'
				
			    }]
                }]
        	}]
	});  
	
	function findPwd() {
		var thisForm = inputForm.getForm();
		if (!thisForm.isValid()) {
			return;
		}

		var params = {findtype:thisForm.findField('findtype').getValue(),findkey:thisForm.findField('findkey').getValue()};
		
		Common.ajaxRequest({
			url: sys.basePath + 'userAction!forgetPwd.do', params: params,
			proccessMsg: '信息处理中, 请稍后...', successHandler: function () {thisForm.reset();}
		});
		
	}

   	Ext.onReady(function() {
		Ext.QuickTips.init();

		inputForm.getForm().reset();
		inputForm.render("findpwd-form");
	});
	
    </script>
</head>
	<body>
			<div id="page">
				<div class="header"></div>
				<div class="main">
					<div class="reg-box">
						<h3>
							密码找回
						</h3>
						<div class="reg-form" id="findpwd-form" style="padding-left:140px;"></div>
					</div>
					<div class="dot"></div>
				</div>
				<div class="foot">
					版权所有：中华人民共和国文化部
				</div>
			</div>
	</body>
  
</html>
