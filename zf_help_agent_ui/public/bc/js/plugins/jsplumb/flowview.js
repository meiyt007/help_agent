jsPlumb.ready(function () {
    var instance = jsPlumb.getInstance({
        // default drag options
        DragOptions: { cursor: 'pointer', zIndex: 2000 },
		Connector: [ "Flowchart", { curviness: 50 } ],
        PaintStyle: { strokeStyle: "gray", lineWidth: 2 },
        ConnectionOverlays: [
            [ "Arrow", { location: 1,
                length: 10,
                width: 12,
                foldback: 1} ]
        ],
        Container: "flowchart-demo"
    });

    var basicType = {
        connector: "StateMachine",
        paintStyle: { strokeStyle: "red", lineWidth: 1 },
        overlays: [ "Arrow"  ]
    };
    instance.registerConnectionType("basic", basicType);

    // this is the paint style for the connecting lines..
    var endpointHoverStyle = {
            fillStyle: "#216477",
            strokeStyle: "#216477"
        },
        sourceEndpoint = {
            endpoint: "Dot",
            paintStyle: { fillStyle: "#7AB02C", radius: 1 },
            maxConnections: -1,
            dropOptions: { hoverClass: "hover", activeClass: "active" },
            isTarget: true
        }; 

    var _addEndpoints = function (toId, sourceAnchors) {
        for (var i = 0; i < sourceAnchors.length; i++) {
            var sourceUUID = toId + "__" + sourceAnchors[i];
            instance.addEndpoint("flowchart" + toId, sourceEndpoint, {
                anchor: sourceAnchors[i], uuid: sourceUUID
            });
        }
    };

    // suspend drawing and initialise.
    instance.batch(function () {
    	for(var i = 0; i < stepList.length; i++) {
    		var step = stepList[i];
    	    _addEndpoints("Window" + step.bzhm, ["LeftMiddle", "RightMiddle", "Top", "Bottom"]);
    	}

        instance.bind("connection", function (connInfo, originalEvent) {
            //init(connInfo.connection);
        	connectionComplete(connInfo.connection);
        });

        instance.draggable(jsPlumb.getSelector(".flowchart-demo .window"), { grid: [20, 20] });

		for ( var i = 0; i < stepList.length; i++) {
			var step = stepList[i];
			if (step.nextStepBzhm && step.nextStepBzhm != "") {
				var ns = step.nextStepBzhm.split(",").unique();
				for(var j = 0; j < ns.length; j++) {
					if(ns[j]) {
						instance.connect({
							uuids : [ "Window" + (step.bzhm) + "__RightMiddle",
									"Window" + ns[j] + "__LeftMiddle" ],
							editable : false
						});
					}
				}
			}
			if (step.rejectNumbers && step.rejectNumbers != "") {
				var ns = step.rejectNumbers.split(",").unique();
				for(var j = 0; j < ns.length; j++) {
					if(ns[j]) {
						instance.connect({
							uuids : [ "Window" + (step.bzhm) + "__RightMiddle",
									"Window" + ns[j] + "__Top" ],
							editable : false
						});
					}
				}
			}
			if (step.fallbackNumbers && step.fallbackNumbers != "") {
				var ns = step.fallbackNumbers.split(",").unique();
				for(var j = 0; j < ns.length; j++) {
					if(ns[j]) {
						instance.connect({
							uuids : [ "Window" + (step.bzhm) + "__RightMiddle",
									"Window" + ns[j] + "__Bottom" ],
							editable : false
						});
					}
				}
			}
			if (step.nextStepNotHandleBzhm && step.nextStepNotHandleBzhm != "") {
				var ns = step.nextStepNotHandleBzhm.split(",").unique();
				for(var j = 0; j < ns.length; j++) {
					if(ns[j] && step.nextStepBzhm.indexOf(ns[j] + ",") < 0) {
						instance.connect({
							uuids : [ "Window" + (step.bzhm) + "__RightMiddle",
									"Window" + ns[j] + "__LeftMiddle" ],
							editable : false,
							type : 'dian'
						});
					}
				}
			}
			if (step.rejectNotHandleNumbers && step.rejectNotHandleNumbers != "") {
				var ns = step.rejectNotHandleNumbers.split(",").unique();
				for(var j = 0; j < ns.length; j++) {
					if(ns[j] && step.rejectNumbers.indexOf(ns[j] + ",") < 0) {
						instance.connect({
							uuids : [ "Window" + (step.bzhm) + "__RightMiddle",
									"Window" + ns[j] + "__Top" ],
							editable : false,
							type : 'dian'
						});
					}
				}
			}
			if (step.fallbackNotHandleNumbers && step.fallbackNotHandleNumbers != "") {
				var ns = step.fallbackNotHandleNumbers.split(",").unique();
				for(var j = 0; j < ns.length; j++) {
					if(ns[j] && step.fallbackNumbers.indexOf(ns[j] + ",") < 0) {
						instance.connect({
							uuids : [ "Window" + (step.bzhm) + "__RightMiddle",
									"Window" + ns[j] + "__Bottom" ],
							editable : false,
							type : 'dian'
						});
					}
				}
			}
			
			(function(step){
				var pl = new PopupLayer({
					trigger : "#flowchartWindow" + step.bzhm,
					popupBlk : "#workflowDetail" + step.bzhm,
					eventType : "mouseover"
				});
				$("#flowchartWindow" + step.bzhm).bind("mouseout", function() {
					pl.close();
				});
				$("#workflowDetail" + step.bzhm).bind("mouseover", function() {
					$("#flowchartWindow" + step.bzhm).mouseover();
				});
				$("#workflowDetail" + step.bzhm).bind("mouseout", function() {
					pl.close();
				});
				
				var flowchartWindow = $("#flowchartWindow" + step.bzhm);
				//获取base路径
				var htmlBasePath = $("html head base").attr("href");
				var url = "";
				if(step.innerHandleOid) {
					url = 'workflow/flow/view.do?handleOid=' + step.innerHandleOid;
				} else {
					if(step.innerFlowOid) {
						url = 'workflow/flow/view.do?bussOid=' + step.innerFlowOid;
					}
				}
				if(url) {
					flowchartWindow.click(function(){
						window.open(htmlBasePath + url);
					});
				}
				flowchartWindow.removeClass();
				//办理状态，1-等待办理,2-正在办理,3-按时办理,4-环节超期,5-暂停
				if(step.handleStatus == "4") {
					flowchartWindow.addClass("flowlist flowlist1");
				} else if(step.handleStatus == "2") {
					flowchartWindow.addClass("flowlist flowlist3 shadow");
					//环节闪烁
					shake(flowchartWindow, "window0");
				} else if(step.handleStatus == "3") {
					flowchartWindow.addClass("flowlist flowlist4");
				} else if(step.handleStatus == "5") {
					flowchartWindow.addClass("flowlist flowlist5");
				} else {
					flowchartWindow.addClass("flowlist flowlist2");
				}
				
			})(step);
		} 
		
    });

    jsPlumb.fire("jsPlumbDemoLoaded", instance);

});
//连接线完成执行方法
var connectionComplete = function(connection) {
	setConnectColor(connection);
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
	var strokeStyleColor = "gray";
	if (uuid.indexOf("__Top") > 0) {
		strokeStyleColor = "#ff0000";
	} else if (uuid.indexOf("__Bottom") > 0) {
		strokeStyleColor = "#1B91D1";
	}
	// 设置连接线的颜色
	var ps = {
		lineWidth : 2,
		strokeStyle : strokeStyleColor,
		joinstyle : "round"
	};

	if(connection.getType().indexOf("dian") > -1) {
		ps.dashstyle="4 1";//分段显示4是每段长度 1是段与段之间的长度
	}
	connection.setPaintStyle(ps);
	connection.setHoverPaintStyle({
		lineWidth : 4,
		strokeStyle : strokeStyleColor
	});
	return true;
}
function shake(ele, cls, times) {
	var i = 0, t = false;
	if(times) times = times || 2;
	if (t)
		return;
	t = setInterval(function() {
		i++;
		if (i % 2) {
			ele.addClass("shadow");
		} else {
			ele.removeClass("shadow");
		}
		if (times && i == 2 * times) {
			clearInterval(t);
			ele.addClass("shadow");
		}
	}, 600);
};