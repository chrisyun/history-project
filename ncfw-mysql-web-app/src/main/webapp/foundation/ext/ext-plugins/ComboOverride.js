/**
 * 
 * @description Combo本地化
 * @author Wuqingming
 * @since 2011-05
 */
Ext.override(Ext.form.ComboBox, {
	loaded	: false,
	trigger1Class: 'x-form-clear-trigger',
    trigger2Class: 'x-form-arrow-trigger',
    hideTrigger1: true,
    // private
    initComponent : function(){
        Ext.form.ComboBox.superclass.initComponent.call(this);
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
	setValue : function(v) {
		var text = v;
		if (!Ext.isEmpty(v) && !Ext.isEmpty(this.triggers)) {
	    	this.triggers[0].show();
	    } else if (!Ext.isEmpty(this.triggers)) {
	    	this.triggers[0].hide();
	    }
		
		// 远程模式且显示字段与值字段不一致
		if (!this.loaded && this.mode == 'remote' && this.displayField != this.valueField) {
			if (Ext.isEmpty(v)) {
				if (this.valueField) {
	            	var r = this.findRecord(this.valueField, v);
		            if (r) {
		                text = r.data[this.displayField];
		            } else if (this.valueNotFoundText !== undefined) {
		                text = this.valueNotFoundText;
		            }
		        }
		        this.lastSelectionText = text;
		        if (this.hiddenField) {
		            this.hiddenField.value = v;
		        }
		        Ext.form.ComboBox.superclass.setValue.call(this, text);
		        this.value = v;
				return;
			}
			//判断store是否已经加载完成
			if (Ext.isEmpty(this.loadCount) || this.loadCount <= 2) {	//允许加载两次
				if (Ext.isEmpty(this.loadCount)) {
					this.loadCount = 1;
				} else {
					this.loadCount++;
				}
				if (this.loadCount == 1) {		//第一次加载
			        if (this.hiddenField) {
			            this.hiddenField.value = v;
			        }
			        Ext.form.ComboBox.superclass.setValue.call(this, v);
					var combo = this;
					this.initValueTask = {
					    run: function(){
					        combo.setValue(v);
					    },
					    interval: 1000 //1 second
					}
					if (this.taskRunner == null) {
						this.taskRunner = new Ext.util.TaskRunner();
					}
					this.taskRunner.start(this.initValueTask);	
					this.value = v;
					return;
				}
				this.store.load({
					params		: this.getParams(this.store.baseParams),
					scope		: this,
					callback	: function (r, options, success) {
						if (!Ext.isEmpty(this.taskRunner) && !Ext.isEmpty(this.initValueTask)) {
							this.taskRunner.stop(this.initValueTask);	//停止初始化任务
						}
						if (success) {
							if(this.valueField){
					            var r = this.findRecord(this.valueField, v);
					            if (r) {
					                text = r.data[this.displayField];
					            } else if (this.valueNotFoundText !== undefined) {
					                text = this.valueNotFoundText;
					            }
					        }
					        this.lastSelectionText = text;
					        if (this.hiddenField) {
					            this.hiddenField.value = v;
					        }
					        Ext.form.ComboBox.superclass.setValue.call(this, text);
					        //this.mode = 'local';
					        this.value = v;
						}
					}
				});
			} else {
				if (this.valueField) {
		            var r = this.findRecord(this.valueField, v);
		            if (r) {
		                text = r.data[this.displayField];
		            } else if (this.valueNotFoundText !== undefined) {
		                text = this.valueNotFoundText;
		            }
		        }
		        this.lastSelectionText = text;
		        if (this.hiddenField) {
		            this.hiddenField.value = v;
		        }
		        Ext.form.ComboBox.superclass.setValue.call(this, text);
		        this.value = v;
			}
		//如果是Local模式
		} else {
			if (this.valueField) {
	            var r = this.findRecord(this.valueField, v);
	            if (r) {
	                text = r.data[this.displayField];
	            } else if (this.valueNotFoundText !== undefined) {
	                text = this.valueNotFoundText;
	            }
	        }
	        this.lastSelectionText = text;
	        if (this.hiddenField) {
	            this.hiddenField.value = v;
	        }
	        Ext.form.ComboBox.superclass.setValue.call(this, text);
	        this.value = v;
		}       
    },
    onLoad : function(){
    	this.loaded = true;
        if(!this.hasFocus){
            return;
        }
        if(this.store.getCount() > 0){
            this.expand();
            this.restrictHeight();
            if(this.lastQuery == this.allQuery){
                if(this.editable){
                    this.el.dom.select();
                }
                if(!this.selectByValue(this.value, true)){
                    this.select(0, true);
                }
            }else{
                this.selectNext();
                if(this.typeAhead && this.lastKey != Ext.EventObject.BACKSPACE && this.lastKey != Ext.EventObject.DELETE){
                    this.taTask.delay(this.typeAheadDelay);
                }
            }
        }else{
            this.onEmptyResults();
        }
        //this.el.focus();
    },
    doQuery : function(q, forceAll){
        if(q === undefined || q === null){
            q = '';
        }
        var qe = {
            query: q,
            forceAll: forceAll,
            combo: this,
            cancel:false
        };
        if(this.fireEvent('beforequery', qe)===false || qe.cancel){
            return false;
        }
        q = qe.query;
        forceAll = qe.forceAll;
        if(forceAll === true || (q.length >= this.minChars)){
            if(this.lastQuery !== q){
                this.lastQuery = q;
                if(this.mode == 'local' || this.loaded == true){
                    this.selectedIndex = -1;
                    if(forceAll){
                        this.store.clearFilter();
                    }else{
                        this.store.filter(this.displayField, q);
                    }
                    this.onLoad();
                }else{
                    this.store.baseParams[this.queryParam] = q;
                    this.store.load({
                        params: this.getParams(q)
                    });
                    this.expand();
                }
            }else{
                this.selectedIndex = -1;
                this.onLoad();
            }
        }
    },
    
    onBlur : function () {
    	Ext.form.ComboBox.superclass.onBlur.call(this);
    	if (!this.disabled && !this.readOnly && this.editable 
			&& this.displayField == this.valueField) {		//可手动编辑输入
				this.value = this.getRawValue();
				if (this.hiddenField) {
					this.hiddenField.value = this.getRawValue();
				}
				
				if (!Ext.isEmpty(this.value) && !Ext.isEmpty(this.triggers)) {
			    	this.triggers[0].show();
			    } else if (!Ext.isEmpty(this.triggers)) {
			    	this.triggers[0].hide();
			    }
		}
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
    }
});
