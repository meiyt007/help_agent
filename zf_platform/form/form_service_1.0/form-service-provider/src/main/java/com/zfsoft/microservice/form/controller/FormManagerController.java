package com.zfsoft.microservice.form.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.google.common.collect.Maps;
import com.zfsoft.microservice.form.data.*;
import com.zfsoft.microservice.form.data.vo.ExportFormVo;
import com.zfsoft.microservice.form.data.vo.FormDataVo;
import com.zfsoft.microservice.form.data.vo.FormMainVo;
import com.zfsoft.microservice.form.data.vo.MergeFormVo;
import com.zfsoft.microservice.form.manager.*;
import com.zfsoft.microservice.form.service.FormManagerService;
import com.zfsoft.microservice.form.util.Assert;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.security.data.CurrentLoginUserHolder;
import com.zfsoft.platform.utils.data.SysAttaTemp;
import com.zfsoft.platform.utils.fileUtil.DownloadUtil;
import com.zfsoft.platform.utils.fileUtil.FileUtil;
import com.zfsoft.platform.utils.fileUtil.UploadUtil;
import com.zfsoft.platform.utils.validate.ResultInfoException;
import lombok.Cleanup;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.apache.tika.Tika;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.*;
import java.util.stream.Collectors;


/**
 * @description: 电子表单管理服务接口实现类
 * @author: wuxx
 * @Date: 2021/4/22 13:07
 **/
@RestController
@RefreshScope
@Slf4j
public class FormManagerController implements FormManagerService {

    @Resource
    private FormMainManager formMainManager;

    @Resource
    private FormObjectManager formObjectManager;

    @Resource
    private FormManagerManager formManagerManager;

    @Resource
    private FormComponentManager formComponentManager;

    @Resource
    private FormModuleManager formModuleManager;

    @Resource
    private FormColumnManager formColumnManager;

    @Resource
    private FormDesignManager formDesignManager;

    @Resource
    private FormAttaManager formAttaManager;

    @Value("${fdfs.fastDFSNginxUrl:''}")
    private String fastDFSNginxUrl;

    @GetMapping("/checkToken")
    public ApiResultSet checkToken(@RequestParam(required = false) String token,
                                   @RequestParam(required = false) String info) {
//        System.out.println(token);
        Map<String, Object> resObj = Maps.newHashMap();
        resObj.put("token",token);
        resObj.put("info",info);
        return new ApiResultSet(resObj);
    }

    /**
     * 测试接口验证
     * @author kkfan
     * @param map 整个表单已填写输入数据
     * @return 结果需包含 status 若状态为false 则需返回错误信息 message
     */
    @PostMapping("/validerObj")
    public ApiResultSet validerObj(@RequestBody Map<String, Object> map) {
//        System.out.println(map);
        Map<String, Object> resObj = Maps.newHashMap();
        if(StringUtils.equals(map.get("name").toString(), "张三")) {
            resObj.put("status", true);
        } else {
            resObj.put("status", false);
        }
        resObj.put("message", "验证码校验失败！");
        return new ApiResultSet(resObj);
    }


    /**
     * @description:  获取表单设计对象的信息
     * @param formMainOid 表单设计对象业务oid
     * @author: wuxx
     * @Date: 2021/4/22 13:07
     **/
    @Override
    public ApiResultSet<FormMain> getFormMainByFormMainOid(String formMainOid) {
        if("{formMainOid}".equals(formMainOid)){
            throw new ResultInfoException("参数formMainOid不能为空!");
        }
        FormMain formMain = formMainManager.getFormMainByFormMainOid(formMainOid);
        return new ApiResultSet<>(formMain);
    }

    /**
     * @description:  获取表单设计对象的信息
     * @param formMainOid 表单主键oid
     * @param designOid 表单设计对象业务oid
     * @author: wuxx
     * @Date: 2021/4/22 13:07
     **/
    @Override
    public ApiResultSet<FormDesign> getFormDesign(String authorizeKey,String formMainOid, String designOid,String formCode) {
        if(StrUtil.isBlank(authorizeKey)){
            throw new ResultInfoException("参数authorizeKey不能为空!");
        }
        if(StrUtil.isBlank(formMainOid) && StrUtil.isBlank(designOid) && StrUtil.isBlank(formCode)){
            throw new ResultInfoException("设计对象信息不能为空!");
        }
        FormDesign design = null;
        if(StrUtil.isNotBlank(designOid)){
             design = formDesignManager.getFormDesignByDesignOid(designOid);
        }else if(StrUtil.isNotBlank(formCode)){
            FormMain mainCode = formMainManager.getFormMainByFormMainCode(formCode);
            if(null == mainCode){
                throw new ResultInfoException("未查询到设计对象信息!");
            }
           // design = formDesignManager.getFormDesignByDesignOid(mainCode.getDesignOid());
            design = formDesignManager.getFormDesignByFormMainOid(mainCode.getFormMainOid());
        }else {
            design = formDesignManager.getFormDesignByFormMainOid(formMainOid);
        }
        return new ApiResultSet<>(design);
    }

    @Override
    public ApiResultSet<FormDesign> getFormDesignByFormMainOid(String formMainOid) {
        FormDesign design = formDesignManager.getFormDesignByFormMainOid(formMainOid);
        return new ApiResultSet<>(design);
    }

