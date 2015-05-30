function getCookie(key) {
	var arrStr = document.cookie.split("; ");

	for ( var i = 0; i < arrStr.length; i++) {
		var temp = arrStr[i].split("=");
		if (temp[0] == key) {
			return unescape(temp[1]);
		}
	}

	return null;
}

function getRequestArguments() {
	var url = window.location.search;
	var theRequest = new Object();

	if (url.indexOf("?") != -1) {
		var str = url.substr(1);
		strs = str.split("&");
		for ( var i = 0; i < strs.length; i++) {
			theRequest[strs[i].split("=")[0]] = unescape(strs[i].split("=")[1]);
		}
	}

	return theRequest;
}

function completePage() {
	    
	var hideMask = function () {
        Ext.get('loading').remove();
        Ext.fly('loading-mask').fadeOut({
            remove:true
        });
    };

    hideMask.defer(250);
}