/** 证照预览、打印，依赖jquery*/
/**  遮罩依赖 loadCanvas.js*/

(function(win) {
  var LIC_PRINT, _this, _BASE_IMG, _jq_prewiew_outer_sel, _jq_prewiew_sel,fromJsp={};
  var _ctxPath, _filePlatAddr, templateInf,_resultOid,_faModelTemplateOid ,_elementValues,_metaDataJsonArrayStr;

  _this = LIC_PRINT = {};

  templateInf = {};

  _this.BASE_IMG = _BASE_IMG = {};

  win.LIC_PRINT = _this;

  // 配置综窗接口服务地址
  _this.setCtxPath = function(ctxPath) {
    _ctxPath = ctxPath;
  };

  // 配置文件接口服务地址
  _this.setFilePlatAddr = function(filePlatAddr) {
    _filePlatAddr = filePlatAddr;
  };

  // 设置证照id
  _this.setResultOid = function(resultOid) {
    _resultOid = resultOid;
  };

  // 设置模板id
  _this.setFaModelTemplateOid = function(faModelTemplateOid) {
    _faModelTemplateOid= faModelTemplateOid;
  };
  // 设置证照要素填写信息
  _this.setElementValues = function(elementValues) {
    _elementValues= elementValues;
  };

  // 设置证照要素信息
  _this.setMetaDataJsonArrayStr = function(metaDataJsonArrayStr) {
    _metaDataJsonArrayStr = metaDataJsonArrayStr;
  };

  //初始化数据
  function getinit(){
    var url = _ctxPath+"/faModelTemplate/initFaModelTemplateByResultOid";
    var data = {
      "faModelTemplateOid": _faModelTemplateOid,
      "resultOid": _resultOid,
    }
    fromJsp.licenseElementValueLlist=_elementValues;
    $.ajaxSettings.async = false;
    $.get( url, data, function(data, textStatus, jqXHR){
      fromJsp.blockArrayStr=JSON.stringify(data.data.blockArrayStr);
      fromJsp.versionTemplateList=JSON.stringify(data.data.versionTemplateList);
      fromJsp.cataOid = _resultOid;
      fromJsp.materialCatalogOid = _resultOid;
      fromJsp.tempOid = data.data.tempOid;
      fromJsp.templateStr=JSON.stringify(data.data.templateStr);
      fromJsp.blockColorClass = data.data.blockColorClass;
      fromJsp.fileOid= data.data.baseImgFileOid;
    },'json');
  }

  // 预览 $sel 为 jQuery对象
  _this.previewLic = function($sel, fun) {
    if (
      !(
        _ctxPath &&
        _filePlatAddr &&
        _resultOid
      )
    ) {
      console.error("缺少必要参数！");
      fun();
      return false;
    } else {
      if (!$sel || $sel.length == 0) {
        console.error("缺少必要参数，或参数错误！");
        return false;
      }
      _jq_prewiew_outer_sel = $sel;
      let dom = document.createElement("div");
      dom.style =
        "position:relative;-webkit-transform-origin:top left;overflow:hidden;";
      dom.id = "innerDiv";
      _jq_prewiew_outer_sel.append(dom);
      _jq_prewiew_sel = $("#innerDiv");
      getinit();
    }
    loadBaseImage(function() {
      loadBlockArray();
      fun();
      // closeLoading();
    });
  };

  // 打印
  // fun 回调函数
  // printImg 是否打印底图
  // 副柜A3打印机名称：'Kyocera ECOSYS P4040dn KX (A3)'
  // 测试打印机 Microsoft Print to PDF
  _this.printLic = function(fun, printImg) {
    if (_jq_prewiew_sel) {
      if (window.PrintUtil) {
        var html =
          '<!DOCTYPE html><html><head><meta charset="UTF-8"></head><body>';
        // 打印底图
        if (printImg === true) {
          html += _jq_prewiew_sel.prop("innerHTML");
        } else {
          var $clone = _jq_prewiew_sel.clone();
          $clone.find("#baseImage").remove();
          html += $clone.prop("innerHTML");
        }
        html += "</body></html>";
        var template = templateInf.templateStr;
        var printSettingStr = template.printSetting;
        var printSetting = JSON.parse(printSettingStr.replace(/'/g, '"'));
        if (!printSettingStr) {
          alert("检查到尚未配置打印设置");
        }
        var param = {};
        param.html = html;
        param.pageSize = printSetting.pageSize;
        param.orientation = printSetting.orientation;
        param.printerName = printSetting.printerName;
        param.callbackFun = fun;
        EP_PRINT_HTML.printHtmlPlus(param);
      } else {
        browserPrint(fun, printImg);
        return false;
      }
    }
  };

  //计算远程图片实际宽度、高度、dpi
  var imgReady = function(ready, error) {
    $.ajaxSettings.async = false;
    $.get(_filePlatAddr + "/industryCaseLicenseManager/getLicenseAttaByOid?attaOid="+fromJsp.fileOid,
      function(data, textStatus, jqXHR) {
        var img = {};
        //保存出错
        if (data.code != 200) {
          let dom = document.querySelector(".loading");
          if (dom) {
            dom.parentNode.removeChild(dom);
          }
          error && error.call(data.message);
        } else {
          var result = data.data;
          fromJsp.imgUrlinfo=data.data.fastdfsNginxUrl;
          img.width = result.width;
          img.height = result.height;
          img.dpi_w = result.dpi_w;
          img.dpi_h = result.dpi_h;
          ready && ready.call(img);
        }
      },
      "json"
    );
  };

  var loadBaseImage = function(fun) {
    imgReady(
      function() {
        _BASE_IMG.real_width = this.width;
        _BASE_IMG.real_height = this.height;

        _BASE_IMG.real_dpi_width = this.dpi_w;
        _BASE_IMG.real_dpi_height = this.dpi_h;

        _BASE_IMG.real_pt_width = baseImgPxConvertPt(_BASE_IMG.real_width);
        _BASE_IMG.real_pt_height = baseImgPxConvertPt(_BASE_IMG.real_height);

        var physics_w =
          (_BASE_IMG.real_width / _BASE_IMG.real_dpi_width) * 25.4;
        var physics_h =
          (_BASE_IMG.real_height / _BASE_IMG.real_dpi_height) * 25.4;
        _BASE_IMG.physics_w = parseInt(physics_w);
        _BASE_IMG.physics_h = parseInt(physics_h);
        var max_panel_w = _jq_prewiew_outer_sel.style.width;
        var max_panel_pt_w = screenPxConvertPt(max_panel_w.replace("px", ""));

        if (_BASE_IMG.real_pt_width > max_panel_pt_w) {
          _BASE_IMG.preview_pt_width = max_panel_pt_w;
          _BASE_IMG.preview_pt_height = Math.round(
            (_BASE_IMG.preview_pt_width / _BASE_IMG.real_pt_width) *
              _BASE_IMG.real_pt_height
          );
        } else {
          _BASE_IMG.preview_pt_width = _BASE_IMG.real_pt_width;
          _BASE_IMG.preview_pt_height = _BASE_IMG.real_pt_height;
        }

        //压缩比
        var rate_ = _BASE_IMG.preview_pt_width / _BASE_IMG.real_pt_width;
        _BASE_IMG.rate = rate_;
        //--

        //面板窗口尺寸
        _jq_prewiew_sel.width(_BASE_IMG.real_pt_width + "pt");
        _jq_prewiew_sel.height(_BASE_IMG.real_pt_height + "pt");
        _jq_prewiew_sel.css({ "-webkit-transform": "scale(" + rate_ + ")" });

        var img_path = fromJsp.imgUrlinfo;
        var $backgroundImg = $('<img id="baseImage" src="' + img_path + '" />');
        $backgroundImg.css({
          width: _BASE_IMG.real_pt_width + "pt",
          height: _BASE_IMG.real_pt_height + "pt"
        });
        $backgroundImg.css({ position: "absolute", top: "0", left: "0" });
        _jq_prewiew_sel.append($backgroundImg);

        _jq_prewiew_sel.css({
          "margin-bottom": _BASE_IMG.real_pt_height * (rate_ - 1) + "pt"
        });

        var imgSrc = new Image();
        imgSrc.src = img_path;

        $(imgSrc).on("load", function() {
          fun();
        });
      },
      function() {
        // closeLoading();
      }
    );
  };

  //加载block_array
  var loadBlockArray = function() {
    var a =  JSON.parse(fromJsp.blockArrayStr);
    var template =  JSON.parse(fromJsp.templateStr);
    var license_meta_val_json_array = _metaDataJsonArrayStr;
    //console.log("&&&_metaDataJsonArrayStr="+JSON.stringify(_metaDataJsonArrayStr))
    var old_baseImgStdWidth = template.baseImgStdWidth;
    var cur_baseImgStdWidth = (_BASE_IMG.real_pt_width / 72) * 96;
    //底图加载失败
    if (!cur_baseImgStdWidth) {
      return;
    }
    for (var i = 0; i < a.length; i++) {
      var java_block = a[i];
      //console.log("&&&java_block="+JSON.stringify(java_block))
      var block = {};
      block.block_oid = java_block.faModelTemplateBlockOid;
      block.block_code = java_block.blockCode;
      block.block_name = java_block.blockName;
      block.block_type = java_block.recognitionType;
      block.materialCatalogElementOid = java_block.materialCatalogElementOid;

      //等比例展示
      if (old_baseImgStdWidth != cur_baseImgStdWidth) {
        var rate = cur_baseImgStdWidth / old_baseImgStdWidth;
        block.x = java_block.x * rate;
        block.y = java_block.y * rate;
        block.width = java_block.width * rate;
        block.height = java_block.height * rate;
      } else {
        block.x = java_block.x;
        block.y = java_block.y;
        block.width = java_block.width;
        block.height = java_block.height;
      }
      block.sort = java_block.sort;
      block.print_css_json_obj = java_block.marqueeJsonValue;
      var block_id = "block_id_" + java_block.sort;
      block.block_html_id = block_id;

      for (var j = 0; j < license_meta_val_json_array.length; j++) {
        var meta_info = license_meta_val_json_array[j];
        if (meta_info.elementOid == java_block.materialCatalogElementOid) {
          block.columnType = meta_info.elementType;
          break;
        }
      }




      drowBlock(block);


    }
  };

  //绘制区块
  var drowBlock = function (block) {
    //console.log("&&&blocknew="+JSON.stringify(block))
    var block_id = block.block_html_id;
    var text = block.text;
    if(_elementValues != ''&& _elementValues != null){
      for(var i = 0;i < _elementValues.length;i++){
        var elements = _elementValues[i];
        //console.log("&&&elements="+JSON.stringify(elements))
        if(elements.elementOid ==block.materialCatalogElementOid){
          if(elements.elementValue != undefined){
            text= elements.elementValue;
          }
        }
      }
    }
    if (block.columnType == 'imgFile') {
      text = '<img style="display: none;" src="' + text + '"/>';
    }
    if (!text) {
      text = "";
    }
    var $block = $("<div id='" + block_id + "'>" + text + "</div>");
    $block.css(JSON.parse(block.print_css_json_obj));
    _jq_prewiew_sel.append($block);
    var _width = screenPxConvertPt(block.width);
    var _height = screenPxConvertPt(block.height);
    $('#' + block_id).css({
      "position": "absolute",
      "top": screenPxConvertPt(block.y) + 'pt',
      "left": screenPxConvertPt(block.x) + 'pt',
      width: _width + 'pt',
      height: _height + 'pt'
    });

    // 修正图片大小
    if (block.columnType == 'imgFile') {
      var $img = $('#' + block_id).find('img');
      var textAlign = $block.css("text-align");
      var margin = "0 auto";
      if (textAlign == 'left') {
        margin = "0 auto auto 0";
      } else if (textAlign == 'right') {
        margin = " 0 0 auto auto";
      }
      $img.css({"display": "block", "max-width": _width + 'pt', "max-height": _height + 'pt', "margin": margin});
    }
  }
  var drowBlockold = function(block) {
    console.log("&&&blocknew="+JSON.stringify(block))
    var block_id = block.block_html_id;
    var innerHtml = "";
    if(_elementValues != ''&& _elementValues != null){
      for(var i = 0;i < _elementValues.length;i++){
        var elements = _elementValues[i];
        console.log("&&&elements="+JSON.stringify(elements))
        if(elements.elementOid ==block.materialCatalogElementOid){
          if(elements.elementValue != undefined){
            innerHtml=elements.elementValue;
          }
        }
      }
    }
    var $block = $("<div id='" + block_id + "'>" + innerHtml + "</div>");
    $block.css({ color: "#000", lineHeight: "1.3em",top:"10em" });
    _jq_prewiew_sel.append($block);
    var _width = screenPxConvertPt(block.width);
    var _height = screenPxConvertPt(block.height);
    $("#" + block_id).css({
      position: "absolute",
      top: screenPxConvertPt(block.y)+4 + "pt",
      left: screenPxConvertPt(block.x) + "pt",
      width: screenPxConvertPt(block.width) + "pt",
      height: screenPxConvertPt(block.height) + "pt"
    });
    if (block.block_type && block.block_type == "imgFile") {
      var $img = $("#" + block_id).find("img");
      var textAlign = $block.css("text-align");
      var margin = "0 auto";
      if (textAlign == "left") {
        margin = "0 auto auto 0";
      } else if (textAlign == "right") {
        margin = " 0 0 auto auto";
      }
      $img.css({
        display: "block",
        "max-width": _width + "pt",
        "max-height": _height + "pt",
        margin: margin
      });
    }
  };

  var showLoading = function() {
    /** 遮罩打开 依赖loadCanvas.js*/
    Dialog.openShade("证照加载中，请稍候...");
    /** div提示打开*/
    /* var max_w = _jq_prewiew_sel.width();
        _$loadingDiv = $('<div style="position: absolute;top: 20px;left: ' + (max_w / 2 - 15) + 'px;z-index: 10000;color:#000;font-size: 30px;">加载中，请稍后...</div>');
        _jq_prewiew_sel.append(_$loadingDiv);*/
  };

  var closeLoading = function() {
    /** 遮罩关闭 依赖loadCanvas.js*/
    Dialog.closeShade();
    /** div提示关闭*/
    /* _$loadingDiv.remove();*/
  };

  var browserPrint = function(fun, printImg) {
    var new_iframe = document.createElement("IFRAME");
    var doc = null;
    new_iframe.setAttribute(
      "style",
      "width:0px;height:0px;position:absolute;left:-2000px;top:-2000px;"
    );
    new_iframe.setAttribute("align", "center");
    document.body.appendChild(new_iframe);
    doc = new_iframe.contentWindow.document;
    var $clone = _jq_prewiew_sel.clone();
    // 打印底图
    if (printImg === true) {
      // 套打
    } else {
      $clone.find("#baseImage").remove();
    }
    doc.write($clone.prop("innerHTML"));
    doc.close();
    new_iframe.contentWindow.focus();
    if (printImg === true) {
      var $imgs = $(doc).find("img");
      var imgLen = $imgs.length;
      var idx = 0;
      $imgs.load(function() {
        if (++idx == imgLen) {
          new_iframe.contentWindow.print();
          new_iframe.contentWindow.onafterprint = function() {
            document.body.removeChild(new_iframe);
            if (fun) {
              fun();
            }
          };
        }
      });
    } else {
      new_iframe.contentWindow.print();
      new_iframe.contentWindow.onafterprint = function() {
        document.body.removeChild(new_iframe);
        if (fun) {
          fun();
        }
      };
    }
  };

  // 底图px转pt
  function baseImgPxConvertPt(pxValue) {
    return (pxValue * 72) / _BASE_IMG.real_dpi_width;
  }

  // px转pt
  function screenPxConvertPt(pxValue) {
    return (pxValue * 72) / 96;
  }
})(window);
