package com.zfsoft.superwindow.service.clzs;

import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.platform.utils.feign.ProcessFeignCalledResult;
import com.zfsoft.superwindow.data.clzs.FaModelTemplate;
import com.zfsoft.superwindow.data.clzs.MaterialCatalog;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @description: 识别模板接口
 * @author: liangss
 * @date: 2020-11-03 16:45:29
 */
@RequestMapping(value = "/faModelTemplate")
public interface FaModelTemplateService {

    @ProcessFeignCalledResult
    @RequestMapping(value = "/queryFaModelMaterialCatalog", method = {RequestMethod.GET})
    ApiResultSet<PageResult<MaterialCatalog>> queryFaModelMaterialCatalog(@RequestParam(value = "materialCatalog", required = false) MaterialCatalog materialCatalog,
                                                                          @RequestParam(value = "pageNum", required = false) Integer pageNum,
                                                                          @RequestParam(value = "pageSize", required = false) Integer pageSize);
    @ProcessFeignCalledResult
    @RequestMapping(value = "/queryFaModelMaterialCatalogWithPage", method = {RequestMethod.GET})
    ApiResultSet<PageResult<MaterialCatalog>> queryFaModelMaterialCatalogWithPage(@RequestParam(value = "catalogName", required = false) String catalogName,
                                                                                  @RequestParam(value = "catalogCode", required = false) String catalogCode,
                                                                                  @RequestParam(value = "pageNum", required = false) Integer pageNum,
                                                                                  @RequestParam(value = "pageSize", required = false) Integer pageSize);

    @ProcessFeignCalledResult
    @RequestMapping(value = "/queryFaModelTemplateList", method = {RequestMethod.GET})
    ApiResultSet<PageResult<FaModelTemplate>> queryFaModelTemplateList(@RequestParam(value = "faModelTemplate", required = false) FaModelTemplate faModelTemplate,
                                                                       @RequestParam(value = "pageNum", required = false) Integer pageNum,
                                                                       @RequestParam(value = "pageSize", required = false) Integer pageSize);

    @ProcessFeignCalledResult
    @RequestMapping(value = "/queryFaModelTemplateWithPage", method = {RequestMethod.GET})
    ApiResultSet<PageResult<FaModelTemplate>> queryFaModelTemplateWithPage(@RequestParam(value = "templateName", required = false) String templateName,
                                                                           @RequestParam(value = "templateCode", required = false) String templateCode,
                                                                           @RequestParam(value = "materialCatalogOid", required = false) String materialCatalogOid,
                                                                           @RequestParam(value = "pageNum", required = false) Integer pageNum,
                                                                           @RequestParam(value = "pageSize", required = false) Integer pageSize);

    @ProcessFeignCalledResult
    @RequestMapping(value = "/initFaModelTemplate", method = {RequestMethod.GET})
    ApiResultSet initFaModelTemplate(@RequestParam(value = "materialCatalogOid", required = false) String materialCatalogOid,
                                     @RequestParam(value = "id", required = false) String id,
                                     @RequestParam(value = "faModelTemplateOid", required = false) String faModelTemplateOid,
                                     @RequestParam(value = "guideType", required = false) String guideType);

    @ProcessFeignCalledResult
    @RequestMapping(value = "/initFaModelTemplateByResultOid", method = {RequestMethod.GET})
    ApiResultSet initFaModelTemplateByResultOid(@RequestParam(value = "resultOid", required = false) String resultOid,
                                                @RequestParam(value = "id", required = false) String id,
                                                @RequestParam(value = "faModelTemplateOid", required = false) String faModelTemplateOid,
                                                @RequestParam(value = "guideType", required = false) String guideType);

    @ProcessFeignCalledResult
    @RequestMapping(value = "/saveOrUpdateFaModelTemplate", method = {RequestMethod.GET})
    ApiResultSet saveOrUpdateFaModelTemplate(@RequestParam(value = "templateStr", required = false) String templateStr,
                                             @RequestParam(value = "blockArrayStr", required = false) String blockArrayStr,
                                             @RequestParam(value = "delBlockIds", required = false) String delBlockIds,
                                             @RequestParam(value = "saveType", required = false) String saveType);


    @ProcessFeignCalledResult
    @RequestMapping(value = "/del", method = {RequestMethod.GET})
    ApiResultSet del(@RequestParam(value = "id", required = false) String id);

    @ProcessFeignCalledResult
    @RequestMapping(value = "/able", method = {RequestMethod.GET})
    ApiResultSet able(@RequestParam(value = "id", required = false) String id,
                      @RequestParam(value = "materialCatalogOid", required = false) String materialCatalogOid,
                      @RequestParam(value = "ableFlag", required = false) String ableFlag);

    @ProcessFeignCalledResult
    @RequestMapping(value = "/cloneTemplate", method = {RequestMethod.GET})
    ApiResultSet cloneTemplate(@RequestParam(value = "id", required = false) String id,
                               @RequestParam(value = "faModelTemplateOid", required = false) String faModelTemplateOid);

    @ProcessFeignCalledResult
    @RequestMapping(value = "/getPulishedFaModelTemplateByCataOid", method = {RequestMethod.GET})
    ApiResultSet<FaModelTemplate> getPulishedFaModelTemplateByCataOid(@RequestParam(value = "materialCatalogOid", required = false) String materialCatalogOid);

    @ProcessFeignCalledResult
    @RequestMapping(value = "/queryModelTemplateList", method = {RequestMethod.GET})
    ApiResultSet<List<FaModelTemplate>> queryFaModelTemplateList
            (FaModelTemplate faModelTemplate, @RequestParam(value = "type", required = false) String type);

    @ProcessFeignCalledResult
    @RequestMapping(value = "/queryLicenseResultTemplateByOid", method = {RequestMethod.GET})
    ApiResultSet<FaModelTemplate> queryLicenseResultTemplateByOid(@RequestParam(value = "materialCatalogOid", required = false) String materialCatalogOid);

    /**
     *初始化打印设置
     * @param faModelTemplateOid
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/initPrintSetting", method = {RequestMethod.GET})
    ApiResultSet initPrintSetting(@RequestParam(value = "faModelTemplateOid", required = false) String faModelTemplateOid);

    /**
     *初始化打印预览 录入照面信息
     * @param materialCatalogOid
     * @param faModelTemplateOid
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/initPreviewPrintTemplate", method = {RequestMethod.GET})
    ApiResultSet initPreviewPrintTemplate(@RequestParam(value = "materialCatalogOid", required = false) String materialCatalogOid,
                                          @RequestParam(value = "faModelTemplateOid", required = false) String faModelTemplateOid);

    /**
     * 保存打印设置
     * @param printSetting
     * @param faModelTemplateOid
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/savePrintSet", method = {RequestMethod.GET})
    ApiResultSet savePrintSet(@RequestParam(value = "printSetting", required = false) String printSetting,
                              @RequestParam(value = "faModelTemplateOid", required = false) String faModelTemplateOid) throws Exception;

}
