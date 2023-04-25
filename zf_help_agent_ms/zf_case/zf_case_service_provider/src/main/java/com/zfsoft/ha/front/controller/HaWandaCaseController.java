package com.zfsoft.ha.front.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zfsoft.cases.data.QlCase;
import com.zfsoft.cases.data.QlCaseApplay;
import com.zfsoft.cases.data.QlCaseMaterial;
import com.zfsoft.cases.data.WdDataVo;
import com.zfsoft.cases.dbaccess.dao.QlCaseMapper;
import com.zfsoft.cases.dbaccess.dao.QlCaseMaterialAttaMapper;
import com.zfsoft.cases.dbaccess.dao.SysAttaMapper;
import com.zfsoft.cases.dbaccess.data.DbQlCase;
import com.zfsoft.cases.dbaccess.data.DbQlCaseMaterialAtta;
import com.zfsoft.cases.dbaccess.data.DbQlSysAtta;
import com.zfsoft.cases.manager.QlCaseApplayManager;
import com.zfsoft.cases.manager.QlCaseManager;
import com.zfsoft.cases.manager.QlCaseMaterialManager;
import com.zfsoft.cases.service.SysDictService;
import com.zfsoft.cases.util.StringUtil;
import com.zfsoft.ha.data.TripartiteVo.*;
import com.zfsoft.ha.thirdParty.HaWandaCaseService;
import com.zfsoft.ha.util.ClientServer;
import com.zfsoft.microservice.settings.data.SysDict;
import com.zfsoft.ocr.util.CommonRestUtil;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.service.dbaccess.dao.sxService.DbSxServiceMapper;
import com.zfsoft.service.dbaccess.data.sxService.DbSxService;
import com.zfsoft.service.manager.sxService.SxServiceMaterialManager;
import com.zfsoft.service.sxService.data.SxServiceMaterial;
import com.zfsoft.service.util.JsonUtil;
import com.zfsoft.single.util.ZipUtils;
import com.zfsoft.superwindow.service.easyquickcase.data.ServiceException;
import com.zfsoft.superwindow.util.FileUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.*;

/**
 * @Description //提交办件信息控制层
 * @Author: Wangyh
 * @Date: 2022/9/14 14:36
 */
@Slf4j
@RestController
public class HaWandaCaseController implements HaWandaCaseService {

    /**
     * 办件基本信息实现类
     */
    @Resource
    private QlCaseManager qlCaseManager;

    /**
     * 办件申请信息实现类
     */
    @Resource
    private QlCaseApplayManager qlCaseApplayManager;

    @Resource
    private DbSxServiceMapper dbSxServiceMapper;
    /**
     * 办件材料信息实现类
     */
    @Resource
    private QlCaseMaterialManager qlCaseMaterialManager;

    @Resource
    private SxServiceMaterialManager sxServiceMaterialManager;

    @Resource
    private QlCaseMaterialAttaMapper qlCaseMaterialAttaMapper;

    @Resource
    private QlCaseMapper qlCaseMapper;

    @Resource
    private SysAttaMapper sysAttaMapper;

    @Resource
    private SysDictService sysDictService;
    @Value("${zfsoft.inter.url}")
    private String interUrl;

    @Value("${zfsoft.inter.applicantConfirm}")
    private String applicantConfirm;

    @Value("${zfsoft.inter.addApplyStuff}")
    private String aaddApplyStuff;

    @Value("${zfsoft.inter.submitApply}")
    private String submitApply;

    @Value("${zfsoft.inter.addStuffAttachment}")
    private String addStuffAttachment;

    @Value("${zfsoft.inter.searchWorkflowCataLog}")
    private String searchWorkflowCataLog;

    @Value("${zfsoft.inter.searchWorkflowNodeInfo}")
    private String searchWorkflowNodeInfo;

    @Value("${zfsoft.inter.handlePostNode}")
    private String handlePostNode;

    @Value("${zfsoft.inter.saveDateUrl}")
    private String saveDateUrl;

