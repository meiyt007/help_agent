package com.zfsoft.single.manager.clzs;


import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zfsoft.cases.data.QlCase;
import com.zfsoft.cases.data.QlCaseMaterial;
import com.zfsoft.cases.data.QlSysAtta;
import com.zfsoft.cases.service.QlCaseMaterialAttaService;
import com.zfsoft.cases.service.QlCaseMaterialService;
import com.zfsoft.cases.service.QlCaseService;
import com.zfsoft.cases.service.SysAttaService;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.data.BaseStaticParameter;
import com.zfsoft.platform.utils.data.SysAttaTemp;
import com.zfsoft.platform.utils.fileUtil.UploadUtil;
import com.zfsoft.platform.utils.validate.ResultInfoException;
import com.zfsoft.rest.pojo.ocr.*;
import com.zfsoft.rest.service.ocr.IOcrClassifierRestService;
import com.zfsoft.rest.service.ocr.IOcrCustomTemplateRestService;
import com.zfsoft.service.sxService.service.RefinedMaterialService;
import com.zfsoft.service.sxService.service.SxServiceMaterialService;
import com.zfsoft.service.sxService.service.SxServiceService;
import com.zfsoft.single.data.clzs.ClassifyRec;
import com.zfsoft.single.data.clzs.FaMaterialPicOcrResult;
import com.zfsoft.single.data.clzs.MaterialClassifyRecVo;
import com.zfsoft.single.data.clzs.dto.ClassifyRecVo;
import com.zfsoft.single.util.*;
import com.zfsoft.single.util.fa.atta.AttaBase64Result;
import com.zfsoft.single.util.fa.atta.FileManageUtil;
import com.zfsoft.superwindow.data.clzs.MaterialCatalog;
import com.zfsoft.superwindow.data.clzs.MaterialCatalogElement;
import com.zfsoft.service.sxService.data.RefinedMaterial;
import com.zfsoft.service.sxService.data.SxService;
import com.zfsoft.service.sxService.data.SxServiceMaterial;
import com.zfsoft.superwindow.dbaccess.data.DbFaMaterialPicOcrResult;
import com.zfsoft.superwindow.service.clzs.MaterialCatalogElementService;
import com.zfsoft.superwindow.service.clzs.MaterialCatalogService;
import com.zfsoft.superwindow.util.fa.AiTokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.URLDecoder;
import java.util.*;


/**
 * @ClassName IndustryClassifyManager
 * @Description: 材料分类实现类
 * @Author liangss
 * @Date 2021-06-12
 **/
@Service
@Slf4j
public class ClassifyManager {
    //材料目录
    //办件
    @Resource
    private QlCaseService qlCaseServiceFeginService;
    //事项材料
    @Resource
    private SxServiceMaterialService sxServiceMaterialFeginService;
    //材料目录
    @Resource
    private MaterialCatalogService materialCatalogFeginService;
    @Resource
    private AiTokenUtil aiTokenUtil;

    @Resource
    private MaterialCatalogElementService materialCatalogElementFeginService;

    @Resource
    private FaMaterialPicOcrResultManager faMaterialPicOcrResultManager;

    @Autowired(required = false)
    private IOcrCustomTemplateRestService ocrCustomTemplateRestService;
    @Autowired
    private QlCaseMaterialAttaService qlCaseMaterialAttaServiceFeginService;
    @Autowired
    private QlCaseMaterialService qlCaseMaterialServiceFeginService;
    @Resource
    private SysAttaService qlSysAttaFeignService;
    public final static String DEFAULT_CHARACTER_SET = "UTF-8";

//    @Resource
//    private IOcrClassifierRestService ocrClassifierRestService;

    @Resource
    private  ClassifyRecManager classifyRecManager;
    //精细化材料
    @Resource
    private RefinedMaterialService refinedMaterialFeginService;
    @Resource
    private SxServiceService sxServiceFeginService;


    //分类接口地址
    @Value("${zfsoft.yjyzsScan.flUrl}")
    private String flUrl;
    //印章接口地址
    @Value("${zfsoft.yjyzsScan.yzUrl}")
    private String yzUrl;
    //签字接口地址
    @Value("${zfsoft.yjyzsScan.qzUrl}")
    private String qzUrl;
    //修改图片去黑边
    @Value("${zfsoft.yjyzsScan.xztpUrl}")
    private String xztpUrl;
    //排序
    @Value("${zfsoft.yjyzsScan.pxUrl}")
    private String pxUrl;

   /* @Resource
    private QlSysAttaFeignService qlSysAttaFeignService;*/
    /*@Resource
    private CaseFileRecService caseFileRecService;*/

    /**
     * 图片去黑边
     * @param base64
     * @return
     */
    public String getEditImageBase64(String base64){
        String base64New="";
        LinkedHashMap<String, Object> params = new LinkedHashMap<String, Object>();
        params.put("data", base64);
        //调用研究院图片去黑边接口
        Boolean state=false;
        String res= HttpUtil.post(xztpUrl + "image_preprocess_base64", params);
        if(StrUtil.isNotEmpty(res)){
            JSONObject jsonObj = JSONObject.parseObject(res);
            base64New= (String) jsonObj.get("base64");
        }
        //System.out.println("base64New="+base64New);
       return base64New;
    }


    /***
    * @Description:  排序
    * @Author:liangss
    * @Date:2021/9/14
    * @Param: [attaOids]
    */
    public ApiResultSet getSortPage(String attaOids){
        Map<String, Object> modelMap=new HashMap<>();
        ApiResultSet apiResultSet=new ApiResultSet();
        String[] attalist=attaOids.split(",");
        LinkedHashMap<String, Object> params = new LinkedHashMap<String, Object>();
        params.put("image_id_list", attaOids);
        //调用研究院排序接口
        Boolean state=false;
        String res= HttpUtil.post(pxUrl + "sort_page", params);
        if(StrUtil.isNotEmpty(res)){
            JSONObject jsonObjimg = JSONObject.parseObject(res);
            JSONObject jsonObj= jsonObjimg.getJSONObject("sorted_page");
            for(String attaOid:attalist){
                if(null!= jsonObj.get(attaOid)){
                    String  xuhao=(String) jsonObj.get(attaOid);
                    modelMap.put(attaOid,xuhao) ;
                }else{
                    modelMap.put(attaOid,null) ;
                }

            }
        }
        apiResultSet.setData(modelMap);
        return apiResultSet;
    }


    /***
    * @Description: 获取分类的材料
    * @Author:liangss
    * @Date:2021/12/9
    * @Param: [classifyRec]
    */
    public ApiResultSet getClassifilerMateial(ClassifyRec classifyRec) throws Exception {
        ApiResultSet apiResultSet=new ApiResultSet();
        Map<String, Object> modelMap=new HashMap<>();
        //获取附件信息
        String picBase64 = classifyRec.getBaseValue();
        String  caseOid=classifyRec.getCaseOid();
        String  serviceOid=classifyRec.getServiceOid();
        String  serviceName=classifyRec.getServiceName();

        String beforeAttaOid="";
        //组装材料参数
        ApiResultSet<List<QlCaseMaterial>> resultSet = qlCaseMaterialServiceFeginService.queryQlCaseMaterialByCaseOid(caseOid);
        List<QlCaseMaterial> qlCaseMaterialList = resultSet.getData();
        String file_list="";
        Map<String,Map<String ,String>>  file_listmap=new HashMap<>();
        //办件材料map
        Map<String,QlCaseMaterial> qlCaseMaterialMap=new HashMap<>();

        for(QlCaseMaterial qlCaseMaterial:qlCaseMaterialList){
            String  materialName=qlCaseMaterial.getMaterialName();
            ApiResultSet<List<RefinedMaterial>>  listApiResultSet= refinedMaterialFeginService.getRefinedMaterialListByMaterialOid(qlCaseMaterial.getMaterialOid());
            if(null!=listApiResultSet.getData()){
                List<RefinedMaterial> refinedMaterialList=listApiResultSet.getData();
                for(RefinedMaterial refinedMaterial:refinedMaterialList){
                    Map<String ,String> maps=new HashMap<>();
                    String licenceOid=refinedMaterial.getLicenceOid();
                    String licenceName=refinedMaterial.getLicenceName();
                    String filename="";
                    if(StringUtils.isNotEmpty(licenceOid)){
                        filename="证照-"+licenceName+"-"+refinedMaterial.getRefinedMaterialName();
                        file_list=file_list+filename+",";
                    }else{
                        filename=serviceName+"-"+materialName+"-"+refinedMaterial.getRefinedMaterialName();
                        file_list=file_list+filename+",";
                    }
                    maps.put("materialCatalogOid",refinedMaterial.getMaterialCatalogOid());
                    maps.put("refinedMaterialOid",refinedMaterial.getOid());
                    maps.put("caseMaterialOid",qlCaseMaterial.getCaseMaterialOid());
                    maps.put("materialOid",qlCaseMaterial.getMaterialOid());
                /*    refinedMaterialMap.put(filename,maps);*/
                    file_listmap.put(filename,maps);
                }}
           /* qlCaseMaterialMap.put(qlCaseMaterial.getMaterialOid(),qlCaseMaterial);*/
        }
        System.out.println("******file_list="+file_list);
      /*  modelMap.put("qlCaseMaterialMap",qlCaseMaterialMap);*/
        modelMap.put("fileListMap",file_listmap);
        modelMap.put("fileList",file_list);
        modelMap.put("success", true);
      /*  modelMap.put("message","未找到对应材料");*/
        apiResultSet.setData(modelMap);
        return apiResultSet;

    }


