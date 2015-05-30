/**
 * 业务方面的特殊转化
 * @type 
 */
var BusiRenderer = {};

/**
 * 提交方式
 */
BusiRenderer.Submittype = function (value) {
	if (value == '0') {
		return '<span style="color: #000a; font-weight: 500;">短信</span>';
	} else if (value == '1') {
		return '<span style="color: red; font-weight: 600;">彩信</span>';
	} if (value == '2') {
		return '<span style="color: #ccc; font-weight: 500;">其它</span>';
	}  
}

/**
 * 性别
 */
BusiRenderer.Gender = function (value) {
	if (value == '0') {
		return '<img src="' + sys.basePath + '/foundation/ext/images/male.png" title="男" />';
	} else if (value == '1') {
		return '<img src="' + sys.basePath + '/foundation/ext/images/female.png" title="女" />';
	}
}