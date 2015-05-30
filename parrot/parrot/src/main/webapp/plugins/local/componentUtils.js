function getButton(attrObj) {
	var btn = new Ext.Button();
	for ( var attr in attrObj) {
		btn[attr] = attrObj[attr];
	}
	return btn;
}

function getQueryBtn(queryHandler) {
	var queryBtn = new Object();

	queryBtn.id = "queryBtn";
	queryBtn.iconCls = "slik-search";
	queryBtn.text = "查询";
	queryBtn.xtype = "button";
	queryBtn.minWidth = 80;
	queryBtn.handler = queryHandler;

	return queryBtn;
}

function getSaveBtn(saveHandler) {
	var saveBtn = new Object();

	saveBtn.id = "saveBtn";
	saveBtn.iconCls = "silk-save";
	saveBtn.text = "保存";
	saveBtn.xtype = "button";
	saveBtn.minWidth = 80;
	saveBtn.handler = saveHandler;

	return saveBtn;
}

function getHidden(id, value) {
	var txt = new Object();

	txt.id = id;
	txt.xtype = "hidden";
	
	if(!Ext.isEmpty(value)) {
		txt.value = value;
	}
	
	return txt;
}

function setTextReadOnly(component) {
	component.el.dom.readOnly = true;
}

function setTextEditable(component) {
	component.el.dom.readOnly = false;
}

function getText(id, text, width) {
	var txt = new Object();

	txt.id = id;
	txt.width = width;
	txt.fieldLabel = text;
	txt.xtype = "textfield";

	return txt;
}

function getNumberText(id, text, width) {
	var txt = getText(id, text, width);

	txt.vtype = "number";
	txt.vtypeText = Validate.number;

	return txt;
}

function getMailText(id, text, width) {
	var txt = getText(id, text, width);

	txt.vtype = "email";
	txt.vtypeText = Validate.eMail;
	return txt;
}

function getMoneyText(id, text, width) {
	var txt = getText(id, text, width);

	txt.vtype = "money";
	txt.vtypeText = Validate.money;
	return txt;
}

function getPwdText(id, text, width) {
	var txt = getText(id, text, width);

	txt.inputType = "password";
	txt.vtype = "pwd";
	txt.vtypeText = Validate.pwd;
	return txt;
}

function getRPwdText(id, text, width, confirmTo) {
	var txt = getPwdText(id, text, width);

	txt.vtype = "rpwd";
	txt.vtypeText = Validate.rpwd;
	txt.confirmTo = confirmTo;
	return txt;
}

function getTextArea(id, text, width, height, maxLength) {
	var txt = getText(id, text, width);

	txt.height = height;
	txt.xtype = "textarea";
	txt.maxLength = maxLength || 500;
	
	return txt;
}

function getComboBox(id, text, width, array, isReadonly) {
	
//	if(array != null && array.length > 1) {
//		array.unshift(["","全部"]);
//	}
	
	var select = new Object();

	select.id = "show_" + id;
	select.hiddenName = id;
	select.width = width;
	select.fieldLabel = text;
	select.readOnly = isReadonly;

	select.xtype = "combo";
	select.valueField = "value";
	select.displayField = "text";
	select.mode = "local";
	select.editable = false;
	select.triggerAction = "all";
	select.resizable = true;
	select.emptyText = "---请选择---";
	select.value = "";
	select.store = new Ext.data.SimpleStore( {
		fields : [ 'value', 'text' ],
		data : array
	});

	return select;
}

function getCheckbox(id, text, isChecked) {
	var checkbox = new Object();

	checkbox.xtype = "checkbox";
	checkbox.boxLabel = text;
	checkbox.id = id;
	checkbox.checked = isChecked || false;

	return checkbox;
}

function getDateText(id, text, width, initValue, maxValue) {
	var date = new Object();
	
	date.xtype = "datefield";
	date.format="Y-m-d";
//	date.emptyText="请选择日期";
	 
	date.fieldLabel = text;
	date.id = id;
	date.width = width;
//	date.value = initValue || new Date().add(Date.DAY, 1);
	date.maxValue = maxValue || new Date().add(Date.DAY, 1);
	date.value = initValue;

	return date;
}
