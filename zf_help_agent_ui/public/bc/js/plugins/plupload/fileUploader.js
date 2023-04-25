/** 分段上传 by zhujiajian*/
(function(w){

    var _this,_defParam;

    _this = w.FileUploader = {};

    // 参数列表
    _defParam = {
        // 文件分块大小
        chunkSize : '10mb',
        // 文件拦截后缀
        extensions : 'jpg,png,jpeg,bmp',
        // 按钮ID
        buttonId : 'plupload_uploader',
        // 接口http地址
        ctxPath : null,
        // swfhttp地址
        ctxPath1 : null,
        // 接口服务地址
        url : '/dev-api/platform/security/atta/nologin/uploadFile',
        // 设置多文件上传样式函数
        files_added: null,
        // 设置文件上传完成样式函数
        upload_complete: null,
        // 文件上传完毕触发函数
        file_uploaded: null,
        // 置文件上传进度样式函数
        upload_progress: null,
        // 设置文件上传失败样式函数
        error_fun: null,
        // 用于参数传递，无实际作用
        otherPrams: null
    };

    // 页面装载进度条容器
    _this.init = function () {
        $(function () {
            var $div = $('<div id="file-pop-partmet" class="pop-container">\
                <h3 class="title">文件上传进度</h3>\
            </div>');
            $('body').append($div);
        });
    }

    _this.init();

    // 装配上传对象
    _this.initUpload = function (param) {
        var param1 = Object.assign({}, _defParam, param);
        var uploader = createPluploadUploader(param1);
        uploader.init();
        return uploader;
    }

    _defParam.error_fun = function (uploader, errObject) {
        var progressDiv = $("#file-pop-partmet");
        var file = errObject.file;
        var fileDiv = '<div class="box-body" style="height: 30px;">\
            <div style="float: left; width:150px; overflow:hidden; margin-right: 5px;"><nobr>' + file.name + '：</nobr></div>\
            <div style="margin-left:0px; width: 70%; height: 20px;">' + errObject.message + '</div>\
        </div>';
        progressDiv.append(fileDiv);
    }

    _defParam.upload_progress = function (uploader, file){
        var processFile = $("#processFile" + file.id);
        processFile.css("width", file.percent + "%");
        processFile.html(file.percent + "%");
    }

    _defParam.upload_complete = function (uploader, files) {
        $("#file-pop-partmet").css('display', 'none');
        $("#file-pop-partmet").find(".progress-box").remove();
    }

    // 文件上传完毕触发
    _defParam.file_uploaded = function (up, file, info, otherPrams) {

    }

    // 完成附件选择事件
    _defParam.files_added = function (up, files) {
        var progressDiv = $("#file-pop-partmet");
        progressDiv.css('display', 'block');
        for(var i = 0; i < files.length; i++) {
            var file = files[i];
            var fileDiv = '<div class="progress-box" style="height: 30px;">\
                <div style="float: left; width:150px; overflow:hidden; margin-right: 15px;margin-left: 20px;"><nobr>' + file.name + '：</nobr></div>\
                <div class="progress" style="margin-left:0px; width: 70%; height: 20px;">\
                    <div class="progress-bar progress-bar-success" id="processFile' + file.id + '" role="progressbar" aria-valuenow="40"\
                        style="width: 0%; height: 20px; line-height: 20px; text-align: left; padding-left: 5px; color: black" \
                        aria-valuemin="0" aria-valuemax="100">0%</div>\
                </div>\
            </div>';
            progressDiv.append(fileDiv);
        }
    }

    // 创建Uploader对象
    var createPluploadUploader = function (param) {
        var uploader = new plupload.Uploader({
            // 设置运行环境，会按设置的顺序，可以选择的值有html5,gears,flash,silverlight,browserplus,html
            runtimes : 'html5,flash,silverlight',
            flash_swf_url : param.ctxPath1 + 'js/plugins/plupload/Moxie.swf',
            silverlight_xap_url : param.ctxPath1 + 'js/plugins/plupload/Moxie.xap',
            // 上传文件路径
            url :  param.url,
            filters : [{
                title : 'files',
                extensions : param.extensions
            }],
            // 100b, 10kb, 10mb, 1gb
            max_file_size : '2gb',
            // 分块大小，小于这个大小的不分块
            chunk_size : param.chunkSize,
            // 生成唯一文件名
            unique_names : true,
            browse_button : param.buttonId,
            init : {
                FilesAdded : function(up, files) {
                    param.files_added(up, files);
                    uploader.start();
                    return false;
                },
                // 文件上传完毕触发
                FileUploaded : function(up, file, info) {
                  //alert(JSON.stringify(info))
                    param.file_uploaded(up, file, info, param.otherPrams);
                },
                // 队列处理完成
                UploadComplete : function(uploader, files) {
                    param.upload_complete(uploader, files);
                },
                UploadProgress : function(uploader, file) {
                    param.upload_progress(uploader, file);
                },
                Error : function(uploader, errObject) {
                    param.error_fun(uploader, errObject);
                }
            }
        });
        return uploader;
    }
})(window);
