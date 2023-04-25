var treeObj;
var ztree_root_id = -1;
var treeId = 'block_tree'
var zNodes =[
	{ id:ztree_root_id, pId:0, name:"区块列表", open:true}
];
treeObj = $.fn.zTree.init($("#" + treeId), setting, zNodes);

//alert(JSON.stringify(treeObj))
var setting = {
	edit: {
		enable: true,
		showRenameBtn:false,
		showRemoveBtn: false
	},
	data: {
		simpleData: {
			enable: true
		}
	},
	callback: {
		beforeClick: zTreeBeforeClick,
		onClick: zTreeOnClick,
		onCollapse: zTreeOnCollapse,
		onExpand: zTreeOnExpand
	}
};

$(document).ready(function(){
	treeObj = $.fn.zTree.init($("#" + treeId), setting, zNodes);

});

//规避删除冒泡
function zTreeBeforeClick(treeId, treeNode, clickFlag){
	if(!treeNode)return false;
}

//V1版本
//点击节点回调
function zTreeOnClick_V1(event, treeId, treeNode){
	//点击编辑、删除按钮
	if(event.target.className.match(/mybtn/)){
		return;
	}
	if(treeNode.id == ztree_root_id) {
		core.resetBlockAssists();
		return;
	}

	var block_id = treeNode.block_html_id;
	if(!core.readOnly){
		ztree_helper.removeBtns();
		addEidtBtn(treeId, treeNode);
		addDelBtn(treeId, treeNode);
	}
	//高亮边缘
    core.activeBlock(block_id);
    //定位block
    core.gotoBlock(block_id);
    //只读查看
    core.readOnly && core.showBlockInfo(block_id);
}

//V2版本
//点击节点回调
function zTreeOnClick(event, treeId, treeNode){
	if(treeNode.id == ztree_root_id) {
		core.resetBlockAssists();
		return;
	}

	var block_id = treeNode.block_html_id;
	if(!block_id){
		return;
	}
	//高亮边缘
    core.activeBlock(block_id);
    //定位block
    core.gotoBlock(block_id);
    //只读查看
    core.readOnly && core.showBlockInfo(block_id);
    //改变区块层级
    core.resetZindex(block_id);
}

//节点被折叠
function zTreeOnCollapse(event, treeId, treeNode){
	core.resizeScroll();
}

//节点被展开
function zTreeOnExpand(event, treeId, treeNode){
	core.resizeScroll();
}

//添加绑定成功标识
function addIcon(treeNode){
	var aObj = $("#" + treeNode.tId + "_a");
	if ($("#sucIcon_" + treeNode.id).length>0) return;
	var iconStr = "<span type='button' class='suc-icon mybtn' id='sucIcon_" + treeNode.id
		+ "' ></span>";
	aObj.append(iconStr);
}

//移除成功标志
function removeIcon(treeNode){
	$("#sucIcon_" + treeNode.id).remove();
}

//添加编辑按钮
function addEidtBtn(treeId, treeNode) {
	var aObj = $("#" + treeNode.tId + "_a");
	if ($("#editBtn_" + treeNode.id).length>0) return;
	var editStr = "<span type='button' class='button edit mybtn' id='editBtn_" + treeNode.id
		+ "' title='编辑' ></span>";
	aObj.append(editStr);
	var btn = $("#editBtn_" + treeNode.id);
	if (btn) btn.bind("click", function(){
		core.initBlock(treeNode.block_html_id);
	});
}

//添加删除按钮
function addDelBtn(treeId, treeNode) {
	var aObj = $("#" + treeNode.tId + "_a");
	if ($("#delBtn_" + treeNode.id).length>0) return;
	var detStr = "<span type='button' class='button remove mybtn' id='delBtn_" + treeNode.id
		+ "' title='删除区块' ></span>";
	aObj.append(detStr);
	var btn = $("#delBtn_" + treeNode.id);
	if (btn) btn.bind("click", function(){
		var block_id = treeNode.block_html_id;
		core.deleteBlock(block_id);
	});
}


