function searchMinds(pid,tableid,panelId) {
	var table = "<table width=100% height=100% class='mind_tab'>"
	var mindsPanel = document.getElementById(panelId);
	Ext.Ajax.request({
		url 	: sys.basePath + 'projectAction!searchMinds.do',
	   	method 	: 'post',
	   	params 	: {pid : pid,tableid: tableid},
	   	success	: function (response, options) {
			var fileJson = Ext.util.JSON.decode(response.responseText);
			
			for ( var i = 0; i < fileJson.length; i++) {
				var minds = fileJson[i];
				table += addMindinPanel(minds.activityname,minds.minds);
			}
			
			mindsPanel.innerHTML = table + "</table>";
	   	}
	});
	
}

function addMindinPanel(activityName,mind) {
	var mindsLength = mind.length;
	
	var temp = 50;
	
	var a1;
	var a2;
	while(mindsLength > temp) {
		if((mindsLength - temp) > 25) {
			a1 = mind.substring(0,temp);
			a2 = mind.substring(temp,mind.length);
			mind = a1 + "<br>" + a2;
			
			temp += 50;
		} else {
			break;
		}
	}
	
	return "<tr><td width='210' align='right'>"+activityName+":&nbsp;&nbsp;</td>" + "<td>" + mind + "</td></tr>";
}
