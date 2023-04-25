//var baseUrl = "http://hf.zhuofansoft.com:16203/file/";
var reg = /\#[\x00-\xff]*/g //去除#之后的所有字符
var href = window.location.href
window.ctxUrl = href.replace(reg,"")
window.config  = {
  // FilePlatformUrl: baseUrl,
  // previewurl: baseUrl + "atta/previewFile.do?attaOid=",
  // pdfviewUrl: baseUrl + "js/pdf/web/viewer.html?attaOid=",
  // download_url: baseUrl + "atta/downloadFile.do?attaOid="
};

