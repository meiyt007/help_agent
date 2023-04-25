package com.zfsoft.service.sxService.service;

import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.utils.feign.ProcessFeignCalledResult;
import com.zfsoft.service.sxService.data.RefinedMaterial;
import com.zfsoft.service.sxService.data.SxMaterialFormTemp;
import com.zfsoft.service.sxService.data.SxServiceMaterial;
import com.zfsoft.service.sxSituation.data.vo.ServiceMaterialVo;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @ClassName SxServiceMaterialService
 * @Description 实施清单申请材料信息服务定义接口
 * @Author wangxl
 * @Date 2020-10-26
 * @Version V1.0
 **/
@RequestMapping("/affair/serviceMaterial")
public interface SxServiceMaterialService {
    /***
     * @Description: 根据细化材料样本更新材料样本
     * @Author:liangss
     * @Date:2021/11/18
     * @Param: [materialOid]
     */
    @ProcessFeignCalledResult
    @RequestMapping( value = "/saveOrUpdateMaterialSample",method = {RequestMethod.GET})
    ApiResultSet saveOrUpdateMaterialSample(@RequestParam("materialOid") String materialOid) throws Exception;
    /**
     * @description:  根据业务主健获取实施清单扩展信息
     * @param materialOid 业务主键
     * @author: wangxl
     * @Date: 2020/10/26
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/getSxServiceMaterialByOid/{materialOid}",method = {RequestMethod.GET})
    ApiResultSet<SxServiceMaterial>  getSxServiceMaterialByOid(@PathVariable("materialOid") String materialOid);

    /**
     * 根据事项OID获取材料列表
     * @param serviceOid
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping( value = "/getSxServiceMaterialListByServiceOid/{serviceOid}",method = {RequestMethod.GET})
    ApiResultSet<List<SxServiceMaterial>> getSxServiceMaterialListByServiceOid(@PathVariable("serviceOid") String serviceOid);

    /**
     * @description:  根据所属事项主健获取实施清单材料信息
     * @param serviceOid 所属事项主健
     * @author: wangns
     * @Date: 2020/10/26
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/getSxServiceMaterialByServiceOid",method = {RequestMethod.GET})
    ApiResultSet<List<SxServiceMaterial>>  getSxServiceMaterialByServiceOid(@RequestParam(value = "serviceOid", required = false) String serviceOid);


    /**
     * @description:  根据所属事项主健更新实施清单材料信息
     * @author: wangns
     * @Date: 2020/11/7
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/updateByMaterialOid",method = {RequestMethod.POST})
    ApiResultSet updateByMaterialOid(@RequestBody String param);



    /**
     * @description:  根据事项 情形 选择获取材料和精细化材料
     * @param serviceOid 所属事项主健
     * @param optionValOids 选项主键
     * @author: wangwg
     * @Date: 2020/11/27
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/getSituationMaterialListByOids",method = {RequestMethod.GET})
    ApiResultSet<List<ServiceMaterialVo>>  getSituationMaterialListByOids(@RequestParam(value = "serviceOid", required = false) String serviceOid,
                                                                          @RequestParam(value = "optionValOids", required = false) String optionValOids);


    /**
     * 根据材料目录主键获取材料列表
     * @param materialCatalogOid
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping( value = "/getSxServiceMaterialListByMaterialCatalogOid",method = {RequestMethod.GET})
    ApiResultSet<List<SxServiceMaterial>> getSxServiceMaterialListByMaterialCatalogOid(@RequestParam(value = "materialCatalogOid", required = false) String materialCatalogOid);



    /**
     * @description:  根据所属事项主健更新实施清单材料信息
     * @author: wangns
     * @Date: 2020/11/7
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/updateByMaterial",method = {RequestMethod.POST})
    ApiResultSet updateByMaterial(@RequestBody SxServiceMaterial sxServiceMaterial);


    /**
     *  根据事项id查询，事项下的细化材料查询接口
     * @param  serviceId
     *
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping( value = "/getRefinedMaterialByServiceId",method = {RequestMethod.GET})
    ApiResultSet<List<RefinedMaterial>> getRefinedMaterialByServiceId(@RequestParam(value = "serviceId", required = false) String serviceId);



    @ProcessFeignCalledResult
    @RequestMapping( value = "/getSxServiceMaterialPageListByServiceOid",method = {RequestMethod.GET})
    ApiResultSet getSxServiceMaterialPageListByServiceOid(@RequestParam(value = "pageNum", required = false) Integer pageNum,
                                                          @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                                          @RequestParam(value = "searchName", required = false) String searchName,
                                                          @RequestParam(value = "serviceOid", required = false) String serviceOid);

    @ProcessFeignCalledResult
    @RequestMapping( value = "/insertSxServiceMaterialService",method = {RequestMethod.POST})
    ApiResultSet insertSxServiceMaterialService(@RequestBody SxServiceMaterial sxServiceMaterial);

    @ProcessFeignCalledResult
    @RequestMapping( value = "/getSxServiceMaterialHasFileByOid/{materialOid}",method = {RequestMethod.GET})
    ApiResultSet<SxServiceMaterial>  getSxServiceMaterialHasFileByOid(@PathVariable("materialOid") String materialOid);

    @ProcessFeignCalledResult
    @RequestMapping( value = "/delSxServiceMaterialByOid/{materialOid}",method = {RequestMethod.GET})
    ApiResultSet  delSxServiceMaterialByOid(@PathVariable("materialOid") String materialOid);

    /**
     * @description 保存事项材料与表单模板关系
     * @param sxMaterialFormTemp
     * @author meiyt 
     * @date 2022/5/18
     * @return 
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/saveMaterialFormTemplate",method = {RequestMethod.POST})
    ApiResultSet saveMaterialFormTemplate(@RequestBody SxMaterialFormTemp sxMaterialFormTemp);

    /**
     * @description 根据事项主键和材料主键查询表单模板
     * @author meiyt
     * @date 2022/5/18
     * @return 
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/getMaterialFormTemplate",method = {RequestMethod.GET})
    ApiResultSet<SxMaterialFormTemp> getMaterialFormTemplate(@RequestParam(value = "sxServiceOid", required = false) String sxServiceOid,
                                                             @RequestParam(value = "materialOid", required = false) String materialOid);
    /**
     * @description 查询事项所有表单模板
     * @param sxServiceOid
     * @author meiyt
     * @date 2022/5/25
     * @return
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/getServiceFormTemplateList",method = {RequestMethod.GET})
    ApiResultSet<List<SxMaterialFormTemp>> getServiceFormTemplateList(@RequestParam(value = "sxServiceOid", required = false) String sxServiceOid);

    /**
     * @description:  专属指南
     * @param serviceOid 所属事项主健
     * @param optionValOids 选项主键
     * @author: wangwg
     * @Date: 2020/11/27
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/getExclusiveGuide",method = {RequestMethod.GET})
    ApiResultSet<Map<String, Object>>  getExclusiveGuide(@RequestParam(value = "serviceOid", required = false) String serviceOid,
                                                         @RequestParam(value = "optionValOids", required = false) String optionValOids);
}
