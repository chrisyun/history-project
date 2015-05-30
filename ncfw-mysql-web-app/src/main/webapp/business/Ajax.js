/**
*@author Wuqingming
*@filename ajax.js
*@since 2009/04/20
*使用说明：开始 new AjaxRequest()对象，
*		再调用getResultData()方法既可获得responseXML或responseText或json对象
*/


//Ajax Object
function AjaxRequest(method, requestUrl, asynchronous, dataType){
	this.errorMessage 		= "";
	this.XMLHttpReq 		= null;
	this.requestUrl 		= requestUrl; 				//请求的路径 
	this.method 			= method; 					//post或者get方法 
	//this.callBackMethod		= callBackMethod;
	this.asynchronous 		= asynchronous || false;	//true或者false 实现异步或同步
	this.dataType			= dataType;					//返回结果的类型，如：xml、text或json
	this.resultData			= null;						//结果值，为XMl、Text或Json格式
	
	this.readyState = {
		unInitialized 		: 0,  	//（未初始化）还没有调用send()方法
		loading 			: 1,  	//（载入）已调用send()方法，正在发送请求
		loaded  			: 2,  	//（载入完成）send()方法执行完成，已经接收到全部响应内容
		interactive			: 3,  	//（交互）正在解析响应内容
		completed			: 4   	//（完成）响应内容解析完成，可以在客户端调用了	
	}
	this.status = {
		success				: 200,	//成功
		error_400			: 400, 	//400错误，错误请求，如语法错误
		error_404			: 404,	//404错误，没有发现文件、查询或URl
		error_500			: 500,	//500错误，服务器产生内部错误
		error_505			: 505	//505错误，服务器不支持或拒绝支请求头中指定的HTTP版本
	};
	
	this.createXMLHttpRequest();
	this.sendRequest(this.XMLHttpReq);
	this.setResultData(this.dataType);
};

//创建XMLHttpRequest对象
AjaxRequest.prototype.createXMLHttpRequest = function (){
	if(this.XMLHttpReq != null){
		return;
	}
	if(window.XMLHttpRequest) { //Mozilla  火狐浏览器
        this.XMLHttpReq = new XMLHttpRequest();
    } else if (window.ActiveXObject) { // IE浏览器
        try {
            this.XMLHttpReq = new ActiveXObject("Msxml2.XMLHTTP");
        } catch (ex) {
            try {
                this.XMLHttpReq = new ActiveXObject("Microsoft.XMLHTTP");
            } catch (ex) {
            	this.errorMessage = "不能创建XMLHttpRequest";
            }
       }
   }
};

//发送请求
AjaxRequest.prototype.sendRequest = function(){
	var XMLHttpReq = this.XMLHttpReq;
	var request = this;
	XMLHttpReq.open(this.method, this.requestUrl, this.asynchronous);
	this.XMLHttpReq.onreadystatechange = function () {
		if(XMLHttpReq.readyState == request.readyState.unInitialized){//未初始化
			//alert(XMLHttpReq.readyState + "  means : 未初始化");
		} else if (XMLHttpReq.readyState == request.readyState.loading){//载入
			//alert(XMLHttpReq.readyState + "  means : 载入");
		} else if (XMLHttpReq.readyState == request.readyState.loaded){//载入完成
			//alert(XMLHttpReq.readyState + "  means : 载入完成");
		} else if (XMLHttpReq.readyState == request.readyState.interactive){//交互
			//alert(XMLHttpReq.readyState + "  means : 交互");
		} else if (XMLHttpReq.readyState == request.readyState.completed) {//已完成响应
			//alert(XMLHttpReq.readyState + "  means : 已完成响应");
			if (XMLHttpReq.status == request.status.success) {//成功
				
			} else if (XMLHttpReq.status == request.status.error_400){
            	request.errorMessage = "400错误";
			} else if (XMLHttpReq.status == request.status.error_404){
            	request.errorMessage = "404错误";
			} else if (XMLHttpReq.status == request.status.error_500){
            	request.errorMessage = "500错误，服务器产生内部错误";
			} else if (XMLHttpReq.status == request.status.error_505){
            	request.errorMessage = "505错误，服务器不支持或拒绝支请求头中指定的HTTP版本";
			}
		}
	}
	this.XMLHttpReq.send(null);
};
	    
AjaxRequest.prototype.setErrorMessage = function(errorMessage){
	this.errorMessage = errorMessage;
};
	
AjaxRequest.prototype.getErrorMessage = function(errorMessage){
	return this.errorMessage;
};
	
//设置结果对象，如dataType可为XML、Text或Json
AjaxRequest.prototype.setResultData = function(dataType){
	if (dataType == "xml"){
		this.resultData = this.XMLHttpReq.responseXML;
	} else if (dataType == "text"){
		this.resultData = this.XMLHttpReq.responseText;
	} else if (dataType == "json"){
		this.resultData = eval('(' + this.XMLHttpReq.responseText + ')');
	} /**else {//默认数据格式为xml类型
		this.resultData = this.XMLHttpReq.responseXML;
	}
	*/
};

//返回结果对象，如dataType可为XML、Text或Json
AjaxRequest.prototype.getResultData = function(){
	return this.resultData;
};