    /**
     * redisTemplate
     */
    @Autowired
    private RedisTemplate redisTemplate;
    /**
     * 提交办件基本信息
     * @return
     * @throws Exception
     */
    public Map<String,Object> applicantConfirm(getWdCserIdVo getWdCserIdVo) throws ServiceException {
        Map<String,Object> mapResult = new HashMap<>();
        log.info("提交办件信息");
        //请求对象实体类
        WorkApplyInfoVo workApplyInfoVo = new WorkApplyInfoVo();
        String caseOid = getWdCserIdVo.getCaseOid();
        //办件信息
        QlCase qlCaseOld = qlCaseManager.queryQlCaseByCaseOid(caseOid);
        //办件申请信息
        QlCaseApplay applay = qlCaseApplayManager.queryQlCaseApplayByCaseOid(caseOid);
        //根据事项id查询事项信息
        DbSxService service=dbSxServiceMapper.getDbSxServiceByServiceOid(qlCaseOld.getServiceOid());

        //获取redis缓存个人信息
        WdDataVo wdDataVo = (WdDataVo) redisTemplate.opsForValue().get(caseOid);
        /**
         * 请求参数封装
         */
        Map<String,Object> map = new HashMap<>();
        workApplyInfoVo.setIsScene(service.getIsScene());//是否是主题事项，0：否；1：是 (数据库新增字段)
        workApplyInfoVo.setItemNo(service.getItemNo());//12位事项编码 (数据库新增字段)
        workApplyInfoVo.setUsername(wdDataVo.getName()); //申请人姓名
        if(StrUtil.isNotEmpty(applay.getApplyUserType()) && applay.getApplyUserType().equals("1")){
            ApiResultSet<com.zfsoft.microservice.settings.data.SysDict> dictApiResultSet = sysDictService.getSysDictByDictOid(applay.getCredentialType());
            SysDict sysDict = dictApiResultSet.getData();
            if(sysDict.getName().equals("身份证")){
                workApplyInfoVo.setLicenseType("111");//申请人证照类型，数值：1-身份证
            } else if(sysDict.getName().equals("军官证")){
                workApplyInfoVo.setLicenseType("114");//申请人证照类型，数值：1-身份证
            } else if(sysDict.getName().equals("士兵证")){
                workApplyInfoVo.setLicenseType("10");//申请人证照类型，数值：1-身份证
            }else if(sysDict.getName().equals("护照")){
                workApplyInfoVo.setLicenseType("414");//申请人证照类型，数值：1-身份证
            }else {
                workApplyInfoVo.setLicenseType("111");//申请人证照类型，数值：1-身份证
            }
        }else{
            workApplyInfoVo.setLicenseType("111");//申请人证照类型，数值：1-身份证
        }
        //新增
        workApplyInfoVo.setLicenseNo(wdDataVo.getCardNo());//申请人证照编码
        workApplyInfoVo.setMobile(wdDataVo.getPhone());//申请人手机号
        List<Status> statusList =new ArrayList<>();
        Status status = new Status();
        status.setStatusId(service.getServiceOid()); //情形id 这个写死是因为,实施录入的新增实施
//        status.setStatusId(service.getServiceOid()); //情形id
        if(StringUtil.isNotEmpty(service.getStatusNo())){
            status.setStatusNo(service.getStatusNo());//情形33位编码   (数据库新增字段)
        }else{
            status.setStatusNo(service.getImplementCode());//情形33位编码   (数据库新增字段)
        }

        status.setStatusName(service.getServiceName());//情形名称 service.getServiceName()
        statusList.add(status);
        workApplyInfoVo.setStatus(statusList);
        if(applay.getApplyUserType().equals("2")){ //企业
            workApplyInfoVo.setTargetType("企业");//办理对象类型，取值有个人和企业
        }else{ //个人
            workApplyInfoVo.setTargetType("个人");//办理对象类型，取值有个人和企业
        }
        workApplyInfoVo.setTargetNo(applay.getCredentialNumber()); //办理对象编号，如果类型是个人，则为身份证号等；如果类型是企业，则为统一社会信用代码
        workApplyInfoVo.setTargetName(applay.getApplyUserName());//办理对象名称，企业名称、个人姓名
        if(applay.getApplyUserType().equals("2")){ //企业
            workApplyInfoVo.setTargetLicenseType("01");//办理对象类型，取值有个人和企业  01统一社会信用代码 111-居民身份证
        }else{ //个人
            workApplyInfoVo.setTargetLicenseType("111");//办理对象类型，取值有个人和企业  01统一社会信用代码 111-居民身份证
        }

//        if(applay.getApplyUserType().equals("1")){
//            workApplyInfoVo.setTargetLicenseType("身份证"); //办理对象证件类型，取值：身份证、护照、军官证、营业执照
//        }else if(applay.getApplyUserType().equals("2")){
//            workApplyInfoVo.setLicenseType("护照");//申请人证照类型，数值：2-护照
//        }else if(applay.getApplyUserType().equals("3")){
//            workApplyInfoVo.setLicenseType("军官证");//申请人证照类型，数值：3-军官证
//        }else{
//            workApplyInfoVo.setLicenseType("营业执照");//申请人证照类型，数值：4-营业执照
//        }
        workApplyInfoVo.setCallId("");
        workApplyInfoVo.setOrgCode(service.getOrganOid());
        //拼接inter地址
        String url = interUrl + applicantConfirm;
        String json = JSON.toJSONString(workApplyInfoVo);
        map.put("json",json);
        log.info("json:{}",json);
        List<ResponseApplyVo> applyVoList= new ArrayList<>();
        try{
            String mapResult1 = CommonRestUtil.sendPostString(url,map);
            JSONObject jsonObject = JSON.parseObject(mapResult1);
            if(jsonObject.get("code").equals("200")){
                applyVoList = (List<ResponseApplyVo>) jsonObject.get("applyVoList");
                mapResult.put("code","200");
                mapResult.put("applyVoList",applyVoList);
            }else{
                mapResult.put("code","500");
                mapResult.put("applyVoList",null);
            }
        }catch (Exception e){
            mapResult.put("code","501");
            mapResult.put("applyVoList",null);
        }
        return mapResult;
    }