var ztree_helper = {
	//V1 根据block_id增加ztree节点
	addNode : function(block_id){
		var id = block_id.substring(9);
		var block_name = "区块" + id;
		var newNode = {name:block_name,block_html_id:block_id,id:id};
		var rootNode = treeObj.getNodeByParam("id", ztree_root_id, null);
		treeObj.addNodes(rootNode, newNode);
		core.resizeScroll();
		return block_name;
	},
	//V1 根据block对象添加ztree节点
	addNode_1 : function(block){
		var block_name = block.block_name;
		var id = block.sort;
		var newNode = {name:block_name,block_html_id:block.block_html_id,id:id};
		var rootNode = treeObj.getNodeByParam("id", ztree_root_id, null);
		treeObj.addNodes(rootNode, newNode);
		core.resizeScroll();
		return block_name;
	},
	//V2 加载目录子项元素
	addNodes : function(metaDataJsonArrayStr){
		if(metaDataJsonArrayStr == '' && metaDataJsonArrayStr == undefined){
			return false;
		}
		var metaDataJsonArray = JSON.parse(metaDataJsonArrayStr);
		for(var i=0;i<metaDataJsonArray.length;i++){
			var metaData = metaDataJsonArray[i];
			var node_id = metaData.materialCatalogElementOid;
			var block_name = metaData.elementName;
			var newNode = {name:block_name,block_html_id:null,id:metaData.elementOid};
			var rootNode = treeObj.getNodeByParam("id", ztree_root_id, null);
      treeObj.addNodes(rootNode, newNode);
		}
/*    console.log("@@@"+JSON.stringify(newNode))
		console.log("%%%%"+JSON.stringify(rootNode))
    alert("%%%%"+JSON.stringify(treeObj))*/
    zNodes=treeObj.getNodeByParam("id", ztree_root_id, null);
		core.resizeScroll();
		return true;
	},
	//V1
	removeNode : function(block_id){
		var node = treeObj.getNodeByParam("block_html_id", block_id, null);
		treeObj.removeNode(node,false);
		core.resizeScroll();
	},
	//增加元素绑定
	updateNode : function(block_id,meta_data_oid){
	  //alert(block_id+"**"+meta_data_oid)
		var treeNode1 = treeObj.getNodeByParam("block_html_id", block_id, null);
		if(treeNode1 && treeNode1.id == meta_data_oid){
			this.selectNode(block_id);
			return;
		}else if(treeNode1){
			treeNode1.block_html_id = null;
			treeObj.updateNode(treeNode1);
			removeIcon(treeNode1);
		}
		var treeNode = treeObj.getNodeByParam("id", meta_data_oid,null);
		var NodesArr = treeObj.getNodes()

		//alert(block_id);
    console.log(treeObj);
    console.log(treeNode1);
    console.log(treeNode);
    if(block_id != '' && block_id !=null){
      treeNode.block_html_id = block_id;
    }

		treeObj.updateNode(treeNode);
		addIcon(treeNode);
		this.selectNode(block_id);
	},
	//解除元素绑定
	updateNode2 : function(block_id){
		var treeNode = treeObj.getNodeByParam("block_html_id", block_id, null);
		if(!treeNode)return;
		treeNode.block_html_id = null;
		treeObj.updateNode(treeNode);
		removeIcon(treeNode);
		ztree_helper.unSelectNode();
	},
	//初始化左侧节点
	updateNode3 : function(block_id,meta_data_oid){
		var treeNode = treeObj.getNodeByParam("id", meta_data_oid, null);
		if(!treeNode)return;
		treeNode.block_html_id = block_id;
		treeObj.updateNode(treeNode);
		addIcon(treeNode);
	},
	//V1
	updateNode_V1 : function(block_id,block_name){
		var node = treeObj.getNodeByParam("block_html_id", block_id, null);
		node.name = block_name;
		treeObj.updateNode(node);
	},
	//删除所有自定义按钮
	removeBtns : function(){
		$('.mybtn').remove();
	},
	//选中ztree节点
	selectNode_V1 : function(block_id){
		var treeNode = treeObj.getNodeByParam("block_html_id", block_id, null);
		treeObj.selectNode(treeNode);
		if(!core.readOnly){
			ztree_helper.removeBtns();
			addEidtBtn(treeId, treeNode);
			addDelBtn(treeId, treeNode);
		}
	},
	//选中ztree节点
	selectNode : function(block_id){
		var treeNode = treeObj.getNodeByParam("block_html_id", block_id, null);
		treeObj.selectNode(treeNode);
	},
	loaded : function(fun){
		var t;
		if(!treeObj){
			t = setTimeout(function(){
				ztree_helper.loaded(fun);
			},100);
		}else{
			clearTimeout(t);
			fun();
		}
	},
	unSelectNode : function(){
		$('.curSelectedNode').removeClass('curSelectedNode');
	}
}
