var defaults = {
	/* 右键菜单样式 */
	menuStyle : {
		border : '1px solid #5a6377',
		minWidth : '150px',
		padding : '5px 0'
	},
	itemStyle : {
		fontFamily : 'verdana',
		color : '#333',
		border : '0',
		padding : '5px 5px 5px 5px'
	},
	Top : "Top",
	Bottom : "Bottom",
	TopColor : "#11a988",	//上结束节点颜色
	TopConnectColor : "#ff0000",	//上结束节点连接线颜色
	BottomColor : "#11a988",	//下结束节点颜色
	BottomConnectColor : "#00afff",	//下结束节点连接线颜色
	defaultColor : "#11a988",	//默认节点颜色
	defaultBeginEndColor : "#FFFFFF",	//开始结束节点颜色
	prefixNodeId : "flowNode"
};

// this is the paint style for the connecting lines..
var connectorPaintStyle = {
	lineWidth : 2,
	strokeStyle : defaults.defaultColor,
	joinstyle : "round"
}, endpointHoverStyle = {
	fillStyle : defaults.defaultColor,
	strokeStyle : defaults.defaultColor
},
// the definition of source endpoints (the small blue ones)
sourceFirstEndpoint = {
	endpoint : "Dot",
	paintStyle : {
		strokeStyle : defaults.defaultBeginEndColor,
		fillStyle : "transparent",
		radius : 7,
		lineWidth : 2
	},
	isSource : true,
	connector : [ "Flowchart", {
		stub : [ 10, 40 ],
		gap : 1,
		cornerRadius : 5,
		alwaysRespectStubs : true
	} ],
	maxConnections : -1,
	connectorStyle : connectorPaintStyle,
	hoverPaintStyle : endpointHoverStyle
},
// the definition of source endpoints (the small blue ones)
sourceEndpoint = {
	endpoint : "Dot",
	paintStyle : {
		strokeStyle : "#04f48b",
		fillStyle : "#04f48b",
		radius : 7,
		lineWidth : 2
	},
	isSource : true,
	connector : [ "Flowchart", {
		stub : [ 10, 60 ],
		gap : 1,
		cornerRadius : 5,
		alwaysRespectStubs : true
	} ],
	maxConnections : -1,
	connectorStyle : connectorPaintStyle,
	hoverPaintStyle : endpointHoverStyle
},
// the definition of target endpoints (will appear when the user drags a
// connection)
targetFinishEndpoint = {
	endpoint : "Dot",
	paintStyle : {
		fillStyle : defaults.defaultBeginEndColor,
		radius : 7
	},
	hoverPaintStyle : endpointHoverStyle,
	maxConnections : -1,
	isTarget : true
};
// the definition of target endpoints (will appear when the user drags a
// connection)
targetEndpoint = {
	endpoint : "Rectangle",
	paintStyle : {
		fillStyle : defaults.defaultColor,
		radius : 11
	},
	hoverPaintStyle : endpointHoverStyle,
	maxConnections : -1,
	beforeDrop : function(params) {
		return setConnectColor(params.connection, params);
	},
	isTarget : true
};

var instance = jsPlumb.getInstance({
	DragOptions : {
		cursor : 'pointer',
		zIndex : 2000
	},
	ConnectionOverlays : [ [ "Arrow", {
		location : 1
	} ] ],
	Container : "flowchart-demo"
});

