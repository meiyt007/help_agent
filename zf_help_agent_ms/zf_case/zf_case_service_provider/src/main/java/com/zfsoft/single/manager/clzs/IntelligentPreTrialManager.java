package com.zfsoft.single.manager.clzs;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zfsoft.cases.data.QlCase;
import com.zfsoft.cases.data.QlCaseApplay;
import com.zfsoft.cases.data.QlCaseMaterial;
import com.zfsoft.cases.data.QlCaseMaterialAtta;
import com.zfsoft.cases.service.QlCaseApplayService;
import com.zfsoft.cases.service.QlCaseMaterialAttaService;
import com.zfsoft.cases.service.QlCaseMaterialService;
import com.zfsoft.cases.service.QlCaseService;
import com.zfsoft.microservice.platform.service.sys.SysAttaService;
import com.zfsoft.ocr.data.pojo.ocr.*;
import com.zfsoft.ocr.data.pojo.textInOcr.TextInOcrItemResponse;
import com.zfsoft.ocr.data.pojo.textInOcr.TextInOcrRequest;
import com.zfsoft.ocr.data.pojo.textInOcr.TextInOcrResponse;
import com.zfsoft.microservice.platform.data.sys.SysAtta;
import com.zfsoft.ocr.service.ocr.OcrCertificateRestService;
import com.zfsoft.ocr.service.ocr.OcrCustomTemplateRestService;
import com.zfsoft.ocr.service.textInOcr.TextInOcrCertificateRestService;
import com.zfsoft.ocr.service.textInOcr.TextInOcrCustomTemplateRestService;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.data.BaseStaticParameter;
import com.zfsoft.platform.utils.data.SysAttaTemp;
import com.zfsoft.platform.utils.fileUtil.UploadUtil;
import com.zfsoft.platform.utils.validate.ResultInfoException;
import com.zfsoft.rest.pojo.imageManipulation.WatermarkParamRequest;
import com.zfsoft.rest.service.ocr.IOcrCustomTemplateRestService;
import com.zfsoft.rest.service.ocr.IOcrHandWrittenRestService;
import com.zfsoft.rest.service.ocr.IOcrSealRestService;
import com.zfsoft.service.sxService.data.RefinedMaterial;
import com.zfsoft.service.sxService.data.ReviewPoints;
import com.zfsoft.service.sxService.data.SxServiceMaterial;
import com.zfsoft.service.sxService.service.RefinedMaterialService;
import com.zfsoft.service.sxService.service.ReviewPointsService;
import com.zfsoft.service.sxService.service.SxServiceMaterialService;
import com.zfsoft.service.sxSys.data.SxSysAtta;
import com.zfsoft.service.sxSys.service.SxSysAttaService;
import com.zfsoft.single.data.clzs.AuditResult;
import com.zfsoft.single.data.clzs.FaMaterialPicOcrResult;
import com.zfsoft.single.data.clzs.PreDetailTrialResultVo;
import com.zfsoft.single.data.clzs.dto.BusinessLiceInfo;
import com.zfsoft.single.data.clzs.dto.HouseholdRegisterInfo;
import com.zfsoft.single.data.yxpz.AhsSamplePicInfo;
import com.zfsoft.single.data.yxpz.ExaminePointCarding;
import com.zfsoft.single.data.yxpz.FaModelRuleValidation;
import com.zfsoft.single.manager.yxpz.AhsSamplePicInfoManager;
import com.zfsoft.single.manager.yxpz.ExaminePointCardingManager;
import com.zfsoft.single.manager.yxpz.FaModelRuleValidationManager;
import com.zfsoft.single.util.CommonUtil;
import com.zfsoft.single.util.*;
import com.zfsoft.single.util.fa.*;
import com.zfsoft.single.util.fa.atta.AttaBase64Result;
import com.zfsoft.single.util.fa.atta.FileManageUtil;
import com.zfsoft.superwindow.data.clzs.*;
import com.zfsoft.superwindow.data.clzs.dto.IdcardInfo;
import com.zfsoft.superwindow.data.clzs.vo.IdCardInfoVo;
import com.zfsoft.superwindow.dbaccess.data.DbFaMaterialPicOcrResult;
import com.zfsoft.superwindow.feign.settings.SysAttaFeginService;
import com.zfsoft.superwindow.manager.clzs.OcrRecordManager;
import com.zfsoft.superwindow.service.clzs.*;
import com.zfsoft.superwindow.util.fa.AiTokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.opencv.core.Rect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Encoder;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.lang.reflect.Field;
import java.net.URLDecoder;
import java.util.List;
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;


/**
 * @ClassName IntelligentPreTrialManager
 * @Description: 智能预审 实现类
 * @Author liangss
 * @Date 2020-12-10 15:46:29
 **/
@Service
@Slf4j
public class IntelligentPreTrialManager {
    public final static String DEFAULT_CHARACTER_SET = "UTF-8";
    //办件
    @Resource
    private QlCaseService qlCaseServiceService;
    //事项材料
    @Resource
    private SxServiceMaterialService sxServiceMaterialService;
    //材料目录
    @Resource
    private MaterialCatalogService materialCatalogService;
    @Resource
    private AiTokenUtil aiTokenUtil;
//    @Resource
//    private IOcrHandPrintRestService ocrHandPrintRestService;

    @Autowired(required = false)
    private IOcrSealRestService ocrSealRestService;

    @Resource
    private StampRecordManager stampRecordManager;
    @Resource
    private FaModelTemplateService faModelTemplateService;

    @Resource
    private MaterialCatalogElementService materialCatalogElementService;

    @Resource
    private FaMaterialPicOcrResultManager faMaterialPicOcrResultManager;
    @Resource
    private OcrRecordManager ocrRecordManager;

    @Resource
    private IdcardInfoService idcardInfoService;
    @Resource
    private BusinessLiceManager businessLiceManager;
    @Resource
    private HouseholdRegisterInfoManager householdRegisterInfoManager;

    @Autowired(required = false)
    private IOcrCustomTemplateRestService ocrCustomTemplateRestService;
    @Autowired
    private FaModelTemplateBlockService faModelTemplateBlockService;

    @Autowired(required = false)
    private IOcrHandWrittenRestService ocrHandWrittenRestService;
    @Autowired
    private QlCaseMaterialAttaService qlCaseMaterialAttaServiceService;
    @Autowired
    private QlCaseMaterialService qlCaseMaterialServiceService;
    @Resource
    private FaModelRuleValidationManager faModelRuleValidationManager;
    @Resource
    private SysAttaService sysAttaService;
    @Resource
    private ReviewPointsService reviewPointsService;
    @Resource
    private AuditResultManager auditResultManager;
    /**
     * 水印图片处理接口
     */
//    @Resource
//    private ImageManipulationRestService manipulationRestService;
    //印章接口地址
    @Value("${zfsoft.yjyzsScan.yzUrl}")
    private String yzUrl;
    //签字接口地址
    @Value("${zfsoft.yjyzsScan.qzUrl}")
    private String qzUrl;

    //签字和盖章接口
    @Value("${zfsoft.yjyzsScan.qzAndYzUrl}")
    private String qzAndYzUrl;

    @Value("${zfsoft.electronicForm.url}")
    private String requestUrl;

    @Value("${zfsoft.electronicForm.getFormApiDataUrl}")
    private String getFormApiDataUrl;


    @Resource
    private AhsSamplePicInfoManager ahsSamplePicInfoManager;

    @Resource
    private ExaminePointCardingManager examinePointCardingManager;

    @Resource
    private QlCaseApplayService qlCaseApplayServiceService;
    //精细化材料
    @Resource
    private RefinedMaterialService refinedMaterialService;
    @Resource
    private SxSysAttaService sxServiceAttaFeignService;

    @Resource
    private IdcardInfoManager idcardInfoManager;

    @Resource
    private OcrCertificateRestService ocrCertificateRestService;

    @Resource
    private TextInOcrCertificateRestService textInOcrCertificateRestService;
    @Resource
    private TextInOcrCustomTemplateRestService textInOcrCustomTemplateRestService;

    //获取表单数据
    public Map<String, Object> getFormApiData(String reportOid, String designOid, String authorizeKey) {
        //解析办件信息
        Map<String, Object> map = new HashMap<>();
        String uriString = requestUrl + getFormApiDataUrl + "?reportOid=" + reportOid + "&designOid=" + designOid + "&authorizeKey=" + authorizeKey;
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<String> requestEntity = new HttpEntity<>(null, headers);
        ResponseEntity<String> resEntity = restTemplate.exchange(uriString.toString(), HttpMethod.GET, requestEntity, String.class);
        String body = resEntity.getBody();
        if (org.apache.commons.lang3.StringUtils.isNotEmpty(body)) {

            JSONObject json_test = JSONObject.parseObject(body);
            JSONObject json = json_test.getJSONObject("data");
            String formData = json.getString("formData");
            if (org.apache.commons.lang3.StringUtils.isNotEmpty(formData)) {
                JSONObject json_formData = JSONObject.parseObject(formData);
                Map<String, Object> formExmap = (Map<String, Object>) json_formData;
                for (String key : formExmap.keySet()) {//keySet获取map集合key的集合  然后在遍历key即可
                    Object value = formExmap.get(key);
                    if (value instanceof String) {
                        map.put(key, value.toString());
                    } else if (value instanceof Boolean) {
                        map.put(key, value.toString());
                    } else if (value instanceof JSONArray) {
                        if (((JSONArray) value).size() == 1) {
                            map.put(key, ((JSONArray) value).get(0).toString());
                        } else if (((JSONArray) value).size() > 1) {
                            String blqz = "";
                            for (int i = 0; i < ((JSONArray) value).size(); i++) {
                                blqz = blqz + ((JSONArray) value).get(i).toString() + ";";
                            }
                            map.put(key, blqz);
                        }
                    } else if (value instanceof JSONObject) {
                        Map<String, Object> map2 = (Map<String, Object>) value;
                        for (String key2 : map2.keySet()) {
                            Object value2 = map2.get(key2);
                            if (value2 instanceof String) {
                                map.put(key2, value2.toString());
                            } else if (value2 instanceof Boolean) {
                                map.put(key2, value2.toString());
                            } else if (value2 instanceof JSONArray) {//数组取第一条数据
                                if (((JSONArray) value2).size() > 0) {
                                    Object array3 = ((JSONArray) value2).get(0);
                                    if (array3 instanceof JSONObject) {
                                        Map<String, Object> child3 = (Map<String, Object>) array3;
                                        for (String key33 : child3.keySet()) {
                                            Object child3a = child3.get(key33);//
                                            if (child3a instanceof JSONArray || child3a instanceof JSONObject) {
                                            } else {
                                                map.put(key33, child3a.toString());
                                            }
                                        }
                                    }
                                }
                            } else if (value2 instanceof JSONObject) {
                                Map<String, Object> child3 = (Map<String, Object>) value2;
                                for (String key3 : child3.keySet()) {
                                    Object child3a = child3.get(key3);
                                    if (child3a instanceof JSONArray || child3a instanceof JSONObject) {
                                    } else {
                                        map.put(key3, child3a.toString());
                                    }
                                }
                            } else {
                                map.put(key2, value2.toString());
                            }
                        }
                    } else {
                        map.put(key, value.toString());
                    }

                }
            }

        }
        return map;
    }

    /**
     * 多张样本图转pdf文件
     *
     * @param dzUrl
     * @param pdfPath
     * @return
     * @throws Exception
     */
    public Map<String, String> getPdf(List<String> dzUrl, String pdfPath) throws Exception {
        Map<String, String> map = new HashMap<>();
        String materialSampleOid = "";
        String materialSampleAddr = "";

        String url = PdfUtil.toPdf(dzUrl, pdfPath);
        String picBase642 = ImageToBase64(url);

        //上传pdf
        //ApiResultSet<SysAtta> apiResultSet2 =this.uploadBase64Pdf(picBase642);

        ApiResultSet<SxSysAtta> apiResultSet2 = this.uploadBase64PdfToSX(picBase642);

        if (null != apiResultSet2.getData()) {
            SxSysAtta atta = apiResultSet2.getData();
            materialSampleOid = atta.getOid();
            materialSampleAddr = atta.getFilePath();
            // log.info("附件地址:" + atta.getFastdfsNginxUrl());
            //localAuditImageUrl=atta.getFastdfsNginxUrl();
        }
        map.put("materialSampleOid", materialSampleOid);
        map.put("materialSampleAddr", materialSampleAddr);

        //保存后清除本地的附件
        File myFilePath = new File(pdfPath);
        myFilePath.delete();
        return map;

    }


    /***
     * @Description: 上传附件到事项系统
     * @Author:liangss
     * @Date:2021/8/26
     * @Param: [imageBase64]
     */
    public ApiResultSet<SxSysAtta> uploadBase64PdfToSX(String imageBase64) {
        ApiResultSet<SxSysAtta> apiResultSet;
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        try {
            // log.info("获取附件imageBase64成功：****", imageBase64,"***");
            byte[] bytes = Base64.getDecoder().decode(imageBase64);
            InputStream inputStream = new ByteArrayInputStream(bytes);
            String originaFiliname = "image" + System.currentTimeMillis() + ".png";
            //MultipartFile file = new MockMultipartFile("image"+new Date().getTime()+".png", bytes);
            // MultipartFile file = new MockMultipartFile("File","image"+new Date().getTime()+".png","text/plain",inputStream);
            UploadUtil uploadUtil = new UploadUtil(request);
            String filePath = null;
            MultipartFile file = new MockMultipartFile("File", "image" + System.currentTimeMillis() + ".pdf", "text/plain", inputStream);
            filePath = uploadUtil.uploadFile(file);
            SysAttaTemp sysAttaFile = uploadUtil.getSysAttaFile(filePath, "");


            SxSysAtta sxSysAtta = new SxSysAtta();
            BeanUtils.copyProperties(sysAttaFile, sxSysAtta);
            sxSysAtta.setFilePath(sysAttaFile.getFastdfsNginxUrl());
            sxSysAtta.setOid(sysAttaFile.getAttaOid());
            //保存附件信息
            // apiResultSet=sysAttaService.saveSysAtta(atta);

            apiResultSet = sxServiceAttaFeignService.saveSysAtta(sxSysAtta);
            /* .*/

            // log.info("获取附件apiResultSet成功：****", apiResultSet,"***");
        } catch (Exception e) {
            e.printStackTrace();
            throw new ResultInfoException("图片信息异常！");
        }
        return apiResultSet;

    }


