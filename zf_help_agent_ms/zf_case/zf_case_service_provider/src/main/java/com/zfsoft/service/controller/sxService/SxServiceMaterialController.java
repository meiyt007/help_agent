package com.zfsoft.service.controller.sxService;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.csp.sentinel.util.StringUtil;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.platform.utils.validate.ResultInfoException;
import com.zfsoft.service.manager.sxService.RefinedMaterialManager;
import com.zfsoft.service.manager.sxService.ReviewPointsManager;
import com.zfsoft.service.manager.sxService.SxServiceManager;
import com.zfsoft.service.manager.sxService.SxServiceMaterialManager;
import com.zfsoft.service.manager.sxSituation.ServiceMaterialManager;
import com.zfsoft.service.manager.sxSituation.SxServiceMateOptRelManager;
import com.zfsoft.service.manager.sxSituation.SxServiceOptionTitleManager;
import com.zfsoft.service.manager.sxSituation.SxServiceOptionValManager;
import com.zfsoft.service.manager.sxSys.SxSysAttaManager;
import com.zfsoft.service.sxService.data.*;
import com.zfsoft.service.util.CommonUtil;
import com.zfsoft.service.sxService.service.SxServiceMaterialService;
import com.zfsoft.service.sxSituation.data.ServiceMaterial;
import com.zfsoft.service.sxSituation.data.SxServiceMateOptRel;
import com.zfsoft.service.sxSituation.data.SxServiceOptionTitle;
import com.zfsoft.service.sxSituation.data.SxServiceOptionVal;
import com.zfsoft.service.sxSituation.data.vo.ServiceMaterialVo;
import com.zfsoft.service.sxSys.data.SxSysAtta;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

/**
 *
 * @ClassName SxServiceMaterialController
 * @Description 实施清单申请材料
 * @Author wangxl
 * @Date 2020-10-26
 * @Version V1.0
 **/
@RestController
@Slf4j
public class SxServiceMaterialController implements SxServiceMaterialService {
    @Resource
    private SxServiceMaterialManager sxServiceMaterialManager;

    @Resource
    private ServiceMaterialManager serviceMaterialManager;

    @Resource
    private SxServiceMateOptRelManager sxServiceMateOptRelManager;

    @Resource
    private RefinedMaterialManager refinedMaterialManager;
    @Resource
    private SxSysAttaManager sxSysAttaManager;

    @Resource
    private SxServiceOptionTitleManager sxServiceOptionTitleManager;

    @Resource
    private SxServiceOptionValManager sxServiceOptionValManager;

    @Resource
    private SxServiceManager sxServiceManager;

    @Resource
    private ReviewPointsManager reviewPointsManager;