    /***
    * @Description:  根据数据去黑边分类
    * @Author:liangss
    * @Date:2021/12/9
    * @Param: [classifyRec]
    */
    public ApiResultSet getEditImageAndClassifilerResult(ClassifyRec classifyRec) throws Exception {
        ApiResultSet apiResultSet=new ApiResultSet();
        Map<String, Object> modelMap=new HashMap<>();

        QlSysAtta beforeQlSysAtta=new QlSysAtta();
        QlSysAtta qlSysAtta=new QlSysAtta();
        //获取附件信息
        String picBase64 = classifyRec.getBaseValue();
        String  caseOid=classifyRec.getCaseOid();
        String  serviceOid=classifyRec.getServiceOid();
        String  serviceName=classifyRec.getServiceName();
        String beforeAttaOid="";

        String file_list="";
        Map<String,Map<String ,String>>  file_listmap=new HashMap<>();
        file_list=classifyRec.getFileList();
        file_listmap=classifyRec.getFileListMap();

         beforeQlSysAtta=this.uploadBase64PdfToQlSysAtta(picBase64);
         log.info("beforeQlSysAtta: {}", beforeQlSysAtta);
         beforeAttaOid=beforeQlSysAtta.getAttaOid();
        if(StringUtils.isNotEmpty(file_list)&&null!=file_listmap&&file_listmap.size()>0){
        }else{
            file_listmap=new HashMap<>();
            //组装材料参数
            ApiResultSet<List<QlCaseMaterial>> resultSet = qlCaseMaterialServiceFeginService.queryQlCaseMaterialByCaseOid(caseOid);
            List<QlCaseMaterial> qlCaseMaterialList = resultSet.getData();
            for(QlCaseMaterial qlCaseMaterial:qlCaseMaterialList){
                String  materialName=qlCaseMaterial.getMaterialName();
                ApiResultSet<List<RefinedMaterial>>  listApiResultSet= refinedMaterialFeginService.getRefinedMaterialListByMaterialOid(qlCaseMaterial.getMaterialOid());
                if(null!=listApiResultSet.getData()){
                    List<RefinedMaterial> refinedMaterialList=listApiResultSet.getData();
                    for(RefinedMaterial refinedMaterial:refinedMaterialList){
                        Map<String ,String> maps=new HashMap<>();
                        String licenceOid=refinedMaterial.getLicenceOid();
                        String licenceName=refinedMaterial.getLicenceName();
                        String filename="";
                        if(StringUtils.isNotEmpty(licenceOid)){
                            filename="证照-"+licenceName+"-"+refinedMaterial.getRefinedMaterialName();
                            file_list=file_list+filename+",";
                        }else{
                            filename=serviceName+"-"+materialName+"-"+refinedMaterial.getRefinedMaterialName();
                            file_list=file_list+filename+",";
                        }
                        maps.put("materialCatalogOid",refinedMaterial.getMaterialCatalogOid());
                        maps.put("refinedMaterialOid",refinedMaterial.getOid());
                        maps.put("caseMaterialOid",qlCaseMaterial.getCaseMaterialOid());
                        maps.put("materialOid",qlCaseMaterial.getMaterialOid());
                        file_listmap.put(filename,maps);
                    }

                }}
            }

            System.out.println("******file_list="+file_list);
        // file_list="个人独资企业-个人独资企业登记（备案）申请书,个人独资企业-承诺书,个人独资企业-变更前后投资人身份证,个人独资企业-营业执照正、副本,个人独资企业-转让协议, ";
        LinkedHashMap<String, Object> params = new LinkedHashMap<String, Object>();
        params.put("data", picBase64);
        params.put("file_list", file_list);
        params.put("attaOid", beforeAttaOid);

        String  shMaterialName="";
        Long serialNum = null;
        String key="";
        //调用研究院分类接口
        Boolean state=false;
        String mataril="";
        String page="";
        String base64="";
        String res= HttpUtil.post(flUrl + "classification_base64", params);
        if(StrUtil.isNotEmpty(res)){
            JSONObject jsonObj = JSONObject.parseObject(res);
            Map<String,Object> map=jsonObj;
            mataril=(String)map.get("class");//所属材料名称
            state=(Boolean)map.get("state");
            page=(String)map.get("page");
            key=mataril+"-"+page;
            base64=(String) map.get("base64");
            if(StringUtils.isNotEmpty(base64)){
                int i = base64.length();
                String  zhzf=base64.substring(i-1);
                if(zhzf.equals("'")){
                    base64 = base64.substring(0, base64.length() - 1);
                }
            }
            qlSysAtta=this.uploadBase64PdfToQlSysAtta(base64);
        }
        if(!state){
            ClassifyRec classifyRecNew  = saveClassifyRecNew(caseOid, qlSysAtta.getAttaOid(),
                    null, null,null,null,null,null);
            classifyRecNew.setBeforeAttaOid(beforeAttaOid);
            classifyRecNew.setFastdfsNginxUrl(qlSysAtta.getFastdfsNginxUrl());
            modelMap.put("classifyRec", classifyRecNew);
            modelMap.put("success", false);
            modelMap.put("message","未找到对应的材料");
            apiResultSet.setData(modelMap);
            return apiResultSet;
        }
        //根据返回值验证属于那个材料和哪个细化材料
        Map<String ,String> stringMap=file_listmap.get(key);
        //未找到对应模板id的事项材料
        if (stringMap != null && stringMap.size()>0 ) {
            String materialOid=stringMap.get("materialOid");
            String refinedMaterialOid=stringMap.get("refinedMaterialOid");
            String caseMaterialOid=stringMap.get("caseMaterialOid");
            String materialCatalogOid=stringMap.get("materialCatalogOid");
            //保存分类
            ClassifyRec classifyRecNew  = saveClassifyRecNew(caseOid, qlSysAtta.getAttaOid(),
                    null,caseMaterialOid,
                    materialOid,
                    "",
                    materialCatalogOid,
                    refinedMaterialOid);
            classifyRecNew.setBeforeAttaOid(beforeAttaOid);
            classifyRecNew.setFastdfsNginxUrl(qlSysAtta.getFastdfsNginxUrl());
            modelMap.put("classifyRec", classifyRecNew);
            modelMap.put("success", true);
            modelMap.put("message","该附件对应"+shMaterialName+"材料");
            apiResultSet.setData(modelMap);
            return apiResultSet;
        }else {
            ClassifyRec classifyRecNew  = saveClassifyRecNew(caseOid, qlSysAtta.getAttaOid(),
                    null, null,null,null,null,null);
            classifyRecNew.setBeforeAttaOid(beforeAttaOid);
            classifyRecNew.setFastdfsNginxUrl(qlSysAtta.getFastdfsNginxUrl());
            modelMap.put("classifyRec", classifyRecNew);
            modelMap.put("success", false);
            modelMap.put("message","未找到对应材料");
            apiResultSet.setData(modelMap);
            return apiResultSet;

        }
    }


    /***
    * @Description:  把base64图片上传到办件服务上
    * @Author:liangss
    * @Date:2021/12/9
    * @Param: [imageBase64]
    */
    public  QlSysAtta  uploadBase64PdfToQlSysAtta(String imageBase64){
        QlSysAtta qlSysAtta=new QlSysAtta();
        try {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            byte[] bytes = Base64.getDecoder().decode(imageBase64);
            InputStream inputStream = new ByteArrayInputStream(bytes);
            String originaFiliname="image"+System.currentTimeMillis()+".png";
            //MultipartFile file = new MockMultipartFile("image"+new Date().getTime()+".png", bytes);
            // MultipartFile file = new MockMultipartFile("File","image"+new Date().getTime()+".png","text/plain",inputStream);
            UploadUtil uploadUtil = new UploadUtil(request);
            String filePath = null;
            MultipartFile file = new MockMultipartFile("File","image"+System.currentTimeMillis()+".png","text/plain",inputStream);
            filePath = uploadUtil.uploadFile(file);
            log.info("111111ffilePath: {}" ,filePath);
            SysAttaTemp sysAttaFile = uploadUtil.getSysAttaFile(filePath, "");


            BeanUtils.copyProperties(sysAttaFile,qlSysAtta);
            qlSysAtta.setFilePath(sysAttaFile.getFastdfsNginxUrl());
            qlSysAtta.setAttaOid(sysAttaFile.getAttaOid());
            //保存附件信息
            // apiResultSet=sysAttaFeginService.saveSysAtta(atta);
            ApiResultSet<QlSysAtta> qlSysAtta1= qlSysAttaFeignService.saveSysAtta(qlSysAtta);
            qlSysAtta=qlSysAtta1.getData();
        } catch (Exception e) {
            log.info("uploadBase64PdfToQlSysAtta error: {}", e.getMessage());
            e.printStackTrace();
        }
        return  qlSysAtta;
    }