    @Override
    public ApiResultSet<FormDesign> getFormDesignByFormCode(String formCode) {
        FormMain main = formMainManager.getFormMainByFormMainCode(formCode);
        if(null == main){
            throw new ResultInfoException("未查询到设计对象信息!");
        }
        FormDesign design = formDesignManager.getFormDesignByFormMainOid(main.getFormMainOid());
        return new ApiResultSet<>(design);
    }

    /**
     * @description:  获取表单设计对象的组件配置信息
     * @param formMainOid 表单主键oid
     * @param designOid 表单设计对象业务oid
     * @param components 设计的组件name 多个用逗号,隔开
     * @author: wuxx
     * @Date: 2021/5/6 13:07
     **/
    @Override
    public ApiResultSet<String> getFormDesignConfigList(String authorizeKey,String formMainOid, String designOid,String components) {
        String configList = formDesignManager.getFormDesignConfigList(formMainOid, designOid, components);
        return new ApiResultSet<>(configList);
    }

    /**
     * @description:  保存设计的config
     * @param formDesign 设计对象
     * @author: wuxx
     * @Date: 2021/4/22 13:07
     **/
    @Override
    public ApiResultSet<FormDesign> saveFormDesign(FormDesign formDesign) {
        formDesignManager.saveFormDesign(formDesign);
        //表单的发布
        if(null!=formDesign.getIsPublish() && formDesign.getIsPublish()){
            formMainManager.deployFormMain(formDesign.getFormMainOid());
        }
        return new ApiResultSet<>(formDesign);
    }

    /**
     * @description:  获取表单设计对象的信息
     * @param formCode 表单设计对象编码
     * @author: wuxx
     * @Date: 2021/4/22 13:07
     **/
    @Override
    public ApiResultSet<FormMain> getFormMainByFormCode(String formCode) {
        if(StrUtil.isBlank(formCode)){
            throw new ResultInfoException("参数formCode不能为空!");
        }
        FormMain formMain = formMainManager.getFormMainByFormMainCode(formCode);
        return new ApiResultSet<>(formMain);
    }

    @Override
    public ApiResultSet<List<FormMainVo>> getFormMainByAuthorizeKey(String authorizeKey,Integer formStatus) {
        if(StrUtil.isBlank(authorizeKey)){
            throw new ResultInfoException("参数authorizeKey不能为空!");
        }
        List<FormMainVo> formMainList = formMainManager.queryFormMainListByAuthorizeKey(authorizeKey,formStatus);
        return new ApiResultSet<>(formMainList);
    }

    /**
     * @description:  根据授权key获取表单设计Vo记录
     * @param authorizeKey 授权key
     * @param moduleOid 模块oid
     * @author: wuxx
     * @Date: 2021/4/22 13:07
     **/
    @Override
    public ApiResultSet<List<FormMainVo>> getFormMainVoByModuleOid(String authorizeKey, String moduleOid) {
        if(StrUtil.isBlank(authorizeKey)){
            throw new ResultInfoException("参数authorizeKey不能为空!");
        }
        if(StrUtil.isBlank(moduleOid)){
            throw new ResultInfoException("参数moduleOid不能为空!");
        }
        FormMain formMain = new FormMain();
        formMain.setModuleOid(moduleOid);
        formMain.setAuthorizeKey(authorizeKey);
        List<FormMainVo> formMainList = formMainManager.queryFormMainVoList(formMain);
        return new ApiResultSet<>(formMainList);
    }

    /**
     * @description:  获取模块的信息集合
     * @param authorizeKey 模块实体类授权key
     * @author: wuxx
     * @Date: 2021/04/2 10:14
     **/
    @Override
    public ApiResultSet<List<FormModule>> queryFormModuleList(String authorizeKey) {
        FormModule formModule = new FormModule();
        formModule.setAuthorizeKey(authorizeKey);
        List<FormModule> formModuleList = formModuleManager.queryFormModuleList(formModule);
        return new ApiResultSet<>(formModuleList);
    }

    /**
     * @description:  根据授权key查询存储对象下级列表
     * @param authorizeKey 授权key
     * @param moduleOid 模块oid
     * @author: wuxx
     * @Date: 2021/04/13 16:14
     **/
    @Override
    public ApiResultSet<List<FormObject>> queryFormObjectList(String authorizeKey, String moduleOid) {
        FormObject formObject = new FormObject();
        formObject.setAuthorizeKey(authorizeKey);
        formObject.setModuleOid(moduleOid);
        List<FormObject> formObjectList = formObjectManager.queryFormObjectList(formObject);
        return new ApiResultSet<>(formObjectList);
    }

    /**
     * @description:  根据对象主键查询表结构列表
     * @param objectOid 对象主键
     * @param authorizeKey 授权主键
     * @author: wuxx
     * @Date: 2021/04/13 16:14
     **/
    @Override
    public ApiResultSet<List<FormColumn>> queryFormColumnList(String authorizeKey, String objectOid) {
        FormObject object = formObjectManager.getFormObjectByObjectOid(objectOid);
        FormColumn formColumn = new FormColumn();
        formColumn.setObjectOid(objectOid);
        formColumn.setDatasourceOid(object.getDatasourceOid());
        List<FormColumn> formColumnList = formColumnManager.queryFormColumnList(formColumn);
        return new ApiResultSet<>(formColumnList);
    }