    /**
     * 推送数据到万达
     * @param pushDataVo
     * @return
     * @throws Exception
     */
    @Override
    public ApiResultSet pushData(PushDataVo pushDataVo) throws ServiceException, IOException {
        getWdCserIdVo getWdCserIdVo = new getWdCserIdVo();
        getWdCserIdVo.setCaseOid(pushDataVo.getCaseOid());
        //获取万达办件编号
        Map<String,Object> applicantConfirm = this.applicantConfirm(getWdCserIdVo);
        ResponseApplyVo responseApplyVo =null;
        File file2 = null;
        if(applicantConfirm.get("code").equals("200")){
            //获取办件信息接口返回参数
            List<ResponseApplyVo> applyVoList = (List<ResponseApplyVo>) applicantConfirm.get("applyVoList");
            for(int i=0;i<applyVoList.size();i++){
                String applyjson = String.valueOf(applyVoList.get(i));
                responseApplyVo = (ResponseApplyVo) JsonUtil.jsonToObject(applyjson,ResponseApplyVo.class);
                //因为我们是一条办件推一次，所以直接break
                break;
            }
            //数据库后来新增字段 end
            BeanUtil.copyProperties(responseApplyVo,pushDataVo);
            //推送的办件编号入库
            DbQlCase dbQlCase = qlCaseMapper.queryQlCaseByCaseOid(getWdCserIdVo.getCaseOid());
            dbQlCase.setCaseNumber(responseApplyVo.getApplyNo());
            qlCaseManager.updateQlcase(dbQlCase);
            //调用增加材料信息
            Map<String,Object> ApplyStuff = this.addApplyStuff(pushDataVo);
            if(ApplyStuff.get("code").equals("200")){
                //获取材料返回集合信息
                List<ResponseApplyStuffVo> applyStuffVoList = (List<ResponseApplyStuffVo>) ApplyStuff.get("applyVoList");
                ResponseApplyStuffVo responseApplyStuffVo = null; //声明响应类
                for(int i=0;i<applyStuffVoList.size();i++){ //循环提交材料派送 （这一层循环是一个事项有多个材料）
                    String applyStuffjson= String.valueOf(applyStuffVoList.get(i));
                    //###返回的材料响应类，一个材料（身份证）
                    responseApplyStuffVo = (ResponseApplyStuffVo) JsonUtil.jsonToObject(applyStuffjson,ResponseApplyStuffVo.class);
                    //需要拼接的图片
                    List<File> files = new ArrayList<>();
                    //需要上传的材料
                    MultipartFile multipartFile = null;
                    //1.根据CASE_MATERIAL_OID查询t_ql_case_material_atta表获取ATTA_OID存储位置
                    List<DbQlCaseMaterialAtta> dbQlCaseMaterialAttas = qlCaseMaterialAttaMapper.queryQlCaseMaterialAttaByCaseMaterialOid(responseApplyStuffVo.getRowguid());
                    for(int j=0;j<dbQlCaseMaterialAttas.size();j++){ //这一层循环是一个材料可能有多个材料。因为可能是高拍仪所以一个材料办件，可能会有多个材料，需要拼接成一个材料
                        if(dbQlCaseMaterialAttas.size()>1){
                            DbQlCaseMaterialAtta dbQlCaseMaterialAtta = dbQlCaseMaterialAttas.get(j); //获取办件材料att对象
                            DbQlSysAtta DbQlSysAtta = sysAttaMapper.querySysAttaByOid(dbQlCaseMaterialAtta.getAttaOid()); //根据t_ql_case_material_attaoid查询材料下载表
                            if(DbQlSysAtta !=null){
                                String fastdfsNginxUrl = DbQlSysAtta.getFastdfsNginxUrl();//拿到材料文件地址
                                responseApplyStuffVo.setExtensionName(DbQlSysAtta.getExtensionName());
                                /*
                                 * 这里以后需要增加高拍仪上传的材料照片拼接功能
                                 */
                                File file = null;
                                if (fastdfsNginxUrl.startsWith("http://")) {
                                     file   = FileUtils.getHttpFile(fastdfsNginxUrl);
                                } else if (fastdfsNginxUrl.startsWith("https://")) {
                                     file   = FileUtils.getSSLFile(fastdfsNginxUrl);
                                }
                                files.add(file);
                                }
                        }else{
                            DbQlCaseMaterialAtta dbQlCaseMaterialAtta = dbQlCaseMaterialAttas.get(j); //获取办件材料att对象
                            DbQlSysAtta DbQlSysAtta = sysAttaMapper.querySysAttaByOid(dbQlCaseMaterialAtta.getAttaOid()); //根据t_ql_case_material_attaoid查询材料下载表
                            if(DbQlSysAtta !=null){
                                String fastdfsNginxUrl = DbQlSysAtta.getFastdfsNginxUrl();//拿到材料文件地址
                                multipartFile = FileUtils.tranFile(fastdfsNginxUrl); //将地址转成文件流对象
                                responseApplyStuffVo.setExtensionName(DbQlSysAtta.getExtensionName());
                                //封装请求参数
                                Map<String,Object> map = this.addStuffAttachment(responseApplyStuffVo, multipartFile); //派送办件材料
                                if(!map.get("code").equals("200")){
                                    return new ApiResultSet(500,"数据推送失败,","材料派送接口调用失败");
                                }
                            }
                        }

                    }
                     if(dbQlCaseMaterialAttas.size()>1){
                         //获取当前项目目录
                        String path = System.getProperty("user.dir");
                        file2 = new File(path + "\\zf_help_agent_ms\\zf_case\\zf_case_service_provider\\src\\main\\java\\com\\zfsoft\\materialZip");
                        file2.mkdir();
                        String pathZip = path + "\\zf_help_agent_ms\\zf_case\\zf_case_service_provider\\src\\main\\java\\com\\zfsoft\\materialZip\\材料压缩包.zip";
                        File fileZip = new File(pathZip);
                        FileOutputStream fos2 = new FileOutputStream(fileZip);
                        ZipUtils.toZip(files, fos2);
                        multipartFile = FileUtils.tranInputStream(pathZip); //将地址转成文件流对象
                        //String composePath = composeDir + "composeJpg.jpg";
                        // 通过 File 对象的 getParent() 方法获取到根目录的上级目录
                        // 通过 System.getProperty("user.dir") 方式获取到项目根目录
//                        String parentPath = new File(path1).getParent();
                         //封装请求参数
                         Map<String,Object> map = this.addStuffAttachment(responseApplyStuffVo, multipartFile); //派送办件材料
                         if(!map.get("code").equals("200")){
                             return new ApiResultSet(500,"数据推送失败,","材料派送接口调用失败");
                         }
                        FileUtils.deleteFile(file2);//刪除文件路径
                        FileUtils.deleteFile(fileZip);//刪除文件路径
                     }
                }
                /*6.6领取方式*/
                Map<String,Object> saveData = this.saveDate(pushDataVo);
                if(!saveData.get("code").equals("200")){
                    return new ApiResultSet(500,"数据推送失败,", (String) saveData.get("error"));
                }
                //新增替换接口
                Map<String,Object> sendSubmitApply = this.submitApply(pushDataVo);
                if(!sendSubmitApply.get("code").equals("200")){
                    return new ApiResultSet(500,"数据推送失败,", (String) sendSubmitApply.get("error"));
                }

                /*//调用提交办件方法
                Map<String,Object> sendSubmitApply = this.submitApply(pushDataVo);
                if(sendSubmitApply.get("code").equals("200")){
                    List<ResponseSubmitVo> submitVoList= (List<ResponseSubmitVo>) sendSubmitApply.get("applyVoList");
                    ResponseSubmitVo responseSubmitVo =null;
                    for(int i=0;i<submitVoList.size();i++){
                        String submitJson = String.valueOf(submitVoList.get(i));
                        responseSubmitVo = (ResponseSubmitVo) JsonUtil.jsonToObject(submitJson,ResponseSubmitVo.class);
                    }
                }else{
                    return new ApiResultSet(500,"数据推送失败,","提交办件接口调用失败");
                }*/
            }else {
                return new ApiResultSet(500,"数据推送失败,","增加材料接口调用失败");
            }
        }else {
            return new ApiResultSet(500,"数据推送失败,","获取办件编号接口调用失败"+applicantConfirm.get("message"));
        }

        log.info("办件编号=",responseApplyVo.getApplyId());
        System.out.println("办件编号="+responseApplyVo.getApplyId());
        redisTemplate.delete(pushDataVo.getCaseOid());
       return ApiResultSet.ok("数据推送成功",responseApplyVo.getApplyNo());
    }
    //6.6 领取方式
    public Map<String,Object> saveDate(PushDataVo pushDataVo){
        /**
         * 增加材料测试入参
         */
        Map<String,Object> mapResult = new HashMap<>();
        SaveDataVo saveDataVo = new SaveDataVo();
        //办件申请信息
        QlCaseApplay applay = qlCaseApplayManager.queryQlCaseApplayByCaseOid(pushDataVo.getCaseOid());
        //办件信息
        QlCase qlCaseOld = qlCaseManager.queryQlCaseByCaseOid(pushDataVo.getCaseOid());
        saveDataVo.setApplyId(pushDataVo.getApplyId());
        saveDataVo.setCertWay(qlCaseOld.getCertWay());
        saveDataVo.setReceiver(applay.getApplyUserName());
        saveDataVo.setRevPhone(applay.getApplyUserPhone());//申请人电话
        saveDataVo.setRevZipcode(applay.getAddresseePostCode());
        String [] address = applay.getAddresseeAddress().split(",");
        List<String> addressList = Arrays.asList(address);
        saveDataVo.setRevProvince(addressList.get(0));//申请人地址所在（省、区、市）
        saveDataVo.setRevCity(addressList.get(1));//申请人地址所在（省、区、市）
        saveDataVo.setRevArea(addressList.get(2));//申请人地址所在（区
        saveDataVo.setRevAddress(applay.getAddresseeDetailAddress());//申请人地址
        saveDataVo.setSender("");
        saveDataVo.setSndPhone("");
        saveDataVo.setSndZipcode("");
        saveDataVo.setSndProvince("");
        saveDataVo.setSndCity("");
        saveDataVo.setSndArea("");
        saveDataVo.setSndAddress("");
        if(StringUtil.isNotEmpty(qlCaseOld.getExpressCompany())){
            saveDataVo.setExpressCompany(qlCaseOld.getExpressCompany()); //配送公司，取值为EMS、顺丰速递、其他
        }else{
            saveDataVo.setExpressCompany("");
        }
        //拼接inter地址
        Map<String,Object> map = new HashMap<>();
        String url = interUrl + saveDateUrl;
        String json = JSON.toJSONString(saveDataVo);
        map.put("json",json);
        try{
            String mapResult1 = CommonRestUtil.sendPostString(url,map);
            JSONObject jsonObject = JSON.parseObject(mapResult1);
            if(jsonObject.get("code").equals("200")){
                mapResult.put("code","200");
                mapResult.put("message",jsonObject.get("message"));
            }else{
                mapResult.put("code","500");
                mapResult.put("error","领取方式接口调用失败");
            }
        }catch (Exception e){
            mapResult.put("code","500");
            mapResult.put("error","领取方式接口调用失败");
        }
        return mapResult;
    }
    //增加材料
    public Map<String,Object> addApplyStuff(PushDataVo pushDataVo){
        /**
         * 增加材料测试入参
         */
        Map<String,Object> mapResult = new HashMap<>();
        //办件信息
        QlCase qlCaseOld = qlCaseManager.queryQlCaseByCaseOid(pushDataVo.getCaseOid());
        //根据事项id查询事项信息
        DbSxService service=dbSxServiceMapper.getDbSxServiceByServiceOid(qlCaseOld.getServiceOid());
        //查询是否已保存办件材料关系
        List<QlCaseMaterial> list = qlCaseMaterialManager.queryQlCaseMaterialByCaseOid(pushDataVo.getCaseOid());
        WorkApplyStuffVo workApplyStuffVo = new WorkApplyStuffVo(); //增加材料Vo
        workApplyStuffVo.setStuffStatus("0"); //取值范围：0为首次提交、2为补充材料 默认都是首次提交(数据库新增字段)
        workApplyStuffVo.setIsScene(service.getIsScene()); //是否是主题事项，0：否；1：是 (数据库新增字段)
        List<WorkApplyStuffVo.Stuffs> statusList = new ArrayList<>();
        for(int i=0;i<list.size();i++){
            QlCaseMaterial qlCaseMaterial = list.get(i); //办件材料对象
            SxServiceMaterial sxServiceMaterial = sxServiceMaterialManager.getSxServiceMaterialHasFileByOid(qlCaseMaterial.getMaterialOid());
            WorkApplyStuffVo.Stuffs status = new WorkApplyStuffVo.Stuffs();
            status.setApplyId(pushDataVo.getApplyId());  //(数据库新增字段)
            status.setProcessID(pushDataVo.getProcessId());  //(数据库新增字段)
            status.setStuffName(qlCaseMaterial.getMaterialName());
            status.setOrder("1"); //展示顺序，空时，自增
            if(StringUtil.isNotEmpty(sxServiceMaterial.getNmOriginal())){
                status.setNmOriginal(sxServiceMaterial.getNmOriginal()); //原件数量,默认1  //(数据库新增字段)
            }else{
                status.setNmOriginal("1");
            }
            if(StringUtil.isNotEmpty(sxServiceMaterial.getNmCopy())){
                status.setNmCopy(sxServiceMaterial.getNmCopy()); //复印件数量,默认1   //(数据库新增字段)
            }else{
                status.setNmCopy("1");
            }

            //不必传字段
            status.setRowguid(qlCaseMaterial.getCaseMaterialOid());  //先插入卓繁办件材料id,以便材料派送下载文件流
            status.setApplyStuffId("");
            status.setStuffDetailId("");
            status.setStuffCode("");
            status.setStuffId("");
            status.setSourceLicenseId("");
            status.setSourceStuffId("");
            status.setStuffType("");
            status.setStuffSource("");
            status.setDesc("");
            status.setAttachId("");
            statusList.add(status);
            workApplyStuffVo.setStuffs(statusList);
        }
        //拼接inter地址
        Map<String,Object> map = new HashMap<>();
        String url = interUrl + aaddApplyStuff;
        String json = JSON.toJSONString(workApplyStuffVo);
        map.put("json",json);
        List<ResponseApplyStuffVo> applyStuffVoList= new ArrayList<>();
        try{
            String mapResult1 = CommonRestUtil.sendPostString(url,map);
            JSONObject jsonObject = JSON.parseObject(mapResult1);
            if(jsonObject.get("code").equals("200")){
                applyStuffVoList = (List<ResponseApplyStuffVo>) jsonObject.get("applyVoList");
                mapResult.put("code","200");
                mapResult.put("applyVoList",applyStuffVoList);
            }
        }catch (Exception e){
            mapResult.put("code","500");
            mapResult.put("applyVoList",null);
        }
        return mapResult;
    }
    //18.4获取工作流列表
    public  Map<String,Object> searchWorkflowCataLog(PushDataVo pushDataVo){
        Map<String,Object> mapResult = new HashMap<>();
        /**
         * 封装请求参数
         */
        SearchWorkflowVo searchWorkflowVo = new SearchWorkflowVo();
        searchWorkflowVo.setPage(1);
        searchWorkflowVo.setSize(10);
        searchWorkflowVo.setStatusId(pushDataVo.getStatusId());
        searchWorkflowVo.setDatabaseID("262EA9AF-8D15-4CD3-8B20-18D614AB2453");
        searchWorkflowVo.setWorkflowId("");
        searchWorkflowVo.setOrganNodeId("");
        searchWorkflowVo.setOrganName("");
        searchWorkflowVo.setItemId("");
        searchWorkflowVo.setItemName("");
        searchWorkflowVo.setStatusName("");
        //拼接inter地址
        Map<String,Object> map = new HashMap<>();
        String url = interUrl + searchWorkflowCataLog;
        String json = JSON.toJSONString(searchWorkflowVo);
        map.put("json",json);
        ResponseWorkflowCatVo responseWorkflowCatVo = null;
        try{
            String mapResult1 = CommonRestUtil.sendPostString(url,map);
            JSONObject jsonObject = JSON.parseObject(mapResult1);
            if(jsonObject.get("code").equals("200")){
                JSONObject jsonObject1 = (JSONObject) jsonObject.get("responseWorkflowCatVo");
                if(jsonObject1 !=null){
                    String workflowCatVoJson = jsonObject1.toJSONString();
                    responseWorkflowCatVo = (ResponseWorkflowCatVo) JsonUtil.jsonToObject(workflowCatVoJson,ResponseWorkflowCatVo.class);
                    mapResult.put("code","200");
                    mapResult.put("responseWorkflowCatVo",responseWorkflowCatVo);
                }else{
                    mapResult.put("code","200");
                    mapResult.put("responseWorkflowCatVo",null);
                }

            }else{
                mapResult.put("code",500);
                mapResult.put("responseWorkflowCatVo","18.4获取工作流列表接口调用失败");
            }
        }catch (Exception e){
            mapResult.put("code","500");
            mapResult.put("responseWorkflowCatVo","18.4获取工作流列表接口调用失败");
        }
        return mapResult;
    }

