//block标准对象
var Block = function(){
	//htmlid
	//'block_id_' + idx++
	//this.sort = idx
	this.block_html_id;
	//编号
	this.block_code;
	//名称
	this.block_name;
	//类型（1-印刷、2-手写、3-图像、4-标识块）
	this.block_type;
	//排序
	//this.sort = this.block_html_id.substring(9);
	this.sort;
	//以下属性为初始化block及提交服务端时使用
	//数据库持久化id
	this.block_oid;
	//x轴坐标
	this.x;
	//y轴坐标
	this.y;
	//宽
	this.width;
	//高
	this.height;
	//所属目录元素
	this.meta_data;
	//是否确认保存
	this.is_saved = false;
}
//block工具方法
var block_helper = {
	add_block : function(a,block){
		a.push(block);
	},
	//V1
	add_block_1 : function(a,block_html_id,block_name){
		var b = new Block();
		b.block_html_id = block_html_id;
		b.block_name = block_name;
		b.sort = block_html_id.substring(9);
		a.push(b);
	},
	//V2
	add_block_2 : function(a,block_html_id){
		var b = new Block();
		b.block_html_id = block_html_id;
		b.sort = block_html_id.substring(9);
		a.push(b);
	},
	remove_block : function(a,block){
		for(var i = 0;i < a.length;i++){
			var b = a[i];
			if(b.block_html_id = block.block_html_id){
				a.splice(i,1);
			}
		}
	},
	remove_block_1 : function(a,block_html_id){
		for(var i = 0;i < a.length;i++){
			var b = a[i];
			if(b.block_html_id == block_html_id){
				var java_block_oid = b.block_oid;
				if(java_block_oid){
					business.del_java_block_oid_array.push(java_block_oid);
				}
				a.splice(i,1);
				return a;
			}
		}
	},
	update_block : function(a,block){
		for(var i = 0;i < a.length;i++){
			var b = a[i];
			if(b.block_html_id == block.block_html_id){
				if(block.block_html_id)
					b.block_html_id = block.block_html_id;
				if(block.block_code)
					b.block_code = block.block_code;
				if(block.block_name)
					b.block_name = block.block_name;
				if(block.block_type)
					b.block_type = block.block_type;
				if(block.block_oid)
					b.block_oid = block.block_oid;
				if(block.x)
					b.x = block.x;
				if(block.y)
					b.y = block.y;
				if(block.width)
					b.width = block.width;
				if(block.height)
					b.height = block.height;
				if(block.meta_data)
					b.meta_data = block.meta_data;
				if(block.is_saved)
					b.is_saved = block.is_saved;
			}
		}
	},
	get_block : function(a,block_html_id){
		for(var i = 0;i < a.length;i++){
			var b = a[i];
			if(b.block_html_id == block_html_id){
				return b;
			}
		}
		return null;
	},
	//查找已被设置为特征块的区块
	get_block_2 : function(a){
		for(var i = 0;i < a.length;i++){
			var b = a[i];
			if(b.block_type == 4){
				return b;
			}
		}
		return null;
	},
	to_java_block : function(block){
    console.log("block_array******"+JSON.stringify(block_array))
		var java_block = {};
    java_block.faModelTemplateBlockOid = block.block_oid;
    java_block.id = block.block_idnew;
		java_block.oid = block.block_oid;
		java_block.blockCode = block.block_code;
		java_block.blockName = block.block_name;
		java_block.recognitionType = block.block_type;
		java_block.height = block.height;
		java_block.width = block.width;
		java_block.x = block.x;
		java_block.y = block.y;
		java_block.sort = block.sort;
		if(block.is_saved && block.meta_data){
			java_block.materialCatalogElementOid = block.meta_data.materialCatalogElementOid;
		}
    if(block.is_saved && block.meta_data.elementOid){
      java_block.materialCatalogElementOid = block.meta_data.elementOid;
    }
		return java_block;
	},
	to_string : function(a){
		return JSON.stringify(a);
	},
	clone_block : function(block){
		return JSON.parse(JSON.stringify(block));
	}

}
