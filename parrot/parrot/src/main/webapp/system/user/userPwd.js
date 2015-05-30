var changePwd = sys.path + "/user/changePwd.jmt";
var changeMyPwd = sys.path + "/user/changeMyPwd.jmt";

var pwdUrl;
var type;

var UserPwd = function(_type) {
	type = _type;
	if (type == "my") {
		pwdUrl = changeMyPwd;
	} else {
		pwdUrl = changePwd;
	}

	this.form = new FormUtil( {
		id : "pwdForm",
		collapsible : false,
		labelWidth : 80,
		border : false
	});
	this.form.addComp(getPwdText("newPwd", "密码", 200), 1.0, false);
	this.form.addComp(getRPwdText("newrPwd", "重复密码", 200, "newPwd"),
		1.0, false);
	this.form.addComp(getHidden("userId"), 0, true);

	var win_attr = {
		width : 340,
		height : 150
	};
	if (type == "my") {
		win_attr.closable = false;
		win_attr.draggable = false;
	}

	this.win = new WindowUtil(win_attr, this.form.getPanel(), changePwdHandler);

	function changePwdHandler() {
		// 此处this指windowUtil对象
		var window = this.win;

		var form = window.getComponent("pwdForm").getForm();

		if (!form.isValid()) {
			Ext.Msg.alert("提示信息", "请填写完整的信息!");
			return;
		}

		var callBack_obj = new Object();
		callBack_obj.win = this;
		var ajaxClass = new AjaxUtil(pwdUrl);
		ajaxClass.submit(form, null, true, callBack_obj, function(obj) {
			if (type != "my") {
				obj.win.hide();
			}
		});
	}
};

UserPwd.prototype.show = function(uid) {
	this.form.getForm().reset();
	this.form.findById("userId").setValue(uid);
	this.win.show("修改密码");
};
