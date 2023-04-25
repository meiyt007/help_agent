package com.zfsoft.service.util;


import cn.hutool.core.util.StrUtil;
import com.zfsoft.service.common.SxptBaseStaticParameter;
import org.apache.commons.fileupload.FileItem;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * 文件上传类
 * 
 * @author gaolh
 * @date 2016-12-2 11:12:49
 *
 */
public class UploadUtil {

    // 最大文件大小
    private long maxSize = 100000000;
    // 定义允许上传的文件扩展名
    private static Map<String, String> extMap = new HashMap<String, String>();
    // 文件保存目录相对路径
    public static String basePath = "upload";
    // 文件的目录名
    private String dirName = "all";
    // 若不指定则文件名默认为 yyyyMMddHHmmss_xyz
    private String fileName;
    // 原始文件名
    private String originalFileName;
    // 文件扩展名
    private String fileExt;

    // 文件保存目录路径
    private String savePath;
    // 不包含系统配置的路径，移植时方便移植
    private String systemPath;
    // 错误信息
    private String errorInfo = "true";
    // web请求对象
    private HttpServletRequest request;

    static {
        // 其中images,flashs,medias,files,对应文件夹名称,对应dirName
        // key文件夹名称
        // value该文件夹内可以上传文件的后缀名
        String imagesExt = "gif,jpg,jpeg,png,bmp";
        extMap.put("images", imagesExt);
        String flashsExt = "swf,flv";
        extMap.put("flashs", flashsExt);
        String mediasExt = "swf,flv,mp3,wav,wma,wmv,mid,avi,mpg,asf,rm,rmvb";
        extMap.put("medias", mediasExt);
        String filesExt = "doc,docx,xls,xlsx,pdf,ppt,pptx,zip,rar,tar,gz,et,ett,dbf,wps,wpt,bz2,apk";
        extMap.put("files", filesExt);
        extMap.put("all", imagesExt + "," + flashsExt + "," + mediasExt + "," + filesExt + ",data");
    }

    /**
     * 得到真实的保存路径
     */
    public static String getRealSavePath(HttpServletRequest request) {
        String fullPath = SxptBaseStaticParameter.UPLOAD_PATH;
        return fullPath;
    }

    public UploadUtil() {
        String path = this.getClass().getResource("").toString();
            savePath = SxptBaseStaticParameter.UPLOAD_PATH + "/" + basePath + "/";

        systemPath = basePath + "/";
        // 创建文件夹
        savePath += dirName + "/";
        systemPath += dirName + "/";
        // .../basePath/dirName/yyyyMMdd/
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String ymd = sdf.format(new Date());
        savePath += ymd + "/";
        systemPath += ymd + "/";
    }

    public UploadUtil(HttpServletRequest request) {
        this.request = request;
        String contentType = request.getContentType();
        int contentLength = request.getContentLength();
        String scannedFile = (String) request.getAttribute("scannedFile");
        // 文件保存目录路径
        savePath = getRealSavePath(request) + "/" + basePath + "/";
        systemPath = basePath + "/";
        if ((contentType == null || !contentType.startsWith("multipart")) & StrUtil.isEmpty(scannedFile)) {
            errorInfo = "请求不包含multipart/form-data流";
        } else if (maxSize < contentLength) {
            errorInfo = "上传文件大小超出文件最大大小[" + maxSize + "]";
        } else {
            // .../basePath/dirName/
            // 创建文件夹
            savePath += dirName + "/";
            systemPath += dirName + "/";

            // .../basePath/dirName/yyyyMMdd/
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            String ymd = sdf.format(new Date());
            savePath += ymd + "/";
            systemPath += ymd + "/";
        }
    }

    /**
     * 保存文件
     * 
     *            要上传的文件域
     */
    public String saveFile(MultipartFile mFile) throws UnsupportedEncodingException {
        CommonsMultipartFile uploadFile = (CommonsMultipartFile) mFile;
        FileItem item = uploadFile.getFileItem();

        String error = "true";
        originalFileName = java.net.URLDecoder.decode(item.getName(), "UTF-8");
        fileExt = originalFileName.substring(originalFileName.lastIndexOf(".") + 1).toLowerCase();

        if (item.getSize() > maxSize) { // 检查文件大小
            error = "上传文件大小超过限制";
        } else if (!Arrays.<String> asList(extMap.get(dirName).split(",")).contains(fileExt)) {// 检查扩展名
            error = "上传文件扩展名是不允许的扩展名。\n只允许" + extMap.get(dirName) + "格式。";
        } else {
            // 当保存文件名为空时，按照日期生成文件名
            if (StrUtil.isEmpty(fileName)) {
                SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmssSSS");
                fileName = df.format(new Date()) + "_" + new Random().nextInt(1000) + "." + fileExt;
            }
            try {
              Map<String, String> paramMap = new HashMap<>();
            		// fileName 文件保存名称名
            		// downDir 文件保存路径
            		paramMap.put("fileName", fileName);
            		paramMap.put("fileOriginalName", originalFileName);
            		paramMap.put("downDir", systemPath);
            		FileSysUtil.TYWJSave(paramMap, new FileInputStream(new File(systemPath, fileName)));

            } catch (Exception e) {
                error = "上传出现错误，" + e.getMessage();
            }
        }
        return error;
    }

