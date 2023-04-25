var initialized = false;
var displayGrid = 0;
var imgPath = null;
var cutting = false;
var handle = false;

$(function() {
	// 点击 打开摄像头
	$('#init_btn').click(function() {
		CZUR_Initialize_JS();
	});

	// 点击 拍照
	$('#pic_btn').click(function() {
		if (handle)
			return;
		var $btn = $(this);
		$btn.html('<i class="fa fa-camera"></i> 拍照中...');
		$btn.attr('disabled', 'disabled');
		handle = true;
		CZUR_GrabSingleImage();
	});

	// 点击 网格线
	$('#wg_btn').click(function() {
		CZUR_DisplayGrid();
	});

	// 点击 单页裁边
	$('#cb_btn').click(function() {
		CZUR_EdgeCutting();
	});

	// 点击 保存
	$('#save_btn').click(function() {
		saveShots();
	});

	// 点击 颜色模式
	$('#ys_ul a').click(function() {
		var imageClrMode = $(this).attr('val');
		$('#ys_ul a i').remove();
		$(this).append($('<i class="fa fa-check-circle"></i>'));
		CZUR_ClrMode(imageClrMode);
	});

	// 点击 旋转角度
	$('#xz_ul a').click(function() {
		var rotateAngle = $(this).attr('val');
		$('#xz_ul a i').remove();
		$(this).append($('<i class="fa fa-check-circle"></i>'));
		CZUR_Rotate(rotateAngle);
	});

	// 点击删除
	$(document).on('click', '#imgs_ul i', function() {
		$(this).parent().remove();
	});

	// 点击图片 预览
	$(document).on('click', '#imgs_ul img', function() {
		window.open($(this).attr('src'));
	});
});

// 保存操作
function saveShots() {
	var fileOids = '';
	$('[fileOid]').each(function() {
		fileOids += $(this).attr('fileOid') + ','
	});

	fileOids = fileOids.substring(0, fileOids.length - 1);

	if (fileOids == '') {
		$('.object-mask').show();
		Dialog.alert('请至少扫描一张图片！', function() {
			$('.object-mask').hide();
		});
		return;
	}
	$('.object-mask').show();
	Dialog.confirm('确定保存扫描底图吗？', function() {
		var $btn = $('#save_btn');
		$btn.html('<i class="fa fa-save"></i> 保存...');
		$btn.attr('disabled', 'disabled');
		$.post(fromJsp.ctxPath + "fa/modelTemplate/saveShots.do?random="
				+ Math.random(), {
			fileOids : fileOids
		}, function(data, textStatus, jqXHR) {
			if (data.success) {
				Dialog.close();
			} else {
				Dialog.alert('保存信息出错！', function() {
					$('.object-mask').hide();
					$btn.html('<i class="fa fa-save"></i> 保存');
					$btn.removeAttr('disabled');
				});
			}
		}, 'json');
	}, function() {
		$('.object-mask').hide();
	});
}
// 颜色模式
function CZUR_ClrMode(imageClrMode) {
	EtOcxEx.CZUR_ClrMode(imageClrMode);
}
// 网格线
function CZUR_DisplayGrid() {
	var val = displayGrid == 0 ? 1 : 0;
	EtOcxEx.CZUR_DisplayGrid(val);
	displayGrid = val;
}
// 裁边操作
function CZUR_EdgeCutting() {
	if (cutting) {
		EtOcxEx.CZUR_Original();
	} else {
		EtOcxEx.CZUR_EdgeCutting();
	}
	cutting = cutting == true ? false : true;
}
// 旋转设置
function CZUR_Rotate(rotateAngle) {
	EtOcxEx.CZUR_Rotate(rotateAngle);
}

// 初始化资源
function CZUR_Initialize_JS() {
	var error = null;
	try {
		if (initialized)
			return;
		var lInitialize = EtOcxEx.CZUR_Initialize_JS("JS_OCX.log");
		if (0 == lInitialize) {
			alert("初始化高拍仪失败！");
		} else {
			CZUR_OpenDevice();
			// 不进行裁边拍照
			CZUR_EdgeCutting();
		}
	} catch (e) {
		error = e;
		Dialog.errorAlert('高拍仪加载失败！');
	}
	if (error)
		return;
	initialized = true;
	$("[disabled='disabled']").removeAttr("disabled");
	$('#init_btn').attr('disabled', 'disabled');
	EtOcxEx.CZUR_Custom(fromJsp.cataName, 1)
}
// 初始化资源
function CZUR_Deinitialize() {
	EtOcxEx.CZUR_Deinitialize();
}
// 打开设备
function CZUR_OpenDevice() {
	var bOpenDevice = EtOcxEx.CZUR_OpenDevice();
	if (0 == bOpenDevice) {
		alert("高拍仪加载失败！");
	}
}
// 关闭设备
function CZUR_CloseDevice() {
	EtOcxEx.CZUR_CloseDevice();
}

// 拍照
function CZUR_GrabSingleImage() {
	var lSuccess = EtOcxEx.CZUR_GrabSingleImage();
	if (0 == lSuccess) {
		alert("高拍仪加载失败！")
	}
}

// 设置图片本地保存路径
function CZUR_Path(path) {
	EtOcxEx.CZUR_Path(save_path);
}

// 销毁资源
function destoryShot() {
	CZUR_CloseDevice();
	CZUR_Deinitialize();
}

// 图片上传
function CZUR_Http_Upload() {
	var locafile = imgPath;
	var url = fromJsp.ctxPath + 'ImageUploadServlet?userOid=' + fromJsp.userOid;
	var name = 'name';
	var username = "";
	var password = "";
	var ret = EtOcxEx.CZUR_Http_Upload(locafile, url, name, username, password);
	switch (ret) {
	case 1:
		alert("未初始化EtOcxEx资源");
		break;
	case 2:
		alert("本地文件不存在");
		break;
	case 3:
		alert("失败");
		break;
	case 0:
		break;
	}
}
// 拍照 回调
function CZUR_CALLBACK(iUploadCnt, httpInfo, imageFile1, imageFile2) {
	// var varInfo = "上传个数: " + iUploadCnt + "\n" + "http反馈信息: " + httpInfo +
	// "\n" + "文件1: " + imageFile1 + "\n" + "文件2: " + imageFile2;
	imgPath = imageFile1;
	CZUR_Http_Upload();

}
// 图片上传回调
function CZUR_UPLOAD_CALLBACK(iUploadCnt, localFile, iErrorCode, ErrorInfo) {
	if (0 == iUploadCnt) {
		// var varInfo = "本地文件: " + localFile + " 上传失败" + "\n" + "错误代码: " +
		// iErrorCode + "\n" + "错误信息: " + ErrorInfo;
		alert(ErrorInfo);
	} else {
		var fileOid = ErrorInfo;
		if (fileOid.length > 32) {
			fileOid = fileOid.substr(0, 32);
		}
		$('#imgs_ul').append(
				'<li><img style="height:156px"  src="ImageShowServlet?fileOid='
						+ fileOid + '" fileOid="' + fileOid
						+ '"/><i class="fa fa-remove"></i></li>');
		$('#pic_btn').html('<i class="fa fa-camera"></i> 拍照').removeAttr(
				'disabled');
		handle = false;
	}
}
