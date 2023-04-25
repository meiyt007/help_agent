// 识别模板服务端json串
var java_template_json_str;
// 区块服务端json串
var java_block_list_json_str;

// 业务操作模块
var business = {
    // 数据初始化
    init: function () {
        // 禁用批量操作按钮
        core.disabledBatchBtns();
        // 禁用历史操作按钮
        core.disabledHisBtns();
        // 批量添加按钮单击事件
        core.batchAddEvent();
        // templateVersion从jsp页面获取
        if (fromJsp.templateVersion != '') {

            $('#uploadBtn').addClass('disabled');
            $('#uploadMoreBtn').addClass('disabled');
            $('#clearBtn').addClass('disabled');
            $('#saveBtn').addClass('disabled');
            $('#submitBtn').addClass('disabled');
            // $('#switchCataBtn').addClass('disabled');
            $('#previewBtn').click(function () {
                business.previewTemplate();
            });
            $('#printSetBtn').click(function () {
                business.printSet();
            });
            $('#autoAlignBtn').hide();
            $('#helpBtn').hide();
        } else {
            $('#clearBtn').click(function () {
                if (!core.fileOid) {
                    util.alert('请上传模板底图！');
                    return;
                }
                business.clearAll();
            });
            $('#saveBtn').click(function () {
                if (!core.fileOid) {
                    util.alert('请上传模板底图！');
                    return;
                }
                business.saveTemplate();
            });
            $('#previewBtn').click(function () {
                if (!core.fileOid) {
                    util.alert('请上传模板底图！');
                    return;
                }
               business.previewTemplate();
                 /*business.saveTemplate(function () {
                    setTimeout(business.previewTemplate, 1000);
                });*/
            });
            $('#submitBtn').click(function () {
                if (!core.fileOid) {
                    util.alert('请上传模板底图！');
                    return;
                }
                var vali = core.valiteBlocks();
                if (!vali) {
                    return;
                }
                business.publishTemplate();
            });
            $('#uploadBtn').click(function () {
                core.cleanAllCovering();
                business.uploadImg();
            });
            $('#autoAlignBtn').click(function () {
                if (!core.fileOid) {
                    util.alert('请上传模板底图！');
                    return;
                }
                core.cleanAllCovering();
                if (window.autoAlign){
                    window.autoAlign = false;
                    $(this).removeClass('active');
                    util.warningTip('已关闭智能对齐');
                } else {
                    window.autoAlign = true;
                    $(this).addClass('active');
                    util.warningTip('已开启智能对齐');
                }
            });
            $('#printSetBtn').click(function () {
                business.printSet();
            });
            $('#helpBtn').mouseover(function () {
                core.cleanAllCovering();
                business.showHelp($(this));
            }).mouseout(function(){
                business.hideHelp($(this));
            });

        }

        ztree_helper.addNodes(fromJsp.metaDataJsonArrayStr);


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
    // 删除的java block oid
    del_java_block_oid_array: [],
    // 模板选择divid
    div_id_v: null,
    // 目录选择divid
    div_id_c: null,
    // 引导类型：1-切换目录，2-当前模板配置完成提示继续下一个模板配置
    // 切换目录 按钮 已屏蔽
    guide_type: 1,
    // 单击清空按钮
    clearAll: function () {
        util.confirm('确定要清空所有区块吗？', function () {
            core.clearAllBlock(function () {
                util.sucTip('成功清空所有区块！');
            });
        });
    },
    reloadOpener: function () {
        if (window.opener && window.opener.reloadPage) {
            window.opener.reloadPage();
        }
    },
    // 暂存
    saveTemplate: function (fun) {
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
     // console.log("blockArrayStr=*"+blockArrayStr+"%%%")
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
  //预览
    previewTemplate: function () {
      var jo = JSON.parse(java_template_json_str);
      var ctxPath= fromJsp.ctxPath;
      var ctxPath1= fromJsp.ctxPath1;
      var diag = new Dialog();
      diag.Width = 800;
      diag.Height = 560;
      diag.Title = "请填写照面元素信息";
      diag.URL = 'initPreviewPrintTemplate.html?faModelTemplateOid='+jo.faModelTemplateOid+'&materialCatalogOid='+jo.materialCatalogOid
        +'&ctxPath='+ctxPath+'&ctxPath1='+ctxPath1;
        diag.diagClose = function(fileOid,filePath){
        diag.close();
        /*core.resetPanelUpdateBlockArray();*/
      };
      diag.show();
     /* fun && fun.call(diag);*/
       /* var jo = JSON.parse(java_template_json_str);
        var diag = new Dialog();
        diag.Width = 700;
        diag.Height = 450;
        diag.Title = "请填写照面元素信息";
        diag.URL = fromJsp.ctxPath + "assistant/printTemplate/initPreviewPrintTemplate.do?licenseDirOid=" + fromJsp.licenseDirOid + '&oid=' + jo.oid;
        diag.ChangeWinSize = false;
        diag.show();*/
    },
    //打印设置
    printSet: function (fun) {
      var jo = JSON.parse(java_template_json_str);
      var ctxPath= fromJsp.ctxPath;
      var ctxPath1= fromJsp.ctxPath1;
      var diag = new Dialog();
      diag.Width = 800;
      diag.Height = 350;
      diag.Title = "打印设置";
      diag.URL = 'initPrintSetting.html?faModelTemplateOid='+jo.faModelTemplateOid+'&materialCatalogOid='+jo.materialCatalogOid  +'&ctxPath='+ctxPath+'&ctxPath1='+ctxPath1;
      diag.diagClose = function(fileOid,filePath){
      diag.close();
      //core.resetPanelUpdateBlockArray();
      };
      diag.show();
      fun && fun.call(diag);
    },
    // 发布，生成版本信息
    publishTemplate: function () {

        /*util.confirm('确定发布当前配置吗？', function () {*/
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
        // console.log("blockArrayStr=*"+blockArrayStr+"%%%")
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
               /* if(fun)fun();*/
                //保存出错
              }else{
                util.closeLoading();
                util.alert('保存信息出错！');
              }
            },'json');

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
    showChooseTemplate: function (versionTemplateList) {
        var diag = new Dialog();
        diag.Width = 600;
        diag.Height = 450;
        diag.Title = "选择已有模板";
        diag.InnerHtml = this.joinHtml_1(versionTemplateList);
        diag.ChangeWinSize = false;
        diag.ShowCloseButton = false;
        diag.show();
        // 美化滚动条
        $("#" + this.div_id_v).niceScroll({
            autohidemode: false,
            cursorwidth: "8px",
            cursorborderradius: "0px",
            cursorcolor: "#9E9E9E",
            background: "#E2E2E2",
            cursorborder: "0px"
        });
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
    unUseTemplate: function () {
        // 隐藏自定义滚动条
        $("#" + this.div_id_v).getNiceScroll().hide();
        Dialog.close();
        business.uploadImg();
    },
    useTemplate: function (templateOid) {
        util.showLoading();
        // 隐藏自定义滚动条
        $("#" + this.div_id_v).getNiceScroll().hide();
        Dialog.close();
        $.post(fromJsp.ctxPath + "assistant/printTemplate/cloneTemplate.do?templateOid=" + templateOid + "&random=" + Math.random(),
            null,
            function (data, textStatus, jqXHR) {
                if (data.success) {
                    // 刷新父页面
                    business.reloadOpener();
                    location.reload();
                } else {
                    util.closeLoading();
                }
            }, 'json');
    },
    pulishSuccess: function () {
        var diag = new Dialog();
        diag.Width = 350;
        diag.Height = 350;
        diag.InnerHtml = '<div style="font-size:16px;margin:20px auto;">当前模板发布成功！5秒后将自动关闭！</div>';
        diag.ChangeWinSize = false;
        diag.Title = "模板发布成功！";
        diag.AutoClose = 5;
        diag.ShowCloseButton = true;
        diag.CancelEvent = function () {
            window.close();
        }
        // 等待loading关闭
        setTimeout(function () {
            diag.show();
        }, 1000);
        setTimeout(function () {
            window.close();
        }, 5000);
    },
    showHelp: function ($btn) {
        // https://www.sojson.com/jshtml.html
        var html = '<table class="table table-bordered" style="width: 400px;"><tbody><tr><td>1、点开左侧“底图信息”栏，可查看上传的底图实际打印尺寸（底图物理尺寸），请确保和实际的证照底图尺寸一致。</td></tr><tr><td>2、在有标尺的面板区域，鼠标左键点击并沿右下方拖拽鼠标，在确认合适的大小时松开鼠标左键，以完成一个打印区域的绘制，在绘制的区块上双击、或鼠标右键菜单选择“编辑”后，在出现的打印区域信息设置面板，可对打印区域进行相应设置。</td></tr><tr><td>3、按住键盘Ctrl键，可批量选择已绘制的打印区块，当两个及以上的区块被选定时，顶部批量操作按钮将被激活，此时可以对已选定的区块进行相应对其操作。</td></tr><tr><td>4、按住键盘空格键，点击并拖动已绘制的区块，可复制当前区块。</td></tr><tr><td>5、键盘方向键可对已选定的一个或多个区块按上下左右四个方向每次以1个像素值进行位置微调。</td></tr><tr><td>6、使用键盘delete键，可删除已选定的一个或多个打印区域块。</td></tr></tbody></table>';
        $btn.popover({title: '在线帮助', content: html, html: true, placement: 'bottom'});
        $btn.popover('show');
    },
    hideHelp: function ($btn) {
        $btn.popover('destroy');
    }

}
