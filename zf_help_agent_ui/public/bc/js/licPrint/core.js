// 核心模块
var core = {
    fileOid: '',
    filePath: '',
    sizeRate: '',
    baseImgWidth: null,
    baseImgHeight: null,
    baseImgDpiWidth: null,
    baseImgDpiHeight: null,
    baseImgStdWidth: null,
    baseImgStdHeight: null,
    blockColorClass: '',
    readOnly: false
};
// 面板对象
var $panel;
// 面板外层滚动区域,主要用于定位区块
var $scroll_div;
// 底板图片真实宽度
var bgImg_real_width;
// 底板图片真实高度
var bgImg_real_height;
// 底板图片宽度dpi
var bgImg_real_dpi_width;
// 底板图片高度dpi
var bgImg_real_dpi_height;
// 底板图片当前宽度
var bgImg_panel_width;
// 底板图片当前高度
var bgImg_panel_height;
// 区块模板对应html
var block_html_template;
// 区块自增编号
var block_id_idx = 0;
// 当前正在操作的区划id
var active_block_id;
// 当前正在移动的区块id
var moving_block_id;
// 当前正在缩放的区块id
var resize_block_id;
// 禁止绘制区块坐标
var forbid_x_1, forbid_x_2, forbid_y_1, forbid_y_2;
// block队列
var block_array = new Array();
// 标尺对象
var myRuler;
// 区块颜色
var block_color_style = 'color3';
// 编辑框高
var edit_panel_height = 300;
// 编辑框宽
var edit_panel_width = 300;
// 区块宽度检测
var check_block_size_error = false;
// 区块超出面板检测
var check_block_overflow_error = false;
// 最新区块尺寸
var min_block_px = 20;
// 已完成block复制的id
var copy_block_id;
// 是否已按space键
var pressingSpace = false;

// 是否已按Ctrl键
var pressingCtrl = false;
// 批量操作历史记录
var batchOperLastHis = [];
// 批量操作历史记录
var batchOperNextHis = [];
// 当前已批量选择的区块列表，批量操作之前更新
var batchSelBlockArray = [];

// 是否开启自动对齐
window.autoAlign = false;
// 辅助对其-当前正在移动的区块
window.moving_block = null;

// block原状态
var pre_block ;


$(function(){
  //判断是修改还是查看
  if(viewOrEdit=='view'){
    core.readOnly=true;
  }
  //获取综窗地址
  getIndustryUrl();
  getZcUrl();


});

//获取综窗地址
function getZcUrl(){
  //http访问的时候使用配置参数获取
 /* var code="ZC_ROUTE_URL" ;
  var urlImg = VUE_APP_BASE_API+'/settings/security/config/getSysConfigByCode';
  var dataimg = {
    "code": code,
  }
  $.ajax({
    url: urlImg, //请求地址
    type: "get", //请求方式
    data:dataimg,
    dataType:'json',
    success: function(data) { //成功回调
      VUE_APP_ZC_ROUTE_PATH=data.data.value;
      fromJsp.ctxPath = VUE_APP_ZC_ROUTE_PATH;
      getinit();
    },
    fail: function(data) {
      console.log('fail')
    }
  });*/
  //https访问使用代理地址
  VUE_APP_ZC_ROUTE_PATH='/zc-api';
  fromJsp.ctxPath = VUE_APP_ZC_ROUTE_PATH;
  getinit();
}
//获取一业一证地址
function getIndustryUrl(){
  //http访问的时候使用配置参数获取
  /*var code="INDUSTRY_ROUTE_URL" ;
  var urlImg = VUE_APP_BASE_API+'/settings/security/config/getSysConfigByCode';
  var dataimg = {
    "code": code,
  }
  $.ajax({
    url: urlImg, //请求地址
    type: "get", //请求方式
    data:dataimg,
    dataType:'json',
    success: function(data) { //成功回调
      VUE_APP_ZC_INDUSTRY_ROUTE_PATH=data.data.value;
      fromJsp.ctxPath1 = VUE_APP_ZC_INDUSTRY_ROUTE_PATH;

    },
    fail: function(data) {
      console.log('fail')
    }
  });*/
  //https访问使用代理地址
  VUE_APP_ZC_INDUSTRY_ROUTE_PATH='/industry-api';
  fromJsp.ctxPath1 = VUE_APP_ZC_INDUSTRY_ROUTE_PATH;
}

//初始化数据
function getinit(){
  var  faModelTemplateOid = getURLParameter("faModelTemplateOid");
  var materialCatalogOid=getURLParameter("materialCatalogOid");
   getMetaDataJsonArrayStr(materialCatalogOid);
  var url = VUE_APP_ZC_ROUTE_PATH+"/faModelTemplate/initFaModelTemplateByResultOid";

  var data = {
    "faModelTemplateOid": faModelTemplateOid,
    "resultOid": materialCatalogOid,
  }
  $.get( url, data, function(data, textStatus, jqXHR){

    // alert(JSON.stringify(data.data.RECOGNITION_TYPE))
    //alert(JSON.stringify(data.data.versionTemplateList))
    fromJsp.blockArrayStr=JSON.stringify(data.data.blockArrayStr);
    fromJsp.versionTemplateList=JSON.stringify(data.data.versionTemplateList);
    console.log("metaDataJsonArrayStr=****"+fromJsp.blockArrayStr);
    /*
    console.log("blockArrayStr=****"+fromJsp.blockArrayStr);*/
    fromJsp.cataOid = materialCatalogOid;
    fromJsp.materialCatalogOid = materialCatalogOid;
    // fromJsp.materialCataName = data.data.materialCataName;
    //fromJsp.bigCataOid = data.data.bigCataOid;
    fromJsp.tempOid = data.data.tempOid;
    if(fromJsp.tempOid!=null){
      fromJsp.templateStr=JSON.stringify(data.data.templateStr);
      fromJsp.blockColorClass = data.data.blockColorClass;
      fromJsp.version = data.data.version;
      if(viewOrEdit=='view'){
        fromJsp.templateVersion= data.data.version;
      }
      fromJsp.fileOid= data.data.baseImgFileOid;
      fromJsp.baseImgFileOid = data.data.baseImgFileOid;
      getFilePath(fromJsp.baseImgFileOid);//获取地址
    }else{
      setTimeout("initialize()","1000");
     // initialize();
    }
    //console.log(fromJsp.blockArrayStr);
  },'json');
}

/**
 * 获取要素
 * @param materialCatalogOid
 */
function getMetaDataJsonArrayStr(materialCatalogOid){
  var urlIndustry = VUE_APP_ZC_INDUSTRY_ROUTE_PATH+"/combo/industryElement/queryIndustryElementByResultOid/"+materialCatalogOid;
  $.get( urlIndustry,  function(data){
    console.log("返回信息----------"+JSON.stringify(data.data.metaDataJsonArrayStr));
    fromJsp.metaDataJsonArrayStr=JSON.stringify(data.data.metaDataJsonArrayStr);
  },'json');
}

//获取底图
function  getFilePath(baseImgFileOid){
  var urlImg = VUE_APP_BASE_API+'/platform/security/atta/nologin/getByAttaOid';
  var dataimg = {
    "attaOid": baseImgFileOid,
  }
  $.ajax({
    url: urlImg, //请求地址
    type: "get", //请求方式
    data:dataimg,
    dataType:'json',
    success: function(data) { //成功回调
      var imgUrlinfo=data.data.fastdfsNginxUrl;
      //core.filePath=imgUrlinfo;
      fromJsp.baseImgFilePath = imgUrlinfo;
      initialize();
    },
    fail: function(data) {
      console.log('fail')
    }
  });
}


