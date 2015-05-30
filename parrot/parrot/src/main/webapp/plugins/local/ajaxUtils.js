/**
 * 对EXT的ajax类进行封装，提供数据请求，状态请求，表单提交三个方法
 * @param {Object} url
 * @param {Object} timeout
 * @memberOf {TypeName} 
 */
function AjaxUtil(url, timeout) {
	this.ajaxUrl = url;
	this.timeout = timeout || 120000;
}

AjaxUtil.prototype.getData = function(params, scope, callback) {
	Message.showProcessMsgBox();

	Ext.Ajax.request( {
		url : this.ajaxUrl,
		timeout : this.timeout,
		params : params,
		method : "POST",
		success : function(response, options) {
			var result = Ext.util.JSON.decode(response.responseText);
			Message.hideProcessMsgBox();

			if (result.success) {

				if (callback != null) {
					callback(scope, result.obj);
				}

			} else {
				var failMsg = Ext.isEmpty(result.message) ? "数据处理时发生异常."
						: result.message;
				Message.showErrorMsgBox(failMsg);
			}
		},
		failure : function(response, options) {
			Message.hideProcessMsgBox();
			var result = Ext.util.JSON.decode(response.responseText);

			var failMsg = Ext.isEmpty(result) ? "数据处理时发生异常." : result;
			Message.showErrorMsgBox(failMsg);
		}
	});
};

AjaxUtil.prototype.request = function(params, showMsg, scope, callback) {
	if (showMsg) {
		Message.showProcessMsgBox();
	}

	Ext.Ajax.request( {
		url : this.ajaxUrl,
		timeout : this.timeout,
		params : params,
		method : "POST",
		success : function(response, options) {
			var result = Ext.util.JSON.decode(response.responseText);
			if (showMsg) {
				Message.hideProcessMsgBox();
			}

			if (result.success) {
				TopShow.msg("成功提示", result.message);

				if (callback != null) {
					callback(scope);
				}
			} else {
				var failMsg = Ext.isEmpty(result.message) ? "数据处理时发生异常."
						: result.message;
				Message.showErrorMsgBox(failMsg);
			}
		},
		failure : function(response, options) {
			if (showMsg) {
				Message.hideProcessMsgBox();
			}
			var result = Ext.util.JSON.decode(response.responseText);

			var failMsg = Ext.isEmpty(result) ? "数据处理时发生异常." : result;
			Message.showErrorMsgBox(failMsg);
		}
	});
};

AjaxUtil.prototype.submit = function(thisForm, params, showMsg, scope, callback) {
	if (showMsg) {
		Message.showProcessMsgBox();
	}

	thisForm.submit( {
		url : this.ajaxUrl,
		timeout : this.timeout,
		params : params,
		method : "POST",
		success : function(form, action) {
			if (showMsg) {
				Message.hideProcessMsgBox();
			}
			TopShow.msg("成功提示", action.result.message);

			// 调用回调函数
			if(callback != null) {
				
				if(!Ext.isEmpty(action.result.obj)) {
					callback(scope, action.result.obj);
				} else {
					callback(scope);
				}
			}
		},
		failure : function(form, action) {
			if (showMsg) {
				Message.hideProcessMsgBox();
			}
			var failMsg = Ext.isEmpty(action.result.message) ? "数据处理时发生异常."
					: action.result.message;
			Message.showErrorMsgBox(failMsg);
		}
	});
};
