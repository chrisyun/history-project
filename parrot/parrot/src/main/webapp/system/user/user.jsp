<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="thorn" uri="/thorn"%>
<jsp:include page="/springTag/header.jmt"></jsp:include>

<script type="text/javascript" src="../../plugins/local/treeField.js"></script>
<script type="text/javascript" src="../org/orgTree.js"></script>
<script type="text/javascript" src="userPwd.js"></script>
<script type="text/javascript">

	document.title = "User - Manage";
	
	var area = <thorn:dd  typeId="AREA" />;
	var areaRender = function(str) {
		return Render.dictRender(area, str);
	};
	
	var defaultRole = <thorn:dd  typeId="DEFAULTROLE" />;
	var defaultRoleRender = function(role) {
		return Render.dictRender(defaultRole, role);
	};
	
	var gender = <thorn:dd  typeId="GENDER" />;
	var genderRender = function(str) {
		return Render.dictRender(gender, str);
	};
	
	var userPermission = {
		SAVE : '<sec:authorize url="/user/saveOrModify*.jmt">true</sec:authorize>',
		MODIFY : '<sec:authorize url="/user/saveOrModify*.jmt">true</sec:authorize>',
		REMOVE : '<sec:authorize url="/user/deleteUser.jmt">true</sec:authorize>',
		DISABLED : '<sec:authorize url="/user/disabledUser.jmt">true</sec:authorize>',
		CHANGEPWD : '<sec:authorize url="/user/changePwd.jmt">true</sec:authorize>',
		AUTH : '<sec:authorize url="/user/saveRoleByUser.jmt">true</sec:authorize>'
	};
	
</script>
<script type="text/javascript" src="user.js"></script>
<jsp:include page="../../reference/footer.jsp"></jsp:include>