    /**
     * 材料分类通过研究院接口并预审
     * @param caseOid
     * @param attaOid
     * @param fastdfsNginxUrl
     * @return
     * @throws Exception
     */
    public ApiResultSet getClassifilerAndPicOcrResult(String caseOid, String attaOid, String serviceOid, String serviceName,String fastdfsNginxUrl) throws Exception {
        ApiResultSet apiResultSet=new ApiResultSet();
        Map<String, Object> modelMap=new HashMap<>();
        //获取附件信息
   /*     ApiResultSet<QlSysAtta>   sysAttaresult=qlSysAttaFeignService.querySysAttaByOid(attaOid);
        QlSysAtta qlSysAtta=sysAttaresult.getData();
        String fastdfsNginxUrl=qlSysAtta.getFastdfsNginxUrl();*/

        AttaBase64Result attaBase64Result=getAttaBase64ByUrl(fastdfsNginxUrl);
        String picBase64 = "";
        if(null!=attaBase64Result){
            picBase64 = attaBase64Result.getResult();
        }else {
            log.error("未获取附件base64信息!");
            modelMap.put("success", false);
            modelMap.put("message","未获取附件base64信息!");
            throw new Exception("未获取附件base64信息!");
        }

        //组装材料参数
        ApiResultSet<List<QlCaseMaterial>> resultSet = qlCaseMaterialServiceFeginService.queryQlCaseMaterialByCaseOid(caseOid);
        List<QlCaseMaterial> qlCaseMaterialList = resultSet.getData();
        String file_list="";
        //细化材料map
        Map<String,RefinedMaterial> refinedMaterialMap=new HashMap<>();
        //办件材料map
        Map<String,QlCaseMaterial> qlCaseMaterialMap=new HashMap<>();
        for(QlCaseMaterial qlCaseMaterial:qlCaseMaterialList){
            String  materialName=qlCaseMaterial.getMaterialName();
            ApiResultSet<List<RefinedMaterial>>  listApiResultSet= refinedMaterialFeginService.getRefinedMaterialListByMaterialOid(qlCaseMaterial.getMaterialOid());
            if(null!=listApiResultSet.getData()){
                List<RefinedMaterial> refinedMaterialList=listApiResultSet.getData();
                for(RefinedMaterial refinedMaterial:refinedMaterialList){
                    String licenceOid=refinedMaterial.getLicenceOid();
                    String licenceName=refinedMaterial.getLicenceName();
                    String filename="";
                    if(StringUtils.isNotEmpty(licenceOid)){
                        filename="证照-"+licenceName+"-"+refinedMaterial.getRefinedMaterialName();
                        file_list=file_list+filename+",";
                    }else{
                        filename=serviceName+"-"+materialName+"-"+refinedMaterial.getRefinedMaterialName();
                        file_list=file_list+filename+",";
                    }
                    refinedMaterialMap.put(filename,refinedMaterial);

                }}
            qlCaseMaterialMap.put(qlCaseMaterial.getMaterialOid(),qlCaseMaterial);
           /* file_list=file_list+serviceName+"-"+materialName+",";*/
        }

        System.out.println("******file_list="+file_list);

       // file_list="个人独资企业-个人独资企业登记（备案）申请书,个人独资企业-承诺书,个人独资企业-变更前后投资人身份证,个人独资企业-营业执照正、副本,个人独资企业-转让协议, ";
        LinkedHashMap<String, Object> params = new LinkedHashMap<String, Object>();
        params.put("data", picBase64);
        params.put("file_list", file_list);
        params.put("attaOid", attaOid);

        String  shMaterialName="";
        Long serialNum = null;
        String key="";
        //调用研究院分类接口
        Boolean state=false;
        String mataril="";
        String page="";
        String base64="";
        String res= HttpUtil.post(flUrl + "classification_base64", params);
        if(StrUtil.isNotEmpty(res)){
            JSONObject jsonObj = JSONObject.parseObject(res);
            Map<String,Object> map=jsonObj;
             mataril=(String)map.get("class");//所属材料名称
             state=(Boolean)map.get("state");
             page=(String)map.get("page");
             key=mataril+"-"+page;
             base64=(String) map.get("base64");
        }
        if(!state){
            ClassifyRec classifyRec  = saveClassifyRecNew(caseOid, attaOid, null, null,null,null,null,null);
            modelMap.put("classifyRec", classifyRec);
            modelMap.put("success", false);
            modelMap.put("message","未找到对应的材料");
            apiResultSet.setData(modelMap);
            return apiResultSet;
        }
        //根据返回值验证属于那个材料和哪个细化材料
        QlCaseMaterial ssqlCaseMaterial = null;
        RefinedMaterial ssRefinedMaterial= ssRefinedMaterial=refinedMaterialMap.get(key);
        if(null!=ssRefinedMaterial){
            ssqlCaseMaterial=qlCaseMaterialMap.get(ssRefinedMaterial.getMaterialOid());
        }
        //未找到对应模板id的事项材料
        if (ssqlCaseMaterial != null && ssRefinedMaterial!=null ) {
            //保存分类
            ClassifyRec classifyRec  = saveClassifyRecNew(caseOid, attaOid,
                    null, ssqlCaseMaterial.getCaseMaterialOid(),
                    ssqlCaseMaterial.getMaterialOid(),
                    ssRefinedMaterial.getBaiduTemplateIds(),
                    ssRefinedMaterial.getMaterialCatalogOid(),
                    ssRefinedMaterial.getOid());
            //智能预审
           /* if(StringUtils.isNotEmpty(ssRefinedMaterial.getMaterialCatalogOid())){
                Map<String, Object> prePrialResult= intelligentPretrialmaterialPrePrial(caseOid,attaOid,
                        ssqlCaseMaterial.getMaterialOid(),ssqlCaseMaterial.getCaseMaterialOid(),
                        ssRefinedMaterial.getOid(),ssRefinedMaterial.getMaterialCatalogOid(),picBase64,picPath);
                        modelMap.put("prePrialResult", prePrialResult);
            }*/
            modelMap.put("classifyRec", classifyRec);
            modelMap.put("success", true);
            modelMap.put("message","该附件对应"+shMaterialName+"材料");
            apiResultSet.setData(modelMap);
            return apiResultSet;
        }else {
            ClassifyRec classifyRec  = saveClassifyRecNew(caseOid, attaOid,
                    null, null,null,null,null,null);
            modelMap.put("classifyRec", classifyRec);
            modelMap.put("success", false);
            modelMap.put("message","未找到对应材料");
            apiResultSet.setData(modelMap);
            return apiResultSet;

        }
    }

    /**
     * 获取上传材料分类信息（细化材料一个，直接分类；细化材料多个，调用分类接口分类）
     * @param caseOid
     * @param caseMaterialOid
     * @param attaOid
     * @return
     * @throws Exception
     */
    public ApiResultSet getUploadMaterialClassification(String caseOid, String caseMaterialOid, String attaOid) throws Exception {
        ApiResultSet apiResultSet = new ApiResultSet();
        Map<String, Object> modelMap = new HashMap<>();

        ApiResultSet<QlCase> qlCaseResult = qlCaseServiceFeginService.queryQlCaseByCaseOid(caseOid);
        if (qlCaseResult == null || qlCaseResult.getData() == null) {
            throw new ResultInfoException("当前办件信息为空！");
        }
        QlCase qlCase = qlCaseResult.getData();

        ApiResultSet<QlCaseMaterial> qlCaseMaterialResult = qlCaseMaterialServiceFeginService.queryMaterialByCaseMaterialOid(caseMaterialOid);
        if (qlCaseMaterialResult == null || qlCaseMaterialResult.getData() == null) {
            throw new ResultInfoException("当前办件材料信息为空！");
        }
        QlCaseMaterial qlCaseMaterial = qlCaseMaterialResult.getData();

        // 细化材料名字字符串
        String fileList="";
        //细化材料map
        Map<String,RefinedMaterial> refinedMaterialMap = new HashMap<>();
        ApiResultSet<List<RefinedMaterial>>  refinedMaterialListResult = refinedMaterialFeginService.getRefinedMaterialListByMaterialOid(qlCaseMaterial.getMaterialOid());
        if (refinedMaterialListResult != null && refinedMaterialListResult.getData() != null) {
            List<RefinedMaterial> refinedMaterialList = refinedMaterialListResult.getData();
            if (refinedMaterialList.size() < 1) {
                throw new ResultInfoException("当前办件材料未配置细化材料！");
            }
            if (refinedMaterialList.size() == 1) {
                RefinedMaterial refinedMaterial = refinedMaterialList.get(0);
                ClassifyRec classifyRec  = saveClassifyRecNew(caseOid, attaOid,
                        null, qlCaseMaterial.getCaseMaterialOid(),
                        refinedMaterial.getMaterialOid(),
                        refinedMaterial.getBaiduTemplateIds(),
                        refinedMaterial.getMaterialCatalogOid(),
                        refinedMaterial.getOid());
                modelMap.put("classifyRec", classifyRec);
                modelMap.put("success", true);
                modelMap.put("message","该附件对应[" + refinedMaterial.getRefinedMaterialName() + "]材料");
                apiResultSet.setData(modelMap);
                return apiResultSet;
            }
            for(RefinedMaterial refinedMaterial : refinedMaterialList){
                String licenceOid=refinedMaterial.getLicenceOid();
                String licenceName=refinedMaterial.getLicenceName();
                String filename="";
                if(StringUtils.isNotEmpty(licenceOid)){
                    filename = "证照-"+licenceName+"-"+refinedMaterial.getRefinedMaterialName();
                    fileList = fileList + filename + ",";
                }else{
                    filename = qlCase.getServiceName() + "-"+qlCaseMaterial.getMaterialName()+"-"+refinedMaterial.getRefinedMaterialName();
                    fileList = fileList + filename + ",";
                }
                refinedMaterialMap.put(filename,refinedMaterial);
            }
        }

        //获取附件信息
        ApiResultSet<QlSysAtta> sysAttaResult = qlSysAttaFeignService.querySysAttaByOid(attaOid);
        if (sysAttaResult == null || sysAttaResult.getData() == null) {
            throw new ResultInfoException("当前办件材料附件信息为空！");
        }
        QlSysAtta qlSysAtta = sysAttaResult.getData();
        AttaBase64Result attaBase64Result = getAttaBase64ByUrl(qlSysAtta.getFastdfsNginxUrl());
        if(attaBase64Result == null || StrUtil.isBlank(attaBase64Result.getResult())){
            throw new ResultInfoException("未获取当前办件材料base64信息！");
        }

        // 分类接口参数封装
        LinkedHashMap<String, Object> params = new LinkedHashMap<String, Object>();
        params.put("data", attaBase64Result.getResult());
        params.put("file_list", fileList);
        params.put("attaOid", attaOid);
        //调用研究院分类接口
        String key="";
        Boolean state=false;
        String materialClass = "";
        String page="";
        String res= HttpUtil.post(flUrl + "classification_base64", params);
        if(StrUtil.isNotEmpty(res)){
            JSONObject jsonObj = JSONObject.parseObject(res);
            Map<String,Object> map=jsonObj;
            materialClass=(String)map.get("class");//所属材料名称
            state=(Boolean)map.get("state");
            page=(String)map.get("page");
            key=materialClass+"-"+page;
        }
        if(!state){
            ClassifyRec classifyRec  = saveClassifyRecNew(caseOid, attaOid, null, null,null,null,null,null);
            modelMap.put("classifyRec", classifyRec);
            modelMap.put("success", false);
            modelMap.put("message","未正确分类该材料！");
            apiResultSet.setData(modelMap);
            return apiResultSet;
        }
        RefinedMaterial refinedMaterial = refinedMaterialMap.get(key);
        if (refinedMaterial != null ) {
            //保存分类
            ClassifyRec classifyRec  = saveClassifyRecNew(caseOid, attaOid,
                    null, qlCaseMaterial.getCaseMaterialOid(),
                    refinedMaterial.getMaterialOid(),
                    refinedMaterial.getBaiduTemplateIds(),
                    refinedMaterial.getMaterialCatalogOid(),
                    refinedMaterial.getOid());
            modelMap.put("classifyRec", classifyRec);
            modelMap.put("success", true);
            modelMap.put("message","该附件对应[" + qlCaseMaterial.getMaterialName() + "]材料");
            apiResultSet.setData(modelMap);
            return apiResultSet;
        }else {
            ClassifyRec classifyRec  = saveClassifyRecNew(caseOid, attaOid,
                    null, null,null,null,null,null);
            modelMap.put("classifyRec", classifyRec);
            modelMap.put("success", false);
            modelMap.put("message","未找到对应材料！");
            apiResultSet.setData(modelMap);
            return apiResultSet;
        }
    }


