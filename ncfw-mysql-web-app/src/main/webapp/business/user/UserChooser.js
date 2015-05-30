
/**
 * 人员选择器
 * @author Wuqingming
 * @since 2011-01-12
 * @type 
 */

Ext.UserChooser = function (config) {
	Ext.apply(this, config);
	Ext.UserChooser.superclass.constructor.call(this);
};

Ext.extend(Ext.UserChooser, Ext.form.TwinTriggerField, {
    trigger1Class	: 'x-form-clear-trigger',
    trigger2Class	:'x-form-search-trigger',
    hideTrigger1	: true,
    hasSearch		: true,
    displayField 	: 'username',   
    valueField 		: 'userid',   
    pageSize		: 15,
    dataUrl			: null,
    hiddenName		: null,
    baseParams 		: {},
    genderRenderer 	: null,
    //defaultAutoCreate : {tag: "input", type: "text", size: "24", autocomplete: "off"},
    setBaseParams : function (baseParams) {
    	this.baseParams = baseParams;
    	this.hasSearch = true;
    },
    setReadOnly : function (flag) {
    	this.readOnly = flag;
    	if (this.readOnly) {
    		this.triggers[1].hide();
    	} else {
    		this.triggers[1].show();
    	}
    },
    onRender : function(ct, position){   
        Ext.UserChooser.superclass.onRender.call(this, ct, position);   
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
        
        if (this.readOnly) {
    		this.triggers[1].hide();
    	} else {
    		this.triggers[1].show();
    	}
    },
	onTrigger1Click : function () {
		if (this.readOnly) {
			return;
		}
		this.setRawValue('');
		this.setValue('');
		this.triggers[0].hide();
	},
 	onTrigger2Click : function(){
		if (this.readOnly) {
			return;
		}
		this.showUserWindow();
		if (this.hasSearch) {
			this.queryUserList();
			this.hasSearch = false;
		}
    },
    getValue : function(){
    	var value = '';
        if(this.valueField){
        	value = typeof this.value != 'undefined' ? this.value : '';
        } else {
            value = Ext.form.ComboBox.superclass.getValue.call(this);
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
        Ext.UserChooser.superclass.setValue.call(this, text);
        this.value = value;
        if (!Ext.isEmpty(value)) {
        	this.triggers[0].show();
        } else {
        	this.triggers[0].hide();
        }
    },
    getUserGridPanel : function () {
		if (this.userGridPanel != null) {
			return this.userGridPanel;
		}
		var dataUrl = this.dataUrl || sys.basePath + 'userAction!listUser.do';
		var store = new Ext.data.Store( {
			proxy : new Ext.data.HttpProxy(new Ext.data.Connection({timeout:0, url: dataUrl})),
			reader : new Ext.data.JsonReader({
				root : 'data',
				totalProperty : 'totalProperty'
			}, 
				Ext.data.Record.create([
					{name : 'userid',type : 'string'}, 
					{name : 'username',type : 'string'},
					{name : 'mobile',type : 'string'},
					{name : 'mail',type : 'string'},
					{name : 'gender',type : 'string'}
				])
			),
			remoteSort : true
		});
		var sm = new Ext.grid.CheckboxSelectionModel({singleSelect: true});
		
		var genderCm = Ext.isEmpty(this.genderRenderer) ? {header : '性别', dataIndex: 'GENDER', hidden: true, sortable: true} : 
				{header : '性别', dataIndex: 'GENDER', sortable: true, renderer: this.genderRenderer};
		//定义页面查询表格
		var cm = new Ext.grid.ColumnModel({
       		columns:[
       		new Ext.grid.RowNumberer(),
       		sm,
			{
				header: '姓名',
				dataIndex : 'username',
				sortable: true
			},
			genderCm, 
			{
				header : '手机号码',
				dataIndex: 'mobile',
				sortable: true
			}]
		});
		
		var querUserButton = new Ext.Button({
		 	text:'查询', iconCls : 'icon_search', scope: this, minWidth: 80,
        	handler: this.queryUserList 
        });
        
        //注册快捷键
		Ext.getBody().on("keyup", function(e){
	    	if(e.keyCode == Ext.EventObject.ENTER){
	    		this.queryUserList();
	     	}
	    }, this);
		this.userGridPanel = new Ext.grid.GridPanel({
			region: 'center',
        	store: store,
	        cm: cm,
	        sm: sm,
	        border : false,
	        tbar:[
	        	'人员姓名:', new Ext.form.TextField({id:'username', anchor: '96%'}), 
	        	'手机号码:', new Ext.form.TextField({id:'mobile', anchor: '96%'}), 
	        	'-', querUserButton
	        ],
	        loadMask : true,
	        split : true,
	        enableColumnMove: false,
	        stripeRows: true,
	        iconCls : 'grid-icon',
	        viewConfig: {
		        forceFit: true
		    },
	        bbar: new Ext.PagingToolbar({
		        pageSize: this.pageSize || 10,
		        store: store,
		        displayInfo: true
	        })
	    });
	    this.userGridPanel.on("dblclick", this.onUserChoose, this);
	    return this.userGridPanel;
	},
	queryUserList : function () {
		var store 					= this.userGridPanel.getStore();
		this.baseParams.username 	= Ext.getCmp("username").getValue();
		this.baseParams.mobile 		= Ext.getCmp("mobile").getValue();
		store.baseParams 			= this.baseParams;
		store.reload( {params : {start : 0,limit : this.pageSize}});
	},
	onUserChoose : function () {
		var grid = this.userGridPanel;
		if(!grid.getSelectionModel().hasSelection()){
        	Ext.Msg.alert("提示信息",  "请至少选择一条记录.");
         	return;
        }
        var record = grid.getSelectionModel().getSelected();
        this.setValue(record);
        this.userWindow.hide();
	},
	showUserWindow : function () {
		if (this.userWindow == null) {
			var userGridPanel = this.getUserGridPanel();
			var windowWidth = document.body.clientWidth > 580 ? 580 : document.body.clientWidth - 20;
			var windowHeight = document.body.clientHeight > 600 ? 600 : document.body.clientHeight - 10;
			this.userWindow = new Ext.Window({ //定义对话框
				width : windowWidth,
				height : windowHeight,
				shadow : true,
				title: '人员选择',
				closeAction : 'hide',
				modal : true,
				closable : true,
				layout : 'fit',
				items:[userGridPanel],
				buttons:[
					{text:'选择', iconCls: 'icon_sure',  scope: this, handler: this.onUserChoose},
					{text:'关闭', iconCls: 'icon_close', scope: this, handler:function(){this.userWindow.hide();} }
				]
			});
		}
		this.userWindow.show();
		if (this.baseParams != null && this.baseParams.username != null) {
			if (this.baseParams.username != null) {
				Ext.getCmp("username").setValue(this.baseParams.username);
			} 
			if (this.baseParams.mobile != null) {
				Ext.getCmp("mobile").setValue(this.baseParams.mobile);
			}
		}
	}
});

Ext.reg('userchooser', Ext.UserChooser);