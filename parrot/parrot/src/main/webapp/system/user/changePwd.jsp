<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="thorn" uri="/thorn"%>
<jsp:include page="/springTag/header.jmt"></jsp:include>

<script type="text/javascript" src="userPwd.js"></script>
<script type="text/javascript">

	document.title = "Change - MyPassword";
	
	Ext.onReady(function() {
		Ext.QuickTips.init();
		
		var Pwd_Obj = new UserPwd("my");
		
		Pwd_Obj.show();
		
		var html = "<table width=\"70%\" align=\"center\" style=\"font-size: 15px;color: red;margin-top: 30px;\">";
		html += "<tr><td>密码强度说明：</td></tr>";
		html += "<tr><td>&nbsp;&nbsp;&nbsp;&nbsp;1、密码长度为6至15位。</td></tr>";
		html += "<tr><td>&nbsp;&nbsp;&nbsp;&nbsp;2、密码格式为数字、小写字母、大写字母、特殊符号中的两种或两种以上。</td></tr>";
		html += "<tr><td>示例：<span style=\"color: blue;\">hjko239</span>（一般）、";
		html += "<span style=\"color: blue;\">hji6k9#</span>（极佳）、";
		html += "<span style=\"color: blue;\">ghuy@23ASD</span>（非常好）</td></tr></table>";
		
		var panel = new Ext.Panel({
			title : "&nbsp;",
			bodyStyle : "padding-top: 7px;",
			region : "center",
			html : html,
			margins : "2 0 0 0"
		});
		
		var viewport = new Ext.Viewport({
			border : true,
			layout : "border",
			items : [panel]
		});
		completePage();
	});	
	
</script>
<jsp:include page="../../reference/footer.jsp"></jsp:include>