// 连接线完成执行方法
var connectionComplete = function(connection) {
	setConnectColor(connection);
	if (!connection.endpoints[1].getUuid()) {
		var sourceId = connection.sourceId;
		var targetId = connection.targetId;
		setTimeout(function(){
			clearAllErrorConnections(sourceId, targetId)
		}, 10);
	}
};
var setConnectColor = function(connection, params) {
	var dropEndpoint;
	if(params) {
		dropEndpoint = params.dropEndpoint;
	} else {
		dropEndpoint = connection.endpoints[1];
	}
	if(params) {
		// 判断起点、终点是否可以连接，加入一些条件，防止随意画线
		// 返回false时，连接线不能连接
		// 不能连接自己
		var beginElementId = connection.endpoints[0].elementId;
		var endElementId = dropEndpoint.elementId;
		if (beginElementId == endElementId)
			return false;
	}
	// 根据连接线终点的uuid，动态修改连接线的颜色
	var uuid = dropEndpoint.getUuid();
	if(uuid) {
		var strokeStyleColor = defaults.defaultColor;
		if (uuid.indexOf("__" + defaults.Top) > 0) {
			strokeStyleColor = defaults.TopConnectColor;
		} else if (uuid.indexOf("__" + defaults.Bottom) > 0) {
			strokeStyleColor = defaults.BottomConnectColor;
		}
		// 设置连接线的颜色
		connection.setPaintStyle({
			lineWidth : 2,
			strokeStyle : strokeStyleColor,
			joinstyle : "round"
		});
		connection.setHoverPaintStyle({
			lineWidth : 4,
			strokeStyle : strokeStyleColor
		});
		return true;
	} else {
		var sourceId = connection.sourceId;
		var targetId = connection.targetId;
		if(sourceId == "flowchartBegin") {
			addConnection("flowchartBegin__", targetId + "__LeftMiddle");
		} else if(targetId == "flowchartEnd") {
			addConnection(sourceId + "__RightMiddle", "flowchartEnd__");
		} else {
			addConnection(sourceId + "__RightMiddle", targetId + "__LeftMiddle");
		}
		return true;
	}
}
var contextMenuRightClick = {
	'cmSave' : function(t) {
		saveFlow();
	},
	'cmCheck' : function(t) {
		checkFlow();
	},
	'cmRefresh' : function(t) {
		location.reload();
	},
	'pmAttribute' : function(t) {
		attrSetup(t);
	},
	'pmDelete' : function(t) {
		deleteFlowNodeJson(t.id);
		deleteEndpoints(t.id);
	}
};
function attrSetup(t) {
	var bussOid = $("#bussOid").val();
	var diag = openDialog('环节属性', 900, 600, 'initWorkflowLinkAttr.do?nodeId=' + t.id + "&bussOid=" + bussOid, function(nodeJson){
		if(nodeJson) {
			addOrUpdateFlowNodeJson(nodeJson);
		}
		diag.close();
	});
}
//保留当前已编辑的工作流
function saveFlow() {
	if(checkFlow(true)) {
		var bussOid = $("#bussOid").val();
		// 对流程信息进行编码
		var flowInfo = encodeURIComponent(encodeURIComponent(JSON.stringify(flowEditJson)));
		var data = {
			"bussOid" : bussOid,
			"flowInfo" : flowInfo
		};
		common.ajaxPost('workflow/flow/saveFlow.do', data,
			function(data) {
				var mes = eval(data);
				if (mes.success) {
					Dialog.alert("保存成功！", function() {
						window.close();
					});
				} else {
					if (mes.data.length > 0) {
						Dialog.alert(mes.data);
					}
				}
			});
	}
}
var timtLimitTotal;
//对当前已编辑的工作流进行效验
function checkFlow(flag) {
	var endpoints = $("#flowchart-demo .endpoint");
	if (endpoints.length < 1) {
		Dialog.alert("至少添加一个节点信息！");
		return;
	}
	var mes = checkBeginEndNode();
	if(mes) {
		Dialog.alert(mes);
		return false;
	}
	timtLimitTotal = 0;
	for(var i = 0; i < endpoints.length; i++) {
		var mes = checkNode($(endpoints[i]).attr("id"));
		if(mes) {
			Dialog.alert(mes);
			return false;
		}
	}
	/* if(timtLimitTotal > parseInt(bussTimeLimit)) {
		Dialog.alert('环节总时限不能大于业务总时限！');
		return;
	} */
	if (!flag) {
		Dialog.alert('效验通过！');
	}
	return true;
}
//验证节点属性，配置是否正确，连接点，属性验证
function checkBeginEndNode() {
	var beginNodeId = "flowchartBegin";
	var endNodeId = "flowchartEnd";
	var beginEpCons = getConnectionByNodeId(beginNodeId); 
	if(beginEpCons.length < 1) {
		return "效验不通过，未设置开始节点！";
	}
	var endEpCons = getConnectionByNodeId(endNodeId); 
	if(endEpCons.length < 1) {
		return "效验不通过，未设置结束节点！";
	}
}
//验证节点属性，配置是否正确，连接点，属性验证
function checkNode(nodeId) {
	var nodeJson = getFlowNodeJson(nodeId);
	if(nodeJson) {
		//获取当前节点的连接线
		var epCons = getConnectionByNodeId(nodeId);
		if(epCons.length < 2) {
			return "效验不通过，节点“<font color='red'>" + nodeJson.name + "”</font>的连接线不完整！";
		}
		var beginLinkFlag = false;
		var endLinkFlag = false;
		for(var i = 0; i < epCons.length; i++) {
			var epCon = epCons[i];
			var epConPoints = epCon.endpoints;
			if(epConPoints.length >= 2) {
				var beginEp = epCon.endpoints[0];
				var beginElementId = beginEp.elementId;	//获取连接线的第节点的ID
				if(beginElementId == nodeId) {
					beginLinkFlag = true;
					continue;
				}
				var endEp = epCon.endpoints[1];
				var endElementId = endEp.elementId;	//获取连接线的第节点的ID
				if(endElementId == nodeId) {
					endLinkFlag = true;
					continue;
				}
			}
		}
		if(!beginLinkFlag || !endLinkFlag) {
			return "效验不通过，节点“<font color='red'>" + nodeJson.name + "”</font>的连接线不完整！";
		}
		
		if(!nodeJson.handleType || !nodeJson.handleUserType) {
			return "效验不通过，节点“<font color='red'>" + nodeJson.name + "”</font>属性未设置！";
		}
		
		if(!nodeJson.userOids && !nodeJson.postOids && !nodeJson.innerFlowOid) {
			return "效验不通过，节点“<font color='red'>" + nodeJson.name + "”</font>办理人、办理岗位、内嵌流程至少设置一个！";
		}
		
		if(!common.isDigits(nodeJson.timeLimit + "")) {
			return "效验不通过，节点“<font color='red'>" + nodeJson.name + "”</font>的办理时限不是数字！";
		}
		
		timtLimitTotal += parseInt(nodeJson.timeLimit);
		
		//获取节点的位置
		var nodeTop = $("#" + nodeId).css('top');
		var nodeLeft = $("#" + nodeId).css('left');
		nodeTop = nodeTop.replace('px', '');
		nodeLeft = nodeLeft.replace('px', '');
		nodeJson.top = nodeTop - $("#flowchart-demo").offset().top + 40;;
		nodeJson.left = nodeLeft - $("#flowchart-demo").offset().left;
		addOrUpdateFlowNodeJson(nodeJson);
		//获取节点的后节点、驳回节点、退回节点
		var nextIds = '';	//后节点集合
		var rejectIds = '';	//驳回节点集合
		var fallbackIds = '';//退回节点集合
		var preIds = '';	//前节点集合
		nodeJson.nodeType = '';
		for(var i = 0; i < epCons.length; i++) {
			var epCon = epCons[i];
			var eps = epCon.endpoints;
			var beginNodeElementId = eps[0].elementId;
			var nodeElementId = eps[1].elementId;
			var type = eps[1].anchor.type;
			//开始节点为当前节点时，计算后续节点
			if(beginNodeElementId == nodeId) {	
				//下一节点不是结束节点
				if(nodeElementId != "flowchartEnd") {
					if(type == "LeftMiddle") {
						if (nextIds.indexOf(nodeElementId) < 0) {
							nextIds += nodeElementId + ",";
						}
					} else if(type == "Top") {
						if (rejectIds.indexOf(nodeElementId) < 0) {
							rejectIds += nodeElementId + ",";
						}
					} else if(type == "Bottom") {
						if (fallbackIds.indexOf(nodeElementId) < 0) {
							fallbackIds += nodeElementId + ",";
						}
					}
				} else {
					//设置结束节点的连接线
					nodeJson.nodeType += "end,";
				}
			}
			//结束节点为当前节点时，计算前节点，不计算开始节点
			if(nodeElementId == nodeId) {
				if(beginNodeElementId != 'flowchartBegin') {
					if(type == "LeftMiddle") {
						if (preIds.indexOf(beginNodeElementId) < 0) {
							preIds += beginNodeElementId + ",";
						}
					}
				} else {
					//设置开始节点的连接线
					nodeJson.nodeType += "begin,";
				}
			}
		}
		nodeJson.nextIds = nextIds;
		nodeJson.rejectIds = rejectIds;
		nodeJson.fallbackIds = fallbackIds;
		nodeJson.preIds = preIds;
	}
}
// 右键菜单的样式
var contextmenu = {
	bindings : contextMenuRightClick,
	itemStyle : defaults.itemStyle,
	itemHoverStyle : defaults.itemHoverStyle
};
/*
 * 插入一个节点 toId：对象的ID nodeType：节点类型1-开始节点，2-正常节点，3-结束节点
 */
