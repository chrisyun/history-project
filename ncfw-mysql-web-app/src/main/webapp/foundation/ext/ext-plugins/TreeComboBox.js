Ext.namespace("Ext.ux.form");

Ext.ux.form.TreeComboBox = Ext.extend(Ext.form.ComboBox, {
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
    
    trigger1Class: 'x-form-clear-trigger',
    trigger2Class: 'x-form-arrow-trigger',
    hideTrigger1: true,

    initComponent: function(ct, position) {
        this.divId = 'tree-' + Ext.id();
        if (isNaN(this.maxHeight)) this.maxHeight = 200;
        Ext.apply(this, {
            tpl: '<tpl>' + '<div style="height:' + this.maxHeight + 'px;">' + '<div id="' + this.divId + '"></div>' + '</div></tpl>'
        });

        var root = new Ext.tree.AsyncTreeNode({
            text: this.rootText,
            id: this.rootId,
            loader: new Ext.tree.TreeLoader({
                dataUrl: this.treeUrl,
                clearOnLoad: true
            })
        });

        this.tree = new Ext.tree.TreePanel({
            border: false,
            root: root,
            rootVisible: this.rootVisible,
            listeners: {
                scope: this,
                click: function(node) {
                    this.setValue(node);
                    this.collapse();
                }
            }
        });
        
        this.addEvents(   
            'select',   
            'expand',   
            'collapse',   
            'beforeselect'
        );

        Ext.ux.form.TreeComboBox.superclass.initComponent.call(this);

        this.triggerConfig = {
             tag:'span', cls:'x-form-twin-triggers', cn:[
             {tag: "img", src: Ext.BLANK_IMAGE_URL, cls: "x-form-trigger " + this.trigger1Class},
             {tag: "img", src: Ext.BLANK_IMAGE_URL, cls: "x-form-trigger " + this.trigger2Class}
         ]};
    },
    onRender: function(ct, position) {
        Ext.ux.form.TreeComboBox.superclass.onRender.call(this, ct, position);
		
        this.on("expand",
        function() {
            if (!this.tree.rendered) {
        		alert("ok rendered");
                this.tree.render(this.divId);
            }
        },
        this);
    },
     /**
     * Expands the dropdown list if it is currently hidden. Fires the {@link #expand} event on completion.
     */
    expand : function(){
        if(this.isExpanded() || !this.hasFocus){
            return;
        }
        this.list.alignTo(this.wrap, this.listAlign);
        this.list.show();
        this.innerList.setOverflow('auto'); // necessary for FF 2.0/Mac
        Ext.getDoc().on('mousewheel', this.collapseIf, this);
        Ext.getDoc().on('mousedown', this.collapseIf, this);
        this.fireEvent('expand', this);
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
    onTriggerClick : function(){   
        if(this.disabled){
            return;
        }
        //alert("onTriggerClick");
        if(this.isExpanded()){
            this.collapse();
            this.el.focus();
        } else {
            this.onFocus({});
            this.expand();
            this.el.focus();
        }
        //this.el.focus();   
    }   
    
    /**,
    onTriggerClick : function () {
    	Ext.ux.form.TreeComboBox.superclass.onTriggerClick.call(this);
    	if (this.displayValue != '') {
			this.triggers[0].show();
    	} else {
    		this.triggers[0].hide();
    	}
    }

    /*以下代码是为了将tree的node设置到combo中 因为我还有一些其他的作用，所以我是将这部分的代码重写到其他地方了 
          setValue : function(node) { 
            if (typeof node == "object") { 
              this.setRawValue(node.text); 
              if (this.hiddenField) { 
                this.hiddenField.value = node.id; 
              } 
            } else { 
              this.setRawValue(node); 
            } 
          } 
          */

});

Ext.reg('uxtreecombobox', Ext.ux.form.TreeComboBox);