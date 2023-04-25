//识别模板服务端json串
var java_template_json_str;
//区块服务端json串
var java_block_list_json_str;

//业务操作模块
var business = {
	//数据初始化
	init : function(){

    /*$('#saveBtncs').click(function(){
      business.pulishSuccess();
    });*/

		//templateVersion从jsp页面获取
		if(fromJsp.templateVersion != ''){
			$('#uploadBtn').addClass('disabled');
			$('#uploadMoreBtn').addClass('disabled');
			$('#clearBtn').addClass('disabled');
			$('#saveBtn').addClass('disabled');
			$('#submitBtn').addClass('disabled');
			//$('#switchCataBtn').addClass('disabled');
		}else{

			$('#clearBtn').click(function(){

				if(!core.fileOid){
					util.alert('请上传模板底图！');
					return;
				}
				business.clearAll();
			});
			$('#saveBtn').click(function(){
				if(!core.fileOid){
					util.alert('请上传模板底图！');
					return;
				}
				business.saveTemplate();
			});
			$('#submitBtn').click(function(){
				if(!core.fileOid){
					util.alert('请上传模板底图！');
					return;
				}
				var vali = core.valiteBlocks();
				if(!vali){
					return;
				}
				business.publishTemplate();
			});
			$('#uploadBtn').click(function(){
				core.cleanAllCovering();
				business.uploadImg();
			});
			$('#highShotBtn').click(function(){
				core.cleanAllCovering();
				business.uploadImg(function(){
					var d = this;
					$(d.innerFrame.contentWindow).load(function(){
						this.$('#highShot').click();
					});
				});
			});
			//$('#switchCataBtn').click(function(){
			//	business.showSwitchMaterialCata(fromJsp.bigCataOid,fromJsp.cataOid);
			//});

		}

    setTimeout(	business.clearAllDe,1000);


		if(fromJsp.templateStr == ''){//materialCatalogOid
			java_template_json_str = JSON.stringify({materialCatalogOid:fromJsp.cataOid});
		}else{
			java_template_json_str = fromJsp.templateStr;
		}

		java_block_list_json_str = fromJsp.blockArrayStr;

		//alert(java_block_list_json_str)
		//面板区域加载区块
		if(java_block_list_json_str != ''&& java_block_list_json_str != 'null')
			core.loadBlockArray(java_block_list_json_str,java_template_json_str);

		//保存成功后重定向到当前页面
		if(fromJsp.guideType != ''){
		  alert("保存成功后重定向到当前页面")
			business.guide_type = fromJsp.guideType;
			util.closeLoading();
			business.showSwitchMaterialCata(fromJsp.bigCataOid,fromJsp.cataOid);
		}

	},
	//删除的java block oid
	del_java_block_oid_array:[],
	//模板选择divid
	div_id_v : null,
	//目录选择divid
	div_id_c : null,
	//引导类型：1-切换目录，2-当前模板配置完成提示继续下一个模板配置
	//切换目录 按钮 已屏蔽
	guide_type : 1,
	//单击清空按钮
	clearAll : function(){
		util.confirm('确定要清空所有区块吗？',function(){
			core.clearAllBlock(function(){
				util.sucTip('成功清空所有区块！');
			});
		});
	},
  //单击清空按钮
  clearAllDe : function(){

      ztree_helper.addNodes(fromJsp.metaDataJsonArrayStr);

  },
	reloadOpener : function(){
		if(window.opener && window.opener.reloadPage) {
			window.opener.reloadPage();
		}
	},
	//暂存
	saveTemplate : function(fun){
	 /* alert("暂存")*/
    //刷新父页面
/*    window.close();
    business.reloadOpener();
    return;*/
		util.showLoading();
		var templateStr = java_template_json_str;
		var templateJ = JSON.parse(templateStr);
		var faModelTemplateOid=templateJ.faModelTemplateOid;
		var materialCatalogOid=templateJ.materialCatalogOid;
		templateJ.sizeRate = core.sizeRate;
		templateJ.baseImgWidth = core.baseImgWidth;
		templateJ.baseImgHeight = core.baseImgHeight;
		templateJ.baseImgStdWidth = core.baseImgStdWidth;
		templateJ.baseImgStdHeight = core.baseImgStdHeight;
		templateJ.baseImgFileOid = core.fileOid;
		templateJ.blockColorClass = core.blockColorClass;
		templateStr = JSON.stringify(templateJ);
		var blockArrayStr = core.getBlockArrayStr();
		var delBlockIds = this.del_java_block_oid_array.join(',');
		$.get(fromJsp.ctxPath + "/faModelTemplate/saveOrUpdateFaModelTemplate?saveType=1&random=" + Math.random(),
      {templateStr:templateStr,blockArrayStr:blockArrayStr,delBlockIds:delBlockIds},
			function(data, textStatus, jqXHR){
				if(data.data.success){
					java_template_json_str = JSON.stringify(data.data.templateStr);
					java_block_list_json_str = JSON.stringify(data.data.blockArrayStr);
					var faModelTemplateOidNew=data.data.templateOid;
					core.updateBlockArray(java_block_list_json_str);
					business.del_java_block_oid_array = [];
					util.closeLoading(function(){
						util.sucTip('保存成功！');
           /* var url='http://localhost:8899/bc/index.html?faModelTemplateOid='+faModelTemplateOidNew+'&materialCatalogOid='+materialCatalogOid;
            location.href=url;*/
					});

					//刷新父页面
					business.reloadOpener();
					if(fun)fun();
				//保存出错
				}else{
					util.closeLoading();
					util.alert('保存信息出错！');
				}
			},'json');

	},
	//发布，生成版本信息
	publishTemplate : function(){
		util.confirm('确定发布当前配置吗？',function(){
			util.showLoading();
			var templateStr = java_template_json_str;
			var templateJ = JSON.parse(templateStr);
      var materialCatalogOid=templateJ.materialCatalogOid;
			templateJ.sizeRate = core.sizeRate;
			templateJ.baseImgWidth = core.baseImgWidth;
			templateJ.baseImgHeight = core.baseImgHeight;
			templateJ.baseImgStdWidth = core.baseImgStdWidth;
			templateJ.baseImgStdHeight = core.baseImgStdHeight;
			templateJ.baseImgFileOid = core.fileOid;
			templateJ.blockColorClass = core.blockColorClass;
			templateStr = JSON.stringify(templateJ);
			var blockArrayStr = core.getBlockArrayStr();
			var delBlockIds = business.del_java_block_oid_array.join(',');
			//$.post(fromJsp.ctxPath + "fa/modelTemplate/save.do?saveType=2&random=" + Math.random(),
      $.get(fromJsp.ctxPath + "/faModelTemplate/saveOrUpdateFaModelTemplate?saveType=2&random=" + Math.random(),
				{templateStr:templateStr,blockArrayStr:blockArrayStr,delBlockIds:delBlockIds},
				function(data, textStatus, jqXHR){
					if(data.data.success){
						//business.guide_type = 2;
						util.closeLoading();
						//刷新父页面
						business.reloadOpener();
						//business.showSwitchMaterialCata(fromJsp.bigCataOid,fromJsp.cataOid);
						//改为url重定向，防止模板发布成功后，刷新页面
            var faModelTemplateOidNew=data.data.templateOid;
            /*var url='http://localhost:8899/bc/index.html?faModelTemplateOid='+faModelTemplateOidNew+'&materialCatalogOid='+materialCatalogOid;
            location.href=url;*/
            business.pulishSuccess();
						//location.href = fromJsp.ctxPath + "fa/modelTemplate/init.do?cataOid=" + fromJsp.cataOid + "&oid=" + data.templateOid + "&guideType=2";
					}else{
						util.closeLoading();
						util.alert('保存信息出错！');
					}
				},'json');
		});
	},
	uploadImg : function(fun){
		var diag = new Dialog();
		diag.Width = 800;
		diag.Height = 560;
		diag.Title = "上传底图";
		var cataName = encodeURI(encodeURI(fromJsp.materialCataName));
		diag.URL = 'imgatta.html?faModelTemplateOid='+faModelTemplateOid+'&materialCatalogOid='+materialCatalogOid;
		diag.diagClose = function(fileOid,filePath){
			diag.close();
			core.resetPanelUpdateBlockArray();
			var old_baseImgStdWidth = core.baseImgStdWidth;
			core.loadBgImage(filePath,fileOid,function(){
				//第一次底图加载失败
				if(!old_baseImgStdWidth && java_block_list_json_str != ''){
					core.loadBlockArray(java_block_list_json_str,java_template_json_str);
				}else{
					core.reDrawBlocks(old_baseImgStdWidth,core.baseImgStdWidth);
				}
			});

		};
		diag.show();
		fun && fun.call(diag);
	},
	showChooseTemplate : function(versionTemplateList){
		var diag = new Dialog();
		diag.Width = 600;
		diag.Height = 450;
		diag.Title = "选择已有模板";
		diag.InnerHtml = this.joinHtml_1(versionTemplateList);
		diag.ChangeWinSize = false;
		diag.ShowCloseButton = false;
		diag.show();
		//美化滚动条
		$("#" + this.div_id_v).niceScroll({autohidemode: false,cursorwidth: "8px",cursorborderradius: "0px",cursorcolor: "#9E9E9E",background: "#E2E2E2",cursorborder: "0px"});
	},
	joinHtml_1 : function(versionTemplateList){
		var l = JSON.parse(versionTemplateList);
		var idx = 1;
		var html = '';
		for(var i=0;i < l.length;i++){
			var t = l[i];
			html += '<tr>';
			html += '<td>' + idx++ + '</td>';
			html += '<td>' + t.templateCode + '</td>';
			html += '<td>' + t.templateName + '</td>';
			html += '<td>' + t.version + '</td>';
			html += '<td>' + t.modifyDate + '</td>';
			html += '<td><button type="button" class="btn btn-success" onclick="business.useTemplate(\'' + t.id + '\',\'' + t.faModelTemplateOid + '\');">选择</button></td>';
			html += '</tr>';
		}
		this.div_id_v = 'div_id_v_' + parseInt(Math.random()*10000,10);
		var html_ = $('#ver_sel_html_template').html().replace(/\{div_id\}/,this.div_id_v);
		var $html = $(html_);
		$html.find('#' + this.div_id_v + ' tbody').html(html);
		return $html.html();
	},
	unUseTemplate : function(){
		//隐藏自定义滚动条
		$("#" + this.div_id_v).getNiceScroll().hide();
		Dialog.close();
		business.uploadImg();
	},
	useTemplate : function(id,templateOid){
		util.showLoading();
		//隐藏自定义滚动条
		$("#" + this.div_id_v).getNiceScroll().hide();
		Dialog.close();
		 var urls=fromJsp.ctxPath + "/faModelTemplate/cloneTemplate?id=" + id + "&faModelTemplateOid=" + templateOid + "&random=" + Math.random();
		$.get(fromJsp.ctxPath + "/faModelTemplate/cloneTemplate?id=" + id + "&faModelTemplateOid=" + templateOid + "&random=" + Math.random(),
			null,
			function(data, textStatus, jqXHR){
				if(data.data.success){
					//刷新父页面
					business.reloadOpener();
					location.reload();
				}else{
					util.closeLoading();
				}
			},'json');
	},
	showSwitchMaterialCata : function(bigCataOid,smallCataOid){
		//没有子目录的情况
		if(bigCataOid == ''){
			if(business.guide_type == 1){
				util.alert('未查询到其他可切换的目录子项！');
				return;
			}else if(business.guide_type == 2){
				business.pulishSuccess();
				return;
			}
		}
		$.get(fromJsp.ctxPath + "fa/modelTemplate/otherCataList.do?random=" + Math.random(),
			{bigCataOid:bigCataOid,smallCataOid:smallCataOid},
			function(data, textStatus, jqXHR){
				if(data.success){
					//没有其他目录，提示发布成功
					if(data.data == -1){
						business.pulishSuccess();
						return;
					}

					var diag = new Dialog();
					diag.Width = 600;
					diag.Height = 450;
					diag.InnerHtml = business.joinHtml_2(data.data);
					diag.ChangeWinSize = false;
					if(business.guide_type == 1){
						diag.Title = "切换目录子项";
						diag.ShowCloseButton = true;
					}else if(business.guide_type == 2){
						diag.Title = "模板发布成功！";
						diag.ShowCloseButton = true;
						diag.CancelEvent = function(){
							window.close();
						}
					}
					diag.show();
				}else{
					util.closeLoading();
				}
			},'json');
	},
	joinHtml_2 : function(cataList){
		var l = JSON.parse(cataList);
		var idx = 1;
		var html = '';
		for(var i=0;i < l.length;i++){
			var c = l[i];
			html += '<tr>';
			html += '<td style="text-align:center">' + idx++ + '</td>';
			html += '<td><a style="cursor:pointer;" onclick="business.doSwitchCata(\'' + c.smallCataOid + '\')">' + c.smallCataName + '</a></td>';
			html += '<td style="text-align:center">' + c.stateDesc + '</td>';
			html += '</tr>';
		}
		this.div_id_c = 'div_id_c_' + parseInt(Math.random()*10000,10);
		var html_ = $('#cata_sw_html_template').html().replace(/\{materialCataName\}/g,fromJsp.materialCataName).replace(/\{div_id\}/,this.div_id_c);
		var $html = $(html_);
		$html.find('#' + this.div_id_c + ' tbody').html(html);
		if(business.guide_type == 1){
			$html.find('blockquote p:last').remove();
		}else if(business.guide_type == 2){
			$html.find('blockquote p:first').remove();
		}
		return $html.html();
	},
	doSwitchCata : function(cataOid){
	  alert("doSwitchCata")
		if(business.guide_type == 1){
			this.saveTemplate(function(){
				var url = fromJsp.ctxPath + 'fa/modelTemplate/init.do?cataOid=' + cataOid;
				window.close();
				window.open(url);
			});
		}else if(business.guide_type == 2){
			var url = fromJsp.ctxPath + 'fa/modelTemplate/init.do?cataOid=' + cataOid;
			window.close();
			window.open(url);
		}

	},
	pulishSuccess : function(){
		var diag = new Dialog();
		diag.Width = 350;
		diag.Height = 350;
		diag.InnerHtml = '<div style="font-size:16px;margin:20px auto;">当前模板发布成功！请手动关闭当前页面！</div>';
		diag.ChangeWinSize = false;
		diag.Title = "模板发布成功！";
		diag.AutoClose = 10;
		diag.ShowCloseButton = true;
		diag.CancelEvent = function(){
			window.close();
		}
		//等待loading关闭
		setTimeout(function(){
			diag.show();
		},1000);
		setTimeout(function(){
			window.close();
		},5000);
	}

}
