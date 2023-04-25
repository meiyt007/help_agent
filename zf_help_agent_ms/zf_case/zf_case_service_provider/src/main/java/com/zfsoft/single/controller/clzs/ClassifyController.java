package com.zfsoft.single.controller.clzs;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import com.zfsoft.cases.data.QlCase;
import com.zfsoft.cases.data.QlCaseMaterial;
import com.zfsoft.cases.data.QlCaseMaterialAtta;
import com.zfsoft.cases.data.QlSysAtta;
import com.zfsoft.cases.service.QlCaseMaterialAttaService;
import com.zfsoft.cases.service.QlCaseMaterialService;
import com.zfsoft.cases.service.QlCaseService;
import com.zfsoft.cases.service.SysAttaService;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.service.sxService.data.RefinedMaterial;
import com.zfsoft.service.sxService.data.SxServiceMaterial;
import com.zfsoft.service.sxService.service.RefinedMaterialService;
import com.zfsoft.service.sxService.service.SxServiceMaterialService;
import com.zfsoft.service.sxSys.data.SxSysAtta;
import com.zfsoft.service.sxSys.service.SxSysAttaService;
import com.zfsoft.single.data.clzs.ClassifyRec;
import com.zfsoft.single.data.clzs.dto.ClassifyRecVo;
import com.zfsoft.single.data.yxpz.AhsSamplePicInfo;
import com.zfsoft.single.data.yxpz.SxMaterialBill;
import com.zfsoft.single.data.yxpz.SxMaterialElmsConfig;
import com.zfsoft.single.manager.clzs.ClassifyManager;
import com.zfsoft.single.manager.sxpz.SxMaterialBillManager;
import com.zfsoft.single.manager.yxpz.AhsSamplePicInfoManager;
import com.zfsoft.single.service.clzs.ClassifyService;
import com.zfsoft.single.service.sxpz.SxMaterialElmsConfigService;
import com.zfsoft.single.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.File;
import java.util.*;

/**
 * @author: liangss
 * @create: 2021-06-12
 * @description: 材料分类
 */
@Slf4j
@RestController
public class ClassifyController implements ClassifyService {

    @Resource
    private ClassifyManager classifyManager;

    @Autowired
    private QlCaseMaterialService qlCaseMaterialServiceFeginService;

    @Autowired
    private AhsSamplePicInfoManager ahsSamplePicInfoManager;

    @Autowired
    private QlCaseService qlCaseServiceFeginService;
    @Autowired
    private QlCaseMaterialAttaService qlCaseMaterialAttaServiceFeginService;

    @Resource
    private SysAttaService qlSysAttaFeignService;

    //精细化材料
    @Resource
    private RefinedMaterialService refinedMaterialFeginService;

    @Resource
    private SxSysAttaService sxServiceAttaFeignService;
    @Resource
    private SxMaterialElmsConfigService sxMaterialElmsConfigService;
    @Resource
    private SxServiceMaterialService sxServiceMaterialFeginService;

    @Resource
    private SxMaterialBillManager sxMaterialBillManager;

    /**
     * 电子证照授权码
     */
    @Value("${zfsoft.dzzz.token}")
    private  String zzToken;
    /** url */
    @Value("${zfsoft.dzzz.url}")
    private  String urlElec;

