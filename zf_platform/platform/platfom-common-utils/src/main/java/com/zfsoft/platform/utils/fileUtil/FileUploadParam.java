package com.zfsoft.platform.utils.fileUtil;


import cn.hutool.core.util.StrUtil;
import com.zfsoft.platform.common.data.BaseStaticParameter;
import com.zfsoft.platform.utils.data.SysConfig;
import com.zfsoft.platform.utils.feign.SysConfigFeignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

/**
 * 文件上传参数
 *
 * @author wuxx
 * @date 2020-12-1 14:59:14
 */
public class FileUploadParam {

    private static SysConfigFeignService configFeignService;

    @Autowired
    public void setConfigFeignService(SysConfigFeignService configFeignService) {
        this.configFeignService = configFeignService;
    }

    @Value("${fdfs.fastDFSNginxUrl:#{null}}")
    public void setNginxUrl(String nginxUrl) {
        if(null!=nginxUrl){
            BaseStaticParameter.FASTDFS_NGINX_URL = nginxUrl.trim() ;
        }
        fastDFSNginxUrl = nginxUrl;
    }
    /**
     *  fastdfsNGINX地址
     */
    public static String fastDFSNginxUrl;

    /**
     * 存储类型
     */
    private static String ATTA_UPLOAD_TYPE;
    /**
     * 附件存储的路径
     */
    private static String ATTA_UPLOAD_PATH;
    /**
     * FTPip
     */
    private static String ATTA_UPLOAD_FTP_IP;
    /**
     * FTP端口
     */
    private static String ATTA_UPLOAD_FTP_PORT;
    /**
     * FTP用户名
     */
    private static String ATTA_UPLOAD_FTP_USERNAME;
    /**
     * FTP密码
     */
    private static String ATTA_UPLOAD_FTP_PASSWORD;

    /**
     * 1-存储到项目中，2-存储到磁盘中，3-存储到FTP中  4-文件服务器中
     */
    public static String uploadType() {
        if(StrUtil.isNotEmpty(ATTA_UPLOAD_TYPE)){
            return ATTA_UPLOAD_TYPE;
        }
        SysConfig configByType = configFeignService.getSysConfigByCode("ATTA_UPLOAD_TYPE").getData();
        if (null != configByType) {
            ATTA_UPLOAD_TYPE = configByType.getValue();
            return configByType.getValue();
        }
        return "4";
    }

    /**
     * 附件存储的路径，当为项目中时，为文件夹名称，当为磁盘中，需要写全路径，当为FTP中，填写FTP的文件路径
     */
    public static String uploadPath() {
        if (StrUtil.isNotEmpty(ATTA_UPLOAD_PATH)) {
            return ATTA_UPLOAD_PATH;
        }
        SysConfig configByType = configFeignService.getSysConfigByCode("ATTA_UPLOAD_PATH").getData();
        if (null != configByType) {
            ATTA_UPLOAD_PATH = configByType.getValue();
            return configByType.getValue();
        }
        return "/zwfw_file";
    }

    /**
     * FTP存储，服务器地址
     */
    public static String uploadFtpIp() {
        if (StrUtil.isNotEmpty(ATTA_UPLOAD_FTP_IP)) {
            return ATTA_UPLOAD_FTP_IP;
        }
        SysConfig configByType = configFeignService.getSysConfigByCode("ATTA_UPLOAD_FTP_IP").getData();
        if (null != configByType) {
            ATTA_UPLOAD_FTP_IP = configByType.getValue();
            return configByType.getValue();
        }
        return "127.0.0.1";
    }

    /**
     * FTP存储，服务器端口
     */
    public static String uploadFtpPort() {
        if (StrUtil.isNotEmpty(ATTA_UPLOAD_FTP_PORT)) {
            return ATTA_UPLOAD_FTP_PORT;
        }
        SysConfig configByType = configFeignService.getSysConfigByCode("ATTA_UPLOAD_FTP_PORT").getData();
        if (null != configByType) {
            ATTA_UPLOAD_FTP_PORT = configByType.getValue();
            return configByType.getValue();
        }
        return "21";
    }

    /**
     * FTP用户名
     */
    public static String uploadFtpUsername() {
        if (StrUtil.isNotEmpty(ATTA_UPLOAD_FTP_USERNAME)) {
            return ATTA_UPLOAD_FTP_USERNAME;
        }
        SysConfig configByType = configFeignService.getSysConfigByCode("ATTA_UPLOAD_FTP_USERNAME").getData();
        if (null != configByType) {
            ATTA_UPLOAD_FTP_USERNAME = configByType.getValue();
            return configByType.getValue();
        }
        return "user";
    }

    /**
     * FTP密码
     */
    public static String uploadFtpPassword() {
        if (StrUtil.isNotEmpty(ATTA_UPLOAD_FTP_PASSWORD)) {
            return ATTA_UPLOAD_FTP_PASSWORD;
        }
        SysConfig configByType = configFeignService.getSysConfigByCode("ATTA_UPLOAD_FTP_PASSWORD").getData();
        if (null != configByType) {
            ATTA_UPLOAD_FTP_PASSWORD = configByType.getValue();
            return configByType.getValue();
        }
        return "123";
    }

    public static void setAttaUploadType(String attaUploadType) {
        ATTA_UPLOAD_TYPE = attaUploadType;
    }

    public static void setAttaUploadFtpPassword(String attaUploadFtpPassword) {
        ATTA_UPLOAD_FTP_PASSWORD = attaUploadFtpPassword;
    }

    public static void setAttaUploadFtpUsername(String attaUploadFtpUsername) {
        ATTA_UPLOAD_FTP_USERNAME = attaUploadFtpUsername;
    }

    public static void setAttaUploadFtpPort(String attaUploadFtpPort) {
        ATTA_UPLOAD_FTP_PORT = attaUploadFtpPort;
    }

    public static void setAttaUploadFtpIp(String attaUploadFtpIp) {
        ATTA_UPLOAD_FTP_IP = attaUploadFtpIp;
    }

    public static void setAttaUploadPath(String attaUploadPath) {
        ATTA_UPLOAD_PATH = attaUploadPath;
    }

}