    @Override
    public ApiResultSet saveOrUpdateMaterialSample(String materialOid) throws Exception {
        SxServiceMaterial sxServiceMaterial = sxServiceMaterialManager.getSxServiceMaterialByOid(materialOid);
        List<RefinedMaterial> refinedMaterialList=refinedMaterialManager.getRefinedMaterialListByMaterialOid(materialOid);
        List<String> dzUrl=new ArrayList<>();
        List<String> materialSampleAddrs= new ArrayList<>();
        if(null!=refinedMaterialList&&refinedMaterialList.size()>0){
            for(RefinedMaterial refinedMaterial1:refinedMaterialList){
                String materialSampleOid= refinedMaterial1.getMaterialSampleOid();
                String materialSampleAddr = null;
                if(StringUtil.isNotEmpty(materialSampleOid)){
                    SxSysAtta sxSysAtta = sxSysAttaManager.getSxSysAttaByOid(materialSampleOid);
                    if(null!=sxSysAtta){
                        if(sxSysAtta.getFilePath().contains("http")){
                            materialSampleAddr=sxSysAtta.getFilePath();
                        }
                    }
                    dzUrl.add(materialSampleAddr);
                    materialSampleAddrs.add(materialSampleOid);
                }
            }
        }

        //多张图合并成pdf
        if(dzUrl.size()>1){
            String fileUrl ="";
            if(CommonUtil.isWindows()){
                fileUrl = "D:\\sxservice\\image\\";
            }else{
                fileUrl = "/soft/sxservice/image/";
            }
            String pdfPath=fileUrl+ CommonUtil.random(6) + ".pdf";
            File file = new File(fileUrl);
            if(!file.exists()){
                File dir = new File(fileUrl);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
            }
            Map<String ,String >  urlmap= this.refinedMaterialManager.getPdf(dzUrl,pdfPath);
            String materialSampleOid1=urlmap.get("materialSampleOid");
            String materialSampleAddr1=urlmap.get("materialSampleAddr");
            sxServiceMaterial.setMaterialSampleOid(materialSampleOid1);
            sxServiceMaterial.setMaterialSampleUrl(materialSampleAddr1);
        }

        //只有一张图
        if(dzUrl.size()==1){
            sxServiceMaterial.setMaterialSampleOid(materialSampleAddrs.get(0));
            sxServiceMaterial.setMaterialSampleUrl(dzUrl.get(0));

        }
        if(dzUrl.size()==0) {
            if (sxServiceMaterial != null) {
                String materSampleAddr = sxServiceMaterial.getMaterialSampleAddr();
                sxServiceMaterial.setMaterialSampleOid(materSampleAddr);
            }
        }
        sxServiceMaterialManager.updateSxServiceMaterialService(sxServiceMaterial);
        return new ApiResultSet("更新样本材料成功");
    }


    @Override
    public ApiResultSet<SxServiceMaterial> getSxServiceMaterialByOid(String materialOid) {
        SxServiceMaterial sxServiceMaterial = sxServiceMaterialManager.getSxServiceMaterialByOid(materialOid);
        ApiResultSet<SxServiceMaterial> apiResultSet = new ApiResultSet<SxServiceMaterial>();
        apiResultSet.setData(sxServiceMaterial);
        return apiResultSet;
    }

    @Override
    public ApiResultSet<List<SxServiceMaterial>> getSxServiceMaterialListByServiceOid(String serviceOid) {
        ApiResultSet<List<SxServiceMaterial>> apiResultSet = new ApiResultSet<List<SxServiceMaterial>>();
        List<SxServiceMaterial> serviceMaterials = sxServiceMaterialManager.getSxServiceMaterialByServiceOid(serviceOid);
        if(null != serviceMaterials && serviceMaterials.size()>0){
            apiResultSet.setData(serviceMaterials);
            return apiResultSet;
        }else{
            return null;
        }
    }

    /**
     * @param serviceOid 所属事项主健
     * @description: 根据所属事项主健获取实施清单材料信息
     * @author: wangns
     * @Date: 2020/10/26
     **/
    @Override
    public ApiResultSet<List<SxServiceMaterial>> getSxServiceMaterialByServiceOid(String serviceOid) {
        List<SxServiceMaterial> sxServiceMaterials = sxServiceMaterialManager.getSxServiceMaterialByServiceOid(serviceOid);
        ApiResultSet<List<SxServiceMaterial>> apiResultSet = new ApiResultSet<>();
        apiResultSet.setData(sxServiceMaterials);
        return apiResultSet;
    }

    /**
     * @param param
     * @description: 根据所属事项主健更新实施清单材料信息
     * @author: wangns
     * @Date: 2020/11/7
     */
    @Override
    public ApiResultSet updateByMaterialOid(String param) {
        this.sxServiceMaterialManager.updateByMaterialOid(param);
        log.info("据所属事项主健更新实施清单材料信息成功：{}", param);
        return new ApiResultSet(param);
    }

