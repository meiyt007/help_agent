import config from "./config";
import plupload from "plupload";
import { Toast } from "vant";
// 创建Uploader对象
var createPluploadUploader = function(param) {
  let UploadParams = {
    // 设置运行环境，会按设置的顺序，可以选择的值有html5,gears,flash,silverlight,browserplus,html
    runtimes: "html5,flash,silverlight,html4",
    flash_swf_url: param.ctxPath1 + "js/plugins/plupload/Moxie.swf",
    silverlight_xap_url: param.ctxPath1 + "js/plugins/plupload/Moxie.xap",
    // 上传文件路径
    url: param.ctxPath + param.url,
    extensions: "",
    // filters : [{
    //     title : 'files',
    //     extensions : param.extensions
    // }],
    // 100b, 10kb, 10mb, 1gb
    max_file_size: "2gb",
    multi_selection: param.multi_selection,
    // 分块大小，小于这个大小的不分块
    chunk_size: param.chunkSize,
    // 生成唯一文件名
    unique_names: false,
    browse_button: param.buttonId,
    init: {
      FilesAdded: function(up, files) {
        param.files_added(up, files);
        uploader.start();
        return false;
      },
      // 文件上传完毕触发
      FileUploaded: function(up, file, info) {
        param.file_uploaded(up, file, info, param.otherPrams);
      },
      // 队列处理完成
      UploadComplete: function(uploader, files) {
        param.upload_complete(uploader, files);
      },
      UploadProgress: function(uploader, file) {
        param.upload_progress(uploader, file);
      },
      Error: function(uploader, errObject) {
        param.error_fun(uploader, errObject);
      }
    }
  };
  if (param.extensions != "") {
    UploadParams.filters = [
      {
        title: "files",
        extensions: param.extensions
      }
    ];
  }
  var uploader = new plupload.Uploader(UploadParams);
  return uploader;
};

const initUpload = function(params) {
  const new_params = Object.assign({}, config, params);
  let uploader = createPluploadUploader(new_params);
  uploader.init();
  window.pupload = uploader;
  return uploader;
};

export default initUpload;