    //18.5获取工作流明细
    public  Map<String,Object> searchWorkflowNodeInfo(String workflowId){
        log.info("获取工作流明细, workflowId: {}",workflowId);
        Map<String,Object> mapResult = new HashMap<>();
        /**
         * 封装请求参数
         */
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("workflowId",workflowId);

        //拼接inter地址
        Map<String,Object> map = new HashMap<>();
        String url = interUrl + searchWorkflowNodeInfo;
        String json = JSON.toJSONString(jsonObject);
        map.put("json",json);
        ResponseWorkflowNodeVo responseWorkflowNodeVo = null;
        try{
            String mapResult1 = CommonRestUtil.sendPostString(url,map);
            JSONObject jsonObject1 = JSON.parseObject(mapResult1);
            if(jsonObject1.get("code").equals("200")){
                JSONObject jsonObj = (JSONObject) jsonObject1.get("responseWorkflowNodeVo");
                String WorkflowNodeVoJson = jsonObj.toJSONString();
                responseWorkflowNodeVo = (ResponseWorkflowNodeVo) JsonUtil.jsonToObject(WorkflowNodeVoJson,ResponseWorkflowNodeVo.class);
                mapResult.put("code","200");
                mapResult.put("responseWorkflowNodeVo",responseWorkflowNodeVo);
            }else{
                mapResult.put("code",500);
                mapResult.put("responseWorkflowNodeVo","18.5获取工作流明细接口调用失败");
            }
        }catch (Exception e){
            mapResult.put("code",500);
            mapResult.put("responseWorkflowNodeVo","18.5获取工作流明细接口调用失败");
        }
        return mapResult;
    }

