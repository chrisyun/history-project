var editOrgUrl = sys.basePath + "orgAction!editOrg.do";
var getOneOrgUrl = sys.basePath + "orgAction!searchOrg.do";
var deleteOrgUrl = sys.basePath + "orgAction!deleteOrg.do";
var currentActiveNode;
var currentOptype;	
//初始化页面信息
	function initPage () {
		orgStore.baseParams = {
   	 		'parentorg'	: orgTree.root.id
   	   	};
		orgStore.reload({params:{start: 0, limit: orgPagingBar.pageSize}});
    	orgTree.on("click", function(node){
			var thisForm = queryFormPanel.getForm();
	   	 	orgStore.baseParams = {
	   	 		'parentorg'	: node.id
	   	   	};
	   	 	orgStore.reload({params:{start: 0, limit: orgPagingBar.pageSize}});
		});
		var menuTop1 = new Ext.menu.Menu({
		 	items:[
		 		{text: '增加子组织',iconCls : 'icon_add', handler: onAdd}
		 	]
		});
		var menuTop2 = new Ext.menu.Menu({
		 	items:[
			 	{text: '增加子组织',iconCls : 'icon_add', handler: onAdd},
			 	'-',
			 	{text: '修改组织',iconCls : 'icon_edit', handler: onUpdate},
			 	'-',
			 	{text: '删除组织',iconCls : 'icon_delete', handler: onDelete}
		 	]
		});
    	orgTree.on("contextmenu", function(node, ev){
            ev.preventDefault();
            node.select();
            currentActiveNode = node;
            if(node.attributes.id == 0){
            	menuTop1.showAt(ev.getXY());
            }else {
            	menuTop2.showAt(ev.getXY());
            }
       	});
		orgTree.getRootNode().expand(false, false);	
	}

 //增加子节点初始化
    function onAdd(){
    	currentOptype = Common.optype.add;
    	var node = currentActiveNode;
    	//getisdishi(node);
        systemMenuWindow.show();
        systemMenuWindow.setTitle("新增组织");
    	orgForm.getForm().reset();
		orgForm.getForm().findField('org.provinceNumber').clearValue();
	    $("parentName").setValue(currentActiveNode.text);
	    $("org.parentorg").setValue(currentActiveNode.attributes.id);
	    
	    if (currentActiveNode.attributes && currentActiveNode.attributes.attributes 
			&& currentActiveNode.attributes.attributes.provinceCode) {
			var provinceCode = currentActiveNode.attributes.attributes.provinceCode;
			Ext.getCmp('provinceCode').setValue(provinceCode);
			//Ext.getCmp('provinceCode').setDisabled(true);
		}
	    
    }
    
//修改子节点初始化
    function onUpdate(){
    	currentOptype = Common.optype.update;
        systemMenuWindow.show();
        systemMenuWindow.setTitle("修改组织");
       	initUpdateValues();
    }
//修改系统菜单初始化表单
	function initUpdateValues(){
		var orgid = currentActiveNode.id;
		Ext.Ajax.request({
			url : getOneOrgUrl,
		   	method : 'POST',
		   	params: { orgid: orgid },
		   	success: function (response, options) {
				var orgObj = Ext.util.JSON.decode(response.responseText);
				if (orgObj == null) {
					Ext.Msg.alert("提示信息", "初始化系统菜单信息失败！");
					return;
				}
				var parentName = '';
				//if(currentActiveNode.parentNode.id != 0){
					parentName = currentActiveNode.parentNode.text;
				//}
				orgForm.getForm().findField('org.provinceNumber').clearValue();
				var values = {
					'parentName' : parentName,
					'org.orgname' : orgObj.orgname,
					'org.orgid' : orgObj.orgid, 
					'org.sort' : orgObj.sort,
					'org.orgtype' : orgObj.orgtype,
					'org.provinceNumber':orgObj.provinceNumber,
					'org.isshow' : orgObj.isshow,
					'org.parentorg' : orgObj.parentorg,
					'org.orgsbname' : orgObj.orgsbname,
					'org.orglevel' : orgObj.orglevel
				};
				orgForm.getForm().reset();
				orgForm.getForm().setValues(values);
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
//新增或修改后保存	
	function onSave(){
		var thisForm = orgForm.getForm();
		if(!thisForm.isValid()){
			return;
		}
		Common.showProcessMsgBox();
        thisForm.submit({
			timeout: 60000,
			params: {'opType': currentOptype},
		 	success: function(form, action) {
				Common.hideProcessMsgBox();
		 		systemMenuWindow.hide();
				Ext.Msg.show({
	                title : '成功提示',
	                msg : action.result.msg,
	                buttons : Ext.Msg.OK,
	                icon: Ext.MessageBox.INFO,
	                fn: function(){
	  					form.reset();
	  					var refreshNode;
	  					if(currentOptype == Common.optype.update) {
	  						refreshNode = currentActiveNode.parentNode;
	  					} else {
	  						refreshNode = currentActiveNode;
	  					}
	  					 
	  					orgTree.getLoader().load(refreshNode);
	  					refreshNode.expand();
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
	
//删除组织节点
	function onDelete(){
		Ext.Msg.confirm("确认信息:", "删除组织同时将组织下的所有子组织删除,是否确定删除?",function(btn){
	 		if(btn == 'yes'){
	 			//currentActiveNode.parentNode.select();
	 			var orgId = currentActiveNode.id;
	    		var ajaxParams = {'orgid': orgId};
				Common.showProcessMsgBox('正在删除系统菜单，请稍后...');
	    		ExtAjax(deleteOrgUrl,ajaxParams);
	 		}
		});
	}
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
			  					orgTree.getLoader().load(refreshNode);
			  					refreshNode.expand();
								orgGrid.getStore().reload();
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
  //关闭增加或修改对话框
    function onClose(){
    	systemMenuWindow.hide();
    	orgForm.getForm().reset();
    }