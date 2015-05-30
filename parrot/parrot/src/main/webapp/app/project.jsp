<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="thorn" uri="/thorn"%>
<jsp:include page="/springTag/header.jmt"></jsp:include>

<script type="text/javascript" src="project.js"></script>
<script type="text/javascript">

	document.title = "Project - Page";
	
	var area = <thorn:dd  typeId="AREA" />;
	area.unshift(["","全部"]);
	var areaRender = function(str) {
		return Render.dictRender(area, str);
	};
	
	var provinceAreaDD = <thorn:dd  typeId="PROVINCE_AREA" />;
	provinceAreaDD.unshift(["","全部"]);
	var provinceAreaDDRender = function(str) {
		return Render.dictRender(provinceAreaDD, str);
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
	projectType.unshift(["","全部"]);
	var projectTypeRender = function(str) {
		return Render.dictRender(projectType, str);
	};
	
	var minority = <thorn:dd  typeId="MINORITY" />;
	minority.unshift(["","全部"]);
	var minorityRender = function(str) {
		return Render.dictRender(minority, str);
	};
	
	var gender = <thorn:dd  typeId="GENDER" />;
	var genderRender = function(str) {
		return Render.dictRender(gender, str);
	};

	var userPermission = {
		SAVE : '<sec:authorize url="/project/saveOrModify*.jmt">true</sec:authorize>',
		MODIFY : '<sec:authorize url="/project/saveOrModify*.jmt">true</sec:authorize>',
		REMOVE : '<sec:authorize url="/project/deleteProject.jmt">true</sec:authorize>',
		BINGING : '<sec:authorize url="/heritor/bingingProject.jmt">true</sec:authorize>',
		SETTINGFUND : '<sec:authorize url="/project/saveOrModifyFund.jmt">true</sec:authorize>'
	};
</script>
<jsp:include page="../reference/footer.jsp"></jsp:include>