function  initialize(){
    // 参数初始化（初始化一次即可）
    $panel = $('#panel');
    block_html_template = $('#block_html_template').html();
    // 增加自动对齐支持
    block_html_template = $(block_html_template).addClass('draggable').prop('outerHTML');

    // 加载右键菜单
    context.init({preventDoubleContext: false});




    // 美化滚动条
    // http:// blog.csdn.net/mss359681091/article/details/52838179
    $(".sidebar-nav").niceScroll({
        autohidemode: false, cursorwidth: "8px", cursorborderradius: "0px",
        cursorcolor: "#9E9E9E", background: "#E2E2E2", cursorborder: "0px",
        railpadding: {top: 0, right: 2, left: 0, bottom: 0}
    });

    // 给切换区块颜色按钮增加点击事件
    $('.menu1 a').click(function () {
        switchColor($(this));
    });

    // 给元素选择按钮添加单击事件
    $(document).on('click', "button[id$='_sel_md']", function () {
        var block_id = this.id.replace('_sel_md', '');
        core.showMetaDataPanel(block_id);
        return false;
    });

    // 给关闭按钮添加单击事件
    $(document).on('click', ".close-panel", function () {
        $(this).parent().addClass('hide');
        var block_id = $(this).parent().attr('id').replace('_sel_panel', '');
        $('#' + block_id + '_md_list').getNiceScroll().hide();
        return false;
    });

    // 给目录元素添加单击事件
    $(document).on('click', ".md-li", function () {
        var $sel_panel = $(this).parent().parent();
        $sel_panel.addClass('hide');
        var block_id = $sel_panel.attr('id').replace('_sel_panel', '');
        $('#' + block_id + '_md_list').getNiceScroll().hide();
        core.doSelMetaData($(this).attr('oid'), block_id);
        return false;
    });

    // 给 字体选择 添加单击事件
    $(document).on('change', "[s_k='font-family']", function () {
        var $this = $(this);
        var block_id = $this.parents("[b_id]").attr('b_id');
        var b = block_helper.get_block(block_array, block_id);
        b.temp_print_css_json_obj[$this.attr('s_k')] = $this.val();
        return false;
    });
    // 给 字号选择 添加单击事件
    $(document).on('change', "[s_k='font-size']", function () {
        var $this = $(this);
        var block_id = $this.parents("[b_id]").attr('b_id');
        var b = block_helper.get_block(block_array, block_id);
        b.temp_print_css_json_obj[$this.attr('s_k')] = $this.val();
        return false;
    });
    // 给 加粗、斜体 添加单击事件
    $(document).on('click', ".css-btn-group1 button", function () {
        var $this = $(this);
        var block_id = $this.parents("[b_id]").attr('b_id');
        var b = block_helper.get_block(block_array, block_id);
        var ck = $this.hasClass('active');
        if (ck) {
            delete b.temp_print_css_json_obj[$this.attr('s_k')];
            $this.removeClass('active');
        } else {
            b.temp_print_css_json_obj[$this.attr('s_k')] = $this.attr('s_v');
            $this.addClass('active');
        }
        return false;
    });
    // 给 对齐 添加单击事件
    $(document).on('click', ".css-btn-group2 button", function () {
        var $this = $(this);
        $this.parent().find('button').removeClass("active");
        $this.addClass("active");
        var block_id = $this.parents("[b_id]").attr('b_id');
        var b = block_helper.get_block(block_array, block_id);
        b.temp_print_css_json_obj[$this.attr('s_k')] = $this.attr('s_v');
        return false;
    });
    // 给 自动换行 添加单击事件
    $(document).on('change', "[s_k='overflow-wrap']", function () {
        var $this = $(this);
        var block_id = $this.parents("[b_id]").attr('b_id');
        var b = block_helper.get_block(block_array, block_id);
        var ck = $this.is(':checked');
        if (ck) {
            b.temp_print_css_json_obj[$this.attr('s_k')] = $this.attr('s_v');
        } else {
            delete b.temp_print_css_json_obj[$this.attr('s_k')];
        }
        return false;
    });
    //--

    // blockColorClass从jsp页面获取
    if (fromJsp.blockColorClass) {
        core.blockColorClass = block_color_style = fromJsp.blockColorClass;
    } else {
        // block使用默认颜色
        core.blockColorClass = block_color_style;
    }

    $('[b-class=' + block_color_style + ']').addClass('active');

    // versionTemplateList从jsp页码获取
    if(fromJsp.versionTemplateList&&fromJsp.versionTemplateList!='null'&&fromJsp.versionTemplateList!='[]'){
      ztree_helper.loaded(function(){
        business.init();
        util.closeLoading();
        business.showChooseTemplate(fromJsp.versionTemplateList);
      });

      return ;
    }
    // fileOid从jsp页面获取
    if(fromJsp.baseImgFilePath){
      core.loadBgImage(fromJsp.baseImgFilePath,fromJsp.fileOid,business.init);
    }else{
      util.closeLoading();
      ztree_helper.loaded(function(){
        business.init();
        business.uploadImg();
      });
    }

    // templateVersion从jsp页面获取
    if (fromJsp.templateVersion != '') {
        core.readOnly = true;
        // 只读单击区块查看信息
        // 给区块添加单击事件
        $(document).on('click', ".block", function () {
            var block_id = $(this).attr('id')
            core.showBlockInfo(block_id);
            return false;
        });
    }
}

util.showLoading();

// 修改区块展示颜色
function switchColor($a) {
    var b_class = $a.attr('b-class');
    if (b_class == block_color_style) {
        return;
    }
    $('.menu1 a').removeClass('active');
    $a.addClass('active');
    $('.block').removeClass(block_color_style);
    $('.block').addClass(b_class);

    block_color_style = b_class;
    core.blockColorClass = block_color_style;
}
core.loadBgImageold = function(path,fileOid,fun){
  $scroll_div = $('#scroll_div');
  util.imgReady(fileOid, function () {
    //记录底图文件信息
    core.fileOid = fileOid;
    core.filePath = path;
    //底图实际尺寸
    bgImg_real_width = this.width;
    bgImg_real_height = this.height;
    $('#real_w').text(bgImg_real_width + ' px(宽)');
    $('#real_h').text(bgImg_real_height + ' px(高)');
    core.baseImgWidth = bgImg_real_width;
    core.baseImgHeight = bgImg_real_height;
    //--

    //面板尺寸
    var max_panel_w = getMaxPanelWidth();
    //console.log("bgImg_real_width-->" + bgImg_real_width);
    //console.log("max_panel_w-->" + max_panel_w);

    if(bgImg_real_width > max_panel_w){
      bgImg_panel_width = max_panel_w;
      bgImg_panel_height = Math.round((max_panel_w/bgImg_real_width) * bgImg_real_height);
    }else{
      bgImg_panel_width = bgImg_real_width;
      bgImg_panel_height = bgImg_real_height;
    }
    $('#panel_w').text(bgImg_panel_width + ' px(宽)');
    $('#panel_h').text(bgImg_panel_height + ' px(高)');
    core.baseImgStdWidth = bgImg_panel_width;
    core.baseImgStdHeight = bgImg_panel_height;
    //--

    //压缩比
    var rate_ = bgImg_panel_width/bgImg_real_width;
    if(rate_ < 1){
      core.sizeRate = rate_.toFixed(4);
      rate_ = rate_.toFixed(2);
    }else{
      core.sizeRate = rate_;
    }
    $('#show_rate').text("1:" + rate_);
    //--

    //面板窗口尺寸
    $scroll_div.height(getMaxPanelContainerHeight());

    //面板编辑区域尺寸
    $panel.width(max_panel_w + util.scrollWidth());
    $panel.height(bgImg_panel_height);
    path=path;
    /*path="http://localhost:8899/bc/img/timg.png";*/
    // path = fromJsp.ctxPath + "ImageShowServlet?fileOid=" + fileOid;
    $panel.css({'background':'url(' + path + ')','background-repeat':'no-repeat'});
    //$panel.css({'background-size':bgImg_panel_width + 'px ' + bgImg_panel_height + 'px'});
    $panel.css({'background-size':bgImg_panel_width + 'px auto'});

    //加载标尺
    if(myRuler){
      myRuler.ruler('refresh');
    }else{
      myRuler = $('#scroll_div').ruler();
      $('#scroll_div').css({'overflow':'hidden'});
    }

    //'18' - 标尺的宽度
    //$scroll_div.width(bgImg_panel_width + 18 + ext_w);
    $scroll_div.height($scroll_div.height() + 18);

    $scroll_div = $('.stage');

    //屏蔽横向滚动条
    $('.ef-ruler').css({'overflow-x':'hidden'});
    $('.ef-ruler').css({'overflow-y':'auto'});

    //计算禁止绘制区块坐标范围(鼠标所在坐标)
    forbid_x_1 = $scroll_div.offset().left;
    forbid_x_2 = $scroll_div.offset().left + bgImg_panel_width;
    forbid_y_1 = $scroll_div.offset().top;
    forbid_y_2 = $('#scroll_div').offset().top + $('#scroll_div').height();
    //可能的后续逻辑
    if(fun)fun();
    //刷新标尺，修复标尺局部展示问题
    myRuler.ruler('refresh');
    if($('.ef-ruler').height() > $panel.height() + 18){
      $('.ef-ruler').css({'overflow-y':'hidden'});
      forbid_y_2 = $('#scroll_div').offset().top + bgImg_panel_height + 18;
    }

    var img = new Image();
    /* img.src = fromJsp.ctxPath + 'ImageShowServlet?fileOid=' + fileOid;*/
    img.src=path;
    /*  alert(img.src+"****");*/
    $(img).load(function(){
      util.closeLoading();
    });

  },function(){
    util.closeLoading();
    if(fun)fun();
    setTimeout(function(){
      util.alert('模板底图加载出错！');
    },1000);
  });

}

