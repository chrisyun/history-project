<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/foundation/ext/jsp/taglib.jsp"%>
<%@ include file="/foundation/ext/jsp/workspace.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>数据字典管理</title>
<style>
</style>
    <script type="text/javascript">
	var pageSize = 8;
	var dictEntryPageSize = 50;
	var queryDictTypeUrl = sys.basePath + 'datadict/dictAction!listDictType.do';
	var queryDictEntryUrl = sys.basePath + 'datadict/dictAction!listDictEntry.do';
	var dictTypeInputWindow, dictEntryInputWindow;
	var dictTypeInputForm, dictEntryInputForm;
	var target;
	var OPTYPE = {
		ADD		: 'add',
		UPDATE	: 'update',
		DELETE	: 'delete'
	};
	
	//------------------------------------------------------------------------//
	// 数据字典类型管理开始
	//------------------------------------------------------------------------//
	
	//获取数据字典类型录入表单
	function getDictTypeInputForm(){
		if(dictTypeInputForm == null){
			dictTypeInputForm = new Ext.FormPanel({
				id: 'dictTypeInputForm',
				region: 'center',
				url: sys.basePath + 'datadict/dictAction!inputDictType.do',
	        	labelAlign:'left',
	        	buttonAlign:'center',
	        	// bodyStyle:'padding:0 0 0 0',
	        	frame:true,
	        	labelWidth: 90,
	        	monitorValid:true,
				items : [{
					layout:'column',border:false,labelSeparator:':',
				  	defaults:{layout: 'form',border:false,columnWidth:1.00, width:30},
				  	items : [{
				  		id: 'dicttypeIdContainer',
				  		items: [{
				  			xtype:'textfield', fieldLabel: '<em class="required">*</em>字典类型编号', 
				  			name:'dicttypeId', id: 'dictType.dicttypeId', width:245, allowBlank : false, 
				  			maxLength: 255, maxLengthText: '键值长度不能超过255个字符'
				  		}]
				  	},{
				  		items: [{
				  			xtype:'textfield', fieldLabel: '<em class="required">*</em>字典类型名称', 
				  			name: 'dicttypeName', id: 'dictType.dicttypeName', width: 245, allowBlank : false, 
				  			maxLength: 255, maxLengthText: '名称长度不能超过255个字符'
				  		}]
				  	}]
				}]
			});
		}
		return dictTypeInputForm;
	}
	
	//显示数据字典类型对话框
	function showDictTypeInputWindow(){
	  	if(dictTypeInputWindow == null){
	  		var localDictTypeInputForm = getDictTypeInputForm();
			dictTypeInputWindow = new Ext.Window({ //定义对话框
	  			id: 'dictTypeInputWindow',
				title: '数据字典类型录入',
				width : 420,
				height : 136,
				layout:'fit',
				shadow : true,
				closeAction : 'hide', //hide表示关闭的时候对话框是隐藏的，并没有真正的销毁，下次要用时在show出来
				modal : true,
				closable : true,
				items: [localDictTypeInputForm],
				buttons : [{
					text :'保存',
					iconCls: 'icon_save',
					handler: onSaveDictType
				},{
					text : '关闭',
					iconCls: 'icon_close',
					handler : function (){dictTypeInputWindow.hide()}
				}]
			});
        }
        dictTypeInputWindow.show(); 
	}
	
	//保存数据字典类型信息
	function onSaveDictType(){
		var thisForm = getDictTypeInputForm().getForm();
    	if(!thisForm.isValid()){
    		return;
    	}
    	var dicttypeId = thisForm.findField("dictType.dicttypeId").getValue();
    	Ext.Ajax.request({
			url: sys.basePath + 'datadict/dictAction!validateDictTypeUnique.do',
			timeout: 300000,
			params:{'dicttypeId': dicttypeId, 'target': target},
			success: function(response, options){
				var result = Ext.util.JSON.decode(response.responseText);
				if(result.success){
			    	submitDictTypeInputForm();
		        } else {
		         	Ext.Msg.show({
		                title : '提示信息',
		                msg : result.msg,
		                buttons : Ext.Msg.OK,
		                icon: Ext.MessageBox.ERROR,
		                fn: function(){
		                	thisForm.findField("dictType.dicttypeId").focus();
		                }
		            });
		         }
			},
			failure:function(){Ext.MessageBox.hide();return;}
		});
	}
	
	function submitDictTypeInputForm(){
		var thisForm = getDictTypeInputForm().getForm();
		Common.showProcessMsgBox();
    	thisForm.submit({
			params: {'target': target},
			timeout: 300000,
		 	success: function(form, action) {
				Common.hideProcessMsgBox();
				Ext.Msg.show({
	                title : '成功提示',
	                msg : action.result.msg,
	                minWidth: 160,
	                buttons : Ext.Msg.OK,
	                icon: Ext.MessageBox.INFO,
	                fn: function(){
						dictTypeStore.reload({});
	                	dictTypeInputWindow.hide();
	  					form.reset();
	                }
	            });
	            
			},
			failure: function(form, action) {
				Ext.MessageBox.hide();
	           	Ext.Msg.show({
	                title : '失败提示',
	                minWidth: 160,
	                msg : "数据字典类型保存失败",
	                buttons : Ext.Msg.OK,
	                icon: Ext.MessageBox.ERROR
	            });
			}
		});	
	}
	
	//添加数据字典类型
	function onAddDictType(){
		target = OPTYPE.ADD;
		showDictTypeInputWindow();
		Ext.getCmp("dicttypeIdContainer").show();
		dictTypeInputWindow.setTitle("新增数据字典类型");
		getDictTypeInputForm().getForm().reset();
	}
	
	//修改数据字典类型
	function onUpdateDictType(){
		target = OPTYPE.UPDATE;
		var selectionModel = dictTypeGrid.getSelectionModel();
		var count = selectionModel.getCount();
		if(count != 1){
			Ext.Msg.alert("提示信息", "请选择一条记录");
			return;
		}
		var dictTypeRecord = selectionModel.getSelected();
		showDictTypeInputWindow();
		Ext.getCmp("dicttypeIdContainer").hide();
		dictTypeInputWindow.setTitle("修改数据字典类型");
		initDictTypeInputForm(dictTypeRecord);
	}
	
	//初始化数据字典类型录入表单
	function initDictTypeInputForm(dictTypeRecord){
		var values = [
			{id: 'dictType.dicttypeId', value: dictTypeRecord.get("dicttypeId")}, {id: 'dictType.dicttypeName', value: dictTypeRecord.get("dicttypeName")}
		]
		var dictTypeInputForm = getDictTypeInputForm().getForm();
		dictTypeInputForm.setValues(values);
	}
	
	//删除数据字典类型
	function onDeleteDictType(){
		var selectionModel = dictTypeGrid.getSelectionModel();
		var count = selectionModel.getCount();
		if(count < 1){
			Ext.Msg.alert("友情提示", "请至少选择一条记录");
			return;
		}
		var records = selectionModel.getSelections();
    	Ext.Msg.confirm("确认消息框", "此操作将删除数据字典类型信息，是否继续？", function(btn){
	 		if(btn == 'yes'){
				var dicttypeIds = "";
				for(var i = 0;i < records.length;i++){
					dicttypeIds += records[i].get("dicttypeId");
					if((i + 1) < records.length){
						dicttypeIds += ",";
					}
				}
				
				Common.showProcessMsgBox('数据删除中，请稍后...');				
				Ext.Ajax.request({
					url: sys.basePath + 'datadict/dictAction!deleteDictTypeBatch.do',
					timeout: 300000,
					params:{dicttypeIds: dicttypeIds},
					success: function(response, options){
						Common.hideProcessMsgBox();
						var result = Ext.util.JSON.decode(response.responseText);
						if(result.success){
							Ext.Msg.show({
				                title : '成功提示',
				                msg : result.msg,
				                buttons : Ext.Msg.OK,
				                icon: Ext.MessageBox.INFO,
				                fn: function(){
				                	dictTypeStore.reload();
				                	dictEntryStore.removeAll();
				                }
				            });
				         } else {
				         	Ext.Msg.show({
				                title : '失败提示',
				                msg : result.msg,
				                buttons : Ext.Msg.OK,
				                icon: Ext.MessageBox.ERROR,
				                fn: function(){
				                }
				            });
				         }
					},
					failure:function(){Ext.MessageBox.hide();return;}
				});
		 	}
		});	
	}
	
	function refreshDictCache(){
	    Ext.Ajax.request({
			url: sys.basePath + 'datadict/dictAction!refreshDictCache.do',
			timeout: 300000,
			success: function(response, options){
				Ext.MessageBox.hide();
				var result = Ext.util.JSON.decode(response.responseText);
				Ext.topShow.msg("成功提示", result.msg);
			},
			failure:function(){Ext.MessageBox.hide();return;}
		});
	}
	
	//查询提交事件
	function submitSearchDictTypeHandler(){
		var thisForm = queryFormPanel.getForm();
		if (!thisForm.isValid()) {
			return;
		}
		dictTypeStore.baseParams = {
			'dictypeIdLike': Ext.getCmp("query.dicttypeId").getValue().trim(),
			'dicttypeName': Ext.getCmp("query.dicttypeName").getValue().trim()
		};
    	dictTypeStore.reload({params:{start: 0, limit: dictTypePagingBar.pageSize}});
	}
	
	//------------------------------------------------------------------------//
	// 数据字典类型管理结束
	//------------------------------------------------------------------------//
    
    
	//------------------------------------------------------------------------//
	// 数据字典项管理开始
	//------------------------------------------------------------------------//
	
  	var dictEntryGridTitle = '数据字典项列表';
  	var _dictTypeId = null;
	var _dictTypeName = null;
	
	var dictEntryStatusDictArray = <ncframework:dictWrite dicttypeId="SYS_DICTENTRY_STATUS" type="array"/>;
	
	var dictEntryStatusCombo = new Ext.form.ComboBox({
    	hideLabel: false, fieldLabel: '<em class="required">*</em>状态', width: 100, id: 'dictEntryStatusCombo',
		name: 'statusText', hiddenName: 'status', allowBlank: false, value: '1',
		store: new Ext.data.SimpleStore({
			fields: ['value', 'text'],
			data: dictEntryStatusDictArray
		}),
		mode: 'local',triggerAction: 'all',valueField: 'value',
		displayField: 'text',readOnly: true, resizable: true
	});
	
    //获取数据字典类型项录入表单
	function getDictEntryInputForm(){
		if(dictEntryInputForm == null){
			dictEntryInputForm = new Ext.FormPanel({
				url: sys.basePath + 'datadict/dictAction!inputDictEntry.do',
				//region: 'center',
	        	labelAlign: 'right',
	        	buttonAlign:'center',
	        	bodyStyle: 'padding:0 0 0 0',
	        	frame:true,
	        	labelWidth: 80,
	        	monitorValid:true,
				items : [
				  {layout:'column',border:false,labelSeparator:':',
				  defaults:{layout: 'form',border:false,columnWidth:.5, width:30},
				  items : [
	                	{columnWidth:.4, id: 'dictIdContainer', items: [
	                		{xtype:'hidden', name:'dicttypeId', id:'dictEntry.dicttypeId'},
	                		{xtype:'textfield',fieldLabel: '<em class="required">*</em>字典项编号', name:'dictId', id:'dictEntry.dictId', width:100, allowBlank: false}
	                	]},
	                	{columnWidth:.6, items: [
	                		{xtype:'textfield',fieldLabel: '<em class="required">*</em>字典项名称', name:'dictName', id:'dictEntry.dictName', width:200, allowBlank: false}
	                	]},
	                	
	                	{columnWidth:.4, items: [dictEntryStatusCombo]},
	                	{columnWidth:.6, items: [
	                		{xtype:'numberfield',fieldLabel: '显示序号', name:'sortNo', id:'dictEntry.sortNo', width:200, readOnly: false}
	                	]},
						{columnWidth:1.00,items: [{xtype:'textarea', fieldLabel: '备忘', name:'memo', id: 'dictEntry.memo', width: 405, allowBlank : true, maxLength: 1024, maxLengthText: '备注长度不能超过1024个字符'}]}
					]
				}]
			});
		}
		return dictEntryInputForm;
	}
	
	//显示数据字典项录入对话框
	function showDictEntryInputWindow(){
	  	if(dictEntryInputWindow == null){
	  		var localDictEntryInputForm = getDictEntryInputForm();
			dictEntryInputWindow = new Ext.Window({ //定义对话框
				width : 550,
				height : 186,
				layout:'fit',
				shadow : true,
				closeAction : 'hide', //hide表示关闭的时候对话框是隐藏的，并没有真正的销毁，下次要用时在show出来
				modal : true,
				closable : true,
				minWidth : 400,
				minHeight : 230,
				items:[localDictEntryInputForm],
				buttons : [{
					text :'保存',
					iconCls: 'icon_save',
					handler: onSaveDictEntry
				},{
					text : '关闭',
					iconCls: 'icon_close',
					handler : function (){dictEntryInputWindow.hide()}
				}]
			});
        }
        dictEntryInputWindow.show(); 
	}
	
	//按所属数据字典类型查找数据字典项
	function searchDictEntry(dictTypeId, dictTypeName){
		_dictTypeId = dictTypeId;
		_dictTypeName = dictTypeName;
		dictEntryGrid.setTitle(dictEntryGridTitle + " (所属数据字典类型：" + _dictTypeName + ")");
		dictEntryStore.baseParams = {
			dicttypeId: _dictTypeId
		};
    	dictEntryStore.load({start: 0, limit: dictEntryPagingBar.pageSize});
	}
	
	//保存数据字典项
	function onSaveDictEntry(){
		var thisForm = getDictEntryInputForm().getForm();
    	if(!thisForm.isValid()){
    		return;
    	}
    	var dicttypeId = thisForm.findField("dictEntry.dicttypeId").getValue();
    	var dictId = thisForm.findField("dictEntry.dictId").getValue();
    	Ext.Ajax.request({
			url: sys.basePath + 'datadict/dictAction!validateDictEntryUnique.do',
			timeout: 300000,
			params:{'dicttypeId': dicttypeId, 'dictId': dictId, 'target': target},
			success: function(response, options){
				Ext.MessageBox.hide();
				var result = Ext.util.JSON.decode(response.responseText);
				if(result.success){
			    	submitDictEntryInputForm();
		        } else {
		         	Ext.Msg.show({
		                title : '提示信息',
		                msg : result.msg,
		                buttons : Ext.Msg.OK,
		                icon: Ext.MessageBox.ERROR,
		                fn: function(){
		                	thisForm.findField("dictEntry.dictId").focus();
		                }
		            });
		         }
			},
			failure:function(){Ext.MessageBox.hide();return;}
		});
    	
	}
	
	function submitDictEntryInputForm () {
		var thisForm = getDictEntryInputForm().getForm();
		Common.showProcessMsgBox();
    	thisForm.submit({
			timeout: 300000,
			params: {'target': target},
		 	success: function(form, action) {
				Common.hideProcessMsgBox();
				Ext.Msg.show({
	                title : '成功提示',
	                msg : action.result.msg,
	                buttons : Ext.Msg.OK,
	                icon: Ext.MessageBox.INFO,
	                fn: function(){
						dictEntryStore.reload({});
						if(target == OPTYPE.UPDATE){
							dictEntryInputWindow.hide();
						}
	  					form.reset();
						form.findField("dictEntry.dicttypeId").setValue(_dictTypeId);
	                }
	            });
	            
			},
			failure: function(form, action) {
				Ext.MessageBox.hide();
	           	Ext.Msg.show({
	                title : '失败提示',
	                msg : action.result.msg,
	                buttons : Ext.Msg.OK,
	                icon: Ext.MessageBox.ERROR
	            });
			}
		});
	}
	
	//添加数据字典项
	function onAddDictEntry(){
		if(_dictTypeId == null || _dictTypeId == ""){
			Ext.Msg.alert("友情提示", "请先选择数据字典类型.");
			return;
		}
		target = OPTYPE.ADD;
		showDictEntryInputWindow();
		Ext.getCmp("dictIdContainer").show();
		dictEntryInputWindow.setTitle("新增数据字典项");
		getDictEntryInputForm().getForm().reset();
		getDictEntryInputForm().getForm().findField("dictEntry.dicttypeId").setValue(_dictTypeId);
	}
	
	//修改数据字典项
	function onUpdateDictEntry(){
		var dictEntryRecord;
		var selectionModel = dictEntryGrid.getSelectionModel();
		var count = selectionModel.getCount();
		if(count != 1){
			Ext.Msg.alert("提示信息", "请选择一条记录进行修改！");
			return;
		}
		dictEntryRecord = selectionModel.getSelected();
		target = OPTYPE.UPDATE;
		showDictEntryInputWindow();
		Ext.getCmp("dictIdContainer").hide();
		dictEntryInputWindow.setTitle("修改数据字典项");
		initDictEntryInputForm(dictEntryRecord);
	}
	
	//初始化数据字典项录入表单
	function initDictEntryInputForm(dictEntryRecord){
		var values = [
			{id: 'dictEntry.dicttypeId', value: dictEntryRecord.get("dicttypeId")}, {id: 'dictEntry.dictId', value: dictEntryRecord.get("dictId")},
			{id: 'dictEntry.dictName', value: dictEntryRecord.get("dictName")}, {id: 'dictEntry.sortNo', value: dictEntryRecord.get("sortNo")},
			{id: 'dictEntry.memo', value: dictEntryRecord.get("memo")}, {id: 'dictEntryStatusCombo', value: dictEntryRecord.get("status")}
		]
		var dictEntryInputForm = getDictEntryInputForm().getForm();
		dictEntryInputForm.setValues(values);
	}
	
	//删除数据字典项
	function onDeleteDictEntry(){
		var selectionModel = dictEntryGrid.getSelectionModel();
		var count = selectionModel.getCount();
		if(count < 1){
			Ext.Msg.alert("友情提示", "请至少选择一条记录");
			return;
		}
    	Ext.Msg.confirm("确认消息框", "此操作将删除数据字典项信息，是否继续？", function(btn){
	 		if(btn == 'yes'){
				var records = selectionModel.getSelections();
				var dictIds = "";
				for(var i = 0;i < records.length;i++){
					dictIds += records[i].get("dictId");
					if((i + 1) < records.length){
						dictIds += ",";
					}
				}
				Common.showProcessMsgBox('数据删除中，请稍后...');
			    Ext.Ajax.request({
					url: sys.basePath + 'datadict/dictAction!deleteDictEntryBatch.do',
					timeout: 300000,
					params:{dictIds: dictIds, dicttypeId: _dictTypeId},
					success: function(response, options){
						Common.hideProcessMsgBox();
						var result = Ext.util.JSON.decode(response.responseText);
						if(result.success){
							Ext.Msg.show({
				                title : '成功提示',
				                msg : result.msg,
				                buttons : Ext.Msg.OK,
				                icon: Ext.MessageBox.INFO,
				                fn: function(){
				                	dictEntryStore.reload();
				                }
				            });
				         } else {
				         	Ext.Msg.show({
				                title : '失败提示',
				                msg : result.msg,
				                buttons : Ext.Msg.OK,
				                icon: Ext.MessageBox.ERROR,
				                fn: function(){
				                }
				            });
				         }
					},
					failure:function(){Ext.MessageBox.hide();return;}
				});
		 	}
		});	
	}

    function initPage () {
    	submitSearchDictTypeHandler();
    }
    </script>
