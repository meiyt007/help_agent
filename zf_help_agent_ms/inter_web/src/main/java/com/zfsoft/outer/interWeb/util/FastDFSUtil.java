package com.zfsoft.outer.interWeb.util;

import cn.hutool.core.util.StrUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.domain.proto.storage.DownloadByteArray;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;

/**
 * FastDFSUtil 文件处理工具类
 *
 * @author wuxx
 * @date 20200715
 */
@Repository("fastDFSUtil")
@Component
public class FastDFSUtil {

    static Log LOGGER = LogFactory.get();

    private static  FastFileStorageClient storageClient;

    @Autowired
    public void setStorageClient(FastFileStorageClient component) {
        FastDFSUtil.storageClient = component;
    }

    /**
     * 上传文件
     * @param is 文件流对象
     * @param fileType 文件名后缀
     * @return
     */
    public static String uploadFile(InputStream is, String fileType) {
        String fullPath=null;
        try {
            //上传文件
            StorePath storePath = storageClient.uploadFile(is,is.available(), fileType, null);
            //带分组的路径
            fullPath = storePath.getFullPath();
            //不带分组的路径
            //System.err.println(storePath.getPath());
        } catch (Exception e) {
            LOGGER.error(e);
        }
        return fullPath;
    }

    /**
     * 上传缩略图文件适合图片
     * @param is 文件流对象
     * @param fileType 文件名后缀
     * @return
     */
    public static String uploadImageAndCrtThumbImage(InputStream is, String fileType) {
        String fullPath=null;
        try {
            //上传文件
            StorePath storePath = storageClient.uploadImageAndCrtThumbImage(is,is.available(), fileType, null);
            //带分组的路径
            fullPath = storePath.getFullPath();
        } catch (Exception e) {
            LOGGER.error(e);
        }
        return fullPath;
    }

    /*
     * @description 文件下载到 response 中
     * @author wuxx
     * @date 20200715
     * @return
     */
    public static void downloadFile(String fileName, String path, HttpServletResponse response) {
        // 重置buffer
        response.resetBuffer();
        // 设定编码为UTF-8
        response.setCharacterEncoding("UTF-8");
        // 设置头部为下载信息
        response.setHeader("Content-type", "application/force-download;charset=UTF-8");
        /*response.setHeader("content-disposition",
                "attachment;filename=" + StringUtils.substringAfter(fileName, "UTF-8"));// 设置文件名*/
        try {
            response.setHeader("content-disposition", "attachment;filename="
                    + URLEncoder.encode(fileName, "UTF-8"));
        }catch (Exception e){
        }
        OutputStream outputStream = null;
        try {
            outputStream = response.getOutputStream();
            //文件下载
            //分组默认1
            String groupName = "group1";
            String dataPath =null;
            if(StrUtil.isNotEmpty(path)){
                groupName = path.substring(0,path.indexOf("/"));
                dataPath = path.substring(path.indexOf("/"));
            }
            byte[] content = storageClient.downloadFile(groupName, dataPath, new DownloadByteArray());
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(content);
            IOUtils.copy(byteArrayInputStream, outputStream);
        } catch (Exception e) {
            response.setStatus(500);
            LOGGER.error(e);
            response.setStatus(response.SC_INTERNAL_SERVER_ERROR);
        }
    }

    /* *
     * @Description: 获取fastDFS文件流
     * @Param: [fileId, key, encrypt]
     * @Author: wuxx
     * @Date: 2020/7/15 10:31
     * @Return: java.io.InputStream
     **/
    public static InputStream getFastDFSFileStream(String path){
        if (path != null) {
            String groupName = "group1";
            String dataPath =null;
            if(StrUtil.isNotEmpty(path)){
                groupName = path.substring(0,path.indexOf("/"));
                dataPath = path.substring(path.indexOf("/")+1);
            }
            byte[] content = storageClient.downloadFile(groupName, dataPath, new DownloadByteArray());
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(content);
            return  byteArrayInputStream;
        }
        return null;
    }

    /**
     * 删除文件
     * @param filePath 文件返回的路径filePath  如;group2/M00/00/12/rKj83V8Oa--AJu2NABOPRdc8J44282.jpg
     */
    public static void deleteFile(String filePath) {
        try {
            storageClient.deleteFile(filePath);
        } catch (Exception e) {
            LOGGER.error(e);
        }
    }
}
