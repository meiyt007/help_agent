package com.zfsoft.microservice.form.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.zfsoft.microservice.form.data.FormAtta;
import com.zfsoft.microservice.form.manager.FormAttaManager;
import com.zfsoft.microservice.form.service.FormAttaService;
import com.zfsoft.microservice.form.util.FileDownUtil;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.data.BaseStaticParameter;
import com.zfsoft.platform.utils.data.SysAttaTemp;
import com.zfsoft.platform.utils.fileUtil.FileUtil;
import com.zfsoft.platform.utils.fileUtil.UploadUtil;
import com.zfsoft.platform.utils.validate.ResultInfoException;
import lombok.Cleanup;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.tika.Tika;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

/**
 * @ClassName FormAttaController
 * @Description 附件管理的实现类
 * @Author wuxx
 * @Date 2020-09-12 10:30
 * @Version V1.0
 **/
@RestController
@Slf4j
@RefreshScope
public class FormAttaController implements FormAttaService {

    @Resource
    private FormAttaManager formAttaManager;

    @Value("${fdfs.fastDFSNginxUrl:''}")
    private String fastDFSNginxUrl;

    /**
     * @param formAtta 附件实体类
     * @description: 附件的新增或者修改
     * @author: wuxx
     * @Date: 2020/9/10 10:14
     **/
    @Override
    public ApiResultSet<FormAtta> saveFormAtta(FormAtta formAtta) {
        formAttaManager.saveFormAtta(formAtta);
        ApiResultSet<FormAtta> apiResultSet = new ApiResultSet<>();
        apiResultSet.setData(formAtta);
        return apiResultSet;
    }

    @Override
    public ApiResultSet<FormAtta> getFormAttaByAttaOid(String attaOid) {
        if(StrUtil.isEmpty(attaOid)){
            return  new ApiResultSet<>();
        }
        FormAtta atta = formAttaManager.getFormAttaByAttaOid(attaOid);
        return new ApiResultSet<>(atta);
    }

    /**
     * @param oid 附件实体类主键
     * @description: 附件的信息的删除-只删除数据，不删除文件
     * @author: wuxx
     * @Date: 2020/9/10 10:14
     **/
    @Override
    public ApiResultSet<Integer> deleteFormAttaById(Long oid) {
        int rows = formAttaManager.deleteFormAttaById(oid);
        ApiResultSet<Integer> apiResultSet = new ApiResultSet<Integer>();
        apiResultSet.setData(rows);
        return apiResultSet;
    }

    /**
     * @param oid 附件实体类主键
     * @description: 获取附件的信息
     * @author: wuxx
     * @Date: 2020/9/10 10:14
     **/
    @Override
    public ApiResultSet<FormAtta> getFormAttaById(Long oid) {
        FormAtta formAtta = formAttaManager.getFormAttaById(oid);
        ApiResultSet<FormAtta> apiResultSet = new ApiResultSet<FormAtta>();
        apiResultSet.setData(formAtta);
        return apiResultSet;
    }
    /**
     * @description:  根据id 集合 获取 atta信息
     * @param attaOidList id主键集合
     * @author: wuxx
     * @Date: 2020/10/28 17:28
     **/
    @Override
    public ApiResultSet getAttaListByOidList(List<String> attaOidList) {
        List<FormAtta> formAttaList = formAttaManager.getAttaListByOidList(attaOidList);
        return new ApiResultSet(formAttaList);
    }

    /**
     * @param name 上传文件名称插件自带
     * @param file 文件
     * @description: 上传图片
     * @author: wuxx
     * @Date: 2020/10/29 11:33
     **/
    @PostMapping(value = "/uploadImage", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResultSet uploadImage(HttpServletRequest request, @RequestPart(value = "file") MultipartFile file, @RequestParam(value = "userOid", required = false) String userOid,
                                    @RequestParam(value = "name", required = false) String name){
        try {
            JSONObject jsonObject = formAttaManager.uploadImage(request,file,userOid,name);
            return new ApiResultSet<>(jsonObject);
        }catch (Exception e){
            throw new ResultInfoException(e.getMessage());
        }
    }


    /**
     * @Description:  在线预览图片
     * @Author: wuxx
     * @Date: 2021/5/26 15:55
     * @Return: java.util.Map<java.lang.String,java.lang.Object>
     **/
    @RequestMapping(value = {"/imageDisplay/{attaOid}"},method = {RequestMethod.GET})
    @ResponseBody
    public void imageDisplay(HttpServletResponse response, HttpServletRequest request, @PathVariable("attaOid") String attaOid) {
        try {
            FormAtta atta = formAttaManager.getFormAttaByAttaOid(attaOid);
            @Cleanup InputStream inputStream = null;
            try {
                if(StrUtil.isNotEmpty(atta.getFastdfsUploadUrl())){
                    //解决可能nginx文件中无附件的问题，如果存在附件，直接再nginx中下载
                    String fastdfsNginxUrl = fastDFSNginxUrl + "/"+atta.getFastdfsUploadUrl();
                    inputStream = FileDownUtil.getFileInputStream(fastdfsNginxUrl);
                }else{
                    inputStream = formAttaManager.getFileInputStream(request, attaOid);
                }
            }catch (Exception e){
                inputStream = formAttaManager.getFileInputStream(request, attaOid);
            }
            // 20200911 modidy by kkfan
            Tika tika = new Tika();
            String mimeType = tika.detect(inputStream);
            byte[] streamBytes = FileUtil.getStreamBytes(inputStream);
            if (streamBytes != null) {
                response.setContentLength(streamBytes.length);
                response.setHeader("Content-Disposition", "attachment;filename=" + atta.getName());
                response.setContentType(StringUtils.endsWith(atta.getName(), ".mp4") ? "video/mp4" : mimeType);
                @Cleanup OutputStream outputStream = response.getOutputStream();
                outputStream.write(streamBytes);
                outputStream.flush();
            }
        }catch (Exception e){
             throw new ResultInfoException("预览图片失败！");
        }

    }

}
