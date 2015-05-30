var uploadUrl = sys.path + "/att/getUpload.jmt";
var queryAttsUrl = sys.path + "/att/getAtts.jmt";
var downloadUrl = sys.path + "/att/download.jmt";
var removeAttsUrl = sys.path + "/att/delete.jmt";

function UploadUtil(id, type) {
	this.id = id;
	this.type = type || "read";

	this.uploadForm = new Ext.FormPanel( {
		fileUpload : true,
		border : false,
		autoHeight : true,
		bodyStyle : "padding: 10px 10px 0 10px;",
		labelWidth : 80,
		labelAlign : "right",
		height : 130,
		defaults : {
			anchor : "95%",
			allowBlank : false,
			blankText : Validate.empty
		},
		items : [ {
			xtype : "hidden",
			id : id + "_ids",
			name : "fileIds"
		}, {
			xtype : "textfield",
			id : id + "_name",
			name : "fileName",
			fieldLabel : "文件名"
		}, {
			xtype : "fileuploadfield",
			id : id,
			emptyText : "请选择需要上传的文件",
			fieldLabel : "附件",
			name : "attach",
			buttonText : "",
			buttonCfg : {
				iconCls : "upload-icon"
			}
		} ],
		buttonAlign : "center",
		buttons : [ {
			text : "上传",
			iconCls : "silk-accept",
			scope : this,
			handler : upload
		}, {
			text : "删除",
			iconCls : "silk-delete",
			id : id + "_delBtn",
			scope : this,
			handler : remove
		}, {
			text : "关闭",
			iconCls : "slik-close",
			scope : this,
			handler : function() {
				this.uploadWin.hide();
			}
		} ]
	});

	this.uploadForm.findById(id).addListener(
			"fileselected",
			function(field, value) {
				var file_name = value.substring(value.lastIndexOf("\\") + 1,
						value.length);
				this.uploadForm.getForm().findField("fileName").setValue(
						file_name);
			}, this);

	var util = this;

	function remove() {
		// 移除下拉列表中的数据
		var multiSel = this.selectPanel.findById(this.id + "_multiSel");
		var store = this.selectPanel.findById(this.id + "_multiSel").store;
		var ids = multiSel.getValue();

		if (Ext.isEmpty(ids)) {
			return;
		}
		
		var ajax = new AjaxUtil(removeAttsUrl);
		ajax.request({
					"ids" : ids
				}, true, util, function(obj) {
			var idsArray = ids.split(",");
			for ( var i = 0; i < idsArray.length; i++) {
				obj.removeAtt(idsArray[i]);
			}
	
			var selectionsArray = multiSel.view.getSelectedIndexes();
			//对数组排序，先删高位的
			selectionsArray = selectionsArray.sort(sortNumber);
			for ( var i = 0; i < selectionsArray.length; i++) {
				store.removeAt(selectionsArray[i]);
			}
		});
	}
	
	// 实现反序
	function sortNumber(a, b) {
		return b - a;
	}
	
	
	function upload() {
		if (!this.uploadForm.getForm().isValid()) {
			return;
		}

		this.uploadForm.getForm().submit( {
			url : uploadUrl,
			timeout : 600000,
			method : "POST",
			success : function(form, action) {
				Message.hideProcessMsgBox();
				TopShow.msg("成功提示", action.result.message);

				var att = new Object();
				att.id = action.result.obj.id;
				att.name = action.result.obj.name;

				util.addAtt(att);
			},
			failure : function(form, action) {
				Message.hideProcessMsgBox();
				var failMsg = Ext.isEmpty(action.result.message)
				   		 ? "文件上传失败." : action.result.message;
				Message.showErrorMsgBox(failMsg);
			}
		});
	}

	this.selectPanel = new Ext.Panel( {
		border : false,
		autoHeight : true,
		layout : "form",
		items : [ {
			xtype : "multiselect",
			name : 'uploadAtts',
			draggable : false,
			id : id + "_multiSel",
			width : 390,
			height : 130,
			hideLabel : true,
			store : [ [] ]
		} ]
	});

	this.uploadWin = new Ext.Window( {
		closeAction : "hide",
		modal : true,
		shadow : true,
		closable : true,
		layout : "fit",
		width : 400,
		autoHeight : true,
		items : [ this.uploadForm, this.selectPanel ]
	});
}

UploadUtil.prototype.initShowPanel = function(attrObj, ids) {
	this.showPanel = new Ext.Panel( {
		id : this.id + "_show",
		autoHeight : true,
		autoWidth : true,
		html : "<div id='" + this.id + "_show_div'></div>"
	});

	for ( var attr in attrObj) {
		this.showPanel[attr] = attrObj[attr];
	}
	
	this.loadAtt(ids);

	return this.showPanel;
};

UploadUtil.prototype.loadAtt = function(ids) {
	if (!Ext.isEmpty(ids)) {
		var util = this;
		
		var ajax = new AjaxUtil(queryAttsUrl);
		ajax.getData( {
			"ids" : ids
		}, this, function(obj, result) {
			if (result == null) {
				return;
			}

			for ( var i = 0; i < result.length; i++) {
				var att = new Object();
				att.id = result[i].id;
				att.name = result[i].fileName;
				
				util.addAtt(att);
			}
		});
	}
};

UploadUtil.prototype.show = function(title) {
	this.uploadWin.setTitle(title);
	this.uploadWin.show();
	if(this.type == "read") {
		var multiSel = this.selectPanel.findById(this.id + "_multiSel");
		multiSel.disable();
		
		var delBtn = Ext.getCmp(this.id + "_delBtn");
		delBtn.disable();
	}
	
	
};

UploadUtil.prototype.addAtt = function(att) {
	var hiddenIds = this.uploadForm.findById(this.id + "_ids").getValue();
	
	if(Ext.isEmpty(hiddenIds)) {
		hiddenIds = "";
	}
	
	this.uploadForm.findById(this.id + "_ids").setValue(
			hiddenIds + "," + att.id);

	var attHtml = "<div id='" + this.id + "_" + att.id + "' class='uploadArea'><a href=\""
			+ getDownloadUrl(att) + "\" title=\"下载\" target=\"_blank\">"
			+ att.name + "</a></div>";

	Ext.getDom(this.id + "_show_div").innerHTML += attHtml;

	// 向下拉列表中添加数据
	var store = this.selectPanel.findById(this.id + "_multiSel").store;
	store.loadData( [ [ att.id, att.name ] ], true);
};

UploadUtil.prototype.removeAtt = function(id) {
	var hiddenIds = this.uploadForm.findById(this.id + "_ids").getValue();
	
	var idArray = hiddenIds.split("," + id);
	this.uploadForm.findById(this.id + "_ids")
			.setValue(idArray[0] + idArray[1]);
	
	Ext.getDom(this.id + "_" + id).innerHTML = "";
	Ext.getDom(this.id + "_" + id).removeNode();
};

UploadUtil.prototype.getUploadAttIds = function() {
	return this.uploadForm.findById(this.id + "_ids").getValue();
};

function getDownloadUrl(att) {
	return downloadUrl + "?id=" + att.id;
};
