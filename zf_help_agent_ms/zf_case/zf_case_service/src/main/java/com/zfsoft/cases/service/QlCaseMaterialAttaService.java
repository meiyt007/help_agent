package com.zfsoft.cases.service;

import com.zfsoft.cases.data.QlCaseMaterial;
import com.zfsoft.cases.data.QlCaseMaterialAtta;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.utils.feign.ProcessFeignCalledResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * @（#）: QlCaseMaterialAttaService
 * @description: 材料附件信息服务定义接口
 * @author: wangwg
 * @date: 2020/10/23
 * @version: 1.0
 * @Copyright: 上海卓繁信息技术股份有限公司版权所有
 */
@RequestMapping("/qlCaseMaterialAttaService")
public interface QlCaseMaterialAttaService {
    
    /**
     * 保存附件材料信息
     *
     * @author wangwg
     * @date 2020/10/26
     * @param object
     * @return com.zfsoft.platform.common.data.ApiResultSet<java.util.List<java.util.Map<java.lang.String,java.lang.String>>>
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/saveQlCaseMaterialAtta",method = {RequestMethod.POST})
    ApiResultSet<List<Map<String, String>>> saveQlCaseMaterialAtta(@RequestBody Object object);

    /**
     *办件材料业务主键查询附件材料信息
     *
     * @author wangwg
     * @date 2020/10/26
     * @param caseMaterialOid
     * @return com.zfsoft.platform.common.data.ApiResultSet<java.util.List<com.zfsoft.data.QlCaseMaterialAtta>>
     **/
    @ProcessFeignCalledResult
    @RequestMapping(value = "/queryQlCaseMaterialAttaByCaseMaterialOid", method = {RequestMethod.GET})
    ApiResultSet<List<QlCaseMaterialAtta>> queryQlCaseMaterialAttaByCaseMaterialOid(@RequestParam("caseMaterialOid") String caseMaterialOid);

    @ProcessFeignCalledResult
    @RequestMapping(value = "/deleteQlCaseMaterialAttaByCaseMaterialOid", method = {RequestMethod.GET})
    ApiResultSet deleteQlCaseMaterialAttaByCaseMaterialOid(@RequestParam("caseMaterialOid") String caseMaterialOid);

    @ProcessFeignCalledResult
    @RequestMapping( value = "/saveQlCaseMaterialAttaList",method = {RequestMethod.POST})
    ApiResultSet<List<Map<String, String>>> saveQlCaseMaterialAttaList(@RequestBody List<QlCaseMaterialAtta> qlCaseMaterialAttaList);


    @ProcessFeignCalledResult
    @RequestMapping(value = "/queryQlCaseMaterialAttaByOid", method = {RequestMethod.GET})
    ApiResultSet<QlCaseMaterialAtta> queryQlCaseMaterialAttaByOid(@RequestParam("materialAttaOid") String materialAttaOid);


    @ProcessFeignCalledResult
    @RequestMapping(value = "/queryQlCaseMaterialAttaByCatalogOid", method = {RequestMethod.GET})
    ApiResultSet<QlCaseMaterialAtta> queryQlCaseMaterialAttaByCatalogOid(@RequestParam("caseMaterialOid") String caseMaterialOid, @RequestParam("materialCatalogOid") String materialCatalogOid);


    @ProcessFeignCalledResult
    @RequestMapping( value = "/saveQlCaseMaterialList",method = {RequestMethod.POST})
    ApiResultSet<List<Map<String, String>>> saveQlCaseMaterialList(@RequestBody List<QlCaseMaterial> qlCaseMaterialList);

    @RequestMapping(value = "/queryQlCaseMaterialAttaByCatalogOidAndAttaOid", method = {RequestMethod.GET})
    ApiResultSet<QlCaseMaterialAtta> queryQlCaseMaterialAttaByCatalogOidAndAttaOid(@RequestParam("caseMaterialOid") String caseMaterialOid, @RequestParam("attaOid") String attaOid);


    /**
     * 根据materialAttaOid删除关联附件
     * @param materialAttaOid
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/deleteByOid", method = {RequestMethod.GET})
    ApiResultSet deleteByOid(@RequestParam("materialAttaOid") String materialAttaOid);




    /**
     * 根据精细化材料oid查询办件关联列表
     * @param refinedMaterialOid
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/queryQlCaseMaterialAttaByRefinedMaterialOid", method = {RequestMethod.GET})
    ApiResultSet<List<QlCaseMaterialAtta>> queryQlCaseMaterialAttaByRefinedMaterialOid(@RequestParam("refinedMaterialOid") String refinedMaterialOid, @RequestParam("caseMaterialOid") String caseMaterialOid);


    /**
     * 更新修改办件材料附件
     * @Description:liangss
     * @param qlCaseMaterialList
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping( value = "/saveOrUpdateCaseMaterialAttaList",method = {RequestMethod.POST})
    ApiResultSet saveOrUpdateCaseMaterialAttaList(@RequestBody List<QlCaseMaterial> qlCaseMaterialList);


    /***
    * @Description: 更新办件附件用于补正
    * @Author:liangss
    * @Date:2021/8/9
    * @Param: [qlCaseMaterialAttaList]
    */
    @ProcessFeignCalledResult
    @RequestMapping( value = "/updateCaseMaterialAttaList",method = {RequestMethod.POST})
    ApiResultSet updateCaseMaterialAttaList(@RequestBody List<QlCaseMaterialAtta> qlCaseMaterialAttaList);

    /**
     * @description 网站端更新材料附件
     * @param qlCaseMaterialList
     * @author meiyt
     * @date 2022/6/7
     * @return
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/saveOrUpdateCaseMaterialAttaListForWeb",method = {RequestMethod.POST})
    ApiResultSet saveOrUpdateCaseMaterialAttaListForBrowser(@RequestBody List<QlCaseMaterial> qlCaseMaterialList);
}