    public ApiResultSet updateClassifilerAndPicOcrResult(String caseOid,
                                                         String attaOid,
                                                         String caseMaterialOid,
                                                         String materialOid,
                                                         String refinedMaterialOid,
                                                         String materialCatalogOid,
                                                         String materialAttaOid,
                                                         String fastdfsNginxUrl) throws Exception {
        ApiResultSet apiResultSet=new ApiResultSet();
        Map<String, Object> modelMap=new HashMap<>();
/*
        ApiResultSet<QlCaseMaterialAtta> qlCaseMaterialAttaApiResultSet= qlCaseMaterialAttaServiceFeginService.queryQlCaseMaterialAttaByOid(materialAttaOid);
        QlCaseMaterialAtta qlCaseMaterialAtta=qlCaseMaterialAttaApiResultSet.getData();*/

        //删除办件材料和附件的关联表
        if(StringUtils.isNotEmpty(materialAttaOid)){
            qlCaseMaterialAttaServiceFeginService.deleteByOid(materialAttaOid);
        }

        //删除之前的预审信息
        FaMaterialPicOcrResult faMaterialPicOcrResult=new FaMaterialPicOcrResult();
        faMaterialPicOcrResult.setUuid(caseOid);
        faMaterialPicOcrResult.setAttaOid(attaOid);
        if(StringUtils.isNotEmpty(caseMaterialOid)){
            faMaterialPicOcrResult.setCaseMaterialOid(caseMaterialOid);
        }
        DbFaMaterialPicOcrResult dbFaMaterialPicOcrResult=faMaterialPicOcrResultManager.getFaMaterialPicOcrResultByFaMaterialPicOcrResult(faMaterialPicOcrResult);
        if(null!=dbFaMaterialPicOcrResult){
            faMaterialPicOcrResultManager.del(String.valueOf(dbFaMaterialPicOcrResult.getId()));
        }

        //保存分类
        ClassifyRec classifyRec  = saveClassifyRecNew(caseOid, attaOid, null, caseMaterialOid,
                materialOid,null,materialCatalogOid,refinedMaterialOid);
        //未找到对应模板id的事项材料
        if (StringUtils.isNotEmpty(materialOid)) {
            //智能预审
            if(StringUtils.isNotEmpty(materialCatalogOid)){
                AttaBase64Result attaBase64Result=getAttaBase64ByUrl(fastdfsNginxUrl);
                String picBase64 = "";
                if(null!=attaBase64Result){
                    picBase64 = attaBase64Result.getResult();
                }else {
                    modelMap.put("success", false);
                    modelMap.put("message","未获取附件base64信息!");
                    throw new Exception("未获取附件base64信息!");
                }

                File path = new File(ResourceUtils.getURL("classpath:").getPath());
                //String fileUrl= path+"\\modelTemples\\model";
                //String dirPath = File.separator + "pic" + File.separator + "clshjg" + File.separator;
                String dirPath= path+"\\modelTemples\\model\\pic\\clshjg\\";
                String picName=attaOid + "_" + StringUtil.random(6)+"new"+ ".jpg";
                //String picNameNew=picNameQZ+ ".jpg";
                String  picPath = dirPath + picName;

                File dir = new File(dirPath);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                File file = new File(picPath);
                if(!file.exists()){
                    //复制文件系统中的附件到本地系统
                    HttpUtil.downloadFile(fastdfsNginxUrl, cn.hutool.core.io.FileUtil.file(picPath));
                }


                Map<String, Object> prePrialResult= intelligentPretrialmaterialPrePrial(caseOid,attaOid,
                        materialOid,caseMaterialOid,
                        refinedMaterialOid,materialCatalogOid,picBase64,picPath);
                 modelMap.put("prePrialResult", prePrialResult);
            }

        }
        apiResultSet.setData(modelMap);
        modelMap.put("success", true);
        modelMap.put("classifyRec", classifyRec);
        return apiResultSet;
    }

    /**
     * 删除分类预审信息和材料关联
     * @param caseOid
     * @param attaOid
     * @param caseMaterialOid
     * @param materialOid
     * @param refinedMaterialOid
     * @param materialAttaOid
     * @return
     * @throws Exception
     */
    public ApiResultSet deleteClassifilerAndPicOcrResult(String caseOid,
                                                         String attaOid,
                                                         String caseMaterialOid,
                                                         String materialOid,
                                                         String refinedMaterialOid,
                                                         String materialAttaOid
                                                    ) throws Exception {

        ApiResultSet apiResultSet=new ApiResultSet();
        Map<String, Object> modelMap=new HashMap<>();
        if(StringUtils.isNotEmpty(attaOid)){
            //删除之前的预审信息
            FaMaterialPicOcrResult faMaterialPicOcrResult=new FaMaterialPicOcrResult();
            faMaterialPicOcrResult.setUuid(caseOid);
            faMaterialPicOcrResult.setAttaOid(attaOid);
            if(StringUtils.isNotEmpty(caseMaterialOid)){
                faMaterialPicOcrResult.setCaseMaterialOid(caseMaterialOid);
            }
            if(StringUtils.isNotEmpty(materialOid)){
                faMaterialPicOcrResult.setMaterialOid(materialOid);
            }
            if(StringUtils.isNotEmpty(refinedMaterialOid)){
                faMaterialPicOcrResult.setRefinedMaterialOid(refinedMaterialOid);
            }
            DbFaMaterialPicOcrResult dbFaMaterialPicOcrResult=faMaterialPicOcrResultManager.getFaMaterialPicOcrResultByFaMaterialPicOcrResult(faMaterialPicOcrResult);
            if(null!=dbFaMaterialPicOcrResult){
                faMaterialPicOcrResultManager.del(String.valueOf(dbFaMaterialPicOcrResult.getId()));
            }
            //删除办件材料和附件的关联表
            if(StringUtils.isNotEmpty(materialAttaOid)){
                qlCaseMaterialAttaServiceFeginService.deleteByOid(materialAttaOid);
            }
            modelMap.put("success", true);
        }

        apiResultSet.setData(modelMap);
        return apiResultSet;
    }

    /**
     * 拖动修改分类
     * @param caseOid
     * @param attaOid
     * @param caseMaterialOid
     * @param materialOid
     * @param refinedMaterialOid
     * @param fastdfsNginxUrl
     * @return
     * @throws Exception
     */
    public ApiResultSet updateClassifilerAndPicOcrResultOld(String caseOid,
                                                         String attaOid,
                                                         String caseMaterialOid,
                                                         String materialOid,
                                                         String refinedMaterialOid,
                                                         String fastdfsNginxUrl) throws Exception {
        ApiResultSet apiResultSet=new ApiResultSet();
        Map<String, Object> modelMap=new HashMap<>();

        //删除之前的预审信息
        FaMaterialPicOcrResult faMaterialPicOcrResult=new FaMaterialPicOcrResult();
        faMaterialPicOcrResult.setUuid(caseOid);
        faMaterialPicOcrResult.setAttaOid(attaOid);
        if(StringUtils.isNotEmpty(caseMaterialOid)){
            faMaterialPicOcrResult.setCaseMaterialOid(caseMaterialOid);
        }
        DbFaMaterialPicOcrResult dbFaMaterialPicOcrResult=faMaterialPicOcrResultManager.getFaMaterialPicOcrResultByFaMaterialPicOcrResult(faMaterialPicOcrResult);
        if(null!=dbFaMaterialPicOcrResult){
            faMaterialPicOcrResultManager.del(String.valueOf(dbFaMaterialPicOcrResult.getId()));
        }

        //获取附件信息
      /*     ApiResultSet<QlSysAtta>   sysAttaresult=qlSysAttaFeignService.querySysAttaByOid(attaOid);
        QlSysAtta qlSysAtta=sysAttaresult.getData();
        String fastdfsNginxUrl=qlSysAtta.getFastdfsNginxUrl();*/
        AttaBase64Result attaBase64Result=getAttaBase64ByUrl(fastdfsNginxUrl);
        String picBase64 = "";
        if(null!=attaBase64Result){
            picBase64 = attaBase64Result.getResult();
        }else {
            log.error("材料分类识别预审", "未获取附件base64信息！办件主键为：{}，附件主键为：{}", caseOid, attaOid, null);
            modelMap.put("success", false);
            modelMap.put("message","未获取附件base64信息!");
            throw new Exception("未获取附件base64信息!");
        }

        String fileUrl= FaStaticParam.PROJECT_PATH+"\\modelTemples\\model";
        String dirPath = File.separator + "pic" + File.separator + "clshjg" + File.separator;
        String picNameQZ = attaOid + "_" + StringUtil.random(6) ;
        String picName=picNameQZ+"new"+ ".jpg";
        String picNameNew=picNameQZ+ ".jpg";

        String  picPath = fileUrl + dirPath + picName;
        String  picPathNew = fileUrl + dirPath + picNameNew;
        File file = new File(picPath);
        if(!file.exists()){
            File dir = new File(dirPath);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            //复制文件系统中的附件到本地系统
            HttpUtil.downloadFile(fastdfsNginxUrl, cn.hutool.core.io.FileUtil.file(picPath));
        }

        //事项材料
        ApiResultSet<SxServiceMaterial> sxServiceMaterialApiResultSet=sxServiceMaterialFeginService.getSxServiceMaterialByOid(materialOid);
        SxServiceMaterial sxServiceMaterial = null;
        if(sxServiceMaterialApiResultSet.getData()!=null){
            sxServiceMaterial= sxServiceMaterialApiResultSet.getData();
        }
        //细化材料
        ApiResultSet<RefinedMaterial>  refinedMaterialApiResultSet=  refinedMaterialFeginService.getRefinedMaterialByOid(refinedMaterialOid);
        RefinedMaterial refinedMaterial=null;
        if(null!=refinedMaterialApiResultSet.getData()){
            refinedMaterial=refinedMaterialApiResultSet.getData();
        }
        //未找到对应模板id的事项材料
        if (sxServiceMaterial != null && refinedMaterial!=null ) {
            //保存分类
            ClassifyRec classifyRec  = saveClassifyRecNew(caseOid, attaOid, null, caseMaterialOid,
                    materialOid,refinedMaterial.getBaiduTemplateIds(),refinedMaterial.getMaterialCatalogOid(),refinedMaterial.getOid());
            //智能预审
            if(StringUtils.isNotEmpty(refinedMaterial.getMaterialCatalogOid())){
                Map<String, Object> prePrialResult= intelligentPretrialmaterialPrePrial(caseOid,attaOid,
                        materialOid,caseMaterialOid,
                        refinedMaterialOid,refinedMaterial.getMaterialCatalogOid(),picBase64,picPath);
                modelMap.put("prePrialResult", prePrialResult);
            }
            modelMap.put("classifyRec", classifyRec);
            modelMap.put("success", true);
            modelMap.put("message","该附件对应"+sxServiceMaterial.getMaterialName()+"材料");
            apiResultSet.setData(modelMap);
            return apiResultSet;
        }else {
            ClassifyRec classifyRec  = saveClassifyRecNew(caseOid, attaOid, null,
                    null,null,null,null,null);
            modelMap.put("classifyRec", classifyRec);
            modelMap.put("success", false);
            modelMap.put("message","未找到对应材料");
            apiResultSet.setData(modelMap);
            return apiResultSet;

        }

    }





