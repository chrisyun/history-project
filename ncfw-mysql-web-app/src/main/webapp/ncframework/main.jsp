<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="com.talkweb.security.SecurityHelper,com.talkweb.ncfw.entity.User,com.talkweb.security.UserSession" %>
<%@ include file="/foundation/ext/jsp/workspace.jsp"%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>文化部科研项目申报平台</title>
		<style type="text/css">
		    #rightToolNav {
		    	float:right;width:260px;z-index:999;position:relative;
		    	margin:18px 0px 0px 0px;
		    }
		    #rightToolNav li {
		    	float:right;padding-left:20px; margin:0 10 2 0px;
		    }
		    #rightToolNav li a {
		    	color:#fff;
		    }
		</style>
	</head>
	<body>
		<div id="header">
			 <table width="100%" border="0" cellspacing="0" cellpadding="0">
			  <tr>
			    <td width="664"><img src="<%=basePath %>foundation/ext/images/t-1.jpg" width="664" height="67" /></td>
			    <td background="<%=basePath %>foundation/ext/images/t-2.jpg">&nbsp;</td>
			    <td width="372"><img src="<%=basePath %>foundation/ext/images/t-3.jpg" width="372" height="67" /></td>
			  </tr>
			</table>
		</div>
	</body>
	<script type="text/javascript">
	
		var loginFlag = <%=loginFlag%>;
		if(!loginFlag){
			window.location.href = "<%=basePath%>login.jsp";
		}
		var currentUser = "<%=username%>";
		var currentUserInfo = '当前用户:&nbsp;&nbsp;' + currentUser;
		var nav = '提示';
		
		//var treeDataUrl = '<%=basePath%>ncframework/tree-data.json'; 
		var treeDataUrl = sys.basePath + "authAction!createMenuTree.do"; 
		
	    var menuTree = new Ext.tree.TreePanel( {
			// title: '菜单导航',
		    autoScroll:true,
		    border: false,
		    iconCls: 'nav',
		    useArrows: true,
			//width: 100,,
			rootVisible: false,
		 	loader: new Ext.tree.TreeLoader({
		 		dataUrl : treeDataUrl
		 	}),
		 	/**
		 	tbar: [{
	    		iconCls: 'icon_refresh',
	    		text: '刷新',
	    		handler: function(){
	    			menuTree.getRootNode().reload();
    			}
		 	}],
		 	*/
			root : new Ext.tree.AsyncTreeNode({
				text : "根目录",
				id: 'root',
				leaf: false
			})
			
		});
	    	
		menuTree.on('click',function(node, ev){
			var action = node.attributes.action;
       		if(!Ext.isEmpty(action) && action != "null"){
    			var prefix = '?';
    			if (action.indexof('?') > -1) {
    				prefix = '&'
        		}
           		Ext.getDom("x-workspace").src = sys.basePath + action + prefix + 'random=' + Math.random();
           	}
       	});
           
		Ext.onReady(function() {
		
			Ext.getDom('header').style.visibility = 'visible';
			//Ext.getDom('header').style.display = 'none';
			/*
			var centerArray = Ext.get('title').getCenterXY();
			var centerX = centerArray[0];
			var centerY = centerArray[1];
			
			Ext.getDom('title').style.margin = "8 0 5 " + centerX + "px";*/
			
			var skinItemArray = [{
				text: '传统',
				handler : function(){
					Ext.ux.ChangeSkine.change('default');
				}
			},{
				text: '银色',
				handler : function(){
					Ext.ux.ChangeSkine.change('gray');
				}
			}/**,{
				text: '绿色',
				handler : function(){
					Ext.ux.ChangeSkine.change('green');
				}
			},{
				text: '紫蓝色',
				handler : function(){
					Ext.ux.ChangeSkine.change('indigo');
				}
			},{
				text: '巧克力色',
				handler : function(){
					Ext.ux.ChangeSkine.change('chocolate');
				}
			},{
				text: '粉红色',
				handler : function(){
					Ext.ux.ChangeSkine.change('pink');
				}
			},{
				text: 'silverCherry',
				handler : function(){
					Ext.ux.ChangeSkine.change('silverCherry');
				}
			}*/];
			
			// layout start
			var viewport = new Ext.Viewport({
				layout: 'border',
				items: [{
					region: 'north',
					margins : '2 3 0 2',
					contentEl: 'header',
					height: 0,
					split : false
				}, {
					region: 'west',
		            id: 'west-panel',
		            title: currentUserInfo,
		            split: true,
		            width: 190,
		            minSize: 175,
		            maxSize: 400,
		            collapsible: true,
					margins : '1 0 3 2',
		            layout: 'accordion',
		            layoutConfig:{
		                animate:true
		            },
		            items: [{
						title : '导航菜单',
						iconCls : 'icon_nav',
						border : false,
						autoScroll: true,
						items : [menuTree]
					}],
	            	bbar : [{
						text : '选项',
						iconCls : 'icon_plugin',
						menu : new Ext.menu.Menu({
							items : [/**{
								text : '更换皮肤',
							 	menu: {
                       				items: skinItemArray
                       			}
							},*/ {
								text : '注销',
								iconCls : 'icon_logout',
								handler : logout
							}]
						})
					}]
		        },{
		        	region:'center',
		        	border: false,
					margins : '1 2 3 0',
					html: "<iframe src='<%=basePath%>business/project/projectPending.jsp' name='x-workspace' id='x-workspace' width='100%' height='100%' frameborder='0'></iframe>"
				}]
			});
		});
		
		// Ext.util.CSS.swapStyleSheet("theme", "<%=basePath%>foundation/ext/resources/css/xtheme-calista.css");
		
		function logout() {
			Ext.Msg.confirm('操作提示', '您确定要退出本系统?', function(btn) {
				if ('yes' == btn) {
					window.location.href = '<%=basePath%>logout';
					/**
					Ext.Ajax.request({
						url: '<%=basePath%>logout?ajax=true',
						success: function(response, options){
							var result = Ext.util.JSON.decode(response.responseText);
							if (result.success) {
								window.location = '<%=basePath%>';
							}
						}
					});
					*/
				}
			});
		}

		var task = {
			run: checkSessionExpire,
		   	interval: 15000		//15秒
		};
		var taskRunner = new Ext.util.TaskRunner();
		function checkSessionExpire(){
			Ext.Ajax.request({
				url: '<%=basePath%>checkSessionExpire.do',
				timeout: 6000,
				method: 'post',
				success: function(response, options){
					var result = Ext.util.JSON.decode(response.responseText);
					if (Ext.isEmpty(result.success)) {
						taskRunner.stop(task);
					}
					if (result.success) {
						taskRunner.stop(task);
						Ext.Msg.show({
							title: '提示信息',
						   	msg: 'session失效, 请重新登录!',
						   	buttons: Ext.Msg.OK,
						   	fn: function () {
						   		window.location.href = "<%=basePath%>login.jsp";
						   	},
						   	icon: Ext.MessageBox.INFO
						});
					}
					
				},
				failure: function(){
					taskRunner.stop(task);
				} 
			});
		}
		taskRunner.start(task);

	</script>
</html>