    //7.3 触发流程操作
    public  Map<String,Object> handlePostNode(ResponseWorkflowNodeVo responseWorkflowNodeVo,String applyID){
        log.info("获取工作流明细, responseWorkflowNodeVo: {}",responseWorkflowNodeVo);
        Map<String,Object> mapResult = new HashMap<>();
        /**
         * 封装请求参数
         */
        HandlePostNodeVo handlePostNodeVo =  new HandlePostNodeVo();
        if(responseWorkflowNodeVo == null){
            handlePostNodeVo.setWorkflowProcID("");
            handlePostNodeVo.setProcessDetail("");
        }else{
            handlePostNodeVo.setWorkflowProcID(responseWorkflowNodeVo.getProcessId());
            handlePostNodeVo.setProcessDetail(responseWorkflowNodeVo.getProcessDetail());

        }
        handlePostNodeVo.setNodeDetailId("");
        handlePostNodeVo.setProcessName("帮办提交");
        handlePostNodeVo.setProcessCode("HELP_UPLOAD");
        handlePostNodeVo.setApplyID(applyID);
        handlePostNodeVo.setAutoProcess("");
        handlePostNodeVo.setStartTime(null);
        handlePostNodeVo.setEndTime(null);
        //拼接inter地址
        Map<String,Object> map = new HashMap<>();
        String url = interUrl + handlePostNode;
        String json = JSON.toJSONString(handlePostNodeVo);
        map.put("json",json);
        List<ResponseHandlePostNodeVo> applyStuffVoList= new ArrayList<>();
        try{
            String mapResult1 = CommonRestUtil.sendPostString(url,map);
            JSONObject jsonObject1 = JSON.parseObject(mapResult1);
            if(jsonObject1.get("code").equals("200")){
                applyStuffVoList = (List<ResponseHandlePostNodeVo>) jsonObject1.get("applyVoList");
                mapResult.put("code","200");
                mapResult.put("applyVoList",applyStuffVoList);
            }else{
                mapResult.put("code",500);
                mapResult.put("applyVoList","7.3 触发流程操作接口调用失败");
            }
        }catch (Exception e){
            mapResult.put("code",500);
            mapResult.put("applyVoList","7.3 触发流程操作接口调用失败");
        }
        return mapResult;
    }
    //提交办件
    public  Map<String,Object> submitApply(PushDataVo pushDataVo){
        Map<String,Object> mapResult = new HashMap<>();
        Map<String,Object> searchWorkflowCataLog = this.searchWorkflowCataLog(pushDataVo);
        if(searchWorkflowCataLog.get("code").equals("200")){
            ResponseWorkflowCatVo responseWorkflowCatVo =null;
             responseWorkflowCatVo = (ResponseWorkflowCatVo) searchWorkflowCataLog.get("responseWorkflowCatVo");
            ResponseWorkflowNodeVo responseWorkflowNodeVo = null;
            if(responseWorkflowCatVo != null){//判断是否有工作流
//                responseWorkflowCatVo = (ResponseWorkflowCatVo) JsonUtil.jsonToObject(resultJson,ResponseWorkflowCatVo.class);
                Map<String,Object> searchWorkflowNodeInfo = this.searchWorkflowNodeInfo(responseWorkflowCatVo.getWorkflowId());
                if(searchWorkflowNodeInfo.get("code").equals("200")){
                    String resultNodeJson = (String) searchWorkflowCataLog.get("responseWorkflowNodeVo");
                    responseWorkflowNodeVo = (ResponseWorkflowNodeVo) JsonUtil.jsonToObject(resultNodeJson,ResponseWorkflowNodeVo.class);
                    Map<String,Object> handlePostNode = this.handlePostNode(responseWorkflowNodeVo,pushDataVo.getApplyId());
                    if(handlePostNode.get("code").equals("200")){
                        mapResult.put("code","200");
                        mapResult.put("applyVoList",handlePostNode.get("applyVoList"));
                    }
                }else{
                    mapResult.put("code",500);
                    mapResult.put("error",searchWorkflowCataLog.get("responseWorkflowNodeVo"));
                }
            }else{
                    Map<String,Object> handlePostNode = this.handlePostNode(responseWorkflowNodeVo,pushDataVo.getApplyId());
                    if(handlePostNode.get("code").equals("200")){
                        mapResult.put("code","200");
                        mapResult.put("applyVoList",handlePostNode.get("applyVoList"));
                    }

            }

        }else{

            mapResult.put("code",500);
            mapResult.put("error",searchWorkflowCataLog.get("responseWorkflowCatVo"));
        }

        return mapResult;
    }

