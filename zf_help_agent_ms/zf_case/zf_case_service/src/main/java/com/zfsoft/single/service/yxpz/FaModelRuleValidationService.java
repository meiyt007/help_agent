package com.zfsoft.single.service.yxpz;

import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.utils.feign.ProcessFeignCalledResult;
import com.zfsoft.single.data.yxpz.FaModelRuleValidation;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @description: 规则验证接口
 * @author: liangss
 * @date: 2020-11-03 16:45:29
 */
@RequestMapping(value = "/faModelRuleValidation")
public interface FaModelRuleValidationService {



    @ProcessFeignCalledResult
    @RequestMapping(value = "/initFaModelRule", method = {RequestMethod.POST})
    ApiResultSet initFaModelRule(@RequestParam(value = "serviceOid", required = false) String serviceOid);


    @ProcessFeignCalledResult
    @RequestMapping(value = "/saveOrUpdate", method = {RequestMethod.POST})
    ApiResultSet saveOrUpdate(@RequestBody List<FaModelRuleValidation> faModelRuleValidationList);

    @ProcessFeignCalledResult
    @RequestMapping(value = "/initAll", method = {RequestMethod.POST})
    ApiResultSet initAll();

    @ProcessFeignCalledResult
    @RequestMapping(value = "/queryFaModelRuleValidationList", method = {RequestMethod.POST})
    ApiResultSet<List<FaModelRuleValidation>> queryFaModelRuleValidationList(@RequestBody FaModelRuleValidation faModelRuleValidation);



    @ProcessFeignCalledResult
    @RequestMapping(value = "/initFaModelRuleValidation", method = {RequestMethod.POST})
    ApiResultSet initFaModelRuleValidation(@RequestParam(value = "serviceOid", required = false) String serviceOid);




    @ProcessFeignCalledResult
    @RequestMapping(value = "/queryMaterialAndCataAndElementTree", method = {RequestMethod.POST})
    ApiResultSet queryMaterialAndCataAndElementTree(@RequestParam(value = "serviceOid", required = false) String serviceOid);



    /***
    * @Description:  查询事项材料目录树
    * @Author:liangss
    * @Date:2021/11/24
    * @Param: [serviceOid]
    */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/queryMaterialElementTreeSelect", method = {RequestMethod.POST})
    ApiResultSet queryMaterialElementTreeSelect(@RequestParam(value = "serviceOid", required = false) String serviceOid);


    /***
    * @Description:保存更新电子表单规则
    * @Author:liangss
    * @Date:2021/11/23
    * @Param: [faModelRuleValidationList]
    */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/saveOrUpdateFaModelRuleValidation", method = {RequestMethod.POST})
    ApiResultSet saveOrUpdateFaModelRuleValidation(@RequestBody List<FaModelRuleValidation> faModelRuleValidationList);

    /***
    * @Description: 查询规则通过类型
    * @Author:liangss
    * @Date:2021/11/24
    * @Param: [faModelRuleValidation]
    */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/queryFaModelRuleValidationListByRuleType", method = {RequestMethod.POST})
    ApiResultSet<List<FaModelRuleValidation>> queryFaModelRuleValidationListByRuleType(@RequestBody FaModelRuleValidation faModelRuleValidation);

    /***
     * @Description: 根据目录oid更新材料规则
     * @Author:liangss
     * @Date:2021/12/1
     * @Param: [comboDirectoryOid, materialCatalogOids]
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/updateFaModelRuleValidation", method = {RequestMethod.POST})
    ApiResultSet updateFaModelRuleValidation(@RequestParam(value = "serviceOid", required = false) String serviceOid, @RequestParam(value = "materialCatalogOids", required = false) String materialCatalogOids) throws Exception;



    /***
    * @Description: 查询关联的数据
    * @Author:liangss
    * @Date:2021/12/7
    * @Param: [ruleType, thanTemplateMetadataOid, thanTemplateMetadataCode]
    */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/getFaModelRuleValidationList", method = {RequestMethod.POST})
    ApiResultSet getFaModelRuleValidationList(
            @RequestParam(value = "ruleType", required = false) String ruleType,
            @RequestParam(value = "thanTemplateMetadataOid", required = false) String thanTemplateMetadataOid,
            @RequestParam(value = "thanTemplateMetadataCode", required = false) String thanTemplateMetadataCode) throws Exception;

}