    @Override
    public ApiResultSet<List<ServiceMaterialVo>> getSituationMaterialListByOids(String serviceOid,String optionValOids) {
        if(StringUtils.isEmpty(serviceOid)) {
            throw new ResultInfoException("事项主键不能为空");
        }
        List<SxServiceOptionTitle> optionTitles = sxServiceOptionTitleManager.getServiceOptionTitlesByServiceOid(serviceOid);
        List<SxServiceOptionVal> allOptionVal = new ArrayList<>();
        for(SxServiceOptionTitle title : optionTitles) {
            List<SxServiceOptionVal> optionVals = sxServiceOptionValManager.getSxServiceOptionValsByTitleOid(title.getOid());
            allOptionVal.addAll(optionVals);
        }
        Map<String, List<SxServiceMaterial>> materialMap = new HashMap<>();
        Map<String, List<ServiceMaterial>> serviceMaterialMap = new HashMap<>();
        Map<String, Integer> tempMap = new HashMap<>();
        for(SxServiceOptionVal optionVal : allOptionVal) {
            List<SxServiceMateOptRel> optRelList = sxServiceMateOptRelManager.getSxServiceMateOptRelsByOptionValOid(optionVal.getOid());
            List<SxServiceMaterial> sxServiceMaterials = new ArrayList<>();
            List<ServiceMaterial> serviceMaterials = new ArrayList<>();
            for(SxServiceMateOptRel optRel : optRelList) {
                if(StringUtils.isNotEmpty(optRel.getSxMaterialOid())) {
                    SxServiceMaterial sxMaterial = sxServiceMaterialManager.getSxServiceMaterialByOid(optRel.getSxMaterialOid());
                    if(null != sxMaterial) {
                        sxServiceMaterials.add(sxMaterial);
                    }
                }
                if(StringUtils.isNotEmpty(optRel.getMaterialOid())) {
                    ServiceMaterial serviceMaterial = serviceMaterialManager.getServiceMaterialByOid(optRel.getMaterialOid());
                    if(null != serviceMaterial) {
                        serviceMaterials.add(serviceMaterial);
                    }
                }
            }
            tempMap.put(optionVal.getOid(), optRelList.size());
            materialMap.put(optionVal.getOid(), sxServiceMaterials);
            serviceMaterialMap.put(optionVal.getOid(), serviceMaterials);
        }
        // 所有材料
        List<SxServiceMaterial> allSxServiceMaterial = sxServiceMaterialManager.getSxServiceMaterialByServiceOid(serviceOid);
        // 最终展示的材料
        List<SxServiceMaterial> needShowMaterialList = new ArrayList<>();
        // 不展示的材料
        List<SxServiceMaterial> notShowList = new ArrayList<>();
        for(String key : materialMap.keySet()) {
            if(StringUtils.isEmpty(optionValOids)) {
                if(null != tempMap.get(key) && tempMap.get(key) != 0) {
                    List<SxServiceMaterial> list = materialMap.get(key);
                    notShowList.addAll(list);
                }
            } else {
                String[] valueOids = optionValOids.split(";");
                for(String oid : valueOids) {
                    if(oid.equals(key)) {
                        List<SxServiceMaterial> list = materialMap.get(key);
                        needShowMaterialList.addAll(list);
                    } else {
                        if(null != tempMap.get(key) && tempMap.get(key) != 0) {
                            List<SxServiceMaterial> list = materialMap.get(key);
                            notShowList.addAll(list);
                        }
                    }
                }
            }
        }
        for(SxServiceMaterial material : allSxServiceMaterial) {
            if(!notShowList.contains(material)) {
                needShowMaterialList.add(material);
            }
        }
        needShowMaterialList = distinctFiled(needShowMaterialList);
        List<ServiceMaterialVo> materialVoList = this.getServiceMaterialVoBySxMaterial(needShowMaterialList);
        ApiResultSet<List<ServiceMaterialVo>> apiResultSet = new ApiResultSet<>();
        apiResultSet.setData(materialVoList);
        return apiResultSet;
//        List<SxServiceMaterial> sxServiceMaterials = null;
////        List<ServiceMaterial> serviceMaterials = null;
////        List<SxServiceMateOptRel> sxServiceMateOptRelList =null;
////        List<ServiceMaterialVo> materialVoList = new ArrayList<ServiceMaterialVo>();
////        //加载全部情形 查询实施清单全部材料
////        if(StringUtils.isEmpty(optionValOids)){
////            sxServiceMaterials = sxServiceMaterialManager.getSxServiceMaterialByServiceOid(serviceOid);
////            //排序
////            Collections.sort(sxServiceMaterials, new Comparator<SxServiceMaterial>(){
////                @Override
////                public int compare(SxServiceMaterial sm1, SxServiceMaterial sm2) {
////                    return sm1.getMaterialSort().compareTo(sm2.getMaterialSort());
////                }
////            });
////            if(sxServiceMaterials != null){
////                materialVoList= this.getServiceMaterialVoBySxMaterial(sxServiceMaterials);
////            }
////        }
////        //查询与选项关联的精细化材料
////        if(StringUtils.isNotEmpty(optionValOids)){
////            if(optionValOids.contains(",")){
////                optionValOids=  optionValOids.replaceAll(",",";");
////            }
////            sxServiceMateOptRelList = new ArrayList<SxServiceMateOptRel>();
////            String[] valueOids = optionValOids.split(";");
////            for (String oid : valueOids ) {
////                List<SxServiceMateOptRel> list = sxServiceMateOptRelManager.getSxServiceMateOptRelsByOptionValOid(oid);
////                sxServiceMateOptRelList.addAll(list);
////            }
////            if(sxServiceMateOptRelList !=null){
////                sxServiceMaterials = new ArrayList<SxServiceMaterial>();
////                serviceMaterials = new ArrayList<ServiceMaterial>();
////                for (SxServiceMateOptRel sxServiceMateOptRel: sxServiceMateOptRelList) {
////                    if(StringUtils.isNotEmpty(sxServiceMateOptRel.getSxMaterialOid())){
////                        SxServiceMaterial sxServiceMaterial = sxServiceMaterialManager.getSxServiceMaterialByOid(sxServiceMateOptRel.getSxMaterialOid());
////                        if(sxServiceMaterial !=null){
////                            sxServiceMaterials.add(sxServiceMaterial);
////                        }
////                    }
////                    if(StringUtils.isNotEmpty(sxServiceMateOptRel.getMaterialOid())){
////                        ServiceMaterial serviceMaterial = serviceMaterialManager.getServiceMaterialByOid(sxServiceMateOptRel.getMaterialOid());
////                        if(serviceMaterial !=null){
////                            serviceMaterials.add(serviceMaterial);
////                        }
////                    }
////                }
////                //精细化材料去重
////                if(!serviceMaterials.isEmpty()){
////                    serviceMaterials = distinctObjectFiled(serviceMaterials);
////                }
////                if(!sxServiceMaterials.isEmpty()){
////                    sxServiceMaterials=distinctFiled(sxServiceMaterials);
////                }
////            }
////            List<ServiceMaterialVo> voList1 = this.getServiceMaterialVoBySxMaterial(sxServiceMaterials);
////            List<ServiceMaterialVo> voList2 = this.getServiceMaterialVoByServiceMaterial(serviceMaterials);
////            materialVoList.addAll(voList1);
////            materialVoList.addAll(voList2);
////        }
////        ApiResultSet<List<ServiceMaterialVo>> apiResultSet = new ApiResultSet<>();
////        apiResultSet.setData(materialVoList);
////        return apiResultSet;
    }