    /**
     * 电子表单初始化
     * @param caseOid
     * @return
     */
    @GetMapping(value = "/getAllQlCaseMaterialListByAttaOid")
    public ApiResultSet<List<QlCaseMaterial>>  getAllQlCaseMaterialListByAttaOid(String caseOid, String userName, String idCard)  throws Exception {
        Map<String, Object> modelMap=new HashMap<>();
        ApiResultSet<List<QlCaseMaterial>>  apiResultSet=new ApiResultSet();

            File path = new File(ResourceUtils.getURL("classpath:").getPath());
            apiResultSet=qlCaseMaterialServiceFeginService.queryQlCaseMaterialByCaseOid(caseOid);
            if(null != apiResultSet && null != apiResultSet.getData()){
                List<QlCaseMaterial> qlCaseMaterials=apiResultSet.getData();
                for(QlCaseMaterial qlCaseMaterial:qlCaseMaterials){
                    String directoryObj="";
                    LinkedHashMap<String, Object> params = new LinkedHashMap<String, Object>();
                    String materName=qlCaseMaterial.getMaterialName();
                    ApiResultSet<SxServiceMaterial> material = sxServiceMaterialFeginService.getSxServiceMaterialByOid(qlCaseMaterial.getMaterialOid());
                    if(null == material || null == material.getData()) {
                        continue;
                    }
                    SxServiceMaterial  sxServiceMaterial = material.getData();
                    if(sxServiceMaterial!=null){
                        qlCaseMaterial.setMustFlag(sxServiceMaterial.getMustFlag());
                        if(null!=sxServiceMaterial.getMaterialFormat()){
                            if(sxServiceMaterial.getMaterialFormat()==3){
                                ApiResultSet<SxMaterialElmsConfig> config = sxMaterialElmsConfigService.getElecConfigByMaterialOid(qlCaseMaterial.getMaterialOid());
                                if(null == config || null == config.getData()) {
                                    continue;
                                }
                                SxMaterialElmsConfig materialElmsConfig = config.getData();
                                if(materialElmsConfig !=null){
                                    qlCaseMaterial.setElecBillOid(materialElmsConfig.getBillOid());
                                    String billOid=materialElmsConfig.getBillOid();
                                    SxMaterialBill materialBill = sxMaterialBillManager.getSxMaterialBillByMaterOid(billOid);
                                    if(null!=materialBill){
                                        directoryObj=materialBill.getDirectoryObj();
                                        if(StringUtils.isNotEmpty(directoryObj)){
                                              if(!directoryObj.equals("1")){
                                                if(StrUtil.isNotEmpty(billOid)&&(!billOid.equals("null")) && (!billOid.equals("undefined"))
                                                          &&StrUtil.isNotEmpty(userName)&&StrUtil.isNotEmpty(idCard)
                                                  ){
                                                      params.put("token", zzToken);
                                                      params.put("info1",idCard);//"121212"
                                                      params.put("info2",userName);//"综窗测试1130"
                                                      params.put("directoryOid",billOid);//"ff8080816d3d0f36016d6cfa3d530247"
                                                      String res="";
                                                      try {
                                                          res= HttpUtil.post(urlElec + "/ws/elecServeESWs/getElecLicenseByBillOidAndOwnerInfoWs.do", params);
                                                          System.out.println(res);
                                                      }catch (Exception e){
                                                           e.printStackTrace();
                                                      }

                                                      if(StrUtil.isNotEmpty(res)){
                                                          JSONObject jsonObj = JSONObject.parseObject(res);
                                                          String resultType = null;
                                                          String resultData = null;
                                                          if(null != jsonObj.get("resultType") && null != jsonObj.get("resultData")) {
                                                              resultType = String.valueOf(jsonObj.get("resultType"));
                                                              resultData = String.valueOf(jsonObj.get("resultData")==null?"":jsonObj.get("resultData"));
                                                          }
                                                          if (!StrUtil.isEmpty(resultType)
                                                                  && "101".equals(resultType)) {
                                                              if (StrUtil.isNotEmpty(resultData)) {
                                                                  // jason字符串转map
                                                                  Map maps = (Map) JSONObject.parseObject(resultData);
                                                                  String licenseNumber = maps.get("licenseNumber").toString();
                                                                  String elecLicenOid=maps.get("elecLicenOid").toString();
                                                                  String elecLicenNumber=maps.get("elecLicenseUnique").toString();
                                                                  //Map resMap=new HashMap();
                                                                  qlCaseMaterial.setElemNumber(licenseNumber);
                                                                  qlCaseMaterial.setElecLicenNumber(elecLicenNumber);
                                                                  qlCaseMaterial.setElecLicenName(materialBill.getDirectoryName());
                                                                  qlCaseMaterial.setElemLicenseOid(elecLicenOid);
                                                                  //保存电子证照返回结果，用于表单字段填充
                                                                  qlCaseMaterial.setElectronicResult(res);
                                                                  qlCaseMaterial.setElecBillOid(billOid);
                                                                  qlCaseMaterialServiceFeginService.updateQlCaseMaterial(qlCaseMaterial);

/*
                                                                  resMap.put("licenseNumber",licenseNumber);
                                                                  resMap.put("elecLicenOid",elecLicenOid);
                                                                  resMap.put("elecLicenName",materialBill.getDirectoryName());
                                                                  resMap.put("elecLicenNumber",elecLicenNumber);*/

                                                              }
                                                          }
                                                      }

                                                  }


                                              }

                                        }
                                    }
                                }
                            }
                            qlCaseMaterial.setDirectoryObj(directoryObj);
                            if(sxServiceMaterial.getMaterialFormat() ==4 || sxServiceMaterial.getMustFlag()==2){
                                qlCaseMaterial.setRqbzFlag("1");
                            }
                        }
                    }
                   /* List<String> dzUrl=new ArrayList<>();
                    List<String> materialSampleAddrs= new ArrayList<>();*/
                    List<RefinedMaterial> refinedMaterialList = new ArrayList<>();
                    if(StringUtils.isEmpty(qlCaseMaterial.getMaterialSampleAddrYl())){
                        String materialOid=qlCaseMaterial.getMaterialOid();
                        //查看细化材料列表
                        ApiResultSet<List<RefinedMaterial>>  refineResult= refinedMaterialFeginService.getRefinedMaterialListByMaterialOid(materialOid);
                        if(null != refineResult && null != refineResult.getData()){
                            refinedMaterialList=refineResult.getData();
                            for(RefinedMaterial refinedMaterial:refinedMaterialList){
                                String materialSampleOid= refinedMaterial.getMaterialSampleOid();
                                String materialSampleAddr = null;
                                if(StringUtils.isNotEmpty(materialSampleOid)){
                                    ApiResultSet<SxSysAtta>  sxSysAttaApiResultSet=sxServiceAttaFeignService.getSxSysAttaByOId(materialSampleOid);
                                    if(null!=sxSysAttaApiResultSet){
                                        SxSysAtta sxSysAtta=sxSysAttaApiResultSet.getData();
                                        if(null == sxSysAtta || null == sxSysAtta.getFilePath()) {
                                            continue;
                                        }
                                        //System.out.println("样本附件地址:" + sxSysAtta.getFilePath());
                                        //materialSampleAddr=SGfastDFSNginxUrl+sxSysAtta.getFilePath();
                                        if(sxSysAtta.getFilePath().contains("http")){
                                            materialSampleAddr=sxSysAtta.getFilePath();
                                        }
                                        refinedMaterial.setMaterialSampleAddr(materialSampleAddr);
                                      /*  dzUrl.add(materialSampleAddr);
                                        materialSampleAddrs.add(materialSampleOid);*/
                                    }

                                }
                            }
                        }
                        //多张图合并成pdf
                        /*if(dzUrl.size()>1){
                           String fileUrl ="";
                            if(CommonUtil.isWindows()){
                                fileUrl = "D:\\commonservice\\image\\";
                            }else{
                                fileUrl = "/soft/commonservice/image/";
                            }
                            String pdfPath=fileUrl+ StringUtil.random(6) + ".pdf";
                            File file = new File(fileUrl);
                            if(!file.exists()){
                                File dir = new File(fileUrl);
                                if (!dir.exists()) {
                                    dir.mkdirs();
                                }
                            }
                            Map<String ,String >  urlmap= this.intelligentPreTrialManager.getPdf(dzUrl,pdfPath);
                            String materialSampleOid1=urlmap.get("materialSampleOid");
                            String materialSampleAddr1=urlmap.get("materialSampleAddr");
                            qlCaseMaterial.setMaterialSampleAddr(materialSampleOid1);
                            qlCaseMaterial.setMaterialSampleAddrYl(materialSampleAddr1);
                            qlCaseMaterialServiceFeginService.updateQlCaseMaterial(qlCaseMaterial);
                        }*/

                        //只有一张图
                      /*  if(dzUrl.size()==1){
                            qlCaseMaterial.setMaterialSampleAddr(materialSampleAddrs.get(0));
                            qlCaseMaterial.setMaterialSampleAddrYl(dzUrl.get(0));
                            qlCaseMaterialServiceFeginService.updateQlCaseMaterial(qlCaseMaterial);
                        }*/
                    }


                 /*   //没有细化保存事项材料中的样本oid
                    if(dzUrl.size()==0) {
                        if (sxServiceMaterial != null) {
                            String materSampleAddr = sxServiceMaterial.getMaterialSampleAddr();
                            if(StringUtils.isNotEmpty(sxServiceMaterial.getMaterialSimpleAddrYl())){
                                qlCaseMaterial.setMaterialSampleAddr(materSampleAddr);
                            }

                        }
                    }*/
                    qlCaseMaterial.setRefinedMaterialList(refinedMaterialList);
                }
            }


        return apiResultSet;
    }