    /**
     * 根據地址获取base64数据
     * @param url
     * @return
     * @throws Exception
     */
    public AttaBase64Result getAttaBase64ByUrl(String url) throws Exception {
        AttaBase64Result base64Result = new AttaBase64Result();
        String base64= FileManageUtil.getImgFromUrl(url);
        if(null!=base64){
            String result=base64;
            result = result.replace("\n", "").replace("\t", "").replace("\r", "");
            result = URLDecoder.decode(result, DEFAULT_CHARACTER_SET);
            base64Result.setResult(result);
            String resultNew = base64Result.getResult();
            String strOne = " ";
            String strTwo = "+";
            resultNew = resultNew.replaceAll(strOne, strTwo);
            base64Result.setResult(resultNew);
        }
        return base64Result;
    }







    /**
     * 材料分类通过研究院接口并预审
     * @param caseOid
     * @param attaOid
     * @param fastdfsNginxUrl
     * @return
     * @throws Exception
     */
    public ApiResultSet getClassifilerByYjy(String caseOid, String attaOid,String fastdfsNginxUrl) throws Exception {
        ApiResultSet apiResultSet=new ApiResultSet();
        Map<String, Object> modelMap=new HashMap<>();
        //获取附件信息
   /*     ApiResultSet<QlSysAtta>   sysAttaresult=qlSysAttaFeignService.querySysAttaByOid(attaOid);
        QlSysAtta qlSysAtta=sysAttaresult.getData();
        String fastdfsNginxUrl=qlSysAtta.getFastdfsNginxUrl();*/

        AttaBase64Result attaBase64Result=getAttaBase64ByUrl(fastdfsNginxUrl);
        String picBase64 = "";
        if(null!=attaBase64Result){
            picBase64 = attaBase64Result.getResult();
        }else {
            log.error("材料分类识别预审", "未获取附件base64信息！办件主键为：{}，附件主键为：{}", caseOid, attaOid, null);
            modelMap.put("success", false);
            modelMap.put("message","未获取附件base64信息!");
            throw new Exception("未获取附件base64信息!");
        }

        String fileUrl= FaStaticParam.PROJECT_PATH+"\\modelTemples\\model";
        String dirPath = File.separator + "pic" + File.separator + "clshjg" + File.separator;
        String picNameQZ = attaOid + "_" + StringUtil.random(6) ;
        String picName=picNameQZ+"new"+ ".jpg";
        String picNameNew=picNameQZ+ ".jpg";

        String  picPath = fileUrl + dirPath + picName;
        String  picPathNew = fileUrl + dirPath + picNameNew;
        File file = new File(picPath);
        if(!file.exists()){
            File dir = new File(dirPath);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            //复制文件系统中的附件到本地系统
            HttpUtil.downloadFile(fastdfsNginxUrl, cn.hutool.core.io.FileUtil.file(picPath));
        }
        //获取办件信息
        ApiResultSet<QlCase> qlCaseApiResultSet=qlCaseServiceFeginService.queryQlCaseByCaseOid(caseOid);
        QlCase qlCase = null;
        if(qlCaseApiResultSet.getData() != null){
            qlCase=qlCaseApiResultSet.getData();
        }
        //事项oid
        String serviceOid = qlCase.getServiceOid();
        //事项名称
        String serviceName="";
        //获取事项信息
        ApiResultSet<SxService> sxServiceApiResultSet = sxServiceFeginService.getSxServiceByOid(serviceOid);
        SxService sxService = sxServiceApiResultSet.getData();
        if (null != sxService) {
            serviceName=sxService.getServiceName();
        }

        //组装材料参数
        ApiResultSet<List<QlCaseMaterial>> resultSet = qlCaseMaterialServiceFeginService.queryQlCaseMaterialByCaseOid(caseOid);
        List<QlCaseMaterial> qlCaseMaterialList = resultSet.getData();
        String file_list="";
        for(QlCaseMaterial qlCaseMaterial:qlCaseMaterialList){
            String  materialName=qlCaseMaterial.getMaterialName();
            file_list=file_list+serviceName+"-"+materialName+",";
        }

        file_list="个人独资企业-个人独资企业登记（备案）申请书,个人独资企业-承诺书,个人独资企业-变更前后投资人身份证,个人独资企业-营业执照正、副本,个人独资企业-转让协议, ";
        LinkedHashMap<String, Object> params = new LinkedHashMap<String, Object>();
        params.put("data", picBase64);
        params.put("file_list", file_list);


        String  shMaterialName="";
        Long serialNum = null;

        //调用研究院分类接口
        String res= HttpUtil.post(flUrl + "classification_base64", params);
        if(StrUtil.isNotEmpty(res)){
            JSONObject jsonObj = JSONObject.parseObject(res);
            Map<String,Object> map=jsonObj;
            String mataril=(String)map.get("class");//所属材料名称
            Boolean state=(Boolean)map.get("state");
            String page=(String)map.get("page");//页码
           /* String reason=(String)map.get("reason");
            String confidence=(String)map.get("confidence");*/
            if(state){
                  shMaterialName=mataril.toString();
                  serialNum=Long.parseLong(page);
                  shMaterialName=shMaterialName.replaceAll(serviceName+"-","");
            }
        }

        if(StringUtils.isEmpty(shMaterialName)){
            ClassifyRec classifyRec  = saveClassifyRecNew(caseOid, attaOid, null, null,null,null,null,null);
            modelMap.put("classifyRec", classifyRec);
            modelMap.put("success", false);
            modelMap.put("message","未找到对应的材料");
            apiResultSet.setData(modelMap);
            return apiResultSet;
        }

        shMaterialName="转让协议（原件）";

        //根据返回值验证属于那个材料和哪个细化材料
        QlCaseMaterial ssqlCaseMaterial = null;
        RefinedMaterial ssRefinedMaterial=null;
        for (QlCaseMaterial material : qlCaseMaterialList) {
            //事项材料
            ApiResultSet<SxServiceMaterial> sxmResult=sxServiceMaterialFeginService.getSxServiceMaterialByOid(material.getMaterialOid());
            if(sxmResult.getData()!=null){
                SxServiceMaterial sxServiceMaterial= sxmResult.getData();
                String  materialName=material.getMaterialName();
                if (materialName.equals(shMaterialName)) {
                    //找到模板id对应事项材料
                    ssqlCaseMaterial = material;
                    String materiaOid=ssqlCaseMaterial.getMaterialOid();
                    //精细化材料列表
                    ApiResultSet<List<RefinedMaterial>>  listApiResultSet= refinedMaterialFeginService.
                            getRefinedMaterialListByMaterialOid(materiaOid);
                    if(null!=listApiResultSet.getData()){
                        List<RefinedMaterial> refinedMaterialList=listApiResultSet.getData();
                        for(RefinedMaterial refinedMaterial:refinedMaterialList){
                            Long  serialNumber=refinedMaterial.getSerialNumber();
                            if(serialNumber==serialNum+1){
                                ssRefinedMaterial=refinedMaterial;
                                break;
                            }
                        }
                    }
                    break;
                }
            }

        }
        //未找到对应模板id的事项材料
        if (ssqlCaseMaterial != null && ssRefinedMaterial!=null ) {
            //保存分类
            ClassifyRec classifyRec  = saveClassifyRecNew(caseOid, attaOid, null, ssqlCaseMaterial.getCaseMaterialOid(),
                    ssqlCaseMaterial.getMaterialOid(),ssRefinedMaterial.getBaiduTemplateIds(),ssRefinedMaterial.getMaterialCatalogOid(),ssRefinedMaterial.getOid());
            //智能预审
            Map<String, Object> prePrialResult= intelligentPretrialmaterialPrePrial(caseOid,attaOid,ssqlCaseMaterial.getMaterialOid(),ssqlCaseMaterial.getCaseMaterialOid(),
                    ssRefinedMaterial.getOid(),ssRefinedMaterial.getMaterialCatalogOid(),picBase64,picPath);
            modelMap.put("classifyRec", classifyRec);
            modelMap.put("success", true);
            modelMap.put("message","该附件对应"+shMaterialName+"材料");
            apiResultSet.setData(modelMap);
            return apiResultSet;
        }else {
            ClassifyRec classifyRec  = saveClassifyRecNew(caseOid, attaOid, null, null,null,null,null,null);
            modelMap.put("classifyRec", classifyRec);
            modelMap.put("success", false);
            modelMap.put("message","未找到对应材料");
            apiResultSet.setData(modelMap);
            return apiResultSet;

        }
    }

