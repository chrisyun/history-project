<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<jsp:include page="/springTag/header.jmt"></jsp:include>

<script type="text/javascript">

	document.title = "Configurator - Manager";
	
	var userPermission = {
		MODIFY : '<sec:authorize url="/cf/modifyConfig.jmt">true</sec:authorize>'
	};
</script>
<script type="text/javascript" src="configurator.js" ></script>
<jsp:include page="../../reference/footer.jsp"></jsp:include>