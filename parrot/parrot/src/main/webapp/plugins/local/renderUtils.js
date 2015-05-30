var Render = {
	// 字典转化
	dictRender : function(dictArray, dictValue) {
		if (Ext.isEmpty(dictArray) || dictArray.length == 0) {
			return dictValue;
		}
		if (Ext.isEmpty(dictValue)) {
			return "";
		}
		for ( var i = 0; i < dictArray.length; i++) {
			var arr = dictArray[i];
			if (arr[0] == dictValue) {
				return arr[1];
			}
		}
		return dictValue;
	},
	// 日期转化
	dateRender : function(value) {
		if (Ext.isEmpty(value)) {
			return "";
		}
		var reDate = /\d{4}\-\d{2}\-\d{2}/gi;
		return value.match(reDate);
	},
	// 时间转化
	timeRender : function(value) {
		if (Ext.isEmpty(value)) {
			return "";
		}
		var reDate = /\d{4}\-\d{2}\-\d{2}/gi;
		var reTime = /\d{2}:\d{2}:\d{2}/gi;
		return value.match(reDate) + " " + value.match(reTime);
	},
	// 详细信息转化
	detailRender : function(value, cm, colIndex, length) {
		var _maxLength = 20;
		
		if(cm == null && !Ext.isEmpty(length)) {
			_maxLength = length;
		} else if(cm != null) {
			_maxLength = cm.getColumnWidth(colIndex) / 7;
		}
		
		if (Ext.isEmpty(value) || value.length <= _maxLength) {
			return value;
		}
		var valueDisplay = value.length > _maxLength ? value.substring(0,
				_maxLength)
				+ "..." : value;
		var contentEl = "content_" + Math.random();
		return '<a title="'
				+ value
				+ '" href="javascript: void(0);" onclick="javascript: Render.viewFieldDetail(\''
				+ contentEl + '\');">' + valueDisplay + '</a><div id="'
				+ contentEl + '" style="display: none;">' + value + '</div>';
	},
	viewFieldDetail : function(contentEl, title) {
		if (this.viewFieldDetailWin == null) {
			this.viewFieldDetailWin = new Ext.Window( { //定义对话框
						title : title || '详细信息',
						closeAction : 'hide',
						modal : true,
						shadow : true,
						closable : true,
						layout : 'fit',
						width : 380,
						height : 200,
						items : [ {
							xtype : 'textarea',
							id : 'detailField'
						} ]
					});
		}
		this.viewFieldDetailWin.show();
		this.viewFieldDetailWin.doLayout();
		Ext.getCmp("detailField").setValue(
				document.getElementById(contentEl).innerHTML);
	}
};