    @Override
    public ApiResultSet<List<SxServiceMaterial>> getSxServiceMaterialListByMaterialCatalogOid(String materialCatalogOid) {
        List<SxServiceMaterial> sxServiceMaterialList = sxServiceMaterialManager.getSxServiceMaterialListByMaterialCatalogOid(materialCatalogOid);
        ApiResultSet<List<SxServiceMaterial>> apiResultSet = new ApiResultSet<List<SxServiceMaterial>>();
        apiResultSet.setData(sxServiceMaterialList);
        return apiResultSet;
    }

    @Override
    public ApiResultSet updateByMaterial(SxServiceMaterial sxServiceMaterial) {
        this.sxServiceMaterialManager.updateByMaterial(sxServiceMaterial);
        log.info("据所属事项主健更新实施清单材料信息成功：{}", sxServiceMaterial);
        return new ApiResultSet(sxServiceMaterial);
    }

    @Override
    public ApiResultSet<List<RefinedMaterial>> getRefinedMaterialByServiceId(String serviceId) {
        List<RefinedMaterial> lists =refinedMaterialManager.getRefinedMaterialListByServiceOid(serviceId);
        return new ApiResultSet(lists);
        //return refinedMaterialManager.getRefinedMaterialListByServiceOid(serviceId);
    }