    /***
    * @Description:  根据办件材料oid查看已上传的附件
    * @Author:liangss
    * @Date:2021/8/6
    * @Param: [caseMaterialOid]
    */
 @GetMapping(value = "/getQlCaseMaterialAttaList")
    public ApiResultSet<List<QlCaseMaterialAtta>> getQlCaseMaterialAttaList(String caseMaterialOid){
        Map<String, Object> modelMap=new HashMap<>();
        ApiResultSet<List<QlCaseMaterialAtta>>  apiResultSet=new ApiResultSet();
        ApiResultSet<List<QlCaseMaterialAtta>> qlCaseMaterialAttaResult=qlCaseMaterialAttaServiceFeginService.queryQlCaseMaterialAttaByCaseMaterialOid(caseMaterialOid);
        List<QlCaseMaterialAtta> qlCaseMaterialAttaList=new ArrayList<>();
        if(null!= qlCaseMaterialAttaResult.getData()) {
            qlCaseMaterialAttaList = qlCaseMaterialAttaResult.getData();
            for (QlCaseMaterialAtta qlCaseMaterialAtta : qlCaseMaterialAttaList) {
                String attaOid = qlCaseMaterialAtta.getAttaOid();
                if (StringUtils.isNotEmpty(attaOid)) {
                    ApiResultSet<QlSysAtta> sysAttaresult = qlSysAttaFeignService.querySysAttaByOid(attaOid);
                    if (null != sysAttaresult.getData()) {
                        QlSysAtta qlSysAtta = sysAttaresult.getData();
                        String fastdfsNginxUrl = qlSysAtta.getFastdfsNginxUrl();
                        qlCaseMaterialAtta.setSrc(fastdfsNginxUrl);
                    }
                }
            }
        }
        apiResultSet.setData(qlCaseMaterialAttaList);
        return apiResultSet;

    }



