<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="thorn" uri="/thorn"%>
<jsp:include page="/springTag/header.jmt"></jsp:include>

<script type="text/javascript">

	document.title = "AppLog - Page";
	
	var module = <thorn:dd  typeId="MODULE" />;
	var moduleRender = function(str) {
		return Render.dictRender(module, str);
	};
	
	var handleResult = <thorn:dd  typeId="HANDLERESULT" />;
	var handleResultRender = function(str) {
		return Render.dictRender(handleResult, str);
	};
	
	var userPermission = {
		EXPORT : '<sec:authorize url="/log/exportLogExcel.jmt">true</sec:authorize>'
	};
	
</script>
<script type="text/javascript" src="appLog.js"></script>
<iframe id="excelFrame" src="" style="display:none;"></iframe>
<jsp:include page="../../reference/footer.jsp"></jsp:include>