    /**
     * @description:  根据表单编码查询表属性列表
     * @param formCode 表单编码
     * @param authorizeKey 授权主键
     * @author: wuxx
     * @Date: 2021/07/20 14:14
     **/
    @Override
    public ApiResultSet<List<String>> queryFormColumnListByFormCode(String authorizeKey, String formCode) {
        if(StrUtil.isBlank(authorizeKey)){
            throw new ResultInfoException("参数authorizeKey不能为空！");
        }
        if(StrUtil.isBlank(formCode)){
            throw new ResultInfoException("参数formCode不能为空！");
        }
        FormMain main = formMainManager.getFormMainByFormMainCode(formCode);
        if(null==main){
            throw new ResultInfoException("参数formCode错误！");
        }
        if(!authorizeKey.equals(main.getAuthorizeKey())){
            throw new ResultInfoException("参数authorizeKey错误！");
        }
        FormColumn formColumn = new FormColumn();
        formColumn.setObjectOid(main.getObjectOid());
        List<FormColumn> formColumnList = formColumnManager.queryFormColumnList(formColumn);
        List<String> objectPropertyList = formColumnList.stream().map(FormColumn::getObjectProperty)
                .collect(Collectors.toList());
        return new ApiResultSet<>(objectPropertyList);
    }

    /**
     * @description:  存储对象的数据的保存本地、数据库、接口返回
     * @param formDataVo authorizeKey 授权key
     * @param formDataVo formMainOid 设计主表的业务主键
     * @param formDataVo designOid 设计表的业务主键
     * @param formDataVo formData 存储对象数据的JSON
     * @author: wuxx
     * @Date: 2021/4/22 13:07
     **/
    @Override
    public ApiResultSet saveFormData(FormDataVo formDataVo) {
        Map<String, Object> formData = formManagerManager.saveObject(formDataVo);
        return new ApiResultSet<>(com.alibaba.fastjson.JSONObject.toJSONString(formData, SerializerFeature.WriteMapNullValue));
    }

    /**
     * @description: 根据设计主键和表单数据主键获取数据
     * @param designOid 设计主键
     * @param reportOid 表单数据的主键 （API方式可以为null）
     * @author: wuxx
     * @Date: 2021/4/23 9:43
     **/
    @Override
    public ApiResultSet<FormDataVo> getFormData(String authorizeKey,String designOid, String reportOid) {
        if(StrUtil.isBlank(authorizeKey)){
            throw new ResultInfoException("参数authorizeKey不能为空！");
        }
        if(StrUtil.isBlank(designOid)){
            throw new ResultInfoException("参数designOid不能为空！");
        }
        FormDataVo formData = formManagerManager.getFormData(authorizeKey,designOid, reportOid,true);
        return new ApiResultSet<>(formData);
    }

    /**
     * @description: 根据设计主键和表单数据主键获取数据--接口使用
     * @param designOid 设计主键
     * @param reportOid 表单数据的主键 （API方式可以为null）
     * @author: wuxx
     * @Date: 2021/4/23 9:43
     **/
    @Override
    public ApiResultSet<FormDataVo> getFormApiData(String authorizeKey,String designOid, String reportOid) {
        if(StrUtil.isBlank(authorizeKey)){
            throw new ResultInfoException("参数authorizeKey不能为空！");
        }
        if(StrUtil.isBlank(designOid)){
            throw new ResultInfoException("参数designOid不能为空！");
        }
        FormDataVo formData = formManagerManager.getFormData(authorizeKey,designOid, reportOid,false);
        return new ApiResultSet<>(formData);
    }

    /**
     * @description: 根据 表单formcode、填报reportOid和 某个字段   来获取 这个字段的填写值  (只针对最外层填报data的检索，子表字段不在本接口范围内)
     * @param authorizeKey 授权码
     * @param designOid 设计主键
     * @param reportOid 存储类型为表单系统，reportOid是存储数据的主键，存储类型为数据库，reportOid是数据库主键 （API方式可以为null）
     * @param code 字段编码
     * @author: zje
     * @Date:
     **/
    @Override
    public ApiResultSet<String> getCodeWriteDataByCodeName(String authorizeKey,String designOid, String reportOid,String code) {
        if(StrUtil.isBlank(authorizeKey)){
            throw new ResultInfoException("参数authorizeKey不能为空！");
        }
        if(StrUtil.isBlank(designOid)){
            throw new ResultInfoException("参数designOid不能为空！");
        }
        if(StrUtil.isBlank(code)){
            throw new ResultInfoException("参数code不能为空！");
        }
        FormDataVo formData = formManagerManager.getFormData(authorizeKey,designOid, reportOid,false);
        if (StrUtil.isBlank(formData.getFormData())){
            return new ApiResultSet<>("");
        }

        com.alibaba.fastjson.JSONObject jsonObject = com.alibaba.fastjson.JSONObject.parseObject(formData.getFormData());
        Object obj = jsonObject.get(code);
        return new ApiResultSet<>(obj == null?"":obj.toString());
    }

    @Override
    public ApiResultSet<FormDataVo> getFormDataByReportOid(String authorizeKey, String reportOid) {
        if(StrUtil.isBlank(authorizeKey)){
            throw new ResultInfoException("参数authorizeKey不能为空！");
        }
        if(StrUtil.isBlank(reportOid)){
            throw new ResultInfoException("参数reportOid不能为空！");
        }
        FormDataVo formData = formManagerManager.getFormDataByReportOid(authorizeKey,reportOid);
        return new ApiResultSet<>(formData);
    }

    @Override
    public ApiResultSet<List<FormDataVo>> getFormReportByFormCode(String authorizeKey, String formCode) {
        if(StrUtil.isBlank(authorizeKey)){
            throw new ResultInfoException("参数authorizeKey不能为空！");
        }
        if(StrUtil.isBlank(formCode)){
            throw new ResultInfoException("参数formCode不能为空！");
        }
        List<FormDataVo> formReports = formManagerManager.getFormReportByFormCode(authorizeKey, formCode);
        return new ApiResultSet<>(formReports);
    }