    /**
     * 分类后材料预审
     * @param caseOid 办件oid
     * @param attaOid  附件oid
     * @param materialOid 事项材料oid
     * @param caseMaterialOid 办件材料oid
     * @param refinedMaterialOid 细化材料oid
     * @param materialCatalogOid 目录oid
     * @param picBase64 图片oid
     * @return
     * @throws Exception
     */
    public Map<String, Object>  intelligentPretrialmaterialPrePrial(String caseOid,String attaOid,
                                                                    String materialOid,
                                                                    String  caseMaterialOid,
                                                                    String  refinedMaterialOid,
                                                                    String materialCatalogOid,
                                                                    String picBase64,
                                                                    String picPath)
            throws Exception {

        Map<String, Object> ocrResultMap = new HashMap<String, Object>();
        MaterialCatalog cata = null;
        // 获得目录子项
        /*ApiResultSet<List<MaterialCatalog>> list= materialCatalogFeginService.queryList(parentCataOid);
        List<MaterialCatalog> cataList=null;
        if(list!=null&&list.getData()!=null&&list.getData().size()>0){
             cataList=list.getData();
             cata = cataList.get(0);
        }else{
            ocrResultMap.put("message","未关联目录");
        }*/
        ApiResultSet<MaterialCatalog> materialCatalogApiResultSet=materialCatalogFeginService.getMaterialCatalogOid(materialCatalogOid);
        if(null!=materialCatalogApiResultSet.getData()){
            cata=materialCatalogApiResultSet.getData();
        }else {
            ocrResultMap.put("message","未关联目录");
        }

        if(null!=cata){
            // 调用百度自定义模板识别接口
            OcrCustomTemplateRequest ocrCustomTemplateRequest = aiTokenUtil.getTokenRequest(OcrCustomTemplateRequest.class);
            ocrCustomTemplateRequest.setImgBase64(picBase64);
            ocrCustomTemplateRequest.setTemplateId(cata.getBaiduTemplateId());
            OcrCustomTemplateResponse ocrCustomTemplateResponse = ocrCustomTemplateRestService.customTemplate(ocrCustomTemplateRequest);
            log.info("ocrCustomTemplateResponse", JSON.toJSONString(ocrCustomTemplateResponse));
            List<Map<String, Object>> blockJsonArray = new ArrayList<Map<String, Object>>();
            // 调用成功
            if (200 == ocrCustomTemplateResponse.getCode()) {
                List<OcrCustomTemplateItemResponse> ocrCustomTemplateItemResponseList = ocrCustomTemplateResponse.getOcrCustomTemplateItemResponseList();
                // 获取百度识别区块识别结果集合
                MaterialCatalogElement materialCatalogElement=new MaterialCatalogElement();
                materialCatalogElement.setMaterialCatalogOid(cata.getMaterialCatalogOid());
                //List<MaterialCatalogElement> faMaterialCatalogMetadataList=this.materialCatalogElementManager.queryList(materialCatalogElement);
                ApiResultSet<List<MaterialCatalogElement>> listRest=materialCatalogElementFeginService.queryList(materialCatalogElement);
                List<MaterialCatalogElement> faMaterialCatalogMetadataList=null;
                if(listRest!=null&&listRest.getData()!=null){
                    faMaterialCatalogMetadataList=listRest.getData();
                }
                //检验印章和签字
                Map<String, Object> sealList=this.checkSealByBase64(picBase64);
                Map<String, Object>  signList=this.checkSignByBase64(picBase64);

                for (MaterialCatalogElement faMaterialCatalogMetadata : faMaterialCatalogMetadataList) {
                    Map<String, Object> blockJson = new HashMap<String, Object>();
                    blockJson.put("code", faMaterialCatalogMetadata.getElementCode());
                    blockJson.put("name", faMaterialCatalogMetadata.getElementName());
                    String words = "";
                    for (OcrCustomTemplateItemResponse itemResponse : ocrCustomTemplateItemResponseList) {
                        String name = itemResponse.getName();
                        words = itemResponse.getWord();
                        // 获得区块列表
                        if (faMaterialCatalogMetadata.getElementName().equals(name)) {
                            if (StrUtil.isNotBlank(itemResponse.getHeight())) {
                                blockJson.put("height", itemResponse.getHeight());
                                blockJson.put("width", itemResponse.getWidth());
                                blockJson.put("left", itemResponse.getLeft());
                                blockJson.put("top", itemResponse.getTop());
                            }
                            break;
                        }
                    }
                    blockJson.put("words", words);
                    blockJsonArray.add(blockJson);
                }
                String  sealListJson="";
                String signListJson="";
                if(sealList.size()>0){
                    sealListJson=new JSONObject(sealList).toString();
                }
                if(signList.size()>0){
                    signListJson=new JSONObject(signList).toString();

                }

                ocrResultMap.put("code", FaStaticParam.INTER_STATUS_CODE_SUCCESS);
                ocrResultMap.put("result", blockJsonArray);
                ocrResultMap.put("success", true);
                String resultJson = new JSONObject(ocrResultMap).toString();
                saveResult(caseOid, materialOid, refinedMaterialOid,
                        cata.getCatalogCode(), attaOid, CommonUtil.md5(picBase64),
                        resultJson, null ,sealListJson,signListJson,caseMaterialOid,picPath);
                return ocrResultMap;

            }else{
                ocrResultMap.put("success", false);
            }


        }
        return ocrResultMap;
    }



    /**
     *
     * @param caseOid 办件oid
     * @param materialOid 材料
     * @param refinedMaterialOid 细化材料oid
     * @param cataCode 目录code
     * @param attaOid 附件oid
     * @param base64MD5  附件base64MD5加密字符串
     * @param resultJson 识别结果json
     * @param obj
     * @param sealListJson 印章识别结果
     * @param signListJson 签字识别结果
     * @throws Exception
     */
    private void saveResult(String caseOid, String materialOid, String refinedMaterialOid, String cataCode, String attaOid, String base64MD5,
                            String resultJson, Object obj,String sealListJson,String signListJson,String caseMaterialOid,String picPath) throws Exception {
        FaMaterialPicOcrResult ocrResult = new FaMaterialPicOcrResult();
        ocrResult.setUuid(caseOid);
        ocrResult.setMaterialOid(materialOid);

        //ocrResult.setMaterialAttaOid(materialAttaOid);
        ocrResult.setCaseMaterialOid(caseMaterialOid);
        ocrResult.setRefinedMaterialOid(refinedMaterialOid);
        ocrResult.setAttaOid(attaOid);
        ocrResult.setSealResultJson(sealListJson);
        ocrResult.setSignResultJson(signListJson);
        ocrResult.setLocalImageUrl(picPath);
        if(StringUtils.isNotEmpty(picPath)){
            BufferedImage image = ImageIO.read(new File(picPath));
            int imageHeight=image.getHeight();
            int imageWedth=image.getWidth();
            ocrResult.setImageHeight(String.valueOf(imageHeight));
            ocrResult.setImageWidth(String.valueOf(imageWedth));
        }
        //保存ocr识别新对应办件记录材料、附件记录表
        /*ocrResult.setCaseFileAttaRecOid(caseFileAttaRecOid);*/
        ocrResult.setCataCode(cataCode);
        ocrResult.setPicBase64Md5(base64MD5);
        ocrResult.setResultJson(resultJson);
        // 将处理结果保存到数据库中
        this.faMaterialPicOcrResultManager.saveOrUpdate(ocrResult);
        log.info("保存或修改材料目录图片识别结果信息", "保存成功！办件主键为：{}，材料主键为：{}，目标编号为：{}，图片Base64的MD5值为：{}，识别结果json为：{}", caseOid, materialOid, cataCode, base64MD5, resultJson);
        // OCRRecord记录保存
       /* OcrRecord ocrRecord = new OcrRecord();
        ocrRecord.setAttaOid(attaOid);
        ocrRecord.setCaseOid(caseOid);
        if(obj instanceof BusinessLiceInfo){
            ocrRecord.setBusinessLicenseStatus(BaseStaticParameter.YES);
        } else {
            ocrRecord.setBusinessLicenseStatus(BaseStaticParameter.NO);
        }
        //对象为空时，保存识别结果json信息
        ocrRecord.setOcrResult(obj == null || this.allFieldIsNull(obj)==true ? resultJson : JsonUtil.objToJsonStr(obj));
        this.ocrRecordManager.saveOrUpdate(ocrRecord);*/
    }

