<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page language="java" pageEncoding="UTF-8"%>
<%@page import="com.talkweb.security.SecurityHelper"%>
<%@page import="com.talkweb.ncfw.entity.Org"%>
<%@page import="com.talkweb.ncfw.action.OrgAction"%>
<%@ include file="/foundation/ext/jsp/taglib.jsp"%>
<%@ include file="/foundation/ext/jsp/workspace.jsp"%>
<%
	String flowid = request.getParameter("flowid") == null?"":request.getParameter("flowid");
%>
<html>
<head>
	<title>项目申报</title>
	<link rel="stylesheet" type="text/css" href="<%=basePath%>foundation/ext/ext-plugins/lovcombo-1.0/css/Ext.ux.form.LovCombo.css"/>
	<script type="text/javascript" src="<%=basePath%>foundation/ext/ext-plugins/lovcombo-1.0/js/Ext.ux.form.LovCombo.js"></script>
	<script type="text/javascript" src="<%=basePath%>foundation/ext/ext-plugins/TreeField.js"></script>
	<script type="text/javascript" src="<%=basePath%>foundation/ext/ext-plugins/SearchField.js"></script>
	<link rel="stylesheet" type="text/css" href="<%=basePath%>foundation/ext/ext-plugins/file-upload.css" />
	<script type="text/javascript" src="<%=basePath%>foundation/ext/ext-plugins/FileUploadField.js"></script>
	
	
	<script type="text/javascript">

	var flowid		   = '<%=flowid%>';
	var queryUrl       = sys.basePath + "switchAction!searchStatus.do";
	var whbkjcx       	= sys.basePath + "business/project/WHBKJCXXM.jsp";
	var gjwhkjts       	= sys.basePath + "business/project/GJWHKJTSJHXM.jsp";
	var gjwhcxgc      	= sys.basePath + "business/project/GJWHCXGC.jsp";
	var whbcxj       = sys.basePath + "business/project/WHBCXJ.jsp";
	var bzhky       = sys.basePath + "business/project/BZHKYXM.jsp";

	function initPage(){
		if(flowid == "") {
			document.ggetElementById("show").innerHTML = "缺少请求参数，请重试！";
			return ;
		}

		Common.showProcessMsgBox('查询项目申报状态，请稍后...');
		Ext.Ajax.request({
			method:'POST', 
			timeout:0,
			url : queryUrl,
			params : {
				flowid : flowid
			},
			success : function(response, options) {
				var result = Ext.util.JSON.decode(response.responseText);
				Ext.MessageBox.hide();
				if (result.success) {
					doResult(result.msg);
   			   	} else {
   			   		Ext.Msg.show({
       	                title: '失败提示',
       	                msg: result.msg,
       	                width: 180,
       	                modal: false,
       	                buttons : Ext.Msg.OK,
       	                icon: Ext.MessageBox.ERROR
       	            });
       			}
			},
			failure : function() {
				Ext.MessageBox.hide();
   			   	Ext.Msg.show({
   	                title: '失败提示',
   	                msg: '查询项目申报状态时发生异常.',
   	                width: 180,
   	                modal: false,
   	                buttons : Ext.Msg.OK,
   	                icon: Ext.MessageBox.ERROR
   	            });
			}
		});
	}
	
	function doResult(status) {
		if(status.indexOf("start") > -1) {
			var url = "";
			
			if(flowid == "WHBKJCX") {
				url = whbkjcx;
			} else if(flowid == "GJWHKJTS") {
				url = gjwhkjts;
			} else if(flowid == "GJWHCXGC") {
				url = gjwhcxgc;
			} else if(flowid == "WHBCXJ") {
				url = whbcxj;
			} else if(flowid == "BZHKY") {
				url = bzhky;
			} 
			
			window.location = url;
		} else {
			document.getElementById("show").innerHTML = "您申报的项目已经过截止期！";
		}
	}


	
    </script>
</head>
  <body onload="initPage();">
    
    <table width="100%" align="center" style="font-size: 20px;color: red;">
    	<tr>
    		<td height="100px;">&nbsp;</td>
    	</tr>
    	<tr>
    		<td id="show" align="center"></td>
    	</tr>
    </table>
  </body>
  
</html>
