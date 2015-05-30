<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<jsp:include page="/springTag/header.jmt"></jsp:include>

<script type="text/javascript" src="../../plugins/ext-3.2.1/ux/FileUploadField.js"></script>
<script type="text/javascript" src="../../plugins/ext-3.2.1/ux/MultiSelect.js"></script>
<script type="text/javascript" src="../../plugins/local/uploadUtils.js"></script>
<style type="text/css">
#attManager {
    background-color: #fff;
    text-shadow: #fff 0 1px 0;
}

#attManager ul {
    position: relative;
    display: block;
    height: auto;
    font-size: 85%;
}

#attManager ul li img {
    margin-bottom: 1px;
}

#attManager ul li {
    float: left;
    padding: 8px 17px;
    margin: 5px;
    margin: 10px 0 0 10px;
    text-align: center;
    line-height: 1.25em;
    color: #333;
    font-family: "Helvetica Neue",sans-serif;
    height: 113px;
    width: 112px;
    overflow: hidden;
    border-top: 1px solid transparent;
    cursor: pointer;
}

#attManager ul li.over-hover {
    background-color: #7CC7F2;
}

#attManager ul li.x-view-selected {
    background-color: #61C7E0;
}


#attManager li strong {
    color: #000;
    display: block;
}

#attManager li span {
	font-size: 9px;
    color: #999;
}	
</style>

<script type="text/javascript">

	document.title = "attachment - Manager";
	
	var userPermission = {
		DOWNLOAD : '<sec:authorize url="/att/download.jmt">true</sec:authorize>',
		DELETE : '<sec:authorize url="/att/delete.jmt">true</sec:authorize>'
	};
</script>
<script type="text/javascript" src="attachment.js"></script>
<jsp:include page="../../reference/footer.jsp"></jsp:include>