    public boolean allFieldIsNull(Object o) {
        try {
            if(null==o){
                return true;
            }
            for (Field field : o.getClass().getDeclaredFields()) {
                //把私有属性公有化
                field.setAccessible(true);
                Object object = field.get(o);
                if (!Objects.isNull(object)) {
                    return false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


    /**
     * 检验印章接口
     * @param picBase64
     * @return
     * @throws Exception
     */
    public String getSealByBase64(String picBase64) throws Exception {
        List<Map<String, Object>> seal=new ArrayList<>();
        Map<String, Object> map=new HashMap<>();
        //yzUrl="http://101.230.251.254:10504/";
        LinkedHashMap<String, Object> params = new LinkedHashMap<String, Object>();
        params.put("data", picBase64);
        String res = HttpUtil.post(yzUrl + "predict_base64", params);
        /*if (StrUtil.isNotEmpty(res)) {


        }*/
        return res;
    }

    /**
     * 检验签字接口
     * @param picBase64
     * @return
     * @throws Exception
     */
    public String  getSignByBase64(String picBase64) throws Exception {
        List<Map<String, Object>> sign=new ArrayList<>();
        Map<String, Object> map=new HashMap<>();
        // qzUrl="http://101.230.251.254:10505/";
        LinkedHashMap<String, Object> params = new LinkedHashMap<String, Object>();
        params.put("data", picBase64);
        String res = HttpUtil.post(qzUrl + "predict_base64", params);
        return res;
    }




    /**
     * 检验印章接口
     * @param picBase64
     * @return
     * @throws Exception
     */
    public Map<String, Object> checkSealByBase64(String picBase64) throws Exception {
        Map<String, Object>   ocrResultMap = new HashMap<String, Object>();
        List<Map<String, Object>> seal=new ArrayList<>();
        Map<String, Object> map=new HashMap<>();
        //yzUrl="http://101.230.251.254:10504/";
        LinkedHashMap<String, Object> params = new LinkedHashMap<String, Object>();
        params.put("data", picBase64);
        String res = HttpUtil.post(yzUrl + "predict_base64", params);
        if (StrUtil.isNotEmpty(res)) {
            ocrResultMap.put("success", true);
            JSONObject jsonObjimg = JSONObject.parseObject(res);
            Map<String,Object> map2=jsonObjimg.getJSONObject("stamp");
            List boxes= (List) map2.get("boxes");
            if(boxes.size()>0){
                for (int i = 0; i < boxes.size(); i++) {
                    List boxesChild=(List) boxes.get(i);
                    if(boxesChild.size()>0){
                        map=new HashMap<>();
                        map.put("class_name", boxesChild.get(0));
                        map.put("x", boxesChild.get(1));
                        map.put("y", boxesChild.get(2));
                        map.put("w", boxesChild.get(3));
                        map.put("h", boxesChild.get(4));
                        seal.add(map);
                    }
                }
            }
            ocrResultMap.put("success", true);
            ocrResultMap.put("size",boxes.size());
        }else{
            ocrResultMap.put("success", false);
        }

        ocrResultMap.put("result",seal);
        return ocrResultMap;
    }

    /**
     * 检验签字接口
     * @param picBase64
     * @return
     * @throws Exception
     */
    public  Map<String, Object>  checkSignByBase64(String picBase64) throws Exception {

        Map<String, Object>   ocrResultMap = new HashMap<String, Object>();
        List<Map<String, Object>> sign=new ArrayList<>();
        Map<String, Object> map=new HashMap<>();
        // qzUrl="http://101.230.251.254:10505/";
        LinkedHashMap<String, Object> params = new LinkedHashMap<String, Object>();
        params.put("data", picBase64);
        String res = HttpUtil.post(qzUrl + "predict_base64", params);
        if (StrUtil.isNotEmpty(res)) {
            ocrResultMap.put("success", true);
            JSONObject jsonObjimg = JSONObject.parseObject(res);
            Map<String,Object> map2=jsonObjimg.getJSONObject("sign");
            List boxes= (List) map2.get("boxes");
            if(boxes.size()>0){
                for (int i = 0; i < boxes.size(); i++) {
                    List boxesChild=(List) boxes.get(i);
                    if(boxesChild.size()>0){
                        map=new HashMap<>();
                        map.put("class_name", boxesChild.get(0));
                        map.put("x", boxesChild.get(1));
                        map.put("y", boxesChild.get(2));
                        map.put("w", boxesChild.get(3));
                        map.put("h", boxesChild.get(4));
                        sign.add(map);
                    }
                }
            }
            ocrResultMap.put("success", true);
            ocrResultMap.put("size",boxes.size());

        }else{
            ocrResultMap.put("success", false);
        }

        ocrResultMap.put("result",sign);
        return ocrResultMap;
    }





    /**
     * 保存分类记录
     *
     * @param: caseOid
     * @param: attaOid
     * @return: void
     * @Auther: chenbw
     * @Date: 2020/7/29 15:57
     */
    private String saveClassifyRec(String caseOid, String attaOid, String classifierId, String caseFileAttaRecOid) {
        ClassifyRec classifyRec=new  ClassifyRec();
        if (StrUtil.isNotBlank(caseFileAttaRecOid)) {
            classifyRec.setCaseFileAttaRecOid(caseFileAttaRecOid);
            classifyRec.setCaseMaterialOid(caseFileAttaRecOid);
            classifyRec.setClassifierStatus(BaseStaticParameter.YES);
        } else {
            classifyRec.setClassifierStatus(BaseStaticParameter.NO);
        }
        classifyRec.setCaseOid(caseOid);
        classifyRec.setAttaOid(attaOid);
        classifyRec.setClassifierId(classifierId);
        classifyRecManager.saveOrUpdate(classifyRec);
        return classifyRec.getOid();
    }


    private ClassifyRec saveClassifyRecNew(String caseOid, String attaOid, String classifierId, String  caseMaterialOid ,
                                           String materialOid,String baiduTemplateIds,String materialCatalogOid,String refinedMaterialOid) {
        ClassifyRec classifyRec=new  ClassifyRec();
        if (StrUtil.isNotBlank(caseMaterialOid)) {
           /* classifyRec.setCaseFileAttaRecOid(caseFileAttaRecOid);*/
            classifyRec.setMaterialOid(materialOid);
            classifyRec.setCaseMaterialOid(caseMaterialOid);
            classifyRec.setClassifierStatus(BaseStaticParameter.YES);
        } else {
            classifyRec.setClassifierStatus(BaseStaticParameter.NO);
        }
        classifyRec.setRefinedMaterialOid(refinedMaterialOid);
        classifyRec.setBaiduTemplateIds(baiduTemplateIds);
        classifyRec.setMaterialCatalogOid(materialCatalogOid);
        classifyRec.setCaseOid(caseOid);
        classifyRec.setAttaOid(attaOid);
        classifyRec.setClassifierId(classifierId);
        classifyRec.setDelFlag(0);
        classifyRecManager.saveOrUpdate(classifyRec);
        return classifyRec;
    }









    public ApiResultSet initAutoClassifierCaseFileList(String caseOid){
        ApiResultSet apiResultSet=new ApiResultSet();
        Map<String, Object> modelMap=new HashMap<>();

        List<MaterialClassifyRecVo>  materialClassifyRecVoList=new ArrayList();
        ClassifyRec classifyRecOne=new  ClassifyRec();
        classifyRecOne.setCaseOid(caseOid);
        classifyRecOne.setClassifierStatus(BaseStaticParameter.YES);
        List<ClassifyRec>  classifyRecList=classifyRecManager.queryClassifyRecList(classifyRecOne);

        ApiResultSet<List<QlCaseMaterial>> resultSet = qlCaseMaterialServiceFeginService.queryQlCaseMaterialByCaseOid(caseOid);
        List<QlCaseMaterial> qlCaseMaterialList = resultSet.getData();
        for(QlCaseMaterial qlCaseMaterial:qlCaseMaterialList){
            MaterialClassifyRecVo materialClassifyRecVo=new MaterialClassifyRecVo();
            String caseMaterialOid=qlCaseMaterial.getCaseMaterialOid();
            List<QlSysAtta> attaListwgl=new ArrayList<>();
            QlSysAtta atta = null;
            for(ClassifyRec classifyRec:classifyRecList){
                String caseMaterialOidOne=classifyRec.getCaseMaterialOid();
                if(caseMaterialOid.equals(caseMaterialOidOne)){
                    ApiResultSet<QlSysAtta>   sysAttaresult=qlSysAttaFeignService.querySysAttaByOid(classifyRec.getAttaOid());
                    atta=sysAttaresult.getData();
                    attaListwgl.add(atta);
                }
            }

            qlCaseMaterial.setAttaList(attaListwgl);
            materialClassifyRecVo.setCaseMaterialOid(caseMaterialOid);
            materialClassifyRecVo.setQlCaseMaterial(qlCaseMaterial);
            materialClassifyRecVoList.add(materialClassifyRecVo);

        }

        ClassifyRec classifyRecTwo=new  ClassifyRec();
        classifyRecTwo.setCaseOid(caseOid);
        classifyRecTwo.setClassifierStatus(BaseStaticParameter.NO);
        List<ClassifyRec>  unClassifyRecList=classifyRecManager.queryClassifyRecList(classifyRecTwo);
        for(ClassifyRec classifyRec:unClassifyRecList){
            ApiResultSet<QlSysAtta>   sysAttaresult=qlSysAttaFeignService.querySysAttaByOid(classifyRec.getAttaOid());
            QlSysAtta  atta=sysAttaresult.getData();
            classifyRec.setQlSysAtta(atta);
        }

        modelMap.put("classifyRecList",classifyRecList);
        modelMap.put("unClassifyRecList",unClassifyRecList);
        modelMap.put("caseMaterials",qlCaseMaterialList);
        modelMap.put("materialClassifyRecVoList",materialClassifyRecVoList);
        apiResultSet.setData(modelMap);
        return apiResultSet;

    }



    public ApiResultSet initAutoClassifierCaseFileRecUploadList(String caseOid){
        ApiResultSet apiResultSet=new ApiResultSet();
        Map<String, Object> modelMap=new HashMap<>();

        ClassifyRec classifyRecOne=new  ClassifyRec();
        classifyRecOne.setCaseOid(caseOid);
        classifyRecOne.setClassifierStatus(BaseStaticParameter.YES);
        List<ClassifyRec>  classifyRecList=classifyRecManager.queryClassifyRecList(classifyRecOne);

        ApiResultSet<List<QlCaseMaterial>> resultSet = qlCaseMaterialServiceFeginService.queryQlCaseMaterialByCaseOid(caseOid);
        List<QlCaseMaterial> qlCaseMaterialList = resultSet.getData();
        for(QlCaseMaterial materialAtta:qlCaseMaterialList){
            String caseMaterialOid=materialAtta.getCaseMaterialOid();
            List<QlSysAtta> attaListwgl=new ArrayList<>();
            List<ClassifyRec> qlcaseClassifyRecList=new ArrayList<>();
            QlSysAtta atta = null;
            for(ClassifyRec classifyRec:qlcaseClassifyRecList){
                  String caseMaterialOidOne=classifyRec.getCaseMaterialOid();
                  if(caseMaterialOid.equals(caseMaterialOidOne)){
                      ApiResultSet<QlSysAtta>   sysAttaresult=qlSysAttaFeignService.querySysAttaByOid(classifyRec.getAttaOid());
                      atta=sysAttaresult.getData();
                      classifyRec.setQlSysAtta(atta);
                      qlcaseClassifyRecList.add(classifyRec);
                      attaListwgl.add(atta);
                  }

            }
            materialAtta.setClassifyRecList(qlcaseClassifyRecList);
            materialAtta.setAttaListwgl(attaListwgl);
        }



        ClassifyRec classifyRecTwo=new  ClassifyRec();
        classifyRecTwo.setCaseOid(caseOid);
        classifyRecTwo.setClassifierStatus(BaseStaticParameter.NO);
        List<ClassifyRec>  unClassifyRecList=classifyRecManager.queryClassifyRecList(classifyRecTwo);
        for(ClassifyRec classifyRec:unClassifyRecList){
            ApiResultSet<QlSysAtta>   sysAttaresult=qlSysAttaFeignService.querySysAttaByOid(classifyRec.getAttaOid());
            QlSysAtta  atta=sysAttaresult.getData();
            classifyRec.setQlSysAtta(atta);
        }

        modelMap.put("classifyRecList",classifyRecList);
        modelMap.put("unClassifyRecList",unClassifyRecList);
        modelMap.put("caseMaterials",qlCaseMaterialList);
        apiResultSet.setData(modelMap);
        return apiResultSet;

    }
    public ApiResultSet deleteClassifiler(ClassifyRecVo classifyRecVo){
        ApiResultSet apiResultSet=new ApiResultSet();
        Map<String, Object> modelMap=new HashMap<>();
        String caseOid=classifyRecVo.getCaseOid();
        String classifierId=classifyRecVo.getClassifierId();
        //删除之前的分类记录
        ClassifyRec classifyRecTwo=new ClassifyRec();
        classifyRecTwo.setCaseOid(caseOid);
        List<ClassifyRec>  unClassifyRecList=classifyRecManager.queryClassifyRecList(classifyRecTwo);
        for(ClassifyRec classifyRec:unClassifyRecList){
            classifyRecManager.del(String.valueOf(classifyRec.getId()));
        }
        modelMap.put("message","success");
        apiResultSet.setData(modelMap);
        return apiResultSet;
    }





    public ApiResultSet updateClassifilerList(ClassifyRecVo classifyRecVo){
        ApiResultSet apiResultSet=new ApiResultSet();
        Map<String, Object> modelMap=new HashMap<>();
        String caseOid=classifyRecVo.getCaseOid();
        String classifierId=classifyRecVo.getClassifierId();
        //删除之前的分类记录
        ClassifyRec classifyRecTwo=new ClassifyRec();
        classifyRecTwo.setCaseOid(caseOid);
        List<ClassifyRec>  unClassifyRecList=classifyRecManager.queryClassifyRecList(classifyRecTwo);
        for(ClassifyRec classifyRec:unClassifyRecList){
            classifyRecManager.del(String.valueOf(classifyRec.getId()));
        }
        //重新生成新的分类记录
        List<QlCaseMaterial> caseMaterials=classifyRecVo.getCaseMaterials();
        for(QlCaseMaterial materialAtta:caseMaterials) {
            String caseMaterialOid = materialAtta.getCaseMaterialOid();
           // List<ClassifyRec> classifyRecList = materialAtta.getClassifyRecList();
            List<ClassifyRec> classifyRecList=null;
           for(ClassifyRec idcr:classifyRecList){
               ClassifyRec classifyRec=new ClassifyRec();
               classifyRec.setCaseFileAttaRecOid(caseMaterialOid);
               classifyRec.setCaseMaterialOid(caseMaterialOid);
               classifyRec.setClassifierStatus(BaseStaticParameter.YES);
               classifyRec.setCaseOid(caseOid);
               classifyRec.setClassifierId(classifierId);
               classifyRec.setAttaOid(idcr.getAttaOid());
               classifyRecManager.saveOrUpdate(classifyRec);
           }

        }



        apiResultSet.setData(modelMap);
        return apiResultSet;
    }


    public ApiResultSet updateclassifiler(ClassifyRec classifyRec){
        ApiResultSet apiResultSet=new ApiResultSet();
        Map<String, Object> modelMap=new HashMap<>();
           classifyRec.setModifyDate(new Date());
           classifyRecManager.saveOrUpdate(classifyRec);
           apiResultSet.setData(modelMap);
        return apiResultSet;
    }


    private ClassifyRec saveOrUpdateClassifyRec(ClassifyRec classifyRec) {
        classifyRecManager.saveOrUpdate(classifyRec);
        return classifyRec;
    }


    public ApiResultSet materialClassifyPrePrial(String caseOid, String attaOid, String classifierId) throws Exception {
        ApiResultSet apiResultSet=new ApiResultSet();
        Map<String, Object> modelMap=new HashMap<>();
        //附件信息
        ApiResultSet<QlSysAtta>   sysAttaresult=qlSysAttaFeignService.querySysAttaByOid(attaOid);
        QlSysAtta qlSysAtta=sysAttaresult.getData();
        String fastdfsNginxUrl=qlSysAtta.getFastdfsNginxUrl();
        AttaBase64Result attaBase64Result=getAttaBase64ByUrl(fastdfsNginxUrl);
        String picBase64 = "";
        if(null!=attaBase64Result){
            picBase64 = attaBase64Result.getResult();
        }else {
            log.error("材料分类识别预审", "未获取附件base64信息！办件主键为：{}，附件主键为：{}，分类器id：{}", caseOid, attaOid, classifierId);
            modelMap.put("success", false);
            modelMap.put("message","未获取附件base64信息!");
            throw new Exception("未获取附件base64信息!");
        }

//        // 调用百度自定义模板识别接口
//        OcrClassifierRequest ocrClassifierRequest = aiTokenUtil.getTokenRequest(OcrClassifierRequest.class);
//        ocrClassifierRequest.setImgBase64(picBase64);
//        ocrClassifierRequest.setClassifierId(Integer.parseInt(classifierId));
//        OcrClassifierResponse ocrClassifierResponse = ocrClassifierRestService.classifier(ocrClassifierRequest);
//
//        // 调用失败
//        if (200 != ocrClassifierResponse.getCode()) {
//            ClassifyRec classifyRec  = saveClassifyRecNew(caseOid, attaOid, classifierId, null,null,null,null,null);
//            modelMap.put("classifyRec", classifyRec);
//            modelMap.put("success", true);
//            modelMap.put("message","调用材料分类失败");
//            apiResultSet.setData(modelMap);
//            return apiResultSet;
//        }
//
//        // templateId分类结果对应的模板id
//        String templateId = ocrClassifierResponse.getTemplateId();
//        System.out.println("attaOid:" + attaOid + "   templateId:" + templateId);
//        // templateId为空，未能正确分类到对应模板
//        if (StrUtil.isBlank(templateId)) {
//            ClassifyRec classifyRec  = saveClassifyRecNew(caseOid, attaOid, classifierId, null,null,null,null,null);
//            modelMap.put("classifyRec", classifyRec);
//            modelMap.put("success", true);
//            modelMap.put("message","未正确分配到对应模板");
//            apiResultSet.setData(modelMap);
//            return apiResultSet;
//        }
        String templateId = "";
        //根据办件oid获取办件对应信息
        ApiResultSet<QlCase> qlCaseApiResultSet= qlCaseServiceFeginService.queryQlCaseByCaseOid(caseOid);
        QlCase qlCase=qlCaseApiResultSet.getData();
        if (null==qlCase) {
            log.error("");
            throw new Exception("系统异常，请联系服务人员!");
        }
        //事项oid
        String serviceOid=qlCase.getServiceOid();
        //查询对应办件下的材料信息
        ApiResultSet<List<QlCaseMaterial>> resultSet = qlCaseMaterialServiceFeginService.queryQlCaseMaterialByCaseOid(caseOid);
        List<QlCaseMaterial> qlCaseMaterialList = resultSet.getData();
        QlCaseMaterial qlCaseMaterial = null;
        for (QlCaseMaterial material : qlCaseMaterialList) {
            if (material.getBaiduTemplateIds().contains(templateId)) {
                //找到模板id对应事项材料
                qlCaseMaterial = material;
                break;
            }
        }
        //未找到对应模板id的事项材料
        if (qlCaseMaterial == null) {
            ClassifyRec classifyRec  = saveClassifyRecNew(caseOid, attaOid, classifierId, null,null,null,null,null);
            modelMap.put("classifyRec", classifyRec);
            modelMap.put("success", true);
            modelMap.put("message","未找到对应模板id的事项材料");
            apiResultSet.setData(modelMap);
            return apiResultSet;
        }else {
            ClassifyRec classifyRec  = saveClassifyRecNew(caseOid, attaOid, classifierId, qlCaseMaterial.getCaseMaterialOid(),qlCaseMaterial.getMaterialOid(),qlCaseMaterial.getBaiduTemplateIds(),qlCaseMaterial.getMaterialCatalogOid(),null);
            modelMap.put("classifyRec", classifyRec);
            modelMap.put("success", true);
            modelMap.put("message","未找到对应模板id的事项材料");
            apiResultSet.setData(modelMap);
            return apiResultSet;
        }

    }













    public ApiResultSet getClassifilerAndPicOcrResultLS(String caseOid, String attaOid, String serviceOid, String serviceName,
                                                        String fastdfsNginxUrl,String caseMaterialOid,String refinedMaterialOid) throws Exception {
        ApiResultSet apiResultSet=new ApiResultSet();
        Map<String, Object> modelMap=new HashMap<>();
        //获取附件信息
   /*     ApiResultSet<QlSysAtta>   sysAttaresult=qlSysAttaFeignService.querySysAttaByOid(attaOid);
        QlSysAtta qlSysAtta=sysAttaresult.getData();
        String fastdfsNginxUrl=qlSysAtta.getFastdfsNginxUrl();*/

        AttaBase64Result attaBase64Result=getAttaBase64ByUrl(fastdfsNginxUrl);
        String picBase64 = "";
        if(null!=attaBase64Result){
            picBase64 = attaBase64Result.getResult();
        }else {
            log.error("材料分类识别预审", "未获取附件base64信息！办件主键为：{}，附件主键为：{}", caseOid, attaOid, null);
            modelMap.put("success", false);
            modelMap.put("message","未获取附件base64信息!");
            throw new Exception("未获取附件base64信息!");
        }

        String fileUrl= FaStaticParam.PROJECT_PATH+"\\modelTemples\\model";
        String dirPath = File.separator + "pic" + File.separator + "clshjg" + File.separator;
        String picNameQZ = attaOid + "_" + StringUtil.random(6) ;
        String picName=picNameQZ+"new"+ ".jpg";
        String picNameNew=picNameQZ+ ".jpg";

        String  picPath = fileUrl + dirPath + picName;
        String  picPathNew = fileUrl + dirPath + picNameNew;
        File file = new File(picPath);
        if(!file.exists()){
            File dir = new File(dirPath);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            //复制文件系统中的附件到本地系统
            HttpUtil.downloadFile(fastdfsNginxUrl, cn.hutool.core.io.FileUtil.file(picPath));
        }

        //组装材料参数
        QlCaseMaterial ssqlCaseMaterial = null;
        RefinedMaterial ssRefinedMaterial=null;

        ApiResultSet<QlCaseMaterial> resultSet =qlCaseMaterialServiceFeginService.queryMaterialByCaseMaterialOid(caseMaterialOid);
        ApiResultSet<RefinedMaterial> refinedMaterialtSet = refinedMaterialFeginService.getRefinedMaterialByOid(refinedMaterialOid);

        if(null!=resultSet.getData()){
            ssqlCaseMaterial=resultSet.getData();
        }
        if(null!=refinedMaterialtSet.getData()){
            ssRefinedMaterial=refinedMaterialtSet.getData();
        }
        //未找到对应模板id的事项材料
        if (ssqlCaseMaterial != null && ssRefinedMaterial!=null ) {
            //智能预审
            if(StringUtils.isNotEmpty(ssRefinedMaterial.getMaterialCatalogOid())){
                Map<String, Object> prePrialResult= intelligentPretrialmaterialPrePrial(caseOid,attaOid,
                        ssqlCaseMaterial.getMaterialOid(),ssqlCaseMaterial.getCaseMaterialOid(),
                        ssRefinedMaterial.getOid(),ssRefinedMaterial.getMaterialCatalogOid(),picBase64,picPath);
                modelMap.put("prePrialResult", prePrialResult);
            }
            modelMap.put("success", true);
            apiResultSet.setData(modelMap);
            return apiResultSet;
        }else {
            ClassifyRec classifyRec  = saveClassifyRecNew(caseOid, attaOid, null, null,null,null,null,null);
            modelMap.put("classifyRec", classifyRec);
            modelMap.put("success", false);
            modelMap.put("message","未找到对应材料");
            apiResultSet.setData(modelMap);
            return apiResultSet;

        }
    }



}