    /**
     * 根据文件路径，名称下载文件
     * 
     * @param displayFileName
     *            文件名称
     * @param filePath
     *            文件路径
     * @param request
     *            http对象
     * @throws Exception
     */
    public static void downloadFile(String displayFileName, String filePath, String fileName,
                                    HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("multipart/form-data");
        response.setHeader("Content-Disposition",
                "attachment;fileName=\"" + new String(displayFileName.getBytes("UTF-8"), "ISO-8859-1") + "\"");

        String savePath = UploadUtil.getRealSavePath(request) + "/" + UploadUtil.basePath + "/";
        String downloadFilePath = savePath + filePath + fileName;
       String tempFileName = request.getSession().getServletContext().getRealPath("/") + "temp" + "/" + fileName;
          //下载
			FileSysUtil.downloadFile(filePath, fileName,tempFileName);
            downloadFilePath = tempFileName;

        InputStream inputStream = new FileInputStream(new File(downloadFilePath));

        OutputStream os = response.getOutputStream();
        byte[] b = new byte[2048];
        int length;
        while ((length = inputStream.read(b)) > 0) {
            os.write(b, 0, length);
        }

        // 这里主要关闭。
        os.close();

        inputStream.close();

    }

    public String saveFile(File file) throws UnsupportedEncodingException {
        String error = "true";
        originalFileName = java.net.URLDecoder.decode(file.getName(), "UTF-8");
        fileExt = originalFileName.substring(originalFileName.lastIndexOf(".") + 1).toLowerCase();

        if (file.length() > maxSize) { // 检查文件大小
            error = "上传文件大小超过限制";
        } else if (!Arrays.<String> asList(extMap.get(dirName).split(",")).contains(fileExt)) {// 检查扩展名
            error = "上传文件扩展名是不允许的扩展名。\n只允许" + extMap.get(dirName) + "格式。";
        } else {
            // 当保存文件名为空时，按照日期生成文件名
            if (StrUtil.isEmpty(fileName)) {
                SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmssSSS");
                fileName = df.format(new Date()) + "_" + new Random().nextInt(1000) + "." + fileExt;
            }
            try {
               Map<String, String> paramMap = new HashMap<>();
            		// fileName 文件保存名称名
            		// downDir 文件保存路径
            		paramMap.put("fileName", fileName);
            		paramMap.put("fileOriginalName", originalFileName);
            		paramMap.put("downDir", savePath);
                	FileSysUtil.TYWJSave(paramMap, new FileInputStream(file));

            } catch (Exception e) {
                error = "上传出现错误，" + e.getMessage();
            }
        }
        return error;
    }

    /**
     * 
     * @description 构造函数，主要用于ueditor
     * @author zhujiajian
     * @date 2017年4月8日 下午4:53:26
     *            文件路径，包含文件名。具体生成规则见ueditor config.json
     */
    public UploadUtil(HttpServletRequest request, String fileDir, String fileName) {
        this.request = request;
        this.savePath = getRealSavePath(request) + File.separator + fileDir;
        this.fileName = fileName;

    }
    /** **********************get/set方法********************************* */

    public String getSavePath() {
        return savePath;
    }

    public long getMaxSize() {
        return maxSize;
    }

    public String getSystemPath() {
        return systemPath;
    }

    public void setMaxSize(long maxSize) {
        this.maxSize = maxSize;
    }

    public Map<String, String> getExtMap() {
        return extMap;
    }

    public String getBasePath() {
        return basePath;
    }

    public String getDirName() {
        return dirName;
    }

    public void setDirName(String dirName) {
        this.dirName = dirName;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getOriginalFileName() {
        return originalFileName;
    }

    public void setOriginalFileName(String originalFileName) {
        this.originalFileName = originalFileName;
    }

    public String getFileExt() {
        return fileExt;
    }

    public void setFileExt(String fileExt) {
        this.fileExt = fileExt;
    }

    public void setSavePath(String savePath) {
        this.savePath = getRealSavePath(request) + "/" + savePath;
        this.systemPath = savePath;
    }

    public String getErrorInfo() {
        return errorInfo;
    }

    public void setErrorInfo(String errorInfo) {
        this.errorInfo = errorInfo;
    }
}