// 导入底图
core.loadBgImage = function (path, fileOid, fun) {

    $scroll_div = $('#scroll_div');
    util.imgReady(fileOid, function () {
        // 记录底图文件信息
        core.fileOid = fileOid;

        // 底图实际尺寸
        bgImg_real_width = this.width;
        bgImg_real_height = this.height;
        $('#real_w').text(bgImg_real_width + 'px(宽)');
        $('#real_h').text(bgImg_real_height + 'px(高)');
        core.baseImgWidth = bgImg_real_width;
        core.baseImgHeight = bgImg_real_height;
        bgImg_real_dpi_width = this.dpi_w;
        bgImg_real_dpi_height = this.dpi_w;
        $('#dpi_w').text(bgImg_real_dpi_width + '(宽)');
        $('#dpi_h').text(bgImg_real_dpi_height + '(高)');
        core.baseImgDpiWidth = bgImg_real_dpi_width;
        core.baseImgDpiHeight = bgImg_real_dpi_height;
        // --

        // 底图物理尺寸
        var physics_w = (bgImg_real_width / bgImg_real_dpi_width) * 25.4;
        var physics_h = (bgImg_real_height / bgImg_real_dpi_height) * 25.4;
        $('#physics_w').text(parseInt(physics_w) + 'mm(宽)');
        $('#physics_h').text(parseInt(physics_h) + 'mm(高)');
        // --

        // 面板尺寸
        var max_panel_w = getMaxPanelWidth();
        // console.log("bgImg_real_width-->" + bgImg_real_width);
        // console.log("max_panel_w-->" + max_panel_w);

        if (bgImg_real_width > max_panel_w) {
            bgImg_panel_width = max_panel_w;
            bgImg_panel_height = Math.round((max_panel_w / bgImg_real_width) * bgImg_real_height);
        } else {
            bgImg_panel_width = bgImg_real_width;
            bgImg_panel_height = bgImg_real_height;
        }
        $('#panel_w').text(bgImg_panel_width + 'px(宽)');
        $('#panel_h').text(bgImg_panel_height + 'px(高)');
        core.baseImgStdWidth = bgImg_panel_width;
        core.baseImgStdHeight = bgImg_panel_height;
        // --

        // 压缩比
        var rate_ = bgImg_panel_width / bgImg_real_width;
        if (rate_ < 1) {
            core.sizeRate = rate_.toFixed(4);
            rate_ = rate_.toFixed(2);
        } else {
            core.sizeRate = rate_;
        }
        $('#show_rate').text("1:" + rate_);
        // --

        // 面板窗口尺寸
        $scroll_div.height(getMaxPanelContainerHeight());

        // 面板编辑区域尺寸
        $panel.width(max_panel_w + util.scrollWidth());
        $panel.height(bgImg_panel_height);
         path=path;
        //path = fromJsp.filePlatAddr + 'atta/previewFile.do?attaOid=' + fileOid;
        $panel.css({'background': 'url(' + path + ')', 'background-repeat': 'no-repeat'});
        // $panel.css({'background-size':bgImg_panel_width + 'px ' + bgImg_panel_height + 'px'});
        $panel.css({'background-size': bgImg_panel_width + 'px auto'});
        // 加载标尺
        if (myRuler) {
            myRuler.ruler('refresh');
        } else {
            myRuler = $('#scroll_div').ruler();
            $('#scroll_div').css({'overflow': 'hidden'});
        }

        // '18' - 标尺的宽度
        // $scroll_div.width(bgImg_panel_width + 18 + ext_w);
        $scroll_div.height($scroll_div.height() + 18);

        $scroll_div = $('.stage');

        // 屏蔽横向滚动条
        $('.ef-ruler').css({'overflow-x': 'hidden'});
        $('.ef-ruler').css({'overflow-y': 'auto'});

        // 计算禁止绘制区块坐标范围(鼠标所在坐标)
        forbid_x_1 = $scroll_div.offset().left;
        forbid_x_2 = $scroll_div.offset().left + bgImg_panel_width;
        forbid_y_1 = $scroll_div.offset().top;
        forbid_y_2 = $('#scroll_div').offset().top + $('#scroll_div').height();

        // 可能的后续逻辑
        if (fun) fun();

        // 刷新标尺，修复标尺局部展示问题
        myRuler.ruler('refresh');
        if ($('.ef-ruler').height() > $panel.height() + 18) {
            $('.ef-ruler').css({'overflow-y': 'hidden'});
            forbid_y_2 = $('#scroll_div').offset().top + bgImg_panel_height + 18;
        }
        var img = new Image();
        img.src=path;
        $(img).load(function(){
          util.closeLoading();
        });

    }, function () {
        util.closeLoading();
        if (fun) fun();
        setTimeout(function () {
            util.alert('模板底图加载出错！');
        }, 1000);
    });

}

//点击打开目录元素列表面板
core.showMetaDataPanel = function(block_id){
  //列表容器
  var $md_list = $('#' + block_id + '_md_list');
  //组装元素列表
  var li_html = '';
  var metaDataJsonArray = JSON.parse(fromJsp.metaDataJsonArrayStr);
  var flag = true;
  for(var i=0;i<metaDataJsonArray.length;i++){
    var metaData = metaDataJsonArray[i];
    var oid = metaData.elementOid;
    var name = metaData.elementName;
    for(var j=0;j<block_array.length;j++){
      var b = block_array[j];
      var meta_data = b.meta_data;
      var meta_data_oid = meta_data ? meta_data.elementOid : null;
      if(meta_data_oid&&meta_data_oid == oid){
        flag = false;
        break;
      }
    }
    if(flag){
      li_html += ['<li class="md-li" oid=' + oid ,
        ' name=' + name + '>',
        metaData.elementName,
        '</li>'].join("");
    }
    flag = true;
  }

  if(li_html != ''){
    $md_list.html(li_html);
  }else{
    var tip_html = '<span class="label label-warning">&nbsp;暂无更多目录元素</span>';
    tip_html = ['<div class="alert alert-error alert-block" style="margin:2px 5px;">',
      '<strong>提示：</strong>暂无更多目录元素。',
      '</div>'].join("");
    $md_list.html(tip_html);
  }

  //展示选择框
  $('#' + block_id + '_sel_panel').removeClass('hide');
  if($md_list.getNiceScroll().length > 0){
    $md_list.getNiceScroll().show();
    $md_list.getNiceScroll().resize();
  }else{
    $('#' + block_id + '_md_list').niceScroll({
      autohidemode: false,cursorwidth: "6px",cursorborderradius: "0px",
      cursorcolor: "#9E9E9E",background: "#E2E2E2",cursorborder: "0px",
      preservenativescrolling: true,spacebarenabled: true
    });
  }
}

//绑定目录元素
core.doSelMetaData = function(metaDataOid,block_id){
  var metaDataJsonArray = JSON.parse(fromJsp.metaDataJsonArrayStr);
  for(var i=0;i<metaDataJsonArray.length;i++){
    var metaData = metaDataJsonArray[i];
    var oid = metaData.elementOid;
    var name = metaData.elementName;
    if(oid == metaDataOid){
      var b = block_helper.get_block(block_array,block_id);
      b.meta_data = metaData;
      $('#' + block_id + '_md').text(name);
      break;
    }

  }

}

// 关闭所有元素列表面板
core.hideAllMetaDataPanel = function () {
    $('.sel-panel').hide();
}

// 单击block,高亮边缘
core.activeBlock = function (block_id) {
    $('.block').removeClass('active');
    $('#' + block_id).addClass('active');
}

// 按住ctrl键盘，单击block,高亮边缘
core.activeBlockCtrl = function (block_id) {
    $('#' + block_id).addClass('active');
}

