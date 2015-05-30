function WindowUtil(winAttr, form, saveHandler) {
	
	this.saveBtn = getButton({
		text : "保存",
		iconCls : "silk-accept",
		scope : this,
		handler : saveHandler
	});
	
	this.win = new Ext.Window( {
		closeAction : "hide",
		modal : true,
		shadow : true,
		closable : true,
		layout : "fit",
		width : 350,
		height : 210,
		items : [ form ],
		buttonAlign : "center",
		buttons : [ this.saveBtn, {
			text : "关闭",
			iconCls : "slik-close",
			scope : this,
			handler : function() {
				this.win.hide();
			}
		} ]
	});
	for ( var attr in winAttr) {
		this.win[attr] = winAttr[attr];
	}
}

WindowUtil.prototype.show = function(title) {
	if(!Ext.isEmpty(title)) {
		this.win.setTitle(title);
	}
	this.win.show();
};

WindowUtil.prototype.hide = function() {
	this.win.hide();
};

WindowUtil.prototype.getWindow = function() {
	return this.win;
};

WindowUtil.prototype.hideSaveBtn = function() {
	return this.saveBtn.hide();
};

WindowUtil.prototype.showSaveBtn = function() {
	return this.saveBtn.show();
};