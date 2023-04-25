package com.zfsoft.microservice.platform.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.zfsoft.microservice.platform.data.sys.SysAtta;
import com.zfsoft.microservice.platform.manager.sys.SysAttaManager;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.data.BaseStaticParameter;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.platform.common.security.data.CurrentLoginUser;
import com.zfsoft.platform.common.security.data.CurrentLoginUserHolder;
import com.zfsoft.platform.utils.data.SysAttaTemp;
import com.zfsoft.platform.utils.fileUtil.FileUtil;
import com.zfsoft.platform.utils.fileUtil.SslUtils;
import com.zfsoft.platform.utils.fileUtil.UploadUtil;
import com.zfsoft.platform.utils.validate.ResultInfoException;
import lombok.Cleanup;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import net.sf.jmimemagic.Magic;
import net.sf.jmimemagic.MagicMatch;
import org.springframework.beans.BeanUtils;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.net.ssl.HttpsURLConnection;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;

/**
 * @ClassName FileController
 * @Description: 文件上传、下载控制层
 * @Author wuxx
 * @Date 2020/9/14
 **/
@RestController
@Slf4j
@RequestMapping("/security/atta")
public class FileController{

    @Resource
    private SysAttaManager sysAttaManager;

    /**
     * @description:  上传文件
     * @param name 上传文件名称
     * @param file 文件
     * @author: wuxx
     * @Date: 2020/9/14 16:33
     **/
    @PostMapping(value = "/uploadFile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResultSet uploadFile(HttpServletRequest request,  MultipartFile file,String name){
        try {
            String userOid = CurrentLoginUserHolder.getCurrentLoginUser().getUserOid();
            if(StrUtil.isNotEmpty(name)){
                file = new MockMultipartFile("File",name,"text/plain", file.getInputStream());
            }
            //HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            UploadUtil uploadUtil = new UploadUtil(request);
            String filePath = uploadUtil.uploadFile(file);
            //SysAtta atta = uploadUtil.getSysAttaFile(filePath, userOid);
            SysAttaTemp sysAttaTemp = uploadUtil.getSysAttaFile(filePath, userOid);
            SysAtta atta = new SysAtta();
            BeanUtils.copyProperties(sysAttaTemp, atta);
            //保存附件信息
            sysAttaManager.saveSysAtta(atta);
            JSONObject jsonObject = JSONUtil.createObj().set("oid", atta.getAttaOid()).set("url", atta.getFastdfsNginxUrl()).set("name", atta.getOriginName()).set("size",atta.getFileSize());
            return new ApiResultSet<>(jsonObject);
        }catch (Exception e){
            throw new ResultInfoException(e.getMessage());
        }
    }