var insertEndpoints = function(toId, nodeType) {
	var sourceAnchors = [ "RightMiddle" ];
	//目标节点的连接点，LeftMiddle正常流程节点，Top驳回节点，Bottom退回节点
	var targetAnchors = [ "LeftMiddle", "Top", "Bottom" ];
	if (!nodeType || (nodeType != "1" && nodeType != "3")) {
		nodeType = "2";
	}
	if (nodeType == "1") {
		sourceAnchors = [ [ 0.5, 0.5, 0, 0 ] ];
		targetAnchors = [];
	} else if (nodeType == "3") {
		sourceAnchors = [];
		targetAnchors = [ [ 0.5, 0.5, 0, 0 ] ];
	} else {
		// 为节点添加右键事件
		$("#" + toId).contextMenu('processMenu', contextmenu);
	}
	// 设置连接开始点
	for (var i = 0; i < sourceAnchors.length; i++) {
		var sourceUUID = toId + "__" + sourceAnchors[i];
		if (nodeType == "1" || nodeType == "3") {
			sourceUUID = toId + "__";
		}
		
		var epp;
		if (nodeType == "1") {
			epp = instance.addEndpoint(toId, sourceFirstEndpoint, {
				anchor : sourceAnchors[i],
				uuid : sourceUUID
			});
		} else {
			epp = instance.addEndpoint(toId, sourceEndpoint, {
				anchor : sourceAnchors[i],
				uuid : sourceUUID
			});
		}
	}
	// 设置连接结束点
	for (var i = 0; i < targetAnchors.length; i++) {
		var targetUUID = toId + "__" + targetAnchors[i];
		if (nodeType == "1" || nodeType == "3") {
			targetUUID = toId + "__";
		}
		var anchor = targetAnchors[i];
		var ep;
		if (nodeType == "3") {
			ep = instance.addEndpoint(toId, targetFinishEndpoint, {
				anchor : anchor,
				uuid : targetUUID
			});
		} else {
			ep = instance.addEndpoint(toId, targetEndpoint, {
				anchor : anchor,
				uuid : targetUUID
			});
		}
		var fillStyleColor = defaults.defaultColor;
		var paintStyle = {
			radius : 3
		};
		if (nodeType == "3") {	//重设结束节点的样式
			paintStyle = { radius : 5 };
			fillStyleColor = defaults.defaultBeginEndColor;
		}
		paintStyle.width = 8;
		paintStyle.height = 20;
		if (targetAnchors[i] == defaults.Top) {
			fillStyleColor = defaults.TopColor;
			paintStyle.width = 20;
			paintStyle.height = 8;
		} else if (targetAnchors[i] == defaults.Bottom) {
			fillStyleColor = defaults.BottomColor;
			paintStyle.width = 20;
			paintStyle.height = 8;
		}
		paintStyle.fillStyle = fillStyleColor;
		ep.setDragAllowedWhenFull(true);
		ep.setPaintStyle(paintStyle);
		ep.setElement(toId);
	}

	// make all the window divs draggable
	instance.draggable(jsPlumb.getSelector("#" + toId), {
		grid : [ 20, 20 ]
	});
	if (nodeType == "2") {
		instance.makeTarget(toId);
	}
};

