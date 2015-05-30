function FormUtil(attrObj) {

	this.panel = new Ext.FormPanel( {
		iconCls : "silk-app-go",
		bodyStyle : "padding-top: 7px;",
		collapsible : true,
		height : 100,
		margins : "2 0 0 0",
		layout : "column",
		split : true,
		labelWidth : 100,
		labelAlign : "right",
		buttonAlign : "center",
		defaults : {
			xtype : "panel",
			border : false,
			layout : "form"
		}
	});

	for ( var attr in attrObj) {
		this.panel[attr] = attrObj[attr];
	}
}

FormUtil.prototype.addButton = function(btn) {
	this.panel.addButton(btn);
};

FormUtil.prototype.addComp = function(comp, columnWidth, empty) {

	if (!empty) {
		comp.fieldLabel = Validate.redStar + comp.fieldLabel;
		comp.allowBlank = empty;
		comp.blankText = Validate.empty;
	}

	var panelItem = ( {
		columnWidth : columnWidth || 0.5,
		bodyStyle : "padding-top: 4px;",
		items : [ comp ]
	});

	this.panel.add(panelItem);
};

FormUtil.prototype.getPanel = function() {
	return this.panel;
};

FormUtil.prototype.getForm = function() {
	return this.panel.getForm();
};

FormUtil.prototype.findById = function(id) {
	return this.panel.findById(id);
};
