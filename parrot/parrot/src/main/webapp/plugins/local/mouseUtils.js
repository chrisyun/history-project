document.onmousemove = mouseMove;
var mouseXY;

function mouseMove(event) {
	ev = event || window.event;// 所有浏览器下获取了event事件
	mouseXY = mousePos(ev);// 获取位置
}

function mousePos(ev) {
	if (ev.pageX || ev.pageY) {
		return {
			x : ev.pageX,
			y : ev.pageY
		};
	}
	return {
		x : ev.clientX + document.body.scrollLeft - document.body.clientLeft,
		y : ev.clientY + document.body.scrollTop - document.body.clientTop
	};
}
