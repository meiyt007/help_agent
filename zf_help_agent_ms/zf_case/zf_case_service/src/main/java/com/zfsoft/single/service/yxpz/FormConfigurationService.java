package com.zfsoft.single.service.yxpz;

import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.utils.feign.ProcessFeignCalledResult;
import com.zfsoft.single.data.yxpz.SxSerForm;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author wangns
 * @description 表单配置 service
 * @date 2020/11/5 11:42
 * @copyright 上海卓繁信息技术股份有限公司版权所有
 */
@RequestMapping(value = "/formRunConfiguration")
public interface FormConfigurationService {

    /**
     * 保存
     * @param serForm
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping( value = "/save",method = {RequestMethod.POST})
    ApiResultSet save(@RequestBody SxSerForm serForm);



    /**
     * 获取是事项关联表单列表
     * @param serviceOid
     * @param pageNum
     * @param pageSize
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping( value = "/getSerFormsByServiceOid/{serviceOid}/{pageNum}/{pageSize}",method = {RequestMethod.GET})
    ApiResultSet getSerFormsByServiceOid(@PathVariable String serviceOid, @PathVariable Integer pageNum, @PathVariable Integer pageSize);


    /**
     * 根据sxSerFormOid查询sxSerForm
     * @param sxSerFormOid
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping( value = "/getSxSerFormByOid",method = {RequestMethod.GET})
    ApiResultSet getSxSerFormByOid(@RequestParam(value = "sxSerFormOid") String sxSerFormOid);

    /**
     * 启禁用
     * @param oid
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping( value = "/sxSerFormAble",method = {RequestMethod.POST})
    ApiResultSet sxSerFormAble(String oid);

    /**
     * 批量更新
     * @param sxSerForms
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping( value = "/saveSxSerFormByList",method = {RequestMethod.POST})
    ApiResultSet saveSxSerFormByList(@RequestBody List<SxSerForm> sxSerForms);

    /**
     * 单条更新
     * @param oid
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping( value = "/updateSxSerForm",method = {RequestMethod.POST})
    ApiResultSet updateSxSerFormByList(@RequestParam(value = "oid", required = false) String oid);

    /**
     * 根据事项主键查询启用的表单
     * @param serviceOid
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping( value = "/selectBySxSerFormByServiceOid",method = {RequestMethod.GET})
    ApiResultSet selectBySxSerFormByServiceOid(@RequestParam(value = "serviceOid") String serviceOid);

    /**
     * 根据条件查询表单配置
     * @param sxSerForm
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping( value = "/selectBySxSerForm",method = {RequestMethod.GET})
    ApiResultSet selectBySxSerForm(SxSerForm sxSerForm);

    @ProcessFeignCalledResult
    @RequestMapping( value = "/getSerFormsByServiceOid",method = {RequestMethod.POST})
    ApiResultSet getSerFormsByServiceOid(SxSerForm sxSerForm,
                                         @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
                                         @RequestParam(value = "pageSize", required = false) Integer pageSize);



    /**
     * 根据事项oid主键获取分页数据--实施清单目录
     * @param oidStrs
     * @param pageNum
     * @param pageSize
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping( value = "/getSerFormsByServiceOidStrs/{oidStrs}/{comboDirectoryOid}/{pageNum}/{pageSize}",method = {RequestMethod.GET})
    ApiResultSet getSerFormsByServiceOidStrs(@PathVariable String oidStrs, @PathVariable String comboDirectoryOid, @PathVariable Integer pageNum, @PathVariable Integer pageSize);


    /**
     * 删除
     * @param oid
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping( value = "/sxSerFormDel",method = {RequestMethod.POST})
    ApiResultSet sxSerFormDel(String oid);



    /**
     * 查询电子表单配置模板列表
     *
     * @author: wangwg
     * @date: 2021-07-14
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping( value = "/queryPublishFormList",method = {RequestMethod.GET})
    ApiResultSet queryPublishFormList();


    /**
     * 分页查询电子表单配置模板列表
     *
     * @author: wangwg
     * @date: 2021-07-24
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping( value = "/queryElectronicsFormPageList",method = {RequestMethod.GET})
    ApiResultSet queryElectronicsFormPageList(@RequestParam(value = "queryName", required = false) String queryName,
                                              @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
                                              @RequestParam(value = "pageSize", required = false) Integer pageSize);


}
