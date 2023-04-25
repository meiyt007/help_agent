package com.zfsoft.service.sxService.service;

import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.platform.utils.feign.ProcessFeignCalledResult;
import com.zfsoft.service.sxService.data.SxFillField;
import com.zfsoft.service.sxService.data.SxFormInfo;
import com.zfsoft.service.sxService.data.SxOptionField;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @ClassName SxFillFieldService
 * @Author xiayj
 * @Date 2021/7/14 11:44
 **/
@RequestMapping("/matter/fillField")
public interface SxFillFieldService {

    /***
     * @param contentOid
     * @description: 查询事项表字段分页
     * @return: com.zfsoft.platform.common.data.ApiResultSet
     * @author: xiayj
     * @date: 2021/7/14
     */
    @RequestMapping(value = "/queryFillFieldPage", method = {RequestMethod.GET})
    ApiResultSet<PageResult<SxFillField>> queryFillFieldPage(
            @RequestParam(value = "contentOid") String contentOid,
            @RequestParam(value = "fieldName", required = false) String fieldName,
            @RequestParam(value = "fieldCode", required = false) String fieldCode,
            @RequestParam(value = "labelOid", required = false) String labelOid,
            @RequestParam(value = "pageNum", required = false) Integer pageNum,
            @RequestParam(value = "pageSize", required = false) Integer pageSize);


    /***
     * @param fieldOid
     * @description: 获取字段信息
     * @return: com.zfsoft.platform.common.data.ApiResultSet<com.zfsoft.formConfig.data.SxFillField>
     * @author: xiayj
     * @date: 2021/7/15
     */
    @RequestMapping(value = "/getFillField", method = {RequestMethod.GET})
    ApiResultSet<SxFillField> getFillField(@RequestParam(value = "fieldOid") String fieldOid);

    /***
     * @param sxFillField
     * @description: 保存更新字段信息
     * @return: com.zfsoft.platform.common.data.ApiResultSet
     * @author: xiayj
     * @date: 2021/7/14
     */
    @RequestMapping(value = "/saveFillField", method = {RequestMethod.POST})
    ApiResultSet saveOrUpdateFillField(@RequestBody SxFillField sxFillField);

    /**
     * @param fieldOid
     * @description: 删除字段
     * @return: com.zfsoft.platform.common.data.ApiResultSet
     * @author: xiayj
     * @date: 2021/7/14
     */
    @RequestMapping(value = "/deleteFillField", method = {RequestMethod.POST})
    ApiResultSet deleteFillField(@RequestParam(value = "fieldOid", required = false) String fieldOid);


    /***
     * @param serviceOid
     * @description: 根据serviceOid查询对应字段数据
     * @return: com.zfsoft.platform.common.data.ApiResultSet<java.util.List < com.zfsoft.formConfig.data.SxFillField>>
     * @author: xiayj
     * @date: 2021/7/28
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/queryFieldList", method = {RequestMethod.GET})
    ApiResultSet<List<SxFillField>> queryFieldList(
            @RequestParam(value = "serviceOid") String serviceOid,
            @RequestParam(value = "fieldTypeOid", required = false) String fieldTypeOid);


    /***
     * @param labelOid
     * @description: 根据oid查询对应字段数据
     * @return: com.zfsoft.platform.common.data.ApiResultSet<java.util.List < com.zfsoft.formConfig.data.SxFillField>>
     * @author: wangwg
     * @date: 2021/9/24
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/queryFieldListByOids", method = {RequestMethod.GET})
    ApiResultSet<List<SxFillField>> queryFieldListByOids(
            @RequestParam(value = "serviceOid") String serviceOid,
            @RequestParam(value = "fieldTypeOid", required = false) String fieldTypeOid,
            @RequestParam(value = "labelOid", required = false) String labelOid);

    /***
     * 批量更新字段信息集合
     * @param sxFillFields
     * @author lushuai
     * @date 2021/8/27 15:58
     */
    @RequestMapping(value = "/batchUpdateFillField", method = {RequestMethod.POST})
    ApiResultSet batchUpdateFillField(@RequestBody List<SxFillField> sxFillFields);

    /**
     * 查询主表和子表等标签下面的字段
     * @param serviceOid
     * @param fieldTypeOid
     * labelOid 为空的时候查询分类下面不是动态表格的所有标签
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/queryFieldListInfo", method = {RequestMethod.GET})
    ApiResultSet<List<SxFillField>> queryFieldListInfo(
            @RequestParam(value = "serviceOid") String serviceOid,
            @RequestParam(value = "fieldTypeOid", required = false) String fieldTypeOid,
            @RequestParam(value = "labelOid", required = false) String labelOid);

    /**
     * 保存所有的表单配置字段
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/saveOrUpdateField", method = {RequestMethod.POST})
    ApiResultSet<SxFormInfo> saveOrUpdateField(@RequestBody SxFormInfo sxFormInfo);

    @ProcessFeignCalledResult
    @RequestMapping(value = "/getDbSxFillFieldByOid", method = {RequestMethod.GET})
    ApiResultSet<SxFillField> getDbSxFillFieldByOid(@RequestParam(value = "fieldOid") String fieldOid);

    /**
     * @description 保存：选项-字段关系
     * @param sxOptionField
     * @author meiyt
     * @date 2022/5/16
     * @return
     **/
    @RequestMapping(value = "/saveOrUpdateOptionField", method = {RequestMethod.POST})
    ApiResultSet saveOrUpdateOptionField(@RequestBody SxOptionField sxOptionField);

    /**
     * @description 查询：选项-字段关系列表
     * @param valOid
     * @author meiyt
     * @date 2022/5/16
     * @return
     **/
    @ProcessFeignCalledResult
    @RequestMapping(value = "/queryOptionFieldListByValOid", method = {RequestMethod.GET})
    ApiResultSet queryOptionFieldListByValOid(@RequestParam(value = "valOid") String valOid);

    /**
     * @description 根据事项查询情形选项
     * @param serviceOid
     * @author meiyt
     * @date 2022/6/17
     * @return
     **/
    @ProcessFeignCalledResult
    @RequestMapping(value = "/queryOptionFieldListByServiceOid", method = {RequestMethod.GET})
    ApiResultSet<List<SxOptionField>> queryOptionFieldListByServiceOid(@RequestParam(value = "serviceOid") String serviceOid);

    /**
     * @description 查询列表
     * @param serviceOid
     * @param fieldTypeOid
     * @param formOid
     * @author meiyt
     * @date 2022/6/17
     * @return
     **/
    @ProcessFeignCalledResult
    @RequestMapping(value = "/queryFillFieldList", method = {RequestMethod.GET})
    ApiResultSet<List<SxFillField>> queryFillFieldList(@RequestParam(value = "serviceOid") String serviceOid,
                                                       @RequestParam(value = "fieldTypeOid", required = false) String fieldTypeOid,
                                                       @RequestParam(value = "formOid", required = false) String formOid);
}
