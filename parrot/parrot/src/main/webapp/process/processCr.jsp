<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="thorn" uri="/thorn"%>
<jsp:include page="/springTag/header.jmt"></jsp:include>

<script type="text/javascript">

	document.title = "Process - Create";
	
	function openNewFlow(type) {
		var url = sys.path + "/wf/cm/startNewProcess.jmt?key=" + type;
		var height = window.screen.availHeight - 50;
		var width = window.screen.availWidth - 50;
		
		window.open (url, "flowPage", 
				"height="+height+", width="+width+", top=0, left=0, toolbar=no, menubar=no, scrollbars=yes,resizable=no,location=no, status=yes");
	}
	
	
	Ext.onReady(function() {
		Ext.QuickTips.init();
		
		var projectSet = new Ext.form.FieldSet({
			title : "<span style='color:blue;font-size:15px;'>国家级非物质文化遗产代表性项目补助费申报</span>",
			html : "oooooooooooopppp",
			buttonAlign : "center",
			buttons : [{
				text : "点击发起申报",
				handler : function() {
					openNewFlow("project");
				}
			}]
		});
		
		var projectPanel = new Ext.Panel({
			height : 220,
			bodyStyle : "padding-top: 10px;",
			region : "north",
			border : false,
			layout : "fit",
			items : [ projectSet ]
		});
		
		var reseverSet = new Ext.form.FieldSet({
			region : "south",
			title : "<span style='color:blue;font-size:15px;'>国家级文化生态保护区补助费申报</span>",
			html : "oooooooooooopppp",
			buttonAlign : "center",
			buttons : [{
				text : "点击发起申报",
				handler : function() {
					openNewFlow("resever");
				}
			}]
		});
		
		var reseverPanel = new Ext.Panel({
			bodyStyle : "padding-top: 20px;",
			region : "center",
			border : false,
			layout : "fit",
			items : [ reseverSet ]
		});
		
		var viewport = new Ext.Viewport({
			border : true,
			layout : "border",
			items : [projectPanel, reseverPanel]
		});
		completePage();
	});	
	
</script>
<jsp:include page="../reference/footer.jsp"></jsp:include>