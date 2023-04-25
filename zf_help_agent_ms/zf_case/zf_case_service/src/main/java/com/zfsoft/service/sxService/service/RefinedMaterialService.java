package com.zfsoft.service.sxService.service;

import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.utils.feign.ProcessFeignCalledResult;
import com.zfsoft.service.sxService.data.RefinedMaterial;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @ClassName refinedMaterialService
 * @Description 细化材料信息服务定义接口
 * @Author liangss
 * @Date 2021-07-12
 * @Version V1.0
 **/

@RequestMapping("/affair/refindeMaterial")
public interface RefinedMaterialService {

    /**
     * 根据事项材料id获取细化材料列表
     * @param materialOid
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping( value = "/getRefinedMaterialListByMaterialOid",method = {RequestMethod.GET})
    ApiResultSet<List<RefinedMaterial>> getRefinedMaterialListByMaterialOid(@RequestParam("materialOid") String materialOid);


    /**
     * 根据业务主键获取细化材料列表
     * @param oid
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping( value = "/getRefinedMaterialByOid",method = {RequestMethod.GET})
    ApiResultSet<RefinedMaterial>  getRefinedMaterialByOid(@RequestParam("oid") String oid);






    /**
     * 根据列表根据细化材料
     * @param refinedMaterialList
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping( value = "/updateRefinedMaterialList",method = {RequestMethod.POST})
    ApiResultSet updateRefinedMaterialList(@RequestBody List<RefinedMaterial> refinedMaterialList);

    @ProcessFeignCalledResult
    @RequestMapping( value = "/saveOrUpdateRefinedMaterial",method = {RequestMethod.POST})
    ApiResultSet saveOrUpdateRefinedMaterial(@RequestBody RefinedMaterial refinedMaterial);


    @ProcessFeignCalledResult
    @RequestMapping( value = "/delete",method = {RequestMethod.GET})
    ApiResultSet delete(String id);

    /**
     * 根据事项主键查询细化材料信息
     * @param serviceOid
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping( value = "/getOnlyRefinedMaterialByServiceOid",method = {RequestMethod.GET})
    ApiResultSet<List<RefinedMaterial>>  getOnlyRefinedMaterialByServiceOid(@RequestParam("serviceOid") String serviceOid);

    /**
     * 根据一件事主键查询细化材料信息
     * @param comboDirectoryOid
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping( value = "/getOnlyRefinedMaterialByComboDirectoryOid",method = {RequestMethod.GET})
    ApiResultSet<List<RefinedMaterial>>  getOnlyRefinedMaterialByComboDirectoryOid(@RequestParam("comboDirectoryOid") String comboDirectoryOid);

}
