var Message = {
	showProcessMsgBox : function(_msg) {
		var msg = _msg || "数据提交中，请等候...";
		Ext.MessageBox.show({
					msg : '<div style="margin: 5 5 8 15px;">' + msg + '</div>'
							+ '<div style="margin-left: 8px;"><img src="'
							+ sys.path
							+ '/resources/images/local/loading.gif"/></div>',
					width : 300,
					progress : false,
					wait : false,
					closable : true
				});
	},
	hideProcessMsgBox : function() {
		Ext.MessageBox.hide();
	},
	showErrorMsgBox : function(failMsg, title) {
		Ext.Msg.show({
					title : title || "失败提示",
					msg : failMsg || "对不起，您的操作失败.",
					modal : false,
					buttons : Ext.Msg.OK,
					icon : Ext.MessageBox.ERROR
				});
	},
	debugJsonDetail : function(data) {
		if (data == undefined) {
			return "";
		}
		var r = [];
		if (typeof data == "string")
			return "\""
					+ data.replace(/([\"\\])/g, "\\$1").replace(/(\n)/g, "\\n")
							.replace(/(\r)/g, "\\r").replace(/(\t)/g, "\\t")
					+ "\"";
		if (typeof data == "object") {
			if (!data.sort) {
				for (var i in data)
					r.push("\"" + i + "\":" + Message.debugJsonDetail(data[i]));
				if (!!document.all
						&& !/^\n?function\s*toString\(\)\s*\{\n?\s*\[native code\]\n?\s*\}\n?\s*$/
								.test(data.toString)) {
					r.push("toString:" + data.toString.toString());
				}
				r = "{" + r.join() + "}";
			} else {
				for (var i = 0; i < data.length; i++)
					r.push(Message.debugJsonDetail(data[i]));
				r = "[" + r.join() + "]";
			}
			return r;
		}
		return data.toString().replace(/\"\:/g, '":""');
	}
};


TopShow = function() {
	var msgCt;
	function createBox(t, s) {
		return [
				'<div class="msg">',
				'<div class="x-box-tl"><div class="x-box-tr"><div class="x-box-tc"></div></div></div>',
				'<div class="x-box-ml"><div class="x-box-mr"><div class="x-box-mc"><h3>',
				t,
				'</h3>',
				s,
				'</div></div></div>',
				'<div class="x-box-bl"><div class="x-box-br"><div class="x-box-bc"></div></div></div>',
				'</div>'].join('');
	}
	return {
		msg : function(title, format) {
			if (!msgCt) {
				msgCt = Ext.DomHelper.insertFirst(document.body, {
							id : "msg-div"
						}, true);
			}
			msgCt.alignTo(document, "t-t");
			var s = String.format.apply(String, Array.prototype.slice.call(
							arguments, 1));
			var m = Ext.DomHelper.append(msgCt, {
						html : createBox(title, s)
					}, true);
			m.slideIn("t").pause(1).ghost("t", {
						remove : true
					});
		},
		init : function() {
			var t = Ext.get("exttheme");
			if (!t) {
				return;
			}
			var theme = Cookies.get("exttheme") || "aero";
			if (theme) {
				t.dom.value = theme;
				Ext.getBody().addClass("x-" + theme);
			}
			t.on("change", function() {
						Cookies.set("exttheme", t.getValue());
						setTimeout(function() {
									window.location.reload();
								}, 1200);
					});
			var lb = Ext.get("lib-bar");
			if (lb) {
				lb.show();
			}
		}
	};
}();
