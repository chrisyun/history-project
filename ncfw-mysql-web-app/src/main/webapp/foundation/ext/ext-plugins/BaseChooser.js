
/**
 * 基本选择器
 * @author Wuqingming
 * @since 2011-05-19
 * @type 
 */
Ext.namespace('Ext.chooser');

Ext.chooser.BaseChooser = function (config) {
	Ext.apply(this, config);
	Ext.chooser.BaseChooser.superclass.constructor.call(this);
};

Ext.extend(Ext.chooser.BaseChooser, Ext.form.TwinTriggerField, {
    trigger1Class	: 'x-form-clear-trigger',
    trigger2Class	: 'x-form-search-trigger',
    hideTrigger1	: true,
    hasSearch		: true,
    valueField 		: null,
    displayField	: null,
    pageSize		: 15,
    chooseable		: true,			//可选择
    dialogWidth		: document.body.clientWidth - 10,
    dialogHeight	: document.body.clientHeight - 10,
    dataUrl			: null,
    hiddenName		: null,
    baseParams 		: {},
    //defaultAutoCreate : {tag: "input", type: "text", size: "24", autocomplete: "off"},
    getChooseGridPanel : function () {		//待实现
    	
	},
	queryChooseItems 	: function () {		//待实现
		
	},
	onChoose : function () {
		var grid = this.chooseGridPanel;
		if(!grid.getSelectionModel().hasSelection()){
        	Ext.Msg.alert("提示信息",  "请至少选择一条记录.");
         	return;
        }
        var record = grid.getSelectionModel().getSelected();
        this.setValue(record);
        this.chooseWindow.hide();
	},
	showChooseWindow : function () {
		if (this.chooseWindow == null) {
			var chooseGridPanel = this.getChooseGridPanel();
			var windowWidth 	= this.dialogWidth || document.body.clientWidth - 10;
			var windowHeight 	= this.dialogHeight || document.body.clientHeight - 10;
			this.chooseWindow = new Ext.Window({ //定义对话框
				width 		: windowWidth,
				height 		: windowHeight,
				shadow 		: true,
				title		: this.chooseTitle || '选择',
				closeAction : 'hide',
				modal 		: true,
				closable 	: true,
				layout 		: 'fit',
				items:[chooseGridPanel],
				buttons:[
					{text:'选择', iconCls: 'icon_sure',  scope: this, handler: this.onChoose},
					{text:'关闭', iconCls: 'icon_close', scope: this, handler:function(){this.chooseWindow.hide();} }
				]
			});
		}
		this.chooseWindow.show();
	},
    setBaseParams : function (baseParams) {
    	this.baseParams = baseParams;
    	this.hasSearch = true;
    },
    setReadOnly : function (flag) {
    	this.readOnly = flag;
    },
    setChooseable : function (flag) {		//设置是否可选择
    	this.chooseable = flag;
    	if (!this.chooseable) {
    		this.triggers[1].hide();
    	} else {
    		this.triggers[1].show();
    	}
    },
    onRender : function(ct, position){   
        Ext.chooser.BaseChooser.superclass.onRender.call(this, ct, position);   
        if(this.hiddenName){   
            this.hiddenField = this.el.insertSibling({
            	tag:'input',    
             	type:'hidden',    
             	name: this.hiddenName,    
             	id: (this.hiddenId || this.hiddenName)},   
            'before', true);   
            this.hiddenField.value = this.hiddenValue !== undefined ? this.hiddenValue :   
                this.value !== undefined ? this.value : '';   
            this.el.dom.removeAttribute('name');   
        }
    },
    initTrigger : function(){
        var ts = this.trigger.select('.x-form-trigger', true);
        this.wrap.setStyle('overflow', 'hidden');
        var triggerField = this;
        ts.each(function(t, all, index){
            t.hide = function(){
                var w = triggerField.wrap.getWidth();
                this.dom.style.display = 'none';
                triggerField.el.setWidth(w-triggerField.trigger.getWidth());
            };
            t.show = function(){
                var w = triggerField.wrap.getWidth();
                this.dom.style.display = '';
                triggerField.el.setWidth(w-triggerField.trigger.getWidth());
            };
            var triggerIndex = 'Trigger'+(index+1);

            if(this['hide'+triggerIndex]){
                t.dom.style.display = 'none';
            }
            t.on("click", this['on'+triggerIndex+'Click'], this, {preventDefault:true});
            t.addClassOnOver('x-form-trigger-over');
            t.addClassOnClick('x-form-trigger-click');
        }, this);
        this.triggers = ts.elements;
        
        if (!this.chooseable) {
    		this.triggers[1].hide();
    	} else {
    		this.triggers[1].show();
    	}
    },
	onTrigger1Click : function () {
		if (!this.chooseable) {
			return;
		}
		this.setRawValue('');
		this.setValue('');
		this.triggers[0].hide();
	},
 	onTrigger2Click : function(){
		if (!this.chooseable) {
			return;
		}
		this.showChooseWindow();
		if (this.hasSearch) {
			this.queryChooseItems();
			this.hasSearch = false;
		}
    },
    getValue : function(){
    	var value = '';
    	if(this.valueField){
        	value = typeof this.value != 'undefined' ? this.value : '';
        } else {
            value = Ext.chooser.BaseChooser.superclass.getValue.call(this);
        }
        return value;
    },
    
    /**
     * 支撑三种数据格式, string, Ext.data.Record, json
     * @param {} record
     */
 	setValue : function(record){
 		var text, value;
 		if (record && typeof record == 'object') {
 			if (record instanceof Ext.data.Record) {
		 		text  = record.get(this.displayField);
		 		value = record.get(this.valueField);
 			} else {
	            text = record[this.displayField];
	            value = record[this.valueField || this.displayField];   
 			}
        } else {   
            text = record;   
            value = record;  
        }   
        if(this.hiddenField){
            this.hiddenField.value = value;
        }
        Ext.chooser.BaseChooser.superclass.setValue.call(this, text);
        this.value = value;
        if (!Ext.isEmpty(value)) {
        	this.triggers[0].show();
        } else {
        	this.triggers[0].hide();
        }
    }
});