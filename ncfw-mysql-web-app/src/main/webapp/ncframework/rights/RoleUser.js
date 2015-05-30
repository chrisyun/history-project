/**
 * 角色成员管理
 * @type 
 */
var RoleUser = {
	userChoosePanel		: null,		//用户选择列表
	userChooseDialog	: null		//用户选择对话框
};

RoleUser.UserGridPanel = Ext.extend(Ext.grid.GridPanel, {
	loadMask			: true,
	split				: true,
	region 				: 'center',
	iconCls 			: 'grid-icon',
	viewConfig 			: {forceFit: true},
	
	constructor: function(config){
		RoleUser.UserGridPanel.superclass.constructor.call(this, config);
	},
	beforeInit : function () {
		var sm 		= new Ext.grid.CheckboxSelectionModel({singleSelect: false});
		//定义页面查询表
		var cm 		= new Ext.grid.ColumnModel({
	   		columns:[
	   		new Ext.grid.RowNumberer(),
	   		sm,
			/**{header : '登录名', width: 100, dataIndex: 'userid'},*/
			{header : '姓名', width: 100, dataIndex: 'username'},
			{header : '手机号码', width: 100, dataIndex: 'mobile'}, 
			{header : '邮箱', width: 180, dataIndex: 'mail'}
			]
		});
		this.cm		= cm;
		this.sm 	= sm;
		this.store	= new Ext.data.Store( {
			proxy : new Ext.data.HttpProxy(new Ext.data.Connection({timeout:0, url: sys.basePath + "authAction!listUserByRole.do"})),
			reader : new Ext.data.JsonReader({
				root : 'data',
				totalProperty : 'totalProperty'
			}, 
				Ext.data.Record.create([
					{name : 'userid',type : 'string'}, 
					{name : 'username',type : 'string'},
					{name : 'mobile',type : 'string'},
					{name : 'mail',type : 'string'},
					{name : 'gender',type : 'string'}
				])
			),
			remoteSort : true
		});
		
		var preffix	= this.id;
		
		var searchField = new Ext.ux.form.SearchField({id : preffix + 'SearchField', width: 160, emptyText:'请输入查询关键字', 
			trigger2ClickHandlerScope: this});
		searchField.onTrigger2Click = this.searchUserHandler;
		
		this.tbar	= [
			{xtype: 'tbbutton', id: preffix + 'QueryType', text : '姓名', minWidth: 100, menu : new Ext.menu.Menu({
				width: 100,
				items : [/**{
					id: preffix + 'QueryTypeID',
					text : '登录名',
					handler: function () {
						var QueryType = Ext.getCmp(preffix + "QueryType");
						QueryType.setText(Ext.getCmp(preffix + "QueryTypeID").text);
					}
				},*/{
					id: preffix + 'QueryTypeCn',
					text : '姓名',
					handler: function () {
						var QueryType = Ext.getCmp(preffix + "QueryType");
						QueryType.setText(Ext.getCmp(preffix + "QueryTypeCn").text);
					}
				},{
					id: preffix + 'QueryTypeMail',
					text : '邮箱',
					handler: function () {
		  				var QueryType = Ext.getCmp(preffix + "QueryType");
		  				QueryType.setText(Ext.getCmp(preffix + "QueryTypeMail").text);
					}
				},{
					id: preffix + 'QueryTypeMobile',
					text : '手机号码',
					handler: function () {
		  				var QueryType = Ext.getCmp(preffix + "QueryType");
		  				QueryType.setText(Ext.getCmp(preffix + "QueryTypeMobile").text);
					}
				}]
			})
		},
		searchField
		];
		
		this.bbar 	= new Ext.PagingToolbar({
	        pageSize: this.pageSize || 15,
	        store: this.store,
	        displayInfo: true,
	        plugins: [new Ext.ux.Page.pageSize({beforeText : '显示', afterText : '条' })]
	    });
	},
	afterInit	: function () {
	
	},
	initComponent : function () {
		this.beforeInit();
        RoleUser.UserGridPanel.superclass.initComponent.call(this);
		this.afterInit();
	},
	searchUserHandler : function () {
		var queryType 		= Ext.getCmp(this.getId() + "QueryType").getText();
		if (this.store.baseParams == null) {
			this.store.baseParams	= {};
		}
		switch (queryType) {
			//case "登录名" 	: 	this.store.baseParams.useridLike 	= Ext.getCmp(this.id + "SearchField").getValue(); 	break;
			case "姓名" 		: 	this.store.baseParams.username 		= Ext.getCmp(this.id + "SearchField").getValue(); 	break;
			case "邮箱" 		: 	this.store.baseParams.mailLike		= Ext.getCmp(this.id + "SearchField").getValue(); 	break;
			case "手机号码" 	: 	this.store.baseParams.mobileLike	= Ext.getCmp(this.id + "SearchField").getValue(); 	break;
		}
		this.store.reload({params: {start: 0, limit: this.bbar.pageSize || 15}});
	}
});


/**
 * 角色成员新增列表
 * @class RoleUser.UserAddListGrid
 * @extends RoleUser.UserGridPanel
 */
