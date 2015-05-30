<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="thorn" uri="/thorn"%>
<jsp:include page="/springTag/header.jmt"></jsp:include>

<script type="text/javascript">

	document.title = "Resource - Manage";
	
	var iconCls = <thorn:dd  typeId="ICONCLS" />;
	var iconClsRender = function(icon) {
		return Render.dictRender(iconCls, icon);
	};
	
	var userPermission = {
		SAVE : '<sec:authorize url="/org/saveOrModify*.jmt">true</sec:authorize>',
		MODIFY : '<sec:authorize url="/org/saveOrModify*.jmt">true</sec:authorize>',
		REMOVE : '<sec:authorize url="/org/deleteSource.jmt">true</sec:authorize>'
	};
	
</script>
<script type="text/javascript" src="source.js"></script>
<jsp:include page="../../reference/footer.jsp"></jsp:include>