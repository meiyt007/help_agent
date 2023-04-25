package com.zfsoft.single.util.fa;

import cn.hutool.core.util.StrUtil;

import java.util.HashMap;
import java.util.Map;

public class UploadChunkUtil {

    /** 最大文件大小 */
    private long maxSize = 100000000;
    /** 定义允许上传的文件扩展名 */
    private static Map<String, String> extMap = new HashMap<String, String>();
    /** 文件保存目录相对路径 */
    public static String basePath = "upload";
    /** 若不指定则文件名默认为 yyyyMMddHHmmss_xyz */
    private String fileName;


    /** 当前项目所在文件路径 */
    private static String appPath = "";


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
        String filesExt = "doc,docx,wps,xls,xlsx,ppt,txt,zip,rar,gz,bz2,pdf";
        extMap.put("files", filesExt);
        extMap.put("all", imagesExt + "," + flashsExt + "," + mediasExt + "," + filesExt + ",data");
    }

    /**
     * 得到真实的保存路径
     */
    public static String getRealSavePath() {
        String fullPath = null;
        String charOne = "2";
        String charTwo = "3";
        if (charOne.equals(UploadUtil.UPLOADTYPE)) {
            fullPath = UploadUtil.UPLOADPATH + "/";
        } else if (charTwo.equals(UploadUtil.UPLOADTYPE)) {
            fullPath = UploadUtil.UPLOADPATH;
        } else if(StrUtil.isBlank(UploadUtil.UPLOADTYPE)){
            fullPath = getAppPath();
        } else {
            fullPath = getAppPath() + UploadUtil.UPLOADPATH + "/";
        }
        return fullPath;
    }

    public synchronized static String getAppPath() {
        if (StrUtil.isNotBlank(UploadChunkUtil.appPath)) {
            return UploadChunkUtil.appPath;
        }
        String appPath = UploadChunkUtil.class.getClassLoader().getResource("").getPath();
        if (appPath != null) {
            int pos = appPath.lastIndexOf("WEB-INF/classes/");
            if (pos > 0) {
                appPath = appPath.substring(0, pos);
            }
        }
        UploadChunkUtil.appPath = appPath;
        return UploadChunkUtil.appPath;
    }









    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }


}