    @Override
    public ApiResultSet getSxServiceMaterialPageListByServiceOid(Integer pageNum, Integer pageSize, String searchName,String serviceOid) {
        PageResult<SxServiceMaterial> lists = sxServiceMaterialManager.getSxServiceMaterialPageListByServiceOid(pageNum,pageSize,searchName,serviceOid);
        ApiResultSet apiResultSet = new ApiResultSet();
        apiResultSet.setData(lists);
        return apiResultSet;
    }

    @Override
    public ApiResultSet insertSxServiceMaterialService(SxServiceMaterial sxServiceMaterial) {
        if(sxServiceMaterial.getMaterialOid()==null){
            sxServiceMaterialManager.insertSxServiceMaterialService(sxServiceMaterial);
        }else{
            sxServiceMaterialManager.updateSxServiceMaterialService(sxServiceMaterial);
        }
        return new ApiResultSet("新增成功");
    }

    @Override
    public ApiResultSet<SxServiceMaterial> getSxServiceMaterialHasFileByOid(String materialOid) {
        SxServiceMaterial sxServiceMaterial = sxServiceMaterialManager.getSxServiceMaterialHasFileByOid(materialOid);
        return new ApiResultSet<>(sxServiceMaterial);
    }

    @Override
    public ApiResultSet delSxServiceMaterialByOid(String materialOid) {
        int result = sxServiceMaterialManager.delSxServiceMaterialByOid(materialOid);
        return new ApiResultSet<>("删除成功");
    }

    /**
     * 事项材料处理
     * @param list
     * @return
     */
    protected List<ServiceMaterialVo> getServiceMaterialVoBySxMaterial(List<SxServiceMaterial> list) {
        ServiceMaterialVo serviceMaterialVo =null;
        List<ServiceMaterialVo> voList=null;
        if(list != null){
            voList=new LinkedList<ServiceMaterialVo>();
            for (SxServiceMaterial sxServiceMaterial:list) {
                serviceMaterialVo = new ServiceMaterialVo();
                serviceMaterialVo.setMaterialOid(sxServiceMaterial.getMaterialOid());
                serviceMaterialVo.setMaterialName(sxServiceMaterial.getMaterialName());
                serviceMaterialVo.setMaterialType(sxServiceMaterial.getMaterialType().intValue());
                serviceMaterialVo.setMaterialMustFlag(sxServiceMaterial.getMustFlag().intValue());
                if(sxServiceMaterial.getMaterialSource() !=null){
                    serviceMaterialVo.setMaterialSource(sxServiceMaterial.getMaterialSource().intValue());
                }
                serviceMaterialVo.setMaterialSimpleAddr(sxServiceMaterial.getMaterialSampleAddr());
                serviceMaterialVo.setMaterialServiceOid(sxServiceMaterial.getServiceOid());
                if(sxServiceMaterial.getMaterialFormat() !=null){
                    serviceMaterialVo.setMaterialFormat(sxServiceMaterial.getMaterialFormat().intValue());
                }else{
                    serviceMaterialVo.setMaterialFormat(1);
                }
                serviceMaterialVo.setMaterialCatalogOid(sxServiceMaterial.getMaterialCatalogOid());
                serviceMaterialVo.setBaiduTemplateIds(sxServiceMaterial.getBaiduTemplateIds());
                serviceMaterialVo.setPaperNumber(sxServiceMaterial.getPaperNumber());
                serviceMaterialVo.setMaterialSampleAddr(sxServiceMaterial.getMaterialSampleAddr());
                serviceMaterialVo.setMaterialSimpleAddrYl(sxServiceMaterial.getMaterialSampleAddrYl());
                serviceMaterialVo.setAuditType(sxServiceMaterial.getAuditType());
                serviceMaterialVo.setMaterialSampleOid(sxServiceMaterial.getMaterialSampleOid());
                serviceMaterialVo.setMaterialSampleUrl(sxServiceMaterial.getMaterialSampleUrl());
                serviceMaterialVo.setMaterialFlag(0);
                serviceMaterialVo.setMadeMaterialType(sxServiceMaterial.getMadeMaterialType());
                serviceMaterialVo.setReviewPointsFlag(0);
                List<RefinedMaterial> refinedMaterialList = refinedMaterialManager.getRefinedMaterialListByMaterialOid(sxServiceMaterial.getMaterialOid());
                for (RefinedMaterial refinedMaterial : refinedMaterialList) {
                    List<ReviewPoints> reviewPointsList = reviewPointsManager.getReviewPointsListByRefinedMaterialOid(refinedMaterial.getOid());
                    if (CollUtil.isNotEmpty(reviewPointsList)) {
                        serviceMaterialVo.setReviewPointsFlag(1);
                        break;
                    }
                }
                serviceMaterialVo.setMaterialEmptyAddr(sxServiceMaterial.getMaterialEmptyAddr());
                voList.add(serviceMaterialVo);
            }
        }
        return  voList;
    }

