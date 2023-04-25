package com.zfsoft.service.sxService.service;

import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.platform.utils.feign.ProcessFeignCalledResult;
import com.zfsoft.service.sxService.data.FieldTree;
import com.zfsoft.service.sxService.data.SxFillLabel;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @ClassName SxFillLabelService
 * @Date 2021/7/14 15:32
 **/
@RequestMapping("/matter/fillLabel")
public interface SxFillLabelService {

    /***
     * @param contentOid
     * @description: 查询事项表单标签分页
     * @return: com.zfsoft.platform.common.data.ApiResultSet
     * @author: xiayj
     * @date: 2021/7/14
     */
    @RequestMapping(value = "/queryFillLabelPage", method = {RequestMethod.GET})
    ApiResultSet<PageResult<SxFillLabel>> queryFillLabelPage(
            @RequestParam(value = "contentOid") String contentOid,
            @RequestParam(value = "labelName", required = false) String labelName,
            @RequestParam(value = "labelCode", required = false) String labelCode,
            @RequestParam(value = "typeOid", required = false) String typeOid,
            @RequestParam(value = "pageNum", required = false) Integer pageNum,
            @RequestParam(value = "pageSize", required = false) Integer pageSize);


    /***
     * @param labelOid
     * @description: 获取标签详情
     * @return: com.zfsoft.platform.common.data.ApiResultSet<com.zfsoft.formConfig.data.SxFillLabel>
     * @author: xiayj
     * @date: 2021/7/14
     */
    @RequestMapping(value = "/getFillLabel", method = {RequestMethod.GET})
    ApiResultSet<SxFillLabel> getFillLabel(@RequestParam(value = "labelOid") String labelOid);

    /***
     * @param sxFillLabel
     * @description: 保存更新标签信息
     * @return: com.zfsoft.platform.common.data.ApiResultSet
     * @author: xiayj
     * @date: 2021/7/14
     */
    @RequestMapping(value = "/saveFillLabel", method = {RequestMethod.POST})
    ApiResultSet saveOrUpdateFillLabel(@RequestBody SxFillLabel sxFillLabel);

    /**
     * @param labelOid
     * @description: 删除标签（必须保证标签没有关联字段）
     * @return: com.zfsoft.platform.common.data.ApiResultSet
     * @author: xiayj
     * @date: 2021/7/14
     */
    @RequestMapping(value = "/deleteFillLabel", method = {RequestMethod.POST})
    ApiResultSet<String> deleteFillLabel(@RequestParam(value = "labelOid", required = false) String labelOid);



    /***
     * @description: 查看分类标签树
     * @return: com.zfsoft.platform.common.data.ApiResultSet<java.util.List < com.zfsoft.dataChange.data.FieldTree>>
     * @author: xiayj
     * @date: 2021/7/16
     */
    @RequestMapping( value = "/querySxFieldTypeAndLabelTree",method = {RequestMethod.GET})
    ApiResultSet<List<FieldTree>> querySxFieldTypeAndLabelTree(
            @RequestParam(value = "serviceOid") String serviceOid);


    /***
     *
     * 根据事项查询标签列表
     * @param serviceOid
     * @param typeOid
     * @return: com.zfsoft.platform.common.data.ApiResultSet<java.util.List < com.zfsoft.formConfig.data.SxFillLabel>>
     * @author: xiayj
     * @date: 2021/8/4
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/querySxFillLabelList", method = {RequestMethod.GET})
    ApiResultSet<List<SxFillLabel>> querySxFillLabelList(
            @RequestParam(value = "serviceOid", required = false) String serviceOid,
            @RequestParam(value = "typeOid", required = false) String typeOid);






    /***
    * @Description:  根据事项查询类型标签字段三级树
    * @Author:liangss
    * @Date:2021/11/23
    * @Param: [serviceOid]
    */
    @RequestMapping( value = "/querySxFieldTypeAndLabelAndSxFillFieldTree",method = {RequestMethod.GET})
    ApiResultSet<List<FieldTree>> querySxFieldTypeAndLabelAndSxFillFieldTree(
            @RequestParam(value = "serviceOid") String serviceOid);





}
