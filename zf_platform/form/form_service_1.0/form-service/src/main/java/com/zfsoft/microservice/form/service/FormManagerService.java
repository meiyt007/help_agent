package com.zfsoft.microservice.form.service;

import cn.hutool.json.JSONObject;
import com.zfsoft.microservice.form.data.*;
import com.zfsoft.microservice.form.data.vo.FormDataVo;
import com.zfsoft.microservice.form.data.vo.FormMainVo;
import com.zfsoft.microservice.form.data.vo.MergeFormVo;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.utils.feign.ProcessFeignCalledResult;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * @description: 电子表单管理服务接口
 * @author: wuxx
 * @Date: 2021/4/22 13:07
 **/
@RequestMapping("/manager")
public interface FormManagerService {

    /**
     * @description:  获取表单设计对象的信息
     * @param formMainOid 表单设计对象业务oid
     * @author: wuxx
     * @Date: 2021/4/22 13:07
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/getFormMainByFormMainOid/{formMainOid}",method = {RequestMethod.GET})
    ApiResultSet<FormMain> getFormMainByFormMainOid(@PathVariable("formMainOid") String formMainOid);

    /**
     * @description:  获取表单设计对象的信息
     * @param formCode 表单设计对象编码
     * @author: wuxx
     * @Date: 2021/4/22 13:07
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/getFormMainByFormCode",method = {RequestMethod.GET})
    ApiResultSet<FormMain> getFormMainByFormCode(@RequestParam("formCode") String formCode);

    /**
     * @description:  根据授权key获取表单设计已经发布的记录
     * @param authorizeKey 授权key
     * @author: wuxx
     * @Date: 2021/4/22 13:07
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/getFormMainByAuthorizeKey",method = {RequestMethod.GET})
    ApiResultSet<List<FormMainVo>> getFormMainByAuthorizeKey(@RequestParam("authorizeKey") String authorizeKey,
                                                             @RequestParam(value = "formStatus",required = false) Integer formStatus);

    /**
     * @description:  根据授权key和模块oid获取表单设计Vo记录
     * @param authorizeKey 授权key
     * @param moduleOid 模块oid
     * @author: wuxx
     * @Date: 2021/4/22 13:07
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/getFormMainVoByModuleOid",method = {RequestMethod.GET})
    ApiResultSet<List<FormMainVo>> getFormMainVoByModuleOid(@RequestParam("authorizeKey") String authorizeKey,@RequestParam("moduleOid") String moduleOid);

    /**
     * @description:  获取表单设计对象的信息
     * @param formMainOid 表单主键oid
     * @param designOid 表单设计对象业务oid
     * @author: wuxx
     * @Date: 2021/4/22 13:07
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/getFormDesign",method = {RequestMethod.GET})
    ApiResultSet<FormDesign> getFormDesign(@RequestParam("authorizeKey") String authorizeKey,
                                           @RequestParam(value = "formMainOid",required = false) String formMainOid,
                                           @RequestParam(value = "designOid",required = false) String designOid,
                                           @RequestParam(value = "formCode",required = false) String formCode);

    /**
     * @description:  获取表单设计对象的信息
     * @param formMainOid 表单主键oid
     * @author: wuxx
     * @Date: 2021/6/1 13:07
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/getFormDesignByFormMainOid",method = {RequestMethod.GET})
    ApiResultSet<FormDesign> getFormDesignByFormMainOid(@RequestParam(value = "formMainOid") String formMainOid);

    /**
     * @description:  获取表单设计对象的信息
     * @param formCode 表单主设计对象formCode
     * @author: wuxx
     * @Date: 2021/8/6 10:14
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/getFormDesignByFormCode",method = {RequestMethod.GET})
    ApiResultSet<FormDesign> getFormDesignByFormCode(@RequestParam("formCode") String formCode);

    /**
     * @description:  获取表单设计对象的组件配置信息
     * @param formMainOid 表单主键oid
     * @param designOid 表单设计对象业务oid
     * @param components 设计的组件name 多个用逗号,隔开
     * @author: wuxx
     * @Date: 2021/5/6 13:07
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/getFormDesignConfigList",method = {RequestMethod.GET})
    ApiResultSet<String> getFormDesignConfigList(@RequestParam("authorizeKey") String authorizeKey,
                                           @RequestParam(value = "formMainOid",required = false) String formMainOid,
                                           @RequestParam(value = "designOid",required = false) String designOid,
                                           @RequestParam(value = "components",required = false) String components);


    /**
     * @description:  保存设计的config
     * @param formDesign 设计对象
     * @author: wuxx
     * @Date: 2021/4/22 13:07
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/saveFormDesign",method = {RequestMethod.POST})
    ApiResultSet<FormDesign> saveFormDesign(@RequestBody FormDesign formDesign);

    /**
     * @description:  获取模块的信息集合
     * @param authorizeKey 模块实体类授权key
     * @author: wuxx
     * @Date: 2021/04/26 14:14
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/queryFormModuleList/{authorizeKey}",method = {RequestMethod.GET})
    ApiResultSet<List<FormModule>> queryFormModuleList(@PathVariable("authorizeKey") String authorizeKey);

    /**
     * @description:  根据授权key查询存储对象下级列表
     * @param authorizeKey 授权key
     * @param moduleOid 模块主键
     * @author: wuxx
     * @Date: 2021/04/26 14:14
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/queryFormObjectList",method = {RequestMethod.GET})
    ApiResultSet<List<FormObject>> queryFormObjectList(@RequestParam("authorizeKey") String authorizeKey,
                                                       @RequestParam(value = "moduleOid") String moduleOid);

    /**
     * @description:  根据对象主键查询表结构列表
     * @param objectOid 对象主键
     * @param authorizeKey 授权主键
     * @author: wuxx
     * @Date: 2021/04/26 14:14
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/queryFormColumnList",method = {RequestMethod.GET})
    ApiResultSet<List<FormColumn>> queryFormColumnList(@RequestParam("authorizeKey") String authorizeKey,
                                                       @RequestParam("objectOid") String objectOid);

    /**
     * @description:  根据表单编码查询表属性列表
     * @param formCode 表单编码
     * @param authorizeKey 授权主键
     * @author: wuxx
     * @Date: 2021/07/20 14:14
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/queryFormColumnListByFormCode",method = {RequestMethod.GET})
    ApiResultSet<List<String>> queryFormColumnListByFormCode(@RequestParam("authorizeKey") String authorizeKey,
                                                       @RequestParam("formCode") String formCode);


    /**
     * @description:  存储对象的数据的保存本地、数据库、接口返回
     * @param formDataVo authorizeKey 授权key
     * @param formDataVo formMainOid 设计主表的业务主键
     * @param formDataVo designOid 设计表的业务主键
     * @param formDataVo formData 存储对象数据的JSON
     * @author: wuxx
     * @Date: 2021/4/22 13:07
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/saveFormData",method = {RequestMethod.POST})
    ApiResultSet<Map<String,Object>> saveFormData(@RequestBody FormDataVo formDataVo);

    /**
     * @description: 根据设计主键和表单数据主键获取数据
     * @param designOid 设计主键
     * @param reportOid 存储类型为表单系统，reportOid是存储数据的主键，存储类型为数据库，reportOid是数据库主键 （API方式可以为null）
     * @author: wuxx
     * @Date: 2021/4/23 9:43
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/getFormData",method = {RequestMethod.GET})
    ApiResultSet<FormDataVo> getFormData(@RequestParam("authorizeKey") String authorizeKey,
                                         @RequestParam("designOid") String designOid,
                                         @RequestParam(value = "reportOid",required = false) String reportOid);

    /**
     * @description: 根据设计主键和表单数据主键获取数据--接口使用
     * @param designOid 设计主键
     * @param reportOid 存储类型为表单系统，reportOid是存储数据的主键，存储类型为数据库，reportOid是数据库主键 （API方式可以为null）
     * @author: wuxx
     * @Date: 2021/4/23 9:43
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/getFormApiData",method = {RequestMethod.GET})
    ApiResultSet<FormDataVo> getFormApiData(@RequestParam("authorizeKey") String authorizeKey,
                                         @RequestParam("designOid") String designOid,
                                         @RequestParam(value = "reportOid",required = false) String reportOid);

    /**
     * @description: 根据 表单formcode、填报reportOid和 某个字段   来获取 这个字段的填写值  (只针对最外层填报data的检索，子表字段不在本接口范围内)
     * @param authorizeKey 授权码
     * @param designOid 设计主键
     * @param reportOid 存储类型为表单系统，reportOid是存储数据的主键，存储类型为数据库，reportOid是数据库主键 （API方式可以为null）
     * @param code 字段编码
     * @author: zje
     * @Date:
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/getCodeWriteDataByCodeName",method = {RequestMethod.GET})
    ApiResultSet<String> getCodeWriteDataByCodeName(@RequestParam(value = "authorizeKey") String authorizeKey,
                                            @RequestParam("designOid") String designOid,
                                            @RequestParam(value = "reportOid") String reportOid,
                                            @RequestParam("code") String code);


    /**
     * @description: 根据表单数据主键获取数据
     * @param reportOid 设计数据主键
     * @author: wuxx
     * @Date: 2021/5/17 9:43
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/getFormDataByReportOid",method = {RequestMethod.GET})
    ApiResultSet<FormDataVo> getFormDataByReportOid(@RequestParam("authorizeKey") String authorizeKey,
                                         @RequestParam(value = "reportOid") String reportOid);

    /**
     * @description: 根据表单编码获取填报数据的集合
     * @param formCode 表单编码
     * @author: wuxx
     * @Date: 2021/5/17 9:43
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/getFormReportByFormCode",method = {RequestMethod.GET})
    ApiResultSet<List<FormDataVo>> getFormReportByFormCode(@RequestParam("authorizeKey") String authorizeKey,
                                                    @RequestParam("formCode") String formCode);

    /**
     * @description: 初始化设计器中的授权组件
     * @param authorizeKey 授权key
     * @author: wuxx
     * @Date: 2021/04/21 10:14
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/initFormComponent/{authorizeKey}",method = {RequestMethod.GET})
    ApiResultSet<List<FormComponent>> initFormComponent(@PathVariable("authorizeKey") String authorizeKey);

    /**
     * @description:  根据授权key查询设计器中的授权组件
     * @param authorizeKey 授权key
     * @param componentCode 组件类型
     * @author: wuxx
     * @Date: 2021/04/21 10:14
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/getFormComponent",method = {RequestMethod.GET})
    ApiResultSet<FormComponent> getFormComponent(@RequestParam("authorizeKey") String authorizeKey,
                                                 @RequestParam("componentCode") String componentCode);

    /**
     * @description:  保存组件信息
     * @param formComponent 组件
     * @author: wuxx
     * @Date: 2021/04/21 10:14
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/saveFormComponent",method = {RequestMethod.POST})
    ApiResultSet saveFormComponent(@RequestBody FormComponent formComponent);

    /**
     * 通过表单编码获取绑定数据项列表
     * @param authorizeKey 授权key
     * @param formMainOid 表单业务主键
     * @return 绑定数据项列表
     */
    @ProcessFeignCalledResult
    @RequestMapping( value = "/queryFormColumnListByFormMainCode",method = {RequestMethod.GET})
    ApiResultSet<List<FormColumn>> queryFormColumnListByFormMainCode(@RequestParam("authorizeKey") String authorizeKey,
                                                       @RequestParam(value = "formMainOid",required = false) String formMainOid,
                                                       @RequestParam(value = "formCode",required = false) String formCode);