    /**
     * 精细化材料处理
     * @param list
     * @return
     */
    protected List<ServiceMaterialVo>  getServiceMaterialVoByServiceMaterial(List<ServiceMaterial> list) {
        ServiceMaterialVo serviceMaterialVo =null;
        List<ServiceMaterialVo> voList=null;
        if(list != null) {
            voList=new LinkedList<ServiceMaterialVo>();
            for (ServiceMaterial serviceMaterial:list) {
                serviceMaterialVo = new ServiceMaterialVo();
                serviceMaterialVo.setMaterialOid(serviceMaterial.getOid());
                serviceMaterialVo.setMaterialName(serviceMaterial.getMaterialName());
                serviceMaterialVo.setMaterialType(serviceMaterial.getMaterialType().intValue());
                serviceMaterialVo.setMaterialMustFlag(serviceMaterial.getNeedStatus().intValue());
                if(StringUtils.isNotEmpty(serviceMaterial.getMaterialSource())){
                    serviceMaterialVo.setMaterialSource(0);//与事项材料来源值不一致，颗粒化材料来源是手动输入的文字值
                }
                serviceMaterialVo.setMaterialSimpleAddr(serviceMaterial.getMaterialSampleAddr());
                serviceMaterialVo.setMaterialServiceOid(serviceMaterial.getServiceOid());
                if(serviceMaterial.getMaterialFormat() !=null){
                    serviceMaterialVo.setMaterialFormat(serviceMaterial.getMaterialFormat().intValue());
                }else{
                    serviceMaterialVo.setMaterialFormat(1);
                }
                serviceMaterialVo.setBaiduTemplateIds(serviceMaterial.getBaiduTemplateIds());
                serviceMaterialVo.setMaterialCatalogOid(serviceMaterial.getMaterialCatalogOid());
                serviceMaterialVo.setMaterialSampleAddr(serviceMaterial.getMaterialSampleAddr());
                serviceMaterialVo.setMaterialSimpleAddrYl(serviceMaterial.getMaterialSampleAddrYl());
                serviceMaterialVo.setPaperNumber(Long.parseLong(serviceMaterial.getPaperNumber()));
                serviceMaterialVo.setAuditType(serviceMaterial.getAuditType());
                serviceMaterialVo.setMaterialSampleOid(serviceMaterial.getMaterialSampleOid());
                serviceMaterialVo.setMaterialSampleUrl(serviceMaterial.getMaterialSampleUrl());
                serviceMaterialVo.setMaterialFlag(1);
                voList.add(serviceMaterialVo);
            }
        }
        return voList;
    }