    /**
     * 材料材料下的精细化材料和精细化材料下的附件
     * @param caseOid
     * @return
     */
    @GetMapping(value = "/getAllQlCaseMaterialList")
    public ApiResultSet<List<QlCaseMaterial>>  getAllQlCaseMaterialList(String caseOid)  {

        Map<String, Object> modelMap=new HashMap<>();
        ApiResultSet<List<QlCaseMaterial>>  apiResultSet=new ApiResultSet();
        try {
            apiResultSet=qlCaseMaterialServiceFeginService.queryQlCaseMaterialByCaseOid(caseOid);
            if(null!=apiResultSet.getData()){
                List<QlCaseMaterial> qlCaseMaterials=apiResultSet.getData();
                for(QlCaseMaterial qlCaseMaterial:qlCaseMaterials){
                    String  caseMaterialOid=qlCaseMaterial.getCaseMaterialOid();
                    String materialOid=qlCaseMaterial.getMaterialOid();
                    String materialName=qlCaseMaterial.getMaterialName();
                    //查看材料关联表
                    ApiResultSet<List<QlCaseMaterialAtta>> qlCaseMaterialAttaResult=qlCaseMaterialAttaServiceFeginService.queryQlCaseMaterialAttaByCaseMaterialOid(caseMaterialOid);
                    List<QlCaseMaterialAtta> qlCaseMaterialAttaList=new ArrayList<>();
                    if(null!= qlCaseMaterialAttaResult.getData()){
                        qlCaseMaterialAttaList=qlCaseMaterialAttaResult.getData();
                        for(QlCaseMaterialAtta qlCaseMaterialAtta:qlCaseMaterialAttaList){
                            String attaOid=qlCaseMaterialAtta.getAttaOid();
                            if(StringUtils.isNotEmpty(attaOid)){
                                ApiResultSet<QlSysAtta>   sysAttaresult=qlSysAttaFeignService.querySysAttaByOid(attaOid);
                                if(null!=sysAttaresult.getData()){
                                    QlSysAtta qlSysAtta=sysAttaresult.getData();
                                    String fastdfsNginxUrl=qlSysAtta.getFastdfsNginxUrl();
                                    qlCaseMaterialAtta.setSrc(fastdfsNginxUrl);
                                }
                            }
                        }
                        qlCaseMaterial.setQlCaseMaterialAttaList(qlCaseMaterialAttaList);
                    }

                    List<String> dzUrl=new ArrayList<>();
                    //查看细化材料列表
                    List<RefinedMaterial> refinedMaterialList = null;
                    ApiResultSet<List<RefinedMaterial>>  refineResult= refinedMaterialFeginService.getRefinedMaterialListByMaterialOid(materialOid);
                    if(null!=refineResult.getData()){
                        refinedMaterialList=refineResult.getData();
                        for(RefinedMaterial refinedMaterial:refinedMaterialList){
                            String materialSampleOid= refinedMaterial.getMaterialSampleOid();
                            String materialSampleAddr = null;
                            if(StringUtils.isNotEmpty(materialSampleOid)){
                                ApiResultSet<SxSysAtta>  sxSysAttaApiResultSet=sxServiceAttaFeignService.getSxSysAttaByOId(materialSampleOid);
                                if(null!=sxSysAttaApiResultSet){
                                    SxSysAtta sxSysAtta=sxSysAttaApiResultSet.getData();
                                    //System.out.println("样本附件地址:" + sxSysAtta.getFilePath());
                                    //materialSampleAddr=SGfastDFSNginxUrl+sxSysAtta.getFilePath();
                                    if(sxSysAtta.getFilePath().contains("http")){
                                        materialSampleAddr=sxSysAtta.getFilePath();
                                    }
                                    refinedMaterial.setMaterialSampleAddr(materialSampleAddr);
                                    dzUrl.add(materialSampleAddr);
                                }

                            }
                        }
                    }
                    qlCaseMaterial.setRefinedMaterialList(refinedMaterialList);
                }
            }

        } catch (Exception e) {
        }
        return apiResultSet;
    }

