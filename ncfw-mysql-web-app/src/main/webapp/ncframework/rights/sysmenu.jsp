<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page language="java" import="com.talkweb.ncframework.pub.utils.tree.TreeUtils" pageEncoding="UTF-8"%>
<%@ include file="/foundation/ext/jsp/taglib.jsp"%>
<%@ include file="/foundation/ext/jsp/workspace.jsp"%>
<html>
<head>
	<title>系统菜单管理</title>
	<script type="text/javascript">
    var getSystemMenuNodesUrl = sys.basePath + "rights/systemMenuAction!getSubMenuNodes.do";
    var editSystemMenuUrl     = sys.basePath + "rights/systemMenuAction!editSystemMenu.do";
    var querySubMenuListUrl   = sys.basePath + "rights/systemMenuAction!querySubMenuList.do";
    var deleteSystemMenuUrl   = sys.basePath + "rights/systemMenuAction!deleteSystemMenu.do";
    var getOneMenuUrl         = sys.basePath + "rights/systemMenuAction!getOneMenu.do";
    var treeRootId			  = "<%=TreeUtils.treeRootId%>";
    var pageSize = 15;
    var currentActiveNode;
	var currentOptype;					//当前操作类型

    //数据字典
    var DictData = {
   		menuLevelArray 	: [['0','0级'], ['1', '1级'], ['2', '2级'], ['3', '3级'], ['4', '4级'], ['5', '5级']],
   		isShowArray		: [['y', '是'], ['n', '否']],
   		isLeafArray 	: [['y', '是'], ['n', '否']],
   		isMenuArray 	: [['y', '是'], ['n', '否']],
   		openModeArray	: [['x-workspace', '普通链接']]
   	};

   	//本地转化
   	var LocalRenderer = {
   		isLeaf : function (isLeaf) {		//是否为叶子节点
   			return Common.renderer.dictRender (DictData.isLeafArray, isLeaf);
   		},
   		isShow : function (isShow) {		//是否显示
   			return Common.renderer.dictRender (DictData.isShowArray, isShow);
   		},
   		isMenu : function (isMenu) {		//是否可为菜单
   			return Common.renderer.dictRender (DictData.isMenuArray, isMenu);
   		}
   	};

  	//ajax请求对象
	function ExtAjax(url, params){
		var extObject = 
			Ext.Ajax.request({
				url: url,
				timeout: 60000,
				params: params,
				success: function(response, options){
					Common.hideProcessMsgBox();
					var result = Ext.util.JSON.decode(response.responseText);
					if(result.success){
						Ext.Msg.show({
			                title : '成功提示',
			                msg : result.msg,
			                buttons : Ext.Msg.OK,
			                icon: Ext.MessageBox.INFO,
			                fn: function(){
			                	var refreshNode = currentActiveNode.parentNode;
			  					menuTree.getLoader().load(refreshNode);
			  					refreshNode.expand();
								menuGrid.getStore().reload();
			                }
			            });
			         } else {
			         	Ext.Msg.show({
			                title : '失败提示',
			                msg : result.msg,
			                buttons : Ext.Msg.OK,
			                icon: Ext.MessageBox.ERROR,
			                fn: function(){
			                }
			            });
			         }
				},
				failure:function(){Common.hideProcessMsgBox();return;}
			});
	    return extObject;
	}

   	//查询事件
   	function onMenuSearch(){
   	   	var menuCode = $("query.MENUCODE").getValue();
   	   	var menuName = $("query.MENUNAME").getValue();
   	   	var menuLabel = $("query.MENULABEL").getValue();
   	   	if(currentActiveNode){
	   	 	menuGrid.setTitle("子菜单列表(当前父节点为："+currentActiveNode.text+")");
	   	 	var store = menuGrid.getStore();
	   	 	store.baseParams = {
	 			'parentMenuId': currentActiveNode.id,
	 			'menuCode' : menuCode,
	 			'menuName' : menuName,
	 			'menuLabelName' : menuLabel
	 		};
	 	    store.load({params:{start: 0, limit: systemMenuGridPagingbar.pageSize}});
   	   	}
   	}

	//菜单点击事件
    function treeNodeClick(node, ev){
    	queryMenuFormPanel.getForm().reset();
	    currentActiveNode = node;
	    var store = menuGrid.getStore();
	    menuGrid.setTitle("子菜单列表(当前父节点为："+currentActiveNode.text+")");
	    store.baseParams = {
			'parentMenuId': currentActiveNode.id
		};
	    store.load();
    }

    //按下修改按钮时触发
    function onUpdateMenu(){
        currentOptype = Common.optype.update;
        systemMenuWindow.show();
        systemMenuWindow.setTitle("修改系统菜单");
       	initUpdateValues();
       	
	}

	//修改系统菜单初始化表单
	function initUpdateValues(){
		var menuId = currentActiveNode.id;
		Ext.Ajax.request({
			url : getOneMenuUrl,
		   	method : 'POST',
		   	params: { menuId: menuId },
		   	success: function (response, options) {
				var menuObj = Ext.util.JSON.decode(response.responseText);
				if (menuObj == null) {
					Ext.Msg.alert("提示信息", "初始化系统菜单信息失败！");
					return;
				}
				var currentActiveParentNode = currentActiveNode.parentNode;
				var values = {
					parentMenuId : menuObj.parentMenuId, 
					menuId : menuObj.menuId,
					parentMenuLabel : currentActiveNode.parentNode.text,
					menuName : menuObj.menuName,
					menuLabelName : menuObj.menuLabelName,
					menuCode : menuObj.menuCode,
					isLeafComb : menuObj.isLeaf,
					menuEntry : menuObj.menuEntry,
					openModeComb : menuObj.openMode,
					displayOrder : menuObj.displayOrder,
					isDisplayComb : menuObj.isDisplay
				};
				menuForm.getForm().reset();
				menuForm.getForm().setValues(values);
		   	},
		   	failure: function () {
			   Ext.Msg.show({
	                title: '失败提示',
	                msg: '初始化系统菜单信息时发生异常.',
	                width: 180,
	                modal: false,
	                buttons : Ext.Msg.OK,
	                icon: Ext.MessageBox.ERROR
	            });
		   	}
		});
	}
	//菜单上右键删除时
	function onDeleteMenu(){
		Ext.Msg.confirm("确认信息:", "删除菜单同时将删除菜单下的所有子菜单,是否确定删除?",function(btn){
	 		if(btn == 'yes'){
	 			//currentActiveNode.parentNode.select();
	 			var menuId = currentActiveNode.id+",";
	    		var ajaxParams = {'menuIds': menuId};
				Common.showProcessMsgBox('正在删除系统菜单，请稍后...');
	    		ExtAjax(deleteSystemMenuUrl,ajaxParams);
	 		}
		});
	}

	//列表中选中删除时
	function deleteMenuGrid () {
		if (!menuGrid.getSelectionModel().hasSelection()) {
            Ext.Msg.alert("提示:", "请选择要删除的记录!");
            return;
        }
        
		Ext.Msg.confirm("友情提示:","将彻底删除选中的系统菜单,是否确定删除?",function(btn){
	 		if(btn == 'yes'){
	 			var selects = menuGrid.getSelectionModel().getSelections();
	                var menuIds = "";
	        		for(var i = 0;i < selects.length;i++){
	        			menuIds += selects[i].get("menuId");
	    				if((i + 1) < selects.length){
	    					menuIds += ",";
	    				}
	    		}
	    		var ajaxParams = {'menuIds': menuIds};
	    		Common.showProcessMsgBox('正在删除系统菜单，请稍后...');
	    		ExtAjax(deleteSystemMenuUrl,ajaxParams);
	 		}
		});
	}
	//增加一级菜单		
	function addTopMenu(){
        currentOptype = Common.optype.add;
		menuForm.getForm().reset();
		var values = [
			{id: 'parentMenuLabel', value: menuTree.root.text},
			{id: 'parentMenuId', value: menuTree.root.id}
		];
		systemMenuWindow.show();
		systemMenuWindow.setTitle("新增系统菜单");
		menuForm.getForm().setValues(values);
	}
	//修改或新增后保存	
	function onSaveMenu(){
		var thisForm = menuForm.getForm();
		if(!thisForm.isValid()){
			return;
		}
		if("y" == $("isLeafComb").getValue()){
			if("" == $("menuEntry").getValue()){
				$("menuEntry").markInvalid("叶子节点的页面链接地址不能为空！");
				return;
			}
		}
		Common.showProcessMsgBox();
        thisForm.submit({
			timeout: 60000,
			params: {'opType': currentOptype},
		 	success: function(form, action) {
				Common.hideProcessMsgBox();
		 		//dataGrid.getStore().reload();
		 		systemMenuWindow.hide();
				Ext.Msg.show({
	                title : '成功提示',
	                msg : action.result.msg,
	                buttons : Ext.Msg.OK,
	                icon: Ext.MessageBox.INFO,
	                fn: function(){
	  					form.reset();
	  					menuTree.getLoader().load(currentActiveNode);
	  					currentActiveNode.expand();
	                }
	            });
			},
			failure: function(form, action) {
				Ext.MessageBox.hide();
	           	Ext.Msg.show({
	                title : '失败提示',
	                msg : action.result.msg,
	                buttons : Ext.Msg.OK,
	                icon: Ext.MessageBox.ERROR
	            });
			}
        });
		
	}
    //增加子节点初始化
    function onAddSubMenu(){
        currentOptype = Common.optype.add;
        systemMenuWindow.show();
        systemMenuWindow.setTitle("新增系统菜单");
    	menuForm.getForm().reset();
	    $("parentMenuLabel").setValue(currentActiveNode.text);
	    $("parentMenuId").setValue(currentActiveNode.attributes.attributes.id);
	    
    }
    
    //关闭增加或修改对话框
    function onClose(){
    	systemMenuWindow.hide();
    	menuForm.getForm().reset();
    }


	//初始化页面信息
	function initPage () {
		var menuTop1 = new Ext.menu.Menu({
		 	items:[
		 		{text: '增加顶级菜单',iconCls : 'icon_add',handler: addTopMenu}
		 	]
		});
		var menuTop2 = new Ext.menu.Menu({
		 	items:[
			 	{text: '增加子菜单',iconCls : 'icon_add',handler: onAddSubMenu},
			 	'-',
			 	{text: '修改菜单',iconCls : 'icon_edit',handler: onUpdateMenu},
			 	'-',
			 	{text: '删除菜单',iconCls : 'icon_delete',handler: onDeleteMenu}
		 	]
		});
		var menuTop3 = new Ext.menu.Menu({
		 	items:[
			 	{text: '增加子菜单',iconCls : 'icon_add',handler: onAddSubMenu},
		 		'-',
		 		{text: '修改菜单',iconCls : 'icon_edit',handler: onUpdateMenu},
		 		'-',
		 		{text: '删除菜单',iconCls : 'icon_delete',handler: onDeleteMenu}
	 		]
		});
    	menuTree.on("contextmenu", function(node, ev){
            ev.preventDefault();
            node.select();
            currentActiveNode = node;
            treeNodeClick(node, ev);
            if (node.getDepth() == 0) {
            	menuTop1.showAt(ev.getXY());
            } else if (!node.attributes.leaf) {
            	menuTop2.showAt(ev.getXY());
            } else {
            	menuTop3.showAt(ev.getXY());
            }
       	});

		menuTree.on("click", treeNodeClick);
		menuTree.getRootNode().expand(false, false);	
	}
    </script>
