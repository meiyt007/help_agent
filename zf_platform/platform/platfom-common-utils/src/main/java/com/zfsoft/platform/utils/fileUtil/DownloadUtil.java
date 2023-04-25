package com.zfsoft.platform.utils.fileUtil;


import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import com.zfsoft.platform.common.data.BaseStaticParameter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URLEncoder;


/**
 * 下载文件帮助类
 *
 * @author wuxx
 * @date 2020-09-14 13:44:34
 *
 */
public class DownloadUtil {

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
    public static void downloadFile(String displayFileName, String filePath,
            String fileName,String fastdfsNginxUrl, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        // 设定编码为UTF-8
        response.setCharacterEncoding("UTF-8");
        // 设置头部为下载信息
        response.setHeader("Content-type", "application/force-download;charset=UTF-8");
        // 文件下载，中文名称乱码，针对不同浏览器的解决
        String header = request.getHeader("User-Agent").toUpperCase();
        if (header.contains("MSIE") || header.contains("TRIDENT") || header.contains("EDGE")) {
            displayFileName = URLEncoder.encode(displayFileName, "utf-8");
            displayFileName = displayFileName.replace("+", "%20");    //IE下载文件名空格变+号问题
        } else {
            displayFileName = URLEncoder.encode(displayFileName, "utf-8");
        }
        response.setHeader("Content-Disposition",
                "attachment;fileName=\"" + displayFileName + "\"");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("multipart/form-data");
        response.setHeader("Access-Control-Allow-Origin","*");
        String savePath = UploadUtil.getRealSavePath(request) + "/";
        if (BaseStaticParameter.UPLOAD_TYPE.UNIFIED_FILES.equals(FileUploadParam.uploadType()) && StrUtil.isNotEmpty(fastdfsNginxUrl)) {
            //解决可能nginx文件中无附件的问题，如果存在附件，直接再nginx中下载
            HttpUtil.download(fastdfsNginxUrl, response.getOutputStream(), true);
        }else {
            // 当为FTP存储时，先将文件下载到项目中，在进行下载
            InputStream inputStream = FileUtil.getInputStreamByUploadType(savePath + filePath, fileName,fastdfsNginxUrl);
            if(null==inputStream){
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                PrintWriter out = response.getWriter();
                String message="文件不存在或已经被删除";
                out.println("{success:false,message:'" + message + "'}");
                out.flush();
                out.close();
                return;
            }
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

    }


    /**
     * 根据FASTDFS上传URL，名称下载文件
     *
     * @param displayFileName
     *            文件名称
     * @param filePath
     *            文件路径
     * @param request
     *            http对象
     * @throws Exception
     */
    public static void downloadUploadUrlFile(String displayFileName, String filePath,
                                    String fileName,String fastdfsUploadUrl, HttpServletRequest request,
                                    HttpServletResponse response) throws Exception {
        // 设定编码为UTF-8
        response.setCharacterEncoding("UTF-8");
        // 设置头部为下载信息
        response.setHeader("Content-type", "application/force-download;charset=UTF-8");
        // 文件下载，中文名称乱码，针对不同浏览器的解决
        String header = request.getHeader("User-Agent").toUpperCase();
        if (header.contains("MSIE") || header.contains("TRIDENT") || header.contains("EDGE")) {
            displayFileName = URLEncoder.encode(displayFileName, "utf-8");
            displayFileName = displayFileName.replace("+", "%20");    //IE下载文件名空格变+号问题
        } else {
            displayFileName = URLEncoder.encode(displayFileName, "utf-8");
        }
        response.setHeader("Content-Disposition",
                "attachment;fileName=\"" + displayFileName + "\"");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("multipart/form-data");
        response.setHeader("Access-Control-Allow-Origin","*");
        String savePath = UploadUtil.getRealSavePath(request) + "/";
        String fastdfsNginxUrl = FileUploadParam.fastDFSNginxUrl+ "/" + fastdfsUploadUrl;
        if (BaseStaticParameter.UPLOAD_TYPE.UNIFIED_FILES.equals(FileUploadParam.uploadType()) && StrUtil.isNotEmpty(fastdfsNginxUrl)) {
            //解决可能nginx文件中无附件的问题，如果存在附件，直接再nginx中下载
            HttpUtil.download(fastdfsNginxUrl, response.getOutputStream(), true);
        }else {
            // 当为FTP存储时，先将文件下载到项目中，在进行下载
            InputStream inputStream = FileUtil.getInputStreamByUploadType(savePath + filePath, fileName,fastdfsNginxUrl);
            if(null==inputStream){
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                PrintWriter out = response.getWriter();
                String message="文件不存在或已经被删除";
                out.println("{success:false,message:'" + message + "'}");
                out.flush();
                out.close();
                return;
            }
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

    }

    /**
     * 根据文件路径，获取文件流
     *
     * @param fileName
     *            文件名称
     * @param filePath
     *            文件路径
     *            http对象
     * @throws Exception
     */
    public static InputStream downloadFile(String fileName, String filePath,String fastDFSUploadUrl, HttpServletRequest request) throws Exception {
        String savePath = UploadUtil.getRealSavePath(request) + "/";
        // 当为FTP存储时，先将文件下载到项目中，在进行下载
        InputStream inputStream = FileUtil.getInputStreamByUploadType(savePath + filePath, fileName,fastDFSUploadUrl);
        return inputStream;
    }

}