// 打开block信息编辑
core.initBlock = function (block_id) {
    //debugger;
    var b = block_helper.get_block(block_array, block_id);
    var new_block = Object.assign({}, b);
    pre_block = new_block;
    var html = $('#block_edit_html_template').html().replace(/\{block_id\}/g, block_id);
    // console.log(html);
    var $block = $('#' + block_id);

    var title = core.readOnly ? '打印区域信息' : '打印区域信息设置';

    if (core.readOnly) {
        // 已经打开了信息编辑框
        if ($('#' + block_id + '_md')[0]) {
            return;
        }
    } else {
        // $block.popover('destroy');
    }
    // 先关闭所有弹出框
    for (var i = 0; i < block_array.length; i++) {
        var b_id = block_array[i].block_html_id;
        $('#' + b_id).popover('destroy');
    }

    var pla = popoverPlacement(block_id);
    $block.popover({title: title, content: html, html: true, placement: pla});
    $block.popover('show');

    // 重置临时对象
    b.temp_print_css_json_obj = Object.assign({}, b.print_css_json_obj);

  var print_css_json_obj = b.print_css_json_obj;

/*
  alert(JSON.stringify(print_css_json_obj))*/
  $.each(print_css_json_obj, function(k, val) {
    if (k == 'font-family') {
      $('#' + block_id + '_font_family').val(val);
    } else if (k == 'font-size') {
      $('#' + block_id + '_font_size').val(val);
    } else if (k == 'font-weight') {
      $('#' + block_id + '_bold').addClass('active');
    } else if (k == 'font-style') {
      $('#' + block_id + '_italic').addClass('active');
    } else if (k == 'text-align') {
      var $sel = $('#' + block_id + '_align_' + val);
      $sel.parent().find('button').removeClass('active');
      $sel.addClass('active');
    } else if (k == 'overflow-wrap') {

    }
  });

  if (print_css_json_obj['overflow-wrap']) {
    $('#' + block_id + '_overflow_wrap').prop('checked', true);
  } else {
    $('#' + block_id + '_overflow_wrap').prop('checked', false);
  }

  var meta_data = b.meta_data;
  if (meta_data) {
    $('#' + block_id + '_md').text(meta_data.metadataName);
  }

  if (b.block_type)
    $('#' + block_id + '_type_' + b.block_type).attr("checked", "checked");

  if (core.readOnly) {
    $("input[id^='" + block_id + "']").attr('disabled', 'disabled');
    $('.jq_selecter').remove();
    $('#' + block_id + '_sel_md').remove();
  }

  if (pla == 'bottom') {
    var $popover = $block.next();
    var lf = $popover.position().left;
    var abs_lf = $popover.offset().left;
    var panel_lf = $panel.offset().left;

    // console.log("lf:" + lf + ",abs_lf=" + abs_lf + ",panel_lf:" + panel_lf);

    var top = $popover.position().top;
    // 三角形高度
    var popover_h = $popover.height() + 11;
    // 18 - 标尺的高度
    var s_d_h = $('#scroll_div').height() - 18;
    var panel_h = $panel.height();
    var final_h = s_d_h >= panel_h ? s_d_h : panel_h;

    // console.log("top:" + top + ",s_d_h=" + s_d_h + ",popover_h:" + popover_h + ",panel_h:" + panel_h + ",final_h=" + final_h);

    var popover_w = $popover.width();
    var s_d_w = $('#scroll_div').width();

    // 左边被遮挡
    if (abs_lf < panel_lf) {
      $popover.animate({left: lf + (panel_lf - abs_lf) + 'px'});
    }

    // 下边被遮挡
    if (top + popover_h > final_h) {
      var hide_h = top + popover_h - final_h;
      $popover.animate({top: top - hide_h + 'px'});
    }

    // 右边被遮挡
    if (lf + popover_h > s_d_w) {
      var hide_w = lf + popover_h - s_d_w;
      $popover.animate({left: lf - (hide_w + 25) + 'px'});
    }
  }


}



// 删除区块
core.deleteBlock = function (block_id) {

    // 销毁编辑框
    $('#' + block_id).popover('destroy');
    // 销毁右键菜单
    context.destroy('#' + block_id);
    // 删除html节点
    $('#' + block_id).remove();
    // 更新ztree节点
    ztree_helper.updateNode2(block_id);
    // 删除队列
    block_helper.remove_block_1(block_array, block_id);

    pre_block = undefined;
}

// 定位block 如超出视野自动控制滚动条
core.gotoBlock = function (block_id) {
    // 面板容器高度
    var panel_out_h;
    // 面板滚动条高度
    var panel_scroll_h;
    // 区块当前x坐标
    var x;
    // 面板高度对象宿主
    var $panel_out;
    // 面板滚动条对象宿主
    var $panel_scroll;
    // block对象
    var $block = $('#' + block_id);

    $panel_out = $('#scroll_div');
    $panel_scroll = $('.ef-ruler');
    panel_out_h = $panel_out.height();
    panel_scroll_h = $panel_scroll.scrollTop();
    x = $block.position().top;

    // console.log("sh->" + panel_scroll_h);
    // console.log("x->" + x);
    // console.log("panelh->" + $('#scroll_div').height());
    // 当前区块隐藏于下方
    if (x > panel_out_h) {
        var s_h = (x - panel_out_h) + $block.height() + 200;
        $panel_scroll.scrollTop(s_h);
        // 当前区块隐藏于上方
    } else if (x < panel_scroll_h) {
        if (x < panel_out_h / 2) {
            $panel_scroll.scrollTop(0);
        } else {
            var s_h = (panel_scroll_h - x) - $block.height();
            $panel_scroll.scrollTop(s_h);
        }
    }
}

// 清空
core.clearAllBlock = function (fun) {
    var a = block_array.slice(0);
    for (var i = 0; i < a.length; i++) {
        var block_id = a[i].block_html_id;
        core.deleteBlock(block_id);
    }
    // 防止存在错误数据
    block_array = new Array();
    $('.block').remove();
    // ---
    if (fun) fun();
}

//加载block_array
core.loadBlockArray = function(java_block_list_json_str,java_template_json_str){

  //console.log("java_block_list_json_str="+java_block_list_json_str)
  //console.log("java_template_json_str="+java_template_json_str)
  var a = JSON.parse(java_block_list_json_str);
  var template = JSON.parse(java_template_json_str);

  var old_baseImgStdWidth = template.baseImgStdWidth;
  var cur_baseImgStdWidth = core.baseImgStdWidth;
  //底图加载失败
  if(!cur_baseImgStdWidth){
    return;
  }
  if(java_block_list_json_str != ''&& java_block_list_json_str != 'null'){
    for(var i = 0;i < a.length;i++){
      var java_block = a[i];
      var block = new Block();
      block.block_idnew = java_block.id;
      block.block_oid = java_block.faModelTemplateBlockOid;
      block.block_code = java_block.blockCode;
      block.block_name = java_block.blockName;
      block.block_type = java_block.recognitionType;

      if(java_block.marqueeJsonValue){
        block.print_css_json_obj = JSON.parse(java_block.marqueeJsonValue);
        //console.log("java_block.marqueeJsonValue="+java_block.marqueeJsonValue)
      }
      //等比例展示
      if(old_baseImgStdWidth != cur_baseImgStdWidth){
        var rate = cur_baseImgStdWidth/old_baseImgStdWidth;
        block.x = java_block.x * rate;
        block.y = java_block.y * rate;
        block.width = java_block.width * rate;
        block.height = java_block.height * rate;
      }else{
        block.x = java_block.x;
        block.y = java_block.y;
        block.width = java_block.width;
        block.height = java_block.height;
      }
      block.sort = java_block.sort;

      if(block.sort > block_id_idx){
        block_id_idx = block.sort;
      }

      var block_id = 'block_id_' + java_block.sort;
      block.block_html_id = block_id;
      if(java_block.materialCatalogElementOid){
        block.meta_data = get_meta_data(java_block.materialCatalogElementOid);
        block.is_saved = true;
        ztree_helper.updateNode3(block_id,java_block.materialCatalogElementOid);
      }

      block_array.push(block);

      core.drowBlock(block);

      if(core.readOnly){
        $('.resize').remove();
        $('.block').css({cursor:'pointer'});
      }

    }
  }

  //关闭loading遮罩后，重置左侧滚动条
  setTimeout(function(){
    core.resizeScroll();
  },1000);
}


// 改变底图后，重新绘制区块
core.reDrawBlocks = function (old_baseImgStdWidth, cur_baseImgStdWidth) {
    // 底图宽度不存在
    if (!cur_baseImgStdWidth) {
        return false;
    }

    // 没有需要加载的区块
    if (block_array.length == 0) {
        return false;
    }
    // 修改模板，底图加载失败时
    if (!old_baseImgStdWidth && fromJsp.templateStr != '') {
        var template = JSON.parse(fromJsp.templateStr);
        old_baseImgStdWidth = template.baseImgStdWidth;
    }

    var rate = cur_baseImgStdWidth / old_baseImgStdWidth;
    check_block_size_error = false;
    check_block_overflow_error = false;
    var a = block_array.slice(0);
    for (var i = 0; i < a.length; i++) {
        var b = a[i];
        var block_id = b.block_html_id;
        b.x = b.x * rate;
        b.y = b.y * rate;
        b.width = b.width * rate;
        b.height = b.height * rate;

        // 销毁编辑框
        $('#' + block_id).popover('destroy');
        // 销毁右键菜单
        context.destroy('#' + block_id);
        // 删除html节点
        $('#' + block_id).remove();
        if (b.width < min_block_px || b.height < min_block_px) {
            core.deleteBlock(block_id);
            check_block_size_error = true;
        } else if (b.x + b.width > bgImg_panel_width || b.y + b.height > bgImg_panel_height) {
            core.deleteBlock(block_id);
            check_block_overflow_error = true;
        } else {
            core.drowBlock(b);
        }

    }

    var msg = '';
    if (check_block_size_error) {
        msg += '注意：检测到区块宽高小于' + min_block_px + 'px，已被清除！<br/>';
    }

    if (check_block_overflow_error) {
        msg += '注意：检测到区块超出面板，已被清除！';
    }

    if (msg != '') {
        util.warningTip(msg);
    }

}

