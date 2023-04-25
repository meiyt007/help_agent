package com.zfsoft.superwindow.service.clzs;

import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.platform.utils.feign.ProcessFeignCalledResult;
import com.zfsoft.superwindow.data.clzs.MaterialCatalogElement;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @（#）: MaterialCatalogElementService
 * @description: 材料目录元素管理接口
 * @author: liangss
 * @date: 2020-11-04 16:45:29
 */
@RequestMapping(value = "/materialCatalogElement")
public interface MaterialCatalogElementService {
    @ProcessFeignCalledResult
    @RequestMapping(value = "/queryMaterialCatalogElementWithPage", method = {RequestMethod.GET})
    ApiResultSet<PageResult<MaterialCatalogElement>> queryMaterialCatalogElementWithPage(@RequestParam(value = "elementCode", required = false) String elementCode,
                                                                                         @RequestParam(value = "elementName", required = false) String elementName,
                                                                                         @RequestParam(value = "materialCatalogOid", required = false) String materialCatalogOid,
                                                                                         @RequestParam(value = "pageNum", required = false) Integer pageNum,
                                                                                         @RequestParam(value = "pageSize", required = false) Integer pageSize);

    @ProcessFeignCalledResult
    @RequestMapping(value = "/getone", method = {RequestMethod.GET})
    ApiResultSet getone(@RequestParam(value = "id", required = false) String id);

    @ProcessFeignCalledResult
    @RequestMapping(value = "/saveOrUpdate", method = {RequestMethod.POST})
    ApiResultSet saveOrUpdate(@RequestBody MaterialCatalogElement materialCatalogElement);

    @ProcessFeignCalledResult
    @RequestMapping(value = "/delete", method = {RequestMethod.GET})
    ApiResultSet delete(@RequestParam(value = "id", required = false) String id);

    @ProcessFeignCalledResult
    @RequestMapping(value = "/checkIsRelation", method = {RequestMethod.GET})
    ApiResultSet checkIsRelation(@RequestParam(value = "materialCatalogElementOid", required = false) String materialCatalogElementOid);

    @ProcessFeignCalledResult
    @RequestMapping(value = "/queryList", method = {RequestMethod.POST})
    ApiResultSet<List<MaterialCatalogElement>> queryList(@RequestBody MaterialCatalogElement materialCatalogElement);


    @ProcessFeignCalledResult
    @RequestMapping(value = "/checkMaterialCatalogChlidRepeat", method = {RequestMethod.GET})
    ApiResultSet checkMaterialCatalogChlidRepeat(
            @RequestParam(value = "materialCatalogElementOid", required = false) String materialCatalogElementOid,
            @RequestParam(value = "materialParentOid", required = false) String materialParentOid,
            @RequestParam(value = "elementName", required = false) String elementName);


    @ProcessFeignCalledResult
    @RequestMapping(value = "/queryMaterialCatalogElementList", method = {RequestMethod.GET})
    ApiResultSet<List<MaterialCatalogElement>> queryMaterialCatalogElementList(@RequestParam(value = "materialCatalogOid", required = false) String materialCatalogOid);

    @ProcessFeignCalledResult
    @RequestMapping(value = "/queryMaterialCatalogElementByCatalogOids", method = {RequestMethod.GET})
    ApiResultSet<List<MaterialCatalogElement>> queryMaterialCatalogElementByCatalogOids(@RequestParam(value = "materialCatalogOids", required = false) List<String> materialCatalogOid);

}