/**
 * 创建新的节点 nodeId 节点名称 nodeType：节点类型1-开始节点，2-正常节点，3-结束节点 nodeText 节点内容 top 位置
 * left 位置
 */
var createEndpoints = function(nodeId, nodeType, nodeText, top, left) {
	if (!nodeText)
		nodeText = "默认节点名称";
	if (!top)
		top = 50;
	if (!left)
		left = 150;
	top = top + "px";
	left = left + "px";
	var divContent = "<div ondblclick='attrSetup(this)' class='window endpoint point' id='"
			+ nodeId
			+ "' style='top: "
			+ top
			+ "; left: "
			+ left
			+ ";font-family:\"microsoft yahei\"'><span class='process-flag badge badge-info' id='jsPlumb_1_1' >"
			+ "<i class='icon-book icon-white'></i></span>&nbsp;"
			+ nodeText + "</div>";
	if (nodeType == "1") {
		divContent = "<div class='window' id='"
				+ nodeId
				+ "' style='top: "
				+ top
				+ "; left: "
				+ left
				+ "; width: 20px; height: 20px; border-radius: 20px; background-color: #2ec8a4'></div>";
	} else if (nodeType == "3") {
		divContent = "<div class='window' id='"
				+ nodeId
				+ "' style='top: "
				+ top
				+ "; left: "
				+ left
				+ "; width: 20px; height: 20px; border-radius: 20px; background-color: #FF0000'></div>";
	}
	$("#flowchart-demo").append(divContent);

	insertEndpoints(nodeId, nodeType);
}
/**
 * 修改节点信息 目前只修改节点显示值
 */
