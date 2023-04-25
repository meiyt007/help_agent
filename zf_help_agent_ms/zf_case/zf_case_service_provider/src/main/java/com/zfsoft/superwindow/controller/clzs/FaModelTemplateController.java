package com.zfsoft.superwindow.controller.clzs;


import com.alibaba.fastjson.JSON;
import com.zfsoft.cases.feign.SysDictFeignService;
import com.zfsoft.microservice.settings.data.SysDict;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.superwindow.data.clzs.FaModelTemplate;
import com.zfsoft.superwindow.data.clzs.FaModelTemplateBlock;
import com.zfsoft.superwindow.data.clzs.MaterialCatalog;
import com.zfsoft.superwindow.data.clzs.MaterialCatalogElement;
import com.zfsoft.superwindow.dbaccess.data.DbMaterialCatalog;
import com.zfsoft.superwindow.manager.clzs.FaModelTemplateBlockManager;
import com.zfsoft.superwindow.manager.clzs.FaModelTemplateManager;
import com.zfsoft.superwindow.manager.clzs.MaterialCatalogElementManager;
import com.zfsoft.superwindow.manager.clzs.MaterialCatalogManager;
import com.zfsoft.superwindow.service.clzs.FaModelTemplateService;
import com.zfsoft.superwindow.util.FaStaticParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author: liangss
 * @create: 2020-11-07 17:01:29
 * @description: 识别模板控制层
 */
@Slf4j
@RestController
public class FaModelTemplateController implements FaModelTemplateService {
    //模板
    @Resource
    private FaModelTemplateManager faModelTemplateManager;
    //材料目录
    @Resource
    private MaterialCatalogManager materialCatalogManager;
    //材料元素
    @Resource
    private MaterialCatalogElementManager materialCatalogElementManager;
    //识别模板区块
    @Resource
    private FaModelTemplateBlockManager faModelTemplateBlockManager;
    @Resource
    private SysDictFeignService sysDictFeignService;