    /***
    * @Description:  去黑白矫正图片和分类接口
    * @Author:liangss
    * @Date:2021/12/9
    * @Param: [caseOid, attaOid, serviceOid, serviceName, fastdfsNginxUrl]
    */
    @PostMapping(value = "/getEditImageAndClassifilerResult")
    public ApiResultSet getClassifilerAndPicOcrResult(@RequestBody ClassifyRec classifyRec) {
        Long beginTime = System.currentTimeMillis();
        Map<String, Object> modelMap=new HashMap<>();
        ApiResultSet apiResultSet=new ApiResultSet();
        try {
            apiResultSet=classifyManager.getEditImageAndClassifilerResult(classifyRec);
        } catch (Exception e) {
            log.error("材料分类失败：{}", e.getMessage());
            modelMap.put("success", false);
            modelMap.put("message", "材料分类失败");
            apiResultSet.setData(modelMap);
        }

        Long totalConsumedTime=System.currentTimeMillis()-beginTime;
        String time=totalConsumedTime.toString();
        apiResultSet.setTime(time);
        return apiResultSet;
    }

    /***
    * @Description: 获取分类的材料数据
    * @Author:liangss
    * @Date:2021/12/9
    * @Param: [classifyRec]
    */
    @PostMapping(value = "/getClassifilerMateial")
    public ApiResultSet getClassifilerMateial(@RequestBody ClassifyRec classifyRec) {
        Map<String, Object> modelMap=new HashMap<>();
        ApiResultSet apiResultSet=new ApiResultSet();
        try {
            apiResultSet=classifyManager.getClassifilerMateial(classifyRec);
        } catch (Exception e) {
            modelMap.put("success", false);
            modelMap.put("message", "获取材料失败");
            apiResultSet.setData(modelMap);
        }
        return apiResultSet;
    }