    /**
     * @description: 初始化设计器中的授权组件
     * @param authorizeKey 授权key
     * @author: wuxx
     * @Date: 2021/04/21 10:14
     **/
    @Override
    public ApiResultSet<List<FormComponent>> initFormComponent(String authorizeKey) {
        List<FormComponent> formComponentList = formComponentManager.getFormComponentListByAuthorizeKey(authorizeKey);
        return new ApiResultSet<>(formComponentList);
    }

    /**
     * @description:  根据授权key查询设计器中的授权组件
     * @param authorizeKey 授权key
     * @param componentCode 组件类型
     * @author: wuxx
     * @Date: 2021/04/21 10:14
     **/
    @Override
    public ApiResultSet<FormComponent> getFormComponent(String authorizeKey, String componentCode) {
        FormComponent formData = formComponentManager.getFormComponent(authorizeKey,componentCode);
        return new ApiResultSet<>(formData);
    }

    /**
     * @description:  保存组件信息
     * @param formComponent 组件
     * @author: wuxx
     * @Date: 2021/04/21 10:14
     **/
    @Override
    public ApiResultSet saveFormComponent(FormComponent formComponent) {
        formComponentManager.saveFormComponent(formComponent);
        return new ApiResultSet<>(formComponent);
    }

    /**
     * 通过表单编码获取绑定数据项列表
     * @param authorizeKey 授权key
     * @param formMainOid 表单业务主键
     * @return 绑定数据项列表
     */
    @Override
    public ApiResultSet<List<FormColumn>> queryFormColumnListByFormMainCode(String authorizeKey, String formMainOid,String formCode) {
        if(StrUtil.isBlank(authorizeKey)){
            throw new ResultInfoException("参数authorizeKey不能为空！");
        }
        if(StrUtil.isBlank(formMainOid) && StrUtil.isBlank(formCode)){
            throw new ResultInfoException("参数formMainOid和参数formCode不能同时为空！");
        }

        if((StrUtil.isBlank(formMainOid) || StrUtil.isEmpty(formMainOid)) && StrUtil.isNotBlank(formCode)){
            FormMain formMainCode = formMainManager.getFormMainByFormMainCode(formCode);
            if(null ==formMainCode){
                throw new ResultInfoException("未查询到表单信息！");
            }
            if(!authorizeKey.equals(formMainCode.getAuthorizeKey())){
                throw new ResultInfoException("参数authorizeKey或参数formCode不正确！");
            }
            formMainOid = formMainCode.getFormMainOid();
        }
        return new ApiResultSet<>(this.formManagerManager.queryFormColumnListByFormMainCode(authorizeKey, formMainOid));
    }

    @Override
    public ApiResultSet uploadBase64Images(@RequestBody List<Object> base64Images) {
        if(null == base64Images || base64Images.size()==0){
            throw new ResultInfoException("图片信息不能为空！");
        }
        List<JSONObject> attaList = new ArrayList<>();
        try {
            for (Object image : base64Images){
                String imageBase64 = image.toString();
                if(imageBase64.contains(",")){
                    imageBase64 = image.toString().substring(imageBase64.indexOf(",") + 1);
                }
                byte[] bytes = Base64.decodeBase64(imageBase64);
                InputStream inputStream = new ByteArrayInputStream(bytes);
                //MultipartFile file = new MockMultipartFile("image"+new Date().getTime()+".png", bytes);
                MultipartFile file = new MockMultipartFile("File","image"+new Date().getTime()+".png","text/plain",inputStream);
                HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
                UploadUtil uploadUtil = new UploadUtil(request);
                String filePath = uploadUtil.uploadFile(file);
                //SysAtta atta = uploadUtil.getSysAttaFile(filePath, userOid);
                SysAttaTemp sysAttaTemp = uploadUtil.getSysAttaFile(filePath, null);
                FormAtta atta = new FormAtta();
                BeanUtils.copyProperties(sysAttaTemp, atta);
                //保存附件信息
                FormAtta sysAtta = formAttaManager.saveFormAtta(atta);
//                System.out.println("fastDFSNginxUrl: " + fastDFSNginxUrl);
                JSONObject jsonObject = JSONUtil.createObj().set("attaOid", sysAtta.getAttaOid()).set("url", fastDFSNginxUrl +"/" + sysAtta.getFastdfsUploadUrl()).set("name", sysAtta.getOriginName()).set("size",sysAtta.getFileSize());
                attaList.add(jsonObject);
            }
        }catch (Exception e){
            e.printStackTrace();
            throw new ResultInfoException("图片信息异常！");
        }
        return new ApiResultSet<>(attaList);
    }