// 暂存成功后更新block信息
core.updateBlockArray = function (jsonstr) {
    var array = JSON.parse(jsonstr);
    for (var i = 0; i < array.length; i++) {
        var java_block = array[i];
        for (var ii = 0; ii < array.length; ii++) {
            var block = block_array[ii];
            if (block.sort == java_block.sort) {
                block.block_oid = java_block.oid;
                break;
            }
        }
    }
}

// 绘制区块
core.drowBlock = function (block) {
    var block_id = block.block_html_id;
    var block_type = block.block_type;
    var block_html_template_ = block_html_template.replace(/\{block_id\}/g, block_id);
    block_html_template_ = block_html_template_.replace(/\{color\}/g, block_color_style);
    var block_html = $(block_html_template_);
    $panel.append(block_html);
    $('#' + block_id).css({"top": block.y + 'px', "left": block.x + 'px', width: block.width, height: block.height});
    $('#' + block_id + '_resize').show();
    if (!core.readOnly) {
        // 右键菜单
        core.addMenu(block_id);
        // 添加双击事件
        core.addDbClick(block_id);
    }
}

// 获取服务端区块格式json字符串
core.getBlockArrayStr = function(){
  //alert("获取服务端区块格式json字符串block_array******"+JSON.stringify(block_array))
  var java_block_array = new Array();
  for(var i = 0;i < block_array.length;i++){
    var b = block_array[i];
    //console.log(block_array[i]);
    var block_id = b.block_html_id;
    var $block = $('#' + block_id);
    b.x = $block.position().left;
    b.y = $block.position().top;
    //b.materialCatalogElementOid =
    b.width = $block.width();
    b.height = $block.height();
    java_block_array.push(block_helper.to_java_block(b));
  }
  return JSON.stringify(java_block_array);
}
// 更新区块信息
core.resetPanelUpdateBlockArray = function () {
    for (var i = 0; i < block_array.length; i++) {
        var b = block_array[i];
        var block_id = b.block_html_id;
        var $block = $('#' + block_id);
        b.x = $block.position().left;
        b.y = $block.position().top;
        b.width = $block.outerWidth();
        b.height = $block.outerHeight();
    }
}

// 添加右键菜单
core.addMenu = function (block_id) {
    var b = block_helper.get_block(block_array, block_id);
    var param_array = [];
    var idx = 0;
    param_array[idx++] = {header: '区块设置'};
    param_array[idx++] = {
        text: '编辑', action: function (e) {
            e.preventDefault();
            core.initBlock(block_id);
        }
    };
    // param_array[idx++] = {
    //     text: '复制', action: function (e) {
    //         e.preventDefault();
    //         core.cloneBlock(block_id, e);
    //     }
    // };
    param_array[idx++] = {
        text: '删除', action: function (e) {
            e.preventDefault();
            core.deleteBlock(block_id);
        }
    };

    context.attach('#' + block_id, param_array);
}

// 区块添加双击事件
core.addDbClick = function (block_id) {
    var $block = $('#' + block_id);
    $block.dblclick(function () {
        core.initBlock(block_id);
    });
}

// 只读单击区块查看信息
core.addClick = function (block_id) {
    var $block = $('#' + block_id);
    $block.click(function () {
        core.showBlockInfo(block_id);
    });
}

// 改变区块z-index，重叠的区块始终展示到最上面
core.resetZindex = function (block_id) {
    for (var i = 0; i < block_array.length; i++) {
        var b_id = block_array[i].block_html_id;
        $('#' + b_id).css('z-index', '1');
    }
    $('#' + block_id).css('z-index', '2');
}

// 查看当前block信息
core.showBlockInfo = function (block_id) {
    for (var i = 0; i < block_array.length; i++) {
        var b_id = block_array[i].block_html_id;
        if (b_id != block_id) {
            $('#' + b_id).popover('destroy');
        }
    }
    core.initBlock(block_id);
}

// 清除block高亮边缘、block信息弹层
core.resetBlockAssists = function () {
    $('.block').removeClass('active');
    for (var i = 0; i < block_array.length; i++) {
        var b_id = block_array[i].block_html_id;
        $('#' + b_id).popover('destroy');
    }
}

// 区块信息非空验证
core.valiteBlocks = function () {
  //console.log("block_array"+JSON.stringify(block_array))
    for (var i = 0; i < block_array.length; i++) {
        var block = block_array[i];
        var block_id = block.block_html_id;
        var block_type = block.block_type;
        if (!block.is_saved && block_type != 4) {
            util.alert('检测到打印区域未绑定目录元素！', function () {
                ztree_helper.selectNode(block_id);
                // 高亮边缘
                core.activeBlock(block_id);
                // 定位block
                core.gotoBlock(block_id);
                // 打开block信息编辑
                core.initBlock(block_id);
            });
            return false;
        }
    }
    return true;
}

// 重置左侧导航滚动条
core.resizeScroll = function () {
    $(".sidebar-nav").getNiceScroll().resize();
}

// 更新左侧整体信息
/**
 core.updateWholeInfo = function(info){
	if(!info) return;
	if(info.real_w)$('#real_w').text(info.real_w);
	if(info.real_h)$('#real_h').text(info.real_h);
	if(info.panel_w)$('#panel_w').text(info.panel_w);
	if(info.panel_h)$('#panel_h').text(info.panel_h);
	if(info.show_rate)$('#show_rate').text(info.show_rate);
}*/

// 获取主面板容器高度
// 60 顶栏高度
function getMaxPanelContainerHeight() {
    var h = document.documentElement.clientHeight || document.body.clientHeight;
    return h - 60 - 18;
}

// 获取主面板宽度
// 剔除滚动条以后的宽度
// 240 左侧导航宽度 + 容器2侧留白
function getMaxPanelWidth() {
    var w = document.documentElement.clientWidth || document.body.clientWidth;
    return w - 240 - util.scrollWidth() - 18;
}

// 计算多出的宽度
// if(是否有滚动条) +滚动条宽度
// if(底图实际尺寸过小) +100空白区域
function getExtraWidth() {
    var ext_w = 0;
    // 有滚动条
    if (bgImg_real_height > bgImg_panel_height) {
        ext_w += util.scrollWidth();
    }
    if ((bgImg_real_width + ext_w + 100) < getMaxPanelWidth()) {
        ext_w += 100;
    }
    return ext_w;
}

// 计算popover展示位置
// 右 -> 上  -> 下
function popoverPlacement(block_id) {
    var p_w = edit_panel_width;
    var p_h = edit_panel_height;
    // 面板高度对象宿主
    var $panel_out;
    // block对象
    var $block = $('#' + block_id);
    var b_w = $block.width();
    var b_h = $block.height();

    $panel_out = $('#scroll_div');
    x = $block.position().left;// 获取相对(父元素)位置
    y = $block.position().top;
    // if(x + b_w + p_w < $panel_out.width()
    if (x + b_w + p_w < $panel.width()
        && y + b_h + p_h / 2 < core.baseImgStdHeight
        && y > p_h / 2 + b_h) {
        return 'right';
    } else if (y > p_h) {
        return 'top';
    } else {
        return 'bottom';
    }

}

// 取消block编辑
function canselBlockEdit(block_id, type) {
    //debugger;
    if(!type){
        for (var i = 0; i < block_array.length; i++) {
            var b = block_array[i];
            if(b){
                if (b.block_html_id == block_id) {
                    var new_block = Object.assign({}, pre_block);
                    block_array[i] = new_block;
                }
            }
        }
    }

    $('#' + block_id).popover('destroy');

}