    /**
     * 获取分类和预审结果
     * @param caseOid 办件oid
     * @param attaOid 附件oid
     * @param serviceOid 事项oid
     * @param serviceName 事项name
     * @param fastdfsNginxUrl
     * @return
     */
    @GetMapping(value = "/getClassifilerResult")
    public ApiResultSet getClassifilerAndPicOcrResult(String caseOid, String attaOid, String serviceOid, String serviceName,String fastdfsNginxUrl) {
        Map<String, Object> modelMap=new HashMap<>();
        ApiResultSet apiResultSet=new ApiResultSet();
        try {
            apiResultSet=classifyManager.getClassifilerAndPicOcrResult(caseOid,attaOid,serviceOid,serviceName,fastdfsNginxUrl);
        } catch (Exception e) {
            modelMap.put("success", false);
            modelMap.put("message", "材料分类失败");
            apiResultSet.setData(modelMap);
        }
        return apiResultSet;
    }

    /**
     * 获取上传材料分类信息
     * @param caseOid
     * @param caseMaterialOid
     * @param attaOid
     * @return
     */
    @Override
    public ApiResultSet getUploadMaterialClassification(String caseOid, String caseMaterialOid, String attaOid) {
        Long beginTime = System.currentTimeMillis();
        Map<String, Object> modelMap=new HashMap<>();
        ApiResultSet apiResultSet=new ApiResultSet();
        try {
            apiResultSet=classifyManager.getUploadMaterialClassification(caseOid, caseMaterialOid, attaOid);
        } catch (Exception e) {
            log.error("材料分类失败：{}", e.getMessage());
            modelMap.put("success", false);
            modelMap.put("message", "材料分类失败");
            apiResultSet.setData(modelMap);
        }
        Long totalConsumedTime=System.currentTimeMillis()-beginTime;
        String time=totalConsumedTime.toString();
        apiResultSet.setTime(time);
        return apiResultSet;
    }

    /***
    * @Description:  去黑边操作
    * @Author:liangss
    * @Date:2021/9/14
    * @Param: [classifyRec]
    */
    @PostMapping(value = "/getEditImageBase64")
    public ApiResultSet getEditImageBase64(@RequestBody ClassifyRec classifyRec ){
        Map<String, Object> modelMap=new HashMap<>();
        ApiResultSet apiResultSet=new ApiResultSet();
        try {
            String baseValue=classifyRec.getBaseValue();
            String base64New=classifyManager.getEditImageBase64(baseValue);

            if(StringUtils.isNotEmpty(base64New)){
                int i = base64New.length();
                String  zhzf=base64New.substring(i-1);
                if(zhzf.equals("'")){
                    base64New = base64New.substring(0, base64New.length() - 1);
                }
              /*  base64New = base64New.substring(0, base64New.length() - 1);*/
                modelMap.put("success", true);
                modelMap.put("result", base64New);
            }else{
                modelMap.put("success", false);
                modelMap.put("message", "图片去黑边失败");
            }
            apiResultSet.setData(modelMap);
        } catch (Exception e) {
            modelMap.put("success", false);
            modelMap.put("message", "图片去黑边失败");
            apiResultSet.setData(modelMap);
        }
        return apiResultSet;

    }
    /*sort_page*/

    /***
    * @Description:排序
    * @Author:liangss
    * @Date:2021/9/14
    * @Param: [classifyRec]
    */
    @GetMapping(value = "/getSortPage")
    public ApiResultSet getSortPage(String  attaOids){
        ApiResultSet apiResultSet=new ApiResultSet();
        apiResultSet= classifyManager.getSortPage(attaOids);
        return apiResultSet;

    }


    /**
     * 拖动修改材料附件
     * @param caseOid
     * @param attaOid
     * @param caseMaterialOid
     * @param materialOid
     * @param refinedMaterialOid
     * @param materialCatalogOid
     * @param materialAttaOid
     * @param fastdfsNginxUrl
     * @return
     */
    @GetMapping(value = "/updateClassifilerAndPicOcrResult")
    public ApiResultSet updateClassifilerAndPicOcrResult(String caseOid, String attaOid, String caseMaterialOid,
                                                         String materialOid,String refinedMaterialOid, String materialCatalogOid,
                                                         String materialAttaOid,String fastdfsNginxUrl) {
        Map<String, Object> modelMap=new HashMap<>();
        ApiResultSet apiResultSet=new ApiResultSet();
        try {
            apiResultSet=classifyManager.updateClassifilerAndPicOcrResult(caseOid,attaOid,caseMaterialOid,materialOid, refinedMaterialOid,  materialCatalogOid,
                     materialAttaOid,fastdfsNginxUrl);
        } catch (Exception e) {
            modelMap.put("success", false);
            modelMap.put("message", "拖动失败");
            apiResultSet.setData(modelMap);
        }
        return apiResultSet;
    }