    //提交办件
    /*public  Map<String,Object> submitApply(PushDataVo pushDataVo){
        *//**
         * 提交办件入参对象测试
         *//*
        Map<String,Object> mapResult = new HashMap<>();
        //办件申请信息
        QlCaseApplay applay = qlCaseApplayManager.queryQlCaseApplayByCaseOid(pushDataVo.getCaseOid());
        //办件信息
        QlCase qlCaseOld = qlCaseManager.queryQlCaseByCaseOid(pushDataVo.getCaseOid());
        WorkUnionLogisticsVo workUnionLogisticsVo = new WorkUnionLogisticsVo();
        *//*
        * 封装请求参数
        * *//*
        workUnionLogisticsVo.setApplyId(pushDataVo.getApplyId());//办件Id
        workUnionLogisticsVo.setApplySubmitType("已收件"); //办件提交类型（已收件、当场办结、当场退件）
        workUnionLogisticsVo.setCertWay(qlCaseOld.getCertWay()); //发证方式：自取，物流，无结果物
        if(applay.getApplyUserType().equals("2")){ //企业
            workUnionLogisticsVo.setApplicant("企业"); //办理对象类型，取值有个人和企业
        }else{ //个人
            workUnionLogisticsVo.setApplicant("个人"); //办理对象类型，取值有个人和企业
        }
        if(StringUtil.isNotEmpty(applay.getApplyUserName())){
            workUnionLogisticsVo.setReceiver(applay.getApplyUserName()); //寄件人姓名（经办人）
        }else{
            workUnionLogisticsVo.setReceiver("");
        }
        if(StringUtil.isNotEmpty(applay.getAddresseeAddress())){
            workUnionLogisticsVo.setRevAddress(applay.getAddresseeAddress());//申请人地址
        }else{
            workUnionLogisticsVo.setRevAddress("");
        }
        if(StringUtil.isNotEmpty(applay.getAddresseeAddress())){
            String [] address = applay.getAddresseeAddress().split(",");
            List<String> addressList = Arrays.asList(address);
            workUnionLogisticsVo.setRevProvince(addressList.get(0));//申请人地址所在（省、区、市）
            workUnionLogisticsVo.setRevCity(addressList.get(1));//申请人地址所在（省、区、市）
            workUnionLogisticsVo.setRevArea(addressList.get(2));//申请人地址所在（区
        }else{
            workUnionLogisticsVo.setRevProvince("");//申请人地址所在（省、区、市）
            workUnionLogisticsVo.setRevCity("");//申请人地址所在（省、区、市）
            workUnionLogisticsVo.setRevArea("");//申请人地址所在（区
        }
        *//*for(int i=0;i<addressList.size();i++){
            String addrs = addressList.get(i);
            if(addrs.indexOf("省")!=-1){
                workUnionLogisticsVo.setRevProvince(addrs);//申请人地址所在（省、区、市）
            }else if(addrs.indexOf("市")!=-1){
                workUnionLogisticsVo.setRevCity(addrs);//申请人地址所在（城市）
            }else{
                workUnionLogisticsVo.setRevArea(addrs);//申请人地址所在（区
            }
        }*//*
        workUnionLogisticsVo.setRevZipcode(applay.getAddresseePostCode());//申请人邮编
        workUnionLogisticsVo.setRevPhone(applay.getApplyUserPhone());//申请人电话
        workUnionLogisticsVo.setExpressCompany(qlCaseOld.getExpressCompany()); //配送公司，取值为EMS、顺丰速递、其他
        workUnionLogisticsVo.setStartTime(new Date());//办件窗口受理开始时间，即窗口收件环节的开始时间
       //拼接inter地址
        Map<String,Object> map = new HashMap<>();
        String url = interUrl + submitApply;
        String json = JSON.toJSONString(workUnionLogisticsVo);
        map.put("json",json);
        List<ResponseSubmitVo> submitVoList= new ArrayList<>();
        try{
            String mapResult1 = CommonRestUtil.sendPostString(url,map);
            JSONObject jsonObject = JSON.parseObject(mapResult1);
            if(jsonObject.get("code").equals("200")){
                submitVoList = (List<ResponseSubmitVo>) jsonObject.get("applyVoList");
                mapResult.put("code","200");
                mapResult.put("applyVoList",submitVoList);
            }
        }catch (Exception e){
            mapResult.put("code",500);
            mapResult.put("applyVoList",submitVoList);
        }
        return mapResult;
    }*/