    @Override
    public ApiResultSet uploadFormFile(MultipartFile file, String name) {
        try {
            Assert.isTrue(!(file.getSize() == 0), "禁止上传大小为0kb文件！");
            String userOid = null!=CurrentLoginUserHolder.getCurrentLoginUser()?CurrentLoginUserHolder.getCurrentLoginUser().getUserOid():null;
            if(StrUtil.isNotEmpty(name)){
                file = new MockMultipartFile("File",name,"text/plain", file.getInputStream());
            }
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            UploadUtil uploadUtil = new UploadUtil(request);
            String filePath = uploadUtil.uploadFile(file);
            //SysAtta atta = uploadUtil.getSysAttaFile(filePath, userOid);
            SysAttaTemp sysAttaTemp = uploadUtil.getSysAttaFile(filePath, userOid);
            //保存附件信息
            FormAtta atta = new FormAtta();
            BeanUtils.copyProperties(sysAttaTemp, atta);
            //保存附件信息
            FormAtta sysAtta = formAttaManager.saveFormAtta(atta);
//            System.out.println("fastDFSNginxUrl: " + fastDFSNginxUrl);
            String url = fastDFSNginxUrl +"/" + sysAtta.getFastdfsUploadUrl();
            JSONObject jsonObject = JSONUtil.createObj().set("attaOid", sysAtta.getAttaOid()).set("url", url).set("name", sysAtta.getOriginName()).set("size",sysAtta.getFileSize());
            return new ApiResultSet<>(jsonObject);
        }catch (Exception e){
            e.printStackTrace();
            throw new ResultInfoException(e.getMessage());
        }

    }

    @Override
    public ApiResultSet<FormAtta> getFormAttaByAttaOid(String attaOid) {
        if(StrUtil.isEmpty(attaOid)){
            return  new ApiResultSet<>();
        }
        FormAtta atta = formAttaManager.getFormAttaByAttaOid(attaOid);
        return new ApiResultSet(atta);
    }

    /**
     * @description:  文件下载
     * @param attaOid 文件的oid
     * @author: wuxx
     * @Date: 2021/5/26 14:22
     **/
    @Override
    public void downloadFile(String attaOid) {
        if(StrUtil.isNotEmpty(attaOid)){
            FormAtta atta = formAttaManager.getFormAttaByAttaOid(attaOid);
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
            try {
                String displayFileName = atta.getOriginName();
                String header = request.getHeader("User-Agent").toUpperCase();
                if (!header.contains("MSIE") && !header.contains("TRIDENT") && !header.contains("EDGE")) {
                    displayFileName = URLEncoder.encode(displayFileName, "utf-8");
                } else {
                    displayFileName = URLEncoder.encode(displayFileName, "utf-8");
                    displayFileName = displayFileName.replace("+", "%20");
                }
                // 20210706 modify by kkfan
                @Cleanup InputStream inputStream = DownloadUtil.downloadFile(atta.getName(), atta.getFilePath(), atta.getFastdfsUploadUrl(), request);
                Tika tika = new Tika();
                String mimeType = tika.detect(inputStream);
                byte[] streamBytes = FileUtil.getStreamBytes(inputStream);
                if (streamBytes != null) {
                    response.setCharacterEncoding("UTF-8");
                    response.setHeader("Access-Control-Allow-Origin", "*");
                    response.setContentLength(streamBytes.length);
                    response.setHeader("Content-Disposition", "attachment;fileName=\"" + displayFileName + "\"");
                    response.setContentType(StringUtils.endsWith(displayFileName, ".mp4") ? "video/mp4" : mimeType);
                    @Cleanup OutputStream outputStream = response.getOutputStream();
                    outputStream.write(streamBytes);
                    outputStream.flush();
                }
                //DownloadUtil.downloadUploadUrlFile(atta.getOriginName(), atta.getFilePath(), atta.getName(), atta.getFastdfsUploadUrl(), request, response);
            }catch (Exception e){
                e.printStackTrace();
                throw new ResultInfoException(e.getMessage());
            }
       }
    }

    /**
     * @description: 请求ajax方式获取其他地址的数据
     * @param url 请求地址
     * @param method 请求方式
     * @param paramsData 参数
     * @author: wuxx
     * @Date: 2021/5/31 14:16
     **/
    @Override
    public ApiResultSet getAjaxData(String url, String method, String paramsData) {
        if(StrUtil.isBlank(url)){
            throw new ResultInfoException("请求地址URL不能为空！");
        }
        if(StrUtil.isBlank(method)){
            method = "GET";
        }
        String result = null;
        if(method.toUpperCase().equals("POST")){
            Map stringToMap =  null;
            try {
                stringToMap =  JSONUtil.parseObj(paramsData);
            }catch (Exception e){

            }
            result = HttpUtil.post(url, stringToMap);
        }else {
             result = HttpUtil.get(url);
        }
        if(StrUtil.isNotBlank(result)){
            try {
                JSONObject jsonObject = JSONUtil.parseObj(result,true);
                return new ApiResultSet(null==jsonObject.get("data")?jsonObject:jsonObject.get("data"));
            }catch (Exception e){
                throw new ResultInfoException("请求地址URL异常！");
            }

        }
        return new ApiResultSet(result);
    }

    @Override
    public ApiResultSet<List<FormObjectExtand>> queryFormObjectExtandListByFormMainCode(String authorizeKey, String formMainOid, String formCode, Integer type) {
        if(StrUtil.isBlank(authorizeKey)){
            throw new ResultInfoException("参数authorizeKey不能为空！");
        }
        if((StrUtil.isBlank(formMainOid) || StrUtil.isEmpty(formMainOid)) && StrUtil.isNotBlank(formCode)){
            FormMain formMainCode = formMainManager.getFormMainByFormMainCode(formCode);
            if(null ==formMainCode){
                throw new ResultInfoException("未查询到表单信息！");
            }
            if(!authorizeKey.equals(formMainCode.getAuthorizeKey())){
                throw new ResultInfoException("参数authorizeKey或参数formCode不正确！");
            }
            formMainOid = formMainCode.getFormMainOid();
        }
        return new ApiResultSet<>(this.formManagerManager.queryFormObjectExtandListByFormMainCode(authorizeKey, formMainOid,type));
    }

