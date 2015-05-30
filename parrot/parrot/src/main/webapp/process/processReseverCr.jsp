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
	
	var html = "<table width=\"90%\" align=\"center\" style=\"font-size: 14px;\">" +
		"<tr><td><strong>为确保申报规范，准确，请注意以下事项：</strong></td></tr>" +
		"<tr><td>&nbsp;&nbsp;&nbsp;&nbsp;</td></tr>" +
		"<tr><td>&nbsp;&nbsp;&nbsp;&nbsp;1、若申报过程中发现屏幕右侧滚动条无法显示，请调整计算机屏幕分辨率至1024*768或1366*768。</td></tr>" +
		
		"<tr><td>&nbsp;&nbsp;&nbsp;&nbsp;2、申报单位首次申报时，应详细填写基本信息，系统将自动记录申报单位基本信息，申报单位再次进行申报时，系统会自动显示基本信息，申报用户只需进行核对即可，若本单位基本信息变更，可直接进行修改。</td></tr>" +
		
		"<tr><td>&nbsp;&nbsp;&nbsp;&nbsp;3、填写完成基本信息后，点击下一步进入详细信息页面，根据工作实际填写补助申请理由、补助资金使用内容和年度目标及预期效益。若字数过多，可点击附件上传，直接上传word、pdf文件。</td></tr>" +
		
		"<tr><td>&nbsp;&nbsp;&nbsp;&nbsp;4、填写完成详细信息后，点击下一步进入金额及预算明细页面，首先填写项目支出明细预算，按照预算中列出的科目填写金额，单位为万元，在说明中填写简要支出说明，填写时可以通过下拉条进行下拉，系统会自动计算出合计，并在左侧申请金额中显示，无需自己填写，用户同时填写预算测算依据及说明，若字数太多，可点击附件上传，上传文字材料，word、pdf格式均可。</td></tr>" +
		
		"<tr><td>&nbsp;&nbsp;&nbsp;&nbsp;5、流程意见中填写意见一栏可以根据申报项目实际情况填写需提醒资料审核部门注意的事项，也可以选择不填写。</td></tr>" +
		
		"<tr><td>&nbsp;&nbsp;&nbsp;&nbsp;6、填写完毕后，认真核对填写内容，若无法一次填写完成可以保存为草稿，下次填写时，在“我的代办”中找到未完成的申报书，双击进行填写。若填写完成，点击送省厅审批按钮，完成资料提交。</td></tr>" +
		
		"<tr><td>&nbsp;&nbsp;&nbsp;&nbsp;7、在线申报完成后，请点击项目资金申报管理查询申报结果。待项目审批完成后，双击项目查看审批详细信息，点击导出文件按钮下载《国家级非物质文化遗产代表性项目补助费申报书》，即可打印申报书，导出文件请用word2003以上版本打开。</td></tr>";
	
	Ext.onReady(function() {
		Ext.QuickTips.init();
		
		var reseverSet = new Ext.form.FieldSet({
			region : "south",
			title : "<span style='color:blue;font-size:15px;'>国家级文化生态保护区补助费申报</span>",
			html : html,
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
			layout : "fit",
			items : [ reseverPanel]
		});
		completePage();
	});	
	
</script>
<jsp:include page="../reference/footer.jsp"></jsp:include>