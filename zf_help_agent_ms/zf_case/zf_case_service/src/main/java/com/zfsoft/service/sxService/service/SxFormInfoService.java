package com.zfsoft.service.sxService.service;

import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.utils.feign.ProcessFeignCalledResult;
import com.zfsoft.service.dto.FormTableDto;
import com.zfsoft.service.sxService.data.SxFormInfo;
import com.zfsoft.service.sxService.data.ZcFormInfo;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @ClassName SxFormInfoService
 * @Description 事项分类表单关联
 * @Author xiayj
 * @Date 2021/7/21 15:28
 **/
@RequestMapping("/matter/formInfo")
public interface SxFormInfoService {

    /***
     * @param serviceOid
     * @description: 获取表单关联列表
     * @return: com.zfsoft.platform.common.data.ApiResultSet<java.util.List < com.zfsoft.formConfig.data.SxFormInfo>>
     * @author: xiayj
     * @date: 2021/7/21
     */
    @RequestMapping(value = "/getSxFormInfoList", method = {RequestMethod.GET})
    ApiResultSet<List<SxFormInfo>> getSxFormInfoList(@RequestParam(value = "serviceOid", required = false) String serviceOid);

    /***
     * @param connectOid
     * @description: 查询关联信息
     * @return: com.zfsoft.platform.common.data.ApiResultSet<com.zfsoft.formConfig.data.SxFormInfo>
     * @author: xiayj
     * @date: 2021/7/22
     */
    @RequestMapping(value = "/getSxFormInfoByOid", method = {RequestMethod.GET})
    ApiResultSet<SxFormInfo> getSxFormInfoByOid(String connectOid);

    /**
     * @param sxFormInfo
     * @description: 保存更新关联信息
     * @return: com.zfsoft.platform.common.data.ApiResultSet<java.lang.String>
     * @author: xiayj
     * @date: 2021/7/22
     */
    @RequestMapping(value = "/saveSxFormInfo", method = {RequestMethod.POST})
    ApiResultSet<String> saveSxFormInfo(@RequestBody SxFormInfo sxFormInfo);

    /***
     * @param connectOid
     * @description: 删除关联信息
     * @return: com.zfsoft.platform.common.data.ApiResultSet<java.lang.String>
     * @author: xiayj
     * @date: 2021/7/22
     */
    @RequestMapping(value = "/deleteSxFormInfo", method = {RequestMethod.POST})
    ApiResultSet<String> deleteSxFormInfo(String connectOid);


    /***
     * @param id
     * @description: 启禁用表单信息
     * @return: com.zfsoft.platform.common.data.ApiResultSet<java.lang.String>
     * @author: xiayj
     * @date: 2021/9/24
     */
    @RequestMapping(value = "/ableSxFormInfo", method = {RequestMethod.POST})
    ApiResultSet<String> ableSxFormInfo(Long id);


    /***
     * 创建表存储
     *
     * @return: ApiResultSet<String>
     * @author: yuy
     * @date: 2021/8/3
     */
    @RequestMapping(value = "/saveOrUpdateCreateTable", method = {RequestMethod.POST})
    ApiResultSet<String> saveOrUpdateCreateTable(
            @RequestParam("url") String url,
            @RequestParam("userName") String userName,
            @RequestParam("password") String password,
            @RequestParam("list") List<String> list) throws Exception;

    /***
     * 拼接创建表sql
     *
     * @return: ApiResultSet<String>
     * @author: yuy
     * @date: 2021/8/5
     */
    @RequestMapping(value = "/createTableSqlByParams", method = {RequestMethod.POST})
    ApiResultSet<String> createTableSqlByParams(@RequestBody FormTableDto formTableDto) throws Exception;



    /***
     * 获取事项表单列表
     * @param contentOid
     * @param typeOid
     * @return: com.zfsoft.platform.common.data.ApiResultSet<java.util.List < com.zfsoft.formConfig.data.SxFormInfo>>
     * @author: xiayj
     * @date: 2021/8/12
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/getFormInfoList", method = {RequestMethod.GET})
    ApiResultSet<List<SxFormInfo>> getFormInfoList(
            @RequestParam(value = "contentOid") String contentOid,
            @RequestParam(value = "typeOid", required = false) String typeOid
    );

    /**
     * 更新表单的designOid
     * @param oid
     * @param designOid
     * @return: com.zfsoft.platform.common.data.ApiResultSet<java.lang.String>
     * @author: xiayj
     * @date: 2021/8/26
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/updateDesign", method = {RequestMethod.GET})
    ApiResultSet<String> updateDesign(
            @RequestParam(value = "oid") String oid,
            @RequestParam(value = "designOid") String designOid,
            @RequestParam(value = "formMainOid") String formMainOid,
            @RequestParam(value = "serviceOid") String serviceOid
    );

    /**
     * 获取已设计发布的表单列表
     *
     * @author: wangwg
     *  @date: 2021/9/8
     * @param serviceOid
     * @return
     */
    @RequestMapping(value = "/getDesignFormList", method = {RequestMethod.GET})
    ApiResultSet<List<SxFormInfo>> getDesignFormList(@RequestParam(value = "serviceOid") String serviceOid);


    /**
     * 查询分类是否已存在
     *
     * @author: wangwg
     *  @date: 2021/9/22
     * @param serviceOid
     * @return
     */

    @RequestMapping(value = "/queryFormInfoExist", method = {RequestMethod.GET})
    ApiResultSet<List<SxFormInfo> > queryFormInfoExist(@RequestParam(value = "serviceOid") String serviceOid, @RequestParam(value = "fieldTypeOid") String fieldTypeOid);



    @ProcessFeignCalledResult
    @RequestMapping(value = "/updateDesignChildName", method = {RequestMethod.GET})
    ApiResultSet<String> updateDesignChildName(
            @RequestParam(value = "oid") String oid,
            @RequestParam(value = "childFormName") String childFormName
    );

    @ProcessFeignCalledResult
    @RequestMapping(value = "/getSxFormInfoByDesignOid", method = {RequestMethod.GET})
    ApiResultSet<SxFormInfo> getSxFormInfoByDesignOid(@RequestParam(value = "designOid") String designOid);

    @ProcessFeignCalledResult
    @RequestMapping(value = "/getDesignFormListByServiceOidAndTypeOid", method = {RequestMethod.GET})
    ApiResultSet<List<SxFormInfo>> getDesignFormListByServiceOidAndTypeOid(@RequestParam(value = "serviceOid") String serviceOid, @RequestParam(value = "typeOid", required = false) String typeOid);

    /**
     * 网站端获取已设计发布的表单列表
     * @param serviceOid
     * @return
     */
    @RequestMapping(value = "/getWebDesignFormList", method = {RequestMethod.GET})
    ApiResultSet<List<SxFormInfo>> getBrowserDesignFormList(@RequestParam(value = "serviceOid") String serviceOid);

    /**
     * @description 获取精细化表单数据
     * @param serviceOid
     * @author meiyt
     * @date 2022/6/18
     * @return
     **/
    @RequestMapping(value = "/getZcFormInfoList", method = {RequestMethod.GET})
    ApiResultSet<List<ZcFormInfo>> getZcFormInfoList(@RequestParam(value = "serviceOid", required = false) String serviceOid,
                                                     @RequestParam(value = "caseOid", required = false) String caseOid,
                                                     @RequestParam(value = "valOids", required = false) String valOids);
}
