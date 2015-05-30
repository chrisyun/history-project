<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="thorn" uri="/thorn"%>
<jsp:include page="/springTag/header.jmt"></jsp:include>

<script type="text/javascript" src="../../plugins/local/tree3.js"></script>
<script type="text/javascript">

	document.title = "Role - Manage";
	
	var defaultRole = <thorn:dd  typeId="DEFAULTROLE" />;
	var defaultRoleRender = function(role) {
		return Render.dictRender(defaultRole, role);
	};
	
	var userPermission = {
		SAVE : '<sec:authorize url="/role/saveOrModify*.jmt">true</sec:authorize>',
		MODIFY : '<sec:authorize url="/role/saveOrModify*.jmt">true</sec:authorize>',
		REMOVE : '<sec:authorize url="/role/deleteRole.jmt">true</sec:authorize>',
		AUTH : '<sec:authorize url="/role/saveAuth.jmt">true</sec:authorize>'
	};
	
</script>
<script type="text/javascript" src="role.js"></script>
<jsp:include page="../../reference/footer.jsp"></jsp:include>