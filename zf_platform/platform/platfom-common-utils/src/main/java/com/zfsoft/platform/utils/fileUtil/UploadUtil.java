package com.zfsoft.platform.utils.fileUtil;


import cn.hutool.core.util.StrUtil;
import com.zfsoft.platform.common.data.BaseStaticParameter;
import com.zfsoft.platform.utils.data.SysAttaTemp;
import com.zfsoft.platform.utils.validate.ResultInfoException;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.awt.image.BufferedImage;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * @description:  文件上传类
 * @author: wuxx
 * @Date: 2020/11/27 17:36
 **/
public class UploadUtil {

    /** 最大文件大小 */
    private long maxSize = 500000000;
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
    /** 文件大小 */
    private int fileSize;
    /** 文件保存目录路径 */
    private String savePath;
    /** 不包含系统配置的路径，移植时方便移植 */
    private String systemPath;
    /** 错误信息 */
    private String errorInfo;
    /**web请求对象 */
    private HttpServletRequest request;

    /** 文件上传统一文件服务后的id */
    private String fileOid;

    static {
        // 其中images,flashs,medias,files,对应文件夹名称,对应dirName
        // key文件夹名称
        // value该文件夹内可以上传文件的后缀名
        String imagesExt = "gif,jpg,jpeg,png,bmp";
        extMap.put("images", imagesExt);
        String flashsExt = "swf,flv";
        extMap.put("flashs", flashsExt);
        String mediasExt = "swf,flv,mp3,wav,wma,wmv,mid,avi,mpg,asf,rm,rmvb,mp4,mp5";
        extMap.put("medias", mediasExt);
        //String filesExt = "doc,docx,xls,xlsx,ppt,htm,html,txt,zip,rar,gz,bz2,pdf";
        String filesExt = "doc,docx,xls,xlsx,ppt,txt,zip,rar,gz,bz2,pdf";
        //ofd 电子证照
        String dzExt = "ofd";
        extMap.put("dz", dzExt);
        extMap.put("files", filesExt);
        extMap.put("all", imagesExt + "," + flashsExt + "," + mediasExt + ","
                + filesExt +","+dzExt+",data");
    }

    /**
     * @description: 新增限制的map(多个用逗号隔开)
     * @author: wuxx
     * @Date: 2021/8/23 16:19
     **/
    public static void initExtMap(String fileExt) {
        String all = extMap.get("all");
        extMap.put("all",all+","+fileExt);
    }
    /**
     * 得到真实的保存路径
     */
    public static String getRealSavePath(HttpServletRequest request) {
        String fullPath = null;
        if (BaseStaticParameter.UPLOAD_TYPE.IN_DISK.equals(FileUploadParam.uploadType())) {
            fullPath = FileUploadParam.uploadPath() + "/";
        } else if (BaseStaticParameter.UPLOAD_TYPE.FTP.equals(FileUploadParam.uploadType())) {
            fullPath = FileUploadParam.uploadPath();
        }else if (BaseStaticParameter.UPLOAD_TYPE.UNIFIED_FILES.equals(FileUploadParam.uploadType())) {
            fullPath = FileUploadParam.uploadPath();
        }else {
            fullPath = request.getSession().getServletContext().getRealPath("/")
                    + FileUploadParam.uploadPath() + "/";
        }
        return fullPath;
    }