    /**
     * 审查附件
     * @param caseOid
     * @param attaOid
     * @param caseMaterialOid
     * @param materialOid
     * @param refinedMaterialOid
     * @param materialAttaOid
     * @return
     */
    @GetMapping(value = "/deleteClassifilerAndPicOcrResult")
    public ApiResultSet deleteClassifilerAndPicOcrResult(String caseOid, String attaOid, String caseMaterialOid,
                                                         String materialOid,String refinedMaterialOid, String materialAttaOid
                                                         ) {
        Map<String, Object> modelMap=new HashMap<>();
        ApiResultSet apiResultSet=new ApiResultSet();
        try {
            apiResultSet=classifyManager.deleteClassifilerAndPicOcrResult(caseOid,attaOid,caseMaterialOid,materialOid, refinedMaterialOid,  materialAttaOid);
        } catch (Exception e) {
            modelMap.put("success", false);
            modelMap.put("message", "删除失败");
            apiResultSet.setData(modelMap);
        }
        return apiResultSet;
    }




    @GetMapping(value = "/getClassifilerAndPicOcrResultLS")
    public ApiResultSet getClassifilerAndPicOcrResultLS(String caseOid, String attaOid, String serviceOid, String serviceName
            ,String fastdfsNginxUrl,String caseMaterialOid,String refinedMaterialOid) {
        Map<String, Object> modelMap=new HashMap<>();
        ApiResultSet apiResultSet=new ApiResultSet();
        try {
            apiResultSet=classifyManager.getClassifilerAndPicOcrResultLS(caseOid,attaOid,serviceOid,serviceName,fastdfsNginxUrl,caseMaterialOid,refinedMaterialOid);
        } catch (Exception e) {
            modelMap.put("success", false);
            modelMap.put("message", "材料分类失败");
            apiResultSet.setData(modelMap);
        }
        return apiResultSet;
    }






    @GetMapping(value = "/getQlCaseMaterialList")
    public ApiResultSet<List<QlCaseMaterial>>  getQlCaseMaterialList(String caseOid)  {
        Map<String, Object> modelMap=new HashMap<>();

        ApiResultSet<List<QlCaseMaterial>>  apiResultSet=new ApiResultSet();
        try {
            apiResultSet=qlCaseMaterialServiceFeginService.queryQlCaseMaterialByCaseOid(caseOid);
            if(null!=apiResultSet.getData()){
                List<QlCaseMaterial> qlCaseMaterials=apiResultSet.getData();
                for(QlCaseMaterial qlCaseMaterial:qlCaseMaterials){
                    //办件信息
                    ApiResultSet<QlCase> resultSet=qlCaseServiceFeginService.queryQlCaseByCaseOid(caseOid);
                    QlCase qlCase = null;
                    if(resultSet.getData() != null){
                        qlCase=resultSet.getData();
                    }
                    String serviceOid = qlCase.getServiceOid();
                    //审查要点
                    AhsSamplePicInfo ahsSamplePicInfo1 = new AhsSamplePicInfo();
                    ahsSamplePicInfo1.setMateriaOid(qlCaseMaterial.getMaterialOid());
                    ahsSamplePicInfo1.setSampleInfoOid(serviceOid);
                    List<AhsSamplePicInfo> ahsSamplePicInfoList = this.ahsSamplePicInfoManager.queryAhsSamplePicInfoList(ahsSamplePicInfo1);
                    qlCaseMaterial.setAttaListwgl(ahsSamplePicInfoList);

                    //查看材料
                    ApiResultSet<List<QlCaseMaterialAtta>> qlCaseMaterialAttaResult=qlCaseMaterialAttaServiceFeginService.queryQlCaseMaterialAttaByCaseMaterialOid(qlCaseMaterial.getCaseMaterialOid());
                    List<QlCaseMaterialAtta> qlCaseMaterialAttaList = null;
                    if(null!= qlCaseMaterialAttaResult.getData()){
                        qlCaseMaterialAttaList=qlCaseMaterialAttaResult.getData();
                        for(QlCaseMaterialAtta qlCaseMaterialAtta:qlCaseMaterialAttaList){
                            String attaOid=qlCaseMaterialAtta.getAttaOid();
                            if(StringUtils.isNotEmpty(attaOid)){
                                ApiResultSet<QlSysAtta>   sysAttaresult=qlSysAttaFeignService.querySysAttaByOid(attaOid);
                                if(null!=sysAttaresult.getData()){
                                    QlSysAtta qlSysAtta=sysAttaresult.getData();
                                    String fastdfsNginxUrl=qlSysAtta.getFastdfsNginxUrl();
                                    qlCaseMaterialAtta.setSrc(fastdfsNginxUrl);
                                }

                            }


                        }
                    }
                    qlCaseMaterial.setQlCaseMaterialAttaList(qlCaseMaterialAttaList);
                }

            }


        } catch (Exception e) {
        }
        return apiResultSet;
    }


