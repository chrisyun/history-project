<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="thorn" uri="/thorn"%>
<%
	String path = request.getContextPath();
%>
<script type="text/javascript" src="<%=path%>/app/reseverCost.js"></script>
<script type="text/javascript">
<!--
	var year = new Array();
	var now = new Date(); 
	var curYear = now.getFullYear();
	var nextYear = parseInt(curYear);
	//year.push([curYear,curYear]);
	year.push([nextYear,nextYear]);

	var budgetDD = <thorn:dd  typeId="BUDGET_RESEVER_DETAIL" />;
	var budgetArray = new Array();
	for ( var i = 0; i < budgetDD.length; i++) {
		budgetArray.push(budgetDD[i][0]);
	}
//-->
</script>