// 保存区块信息
function saveBlockInfo(block_id) {
    var old_block = block_helper.get_block(block_array, block_id);
    var block_type = $("input[name='" + block_id + "_type" + "']:checked").val();
    // console.log(block_code + ';' + block_name + ';' + block_type);
    if (!old_block.meta_data) {
        util.alert('请选择一个目录元素！', function () {
            $('#' + block_id + '_sel_md').click();
        });
        return false;
    }
    var meta_data = get_meta_data(old_block.meta_data.elementOid);
    var b = new Block();
    b.block_html_id = block_id;
    b.block_code = meta_data.elementCode;
    b.block_name = meta_data.elementName;
    b.block_type = block_type;
    b.is_saved = true;

    // 复制css样式
    b.print_css_json_obj = Object.assign({}, old_block.temp_print_css_json_obj);

    block_helper.update_block(block_array, b);
    ztree_helper.updateNode(block_id, meta_data.elementOid);

    $('#' + block_id).popover('destroy');

  //console.log("&&&"+ JSON.stringify(block_array))
    context.destroy('#' + block_id);
    core.addMenu(block_id);
}

// 根据oid查询 metadata
function get_meta_data(meta_data_oid) {
  //console.log("fromJsp.metaDataJsonArrayStr"+fromJsp.metaDataJsonArrayStr)
    var metaDataJsonArray = JSON.parse(fromJsp.metaDataJsonArrayStr);
    for (var i = 0; i < metaDataJsonArray.length; i++) {
        var metaData = metaDataJsonArray[i];
      var oid = metaData.elementOid;
      var name = metaData.elementName;
        if (oid == meta_data_oid) {
            return metaData;
        }
    }
    return null;
}

var switchBlockCursor = function (block_id, style) {
    $('#' + block_id).css({'cursor': style});
    $('#panel').css({'cursor': style == 'move' ? 'default' : style});
}

window.onload = function (e) {
    //  startX, startY 为鼠标点击时初始坐标
    //  diffX, diffY 为鼠标初始坐标与 box 左上角坐标之差，用于拖动
    var startX, startY, diffX, diffY;

    //  鼠标按下
    document.onmousedown = function (e) {

        // 右键 滚轮
        if (e.button == 2 || e.button == 1) {
            console.log('右键、滚轮');
            return;
        }

        startX = e.pageX;
        startY = e.pageY;

        if (startX < forbid_x_1 || startX > forbid_x_2
            || startY < forbid_y_1 || startY > forbid_y_2) {
            console.log('超出面板');
            return;
        }

        //  如果鼠标在 block 上被按下
        if (e.target.className.match(/block/)) {
            var block_id = e.target.id;

            // 销毁编辑框
            canselBlockEdit(block_id, 'block');

            // 通过菜单复制
            if (copy_block_id) {
                return false;
            }

            if (core.readOnly) {
                console.log('只读模式');
                core.resetBlockAssists();
                return;
            }

            //  检查组合键，是否已按CTRL
            if (pressingCtrl) {
                core.activeBlockCtrl(block_id);
                core.checkBatchHandle();
            } else if (pressingSpace){
                moveCopy(block_id, e);
                //  计算坐标差值
                diffX = startX - e.target.offsetLeft;
                diffY = startY - e.target.offsetTop;
            } else {
                moving_block_id = block_id;
                window.moving_block = $('#' + moving_block_id)[0];
                core.disabledBatchBtns();
                // 高亮边缘
                core.activeBlock(moving_block_id);
                //先ztree节点取消选中
                ztree_helper.unSelectNode();
                // 选择ztree节点
                ztree_helper.selectNode(moving_block_id);
                //  计算坐标差值
                diffX = startX - e.target.offsetLeft;
                diffY = startY - e.target.offsetTop;
                if (autoAlign) {
                    draggable.start(e);
                }
            }

            // 只读增加区块信息查看
            // core.readOnly && core.addClick(moving_block_id);

        } else if (e.target.className.match(/resize/)) {

            resize_block_id = $(e.target).parent().attr('id');

            switchBlockCursor(resize_block_id, 'se-resize');

            // 高亮边缘
            core.activeBlock(resize_block_id);

            core.disabledBatchBtns();

            // 创建区块
        } else {

            if (core.readOnly) {
                console.log('只读模式');
                core.resetBlockAssists();
                return;
            }

            // 只有面板区域的点击才是有效点击
            if (!e.target.className.match(/panel/)) {
                console.log('非面板区域');
                return;
            }

            core.disabledBatchBtns();

            var block_id = 'block_id_' + ++block_id_idx;
            // console.log(block_id_idx);
            var block_html_template_ = block_html_template.replace(/\{block_id\}/g, block_id);
            block_html_template_ = block_html_template_.replace(/\{color\}/g, block_color_style);
            var block_html = $(block_html_template_);
            $panel.append(block_html);
            $('#' + block_id).css({
                "top": (startY - $scroll_div.offset().top + $scroll_div.scrollTop()) + 'px',
                "left": (startX - $scroll_div.offset().left + $scroll_div.scrollLeft()) + 'px'
            });
            active_block_id = block_id;

            switchBlockCursor(active_block_id, 'se-resize');

            // 高亮边缘
            core.activeBlock(active_block_id);

            ztree_helper.unSelectNode();
        }
    };

    //  鼠标移动
    document.onmousemove = function (e) {
        // 左侧面板鼠标位置实时展示
        // 当前 总体信息面板是展开的
        if ($('#my_collapse').hasClass('in')) {
            // 未上传底图
            if (!core.fileOid) {
                return;
            }
            var x = e.pageX;
            var y = e.pageY;
            // 计算当前鼠标位置
            if (x < forbid_x_1 || x > forbid_x_2
                || y < forbid_y_1 || y > forbid_y_2) {
                $('#show_x').text('-');
                $('#show_y').text('-');
            } else {
                var $panel_scroll;
                $panel_scroll = $('.ef-ruler');

                panel_scroll_h = $panel_scroll.scrollTop();
                $('#show_x').text(x - $scroll_div.offset().left);
                var offset_top = $scroll_div.offset().top;
                var scroll_div_top_h = offset_top > 0 ? offset_top : panel_scroll_h + offset_top;
                $('#show_y').text(y - scroll_div_top_h + panel_scroll_h);
            }
        }
        // 只读
        if (core.readOnly) {
            return;
        }

        //  更新 block 尺寸
        if (active_block_id) {

            var $active_block = $('#' + active_block_id);
            var w = e.pageX - startX;
            var h = e.pageY - startY;

            var startX_2 = $active_block.offset().left + w;
            var startY_2 = $active_block.offset().top + h;

            // 超出绘制区域
            if (startX_2 > forbid_x_2
                || startY_2 > forbid_y_2) {

            } else {
                $active_block.css({'width': w + 'px', 'height': h + 'px'});
            }

        }

        //  移动，更新 block 坐标
        if (moving_block_id) {

            var $moving_block = $('#' + moving_block_id);

            var top_1 = e.pageY - diffY;
            var top_2 = top_1 + $moving_block.height();
            var left_1 = e.pageX - diffX;
            var left_2 = left_1 + $moving_block.width();

            // 超出绘制区域
            if (top_1 < 0 || left_1 < 0
                || top_2 > bgImg_panel_height
                || left_2 > bgImg_panel_width) {

            } else {
                $moving_block.css({'top': e.pageY - diffY + 'px', 'left': e.pageX - diffX + 'px'});
                if (autoAlign) {
                    draggable.drag(e);
                }
            }

        }

        //  移动，更新 复制的block 坐标
        if (copy_block_id) {
            var $copying_block = $('#' + copy_block_id);

            if (!diffY) {
                diffX = $copying_block.width() / 2 + $('#panel').offset().left;
                diffY = $copying_block.height() / 2 + $('#panel').offset().top;
            }

            var top_1 = e.pageY - diffY;
            var top_2 = top_1 + $copying_block.height();
            var left_1 = e.pageX - diffX;
            var left_2 = left_1 + $copying_block.width();

            // 超出绘制区域
            if (top_1 < 0 || left_1 < 0
                || top_2 > bgImg_panel_height
                || left_2 > bgImg_panel_width) {

            } else {
                $copying_block.css({'top': e.pageY - diffY + 'px', 'left': e.pageX - diffX + 'px'});
                if (autoAlign) {
                    draggable.drag(e);
                }
            }

        }

        // 拖拽改变block尺寸
        if (resize_block_id) {

            var $resize_block = $('#' + resize_block_id);
            startX = $resize_block.offset().left;
            startY = $resize_block.offset().top;

            var w = e.pageX - startX;
            var h = e.pageY - startY;

            if (e.pageX > forbid_x_2
                || e.pageY > forbid_y_2) {

            } else {
                $resize_block.css({'width': w + 'px', 'height': h + 'px'});
            }

        }

    };

    //  鼠标抬起
    document.onmouseup = function () {
        if (active_block_id) {
            var $active_block = $('#' + active_block_id);
            //  如果长宽均小于 3px，移除 box
            if ($active_block.width() < min_block_px || $active_block.height() < min_block_px) {
                $active_block.remove();
                --block_id_idx;
            } else {

                block_helper.add_block_2(block_array, active_block_id);
                $('#' + active_block_id + '_resize').show();

                // console.log(block_helper.to_string(block_array));

                if (!core.readOnly) {
                    var block_id = active_block_id;

                    core.addMenu(block_id);

                    core.addDbClick(block_id);
                }
            }
            switchBlockCursor(active_block_id, 'move');

            active_block_id = null;

        }

        if (copy_block_id) {
            var $copy_block = $('#' + copy_block_id);

            block_helper.add_block_2(block_array, copy_block_id);
            $('#' + copy_block_id + '_resize').show();


            if (!core.readOnly) {
                var block_id = copy_block_id;

                core.addMenu(copy_block_id);

                core.addDbClick(copy_block_id);
            }

            switchBlockCursor(copy_block_id, 'move');

            copy_block_id = null;
        }

        moving_block_id = null;

        if (resize_block_id) {
            var resize_block = $('#' + resize_block_id);
            //  如果长宽均小于 20px，移除 box
            if (resize_block.width() < min_block_px || resize_block.height() < min_block_px) {
                resize_block.remove();
                // 删除区块
                core.deleteBlock(resize_block_id);
            }
            switchBlockCursor(resize_block_id, 'move');
            resize_block_id = null;
        }
        if (autoAlign) {
            draggable.stop(e);
        }
    };

    // 禁止div被选中
    document.onselectstart = function (event) {
        if (window.event) {
            event = window.event;
        }
        try {
            var the = event.srcElement;
            if (the.tagName == "DIV") {
                return false;
            }
            return true;
        } catch (e) {
            return false;
        }
    }

    document.onkeydown = function (e) {
        e = e || event;
        var keycode = e.keyCode || e.which || e.charCode;
        if (keycode == 32){
            e.preventDefault();
        }
        var $blockArr = $panel.find('.block.active');
        var len = $blockArr.size();
        if(len > 0 && (keycode == 38 || keycode == 40 || keycode == 37 || keycode == 39)){
            e.preventDefault();
        }
        if (keycode == 17) {
            pressingCtrl = true;
        } else if (keycode == 32) {
            pressingSpace = true;
        }
    }

    document.onkeyup = function (e) {
        e = e || event;
        var keycode = e.keyCode || e.which || e.charCode;
        // ctrl
        if (keycode == 17) {
            pressingCtrl = false;
            // 空格
        } else if (keycode == 32) {
            pressingSpace = false;
            // 删除区块
        } else if (keycode == 46) {
            core.keyBordDeleteBlock();
        } else {
            core.keyBordMoveBlock(keycode);
        }
    }
};

