<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="thorn" uri="/thorn"%>
<jsp:include page="/springTag/header.jmt"></jsp:include>

<script type="text/javascript" src="processTodo.js"></script>
<script type="text/javascript">

	document.title = "Process - Todo";
	
	var area = <thorn:dd  typeId="AREA" />;
	var areaRender = function(str) {
		return Render.dictRender(area, str);
	};

	var province = new Array();

	var temp = new Array();
	temp.push(user.role);
	var roles = temp.join(",");
	if (roles.indexOf(Configuration.keyRole.ADMIN) < 0
			&& roles.indexOf(Configuration.keyRole.CENTRE) < 0) {
		for ( var i = 0; i < area.length; i++) {
			if (area[i][0] == user.org) {
				province.push(area[i]);
				break;
			}
		}
		area = province;
	}
	
	var flowTypeDD = <thorn:dd  typeId="FLOW_TYPE" />;
	flowTypeDD.unshift(["","全部"]);
	var flowTypeRender = function(str) {
		return Render.dictRender(flowTypeDD, str);
	};
	
	var flowStatusDD = <thorn:dd  typeId="FLOW_STATUS" />;
	flowStatusDD.unshift(["","全部"]);
	var flowStatusRender = function(str) {
		return Render.dictRender(flowStatusDD, str);
	};

</script>
<jsp:include page="../reference/footer.jsp"></jsp:include>