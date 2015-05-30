/**
 * @description 公共js对象
 * @author Wuqingming
 * @version 1.0
 * @since 2011-04-07
 */
var Common = {
	config: {
		pageSize	: 15,		//页大小
		nullArray 	: [['', '请选择...']],
		redStar 	: '<em class="required">*</em>',
		buttonWidth : {
			minButton: 66,
			defaultButton: 80
		},
		fieldAnchor	: '94%',
		dfieldAnchor: '97%',
		dareaAnchor	: '96.8%'
	},
	optype: {		//系统操作类型
		add		: 'add',
		del		: 'del',
		update	: 'update',
		query	: 'query'
	},
	renderer : {
		dictRender : function (dictArray, dictValue) {		//字典转化
			if (Ext.isEmpty(dictArray) || dictArray.length == 0) {
				return dictValue;
			}
			if (Ext.isEmpty(dictValue)) {
				return "";
			}
	    	for(var i = 0;i < dictArray.length;i++){
	        	var arr = dictArray[i];
	        	if(arr[0] == dictValue) {
	        		return arr[1];
	        	}
	     	}
	     	return dictValue;
		},
		dateRender : function (value) {						//日期转化
			if (Ext.isEmpty(value)) {
				return "";
			}
		    var reDate = /\d{4}\-\d{2}\-\d{2}/gi;
		    return value.match(reDate);
		},
		timeRender : function (value) {						//时间转化
			if (Ext.isEmpty(value)) {
				return "";
			}
		    var reDate = /\d{4}\-\d{2}\-\d{2}/gi;
		    var reTime = /\d{2}:\d{2}:\d{2}/gi;
		    return value.match(reDate) + " " + value.match(reTime);
		},
		detailRender : function (value, cm, colIndex) {
			/**
			var _maxLength = Ext.isEmpty(maxLength) ? 12 : maxLength;
			*/
			var _maxLength = cm.getColumnWidth(colIndex) / 11.6;
			if (Ext.isEmpty(value) || value.length <= _maxLength) {
				return value;
			}
			var valueDisplay = value.length > _maxLength ? value.substring(0, _maxLength) + "..." : value;
			var contentEl = "content_" + Math.random();
			return '<a title="' + value + '" href="javascript: void(0);" onclick="javascript: Common.viewFieldDetail(\'' + contentEl + '\');">' + 
				valueDisplay + '</a><div id="'+ contentEl +'" style="display: none;">' + value + '</div>';
		}
	},
	viewFieldDetail : function (contentEl, title) {
		if (this.viewFieldDetailWin == null) {
			var windowWidth = document.body.clientWidth > 760 ? 760 : document.body.clientWidth - 10;
			var windowHeight = document.body.clientHeight > 560 ? 560 : document.body.clientHeight - 10;
			this.viewFieldDetailWin = new Ext.Window({ //定义对话框
				title		: title || '详细信息',
				closeAction : 'hide',
				modal 		: true,
				shadow 		: true,
				closable 	: true,
				layout 		: 'fit',
				width 		: windowWidth,
				height 		: windowHeight,
				items 		: [{
					xtype: 'textarea',
					id: 'detailField',
					anchor: Common.config.fieldAnchor
				}]
			});
		}
		this.viewFieldDetailWin.show();
		this.viewFieldDetailWin.doLayout();
		Ext.getCmp("detailField").setValue(document.getElementById(contentEl).innerHTML);
	}, 
	nullToString : function (obj) {
		if(obj == null || obj == "null"){
			return "";
		}
		return obj;
	},
    hideField : function (field) {	//隐藏控件
    	if (!field) {
    		return;
    	}
    	try {
		    field.disable();// for validation 
		    field.hide();
		    field.getEl().up('.x-form-item').setDisplayed(false); // hide label
    	} catch (e) {
    		alert("hideField error " + e);
    	}
    },
    showField : function (field) {//显示控件
    	if (!field) {
    		return;
    	}
    	try {
	        field.enable();
	        field.show();
	        field.getEl().up('.x-form-item').setDisplayed(true);// show label
    	} catch (e) {}
    },
    showProcessMsgBox : function (_msg){
		var msg = _msg || '数据提交中，请等候...';
		Ext.MessageBox.show({
	        msg: '<div style="margin: 5 5 8 15px;">' + msg + '</div>' +
	        	'<div style="margin-left: 8px;"><img src="' + sys.basePath + 'foundation/ext/images/loading.gif"/></div>',
	        width: 300,
	        progress: false,
	        wait: false,
	        closable: true
	    });
	},
    hideProcessMsgBox : function () {
    	Ext.MessageBox.hide();
	},
	/**
	 * 初始化表单数据
	 * @param {} config
	 */
	initFormPanelData : function (config) {
		var formPanel	= config.formPanel;				//请求填充的表单
		var dataUrl		= config.dataUrl;				//请求地址
		var proccessMsg	= config.proccessMsg;			//请求处理提示信息
		var params		= config.params;
		var thisForm	= formPanel.getForm();
		thisForm.reset();								//重置表单
		
		Common.showProcessMsgBox(proccessMsg || '页面渲染中, 请稍后...');			
		Ext.Ajax.request({
			url: dataUrl,
			timeout: 300000,
			params: params,
			success: function(response, options){
				Common.hideProcessMsgBox();
				var result 		= Ext.util.JSON.decode(response.responseText);
				if (result == null) return;
				thisForm.setValues(result);
			},
			failure:function(){
				Common.hideProcessMsgBox();	
				Ext.Msg.alert('提示信息', '页面渲染失败.');
			}
		});
	}
	,
	/**
	 * 初始化表单数据
	 * @param {} config
	 */
	ajaxRequest : function (requestConfig) {
		var proccessMsg		= requestConfig.proccessMsg;			//请求处理信息
		var url				= requestConfig.url;					//请求url
		var params			= requestConfig.params || {};			//请求参数
		var method			= requestConfig.method || 'POST';		//请求方式
		var successMsg		= requestConfig.successMsg;				//请求成功提示信息
		var failMsg			= requestConfig.failMsg;				//请求失败提示信息
		var successHandler	= requestConfig.successHandler;			//请求成功后钩子（方法）
		var failHandler		= requestConfig.failHandler;			//请求失败后钩子（方法）
		var errorHandler	= requestConfig.errorHandler;			//请求异常钩子（方法）
		if (Ext.isEmpty(url)) {
			Ext.Msg.alert("提示信息", "请求url不能置空.");
			return false;
		}
		if (!Ext.isEmpty(proccessMsg)) {
			Common.showProcessMsgBox(proccessMsg);
		}
	    Ext.Ajax.request({
			url : url,
		   	method : method,
		   	params : params,
		   	success: function (response, options) {
		   		if (!Ext.isEmpty(proccessMsg)) {
					Ext.MessageBox.hide();
				}
				var result = Ext.util.JSON.decode(response.responseText);
		   		if (result.success) {
		        	Ext.topShow.msg('成功提示', successMsg || result.msg || '请求处理成功.');
		        	if (successHandler != null && typeof successHandler == 'function') {
		        		successHandler(response, options);
	   		        }
			   	} else {
			   		Ext.Msg.show({
	   	                title: '失败提示',
	   	                msg: failMsg || result.msg || '请求处理失败',
	   	                width: 180,
	   	                modal: false,
	   	                buttons : Ext.Msg.OK,
	   	                icon: Ext.MessageBox.ERROR,
	   	                fn: function () {
				        	if (failHandler != null && typeof failHandler == 'function') {
				        		failHandler(response, options);
			   		        }
	   	                }
	   	            });
	   			}
		   	},
		   	failure: function () {
				Ext.MessageBox.hide();
			   	Ext.Msg.show({
	                title: '失败提示',
	                msg: '请求时发生异常.',
	                width: 180,
	                modal: false,
	                buttons : Ext.Msg.OK,
	                icon: Ext.MessageBox.ERROR,
	                fn: function () {
	                	var hasErrorHandler = false;
			        	if (errorHandler != null && typeof errorHandler == 'function') {
			        		hasErrorHandler	= true;
			        		errorHandler();
		   		        }
		   		        if (!hasErrorHandler && failHandler != null && typeof failHandler == 'function') {
			        		failHandler();
		   		        }
	                }
	            });
		   	}
		});
	},
	getMutltiSelectValues : function (selectEl, splitChar) {
		var options = selectEl.options;
		var value = "";
		var split = Ext.isEmpty(splitChar) ? "," : splitChar;
		for (var i = 0; i < options.length; i++) {
			value += split + options[i].value;
		}
		if (value.length > 0) {
			value = value.substring(split.length);
		}
		return value;
	}
};