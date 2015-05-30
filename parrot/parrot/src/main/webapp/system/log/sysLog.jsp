<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="thorn" uri="/thorn"%>
<jsp:include page="/springTag/header.jmt"></jsp:include>

<script type="text/javascript">

	document.title = "SysLog - Page";
	var logLevelUrl = sys.path + "/log/getLogLevel.jmt";
	var LogModifyUrl = sys.path + "/log/modifyLogLevel.jmt";
	
	var userPermission = {
		MODIFY : '<sec:authorize url="/log/modifyLogLevel.jmt">true</sec:authorize>'
	};
	
	Ext.onReady(function() {
		Ext.QuickTips.init();
		
		var level_attr = {
			title : "调整日志级别",
			region : "north",
			margins : "2 0 0 0",
			height : 70,
			labelWidth : 100
		};
		var level_form = new FormUtil(level_attr);	
		
		var level = {
			xtype : "combo",
			id : "show_level",
			hiddenName : "level",
			triggerAction : "all",
			resizable : true,
			lazyInit : true,
			mode : "remote",
			width : 120,
			emptyText : "---选择日志级别---",
			fieldLabel : "日志级别",
			valueField : "id",
			displayField : "name",
			store : new Ext.data.Store({
						url : logLevelUrl,
						reader : new Ext.data.JsonReader({}, Ext.data.Record
										.create([{
													name : 'id',
													type : 'string'
												}, {
													name : 'name',
													type : 'string'
												}]))
					})
		};		
		level_form.addComp(level, 0.3, false);
		level_form.addComp(getSaveBtn(saveHandler), 0.3, true);
			
		function saveHandler() {
			var form = level_form.getForm();

			if (!form.isValid()) {
				return;
			}
			
			var ajaxClass = new AjaxUtil(LogModifyUrl);
			ajaxClass.submit(form, null, true);
		}
		
		var logPanel = new Ext.Panel({
		    title : "系统日志",
		    iconCls : "silk-grid",
			region : "center",
			margins : "2 0 2 0",
			html : "<iframe src='"+ sys.path + "/system/log/sysLogView.jsp' width='100%' height='100%' frameborder='0'></iframe>"
		});
		
		if(userPermission.MODIFY == "true") {
			var viewport = new Ext.Viewport({
				border : false,
				layout : "border",
				items : [level_form.getPanel(), logPanel]
			});
		} else {
			var viewport = new Ext.Viewport({
				border : false,
				layout : "border",
				items : [logPanel]
			});
		}
		
		completePage();
	});
</script>
<jsp:include page="../../reference/footer.jsp"></jsp:include>