<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/foundation/ext/jsp/taglib.jsp"%>
<%@ include file="/foundation/ext/jsp/workspace.jsp"%>
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>用户注册</title>
		<script type="text/javascript" src="./register.js"></script>
		<script type="text/javascript"
			src="<%=basePath%>foundation/ext/ext-plugins/TreeField.js"></script>
		<link rel="stylesheet" type="text/css"
			href="<%=basePath%>foundation/ext/css/style.css" />
		<script type="text/javascript">

	var orgRoot				= '0';
	var orgRootName			= '组织树';

    //数据字典
    var DataDict = {
    	sexArray			: <ncframework:dictWrite dicttypeId="COMMON_USER_SEX" type="array"/>,
   		statusArray			: <ncframework:dictWrite dicttypeId="SYS_USER_STATUS" type="array"/>,
   		provinceCodeArray	: <ncframework:dictWrite dicttypeId="COMMON_PROVINCE" type="array"/>
   	};

   	//本地转化
   	var LocalRenderer = {
   		sex				: function (sex) {
			return Common.renderer.dictRender (DataDict.sexArray, sex);
   		},
   		status			: function (status) {
			return Common.renderer.dictRender (DataDict.statusArray, status);
   		},
   		provincenumber	: function (value) {
			return Common.renderer.dictRender (DataDict.provinceCodeArray, value);
   		}
   	};


	Ext.onReady(function() {
		Ext.QuickTips.init();

		Register.showRegister();
	});
	
    </script>
	</head>
	<body>
			<div id="page">
				<div class="header"></div>
				<div class="main">
					<div class="reg-box">
						<h3>
							用户注册
						</h3>
						<div class="reg-form" style="padding-left:140px;">
							<table width="99%" style="border: 1px red solid;">
								<tr style="padding-left: 3px;">
									<td align="center" style="color: red;"><b>用户注册说明</b></td>
								</tr>	
								<tr>
									<td style="padding: 3px;">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;文化部科研项目申报平台是文化部科技司各类科技计划项目信息网络报送的统一入口。申报单位须仔细阅读如下申报说明，只有申报单位确认遵守如下约定才能进行申报帐号注册、项目信息报送。</td>
								</tr>	
								<tr>
									<td style="padding: 3px;">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;1.申报单位必须严格遵守国家有关信息保密的法律、法规，不能在本系统登录任何涉密信息。</td>
								</tr>	
								<tr>
									<td style="padding: 3px;">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;2.申报单位在帐号注册、项目信息报送过程中必须遵守国家有关网络使用、信息安全的法律规定。</td>
								</tr>		
								<tr>
									<td style="padding: 3px;">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;3.申报单位须仔细阅读相关科技计划的管理文件、办法，确认本单位具备相关科技计划的项目申报资格。</td>
								</tr>	
								<tr>
									<td style="padding: 3px;">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;4.申报单位必须保证各项注册信息的真实性、准确性，注册完成后，可直接返回登陆页面进行登录。</td>
								</tr>	
								<tr>
									<td style="padding: 3px;">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;5.申报单位在注册完成后须确保帐号信息的安全性，若发现任何非法使用帐号信息或存在安全隐患的情况，请立即通知管理单位。</td>
								</tr>	
                                <tr>
									<td style="padding: 3px;">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;6.<strong style="color: red;">2012年度申报中已注册用户可直接登录，无需重新注册。若忘记密码，请点击"找回密码"</strong>。
</td>
								</tr>
                                <tr>
									<td style="padding: 3px;">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;7.<strong style="color: red;">若未参加2012年度申报，本单位用户名已被注册，请与文化部信息中心联系，联系电话：01059881182，联系人：邢波。</strong></td>
								</tr>		
							</table>
						</div>
						<div class="reg-form" id="reg-form" style="padding-left:80px;"></div>
					</div>
					<div class="dot"></div>
				</div>
				<div class="foot">
					版权所有：中华人民共和国文化部
				</div>
			</div>
	</body>

</html>
