<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="thorn" uri="/thorn"%>
<jsp:include page="/springTag/header.jmt"></jsp:include>

<script type="text/javascript" src="../org/orgTree.js"></script>
<script type="text/javascript">

	document.title = "UserAuth - Manage";
	
	var defaultRole = <thorn:dd  typeId="DEFAULTROLE" />;
	var defaultRoleRender = function(role) {
		return Render.dictRender(defaultRole, role);
	};
	
	var userPermission = {
		SAVEAUTH : '<sec:authorize url="/user/saveUserRole.jmt">true</sec:authorize>',
		REMOVEAUTH : '<sec:authorize url="/user/deleteUserRole.jmt">true</sec:authorize>'
	};
	
</script>
<script type="text/javascript" src="userAuth.js"></script>
<jsp:include page="../../reference/footer.jsp"></jsp:include>