    //材料添加附件并关联派生
    public Map<String,Object> addStuffAttachment(ResponseApplyStuffVo responseApplyStuffVo,MultipartFile file) throws ServiceException {
        Map<String,Object> mapResult = new HashMap<>();
        Map<String,Object> formTexts = new HashMap<>();
        //请求参数
        formTexts.put("stuffId",responseApplyStuffVo.getStuffID());
        formTexts.put("catalogCode","");
        formTexts.put("stuffUserCard","");
        formTexts.put("stuffUserType","");
        formTexts.put("catalogCode","");
        formTexts.put("stuffUser","");
        formTexts.put("stuffEndTime","");
        formTexts.put("stuffType","0");
        formTexts.put("stuffSource","未实现复用");
        Map<String,MultipartFile> files = new HashMap<>();
        //inter地址
        String url = interUrl + addStuffAttachment;
        files.put(responseApplyStuffVo.getStuffName()+"."+responseApplyStuffVo.getExtensionName(),file);
        String mapResult1 = null;
        try {
            mapResult1 = ClientServer.sendPost(url,null,formTexts,files,null,null);
            JSONObject jsonObject = JSON.parseObject(mapResult1);
            if(jsonObject.get("code").equals("200")){
                List<ResponseaStuffAttaVo> applyStuffVoList = (List<ResponseaStuffAttaVo>) jsonObject.get("applyVoList");
                mapResult.put("code","200");
                mapResult.put("applyVoList",applyStuffVoList);
            }
        }catch (Exception e){
            mapResult.put("code","500");
            mapResult.put("applyVoList",null);
        }
      return mapResult;
    }


