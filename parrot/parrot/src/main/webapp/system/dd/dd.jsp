<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<jsp:include page="/springTag/header.jmt"></jsp:include>

<script type="text/javascript">

	document.title = "Data - Dict";
	
	var userPermission = {
		SAVE : '<sec:authorize url="/dd/saveOrModify*.jmt">true</sec:authorize>',
		MODIFY : '<sec:authorize url="/dd/saveOrModify*.jmt">true</sec:authorize>',
		REMOVE : '<sec:authorize url="/dd/delete*.jmt">true</sec:authorize>'
	};
</script>
<script type="text/javascript" src="dd.js" ></script>
<jsp:include page="../../reference/footer.jsp"></jsp:include>