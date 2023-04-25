package com.zfsoft.single.util.fa;

import cn.hutool.core.util.StrUtil;
import com.zfsoft.platform.utils.fileUtil.FtpUtil;
import org.apache.commons.fileupload.FileItem;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class UploadUtil {

    /** 最大文件大小 */
    private long maxSize = 100000000;
    /** 定义允许上传的文件扩展名 */
    private static Map<String, String> extMap = new HashMap<String, String>();
    /** 文件保存目录相对路径 */
    public static String basePath = "upload";
    /** 文件的目录名 */
    private String dirName = "all";
    /** 若不指定则文件名默认为 yyyyMMddHHmmss_xyz */
    private String fileName;
    /** 原始文件名 */
    private String originalFileName;
    /** 文件扩展名 */
    private String fileExt;

    /** 文件保存目录路径 */
    private String savePath;
    /** 不包含系统配置的路径，移植时方便移植 */
    private String systemPath;
    /** 错误信息 */
    private String errorInfo = "true";
    /** web请求对象 */
    private HttpServletRequest request;

    public static final String UPLOADTYPE;
    public static final String UPLOADPATH;
    public static final String UPLOADFTPIP;
    public static final String UPLOADFTPPORT;
    public static final String UPLOADFTPUSERNAME;
    public static final String UPLOADFTPPASSWORD;
    static {
        Map<String, Object> pu = new HashMap<String, Object>();// FileManageUtil.attaConfig();
        UPLOADTYPE = (String) pu.get("uploadType");
        UPLOADPATH = (String) pu.get("uploadPath");
        UPLOADFTPIP = (String) pu.get("uploadFtpIp");
        UPLOADFTPPORT = (String) pu.get("uploadFtpPort");
        UPLOADFTPUSERNAME = (String) pu.get("uploadFtpUsername");
        UPLOADFTPPASSWORD = (String) pu.get("uploadFtpPassword");
        // 其中images,flashs,medias,files,对应文件夹名称,对应dirName
        // key文件夹名称
        // value该文件夹内可以上传文件的后缀名
        String imagesExt = "gif,jpg,jpeg,png,bmp";
        extMap.put("images", imagesExt);
        String flashsExt = "swf,flv";
        extMap.put("flashs", flashsExt);
        String mediasExt = "swf,flv,mp3,wav,wma,wmv,mid,avi,mpg,asf,rm,rmvb";
        extMap.put("medias", mediasExt);
        // String filesExt =
        // "doc,docx,xls,xlsx,ppt,htm,html,txt,zip,rar,gz,bz2,pdf";
        String filesExt = "doc,docx,xls,xlsx,ppt,txt,zip,rar,gz,bz2,pdf";
        extMap.put("files", filesExt);
        extMap.put("all", imagesExt + "," + flashsExt + "," + mediasExt + "," + filesExt + ",data");
    }

    /**
     * 得到真实的保存路径
     */
    public static String getRealSavePath(HttpServletRequest request) {
        String fullPath = null;
        String charOne = "2";
        String charTwo = "3";
        if (charOne.equals(UPLOADTYPE)) {
            fullPath = UPLOADPATH + "/";
        } else if (charTwo.equals(UPLOADTYPE)) {
            fullPath = UPLOADPATH;
        } else if (StrUtil.isBlank(UPLOADPATH)) {
            fullPath = request.getSession().getServletContext().getRealPath("/") ;
        } else {
            fullPath = request.getSession().getServletContext().getRealPath("/") + UPLOADPATH + "/";
        }
        return fullPath;
    }

    public UploadUtil(HttpServletRequest request) {
        this.request = request;
        String contentType = request.getContentType();
        int contentLength = request.getContentLength();

        // 文件保存目录路径
        savePath = getRealSavePath(request) + basePath + "/";
        systemPath = basePath + "/";
        String charOne = "multipart";
        if (contentType == null || !contentType.startsWith(charOne)) {
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
            String strOne = "3";
            if (!strOne.equals(UPLOADTYPE)) {
                File saveDirFile = new File(savePath);
                if (!saveDirFile.exists()) {
                    saveDirFile.mkdirs();
                }
                File dirFile = new File(savePath);
                if (!dirFile.exists()) {
                    dirFile.mkdirs();
                }
            }
        }
    }

    /**
     * 保存文件
     *
     * @paramobj
     *            要上传的文件域
     */
    public String saveFile(MultipartFile mFile) throws UnsupportedEncodingException {
        CommonsMultipartFile uploadFile = (CommonsMultipartFile) mFile;
        FileItem item = uploadFile.getFileItem();
        String error = "true";
        originalFileName = java.net.URLDecoder.decode(item.getName(), "UTF-8");
        // 当 文件名称中存在文件路径时，去除路径
        int lastSlashPos = originalFileName.lastIndexOf("\\");
        if (lastSlashPos > -1) {
            originalFileName = originalFileName.substring(originalFileName.lastIndexOf("\\") + 1);
        }
        fileExt = originalFileName.substring(originalFileName.lastIndexOf(".") + 1).toLowerCase();
        String charOne = ",";
        // 检查文件大小
        if (item.getSize() > maxSize) {
            error = "上传文件大小超过限制";
            // 检查扩展名
        } else if (!Arrays.<String>asList(extMap.get(dirName).split(charOne)).contains(fileExt)) {
            error = "上传文件扩展名是不允许的扩展名。\n只允许" + extMap.get(dirName) + "格式。";
        } else {
            // 当保存文件名为空时，按照日期生成文件名
            if (StrUtil.isBlank(fileName)) {
                SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmssSSS");
                fileName = df.format(new Date()) + "_" + new Random().nextInt(1000) + "." + fileExt;
            }
            try {
                // 3-FTP上传
                String strOne = "3";
                if (strOne.equals(UPLOADTYPE)) {
                    FtpUtil.uploadFile(UPLOADFTPIP, Integer.parseInt(UPLOADFTPPORT), UPLOADFTPUSERNAME,
                            UPLOADFTPPASSWORD, savePath, fileName, mFile.getInputStream());
                } else {
                    File uploadedFile = new File(savePath, fileName);

                    item.write(uploadedFile);
                }
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
        // 当为FTP存储时，先将文件下载到项目中，在进行下载
        String charOne = "3";
        if (charOne.equals(UPLOADTYPE)) {
            String tempFileName = request.getSession().getServletContext().getRealPath("/") + "temp" + "/" + fileName;
            FtpUtil.downloadFile(UPLOADFTPIP, Integer.parseInt(UPLOADFTPPORT), UPLOADFTPUSERNAME, UPLOADFTPPASSWORD,
                    savePath + filePath, fileName, tempFileName);
            downloadFilePath = tempFileName;
        }

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