    public UploadUtil(HttpServletRequest request) {
        this.request = request;
        String contentType = request.getContentType();
        int contentLength = request.getContentLength();
        fileSize = contentLength;
        // 文件保存目录路径
        savePath = getRealSavePath(request) + "/" + basePath + "/";
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
            if (!BaseStaticParameter.UPLOAD_TYPE.FTP.equals(FileUploadParam.uploadType())&&
                    !BaseStaticParameter.UPLOAD_TYPE.UNIFIED_FILES.equals(FileUploadParam.uploadType())) {
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
     * @param mFile
     *            要上传的文件域
     */
    public String uploadFile(MultipartFile mFile) throws Exception {
        String originalFilename = mFile.getOriginalFilename();
        originalFileName = java.net.URLDecoder.decode(originalFilename, "UTF-8");
        if(originalFilename.indexOf("%00") > -1 || originalFilename.indexOf("./") > -1 || originalFilename.indexOf(".\\") > -1 ) {
            throw new ResultInfoException("上传文件名称非法！");
        }
        // 当 文件名称中存在文件路径时，去除路径
        int lastSlashPos = originalFileName.lastIndexOf("\\");
        if (lastSlashPos > -1) {
            originalFileName = originalFileName
                    .substring(originalFileName.lastIndexOf("\\") + 1);
        }
        fileExt = originalFileName
                .substring(originalFileName.lastIndexOf(".") + 1).toLowerCase();
        String charOne = ",";
        // 检查文件大小
        if (mFile.getSize() > maxSize) {
            throw new ResultInfoException("上传文件大小超过限制！");
            // 检查扩展名
        } else if (!Arrays.<String> asList(extMap.get(dirName).split(charOne))
                .contains(fileExt)) {
            throw new ResultInfoException("上传文件扩展名是不允许的扩展名！");
            //throw new RuntimeException("上传文件扩展名是不允许的扩展名。\n只允许" + extMap.get(dirName) + "格式。");
        } else {
            //当保存文件名为空时，按照日期生成文件名
            if (StrUtil.isEmpty(fileName)) {
                SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmssSSS");
                fileName = df.format(new Date()) + "_"
                        + new Random().nextInt(1000) + "." + fileExt;
            }
            try {
                // 3-FTP上传
                if (BaseStaticParameter.UPLOAD_TYPE.FTP.equals(FileUploadParam.uploadType())) {
                    FtpUtil.uploadFile(FileUploadParam.uploadFtpIp(),
                            Integer.parseInt(FileUploadParam.uploadFtpPort()),
                            FileUploadParam.uploadFtpUsername(),
                            FileUploadParam.uploadFtpPassword(),savePath,
                            fileName, mFile.getInputStream());
                } else if(BaseStaticParameter.UPLOAD_TYPE.UNIFIED_FILES.equals(FileUploadParam.uploadType())) {
                    //直接上传fastDFS
                    fileOid = FastDFSUtil.uploadFile(mFile.getInputStream(), fileExt);
                } else {
                    File uploadedFile = new File(savePath, fileName);
                    mFile.transferTo(uploadedFile);
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new ResultInfoException("上传文件失败！");
            }
        }
        return fileOid;
    }

    /**
     * @description: 获取附件信息
     * @param filePath fastdfs文件存储路径
     * @param userOid 用户oid
     * @author: wuxx
     * @Date: 2020/9/14 14:06
     **/
    public SysAttaTemp getSysAttaFile(String filePath, String userOid){
        SysAttaTemp atta = new SysAttaTemp();
        atta.setIsDelete(BaseStaticParameter.N);
        if (BaseStaticParameter.UPLOAD_TYPE.UNIFIED_FILES.equals(FileUploadParam.uploadType())) {
            if (StrUtil.isEmpty(filePath)) {
                throw new ResultInfoException("上传文件失败！");
            }
            atta.setFastdfsUploadUrl(filePath);
            atta.setFastdfsNginxUrl(BaseStaticParameter.FASTDFS_NGINX_URL + "/" + atta.getFastdfsUploadUrl());
        }
        atta.setExtensionName(getFileExt());
        atta.setFilePath(getSystemPath());
        atta.setName(getFileName());
        atta.setOriginName(getOriginalFileName());
        atta.setUploadDate(new Date());
        atta.setCanChangeFlag(BaseStaticParameter.NO);
        atta.setFileSize(getFileSize()+"");
        // 获取前台传递的userId参数
        atta.setUserOid(userOid);
        return atta;
    }

    /**
     * 将BufferedImage转换为InputStream
     * @param image
     * @return
     */
    public static InputStream bufferedImageToInputStream(BufferedImage image, String formatName){
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            ImageIO.write(image, formatName, os);
            InputStream input = new ByteArrayInputStream(os.toByteArray());
            return input;
        } catch (IOException e) {
        }
        return null;
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

    public int getFileSize() {
        return fileSize;
    }
}
