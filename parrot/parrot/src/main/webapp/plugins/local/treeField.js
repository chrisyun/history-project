/**
 * @description 树下拉Field
 * @author Wuqingming
 * @since 2010-12-29
 * @version 1.0
 * 
 * 
 * modifies: 
 * 1. 第一次修改:
 * 		修改说明: 修改onRender方法, 解决属性allowBlank: false 导致的显示问题
 * 		修改人: Wuqingming
 * 		修改日期: 2011-05-11
 * 
 */
Ext.form.TreeField = Ext.extend(Ext.form.TriggerField,  {   
    /**  
     * @cfg {Boolean} readOnly  
     * 设置为只读状态  
     *   
     */  
    readOnly : true ,
    /**  
     * @cfg {Boolean} displayField  
     * 控件获得焦点时才会初始化下拉框包括树
     *   
     */  
    lazyInit: true,
    
    /**  
     * @cfg {Boolean} onlyLeafSelectable  
     * 只有叶子节点才能被选择
     *   
     */  
    onlyLeafSelectable: false,

    /**  
     * @cfg {String} loadingText  
     * 树节点加载时显示的信息  
     *   
     */  
    loadingText: '加载中...',

    /**  
     * @cfg {String} displayField  
     * value为空时显示 
     *   
     */  
    emptyText:'---请选择---',
    /**  
     * @cfg {String} displayField  
     * 用于显示数据的字段名  
     *   
     */  
    displayField : 'text',   
    /**  
     * @cfg {String} valueField  
     * 用于保存真实数据的字段名  
     */  
    valueField : null,   
    /**  
     * @cfg {String} hiddenName  
     * 保存真实数据的隐藏域名  
     */  
    hiddenName : null,   
    /**  
     * @cfg {Integer} listWidth  
     * 下拉框的宽度  
     */  
    listWidth : null,   
    /**  
     * @cfg {Integer} minListWidth  
     * 下拉框最小宽度  
     */  
    minListWidth : 50,   
    /**  
     * @cfg {Integer} listHeight  
     * 下拉框高度  
     */  
    listHeight : null,   
    /**  
     * @cfg {Integer} minListHeight  
     * 下拉框最小高度  
     */  
    minListHeight : 50,   
    /**  
     * @cfg {String} dataUrl  
     * 数据地址  
     */  
    dataUrl : null,
    /**  
     * @cfg {Ext.tree.TreePanel} tree  
     * 下拉框中的树  
     */  
    tree : null,   
    /**  
     * @cfg {String} value  
     * 默认值  
     */  
    value : null,   
    /**  
     * @cfg {String} displayValue  
     * 用于显示的默认值  
     */  
    displayValue : null,   
    /**  
     * @cfg {Object} baseParams  
     * 向后台传递的参数集合  
     */  
    baseParams : {},
    /**  
     * @cfg {Object} treeRootConfig  
     * 树根节点的配置参数  
     */  
    treeRootConfig : {
        id : ' ',
        text : 'ROOT',
        draggable:false
    },   
    /**  
     * @cfg {String/Object} autoCreate  
     * A DomHelper element spec, or true for a default element spec (defaults to  
     * {tag: "input", type: "text", size: "24", autocomplete: "off"})  
     */  
    defaultAutoCreate : {tag: "input", type: "text", size: "24", autocomplete: "off"},

    trigger1Class: 'x-form-clear-trigger',
    trigger2Class: 'x-form-arrow-trigger',
    hideTrigger1: true,
  
    initComponent : function(){   
        Ext.form.TreeField.superclass.initComponent.call(this);   
        this.addEvents(   
            'select',   
            'expand',   
            'collapse',   
            'beforeselect'
        );

        this.triggerConfig = {
             tag:'span', cls:'x-form-twin-triggers', cn:[
             {tag: "img", src: Ext.BLANK_IMAGE_URL, cls: "x-form-trigger " + this.trigger1Class},
             {tag: "img", src: Ext.BLANK_IMAGE_URL, cls: "x-form-trigger " + this.trigger2Class}
         ]};
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
    },
    onTrigger1Click : function () {
    	if (!Ext.isEmpty(this.value)) {
    		this.fireEvent("change");
    	}
		this.clearValue();
		this.triggers[0].hide();
	},
 	onTrigger2Click : function(){
	 	this.onTriggerClick();
    },
    initList : function(){
        if(!this.list){   
            var cls = 'x-combo-list';
            
            this.list = new Ext.Layer({ shadow: this.shadow, cls: [cls, this.listClass].join(' '), constrain: false, containerScroll: true });
  
            var lw = this.listWidth || Math.max(this.wrap.getWidth(), this.minListWidth);   
            this.list.setWidth(lw);   
            this.list.swallowEvent('mousewheel');   
               
            this.innerList = this.list.createChild({cls:cls+'-inner'});   
            this.innerList.setWidth(lw - this.list.getFrameWidth('lr'));   
            this.innerList.setHeight(this.listHeight || this.minListHeight);
            
            this.initInner();
        }   
    }, 
    initInner: function(){
        if(!this.tree){
            this.tree = this.createTree(this.innerList);       
        } else {
        	this.tree.render(this.innerList);
        }
        this.tree.on('click',this.select,this);
		var root = this.tree.root;
		if(root instanceof Ext.tree.AsyncTreeNode){
			root.on('beforeload', this.onBeforeRootLoad, this, {
                single: true
            });		
			root.on('load', this.onRootLoad, this, {
                single: true
            });
		}
        this.tree.render();
        
        /**
		if(this.value) {
			alert("this.setValue = " + this.value);
			this.setValue(this.value);
		}*/
    },
    onRender : function(ct, position){
        this.doc = Ext.isIE ? Ext.getBody() : Ext.getDoc();
    	/**
    	 * 解决allowBlank: true不显示的问题
    	 */
        if(!this.el){
            var cfg = this.getAutoCreate();
            if(!cfg.name){
                cfg.name = this.name || this.id;
            }
            if(this.inputType){
                cfg.type = this.inputType;
            }
            this.el = ct.createChild(cfg, position);
        }
        var type = this.el.dom.type;
        if(type){
            if(type == 'password'){
                type = 'text';
            }
            this.el.addClass('x-form-'+type);
        }
        if(this.readOnly){
            this.el.dom.readOnly = true;
        }
        if(this.tabIndex !== undefined){
            this.el.dom.setAttribute('tabIndex', this.tabIndex);
        }

        this.el.addClass([this.fieldClass, this.cls]);
    	
    	
	 	this.wrap = this.el.wrap({cls: "x-form-field-wrap"});
        this.trigger = this.wrap.createChild(this.triggerConfig ||
                {tag: "img", src: Ext.BLANK_IMAGE_URL, cls: "x-form-trigger " + this.triggerClass});
        if(this.hideTrigger){
            this.trigger.setDisplayed(false);
        }
        this.initTrigger();
        if(!this.width){
            this.wrap.setWidth(this.el.getWidth()+this.trigger.getWidth());
        }
        
        //Ext.form.TreeField.superclass.onRender.call(this, ct, position);
        if(this.hiddenName){   
            this.hiddenField = this.el.insertSibling({
            	tag:'input',    
         		type:'hidden',    
             	name: this.hiddenName,    
             	id: (this.hiddenId||this.hiddenName)},   
                'before', true);   
            this.hiddenField.value = this.hiddenValue !== undefined ? this.hiddenValue :   
                this.value !== undefined ? this.value : '';   
            this.el.dom.removeAttribute('name');   
        } 
        if(Ext.isGecko){   
            this.el.dom.setAttribute('autocomplete', 'off');   
        }
        
        this.on('blur', this.onBlur, this, {single: true});
        
        if (this.lazyInit) {
            this.on('focus', this.initList, this, {single: true});
        } else {
            this.initList();
        }
        this.afterRender(ct);
    },
    onBlur : function () {
    	var superValue = Ext.form.TreeField.superclass.getValue.call(this);
    	if (Ext.isEmpty(superValue)) {
    		this.value = '';
    		if(this.hiddenName){ 
    			Ext.getDom(this.hiddenId || this.hiddenName).value = '';
    		}
    	}
		//alert("this.getValue() = " + this.getValue());
    },
    select : function(node){   
        if(this.fireEvent('beforeselect', node, this)!= false){   
            this.onSelect(node);   
            this.fireEvent('select', this, node);   
        }   
    },   
    onSelect:function(node){
    	if (this.onlyLeafSelectable) {
	    	if(node.isLeaf()){
		        this.setValue(node);   
		        this.collapse();
	    	}
    	} else {
    		this.setValue(node);   
	        this.collapse();
    	}
    },   
    createTree:function(el){   
        var Tree = Ext.tree;   
       
        var tree = new Tree.TreePanel({   
            el: el,
            border: false,
            autoScroll:true,   
            animate:true,   
            containerScroll: true, 
            rootVisible: false,
            loader: new Tree.TreeLoader({   
                dataUrl : this.dataUrl,   
                baseParams : this.baseParams   
            })   
        });   
       
        var root = new Tree.AsyncTreeNode(this.treeRootConfig);
        tree.setRootNode(root);
        return tree;   
    },

	//@private
    onBeforeRootLoad : function(){
		this.isLoading = true;
        this.innerList.insertFirst({
        	tag: 'div',
        	cls: 'loading-indicator',
		    html: this.loadingText
		});
    },
	//@private
	onRootLoad : function(){
		this.isLoading = false;
		/**
		if (this.value) {
			this.setValue(this.value);
		}*/
		this.innerList.child('.loading-indicator').remove();
		if(this.isExpanded()){
			this.onLoad();
		}
	},
  
    getValue : function(){
		var value;
        if (this.valueField) {   
            value = typeof this.value != 'undefined' ? this.value : '';   
        } else {   
            value = Ext.form.TreeField.superclass.getValue.call(this);   
        }
		if (Ext.isEmpty(value)) {
			value = '';
		}
		return value;
    },   
    setValue : function(node){   
        //if(!node)return;   
        var text,value;   
        if (node && typeof node == 'object') {
            text = node[this.displayField];   
            
            if(this.valueField.indexOf(".") >= 0) {
            	var valueFieldArry = this.valueField.split(".");
            	value = node;
            	for(var i=0;i<valueFieldArry.length;i++) {
            		value = value[valueFieldArry[i]];
            	}
            } else {
            	value = node[this.valueField || this.displayField];
            }
            
            
        } else {   
            text = node;   
            value = node;  
        }   
        if (this.hiddenField) {   
            this.hiddenField.value = value;   
        }   
        Ext.form.TreeField.superclass.setValue.call(this, text);   
        this.value = value;   

        if (!Ext.isEmpty(this.value)) {
        	this.triggers[0].show();
        } else if (!Ext.isEmpty(this.triggers)) {
        	this.triggers[0].hide();
        }
    },
    clearValue : function () {
    	this.setValue('');
    },
    onResize: function(w, h){   
        Ext.form.TreeField.superclass.onResize.apply(this, arguments);   
        if(this.list && this.listWidth == null){   
            var lw = Math.max(w, this.minListWidth);   
            this.list.setWidth(lw);   
            this.innerList.setWidth(lw - this.list.getFrameWidth('lr'));   
        }   
    },   
    validateBlur : function(){   
        return !this.list || !this.list.isVisible();      
    },   
    onDestroy : function(){   
        if(this.list){   
            this.list.destroy();   
        }   
        if(this.wrap){   
            this.wrap.remove();   
        }   
        Ext.form.TreeField.superclass.onDestroy.call(this);   
    },   
    collapseIf : function(e){   
        if(!e.within(this.wrap) && !e.within(this.list)){   
            this.collapse();   
        }   
    },   
  
    collapse : function(){   
        if(!this.isExpanded()){   
            return;   
        }   
        this.list.hide();   
        Ext.getDoc().un('mousewheel', this.collapseIf, this);   
        Ext.getDoc().un('mousedown', this.collapseIf, this);   
        this.fireEvent('collapse', this);   
    },   
    expand : function(){   
        if(this.isExpanded() || !this.hasFocus){   
            return;   
        }   
        this.onExpand();   
        this.list.alignTo(this.wrap, this.listAlign);   
        this.list.show();   
        Ext.getDoc().on('mousewheel', this.collapseIf, this);   
        Ext.getDoc().on('mousedown', this.collapseIf, this);   
        this.fireEvent('expand', this);   
    },   
    onExpand : function(){   
        var doc = Ext.getDoc();   
        this.on('click',function(){alert(111)},this);   
    },   
    isExpanded : function(){   
        return this.list && this.list.isVisible();   
    },   
    onTriggerClick : function(){  
        if(this.disabled){   
            return;   
        }
        if(this.isExpanded()){   
            this.collapse();   
        }else {   
            this.onFocus({});
            this.expand();   
        }   
        //this.el.focus();   
    }   
});   
Ext.reg('treefield', Ext.form.TreeField);