    @Override
    public ApiResultSet<JSONObject> getDesignInfoByCode(String code,String formMainOid,String designOid,String formCode) {
        //designOid = "11d8b02797434685982d1eb259757fc0";
        //formCode = "FORM20210730UCGNIYBH";
        if(StrUtil.isBlank(formMainOid) && StrUtil.isBlank(designOid) && StrUtil.isBlank(formCode)){
            throw new ResultInfoException("设计对象信息不能为空!");
        }
        FormDesign design = null;
        if(StrUtil.isNotBlank(designOid)){
            design = formDesignManager.getFormDesignByDesignOid(designOid);
        }else if(StrUtil.isNotBlank(formCode)){
            FormMain mainCode = formMainManager.getFormMainByFormMainCode(formCode);
            if(null == mainCode){
                throw new ResultInfoException("未查询到设计对象信息!");
            }
            design = formDesignManager.getFormDesignByFormMainOid(mainCode.getFormMainOid());
        }else {
            design = formDesignManager.getFormDesignByFormMainOid(formMainOid);
        }
        JSONObject designInfoByCode = formDesignManager.getDesignInfoByCode(code, design);
        return new ApiResultSet<>(designInfoByCode);
    }

    /**
     * @description: 根据表单编码集合合并表单成新的表单
     * @author: wuxx
     * @Date: 2021/8/2 9:30
     **/
    @Override
    public ApiResultSet<FormMainVo> mergeForm(MergeFormVo mergeFormVo) {
        if(null == mergeFormVo){
            throw new ResultInfoException("参数对象不能为空！");
        }
        if(StrUtil.isBlank(mergeFormVo.getFormMainOids()) && StrUtil.isBlank(mergeFormVo.getFormCodes())){
            throw new ResultInfoException("参数formMainOids或formCodes不能为空！");
        }
        String objectOid = mergeFormVo.getObjectOid();
        if(StrUtil.isBlank(objectOid)){
            throw new ResultInfoException("参数objectOid不能为空！");
        }
        String formConfigJson = mergeFormVo.getFormConfigJson();
        if(StrUtil.isBlank(formConfigJson)){
            throw new ResultInfoException("参数formConfigJson不能为空！");
        }
        FormMainVo formMainVo = null;
        if(StrUtil.isNotEmpty(mergeFormVo.getFormMainOids())){
             formMainVo = formMainManager.mergeFormByFormMainOids(mergeFormVo.getFormMainOids(),objectOid,mergeFormVo.getSaveDataType(),formConfigJson,mergeFormVo.getFormMainOid(),mergeFormVo.getIncludeChild());
        }
        if(StrUtil.isNotEmpty(mergeFormVo.getFormCodes())){
            formMainVo = formMainManager.mergeFormByFormCodes(mergeFormVo.getFormCodes(),objectOid,mergeFormVo.getSaveDataType(),formConfigJson,mergeFormVo.getFormCode(),mergeFormVo.getIncludeChild());
        }
        return new ApiResultSet<>(formMainVo);
    }

    @Override
    public ApiResultSet<FormObject> getFormObjectByFormMainOid(String formMainOid) {
        if(StrUtil.isBlank(formMainOid)){
            throw new ResultInfoException("参数formMainOid不能为空！");
        }
        FormMain formMain = formMainManager.getFormMainByFormMainOid(formMainOid);
        if(null == formMain){
            throw new ResultInfoException("未查询到设计对象信息!");
        }
        FormObject formObject = formObjectManager.getFormObjectComponentByObjectOid(formMain.getObjectOid());
        return new ApiResultSet<>(formObject);
    }

    @Override
    public ApiResultSet<FormObject> getFormObjectByFormCode(String formCode) {
        if(StrUtil.isBlank(formCode)){
            throw new ResultInfoException("参数formCode不能为空！");
        }
        FormMain formMain = formMainManager.getFormMainByFormMainCode(formCode);
        if(null == formMain){
            throw new ResultInfoException("未查询到设计对象信息!");
        }
        FormObject formObject = formObjectManager.getFormObjectComponentByObjectOid(formMain.getObjectOid());
        return new ApiResultSet<>(formObject);
    }

    /**
     * @description: 根据formCode更新表结构名称和编码
     * @param formCode 表单编码
     * @param oldObjectProperty 旧的属性编码
     * @param newObjectProperty 新的属性编码
     * @param columnName 新的字段名称
     * @author: wuxx
     * @Date: 2021/8/27 17:00
     **/
    @Override
    public ApiResultSet updateColumnByFormCode(String formCode, String oldObjectProperty, String newObjectProperty, String columnName) {
        if(StrUtil.isBlank(formCode)){
            throw new ResultInfoException("参数formCode不能为空！");
        }
        if(StrUtil.isBlank(oldObjectProperty)){
            throw new ResultInfoException("参数oldObjectProperty不能为空！");
        }
        FormMain formMain = formMainManager.getFormMainByFormMainCode(formCode);
        if(null == formMain){
            throw new ResultInfoException("未查询到设计对象信息!");
        }
        FormColumn formColumn = new FormColumn();
        formColumn.setObjectProperty(oldObjectProperty);
        formColumn.setObjectOid(formMain.getObjectOid());
        List<FormColumn> formColumns = formColumnManager.queryFormColumnList(formColumn);
        if(null!=formColumns && formColumns.size()>0){
            FormColumn column = formColumns.get(0);
            column.setObjectProperty(newObjectProperty);
            column.setDemo(columnName);
            formColumnManager.updateFormColumn(column);
        }else {
            throw new ResultInfoException("未查询到对应的字段信息!");
        }
        return new ApiResultSet<>(true);
    }

