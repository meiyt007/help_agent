/** 证照预览、打印，依赖jquery*/
(function (win) {
  var LIC_PRINT, _this, _BASE_IMG, _jq_prewiew_outer_sel, _jq_prewiew_sel, _$loadingDiv;

  _this = LIC_PRINT = {};

  _this.BASE_IMG = _BASE_IMG = {};

  win.LIC_PRINT = _this;

/*  if (!win.fromJsp || !fromJsp.fileOid || !fromJsp.templateStr
    || !fromJsp.blockArrayStr || !fromJsp.licenseMetaInfoStr) {
    console.error('缺少必要参数！');
    return false;
  }*/

  // 预览
  _this.previewLic = function ($sel) {
    _jq_prewiew_outer_sel = $sel;
    _jq_prewiew_outer_sel.append('<div id="innerDiv" style="position:relative;-webkit-transform-origin:top left;overflow:hidden;"></div>');
    _jq_prewiew_sel = $('#innerDiv');
    showLoading();
    loadBaseImage(function () {
      loadBlockArray();
    });
  }

  // 打印
  _this.printLic = function () {
    if (_jq_prewiew_sel) {
      util.showLoading();
      if (window.PrintUtil) {
        PrintUtil.PrintHtml(_jq_prewiew_sel.prop('outerHTML'));
      } else {
        browserPrint();
        return false;
      }
    }
  }

  //计算远程图片实际宽度、高度、dpi
  //计算远程图片实际宽度、高度、dpi
  var imgReady = function(ready, error) {
    $.ajaxSettings.async = false;
    $.get(filePlatAddr + "/industryCaseLicenseManager/getLicenseAttaByOid?attaOid="+fromJsp.fileOid,
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
  var loadBaseImage = function (fun) {
    imgReady(function () {
      _BASE_IMG.real_width = this.width;
      _BASE_IMG.real_height = this.height;

      _BASE_IMG.real_dpi_width = this.dpi_w;
      _BASE_IMG.real_dpi_height = this.dpi_w;

      _BASE_IMG.real_pt_width = baseImgPxConvertPt(_BASE_IMG.real_width);
      _BASE_IMG.real_pt_height = baseImgPxConvertPt(_BASE_IMG.real_height);

      var physics_w = (_BASE_IMG.real_width / _BASE_IMG.real_dpi_width) * 25.4;
      var physics_h = (_BASE_IMG.real_height / _BASE_IMG.real_dpi_height) * 25.4;
      _BASE_IMG.physics_w = parseInt(physics_w);
      _BASE_IMG.physics_h = parseInt(physics_h);

      var max_panel_w = _jq_prewiew_outer_sel.width();
      var max_panel_pt_w = screenPxConvertPt(max_panel_w);

      if (_BASE_IMG.real_pt_width > max_panel_pt_w) {
        _BASE_IMG.preview_pt_width = max_panel_pt_w;
        _BASE_IMG.preview_pt_height = Math.round((_BASE_IMG.preview_pt_width / _BASE_IMG.real_pt_width) * _BASE_IMG.real_pt_height);
      } else {
        _BASE_IMG.preview_pt_width = _BASE_IMG.real_pt_width;
        _BASE_IMG.preview_pt_height = _BASE_IMG.real_pt_height;
      }

      //压缩比
      var rate_ = _BASE_IMG.preview_pt_width / _BASE_IMG.real_pt_width;
      _BASE_IMG.rate = rate_;
      _jq_prewiew_sel.width(_BASE_IMG.real_pt_width + 'pt');
      _jq_prewiew_sel.height(_BASE_IMG.real_pt_height + 'pt');
      _jq_prewiew_sel.css({'-webkit-transform': 'scale(' + rate_ + ')'});
      //--

      //var img_path = fromJsp.filePlatAddr + 'atta/previewFile.do?attaOid=' + fromJsp.fileOid;
      var img_path = fromJsp.imgUrlinfo;
      var $backgroundImg = $('<img id="baseImage" src="' + img_path + '" />');
      $backgroundImg.css({'width': _BASE_IMG.real_pt_width + 'pt', 'height': _BASE_IMG.real_pt_height + 'pt'});
      $backgroundImg.css({"position": "absolute", "top": "0", "left": "0"});
      _jq_prewiew_sel.append($backgroundImg);

      _jq_prewiew_sel.css({'margin-bottom': _BASE_IMG.real_pt_height * (rate_ - 1) + 'pt'});

      var img = new Image();
      img.src = img_path;
      //console.log("img_path="+img_path)
      $(img).load(function () {
        fun();
        closeLoading();
      });

    }, function () {
      closeLoading();
    });

  }

  //加载block_array
  var loadBlockArray = function () {
    console.log("照面元素信息装载")
    var a = JSON.parse(fromJsp.blockArrayStr);
    var template = JSON.parse(fromJsp.templateStr);
    // 照面值信息
    var license_meta_val_json_array = fromJsp.licenseMetaInfoStr;
    //console.log("&&&_metaDataJsonArrayStr="+JSON.stringify(fromJsp.licenseMetaInfoStr))
    var old_baseImgStdWidth = template.baseImgStdWidth;
    //var cur_baseImgStdWidth = _BASE_IMG.preview_pt_width;
    var cur_baseImgStdWidth = (_BASE_IMG.real_pt_width / 72) * 96;
    //底图加载失败
    if (!cur_baseImgStdWidth) {
      console.log("预览打印底图加载失败！")
      return;
    }
    for (var i = 0; i < a.length; i++) {
      var java_block = a[i];
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
      //console.log("***license_meta_val_json_array="+JSON.stringify(license_meta_val_json_array))
      //console.log("***java_block="+JSON.stringify(java_block))
      for (var j = 0; j < fromJsp.metaDataJsonArrayStr.length; j++) {
        var meta_info = fromJsp.metaDataJsonArrayStr[j];
        if (meta_info.elementOid == java_block.materialCatalogElementOid) {
          block.columnType = meta_info.elementType;
          break;
        }
      }
      drowBlock(block);

    }
  }

  //绘制区块
  var drowBlock = function (block) {
    var block_id = block.block_html_id;
    var text = block.text;
    if(elementValues != ''&& elementValues != null){
      for(var i = 0;i < elementValues.length;i++){
        var elements = elementValues[i];
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
    //console.log("***text="+text)
    var $block = $("<div id='" + block_id + "'>" + text + "</div>");
    $block.css(block.print_css_json_obj);
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

  var showLoading = function () {
    var max_w = _jq_prewiew_sel.width();
    _$loadingDiv = $('<div style="position: absolute;top: 20px;left: ' + (max_w / 2 - 5) + 'px;font-size: 30pt;">加载中，请稍后...</div>');
    _jq_prewiew_sel.append(_$loadingDiv);
  }

  var closeLoading = function () {
    _$loadingDiv.remove();
  }

  var browserPrint = function () {
    var new_iframe = document.createElement('IFRAME');
    var doc = null;
    new_iframe.setAttribute('style', 'width:0px;height:0px;position:absolute;left:-2000px;top:-2000px;');
    new_iframe.setAttribute('align', 'center');
    document.body.appendChild(new_iframe);
    doc = new_iframe.contentWindow.document;
    var $clone = _jq_prewiew_sel.clone();
    $clone.css({'-webkit-transform': 'scale(1)'});
    doc.write($clone.prop('innerHTML'));
    doc.close();
    new_iframe.contentWindow.focus();
    var $imgs = $(doc).find("img");
    var imgLen = $imgs.length;
    var idx = 0;
    $imgs.load(function () {
      if (++idx == imgLen) {
        util.closeLoading();
        new_iframe.contentWindow.print();
        new_iframe.contentWindow.onafterprint = function () {
          document.body.removeChild(new_iframe);
        }
      }
    });
  }

  var util = {};

  //loading
  var d = null;

  var isShow = false;
  //展示loading
  util.showLoading = function () {
    if (!d) {
      d = dialog({
        width: 60,
        height: 60
      });
    }
    if (!isShow) {
      d.showModal();
      isShow = true;
    }
  }

  //隐藏loading
  util.closeLoading = function (fun) {
    setTimeout(function () {
      d.close();
      isShow = false;
      if (fun) fun();
    }, 1000);
  }
  // 底图px转pt
  function baseImgPxConvertPt(pxValue) {
    return pxValue * 72 / _BASE_IMG.real_dpi_width;
  }
  // px转pt
  function screenPxConvertPt(pxValue) {
    return pxValue * 72 / 96;
  }

})(window);
