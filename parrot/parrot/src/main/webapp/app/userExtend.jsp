<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="thorn" uri="/thorn"%>
<jsp:include page="/springTag/header.jmt"></jsp:include>

<script type="text/javascript" src="userExtend.js"></script>
<script type="text/javascript">

	document.title = "UserExtend - Manage";
	
	var area = <thorn:dd  typeId="AREA" />;
	area.unshift(["","全部"]);
	var areaRender = function(str) {
		return Render.dictRender(area, str);
	};

	var province = new Array();

	var temp = new Array();
	temp.push(user.role);
	var roles = temp.join(",");
	if (roles.indexOf(Configuration.keyRole.ADMIN) < 0
			&& roles.indexOf(Configuration.keyRole.SEE) < 0
			&& roles.indexOf(Configuration.keyRole.CENTRE) < 0) {
		for ( var i = 0; i < area.length; i++) {
			if (area[i][0] == user.org) {
				province.push(area[i]);
				break;
			}
		}
		area = province;
	}
	
	var projectType = <thorn:dd  typeId="PROJECT_TYPE" />;
	var projectTypeRender = function(str) {
		return Render.dictRender(projectType, str);
	};
	
	var userPermission = {
		MODIFY : '<sec:authorize url="/project/dw/saveOrModify*.jmt">true</sec:authorize>'
	};
	
</script>
<iframe id="excelFrame" src="" style="display:none;"></iframe>
<jsp:include page="../reference/footer.jsp"></jsp:include>
