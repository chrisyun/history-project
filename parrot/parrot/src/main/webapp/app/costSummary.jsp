<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="thorn" uri="/thorn"%>
<jsp:include page="/springTag/header.jmt"></jsp:include>

<script type="text/javascript">

	document.title = "CostSummary - Page";
	
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
	
	var year = <thorn:dd  typeId="YEAR" />;
	var yearRender = function(str) {
		return Render.dictRender(year, str);
	};
	
	var heritorMoney = 1;
	
	var userPermission = {
		EXCEL : '<sec:authorize url="/heritor/exportAllSummaryExcel.jmt">true</sec:authorize>'
	};
	
</script>
<script type="text/javascript" src="costSummary.js"></script>
<iframe id="excelFrame" src="" style="display:none;"></iframe>
<jsp:include page="../reference/footer.jsp"></jsp:include>