    /**
     *
     * 根据办件oid、attaiod、classifierId初始化材料分类
     * @param caseOid
     * @param attaOid
     * @param classifierId
     * @return
     */
    @GetMapping(value = "/materialClassifyPrePrial")
    public ApiResultSet materialClassifyPrePrial(String caseOid, String attaOid, String classifierId)  {
        Map<String, Object> modelMap=new HashMap<>();
        ApiResultSet apiResultSet=new ApiResultSet();
        try {
            apiResultSet=classifyManager.materialClassifyPrePrial(caseOid,attaOid,classifierId);
        } catch (Exception e) {
            modelMap.put("success", false);
            modelMap.put("message", "材料分类失败");
            apiResultSet.setData(modelMap);
        }
        return apiResultSet;
    }


    /**
     * 根据办件oid，附件oid调用研究院的材料分类功能
     * @param caseOid
     * @param attaOid
     * @return
     */
    @GetMapping(value = "/getClassifilerByYjy")
    public ApiResultSet getClassifilerByYjy(String caseOid, String attaOid,String fastdfsNginxUrl) {
        Map<String, Object> modelMap=new HashMap<>();
        ApiResultSet apiResultSet=new ApiResultSet();
        try {
            apiResultSet=classifyManager.getClassifilerByYjy(caseOid,attaOid,fastdfsNginxUrl);
        } catch (Exception e) {
            modelMap.put("success", false);
            modelMap.put("message", "材料分类失败");
            apiResultSet.setData(modelMap);
        }
        return apiResultSet;
    }





    @GetMapping(value = "/initAutoClassifierCaseFileRecUploadList")
    public ApiResultSet initAutoClassifierCaseFileRecUploadList(String caseOid)  {
        ApiResultSet result=classifyManager.initAutoClassifierCaseFileRecUploadList(caseOid);
        return result;
    }

    @GetMapping(value = "/initAutoClassifierCaseFileList")
    public ApiResultSet initAutoClassifierCaseFileList(String caseOid)  {
        ApiResultSet result=classifyManager.initAutoClassifierCaseFileList(caseOid);
        return result;
    }



    @PostMapping(value = "/deleteClassifiler")
    public ApiResultSet deleteClassifiler(@RequestBody ClassifyRecVo classifyRecVo){
        ApiResultSet result=classifyManager.deleteClassifiler(classifyRecVo);
        return result;
    }

    @PostMapping(value = "/updateClassifilerList")
    public ApiResultSet updateClassifilerList(@RequestBody ClassifyRecVo classifyRecVo){
        ApiResultSet result=classifyManager.updateClassifilerList(classifyRecVo);
        return result;
    }

    /**
     * 保存和修改分类
     * @param classifyRec
     * @return
     */
    @PostMapping(value = "/updateClassifiler")
    public ApiResultSet updateClassifiler(@RequestBody ClassifyRec classifyRec){
        ApiResultSet result=classifyManager.updateclassifiler(classifyRec);
        return result;
    }

}