    /**
     * 识别模板-材料目录列表
     * @param materialCatalog
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public ApiResultSet<PageResult<MaterialCatalog>> queryFaModelMaterialCatalog(MaterialCatalog materialCatalog, Integer pageNum, Integer pageSize) {
        PageResult<MaterialCatalog> pageResult=this.materialCatalogManager.queryFaModelMaterialCatalogPaginationForTemplateSet(materialCatalog,pageNum,pageSize);
        log.info("获取材料目录列表调用成功结果为：{}", JSON.toJSONString(pageResult));
        ApiResultSet apiResultSet = new ApiResultSet<>();
        apiResultSet.setData(pageResult);
        return apiResultSet;
    }
    @Override
    public ApiResultSet<PageResult<MaterialCatalog>> queryFaModelMaterialCatalogWithPage(String categoryName, String categoryCode, Integer pageNum, Integer pageSize) {
        MaterialCatalog materialCatalog=new MaterialCatalog();
        materialCatalog.setCatalogName(categoryName);
        materialCatalog.setCatalogCode(categoryCode);
        PageResult<MaterialCatalog> pageResult=this.materialCatalogManager.queryFaModelMaterialCatalogPaginationForTemplateSet(materialCatalog,pageNum,pageSize);
        log.info("获取材料目录列表调用成功结果为：{}", JSON.toJSONString(pageResult));
        ApiResultSet apiResultSet = new ApiResultSet<>();
        apiResultSet.setData(pageResult);
        return apiResultSet;
    }

    /**
     * 查询识别模板列表
     * @param faModelTemplate
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public ApiResultSet<PageResult<FaModelTemplate>> queryFaModelTemplateList(FaModelTemplate faModelTemplate, Integer pageNum, Integer pageSize) {
        Map<String, Object> resultMap = new HashMap<>();
        DbMaterialCatalog dbMaterialCatalog=this.materialCatalogManager.getMaterialCatalogOid(faModelTemplate.getMaterialCatalogOid());
        MaterialCatalog materialCatalog=new MaterialCatalog();
        BeanUtils.copyProperties(dbMaterialCatalog,materialCatalog);
        PageResult<FaModelTemplate> pageResult=this.faModelTemplateManager.queryFaModelTemplateWithPage(faModelTemplate,pageNum,pageSize);
        log.info("获取识别模板列表调用成功结果为：{}", JSON.toJSONString(pageResult));
        resultMap.put("materialCatalog",materialCatalog);
        resultMap.put("pageResult",pageResult);
        ApiResultSet apiResultSet = new ApiResultSet<>();
        //apiResultSet.setData(pageResult);
        apiResultSet.setData(resultMap);
        return apiResultSet;
    }

    @Override
    public ApiResultSet<PageResult<FaModelTemplate>> queryFaModelTemplateWithPage(String categoryName, String categoryCode, String materialCatalogOid, Integer pageNum, Integer pageSize) {
        FaModelTemplate faModelTemplate=new FaModelTemplate();
        faModelTemplate.setMaterialCatalogOid(materialCatalogOid);
        faModelTemplate.setTemplateCode(categoryCode);
        faModelTemplate.setTemplateName(categoryName);
        Map<String, Object> resultMap = new HashMap<>();
        DbMaterialCatalog dbMaterialCatalog=this.materialCatalogManager.getMaterialCatalogOid(faModelTemplate.getMaterialCatalogOid());
        MaterialCatalog materialCatalog=new MaterialCatalog();
        BeanUtils.copyProperties(dbMaterialCatalog,materialCatalog);
        PageResult<FaModelTemplate> pageResult=this.faModelTemplateManager.queryFaModelTemplateWithPage(faModelTemplate,pageNum,pageSize);
        log.info("获取识别模板列表调用成功结果为：{}", JSON.toJSONString(pageResult));
        resultMap.put("materialCatalog",materialCatalog);
        resultMap.put("pageResult",pageResult);
        ApiResultSet apiResultSet = new ApiResultSet<>();
        //apiResultSet.setData(pageResult);
        apiResultSet.setData(resultMap);
        return apiResultSet;
    }

    //初始化数据
    @Override
    public ApiResultSet initFaModelTemplate(String materialCatalogOid, String id,String  faModelTemplateOid,String guideType) {
        FaModelTemplate faModelTemplate=new FaModelTemplate();
        faModelTemplate.setMaterialCatalogOid(materialCatalogOid);
        if(null!=faModelTemplateOid&&faModelTemplateOid!=""){
            faModelTemplate.setFaModelTemplateOid(faModelTemplateOid);
        }
        Map<String, Object> resultMap = new HashMap<>();
        List<FaModelTemplateBlock> l = null;
        List<FaModelTemplate> tList = null;
        //材料目录子项信息
        DbMaterialCatalog dbMaterialCatalog=this.materialCatalogManager.getMaterialCatalogOid(faModelTemplate.getMaterialCatalogOid());
        MaterialCatalog materialCatalog=new MaterialCatalog();
        BeanUtils.copyProperties(dbMaterialCatalog,materialCatalog);
        resultMap.put("cataOid",dbMaterialCatalog.getMaterialCatalogOid());
        resultMap.put("materialCataName",dbMaterialCatalog.getMaterialCategoryName());
        resultMap.put("bigCataOid",dbMaterialCatalog.getMaterialParentOid());

        MaterialCatalogElement mce=new MaterialCatalogElement();
        mce.setMaterialCatalogOid(faModelTemplate.getMaterialCatalogOid());
        //材料目录元素列表
        List<MaterialCatalogElement> mdList=this.materialCatalogElementManager.queryList(mce);
        resultMap.put("metaDataJsonArrayStr",mdList);// 目录元素信息

        String tempOid = null;
        FaModelTemplate faModelTemplateInfo = new FaModelTemplate();

        // 判断oid是否为空，不为空获取识别模板
        if(null!=faModelTemplateOid&&faModelTemplateOid!=""){
            tempOid = faModelTemplateOid;
            faModelTemplateInfo=this.faModelTemplateManager.getFaModelTemplateAllByOid(faModelTemplateOid);
        }else {
            // 通过目录查找是否有正在编制的识别模板
            FaModelTemplate template = this.faModelTemplateManager.getCompilingFaModelTemplateByCataOid(faModelTemplate.getMaterialCatalogOid());
            if (template != null) {
                tempOid = template.getFaModelTemplateOid();
                faModelTemplateInfo = template;
                // 是否存在已发布的模板信息列表
            } else {
                 tList =this.faModelTemplateManager.queryFaModelTemplateList(faModelTemplate, "yfb");
                // 已发布的模板信息列表
               /* if (tList != null && tList.size() > 0) {
                    resultMap.put("versionTemplateList",tList);
                }*/
            }

        }
        String  baseImgFileOid="";
        if(null!=tempOid&&tempOid!=""){
            resultMap.put("templateStr",faModelTemplateInfo);
            resultMap.put("blockColorClass",faModelTemplateInfo.getBlockColorClass());
            resultMap.put("version",faModelTemplateInfo.getVersion());
            baseImgFileOid=faModelTemplateInfo.getBaseImgFileOid();//附件底图oid

            //区块信息
             FaModelTemplateBlock faModelTemplateBlock=new FaModelTemplateBlock();
             faModelTemplateBlock.setFaModelTemplateOid(tempOid);
            l=this.faModelTemplateBlockManager.queryFaModelTemplateBlockList(faModelTemplateBlock);
            for(FaModelTemplateBlock ftb:l){
                ftb.setMarqueeJsonValue(null);
                ftb.setFaModelTemplate(null);
            }

        }

        resultMap.put("baseImgFileOid",baseImgFileOid);//附件底图oid
        resultMap.put("versionTemplateList",tList);
        resultMap.put("blockArrayStr",l);
        resultMap.put("tempOid",tempOid);
        resultMap.put("materialCatalog",materialCatalog);

        Map<String, String> RECOGNITION_TYPE= FaStaticParam.RECOGNITION_TYPE;
        String RECOGNITION_TYPE_YS=FaStaticParam.RECOGNITION_TYPE_YS;
        String RECOGNITION_TYPE_SX=FaStaticParam.RECOGNITION_TYPE_SX;
        String RECOGNITION_TYPE_SX_SINGLE=FaStaticParam.RECOGNITION_TYPE_SX_SINGLE;
        String RECOGNITION_TYPE_TX=FaStaticParam.RECOGNITION_TYPE_TX;
        String RECOGNITION_TYPE_TZ=FaStaticParam.RECOGNITION_TYPE_TZ;
        String RECOGNITION_TYPE_MARQUEE_YS=FaStaticParam.RECOGNITION_TYPE_MARQUEE_YS;
        String RECOGNITION_TYPE_MARQUEE_SX=FaStaticParam.RECOGNITION_TYPE_MARQUEE_SX;
        resultMap.put("RECOGNITION_TYPE",RECOGNITION_TYPE);
        resultMap.put("RECOGNITION_TYPE_YS",RECOGNITION_TYPE_YS);
        resultMap.put("RECOGNITION_TYPE_SX",RECOGNITION_TYPE_SX);
        resultMap.put("RECOGNITION_TYPE_SX_SINGLE",RECOGNITION_TYPE_SX_SINGLE);
        resultMap.put("RECOGNITION_TYPE_TX",RECOGNITION_TYPE_TX);
        resultMap.put("RECOGNITION_TYPE_TZ",RECOGNITION_TYPE_TZ);
        resultMap.put("RECOGNITION_TYPE_MARQUEE_YS",RECOGNITION_TYPE_MARQUEE_YS);
        resultMap.put("RECOGNITION_TYPE_MARQUEE_SX",RECOGNITION_TYPE_MARQUEE_SX);
        ApiResultSet apiResultSet = new ApiResultSet<>();
        apiResultSet.setData(resultMap);
        return apiResultSet;
    }

    //初始化数据
    @Override
    public ApiResultSet initFaModelTemplateByResultOid(String resultOid, String id,String  faModelTemplateOid,String guideType) {
        FaModelTemplate faModelTemplate=new FaModelTemplate();
        faModelTemplate.setMaterialCatalogOid(resultOid);
        if(null!=faModelTemplateOid&&faModelTemplateOid!=""){
            faModelTemplate.setFaModelTemplateOid(faModelTemplateOid);
        }
        Map<String, Object> resultMap = new HashMap<>();
        List<FaModelTemplateBlock> l = null;
        List<FaModelTemplate> tList = null;
        //材料目录子项信息
        /*DbMaterialCatalog dbMaterialCatalog=this.materialCatalogManager.getMaterialCatalogOid(faModelTemplate.getMaterialCatalogOid());
        MaterialCatalog materialCatalog=new MaterialCatalog();
        BeanUtils.copyProperties(dbMaterialCatalog,materialCatalog);
        resultMap.put("cataOid",dbMaterialCatalog.getMaterialCatalogOid());
        resultMap.put("materialCataName",dbMaterialCatalog.getMaterialCategoryName());
        resultMap.put("bigCataOid",dbMaterialCatalog.getMaterialParentOid());

        MaterialCatalogElement mce=new MaterialCatalogElement();
        mce.setMaterialCatalogOid(faModelTemplate.getMaterialCatalogOid());
        //材料目录元素列表
        List<MaterialCatalogElement> mdList=this.materialCatalogElementManager.queryList(mce);
        resultMap.put("metaDataJsonArrayStr",mdList);// 目录元素信息
*/
        String tempOid = null;
        FaModelTemplate faModelTemplateInfo = new FaModelTemplate();

        // 判断oid是否为空，不为空获取识别模板
        if(null!=faModelTemplateOid&&faModelTemplateOid!=""){
            tempOid = faModelTemplateOid;
            faModelTemplateInfo=this.faModelTemplateManager.getFaModelTemplateAllByOid(faModelTemplateOid);
        }else {
            // 通过目录查找是否有正在编制的识别模板
            FaModelTemplate template = this.faModelTemplateManager.getCompilingFaModelTemplateByCataOid(faModelTemplate.getMaterialCatalogOid());
            if (template != null) {
                tempOid = template.getFaModelTemplateOid();
                faModelTemplateInfo = template;
                // 是否存在已发布的模板信息列表
            } else {
                tList =this.faModelTemplateManager.queryFaModelTemplateList(faModelTemplate, "yfb");
                // 已发布的模板信息列表
               /* if (tList != null && tList.size() > 0) {
                    resultMap.put("versionTemplateList",tList);
                }*/
            }

        }
        String  baseImgFileOid="";
        if(null!=tempOid&&tempOid!=""){
            resultMap.put("templateStr",faModelTemplateInfo);
            resultMap.put("blockColorClass",faModelTemplateInfo.getBlockColorClass());
            resultMap.put("version",faModelTemplateInfo.getVersion());
            baseImgFileOid=faModelTemplateInfo.getBaseImgFileOid();//附件底图oid

            //区块信息
            FaModelTemplateBlock faModelTemplateBlock=new FaModelTemplateBlock();
            faModelTemplateBlock.setFaModelTemplateOid(tempOid);
            l=this.faModelTemplateBlockManager.queryFaModelTemplateBlockList(faModelTemplateBlock);
            for(FaModelTemplateBlock ftb:l){
                ftb.setFaModelTemplate(null);
            }

        }

        resultMap.put("baseImgFileOid",baseImgFileOid);//附件底图oid
        resultMap.put("versionTemplateList",tList);
        resultMap.put("blockArrayStr",l);
        resultMap.put("tempOid",tempOid);
        //resultMap.put("materialCatalog",materialCatalog);

        Map<String, String> RECOGNITION_TYPE= FaStaticParam.RECOGNITION_TYPE;
        String RECOGNITION_TYPE_YS=FaStaticParam.RECOGNITION_TYPE_YS;
        String RECOGNITION_TYPE_SX=FaStaticParam.RECOGNITION_TYPE_SX;
        String RECOGNITION_TYPE_SX_SINGLE=FaStaticParam.RECOGNITION_TYPE_SX_SINGLE;
        String RECOGNITION_TYPE_TX=FaStaticParam.RECOGNITION_TYPE_TX;
        String RECOGNITION_TYPE_TZ=FaStaticParam.RECOGNITION_TYPE_TZ;
        String RECOGNITION_TYPE_MARQUEE_YS=FaStaticParam.RECOGNITION_TYPE_MARQUEE_YS;
        String RECOGNITION_TYPE_MARQUEE_SX=FaStaticParam.RECOGNITION_TYPE_MARQUEE_SX;
        resultMap.put("RECOGNITION_TYPE",RECOGNITION_TYPE);
        resultMap.put("RECOGNITION_TYPE_YS",RECOGNITION_TYPE_YS);
        resultMap.put("RECOGNITION_TYPE_SX",RECOGNITION_TYPE_SX);
        resultMap.put("RECOGNITION_TYPE_SX_SINGLE",RECOGNITION_TYPE_SX_SINGLE);
        resultMap.put("RECOGNITION_TYPE_TX",RECOGNITION_TYPE_TX);
        resultMap.put("RECOGNITION_TYPE_TZ",RECOGNITION_TYPE_TZ);
        resultMap.put("RECOGNITION_TYPE_MARQUEE_YS",RECOGNITION_TYPE_MARQUEE_YS);
        resultMap.put("RECOGNITION_TYPE_MARQUEE_SX",RECOGNITION_TYPE_MARQUEE_SX);
        ApiResultSet apiResultSet = new ApiResultSet<>();
        apiResultSet.setData(resultMap);
        return apiResultSet;
    }

    /**
     * 保存区块模板信息
     * @param templateStr
     * @param blockArrayStr
     * @param delBlockIds
     * @param saveType
     * @return
     */
    @Override
    public ApiResultSet saveOrUpdateFaModelTemplate(String templateStr, String blockArrayStr,String delBlockIds, String saveType){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        Map<String, Object> modelMap=new HashMap<>();
        try {
            //modelMap =this.faModelTemplateBlockManager.saveOrUpdateTemplateAndBlock(request,templateStr,blockArrayStr,delBlockIds,saveType);
           /* modelMap.put("templateStr", ret.get("templateStr"));
            modelMap.put("blockArrayStr", ret.get("blockArrayStr"));
            modelMap.put("templateOid", ret.get("templateOid"));*/
            modelMap.put("success", true);
        } catch (Exception e) {
            modelMap.put("success", false);
            modelMap.put("message", e.getMessage());
        }
        return new ApiResultSet(modelMap);
    }

    /**
     * 删除模板信息
     * @param id
     * @return
     */
    @Override
    public ApiResultSet del(String id) {
        Assert.hasLength(String.valueOf(id),  "主键不能为空！");
        this.faModelTemplateManager.del(id);
        log.info("删除成功：{}", id);
        return new ApiResultSet(id);
    }

    /**
     * 起禁用模板信息
     * @param id
     * @param ableFlag
     * @return
     */
    @Override
    public ApiResultSet able(String id,String materialCatalogOid,String ableFlag) {
        Assert.hasLength(String.valueOf(id),  "主键不能为空！");
        this.faModelTemplateManager.able(id,materialCatalogOid,ableFlag);
        log.info("删除成功：{}", id);
        return new ApiResultSet(id);
    }


    /**
     * 克隆识别模板
     * @param id
     * @param faModelTemplateOid
     * @return
     */
    @Override
    public ApiResultSet cloneTemplate(String id,String faModelTemplateOid){
        Map<String, Object> modelMap=new HashMap<>();
        try {
            String message=this.faModelTemplateManager.cloneFaModelTemplate(id,faModelTemplateOid);
            modelMap.put("success", true);
        } catch (Exception e) {
            modelMap.put("success", false);
            modelMap.put("message", e.getMessage());
        }
        return new ApiResultSet(modelMap);
    }

    @Override
    public ApiResultSet<FaModelTemplate> getPulishedFaModelTemplateByCataOid(String materialCatalogOid) {
        FaModelTemplate template = faModelTemplateManager.getPulishedFaModelTemplateByCataOid(materialCatalogOid);
        ApiResultSet apiResultSet = new ApiResultSet<>();
        apiResultSet.setData(template);
        return apiResultSet;
    }

    @Override
    public ApiResultSet<List<FaModelTemplate>> queryFaModelTemplateList(FaModelTemplate faModelTemplate, String type) {
        List<FaModelTemplate> list=faModelTemplateManager.queryFaModelTemplateList(faModelTemplate,type);
        return new ApiResultSet(list);
    }

    @Override
    public ApiResultSet queryLicenseResultTemplateByOid(String materialCatalogOid) {
        FaModelTemplate template=faModelTemplateManager.queryLicenseResultTemplateByOid(materialCatalogOid);
        return new ApiResultSet(template);
    }