    /***
     * @Description: 根据附件地址获取byte
     * @Author:liangss
     * @Date:2021/11/1
     * @Param: [path]
     */
    public static byte[] readfile(String path) {
        String imgFile = path;
        InputStream in = null;
        byte[] data = null;
        try {
            in = new FileInputStream(imgFile);
            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }


    /***
     * @Description: 获取ocr参数用性能测试
     * @Author:liangss
     * @Date:2021/11/15
     * @Param: [caseOid, attaOid, materialOid, caseMaterialOid, refinedMaterialOid, materialCatalogOid, materialAttaOid, materialName]
     */
    public Map<String, Object> intelligentPretrialmaterialPrePrialXn(String caseOid,
                                                                     String attaOid,
                                                                     String materialOid,
                                                                     String caseMaterialOid,
                                                                     String refinedMaterialOid,
                                                                     String materialCatalogOid,
                                                                     String materialAttaOid,
                                                                     String materialName
    )
            throws Exception {
        Map<String, Object> ocrResultMap = new HashMap<String, Object>();
        try {

            log.info("开始审核");


            MaterialCatalog cata = null;
            // 获得目录项
            ApiResultSet<MaterialCatalog> materialCatalogApiResultSet = materialCatalogService.getMaterialCatalogOid(materialCatalogOid);
            if (null != materialCatalogApiResultSet.getData()) {
                cata = materialCatalogApiResultSet.getData();
            } else {
                ocrResultMap.put("success", false);
                ocrResultMap.put("message", "未获取关联目录信息");
            }
            //获取办件信息
            ApiResultSet<SysAtta> sysAttaresult = sysAttaService.getSysAttaByAttaOid(attaOid);
            SysAtta qlSysAtta = sysAttaresult.getData();
            String fastdfsNginxUrl = qlSysAtta.getFastdfsNginxUrl();
            log.info("fastdfsNginxUrl地址：=" + fastdfsNginxUrl);
            AttaBase64Result attaBase64Result = getAttaBase64ByUrl(fastdfsNginxUrl);
            String picBase64 = "";
            if (null != attaBase64Result) {
                picBase64 = attaBase64Result.getResult();
                log.info("获取百度picbase64:" + "办件主键为：{" + caseOid + "}，材料主键为：{" + materialOid + "}，材料名称为：{" + materialName + "}，材料关联主键为：{" + materialAttaOid + "}," + "picBase64为：{" + picBase64 + "}，" +
                        "BaiduTemplateId为：{" + cata.getBaiduTemplateId() + "},");
            } else {
                ocrResultMap.put("success", false);
                ocrResultMap.put("message", "未获取附件base64信息!");
            }
            String dirPath = "";
            if (CommonUtil.isWindows()) {
                dirPath = "D:\\commonservice\\image\\";
            } else {
                dirPath = "/soft/commonservice/image/";
            }
            String picName = attaOid + "new" + ".jpg";
            String picPath = dirPath + picName;
            File dir = new File(dirPath);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            //复制文件系统中的附件到本地系统
            HttpUtil.downloadFile(fastdfsNginxUrl, cn.hutool.core.io.FileUtil.file(picPath));
            if (null != cata) {
                //百度自定义ocr取值list
                List<OcrCustomTemplateItemResponse> ocrCustomTemplateItemResponseList = new ArrayList<>();
                OcrCustomTemplateResponse ocrCustomTemplateResponse = new OcrCustomTemplateResponse();
                ocrCustomTemplateResponse.setCode(200);
                OcrCustomTemplateItemResponse ocr1 = new OcrCustomTemplateItemResponse();
                ocr1.setName("盖章");
                ocr1.setWord("emptyempty");
                ocr1.setWidth("760");
                ocr1.setHeight("673");
                ocr1.setTop("617");
                ocr1.setLeft("630");

                OcrCustomTemplateItemResponse ocr2 = new OcrCustomTemplateItemResponse();
                ocr2.setName("签字");
                ocr2.setWord("emptyempty");
                ocr2.setWidth("473");
                ocr2.setHeight("402");
                ocr2.setTop("779");
                ocr2.setLeft("912");

                ocrCustomTemplateItemResponseList.add(ocr1);
                ocrCustomTemplateItemResponseList.add(ocr2);


                /** 百度自定义模板调用开始     **/
                List<Map<String, Object>> blockJsonArray = new ArrayList<Map<String, Object>>();


                // 获取百度识别区块识别结果集合
                MaterialCatalogElement materialCatalogElement = new MaterialCatalogElement();
                materialCatalogElement.setMaterialCatalogOid(cata.getMaterialCatalogOid());
                //List<MaterialCatalogElement> faMaterialCatalogMetadataList=this.materialCatalogElementManager.queryList(materialCatalogElement);
                ApiResultSet<List<MaterialCatalogElement>> listRest = materialCatalogElementService.queryList(materialCatalogElement);
                List<MaterialCatalogElement> faMaterialCatalogMetadataList = null;
                if (listRest != null && listRest.getData() != null) {
                    faMaterialCatalogMetadataList = listRest.getData();
                }
                //检验印章
           /* Map<String, Object> sealList=new HashMap<>();
            if(null!=cata.getSealsNumber() && cata.getSealsNumber()>0){
                sealList=this.checkSealMapByBase64(picBase64);
            }
            //检验签字
            Map<String, Object> signList=new HashMap<>();
            if(StringUtils.isNotEmpty(cata.getIsMultiPage()) && cata.getIsMultiPage().equals("true")){
                signList=this.checkSignMapByBase64(picBase64);
            }*/
                //获取证照取值
                for (MaterialCatalogElement faMaterialCatalogMetadata : faMaterialCatalogMetadataList) {
                    Map<String, Object> blockJson = new HashMap<String, Object>();
                    blockJson.put("code", faMaterialCatalogMetadata.getElementCode());
                    blockJson.put("name", faMaterialCatalogMetadata.getElementName());
                    String words = "";
                    String elementName = faMaterialCatalogMetadata.getElementName();
                    //关联卡证目录元素取值名称
                    String cardCatalogueElementName = faMaterialCatalogMetadata.getCardCatalogueElementName();
                    //目录元素
                    String bdName = elementName;
                    //卡证取值元素(如果关联了卡证远程就取卡证目录元素)
                    if (StringUtils.isNotEmpty(cardCatalogueElementName)) {
                        bdName = cardCatalogueElementName;
                    }
                    if (StringUtils.isNotEmpty(cata.getBaiduTemplateId()) && null != ocrCustomTemplateItemResponseList && ocrCustomTemplateItemResponseList.size() > 0) {
                        for (OcrCustomTemplateItemResponse itemResponse : ocrCustomTemplateItemResponseList) {
                            String name = itemResponse.getName();
                            // 获得区块列表
                            if (faMaterialCatalogMetadata.getElementName().equals(name)) {
                                words = itemResponse.getWord();
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
                    }

                    blockJsonArray.add(blockJson);
                }

         /*   String  sealListJson="";
            String signListJson="";
            if(sealList.size()>0){
                sealListJson=new JSONObject(sealList).toString();
            }
            if(signList.size()>0){
                signListJson=new JSONObject(signList).toString();
            }*/
                String sealListJson = "";
                String signListJson = "";
                //检验签字和盖章
                Map<String, String> sealAndSignList = new HashMap<>();
                if ((null != cata.getSealsNumber() && cata.getSealsNumber() > 0) || (StringUtils.isNotEmpty(cata.getIsMultiPage()) && cata.getIsMultiPage().equals("true"))) {
                    sealAndSignList = this.checkSignAndSealByBase64(picBase64);
                    sealListJson = sealAndSignList.get("sealListJson");
                    signListJson = sealAndSignList.get("signListJson");
                }
                if (blockJsonArray.size() > 0) {
                    ocrResultMap.put("code", FaStaticParam.INTER_STATUS_CODE_SUCCESS);
                    ocrResultMap.put("result", blockJsonArray);
                    ocrResultMap.put("success", true);
                    String resultJson = new JSONObject(ocrResultMap).toString();
                    saveResult(caseOid, materialOid, refinedMaterialOid,
                            cata.getCatalogCode(), attaOid, CommonUtil.md5(picBase64),
                            resultJson, null, sealListJson, signListJson, caseMaterialOid, picPath, materialAttaOid);

                    log.info("百度ocr取值结果" + "取值成功！！办件主键为：{" + caseOid + "}，材料主键为：{" + materialOid + "}，材料名称为：{" + materialName + "}，材料关联主键为：{" + materialAttaOid + "}," + "识别结果json为：{" + resultJson + "}");
                    ocrResultMap.put("message", "预审成功");
                } else {
                    ocrResultMap.put("success", false);
                    ocrResultMap.put("message", "预审失败");
                }


                return ocrResultMap;

            }

        } catch (IOException e) {
            e.printStackTrace();
            log.info(e.getMessage());
            log.info(e.getMessage());
        }

        return ocrResultMap;
    }


    /***
     * @Description: 获取ocr参数通过调用BaiduOcr
     * @Author:liangss
     * @Date:2021/11/1
     * @Param: [caseOid, attaOid, materialOid, caseMaterialOid, refinedMaterialOid, materialCatalogOid, materialAttaOid, materialName]
     */
    public Map<String, Object> intelligentPretrialmaterialPrePrialBaiduOcr(String caseOid,
                                                                           String attaOid,
                                                                           String materialOid,
                                                                           String caseMaterialOid,
                                                                           String refinedMaterialOid,
                                                                           String materialCatalogOid,
                                                                           String materialAttaOid,
                                                                           String materialName
    )
            throws Exception {

        Map<String, Object> ocrResultMap = new HashMap<String, Object>();
        MaterialCatalog cata = null;
        // 获得目录项
        ApiResultSet<MaterialCatalog> materialCatalogApiResultSet = materialCatalogService.getMaterialCatalogOid(materialCatalogOid);
        if (null != materialCatalogApiResultSet.getData()) {
            cata = materialCatalogApiResultSet.getData();
        } else {
            ocrResultMap.put("success", false);
            ocrResultMap.put("message", "未获取关联目录信息");
        }
        //获取办件信息
        ApiResultSet<SysAtta> sysAttaresult = sysAttaService.getSysAttaByAttaOid(attaOid);
        SysAtta qlSysAtta = sysAttaresult.getData();
        String fastdfsNginxUrl = qlSysAtta.getFastdfsNginxUrl();

        AttaBase64Result attaBase64Result = getAttaBase64ByUrl(fastdfsNginxUrl);
        String picBase64 = "";
        if (null != attaBase64Result) {
            picBase64 = attaBase64Result.getResult();
            log.info("获取百度picbase64:" + "办件主键为：{" + caseOid + "}，材料主键为：{" + materialOid + "}，材料名称为：{" + materialName + "}，材料关联主键为：{" + materialAttaOid + "}," + "picBase64为：{" + picBase64 + "}，" +
                    "BaiduTemplateId为：{" + cata.getBaiduTemplateId() + "},");
        } else {
            ocrResultMap.put("success", false);
            ocrResultMap.put("message", "未获取附件base64信息!");
        }
        String dirPath = "";
        if (CommonUtil.isWindows()) {
            dirPath = "D:\\commonservice\\image\\";
        } else {
            dirPath = "/soft/commonservice/image/";
        }
        String picName = attaOid + "new" + ".jpg";
        String picPath = dirPath + picName;
        File dir = new File(dirPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        //复制文件系统中的附件到本地系统
        HttpUtil.downloadFile(fastdfsNginxUrl, cn.hutool.core.io.FileUtil.file(picPath));
        if (null != cata) {
            //获取证照数据
            Map<String, Map<String, String>> mapList = new HashMap<String, Map<String, String>>();
            //卡证类型
            String materialIdentificationType = cata.getMaterialIdentificationType();
            //ocr卡证调用类型 0百度 1textIn
            String cardCatalogueType = cata.getCardCatalogueType();
            //百度模板id
            String baiduTemplateId = cata.getBaiduTemplateId();
            //textin机器人模板id
            String textinTemplateId = cata.getTextinTemplateId();
            //textinOcr接口取值
            TextInOcrResponse textInOcrResponse = null;
            //textinOcr接口取值(机器人模板)
            TextInOcrResponse textInOcrResponseJQR = new TextInOcrResponse();
            //textinOcr接口取值list
            List<TextInOcrItemResponse> textInOcrItemResponseList = null;
            List<TextInOcrItemResponse> textInOcrItemResponseJQRList = null;
            //百度接口ocr取值
            com.zfsoft.ocr.data.pojo.ocr.OcrCustomTemplateResponse ocrCertificateResponse = null;
            //百度接口ocr取值list
            List<OcrCustomTemplateItemResponse> ocrCertificateList = null;
            //百度自定义ocr取值list
            List<OcrCustomTemplateItemResponse> ocrCustomTemplateItemResponseList = null;
            List<Map<String, Object>> blockJsonArray = new ArrayList<Map<String, Object>>();

            if (StringUtils.isNotEmpty(cardCatalogueType)) {
                /** textIn卡证ocr调用开始     **/
                TextInOcrRequest textInOcrRequest = new TextInOcrRequest();
                textInOcrRequest.setType(materialIdentificationType);
                textInOcrRequest.setImgUrl(fastdfsNginxUrl);
                if (FaStaticParam.TEXTIN_OCR.equals(cardCatalogueType)) {
                    textInOcrResponse = textInOcrCertificateRestService.textInOcrCertificateRestOcr(textInOcrRequest);
                }
                if (null != textInOcrResponse && 200 == textInOcrResponse.getCode()) {
                    textInOcrItemResponseList = textInOcrResponse.getTextInOcrItemResponseList();
                    log.info("textIn卡证ocr取值结果" + "取值成功！！办件主键为：{" + caseOid + "}，材料主键为：{" + materialOid + "}，材料名称为：{" + materialName + "}，材料关联主键为：{" + materialAttaOid + "},"
                            + "识别结果json为：{" + textInOcrResponse.toString() + "}");
                } else {
                    log.info("textIn卡证ocr取值结果" + "取值失败失败！！办件主键为：{" + caseOid + "}，材料主键为：{" + materialOid + "}，材料名称为：{" + materialName + "}，材料关联主键为：{" + materialAttaOid + "}");
                }

                /** textIn卡证ocr调用结束     **/

            }

            /** textIn机器人ocr调用开始     **/
            if (StringUtils.isNotEmpty(textinTemplateId)) {
                TextInOcrRequest textInOcrRequest = new TextInOcrRequest();
                textInOcrRequest.setImgUrl(fastdfsNginxUrl);
                textInOcrRequest.setId(textinTemplateId);
                textInOcrResponseJQR = textInOcrCustomTemplateRestService.customTextInOcr(textInOcrRequest);
                if (null != textInOcrResponseJQR && 200 == textInOcrResponseJQR.getCode()) {
                    textInOcrItemResponseJQRList = textInOcrResponseJQR.getTextInOcrItemResponseList();
                    log.info("textIn机器人ocr取值结果" + "取值成功！！办件主键为：{" + caseOid + "}，材料主键为：{" + materialOid + "}，材料名称为：{" + materialName + "}，材料关联主键为：{" + materialAttaOid + "},"
                            + "识别结果json为：{" + textInOcrResponseJQR.toString() + "}");
                } else {
                    log.info("textIn机器人ocr取值结果" + "取值失败失败！！办件主键为：{" + caseOid + "}，材料主键为：{" + materialOid + "}，材料名称为：{" + materialName + "}，材料关联主键为：{" + materialAttaOid + "}");
                }

            }
            /** textIn机器人ocr调用开始     **/


            /** 百度卡证ocr调用开始     **/
            if (StringUtils.isNotEmpty(materialIdentificationType)) {
                String typename = "";
                if (FaStaticParam.businessLicense.equals(materialIdentificationType)) {
                    // 营业执照
                    OcrBusinessLicenseRequest ocrBusinessLicenseRequest = aiTokenUtil.getTokenRequest(OcrBusinessLicenseRequest.class);
                    ocrBusinessLicenseRequest.setImgBase64(picBase64);
                    ocrCertificateResponse = ocrCertificateRestService.businessLicense(ocrBusinessLicenseRequest);
                    typename = "营业执照";
                } else if (FaStaticParam.idcardFront.equals(materialIdentificationType)) {
                    // 身份证头像面接口
                    OcrIdcardRequest ocrIdcardRequest = aiTokenUtil.getTokenRequest(OcrIdcardRequest.class);
                    ocrIdcardRequest.setImgBase64(picBase64);
                    ocrCertificateResponse = ocrCertificateRestService.idcardFront(ocrIdcardRequest);
                    typename = "身份证头像面";
                } else if (FaStaticParam.idcardBack.equals(materialIdentificationType)) {
                    // 身份证反面接口
                    OcrIdcardRequest ocrIdcardRequest = aiTokenUtil.getTokenRequest(OcrIdcardRequest.class);
                    ocrIdcardRequest.setImgBase64(picBase64);
                    ocrCertificateResponse = ocrCertificateRestService.idcardBack(ocrIdcardRequest);
                    typename = "身份证反面";
                } else if (FaStaticParam.tempIdcard.equals(materialIdentificationType)) {
                    // 临时身份证、身份证复印件接口
                    OcrIdcardRequest ocrIdcardRequest = aiTokenUtil.getTokenRequest(OcrIdcardRequest.class);
                    ocrIdcardRequest.setImgBase64(picBase64);
                    ocrCertificateResponse = ocrCertificateRestService.tempIdcard(ocrIdcardRequest);
                    typename = "营业执照";
                }
                if (null != ocrCertificateResponse && 200 == ocrCertificateResponse.getCode()) {
                    ocrCertificateList = ocrCertificateResponse.getOcrCustomTemplateItemResponseList();
                    log.info("百度ocr" + typename + "接口取值结果" + "取值成功！！办件主键为：{" + caseOid + "}，材料主键为：{" + materialOid + "}，材料名称为：{" + materialName + "}，材料关联主键为：{" + materialAttaOid + "},"
                            + "识别结果json为：{" + ocrCertificateResponse.toString() + "}");
                } else {
                    log.info("百度ocr" + typename + "接口取值结果" + "取值失败失败！！办件主键为：{" + caseOid + "}，材料主键为：{" + materialOid + "}，材料名称为：{" + materialName + "}，材料关联主键为：{" + materialAttaOid + "}");
                }
            }
            /** 百度卡证ocr调用结束    **/

            // 获取百度识别区块识别结果集合
            MaterialCatalogElement materialCatalogElement = new MaterialCatalogElement();
            materialCatalogElement.setMaterialCatalogOid(cata.getMaterialCatalogOid());
            //List<MaterialCatalogElement> faMaterialCatalogMetadataList=this.materialCatalogElementManager.queryList(materialCatalogElement);
            ApiResultSet<List<MaterialCatalogElement>> listRest = materialCatalogElementService.queryList(materialCatalogElement);
            List<MaterialCatalogElement> faMaterialCatalogMetadataList = null;
            if (listRest != null && listRest.getData() != null) {
                faMaterialCatalogMetadataList = listRest.getData();
            }

            /*
            if(sealList.size()>0){
                sealListJson=new JSONObject(sealList).toString();
            }
            if(signList.size()>0){
                signListJson=new JSONObject(signList).toString();
            }*/
            String sealListJson = "";
            String signListJson = "";
            //检验签字和盖章
            Map<String, String> sealAndSignList = new HashMap<>();
            if ((null != cata.getSealsNumber() && cata.getSealsNumber() > 0) || (StringUtils.isNotEmpty(cata.getIsMultiPage()) && cata.getIsMultiPage().equals("true"))) {
                sealAndSignList = this.checkSignAndSealByBase64(picBase64);
                sealListJson = sealAndSignList.get("sealListJson");
                signListJson = sealAndSignList.get("signListJson");
            }
           /*
            //检验印章
            if(null!=cata.getSealsNumber() && cata.getSealsNumber()>0){
                sealList=this.checkSealMapByBase64(picBase64);
            }
            //检验签字
            Map<String, Object> signList=new HashMap<>();
            if(StringUtils.isNotEmpty(cata.getIsMultiPage()) && cata.getIsMultiPage().equals("true")){
                signList=this.checkSignMapByBase64(picBase64);
            }*/
            //获取证照取值
            for (MaterialCatalogElement faMaterialCatalogMetadata : faMaterialCatalogMetadataList) {
                Map<String, Object> blockJson = new HashMap<String, Object>();
                blockJson.put("code", faMaterialCatalogMetadata.getElementCode());
                blockJson.put("name", faMaterialCatalogMetadata.getElementName());
                String words = "";
                String elementName = faMaterialCatalogMetadata.getElementName();
                //关联卡证目录元素取值名称
                String cardCatalogueElementName = faMaterialCatalogMetadata.getCardCatalogueElementName();

                //关联卡证目录元素取值code
                String cardCatalogueElementCode = faMaterialCatalogMetadata.getCardCatalogueElementCode();
                //目录元素
                String bdName = elementName;
                //卡证取值元素(如果关联了卡证远程就取卡证目录元素)
                if (StringUtils.isNotEmpty(cardCatalogueElementName)) {
                    bdName = cardCatalogueElementName;
                }

                //textIn卡证取值比对
                if (null != textInOcrItemResponseList && textInOcrItemResponseList.size() > 0) {
                    for (TextInOcrItemResponse textInOcrItemResponse : textInOcrItemResponseList) {
                        String name = textInOcrItemResponse.getName();
                        String code = textInOcrItemResponse.getKey();
                        // 获得区块列表
                        if (code.equals(cardCatalogueElementCode)) {
                            /*if (bdName.equals(name)) {*/
                            words = textInOcrItemResponse.getWord();
                            if (null != textInOcrItemResponse.getHeight()) {
                                blockJson.put("height", textInOcrItemResponse.getHeight());
                                blockJson.put("width", textInOcrItemResponse.getWidth());
                                blockJson.put("left", textInOcrItemResponse.getLeft());
                                blockJson.put("top", textInOcrItemResponse.getTop());
                            }
                            break;
                        }
                    }
                    blockJson.put("words", words);
                }

                //百度ocr卡证取值比对取值
                if (StringUtils.isNotEmpty(cata.getMaterialIdentificationTypeOid()) && null != ocrCertificateList && ocrCertificateList.size() > 0) {
                    for (OcrCustomTemplateItemResponse itemResponse : ocrCertificateList) {
                        String name = itemResponse.getName();
                        // 获得区块列表
                        if (bdName.equals(name)) {
                            words = itemResponse.getWord();
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
                }

                //textIn机器人
                if (null != textInOcrItemResponseJQRList && textInOcrItemResponseJQRList.size() > 0) {
                    for (TextInOcrItemResponse textInOcrItemResponse : textInOcrItemResponseJQRList) {
                        String name = textInOcrItemResponse.getName();
                        // 获得区块列表
                        if (elementName.equals(name)) {
                            words = textInOcrItemResponse.getWord();
                            if (null != textInOcrItemResponse.getHeight()) {
                                blockJson.put("height", textInOcrItemResponse.getHeight());
                                blockJson.put("width", textInOcrItemResponse.getWidth());
                                blockJson.put("left", textInOcrItemResponse.getLeft());
                                blockJson.put("top", textInOcrItemResponse.getTop());
                            }
                            break;
                        }
                    }
                    blockJson.put("words", words);
                }

                //自定义模板ocr取值比对
                if (StrUtil.isEmpty(words)) {//没有从证照获取到值就走模板取值
                    if (null != ocrCustomTemplateItemResponseList && ocrCustomTemplateItemResponseList.size() > 0) {
                        for (OcrCustomTemplateItemResponse itemResponse : ocrCustomTemplateItemResponseList) {
                            String name = itemResponse.getName();
                            // 获得区块列表
                            if (faMaterialCatalogMetadata.getElementName().equals(name)) {
                                words = itemResponse.getWord();
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
                    }
                }
                blockJsonArray.add(blockJson);
            }

         /*   String  sealListJson="";
            String signListJson="";
            if(sealList.size()>0){
                sealListJson=new JSONObject(sealList).toString();
            }
            if(signList.size()>0){
                signListJson=new JSONObject(signList).toString();
            }*/

            if (blockJsonArray.size() > 0) {
                ocrResultMap.put("code", FaStaticParam.INTER_STATUS_CODE_SUCCESS);
                ocrResultMap.put("result", blockJsonArray);
                ocrResultMap.put("success", true);
                String resultJson = new JSONObject(ocrResultMap).toString();
                saveResult(caseOid, materialOid, refinedMaterialOid,
                        cata.getCatalogCode(), attaOid, CommonUtil.md5(picBase64),
                        resultJson, null, sealListJson, signListJson, caseMaterialOid, picPath, materialAttaOid);

                log.info("百度ocr取值结果" + "取值成功！！办件主键为：{" + caseOid + "}，材料主键为：{" + materialOid + "}，材料名称为：{" + materialName + "}，材料关联主键为：{" + materialAttaOid + "}," + "识别结果json为：{" + resultJson + "}");
                ocrResultMap.put("message", "预审成功");
            } else {
                ocrResultMap.put("success", false);
                ocrResultMap.put("message", "预审失败");
            }


            return ocrResultMap;

        }

        return ocrResultMap;
    }


    /**
     * 保存预审ocr数据
     *
     * @param caseOid            办件oid
     * @param materialOid        材料
     * @param refinedMaterialOid 细化材料oid
     * @param cataCode           目录code
     * @param attaOid            附件oid
     * @param base64MD5          附件base64MD5加密字符串
     * @param resultJson         识别结果json
     * @param obj
     * @param sealListJson       印章识别结果
     * @param signListJson       签字识别结果
     * @throws Exception
     */
    private void saveResult(String caseOid, String materialOid, String refinedMaterialOid, String cataCode, String attaOid, String base64MD5,
                            String resultJson, Object obj, String sealListJson, String signListJson, String caseMaterialOid, String picPath, String materialAttaOid) throws Exception {

        FaMaterialPicOcrResult ocrResult = new FaMaterialPicOcrResult();
        ocrResult.setUuid(caseOid);
        ocrResult.setMaterialOid(materialOid);
        ocrResult.setMaterialAttaOid(materialAttaOid);
        ocrResult.setCaseMaterialOid(caseMaterialOid);
        ocrResult.setRefinedMaterialOid(refinedMaterialOid);
        ocrResult.setAttaOid(attaOid);
        ocrResult.setSealResultJson(sealListJson);
        ocrResult.setSignResultJson(signListJson);
        ocrResult.setLocalImageUrl(picPath);
        if (StringUtils.isNotEmpty(picPath)) {
            /* log.info("保存或修改材料目录图片识别结果信息获取图片宽和高");*/
            BufferedImage image = ImageIO.read(new File(picPath));
            //log.info("获取地址成功");
            int imageHeight = image.getHeight();
            int imageWedth = image.getWidth();
            //log.info("imageHeight"+imageHeight);
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
        log.info("保存或修改材料目录图片识别结果信息:" + "保存成功！办件主键为：{" + caseOid + "}，材料主键为：{" + materialOid + "}，目标编号为：{" + cataCode + "}，" +
                "识别结果json为：{" + resultJson + "}");

    }


    /***
     * @Description: 获取并保存材料审核状态并保存根据材料oid(用于纠正材料后保存)
     * @Author:liangss
     * @Date:2021/7/31
     * @Param: [request, caseOid, serviceOid, caseMaterialOid, MaterialOid, qlCaseMaterial]
     */
    public QlCaseMaterial getQlCaseMaterialListAndAuditResultByCaseMaterialOid(HttpServletRequest request,
                                                                               String caseOid,
                                                                               String serviceOid,
                                                                               String caseMaterialOid,
                                                                               String MaterialOid,
                                                                               QlCaseMaterial qlCaseMaterial
    ) throws Exception {
        //组装审核结果并入库
        Map<String, Object> map = getAndSaveOrUpdataAuditResultByCaseMaterialOid(request, caseOid, serviceOid, caseMaterialOid, MaterialOid, qlCaseMaterial);
        List<AuditResult> resultList = new ArrayList<>();
        if (map.get("result") != null) {
            resultList = (List<AuditResult>) map.get("result");
        }

        List<PreTrialResultVo> list = new ArrayList<>();
        String resultStatus = "";
        String resultDetail = "";
        String xyrgsh = "";
        Map<String, Object> preTrialResul = new HashMap<>();


        String collectionType = qlCaseMaterial.getCollectionType();//1纸质收取
        //默认通过
        resultStatus = FaStaticParam.EXAMINE_RESULT_STATUS_PASS;
        String materialOid = qlCaseMaterial.getMaterialOid();
        String materialName = qlCaseMaterial.getMaterialName();
        //精细化材料列表
        ApiResultSet<List<RefinedMaterial>> listApiResultSet = refinedMaterialService.getRefinedMaterialListByMaterialOid(materialOid);
        List<RefinedMaterial> refinedMaterialList = new ArrayList<>();
        if (null != listApiResultSet.getData()) {
            refinedMaterialList = listApiResultSet.getData();
        }
        //无细化材料无需审核
        if (refinedMaterialList.size() <= 0) {
            resultStatus = FaStaticParam.EXAMINEL_RESULT_STATUS_NOT_AUDIT;
            qlCaseMaterial.setResultStatus(resultStatus);
            qlCaseMaterialServiceService.updateQlCaseMaterial(qlCaseMaterial);
            return qlCaseMaterial;
        }


        //无审核要点无需审核
        List<ReviewPoints> reviewPointsList = new ArrayList<>();
        String materialCatalogOidAll = "";
        for (RefinedMaterial refinedMaterial : refinedMaterialList) {
            String materialCatalogOid = refinedMaterial.getMaterialCatalogOid();
            if (StringUtils.isNotEmpty(materialCatalogOid)) {
                materialCatalogOidAll = materialCatalogOidAll + materialCatalogOid + ";";
            }
            ApiResultSet<List<ReviewPoints>> reviewPointsResultSet = reviewPointsService.getReviewPointsListByRefinedMaterialOid(refinedMaterial.getOid());
            if (null != reviewPointsResultSet.getData()) {
                List<ReviewPoints> reviewPointsListnew = reviewPointsResultSet.getData();
                if (reviewPointsListnew.size() > 0) {
                    reviewPointsList.addAll(reviewPointsResultSet.getData());
                }
            }
        }
        if (reviewPointsList.size() <= 0) {
            resultStatus = FaStaticParam.EXAMINEL_RESULT_STATUS_NOT_AUDIT;
            qlCaseMaterial.setResultStatus(resultStatus);
            qlCaseMaterialServiceService.updateQlCaseMaterial(qlCaseMaterial);
            return qlCaseMaterial;
        }

        //没有配置智审（需人工核验）
        if (StringUtils.isEmpty(materialCatalogOidAll)) {
            resultStatus = FaStaticParam.EXAMINEL_RESULT_STATUS_ARTIFICIAL_AUDIT;
            qlCaseMaterial.setResultStatus(resultStatus);
            qlCaseMaterialServiceService.updateQlCaseMaterial(qlCaseMaterial);
            return qlCaseMaterial;
        }

        //无验证规则 （需人工核验）
        FaModelRuleValidation faModelRuleValidation = new FaModelRuleValidation();
        faModelRuleValidation.setServiceOid(serviceOid);
        faModelRuleValidation.setSxMaterialOid(MaterialOid);
        List<FaModelRuleValidation> ruleValidList = this.faModelRuleValidationManager.queryFaModelRuleValidationList(faModelRuleValidation);
        if (ruleValidList == null || ruleValidList.size() < 1) {
            resultStatus = FaStaticParam.EXAMINEL_RESULT_STATUS_ARTIFICIAL_AUDIT;
            qlCaseMaterial.setResultStatus(resultStatus);
            qlCaseMaterialServiceService.updateQlCaseMaterial(qlCaseMaterial);
            return qlCaseMaterial;
        }

        //纸质提交和容缺候补的需人工核验
        if (!collectionType.equals("3")) {
            resultStatus = FaStaticParam.EXAMINEL_RESULT_STATUS_ARTIFICIAL_AUDIT;
            qlCaseMaterial.setResultStatus(resultStatus);
            qlCaseMaterialServiceService.updateQlCaseMaterial(qlCaseMaterial);
            return qlCaseMaterial;
        }

        refinedMaterialFor:
        for (RefinedMaterial refinedMaterial : refinedMaterialList) {
            String refinedMaterialOid = refinedMaterial.getOid();
            String materialCatalogOid = refinedMaterial.getMaterialCatalogOid();

            if (StringUtils.isEmpty(materialCatalogOid)) {
                //没有配置智审（需人工核验）
                //resultStatus = FaStaticParam.EXAMINEL_RESULT_STATUS_ARTIFICIAL_AUDIT;
                continue refinedMaterialFor;
            }
            //根据ocr结果判断
            if (null != resultList && resultList.size() > 0) {
                for (int i = 0; i < resultList.size(); i++) {
                    AuditResult auditResults = resultList.get(i);
                    if (materialCatalogOid.equals(auditResults.getCatalogOid()) && refinedMaterialOid.equals(auditResults.getRefinedMaterialOid())) {
                        //判断有没有需要人工审核的
                        if (auditResults.getAuditPointStatus().equals("3")) {
                            xyrgsh = "Y";
                        }

                        // 验证结果不通过，设置审核标识为1：不通过
                        if (auditResults.getValid() != null && auditResults.getValid() == FaStaticParam.FLASE_STATUS) {
                            resultStatus = FaStaticParam.EXAMINE_RESULT_STATUS_NOT_PASS;
                            qlCaseMaterial.setResultStatus(resultStatus);
                            qlCaseMaterial.setConfirmStatus("N");
                            qlCaseMaterialServiceService.updateQlCaseMaterial(qlCaseMaterial);
                            return qlCaseMaterial;
                        }
                    }
                }
            } else {
                resultStatus = FaStaticParam.EXAMINE_RESULT_STATUS_NOT_PASS;
                qlCaseMaterial.setConfirmStatus("N");
                qlCaseMaterial.setResultStatus(resultStatus);
                qlCaseMaterialServiceService.updateQlCaseMaterial(qlCaseMaterial);
                return qlCaseMaterial;
            }
        }


        //预审通过并且存在需要人工审核的要点状态改完预审通过（部分需人工审核）
        if (resultStatus.equals(FaStaticParam.EXAMINE_RESULT_STATUS_PASS) && xyrgsh.equals("Y")) {
            resultStatus = FaStaticParam.EXAMINE_RESULT_STATUS_PASS_AND_MUST_ARTIFICIAL;
        }
        qlCaseMaterial.setConfirmStatus("N");
        qlCaseMaterial.setResultStatus(resultStatus);
        qlCaseMaterialServiceService.updateQlCaseMaterial(qlCaseMaterial);
        return qlCaseMaterial;
    }


    /***
     * @Description: 获取并保存审核结果根据办件oid (用于纠正材料后保存)
     * @Author:liangss
     * @Date:2021/7/31
     * @Param: [request, caseOid, serviceOid, caseMaterialOid, materialOidOld]
     */
    public Map<String, Object> getAndSaveOrUpdataAuditResultByCaseMaterialOid(HttpServletRequest request, String caseOid,
                                                                              String serviceOid, String caseMaterialOid, String materialOidOld, QlCaseMaterial qlCaseMaterial) throws Exception {
        //扫描上传的材料集合
        List<String> smMaterial = new ArrayList<>();
        Map<String, Object> jsonObject = new HashMap<String, Object>();
        if (StrUtil.isBlank(caseOid)) {
            jsonObject.put("code", FaStaticParam.INTER_STATUS_CODE_PARAM_EXCEPTION);
            jsonObject.put("message", "办件唯一标识[caseOid]不能为空！");
            return jsonObject;
        }
        //删除之前存储的审核结果
        auditResultManager.updateByCaseOidAndMateriaOid(caseOid, materialOidOld);

        //验证规则
        Map<String, org.json.JSONObject> blockMapResult = new HashMap<String, org.json.JSONObject>();
        List<FaModelRuleValidation> ruleValidList = new ArrayList<>();

        //普通规则
        List<FaModelRuleValidation> ruleValidListPT = this.faModelRuleValidationManager.queryFaModelRuleValidationListByServiceOidAndRuleType(serviceOid, "", materialOidOld);
        ruleValidList.addAll(ruleValidListPT);
        //办件查询
        ApiResultSet<QlCase> resultSet = qlCaseServiceService.queryQlCaseByCaseOid(caseOid);
        QlCase qlCase = null;
        if (resultSet.getData() != null) {
            qlCase = resultSet.getData();
        }
        //电子表单
        List<FaModelRuleValidation> ruleValidListDZBD = this.faModelRuleValidationManager.queryFaModelRuleValidationListByServiceOidAndRuleType(serviceOid, "DZBD", "");
        if (null != ruleValidListDZBD && ruleValidListDZBD.size() > 0) {
            ruleValidList.addAll(ruleValidListDZBD);
            String formOids = qlCase.getFormOids();
            Map<String, Object> map = new HashMap<>();
            if (formOids != null) {
                JSONArray array = JSON.parseArray(formOids);
                for (Object obj : array) {
                    JSONObject object = (JSONObject) JSONObject.toJSON(obj);
                    String designOid = (String) object.get("designOid");
                    String authorizeKey = (String) object.get("authorizeKey");
                    String formCode = (String) object.get("formCode");
                    String formName = (String) object.get("formName");
                    Map<String, Object> mapchild = this.getFormApiData(caseOid, designOid, authorizeKey);
                    map.putAll(mapchild);
                       /* ApiResultSet<SxFormInfo> sxFormInfoApiResultSet= sxFormInfoService.getSxFormInfoByDesignOid(designOid);
                        if(null!=sxFormInfoApiResultSet.getData()){
                            SxFormInfo sxFormInfo= sxFormInfoApiResultSet.getData();
                        }*/
                }
            }
            for (FaModelRuleValidation faModelRuleValidation : ruleValidListDZBD) {
                String thanTemplateMetadataCode = faModelRuleValidation.getThanTemplateMetadataCode();
                String value = (String) map.get(thanTemplateMetadataCode);
                org.json.JSONObject blockJsonObject = new org.json.JSONObject();
                blockJsonObject.put("words", value);
                blockMapResult.put(thanTemplateMetadataCode + "_" + faModelRuleValidation.getThanSxMaterialOid() + "_" + faModelRuleValidation.getThanRefinedMaterialOid(), blockJsonObject);

            }
        }


        //基础表单
        List<FaModelRuleValidation> ruleValidListJCBD = this.faModelRuleValidationManager.queryFaModelRuleValidationListByServiceOidAndRuleType(serviceOid, "JCBD", "");
        if (null != ruleValidListJCBD && ruleValidListJCBD.size() > 0) {
            ruleValidList.addAll(ruleValidListJCBD);
            ApiResultSet<Map<String, Object>> mapCaseResult = qlCaseServiceService.getAllQlCaseByCaseOid(caseOid);
            Map<String, Object> objectMap = mapCaseResult.getData();
            Object qlCaseObject = objectMap.get("qlCase");
            Object qlCaseApplayObject = objectMap.get("qlCaseApplay");
            Object qlCaseExtObject = objectMap.get("qlCaseExt");
            Map<String, Object> map = new HashMap<>();
            if (null != qlCaseObject) {
                Map<String, Object> qlCasemap = (Map<String, Object>) qlCaseObject;
                map.putAll(qlCasemap);
            }
            if (null != qlCaseApplayObject) {
                Map<String, Object> qlCaseApplaymap = (Map<String, Object>) qlCaseApplayObject;
                map.putAll(qlCaseApplaymap);
            }
            if (null != qlCaseExtObject) {
                Map<String, Object> qlCaseExmap = (Map<String, Object>) qlCaseExtObject;
                map.putAll(qlCaseExmap);
            }

            for (FaModelRuleValidation faModelRuleValidation : ruleValidListJCBD) {
                String thanTemplateMetadataCode = faModelRuleValidation.getThanTemplateMetadataCode();
                String value = (String) map.get(thanTemplateMetadataCode);
                org.json.JSONObject blockJsonObject = new org.json.JSONObject();
                blockJsonObject.put("words", value);
                blockMapResult.put(thanTemplateMetadataCode + "_" + faModelRuleValidation.getThanSxMaterialOid() + "_" + faModelRuleValidation.getThanRefinedMaterialOid(), blockJsonObject);
            }
        }
        //电子证照
        List<FaModelRuleValidation> ruleValidListDZZZ = this.faModelRuleValidationManager.queryFaModelRuleValidationListByServiceOidAndRuleType(serviceOid, "DZZZ", "");
        if (null != ruleValidListDZZZ && ruleValidListDZZZ.size() > 0) {
            //电子证照取值
            Map<String, String> dzzzMap = new HashMap<>();
            if (StringUtils.isNotEmpty(qlCaseMaterial.getElecBillOid())) {
                dzzzMap.put(qlCaseMaterial.getElecBillOid(), qlCaseMaterial.getElectronicResult());
            }
            ruleValidList.addAll(ruleValidListDZZZ);
            for (FaModelRuleValidation faModelRuleValidation : ruleValidListDZZZ) {
                String elecBillOid = faModelRuleValidation.getThanCatalogOid();
                String electronicResult = dzzzMap.get(elecBillOid);
                if (StringUtils.isNotEmpty(electronicResult)) {
                    JSONObject jsonObj = JSONObject.parseObject(electronicResult);
                    String resultData = String.valueOf(jsonObj.get("resultData") == null ? "" : jsonObj.get("resultData"));
                    Map maps = (Map) JSONObject.parseObject(resultData);
                    String thanTemplateMetadataCode = faModelRuleValidation.getThanTemplateMetadataCode();
                    String value = (String) maps.get(thanTemplateMetadataCode);
                    org.json.JSONObject blockJsonObject = new org.json.JSONObject();
                    blockJsonObject.put("words", value);
                    blockMapResult.put(thanTemplateMetadataCode + "_" + faModelRuleValidation.getThanSxMaterialOid() + "_" + faModelRuleValidation.getThanRefinedMaterialOid(), blockJsonObject);

                }
            }
        }
        if (ruleValidList == null || ruleValidList.size() < 1) {
            jsonObject.put("code", FaStaticParam.PRE_TRIAL_RESULT_STATUS_NOT_RULE);
            jsonObject.put("message", "无验证规则");
            return jsonObject;
        }

        List<String> sxmaterOids = new ArrayList<>();
        sxmaterOids.add(materialOidOld);
        for (FaModelRuleValidation faModelRuleValidation1 : ruleValidList) {
            if (StringUtils.isNotEmpty(faModelRuleValidation1.getThanSxMaterialOid())) {
                if (!sxmaterOids.contains(faModelRuleValidation1.getThanSxMaterialOid())) {
                    sxmaterOids.add(faModelRuleValidation1.getThanSxMaterialOid());
                }
            }
        }

        //上传材料信息
/*        ApiResultSet<List<QlCaseMaterial>> qlcmResult=qlCaseMaterialServiceService.queryQlCaseMaterialByCaseOid(caseOid);
        List<QlCaseMaterial> qlCaseMaterialList=qlcmResult.getData();*/

        List<FaMaterialPicOcrResult> ocrResultList = new ArrayList<FaMaterialPicOcrResult>();
        for (String aterialoid : sxmaterOids) {
            FaMaterialPicOcrResult faMaterialPicOcrResult = new FaMaterialPicOcrResult();
            faMaterialPicOcrResult.setUuid(caseOid);
            faMaterialPicOcrResult.setMaterialOid(aterialoid);
            List<FaMaterialPicOcrResult> faMaterialPicOcrResultList = this.faMaterialPicOcrResultManager.getFaMaterialPicOcrResultByFmpor(faMaterialPicOcrResult);
            if (!CollUtil.isEmpty(faMaterialPicOcrResultList)) {
                ocrResultList.addAll(faMaterialPicOcrResultList);
            }
        }

        if (ocrResultList == null || ocrResultList.size() < 1) {
            jsonObject.put("code", FaStaticParam.PRE_TRIAL_RESULT_STATUS_NOT_RESULT);
            jsonObject.put("message", "无审核结果");
            return jsonObject;
        }


        // SET集合(元素：目录子项编号+"_"+材料主键组成的唯一标识)，用于过滤同一页材料多次上传产生的识别记录
        Set<String> cataCodeSet = new HashSet<>();
        Map<String, String> blockMap = new HashMap<String, String>();
        for (FaMaterialPicOcrResult ocrResult : ocrResultList) {
            String cataCode = ocrResult.getCataCode();
            String materialOid = ocrResult.getMaterialOid();
            String refinedMaterialOid = ocrResult.getRefinedMaterialOid();
            if (StrUtil.isBlank(cataCode)) {
                continue;
            }
            // 目录子项编号已存在，进入下一次循环
            if (cataCodeSet.contains(cataCode + "_" + materialOid + "_" + refinedMaterialOid)) {
                continue;
            }
            cataCodeSet.add(cataCode + "_" + materialOid + "_" + refinedMaterialOid);
            if (StrUtil.isBlank(ocrResult.getResultJson())) {
                continue;
            }
            org.json.JSONObject resultJsonObject = new org.json.JSONObject(ocrResult.getResultJson());
            org.json.JSONArray resultArray = resultJsonObject.getJSONArray("result");

            String sealResultJson = ocrResult.getSealResultJson();
            String signResultJson = ocrResult.getSignResultJson();

            blockMap.put("sealResultJson" + "_" + materialOid + "_" + refinedMaterialOid, sealResultJson);
            blockMap.put("signResultJson" + "_" + materialOid + "_" + refinedMaterialOid, signResultJson);

            if (resultArray == null || resultArray.length() <= 0) {
                continue;
            }
            for (int i = 0; i < resultArray.length(); i++) {
                org.json.JSONObject blockJsonObject = resultArray.getJSONObject(i);
                String imageHeight = "";
                String imageWidth = "";
                if (StringUtils.isNotEmpty(ocrResult.getImageHeight())) {
                    imageHeight = ocrResult.getImageHeight();
                }
                if (StringUtils.isNotEmpty(ocrResult.getImageWidth())) {
                    imageWidth = ocrResult.getImageWidth();
                }
                blockJsonObject.put("attaOid", ocrResult.getAttaOid());
                blockJsonObject.put("localImageUrl", ocrResult.getLocalImageUrl());
                blockJsonObject.put("imageHeight", imageHeight);
                blockJsonObject.put("imageWidth", imageWidth);
                blockMapResult.put(blockJsonObject.getString("code") + "_" + ocrResult.getMaterialOid() + "_" + refinedMaterialOid, blockJsonObject);
            }

            ocrResult.setResultJsonObject(resultJsonObject);
        }

        jsonObject.put("code", FaStaticParam.INTER_STATUS_CODE_SUCCESS);
        jsonObject.put("result", getAuitResultByValid(request, caseOid, ruleValidList, blockMap, blockMapResult, smMaterial));
        return jsonObject;
    }


    /***
     * @Description: 获取保存办件审核列表结果根据办件id
     * @Author:liangss
     * @Date:2021/7/01
     * @Param: [request, caseOid, serviceOid]
     */
    public List<QlCaseMaterial> getQlCaseMaterialListAndAuditResult(HttpServletRequest request,
                                                                    String caseOid,
                                                                    String serviceOid
    ) throws Exception {
        //上传材料信息
        ApiResultSet<List<QlCaseMaterial>> qlcmResult = qlCaseMaterialServiceService.queryQlCaseMaterialByCaseOid(caseOid);
        List<QlCaseMaterial> qlCaseMaterialList = qlcmResult.getData();
        //组装审核结果并入库
        Map<String, Object> map = getAndSaveOrUpdataAuditResult(request, caseOid, serviceOid);
        List<AuditResult> resultList = new ArrayList<>();
        if (map.get("result") != null) {
            resultList = (List<AuditResult>) map.get("result");
        }
        List<PreTrialResultVo> list = new ArrayList<>();
        String resultStatus = "";
        String resultDetail = "";
        //需要人工审核
        String xyrgsh = "N";
        Map<String, Object> preTrialResul = new HashMap<>();
        qlCaseMaterialFor:
        for (QlCaseMaterial qlCaseMaterial : qlCaseMaterialList) {
            xyrgsh = "N";
            String collectionType = qlCaseMaterial.getCollectionType();//1纸质收取
            if (StringUtils.isEmpty(collectionType)) {
                collectionType = "1";
            }
            //默认通过
            resultStatus = FaStaticParam.EXAMINE_RESULT_STATUS_PASS;
            String materialOid = qlCaseMaterial.getMaterialOid();
            String materialName = qlCaseMaterial.getMaterialName();
            //精细化材料列表
            ApiResultSet<List<RefinedMaterial>> listApiResultSet = refinedMaterialService.getRefinedMaterialListByMaterialOid(materialOid);
            List<RefinedMaterial> refinedMaterialList = new ArrayList<>();
            if (null != listApiResultSet.getData()) {
                refinedMaterialList = listApiResultSet.getData();
            }
            //无细化材料无需审核
            if (refinedMaterialList.size() <= 0) {
                resultStatus = FaStaticParam.EXAMINEL_RESULT_STATUS_NOT_AUDIT;
                qlCaseMaterial.setResultStatus(resultStatus);
                qlCaseMaterialServiceService.updateQlCaseMaterial(qlCaseMaterial);
                continue qlCaseMaterialFor;
            }


            //无审核要点无需审核
            List<ReviewPoints> reviewPointsList = new ArrayList<>();
            String materialCatalogOidAll = "";
            for (RefinedMaterial refinedMaterial : refinedMaterialList) {
                String materialCatalogOid = refinedMaterial.getMaterialCatalogOid();
                if (StringUtils.isNotEmpty(materialCatalogOid)) {
                    materialCatalogOidAll = materialCatalogOidAll + materialCatalogOid + ";";
                }
                ApiResultSet<List<ReviewPoints>> reviewPointsResultSet = reviewPointsService.getReviewPointsListByRefinedMaterialOid(refinedMaterial.getOid());
                if (null != reviewPointsResultSet.getData()) {
                    List<ReviewPoints> reviewPointsListnew = reviewPointsResultSet.getData();
                    if (reviewPointsListnew.size() > 0) {
                        reviewPointsList.addAll(reviewPointsResultSet.getData());
                    }
                }
            }
            if (reviewPointsList.size() <= 0) {
                resultStatus = FaStaticParam.EXAMINEL_RESULT_STATUS_NOT_AUDIT;
                qlCaseMaterial.setResultStatus(resultStatus);
                qlCaseMaterialServiceService.updateQlCaseMaterial(qlCaseMaterial);
                continue qlCaseMaterialFor;
            }


            //没有配置智审（需人工核验）
            if (StringUtils.isEmpty(materialCatalogOidAll)) {
                resultStatus = FaStaticParam.EXAMINEL_RESULT_STATUS_ARTIFICIAL_AUDIT;
                qlCaseMaterial.setResultStatus(resultStatus);
                qlCaseMaterialServiceService.updateQlCaseMaterial(qlCaseMaterial);
                continue qlCaseMaterialFor;
            }
            //无验证规则 （需人工核验）
            /*FaModelRuleValidation faModelRuleValidation=new FaModelRuleValidation();
            faModelRuleValidation.setServiceOid(serviceOid);
            faModelRuleValidation.setSxMaterialOid(qlCaseMaterial.getMaterialOid());
            List<FaModelRuleValidation> ruleValidList=this.faModelRuleValidationManager.queryFaModelRuleValidationList(faModelRuleValidation);*/
            List<FaModelRuleValidation> ruleValidList = this.faModelRuleValidationManager.queryFaModelRuleValidationListByServiceOidAndmaterialOid(serviceOid, qlCaseMaterial.getMaterialOid());
            if (ruleValidList == null || ruleValidList.size() < 1) {
                resultStatus = FaStaticParam.EXAMINEL_RESULT_STATUS_ARTIFICIAL_AUDIT;
                qlCaseMaterial.setResultStatus(resultStatus);
                qlCaseMaterialServiceService.updateQlCaseMaterial(qlCaseMaterial);
                continue qlCaseMaterialFor;
            }

            //纸质提交和容缺候补的需人工核验
            if (!collectionType.equals("3")) {
                resultStatus = FaStaticParam.EXAMINEL_RESULT_STATUS_ARTIFICIAL_AUDIT;
                qlCaseMaterial.setResultStatus(resultStatus);
                qlCaseMaterialServiceService.updateQlCaseMaterial(qlCaseMaterial);
                continue qlCaseMaterialFor;
            }

            refinedMaterialFor:
            for (RefinedMaterial refinedMaterial : refinedMaterialList) {
                String refinedMaterialOid = refinedMaterial.getOid();
                String materialCatalogOid = refinedMaterial.getMaterialCatalogOid();

                if (StringUtils.isEmpty(materialCatalogOid)) {
                    //没有配置智审（需人工核验）
                    //resultStatus = FaStaticParam.EXAMINEL_RESULT_STATUS_ARTIFICIAL_AUDIT;
                    continue refinedMaterialFor;
                }
                //根据ocr结果判断
                if (null != resultList && resultList.size() > 0) {
                    for (int i = 0; i < resultList.size(); i++) {
                        AuditResult auditResults = resultList.get(i);
                        if (materialCatalogOid.equals(auditResults.getCatalogOid()) && refinedMaterialOid.equals(auditResults.getRefinedMaterialOid())) {
                            //判断有没有需要人工审核的
                            if (auditResults.getAuditPointStatus().equals("3")) {
                                xyrgsh = "Y";
                            }
                            // 验证结果不通过，设置审核标识为1：不通过
                            if (auditResults.getValid() != null && auditResults.getValid() == FaStaticParam.FLASE_STATUS) {
                                resultStatus = FaStaticParam.EXAMINE_RESULT_STATUS_NOT_PASS;
                                qlCaseMaterial.setResultStatus(resultStatus);
                                qlCaseMaterialServiceService.updateQlCaseMaterial(qlCaseMaterial);
                                continue qlCaseMaterialFor;
                            }


                        }
                    }
                } else {
                    resultStatus = FaStaticParam.EXAMINE_RESULT_STATUS_NOT_PASS;
                    qlCaseMaterial.setResultStatus(resultStatus);
                    qlCaseMaterialServiceService.updateQlCaseMaterial(qlCaseMaterial);
                    continue qlCaseMaterialFor;
                }
            }
            /*qlCaseMaterial.setAuditType(resultStatus);*/
            //预审通过并且存在需要人工审核的要点状态改完预审通过（部分需人工审核）
            if (resultStatus.equals(FaStaticParam.EXAMINE_RESULT_STATUS_PASS) && xyrgsh.equals("Y")) {
                resultStatus = FaStaticParam.EXAMINE_RESULT_STATUS_PASS_AND_MUST_ARTIFICIAL;
            }
            qlCaseMaterial.setResultStatus(resultStatus);
            qlCaseMaterialServiceService.updateQlCaseMaterial(qlCaseMaterial);
        }

        return qlCaseMaterialList;
    }

    /**
     * 获取智审材料列表
     *
     * @param caseOid
     * @param serviceOid
     * @return
     */
    public List<QlCaseMaterial> getIntelligentAuditMaterialList(String caseOid, String serviceOid) {
        //上传材料信息
        ApiResultSet<List<QlCaseMaterial>> qlCaseMaterialListResult = qlCaseMaterialServiceService.queryQlCaseMaterialByCaseOid(caseOid);
        if (qlCaseMaterialListResult == null || qlCaseMaterialListResult.getData() == null) {
            throw new ResultInfoException("当前办件材料信息为空！");
        }
        List<QlCaseMaterial> qlCaseMaterialList = qlCaseMaterialListResult.getData();

        List<QlCaseMaterial> intelligentAuditMaterialList = new ArrayList<>();

        qlCaseMaterialLabel:
        for (QlCaseMaterial qlCaseMaterial : qlCaseMaterialList) {
            //精细化材料列表
            ApiResultSet<List<RefinedMaterial>> refinedMaterialListResult = refinedMaterialService
                    .getRefinedMaterialListByMaterialOid(qlCaseMaterial.getMaterialOid());
            List<RefinedMaterial> refinedMaterialList = new ArrayList<>();
            if (refinedMaterialListResult != null && refinedMaterialListResult.getData() != null) {
                refinedMaterialList = refinedMaterialListResult.getData();
            }

            //无细化材料无需审核
            if (refinedMaterialList.size() <= 0) {
                qlCaseMaterial.setResultStatus(FaStaticParam.EXAMINEL_RESULT_STATUS_NOT_AUDIT);
                qlCaseMaterialServiceService.updateQlCaseMaterial(qlCaseMaterial);
                continue;
            }

            //无审核要点无需审核
            List<ReviewPoints> reviewPointsList = new ArrayList<>();
            String materialCatalogOidAll = "";
            for (RefinedMaterial refinedMaterial : refinedMaterialList) {
                String materialCatalogOid = refinedMaterial.getMaterialCatalogOid();
                if (StringUtils.isNotEmpty(materialCatalogOid)) {
                    materialCatalogOidAll = materialCatalogOidAll + materialCatalogOid + ";";
                }
                ApiResultSet<List<ReviewPoints>> reviewPointsResultSet = reviewPointsService
                        .getReviewPointsListByRefinedMaterialOid(refinedMaterial.getOid());
                if (reviewPointsResultSet.getData() != null && reviewPointsResultSet.getData() != null) {
                    if (reviewPointsResultSet.getData().size() > 0) {
                        reviewPointsList.addAll(reviewPointsResultSet.getData());
                    }
                }
            }
            if (reviewPointsList.size() <= 0) {
                qlCaseMaterial.setResultStatus(FaStaticParam.EXAMINEL_RESULT_STATUS_NOT_AUDIT);
                qlCaseMaterialServiceService.updateQlCaseMaterial(qlCaseMaterial);
                continue;
            }

            //没有配置智审（需人工核验）
            if (StringUtils.isEmpty(materialCatalogOidAll)) {
                qlCaseMaterial.setResultStatus(FaStaticParam.EXAMINEL_RESULT_STATUS_ARTIFICIAL_AUDIT);
                qlCaseMaterialServiceService.updateQlCaseMaterial(qlCaseMaterial);
                continue;
            }

            //无验证规则 （需人工核验）
            List<FaModelRuleValidation> ruleValidList = this.faModelRuleValidationManager
                    .queryFaModelRuleValidationListByServiceOidAndmaterialOid(serviceOid, qlCaseMaterial.getMaterialOid());
            if (ruleValidList == null || ruleValidList.size() < 1) {
                qlCaseMaterial.setResultStatus(FaStaticParam.EXAMINEL_RESULT_STATUS_ARTIFICIAL_AUDIT);
                qlCaseMaterialServiceService.updateQlCaseMaterial(qlCaseMaterial);
                continue;
            }

            //纸质提交和容缺候补的需人工核验
            if (!"2".equals(qlCaseMaterial.getCollectionType()) && !"3".equals(qlCaseMaterial.getCollectionType())) {
                qlCaseMaterial.setResultStatus(FaStaticParam.EXAMINEL_RESULT_STATUS_ARTIFICIAL_AUDIT);
                qlCaseMaterialServiceService.updateQlCaseMaterial(qlCaseMaterial);
                continue;
            }

            // 无上传材料 且 上传材料未分类的 不进行智审
            ApiResultSet<List<QlCaseMaterialAtta>> qlCaseMaterialAttaResult =
                    qlCaseMaterialAttaServiceService.queryQlCaseMaterialAttaByCaseMaterialOid(qlCaseMaterial.getCaseMaterialOid());
            List<QlCaseMaterialAtta> qlCaseMaterialAttaList = new ArrayList<>();
            if (qlCaseMaterialAttaResult != null && qlCaseMaterialAttaResult.getData() != null) {
                qlCaseMaterialAttaList = qlCaseMaterialAttaResult.getData();
            }
            for (QlCaseMaterialAtta qlCaseMaterialAtta : qlCaseMaterialAttaList) {
                if (StrUtil.isNotBlank(qlCaseMaterialAtta.getMaterialCatalogOid())
                        && StrUtil.isNotBlank(qlCaseMaterialAtta.getRefinedMaterialOid())
                        && StrUtil.isNotBlank(qlCaseMaterialAtta.getAttaOid())) {
                    intelligentAuditMaterialList.add(qlCaseMaterial);
                    continue qlCaseMaterialLabel;
                }
            }

        }
        return intelligentAuditMaterialList;
    }

    /***
     * @Description: 没有扫描类型的材料状态更新
     * @Author:liangss
     * @Date:2021/8/5
     * @Param: [qlCaseMaterialList]
     */
    public List<QlCaseMaterial> getQlCaseMaterialListAndAuditResult(List<QlCaseMaterial> qlCaseMaterialList) {
        String resultStatus = "";
        qlCaseMaterialFor:
        for (QlCaseMaterial qlCaseMaterial : qlCaseMaterialList) {
            if (StringUtils.isEmpty(qlCaseMaterial.getResultStatus())) {
                String collectionType = qlCaseMaterial.getCollectionType();//1纸质收取 3扫描
                /*  if(!collectionType.equals("3")){*/
                //默认人工审核
                resultStatus = FaStaticParam.EXAMINEL_RESULT_STATUS_ARTIFICIAL_AUDIT;
                //精细化材料列表
                ApiResultSet<List<RefinedMaterial>> listApiResultSet = refinedMaterialService.getRefinedMaterialListByMaterialOid(qlCaseMaterial.getMaterialOid());
                List<RefinedMaterial> refinedMaterialList = new ArrayList<>();
                if (null != listApiResultSet.getData()) {
                    refinedMaterialList = listApiResultSet.getData();
                }
                //无细化材料无需审核
                if (refinedMaterialList.size() <= 0) {
                    resultStatus = FaStaticParam.EXAMINEL_RESULT_STATUS_NOT_AUDIT;
                    qlCaseMaterial.setResultStatus(resultStatus);
                    qlCaseMaterialServiceService.updateQlCaseMaterial(qlCaseMaterial);
                    continue qlCaseMaterialFor;
                }


                //无审核要点无需审核
                List<ReviewPoints> reviewPointsList = new ArrayList<>();
                String materialCatalogOidAll = "";
                for (RefinedMaterial refinedMaterial : refinedMaterialList) {
                    String materialCatalogOid = refinedMaterial.getMaterialCatalogOid();
                    if (StringUtils.isNotEmpty(materialCatalogOid)) {
                        materialCatalogOidAll = materialCatalogOidAll + materialCatalogOid + ";";
                    }
                    ApiResultSet<List<ReviewPoints>> reviewPointsResultSet = reviewPointsService.getReviewPointsListByRefinedMaterialOid(refinedMaterial.getOid());
                    if (null != reviewPointsResultSet.getData()) {
                        List<ReviewPoints> reviewPointsListnew = reviewPointsResultSet.getData();
                        if (reviewPointsListnew.size() > 0) {
                            reviewPointsList.addAll(reviewPointsResultSet.getData());
                        }
                    }
                }
                if (reviewPointsList.size() <= 0) {
                    resultStatus = FaStaticParam.EXAMINEL_RESULT_STATUS_NOT_AUDIT;
                    qlCaseMaterial.setResultStatus(resultStatus);
                    qlCaseMaterialServiceService.updateQlCaseMaterial(qlCaseMaterial);
                    continue qlCaseMaterialFor;
                }


                qlCaseMaterial.setResultStatus(resultStatus);
                qlCaseMaterialServiceService.updateQlCaseMaterial(qlCaseMaterial);

                /* }*/
            }

        }

        return qlCaseMaterialList;

    }


    /***
     * @Description: 获取并保存审核结果根据办件oid
     * @Author:liangss
     * @Date:2021/7/28
     * @Param: [request, caseOid, serviceOid]
     */
    public Map<String, Object> getAndSaveOrUpdataAuditResult(HttpServletRequest request, String caseOid,
                                                             String serviceOid) throws Exception {
        Map<String, Object> jsonObject = new HashMap<String, Object>();
        if (StrUtil.isBlank(caseOid)) {
            jsonObject.put("code", FaStaticParam.INTER_STATUS_CODE_PARAM_EXCEPTION);
            jsonObject.put("message", "办件唯一标识[caseOid]不能为空！");
            return jsonObject;
        }
        //扫描上传的材料集合
        List<String> smMaterial = new ArrayList<>();
        //删除之前存储的审核结果
        auditResultManager.updateByCaseOid(caseOid);
        //上传材料信息
        ApiResultSet<List<QlCaseMaterial>> qlcmResult = qlCaseMaterialServiceService.queryQlCaseMaterialByCaseOid(caseOid);
        List<QlCaseMaterial> qlCaseMaterialList = qlcmResult.getData();
        //电子证照取值
        Map<String, String> dzzzMap = new HashMap<>();
        List<FaMaterialPicOcrResult> ocrResultList = new ArrayList<FaMaterialPicOcrResult>();
        for (QlCaseMaterial qlCaseMaterial : qlCaseMaterialList) {
            String collectionType = qlCaseMaterial.getCollectionType();//1纸质收取
            //扫描
            if (StringUtils.isNotEmpty(collectionType) && collectionType.equals("3")) {
                smMaterial.add(qlCaseMaterial.getMaterialOid());
            }
            if (StringUtils.isNotEmpty(qlCaseMaterial.getElecBillOid())) {
                dzzzMap.put(qlCaseMaterial.getElecBillOid(), qlCaseMaterial.getElectronicResult());
            }
            FaMaterialPicOcrResult faMaterialPicOcrResult = new FaMaterialPicOcrResult();
            faMaterialPicOcrResult.setUuid(caseOid);
            faMaterialPicOcrResult.setMaterialOid(qlCaseMaterial.getMaterialOid());
            List<FaMaterialPicOcrResult> faMaterialPicOcrResultList = this.faMaterialPicOcrResultManager.getFaMaterialPicOcrResultByFmpor(faMaterialPicOcrResult);
            if (!CollUtil.isEmpty(faMaterialPicOcrResultList)) {
                ocrResultList.addAll(faMaterialPicOcrResultList);
            }
        }

        Map<String, org.json.JSONObject> blockMapResult = new HashMap<String, org.json.JSONObject>();
        List<FaModelRuleValidation> ruleValidList = new ArrayList<>();
        //普通规则
        List<FaModelRuleValidation> ruleValidListPT = this.faModelRuleValidationManager.queryFaModelRuleValidationListByServiceOidAndRuleType(serviceOid, "", "");
        ruleValidList.addAll(ruleValidListPT);
        //办件查询
        ApiResultSet<QlCase> resultSet = qlCaseServiceService.queryQlCaseByCaseOid(caseOid);
        QlCase qlCase = null;
        if (resultSet.getData() != null) {
            qlCase = resultSet.getData();
        }
        //电子表单
        List<FaModelRuleValidation> ruleValidListDZBD = this.faModelRuleValidationManager.queryFaModelRuleValidationListByServiceOidAndRuleType(serviceOid, "DZBD", "");
        if (null != ruleValidListDZBD && ruleValidListDZBD.size() > 0) {
            ruleValidList.addAll(ruleValidListDZBD);
            String formOids = qlCase.getFormOids();
            Map<String, Object> map = new HashMap<>();
            if (formOids != null) {
                JSONArray array = JSON.parseArray(formOids);
                for (Object obj : array) {
                    JSONObject object = (JSONObject) JSONObject.toJSON(obj);
                    String designOid = (String) object.get("designOid");
                    String authorizeKey = (String) object.get("authorizeKey");
                    String formCode = (String) object.get("formCode");
                    String formName = (String) object.get("formName");
                    Map<String, Object> mapchild = this.getFormApiData(caseOid, designOid, authorizeKey);
                    map.putAll(mapchild);
                       /* ApiResultSet<SxFormInfo> sxFormInfoApiResultSet= sxFormInfoService.getSxFormInfoByDesignOid(designOid);
                        if(null!=sxFormInfoApiResultSet.getData()){
                            SxFormInfo sxFormInfo= sxFormInfoApiResultSet.getData();
                        }*/
                }
            }
            for (FaModelRuleValidation faModelRuleValidation : ruleValidListDZBD) {
                String thanTemplateMetadataCode = faModelRuleValidation.getThanTemplateMetadataCode();
                Object valueObject = map.get(thanTemplateMetadataCode);
                String value = "";
                if (null != valueObject) {
                    value = valueObject.toString();
                }
                org.json.JSONObject blockJsonObject = new org.json.JSONObject();
                blockJsonObject.put("words", value);
                blockMapResult.put(thanTemplateMetadataCode + "_" + faModelRuleValidation.getThanSxMaterialOid() + "_" + faModelRuleValidation.getThanRefinedMaterialOid(), blockJsonObject);

            }
        }


        //基础表单
        List<FaModelRuleValidation> ruleValidListJCBD = this.faModelRuleValidationManager.queryFaModelRuleValidationListByServiceOidAndRuleType(serviceOid, "JCBD", "");
        if (null != ruleValidListJCBD && ruleValidListJCBD.size() > 0) {
            ruleValidList.addAll(ruleValidListJCBD);
            ApiResultSet<Map<String, Object>> mapCaseResult = qlCaseServiceService.getAllQlCaseByCaseOid(caseOid);
            Map<String, Object> objectMap = mapCaseResult.getData();
            Object qlCaseObject = objectMap.get("qlCase");
            Object qlCaseApplayObject = objectMap.get("qlCaseApplay");
            Object qlCaseExtObject = objectMap.get("qlCaseExt");
            Map<String, Object> map = new HashMap<>();
            if (null != qlCaseObject) {
                Map<String, Object> qlCasemap = (Map<String, Object>) qlCaseObject;
                map.putAll(qlCasemap);
            }
            if (null != qlCaseApplayObject) {
                Map<String, Object> qlCaseApplaymap = (Map<String, Object>) qlCaseApplayObject;
                map.putAll(qlCaseApplaymap);
            }
            if (null != qlCaseExtObject) {
                Map<String, Object> qlCaseExmap = (Map<String, Object>) qlCaseExtObject;
                map.putAll(qlCaseExmap);
            }

            for (FaModelRuleValidation faModelRuleValidation : ruleValidListJCBD) {
                String thanTemplateMetadataCode = faModelRuleValidation.getThanTemplateMetadataCode();
                Object valueObject = map.get(thanTemplateMetadataCode);
                String value = "";
                if (null != valueObject) {
                    value = valueObject.toString();
                }
                org.json.JSONObject blockJsonObject = new org.json.JSONObject();
                blockJsonObject.put("words", value);
                blockMapResult.put(thanTemplateMetadataCode + "_" + faModelRuleValidation.getThanSxMaterialOid() + "_" + faModelRuleValidation.getThanRefinedMaterialOid(), blockJsonObject);
            }
        }
        //电子证照
        List<FaModelRuleValidation> ruleValidListDZZZ = this.faModelRuleValidationManager.queryFaModelRuleValidationListByServiceOidAndRuleType(serviceOid, "DZZZ", "");
        if (null != ruleValidListDZZZ && ruleValidListDZZZ.size() > 0) {
            ruleValidList.addAll(ruleValidListDZZZ);
            for (FaModelRuleValidation faModelRuleValidation : ruleValidListDZZZ) {
                String elecBillOid = faModelRuleValidation.getThanCatalogOid();
                String electronicResult = dzzzMap.get(elecBillOid);
                if (StringUtils.isNotEmpty(electronicResult)) {
                    JSONObject jsonObj = JSONObject.parseObject(electronicResult);
                    String resultData = String.valueOf(jsonObj.get("resultData") == null ? "" : jsonObj.get("resultData"));
                    Map maps = (Map) JSONObject.parseObject(resultData);
                    String thanTemplateMetadataCode = faModelRuleValidation.getThanTemplateMetadataCode();
                    Object valueObject = maps.get(thanTemplateMetadataCode);
                    String value = "";
                    if (null != valueObject) {
                        value = valueObject.toString();
                    }
                    org.json.JSONObject blockJsonObject = new org.json.JSONObject();
                    blockJsonObject.put("words", value);
                    blockMapResult.put(thanTemplateMetadataCode + "_" + faModelRuleValidation.getThanSxMaterialOid() + "_" + faModelRuleValidation.getThanRefinedMaterialOid(), blockJsonObject);

                }
            }
        }

        if (ruleValidList == null || ruleValidList.size() < 1) {
            jsonObject.put("code", FaStaticParam.PRE_TRIAL_RESULT_STATUS_NOT_RULE);
            jsonObject.put("message", "无验证规则");
            return jsonObject;
        }
        // SET集合(元素：目录子项编号+"_"+材料主键组成的唯一标识)，用于过滤同一页材料多次上传产生的识别记录
        Set<String> cataCodeSet = new HashSet<>();
        Map<String, String> blockMap = new HashMap<String, String>();

        for (FaMaterialPicOcrResult ocrResult : ocrResultList) {
            String cataCode = ocrResult.getCataCode();
            String materialOid = ocrResult.getMaterialOid();
            String refinedMaterialOid = ocrResult.getRefinedMaterialOid();
            if (StrUtil.isBlank(cataCode)) {
                continue;
            }
            // 目录子项编号已存在，进入下一次循环
            if (cataCodeSet.contains(cataCode + "_" + materialOid + "_" + refinedMaterialOid)) {
                continue;
            }
            cataCodeSet.add(cataCode + "_" + materialOid + "_" + refinedMaterialOid);
            if (StrUtil.isBlank(ocrResult.getResultJson())) {
                continue;
            }
            org.json.JSONObject resultJsonObject = new org.json.JSONObject(ocrResult.getResultJson());
            org.json.JSONArray resultArray = resultJsonObject.getJSONArray("result");

            String sealResultJson = ocrResult.getSealResultJson();
            String signResultJson = ocrResult.getSignResultJson();

            blockMap.put("sealResultJson" + "_" + materialOid + "_" + refinedMaterialOid, sealResultJson);
            blockMap.put("signResultJson" + "_" + materialOid + "_" + refinedMaterialOid, signResultJson);

            if (resultArray == null || resultArray.length() <= 0) {
                continue;
            }
            for (int i = 0; i < resultArray.length(); i++) {
                org.json.JSONObject blockJsonObject = resultArray.getJSONObject(i);
                String imageHeight = "";
                String imageWidth = "";
                if (StringUtils.isNotEmpty(ocrResult.getImageHeight())) {
                    imageHeight = ocrResult.getImageHeight();
                }
                if (StringUtils.isNotEmpty(ocrResult.getImageWidth())) {
                    imageWidth = ocrResult.getImageWidth();
                }
                blockJsonObject.put("attaOid", ocrResult.getAttaOid());
                blockJsonObject.put("localImageUrl", ocrResult.getLocalImageUrl());
                blockJsonObject.put("imageHeight", imageHeight);
                blockJsonObject.put("imageWidth", imageWidth);
                blockMapResult.put(blockJsonObject.getString("code") + "_" + ocrResult.getMaterialOid() + "_" + refinedMaterialOid, blockJsonObject);
            }

            ocrResult.setResultJsonObject(resultJsonObject);
        }
        jsonObject.put("code", FaStaticParam.INTER_STATUS_CODE_SUCCESS);
        jsonObject.put("result", getAuitResultByValid(request, caseOid, ruleValidList, blockMap, blockMapResult, smMaterial));
        return jsonObject;
    }


    /***
     * @Description:比对规则并保存比对结果AuditResult
     * @Author:liangss
     * @Date:2021/7/22
     * @Param: [request, caseOid, ruleValidList, blockMap, blockMapResult]
     */
    private List<AuditResult> getAuitResultByValid(HttpServletRequest request, String caseOid,
                                                   List<FaModelRuleValidation> ruleValidList,
                                                   Map<String, String> blockMap, Map<String, org.json.JSONObject> blockMapResult, List<String> smMaterial)
            throws Exception {
        List<AuditResult> auditResultList = new ArrayList<>();

        List<Map<String, Object>> validResultArray = new ArrayList<Map<String, Object>>();
        for (FaModelRuleValidation ruleValid : ruleValidList) {
            AuditResult auditResult = new AuditResult();
            auditResult.setIsIgnore("N");
            auditResult.setCaseOid(caseOid);
            String name = ruleValid.getTemplateMetadataName();
            String memo = "";
            Map<String, Object> validResult = new HashMap<String, Object>();
            String validateType = ruleValid.getValidateType();
            String metadataCode = ruleValid.getTemplateMetadataCode();
            String materialOid = ruleValid.getSxMaterialOid();
            String refinedMaterialOid = ruleValid.getRefinedMaterialOid();
            auditResult.setValidaeType(validateType);
            auditResult.setMaterialOid(materialOid);
            auditResult.setRefinedMaterialOid(refinedMaterialOid);
            auditResult.setCatalogOid(ruleValid.getCatalogOid());
            auditResult.setCode(ruleValid.getTemplateMetadataCode());
            auditResult.setName(ruleValid.getTemplateMetadataName());
            if (StringUtils.isNotEmpty(ruleValid.getAuditPointStatus())) {
                auditResult.setAuditPointStatus(ruleValid.getAuditPointStatus());
            } else {
                auditResult.setAuditPointStatus("1");
            }
            auditResult.setReviewPointsOid(ruleValid.getReviewPointOid());

            String words = null, picPath = null;
            String sealResultJson = null;
            String signResultJson = null;
            int top = 0;
            int left = 0;
            int width = 0;
            int height = 0;
            int imageHeight = 0;
            int imageWidth = 0;
            String localImageUrl = "";
            String attaOid = "";
            String wordskey = metadataCode + "_" + materialOid + "_" + refinedMaterialOid;
            if (blockMapResult.containsKey(wordskey)) {
                org.json.JSONObject blockJsonObject = blockMapResult.get(wordskey);

                /* log.info("***"+blockJsonObject);*/
                //validResult.put("blockJsonObject", blockJsonObject);
                if (blockJsonObject.has("top")) {
                    top = blockJsonObject.getInt("top");
                }
                if (blockJsonObject.has("left")) {
                    left = blockJsonObject.getInt("left");
                }
                if (blockJsonObject.has("width")) {
                    width = blockJsonObject.getInt("width");
                }
                if (blockJsonObject.has("height")) {
                    height = blockJsonObject.getInt("height");
                }
                imageHeight = blockJsonObject.getInt("imageHeight");
                imageWidth = blockJsonObject.getInt("imageWidth");
                localImageUrl = blockJsonObject.getString("localImageUrl");
                attaOid = blockJsonObject.getString("attaOid");

                auditResult.setTopNum(top);
                auditResult.setLeftNum(left);
                auditResult.setWidthNum(width);
                auditResult.setHeightNum(height);
                auditResult.setImageWidth(imageWidth);
                auditResult.setImageHeight(imageHeight);
                auditResult.setLocalImageUrl(localImageUrl);
                auditResult.setAttaOid(attaOid);


                if (blockJsonObject.has("words")) {
                    words = blockJsonObject.getString("words");
                } else {
                    words = words == null ? "" : words;
                }
            } else {
                words = words == null ? "" : words;
            }
            org.json.JSONArray sealtArray = null;
            if (blockMap.containsKey("sealResultJson" + "_" + materialOid + "_" + refinedMaterialOid)) {
                //印章
                sealResultJson = blockMap.get("sealResultJson" + "_" + materialOid + "_" + refinedMaterialOid);
                if (StringUtils.isNotEmpty(sealResultJson)) {
                    org.json.JSONObject sealResultJsonObject = new org.json.JSONObject(sealResultJson);
                    sealtArray = sealResultJsonObject.getJSONArray("result");
                }
            }
            org.json.JSONArray signtArray = null;
            if (blockMap.containsKey("signResultJson" + "_" + materialOid + "_" + refinedMaterialOid)) {
                //签字
                signResultJson = blockMap.get("signResultJson" + "_" + materialOid + "_" + refinedMaterialOid);
                if (StringUtils.isNotEmpty(signResultJson)) {
                    org.json.JSONObject signlResultJsonObject = new org.json.JSONObject(signResultJson);
                    signtArray = signlResultJsonObject.getJSONArray("result");
                }
            }


            switch (validateType) {
                case FaStaticParam.VALIDATION_TYPE_NOTNULL:
                    memo = name + FaStaticParam.VALIDATION_TYPE.get(FaStaticParam.VALIDATION_TYPE_NOTNULL);
                    auditResult.setMeno(memo);
                    if (StrUtil.isNotBlank(picPath)) { // 图片的非空验证
                        // validResult.put("picPath", picPath);
                        PictureHandle picHandle = new HighShotMeterPictureHandle(picPath);
                        if (!picHandle.isPicBlank()) {
                            auditResult.setValid(FaStaticParam.TRUE_STATUS);
                        } else {
                            auditResult.setValid(FaStaticParam.FLASE_STATUS);
                        }
                        picHandle.releaseMat();
                    } else {
                        auditResult.setWords(words);
                        if (StrUtil.isBlank(words)) {
                            auditResult.setValid(FaStaticParam.FLASE_STATUS);
                        } else {
                            auditResult.setValid(FaStaticParam.TRUE_STATUS);
                        }
                    }
                    break;
                case FaStaticParam.VALIDATION_TYPE_NULL:
                    memo = name + FaStaticParam.VALIDATION_TYPE.get(FaStaticParam.VALIDATION_TYPE_NULL);
                    auditResult.setMeno(memo);
                    if (StrUtil.isNotBlank(picPath)) { // 图片的非空验证
                        //validResult.put("picPath", picPath);
                        PictureHandle picHandle = new HighShotMeterPictureHandle(picPath);
                        if (!picHandle.isPicBlank()) {
                            auditResult.setValid(FaStaticParam.FLASE_STATUS);
                        } else {
                            auditResult.setValid(FaStaticParam.TRUE_STATUS);
                        }
                        picHandle.releaseMat();
                    } else {
                        validResult.put("words", words);
                        if (StrUtil.isNotBlank(words)) {
                            auditResult.setValid(FaStaticParam.FLASE_STATUS);
                        } else {
                            auditResult.setValid(FaStaticParam.TRUE_STATUS);
                        }
                    }
                    break;
                case FaStaticParam.VALIDATION_TYPE_CONTENT:
                    String content = ruleValid.getThanContent();
                    // 如果content为null，设置为空字符串
                    content = content == null ? "" : content;
                    validResult.put("memo", "字符必须为" + content + "！");
                    if (!content.equals(words)) {
                        auditResult.setValid(FaStaticParam.FLASE_STATUS);
                    } else {
                        auditResult.setValid(FaStaticParam.TRUE_STATUS);
                    }
                    break;
                case FaStaticParam.VALIDATION_TYPE_STR_CONTAIN:
                    String containContent = ruleValid.getThanContent();
                    // 如果containContent为null，设置为空字符串
                    containContent = containContent == null ? "" : containContent;
                    validResult.put("memo", "字符必须包含为" + containContent + "！");
                    if (!words.contains(containContent)) {
                        auditResult.setValid(FaStaticParam.FLASE_STATUS);
                    } else {
                        auditResult.setValid(FaStaticParam.TRUE_STATUS);
                    }
                    break;
                case FaStaticParam.VALIDATION_TYPE_RANGE:
                    String contentDown = ruleValid.getContentDown();
                    String contentUp = ruleValid.getContentUp();
                    // 如果containContent为null，设置为空字符串
                    contentDown = contentDown == null ? "" : contentDown;
                    contentUp = contentUp == null ? "" : contentUp;
                    try {
                        validResult.put("memo", "数字范围必须在" + contentDown + "和" + contentUp + "之间！");
                        if (StrUtil.isBlank(words)) {
                            auditResult.setValid(FaStaticParam.FLASE_STATUS);
                            auditResult.setMeno("字符不能为空，数字范围必须在" + contentDown + "和" + contentUp + "之间！");
                            break;
                        }

                        Long cDown = StrUtil.isNotBlank(contentDown) ? Long.parseLong(contentDown) : null;
                        Long cUp = StrUtil.isNotBlank(contentUp) ? Long.parseLong(contentUp) : null;
                        Long c = StrUtil.isNotBlank(words) ? Long.parseLong(words) : null;

                        if (cDown != null && c >= cDown) {
                            if (cUp != null && c <= cUp) {
                                auditResult.setValid(FaStaticParam.TRUE_STATUS);
                            } else {
                                auditResult.setValid(FaStaticParam.FLASE_STATUS);
                            }
                        } else {
                            auditResult.setValid(FaStaticParam.FLASE_STATUS);
                        }
                    } catch (Exception e) {
                        auditResult.setValid(FaStaticParam.FLASE_STATUS);
                        auditResult.setMeno("字符必须是数字");
                    }

                    break;
                case FaStaticParam.VALIDATION_TYPE_DATE_COMPARE:
                    String contentDateFormat = ruleValid.getContentDateFormat();
                    /* String contentDateValue = ruleValid.getContentDateValue();*/
                    memo = "日期比较";
                    Boolean valid = true;
                    String contentBeforeDateValue = ruleValid.getContentBeforeDateValue();
                    String contentAfterDateValue = ruleValid.getContentAfterDateValue();
                    String noMoreThanAge = ruleValid.getNoMoreThanAge();//不超过年龄
                    String noLessThanAge = ruleValid.getNoLessThanAge();
                    String biforeNowDayValue = ruleValid.getBiforeNowDayValue();
                    String afterNowDayValue = ruleValid.getAfterNowDayValue();
                    Date beforecompareDate = new Date();
                    Date aftercompareDate = new Date();
                    try {
                        if (StrUtil.isBlank(words)) {
                            auditResult.setValid(FaStaticParam.FLASE_STATUS);
                            auditResult.setMeno("字符不能为空");
                            break;
                        }
                        // 如果containContent为null，设置为空字符串
                        contentDateFormat = contentDateFormat == null ? "" : contentDateFormat;
                        //ocr取值
                        /*  words=words.replaceAll("年","").replaceAll("月","").replaceAll("日","");*/
                        Date wDate = DateUtil.parse(words, contentDateFormat);

                        //小于
                        if (StrUtil.isNotBlank(contentBeforeDateValue)) {
                            contentBeforeDateValue = contentBeforeDateValue == null ? "" : contentBeforeDateValue;
                            if (!"CURRENT_DATE".equals(contentBeforeDateValue.toUpperCase())) {
                                memo = memo + "日期需要小于" + contentBeforeDateValue;
                                //将验证字符串转化为日期类型进行比对
                                beforecompareDate = DateUtil.parse(contentBeforeDateValue, contentDateFormat);
                            } else {
                                memo = memo + "日期需要小于当前时间";
                                beforecompareDate = new Date();
                            }
                            if (wDate.before(beforecompareDate)) {
                                valid = true;
                            } else {
                                valid = false;
                            }
                        }
                        if (valid) {//审核通过继续审核不通过直接审核不通过
                            if (StrUtil.isNotBlank(contentAfterDateValue)) {
                                contentAfterDateValue = contentAfterDateValue == null ? "" : contentAfterDateValue;
                                if (!"CURRENT_DATE".equals(contentAfterDateValue.toUpperCase())) {
                                    memo = memo + "日期需要大于" + contentAfterDateValue;
                                    aftercompareDate = DateUtil.parse(contentAfterDateValue, contentDateFormat);
                                } else {
                                    memo = memo + "日期需要大于当前时间";
                                    aftercompareDate = new Date();
                                }
                                //将验证字符串转化为日期类型进行比对
                                if (wDate.after(aftercompareDate)) {
                                    valid = true;
                                } else {
                                    valid = false;
                                }
                            }
                        }
                        if (valid) {//审核通过继续审核不通过直接审核不通过
                            if (StrUtil.isNotBlank(noMoreThanAge)) {
                                int age = getAgeByBirth(wDate);//获取年纪
                                int noMoreThanAges = Integer.parseInt(noMoreThanAge);
                                if (age <= noMoreThanAges) {
                                    valid = true;
                                } else {
                                    valid = false;
                                }
                            }

                        }

                        if (valid) {//审核通过继续审核不通过直接审核不通过
                            if (StrUtil.isNotBlank(noLessThanAge)) {
                                int age = getAgeByBirth(wDate);//获取年纪
                                int noLessThanAges = Integer.parseInt(noLessThanAge);
                                if (age >= noLessThanAges) {
                                    valid = true;
                                } else {
                                    valid = false;
                                }
                            }

                        }
                        auditResult.setMeno(memo);
                        if (valid) {
                            auditResult.setValid(FaStaticParam.TRUE_STATUS);
                        } else {
                            auditResult.setValid(FaStaticParam.FLASE_STATUS);
                        }
                        //validResult.put("memo", "日期比较，日期格式：" + contentDateFormat + "，比对日期：" + DateUtil.format(compareDate, contentDateFormat) + "！");

                    } catch (Exception e) {
                        auditResult.setValid(FaStaticParam.FLASE_STATUS);
                        auditResult.setMeno("字符无法转化为日期");
                    }
                    break;
                case FaStaticParam.VALIDATION_TYPE_CONTENT_THAN:
                    String thancontetKey = ruleValid.getThanTemplateMetadataCode() + "_" + ruleValid.getThanSxMaterialOid() + "_" + ruleValid.getThanRefinedMaterialOid();
                    String thanContent = "";
                    if (blockMapResult.containsKey(thancontetKey)) {
                        org.json.JSONObject blockJsonObject = blockMapResult.get(thancontetKey);
                        if (blockJsonObject.has("words")) {
                            thanContent = blockJsonObject.getString("words");
                        } else {
                            thanContent = thanContent == null ? "" : thanContent;
                        }
                    } else {
                        thanContent = thanContent == null ? "" : thanContent;
                    }
                    ApiResultSet<MaterialCatalog> catalog = materialCatalogService.getMaterialCatalogByOid(ruleValid.getThanCatalogOid());
                    MaterialCatalog thanCatalog = null;
                    if (catalog != null && catalog.getData() != null) {
                        thanCatalog = catalog.getData();
                    }
                    // 如果thanContent为null, 比较元素不存在（对应材料不需要上传情况）不进行比较
                    if (thanContent == null) {
                        auditResult.setValid(FaStaticParam.TRUE_STATUS);
                    } else {
                        // 如果thanContent为null，设置为空字符串
                        //thanContent = thanContent == null ? "" : thanContent;
                        String meno = "";
                        if (StringUtils.isNotEmpty(ruleValid.getRuleType())) {
                            if (ruleValid.getRuleType().equals("DZBD")) {
                                meno = ruleValid.getTemplateMetadataName() + "[" + ruleValid.getCatalogName() + "]与电子表单" + ruleValid.getThanTemplateMetadataName() + "]的值必须相同";
                            } else if (ruleValid.getRuleType().equals("JCBD")) {
                                meno = ruleValid.getTemplateMetadataName() + "[" + ruleValid.getCatalogName() + "]与基础表单" + ruleValid.getThanTemplateMetadataName() + "]的值必须相同";
                            } else if (ruleValid.getRuleType().equals("DZZZ")) {
                                meno = ruleValid.getTemplateMetadataName() + "[" + ruleValid.getCatalogName() + "]与电子证照" + ruleValid.getThanTemplateMetadataName() + "]的值必须相同";
                            }
                        } else {
                            meno = ruleValid.getTemplateMetadataName() + "[" + ruleValid.getCatalogName() + "]与"
                                    + ruleValid.getThanTemplateMetadataName() + "["
                                    + thanCatalog.getCatalogName() + "]必须相同";
                        }

                        auditResult.setMeno(meno);
                        if (StringUtils.isNotEmpty(thanContent) || StringUtils.isNotEmpty(words)) {
                            if (!thanContent.equals(words)) {
                                auditResult.setValid(FaStaticParam.FLASE_STATUS);
                            } else {
                                auditResult.setValid(FaStaticParam.TRUE_STATUS);
                            }
                        } else {
                            auditResult.setValid(FaStaticParam.FLASE_STATUS);
                        }
                    }
                    break;
                case FaStaticParam.VALIDATION_TYPE_SIMILARITY:
                    /* Double similar = ruleValid.getSimilar();*/
                    String similarold = ruleValid.getSimilar();
                    Double similar = Double.parseDouble(similarold);
                    validResult.put("memo",
                            ruleValid.getTemplateMetadataName() + "[" + ruleValid.getTemplateMetadataCode() + "]与"
                                    + ruleValid.getThanTemplateMetadataName() + "["
                                    + ruleValid.getThanTemplateMetadataCode() + "]的相识度必须大于" + similar + "%！");
                    // 相似度比对
                    if (StrUtil.isBlank(picPath)) {
                        auditResult.setValid(FaStaticParam.FLASE_STATUS);
                        break;
                    }
                    String thanPicPath = blockMap.get(ruleValid.getThanTemplateMetadataCode() + "_" + materialOid + "-pic");
                    if (StrUtil.isBlank(thanPicPath)) {
                        auditResult.setValid(FaStaticParam.FLASE_STATUS);
                        break;
                    }
                    if (OpenCVImageHandle.similarCalc(picPath, thanPicPath) < similar) {
                        auditResult.setValid(FaStaticParam.FLASE_STATUS);
                    } else {
                        auditResult.setValid(FaStaticParam.TRUE_STATUS);
                    }
                    break;
                case FaStaticParam.VALIDATION_TYPE_HAND_SIGN:
                    memo = name + "必须为手写签字";
                    if (signtArray == null || signtArray.length() <= 0) {
                        auditResult.setMeno(memo);
                        auditResult.setValid(FaStaticParam.FLASE_STATUS);
                        break;
                    } else {
                        Boolean sealOrNOt = false;
                        for (int i = 0; i < signtArray.length(); i++) {
                            if (!sealOrNOt) {
                                org.json.JSONObject seal = (org.json.JSONObject) signtArray.get(i);
                                //研究院检测取值
                                int x = new Double((Double) seal.get("x")).intValue();
                                int y = new Double((Double) seal.get("y")).intValue();
                                int w = new Double((Double) seal.get("w")).intValue();
                                int h = new Double((Double) seal.get("h")).intValue();
                                Rect rect1 = new Rect(top, left, width, height);//ocr位置
                                Rect rect2 = new Rect(x, y, w, h);//研究位置取值
                                sealOrNOt = this.checkPosition(rect1, rect2);

                            }
                        }
                        auditResult.setMeno(memo);
                        if (sealOrNOt) {
                            auditResult.setValid(FaStaticParam.TRUE_STATUS);
                        } else {
                            auditResult.setValid(FaStaticParam.FLASE_STATUS);
                        }
                    }
                    break;
                case FaStaticParam.VALIDATION_TYPE_SEAL:
                    memo = "必须盖章";
                    if (sealtArray == null || sealtArray.length() <= 0) {
                        auditResult.setValid(FaStaticParam.FLASE_STATUS);
                        auditResult.setMeno(memo);
                        break;
                    } else {
                        Boolean sealOrNOt = false;
                        for (int i = 0; i < sealtArray.length(); i++) {
                            if (!sealOrNOt) {
                                org.json.JSONObject seal = (org.json.JSONObject) sealtArray.get(i);
                                //研究院检测取值
                                int x = new Double((Double) seal.get("x")).intValue();
                                int y = new Double((Double) seal.get("y")).intValue();
                                int w = new Double((Double) seal.get("w")).intValue();
                                int h = new Double((Double) seal.get("h")).intValue();
                                Rect rect1 = new Rect(top, left, width, height);//ocr位置
                                Rect rect2 = new Rect(x, y, w, h);//研究位置取值
                                sealOrNOt = this.checkPosition(rect1, rect2);
                               /* org.json.JSONObject seal= (org.json.JSONObject) sealtArray.get(i);
                                double x= (double) seal.get("x");//左侧坐标占比
                                double y= (double) seal.get("y");//上侧坐标占比
                                double w= (double) seal.get("w");//宽占比
                                double h= (double) seal.get("h");//高左边占比

                                //百度ocr取值
                                double leftdouble=Double.valueOf(left) ;//左侧坐标取值
                                double topdouble=Double.valueOf(top) ;//左侧坐标取值
                                double Heightdouble=Double.valueOf(height) ;//宽取值
                                double widthdouble=Double.valueOf(width) ;//宽取值

                                //百度ocr取值占图片的百分比  imageWidth图片整体宽度 imageHeight图片整体高度
                                double leftbl=leftdouble/Double.valueOf(imageWidth);//左侧坐标占比
                                double topbl=topdouble/Double.valueOf(imageHeight);//上侧坐标占比
                                double widthbl=widthdouble/Double.valueOf(imageWidth);//宽占比
                                double heighbl=Heightdouble/Double.valueOf(imageHeight);//高左边占比
                                //研究院上侧坐标在百度ocr左侧坐标取值加减高取值范围内

                                double leftzd=leftbl+widthbl;
                                double leftzx=leftbl-widthbl;
                                double topzD=topbl+heighbl;
                                double topzx=topbl-heighbl;
                                if((leftbl+widthbl>x&&leftbl-widthbl<x) && (topbl+heighbl>y&&topbl-heighbl<y)){
                                    sealOrNOt=true;
                                }*/
                            }
                        }
                        auditResult.setMeno(memo);
                        if (sealOrNOt) {
                            auditResult.setValid(FaStaticParam.TRUE_STATUS);
                        } else {
                            auditResult.setValid(FaStaticParam.FLASE_STATUS);
                        }

                    }
                    break;
                case FaStaticParam.VALIDATION_TYPE_MANUAL_AUDIT:
                    memo = name + "需人工核验";
                    auditResult.setValid(FaStaticParam.ARTIFICAL_STATUS);
                    auditResult.setMeno(memo);
                    break;
                default:
                    break;
            }

            auditResultManager.saveOrUpdateAuditResult(auditResult);
            auditResultList.add(auditResult);
            //validResultArray.add(validResult);
        }
        return auditResultList;
    }

    /***
     * @Description:根据时间获取年龄
     * @Author:liangss
     * @Date:2021/8/3
     * @Param: [birthDay]
     */
    public int getAgeByBirth(Date birthDay) {
        int age = 0;
        Calendar cal = Calendar.getInstance();
        if (cal.before(birthDay)) { //出生日期晚于当前时间，无法计算
            throw new IllegalArgumentException(
                    "The birthDay is before Now.It's unbelievable!");
        }
        int yearNow = cal.get(Calendar.YEAR);  //当前年份
        int monthNow = cal.get(Calendar.MONTH);  //当前月份
        int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH); //当前日期
        cal.setTime(birthDay);
        int yearBirth = cal.get(Calendar.YEAR);
        int monthBirth = cal.get(Calendar.MONTH);
        int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);
        age = yearNow - yearBirth;   //计算整岁数
        if (monthNow <= monthBirth) {
            if (monthNow == monthBirth) {
                if (dayOfMonthNow < dayOfMonthBirth) {
                    age--;
                }//当前日期在生日之前，年龄减一
            } else {
                age--;//当前月份在生日之前，年龄减一
            }
        }
        return age;
    }


    /***
     * @Description: 审核结果详细
     * @Author:liangss
     * @Date:2021/7/22
     * @Param: [caseOid, caseMaterialOid, materialOid, serviceOid]
     */
    public Map<String, Object> viewDetailResult(
            String caseOid,
            String caseMaterialOid,
            String materialOid,
            String serviceOid) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();

        //根据材料oid获取办件材料
        ApiResultSet<QlCaseMaterial> qlCaseMaterialApiResultSet = qlCaseMaterialServiceService.queryMaterialByCaseMaterialOid(caseMaterialOid);
        QlCaseMaterial qlCaseMaterial = null;
        if (null != qlCaseMaterialApiResultSet.getData()) {
            qlCaseMaterial = qlCaseMaterialApiResultSet.getData();
        }
        //精细化材料
        ApiResultSet<List<RefinedMaterial>> listApiResultSet = refinedMaterialService.getRefinedMaterialListByMaterialOid(qlCaseMaterial.getMaterialOid());
        List<RefinedMaterial> refinedMaterialList = null;
        if (String.valueOf(listApiResultSet.getCode()).equals(FaStaticParam.HTTP_REQUEST_CODE_SUCCESS)) {
            refinedMaterialList = listApiResultSet.getData();
        }

        //根据
        Map<String, String> qlSysAttaMap = new HashMap<>();
        //附件关联信息
        Map<String, QlCaseMaterialAtta> qlCaseMaterialAttaMap = new HashMap<>();
        ApiResultSet<List<QlCaseMaterialAtta>> qlCaseMaterialAttaResult = qlCaseMaterialAttaServiceService.queryQlCaseMaterialAttaByCaseMaterialOid(caseMaterialOid);
        List<QlCaseMaterialAtta> qlCaseMaterialAttaList = null;
        if (String.valueOf(qlCaseMaterialAttaResult.getCode()).equals(FaStaticParam.HTTP_REQUEST_CODE_SUCCESS)) {
            qlCaseMaterialAttaList = qlCaseMaterialAttaResult.getData();
            if (qlCaseMaterialAttaList.size() > 0) {
                for (QlCaseMaterialAtta qlCaseMaterialAtta : qlCaseMaterialAttaList) {
                    if (StringUtils.isNotEmpty(qlCaseMaterialAtta.getRefinedMaterialOid())) {
                        qlCaseMaterialAttaMap.put(qlCaseMaterialAtta.getRefinedMaterialOid(), qlCaseMaterialAtta);
                    }
                    //附件信息
                    String attaOid = qlCaseMaterialAtta.getAttaOid();
                    if (StringUtils.isNotEmpty(attaOid)) {
                        ApiResultSet<SysAtta> sysAttaresult = sysAttaService.getSysAttaByAttaOid(attaOid);
                        if (null != sysAttaresult.getData()) {
                            SysAtta qlSysAtta = sysAttaresult.getData();
                            String fastdfsNginxUrl = qlSysAtta.getFastdfsNginxUrl();
                            if (StringUtils.isNotEmpty(qlCaseMaterialAtta.getRefinedMaterialOid())) {
                                qlSysAttaMap.put(qlCaseMaterialAtta.getRefinedMaterialOid(), fastdfsNginxUrl);
                            }
                            qlCaseMaterialAtta.setSrc(fastdfsNginxUrl);
                        }
                    }
                }
            }
        }

        qlCaseMaterial.setQlCaseMaterialAttaList(qlCaseMaterialAttaList);

        //获取审核结果
        AuditResult auditResult = new AuditResult();
        auditResult.setCaseOid(caseOid);
        List<AuditResult> auditResultList = auditResultManager.queryAuditResultList(auditResult);

        Map<String, Map<String, Object>> preResult;
        String resultStatus = FaStaticParam.PRE_TRIAL_RESULT_STATUS_PASS;

        List<PreDetailTrialResultVo> preDetailTrialResultVoList = new ArrayList<>();

        refinedMaterialFor:
        for (RefinedMaterial refinedMaterial : refinedMaterialList) {
            //不通过结果列表
            List<Map<String, Object>> notPassList = new ArrayList<Map<String, Object>>();
            //通过结果列表
            List<Map<String, Object>> passList = new ArrayList<Map<String, Object>>();
            //人工审核列表
            List<Map<String, Object>> manualAuditList = new ArrayList<Map<String, Object>>();
            PreDetailTrialResultVo preDetailTrialResultVo = new PreDetailTrialResultVo();

            String materialSampleOid = refinedMaterial.getMaterialSampleOid();
            String materialSampleAddr = null;
            if (StringUtils.isNotEmpty(materialSampleOid)) {
                ApiResultSet<SxSysAtta> sxSysAttaApiResultSet = sxServiceAttaFeignService.getSxSysAttaByOId(materialSampleOid);
                if (null != sxSysAttaApiResultSet) {
                    SxSysAtta sxSysAtta = sxSysAttaApiResultSet.getData();
                    //log.info("样本附件地址:" + sxSysAtta.getFilePath());
                    if (sxSysAtta.getFilePath().contains("http")) {
                        materialSampleAddr = sxSysAtta.getFilePath();
                    }
                    //
                }

            }
            refinedMaterial.setMaterialSampleAddr(materialSampleAddr);
            //样表图地址
            preDetailTrialResultVo.setMaterialSampleAddr(materialSampleAddr);
            //审核要点
            Map<String, Object> map1 = new HashMap<String, Object>();
            ApiResultSet<List<ReviewPoints>> reviewPointsResultSet = reviewPointsService.getReviewPointsListByRefinedMaterialOid(refinedMaterial.getOid());
            List<ReviewPoints> reviewPointsList = null;
            if (null != reviewPointsResultSet) {
                reviewPointsList = reviewPointsResultSet.getData();
                if (null != reviewPointsList && reviewPointsList.size() > 0) {
                    for (ReviewPoints reviewPoints : reviewPointsList) {
                        map1.put(reviewPoints.getOid(), reviewPoints);
                    }

                }
            }
            //保存审核要点
            preDetailTrialResultVo.setReviewPointsList(reviewPointsList);


            String refinedMaterialOid = refinedMaterial.getOid();
            String materialCatalogOid = refinedMaterial.getMaterialCatalogOid();

            preDetailTrialResultVo.setRefinedMaterialName(refinedMaterial.getRefinedMaterialName());
            preDetailTrialResultVo.setRefinedMaterialOid(refinedMaterialOid);
            preDetailTrialResultVo.setMaterialOid(refinedMaterial.getMaterialOid());
            preDetailTrialResultVo.setSerialNumber(refinedMaterial.getSerialNumber());

            if (StringUtils.isEmpty(materialCatalogOid)) {
                //无关联智审目录
                resultStatus = FaStaticParam.EXAMINEL_RESULT_STATUS_NOT_AUDIT;
                preDetailTrialResultVo.setResultStatus(resultStatus);
                preDetailTrialResultVo.setLocalAuditImageUrl(qlSysAttaMap.get(refinedMaterialOid));
                preDetailTrialResultVoList.add(preDetailTrialResultVo);
                refinedMaterial.setResultStatus(resultStatus);
                continue refinedMaterialFor;
            }
            //未上传附件
            QlCaseMaterialAtta qlCaseMaterialAtta = qlCaseMaterialAttaMap.get(refinedMaterialOid);
            if (null == qlCaseMaterialAtta) {
                resultStatus = FaStaticParam.EXAMINEL_RESULT_STATUS_NOT_AUDIT;
                preDetailTrialResultVo.setResultStatus(resultStatus);
                preDetailTrialResultVoList.add(preDetailTrialResultVo);
                refinedMaterial.setResultStatus(resultStatus);
                continue refinedMaterialFor;
            }
            //上传的具体附件信息
            ApiResultSet<SysAtta> sysAttaresult = sysAttaService.getSysAttaByAttaOid(qlCaseMaterialAtta.getAttaOid());
            SysAtta qlSysAtta = sysAttaresult.getData();
            if (null != qlSysAtta) {
                preDetailTrialResultVo.setUploadURL(qlSysAtta.getFastdfsNginxUrl());
            }
            //ocr结果查询
            FaMaterialPicOcrResult faMaterialPicOcrResult = new FaMaterialPicOcrResult();
            faMaterialPicOcrResult.setUuid(caseOid);
            faMaterialPicOcrResult.setAttaOid(qlCaseMaterialAtta.getAttaOid());
            if (StringUtils.isNotEmpty(caseMaterialOid)) {
                faMaterialPicOcrResult.setCaseMaterialOid(caseMaterialOid);
            }
            DbFaMaterialPicOcrResult dbFaMaterialPicOcrResult = faMaterialPicOcrResultManager.getFaMaterialPicOcrResultByFaMaterialPicOcrResult(faMaterialPicOcrResult);
            if (null == dbFaMaterialPicOcrResult) {
                resultStatus = FaStaticParam.PRE_TRIAL_RESULT_STATUS_NOT_RESULT;
                preDetailTrialResultVo.setResultStatus(resultStatus);
                preDetailTrialResultVoList.add(preDetailTrialResultVo);
                preDetailTrialResultVo.setLocalAuditImageUrl(qlSysAtta.getFastdfsNginxUrl());
                refinedMaterial.setResultStatus(resultStatus);
                continue refinedMaterialFor;
            }

            String localImageUrl = "";
            String attaOid = "";
            //根据ocr结果判断
            if (null != auditResultList && auditResultList.size() > 0) {
                for (int i = 0; i < auditResultList.size(); i++) {
                    //不通过结果
                    JSONObject notPassresultObj = new JSONObject();
                    //通过结果
                    JSONObject passresultObj = new JSONObject();
                    //人工审核
                    JSONObject manualAuditresultObj = new JSONObject();

                    //通用结果
                    JSONObject resultObj = new JSONObject();
                    AuditResult auditResult1 = auditResultList.get(i);
                    String auditPointStatus = auditResult1.getAuditPointStatus();
                    if (materialCatalogOid.equals(auditResult1.getCatalogOid()) && refinedMaterialOid.equals(auditResult1.getRefinedMaterialOid())) {
                        localImageUrl = auditResult1.getLocalImageUrl();
                        attaOid = auditResult1.getAttaOid();

                        //结果
                        resultObj.put("auditResultOid", auditResult1.getOid());
                        resultObj.put("isIgnore", auditResult1.getIsIgnore());
                        resultObj.put("name", auditResult1.getName());
                        resultObj.put("valid", auditResult1.getValid());
                        resultObj.put("memo", auditResult1.getMeno());
                        resultObj.put("top", auditResult1.getTopNum());
                        resultObj.put("left", auditResult1.getLeftNum());
                        resultObj.put("width", auditResult1.getWidthNum());
                        resultObj.put("height", auditResult1.getHeightNum());
                        resultObj.put("auditPointStatus", auditResult1.getAuditPointStatus());
                        String reviewPointOid = auditResult1.getReviewPointsOid();
                        resultObj.put("reviewPointOid", reviewPointOid);
                        ReviewPoints reviewPoints = (ReviewPoints) map1.get(reviewPointOid);
                        if (null != reviewPoints) {
                            resultObj.put("reviewPoints", reviewPoints);
                            resultObj.put("reviewPointsName", reviewPoints.getReviewPoints());
                            resultObj.put("serialNumber", reviewPoints.getSerialNumber());
                        } else {
                            resultObj.put("reviewPointsName", auditResult1.getMeno());
                            resultObj.put("serialNumber", new Long((long) 2));
                        }

                      /*  if(auditPointStatus.equals("3")){//人工审核
                            manualAuditresultObj=resultObj;
                            manualAuditresultObj.put("resultStatus",FaStaticParam.PRE_TRIAL_RESULT_STATUS_NOT_PASS);
                            manualAuditList.add(manualAuditresultObj);
                        }else{*/
                        // 验证结果不通过，设置审核标识为1：不通过
                        if (auditResult1.getValid() != null && auditResult1.getValid() == FaStaticParam.FLASE_STATUS) {
                            notPassresultObj = resultObj;
                            resultStatus = FaStaticParam.PRE_TRIAL_RESULT_STATUS_NOT_PASS;
                            notPassresultObj.put("resultStatus", FaStaticParam.PRE_TRIAL_RESULT_STATUS_NOT_PASS);
                            String notPassreviewPointsName = (String) notPassresultObj.get("reviewPointsName");
                            if (auditPointStatus.equals("3")) {
                                notPassreviewPointsName = notPassreviewPointsName + "(需人工核验)";
                                notPassresultObj.put("reviewPointsName", notPassreviewPointsName);
                            }
                            notPassList.add(notPassresultObj);
                        } else if (auditResult1.getValid() != null && auditResult1.getValid() == FaStaticParam.TRUE_STATUS) {
                            if (auditPointStatus.equals("3")) {//人工审核
                                manualAuditresultObj = resultObj;
                                manualAuditresultObj.put("resultStatus", FaStaticParam.PRE_TRIAL_RESULT_STATUS_NEED_MANUAL_AUDIT);
                                manualAuditList.add(manualAuditresultObj);
                            } else {
                                passresultObj = resultObj;
                                passresultObj.put("resultStatus", FaStaticParam.PRE_TRIAL_RESULT_STATUS_PASS);
                                passList.add(passresultObj);
                            }
                        } else if (auditResult1.getValid() != null && auditResult1.getValid() == FaStaticParam.ARTIFICAL_STATUS) {
                            manualAuditresultObj = resultObj;
                            manualAuditresultObj.put("resultStatus", FaStaticParam.PRE_TRIAL_RESULT_STATUS_NOT_PASS);
                            manualAuditList.add(manualAuditresultObj);

                        }
                        /* }*/

                    }
                }
            }
            preDetailTrialResultVo.setResultStatus(resultStatus);
            preDetailTrialResultVo.setLocalImageUrl(localImageUrl);
            preDetailTrialResultVo.setAttaOid(attaOid);

            preDetailTrialResultVo.setNotPassList(notPassList);
            preDetailTrialResultVo.setPassList(passList);
            preDetailTrialResultVo.setManualAuditList(manualAuditList);

           /* String localAuditImageUrl=getLocalAuditImageUrl(preDetailTrialResultVo.getUploadURL(),preDetailTrialResultVo);
            preDetailTrialResultVo.setLocalAuditImageUrl(localAuditImageUrl);*/
            if (notPassList.size() == 0 && passList.size() == 0 && manualAuditList.size() == 0) {
                preDetailTrialResultVo.setLocalAuditImageUrl(preDetailTrialResultVo.getUploadURL());
            } else {
                String localAuditImageUrl = getLocalAuditImageUrl(preDetailTrialResultVo.getUploadURL(), preDetailTrialResultVo);
                preDetailTrialResultVo.setLocalAuditImageUrl(localAuditImageUrl);
            }


            preDetailTrialResultVoList.add(preDetailTrialResultVo);

            if (notPassList.size() > 0) {
                resultStatus = FaStaticParam.PRE_TRIAL_RESULT_STATUS_NOT_PASS;
                refinedMaterial.setResultStatus(resultStatus);
            } else {
                resultStatus = FaStaticParam.PRE_TRIAL_RESULT_STATUS_PASS;
                refinedMaterial.setResultStatus(resultStatus);
            }
        }
        qlCaseMaterial.setRefinedMaterialList(refinedMaterialList);

        map.put("qlCaseMaterial", qlCaseMaterial);
        map.put("preDetailTrialResultVoList", preDetailTrialResultVoList);
        return map;
    }

    /**
     * 根据材料id查看材料审查要点信息
     *
     * @param materialOid
     * @return
     * @throws Exception
     */
    public Map<String, Object> viewDetailRefinedMaterial(String materialOid) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();

        SxServiceMaterial material = null;
        ApiResultSet<SxServiceMaterial> materialResultSet = sxServiceMaterialService.getSxServiceMaterialByOid(materialOid);
        if (String.valueOf(materialResultSet.getCode()).equals(FaStaticParam.HTTP_REQUEST_CODE_SUCCESS)) {
            material = materialResultSet.getData();
        }
        if (material == null) {
            throw new ResultInfoException("获取事项材料信息异常！");
        }

        // 材料信息封装
        QlCaseMaterial qlCaseMaterial = new QlCaseMaterial();
        qlCaseMaterial.setMaterialOid(material.getMaterialOid());
        qlCaseMaterial.setMaterialName(material.getMaterialName());
        qlCaseMaterial.setMaterialSampleAddr(material.getMaterialSampleAddr());
        qlCaseMaterial.setMaterialSampleAddrYl(material.getMaterialSampleAddrYl());
        qlCaseMaterial.setMaterialCatalogOid(material.getMaterialCatalogOid());

        //精细化材料
        ApiResultSet<List<RefinedMaterial>> listApiResultSet = refinedMaterialService.getRefinedMaterialListByMaterialOid(materialOid);
        List<RefinedMaterial> refinedMaterialList = null;
        if (String.valueOf(listApiResultSet.getCode()).equals(FaStaticParam.HTTP_REQUEST_CODE_SUCCESS)) {
            refinedMaterialList = listApiResultSet.getData();
        }

        List<PreDetailTrialResultVo> preDetailTrialResultVoList = new ArrayList<>();

        refinedMaterialFor:
        for (RefinedMaterial refinedMaterial : refinedMaterialList) {
            PreDetailTrialResultVo preDetailTrialResultVo = new PreDetailTrialResultVo();

            String materialSampleOid = refinedMaterial.getMaterialSampleOid();
            String materialSampleAddr = null;
            if (StringUtils.isNotEmpty(materialSampleOid)) {
                ApiResultSet<SxSysAtta> sxSysAttaApiResultSet = sxServiceAttaFeignService.getSxSysAttaByOId(materialSampleOid);
                if (null != sxSysAttaApiResultSet) {
                    SxSysAtta sxSysAtta = sxSysAttaApiResultSet.getData();
                    //log.info("样本附件地址:" + sxSysAtta.getFilePath());
                    if (sxSysAtta.getFilePath().contains("http")) {
                        materialSampleAddr = sxSysAtta.getFilePath();
                    }
                }
            }
            refinedMaterial.setMaterialSampleAddr(materialSampleAddr);
            //样表图地址
            preDetailTrialResultVo.setMaterialSampleAddr(materialSampleAddr);
            //审核要点
            ApiResultSet<List<ReviewPoints>> reviewPointsResultSet = reviewPointsService.getReviewPointsListByRefinedMaterialOid(refinedMaterial.getOid());
            List<ReviewPoints> reviewPointsList = null;
            if (null != reviewPointsResultSet) {
                reviewPointsList = reviewPointsResultSet.getData();
            }
            //保存审核要点
            preDetailTrialResultVo.setReviewPointsList(reviewPointsList);

            String refinedMaterialOid = refinedMaterial.getOid();
            preDetailTrialResultVo.setRefinedMaterialName(refinedMaterial.getRefinedMaterialName());
            preDetailTrialResultVo.setRefinedMaterialOid(refinedMaterialOid);
            preDetailTrialResultVo.setMaterialOid(refinedMaterial.getMaterialOid());
            preDetailTrialResultVo.setSerialNumber(refinedMaterial.getSerialNumber());

            String resultStatus = FaStaticParam.EXAMINEL_RESULT_STATUS_NOT_AUDIT;
            preDetailTrialResultVo.setResultStatus(resultStatus);
            preDetailTrialResultVoList.add(preDetailTrialResultVo);
            refinedMaterial.setResultStatus(resultStatus);
        }
        qlCaseMaterial.setRefinedMaterialList(refinedMaterialList);

        map.put("qlCaseMaterial", qlCaseMaterial);
        map.put("preDetailTrialResultVoList", preDetailTrialResultVoList);
        return map;
    }


    /**
     * 根据审核结果画图
     *
     * @param localImageUrl
     * @param preDetailTrialResultVo
     * @return
     * @throws Exception
     */
    public String getLocalAuditImageUrl(String localImageUrl, PreDetailTrialResultVo preDetailTrialResultVo) throws Exception {
        String localAuditImageUrl = "";
        if (StringUtils.isNotEmpty(localImageUrl)) {
            String attaOid = preDetailTrialResultVo.getAttaOid();
            String fileUrl = "";
            if (CommonUtil.isWindows()) {
                fileUrl = "D:\\commonservice\\image\\";
            } else {
                fileUrl = "/soft/commonservice/image/";
            }
            String picPath = fileUrl + attaOid + "_UOLOAD" + ".jpg";
            File dir = new File(fileUrl);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File file = new File(picPath);
            if (!file.exists()) {
                //复制文件系统中的附件到本地系统
                HttpUtil.downloadFile(localImageUrl, cn.hutool.core.io.FileUtil.file(picPath));
            }
            String name = attaOid + "_Audit" + ".jpg";
            localAuditImageUrl = fileUrl + name;

            BufferedImage image = ImageIO.read(new File(picPath));
            //不通过结果列表
            List<Map<String, Object>> notPassList = preDetailTrialResultVo.getNotPassList();
            //通过结果列表
            List<Map<String, Object>> passList = preDetailTrialResultVo.getPassList();
            //人工审核列表
            List<Map<String, Object>> manualAuditList = preDetailTrialResultVo.getManualAuditList();
            Graphics g;

            List<String> xyAll = new ArrayList<>();
            for (Map<String, Object> objectMap : passList) {
                Long serialNumber = (Long) objectMap.get("serialNumber");
                int x = (int) objectMap.get("left");
                int y = (int) objectMap.get("top");
                int width = (int) objectMap.get("width");
                int height = (int) objectMap.get("height");
                String reviewPointsName = (String) objectMap.get("reviewPointsName");

                String auditPointStatus = (String) objectMap.get("auditPointStatus");
                g = image.getGraphics();
                if (auditPointStatus.equals("2")) {
                    g.setColor(Color.YELLOW);//画笔颜色
                } else {
                    g.setColor(Color.GREEN);//画笔颜色
                }
                // 设置线的粗细
                BasicStroke stokeLine = new BasicStroke(6.0f);
                Graphics2D g2 = (Graphics2D) g;
                g2.setStroke(stokeLine);
                // 坐标+尺寸
                g.drawRect(x, y, width, height);//矩形框(原点x坐标，原点y坐标，矩形的长，矩形的宽)
                if (serialNumber > 20) {
                    g.setFont(new Font("", Font.BOLD, 60));
                } else {
                    g.setFont(new Font("calibri", Font.BOLD, 60));
                }
                // g.setFont(new Font("", Font.BOLD, 60));//设置字体样式(3个参数分别表示：字体名称，字体样式，字体大小)
                String num = FaStaticParam.SERNUM_MAP.get(Long.toString(serialNumber));
                String xy = x + "-" + y;
                int count = Collections.frequency(xyAll, xy);
                xyAll.add(xy);
                if (count > 0) {
                    x = x + 60 * count;
                }
                g.drawString(num, x, y - 10);
                //g.drawString(num, x, y-10);
            }

            for (Map<String, Object> objectMap : notPassList) {
                Long serialNumber = (Long) objectMap.get("serialNumber");
                int x = (int) objectMap.get("left");
                int y = (int) objectMap.get("top");
                int width = (int) objectMap.get("width");
                int height = (int) objectMap.get("height");
                String reviewPointsName = (String) objectMap.get("reviewPointsName");
                g = image.getGraphics();
                g.setColor(Color.RED);//画笔颜色
                // 设置线的粗细
                BasicStroke stokeLine = new BasicStroke(6.0f);
                Graphics2D g2 = (Graphics2D) g;
                g2.setStroke(stokeLine);
                // 坐标+尺寸
                g.drawRect(x, y, width, height);//矩形框(原点x坐标，原点y坐标，矩形的长，矩形的宽)
                if (serialNumber > 20) {
                    g.setFont(new Font("", Font.BOLD, 60));
                } else {
                    g.setFont(new Font("calibri", Font.BOLD, 60));
                }
                //g.setFont(new Font("", Font.BOLD, 60));//设置字体样式(3个参数分别表示：字体名称，字体样式，字体大小)
                String num = FaStaticParam.SERNUM_MAP.get(Long.toString(serialNumber));
                // g.drawString(num, x, y-10);
                String xy = x + "-" + y;
                int count = Collections.frequency(xyAll, xy);
                xyAll.add(xy);
                if (count > 0) {
                    x = x + 60 * count;
                }
                g.drawString(num, x, y - 10);
            }

            for (Map<String, Object> objectMap : manualAuditList) {
                Long serialNumber = (Long) objectMap.get("serialNumber");
                int x = (int) objectMap.get("left");
                int y = (int) objectMap.get("top");
                int width = (int) objectMap.get("width");
                int height = (int) objectMap.get("height");
                String reviewPointsName = (String) objectMap.get("reviewPointsName");
                g = image.getGraphics();
                g.setColor(Color.blue);//画笔颜色
                // 设置线的粗细
                BasicStroke stokeLine = new BasicStroke(6.0f);
                Graphics2D g2 = (Graphics2D) g;
                g2.setStroke(stokeLine);
                // 坐标+尺寸
                g.drawRect(x, y, width, height);//矩形框(原点x坐标，原点y坐标，矩形的长，矩形的宽)
                if (serialNumber > 20) {
                    g.setFont(new Font("", Font.BOLD, 60));
                } else {
                    g.setFont(new Font("calibri", Font.BOLD, 60));
                }
                //g.setFont(new Font("", Font.BOLD, 60));//设置字体样式(3个参数分别表示：字体名称，字体样式，字体大小)
                String num = FaStaticParam.SERNUM_MAP.get(Long.toString(serialNumber));
                //g.drawString(num, x, y-10);
                String xy = x + "-" + y;
                int count = Collections.frequency(xyAll, xy);
                xyAll.add(xy);
                if (count > 0) {
                    x = x + 60 * count;
                }
                g.drawString(num, x, y - 10);
            }
            FileOutputStream out = new FileOutputStream(localAuditImageUrl);//输出图片的地址
            ImageIO.write(image, "jpeg", out);

            String picBase642 = ImageToBase64(localAuditImageUrl);
            ApiResultSet<SysAtta> apiResultSet2 = this.uploadBase64Images(picBase642);
            if (null != apiResultSet2.getData()) {
                SysAtta atta = apiResultSet2.getData();
                //log.info("附件地址:" + atta.getFastdfsNginxUrl());
                localAuditImageUrl = atta.getFastdfsNginxUrl();
            }

        }

        //保存后清除本地的附件
        File myFilePath = new File(localAuditImageUrl);
        myFilePath.delete();
        return localAuditImageUrl;
    }

    /**
     * 本地地址图片转base64；
     *
     * @param imgPath
     * @return
     * @throws UnsupportedEncodingException
     */
    private String ImageToBase64(String imgPath) throws Exception {
        byte[] data = null;
        // 读取图片字节数组
        try {
            InputStream in = new FileInputStream(imgPath);
            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 对字节数组Base64编码
        BASE64Encoder encoder = new BASE64Encoder();
        // 返回Base64编码过的字节数组字符串
        // log.info("本地图片转换Base64:" + encoder.encode(Objects.requireNonNull(data)));

        String base64 = encoder.encode(Objects.requireNonNull(data));
        if (null != base64) {
            base64 = base64.replace("\n", "").replace("\t", "").replace("\r", "");
            base64 = URLDecoder.decode(base64, DEFAULT_CHARACTER_SET);
            String strOne = " ";
            String strTwo = "+";
            base64 = base64.replaceAll(strOne, strTwo);
        }
        return base64;
    }


    /**
     * 根據地址获取base64数据
     *
     * @param url
     * @return
     * @throws Exception
     */
    public AttaBase64Result getAttaBase64ByUrl(String url) throws Exception {
        AttaBase64Result base64Result = new AttaBase64Result();
        Map<String, Object> param = new HashMap<>(6);
        String base64 = FileManageUtil.getImgFromUrl(url);
        log.info("base64取值=" + base64);
        if (null != base64) {
            String result = base64;
            result = result.replace("\n", "").replace("\t", "").replace("\r", "");
            result = URLDecoder.decode(result, DEFAULT_CHARACTER_SET);
            String jsonResult = result;
            base64Result.setResult(result);
            String resultNew = base64Result.getResult();
            String strOne = " ";
            String strTwo = "+";
            resultNew = resultNew.replaceAll(strOne, strTwo);
            base64Result.setResult(resultNew);
        }
        return base64Result;
    }


    /***
     * @Description:获取签字和盖章数据
     * @Author:liangss
     * @Date:2021/12/8
     * @Param: [picBase64]
     */
    public Map<String, String> getSealAndSignByBase64(String picBase64) throws Exception {

        Map<String, String> JsonMap = new HashMap<String, String>();
        Map<String, Object> sealListJsonMap = new HashMap<String, Object>();
        Map<String, Object> signListJsonMap = new HashMap<String, Object>();
        List<Map<String, Object>> seal = new ArrayList<>();
        List<Map<String, Object>> sign = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        //yzUrl="http://101.230.251.254:10504/";
        LinkedHashMap<String, Object> params = new LinkedHashMap<String, Object>();
        params.put("data", picBase64);
        String res = HttpUtil.post("http://101.230.251.254:10510/material_base64", params);
        if (StrUtil.isNotEmpty(res)) {
            JsonMap.put("success", "true");
            JSONArray jsonArray = JSONArray.parseArray(res);
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String class_label = jsonObject.get("class_label").toString();
                JSONArray boxes = jsonObject.getJSONArray("box");
                if (boxes.size() > 0) {
                    map = new HashMap<>();
                    map.put("x", boxes.get(0));
                    map.put("y", boxes.get(1));
                    map.put("w", boxes.get(2));
                    map.put("h", boxes.get(3));
                    map.put("class_label", class_label);
                    if (class_label.equals("Signature")) {//签字
                        sign.add(map);
                    } else if (("circle_red', 'circle_gray', 'rectangle_red', 'rectangle_gray', 'fingerprint_red', 'fingerprint_gray','other'").contains(class_label)) {//公章
                        seal.add(map);
                    }
                }
            }
            String sealListJson = "";
            String signListJson = "";
            if (seal.size() > 0) {
                sealListJsonMap.put("success", true);
                sealListJsonMap.put("result", seal);
                sealListJson = new JSONObject(sealListJsonMap).toString();
            }

            if (sign.size() > 0) {
                signListJsonMap.put("success", true);
                signListJsonMap.put("result", sign);
                signListJson = new JSONObject(signListJsonMap).toString();
            }
            JsonMap.put("sealListJson", sealListJson);
            JsonMap.put("signListJson", signListJson);

        } else {
            JsonMap.put("success", "false");
        }

        return JsonMap;

    }

    /***
     * @Description:检验位置是否包含
     * @Author:liangss
     * @Date:2021/12/9
     * @Param: [rect1, rect2]
     */
    public Boolean checkPosition(Rect rect1, Rect rect2) {
        int topColumnmin1 = rect1.x - rect1.height;
        int topColumnMax1 = rect1.x + rect1.height;
        int leftColumnmin1 = rect1.y - rect1.width;
        int leftColumnmax1 = rect1.y + rect1.width;
        int topColumnmin2 = rect2.x - rect2.height;
        int topColumnMax2 = rect2.x + rect2.height;
        int leftColumnmin2 = rect2.y - rect2.width;
        int leftColumnmax2 = rect2.y + rect2.width;

        Boolean sealOrNOt = false;
        if ((topColumnMax1 > rect2.x && topColumnMax1 > topColumnMax2) && (leftColumnmax1 > rect2.y && leftColumnmax1 > leftColumnmax2)) {
            sealOrNOt = true;
        }
        return sealOrNOt;
    }


    /**
     * 检验印章接口
     *
     * @param picBase64
     * @return
     * @throws Exception
     */
    public Map<String, Object> checkSealMapByBase64(String picBase64) throws Exception {
        Map<String, Object> ocrResultMap = new HashMap<String, Object>();
        List<Map<String, Object>> seal = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        //yzUrl="http://101.230.251.254:10504/";
        LinkedHashMap<String, Object> params = new LinkedHashMap<String, Object>();
        params.put("data", picBase64);
        String res = HttpUtil.post(yzUrl + "predict_base64", params);
        if (StrUtil.isNotEmpty(res) && !res.equals("图片不存在")) {
            ocrResultMap.put("success", true);
            JSONObject jsonObjimg = JSONObject.parseObject(res);
            Map<String, Object> map2 = jsonObjimg.getJSONObject("stamp");
            List boxes = (List) map2.get("boxes");
            if (boxes.size() > 0) {
                for (int i = 0; i < boxes.size(); i++) {
                    List boxesChild = (List) boxes.get(i);
                    if (boxesChild.size() > 0) {
                        map = new HashMap<>();
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
            ocrResultMap.put("size", boxes.size());
        } else {
            ocrResultMap.put("success", false);
        }

        ocrResultMap.put("result", seal);
        return ocrResultMap;
    }


    /***
     * @Description:根据base64获取签字和盖章的数据
     * @Author:liangss
     * @Date:2021/12/9
     * @Param: [picBase64]
     */
    public Map<String, String> checkSignAndSealByBase64(String picBase64) throws Exception {
        Map<String, String> JsonMap = new HashMap<String, String>();

        Map<String, Object> sealListJsonMap = new HashMap<String, Object>();
        Map<String, Object> signListJsonMap = new HashMap<String, Object>();
        List<Map<String, Object>> seal = new ArrayList<>();
        List<Map<String, Object>> sign = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        //yzUrl="http://101.230.251.254:10504/";
        LinkedHashMap<String, Object> params = new LinkedHashMap<String, Object>();
        params.put("data", picBase64);
        String res = HttpUtil.post(qzAndYzUrl + "material_base64", params);
        //String res = HttpUtil.post("http://101.230.251.254:10510/material_base64", params);
        if (StrUtil.isNotEmpty(res)) {
            JsonMap.put("success", "true");
            JSONArray jsonArray = JSONArray.parseArray(res);
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                if (null != jsonObject.get("box") && null != jsonObject.get("class_label")) {
                    String class_label = jsonObject.get("class_label").toString();
                    JSONArray boxes = jsonObject.getJSONArray("box");
                    if (boxes.size() > 0) {
                        map = new HashMap<>();
                        map.put("y", boxes.get(0));
                        map.put("x", boxes.get(1));
                        map.put("w", boxes.get(2));
                        map.put("h", boxes.get(3));
                        map.put("class_label", class_label);
                        if (class_label.equals("Signature")) {//签字
                            sign.add(map);
                        } else if (("circle_red', 'circle_gray', 'rectangle_red', 'rectangle_gray', 'fingerprint_red', 'fingerprint_gray','other'").contains(class_label)) {//公章
                            seal.add(map);
                        }
                    }
                }

            }
            String sealListJson = "";
            String signListJson = "";
            if (seal.size() > 0) {
                sealListJsonMap.put("success", true);
                sealListJsonMap.put("result", seal);
                sealListJson = new JSONObject(sealListJsonMap).toString();
            }

            if (sign.size() > 0) {
                signListJsonMap.put("success", true);
                signListJsonMap.put("result", sign);
                signListJson = new JSONObject(signListJsonMap).toString();
            }
            JsonMap.put("sealListJson", sealListJson);
            JsonMap.put("signListJson", signListJson);

        } else {
            JsonMap.put("success", "false");
        }

        return JsonMap;

    }


    /**
     * 检验签字接口
     *
     * @param picBase64
     * @return
     * @throws Exception
     */
    public Map<String, Object> checkSignMapByBase64(String picBase64) throws Exception {

        Map<String, Object> ocrResultMap = new HashMap<String, Object>();
        List<Map<String, Object>> sign = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        // qzUrl="http://101.230.251.254:10505/";
        LinkedHashMap<String, Object> params = new LinkedHashMap<String, Object>();
        params.put("data", picBase64);
        String res = HttpUtil.post(qzUrl + "predict_base64", params);
        if (StrUtil.isNotEmpty(res) && !res.equals("图片不存在")) {
            ocrResultMap.put("success", true);
            JSONObject jsonObjimg = JSONObject.parseObject(res);
            Map<String, Object> map2 = jsonObjimg.getJSONObject("sign");
            List boxes = (List) map2.get("boxes");
            if (boxes.size() > 0) {
                for (int i = 0; i < boxes.size(); i++) {
                    List boxesChild = (List) boxes.get(i);
                    if (boxesChild.size() > 0) {
                        map = new HashMap<>();
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
            ocrResultMap.put("size", boxes.size());

        } else {
            ocrResultMap.put("success", false);
        }

        ocrResultMap.put("result", sign);
        return ocrResultMap;
    }


    public boolean allFieldIsNull(Object o) {
        try {
            if (null == o) {
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


//======================old=================================================

    /**
     * 获取审核结果
     *
     * @param request
     * @param caseOid            办件oid
     * @param serviceOid         事项oid
     * @param qlCaseMaterialList 材料办件列表
     * @return
     * @throws Exception
     */
    public Map<String, Object> getMeterMaterialValid(HttpServletRequest request, String caseOid, String serviceOid, List<QlCaseMaterial> qlCaseMaterialList) throws Exception {
        Map<String, Object> jsonObject = new HashMap<String, Object>();
        if (StrUtil.isBlank(caseOid)) {
            jsonObject.put("code", FaStaticParam.INTER_STATUS_CODE_PARAM_EXCEPTION);
            jsonObject.put("message", "办件唯一标识[caseOid]不能为空！");
            return jsonObject;
        }
        //删除之前存储的审核结果
        auditResultManager.updateByCaseOid(caseOid);

        List<FaMaterialPicOcrResult> ocrResultList = new ArrayList<FaMaterialPicOcrResult>();
        for (QlCaseMaterial qlCaseMaterial : qlCaseMaterialList) {
            FaMaterialPicOcrResult faMaterialPicOcrResult = new FaMaterialPicOcrResult();
            faMaterialPicOcrResult.setUuid(caseOid);
            faMaterialPicOcrResult.setMaterialOid(qlCaseMaterial.getMaterialOid());
            List<FaMaterialPicOcrResult> faMaterialPicOcrResultList = this.faMaterialPicOcrResultManager.getFaMaterialPicOcrResultByFmpor(faMaterialPicOcrResult);
            if (!CollUtil.isEmpty(faMaterialPicOcrResultList)) {
                ocrResultList.addAll(faMaterialPicOcrResultList);
            }
        }

        if (ocrResultList == null || ocrResultList.size() < 1) {
            jsonObject.put("code", FaStaticParam.PRE_TRIAL_RESULT_STATUS_NOT_RESULT);
            jsonObject.put("message", "无审核结果");
            return jsonObject;
        }

        //无验证规则
        FaModelRuleValidation faModelRuleValidation = new FaModelRuleValidation();
        faModelRuleValidation.setServiceOid(serviceOid);
        List<FaModelRuleValidation> ruleValidList = this.faModelRuleValidationManager.queryFaModelRuleValidationList(faModelRuleValidation);
        if (ruleValidList == null || ruleValidList.size() < 1) {
            jsonObject.put("code", FaStaticParam.PRE_TRIAL_RESULT_STATUS_NOT_RULE);
            jsonObject.put("message", "无验证规则");
            return jsonObject;
        }
        // SET集合(元素：目录子项编号+"_"+材料主键组成的唯一标识)，用于过滤同一页材料多次上传产生的识别记录
        Set<String> cataCodeSet = new HashSet<>();
        Map<String, String> blockMap = new HashMap<String, String>();
        Map<String, org.json.JSONObject> blockMapResult = new HashMap<String, org.json.JSONObject>();
        for (FaMaterialPicOcrResult ocrResult : ocrResultList) {
            String cataCode = ocrResult.getCataCode();
            String materialOid = ocrResult.getMaterialOid();
            String refinedMaterialOid = ocrResult.getRefinedMaterialOid();
            if (StrUtil.isBlank(cataCode)) {
                continue;
            }
            // 目录子项编号已存在，进入下一次循环
            if (cataCodeSet.contains(cataCode + "_" + materialOid + "_" + refinedMaterialOid)) {
                continue;
            }
            cataCodeSet.add(cataCode + "_" + materialOid + "_" + refinedMaterialOid);
            if (StrUtil.isBlank(ocrResult.getResultJson())) {
                continue;
            }
            org.json.JSONObject resultJsonObject = new org.json.JSONObject(ocrResult.getResultJson());
            org.json.JSONArray resultArray = resultJsonObject.getJSONArray("result");

            String sealResultJson = ocrResult.getSealResultJson();
            String signResultJson = ocrResult.getSignResultJson();

            blockMap.put("sealResultJson" + "_" + materialOid + "_" + refinedMaterialOid, sealResultJson);
            blockMap.put("signResultJson" + "_" + materialOid + "_" + refinedMaterialOid, signResultJson);

            if (resultArray == null || resultArray.length() <= 0) {
                continue;
            }
            for (int i = 0; i < resultArray.length(); i++) {
                org.json.JSONObject blockJsonObject = resultArray.getJSONObject(i);
                String imageHeight = "";
                String imageWidth = "";
                if (StringUtils.isNotEmpty(ocrResult.getImageHeight())) {
                    imageHeight = ocrResult.getImageHeight();
                }
                if (StringUtils.isNotEmpty(ocrResult.getImageWidth())) {
                    imageWidth = ocrResult.getImageWidth();
                }
                blockJsonObject.put("attaOid", ocrResult.getAttaOid());
                blockJsonObject.put("localImageUrl", ocrResult.getLocalImageUrl());
                blockJsonObject.put("imageHeight", imageHeight);
                blockJsonObject.put("imageWidth", imageWidth);
                blockMapResult.put(blockJsonObject.getString("code") + "_" + ocrResult.getMaterialOid() + "_" + refinedMaterialOid, blockJsonObject);
            }

            ocrResult.setResultJsonObject(resultJsonObject);
        }

        jsonObject.put("code", FaStaticParam.INTER_STATUS_CODE_SUCCESS);
        jsonObject.put("result", ruleValidAll(request, caseOid, ruleValidList, blockMap, blockMapResult));
        //jsonObject.put("result", getAuitResultByValid(request, caseOid, ruleValidList,blockMap, blockMapResult));
        return jsonObject;
    }


    /**
     * 规则验证结果
     *
     * @param request
     * @param caseOid
     * @param ruleValidList
     * @param blockMap
     * @param blockMapResult
     * @return
     * @throws Exception
     */
    private List<Map<String, Object>> ruleValidAll(HttpServletRequest request, String caseOid,
                                                   List<FaModelRuleValidation> ruleValidList,
                                                   Map<String, String> blockMap, Map<String, org.json.JSONObject> blockMapResult)
            throws Exception {

        List<Map<String, Object>> validResultArray = new ArrayList<Map<String, Object>>();
        for (FaModelRuleValidation ruleValid : ruleValidList) {

            Map<String, Object> validResult = new HashMap<String, Object>();
            String validateType = ruleValid.getValidateType();
            String metadataCode = ruleValid.getTemplateMetadataCode();
            String materialOid = ruleValid.getSxMaterialOid();
            String refinedMaterialOid = ruleValid.getRefinedMaterialOid();
            validResult.put("validateType", validateType);
            validResult.put("materialOid", ruleValid.getSxMaterialOid());
            validResult.put("refinedMaterialOid", ruleValid.getRefinedMaterialOid());
            validResult.put("catalogOid", ruleValid.getCatalogOid());
            validResult.put("code", ruleValid.getTemplateMetadataCode());
            validResult.put("name", ruleValid.getTemplateMetadataName());
            validResult.put("auditPointStatus", ruleValid.getAuditPointStatus());
            validResult.put("reviewPointOid", ruleValid.getReviewPointOid());
            String words = null, picPath = null;
            String sealResultJson = null;
            String signResultJson = null;
            String top = "";
            String left = "";
            String width = "";
            String height = "";
            String imageHeight = "";
            String imageWidth = "";
            String localImageUrl = "";
            String attaOid = "";
            String wordskey = metadataCode + "_" + materialOid + "_" + refinedMaterialOid;
            if (blockMapResult.containsKey(wordskey)) {
                org.json.JSONObject blockJsonObject = blockMapResult.get(wordskey);
                validResult.put("blockJsonObject", blockJsonObject);


                top = blockJsonObject.getString("top");
                left = blockJsonObject.getString("left");
                width = blockJsonObject.getString("width");
                height = blockJsonObject.getString("height");
                imageHeight = blockJsonObject.getString("imageHeight");
                imageWidth = blockJsonObject.getString("imageWidth");
                localImageUrl = blockJsonObject.getString("localImageUrl");
                attaOid = blockJsonObject.getString("attaOid");

                validResult.put("top", top);
                validResult.put("left", left);
                validResult.put("width", width);
                validResult.put("height", height);
                validResult.put("imageHeight", imageHeight);
                validResult.put("imageWidth", imageWidth);
                validResult.put("localImageUrl", localImageUrl);
                validResult.put("attaOid", attaOid);

                if (blockJsonObject.has("words")) {
                    words = blockJsonObject.getString("words");
                } else {
                    words = words == null ? "" : words;
                }
            } else {
                words = words == null ? "" : words;
            }
            org.json.JSONArray sealtArray = null;
            if (blockMap.containsKey("sealResultJson" + "_" + materialOid + "_" + refinedMaterialOid)) {
                //印章
                sealResultJson = blockMap.get("sealResultJson" + "_" + materialOid + "_" + refinedMaterialOid);
                org.json.JSONObject sealResultJsonObject = new org.json.JSONObject(sealResultJson);
                sealtArray = sealResultJsonObject.getJSONArray("result");
            }
            org.json.JSONArray signtArray = null;
            if (blockMap.containsKey("signResultJson" + "_" + materialOid + "_" + refinedMaterialOid)) {
                //签字
                signResultJson = blockMap.get("signResultJson" + "_" + materialOid + "_" + refinedMaterialOid);
                org.json.JSONObject signlResultJsonObject = new org.json.JSONObject(signResultJson);
                signtArray = signlResultJsonObject.getJSONArray("result");
            }


            switch (validateType) {
                case FaStaticParam.VALIDATION_TYPE_NOTNULL:
                    validResult.put("memo", FaStaticParam.VALIDATION_TYPE.get(FaStaticParam.VALIDATION_TYPE_NOTNULL));
                    if (StrUtil.isNotBlank(picPath)) { // 图片的非空验证
                        validResult.put("picPath", picPath);
                        PictureHandle picHandle = new HighShotMeterPictureHandle(picPath);
                        if (!picHandle.isPicBlank()) {
                            validResult.put("valid", true);
                        } else {
                            validResult.put("valid", false);
                        }
                        picHandle.releaseMat();
                    } else {
                        validResult.put("words", words);
                        if (StrUtil.isBlank(words)) {
                            validResult.put("valid", false);
                        } else {
                            validResult.put("valid", true);
                        }
                    }
                    break;
                case FaStaticParam.VALIDATION_TYPE_NULL:
                    validResult.put("memo", FaStaticParam.VALIDATION_TYPE.get(FaStaticParam.VALIDATION_TYPE_NULL));
                    if (StrUtil.isNotBlank(picPath)) { // 图片的非空验证
                        validResult.put("picPath", picPath);
                        PictureHandle picHandle = new HighShotMeterPictureHandle(picPath);
                        if (!picHandle.isPicBlank()) {
                            validResult.put("valid", false);
                        } else {
                            validResult.put("valid", true);
                        }
                        picHandle.releaseMat();
                    } else {
                        validResult.put("words", words);
                        if (StrUtil.isNotBlank(words)) {
                            validResult.put("valid", false);
                        } else {
                            validResult.put("valid", true);
                        }
                    }
                    break;
                case FaStaticParam.VALIDATION_TYPE_CONTENT:
                    String content = ruleValid.getThanContent();
                    // 如果content为null，设置为空字符串
                    content = content == null ? "" : content;
                    validResult.put("memo", "字符必须为" + content + "！");
                    if (!content.equals(words)) {
                        validResult.put("valid", false);
                    } else {
                        validResult.put("valid", true);
                    }
                    break;
                case FaStaticParam.VALIDATION_TYPE_STR_CONTAIN:
                    String containContent = ruleValid.getThanContent();
                    // 如果containContent为null，设置为空字符串
                    containContent = containContent == null ? "" : containContent;
                    validResult.put("memo", "字符必须包含为" + containContent + "！");
                    if (!words.contains(containContent)) {
                        validResult.put("valid", false);
                    } else {
                        validResult.put("valid", true);
                    }
                    break;
                case FaStaticParam.VALIDATION_TYPE_RANGE:
                    String contentDown = ruleValid.getContentDown();
                    String contentUp = ruleValid.getContentUp();
                    // 如果containContent为null，设置为空字符串
                    contentDown = contentDown == null ? "" : contentDown;
                    contentUp = contentUp == null ? "" : contentUp;
                    try {
                        validResult.put("memo", "数字范围必须在" + contentDown + "和" + contentUp + "之间！");
                        if (StrUtil.isBlank(words)) {
                            validResult.put("valid", false);
                            validResult.put("memo", "字符不能为空，数字范围必须在" + contentDown + "和" + contentUp + "之间！");
                            break;
                        }

                        Integer cDown = StrUtil.isNotBlank(contentDown) ? Integer.parseInt(contentDown) : null;
                        Integer cUp = StrUtil.isNotBlank(contentUp) ? Integer.parseInt(contentUp) : null;
                        Integer c = StrUtil.isNotBlank(words) ? Integer.parseInt(words) : null;

                        if (cDown != null && c >= cDown) {
                            if (cUp != null && c <= cUp) {
                                validResult.put("valid", true);
                            } else {
                                validResult.put("valid", false);
                            }
                        } else {
                            validResult.put("valid", false);
                        }
                    } catch (Exception e) {
                        validResult.put("valid", false);
                        validResult.put("memo", "字符必须是数字！");
                    }

                    break;
                case FaStaticParam.VALIDATION_TYPE_DATE_COMPARE:
                    String contentDateFormat = ruleValid.getContentDateFormat();
                    /* String contentDateValue = ruleValid.getContentDateValue();*/
                    String memo = "日期比较";
                    Boolean valid = true;

                    String contentBeforeDateValue = ruleValid.getContentBeforeDateValue();
                    String contentAfterDateValue = ruleValid.getContentAfterDateValue();
                    String biforeNowDayValue = ruleValid.getBiforeNowDayValue();
                    String afterNowDayValue = ruleValid.getAfterNowDayValue();

                    Date beforecompareDate = new Date();
                    Date aftercompareDate = new Date();
                    try {
                        if (StrUtil.isBlank(words)) {
                            validResult.put("valid", false);
                            validResult.put("memo", "字符不能为空！");
                            break;
                        }
                        // 如果containContent为null，设置为空字符串
                        contentDateFormat = contentDateFormat == null ? "" : contentDateFormat;
                        //ocr取值
                        Date wDate = DateUtil.parse(words, contentDateFormat);

                        //小于
                        if (StrUtil.isNotBlank(contentBeforeDateValue)) {
                            contentBeforeDateValue = contentBeforeDateValue == null ? "" : contentBeforeDateValue;
                            memo = memo + "日期需要小于" + contentBeforeDateValue + ";";
                            if (!"CURRENT_DATE".equals(contentBeforeDateValue.toUpperCase())) {
                                //将验证字符串转化为日期类型进行比对
                                beforecompareDate = DateUtil.parse(contentBeforeDateValue, contentDateFormat);
                                if (wDate.before(beforecompareDate)) {
                                    valid = true;
                                } else {
                                    valid = false;
                                }
                            }
                        }
                        if (valid) {//审核通过继续审核不通过直接审核不通过
                            if (StrUtil.isNotBlank(contentAfterDateValue)) {
                                contentAfterDateValue = contentAfterDateValue == null ? "" : contentAfterDateValue;
                                memo = memo + "日期需要大于" + contentAfterDateValue;
                                if (!"CURRENT_DATE".equals(contentBeforeDateValue.toUpperCase())) {
                                    aftercompareDate = DateUtil.parse(contentAfterDateValue, contentDateFormat);
                                    //将验证字符串转化为日期类型进行比对
                                    if (wDate.after(aftercompareDate)) {
                                        valid = true;
                                    } else {
                                        valid = false;
                                    }
                                }
                            }

                        }


                        validResult.put("memo", memo);
                        validResult.put("valid", valid);
                        //validResult.put("memo", "日期比较，日期格式：" + contentDateFormat + "，比对日期：" + DateUtil.format(compareDate, contentDateFormat) + "！");


                    } catch (Exception e) {
                        validResult.put("valid", false);
                        validResult.put("memo", "字符无法转化为日期！");
                    }
                    break;
                case FaStaticParam.VALIDATION_TYPE_CONTENT_THAN:
                    String thancontetKey = ruleValid.getThanTemplateMetadataCode() + "_" + ruleValid.getThanSxMaterialOid() + "_" + ruleValid.getThanRefinedMaterialOid();
                    String thanContent = "";
                    if (blockMapResult.containsKey(thancontetKey)) {
                        org.json.JSONObject blockJsonObject = blockMapResult.get(thancontetKey);
                        if (blockJsonObject.has("words")) {
                            thanContent = blockJsonObject.getString("words");
                        } else {
                            thanContent = thanContent == null ? "" : thanContent;
                        }
                    } else {
                        thanContent = thanContent == null ? "" : thanContent;
                    }
                    ApiResultSet<MaterialCatalog> catalog = materialCatalogService.getMaterialCatalogByOid(ruleValid.getThanCatalogOid());
                    MaterialCatalog thanCatalog = null;
                    if (catalog != null && catalog.getData() != null) {
                        thanCatalog = catalog.getData();
                    }
                    // 如果thanContent为null, 比较元素不存在（对应材料不需要上传情况）不进行比较
                    if (thanContent == null) {
                        validResult.put("valid", true);
                    } else {
                        // 如果thanContent为null，设置为空字符串
                        //thanContent = thanContent == null ? "" : thanContent;
                        validResult.put("memo", ruleValid.getTemplateMetadataName() + "[" + ruleValid.getCatalogName() + "]与"
                                + ruleValid.getThanTemplateMetadataName() + "["
                                + thanCatalog.getCatalogName() + "]必须相同！");

                        if (!thanContent.equals(words)) {
                            validResult.put("valid", false);
                        } else {
                            validResult.put("valid", true);
                        }
                    }
                    break;
                case FaStaticParam.VALIDATION_TYPE_SIMILARITY:
                    /* Double similar = ruleValid.getSimilar();*/
                    String similarold = ruleValid.getSimilar();
                    Double similar = Double.parseDouble(similarold);
                    validResult.put("memo",
                            ruleValid.getTemplateMetadataName() + "[" + ruleValid.getTemplateMetadataCode() + "]与"
                                    + ruleValid.getThanTemplateMetadataName() + "["
                                    + ruleValid.getThanTemplateMetadataCode() + "]的相识度必须大于" + similar + "%！");
                    // 相似度比对
                    if (StrUtil.isBlank(picPath)) {
                        validResult.put("valid", false);
                        break;
                    }
                    String thanPicPath = blockMap.get(ruleValid.getThanTemplateMetadataCode() + "_" + materialOid + "-pic");
                    if (StrUtil.isBlank(thanPicPath)) {
                        validResult.put("valid", false);
                        break;
                    }
                    if (OpenCVImageHandle.similarCalc(picPath, thanPicPath) < similar) {
                        validResult.put("valid", false);
                    } else {
                        validResult.put("valid", true);
                    }
                    break;
                case FaStaticParam.VALIDATION_TYPE_HAND_SIGN:
                    if (signtArray == null || signtArray.length() <= 0) {
                        validResult.put("valid", false);
                        validResult.put("memo", "必须为手写！");
                        break;
                    } else {
                        Boolean sealOrNOt = false;
                        for (int i = 0; i < signtArray.length(); i++) {
                            if (!sealOrNOt) {
                                org.json.JSONObject seal = (org.json.JSONObject) signtArray.get(i);
                                //研究院检测取值
                                double x = (double) seal.get("x");//左侧坐标占比
                                double y = (double) seal.get("y");//上侧坐标占比
                                double w = (double) seal.get("w");//宽占比
                                double h = (double) seal.get("h");//高左边占比

                                //百度ocr取值
                                double leftdouble = Double.parseDouble(left);//左侧坐标取值
                                double topdouble = Double.parseDouble(top);//左侧坐标取值
                                double Heightdouble = Double.parseDouble(height);//宽取值
                                double widthdouble = Double.parseDouble(width);//宽取值

                                //百度ocr取值占图片的百分比  imageWidth图片整体宽度 imageHeight图片整体高度
                                double leftbl = leftdouble / Double.parseDouble(imageWidth);//左侧坐标占比
                                double topbl = topdouble / Double.parseDouble(imageHeight);//上侧坐标占比
                                double widthbl = widthdouble / Double.parseDouble(imageWidth);//宽占比
                                double heighbl = Heightdouble / Double.parseDouble(imageHeight);//高左边占比

                                //研究院左侧坐标占比在百度ocr左侧坐标占比加减宽占比范围内
                                //研究院上侧坐标占比在百度ocr左侧坐标占比加减高取值范围内
                                /*if(leftbl+widthbl>x&&leftbl-widthbl<x && topbl+heighbl>y&&topbl-heighbl<y){
                                    sealOrNOt=true;
                                }*/

                                if (leftbl + 0.15 > x && leftbl - 0.15 < x && topbl + 0.15 > y && topbl - 0.15 < y) {
                                    sealOrNOt = true;
                                }
                            }
                        }
                        validResult.put("memo", "必须为手写！");
                        validResult.put("valid", sealOrNOt);

                    }
                    break;
                case FaStaticParam.VALIDATION_TYPE_SEAL:
                    if (sealtArray == null || sealtArray.length() <= 0) {
                        validResult.put("valid", false);
                        validResult.put("memo", "必须为盖章！");
                        break;
                    } else {
                        Boolean sealOrNOt = false;
                        for (int i = 0; i < sealtArray.length(); i++) {
                            if (!sealOrNOt) {
                                org.json.JSONObject seal = (org.json.JSONObject) sealtArray.get(i);
                                double x = (double) seal.get("x");
                                double y = (double) seal.get("y");
                                double w = (double) seal.get("w");
                                double h = (double) seal.get("h");
                                double leftdouble = Double.parseDouble(left);
                                double topdouble = Double.parseDouble(top);
                                double Heightdouble = Double.parseDouble(height);
                                double widthdouble = Double.parseDouble(width);

                                double leftbl = leftdouble / Double.parseDouble(imageWidth);
                                double topbl = topdouble / Double.parseDouble(imageHeight);


                                if (leftbl + 0.15 > x && leftbl - 0.15 < x && topbl + 0.15 > y && topbl - 0.15 < y) {
                                    sealOrNOt = true;
                                }
                            }
                        }
                        validResult.put("memo", "必须为盖章！");
                        validResult.put("valid", sealOrNOt);
                    }
                    break;
                case FaStaticParam.VALIDATION_TYPE_MANUAL_AUDIT:
                    validResult.put("valid", true);
                    validResult.put("memo", "人工核验！");
                    break;
                default:
                    break;
            }
            validResultArray.add(validResult);
        }
        return validResultArray;
    }

    /**
     * 保存结果信
     *
     * @param caseOid         办件主键
     * @param materialOid     事项材料主键
     * @param materialAttaOid 办件材料记录、上传文件关系表主键getPreTrialResultVo
     * @param cataCode        审核目录编号
     * @param attaOid         附件主键
     * @param base64MD5       附件base64MD5加密字符串
     * @param resultJson      识别结果json
     * @param obj             识别对象
     * @throws Exception
     * @author chenbw
     * @date 2019年4月26日
     */
    private void saveResult(String caseOid, String materialOid, String materialAttaOid, String cataCode, String attaOid, String base64MD5,
                            String resultJson, Object obj) throws Exception {
        FaMaterialPicOcrResult ocrResult = new FaMaterialPicOcrResult();
        ocrResult.setUuid(caseOid);
        ocrResult.setMaterialOid(materialOid);
        ocrResult.setMaterialAttaOid(materialAttaOid);
        //保存ocr识别新对应办件记录材料、附件记录表
        /*ocrResult.setCaseFileAttaRecOid(caseFileAttaRecOid);*/
        ocrResult.setCataCode(cataCode);
        ocrResult.setPicBase64Md5(base64MD5);
        ocrResult.setResultJson(resultJson);
        // 将处理结果保存到数据库中
        this.faMaterialPicOcrResultManager.saveOrUpdate(ocrResult);
        log.info("保存或修改材料目录图片识别结果信息", "保存成功！办件主键为：{}，材料主键为：{}，目标编号为：{}，图片Base64的MD5值为：{}，识别结果json为：{}", caseOid, materialOid, cataCode, base64MD5, resultJson);
        // 获取对应的字段并填充对应的识别结果
        /*if(StrUtil.isNotBlank(resultJson)){
            org.json.JSONObject resultJsonObject = new org.json.JSONObject(resultJson);
            org.json.JSONArray resultArray = resultJsonObject.getJSONArray("result");
            if (resultArray != null && resultArray.length() > 0) {
                Map<String, PacCaseFillResult> pacCaseFillResultCodeMap = new HashMap<>();
                //Set<String> beforeNameSet = new HashSet<>();
                List<String> fillFieldNameList = new ArrayList<>();
                List<PacCaseFillResult> pacCaseFillResultList = pacCaseFillResultDao.queryPacCaseFillResultListByPacCaseCode(caseOid);
                if (CollUtil.isNotEmpty(pacCaseFillResultList)) {
                    pacCaseFillResultCodeMap = pacCaseFillResultList.stream().collect(Collectors.toMap(PacCaseFillResult::getFieldName, Function.identity()));
                    //beforeNameSet = pacCaseFillResultCodeMap.keySet();
                    fillFieldNameList=pacCaseFillResultList.stream().map(t->t.getFieldName()).collect(Collectors.toList());
                    for (int i = 0; i < resultArray.length(); i++) {
                        org.json.JSONObject blockJsonObject = resultArray.getJSONObject(i);
                        if (blockJsonObject.has("words")) {
                            String name = blockJsonObject.getString("name");
                            String val = blockJsonObject.getString("words");
                            if (fillFieldNameList.contains(name) && StrUtil.isNotBlank(val)) {
                                PacCaseFillResult pacCaseFillResult = pacCaseFillResultCodeMap.get(name);
                                pacCaseFillResult.setResult(val);
                                pacCaseFillResultDao.saveOrUpdatePacCaseFillResult(pacCaseFillResult);
                                log.info("保存或修改一表填报结果信息", "修改一表填报结果，结果信息主键为：{}，结果为：{}", pacCaseFillResult.getOid(), val);
                            }
                        }
                    }
                }
            }
        }*/

        // OCRRecord记录保存
        OcrRecord ocrRecord = new OcrRecord();
        ocrRecord.setAttaOid(attaOid);
        ocrRecord.setCaseOid(caseOid);
        if (obj instanceof BusinessLiceInfo) {
            ocrRecord.setBusinessLicenseStatus(BaseStaticParameter.YES);
        } else {
            ocrRecord.setBusinessLicenseStatus(BaseStaticParameter.NO);
        }
        //对象为空时，保存识别结果json信息
        ocrRecord.setOcrResult(obj == null || this.allFieldIsNull(obj) == true ? resultJson : JsonUtil.objToJsonStr(obj));
        this.ocrRecordManager.saveOrUpdate(ocrRecord);
    }


    public AttaBase64Result getAttaBase64New(String attaOid) {
        AttaBase64Result base64Result = new AttaBase64Result();
        Map<String, Object> param = new HashMap<>(6);
        try {
            ApiResultSet<String> attaResult = sysAttaService.getSysAttaBase64ByAttaOid(attaOid);
            if (null != attaResult) {
                String result = attaResult.getData();
                result = result.replace("\n", "").replace("\t", "").replace("\r", "");
                result = URLDecoder.decode(result, DEFAULT_CHARACTER_SET);
                String jsonResult = result;
                param.put("attaOid", attaOid);
                base64Result.setResult(result);
                String resultNew = base64Result.getResult();
                String strOne = " ";
                String strTwo = "+";
                resultNew = resultNew.replaceAll(strOne, strTwo);
                base64Result.setResult(result);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return base64Result;
    }


    /**
     * 验证印章结果
     *
     * @param picBase64
     * @param faModelMaterialCatalog
     * @param caseOid
     * @param caseFileRecOid
     * @param attaOid
     * @throws Exception
     */
    private void checkSealCount(String picBase64, MaterialCatalog faModelMaterialCatalog, String caseOid, String caseFileRecOid, String attaOid) throws Exception {
        com.zfsoft.rest.pojo.ocr.OcrSealCountRequest ocrSealCountRequest = aiTokenUtil.getTokenRequest(com.zfsoft.rest.pojo.ocr.OcrSealCountRequest.class);
        ocrSealCountRequest.setImgBase64(picBase64);
        com.zfsoft.rest.pojo.ocr.OcrSealCountResponse ocrSealCountResponse = ocrSealRestService.getSealCount(ocrSealCountRequest);
        if (200 != ocrSealCountResponse.getCode()) {
            log.info("调用印章接口失败");
            throw new Exception(ocrSealCountResponse.getMessage());

        }
        log.info("调用印章接口成功");
        Integer fileSealCount = ocrSealCountResponse.getCount();
        StampRecord stampRecord = new StampRecord();
        stampRecord.setAttaOid(attaOid);
        stampRecord.setCaseOid(caseOid);
        stampRecord.setCaseFileRecOid(caseFileRecOid);
        stampRecord.setCataOid(faModelMaterialCatalog.getMaterialCatalogOid());
        stampRecord.setAmount(fileSealCount);
        stampRecord.setDelFlag(SysCode.DELETE_STATUS.NO);
        ;
        //如果文件中的印章数量少于要求的数量，则认为印章校验失败
        if (fileSealCount < faModelMaterialCatalog.getSealsNumber()) {
            stampRecord.setPassFlag(1);
        } else {//印章校验成功
            stampRecord.setPassFlag(0);
        }
        this.stampRecordManager.saveOrUpdate(stampRecord);
    }

    /**
     * 检验印章接口
     *
     * @param picBase64
     * @return
     * @throws Exception
     */
    public List<Map<String, Object>> checkSealByBase64(String picBase64) throws Exception {
        List<Map<String, Object>> seal = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        //yzUrl="http://101.230.251.254:10504/";
        LinkedHashMap<String, Object> params = new LinkedHashMap<String, Object>();
        params.put("data", picBase64);
        String res = HttpUtil.post(yzUrl + "predict_base64", params);
        if (StrUtil.isNotEmpty(res)) {
            JSONObject jsonObjimg = JSONObject.parseObject(res);
            Map<String, Object> map2 = jsonObjimg.getJSONObject("stamp");
            List boxes = (List) map2.get("boxes");
            if (boxes.size() > 0) {
                for (int i = 0; i < boxes.size(); i++) {
                    List boxesChild = (List) boxes.get(i);
                    if (boxesChild.size() > 0) {
                        map = new HashMap<>();
                        map.put("class_name", boxesChild.get(0));
                        map.put("x", boxesChild.get(1));
                        map.put("y", boxesChild.get(2));
                        map.put("w", boxesChild.get(3));
                        map.put("h", boxesChild.get(4));
                        seal.add(map);
                    }
                }
            }

        }
        return seal;
    }

    /**
     * 检验签字接口
     *
     * @param picBase64
     * @return
     * @throws Exception
     */
    public List<Map<String, Object>> checkSignByBase64(String picBase64) throws Exception {
        List<Map<String, Object>> sign = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        // qzUrl="http://101.230.251.254:10505/";
        LinkedHashMap<String, Object> params = new LinkedHashMap<String, Object>();
        params.put("data", picBase64);
        String res = HttpUtil.post(qzUrl + "predict_base64", params);
        if (StrUtil.isNotEmpty(res)) {
            JSONObject jsonObjimg = JSONObject.parseObject(res);
            Map<String, Object> map2 = jsonObjimg.getJSONObject("sign");
            List boxes = (List) map2.get("boxes");
            if (boxes.size() > 0) {
                for (int i = 0; i < boxes.size(); i++) {
                    List boxesChild = (List) boxes.get(i);
                    if (boxesChild.size() > 0) {
                        map = new HashMap<>();
                        map.put("class_name", boxesChild.get(0));
                        map.put("x", boxesChild.get(1));
                        map.put("y", boxesChild.get(2));
                        map.put("w", boxesChild.get(3));
                        map.put("h", boxesChild.get(4));
                        sign.add(map);
                    }
                }
            }

        }
        return sign;
    }


    public List<Map<String, Object>> checkSealCountByYjy(String attaOid) throws Exception {
        ApiResultSet<SysAtta> sysAttaresult = sysAttaService.getSysAttaByAttaOid(attaOid);
        SysAtta qlSysAtta = sysAttaresult.getData();
        String fastdfsNginxUrl = qlSysAtta.getFastdfsNginxUrl();

        AttaBase64Result attaBase64Result = getAttaBase64ByUrl(fastdfsNginxUrl);
        String picBase64 = "";
        if (null != attaBase64Result) {
            picBase64 = attaBase64Result.getResult();
        } else {
            log.error("材料分类识别预审", "未获取附件base64信息！办件主键为：{}，附件主键为：{}", null);
            throw new Exception("未获取附件base64信息!");
        }
        List<Map<String, Object>> seal = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        //yzUrl="http://101.230.251.254:10504/";
        LinkedHashMap<String, Object> params = new LinkedHashMap<String, Object>();
        params.put("data", picBase64);
        String res = HttpUtil.post(yzUrl + "predict_base64", params);
        if (StrUtil.isNotEmpty(res)) {
            JSONObject jsonObjimg = JSONObject.parseObject(res);
            Map<String, Object> map2 = jsonObjimg.getJSONObject("stamp");
            List boxes = (List) map2.get("boxes");
            if (boxes.size() > 0) {
                for (int i = 0; i < boxes.size(); i++) {
                    List boxesChild = (List) boxes.get(i);
                    if (boxesChild.size() > 0) {
                        map = new HashMap<>();
                        map.put("class_name", boxesChild.get(0));
                        map.put("x", boxesChild.get(1));
                        map.put("y", boxesChild.get(2));
                        map.put("w", boxesChild.get(3));
                        map.put("h", boxesChild.get(4));
                        seal.add(map);
                    }
                }
            }

        }
        return seal;
    }


    public List<Map<String, Object>> checkSignCountByYjy(String attaOid) throws Exception {
        ApiResultSet<SysAtta> sysAttaresult = sysAttaService.getSysAttaByAttaOid(attaOid);
        SysAtta qlSysAtta = sysAttaresult.getData();
        String fastdfsNginxUrl = qlSysAtta.getFastdfsNginxUrl();

        AttaBase64Result attaBase64Result = getAttaBase64ByUrl(fastdfsNginxUrl);
        String picBase64 = "";
        if (null != attaBase64Result) {
            picBase64 = attaBase64Result.getResult();
        } else {
            log.error("材料分类识别预审", "未获取附件base64信息！办件主键为：{}，附件主键为：{}", null);
            throw new Exception("未获取附件base64信息!");
        }
        List<Map<String, Object>> seal = new ArrayList<>();
        List<Map<String, Object>> sign = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        //qzUrl="http://101.230.251.254:10505/";
        LinkedHashMap<String, Object> params = new LinkedHashMap<String, Object>();
        params.put("data", picBase64);
        String res = HttpUtil.post(qzUrl + "predict_base64", params);
        if (StrUtil.isNotEmpty(res)) {
            JSONObject jsonObjimg = JSONObject.parseObject(res);
            Map<String, Object> map2 = jsonObjimg.getJSONObject("sign");
            List boxes = (List) map2.get("boxes");
            if (boxes.size() > 0) {
                for (int i = 0; i < boxes.size(); i++) {
                    List boxesChild = (List) boxes.get(i);
                    if (boxesChild.size() > 0) {
                        map = new HashMap<>();
                        map.put("class_name", boxesChild.get(0));
                        map.put("x", boxesChild.get(1));
                        map.put("y", boxesChild.get(2));
                        map.put("w", boxesChild.get(3));
                        map.put("h", boxesChild.get(4));
                        sign.add(map);
                    }
                }
            }

        }
        return sign;
    }


    /**
     * 获得预审结果VO
     *
     * @param request
     * @param caseOid
     * @return
     * @throws Exception
     */
    public List<PreTrialResultVo> getPreTrialResultVo(HttpServletRequest request, String caseOid) throws Exception {
        Map<String, Object> map = highShotMeterMaterialValid(request, caseOid);
        //上传材料信息
        ApiResultSet<List<QlCaseMaterial>> qlcmResult = qlCaseMaterialServiceService.queryQlCaseMaterialByCaseOid(caseOid);
        List<QlCaseMaterial> qlCaseMaterialList = qlcmResult.getData();
        List<PreTrialResultVo> list = new ArrayList<>();
        List<Map<String, Object>> resultList = null;
        if (map.get("result") != null) {
            resultList = (List<Map<String, Object>>) map.get("result");
        }
        int i = 0;
        // 根据办件主键获取办件材料记录列表
        caseFileRecFor:
        for (QlCaseMaterial caseFileRec : qlCaseMaterialList) {
            i = i + 1;
            // 办件记录对应事项材料没有绑定材料审核目录，说明该材料不需要智能审核，设置审核标识为2：无需审核
            String materiaOid = caseFileRec.getMaterialOid();
            SxServiceMaterial sxServiceMaterial = null;
            ApiResultSet<SxServiceMaterial> sxmResult = sxServiceMaterialService.getSxServiceMaterialByOid(materiaOid);
            if (null != sxmResult.getData()) {
                sxServiceMaterial = sxmResult.getData();
            }
            if (sxServiceMaterial == null || StrUtil.isBlank(sxServiceMaterial.getMaterialCatalogOid())) {
                // 继续循环
                continue caseFileRecFor;
            }
            //需要智能审核
            String materialCatalogOid = sxServiceMaterial.getMaterialCatalogOid();
            //获取子项材料信息
            ApiResultSet<List<MaterialCatalog>> materiallist = materialCatalogService.queryList(materialCatalogOid);
            List<MaterialCatalog> faModelMaterialCatalogList = null;
            if (materiallist != null && materiallist.getData() != null) {
                faModelMaterialCatalogList = materiallist.getData();
            }
            if (CollectionUtil.isEmpty(faModelMaterialCatalogList)) {
                PreTrialResultVo vo = new PreTrialResultVo();
                if (null == resultList) {
                    vo.setResultStatus(FaStaticParam.PRE_TRIAL_RESULT_STATUS_NOT_RULE);
                } else {
                    vo.setResultStatus(FaStaticParam.PRE_TRIAL_RESULT_STATUS_NOT_NEED);
                }
                //vo.setResultStatus(FaStaticParam.PRE_TRIAL_RESULT_STATUS_NOT_NEED);
                vo.setQlCaseMaterial(caseFileRec);
                vo.setName(sxServiceMaterial.getMaterialName());
                vo.setXuhao(i);
                list.add(vo);
                // 继续循环
                continue caseFileRecFor;
            }
            String materialName = sxServiceMaterial.getMaterialName();
        /*    if (faModelMaterialCatalogList.size() == 1) {
                PreTrialResultVo vo = handlePreTrialResultVo(materialName, caseFileRec, resultList, faModelMaterialCatalogList.get(0));
                vo.setXuhao(i);
                list.add(vo);
                continue caseFileRecFor;
            }*/
            //创建父级vo对象,名称和材料名称移植，展示审核结果（子级vo对象全部通过则通过），不展示查看按钮
            PreTrialResultVo parentVo = new PreTrialResultVo();
            parentVo.setName(materialName);
            parentVo.setXuhao(i);
            //parentStatus用于前台判断为Y则不展示查看按钮
            parentVo.setParentStatus(BaseStaticParameter.YES);
            list.add(parentVo);

            String parentResultStatus = FaStaticParam.PRE_TRIAL_RESULT_STATUS_PASS;
            // 父级验证状态标识，默认为true，当子级存在不通过改为false
            boolean parentResultStatusFlag = true;
            chlidFor:
            for (MaterialCatalog faModelMaterialCatalog : faModelMaterialCatalogList) {
                if (faModelMaterialCatalog.getIsMultiPage() != null && faModelMaterialCatalog.getIsMultiPage() == "true") {
                    continue chlidFor;
                }
                //组装子级vo对象
                PreTrialResultVo childVo = handlePreTrialResultVo("--> " + faModelMaterialCatalog.getCatalogName(), caseFileRec, resultList, faModelMaterialCatalog);
                if (FaStaticParam.PRE_TRIAL_RESULT_STATUS_NOT_PASS.equals(childVo.getResultStatus())) {
                    parentResultStatusFlag = false;
                }
                //childStatus用于前台判断为Y则不显示序号，并缩进
                childVo.setChildStatus(BaseStaticParameter.YES);
                list.add(childVo);
            }
            if (!parentResultStatusFlag) {
                parentResultStatus = FaStaticParam.PRE_TRIAL_RESULT_STATUS_NOT_PASS;
            }
            parentVo.setResultStatus(parentResultStatus);
        }
        return list;
    }


    /**
     * 根据识别结果vo列表判断事项是否通过
     *
     * @param preTrialResultVoList
     * @return
     * @throws Exception
     */
    public String getValidateResult(List<PreTrialResultVo> preTrialResultVoList) {
        String preTrialResult = null;
        for (PreTrialResultVo preTrialResultVo : preTrialResultVoList) {
            if (FrontBaseStaticParameter.PRE_TRIAL_RESULT_STATUS_NOT_PASS.equals(preTrialResultVo.getResultStatus())) {
                preTrialResult = FrontBaseStaticParameter.PRE_TRIAL_RESULT_STATUS_NOT_PASS;
                break;
            }
        }
        if (preTrialResult == null) {
            preTrialResult = FrontBaseStaticParameter.PRE_TRIAL_RESULT_STATUS_PASS;
        }
        return preTrialResult;
    }

    /**
     * 组装预审结果vo
     *
     * @param materialName           展示的名称
     * @param caseFileRec
     * @param resultList
     * @param faModelMaterialCatalog
     * @return
     * @throws Exception
     * @author chenbw
     * @date 2019年6月8日
     */
    private PreTrialResultVo handlePreTrialResultVo(String materialName, QlCaseMaterial caseFileRec, List<Map<String, Object>> resultList,
                                                    MaterialCatalog faModelMaterialCatalog) throws Exception {
        PreTrialResultVo vo = new PreTrialResultVo();
        vo.setName(materialName);
        vo.setCatalogOid(faModelMaterialCatalog.getMaterialCatalogOid());
        vo.setMaterialOid(caseFileRec.getMaterialOid());
        String memoJsonStr = null;
        // 办件记录对应事项材料绑定材料审核目录,验证识别结果
        String resultStatus = FaStaticParam.PRE_TRIAL_RESULT_STATUS_PASS;
        //查询办件材料附件信息
        String materialCatalogOidAll = "";
        ApiResultSet<List<QlCaseMaterialAtta>> qlCaseMaterialAttaResult = qlCaseMaterialAttaServiceService.queryQlCaseMaterialAttaByCaseMaterialOid(caseFileRec.getCaseMaterialOid());
        List<QlCaseMaterialAtta> caseFileAttaRecList = qlCaseMaterialAttaResult.getData();
        if (CollectionUtil.isNotEmpty(caseFileAttaRecList)) {
            for (QlCaseMaterialAtta caseMaterialAttas : caseFileAttaRecList) {
                String materialCatalogOid = caseMaterialAttas.getMaterialCatalogOid();
                materialCatalogOidAll = materialCatalogOidAll + materialCatalogOid + ";";
            }
        }
        // 结果不存在，说明材料识别不成功，设置审核标识为1：不通过
        if (resultList == null) {
            resultStatus = FaStaticParam.PRE_TRIAL_RESULT_STATUS_NOT_PASS;
        } else {
            if (materialCatalogOidAll.indexOf(faModelMaterialCatalog.getMaterialCatalogOid()) != -1) {
                // 存在标识，用于记录验证结果json中是否存在事项材料对应的验证信息，默认为true
                boolean existFlag = true;
                resultStatus = FaStaticParam.PRE_TRIAL_RESULT_STATUS_PASS;
                for (int i = 0; i < resultList.size(); i++) {
                    Map<String, Object> resultMap = resultList.get(i);
                    if (faModelMaterialCatalog.getMaterialCatalogOid().equals(resultMap.get("catalogOid"))
                            && caseFileRec.getMaterialOid().equals(resultMap.get("materialOid"))) {
                        // 验证结果不通过，设置审核标识为1：不通过
                        if (resultMap.get("valid") != null && !(Boolean) resultMap.get("valid")) {
                            existFlag = false;
                            JSONObject memoJosn = new JSONObject();
                            if (resultMap.get("name") != null) {
                                memoJosn.put("name", resultMap.get("name").toString());
                            } else {
                                memoJosn.put("name", "");
                            }
                            if (resultMap.get("memo") != null) {
                                memoJosn.put("memo", resultMap.get("memo").toString());
                            } else {
                                memoJosn.put("memo", "");
                            }
                            memoJsonStr = memoJosn.toString();
                        }
                    }
                }
                if (faModelMaterialCatalog.getSealsNumber() != null && faModelMaterialCatalog.getSealsNumber() > 0) {
                    if (CollectionUtil.isEmpty(caseFileAttaRecList)) {
                        existFlag = false;
                    } else {
                        List<StampRecord> stampRecord = this.stampRecordManager.getCaseFileAttaRecListByOidAndCataOid(faModelMaterialCatalog.getMaterialCatalogOid(), caseFileAttaRecList.get(0).getAttaOid());
                        if (stampRecord == null || (stampRecord != null && stampRecord.get(0).getAmount() == 0)) {
                            existFlag = false;
                        }
                    }

                }

                if (!existFlag) {
                    resultStatus = FaStaticParam.PRE_TRIAL_RESULT_STATUS_NOT_PASS;
                }
            } else {
                resultStatus = FaStaticParam.PRE_TRIAL_RESULT_STATUS_NOT_ATTA;
            }

        }
        vo.setResultStatus(resultStatus);
        vo.setMemoJson(memoJsonStr);
        vo.setQlCaseMaterial(caseFileRec);
        return vo;
    }

    /**
     * 高拍仪图片验证
     *
     * @param每个办件唯一的标识，保存每个办件的图片识别结果，并通过该标识进行材料验证
     */
    public Map<String, Object> highShotMeterMaterialValid(HttpServletRequest request, String caseOid) throws Exception {
        Map<String, Object> jsonObject = new HashMap<String, Object>();
        if (StrUtil.isBlank(caseOid)) {
            jsonObject.put("code", FaStaticParam.INTER_STATUS_CODE_PARAM_EXCEPTION);
            jsonObject.put("message", "办件唯一标识[caseOid]不能为空！");
            return jsonObject;
        }
        //办件信息
        ApiResultSet<QlCase> resultSet = qlCaseServiceService.queryQlCaseByCaseOid(caseOid);
        QlCase qlCase = null;
        if (resultSet.getData() != null) {
            qlCase = resultSet.getData();
        }
        String serviceOid = qlCase.getServiceOid();
        List<FaMaterialPicOcrResult> ocrResultList = new ArrayList<FaMaterialPicOcrResult>();
        ApiResultSet<List<SxServiceMaterial>> sxmResult = sxServiceMaterialService.getSxServiceMaterialListByServiceOid(serviceOid);
        List<SxServiceMaterial> serviceMaterialList = null;
        if (sxmResult.getData() != null) {
            serviceMaterialList = sxmResult.getData();
        }
        for (SxServiceMaterial serviceMaterial : serviceMaterialList) {
            FaMaterialPicOcrResult faMaterialPicOcrResult = new FaMaterialPicOcrResult();
            faMaterialPicOcrResult.setUuid(caseOid);
            faMaterialPicOcrResult.setMaterialOid(serviceMaterial.getMaterialOid());
            List<FaMaterialPicOcrResult> faMaterialPicOcrResultList = this.faMaterialPicOcrResultManager.getFaMaterialPicOcrResultByFmpor(faMaterialPicOcrResult);
            if (!CollUtil.isEmpty(faMaterialPicOcrResultList)) {
                ocrResultList.addAll(faMaterialPicOcrResultList);
            }
        }

        if (ocrResultList == null || ocrResultList.size() < 1) {
            jsonObject.put("code", FaStaticParam.INTER_STATUS_CODE_PARAM_EXCEPTION);
            jsonObject.put("message", "办件唯一标识[uuid]未查询到已上传的材料信息！");
            return jsonObject;
        }

        //无验证规则
        FaModelRuleValidation faModelRuleValidation = new FaModelRuleValidation();
        faModelRuleValidation.setServiceOid(serviceOid);
        List<FaModelRuleValidation> ruleValidList = this.faModelRuleValidationManager.queryFaModelRuleValidationList(faModelRuleValidation);
        /* List<FaModelRuleValidation> ruleValidList = faModelRuleService.queryValidRuleByServiceOid(serviceOid);*/
        if (ruleValidList == null || ruleValidList.size() < 1) {
            jsonObject.put("code", FaStaticParam.INTER_STATUS_CODE_SUCCESS);
            return jsonObject;
        }
        // SET集合(元素：目录子项编号+"_"+材料主键组成的唯一标识)，用于过滤同一页材料多次上传产生的识别记录
        Set<String> cataCodeSet = new HashSet<>();
        Map<String, String> blockMap = new HashMap<String, String>();
        for (FaMaterialPicOcrResult ocrResult : ocrResultList) {
            String cataCode = ocrResult.getCataCode();
            String materialOid = ocrResult.getMaterialOid();
            if (StrUtil.isBlank(cataCode)) {
                continue;
            }
            // 目录子项编号已存在，进入下一次循环
            if (cataCodeSet.contains(cataCode + "_" + materialOid)) {
                continue;
            }
            cataCodeSet.add(cataCode + "_" + materialOid);
            if (StrUtil.isBlank(ocrResult.getResultJson())) {
                continue;
            }
            org.json.JSONObject resultJsonObject = new org.json.JSONObject(ocrResult.getResultJson());
            org.json.JSONArray resultArray = resultJsonObject.getJSONArray("result");
            if (resultArray == null || resultArray.length() <= 0) {
                continue;
            }
            for (int i = 0; i < resultArray.length(); i++) {
                org.json.JSONObject blockJsonObject = resultArray.getJSONObject(i);
                if (!blockJsonObject.has("picPath") || StrUtil.isBlank(blockJsonObject.getString("picPath"))) {
                    if (blockJsonObject.has("words")) {
                        blockMap.put(blockJsonObject.getString("code") + "_" + ocrResult.getMaterialOid(), blockJsonObject.getString("words"));
                    } else {
                        blockMap.put(blockJsonObject.getString("code") + "_" + ocrResult.getMaterialOid(), "");
                    }
                } else {
                    blockMap.put(blockJsonObject.getString("code") + "_" + ocrResult.getMaterialOid() + "-pic", blockJsonObject.getString("picPath"));
                }
            }

            ocrResult.setResultJsonObject(resultJsonObject);
        }

        jsonObject.put("code", FaStaticParam.INTER_STATUS_CODE_SUCCESS);
        jsonObject.put("result", ruleValidNew(request, caseOid, ruleValidList, blockMap));


        return jsonObject;
    }


    /**
     * 规则验证
     *
     * @param ruleValidList 规则列表
     * @param blockMap      验证对象
     * @return 验证结果
     * @throws Exception
     */
    private List<Map<String, Object>> ruleValid(HttpServletRequest request, String caseOid, List<FaModelRuleValidation> ruleValidList, Map<String, String> blockMap)
            throws Exception {

        List<Map<String, Object>> validResultArray = new ArrayList<Map<String, Object>>();
        for (FaModelRuleValidation ruleValid : ruleValidList) {
            String validateType = ruleValid.getValidateType();
            String metadataCode = ruleValid.getTemplateMetadataCode();
            String materialOid = ruleValid.getSxMaterialOid();
            String words = null, picPath = null;
            if (blockMap.containsKey(metadataCode + "_" + materialOid)) {
                words = blockMap.get(metadataCode + "_" + materialOid);
                // 如果words为null，设置为空字符串
                words = words == null ? "" : words;
            } else if (blockMap.containsKey(metadataCode + "_" + materialOid + "-pic")) {
                picPath = blockMap.get(metadataCode + "_" + materialOid + "-pic");
                picPath = picPath == null ? "" : picPath;
            } else {
                words = words == null ? "" : words;
            }

            Map<String, Object> validResult = new HashMap<String, Object>();
            validResult.put("materialOid", ruleValid.getSxMaterialOid());
            validResult.put("catalogOid", ruleValid.getCatalogOid());
            validResult.put("code", ruleValid.getTemplateMetadataCode());
            validResult.put("name", ruleValid.getTemplateMetadataName());
            switch (validateType) {
                case FaStaticParam.VALIDATION_TYPE_NOTNULL:
                    validResult.put("memo", FaStaticParam.VALIDATION_TYPE.get(FaStaticParam.VALIDATION_TYPE_NOTNULL));
                    if (StrUtil.isNotBlank(picPath)) { // 图片的非空验证
                        validResult.put("picPath", picPath);
                        PictureHandle picHandle = new HighShotMeterPictureHandle(picPath);
                        if (!picHandle.isPicBlank()) {
                            validResult.put("valid", true);
                        } else {
                            validResult.put("valid", false);
                        }
                        picHandle.releaseMat();
                    } else {
                        validResult.put("words", words);
                        if (StrUtil.isBlank(words)) {
                            validResult.put("valid", false);
                        } else {
                            validResult.put("valid", true);
                        }
                    }
                    break;
                case FaStaticParam.VALIDATION_TYPE_NULL:
                    validResult.put("memo", FaStaticParam.VALIDATION_TYPE.get(FaStaticParam.VALIDATION_TYPE_NULL));
                    if (StrUtil.isNotBlank(picPath)) { // 图片的非空验证
                        validResult.put("picPath", picPath);
                        PictureHandle picHandle = new HighShotMeterPictureHandle(picPath);
                        if (!picHandle.isPicBlank()) {
                            validResult.put("valid", false);
                        } else {
                            validResult.put("valid", true);
                        }
                        picHandle.releaseMat();
                    } else {
                        validResult.put("words", words);
                        if (StrUtil.isNotBlank(words)) {
                            validResult.put("valid", false);
                        } else {
                            validResult.put("valid", true);
                        }
                    }
                    break;
                case FaStaticParam.VALIDATION_TYPE_CONTENT:
                    String content = ruleValid.getThanContent();
                    // 如果content为null，设置为空字符串
                    content = content == null ? "" : content;
                    validResult.put("memo", "字符必须为" + content + "！");
                    if (!content.equals(words)) {
                        validResult.put("valid", false);
                    } else {
                        validResult.put("valid", true);
                    }
                    break;
                case FaStaticParam.VALIDATION_TYPE_STR_CONTAIN:
                    // 老年优待证办理材料校验
                    boolean type = judgeOldServiceCondition(request, FaStaticParam.VALIDATION_TYPE_STR_CONTAIN, ruleValid.getServiceOid(), words, validResult);
                    if (type) {
                        // 不需要验证
                        break;
                    }
                    String containContent = ruleValid.getThanContent();
                    // 如果containContent为null，设置为空字符串
                    containContent = containContent == null ? "" : containContent;
                    validResult.put("memo", "字符必须包含为" + containContent + "！");
                    if (!words.contains(containContent)) {
                        validResult.put("valid", false);
                    } else {
                        validResult.put("valid", true);
                    }
                    break;
                case FaStaticParam.VALIDATION_TYPE_RANGE:
                    String contentDown = ruleValid.getContentDown();
                    String contentUp = ruleValid.getContentUp();
                    // 如果containContent为null，设置为空字符串
                    contentDown = contentDown == null ? "" : contentDown;
                    contentUp = contentUp == null ? "" : contentUp;
                    try {
                        // 老年优待证办理材料校验
                        boolean rel = judgeOldServiceCondition(request, FaStaticParam.VALIDATION_TYPE_RANGE, ruleValid.getServiceOid(), words, validResult);
                        if (rel) {
                            break;
                        }
                        validResult.put("memo", "数字范围必须在" + contentDown + "和" + contentUp + "之间！");
                        if (StrUtil.isBlank(words)) {
                            validResult.put("valid", false);
                            validResult.put("memo", "字符不能为空，数字范围必须在" + contentDown + "和" + contentUp + "之间！");
                            break;
                        }

                        Integer cDown = StrUtil.isNotBlank(contentDown) ? Integer.parseInt(contentDown) : null;
                        Integer cUp = StrUtil.isNotBlank(contentUp) ? Integer.parseInt(contentUp) : null;
                        Integer c = StrUtil.isNotBlank(words) ? Integer.parseInt(words) : null;

                        if (cDown != null && c >= cDown) {
                            if (cUp != null && c <= cUp) {
                                validResult.put("valid", true);
                            } else {
                                validResult.put("valid", false);
                            }
                        } else {
                            validResult.put("valid", false);
                        }
                    } catch (Exception e) {
                        validResult.put("valid", false);
                        validResult.put("memo", "字符必须是数字！");
                    }

                    break;
                case FaStaticParam.VALIDATION_TYPE_DATE_COMPARE:
                    String contentDateFormat = ruleValid.getContentDateFormat();
                    String contentDateValue = ruleValid.getContentDateValue();
                    // 如果containContent为null，设置为空字符串
                    contentDateFormat = contentDateFormat == null ? "" : contentDateFormat;
                    contentDateValue = contentDateValue == null ? "" : contentDateValue;
                    Date compareDate = new Date();
                    try {
                        if (StrUtil.isBlank(words)) {
                            validResult.put("valid", false);
                            validResult.put("memo", "字符不能为空！");
                            break;
                        }

                        if (!"CURRENT_DATE".equals(contentDateValue.toUpperCase())) {
                            compareDate = DateUtil.parse(contentDateValue, contentDateFormat);
                        }
                        validResult.put("memo", "日期比较，日期格式：" + contentDateFormat + "，比对日期：" + DateUtil.format(compareDate, contentDateFormat) + "！");

                        //将验证字符串转化为日期类型进行比对
                        Date wDate = DateUtil.parse(words, contentDateFormat);

                        if (wDate.after(compareDate)) {
                            validResult.put("valid", true);
                        } else {
                            validResult.put("valid", false);
                        }
                    } catch (Exception e) {
                        validResult.put("valid", false);
                        validResult.put("memo", "字符无法转化为日期！");
                    }
                    break;
                case FaStaticParam.VALIDATION_TYPE_CONTENT_THAN:
                    String thanContent = blockMap.get(ruleValid.getThanTemplateMetadataCode() + "_" + ruleValid.getThanSxMaterialOid());
                    //MaterialCatalog thanCatalog =this.materialCatalogManager.getMaterialCatalogByOid(ruleValid.getThanCatalogOid());
                    ApiResultSet<MaterialCatalog> catalog = materialCatalogService.getMaterialCatalogByOid(ruleValid.getThanCatalogOid());
                    MaterialCatalog thanCatalog = null;
                    if (catalog != null && catalog.getData() != null) {
                        thanCatalog = catalog.getData();
                    }
                    // 如果thanContent为null, 比较元素不存在（对应材料不需要上传情况）不进行比较
                    if (thanContent == null) {
                        validResult.put("valid", true);
                    } else {
                        // 如果thanContent为null，设置为空字符串
                        //thanContent = thanContent == null ? "" : thanContent;
                        validResult.put("memo", ruleValid.getTemplateMetadataName() + "[" + ruleValid.getCatalogName() + "]与"
                                + ruleValid.getThanTemplateMetadataName() + "["
                                + thanCatalog.getCatalogName() + "]必须相同！");

                        if (!thanContent.equals(words)) {
                            validResult.put("valid", false);
                        } else {
                            validResult.put("valid", true);
                        }
                    }
                    break;
                case FaStaticParam.VALIDATION_TYPE_SIMILARITY:
                    /* Double similar*/
                    String similarold = ruleValid.getSimilar();
                    Double similar = Double.parseDouble(similarold);
                    validResult.put("memo",
                            ruleValid.getTemplateMetadataName() + "[" + ruleValid.getTemplateMetadataCode() + "]与"
                                    + ruleValid.getThanTemplateMetadataName() + "["
                                    + ruleValid.getThanTemplateMetadataCode() + "]的相识度必须大于" + similar + "%！");
                    // 相似度比对
                    if (StrUtil.isBlank(picPath)) {
                        validResult.put("valid", false);
                        break;
                    }
                    String thanPicPath = blockMap.get(ruleValid.getThanTemplateMetadataCode() + "_" + materialOid + "-pic");
                    if (StrUtil.isBlank(thanPicPath)) {
                        validResult.put("valid", false);
                        break;
                    }
                    if (OpenCVImageHandle.similarCalc(picPath, thanPicPath) < similar) {
                        validResult.put("valid", false);
                    } else {
                        validResult.put("valid", true);
                    }
                    break;
                case FaStaticParam.VALIDATION_TYPE_HAND_SIGN:
                    if (StrUtil.isBlank(picPath)) {
                        validResult.put("valid", false);
                        validResult.put("memo", "必须为手写！");
                        break;
                    }
                    Double outlineLength = OpenCVImageHandle.calcPicOutlineLength(picPath);
                    if (outlineLength > 50) {
                        validResult.put("valid", false);
                    } else {
                        validResult.put("valid", true);
                    }
                    break;
                default:
                    break;
            }
            validResultArray.add(validResult);
        }
        return validResultArray;
    }

    /**
     * 老年优待证办理材料校验
     *
     * @param type        规则校验
     * @param serviceOid  事项OID
     * @param idNumber    身份证号
     * @param validResult 校验结果
     * @return 返回结果为true 校验成功
     * @throws Exception
     * @author cbc
     * @date 2019年6月18日
     */
    private boolean judgeOldServiceCondition(HttpServletRequest request, String type, String serviceOid, String idNumber, Map<String, Object> validResult) throws Exception {
        if (StrUtil.isBlank(serviceOid)) {
            return false;
        }
        if (!ExtendStaticParameter.SERVICE_OLD_OID.contains(serviceOid)) {
            return false;
        }
        HttpSession httpSession = request.getSession();
        if (FaStaticParam.VALIDATION_TYPE_STR_CONTAIN.equals(type)) {
            // 获取选项信息
            String optionOids = (String) httpSession.getAttribute(FrontBaseStaticParameter.OPTION_OIDS);
            if (StrUtil.isBlank(optionOids)) {
                return false;
            }
            // 需要验证是本地户口
            boolean judgeType = false;
            String caseOptionOids = ExtendStaticParameter.CASE_LICE_OPTION_NO_OID + "," + ExtendStaticParameter.CASE_LICE_OPTION_YES_OID;
            for (String str : caseOptionOids.split(",")) {
                if (optionOids.contains(str)) {
                    judgeType = true;
                    break;
                }
            }
            if (judgeType) {
                validResult.put("valid", true);
                return false;
            }
            return true;
        }
        // 获取session信息
        String ageCondition = ExtendStaticParameter.CASE_OPTION_AGE_CONDITION;
        Integer ageNum = (Integer) httpSession.getAttribute(ageCondition);
        Integer age = 0;
        if (StrUtil.isNotBlank(idNumber)) {
            // 获取身份证年龄
            Map<String, Object> map = new HashMap<String, Object>();
            if (idNumber.length() > 15) {
                map = IdcardUtil.getCarInfoEiw(idNumber);
            } else {
                map = IdcardUtil.getCarInfoFiw(idNumber);
            }
            age = (Integer) map.get("age");
            boolean valid = (age > ageNum);
            validResult.put("memo", "申请人年龄" + age + "必须大于" + ageNum);
            validResult.put("valid", valid);
        } else {
            validResult.put("memo", "申请人年龄" + age + "必须大于" + ageNum);
            validResult.put("valid", false);
        }
        return true;
    }


    public Map<String, Object> highShotMeterMaterialValidold(HttpServletRequest request, String caseOid) throws Exception {
        Map<String, Object> jsonObject = new HashMap<String, Object>();
        if (StrUtil.isBlank(caseOid)) {
            jsonObject.put("code", FaStaticParam.INTER_STATUS_CODE_PARAM_EXCEPTION);
            jsonObject.put("message", "办件唯一标识[caseOid]不能为空！");
            return jsonObject;
        }
        //办件信息
        ApiResultSet<QlCase> resultSet = qlCaseServiceService.queryQlCaseByCaseOid(caseOid);
        QlCase qlCase = null;
        if (resultSet.getData() != null) {
            qlCase = resultSet.getData();
        }
        String serviceOid = qlCase.getServiceOid();
        List<FaMaterialPicOcrResult> ocrResultList = new ArrayList<FaMaterialPicOcrResult>();

        ApiResultSet<List<SxServiceMaterial>> sxmResult = sxServiceMaterialService.getSxServiceMaterialListByServiceOid(serviceOid);
        List<SxServiceMaterial> serviceMaterialList = null;
        if (sxmResult.getData() != null) {
            serviceMaterialList = sxmResult.getData();
        }
        //List<ServiceMaterial> serviceMaterialList = serviceMaterialDao.queryServiceMaterialListByServiceOid(serviceOid);
       /* for (SxServiceMaterial serviceMaterial : serviceMaterialList) {
            List<FaMaterialPicOcrResult> faMaterialPicOcrResultList = faMaterialPicOcrResultDao
                    .getFaMaterialPicOcrResultByUuidAndMaterialOid(caseOid, serviceMaterial.getOid());
            if (!CollUtil.isEmpty(faMaterialPicOcrResultList)) {
                ocrResultList.addAll(faMaterialPicOcrResultList);
            }
        }*/

        if (ocrResultList == null || ocrResultList.size() < 1) {
            jsonObject.put("code", FaStaticParam.INTER_STATUS_CODE_PARAM_EXCEPTION);
            jsonObject.put("message", "办件唯一标识[uuid]未查询到已上传的材料信息！");
            return jsonObject;
        }

        //无验证规则
      /*  List<FaModelRuleValidation> ruleValidList = faModelRuleService.queryValidRuleByServiceOid(serviceOid);
        if (ruleValidList == null || ruleValidList.size() < 1) {
            jsonObject.put("code", FaStaticParam.INTER_STATUS_CODE_SUCCESS);
            return jsonObject;
        }*/
        // SET集合(元素：目录子项编号+"_"+材料主键组成的唯一标识)，用于过滤同一页材料多次上传产生的识别记录
        Set<String> cataCodeSet = new HashSet<>();
        Map<String, String> blockMap = new HashMap<String, String>();
        for (FaMaterialPicOcrResult ocrResult : ocrResultList) {
            String cataCode = ocrResult.getCataCode();
            String materialOid = ocrResult.getMaterialOid();
            if (StrUtil.isBlank(cataCode)) {
                continue;
            }
            // 目录子项编号已存在，进入下一次循环
            if (cataCodeSet.contains(cataCode + "_" + materialOid)) {
                continue;
            }
            cataCodeSet.add(cataCode + "_" + materialOid);
            if (StrUtil.isBlank(ocrResult.getResultJson())) {
                continue;
            }
          /*  org.json.JSONObject resultJsonObject = new org.json.JSONObject(ocrResult.getResultJson());
            org.json.JSONArray resultArray = resultJsonObject.getJSONArray("result");
            if (resultArray == null || resultArray.length() <= 0) {
                continue;
            }*/
     /*       for (int i = 0; i < resultArray.length(); i++) {
                org.json.JSONObject blockJsonObject = resultArray.getJSONObject(i);
                if (!blockJsonObject.has("picPath") || StrUtil.isBlank(blockJsonObject.getString("picPath"))) {
                    if (blockJsonObject.has("words")) {
                        blockMap.put(blockJsonObject.getString("code") + "_" + ocrResult.getMaterialOid(), blockJsonObject.getString("words"));
                    } else {
                        blockMap.put(blockJsonObject.getString("code") + "_" + ocrResult.getMaterialOid(), "");
                    }
                } else {
                    blockMap.put(blockJsonObject.getString("code") + "_" + ocrResult.getMaterialOid() + "-pic", blockJsonObject.getString("picPath"));
                }
            }*/

            /*ocrResult.setResultJsonObject(resultJsonObject);*/
        }

      /*  jsonObject.put("code", FaStaticParam.INTER_STATUS_CODE_SUCCESS);
        jsonObject.put("result", ruleValid(request, caseOid, ruleValidList, blockMap));*/

        return jsonObject;
    }


    public Map<String, Object> viewResult(HttpServletRequest request, String caseOid, String caseMaterialOid, String caseFileRecOid, String catalogOid) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("success", false);
        //办件材料记录
     /*   ApiResultSet<QlCaseMaterialAtta> qlCaseMaterialAttaResult=qlCaseMaterialAttaServiceService.queryQlCaseMaterialAttaByOid(caseFileRecOid);
        QlCaseMaterialAtta qlCaseMaterialAtta;
        if (null==qlCaseMaterialAttaResult.getData()) {
            map.put("data", "办件材料记录不存在");
            return map;
        }else{
            qlCaseMaterialAtta=qlCaseMaterialAttaResult.getData();
            //attaOid=qlCaseMaterialAtta.getAttaOid();
        }*/
        String materialOid = null;
        QlCaseMaterial qlCaseMaterial = null;
        //办件材料
        ApiResultSet<QlCaseMaterial> qlresult = qlCaseMaterialServiceService.queryMaterialByCaseMaterialOid(caseMaterialOid);
        if (String.valueOf(qlresult.getCode()).equals(FaStaticParam.HTTP_REQUEST_CODE_SUCCESS)) {
            qlCaseMaterial = qlresult.getData();
            materialOid = qlCaseMaterial.getMaterialOid();
        }

        String cataOid = null;
        SxServiceMaterial sxServiceMaterial = null;
        ApiResultSet<SxServiceMaterial> sxmResult = sxServiceMaterialService.getSxServiceMaterialByOid(materialOid);
        /*if(null!=sxmResult.getData()){*/
        if (String.valueOf(sxmResult.getCode()).equals(FaStaticParam.HTTP_REQUEST_CODE_SUCCESS)) {
            sxServiceMaterial = sxmResult.getData();
            //cataOid=sxServiceMaterial.getMaterialCatalogOid();
        } else {
            map.put("data", "事项材料记录不存在");
        }

        //MaterialCatalog faModelMaterialCatalog=this.materialCatalogManager.getMaterialCatalogByOid(catalogOid);
        ApiResultSet<MaterialCatalog> logcata = materialCatalogService.getMaterialCatalogByOid(catalogOid);
        MaterialCatalog faModelMaterialCatalog = null;
        if (logcata != null && logcata.getData() != null) {
            faModelMaterialCatalog = logcata.getData();
        }
        if (faModelMaterialCatalog == null) {
            map.put("data", "目录信息不存在");
            return map;
        }

        ApiResultSet<QlCaseMaterialAtta> qlCaseMaterialAttaResult = qlCaseMaterialAttaServiceService.
                queryQlCaseMaterialAttaByCatalogOid(qlCaseMaterial.getCaseMaterialOid(), catalogOid);
        QlCaseMaterialAtta qlCaseMaterialAtta;
        if (null == qlCaseMaterialAttaResult.getData()) {
            map.put("data", "办件材料记录不存在");
            return map;
        } else {
            qlCaseMaterialAtta = qlCaseMaterialAttaResult.getData();
            //attaOid=qlCaseMaterialAtta.getAttaOid();
        }


        JSONArray returnArray = new JSONArray();
        FaMaterialPicOcrResult faMaterialPicOcrResultold = new FaMaterialPicOcrResult();
        faMaterialPicOcrResultold.setUuid(caseOid);
        faMaterialPicOcrResultold.setCataCode(faModelMaterialCatalog.getCatalogCode());
        faMaterialPicOcrResultold.setMaterialOid(qlCaseMaterial.getMaterialOid());
        faMaterialPicOcrResultold.setMaterialAttaOid(qlCaseMaterialAtta.getMaterialAttaOid());
        DbFaMaterialPicOcrResult faMaterialPicOcrResult = this.faMaterialPicOcrResultManager.
                getFaMaterialPicOcrResultByFaMaterialPicOcrResult(faMaterialPicOcrResultold);
      /*  FaMaterialPicOcrResult faMaterialPicOcrResult = faMaterialPicOcrResultDao
                .getFaMaterialPicOcrResultByCaseOidAndCataCodeAndMaterialOid(caseOid, faModelMaterialCatalog.getCataCode(), caseFileRec.getMaterialOid());*/
        if (faMaterialPicOcrResult == null || StrUtil.isBlank(faMaterialPicOcrResult.getResultJson())) {
            //判断是否有印章识别
            if (faModelMaterialCatalog.getSealsNumber() != null && faModelMaterialCatalog.getSealsNumber() > 0) {
               /* ApiResultSet<QlCaseMaterialAtta> qlCaseMaterialAttaResult=qlCaseMaterialAttaServiceService.queryQlCaseMaterialAttaByCatalogOid(qlCaseMaterial.getCaseMaterialOid(),catalogOid);
                QlCaseMaterialAtta qlCaseMaterialAtta;
                if (null==qlCaseMaterialAttaResult.getData()) {
                    map.put("data", "办件材料记录不存在");
                    return map;
                }else{
                    qlCaseMaterialAtta=qlCaseMaterialAttaResult.getData();
                }*/
                // CaseFileAttaRec caseFileAttaRec = caseFileAttaRecService.queryCaseFileAttaRecListByCaseFileRecOidAndCataOid(caseFileRec.getOid(), faModelMaterialCatalog.getOid());

                if (qlCaseMaterialAtta != null) {
                    StampRecord stampRecord = this.stampRecordManager.getStampRecordByCataOidAndAttaOid(faModelMaterialCatalog.getMaterialCatalogOid(), qlCaseMaterialAtta.getAttaOid());
                    JSONObject resultObj = new JSONObject();
                    resultObj.put("name", "印章检测");
                    if (stampRecord == null || stampRecord.getAmount() == 0) {
                        resultObj.put("words", "未通过");
                        resultObj.put("memo", "印章数低于" + faModelMaterialCatalog.getSealsNumber());
                    } else {
                        resultObj.put("words", "通过");
                    }
                    returnArray.add(resultObj);
                }
                map.put("success", true);
                map.put("data", returnArray);
                return map;
            } else {
                map.put("data", "识别结果信息不存在");
            }
            return map;
        }
        JSONObject resultJsonObject = JSONObject.parseObject(faMaterialPicOcrResult.getResultJson());
        JSONArray ocrResultArray = JSONArray.parseArray(resultJsonObject.get("result").toString());

        Map<String, String> mapResult = new HashMap<>();
        // 办件记录对应事项材料没有绑定材料审核目录，说明该材料不需要智能审核，直接返回
        if (StrUtil.isBlank(catalogOid)) {
            returnArray = ocrResultArray;
        } else {
            Map<String, Object> validMap = highShotMeterMaterialValid(request, caseOid);
            List<Map<String, Object>> validResultList = null;
            if (validMap.get("result") != null) {
                validResultList = (List<Map<String, Object>>) validMap.get("result");
            }
            for (Object o : ocrResultArray) {
                if (null == o) {
                    continue;
                }
                JSONObject resultObj = JsonUtil.objToJSONObject(o);
                if (validResultList != null) {
                    for (Map<String, Object> resultMap : validResultList) {
                        if (null == resultMap) {
                            continue;
                        }
                        if (materialOid.equals(resultMap.get("materialOid"))
                                && resultObj.get("name").equals(resultMap.get("name"))) {
                            resultObj.put("valid", resultMap.get("valid"));
                            if (resultMap.get("valid") != null && (Boolean) resultMap.get("valid") == false) {
                                String memo = resultMap.get("memo").toString();
                                //resultObj.remove("memo");
                                if (mapResult.containsKey(materialOid + resultObj.get("name").toString())) {
                                    memo = mapResult.get(mapResult) == null ? memo : mapResult.get(mapResult) + ";" + memo;
                                    mapResult.put(materialOid + resultObj.get("name").toString(), memo);
                                } else {
                                    mapResult.put(materialOid + resultObj.get("name").toString(), resultMap.get("memo").toString());
                                }
                                resultObj.put("memo", memo);
                            }
                        }
                    }
                }
                returnArray.add(resultObj);
            }
        }
        if (faModelMaterialCatalog.getSealsNumber() != null && faModelMaterialCatalog.getSealsNumber() > 0) {
       /*     ApiResultSet<QlCaseMaterialAtta> qlCaseMaterialAttaResult=qlCaseMaterialAttaServiceService.queryQlCaseMaterialAttaByCatalogOid(qlCaseMaterial.getCaseMaterialOid(),catalogOid);
            QlCaseMaterialAtta qlCaseMaterialAtta;
            if (null==qlCaseMaterialAttaResult.getData()) {
                map.put("data", "办件材料记录不存在");
                return map;
            }else{
                qlCaseMaterialAtta=qlCaseMaterialAttaResult.getData();
            }*/
            // CaseFileAttaRec caseFileAttaRec = caseFileAttaRecService.queryCaseFileAttaRecListByCaseFileRecOidAndCataOid(caseFileRec.getOid(), faModelMaterialCatalog.getOid());
            if (qlCaseMaterialAtta != null) {
                //StampRecord stampRecord = stampRecordDao.getStampRecordByCataOidAndAttaOid(faModelMaterialCatalog.getOid(), caseFileAttaRec.getAtta().getOid());
                List<StampRecord> stampRecord = this.stampRecordManager.getCaseFileAttaRecListByOidAndCataOid(faModelMaterialCatalog.getMaterialCatalogOid(), qlCaseMaterialAtta.getAttaOid());
                JSONObject resultObj = new JSONObject();
                resultObj.put("name", "印章检测");
                if (stampRecord == null || (stampRecord != null && stampRecord.get(0).getAmount() == 0)) {
                    resultObj.put("words", "未通过");
                    resultObj.put("memo", "印章数低于" + faModelMaterialCatalog.getSealsNumber());
                } else {
                    resultObj.put("words", "通过");
                }
                returnArray.add(resultObj);
            }

        }
        map.put("success", true);
        map.put("data", returnArray);
        return map;
    }


    /**
     * 预审
     *
     * @param request
     * @param caseOid
     * @param caseMaterialOid
     * @param caseFileRecOid
     * @param attaOid
     * @param cataOid
     * @param materialOid
     * @return
     * @throws Exception
     */
    public Map<String, Object> materialPrePrialNew(HttpServletRequest request, String caseOid, String caseMaterialOid, String caseFileRecOid,
                                                   String attaOid, String cataOid, String materialOid)
            throws Exception {
        Map<String, Object> ocrResultMap = new HashMap<String, Object>();
        //办件材料记录
        ApiResultSet<QlCaseMaterialAtta> qlCaseMaterialAttaResult = qlCaseMaterialAttaServiceService.queryQlCaseMaterialAttaByOid(caseFileRecOid);
        QlCaseMaterialAtta qlCaseMaterialAtta;
        SysAtta qlSysAtta;
        if (null == qlCaseMaterialAttaResult.getData()) {
            log.error("材料预审", "办件材料记录不存在！");
            ocrResultMap.put("success", false);
            ocrResultMap.put("message", "办件材料记录不存在!");
            return ocrResultMap;
        } else {
            qlCaseMaterialAtta = qlCaseMaterialAttaResult.getData();
            attaOid = qlCaseMaterialAtta.getAttaOid();
            ApiResultSet<SysAtta> sysAttaresult = sysAttaService.getSysAttaByAttaOid(attaOid);
            qlSysAtta = sysAttaresult.getData();
            //cataOid=qlCaseMaterialAtta.getMaterialCatalogOid();
        }
        if (StringUtils.isEmpty(cataOid)) {
            log.error("材料预审", "没有关联目录材料！");
            ocrResultMap.put("success", false);
            ocrResultMap.put("message", "没有关联目录材料!");
            return ocrResultMap;
        }
        //办件材料
        ApiResultSet<QlCaseMaterial> qlresult = qlCaseMaterialServiceService.queryMaterialByCaseMaterialOid(caseMaterialOid);
        QlCaseMaterial qlCaseMaterial = null;
        if (String.valueOf(qlCaseMaterialAttaResult.getCode()).equals(FaStaticParam.HTTP_REQUEST_CODE_SUCCESS)) {
            if (null != qlresult.getData()) {
                qlCaseMaterial = qlresult.getData();
                materialOid = qlCaseMaterial.getMaterialOid();
            }
        }
        String parentCataOid = qlCaseMaterial.getMaterialCatalogOid();

        // 获得目录子项
        ApiResultSet<List<MaterialCatalog>> list = materialCatalogService.queryList(parentCataOid);
        List<MaterialCatalog> cataList = null;
        if (list != null && list.getData() != null && list.getData().size() > 0) {
            cataList = list.getData();
        }
        //List<MaterialCatalog> cataList=this.materialCatalogManager.queryList(parentCataOid);
        if (cataList == null || cataList.size() == 0) {
            log.error("材料预审", "事项材料关联目录不正确！");
            ocrResultMap.put("success", false);
            ocrResultMap.put("message", "事项材料关联目录不正确!");
            return ocrResultMap;
            /*throw new Exception("事项材料关联目录不正确!");*/
        }
        //cataOid
        Map<String, MaterialCatalog> materialCatalogMap = cataList.stream().collect(Collectors.toMap(MaterialCatalog::getMaterialCatalogOid, a -> a, (k1, k2) -> k1));
        // 按照设计，只有一个子项
        MaterialCatalog cata = materialCatalogMap.get(cataOid);
        if (cata == null) {
            log.error("材料预审", "事项材料关联目录不正确！");
            ocrResultMap.put("success", false);
            ocrResultMap.put("message", "事项材料关联目录不正确!");
            return ocrResultMap;
        }

        //附件信息
        String fastdfsNginxUrl = qlSysAtta.getFastdfsNginxUrl();
        log.info("获取附件成功：{}", JSON.toJSONString(qlSysAtta));
        String fileUrl = FaStaticParam.PROJECT_PATH + "\\modelTemples\\model";
        String dirPath = File.separator + "pic" + File.separator + "hsm" + File.separator;
        String picName = materialOid + "_" + cata.getCatalogCode() + "_" + DateUtil.format(new Date(), "yyyyMMddHHmmssSSS") + "_"
                + StringUtil.random(6) + ".jpg";
        String picPath = fileUrl + dirPath + picName;
        File file = new File(picPath);
        if (!file.exists()) {
            File dir = new File(dirPath);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            //复制文件系统中的附件到本地系统
            HttpUtil.downloadFile(fastdfsNginxUrl, cn.hutool.core.io.FileUtil.file(picPath));
        }
        // String base64=FileManageUtil.getImgFromUrl(fastdfsNginxUrl);
        //AttaBase64Result attaBase64Result = FileManageUtil.getAttaBase64(attaOid);
        AttaBase64Result attaBase64Result = getAttaBase64ByUrl(fastdfsNginxUrl);
        //AttaBase64Result attaBase64Result=getAttaBase64New(attaOid);
        String picBase64 = "";
        if (null != attaBase64Result) {
            picBase64 = attaBase64Result.getResult();
        } else {
            log.error("材料预审", "未获取附件base64信息！办件主键为：{}，办件材料记录主键为：{}，附件主键为：{}，目录元素主键为：{}", caseOid, caseFileRecOid, attaOid, cataOid);
            ocrResultMap.put("success", false);
            ocrResultMap.put("message", "未获取附件base64信息!");
            return ocrResultMap;
        }

        List<Map<String, Object>> blockJsonArray = new ArrayList<Map<String, Object>>();
        // 验证印章 start
      /*  if (cata.getSealsNumber()!= null && cata.getSealsNumber() > 0) {
            this.checkSealCount(picBase64, cata, caseOid, caseFileRecOid, attaOid);
        }*/
        // 验证印章 end
        // 材料审核目录绑定特定识别类型
        if (FaStaticParam.LICE_OCR_TYPE_BUSINESSLICE.equals(cata.getMaterialIdentificationTypeOid())) {
            // 营业执照
            BusinessLiceInfo businessLiceInfo = this.businessLiceManager.getBusinessLiceInfo(picBase64);
            boolean flag = this.allFieldIsNull(businessLiceInfo);//判断是不是所有属性均为空
            if (flag == true) {
                log.error("材料预审", "营业执照识别失败！办件主键为：{}，办件材料记录主键为：{}，附件主键为：{}，目录元素主键为：{}", attaOid, cataOid);
                ocrResultMap.put("success", false);
                ocrResultMap.put("message", "营业执照识别失败,请检查附件!");
                return ocrResultMap;

            }
            Map<String, Object> businessLiceMap = this.businessLiceManager.discernBusinessBaiDu(businessLiceInfo, materialOid, cata);
            String code = (String) businessLiceMap.get(FaStaticParam.CODE);
            if (FaStaticParam.HTTP_REQUEST_CODE_SUCCESS.equals(code)) {
                String resultJson = new JSONObject(businessLiceMap).toString();
                saveResult(caseOid, materialOid, qlCaseMaterialAtta.getMaterialAttaOid(), cata.getCatalogCode(), attaOid, CommonUtil.md5(picBase64), resultJson,
                        businessLiceInfo);
                ocrResultMap.put("success", true);
                ocrResultMap.put("message", "营业执照识别成功!");
                return ocrResultMap;
            } else {
                String message = businessLiceMap.get(FaStaticParam.MESSAGE).toString();
                ocrResultMap.put("success", false);
                ocrResultMap.put("message", "解析营业执照对象信息失败" + message);
                return ocrResultMap;
            }
        } else if (FaStaticParam.LICE_OCR_TYPE_IDCARD_FRONT.equals(cata.getMaterialIdentificationTypeOid())) {
            // 身份证头像面接口
            ApiResultSet<IdcardInfo> apiResultSet = idcardInfoService.getIdcardFrontInfo(picBase64);
            IdcardInfo idcardInfo = apiResultSet.getData();
            boolean flag = this.allFieldIsNull(idcardInfo);//判断是不是所有属性均为空
            if (flag == true) {
                log.error("材料预审", "身份证头像面识别失败！办件主键为：{}，办件材料记录主键为：{}，附件主键为：{}，目录元素主键为：{}", attaOid, cataOid);
                ocrResultMap.put("success", false);
                ocrResultMap.put("message", "身份证头像面识别失败,请检查附件!");
                return ocrResultMap;

            }
            IdCardInfoVo vo = new IdCardInfoVo();
            vo.setIdcardInfo(idcardInfo);
            vo.setMaterialCatalog(cata);
            ApiResultSet<Map<String, Object>> mapApiResultSet = idcardInfoService.discernIdcardInfo(vo);
            Map<String, Object> idcardMap = mapApiResultSet.getData();
            String code = (String) idcardMap.get(FaStaticParam.CODE);
            if (FaStaticParam.HTTP_REQUEST_CODE_SUCCESS.equals(code)) {
                String resultJson = new JSONObject(idcardMap).toString();
                saveResult(caseOid, materialOid, qlCaseMaterialAtta.getMaterialAttaOid(), cata.getCatalogCode(), attaOid, CommonUtil.md5(picBase64), resultJson,
                        idcardInfo);
                ocrResultMap.put("success", true);
                ocrResultMap.put("message", "身份证正面识别成功!");
                return ocrResultMap;
            } else {
                String message = idcardMap.get(FaStaticParam.MESSAGE).toString();
                ocrResultMap.put("success", false);
                ocrResultMap.put("message", "解析身份证正面信息失败" + message);
                return ocrResultMap;
            }
        } else if (FaStaticParam.LICE_OCR_TYPE_IDCARD_BACK.equals(cata.getMaterialIdentificationTypeOid())) {
            // 身份证接口
            ApiResultSet<IdcardInfo> apiResultSet = idcardInfoService.getIdcardFrontInfo(picBase64);
            IdcardInfo idcardInfo = apiResultSet.getData();
            boolean flag = this.allFieldIsNull(idcardInfo);//判断是不是所有属性均为空
            if (flag == true) {
                ocrResultMap.put("success", false);
                ocrResultMap.put("message", "身份证反面识别失败,请检查附件!");
                return ocrResultMap;
            }
            IdCardInfoVo vo = new IdCardInfoVo();
            vo.setIdcardInfo(idcardInfo);
            vo.setMaterialCatalog(cata);
            ApiResultSet<Map<String, Object>> mapApiResultSet = idcardInfoService.discernIdcardInfo(vo);
            Map<String, Object> idcardMap = mapApiResultSet.getData();
            String code = (String) idcardMap.get(FaStaticParam.CODE);
            if (FaStaticParam.HTTP_REQUEST_CODE_SUCCESS.equals(code)) {
                String resultJson = new JSONObject(idcardMap).toString();
                saveResult(caseOid, materialOid, qlCaseMaterialAtta.getMaterialAttaOid(), cata.getCatalogCode(), attaOid, CommonUtil.md5(picBase64), resultJson,
                        idcardInfo);
                ocrResultMap.put("success", true);
                ocrResultMap.put("message", "身份证反面识别成功!");
                return ocrResultMap;
            } else {
                String message = idcardMap.get(FaStaticParam.MESSAGE).toString();
                ocrResultMap.put("success", false);
                ocrResultMap.put("message", "解析身份证反面信息失败" + message);
                return ocrResultMap;
            }
        } else if (FaStaticParam.LICE_OCR_TYPE_TEMP_IDCARD.equals(cata.getMaterialIdentificationTypeOid())) {
            // 临时身份证、身份证复印件接口
            ApiResultSet<IdcardInfo> apiResultSet = idcardInfoService.getIdcardFrontInfo(picBase64);
            IdcardInfo idcardInfo = apiResultSet.getData();
            boolean flag = this.allFieldIsNull(idcardInfo);//判断是不是所有属性均为空
            if (idcardInfo == null || flag == true) {
                ocrResultMap.put("success", false);
                ocrResultMap.put("message", "临时身份证识别失败,请检查附件!");
                return ocrResultMap;
            }
            IdCardInfoVo vo = new IdCardInfoVo();
            vo.setIdcardInfo(idcardInfo);
            vo.setMaterialCatalog(cata);
            ApiResultSet<Map<String, Object>> mapApiResultSet = idcardInfoService.discernIdcardInfo(vo);
            Map<String, Object> idcardMap = mapApiResultSet.getData();
            String code = (String) idcardMap.get(FaStaticParam.CODE);
            if (FaStaticParam.HTTP_REQUEST_CODE_SUCCESS.equals(code)) {
                String resultJson = new JSONObject(idcardMap).toString();
                saveResult(caseOid, materialOid, qlCaseMaterialAtta.getMaterialAttaOid(), cata.getCatalogCode(), attaOid, CommonUtil.md5(picBase64), resultJson,
                        idcardInfo);
                ocrResultMap.put("success", true);
                ocrResultMap.put("message", "临时身份证识别成功!");
                return ocrResultMap;
            } else {
                String message = idcardMap.get(FaStaticParam.MESSAGE).toString();
                ocrResultMap.put("success", false);
                ocrResultMap.put("message", "解析临时身份证信息失败" + message);
                return ocrResultMap;
            }
        } else if (FaStaticParam.LICE_OCR_TYPE_HOUSEHOLD_REGISTER.equals(cata.getMaterialIdentificationTypeOid())) {
            // 户口本接口
            HouseholdRegisterInfo householdRegisterInfo = this.householdRegisterInfoManager.getHouseholdRegister(picBase64);
            boolean flag = this.allFieldIsNull(householdRegisterInfo);//判断是不是所有属性均为空
            if (householdRegisterInfo == null || flag == true) {
                ocrResultMap.put("success", false);
                ocrResultMap.put("message", "户口本识别失败,请检查附件!");
                return ocrResultMap;
            }
            Map<String, Object> householdRegisterInfoMap = this.householdRegisterInfoManager
                    .discernHouseholdRegister(householdRegisterInfo, cata);
            String code = (String) householdRegisterInfoMap.get(FaStaticParam.CODE);
            if (FaStaticParam.HTTP_REQUEST_CODE_SUCCESS.equals(code)) {
                String resultJson = new JSONObject(householdRegisterInfoMap).toString();
                saveResult(caseOid, materialOid, qlCaseMaterialAtta.getMaterialAttaOid(), cata.getCatalogCode(), attaOid, CommonUtil.md5(picBase64), resultJson,
                        householdRegisterInfo);
                ocrResultMap.put("success", true);
                ocrResultMap.put("message", "户口本识别成功!");
                return ocrResultMap;
            } else {
                String message = householdRegisterInfoMap.get(FaStaticParam.MESSAGE).toString();
                ocrResultMap.put("success", false);
                ocrResultMap.put("message", "解析户口本信息失败" + message);
                return ocrResultMap;
            }
        }

        ocrResultMap.put("code", FaStaticParam.INTER_STATUS_CODE_SUCCESS);
        ocrResultMap.put("result", blockJsonArray);
        ocrResultMap.put("success", true);
        String resultJson = new JSONObject(ocrResultMap).toString();
        saveResult(caseOid, materialOid, qlCaseMaterialAtta.getMaterialAttaOid(), cata.getCatalogCode(), attaOid, CommonUtil.md5(picBase64), resultJson, null);
        return ocrResultMap;
    }


    /**
     * 获得预审结果VO
     *
     * @param request
     * @param caseOid
     * @return
     * @throws Exception
     */
    public List<QlCaseMaterial> getPreTrialResultVoNew(HttpServletRequest request, String caseOid, QlCase qlCase, QlCaseApplay qlCaseApplay) throws Exception {
        Map<String, Object> map = highShotMeterMaterialValidNew(request, caseOid);
        //上传材料信息
        ApiResultSet<List<QlCaseMaterial>> qlcmResult = qlCaseMaterialServiceService.queryQlCaseMaterialByCaseOid(caseOid);
        List<QlCaseMaterial> qlCaseMaterialList = new ArrayList<>();
        List<QlCaseMaterial> qlCaseMaterialListOLd = qlcmResult.getData();
        List<PreTrialResultVo> list = new ArrayList<>();
        List<Map<String, Object>> resultList = null;
        if (map.get("result") != null) {
            resultList = (List<Map<String, Object>>) map.get("result");
        }
        int i = 0;
        // 根据办件主键获取办件材料记录列表
        String resultStatus = "";
        Map<String, Object> preTrialResul = new HashMap<>();
        for (QlCaseMaterial caseFileRec : qlCaseMaterialListOLd) {
            String collectionFlag = caseFileRec.getCollectionType();//1纸质收取
            String materiaName = caseFileRec.getMaterialName();
            String materialOid = caseFileRec.getMaterialOid();
            if (StringUtils.isNotEmpty(caseFileRec.getAuditType())) {
                if (caseFileRec.getAuditType().equals("1") && StringUtils.isNotEmpty(caseFileRec.getMaterialCatalogOid())) {//判断智审方式
                    ApiResultSet<List<QlCaseMaterialAtta>> qlCaseMaterialAttaResult = qlCaseMaterialAttaServiceService.queryQlCaseMaterialAttaByCaseMaterialOid(caseFileRec.getCaseMaterialOid());
                    if (null != qlCaseMaterialAttaResult.getData()) {
                        List<QlCaseMaterialAtta> qlCaseMaterialAttaList = qlCaseMaterialAttaResult.getData();
                        if (qlCaseMaterialAttaList.size() > 0) {
                            caseFileRec.setMaterialAttaOid(qlCaseMaterialAttaList.get(0).getMaterialAttaOid());
                        }
                    }

                    resultStatus = FaStaticParam.PRE_TRIAL_RESULT_STATUS_PASS;
                    PreTrialResultVo vo = new PreTrialResultVo();
                    String materiaOid = caseFileRec.getMaterialOid();
                    String parentCatalogOid = caseFileRec.getMaterialCatalogOid();
                    if (StringUtils.isEmpty(parentCatalogOid)) {//无配置审核目录
                        preTrialResul.put("resultStatus", FaStaticParam.PRE_TRIAL_RESULT_STATUS_NOT_NEED);
                        /*vo.setResultStatus(FaStaticParam.PRE_TRIAL_RESULT_STATUS_NOT_NEED);*/
                        resultStatus = FaStaticParam.PRE_TRIAL_RESULT_STATUS_NOT_NEED;
                        // 继续循环
                    } else {
                        String materialCatalogOid = "";
                        ApiResultSet<List<MaterialCatalog>> materiallist = materialCatalogService.queryList(parentCatalogOid);
                        List<MaterialCatalog> faModelMaterialCatalogList = null;
                        if (materiallist != null && materiallist.getData() != null) {
                            faModelMaterialCatalogList = materiallist.getData();
                            MaterialCatalog materialCatalog = faModelMaterialCatalogList.get(0);
                            materialCatalogOid = materialCatalog.getMaterialCatalogOid();
                        }

                        boolean existFlag = true;
                        if (null != resultList) {
                            for (int ii = 0; ii < resultList.size(); ii++) {
                                Map<String, Object> resultMap = resultList.get(ii);
                                // 验证结果不通过，设置审核标识为1：不通过
                                if (resultMap.get("catalogOid").equals(materialCatalogOid) && resultMap.get("materialOid").equals(materiaOid) &&
                                        resultMap.get("valid") != null && !(Boolean) resultMap.get("valid")) {
                                    existFlag = false;
                                }
                            }
                        } else {
                            existFlag = false;
                        }
                        if (existFlag) {
                            preTrialResul.put("resultStatus", FaStaticParam.PRE_TRIAL_RESULT_STATUS_PASS);
                            resultStatus = FaStaticParam.PRE_TRIAL_RESULT_STATUS_PASS;
                        } else {
                            preTrialResul.put("resultStatus", FaStaticParam.PRE_TRIAL_RESULT_STATUS_NOT_PASS);
                            resultStatus = FaStaticParam.PRE_TRIAL_RESULT_STATUS_NOT_PASS;
                        }

                    }

                    caseFileRec.setResultStatus(resultStatus);
                } else {
                    //办件信息
                    String credentialNumber = "";
                    String legalPersonName = "";//法定代表人
                    String applyUserName = "";
                    if (qlCaseApplay != null) {
                        credentialNumber = qlCaseApplay.getCredentialNumber();
                        legalPersonName = qlCaseApplay.getLegalPersonName();
                        applyUserName = qlCaseApplay.getApplyUserName();
                    }
                   /* String collectionFlag=caseFileRec.getCollectionType();//1纸质收取
                    String materiaName=caseFileRec.getMaterialName();
                    String materialOid=caseFileRec.getMaterialOid();*/

                    if ((applyUserName.equals("李东阳") || applyUserName.equals("姚蕾")) && materialOid.equals("2c287b8b79f39489017a223685e11a74")) {//判断不通过
                        resultStatus = FaStaticParam.PRE_TRIAL_RESULT_STATUS_NOT_PASS;
                    } else {
                        resultStatus = FaStaticParam.PRE_TRIAL_RESULT_STATUS_PASS;
                    }

                    caseFileRec.setResultStatus(resultStatus);

                }
                if (collectionFlag.equals("1")) {//纸质收取
                    caseFileRec.setAuditType("2");
                }
                qlCaseMaterialList.add(caseFileRec);
                //caseFileRec.setPreTrialResult(preTrialResul);
            }/*else{
                qlCaseMaterialList.remove(caseFileRec);
            }*/


        }

        return qlCaseMaterialList;
    }


    /**
     * 组装预审结果vo
     *
     * @param materialName           展示的名称
     * @param caseFileRec
     * @param resultList
     * @param faModelMaterialCatalog
     * @return
     * @throws Exception
     * @author chenbw
     * @date 2019年6月8日
     */
    private PreTrialResultVo handlePreTrialResultVoNew(String materialName, QlCaseMaterial caseFileRec, List<Map<String, Object>> resultList,
                                                       MaterialCatalog faModelMaterialCatalog) throws Exception {
        PreTrialResultVo vo = new PreTrialResultVo();
        vo.setName(materialName);
        vo.setCatalogOid(faModelMaterialCatalog.getMaterialCatalogOid());
        vo.setMaterialOid(caseFileRec.getMaterialOid());
        String memoJsonStr = null;
        // 办件记录对应事项材料绑定材料审核目录,验证识别结果
        String resultStatus = FrontBaseStaticParameter.PRE_TRIAL_RESULT_STATUS_PASS;
        // 结果不存在，说明材料识别不成功，设置审核标识为1：不通过
        if (resultList == null) {
            resultStatus = FrontBaseStaticParameter.PRE_TRIAL_RESULT_STATUS_NOT_PASS;
        }

        //查询办件材料附件信息
        String materialCatalogOidAll = "";
        ApiResultSet<List<QlCaseMaterialAtta>> qlCaseMaterialAttaResult = qlCaseMaterialAttaServiceService.queryQlCaseMaterialAttaByCaseMaterialOid(caseFileRec.getCaseMaterialOid());
        List<QlCaseMaterialAtta> caseFileAttaRecList = qlCaseMaterialAttaResult.getData();
        if (CollectionUtil.isNotEmpty(caseFileAttaRecList)) {
            for (QlCaseMaterialAtta caseMaterialAttas : caseFileAttaRecList) {
                String materialCatalogOid = caseMaterialAttas.getMaterialCatalogOid();
                materialCatalogOidAll = materialCatalogOidAll + materialCatalogOid + ";";
            }
        }
        /*else if (faModelMaterialCatalog.getMultipageFlag()!=null && faModelMaterialCatalog.getMultipageFlag()==1){
            resultStatus = FrontBaseStaticParameter.PRE_TRIAL_RESULT_STATUS_NOT_NEED;
        }*/


        // 存在标识，用于记录验证结果json中是否存在事项材料对应的验证信息，默认为true
        boolean existFlag = true;
        resultStatus = FrontBaseStaticParameter.PRE_TRIAL_RESULT_STATUS_PASS;
        for (int i = 0; i < resultList.size(); i++) {
            Map<String, Object> resultMap = resultList.get(i);
            if (faModelMaterialCatalog.getMaterialCatalogOid().equals(resultMap.get("catalogOid")) && caseFileRec.getMaterialOid().equals(resultMap.get("materialOid"))) {
                // 验证结果不通过，设置审核标识为1：不通过
                if (resultMap.get("valid") != null && !(Boolean) resultMap.get("valid")) {
                    existFlag = false;
                    JSONObject memoJosn = new JSONObject();
                    if (resultMap.get("name") != null) {
                        memoJosn.put("name", resultMap.get("name").toString());
                    } else {
                        memoJosn.put("name", "");
                    }
                    if (resultMap.get("memo") != null) {
                        memoJosn.put("memo", resultMap.get("memo").toString());
                    } else {
                        memoJosn.put("memo", "");
                    }
                    memoJsonStr = memoJosn.toString();
                }
            }
        }
        if (faModelMaterialCatalog.getSealsNumber() != null && faModelMaterialCatalog.getSealsNumber() > 0) {
            /*List<CaseFileAttaRec> caseFileAttaRecList = caseFileAttaRecService.queryCaseFileAttaRecListByCaseFileRecOid(caseFileRec.getOid());*/
            if (CollectionUtil.isEmpty(caseFileAttaRecList)) {
                existFlag = false;
            } else {
                List<StampRecord> stampRecord = this.stampRecordManager.getCaseFileAttaRecListByOidAndCataOid(faModelMaterialCatalog.getMaterialCatalogOid(), caseFileAttaRecList.get(0).getAttaOid());
                if (stampRecord == null || (stampRecord != null && stampRecord.get(0).getAmount() == 0)) {
                    existFlag = false;
                }
            }

        }

        if (!existFlag) {
            resultStatus = FrontBaseStaticParameter.PRE_TRIAL_RESULT_STATUS_NOT_PASS;
        }

        vo.setResultStatus(resultStatus);
        vo.setMemoJson(memoJsonStr);
        vo.setQlCaseMaterial(caseFileRec);
        return vo;
    }


    /**
     * 图片验证
     *
     * @param request
     * @param caseOid
     * @return
     * @throws Exception
     */
    public Map<String, Object> highShotMeterMaterialValidNew(HttpServletRequest request, String caseOid) throws Exception {
        Map<String, Object> jsonObject = new HashMap<String, Object>();
        if (StrUtil.isBlank(caseOid)) {
            jsonObject.put("code", FaStaticParam.INTER_STATUS_CODE_PARAM_EXCEPTION);
            jsonObject.put("message", "办件唯一标识[caseOid]不能为空！");
            return jsonObject;
        }
        //办件信息
        ApiResultSet<QlCase> resultSet = qlCaseServiceService.queryQlCaseByCaseOid(caseOid);
        QlCase qlCase = null;
        if (resultSet.getData() != null) {
            qlCase = resultSet.getData();
        }
        String serviceOid = qlCase.getServiceOid();
        List<FaMaterialPicOcrResult> ocrResultList = new ArrayList<FaMaterialPicOcrResult>();

        ApiResultSet<List<QlCaseMaterial>> sxmResult = qlCaseMaterialServiceService.queryQlCaseMaterialByCaseOid(caseOid);


        /*ApiResultSet<List<SxServiceMaterial>> sxmResult=  sxServiceMaterialService.getSxServiceMaterialListByServiceOid(serviceOid);*/
        List<QlCaseMaterial> serviceMaterialList = null;
        if (sxmResult.getData() != null) {
            serviceMaterialList = sxmResult.getData();
        }
        for (QlCaseMaterial qlCaseMaterial : serviceMaterialList) {
            FaMaterialPicOcrResult faMaterialPicOcrResult = new FaMaterialPicOcrResult();
            faMaterialPicOcrResult.setUuid(caseOid);
            faMaterialPicOcrResult.setMaterialOid(qlCaseMaterial.getMaterialOid());
            List<FaMaterialPicOcrResult> faMaterialPicOcrResultList = this.faMaterialPicOcrResultManager.getFaMaterialPicOcrResultByFmpor(faMaterialPicOcrResult);
            if (!CollUtil.isEmpty(faMaterialPicOcrResultList)) {
                ocrResultList.addAll(faMaterialPicOcrResultList);
            }
        }

        if (ocrResultList == null || ocrResultList.size() < 1) {
            jsonObject.put("code", FaStaticParam.INTER_STATUS_CODE_PARAM_EXCEPTION);
            jsonObject.put("message", "办件唯一标识[uuid]未查询到已上传的材料信息！");
            return jsonObject;
        }

        //无验证规则
        FaModelRuleValidation faModelRuleValidation = new FaModelRuleValidation();
        faModelRuleValidation.setServiceOid(serviceOid);
        List<FaModelRuleValidation> ruleValidList = this.faModelRuleValidationManager.queryFaModelRuleValidationList(faModelRuleValidation);
        /* List<FaModelRuleValidation> ruleValidList = faModelRuleService.queryValidRuleByServiceOid(serviceOid);*/
        if (ruleValidList == null || ruleValidList.size() < 1) {
            jsonObject.put("code", FaStaticParam.INTER_STATUS_CODE_SUCCESS);
            return jsonObject;
        }
        // SET集合(元素：目录子项编号+"_"+材料主键组成的唯一标识)，用于过滤同一页材料多次上传产生的识别记录
        Set<String> cataCodeSet = new HashSet<>();
        Map<String, String> blockMap = new HashMap<String, String>();
        for (FaMaterialPicOcrResult ocrResult : ocrResultList) {
            String cataCode = ocrResult.getCataCode();
            String materialOid = ocrResult.getMaterialOid();
            if (StrUtil.isBlank(cataCode)) {
                continue;
            }
            // 目录子项编号已存在，进入下一次循环
            if (cataCodeSet.contains(cataCode + "_" + materialOid)) {
                continue;
            }
            cataCodeSet.add(cataCode + "_" + materialOid);
            if (StrUtil.isBlank(ocrResult.getResultJson())) {
                continue;
            }
            org.json.JSONObject resultJsonObject = new org.json.JSONObject(ocrResult.getResultJson());
            org.json.JSONArray resultArray = resultJsonObject.getJSONArray("result");
            if (resultArray == null || resultArray.length() <= 0) {
                continue;
            }
            for (int i = 0; i < resultArray.length(); i++) {
                org.json.JSONObject blockJsonObject = resultArray.getJSONObject(i);
                /*if (!blockJsonObject.has("picPath") || StrUtil.isBlank(blockJsonObject.getString("picPath"))) {
                    if (blockJsonObject.has("words")) {
                        blockMap.put(blockJsonObject.getString("code") + "_" + ocrResult.getCaseFileAttaRecOid(), blockJsonObject.getString("words"));
                    } else {
                        blockMap.put(blockJsonObject.getString("code") + "_" + ocrResult.getCaseFileAttaRecOid(), "");
                    }
                } else {
                    blockMap.put(blockJsonObject.getString("code") + "_" + ocrResult.getCaseFileAttaRecOid() + "-pic", blockJsonObject.getString("picPath"));
                }*/
                if (!blockJsonObject.has("picPath") || StrUtil.isBlank(blockJsonObject.getString("picPath"))) {
                    if (blockJsonObject.has("words")) {
                        blockMap.put(blockJsonObject.getString("code") + "_" + ocrResult.getMaterialOid(), blockJsonObject.getString("words"));
                    } else {
                        blockMap.put(blockJsonObject.getString("code") + "_" + ocrResult.getMaterialOid(), "");
                    }
                } else {
                    blockMap.put(blockJsonObject.getString("code") + "_" + ocrResult.getMaterialOid() + "-pic", blockJsonObject.getString("picPath"));
                }
            }

            ocrResult.setResultJsonObject(resultJsonObject);
        }

        jsonObject.put("code", FaStaticParam.INTER_STATUS_CODE_SUCCESS);
        jsonObject.put("result", ruleValidNew(request, caseOid, ruleValidList, blockMap));


        return jsonObject;
    }


    /**
     * 规则验证
     *
     * @param request
     * @param caseOid
     * @param ruleValidList
     * @param blockMap
     * @return
     * @throws Exception
     */
    private List<Map<String, Object>> ruleValidNew(HttpServletRequest request, String caseOid,
                                                   List<FaModelRuleValidation> ruleValidList, Map<String, String> blockMap)
            throws Exception {

        List<Map<String, Object>> validResultArray = new ArrayList<Map<String, Object>>();
        for (FaModelRuleValidation ruleValid : ruleValidList) {
            String validateType = ruleValid.getValidateType();
            String metadataCode = ruleValid.getTemplateMetadataCode();
            String materialOid = ruleValid.getSxMaterialOid();
            String words = null, picPath = null;
            if (blockMap.containsKey(metadataCode + "_" + materialOid)) {
                words = blockMap.get(metadataCode + "_" + materialOid);
                // 如果words为null，设置为空字符串
                words = words == null ? "" : words;
            } else if (blockMap.containsKey(metadataCode + "_" + materialOid + "-pic")) {
                picPath = blockMap.get(metadataCode + "_" + materialOid + "-pic");
                picPath = picPath == null ? "" : picPath;
            } else {
                words = words == null ? "" : words;
            }

            Map<String, Object> validResult = new HashMap<String, Object>();
            validResult.put("materialOid", ruleValid.getSxMaterialOid());
            validResult.put("catalogOid", ruleValid.getCatalogOid());
            validResult.put("code", ruleValid.getTemplateMetadataCode());
            validResult.put("name", ruleValid.getTemplateMetadataName());
            switch (validateType) {
                case FaStaticParam.VALIDATION_TYPE_NOTNULL:

                    if (StrUtil.isNotBlank(picPath)) {
                        validResult.put("memo", FaStaticParam.VALIDATION_TYPE.get(FaStaticParam.VALIDATION_TYPE_NOTNULL));
                        // 图片的非空验证
                        validResult.put("picPath", picPath);
                        PictureHandle picHandle = new HighShotMeterPictureHandle(picPath);
                        if (!picHandle.isPicBlank()) {
                            validResult.put("valid", true);
                        } else {
                            validResult.put("valid", false);
                        }
                        picHandle.releaseMat();
                    } else {
                        validResult.put("words", words);
                        if (StrUtil.isBlank(words)) {
                            validResult.put("memo", FaStaticParam.VALIDATION_TYPE.get(FaStaticParam.VALIDATION_TYPE_NOTNULL));
                            validResult.put("valid", false);
                        } else {
                            if (ruleValid.getTemplateMetadataName().contains("盖章")) {
                                if (words.equals("盖章")) {
                                    validResult.put("valid", true);
                                } else {
                                    validResult.put("memo", "未盖章");
                                    validResult.put("valid", false);
                                }

                            } else if (ruleValid.getTemplateMetadataName().contains("签字")) {
                                if (words.equals("签字")) {
                                    validResult.put("valid", true);
                                } else {
                                    validResult.put("memo", "未签字");
                                    validResult.put("valid", false);
                                }
                            } else {
                                validResult.put("memo", FaStaticParam.VALIDATION_TYPE.get(FaStaticParam.VALIDATION_TYPE_NOTNULL));
                                validResult.put("valid", true);
                            }
                        }
                    }
                    break;
                case FaStaticParam.VALIDATION_TYPE_NULL:
                    validResult.put("memo", FaStaticParam.VALIDATION_TYPE.get(FaStaticParam.VALIDATION_TYPE_NULL));
                    if (StrUtil.isNotBlank(picPath)) { // 图片的非空验证
                        validResult.put("picPath", picPath);
                        PictureHandle picHandle = new HighShotMeterPictureHandle(picPath);
                        if (!picHandle.isPicBlank()) {
                            validResult.put("valid", false);
                        } else {
                            validResult.put("valid", true);
                        }
                        picHandle.releaseMat();
                    } else {
                        validResult.put("words", words);
                        if (StrUtil.isNotBlank(words)) {
                            validResult.put("valid", false);
                        } else {
                            validResult.put("valid", true);
                        }
                    }
                    break;
                case FaStaticParam.VALIDATION_TYPE_CONTENT:
                    String content = ruleValid.getThanContent();
                    // 如果content为null，设置为空字符串
                    content = content == null ? "" : content;
                    validResult.put("memo", "字符必须为" + content + "！");
                    if (!content.equals(words)) {
                        validResult.put("valid", false);
                    } else {
                        validResult.put("valid", true);
                    }
                    break;
                case FaStaticParam.VALIDATION_TYPE_STR_CONTAIN:
                    String containContent = ruleValid.getThanContent();
                    // 如果containContent为null，设置为空字符串
                    containContent = containContent == null ? "" : containContent;
                    validResult.put("memo", "字符必须包含为" + containContent + "！");
                    if (!words.contains(containContent)) {
                        validResult.put("valid", false);
                    } else {
                        validResult.put("valid", true);
                    }
                    break;
                case FaStaticParam.VALIDATION_TYPE_RANGE:
                    String contentDown = ruleValid.getContentDown();
                    String contentUp = ruleValid.getContentUp();
                    // 如果containContent为null，设置为空字符串
                    contentDown = contentDown == null ? "" : contentDown;
                    contentUp = contentUp == null ? "" : contentUp;
                    try {
                        validResult.put("memo", "数字范围必须在" + contentDown + "和" + contentUp + "之间！");
                        if (StrUtil.isBlank(words)) {
                            validResult.put("valid", false);
                            validResult.put("memo", "字符不能为空，数字范围必须在" + contentDown + "和" + contentUp + "之间！");
                            break;
                        }

                        Integer cDown = StrUtil.isNotBlank(contentDown) ? Integer.parseInt(contentDown) : null;
                        Integer cUp = StrUtil.isNotBlank(contentUp) ? Integer.parseInt(contentUp) : null;
                        Integer c = StrUtil.isNotBlank(words) ? Integer.parseInt(words) : null;

                        if (cDown != null && c >= cDown) {
                            if (cUp != null && c <= cUp) {
                                validResult.put("valid", true);
                            } else {
                                validResult.put("valid", false);
                            }
                        } else {
                            validResult.put("valid", false);
                        }
                    } catch (Exception e) {
                        validResult.put("valid", false);
                        validResult.put("memo", "字符必须是数字！");
                    }

                    break;
                case FaStaticParam.VALIDATION_TYPE_DATE_COMPARE:
                    String contentDateFormat = ruleValid.getContentDateFormat();
                    String contentDateValue = ruleValid.getContentDateValue();
                    // 如果containContent为null，设置为空字符串
                    contentDateFormat = contentDateFormat == null ? "" : contentDateFormat;
                    contentDateValue = contentDateValue == null ? "" : contentDateValue;
                    Date compareDate = new Date();
                    try {
                        if (StrUtil.isBlank(words)) {
                            validResult.put("valid", false);
                            validResult.put("memo", "字符不能为空！");
                            break;
                        }

                        if (!"CURRENT_DATE".equals(contentDateValue.toUpperCase())) {
                            compareDate = DateUtil.parse(contentDateValue, contentDateFormat);
                        }
                        validResult.put("memo", "日期比较，日期格式：" + contentDateFormat + "，比对日期：" + DateUtil.format(compareDate, contentDateFormat) + "！");

                        //将验证字符串转化为日期类型进行比对
                        Date wDate = DateUtil.parse(words, contentDateFormat);

                        if (wDate.after(compareDate)) {
                            validResult.put("valid", true);
                        } else {
                            validResult.put("valid", false);
                        }
                    } catch (Exception e) {
                        validResult.put("valid", false);
                        validResult.put("memo", "字符无法转化为日期！");
                    }
                    break;
                case FaStaticParam.VALIDATION_TYPE_CONTENT_THAN:
                    String thanContent = blockMap.get(ruleValid.getThanTemplateMetadataCode() + "_" + ruleValid.getThanSxMaterialOid());
                    /*FaModelMaterialCatalog thanCatalog = faModelMaterialCatalogService.getFaModelMaterialCatalogByOid(ruleValid.getThanCatalogOid());*/
                    //MaterialCatalog thanCatalog =this.materialCatalogManager.getMaterialCatalogByOid(ruleValid.getThanCatalogOid());
                    ApiResultSet<MaterialCatalog> catalog = materialCatalogService.getMaterialCatalogByOid(ruleValid.getThanCatalogOid());
                    MaterialCatalog thanCatalog = null;
                    if (catalog != null && catalog.getData() != null) {
                        thanCatalog = catalog.getData();
                    }
                    // 如果thanContent为null, 比较元素不存在（对应材料不需要上传情况）不进行比较
                    if (thanContent == null) {
                        validResult.put("valid", true);
                    } else {
                        // 如果thanContent为null，设置为空字符串
                        //thanContent = thanContent == null ? "" : thanContent;
                        validResult.put("memo", ruleValid.getTemplateMetadataName() + "[" + ruleValid.getCatalogName() + "]与"
                                + ruleValid.getThanTemplateMetadataName() + "["
                                + thanCatalog.getCatalogName() + "]必须相同！");

                        if (!thanContent.equals(words)) {
                            validResult.put("valid", false);
                        } else {
                            validResult.put("valid", true);
                        }
                    }
                    break;
                case FaStaticParam.VALIDATION_TYPE_SIMILARITY:
                    /* Double similar = ruleValid.getSimilar();*/
                    String similarold = ruleValid.getSimilar();
                    Double similar = Double.parseDouble(similarold);
                    validResult.put("memo",
                            ruleValid.getTemplateMetadataName() + "[" + ruleValid.getTemplateMetadataCode() + "]与"
                                    + ruleValid.getThanTemplateMetadataName() + "["
                                    + ruleValid.getThanTemplateMetadataCode() + "]的相识度必须大于" + similar + "%！");
                    // 相似度比对
                    if (StrUtil.isBlank(picPath)) {
                        validResult.put("valid", false);
                        break;
                    }
                    String thanPicPath = blockMap.get(ruleValid.getThanTemplateMetadataCode() + "_" + materialOid + "-pic");
                    if (StrUtil.isBlank(thanPicPath)) {
                        validResult.put("valid", false);
                        break;
                    }
                    if (OpenCVImageHandle.similarCalc(picPath, thanPicPath) < similar) {
                        validResult.put("valid", false);
                    } else {
                        validResult.put("valid", true);
                    }
                    break;
                case FaStaticParam.VALIDATION_TYPE_HAND_SIGN:
                    if (StrUtil.isBlank(picPath)) {
                        validResult.put("valid", false);
                        validResult.put("memo", "必须为手写！");
                        break;
                    }
                    Double outlineLength = OpenCVImageHandle.calcPicOutlineLength(picPath);
                    if (outlineLength > 50) {
                        validResult.put("valid", false);
                    } else {
                        validResult.put("valid", true);
                    }
                    break;
                default:
                    break;
            }
            validResultArray.add(validResult);
        }
        return validResultArray;
    }


    public Map<String, Object> viewResultNew(HttpServletRequest request, String caseOid, String caseMaterialOid, String caseFileRecOid, String catalogOid) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("success", false);
        String materialOid = null;
        String parentCatalogOid = "";
        QlCaseMaterial qlCaseMaterial = null;
        //办件材料
        ApiResultSet<QlCaseMaterial> qlresult = qlCaseMaterialServiceService.queryMaterialByCaseMaterialOid(caseMaterialOid);
        if (String.valueOf(qlresult.getCode()).equals(FaStaticParam.HTTP_REQUEST_CODE_SUCCESS)) {
            qlCaseMaterial = qlresult.getData();
            materialOid = qlCaseMaterial.getMaterialOid();
            parentCatalogOid = qlCaseMaterial.getMaterialCatalogOid();
        }
        String auditType = qlCaseMaterial.getAuditType();//1.智审2.人工审核3.多张

        //办件信息
        ApiResultSet<QlCase> resultSet = qlCaseServiceService.queryQlCaseByCaseOid(caseOid);
        QlCase qlCase = null;
        if (resultSet.getData() != null) {
            qlCase = resultSet.getData();
        }
        String serviceOid = qlCase.getServiceOid();


        ApiResultSet<QlCaseApplay> qlCaseApplayApiResultSet = qlCaseApplayServiceService.queryQlCaseApplayByCaseOid(caseOid);
        QlCaseApplay qlCaseApplay = null;
        if (qlCaseApplayApiResultSet.getData() != null) {
            qlCaseApplay = qlCaseApplayApiResultSet.getData();

        }
        map.put("qlCaseApplay", qlCaseApplay);//审核样表列表
        //审查要点
        AhsSamplePicInfo ahsSamplePicInfo1 = new AhsSamplePicInfo();
        ahsSamplePicInfo1.setMateriaOid(materialOid);
        ahsSamplePicInfo1.setSampleInfoOid(serviceOid);
        List<AhsSamplePicInfo> ahsSamplePicInfoList = this.ahsSamplePicInfoManager.queryAhsSamplePicInfoList(ahsSamplePicInfo1);
        AhsSamplePicInfo ahsSamplePicInfo = null;
        if (ahsSamplePicInfoList.size() > 0) {
            ahsSamplePicInfo = ahsSamplePicInfoList.get(0);
            ExaminePointCarding examinePointCarding = new ExaminePointCarding();
            examinePointCarding.setAhsSamplePicInfoOid(ahsSamplePicInfo.getAhsSamplePicInfoOid());
            examinePointCarding.setAttaOid(ahsSamplePicInfo.getAttaOid());
            /*examinePointCarding.setExaminePointCardingOid(examinePointCardingOid);*/
            List<ExaminePointCarding> examinePointCardingList = this.examinePointCardingManager.queryExaminePointCardingList(examinePointCarding);
            ahsSamplePicInfo.setExaminePointCardingList(examinePointCardingList);

        }
        map.put("ahsSamplePicInfoList", ahsSamplePicInfoList);//审核样表列表
        map.put("ahsSamplePicInfo", ahsSamplePicInfo);//审核要点


        ApiResultSet<QlCaseMaterialAtta> qlCaseMaterialAttaResult = qlCaseMaterialAttaServiceService.queryQlCaseMaterialAttaByOid(caseFileRecOid);
        //ApiResultSet<QlCaseMaterialAtta> qlCaseMaterialAttaResult=qlCaseMaterialAttaServiceService.queryQlCaseMaterialAttaByCatalogOid(qlCaseMaterial.getCaseMaterialOid(),catalogOid);
        QlCaseMaterialAtta qlCaseMaterialAtta;
        if (null == qlCaseMaterialAttaResult.getData()) {
            map.put("data", "办件材料记录不存在");
            return map;
        } else {
            qlCaseMaterialAtta = qlCaseMaterialAttaResult.getData();
        }

        //上传图片
        String attaOid = qlCaseMaterialAtta.getAttaOid();
        attaOid = qlCaseMaterialAtta.getAttaOid();
        ApiResultSet<SysAtta> sysAttaresult = sysAttaService.getSysAttaByAttaOid(attaOid);
        SysAtta qlSysAtta = sysAttaresult.getData();
        map.put("qlSysAtta", qlSysAtta);
        String fastdfsNginxUrl = qlSysAtta.getFastdfsNginxUrl();
        //样表
        map.put("MATERIAL_SAMPLE_ADDR_YL", qlCaseMaterial.getMaterialSampleAddrYl());
        log.info("获取附件成功：{}", JSON.toJSONString(qlSysAtta));


        if (auditType.equals("1")) {
            String materialCatalogOid = "";
            ApiResultSet<List<MaterialCatalog>> materiallist = materialCatalogService.queryList(parentCatalogOid);
            List<MaterialCatalog> faModelMaterialCatalogList = null;
            MaterialCatalog faModelMaterialCatalog = null;
            if (materiallist != null && materiallist.getData() != null) {
                faModelMaterialCatalogList = materiallist.getData();
                faModelMaterialCatalog = faModelMaterialCatalogList.get(0);
                catalogOid = faModelMaterialCatalog.getMaterialCatalogOid();
            } else {
                map.put("data", "目录信息不存在");
            }


            FaModelRuleValidation faModelRuleValidation = new FaModelRuleValidation();
            faModelRuleValidation.setSxMaterialOid(qlCaseMaterial.getMaterialOid());
            faModelRuleValidation.setCatalogOid(catalogOid);
            faModelRuleValidation.setServiceOid(serviceOid);
            List<FaModelRuleValidation> ruleValidList = this.faModelRuleValidationManager.queryFaModelRuleValidationList(faModelRuleValidation);
            map.put("ruleValidList", ruleValidList);
            map.put("qlCaseMaterial", qlCaseMaterial);
            String cataOid = null;

            if (StringUtils.isEmpty(qlCaseMaterial.getMaterialOid())) {
                map.put("data", "事项材料记录不存在");
            }


            JSONArray returnArray = new JSONArray();
            FaMaterialPicOcrResult faMaterialPicOcrResultold = new FaMaterialPicOcrResult();
            faMaterialPicOcrResultold.setUuid(caseOid);
            faMaterialPicOcrResultold.setCataCode(faModelMaterialCatalog.getCatalogCode());
            faMaterialPicOcrResultold.setMaterialOid(qlCaseMaterial.getMaterialOid());
            faMaterialPicOcrResultold.setMaterialAttaOid(qlCaseMaterialAtta.getMaterialAttaOid());
            DbFaMaterialPicOcrResult dbFaMaterialPicOcrResult = this.faMaterialPicOcrResultManager.getFaMaterialPicOcrResultByFaMaterialPicOcrResult(faMaterialPicOcrResultold);
            FaMaterialPicOcrResult faMaterialPicOcrResult = new FaMaterialPicOcrResult();
            if (dbFaMaterialPicOcrResult == null || StrUtil.isBlank(dbFaMaterialPicOcrResult.getResultJson())) {
                map.put("data", "识别结果信息不存在");
                return map;
            } else {
                BeanUtils.copyProperties(dbFaMaterialPicOcrResult, faMaterialPicOcrResult);
            }
            JSONObject resultJsonObject = JSON.parseObject(faMaterialPicOcrResult.getResultJson());
            JSONArray ocrResultArray = JSONArray.parseArray(resultJsonObject.get("result").toString());
            // 办件记录对应事项材料没有绑定材料审核目录，说明该材料不需要智能审核，直接返回
            if (StringUtils.isEmpty(catalogOid)) {
                returnArray = ocrResultArray;
            } else {
                Map<String, Object> validMap = highShotMeterMaterialValidNew(request, caseOid);
                List<Map<String, Object>> validResultList = null;
                if (validMap.get("result") != null) {
                    validResultList = (List<Map<String, Object>>) validMap.get("result");
                }
                if (ocrResultArray.size() > 0) {
                    for (Object o : ocrResultArray) {
                        if (null == o) {
                            continue;
                        }
                        JSONObject resultObj = JsonUtil.objToJSONObject(o);
                        if (validResultList != null) {
                            for (Map<String, Object> resultMap : validResultList) {
                                if (null == resultMap) {
                                    continue;
                                }
                                if (materialOid.equals(resultMap.get("materialOid")) && resultObj.get("name").equals(resultMap.get("name"))) {
                                    resultObj.put("valid", resultMap.get("valid"));
                                    resultObj.put("memo", resultMap.get("memo"));
                                    if (resultMap.get("valid") != null && (Boolean) resultMap.get("valid")) {
                                        resultObj.remove("memo");
                                    }
                                }
                            }
                        }
                        returnArray.add(resultObj);
                    }
                } else {
                    JSONObject resultObj = new JSONObject();
                    resultObj.put("valid", false);
                    resultObj.put("memo", "上传附件和样表模板不符");
                    returnArray.add(resultObj);
                }
            }

            map.put("success", true);
            map.put("data", returnArray);


            if (StrUtil.isBlank(faMaterialPicOcrResult.getSelectionMarkOid())) {
                List<WatermarkParamRequest> paramList = new ArrayList();
                for (Object o : returnArray) {
                    if (null == o) {
                        continue;
                    }
                    JSONObject resultObj = JsonUtil.objToJSONObject(o);
                    String memo = resultObj.getString("memo");
                    if (StrUtil.isBlank(memo)) {
                        continue;
                    }
                    String width = resultObj.getString("width");
                    String height = resultObj.getString("height");
                    String top = resultObj.getString("top");
                    String left = resultObj.getString("left");
                    if (StrUtil.isBlank(width) || StrUtil.isBlank(height) || StrUtil.isBlank(top) || StrUtil.isBlank(left)) {
                        continue;
                    }
                    WatermarkParamRequest paramRequest = new WatermarkParamRequest();
                    paramRequest.setWidth(Integer.parseInt(width));
                    paramRequest.setHeight(Integer.parseInt(height));
                    paramRequest.setTop(Integer.parseInt(top));
                    paramRequest.setLeft(Integer.parseInt(left));
                    paramList.add(paramRequest);
                }

                if (CollUtil.isNotEmpty(paramList)) {
                    AttaBase64Result attaBase64Result = getAttaBase64ByUrl(fastdfsNginxUrl);
                    String picBase64 = "";
                    if (null != attaBase64Result) {
                        picBase64 = attaBase64Result.getResult();
                    }

           /*      String picPath= FaStaticParam.PROJECT_PATH+"\\modelTemples\\model\\pic\\hsm\\";
                String picName = materialOid +faModelMaterialCatalog.getCatalogCode() + DateUtil.format(new Date(), "yyyyMMddHHmmssSSS")
                        + StringUtil.random(6) + ".jpg";
                String  picPathALL = picPath  + picName;
                String  picPathNew = picPathALL + picName;
                File file = new File(picPath);
                if(!file.exists()){
                    File dir = new File(picPath);
                    if (!dir.exists()) {
                        dir.mkdirs();
                    }
                }
                HttpUtil.downloadFile(fastdfsNginxUrl, cn.hutool.core.io.FileUtil.file(picPathALL));
                picName =  "image4.jpg";
                PictureHandle ph = new HighShotMeterPictureHandle(picPath, picName);
                ph.setAngleType(faModelMaterialCatalog.getAngleType());// 设置角类型
                boolean flag = ph.pretreatment();
                if (!flag) {
                    ph.releaseMat();// 释放资源

                }
                String filePath = picPath;
                filePath += File.separator + "baiduTemplate_" + picName;
                ph.saveResultPic(filePath);
                ph.releaseMat();// 释放资源


                AttaBase64Result attaBase64ResultNew=getAttaBase64ByUrl(filePath);
                String picBase64New = "";
                if(null!=attaBase64ResultNew){
                    picBase64New = attaBase64ResultNew.getResult();
                }
*/
                    // picBase64 = Base64FileUitl.encodeBase64File(filePath);

                    if (StrUtil.isNotBlank(faModelMaterialCatalog.getBaiduTemplateId())) {
                        // 图片路径
                    }
//                    ImageManipulationRequest imageManipulationRequest = aiTokenUtil.getTokenRequest(ImageManipulationRequest.class);
//                    imageManipulationRequest.setOldImgBase64(picBase64);
//                    imageManipulationRequest.setWatermarkParamList(paramList);
//                    ImageManipulationResponse response = manipulationRestService.addSelectionAreaImgBase64(imageManipulationRequest);
//
//                    String originName = "selectionMark_" + materialOid + "_" + faModelMaterialCatalog.getCatalogCode() + "_" + DateUtil.format(new Date(), "yyyyMMddHHmmssSSS") + "_"
//                            + StringUtil.random(6) + ".jpg";
//                    ApiResultSet<SysAtta> apiResultSet =this.uploadBase64Images(response.getImgBase64());
//
//                    if (null!=apiResultSet.getData()) {
//                        SysAtta atta=apiResultSet.getData();
//                        faMaterialPicOcrResult.setSelectionMarkOid(atta.getAttaOid());
//                        faMaterialPicOcrResult.setSysAtta(atta);
//                        map.put("selectionMarkOid", atta.getAttaOid());
//                        map.put("xzhatta",atta);//不通过的样张图
//                    }
                }
            } else {
                map.put("selectionMarkOid", faMaterialPicOcrResult.getSelectionMarkOid());
            }


        } else {
            map.put("qlCaseMaterial", qlCaseMaterial);
            map.put("data", "");
            map.put("xzhatta", "");
        }


        return map;
    }

    /***
     * @Description: 根据base64上传到基础库附件表
     * @Author:liangss
     * @Date:2021/9/4
     * @Param: [imageBase64]
     */
    public ApiResultSet<SysAtta> uploadBase64Images(String imageBase64) {
        ApiResultSet<SysAtta> apiResultSet;
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        try {
            log.info("获取附件imageBase64成功：****", imageBase64, "***");
            byte[] bytes = Base64.getDecoder().decode(imageBase64);
            InputStream inputStream = new ByteArrayInputStream(bytes);
            String originaFiliname = "image" + System.currentTimeMillis() + ".png";
            //MultipartFile file = new MockMultipartFile("image"+new Date().getTime()+".png", bytes);
            // MultipartFile file = new MockMultipartFile("File","image"+new Date().getTime()+".png","text/plain",inputStream);

            UploadUtil uploadUtil = new UploadUtil(request);
            String filePath = null;
            MultipartFile file = new MockMultipartFile("File", "image" + System.currentTimeMillis() + ".png", "text/plain", inputStream);
            filePath = uploadUtil.uploadFile(file);
            SysAttaTemp sysAttaFile = uploadUtil.getSysAttaFile(filePath, "");

            SysAtta atta = new SysAtta();
            BeanUtils.copyProperties(sysAttaFile, atta);
            //保存附件信息
            apiResultSet = sysAttaService.saveSysAtta(atta);

            log.info("获取附件apiResultSet成功：****", apiResultSet, "***");
        } catch (Exception e) {
            e.printStackTrace();
            throw new ResultInfoException("图片信息异常！");
        }
        return apiResultSet;

    }


    /***
     * @Description: 根据base64上传到事项库办件表
     * @Author:liangss
     * @Date:2021/9/4
     * @Param: [imageBase64]
     */
    public ApiResultSet<SysAtta> uploadBase64ImagesToCase(String imageBase64) {
        ApiResultSet<SysAtta> apiResultSet;
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        try {
            log.info("获取附件imageBase64成功：****", imageBase64, "***");
            byte[] bytes = Base64.getDecoder().decode(imageBase64);
            InputStream inputStream = new ByteArrayInputStream(bytes);
            String originaFiliname = "image" + System.currentTimeMillis() + ".png";
            //MultipartFile file = new MockMultipartFile("image"+new Date().getTime()+".png", bytes);
            // MultipartFile file = new MockMultipartFile("File","image"+new Date().getTime()+".png","text/plain",inputStream);

            UploadUtil uploadUtil = new UploadUtil(request);
            String filePath = null;
            MultipartFile file = new MockMultipartFile("File", "image" + System.currentTimeMillis() + ".png", "text/plain", inputStream);
            filePath = uploadUtil.uploadFile(file);
            SysAttaTemp sysAttaFile = uploadUtil.getSysAttaFile(filePath, "");

            SysAtta atta = new SysAtta();
            BeanUtils.copyProperties(sysAttaFile, atta);
            //保存附件信息
            apiResultSet = sysAttaService.saveSysAtta(atta);
            // apiResultSet=sysAttaService.saveSysAtta(atta);

            log.info("获取附件apiResultSet成功：****", apiResultSet, "***");
        } catch (Exception e) {
            e.printStackTrace();
            throw new ResultInfoException("图片信息异常！");
        }
        return apiResultSet;

    }


    /**
     * 循环预审
     *
     * @param request
     * @param caseOid
     * @param caseMaterialOid
     * @param materialAttaOid
     * @param attaOid
     * @param cataOid
     * @param materialOid
     * @return
     * @throws Exception
     */
    public Map<String, Object> intelligentPretrialmaterialPrePrialAll(HttpServletRequest request, String caseOid, String caseMaterialOid, String materialAttaOid,
                                                                      String attaOid, String cataOid, String materialOid)
            throws Exception {
        log.info("11111111111");
        Map<String, Object> ocrResultMap = new HashMap<String, Object>();
        //办件材料记录
        ApiResultSet<QlCaseMaterialAtta> qlCaseMaterialAttaResult = qlCaseMaterialAttaServiceService.queryQlCaseMaterialAttaByOid(materialAttaOid);
        QlCaseMaterialAtta qlCaseMaterialAtta;
        SysAtta qlSysAtta;
        if (null == qlCaseMaterialAttaResult.getData()) {
            log.error("材料预审", "办件材料记录不存在！");
            ocrResultMap.put("success", false);
            ocrResultMap.put("message", "办件材料记录不存在!");
            return ocrResultMap;
        } else {
            qlCaseMaterialAtta = qlCaseMaterialAttaResult.getData();
            attaOid = qlCaseMaterialAtta.getAttaOid();
            ApiResultSet<SysAtta> sysAttaresult = sysAttaService.getSysAttaByAttaOid(attaOid);
            qlSysAtta = sysAttaresult.getData();
            //cataOid=qlCaseMaterialAtta.getMaterialCatalogOid();
        }
        if (StringUtils.isEmpty(cataOid)) {
            log.error("材料预审", "没有关联目录材料！");
            ocrResultMap.put("success", false);
            ocrResultMap.put("message", "没有关联目录材料!");
            return ocrResultMap;
        }
        log.info("2222");
        //办件材料
        ApiResultSet<QlCaseMaterial> qlresult = qlCaseMaterialServiceService.queryMaterialByCaseMaterialOid(caseMaterialOid);
        QlCaseMaterial qlCaseMaterial = null;
        if (String.valueOf(qlCaseMaterialAttaResult.getCode()).equals(FaStaticParam.HTTP_REQUEST_CODE_SUCCESS)) {
            if (null != qlresult.getData()) {
                qlCaseMaterial = qlresult.getData();
                materialOid = qlCaseMaterial.getMaterialOid();
            }
        }
        String parentCataOid = qlCaseMaterial.getMaterialCatalogOid();

        // 获得目录子项
        ApiResultSet<List<MaterialCatalog>> list = materialCatalogService.queryList(parentCataOid);
        List<MaterialCatalog> cataList = null;
        if (list != null && list.getData() != null && list.getData().size() > 0) {
            cataList = list.getData();
        }
        log.info("3333");
        //List<MaterialCatalog> cataList=this.materialCatalogManager.queryList(parentCataOid);
        if (cataList == null || cataList.size() == 0) {
            log.error("材料预审", "事项材料关联目录不正确！");
            ocrResultMap.put("success", false);
            ocrResultMap.put("message", "事项材料关联目录不正确!");
            return ocrResultMap;
            /*throw new Exception("事项材料关联目录不正确!");*/
        }
        //cataOid
        //Map<String, MaterialCatalog> materialCatalogMap = cataList.stream().collect(Collectors.toMap(MaterialCatalog::getMaterialCatalogOid, a -> a, (k1, k2) -> k1));
        // 按照设计，只有一个子项
        //MaterialCatalog cata = materialCatalogMap.get(0);

        MaterialCatalog cata = cataList.get(0);
        if (cata == null) {
            log.error("材料预审", "事项材料关联目录不正确！");
            ocrResultMap.put("success", false);
            ocrResultMap.put("message", "事项材料关联目录不正确!");
            return ocrResultMap;
        }

        //附件信息
        String fastdfsNginxUrl = qlSysAtta.getFastdfsNginxUrl();
        log.info("获取附件成功：qlSysAtta{}", JSON.toJSONString(qlSysAtta));

        // String base64=FileManageUtil.getImgFromUrl(fastdfsNginxUrl);
        //AttaBase64Result attaBase64Result = FileManageUtil.getAttaBase64(attaOid);
        log.info("attaBase64Result--start");
        /*fastdfsNginxUrl="http://172.168.250.6:8888/group1/M00/00/20/rKj6BmDgbTOANyjhAAdPVQSKO6c433.jpg";*/
        AttaBase64Result attaBase64Result = getAttaBase64ByUrl(fastdfsNginxUrl);
        log.info("attaBase64Result--end");
        //AttaBase64Result attaBase64Result=getAttaBase64New(attaOid);
        String picBase64 = "";
        if (null != attaBase64Result) {
            picBase64 = attaBase64Result.getResult();
            log.info("picBase64==" + picBase64);
        } else {
            log.error("材料预审", "未获取附件base64信息！办件主键为：{}，办件材料记录主键为：{}，附件主键为：{}，目录元素主键为：{}", caseOid, "", attaOid, cataOid);
            ocrResultMap.put("success", false);
            ocrResultMap.put("message", "未获取附件base64信息!");
            return ocrResultMap;
        }

        List<Map<String, Object>> blockJsonArray = new ArrayList<Map<String, Object>>();
        // 验证印章 start
      /*  if (cata.getSealsNumber()!= null && cata.getSealsNumber() > 0) {
            this.checkSealCount(picBase64, cata, caseOid, caseFileRecOid, attaOid);
        }*/
        // 验证印章 end
        // 材料审核目录绑定特定识别类型
        if (FaStaticParam.LICE_OCR_TYPE_BUSINESSLICE.equals(cata.getMaterialIdentificationTypeOid())) {
            // 营业执照
            BusinessLiceInfo businessLiceInfo = this.businessLiceManager.getBusinessLiceInfo(picBase64);
            boolean flag = this.allFieldIsNull(businessLiceInfo);//判断是不是所有属性均为空
            if (flag == true) {
                log.error("材料预审", "营业执照识别失败！办件主键为：{}，办件材料记录主键为：{}，附件主键为：{}，目录元素主键为：{}", attaOid, cataOid);
                ocrResultMap.put("success", false);
                ocrResultMap.put("message", "营业执照识别失败,请检查附件!");
                return ocrResultMap;

            }
            Map<String, Object> businessLiceMap = this.businessLiceManager.discernBusinessBaiDu(businessLiceInfo, materialOid, cata);
            String code = (String) businessLiceMap.get(FaStaticParam.CODE);
            if (FaStaticParam.HTTP_REQUEST_CODE_SUCCESS.equals(code)) {
                String resultJson = new JSONObject(businessLiceMap).toString();
                saveResult(caseOid, materialOid, qlCaseMaterialAtta.getMaterialAttaOid(), cata.getCatalogCode(), attaOid, CommonUtil.md5(picBase64), resultJson,
                        businessLiceInfo);
                ocrResultMap.put("success", true);
                ocrResultMap.put("message", "营业执照识别成功!");
                return ocrResultMap;
            } else {
                String message = businessLiceMap.get(FaStaticParam.MESSAGE).toString();
                ocrResultMap.put("success", false);
                ocrResultMap.put("message", "解析营业执照对象信息失败" + message);
                return ocrResultMap;
            }
        } else if (FaStaticParam.LICE_OCR_TYPE_IDCARD_FRONT.equals(cata.getMaterialIdentificationTypeOid())) {
            // 身份证头像面接口
            ApiResultSet<IdcardInfo> apiResultSet = idcardInfoService.getIdcardFrontInfo(picBase64);
            IdcardInfo idcardInfo = apiResultSet.getData();
            boolean flag = this.allFieldIsNull(idcardInfo);//判断是不是所有属性均为空
            if (flag == true) {
                log.error("材料预审", "身份证头像面识别失败！办件主键为：{}，办件材料记录主键为：{}，附件主键为：{}，目录元素主键为：{}", attaOid, cataOid);
                ocrResultMap.put("success", false);
                ocrResultMap.put("message", "身份证头像面识别失败,请检查附件!");
                return ocrResultMap;

            }
            IdCardInfoVo vo = new IdCardInfoVo();
            vo.setIdcardInfo(idcardInfo);
            vo.setMaterialCatalog(cata);
            ApiResultSet<Map<String, Object>> mapApiResultSet = idcardInfoService.discernIdcardInfo(vo);
            Map<String, Object> idcardMap = mapApiResultSet.getData();
            String code = (String) idcardMap.get(FaStaticParam.CODE);
            if (FaStaticParam.HTTP_REQUEST_CODE_SUCCESS.equals(code)) {
                String resultJson = new JSONObject(idcardMap).toString();
                saveResult(caseOid, materialOid, qlCaseMaterialAtta.getMaterialAttaOid(), cata.getCatalogCode(), attaOid, CommonUtil.md5(picBase64), resultJson,
                        idcardInfo);
                ocrResultMap.put("success", true);
                ocrResultMap.put("message", "身份证正面识别成功!");
                return ocrResultMap;
            } else {
                String message = idcardMap.get(FaStaticParam.MESSAGE).toString();
                ocrResultMap.put("success", false);
                ocrResultMap.put("message", "解析身份证正面信息失败" + message);
                return ocrResultMap;
            }
        } else if (FaStaticParam.LICE_OCR_TYPE_IDCARD_BACK.equals(cata.getMaterialIdentificationTypeOid())) {
            // 身份证接口
            ApiResultSet<IdcardInfo> apiResultSet = idcardInfoService.getIdcardFrontInfo(picBase64);
            IdcardInfo idcardInfo = apiResultSet.getData();
            boolean flag = this.allFieldIsNull(idcardInfo);//判断是不是所有属性均为空
            if (flag == true) {
                ocrResultMap.put("success", false);
                ocrResultMap.put("message", "身份证反面识别失败,请检查附件!");
                return ocrResultMap;
            }
            IdCardInfoVo vo = new IdCardInfoVo();
            vo.setIdcardInfo(idcardInfo);
            vo.setMaterialCatalog(cata);
            ApiResultSet<Map<String, Object>> mapApiResultSet = idcardInfoService.discernIdcardInfo(vo);
            Map<String, Object> idcardMap = mapApiResultSet.getData();
            String code = (String) idcardMap.get(FaStaticParam.CODE);
            if (FaStaticParam.HTTP_REQUEST_CODE_SUCCESS.equals(code)) {
                String resultJson = new JSONObject(idcardMap).toString();
                saveResult(caseOid, materialOid, qlCaseMaterialAtta.getMaterialAttaOid(), cata.getCatalogCode(), attaOid, CommonUtil.md5(picBase64), resultJson,
                        idcardInfo);
                ocrResultMap.put("success", true);
                ocrResultMap.put("message", "身份证反面识别成功!");
                return ocrResultMap;
            } else {
                String message = idcardMap.get(FaStaticParam.MESSAGE).toString();
                ocrResultMap.put("success", false);
                ocrResultMap.put("message", "解析身份证反面信息失败" + message);
                return ocrResultMap;
            }
        } else if (FaStaticParam.LICE_OCR_TYPE_TEMP_IDCARD.equals(cata.getMaterialIdentificationTypeOid())) {
            // 临时身份证、身份证复印件接口
            ApiResultSet<IdcardInfo> apiResultSet = idcardInfoService.getIdcardFrontInfo(picBase64);
            IdcardInfo idcardInfo = apiResultSet.getData();
            boolean flag = this.allFieldIsNull(idcardInfo);//判断是不是所有属性均为空
            if (idcardInfo == null || flag == true) {
                ocrResultMap.put("success", false);
                ocrResultMap.put("message", "临时身份证识别失败,请检查附件!");
                return ocrResultMap;
            }
            IdCardInfoVo vo = new IdCardInfoVo();
            vo.setIdcardInfo(idcardInfo);
            vo.setMaterialCatalog(cata);
            ApiResultSet<Map<String, Object>> mapApiResultSet = idcardInfoService.discernIdcardInfo(vo);
            Map<String, Object> idcardMap = mapApiResultSet.getData();
            String code = (String) idcardMap.get(FaStaticParam.CODE);
            if (FaStaticParam.HTTP_REQUEST_CODE_SUCCESS.equals(code)) {
                String resultJson = new JSONObject(idcardMap).toString();
                saveResult(caseOid, materialOid, qlCaseMaterialAtta.getMaterialAttaOid(), cata.getCatalogCode(), attaOid, CommonUtil.md5(picBase64), resultJson,
                        idcardInfo);
                ocrResultMap.put("success", true);
                ocrResultMap.put("message", "临时身份证识别成功!");
                return ocrResultMap;
            } else {
                String message = idcardMap.get(FaStaticParam.MESSAGE).toString();
                ocrResultMap.put("success", false);
                ocrResultMap.put("message", "解析临时身份证信息失败" + message);
                return ocrResultMap;
            }
        } else if (FaStaticParam.LICE_OCR_TYPE_HOUSEHOLD_REGISTER.equals(cata.getMaterialIdentificationTypeOid())) {
            // 户口本接口
            HouseholdRegisterInfo householdRegisterInfo = this.householdRegisterInfoManager.getHouseholdRegister(picBase64);
            boolean flag = this.allFieldIsNull(householdRegisterInfo);//判断是不是所有属性均为空
            if (householdRegisterInfo == null || flag == true) {
                ocrResultMap.put("success", false);
                ocrResultMap.put("message", "户口本识别失败,请检查附件!");
                return ocrResultMap;
            }
            Map<String, Object> householdRegisterInfoMap = this.householdRegisterInfoManager
                    .discernHouseholdRegister(householdRegisterInfo, cata);
            String code = (String) householdRegisterInfoMap.get(FaStaticParam.CODE);
            if (FaStaticParam.HTTP_REQUEST_CODE_SUCCESS.equals(code)) {
                String resultJson = new JSONObject(householdRegisterInfoMap).toString();
                saveResult(caseOid, materialOid, qlCaseMaterialAtta.getMaterialAttaOid(), cata.getCatalogCode(), attaOid, CommonUtil.md5(picBase64), resultJson,
                        householdRegisterInfo);
                ocrResultMap.put("success", true);
                ocrResultMap.put("message", "户口本识别成功!");
                return ocrResultMap;
            } else {
                String message = householdRegisterInfoMap.get(FaStaticParam.MESSAGE).toString();
                ocrResultMap.put("success", false);
                ocrResultMap.put("message", "解析户口本信息失败" + message);
                return ocrResultMap;
            }
        }

        ocrResultMap.put("code", FaStaticParam.INTER_STATUS_CODE_SUCCESS);
        ocrResultMap.put("result", blockJsonArray);
        ocrResultMap.put("success", true);
        String resultJson = new JSONObject(ocrResultMap).toString();
        saveResult(caseOid, materialOid, qlCaseMaterialAtta.getMaterialAttaOid(), cata.getCatalogCode(), attaOid, CommonUtil.md5(picBase64), resultJson, null);
        return ocrResultMap;
    }


    /**
     * 查看纸质和人工审核
     *
     * @param request
     * @param caseOid
     * @param caseMaterialOid
     * @param caseFileRecOid
     * @param catalogOid
     * @return
     * @throws Exception
     */
    public Map<String, Object> viewResultJTYM(HttpServletRequest request, String caseOid, String caseMaterialOid, String caseFileRecOid, String catalogOid) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("success", false);
        String materialOid = null;
        String parentCatalogOid = "";
        QlCaseMaterial qlCaseMaterial = null;
        //办件材料
        ApiResultSet<QlCaseMaterial> qlresult = qlCaseMaterialServiceService.queryMaterialByCaseMaterialOid(caseMaterialOid);
        if (String.valueOf(qlresult.getCode()).equals(FaStaticParam.HTTP_REQUEST_CODE_SUCCESS)) {
            qlCaseMaterial = qlresult.getData();
            materialOid = qlCaseMaterial.getMaterialOid();
            parentCatalogOid = qlCaseMaterial.getMaterialCatalogOid();
        }
        String auditType = qlCaseMaterial.getAuditType();//1.智审2.人工审核3.多张

        List<QlCaseMaterialAtta> qlCaseMaterialAttaList = null;
        if (auditType.equals("2")) {
            ApiResultSet<List<QlCaseMaterialAtta>> qlCaseMaterialAttaResult = qlCaseMaterialAttaServiceService.queryQlCaseMaterialAttaByCaseMaterialOid(qlCaseMaterial.getCaseMaterialOid());
            if (null != qlCaseMaterialAttaResult.getData()) {
                qlCaseMaterialAttaList = qlCaseMaterialAttaResult.getData();
                for (QlCaseMaterialAtta qlCaseMaterialAtta : qlCaseMaterialAttaList) {
                    String attaOid = qlCaseMaterialAtta.getAttaOid();
                    if (StringUtils.isNotEmpty(attaOid)) {
                        ApiResultSet<SysAtta> sysAttaresult = sysAttaService.getSysAttaByAttaOid(attaOid);
                        if (null != sysAttaresult.getData()) {
                            SysAtta qlSysAtta = sysAttaresult.getData();
                            String fastdfsNginxUrl = qlSysAtta.getFastdfsNginxUrl();
                            qlCaseMaterialAtta.setSrc(fastdfsNginxUrl);
                        }

                    }


                }
            }
        }
        map.put("qlCaseMaterialAttaList", qlCaseMaterialAttaList);//审核样表列表
        //办件信息
        ApiResultSet<QlCase> resultSet = qlCaseServiceService.queryQlCaseByCaseOid(caseOid);
        QlCase qlCase = null;
        if (resultSet.getData() != null) {
            qlCase = resultSet.getData();
        }
        String serviceOid = qlCase.getServiceOid();
        ApiResultSet<List<QlCaseMaterialAtta>> qlCaseMaterialAttaResult = qlCaseMaterialAttaServiceService.queryQlCaseMaterialAttaByCaseMaterialOid(qlCaseMaterial.getCaseMaterialOid());

        ApiResultSet<QlCaseApplay> qlCaseApplayApiResultSet = qlCaseApplayServiceService.queryQlCaseApplayByCaseOid(caseOid);
        QlCaseApplay qlCaseApplay = null;
        if (qlCaseApplayApiResultSet.getData() != null) {
            qlCaseApplay = qlCaseApplayApiResultSet.getData();

        }


        //审查要点
        AhsSamplePicInfo ahsSamplePicInfo1 = new AhsSamplePicInfo();
        ahsSamplePicInfo1.setMateriaOid(materialOid);
        ahsSamplePicInfo1.setSampleInfoOid(serviceOid);
        List<AhsSamplePicInfo> ahsSamplePicInfoList = this.ahsSamplePicInfoManager.queryAhsSamplePicInfoList(ahsSamplePicInfo1);
        AhsSamplePicInfo ahsSamplePicInfo = null;
        if (ahsSamplePicInfoList.size() > 0) {
            ahsSamplePicInfo = ahsSamplePicInfoList.get(0);
            ExaminePointCarding examinePointCarding = new ExaminePointCarding();
            examinePointCarding.setAhsSamplePicInfoOid(ahsSamplePicInfo.getAhsSamplePicInfoOid());
            examinePointCarding.setAttaOid(ahsSamplePicInfo.getAttaOid());
            /*examinePointCarding.setExaminePointCardingOid(examinePointCardingOid);*/
            List<ExaminePointCarding> examinePointCardingList = this.examinePointCardingManager.queryExaminePointCardingList(examinePointCarding);
            ahsSamplePicInfo.setExaminePointCardingList(examinePointCardingList);

        }

        map.put("ahsSamplePicInfoList", ahsSamplePicInfoList);//审核样表列表
        map.put("ahsSamplePicInfo", ahsSamplePicInfo);//审核要点

        map.put("qlCaseMaterial", qlCaseMaterial);
        map.put("qlCaseApplay", qlCaseApplay);

       /* map.put("casematerialName",qlCaseMaterial.getMaterialName());
        map.put("caseMaterialOid",qlCaseMaterial.getCaseMaterialOid());
        map.put("materialOid",qlCaseMaterial.getMaterialOid());
        map.put("credentialNumber",credentialNumber);
        map.put("legalPersonName",legalPersonName);
        map.put("applyUserName",applyUserName);*/


        return map;
    }

    /**
     * 获取材料列表以及审核数据
     *
     * @param request
     * @param caseOid
     * @param serviceOid
     * @return
     * @throws Exception
     */
    public List<QlCaseMaterial> getQlCaseMaterialListAndPreResult(HttpServletRequest request,
                                                                  String caseOid,
                                                                  String serviceOid
    ) throws Exception {
        //上传材料信息
        ApiResultSet<List<QlCaseMaterial>> qlcmResult = qlCaseMaterialServiceService.queryQlCaseMaterialByCaseOid(caseOid);
        List<QlCaseMaterial> qlCaseMaterialList = qlcmResult.getData();
        //获取审核结果
        AuditResult auditResult = new AuditResult();
        auditResult.setCaseOid(caseOid);
        List<AuditResult> resultList = auditResultManager.queryAuditResultList(auditResult);
/*
        AuditResult auditResult=new AuditResult();
        auditResult.setCaseOid(caseOid);
        List<AuditResult> auditResultList=auditResultManager.queryAuditResultList(auditResult);*/
        List<PreTrialResultVo> list = new ArrayList<>();
        String resultStatus = "";
        String resultDetail = "";
        Map<String, Object> preTrialResul = new HashMap<>();
        qlCaseMaterialFor:
        for (QlCaseMaterial qlCaseMaterial : qlCaseMaterialList) {
            String collectionFlag = qlCaseMaterial.getCollectionType();//1纸质收取
            //默认通过
            resultStatus = FaStaticParam.EXAMINE_RESULT_STATUS_PASS;
            String materialOid = qlCaseMaterial.getMaterialOid();
            String materialName = qlCaseMaterial.getMaterialName();
            //精细化材料列表
            ApiResultSet<List<RefinedMaterial>> listApiResultSet = refinedMaterialService.getRefinedMaterialListByMaterialOid(materialOid);
            List<RefinedMaterial> refinedMaterialList = new ArrayList<>();
            if (null != listApiResultSet.getData()) {
                refinedMaterialList = listApiResultSet.getData();
            }
            //无细化材料无需审核
            if (refinedMaterialList.size() <= 0) {
                resultStatus = FaStaticParam.EXAMINEL_RESULT_STATUS_NOT_AUDIT;
                qlCaseMaterial.setResultStatus(resultStatus);
                qlCaseMaterialServiceService.updateQlCaseMaterial(qlCaseMaterial);
                continue qlCaseMaterialFor;
            }

            //附件关联表无需审核
            ApiResultSet<List<QlCaseMaterialAtta>> qlCaseMaterialAttaResult = qlCaseMaterialAttaServiceService.queryQlCaseMaterialAttaByCaseMaterialOid(qlCaseMaterial.getCaseMaterialOid());
            List<QlCaseMaterialAtta> qlCaseMaterialAttaList = null;
            if (null != qlCaseMaterialAttaResult.getData()) {
                qlCaseMaterialAttaList = qlCaseMaterialAttaResult.getData();
            }
            if (qlCaseMaterialAttaList.size() <= 0) {
                resultStatus = FaStaticParam.EXAMINEL_RESULT_STATUS_NOT_AUDIT;
                qlCaseMaterial.setResultStatus(resultStatus);
                qlCaseMaterialServiceService.updateQlCaseMaterial(qlCaseMaterial);
                continue qlCaseMaterialFor;
            }

            if (resultList.size() <= 0) {
                //无ocr审核结果
                resultStatus = FaStaticParam.PRE_TRIAL_RESULT_STATUS_NOT_RESULT;
                qlCaseMaterial.setResultStatus(resultStatus);
                qlCaseMaterialServiceService.updateQlCaseMaterial(qlCaseMaterial);
                continue qlCaseMaterialFor;
            }

            refinedMaterialFor:
            for (RefinedMaterial refinedMaterial : refinedMaterialList) {
                String refinedMaterialOid = refinedMaterial.getOid();
                String materialCatalogOid = refinedMaterial.getMaterialCatalogOid();

                ApiResultSet<List<ReviewPoints>> reviewPointsResultSet = reviewPointsService.getReviewPointsListByRefinedMaterialOid(refinedMaterial.getOid());
                if (null != reviewPointsResultSet.getData()) {
                    List<ReviewPoints> reviewPointsList = reviewPointsResultSet.getData();
                    if (reviewPointsList.size() <= 0) {
                        //无关联审核要点无需审核
                        resultStatus = FaStaticParam.EXAMINEL_RESULT_STATUS_NOT_AUDIT;
                        continue refinedMaterialFor;
                    }
                }

                if (StringUtils.isEmpty(materialCatalogOid)) {
                    //没有配置智审（需人工核验）
                    resultStatus = FaStaticParam.EXAMINEL_RESULT_STATUS_ARTIFICIAL_AUDIT;
                    continue refinedMaterialFor;
                }
                //根据ocr结果判断
                if (null != resultList && resultList.size() > 0) {
                    for (int i = 0; i < resultList.size(); i++) {
                        AuditResult auditResults = resultList.get(i);
                        if (materialCatalogOid.equals(auditResults.getCatalogOid()) && refinedMaterialOid.equals(auditResults.getRefinedMaterialOid())) {
                            // 验证结果不通过，设置审核标识为1：不通过
                            if (auditResults.getValid() != null && auditResults.getValid() == FaStaticParam.FLASE_STATUS) {
                                resultStatus = FaStaticParam.EXAMINE_RESULT_STATUS_NOT_PASS;
                                qlCaseMaterial.setResultStatus(resultStatus);
                                qlCaseMaterialServiceService.updateQlCaseMaterial(qlCaseMaterial);
                                continue qlCaseMaterialFor;
                            }
                        }
                    }
                }
            }
            /*qlCaseMaterial.setAuditType(resultStatus);*/
            qlCaseMaterial.setResultStatus(resultStatus);
            qlCaseMaterialServiceService.updateQlCaseMaterial(qlCaseMaterial);
        }

        return qlCaseMaterialList;
    }


}
