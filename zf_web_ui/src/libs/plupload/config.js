const config = {
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
    url : 'atta/uploadChunkFile.do',
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
    otherPrams: null,
    //多文件上传
    multi_selection: false
}

config.files_added = (up, files) => {
    console.log('files_added'+files)
}

config.upload_complete = (up, files) => {
    console.log('upload_complete'+files)
}

config.file_uploaded = (up, file, info, otherPrams) => {
    console.log('file_uploaded'+file)
}

config.upload_progress = (up, file) => {
    console.log('upload_progress'+file)
}

config.error_fun = (up, errObject) => {
    console.log('error_fun'+errObject)
}

export default config