    public static void main(String[] args) {
        try {
            //File file   = FileUtils.getSSLFile("https://hf.zhuofansoft.com:12102/group1/M00/C0/34/rKj6BmJTv9OAS0yzAABtX1yr6Bs882.jpg");
            //File file1   = FileUtils.getSSLFile("https://hf.zhuofansoft.com:12102/group1/M00/C0/35/rKj6BmJTyguAVC1BAACZMtxWd4k401.jpg");
            File file   = FileUtils.getHttpFile("http://139.9.123.180/group1/M00/00/11/iwl7tGMgIKuAXYW-AACGuUyWFLU190.jpg");
            File file1   = FileUtils.getHttpFile("http://139.9.123.180/group1/M00/00/28/iwl7tGMrArCAeHC2AACGuUyWFLU635.jpg");

            List list = new ArrayList();
            list.add(file);
            list.add(file1);
            File file2 = new File( "C:\\Users\\Lenovo\\Desktop\\jar");
            file2.mkdir();
            //list:多张图片的绝对路径 file1：拼接成功后的图片的存放文件夹   file3：拼接成功后的图片
            File file3 = compositePhoto(list,  file2 + "\\composeJpg.jpg");
            if(file3 != null){
                //1.将文件地址转成MultipartFile流对象
                MultipartFile multipartFile = FileUtils.tranInputStream(file3+"");
                ResponseApplyStuffVo responseApplyStuffVo = new ResponseApplyStuffVo();
                responseApplyStuffVo.setStuffID("8c7a1ef5-d874-4ac6-8361-c9803f86cf30");
                responseApplyStuffVo.setStuffName("composeJpg");
                HaWandaCaseController haWandaCaseController = new HaWandaCaseController();
                Map<String, Object> map = haWandaCaseController.addStuffAttachment(responseApplyStuffVo, multipartFile);
                if(map.get("code").equals(200)){
                    Object applyVoList = map.get("applyVoList");
                }else {
                    System.out.println("返回失败");
                }
                //2.删除上面的文件夹
            }


        }catch (Exception e){
            e.printStackTrace();
        }

    }

    /**
     * 图片合成
     * @param files 合成图片集合
     * @param filePathNmae 合成图片后的路径含名称
     * @return
     * @throws IOException
     */
    public static File compositePhoto(List<File> files,String filePathNmae) throws IOException{
        int width = 0;
        int height = 0;
        for (File file : files) {
            BufferedImage bi = ImageIO.read(file);
            width = bi.getWidth();
            height = height + bi.getHeight();
        }
        //合成的图片
        BufferedImage cp = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics g = cp.getGraphics();
        for (int i = 0; i < files.size(); i++) {
            BufferedImage bi = ImageIO.read(files.get(i));
            if (i ==0) {
                g.drawImage(bi,0,0,null);//第一个
            } else {
                BufferedImage bAfter = ImageIO.read(files.get(i-1));
                g.drawImage(bi,0,bAfter.getHeight(),null);//后面部分
            }
        }
        //检查文件
        File f = new File(filePathNmae);
        if(f.exists()){
            f.delete();
            f.createNewFile();
        }else{
            f.createNewFile();
        }
        //输出图片
        ImageIO.write(cp, "jpg", f);
        return f;
    }


}