/*    List<SysDict> printNameList = sysDictService.queryDictListByDictParentCode(PRINTER_NAME);
            modelAndView.addObject("printNameList", printNameList);
    LicensePrintTemplate printTemplate = licensePrintTemplateService.getLicensePrintTemplateByOid(tempOid);
            modelAndView.addObject("printSetting", printTemplate.getPrintSetting());*/

    @Override
    public ApiResultSet initPrintSetting(String faModelTemplateOid) {
        Map<String, Object> modelMap=new HashMap<>();
        List<SysDict> printNameList=new ArrayList<>();
        ApiResultSet<List<SysDict>>   apiResultSet=  sysDictFeignService.querySysDictListByParentCode("PRINTER_NAME");
        if(null!=apiResultSet.getData()){
            printNameList=apiResultSet.getData();
        }
        modelMap.put("printNameList", printNameList);
        FaModelTemplate template=faModelTemplateManager.getFaModelTemplateAllByOid(faModelTemplateOid);
        modelMap.put("printSetting", template.getPrintSetting());
        return new ApiResultSet(modelMap);
    }




    @Override
    public ApiResultSet initPreviewPrintTemplate(String materialCatalogOid, String faModelTemplateOid) {
        Map<String, Object> modelMap=new HashMap<>();
        List<FaModelTemplateBlock> l = null;
        //区块信息
        FaModelTemplateBlock faModelTemplateBlock=new FaModelTemplateBlock();
        faModelTemplateBlock.setFaModelTemplateOid(faModelTemplateOid);
        l=this.faModelTemplateBlockManager.queryFaModelTemplateBlockList(faModelTemplateBlock);
        FaModelTemplate template=faModelTemplateManager.getFaModelTemplateAllByOid(faModelTemplateOid);
        modelMap.put("metadataList", l);
        modelMap.put("templateOid", faModelTemplateOid);
        modelMap.put("template", template);
        return new ApiResultSet(modelMap);
    }

    @Override
    public ApiResultSet savePrintSet(String printSetting, String faModelTemplateOid) throws Exception {
        FaModelTemplate template=faModelTemplateManager.getFaModelTemplateAllByOid(faModelTemplateOid);
        template.setPrintSetting(printSetting);
         faModelTemplateManager.saveOrUpdate(template);
        //faModelTemplateManager.saveOrUpdateFaModelTemplate(template);
        return new ApiResultSet(template);
    }


}
