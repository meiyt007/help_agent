//工具模块
var util = {browserScrollWidth:null};
function IEVersion(){
    var userAgent = navigator.userAgent; //取得浏览器的userAgent字符串
    var isIE = userAgent.indexOf("compatible") > -1 && userAgent.indexOf("MSIE") > -1; //判断是否IE<11浏览器
    var isEdge = userAgent.indexOf("Edge") > -1 && !isIE; //判断是否IE的Edge浏览器
    var isIE11 = userAgent.indexOf('Trident') > -1 && userAgent.indexOf("rv:11.0") > -1;
    if(isIE) {
        var reIE = new RegExp("MSIE (\\d+\\.\\d+);");
        reIE.test(userAgent);
        var fIEVersion = parseFloat(RegExp["$1"]);
        if(fIEVersion == 7) {
            return 7;
        } else if(fIEVersion == 8) {
            return 8;
        } else if(fIEVersion == 9) {
            return 9;
        } else if(fIEVersion == 10) {
            return 10;
        } else {
            return 6;//IE版本<=7
        }
    } else if(isEdge) {
        return 'edge';//edge
    } else if(isIE11) {
        return 11; //IE11
    }else{
        return -1;//不是ie浏览器
    }
}

util.isIE = IEVersion() == -1 ? false : true;

//loading
var d = dialog({
	        width: 60,
	        height: 60
	    });
var isShow = false;
//展示loading
util.showLoading = function(){
	if(!isShow){
		 d.showModal();
		 isShow = true;
	}
}

//隐藏loading
util.closeLoading = function(fun){
	setTimeout(function(){
		d.close();
		isShow = false;
		if(fun)fun();
	},1000);
}

//确认提示框
util.confirm = function(content,fun){
	var d = dialog({
        title: '消息',
        width: 200,
        content: content,
        okValue: '确 定',
        ok: function() {
        	if(fun)fun();
        	this.close();
            return false;
        },
        cancelValue: '取消',
        cancel: function() {}
    });

    d.show();
}

//提示
util.alert = function(content,fun){
	var d = dialog({
		title: '提示',
		width: 200,
		content: content,
		okValue: '确定',
		ok: function () {
			if(fun)fun();
			d.close();
			return false;
		}
	});
	d.show();
}

//警告信息提示
util.warningTip = function(msg){
	var d = dialog({
		content: msg,
		skin: 'tip-dialog tip-warning',
		tipClassName:'ui-tip'
	});
	d.show();
	setTimeout(function(){
		d.close().remove();
	},4000);

}
//成功信息提示
util.sucTip = function(msg){
	var d = dialog({
		content: msg,
		skin: 'tip-dialog tip-success',
		tipClassName:'ui-tip'
	});
	d.show();
	setTimeout(function(){
		d.close().remove();
	},4000);

}

//计算当前浏览器滚动条宽度
util.scrollWidth = function(){
	if(this.browserScrollWidth){
		return this.browserScrollWidth;
	}
	var odiv = document.createElement('div'),//创建一个div
        styles = {
            width: '100px',
            height: '100px',
            overflowY: 'scroll'//让他有滚动条
        }, i, scrollbarWidth;
    for (i in styles) odiv.style[i] = styles[i];
    document.body.appendChild(odiv);//把div添加到body中
    scrollbarWidth = odiv.offsetWidth - odiv.clientWidth;//相减
    document.body.removeChild(odiv);//移除创建的div
    return scrollbarWidth;//返回滚动条宽度
}
//计算远程图片实际宽度、高度
util.imgReady = function(fileOid, ready, error){
  img = {};
 /* img.width = 1200;
  img.height = 800;
  ready && ready.call(img);*/
 $.get(VUE_APP_BASE_API + "/platform/security/atta/nologin/getImageWidthAndHeightByAttaOid/"+fileOid, null,
    function(data, textStatus, jqXHR){
    if (data.code==200){
      var ret=JSON.parse(data.data) ;
      var height=ret.height;
      var width=ret.width;
      img.width = width;
      img.height = height;
      ready && ready.call(img);
    }else{
      error && error.call(img);
    }
    },'json');
}


//计算远程图片实际宽度、高度
util.imgReady_bak = function (url, ready, load, error) {
    var onready, width, height, newWidth, newHeight,
      img = new Image();

    img.src = url;
    // 如果图片被缓存，则直接返回缓存数据
    if (img.complete && !util.isIE) {
      ready.call(img);
      load && load.call(img);
      return;
    };

    width = img.width;
    height = img.height;

    // 加载错误后的事件
    img.onerror = function () {
      error && error.call(img);
      onready.end = true;
      img = img.onload = img.onerror = null;
    };

    // 图片尺寸就绪
    onready = function () {
      newWidth = img.width;
      newHeight = img.height;
      if (newWidth !== width || newHeight !== height ||
        // 如果图片已经在其他地方加载可使用面积检测
        newWidth * newHeight > 1024
      ) {
        ready.call(img);
        onready.end = true;
      };
    };
    onready();

    // 完全加载完毕的事件
    img.onload = function () {
      // onload在定时器时间差范围内可能比onready快
      // 这里进行检查并保证onready优先执行
      !onready.end && onready();
      load && load.call(img);

      // IE gif动画会循环执行onload，置空onload即可
      img.onload = img.onerror = null;
    };
};

//阻止事件冒泡
util.cancelBubble = function(e){
	e.cancelBubble = true;
    e.stopPropagation();
	return false;
}