    /**
     * @description:  获取f-render 编码
     * @param formCode 表单编码
     * @author: wuxx
     * @Date: 2021/11/11 10:45
     **/
    @Override
    public ApiResultSet<String> getFRenderConfigByFormCode(String formCode) {
        if(StrUtil.isBlank(formCode)){
            throw new ResultInfoException("参数formCode不能为空！");
        }
        FormMain mainCode = formMainManager.getFormMainByFormMainCode(formCode);
        if(null == mainCode){
            throw new ResultInfoException("参数formCode未查询到对应表单！");
        }
        FormDesign design = formDesignManager.getFormDesignByFormMainOid(mainCode.getFormMainOid());
        if(null == design){
            throw new ResultInfoException("参数formCode未查询到对应表单设计！");
        }
        String formConfigStr = design.getFormConfig();
        formConfigStr = formConfigStr.replaceAll("\"undefined\"","undefined");
        if(StrUtil.isEmpty(formConfigStr)){
            throw new ResultInfoException("参数formCode未查询到对应表单设计！");
        }
        /*String template = "<template>\n" +
                "<div>\n" +
                "  <f-render\n" +
                "    v-model=\"formData\"\n" +
                "    :config=\"formConfig\"\n" +
                "    pure\n" +
                "  />\n" +
                "  </div>\n" +
                "</template>" +
                "<script>\n" +
                "export default {\n" +
                "name: \"FRenderVue\",\n" +
                " props:{ \n" +
                "      formData: {\n" +
                "        default: {},\n" +
                "        type: Object\n" +
                "      }\n" +
                "    },"+
                "  data() {\n" +
                "    return {\n" +
                "      formConfig: ";
        String endTemplate = "   }\n" +
                "    }\n" +
                "   \n" +
                "  };\n" +
                "</script>";
        String fRenderVue = template + formConfigStr + endTemplate;*/
        //fRenderVue = fRenderVue.replaceAll("\n","");
        //String fRenderVueEncode = new BASE64Encoder().encode(formConfigStr.getBytes());
        String fRenderVueEncode = cn.hutool.core.codec.Base64.encode(formConfigStr);
       /* byte[] result = new byte[0];
        try {
            result = new BASE64Decoder().decodeBuffer(fRenderVueEncode);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(new String(result));*/
        return new ApiResultSet<>(fRenderVueEncode);
    }

    @Override
    public Object getDictSelectOptions(String objectCode) {
        if(StrUtil.isBlank(objectCode)){
            throw new ResultInfoException("参数objectCode不能为空！");
        }
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        Map<String, Object> params = this.getParams(request);
        Object list = formObjectManager.queryDictListByObjectCode(objectCode, params);
        return list;
    }


    /**
     * @description: 获取request所有的参数值
     * @param request
     * @author: wuxx
     * @Date: 2021/11/15 15:23
     **/
    private Map<String, Object> getParams(HttpServletRequest request) {
        Map<String, Object> map = new HashMap();
        Enumeration paramNames = request.getParameterNames();
        while (paramNames.hasMoreElements()) {
            String paramName = (String) paramNames.nextElement();
            if("objectCode".equals(paramName)){
                continue;
            }
            String[] paramValues = request.getParameterValues(paramName);
            if (paramValues.length == 1) {
                String paramValue = paramValues[0];
                if (paramValue.length() != 0) {
                    map.put(paramName, paramValue);
                }
            }
        }
        return map;
    }

    /**
     * @description: 测试API请求的接口
     * @param formData 请求数据
     * @author: wuxx
     * @Date: 2021/4/29 15:30
     **/
    @RequestMapping( value = "/testData",method = {RequestMethod.POST})
    public ApiResultSet testData(String formData) {
        log.info( "{},测试API请求的接口数据为：{}",DateUtil.now(),formData);
        return new ApiResultSet<>(formData);
    }

    /**
     * @description: 测试API请求的接口
     * @param formData 请求数据
     * @author: wuxx
     * @Date: 2021/4/29 15:30
     **/
    @RequestMapping( value = "/testDataGet",method = {RequestMethod.GET})
    public ApiResultSet testDataGet(String formData) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        Enumeration parameterNames = request.getParameterNames();
        while (parameterNames.hasMoreElements()) {
            String name = (String) parameterNames.nextElement();
            String value = request.getParameter(name);
            System.err.println(name + "=" + value);
        }