</head>
  <body>
    <ext:ext onReady="initPage();">
	  	<ext:viewport layout="border" otherProperties="split: true">
	  		<ext:items>
	  		<ext:tabPanel region="center" border="false" frame="false" otherProperties="activeTab: 0">
	  			<ext:items>
  				<ext:panel title="菜单管理" layout="border" border="false" iconCls="icon_plugin">
  					<ext:items>
		  				<%-- 左边的菜单树 --%>
		   				<ext:treePanel var="menuTree" id="menuTree" title="系统菜单树" border="true" rootVisible="true" autoScroll="true"
		  					width="210" useArrows="false" margins="3 0 0 0" otherProperties="split: true" region="west" collapsible="true"
		  					loader="new Ext.tree.TreeLoader({dataUrl: getSystemMenuNodesUrl, listeners : { 
		  						'beforeload' : function (treeLoader, node) {
		  						}
		  					} })">
		      				<ext:asyncTreeNode key="root" text="系统菜单管理" leaf="false" expanded="true"
		      					otherProperties="id: treeRootId"></ext:asyncTreeNode>
		   				</ext:treePanel>
	   					<%-- 菜单列表面板 --%>
	   					<ext:panel var="menuListPanel" layout="border" border="false" otherProperties="split: true" region="center"
	   						margins="3 0 0 0">
	   						<ext:items>
	   							<%-------------------------------------------------------------------------------------------------
									定义菜单查询条件面板  
								-------------------------------------------------------------------------------------------------%>
								<ext:formPanel var="queryMenuFormPanel" id="queryFormPanel" title="查询条件" collapsible="true" 
									labelAlign="left" frame="true" region="north" height="100" border="false" 
									labelWidth="60" otherProperties="split: true">
									<ext:items>
										<ext:panel layout="column" defaults="{width: 260, layout: 'form'}">
											<ext:items>
												<ext:panel>
													<ext:items>
														<ext:textField id="query.MENUCODE" fieldLabel="菜单代码" width="150">
														</ext:textField>
														<ext:textField id="query.MENUNAME" fieldLabel="菜单名称" width="150">
														</ext:textField>
													</ext:items>
												</ext:panel>
												<ext:panel>
													<ext:items>
														<ext:textField id="query.MENULABEL" fieldLabel="显示名称" width="150">
														</ext:textField>
														<ext:button text="查询" iconCls="icon_search" handler="onMenuSearch"
															otherProperties="minWidth: Common.config.buttonWidth.defaultButton">
														</ext:button>
													</ext:items>
												</ext:panel>
											</ext:items>
										</ext:panel>
									</ext:items>
								</ext:formPanel>
								<%-------------------------------------------------------------------------------------------------
									定义菜单列表  
								-------------------------------------------------------------------------------------------------%>
								<ext:gridPanel var="menuGrid" id="menuGrid" region="center" iconCls="icon_grid" 
									border="true" otherProperties="split: true" title="子菜单列表" iconCls="icon_grid"
									viewConfig="{forceFit: true}" selModel="sm" autoExpandColumn="menuEntryCm">
									<%-- 定义菜单数据源--%>
									<ext:store var="menuStore" remoteSort="true"
										sortInfo="{field: 'displayOrder', direction: 'ASC'}"
										otherProperties="url: querySubMenuListUrl" >
										<ext:jsonReader root="data" totalProperty="totalProperty"
											record="Ext.data.Record.create([
												{name : 'menuId', type : 'string', mapping: 'menuId'},
												{name : 'menuName', type : 'string', mapping: 'menuName'},
												{name : 'menuLabelName', type : 'string', mapping: 'menuLabelName'},
												{name : 'menuCode', type : 'string', mapping: 'menuCode'},
												{name : 'menuEntry', type : 'string', mapping: 'menuEntry'},
												{name : 'isLeaf', type : 'string', mapping: 'isLeaf'},
												{name : 'openMode', type : 'string', mapping: 'openMode'},
												{name : 'isDisplay', type : 'string', mapping: 'isDisplay'},
												{name : 'parentMenuId', type : 'string', mapping: 'parentMenuId'}
											])">
										</ext:jsonReader>
									</ext:store>
									<%-- 定义菜单列表的列头--%>
									<ext:columnModel id="menuCm">
										<ext:rowNumberer></ext:rowNumberer>
										<ext:checkboxSelectionModel var="sm"></ext:checkboxSelectionModel>,
										{header : '菜单名称', width:100, dataIndex:'menuName' },
										{header : '菜单代码', width:100, dataIndex:'menuCode'}, 
										{header : '是否叶子菜单', width:100, dataIndex:'isLeaf', renderer: LocalRenderer.isLeaf}, 
										{header : '是否显示', width:66, dataIndex: 'isDisplay', renderer: LocalRenderer.isShow},
										{header : '菜单访问入口', width:180, id: 'menuEntryCm', dataIndex:'menuEntry'}
									</ext:columnModel>
									<%-- 定义应用列表的分页工具栏--%>
									<ext:pagingToolbar key="bbar" displayInfo="true"
										store="menuStore" var="systemMenuGridPagingbar"
										otherProperties="pageSize: pageSize">
										<ext:items>
											<ext:toolbarSeparator></ext:toolbarSeparator>
											<ext:toolbarButton text="删除" iconCls="icon_delete"
												handler="deleteMenuGrid"
												otherProperties="minWidth: Common.config.buttonWidth.minButton">
											</ext:toolbarButton>
										</ext:items>
										<ext:plugins>new Ext.ux.Page.pageSize({beforeText : '显示', afterText : '条' })</ext:plugins>
									</ext:pagingToolbar>
								</ext:gridPanel>
	   						</ext:items>
	   					</ext:panel>
   					</ext:items>
   				</ext:panel>
	  			</ext:items>
	  		</ext:tabPanel>
	  		</ext:items>
	  	</ext:viewport>
	  	<ext:window var="systemMenuWindow" id="systemMenuWindow"
			title="菜单信息" width="460" autoHeight="true" 
		 	layout="fit" shadow="true" closeAction="hide"
			modal="true" closable="true" resizable="false">
			<ext:items>
			<ext:formPanel var="menuForm" id="menuForm" region="center" autoHeight="true"
				labelAlign="right" frame="true" border="false"
				labelWidth="96" buttonAlign="left" otherProperties="split: true, url:editSystemMenuUrl">
				<ext:items>
					<ext:panel layout="form" defaults="{layout: 'form', anchor: '96%'}">
						<ext:items>
							<ext:hidden id="parentMenuId" name="parentMenuId"></ext:hidden>
							<ext:hidden id="menuId" name="menuId"></ext:hidden>
							<ext:textField id="parentMenuLabel" name="parentMenuLabel" fieldLabel="父菜单显示名称" disabled="true">
							</ext:textField>
							<ext:textField id="menuName" name="menuName" allowBlank="false"
								fieldLabel="<span class=required>*</span>菜单名称" maxLength="25">
							</ext:textField>
							<ext:textField id="menuLabelName" name="menuLabelName" allowBlank="false"
								fieldLabel="<span class=required>*</span>菜单显示名称" maxLength="25">
							</ext:textField>
							<ext:textField id="menuCode" name="menuCode" allowBlank="true" 
								fieldLabel="菜单代码" maxLength="40">
							</ext:textField>
							<ext:comboBox id="isLeafComb" fieldLabel="<span class=required>*</span>是否为叶子" value="y"
								hiddenName="isLeaf" 
								store="new Ext.data.SimpleStore({
									fields: ['value', 'text'], data: DictData.isMenuArray
								})"
								mode="local" triggerAction="all" valueField="value" displayField="text"
								readOnly="true" resizable="true" >
							</ext:comboBox>
							<ext:textField id="menuEntry" fieldLabel="菜单访问入口"
								maxLength="60">
							</ext:textField>
							<ext:comboBox id="openModeComb" fieldLabel="页面打开方式" value="x-workspace"
								hiddenName="openMode"
								store="new Ext.data.SimpleStore({
									fields: ['value', 'text'], data: DictData.openModeArray
								})"
								mode="local" triggerAction="all" valueField="value" displayField="text"
								readOnly="true" resizable="true">
							</ext:comboBox>
							<ext:numberField id="displayOrder" fieldLabel="显示顺序" maxLength="4">
							</ext:numberField>
							<ext:comboBox id="isDisplayComb" fieldLabel="是否显示" value="y"
								hiddenName="isDisplay"
								store="new Ext.data.SimpleStore({
									fields: ['value', 'text'], data: DictData.isShowArray
								})"
								mode="local" triggerAction="all" valueField="value" displayField="text"
								readOnly="true" resizable="true">
							</ext:comboBox>
						</ext:items>
					</ext:panel>
				</ext:items>
			</ext:formPanel>
			</ext:items>
			<ext:buttons>
				<ext:button text="保存" iconCls="icon_save" handler="onSaveMenu"
					otherProperties="minWidth: Common.config.buttonWidth.defaultButton">
				</ext:button>
				<ext:button text="关闭" iconCls="icon_close" handler="onClose"
					otherProperties="minWidth: Common.config.buttonWidth.defaultButton">
				</ext:button>
			</ext:buttons>
		</ext:window>
    </ext:ext>
  </body>
  
</html>
