<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="thorn" uri="/thorn"%>
<jsp:include page="/springTag/header.jmt"></jsp:include>

<script type="text/javascript" src="heritor.js"></script>
<script type="text/javascript">

	document.title = "Heritor - Page";
	
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
	
	yesOrNo.unshift(["","全部"]);
	
	var gender = <thorn:dd  typeId="GENDER" />;
	var genderRender = function(str) {
		return Render.dictRender(gender, str);
	};
	
	var minority = <thorn:dd  typeId="MINORITY" />;
	minority.unshift(["","全部"]);
	var minorityRender = function(str) {
		return Render.dictRender(minority, str);
	};
	
	var userPermission = {
		SAVE : '<sec:authorize url="/heritor/saveOrModify*.jmt">true</sec:authorize>',
		MODIFY : '<sec:authorize url="/heritor/saveOrModify*.jmt">true</sec:authorize>',
		REMOVE : '<sec:authorize url="/heritor/deleteHeritor.jmt">true</sec:authorize>'
	};
</script>
<jsp:include page="../reference/footer.jsp"></jsp:include>