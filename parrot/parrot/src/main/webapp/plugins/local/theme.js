var skinItemArray = [{
	text: '默认',
	handler : function(){
		setActiveStyleSheet("default");
	}
},{
	text: '灰色',
	handler : function(){
		setActiveStyleSheet("gray");
	}
},{
	text: '黑色',
	handler : function(){
		setActiveStyleSheet("black");
	}
},{
	text: '浅灰',
	handler : function(){
		setActiveStyleSheet("blue03");
	}
},{
	text: '棕色',
	handler : function(){
		setActiveStyleSheet("brown");
	}
},{
	text: '浅棕',
	handler : function(){
		setActiveStyleSheet("brown02");
	}
},{
	text: '绿色',
	handler : function(){
		setActiveStyleSheet("green");
	}
},{
	text: '粉红',
	handler : function(){
		setActiveStyleSheet("pink");
	}
},{
	text: '紫色',
	handler : function(){
		setActiveStyleSheet("purple");
	}
},{
	text: '红色',
	handler : function(){
		setActiveStyleSheet("red03");
	}
}];

function setActiveStyleSheet(_theme) {
	
	var params = {theme : _theme};
	
	var ajax = new AjaxUtil(sys.path + "/theme/change.jmt");
	ajax.request(params, false, null, function(){
		setTimeout("window.location.reload()", 1000);
	});
}
