<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ page import="com.talkweb.security.SecurityHelper,com.talkweb.ncfw.entity.User,com.talkweb.security.UserSession" %>
<%@ include file="/foundation/ext/jsp/taglib.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	String extVersion = "2.0.2";
 %>

<link rel="stylesheet" type="text/css" href="<%=basePath%>foundation/ext/ext-<%=extVersion%>/resources/css/ext-all.cssgz" />
<link rel="stylesheet" type="text/css" id="choose-skine" href="<%=basePath%>foundation/ext/ext-<%=extVersion%>/resources/css/xtheme-gray.cssgz" />

<!-- 红皮肤 
<link rel="stylesheet" type="text/css" href="<%=basePath%>foundation/ext/ext-<%=extVersion%>/resources/css/xtheme-silverCherry.cssgz" />-->
<link rel="stylesheet" type="text/css" href="<%=basePath%>foundation/ext/css/base.css" />
<link rel="stylesheet" type="text/css" href="<%=basePath%>foundation/ext/css/button.css" />
<style type="text/css">
	body{
		font-size: 13px;
	}
	#loading{
        position:absolute;
        left:40%;
        top:40%;
        padding:2px;
        z-index:20001;
        height:auto;
    }
    #loading a {
        color:#225588;
    }
    #loading .loading-indicator{
        background:white;
        color:#444;
        font:bold 13px tahoma,arial,helvetica;
        padding:10px;
        margin:0;
        height:auto;
    }
    #loading-msg {
        font: normal 10px arial,tahoma,sans-serif;
    }
    .red{
		color: red;
	}
    .mind_tab {
    	font-size: 13px;
    }
    .mind_tab tr {
    	padding-top: 2px;
    	padding-bottom: 2px;
    }
</style>
<script type="text/javascript" src="<%=basePath%>foundation/ext/ext-<%=extVersion%>/ext-base.jsgz"></script>
<script type="text/javascript" src="<%=basePath%>foundation/ext/ext-<%=extVersion%>/ext-all.jsgz"></script>
<script type="text/javascript" src="<%=basePath%>foundation/ext/ext-plugins/PagingPlugin.js"></script>
<script type="text/javascript" src="<%=basePath%>foundation/ext/ext-plugins/ComboOverride.js"></script>
<script type="text/javascript" src="<%=basePath%>foundation/ext/js/ext-lang-zh_CN.js"></script>
<script type="text/javascript" src="<%=basePath%>foundation/ext/js/Common.js"></script>
<script type="text/javascript" src="<%=basePath%>foundation/js/Validate.js"></script>

<script type="text/javascript">
	var sys = {
		basePath	: "<%=basePath%>",
		extPath		: '<%=basePath%>foundation/ext/ext-<%=extVersion%>'
	};

	/** 本地化 */
	var locale = window.location.search ? Ext.urlDecode(window.location.search.substring(1)).locale : '';
	var head = Ext.fly(document.getElementsByTagName('head')[0]);
	if (locale) {
		Ext.fly('extlocale').set({
			src: sys.config.extPath + '/ext-lang-' + locale + '.js'
		});
	}
	
	Ext.BLANK_IMAGE_URL = sys.extPath + '/resources/images/default/s.gif';

	var $ = Ext.getCmp;
	<%
		UserSession userSession = SecurityHelper.getCurrentUser();
		boolean loginFlag 		= userSession != null;
		String username			= "";
		String mobile			= "";
		if (loginFlag) {
			username			= (userSession.getUser() == null) ? "" : userSession.getUser().getUsername();
			mobile				= (userSession.getUser() == null) ? "" : userSession.getUser().getMobile();
		}
	%>

	var LoginUser	= {
		mobile	: '<%=mobile%>'
	};

</script>
<div id="loading">
	<div class="loading-indicator">
		<img src="<%=basePath%>foundation/ext/images/extanim32.gif" width="31" height="31" style="margin-right:8px;float:left;vertical-align:top;" />
		系统加载中
		<br />
		<span id="loading-msg">加载样式和图片...</span>
	</div>
</div>
<script type="text/javascript">
	Ext.get('loading').remove();
</script>