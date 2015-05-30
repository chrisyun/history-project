<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<jsp:include page="/springTag/header.jmt"></jsp:include>
<%
	String path = request.getContextPath();
%>
	<div id="header">
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td width="600"><img src="<%=path %>/resources/images/local/title-y-l.jpg" width="600" height="95" /></td>
		    	<td background="<%=path %>/resources/images/local/title-y-m.jpg">&nbsp;</td>
		    	<td width="600"><img src="<%=path %>/resources/images/local/title-y-r.jpg" width="600" height="95" /></td>
		  	</tr>
		</table>
	</div>

<script type="text/javascript" src="<%=path %>/plugins/local/theme.js" ></script>
<script type="text/javascript">
	var tabNum = 0;
	var mainTab;
	document.title = "非物质文化遗产项目管理平台  - 首页";
	
	var leftTreeUrl = sys.path + "/resource/getLeftTree.jmt";
	var logoutUrl = sys.path + "/j_spring_security_logout";
	
	Ext.onReady(function() {
		Ext.QuickTips.init();

		var loader = new Ext.tree.TreeLoader( {
			url : leftTreeUrl
		});
		loader.on("beforeload", function(loader, node) {
			loader.baseParams.pid = node.id;
		});
		
		var sysMenuTree = new Ext.tree.TreePanel( {
		    border: false,
		    useArrows: true,
			rootVisible: false,
		 	loader: loader,
			root : new Ext.tree.AsyncTreeNode({
				text : "系统菜单",
				id: "SYS",
				leaf: false
			}),
			listeners : {
				"beforeappend" : function(tree, parent, node) {
					var sysMenu = menuPanel.findById("sysMenu");
					
					if(!sysMenu.isVisible()) {
						sysMenu.show();
						menuPanel.doLayout();
					}
					
					return true;
				}
			}
		});
		
		var navMenuTree = new Ext.tree.TreePanel( {
		    border: false,
		    useArrows: true,
			rootVisible: false,
			loader: loader,
			root : new Ext.tree.AsyncTreeNode({
				text : "应用菜单",
				id: "NAV",
				leaf: false
			})
		});
		
		var menuPanel = new Ext.Panel( {
			region : "west",
			title : "当前用户：" + user.userName,
			split : true,
			width : 200,
			minSize : 175,
			maxSize : 400,
			collapsible : true,
			margins : "2 0 2 0",
			layout : {
				type : "accordion",
				animate : true
			},
			items : [ {
				title : "应用菜单",
				border : false,
				iconCls : "silk-nav",
				items : [navMenuTree]
			}, {
				title : "系统菜单",
				border : false,
				id : "sysMenu",
				hidden : true,
				iconCls : "silk-settings",
				items : [sysMenuTree]
			} ],
			tbar : [ {
				text : "选项",
				iconCls : "silk-application-view-list",
				menu : new Ext.menu.Menu( {
					items : [ {
						text : "更换皮肤",
						hidden : true,
					 	menu: {
               				items: skinItemArray
               			}
					}, {
						text : "注销",
						iconCls : "silk-logout",
						handler : logout
					} ]
				})
				}, "-", {
					text : "刷新",
					iconCls : "silk-table-refresh",
					handler: function(){
						sysMenuTree.getRootNode().reload();
						navMenuTree.getRootNode().reload();
	    			}
				}]
		});

		mainTab = new Ext.TabPanel( {
			region : "center",
			activeTab : 0,
			margins : "2 0 2 0",
			resizeTabs : true,
			border : false,
			minTabWidth : 200,
			items : [ {
				title : "公&nbsp;&nbsp;&nbsp;&nbsp;告",
				iconCls : "",
				html : "<iframe src='"+ sys.path +"/AlertNotice.html' width='100%' height='100%' frameborder='0'></iframe>"
			}, {
				title : "我的待办",
				iconCls : "silk-home",
				html : "<iframe src='"+ sys.path +"/process/processTodo.jsp' width='100%' height='100%' frameborder='0'></iframe>"
			}]
		});

		var treeClick = function(node, ev){
			var isLeaf = node.leaf;
			var openUrl = sys.path + node.attributes.targetUrl;
			
			if(isLeaf == false || Ext.isEmpty(openUrl)) {
				return ;
			}

			if(!Ext.isEmpty(openUrl)){
    			var prefix = "?";
    			if (openUrl.indexOf("?") > -1) {
    				prefix = "&";
        		}
    			openUrl = openUrl + prefix + "random=" + Math.random();
           	}
			
			setActivate(openUrl, node);
		};
		
		navMenuTree.on("click",function(node, ev){
			treeClick(node,ev);
		});
		
		navMenuTree.on("beforeappend",function(tree, pnode, node){
			if(node.id == "PROCESSCPCR") {
				//user.role.indexOf("PROVINCEUSER") >= 0 || 
				if(user.role.indexOf("CENTRALUSER") >= 0) {
					return false;
				}
			}
		});

		sysMenuTree.on("click",function(node, ev){
			treeClick(node,ev);
       	});
		
		var viewport = new Ext.Viewport( {
			layout : "border",
			items : [ {
				region : "north",
				height : 97,
				contentEl: "header"
			}, menuPanel, mainTab/*, {
				region : "south",
				height : 50,
				html : "this is the title"
			} */]
		});
		
		completePage();
	});

	function logout() {
		var rt = Ext.Msg.show( {
			buttons : Ext.Msg.YESNO,
			maxWidth : 200,
			minWidth : 300,
			msg : "<div style='margin: 5 5 8 15px;'>您确定退出本系统?</div>",
			title : "注销提示",
			icon : Ext.Msg.WARNING,
			fn : function(btn) {
				if ("yes" == btn) {
					window.location.href = logoutUrl;
				}
			}
		});
	}
	
	function setActivate(openUrl, node) {
		var activateId = mainTab.getActiveTab().getItemId();
		
		if(node != null && activateId == node.id) {
			return ;
		}
		
		if(mainTab.getItem(node.id) != null) {
			mainTab.remove(node.id);
			tabNum--;
		}
		
		if(tabNum < 2) {
			tabNum++;
		} else {
			mainTab.remove(1);
		}
		
		mainTab.add( {
			title : node.text,
			html : "<iframe src='"+ openUrl +"' width='100%' height='100%' frameborder='0'></iframe>",
			iconCls : node.attributes.iconCls,
			id : node.id,
			closable : true,
			listeners : {beforeclose:function(){
				tabNum--;
			}}	
		});
		mainTab.activate(node.id);
	}
	
</script>
	
<jsp:include page="../reference/footer.jsp"></jsp:include>
