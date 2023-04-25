package com.zfsoft.single.util.fa;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import com.zfsoft.microservice.platform.data.sys.SysAtta;
import com.zfsoft.single.util.Base64Util;
import com.zfsoft.single.util.FaStaticParam;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class FaFileUtil {
    public static String getAbsFilePathBySysAtta(HttpServletRequest request, String sysAttaOid, SysAtta sysAtta) {
        String filePath = null;
        if (sysAtta != null) {
            String fileUrl= FaStaticParam.PROJECT_PATH+"\\modelTemples\\model\\";
           // String dirPath = request.getSession().getServletContext().getRealPath("/") + "fa" + File.separator + DateUtil.format(new Date(), "yyyyMMdd");
            String dirPath = fileUrl + "fa" + File.separator + DateUtil.format(new Date(), "yyyyMMdd");
            String fastdfsNginxUrl=sysAtta.getFastdfsNginxUrl();
            String imageName = fastdfsNginxUrl.substring(fastdfsNginxUrl.lastIndexOf("/")+1);
            filePath = dirPath + File.separator + imageName;
            File file = new File(filePath);
            if(!file.exists()){
                File dir = new File(dirPath);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                //复制文件系统中的附件到本地系统
                HttpUtil.downloadFile(fastdfsNginxUrl, cn.hutool.core.io.FileUtil.file(dirPath));
            }
        }
        return filePath;
    }
/*    public ApiResultSet download(String attaOid) throws IOException {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = servletRequestAttributes.getRequest();
        HttpServletResponse response = servletRequestAttributes.getResponse();
        // 上传文件路径
        String filePath = CaseServiceConfig.getDowloadPath();
        //获取文件信息
        SysAtta atta = sysAttaManager.querySysAttaByOid(attaOid);
        response.reset();
        String realpath = filePath+atta.getFilePath()+ File.separator + atta.getName();
        File file = new File(realpath);
        FileInputStream fileInputStream = new FileInputStream(file);
        OutputStream outputStream = response.getOutputStream();
        IOUtils.write(IOUtils.toByteArray(fileInputStream), outputStream);
        response.setHeader("content-disposition",  "attachment;fileName=" + URLEncoder.encode(atta.getOriginName(), "UTF-8"));
        response.setContentType("application/octet-stream;charset=UTF-8");
        outputStream.flush();
        return null;
    }*/

    /**
     * 根据系统附件表oid获取对应文件绝对位置
     *
     * @param sysAttaOid
     *            附件oid
     * @return 文件绝对路径
     * @throws Exception
     */
    public static String getAbsFilePathBySysAttaOid(HttpServletRequest request, String sysAttaOid){
        String filePath = null;
       /* SysAtta sysAtta=sysAttaFeginService.getSysAttaByAttaOid(sysAttaOid);

        AttaInfoResult attaInfoResult = FileManageUtil.getAttaInfo(sysAttaOid);
        if(!FaStaticParam.HTTP_REQUEST_CODE_SUCCESS.equals(attaInfoResult.getCode())){
            return filePath;
        }
        Atta atta = attaInfoResult.getResult();
        if (atta != null) {
            String dirPath = request.getSession().getServletContext().getRealPath("/") + "fa" + File.separator + DateUtil.format(new Date(), "yyyyMMdd");
            filePath = dirPath + File.separator + atta.getName();
            File file = new File(filePath);
            if(!file.exists()){
                File dir = new File(dirPath);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                AttaBase64Result attaBase64Result = FileManageUtil.getAttaBase64(sysAttaOid);
                if (FaStaticParam.HTTP_REQUEST_CODE_SUCCESS.equals(attaBase64Result.getCode())) {
                    BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(file));
                    byte[] bytes = new Base64Decoder().decode(attaBase64Result.getResult());
                    outputStream.write(bytes);
                    outputStream.flush();
                    outputStream.close();
                }
            }
        }*/
        return filePath;
    }

    /**
     * 根据日期+随机数生成文件名
     *
     * @param fileExt
     *            文件扩展名，支持传递文件名自动截取扩展名
     */
    public static String buildFileName(String fileExt) {
        int lastDotPos = fileExt.lastIndexOf(".");
        String picExt = lastDotPos > -1 ? fileExt.substring(lastDotPos + 1).toUpperCase() : fileExt;
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        String fileName = df.format(new Date()) + "_" + new Random().nextInt(1000);
        return fileName + "." + picExt;
    }

    /**
     * 按系统附件上传设置，获取附件存放位置
     *
     * @parem folderName 文件存放目录
     * @param fileExt
     *            文件扩展名，支持传递文件名自动截取扩展名
     */
    public static String buildFileSavePath(HttpServletRequest request, String folderName, String fileExt) {
        String fileUrl= FaStaticParam.PROJECT_PATH+"\\modelTemples\\model\\";
        String dir = fileUrl + folderName;
        File dirFile = new File(dir);
        if (!dirFile.exists()) {
            dirFile.mkdirs();
        }
        return folderName + "/" + buildFileName(fileExt);
    }

    /**
     * 将base64编码后进行urlencode的图像数据，解析后存储到服务本地 并返回服务器绝对路径
     * urldecode-->base64decode-->保存到本地
     */
    public static String parseImageData(HttpServletRequest request, String imageString, String imageSaveDir) {
        String filePath = null;
        try {
            String urlDecodeImage = URLDecoder.decode(imageString, "UTF-8");
            byte[] imageBtye = Base64Util.decode(urlDecodeImage);
            String fileExt = getExtByStream(imageBtye);
            filePath = UploadUtil.getRealSavePath(request) + imageSaveDir + "/" + buildFileName(fileExt);
            FileUtil.byte2image(imageBtye, imageSaveDir);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return filePath;
    }

    /**
     * byte数组转换成16进制字符串
     *
     * @param src
     *            图片byte数组
     * @return 16进制字符串
     */
    public static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder();
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

    /**
     * 根据文件流读取图片文件真实类型
     *
     * @param fileTypeByte
     *            图片二进制流
     * @return 图片文件真实类型
     */
    public static String getExtByStream(byte[] fileTypeByte) {
        if(fileTypeByte!=null){
            String type = bytesToHexString(fileTypeByte).toUpperCase();
            if (type.contains("FFD8FF")) {
                return "jpg";
            } else if (type.contains("89504E47")) {
                return "png";
            } else if (type.contains("47494638")) {
                return "gif";
            } else if (type.contains("49492A00")) {
                return "tif";
            } else if (type.contains("424D")) {
                return "bmp";
            } else {
                return null;
            }
        }
        return null;
    }

    /**
     * 对图片进行md5
     *
     * @param url
     *            图片存放地址
     * @param image
     *            图片对应二进制流
     * @return
     */
    public static String getMd5Str(String url, String image) {
        if (StrUtil.isNotBlank(url)) {
            return FaCommonUtil.md5(url);
        }
        if (image != null) {
            return FaCommonUtil.md5(image);
        }
        return "";
    }

}
