
package com.zfsoft.single.service.yxpz;

import com.alibaba.fastjson.JSONArray;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.utils.feign.ProcessFeignCalledResult;
import com.zfsoft.single.data.yxpz.FormFieldRelConfig;
import com.zfsoft.single.data.yxpz.vo.CascaderTreeVo;
import com.zfsoft.single.data.yxpz.vo.CopyVo;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author: dongxl
 * @create: 2021-11-25
 * @description: 事项字段填充配置
 */
@RequestMapping(value = "/formFieldRelConfig")
public interface FormFieldRelConfigService {
    /**
     * 分页查询配置信息
     * @param formFieldRelConfig
     * @param pageNum
     * @param pageSize
     * @return
     */
    @ProcessFeignCalledResult
    @GetMapping(value = "/queryPageList")
    ApiResultSet queryPageList(FormFieldRelConfig formFieldRelConfig,
                               @RequestParam(value = "pageNum", required = false, defaultValue = "1") Integer pageNum,
                               @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize);

    /**
     * 保存或更新配置信息
     * @param formFieldRelConfig
     * @return
     */
    @ProcessFeignCalledResult
    @PostMapping(value="/saveOrUpdate")
    ApiResultSet<String> saveOrUpdate(@RequestBody @Validated FormFieldRelConfig formFieldRelConfig);

    /**
     * 根据主键查询单个配置信息
     * @param id
     * @return
     */
    @ProcessFeignCalledResult
    @GetMapping(value="/getOneRelConfig")
    ApiResultSet<FormFieldRelConfig> getOneRelConfig(@RequestParam(value = "id") Long id);

    /**
     * 删除信息
     * @param id
     * @return
     */
    @ProcessFeignCalledResult
    @GetMapping(value="/delRelConfig")
    ApiResultSet delRelConfig(@RequestParam(value = "id") Long id);

    /**
     * 根据事项主键查询事项材料绑定的证照目录和目录的照面元素信息
     * @param serviceOid
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping( value = "/queryElecAndElementTree",method = {RequestMethod.GET})
    ApiResultSet<List<CascaderTreeVo>> queryElecAndElementTree(@RequestParam(value = "serviceOid") String serviceOid);

    /**
     * 查询事项颗粒化材料配置的智审目录和目录元素
     * @param serviceOid
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping( value = "/queryCatalogAndCataLogElementTree",method = {RequestMethod.GET})
    ApiResultSet<List<CascaderTreeVo>> queryCatalogAndCataLogElementTree(@RequestParam(value = "serviceOid") String serviceOid);


    @ProcessFeignCalledResult
    @GetMapping(value="/checkHasRepeat")
    ApiResultSet checkHasRepeat(@RequestParam(value = "serviceOid") String serviceOid,
                                @RequestParam(value = "fillType") Integer fillType,
                                @RequestParam(value = "fillFieldOid") String fillFieldOid,
                                @RequestParam(value = "oid") String oid);

    @ProcessFeignCalledResult
    @GetMapping(value="/getAllBasicFieldByOid")
    ApiResultSet<Map<String,Object>> getAllBasicFieldByOid(@RequestParam(value = "caseOid") String caseOid);

    /**
     *  基础表单和电子表单关联字段
     * @param serviceOid
     * @return
     */
    @ProcessFeignCalledResult
    @GetMapping(value="/getBasicAndFormFieldRel")
    ApiResultSet<JSONArray> getBasicAndFormFieldRel(@RequestParam(value = "serviceOid") String serviceOid);

    /**
     *  查询是否有关联数据
     * @param thanTemplateMetadataOid
     * @return
     * @throws Exception
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/checkIsHasRel", method = {RequestMethod.POST})
    ApiResultSet checkIsHasRel(@RequestParam(value = "thanTemplateMetadataOid", required = false) String thanTemplateMetadataOid);



    /**
     * 复制字段值填充（单办）
     * @param vo
     * @return
     */
    @ProcessFeignCalledResult
    @PostMapping(value="/copyFormFieldRelConfig")
    ApiResultSet<String> copyFormFieldRelConfig(@RequestBody CopyVo vo);

}