    /**
     * 去重
     * @param lists
     * @return
     */
    private static List<ServiceMaterial> distinctObjectFiled(List<ServiceMaterial> lists) {
        return lists.stream().collect(
                Collectors.collectingAndThen(Collectors.toCollection(
                        () -> new TreeSet<>(Comparator.comparing(ServiceMaterial::getOid))), ArrayList::new)
        );
    }

    private static List<SxServiceMaterial> distinctFiled(List<SxServiceMaterial> lists) {
        return lists.stream().collect(
                Collectors.collectingAndThen(Collectors.toCollection(
                        () -> new TreeSet<>(Comparator.comparing(SxServiceMaterial::getMaterialOid))), ArrayList::new)
        );
    }

    @Override
    public ApiResultSet saveMaterialFormTemplate(SxMaterialFormTemp sxMaterialFormTemp) {
        ApiResultSet apiResultSet = new ApiResultSet();
        int i = sxServiceMaterialManager.saveMaterialFormTemplate(sxMaterialFormTemp);
        apiResultSet.setData(i);
        return apiResultSet;
    }

    @Override
    public ApiResultSet<SxMaterialFormTemp> getMaterialFormTemplate(String sxServiceOid, String materialOid) {
        ApiResultSet apiResultSet = new ApiResultSet();
        SxMaterialFormTemp formTemp = sxServiceMaterialManager.getMaterialFormTemplate(sxServiceOid, materialOid);
        apiResultSet.setData(formTemp);
        return apiResultSet;
    }

    @Override
    public ApiResultSet<List<SxMaterialFormTemp>> getServiceFormTemplateList(String sxServiceOid) {
        ApiResultSet apiResultSet = new ApiResultSet();
        List<SxMaterialFormTemp> sxMaterialFormTempList = sxServiceMaterialManager.getServiceFormTemplateList(sxServiceOid);
        apiResultSet.setData(sxMaterialFormTempList);
        return apiResultSet;
    }

