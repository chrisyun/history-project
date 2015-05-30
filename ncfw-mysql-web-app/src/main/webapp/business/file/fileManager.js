var FileConstant = {
	uploadForm		: null,
	uploadWindow	: null,	
	uploadUrl		: sys.basePath	+ 'fileAction!uploadFile.do',//上传地址
	downloadUrl		: sys.basePath	+ 'fileAction!downloadFile.do',//下载地址
	deleteUrl		: sys.basePath	+ 'fileAction!deleteFiles.do',//下载地址
	queryByPidUrl	: sys.basePath	+ 'fileAction!queryFiles.do',
	fileManager		: null,
	deleteFile		: function(attid) {
		Common.ajaxRequest({
			url				: FileConstant.deleteUrl, 
			params			: {attId:attid},
			proccessMsg		: '文件删除中, 请稍后...', 
			successHandler	:  function () {
					Ext.getDom(attid).innerHTML = "";
					Ext.getDom(attid).removeNode();
					
					var thisForm = FileConstant.uploadForm.getForm();
					var attch = thisForm.findField('attach').getValue().split(attid+",");
					thisForm.findField('attach').setValue(attch[0]+attch[1]);
			}
		});
	},
	uploadFile		: function() {
		var thisForm = FileConstant.uploadForm.getForm();
		if (!thisForm.isValid()) {
			return;
		}
		
		var fileValue = Ext.getCmp('uploadFile').getValue();
		if (fileValue == null || fileValue.length <= 0) {
			Ext.Msg.alert("提示信息", "请选择需上传的文件.");
			return;
		}
		Common.showProcessMsgBox("文件上传中，请稍后...");
		
		var obj = FileConstant.fileManager;
		
		thisForm.submit({
			url		: FileConstant.uploadUrl,
            success	: FileConstant.uploadSuccess,
            params 	: {pid : obj.table_pid,tableid:obj.table_id,busatttype:obj.busatttype},
            failure : function (form, action) {
            	Ext.MessageBox.hide();
				Ext.Msg.show({
	                title : '失败提示',
	                minWidth: 180,
	                msg : action.result.msg || "文件上传失败！",
	                buttons : Ext.Msg.OK,
	                icon: Ext.MessageBox.ERROR
	            });
            }
        });
	},
	uploadSuccess 	: function(form, action) {
		FileConstant.uploadForm.getForm().findField('uploadFile').reset();
		Ext.MessageBox.hide();
		FileConstant.fileManager.addFile2Panel(action.result.attid, action.result.attname);
		Ext.Msg.show({
            title : '成功提示',
            minWidth: 180,
            msg : "文件上传成功！",
            buttons : Ext.Msg.OK,
            icon: Ext.MessageBox.SUCCESS
        });
	},
	initUploadWin	: function() {
		if(FileConstant.uploadForm == null) {
			FileConstant.uploadForm = new Ext.FormPanel({
		        id	: 'fileUploadForm',
				fileUpload: true,
		        border: false,
		        frame: true,
		        labelWidth: 58,
		        bodyStyle: 'padding:5px; overflow-x:visible',
		        labelAlign: 'right',
		        layout: 'form',
		        items: [{
			    	xtype: 'fileuploadfield', allowBlank: false, id: 'uploadFile',
			    	name: 'uploadFile', fieldLabel: '文件',
			    	emptyText: '请选择需上传的文件', blankText: '文件不能为空', anchor: '92%',
			    	buttonCfg: {
			    		text: '...'
			    	}
		    	},{
		    		xtype: 'hidden',id: 'attach',name: 'attach'
		    	}]
		    })
		}
		if (FileConstant.uploadWindow == null) {
			var upForm = FileConstant.uploadForm;
			
			FileConstant.uploadWindow = new Ext.Window({ //定义对话框
				title: '文件上传',closeAction : 'hide',shadow : true,
				closable : true,layout : 'fit',width : 436,height : 126,
				items:[upForm],
				buttons : [{
					text :'上传',
					iconCls: 'icon_sure',
					handler: FileConstant.uploadFile
				},{
					text :'关闭',
					iconCls: 'icon_close',
					handler:function(){FileConstant.uploadWindow.hide();}
				}]
			});
		}
		FileConstant.uploadWindow.show();
		FileConstant.uploadWindow.hide();
	},
	showUploadWindow	: function() {
		FileConstant.initUploadWin();
		FileConstant.uploadWindow.show();
	}
}

function FileManager(panel_id,table_pid,table_id,editMode,busatttype) {
	this.table_pid 	= table_pid;
	this.table_id 	= table_id;
	this.editMode 	= editMode;
	this.busatttype = busatttype;
	this.panel_id	= panel_id;
	FileConstant.fileManager = this;
	//初始化
	FileConstant.initUploadWin();
	return this;
}

FileManager.prototype.search = function() {
	Common.showProcessMsgBox('附件信息获取中, 请稍候...');
	var nowObj = this;
	Ext.Ajax.request({
		url 	: FileConstant.queryByPidUrl,
	   	method 	: 'post',
	   	params 	: {pid : this.table_pid,tableid: this.table_id,busatttype:this.busatttype},
	   	success	: function (response, options) {
			Ext.MessageBox.hide();
			var fileJson = Ext.util.JSON.decode(response.responseText);
			for ( var i = 0; i < fileJson.length; i++) {
				var file = fileJson[i];
				nowObj.addFile2Panel(file.attid,file.attname);
			}
	   	}
	});
};

FileManager.prototype.addFile2Panel = function(attid,attName) {
	var buff = '<span id="'+attid+'" style="display: block; margin: 2 10 0 10px;"><span style="width:550px;"><a href="' + 
		FileConstant.downloadUrl + '?downAttid=' + attid + '" title="下载"  target="_blank">' + attName + 
	   	'</a></span>&nbsp;&nbsp;&nbsp;&nbsp;';
	if(this.editMode == 'edit') {
		buff +=  '<a href="javascript: void(0);" onclick="javascript: FileConstant.deleteFile(\'' 
			+ attid + '\');" title="删除">【删除】</a>';
	} 
	buff += '</span><br>';
	
	if(Ext.isEmpty(this.table_pid)) {
		var thisForm = FileConstant.uploadForm.getForm();
		thisForm.findField('attach').setValue(thisForm.findField('attach').getValue() + attid + ",");
	}
	
	Ext.getDom(this.panel_id + "_text").innerHTML += buff;
};

function json2Str(o) {
    if (o == undefined) {
        return "";
    }
    var r = [];
    if (typeof o == "string") return "\"" + o.replace(/([\"\\])/g, "\\$1").replace(/(\n)/g, "\\n").replace(/(\r)/g, "\\r").replace(/(\t)/g, "\\t") + "\"";
    if (typeof o == "object") {
        if (!o.sort) {
            for (var i in o)
                r.push("\"" + i + "\":" + json2Str(o[i]));
            if (!!document.all && !/^\n?function\s*toString\(\)\s*\{\n?\s*\[native code\]\n?\s*\}\n?\s*$/.test(o.toString)) {
                r.push("toString:" + o.toString.toString());
            }
            r = "{" + r.join() + "}"
        } else {
            for (var i = 0; i < o.length; i++)
                r.push(json2Str(o[i]))
            r = "[" + r.join() + "]";
        }
        return r;
    }
    return o.toString().replace(/\"\:/g, '":""');
}