    /**
     * @param name        上传文件名称插件自带
     * @param file        文件
     * @description: 上传图片
     * @author: wuxx
     * @Date: 2020/10/29 11:33
     **/
    @PostMapping(value = "/uploadImage", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResultSet uploadImage(HttpServletRequest request, @RequestPart(value = "file") MultipartFile file, @RequestParam(value = "userOid", required = false) String userOid,
                                    @RequestParam(value = "name", required = false) String name){
        try {
            if (StrUtil.isNotEmpty(name)) {
                String formatName = "png";
                if (StrUtil.isNotEmpty(name)) {
                    formatName = name.substring(name.lastIndexOf(".") + 1, name.length());
                }
                if (!BaseStaticParameter.ATTA_IMAGE_EXT_SET.contains(formatName.toLowerCase())) {
                    throw new ResultInfoException("请上传gif、jpg、jpeg、png或bmp格式的文件！");
                }
                file = new MockMultipartFile("File", name, "text/plain", file.getInputStream());
            }
            UploadUtil uploadUtil = new UploadUtil(request);
            String filePath = uploadUtil.uploadFile(file);
            //SysAtta atta = uploadUtil.getSysAttaFile(filePath, userOid);
            SysAttaTemp sysAttaTemp = uploadUtil.getSysAttaFile(filePath, userOid);
            SysAtta atta = new SysAtta();
            BeanUtils.copyProperties(sysAttaTemp, atta);
            //保存附件信息
            sysAttaManager.saveSysAtta(atta);
            JSONObject jsonObject = JSONUtil.createObj().set("oid", atta.getAttaOid()).set("url", atta.getFastdfsNginxUrl()).set("name", atta.getOriginName()).set("size",atta.getFileSize());
            return new ApiResultSet<>(jsonObject);
        }catch (Exception e){
            throw new ResultInfoException(e.getMessage());
        }
    }


    /**
     * @description:  上传压缩图或者缩略图功能
     * @param name 上传文件名称插件自带
     * @param file 文件
     * @param imageWidth  缩略图宽度
     * @param imageHeight 缩略图高度
     * @author: wuxx
     * @Date: 2020/9/14 16:33
     **/
    @PostMapping(value = "/uploadCompressImage", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResultSet uploadCompressImage(HttpServletRequest request,MultipartFile file, String name
            ,@RequestParam(value = "imageWidth", required = false) Integer imageWidth
            ,@RequestParam(value = "imageHeight", required = false) Integer imageHeight){
        try {
            String userOid = CurrentLoginUserHolder.getCurrentLoginUser().getUserOid();
            CurrentLoginUserHolder.getCurrentLoginUser().getUserOid();
            if(null != file.getOriginalFilename()){
                name = file.getOriginalFilename();
            }
            if(StrUtil.isNotEmpty(name)){
                String formatName = name.substring(name.lastIndexOf(".")+1,name.length());
                if (!BaseStaticParameter.ATTA_IMAGE_EXT_SET.contains(formatName.toLowerCase())) {
                    throw new ResultInfoException("请上传gif、jpg、jpeg、png或bmp格式的文件！");
                }
                //压缩一倍
                BufferedImage bufferedImage = Thumbnails.of(file.getInputStream()).scale(0.5f).outputQuality(1f).asBufferedImage();
                if (!BaseStaticParameter.ATTA_IMAGE_EXT_SET.contains(formatName.toLowerCase())) {
                    throw new ResultInfoException("请上传gif、jpg、jpeg、png或bmp格式的文件！");
                }
                if(null!=imageWidth && null!=imageHeight){
                    bufferedImage = Thumbnails.of(file.getInputStream()).size(imageWidth,imageHeight).keepAspectRatio(false).asBufferedImage();
                }
                file = new MockMultipartFile("File",name,"text/plain", UploadUtil.bufferedImageToInputStream(bufferedImage,formatName));
            }
            UploadUtil uploadUtil = new UploadUtil(request);
            String filePath = uploadUtil.uploadFile(file);
            //SysAtta atta = uploadUtil.getSysAttaFile(filePath, userOid);
            SysAttaTemp sysAttaTemp = uploadUtil.getSysAttaFile(filePath, userOid);
            SysAtta atta = new SysAtta();
            BeanUtils.copyProperties(sysAttaTemp, atta);
            //保存附件信息
            sysAttaManager.saveSysAtta(atta);
            JSONObject jsonObject = JSONUtil.createObj().set("oid", atta.getAttaOid()).set("url", atta.getFastdfsNginxUrl()).set("name", atta.getOriginName()).set("size",atta.getFileSize());
            return new ApiResultSet<>(jsonObject);
        }catch (Exception e){
            throw new ResultInfoException(e.getMessage());
        }
    }

    /**
     * @description:  文件下载
     * @param attaOid 文件的oid
     * @author: wuxx
     * @Date: 2020/9/14 14:22
     **/
    @RequestMapping(value = {"/downloadFile/{attaOid}"},method = {RequestMethod.GET})
    @ResponseBody
    public void downloadFile(HttpServletResponse response, HttpServletRequest request, @PathVariable("attaOid") String attaOid) {
        sysAttaManager.downloadFile(response, request, attaOid);
    }

    /**
     * @Description:  在线预览图片
     * @Author: wuxx
     * @Date: 2020/9/14 11:55
     * @Return: java.util.Map<java.lang.String,java.lang.Object>
     **/
    @RequestMapping(value = {"/imageDisplay/{attaOid}"},method = {RequestMethod.GET})
    @ResponseBody
    public void imageDisplay(HttpServletResponse response, HttpServletRequest request, @PathVariable("attaOid") String attaOid) {
        try {
            SysAtta atta = sysAttaManager.getSysAttaByAttaOid(attaOid);
            @Cleanup InputStream inputStream = null;
            try {
                if(StrUtil.isNotEmpty(atta.getFastdfsNginxUrl())){
                    //解决可能nginx文件中无附件的问题，如果存在附件，直接再nginx中下载
                    inputStream = getFileInputStream(atta.getFastdfsNginxUrl());
                }else{
                    inputStream = sysAttaManager.getFileInputStream(request, attaOid);
                }
            }catch (Exception e){
                inputStream = sysAttaManager.getFileInputStream(request, attaOid);
            }

            byte[] streamBytes = FileUtil.getStreamBytes(inputStream);
            MagicMatch match = Magic.getMagicMatch(streamBytes);
            String mimeType = match.getMimeType();
            if (streamBytes != null) {
                response.setContentLength(streamBytes.length);
                response.setHeader("Content-Disposition", "attachment;filename=" + atta.getName());
                response.setContentType(mimeType);
                @Cleanup OutputStream outputStream = response.getOutputStream();
                outputStream.write(streamBytes);
                outputStream.flush();
            }
        }catch (Exception e){
           // throw new ResultInfoException("预览图片失败！");
        }

    }

    /*读取网络文件*/
    public static InputStream getFileInputStream(String path) {
        URL url = null;
        try {
            url = new URL(path);
            URLConnection conn = null;
            if(path.startsWith("https")) {
                SslUtils.ignoreSsl();
                conn = (HttpsURLConnection)url.openConnection();
            } else {
                conn = (HttpURLConnection)url.openConnection();
            }
            //设置超时间为3秒
            conn.setConnectTimeout(3*1000);
            //防止屏蔽程序抓取而返回403错误
            conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
            //得到输入流
            return conn.getInputStream();
        } catch (Exception e) {
            log.error("读取网络文件异常:"+path);
        }
        return null;
    }

    /**
     * @description:  根据id 集合 获取 atta信息
     * @param attaOids id主键集合
     * @author: wuxx
     * @Date: 2020/9/14 18:28
     **/
    @RequestMapping( value = "/getAttaListByOids",method = {RequestMethod.GET})
    public ApiResultSet getAttaListByOids(String attaOids) {
        if(StrUtil.isEmpty(attaOids)){
            return new ApiResultSet<>();
        }
        JSONArray jsonArray = sysAttaManager.getAttaListByOids(Arrays.asList(attaOids.split(",")));
        return new ApiResultSet<>(jsonArray);
    }

    /**
     * @param attaOid 附件实体类业务主键
     * @description: 附件的信息的删除，删除数据以及删除文件
     * @author: wuxx
     * @Date: 2020/9/14 18:14
     **/
    @RequestMapping(value = "/deleteFile/{attaOid}", method = {RequestMethod.POST})
    public ApiResultSet<Integer> deleteFileByAttaOid(@PathVariable("attaOid") String attaOid) {
        int rows = sysAttaManager.deleteFileByAttaOid(attaOid);
        ApiResultSet<Integer> apiResultSet = new ApiResultSet<Integer>();
        apiResultSet.setData(rows);
        return apiResultSet;
    }


    /**
     * @description:  上传注册授权文件
     * @param name 上传注册授权文件名称
     * @param file 文件
     * @author: wuxx
     * @Date: 2020/11/4 16:33
     **/
    @PostMapping(value = "/uploadRegisterFile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResultSet uploadRegisterFile(HttpServletRequest request,  MultipartFile file,String name){
        try {
            if(StrUtil.isNotEmpty(name)){
                file = new MockMultipartFile("File",name,"text/plain", file.getInputStream());
            }
            if(!file.getOriginalFilename().endsWith(".data")){
                throw new ResultInfoException("请上传正确的注册文件！");
            }
            UploadUtil uploadUtil = new UploadUtil(request);
            String filePath = uploadUtil.uploadFile(file);
            //SysAtta atta = uploadUtil.getSysAttaFile(filePath, null);
            SysAttaTemp sysAttaTemp = uploadUtil.getSysAttaFile(filePath, null);
            SysAtta atta = new SysAtta();
            BeanUtils.copyProperties(sysAttaTemp, atta);
            //保存附件信息
            sysAttaManager.saveSysAtta(atta);
            JSONObject jsonObject = JSONUtil.createObj().set("oid", atta.getAttaOid()).set("url", atta.getFastdfsNginxUrl()).set("name", atta.getOriginName()).set("size",atta.getFileSize());
            return new ApiResultSet<>(jsonObject);
        }catch (Exception e){
            throw new ResultInfoException(e.getMessage());
        }
    }


    /**
     * @description:  上传文件--无需登录
     * @param name 上传文件名称
     * @param file 文件
     * @author: wuxx
     * @Date: 2020/11/23 13:33
     **/
    @PostMapping(value = "/nologin/uploadFile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResultSet uploadNoLoginFile(HttpServletRequest request,  MultipartFile file,String name){
        try {
            String userOid = CurrentLoginUserHolder.getCurrentLoginUser().getUserOid();
            if(StrUtil.isNotEmpty(name)){
                file = new MockMultipartFile("File",name,"text/plain", file.getInputStream());
            }
            //HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            UploadUtil uploadUtil = new UploadUtil(request);
            String filePath = uploadUtil.uploadFile(file);
            //SysAtta atta = uploadUtil.getSysAttaFile(filePath, userOid);
            SysAttaTemp sysAttaTemp = uploadUtil.getSysAttaFile(filePath, userOid);
            SysAtta atta = new SysAtta();
            BeanUtils.copyProperties(sysAttaTemp, atta);
            //保存附件信息
            sysAttaManager.saveSysAtta(atta);
            JSONObject jsonObject = JSONUtil.createObj().set("oid", atta.getAttaOid()).set("url", atta.getFastdfsNginxUrl()).set("name", atta.getOriginName()).set("size",atta.getFileSize());
            return new ApiResultSet<>(jsonObject);
        }catch (Exception e){
            throw new ResultInfoException(e.getMessage());
        }
    }

    /**
     * @description:  根据id 查询单个附件 --无需登录
     * @param attaOid id主键集合
     * @author: wuxx
     * @Date: 2020/11/23 13:28
     **/
    @RequestMapping( value = "/nologin/getByAttaOid",method = {RequestMethod.GET})
    public ApiResultSet<SysAtta> getNoLoginAttaListByOid(String attaOid) {
        if(StrUtil.isEmpty(attaOid)){
            return new ApiResultSet<>();
        }
        SysAtta atta = sysAttaManager.getSysAttaByAttaOid(attaOid);
        return new ApiResultSet<>(atta);
    }

    /**
     * @param sysAtta 附件实体类 --无需登录
     * @description: 查询附件的信息列表
     * @author: wuxx
     * @Date: 2020/11/23 13:14
     **/
    @RequestMapping(value = "/nologin/page", method = {RequestMethod.GET})
    public ApiResultSet<PageResult<SysAtta>> querySysAttaWithPage(SysAtta sysAtta, Integer pageNum,
                                                                  Integer pageSize) {
        PageResult<SysAtta> pageResult = sysAttaManager.querySysAttaWithPage(sysAtta, pageNum, pageSize);
        return  new ApiResultSet<>(pageResult);
    }

    /**
     * @description: 根据附件attaOid获取图片的宽度和高度
     * @param attaOid 附件attaOid
     * @author: wuxx
     * @Date: 2020/11/26 10:18
     **/
    @RequestMapping(value = "/nologin/getImageWidthAndHeightByAttaOid/{attaOid}", method = {RequestMethod.GET})
    public ApiResultSet<String> getImageWidthAndHeightByAttaOid(@PathVariable("attaOid") String attaOid){
        String imageJson = null;
        try {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            SysAtta atta = sysAttaManager.getSysAttaByAttaOid(attaOid);
            InputStream inputStream = null;
            try {
                if(StrUtil.isNotEmpty(atta.getFastdfsNginxUrl())){
                    //解决可能nginx文件中无附件的问题，如果存在附件，直接再nginx中下载
                    inputStream = new URL(atta.getFastdfsNginxUrl()).openStream();
                }else{
                    inputStream = sysAttaManager.getFileInputStream(request, attaOid);
                }
            }catch (Exception e){
                inputStream = sysAttaManager.getFileInputStream(request, attaOid);
            }
            BufferedImage sourceImg = ImageIO.read(inputStream);
            //System.out.println(sourceImg.getWidth());   // 源图宽度
            //System.out.println(sourceImg.getHeight());   // 源图高度
            JSONObject jsonObject = JSONUtil.createObj().set("attaOid", attaOid).set("width",sourceImg.getWidth()).set("height",sourceImg.getHeight()).set("size",atta.getFileSize());
            inputStream.close(); //关闭Stream
            return new ApiResultSet(jsonObject.toString());
        }catch (Exception e){
            ApiResultSet apiResultSet = new ApiResultSet();
            apiResultSet.setCode(ApiResultSet.PARAM_VALID_ERROR);
            apiResultSet.setMessage("当前文件非图片类型！");
            return apiResultSet;
        }
    }

}