    @Override
    @ApiOperation(value = "专属指南查询")
    @ApiImplicitParams({@ApiImplicitParam(paramType = "query", name = "serviceOid", value = "事项oid", dataType = "string", example = ""),
            @ApiImplicitParam(paramType = "query", name = "optionValOids", value = "选项值oid", dataType = "string", example = "")})
    public ApiResultSet<Map<String, Object>> getExclusiveGuide(String serviceOid,String optionValOids) {
        if(StringUtils.isEmpty(serviceOid)) {
            throw new ResultInfoException("事项主键不能为空");
        }
        List<SxServiceOptionTitle> optionTitles = sxServiceOptionTitleManager.getServiceOptionTitlesByServiceOid(serviceOid);
        List<SxServiceOptionVal> allOptionVal = new ArrayList<>();
        for(SxServiceOptionTitle title : optionTitles) {
            List<SxServiceOptionVal> optionVals = sxServiceOptionValManager.getSxServiceOptionValsByTitleOid(title.getOid());
            allOptionVal.addAll(optionVals);
        }
        Map<String, List<SxServiceMaterial>> materialMap = new HashMap<>();
        Map<String, List<ServiceMaterial>> serviceMaterialMap = new HashMap<>();
        Map<String, Integer> tempMap = new HashMap<>();
        for(SxServiceOptionVal optionVal : allOptionVal) {
            List<SxServiceMateOptRel> optRelList = sxServiceMateOptRelManager.getSxServiceMateOptRelsByOptionValOid(optionVal.getOid());
            List<SxServiceMaterial> sxServiceMaterials = new ArrayList<>();
            List<ServiceMaterial> serviceMaterials = new ArrayList<>();
            for(SxServiceMateOptRel optRel : optRelList) {
                if(StringUtils.isNotEmpty(optRel.getSxMaterialOid())) {
                    SxServiceMaterial sxMaterial = sxServiceMaterialManager.getSxServiceMaterialByOid(optRel.getSxMaterialOid());
                    if(null != sxMaterial) {
                        sxServiceMaterials.add(sxMaterial);
                    }
                }
                if(StringUtils.isNotEmpty(optRel.getMaterialOid())) {
                    ServiceMaterial serviceMaterial = serviceMaterialManager.getServiceMaterialByOid(optRel.getMaterialOid());
                    if(null != serviceMaterial) {
                        serviceMaterials.add(serviceMaterial);
                    }
                }
            }
            tempMap.put(optionVal.getOid(), optRelList.size());
            materialMap.put(optionVal.getOid(), sxServiceMaterials);
            serviceMaterialMap.put(optionVal.getOid(), serviceMaterials);
        }
        // 所有材料
        List<SxServiceMaterial> allSxServiceMaterial = sxServiceMaterialManager.getSxServiceMaterialByServiceOid(serviceOid);
        // 最终展示的材料
        List<SxServiceMaterial> needShowMaterialList = new ArrayList<>();
        // 不展示的材料
        List<SxServiceMaterial> notShowList = new ArrayList<>();
        for(String key : materialMap.keySet()) {
            if(StringUtils.isEmpty(optionValOids)) {
                if(null != tempMap.get(key) && tempMap.get(key) != 0) {
                    List<SxServiceMaterial> list = materialMap.get(key);
                    notShowList.addAll(list);
                }
            } else {
                String[] valueOids = optionValOids.split(";");
                for(String oid : valueOids) {
                    if(oid.equals(key)) {
                        List<SxServiceMaterial> list = materialMap.get(key);
                        needShowMaterialList.addAll(list);
                    } else {
                        if(null != tempMap.get(key) && tempMap.get(key) != 0) {
                            List<SxServiceMaterial> list = materialMap.get(key);
                            notShowList.addAll(list);
                        }
                    }
                }
            }
        }
        for(SxServiceMaterial material : allSxServiceMaterial) {
            if(!notShowList.contains(material)) {
                needShowMaterialList.add(material);
            }
        }
        needShowMaterialList = distinctFiled(needShowMaterialList);
        Map<String, Object> result = new HashMap<>();
        //申请材料(0 系统智能生成材料  1 免提交材料  2 需上传材料 madeMaterialType)
        List<ServiceMaterialVo> autoProduceList = new ArrayList<>();
        List<ServiceMaterialVo> noSubmissionList = new ArrayList<>();
        List<ServiceMaterialVo> needUploadList = new ArrayList<>();
        List<ServiceMaterialVo> materialVoList = this.getServiceMaterialVoBySxMaterial(needShowMaterialList);
        if (materialVoList !=null && materialVoList.size()>0) {
            for (ServiceMaterialVo serviceMaterialVo: materialVoList) {
                if(serviceMaterialVo.getMadeMaterialType() !=null) {
                    if (serviceMaterialVo.getMadeMaterialType() ==0) {
                        autoProduceList.add(serviceMaterialVo);
                    } else if (serviceMaterialVo.getMadeMaterialType() ==1) {
                        noSubmissionList.add(serviceMaterialVo);
                    } else if ((serviceMaterialVo.getMadeMaterialType() ==2)) {
                        needUploadList.add(serviceMaterialVo);
                    }
                }
            }
        }
        result.put("autoProduceMaterialList", autoProduceList);
        result.put("noSubmissionMaterialList", noSubmissionList);
        result.put("needUploadMaterialList", needUploadList);
        //关联服务
        SxService sxService = sxServiceManager.getSxServiceByOid(serviceOid);
        String recommendedServiceOids = sxService.getRecommendedServiceOids();
        List<SxService> relationServiceList = new ArrayList<>();
        if(StringUtils.isNotEmpty(recommendedServiceOids)){
            String[] serviceOids = recommendedServiceOids.split(",");
            relationServiceList = sxServiceManager.getSxRecommendServiceListByOid(serviceOids);
        }
        result.put("relationServiceList", relationServiceList);
        ApiResultSet apiResultSet = new ApiResultSet<>();
        apiResultSet.setData(result);
        return apiResultSet;
    }

}