RoleUser.UserAddListGrid	= Ext.extend(RoleUser.UserGridPanel, {
	constructor: function(config){
		RoleUser.UserEditListGrid.superclass.constructor.call(this, config);
	},
	beforeInit	: function () {
        RoleUser.UserAddListGrid.superclass.beforeInit.call(this);
	},
	afterInit	: function () {
		/**
		this.addButton({xtype: 'button', text: '确定', iconCls: 'icon_sure', scope: this, handler: this.addUser});
		*/
	},
	addUser : function () {
		var selectedRecordArray = this.getSelectionModel().getSelections();
		if(selectedRecordArray.length < 1){
     		Ext.Msg.alert("提示信息", "请至少选择一条记录!");
     		return;
      	}
      	var userids = "";
      	for(var i = 0;i < selectedRecordArray.length;i++){
      		userids += selectedRecordArray[i].get("userid") + ",";
	    }
	    if(userids.length > 0){
	    	userids = userids.substring(0, userids.length - 1);
	    }
	    var store	= this.getStore();
		var roleId	= store.baseParams.roleidsOut;
		Common.ajaxRequest({
			proccessMsg: '成员添加中, 请稍后...', url: sys.basePath + 'authAction!updateUsersRole.do',
			params: {userids: userids, roleid: roleId, optype: 'add'},
			successMsg: '成员添加成功.', successHandler : function () {
				store.reload();
			}
		});
	}
});


/**
 * 展示角色成员列表新增对话框
 */
RoleUser.showAddUserListDialog = function () {
	if (!RoleUser.userListAddDialog) {
		var windowWidth 	= document.body.clientWidth > 680 ? 680 : document.body.clientWidth - 10;
		var windowHeight 	= document.body.clientHeight > 600 ? 600 : document.body.clientHeight - 10;
		var userAddListGrid	= new RoleUser.UserAddListGrid({pageSize : 15, id: 'userAddListGrid'});
		RoleUser.userListAddDialog = new Ext.Window({ //定义对话框
			width : windowWidth,
			height : windowHeight,
			shadow : true,
			title: '新增用户成员',
			closeAction : 'hide',
			modal : true,
			closable : true,
			layout : 'fit',
			items:[userAddListGrid],
			buttonAlign: 'center',
			buttons : [
				{xtype: 'button', text: '提交', 	iconCls: 'icon_sure', scope: userAddListGrid, handler: userAddListGrid.addUser},
				{xtype: 'button', text: '关闭', iconCls: 'icon_close', scope: this, handler: function (){RoleUser.userListAddDialog.hide();}}
			]
		}); 
	}
	RoleUser.userListAddDialog.show();
}

/**
 * 角色成员编辑列表
 * @class RoleUser.UserEditListGrid
 * @extends RoleUser.UserGridPanel
 */
RoleUser.UserEditListGrid	= Ext.extend(RoleUser.UserGridPanel, {
	constructor: function(config){
		RoleUser.UserEditListGrid.superclass.constructor.call(this, config);
	},
	afterRender	: function () {
        RoleUser.UserEditListGrid.superclass.afterRender.call(this);
		var toolbar 	= this.getTopToolbar();
		/**
		toolbar.add('->');
		toolbar.addButton({xtype: 'button', text: '添加成员', iconCls: 'icon_add', scope: this, handler: this.addUser});
		toolbar.addButton({xtype: 'button', text: '移除成员', iconCls: 'icon_delete', scope: this, handler: this.removeUser});
		*/
	},
	beforeInit	: function () {
        RoleUser.UserEditListGrid.superclass.beforeInit.call(this);
	},
	afterInit	: function () {
	},
	addUser : function () {
		RoleUser.showAddUserListDialog();
		var userAddListGrid = RoleUser.userListAddDialog.items.first();
		var store			= userAddListGrid.getStore();
		var roleId			= this.getStore().baseParams.roleidsIn;
		store.baseParams	= {
			roleidsOut	: roleId
		};
		userAddListGrid.searchUserHandler();
		//alert('添加成员');
	},
	removeUser : function () {
		var selectedRecordArray = this.getSelectionModel().getSelections();
		if(selectedRecordArray.length < 1){
     		Ext.Msg.alert("提示信息", "请至少选择一条记录!");
     		return;
      	}
      	var userids = "";
      	for(var i = 0;i < selectedRecordArray.length;i++){
      		userids += selectedRecordArray[i].get("userid") + ",";
	    }
	    if(userids.length > 0){
	    	userids = userids.substring(0, userids.length - 1);
	    }
		var store	= this.getStore();
		var roleId	= store.baseParams.roleidsIn;
		Common.ajaxRequest({
			proccessMsg: '成员移除中, 请稍后...', url: sys.basePath + 'authAction!updateUsersRole.do',
			params: {userids: userids, roleid: roleId, optype: 'remove'},
			successMsg: '成员移除成功.', successHandler : function () {
				store.reload();
			}
		});
	}
});

/**
 * 展示角色成员列表对话框
 */
RoleUser.showUserListDialog = function () {
	if (!RoleUser.userListDialog) {
		var windowWidth 	= document.body.clientWidth > 680 ? 680 : document.body.clientWidth - 10;
		var windowHeight 	= document.body.clientHeight > 600 ? 600 : document.body.clientHeight - 10;
		var userListGrid	= new RoleUser.UserEditListGrid({pageSize : 15, id: 'userEditListGrid'});
		RoleUser.userListDialog = new Ext.Window({ //定义对话框
			width : windowWidth,
			height : windowHeight,
			shadow : true,
			title: '用户成员',
			closeAction : 'hide',
			modal : true,
			closable : true,
			layout : 'fit',
			items:[userListGrid]
		});
	}
	RoleUser.userListDialog.show();
}

/**
 * 查询角色成员列表
 * @param {} roleId
 */
RoleUser.viewUserList = function (roleId) {
	RoleUser.showUserListDialog();
	var userListGrid 		= RoleUser.userListDialog.items.first();
	var store				= userListGrid.getStore();
	store.baseParams	= {
		roleidsIn	: roleId
	};
	userListGrid.searchUserHandler();
}