// 复制
core.cloneBlock = function (source_block_id, e){
    var block_id = 'block_id_' + ++block_id_idx;

    var $block = $('#' + source_block_id).clone();
    $block.attr('id', block_id);
    $panel.append($block);

    copy_block_id = block_id;

    // 高亮边缘
    core.activeBlock(copy_block_id);

    ztree_helper.unSelectNode();

    var $copying_block = $('#' + copy_block_id);
    var diffX, diffY;
    diffX = $copying_block.width() / 2 + $('#panel').offset().left;
    diffY = $copying_block.height() / 2 + $('#panel').offset().top;


    var top_1 = e.pageY - diffY;
    var top_2 = top_1 + $copying_block.height();
    var left_1 = e.pageX - diffX;
    var left_2 = left_1 + $copying_block.width();

    // 超出绘制区域
    if (top_1 < 0 || left_1 < 0
        || top_2 > bgImg_panel_height
        || left_2 > bgImg_panel_width) {

    } else {
        $copying_block.css({'top': e.pageY - diffY + 'px', 'left': e.pageX - diffX + 'px'});
        switchBlockCursor(copy_block_id, 'move');
    }
}

// 按住Ctrl移动复制
var moveCopy = function (source_block_id, e) {
    if (copy_block_id) {
        return ;
    }
    var block_id = 'block_id_' + ++block_id_idx;

    var $block = $('#' + source_block_id).clone();
    $block.attr('id', block_id);
    $panel.append($block);

    copy_block_id = block_id;

    window.moving_block = $('#' + copy_block_id)[0];
    if (autoAlign) {
        draggable.start(e);
    }

    switchBlockCursor(copy_block_id, 'move');

    // 高亮边缘
    core.activeBlock(copy_block_id);

    ztree_helper.unSelectNode();

}

// 清除所有覆盖物
core.cleanAllCovering = function () {

    for (var i = 0; i < block_array.length; i++) {
        var b = block_array[i];
        var block_id = b.block_html_id;

        // 销毁编辑框
        $('#' + block_id).popover('destroy');
        // 销毁右键菜单
        context.destroy('#' + block_id);

    }
}

var timer_1 = null;
// 监听浏览器缩放
$(window).resize(function () {
    var fileOid = core.fileOid;
    // 底图存在，缩放浏览器，重绘工作台
    if (fileOid != null && fileOid != '' && fileOid.trim() != '') {
        util.showLoading();
        clearTimeout(timer_1);
        timer_1 = setTimeout(resizeWin, 1000);
    }
});

// 缩放浏览器，重绘工作台
function resizeWin() {
    var old_baseImgStdWidth = core.baseImgStdWidth;
    var filePath = core.filePath;
    var fileOid = core.fileOid;
    core.resetPanelUpdateBlockArray();
    core.loadBgImage(filePath, fileOid, function () {
        core.reDrawBlocks(old_baseImgStdWidth, core.baseImgStdWidth);
    });
}


// ---- 批量操作 ----
// 检测是否激活批量操作
core.checkBatchHandle = function () {
    var batchSelNum = $panel.find('.block.active').size();
    // 已选择2个以上区块，激活批量操作按钮
    if (batchSelNum > 1) {
        core.enabledBatchBtns();
    }
}

core.batchSelBlock = function () {
    batchSelBlockArray = [];
    $panel.find('.block.active').each(function(i, o){
        var b = block_helper.get_block(block_array, $(o).attr('id'));
        var block_id = b.block_html_id;
        var $block = $('#' + block_id);
        b.x = $block.position().left;
        b.y = $block.position().top;
        b.width = $block.outerWidth();
        b.height = $block.outerHeight();
        var b1 = Object.assign({}, b);
        batchSelBlockArray.push(b1);
    });
    // 已选择2个以上区块，激活批量操作按钮
    if (batchSelBlockArray.length > 1) {
        core.enabledBatchBtns();
    }
}

// 左对齐
core.copyBlockLeft = function () {
    core.batchSelBlock();
    var l_b = findLeftBlock();
    for (var i = 0; i < batchSelBlockArray.length; i++) {
        var b = batchSelBlockArray[i];
        var block_id = b.block_html_id;
        $('#' + block_id).css({'left': l_b.x});
    }
    core.saveLastHis();
}

// 居中对齐
core.copyBlockCenter = function () {
    core.batchSelBlock();
    var t_b = findTopBlock();
    var x_c = t_b.x + t_b.width/2;
    for (var i = 0; i < batchSelBlockArray.length; i++) {
        var b = batchSelBlockArray[i];
        var x_c_tmp = b.x + b.width/2;
        var block_id = b.block_html_id;
        if (x_c_tmp > x_c) {
            $('#' + block_id).css({left: b.x - (x_c_tmp - x_c)});
        } else {
            $('#' + block_id).css({left: b.x - (x_c_tmp - x_c)});
        }
    }
    core.saveLastHis();
}

// 右对齐
core.copyBlockRight = function () {
    core.batchSelBlock();
    var r_b = findRightBlock();
    var x_w = r_b.x + r_b.width;
    for (var i = 0; i < batchSelBlockArray.length; i++) {
        var b = batchSelBlockArray[i];
        var block_id = b.block_html_id;
        var x_w_tmp = b.x + b.width;
        if (x_w_tmp > x_w) {
            $('#' + block_id).css({'left': b.x + (x_w_tmp - x_w)});
        } else {
            $('#' + block_id).css({'left': b.x - (x_w_tmp - x_w)});
        }
    }
    core.saveLastHis();
}

// 顶端对齐
core.copyBlockTop = function () {
    core.batchSelBlock();
    var t_b = findTopBlock();
    for (var i = 0; i < batchSelBlockArray.length; i++) {
        var b = batchSelBlockArray[i];
        var block_id = b.block_html_id;
        $('#' + block_id).css({'top': t_b.y});
    }
    core.saveLastHis();
}