    /**
     * @description:  上传base64图片文件
     * @param base64Images 图片文件
     * @author: wuxx
     * @Date: 2021/5/24 10:33
     **/
    @PostMapping(value = "/uploadBase64Images")
    ApiResultSet uploadBase64Images(@RequestBody List<Object> base64Images);

    /**
     * @description:  上传文件
     * @param name 上传文件名称
     * @param file 文件
     * @author: wuxx
     * @Date: 2021/5/24 10:33
     **/
    @PostMapping(value = "/uploadFormFile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ApiResultSet uploadFormFile(@RequestParam(value="file",required=false) MultipartFile file, @RequestParam(value="name",required=false) String name);

    /**
     * @param attaOid 附件实体类业务主键
     * @description: 获取附件的信息
     * @author: wuxx
     * @Date: 2021/5/24 10:33
     **/
    @RequestMapping(value = "/getFormAttaByAttaOid/{attaOid}", method = {RequestMethod.GET})
    ApiResultSet<FormAtta> getFormAttaByAttaOid(@PathVariable("attaOid") String attaOid);

    /**
     * @description:  文件下载
     * @param attaOid 文件的oid
     * @author: wuxx
     * @Date: 2021/5/26 14:22
     **/
    @RequestMapping(value = {"/downloadFile/{attaOid}"},method = {RequestMethod.GET})
    @ResponseBody
    void downloadFile(@PathVariable("attaOid") String attaOid);

    /**
     * @description: 请求ajax方式获取其他地址的数据
     * @param url 请求地址
     * @param method 方法
     * @param paramsData 参数
     * @author: wuxx
     * @Date: 2021/5/31 14:16
     **/
    @ProcessFeignCalledResult
    @RequestMapping(value = {"/getAjaxData"},method = {RequestMethod.GET})
    ApiResultSet getAjaxData(@RequestParam("url") String url,
                            @RequestParam(value = "method",required = false) String method,
                            @RequestParam(value = "paramsData",required = false) String paramsData);


    /**
     * 通过表单编码获取绑定扩展项列表
     * @param authorizeKey 授权
     * @param formMainOid 主键
     * @param formCode 编码
     * @param type 类型 1数组、2对象
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping( value = "/queryFormObjectExtandListByFormMainCode",method = {RequestMethod.GET})
    ApiResultSet<List<FormObjectExtand>> queryFormObjectExtandListByFormMainCode(@RequestParam("authorizeKey") String authorizeKey,
                                                                     @RequestParam(value = "formMainOid",required = false) String formMainOid,
                                                                     @RequestParam(value = "formCode",required = false) String formCode,
                                                                     @RequestParam(value = "type",required = false) Integer type);

    /**
     * @description: 根据编码获取表单设计信息
     * @param code 编码
     * @author: wuxx
     * @Date: 2021/7/30 13:29
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/getDesignInfoByCode",method = {RequestMethod.GET})
    ApiResultSet<JSONObject> getDesignInfoByCode(@RequestParam("code") String code,
                                                 @RequestParam(value = "formMainOid",required = false) String formMainOid,
                                                 @RequestParam(value = "designOid",required = false) String designOid,
                                                 @RequestParam(value = "formCode",required = false) String formCode);

    /**
     * @description: 根据表单编码集合合并表单成新的表单
     * @author: wuxx
     * @Date: 2021/8/2 9:30
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/mergeForm",method = {RequestMethod.POST})
    ApiResultSet<FormMainVo> mergeForm(@RequestBody MergeFormVo mergeFormVo);

    /**
     * @description:  获取表存储对象的信息
     * @param formMainOid 表单主键oid
     * @author: wuxx
     * @Date: 2021/6/1 13:07
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/getFormObjectByFormMainOid",method = {RequestMethod.GET})
    ApiResultSet<FormObject> getFormObjectByFormMainOid(@RequestParam(value = "formMainOid") String formMainOid);

    /**
     * @description:  获取表存储对象的信息
     * @param formCode 表单主设计对象formCode
     * @author: wuxx
     * @Date: 2021/8/6 10:14
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/getFormObjectByFormCode",method = {RequestMethod.GET})
    ApiResultSet<FormObject> getFormObjectByFormCode(@RequestParam("formCode") String formCode);

    /**
     * @description: 根据formCode更新表结构名称和编码
     * @param formCode 表单编码
     * @param oldObjectProperty 旧的属性编码
     * @param newObjectProperty 新的属性编码
     * @param columnName 新的字段名称
     * @author: wuxx
     * @Date: 2021/8/27 17:00
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/updateColumnByFormCode",method = {RequestMethod.POST})
    ApiResultSet updateColumnByFormCode(@RequestParam String formCode,
                                        @RequestParam String oldObjectProperty,
                                        @RequestParam(required = false) String newObjectProperty,
                                        @RequestParam(required = false) String columnName);


    /**
     * @description:  获取f-render 编码
     * @param formCode 表单编码
     * @author: wuxx
     * @Date: 2021/11/11 10:45
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/getFRenderConfigByFormCode",method = {RequestMethod.GET})
    ApiResultSet<String> getFRenderConfigByFormCode(@RequestParam("formCode") String formCode);

    /**
     * @description:  获取设置的下拉字段信息
     * @param objectCode 存储编码
     * @author: wuxx
     * @Date: 2021/11/11 10:45
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/getDictSelectOptions",method = {RequestMethod.GET})
    Object getDictSelectOptions(@RequestParam("objectCode") String objectCode);
}
