package com.zfsoft.superwindow.service.clzs;

import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.platform.utils.feign.ProcessFeignCalledResult;
import com.zfsoft.superwindow.data.clzs.MaterialCatalog;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @（#）: MaterialCategoryService
 * @description: 材料目录管理接口
 * @author: liangss
 * @date: 2020-11-03 16:45:29
 */
@RequestMapping("/materialCatalog")
public interface MaterialCatalogService {
    @ProcessFeignCalledResult
    @RequestMapping(value = "/queryMaterialCatalogWithPage", method = {RequestMethod.GET})
    ApiResultSet<PageResult<MaterialCatalog>> queryMaterialCatalogWithPage(@RequestParam(value = "catalogName", required = false) String catalogName,
                                                                           @RequestParam(value = "catalogCode", required = false) String catalogCode,
                                                                           @RequestParam(value = "pageNum", required = false) Integer pageNum,
                                                                           @RequestParam(value = "pageSize", required = false) Integer pageSize);
    @ProcessFeignCalledResult
    @RequestMapping(value = "/initAll", method = {RequestMethod.POST})
    ApiResultSet initAll();

    @ProcessFeignCalledResult
    @RequestMapping(value = "/saveOrUpdate", method = {RequestMethod.POST})
    ApiResultSet saveOrUpdate(@RequestBody MaterialCatalog materialCatalog);

    @ProcessFeignCalledResult
    @RequestMapping(value = "/saveOrUpdateMaterialCatalogAndSubitem", method = {RequestMethod.POST})
    ApiResultSet saveOrUpdateMaterialCatalogAndSubitem(@RequestBody MaterialCatalog materialCatalog) throws Exception;

    @ProcessFeignCalledResult
    @RequestMapping(value = "/getMaterialCatalogAndSubitem", method = {RequestMethod.GET})
    ApiResultSet getMaterialCatalogAndSubitem(@RequestParam(value = "id", required = false) String id);

    @ProcessFeignCalledResult
    @RequestMapping(value = "/del", method = {RequestMethod.POST})
    ApiResultSet del(@RequestParam(value = "id", required = false) String id);

    @ProcessFeignCalledResult
    @RequestMapping(value = "/getMaterialCatalogList", method = {RequestMethod.POST})
    ApiResultSet<List<MaterialCatalog>> querySysConfigListByParentCode();

    @ProcessFeignCalledResult
    @RequestMapping(value = "/saveOrUpdateMater", method = {RequestMethod.POST})
    ApiResultSet saveOrUpdateMater(@RequestBody String json);

    @ProcessFeignCalledResult
    @RequestMapping(value = "/checkIsReationSx", method = {RequestMethod.GET})
    ApiResultSet checkIsReationSx(@RequestParam(value = "materialCatalogOid", required = false) String materialCatalogOid);

    @ProcessFeignCalledResult
    @RequestMapping(value = "/queryList", method = {RequestMethod.GET})
    ApiResultSet<List<MaterialCatalog>> queryList(@RequestParam(value = "materialParentOid", required = false) String materialParentOid);

    @ProcessFeignCalledResult
    @RequestMapping( value = "/getMaterialCatalogOid",method = {RequestMethod.GET})
    ApiResultSet<MaterialCatalog> getMaterialCatalogOid(@RequestParam(value = "materialCatalogOid", required = false) String materialCatalogOid);


    @ProcessFeignCalledResult
    @RequestMapping(value = "/getMaterialCatalogByOid", method = {RequestMethod.GET})
    ApiResultSet<MaterialCatalog> getMaterialCatalogByOid(@RequestParam(value = "materialCatalogOid", required = false) String materialCatalogOid);

   /* @ProcessFeignCalledResult
    @RequestMapping(value = "/queryListMaterialCatalog", method = {RequestMethod.POST})
    ApiResultSet<List<MaterialCatalog>> queryListMaterialCatalog(MaterialCatalog materialCatalog);
*/
    @ProcessFeignCalledResult
    @RequestMapping(value = "/queryListMaterialCatalog", method = {RequestMethod.POST})
    ApiResultSet<List<MaterialCatalog>> queryListByMaterialCatalog(MaterialCatalog materialCatalog);

    @ProcessFeignCalledResult
    @RequestMapping(value = "/checkMaterialCatalogRepeat", method = {RequestMethod.GET})
    ApiResultSet checkMaterialCatalogRepeat(
            @RequestParam(value = "materialCatalogOid", required = false) String materialCatalogOid,
            @RequestParam(value = "catalogName", required = false) String catalogName);


    /**
     * 查询子项列表
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/querySubitemMaterialCatalogList", method = {RequestMethod.GET})
    ApiResultSet<List<MaterialCatalog>> querySubitemMaterialCatalogList();


    /**
     * 目录区划树
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/queryAllCatalogList", method = {RequestMethod.GET})
    ApiResultSet<List<MaterialCatalog>> queryAllCatalogList();

    /**
     * 根据目录主键查询所有的目录信息
     * @param materialCatalogOids
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/queryAllCatalogByCatalogOidsList", method = {RequestMethod.GET})
    ApiResultSet<List<MaterialCatalog>> queryAllCatalogByCatalogOidsList(@RequestParam(value = "materialCatalogOids") List<String> materialCatalogOids);

}