        log.info("{},测试GET,API请求的接口数据为：{}",DateUtil.now() ,formData);
        JSONObject object = new JSONObject();
        object.set("grade","四年级");
        object.set("name","李四");
        object.set("sex","1");
        object.set("tuition","1111");
        object.set("unPay","100");
        JSONArray JSONArray = new JSONArray();
        JSONArray.add(object);
        Map<String, Object> map = new HashMap<>();
        map.put("list",JSONArray);
        map.put("code","ceshi");
        map.put("name","李四");
        return new ApiResultSet<>(map);
    }

    /**
     * @description: 测试API请求的接口
     * @param formData 请求数据
     * @author: wuxx
     * @Date: 2021/4/29 15:30
     **/
    @RequestMapping( value = "/testDataPost",method = {RequestMethod.POST})
    public ApiResultSet testDataPost(@RequestBody String formData) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        Enumeration parameterNames = request.getParameterNames();
        while (parameterNames.hasMoreElements()) {
            String name = (String) parameterNames.nextElement();
            String value = request.getParameter(name);
            System.err.println(name + "=" + value);
        }
        log.info("{},测试GET,API请求的接口数据为：{}",DateUtil.now() ,formData);
        JSONObject object = new JSONObject();
        object.set("grade","四年级");
        object.set("name","李四");
        object.set("sex","1");
        object.set("tuition","1111");
        object.set("unPay","100");
        JSONArray JSONArray = new JSONArray();
        JSONArray.add(object);
        Map<String, Object> map = new HashMap<>();
        map.put("list",JSONArray);
        map.put("code","ceshi");
        map.put("name","李四");
        return new ApiResultSet<>(map);
    }


    /**
     * @description: 填报数据导出docx或者pdf
     * @param designAndReportOid 设计主键-填报主键
     * @param linkDesignAndReportOids 关联设计主键-填报主键，多个用逗号隔开
     * @param docxTemplateOid 模板主键
     * @param isPdf 是否导出pdf
     * @author: wuxx
     * @Date: 2021/12/3 13:42
     **/
    @RequestMapping(value = {"/exportFormDataToDocx"},method = {RequestMethod.GET})
    @ResponseBody
    public void exportFormDataToDocx(@RequestParam("designAndReportOid") String designAndReportOid,
                                     @RequestParam(value = "linkDesignAndReportOids",required = false) String linkDesignAndReportOids,
                                     @RequestParam("docxTemplateOid") String docxTemplateOid,
                                     @RequestParam(value = "isPdf",required = false) Boolean isPdf) {
        if(StrUtil.isBlank(designAndReportOid)){
            throw new ResultInfoException("参数designAndReportOid不能为空！");
        }
        if(StrUtil.isBlank(docxTemplateOid)){
            throw new ResultInfoException("参数docxTemplateOid不能为空！");
        }
        formManagerManager.exportFormDataToDocx(designAndReportOid,linkDesignAndReportOids,docxTemplateOid, isPdf,null,false);
    }

    /**
     * @description: 填报数据导出docx ,并存到文件服务器，返回文件再服务器的信息
     * @param designAndReportOid 设计主键-填报主键
     * @param linkDesignAndReportOids 关联设计主键-填报主键，多个用逗号隔开
     * @param initData 自定义数据，json串数据。注意{}属于非法的url传参，要进行转义再传过来
     * @param docxTemplateOid 模板主键,以英文逗号隔开
     **/
    @RequestMapping(value = {"/exportFormDataToDocxSaveinFileServer"},method = {RequestMethod.GET})
    @ResponseBody
    public ApiResultSet exportFormDataToDocxSaveinFileServer(@RequestParam("designAndReportOid") String designAndReportOid,
                                     @RequestParam(value = "linkDesignAndReportOids",required = false) String linkDesignAndReportOids,
                                     @RequestParam(value = "initData",required = false) String initData,
                                     @RequestParam("docxTemplateOid") String docxTemplateOid,
                                     @RequestParam(value = "isWord",required = false,defaultValue = "0") Boolean isWord) {
        if(StrUtil.isBlank(designAndReportOid)){
            throw new ResultInfoException("参数designAndReportOid不能为空！");
        }
        if(StrUtil.isBlank(docxTemplateOid)){
            throw new ResultInfoException("参数docxTemplateOid不能为空！");
        }
        String[] docxTemplateOids = docxTemplateOid.split(",");
        List<JSONObject> jsonObjects = new ArrayList<>();
        HashMap<String, Object> initDataMap = new HashMap<>();
        if (StrUtil.isNotEmpty(initData)){
            JSONObject jsonObject = JSONUtil.parseObj(initData);
            jsonObject.forEach(initDataMap::put);
        }
        for (String dtemplateOid : docxTemplateOids){
            JSONObject jsonObject = formManagerManager.exportFormDataToDocx(designAndReportOid, linkDesignAndReportOids, dtemplateOid, !isWord , initDataMap, true);
            jsonObjects.add(jsonObject);
        }
        return new ApiResultSet<>(jsonObjects);
    }

    /**
     * @description: 填报数据导出docx或者pdf
     * @param  exportFormVo
     * -designAndReportOid 设计主键-填报主键
     * -linkDesignAndReportOids 关联设计主键-填报主键，多个用逗号隔开
     * -docxTemplateOid 模板主键
     * -isPdf 是否导出pdf
     * @author: wuxx
     * @Date: 2021/12/3 13:42
     **/
    @RequestMapping(value = {"/exportFormDataToDocxPost"},method = {RequestMethod.POST})
    public void exportFormDataToDocxPost(@RequestBody ExportFormVo exportFormVo) {
        if(StrUtil.isBlank(exportFormVo.getDesignAndReportOid())){
            throw new ResultInfoException("参数designAndReportOid不能为空！");
        }
        if(StrUtil.isBlank(exportFormVo.getDocxTemplateOid())){
            throw new ResultInfoException("参数docxTemplateOid不能为空！");
        }
        formManagerManager.exportFormDataToDocx(exportFormVo.getDesignAndReportOid(),
                exportFormVo.getLinkDesignAndReportOids(),exportFormVo.getDocxTemplateOid(),
                exportFormVo.getIsPdf(),exportFormVo.getInitDataMap(),false);
    }
}