var updateEndpoints = function(nodeId, nodeText) {
	$("#" + nodeId).html("<span class='process-flag badge badge-info' id='jsPlumb_1_1' >"
			+ "<i class='icon-book icon-white'></i></span>&nbsp;" + nodeText);
}
/**
 * 删除节点
 */
var deleteEndpoints = function(toId) {
	// 删除连接线
	instance.detachAllConnections(jsPlumb.getSelector("#" + toId));
	// 删除连接点
	var objEndpoints = instance.getEndpoints(jsPlumb.getSelector("#" + toId));
	for (var i = 0; i < objEndpoints.length; i++) {
		instance.deleteEndpoint(objEndpoints[i]);
	}
	// 删除节点
	$("#" + toId).remove();
}
var addConnection = function(beginNode, endNode) {
	instance.connect({uuids: [beginNode, endNode], editable: false});
}
var clearAllConnections = function() {
	Dialog.confirm("你确定清空所有连接吗?", function(){
		var points = $("#flowchart-demo .endpoint");
		for(var i = 0; i < points.length; i++) {
			instance.detachAllConnections(jsPlumb.getSelector("#" + $(points[i]).attr("id")));
		}
	});
}
var clearAllErrorConnections = function(sourceId, targetId) {
	var conns = instance.getAllConnections();
	if(conns) {
		var deleteConns = new Array();
		var sourceUUID = sourceId + "__RightMiddle";
		if(sourceId == "flowchartBegin") {
			sourceUUID = sourceId + "__";
		}
		var targetUUID = targetId + "__LeftMiddle";
		if(targetId == "flowchartEnd") {
			targetUUID = targetId + "__";
		}
		for(var i = 0; i < conns.length; i++) {
			var conn = conns[i];
			if (conn.endpoints && conn.endpoints.length > 1 
					&& conn.endpoints[0].getUuid() == sourceUUID) {
				if(!conn.endpoints[1].getUuid() || conn.endpoints[1].getUuid() == targetUUID) {
					deleteConns.push(conn);
				}
			}
		}
		for(var i = 0; i < deleteConns.length; i++) {
			jsPlumb.detach(deleteConns[i]);
		}
		addConnection(sourceUUID, targetUUID);
	}
}
// suspend drawing and initialise.
instance.batch(function() {
	$("#flowchart-demo").contextMenu('canvasMenu', contextmenu);

	// listen for new connections; initialise them the same way we
	// initialise the connections at startup.
	instance.bind("connection", function(connInfo, originalEvent) {
		connectionComplete(connInfo.connection);
	});

	// 连接线删除确认操作
	instance.bind("click", function(c) {
		// 当点击的为点时，c.source为undefined
		if (c.source) {
			Dialog.confirm("你确定取消连接吗?", function(){
				var ep0 = c.endpoints[0].getUuid();
				var ep1 = c.endpoints[1].getUuid();
				// 删除两个节点之间的所有连接线
				var conns = instance.getAllConnections();
				var deleteConns = new Array();
				if(conns) {
					for(var i = 0; i < conns.length; i++) {
						var conn = conns[i];
						if (conn.endpoints && conn.endpoints.length > 1 
								&& conn.endpoints[0].getUuid() == ep0 
								&& conn.endpoints[1].getUuid() == ep1) {
							deleteConns.push(conn);
						}
					}
				}
				for(var i = 0; i < deleteConns.length; i++) {
					jsPlumb.detach(deleteConns[i]);
				}
			});
		}
			
	});

});

jsPlumb.fire("jsPlumbDemoLoaded", instance);

//根据节点编号获取节点的所有连接线
function getConnectionByNodeId(nodeId) {
	var epCons = instance.getAllConnections();
	var curEpCons = [];
	for(var i = 0; i < epCons.length; i++) {
		var epCon = epCons[i];
		if(epCon && epCon.endpoints && epCon.endpoints.length > 1) {
			var beginEp = epCon.endpoints[0];
			var beginElementId = beginEp.elementId;	//获取连接线的第节点的ID
			var endEp = epCon.endpoints[1];
			var endElementId = endEp.elementId;	//获取连接线的后节点的ID
			if(beginElementId == nodeId || endElementId == nodeId) {
				curEpCons.push(epCon);
			}
		}
	}
	return curEpCons;
}