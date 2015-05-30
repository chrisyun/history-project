<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/foundation/ext/jsp/taglib.jsp"%>
<%@ include file="/foundation/ext/jsp/workspace.jsp"%>
<html>
  <head>
    <title>系统操作日志查询</title>
    <script type="text/javascript">
    var pageSize = Common.config.pageSize || 15;		//页大小
    var logDetailForm;		//日志详细信息表单
    var logDetailWindow;	//日志详细信息对话框
    
    var querySystemLogUrl = sys.basePath + "log/logAction!listSysOperateLog.do";

    var LocalDataDict = {
		operateTypeArray 	: <ncframework:dictWrite dicttypeId="SYS_LOG_OPERATE_TYPE" type="array" />,
		operateResultArray 	: <ncframework:dictWrite dicttypeId="SYS_LOG_OPERATE_RESULT" type="array" />,
		moduleArray 		: <ncframework:dictWrite dicttypeId="SYS_MODULE" type="array" />
   	};

	var LocalRenderer = {
		operateType: function(value){
			return Common.renderer.dictRender(LocalDataDict.operateTypeArray, value);
		},
		operateResult: function(value){
			return Common.renderer.dictRender(LocalDataDict.operateResultArray, value);
		},
		module: function(value){
			return Common.renderer.dictRender(LocalDataDict.moduleArray, value);
		}
	}

	var sm = new Ext.grid.CheckboxSelectionModel({singleSelect: true});
	
	//导出excel
	function onExcel(){
		var thisForm = queryFormPanel.getForm();
		if (!thisForm.isValid()) {
			return;
		}
		var operateDateBegin = thisForm.findField("query.operateDateBegin").getValue();
		var operateDateEnd = thisForm.findField("query.operateDateEnd").getValue();
		var operationType = Ext.get("query.operateType").dom.value;
		operateDateBegin = (operateDateBegin == "") ? "" : operateDateBegin.format("Y-m-d");
		operateDateEnd = (operateDateEnd == "") ? "" : operateDateEnd.format("Y-m-d");
		if(operateDateBegin != "" && operateDateEnd != ""){
			if(operateDateBegin > operateDateEnd){
				thisForm.findField("query.operateDateEnd").markInvalid("结束日期应该晚于开始日期！");
				return;
			}else if(new Date(operateDateBegin).add(Date.MONTH,2) < new Date(operateDateEnd)){
				thisForm.findField("query.operateDateBegin").markInvalid("只能导出时间差在两个月以内的系统操作日志数据！");
	            thisForm.findField("query.operateDateEnd").markInvalid("只能导出时间差在两个月以内的系统操作日志数据！");
				return;
			}
		}else {
			if(operateDateBegin == ""){
				thisForm.findField("query.operateDateBegin").markInvalid("开始日期不能为空，请选择！");
				return;
			}else{
				thisForm.findField("query.operateDateEnd").markInvalid("结束日期不能为空，请选择！");
				return;
			}
		}
		Ext.getDom("excelIframe").src = sys.basePath + 'log/logAction!exportSysOperateLog.do?operateDateBegin=' 
			+ operateDateBegin + '&endTime=' + operateDateEnd + '&operationType=' + operationType 
			+ '&radom=' + Math.random();
	}

	//查询提交事件
	function onSubmitQueryHandler(){
		var thisForm = queryFormPanel.getForm();
		if (!thisForm.isValid()) {
			return;
		}
		var operateDateBegin = thisForm.findField("query.operateDateBegin").getValue();
		var operateDateEnd = thisForm.findField("query.operateDateEnd").getValue();
		var operationType = Ext.get("query.operateType").dom.value;
		operateDateBegin = (operateDateBegin == "") ? "" : operateDateBegin.format("Y-m-d");
		operateDateEnd = (operateDateEnd == "") ? "" : operateDateEnd.format("Y-m-d");
		if(operateDateBegin != "" && operateDateEnd != ""){
			if(operateDateBegin > operateDateEnd){
				thisForm.findField("query.operateDateEnd").markInvalid("结束日期应该晚于开始日期！");
				return;
			}
		}

		var store = systemLogGrid.getStore();
		var pagingBar = systemLogGrid.bbar;

		store.baseParams = {
			'operateDateBegin': operateDateBegin, 'operateDateEnd': operateDateEnd, 'operationType': operationType 
		};
		store.load({params:{start: 0, limit: systemLogGridPagingbar.pageSize}});
	}

	function onResetQueryHandler () {
		var thisForm = queryFormPanel.getForm();
		thisForm.reset();
	}

	/**
	* 获取日志详细表单
	*/
	function getLogDetailForm () {
		if (logDetailForm != null) {
			return logDetailForm;
		}
		logDetailForm = new Ext.form.FormPanel({
	    	region: 'center',
	        collapsible: true,
	        frame: true,
	        border: false,
	        autoHeight: true,
	        labelAlign: 'right',
	        autoScroll: true,
	        layout: 'form',
	        labelWidth: 80,
	        items: [{
	            layout: 'column',border: false, labelSeparator: ':',
	            defaults:{layout: 'form', border: false, columnWidth: .5}, 
	            items: [{
	              	items: [{
	                    xtype: "textfield",
	                    fieldLabel: '操作人',
						anchor: Common.config.fieldAnchor,
						id : 'detail.operator'
	              	}]
	            }, {
	              	items: [{
						xtype: 'textfield',
						fieldLabel : '操作类型',
						anchor: Common.config.fieldAnchor,
						id : 'detail.operateType'
					}]
	            }, {
	              	items: [{
	                    xtype: "textfield",
	                    fieldLabel: '操作时间',
						anchor: Common.config.fieldAnchor,
						id : 'detail.operateTime'
	              	}]
	            }, {
	              	items: [{
	                    xtype: "textfield",
	                    fieldLabel: '操作结果',
						anchor: Common.config.fieldAnchor,
						id : 'detail.operateResult'
	              	}]
	            }, {
	              	items: [{
	                    xtype: "textfield",
	                    fieldLabel: '所属应用',
						anchor: Common.config.fieldAnchor,
						id : 'detail.app'
	              	}]
	            }, {
	              	items: [{
	                    xtype: "textfield",
	                    fieldLabel: '所属模块',
						anchor: Common.config.fieldAnchor,
						id : 'detail.module'
	              	}]
	            }, {
	            	columnWidth: 1.0,
	              	items: [{
	                    xtype: "textfield",
	                    fieldLabel: '类名',
						anchor: Common.config.dfieldAnchor,
						id : 'detail.className'
	              	}]
	            }, {
	              	items: [{
	                    xtype: "textfield",
	                    fieldLabel: '方法名',
						anchor: Common.config.fieldAnchor,
						id : 'detail.methodName'
	              	}]
	            }, {
	              	items: [{
	                    xtype: "textfield",
	                    fieldLabel: '方法描述',
						anchor: Common.config.fieldAnchor,
						id : 'detail.methodDescription'
	              	}]
	            }, {
	            	columnWidth: 1.0,
	              	items: [{
	                    xtype: "textarea",
	                    fieldLabel: '方法参数',
						anchor: Common.config.dareaAnchor,
						height: 76,
						id : 'detail.methodParameters'
	              	}]
	            }, {
	            	columnWidth: 1.0,
	              	items: [{
	                    xtype: "textarea",
	                    fieldLabel: '备注',
						anchor: Common.config.dareaAnchor,
						height: 70,
						id : 'detail.remark'
	              	}]
	            }]
	    	}]
	    });
		return logDetailForm;
	}

	/**
	* 显示日志详细信息对话框
	*/
	function showLogDetailWindow () {
		if (logDetailWindow == null) {
			var windowWidth = document.body.clientWidth > 860 ? 860 : document.body.clientWidth - 10;
			var windowHeight = document.body.clientHeight > 340 ? 340 : document.body.clientHeight - 10;
			var localLogDetailForm = getLogDetailForm();
			//日志详情窗口
			logDetailWindow = new Ext.Window({ //定义对话框
		        width: windowWidth,
		        height: windowHeight,
		        title: "详细日志",
		        closeAction: 'hide',
		        modal: true,
		        autoHeight: false,
		        closable: true,
		        layout: 'fit',
		        shadow: true,
		        items: [localLogDetailForm]
		    });
		}
		logDetailWindow.show();	
	}
  	
  	//显示日志详细信息的窗口，并填充窗口数据
    function showDetail(){
        var selectedRecord = systemLogGrid.getSelectionModel().getSelected();
        showLogDetailWindow();
		logDetailForm.getForm().reset();
		var values = [
       		{id: 'detail.operateType', value: LocalRenderer.operateType(selectedRecord.get("operateType"))},
       		{id: 'detail.operateTime', value: Common.renderer.timeRender(selectedRecord.get("operateTime"))},
       		{id: 'detail.operateResult', value: LocalRenderer.operateResult(selectedRecord.get("operateResult"))},
       		{id: 'detail.app', value: selectedRecord.get("app")},
       		{id: 'detail.module', value: LocalRenderer.module(selectedRecord.get("module"))},
       		{id: 'detail.className', value: selectedRecord.get("className")},
       		{id: 'detail.methodName', value: selectedRecord.get("methodName")},
       		{id: 'detail.methodDescription', value: selectedRecord.get("methodDescription")},
       		{id: 'detail.methodParameters', value: selectedRecord.get("methodParameters")},
       		{id: 'detail.operator', value: selectedRecord.get("operator")},
       		{id: 'detail.remark', value: selectedRecord.get("remark")}
       	];
		logDetailForm.getForm().setValues(values);
	};
	//加载页面数据
	function initPage () {
		onSubmitQueryHandler();
	};

  </script>
  </head>
  <body>
  	<ext:ext onReady="initPage();">
		<ext:viewport layout="border" otherProperties="split: true">
			<ext:items>
				<ext:tabPanel region="center" border="false" frame="false"
					otherProperties="activeTab: 0">
					<ext:items>
						<ext:panel layout="border" border="false" title="系统操作日志查询"
							iconCls="icon_plugin" otherProperties="split: true">
							<ext:items>
								<%-- 查询面板  --%>
								<ext:formPanel var="queryFormPanel" id="queryFormPanel"
									title="查询条件" floating="false" buttonAlign="right" height="62"
									autoShow="true" header="true" labelAlign="left"
									collapsible="true" region="north" frame="true"
									hideBorders="false" labelWidth="60" margins="3 0 0 0"
									otherProperties="split: true">
									<ext:items>
										<ext:panel layout="column" defaults="{layout: 'form'}">
											<ext:items>
												<ext:panel width="320">
													<ext:items>
														<ext:dateField fieldLabel="操作时间"
															name="query.operateDateBegin"
															id="query.operateDateBegin" width="100" format="Y-m-d"
															allowBlank="true" cls="float_left" itemCls="float_left"
															clearCls="allow_float" emptyText="请选择开始日期"
															otherProperties="shadow : true,editable: false ,maxValue: new Date(), 
															value: new Date().add(Date.MONTH,-1)">
														</ext:dateField>
														,{xtype: 'label', text: '至', cls: 'float_left',itemCls: 'float_left', style: 'font-size: 12px; margin: 3 3 0 4px;display:inline;'}
														<ext:dateField hideLabel="true"
															name="query.operateDateEnd"
															id="query.operateDateEnd" width="100" format="Y-m-d"
															allowBlank="true" cls="float_left" itemCls="float_left"
															clearCls="allow_float" emptyText="请选择结束日期"
															otherProperties="shadow : true,editable: false,
															maxValue: new Date(), value: new Date()">
														</ext:dateField>
													</ext:items>
												</ext:panel>
												<ext:panel width="220">
													<ext:items>
														<ext:comboBox fieldLabel="操作类型" name="query.operateType"
															displayField="text" mode="local" triggerAction="all"
															selectOnFocus="true" forceSelection="true" width="130"
															valueField="value" value="" editable="false" hiddenName="query.operateType"
															store="new Ext.data.SimpleStore({
																	fields : ['value', 'text'],
																	data : Common.config.nullArray.concat(LocalDataDict.operateTypeArray)
																})">
														</ext:comboBox>
													</ext:items>
												</ext:panel>
												<ext:panel width="200" layout="table" defaults="{layout:'column', columnWidth: .3}"
													buttonAlign="center">
													<ext:items>
														<ext:button text="查询" iconCls="icon_search" handler="onSubmitQueryHandler"
															otherProperties="minWidth: Common.config.buttonWidth.defaultButton">
														</ext:button><%--
														<ext:button text="重置" iconCls="icon_reset" handler="onResetQueryHandler"
															otherProperties="minWidth: Common.config.buttonWidth.defaultButton">
														</ext:button>
													--%></ext:items>
												</ext:panel>
											</ext:items>
										</ext:panel>
									</ext:items>
								</ext:formPanel>
								
								<%-- 系统操作日志列表面板   --%>
								<ext:gridPanel var="systemLogGrid" title="系统操作日志列表(<span style='color:blue;'>双击查看详情</span>)"
									region="center" collapsible="false" iconCls="icon_grid"
									border="true" otherProperties="split:true" loadMask="true"
									enableColumnMove="false" stripeRows="false"
									viewConfig="{forceFit: true}" selModel="sm"
									listeners="{celldblclick : function ( thisGrid, rowIndex, columnIndex, ev ) {showDetail();}}">
									<ext:store var="systemLogStore" remoteSort="true"
										sortInfo="{field: 'operateTime', direction: 'DESC'}"
										otherProperties="url: querySystemLogUrl">
										<ext:jsonReader root="data" totalProperty="totalProperty"
											record="Ext.data.Record.create([
												{name : 'logId', type : 'string', mapping: 'logId'},
												{name : 'operateType', type : 'string', mapping: 'operateType'},
												{name : 'operateTime', type : 'string', mapping: 'operateTime'},
												{name : 'operateResult', type : 'string', mapping: 'operateResult'},
												{name : 'app', type : 'string', mapping: 'app'},
												{name : 'module', type : 'string', mapping: 'module'},
												{name : 'className', type : 'string', mapping: 'className'},
												{name : 'methodName', type : 'string', mapping: 'methodName'},
												{name : 'methodParameters', type : 'string', mapping: 'methodParameters'},
												{name : 'methodDescription', type : 'string', mapping: 'methodDescription'},
												{name : 'operator', type : 'string', mapping: 'operator'},
												{name : 'remark', type : 'string', mapping: 'remark'}
											])">
										</ext:jsonReader>
									</ext:store>
									<%-- 定义列表的列头--%>
									<ext:columnModel>
										<ext:rowNumberer></ext:rowNumberer>,
					       				       {header : '所属模块',id: 'module',dataIndex: 'module',width: 70, renderer: LocalRenderer.module}, 
					       				       {header : '操作时间',id: 'operateTime',dataIndex: 'operateTime',width: 120, renderer: Common.renderer.timeRender}, 
					       				       {header : '操作人',id: 'operator',dataIndex: 'operator',width: 90}, 
					       				       {header : '操作方法',id: 'methodDescription', dataIndex: 'methodDescription',width: 160}, 
					       				       {header : '操作类型',id: 'operationType',dataIndex: 'operateType',width: 80 ,renderer: LocalRenderer.operateType}, 
					       				       {header : '操作结果',id: 'operateResult',dataIndex: 'operateResult',width: 60 ,renderer: LocalRenderer.operateResult}
		       						</ext:columnModel>
									<%-- 定义应用列表的分页工具栏--%>
									<ext:pagingToolbar key="bbar" displayInfo="true"
										store="systemLogStore" var="systemLogGridPagingbar"
										otherProperties="pageSize: pageSize">
										<ext:items>
											<ext:toolbarSeparator></ext:toolbarSeparator>
											<ext:toolbarSeparator></ext:toolbarSeparator>
											<ext:toolbarButton text="导出Excel" iconCls="icon_page_excel" handler="onExcel"
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
	<iframe style='width:0%; height:0%;' frameborder='0' scrolling='auto' id='excelIframe'/>
  </body>
</html>