// 上下居中对齐
core.copyBlockValign = function () {
    core.batchSelBlock();
    var t_b = findTopBlock();
    var y_v = t_b.y + t_b.height/2;
    for (var i = 0; i < batchSelBlockArray.length; i++) {
        var b = batchSelBlockArray[i];
        var y_v_tmp = b.y + b.height/2;
        var block_id = b.block_html_id;
        if (y_v_tmp > y_v) {
            $('#' + block_id).css({top: b.y - (y_v_tmp - y_v)});
        } else {
            $('#' + block_id).css({top: b.y - (y_v_tmp - y_v)});
        }
    }
    core.saveLastHis();
}

// 底部对齐
core.copyBlockBottom = function () {
    core.batchSelBlock();
    var b_b = findBottomBlock();
    var y_h = b_b.y + b_b.height;
    for (var i = 0; i < batchSelBlockArray.length; i++) {
        var b = batchSelBlockArray[i];
        var block_id = b.block_html_id;
        var y_h_tmp = b.y + b.height;
        if (y_h_tmp > y_h) {
            $('#' + block_id).css({'top': b.y + (y_h_tmp - y_h)});
        } else {
            $('#' + block_id).css({'top': b.y - (y_h_tmp - y_h)});
        }
    }
    core.saveLastHis();
}

// 等宽
core.copyBlockWidth = function () {
    core.batchSelBlock();
    var t_b = findTopBlock();
    for (var i = 0; i < batchSelBlockArray.length; i++) {
        var b = batchSelBlockArray[i];
        var block_id = b.block_html_id;
        var $block = $('#' + block_id);
        $('#' + block_id).css({'width': t_b.width});
    }
    core.saveLastHis();
}

// 等高
core.copyBlockHeight = function () {
    core.batchSelBlock();
    var t_b = findTopBlock();
    for (var i = 0; i < batchSelBlockArray.length; i++) {
        var b = batchSelBlockArray[i];
        var block_id = b.block_html_id;
        var $block = $('#' + block_id);
        $('#' + block_id).css({'height': t_b.height});
    }
    core.saveLastHis();
}

// 批量添加时间
core.batchAddEvent = function () {
    $('#copyWdBtn').click(function () {
        core.copyBlockWidth();
    });
    $('#copyHtBtn').click(function () {
        core.copyBlockHeight();
    });
    $('#leftAlignBtn').click(function () {
        core.copyBlockLeft();
    });
    $('#centerAlignBtn').click(function () {
        core.copyBlockCenter();
    });
    $('#rightAlignBtn').click(function () {
        core.copyBlockRight();
    });
    $('#topAlignBtn').click(function () {
        core.copyBlockTop();
    });
    $('#verticalAlignBtn').click(function () {
        core.copyBlockValign();
    });
    $('#bottomAlignBtn').click(function () {
        core.copyBlockBottom();
    });
    $('#lastBtn').click(function () {
        core.canselLastHis();
    });
    $('#nextBtn').click(function () {
        core.recoverNextHis();
    });
}

// 找到最上边的
var findTopBlock = function () {
    var block = null;
    var y = 0;
    for (var i = 0; i < batchSelBlockArray.length; i++) {
        var b = batchSelBlockArray[i];
        var b_y = b.y;
        if (i == 0) {
            y = b_y;
            block = b;
        }
        if (b_y <= y) {
            y = b_y;
            block = b;
        }
    }
    return block;
}

// 找到最左边的
var findLeftBlock = function () {
    var block = null;
    var x = 0;
    for (var i = 0; i < batchSelBlockArray.length; i++) {
        var b = batchSelBlockArray[i];
        var b_x = b.x;
        if (i == 0) {
            x = b_x;
            block = b;
        }
        if (b_x <= x) {
            x = b_x;
            block = b;
        }
    }
    return block;
}

// 找到最右边的
var findRightBlock = function () {
    var block = null;
    var x_w = 0;
    for (var i = 0; i < batchSelBlockArray.length; i++) {
        var b = batchSelBlockArray[i];
        var x_w_tmp = b.x + b.width;
        if (i == 0) {
            x_w = x_w_tmp;
            block = b;
        }
        if (x_w_tmp >= x_w) {
            x_w = x_w_tmp;
            block = b;
        }
    }
    return block;
}

// 找到最下边的
var findBottomBlock = function () {
    var block = null;
    var y_h = 0;
    for (var i = 0; i < batchSelBlockArray.length; i++) {
        var b = batchSelBlockArray[i];
        var y_h_tmp = b.y + b.height;
        if (i == 0) {
            y_h = y_h_tmp;
            block = b;
        }
        if (y_h_tmp >= y_h) {
            y_h = y_h_tmp;
            block = b;
        }
    }
    return block;
}

// 按键删除区块
core.keyBordDeleteBlock = function () {
    var $blockArr = $panel.find('.block.active');
    var len = $blockArr.size();
    $blockArr.each(function (i, v) {
        var block_id = $(v).attr('id');
        core.deleteBlock(block_id);
        if (i == len - 1) {
            batchSelBlockArray = [];
            core.disabledBatchBtns();
        }
    });
}

// 按键微调区块位置
core.keyBordMoveBlock = function (keycode) {
    var step = 1;
    var $blockArr = $panel.find('.block.active');
    var len = $blockArr.size();
    $blockArr.each(function (i, v) {
        var block_id = $(v).attr('id');
        var $block = $('#' + block_id);
        switch(keycode) {
            // 上
            case 38:
                $block.css({"top": ($block.position().top - step) + 'px'});
                break;
            // 下
            case 40:
                $block.css({"top": ($block.position().top + step) + 'px'});
                break;
            // 左
            case 37:
                $block.css({"left": ($block.position().left - step) + 'px'});
                break;
            // 右
            case 39:
                $block.css({"left": ($block.position().left + step) + 'px'});
                break;
            default:

        }
        if (i == len - 1) {
            batchSelBlockArray = [];
            core.disabledBatchBtns();
        }
    });
}

// 保存上一步
core.saveLastHis = function (his) {
    if (his) {
        core.batchSelBlock();
    } else {

    }
    if (batchSelBlockArray.length == 0) {
        return ;
    }
    batchOperLastHis.push(batchSelBlockArray.concat());
    core.enabledHisBtn('lastBtn');
}

// 保存下一步
core.saveNextHis = function () {
    core.batchSelBlock();
    if (batchSelBlockArray.length == 0) {
        return ;
    }
    batchOperNextHis.push(batchSelBlockArray.concat());
    core.enabledHisBtn('nextBtn');
}
// 撤销上一步
core.canselLastHis = function () {
    if (batchOperLastHis.length == 0) {
        return false;
    }
    var operArr = batchOperLastHis.pop();
    core.saveNextHis();
    for (var i = 0; i < operArr.length; i++) {
        var block = operArr[i];
        var block_id = block.block_html_id;
        $('#' + block_id).css({"top": block.y + 'px', "left": block.x + 'px', width: block.width, height: block.height});
    }
    if (batchOperLastHis.length == 0) {
        core.disabledHisBtns('lastBtn');
    }
}

// 恢复上一步
core.recoverNextHis = function () {
    if (batchOperNextHis.length == 0) {
        return false;
    }
    var operArr = batchOperNextHis.pop();
    core.saveLastHis(true);
    for (var i = 0; i < operArr.length; i++) {
        var block = operArr[i];
        var block_id = block.block_html_id;
        $('#' + block_id).css({"top": block.y + 'px', "left": block.x + 'px', width: block.width, height: block.height});
    }
    if (batchOperNextHis.length == 0) {
        core.disabledHisBtns('nextBtn');
    }
}

// 清空历史记录
core.cleanHis = function () {
    core.disabledHisBtns();
    batchOperLastHis = [];
    batchOperNextHis = [];
}

// 禁用批量操作按钮
core.disabledBatchBtns = function () {
    $('.batch-btn button').addClass("disabled");
    core.disabledHisBtns();
    core.cleanHis();
}

// 启用批量操作按钮
core.enabledBatchBtns = function () {
    $('.batch-btn button').removeClass("disabled");
}

// 禁用历史操作按钮
core.disabledHisBtns = function (btnId) {
    if (btnId) {
        $('#' + btnId).addClass("disabled");
    }else{
        $('.batch-btn-his button').addClass("disabled");
    }
}

// 启用历史操作按钮
core.enabledHisBtn = function (btnId) {
    $('#' + btnId).removeClass("disabled");
}

// ---- end ----