</head>
  <body>
	<ext:ext onReady="initPage();">
	  	<ext:viewport layout="border" otherProperties="split: true">
	  		<ext:items>
	  		<ext:tabPanel region="center" border="false" frame="false" otherProperties="activeTab: 0">
	  			<ext:items>
	  				<ext:panel title="数据字典管理" layout="border" border="false" iconCls="icon_plugin">
	  					<ext:items>
	  						<%-------------------------------------------------------------------------------------------------
								定义查询条件面板  
							-------------------------------------------------------------------------------------------------%>
							<ext:formPanel var="queryFormPanel" id="queryFormPanel" title="查询条件" collapsible="true" 
								labelAlign="left" frame="true" region="north" height="66" border="false" 
								labelWidth="86" otherProperties="split: true" margins="3 0 0 0">
								<ext:items>
									<ext:container layout="column" defaults="{columnWidth: .27, layout: 'form'}" lazyInit="true">
										<ext:items>
											<ext:container lazyInit="true">
												<ext:items>
													<ext:textField id="query.dicttypeId" fieldLabel="字典类型编号" width="120">
													</ext:textField>
												</ext:items>
											</ext:container>
											<ext:container lazyInit="true">
												<ext:items>
													<ext:textField id="query.dicttypeName" fieldLabel="字典类型名称" width="120">
													</ext:textField>
												</ext:items>
											</ext:container>
											<ext:container layout="table" otherProperties="columnWidth: .3" lazyInit="true"
												defaults="{layout: 'form', border:false, columnWidth: 1.0}">
												<ext:items>
													<ext:button text="查询" iconCls="icon_search" style="margin-right:20px;" 
														handler="submitSearchDictTypeHandler" otherProperties="minWidth: Common.config.buttonWidth.defaultButton">
													</ext:button>
													<ext:button text="刷新字典缓存" iconCls="icon_refresh"
														handler="refreshDictCache" otherProperties="minWidth: Common.config.buttonWidth.defaultButton">
													</ext:button>
												</ext:items>
											</ext:container>
										</ext:items>
									</ext:container>
								</ext:items>
							</ext:formPanel>
							
							<%-------------------------------------------------------------------------------------------------
								定义字典类型列表  
							-------------------------------------------------------------------------------------------------%>
							<ext:gridPanel var="dictTypeGrid" id="dictTypeGrid" title="字典类型列表" sm="dictTypeSm" region="center" iconCls="icon_grid" 
								autoExpandColumn="dicttypeName" border="true" 
								listeners='{rowdblclick: onUpdateDictType, rowclick : function (thisGrid, rowIndex, ev) {
					           		var store = thisGrid.getStore();
					           		var dictTypeRecord = store.getAt(rowIndex);
					           		searchDictEntry(dictTypeRecord.get("dicttypeId"), dictTypeRecord.get("dicttypeName"));
					           	}}'>
								<%-- 定义字典类型数据源 --%>
								<ext:store var="dictTypeStore" remoteSort="true" sortInfo="{field: 'dicttypeId', direction: 'ASC'}"
									otherProperties="url: queryDictTypeUrl">
									<ext:jsonReader root="data" totalProperty="totalProperty"
										record="Ext.data.Record.create([
											{name : 'dicttypeId',type : 'string', mapping: 'dicttypeId'},
											{name : 'dicttypeName',type : 'string', mapping: 'dicttypeName'}
										])">
									</ext:jsonReader>
								</ext:store>
								<%-- 定义字典类型列表的列头 --%>
								<ext:columnModel id="dictTypeCm">
									<ext:rowNumberer></ext:rowNumberer>
									<ext:checkboxSelectionModel var="dictTypeSm"></ext:checkboxSelectionModel>
									<ext:columnModel header="字典类型编号" dataIndex="dicttypeId" width="220" sortable="true" 
										listFlag="false" lazyInit="true"></ext:columnModel>
									<ext:columnModel header="字典类型名称" dataIndex="dicttypeName" id="dicttypeName" width="260" 
										sortable="true" listFlag="false" lazyInit="true"></ext:columnModel>
								</ext:columnModel>
								<%-- 定义字典类型列表的分页工具栏 --%>
								<ext:pagingToolbar key="bbar" var="dictTypePagingBar" displayInfo="true" store="dictTypeStore" otherProperties="pageSize: pageSize">
									<ext:items>
										<ext:toolbarSeparator></ext:toolbarSeparator>
										<ext:toolbarButton text="增加" iconCls="icon_add" handler="onAddDictType"
											otherProperties="minWidth: Common.config.buttonWidth.minButton">
										</ext:toolbarButton>
										<ext:toolbarButton text="修改" iconCls="icon_edit" handler="onUpdateDictType"
											otherProperties="minWidth: Common.config.buttonWidth.minButton">
										</ext:toolbarButton>
										<ext:toolbarButton text="删除" iconCls="icon_delete" handler="onDeleteDictType"
											otherProperties="minWidth: Common.config.buttonWidth.minButton">
										</ext:toolbarButton>
									</ext:items>
									<ext:plugins>new Ext.ux.Page.pageSize({beforeText : '显示', afterText : '条' })</ext:plugins>
								</ext:pagingToolbar>
							</ext:gridPanel>
							
							<%-------------------------------------------------------------------------------------------------
								定义字典项列表  
							-------------------------------------------------------------------------------------------------%>
							<ext:gridPanel var="dictEntryGrid" id="dictEntryGrid" sm="dictEntrySm" region="south" iconCls="icon_grid" 
								autoExpandColumn="memo" border="true" listeners='{rowdblclick: onUpdateDictEntry}' collapsible="true"
								otherProperties="split: true, title: dictEntryGridTitle, height: (document.body.clientHeight - 140) / 2">
								<%-- 定义字典类型数据源 --%>
								<ext:jsonStore var="dictEntryStore" remoteSort="false" sortInfo="{field: 'sortNo', direction: 'ASC'}"
									root="data" otherProperties="url: queryDictEntryUrl">
									<ext:fields>
										{name : 'dicttypeId',type : 'string'},
										{name : 'dictId',type : 'string'},
										{name : 'dictName',type : 'string'},
										{name : 'status',type : 'string'},
										{name : 'sortNo',type : 'int'},
										{name : 'memo',type : 'string'}
									</ext:fields>
								</ext:jsonStore>
								<%-- 定义字典类型列表的列头 --%>
								<ext:columnModel id="dictEntryCm">
									<ext:rowNumberer></ext:rowNumberer>
									<ext:checkboxSelectionModel var="dictEntrySm"></ext:checkboxSelectionModel>
									<ext:columnModel header="字典项编号" dataIndex="dictId" width="140" sortable="true" listFlag="false" lazyInit="true"></ext:columnModel>
									<ext:columnModel header="字典项名称" dataIndex="dictName" width="240" sortable="true" listFlag="false" lazyInit="true"></ext:columnModel>
									<ext:columnModel header="状态" dataIndex="status" width="140" sortable="true" listFlag="false" lazyInit="true"
										renderer="function(value){return Common.renderer.dictRender(dictEntryStatusDictArray, value);}"></ext:columnModel>
									<ext:columnModel header="排序字段" dataIndex="sortNo" sortable="true" width="140" listFlag="false" lazyInit="true"></ext:columnModel>
									<ext:columnModel header="备忘" dataIndex="memo" id="memo" listFlag="false" lazyInit="true"></ext:columnModel>
								</ext:columnModel>
								<%-- 定义字典类型列表的分页工具栏 --%>
								<ext:pagingToolbar key="bbar" var="dictEntryPagingBar" displayInfo="true" store="dictEntryStore" 
									otherProperties="pageSize: dictEntryPageSize">
									<ext:items>
										<ext:toolbarSeparator></ext:toolbarSeparator>
										<ext:toolbarButton text="增加" iconCls="icon_add" handler="onAddDictEntry"
											otherProperties="minWidth: Common.config.buttonWidth.minButton">
										</ext:toolbarButton>
										<ext:toolbarButton text="修改" iconCls="icon_edit" handler="onUpdateDictEntry"
											otherProperties="minWidth: Common.config.buttonWidth.minButton">
										</ext:toolbarButton>
										<ext:toolbarButton text="删除" iconCls="icon_delete" handler="onDeleteDictEntry"
											otherProperties="minWidth: Common.config.buttonWidth.minButton">
										</ext:toolbarButton>
									</ext:items>
									<ext:plugins>new Ext.ux.Page.pageSize({beforeText : '显示', afterText : '条' })</ext:plugins>
								</ext:pagingToolbar>
							</ext:gridPanel>
	  					</ext:items>
	  				</ext:panel>
	  			</ext:items>
	  		</ext:tabPanel>
	  		</ext:items>
	  	</ext:viewport>
	  </ext:ext>
  </body>
  
</html>
    