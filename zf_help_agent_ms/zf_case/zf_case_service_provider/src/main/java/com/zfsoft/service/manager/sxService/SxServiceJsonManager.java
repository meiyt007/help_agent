package com.zfsoft.service.manager.sxService;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zfsoft.service.dbaccess.dao.sxDirectory.DbSxServiceTypeMapper;
import com.zfsoft.service.dbaccess.dao.sxService.*;
import com.zfsoft.service.dbaccess.dao.sxSys.DbSxSysAttaMapper;
import com.zfsoft.service.dbaccess.data.sxDirectory.DbSxServiceType;
import com.zfsoft.service.dbaccess.data.sxService.*;
import com.zfsoft.service.dbaccess.data.sxSys.DbSxSysAtta;
import com.zfsoft.service.common.SxptBaseStaticParameter;
import com.zfsoft.service.sxService.data.SxService;
import com.zfsoft.service.sxService.data.SxServiceJson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SxServiceJsonManager {

    @Resource
    private DbSxServiceMapper dbSxServiceMapper;
    @Resource
    private DbSxServiceJsonMapper dbSxServiceJsonMapper;

    @Resource
    private SxServiceManager sxServiceManager;
    @Resource
    private SxServiceJsonManager sxServiceJsonManager;

    @Resource
    private DbSxServiceExtendMapper dbSxServiceExtendMapper;
    @Resource
    private DbSxAcceptConditionMapper dbSxAcceptConditionMapper;
    @Resource
    private DbSxServiceQuestionMapper dbSxServiceQuestionMapper;
    @Resource
    private DbSxServiceLinkMapper dbSxServiceLinkMapper;
    @Resource
    private DbSxServiceMaterialMapper dbSxServiceMaterialMapper;
    @Resource
    private DbSxServiceChargeMapper dbSxServiceChargeMapper;
    @Resource
    private DbSxSysAttaMapper dbSxSysAttaMapper;
    @Resource
    private DbSxServiceLocationMapper dbSxServiceLocationMapper;
    @Resource
    private DbSxServiceTypeMapper dbSxServiceTypeMapper;


    /**
     * 获取事项表中所有需要解析的数据信息
     * @return
     */
    public List<SxServiceJson> getAllServiceJson(Date modifyDate){
        DbSxServiceJsonExample example=new DbSxServiceJsonExample();
        DbSxServiceJsonExample.Criteria criteria=example.createCriteria();
        if(modifyDate!=null){
            criteria.andModifyDateGreaterThan(modifyDate);
        }
        List<DbSxServiceJson> list= dbSxServiceJsonMapper.selectByExampleWithBLOBs(example);
        List<SxServiceJson> jsonList= list.stream().map(dbSxServiceJson -> {
            SxServiceJson sxService=new SxServiceJson();
            BeanUtil.copyProperties(dbSxServiceJson,sxService);
            return sxService;
        }).collect(Collectors.toList());
        return jsonList;
    }

    public  Map<String, Object> analysisDataJson(){
        Map<String, Object> modelMap=new HashMap<>();
        //获取上次解析的最大时间
        Date maxDate= dbSxServiceMapper.getMaxServiceTime();
        List<SxServiceJson> list= sxServiceJsonManager.getAllServiceJson(maxDate);
        if(list!=null && list.size()>0){
            String extendOid="";
            for(SxServiceJson service:list){
                //查询是否存在
                SxService oldService= sxServiceManager.getDbSxServiceByServiceOid(service.getOid());
                if(service.getDataJson()!=null){
                    JSONObject dataJson= JSONArray.parseObject(JSONArray.toJSON(service.getDataJson()).toString());
                    Map<String,Object> map=dataJson;
                    Map<String,Object> infoResult=new HashMap<>();
                    //事项结果
                    if(map.get("serviceResult")!=null){
                        infoResult=JSONObject.parseObject(map.get("serviceResult").toString());
                    }
                    String chargeAccord="";
                    String chargeStandard="";
                    String appointFlag="";
                    //事项信息
                    String handleScope="";
                    if(map.get("serviceInfo")!=null){
                        Map<String,Object> infoObj= JSONObject.parseObject(map.get("serviceInfo").toString());
                        handleScope=infoObj.get("handleScope")==null?"":infoObj.get("handleScope").toString();
                        DbSxServiceWithBLOBs servicesx= JSONObject.parseObject(JSONObject.toJSONString(infoObj), DbSxServiceWithBLOBs.class);
                        DbSxServiceWithBLOBs dbSxService=new DbSxServiceWithBLOBs();
                        BeanUtil.copyProperties(servicesx,dbSxService);
                        dbSxService.setServiceOid(service.getOid());
                        dbSxService.setDistrictOid(service.getDistrictOid());
                        dbSxService.setOrganOid(service.getOrganOid());
                        dbSxService.setCreateDate(service.getCreateDate());
                        dbSxService.setModifyDate(service.getModifyDate());
                        dbSxService.setImplementCode(service.getImplementCode());
                        dbSxService.setServiceTypeOid(service.getServiceTypeOid());
                        dbSxService.setDelFlag((short) 0);
                        dbSxService.setExistChildItem("0");
                        dbSxService.setServiceStatus(service.getContentStatus().shortValue());
                        dbSxService.setVersionNumber(service.getContentVersion().longValue());
                        if(service.getContentStatus()==8){
                            dbSxService.setServiceStatus((short) 3);
                        }

                        DbSxServiceType sysType= dbSxServiceTypeMapper.getDbSxServiceTypeByOid(service.getServiceTypeOid());
                        if(sysType!=null){
                            dbSxService.setServiceCharacter(sysType.getServiceCharacter()+"");
                        }

                        //事项字典
                        if(dbSxService.getImplementOrganProperty()!=null){
                            dbSxService.setImplementOrganProperty(SxptBaseStaticParameter.implementOrganPropertyMap.get(dbSxService.getImplementOrganProperty()));
                        }
                        //权力来源基础平台字典
                        if(dbSxService.getOrigin()!=null){
                            dbSxService.setOrigin(SxptBaseStaticParameter.ORGIN_QL_MAP.get(dbSxService.getOrigin()));
                        }
                        if(dbSxService.getHandleDepth()!=null){
                            dbSxService.setHandleDepth(SxptBaseStaticParameter.HANDLE_DEPTH_MAP.get(dbSxService.getHandleDepth()));
                        }

                        if(infoResult.get("resultDeliveryWay")!=null){
                            dbSxService.setResultDeliveryWay(infoResult.get("resultDeliveryWay").toString());
                        }
                        //收费信息设置
                        if(map.get("serviceChargeInfo")!=null){
                            JSONObject chargeJson= JSONObject.parseObject(map.get("serviceChargeInfo").toString());
                            dbSxService.setChargeFlag(Short.valueOf(chargeJson.get("chargeFlag").toString()));
                            //收费环节
                            if(chargeJson.get("chargeLinkOid")!=null && chargeJson.get("chargeLinkOid")!=""){
                                dbSxService.setChargeLinkOid(SxptBaseStaticParameter.LINK_MAP.get(chargeJson.get("chargeLinkOid").toString()));
                            }
                            //收费依据
                             chargeAccord= chargeJson.get("chargeAccord")==null?"":chargeJson.get("chargeAccord").toString();
                            //收费标准
                             chargeStandard= chargeJson.get("chargeStandard")==null?"":chargeJson.get("chargeStandard").toString();

                        }
                        //公开方式
                        if(dbSxService.getOpenWay()!=null){
                            if(dbSxService.getOpenWay()==0){
                                dbSxService.setOpenWay((short) 1);
                            }else if(dbSxService.getOpenWay()==1){
                                dbSxService.setOpenWay((short) 2);
                            }
                        }
                        //办事群体分类
                        if(infoObj.get("serviceGroupClassificatio")!=null){
                            StringBuffer bf=new StringBuffer();
                            if(infoObj.get("serviceGroupClassificatio").toString().indexOf("1")>-1){//中小学学生
                                bf.append("402881ef5ba2bf61015ba4469d2d0066,");
                            }
                            if(infoObj.get("serviceGroupClassificatio").toString().indexOf("2")>-1){//外国人
                                bf.append("55b834d1299411e794107824af9b43cd,");
                            }
                            if(infoObj.get("serviceGroupClassificatio").toString().indexOf("3")>-1){//港澳台侨
                                bf.append("55c16e2d299411e794107824af9b43cd");
                            }
                            if(bf!=null){
                                dbSxService.setServiceGroupClassification(bf.toString());
                            }
                        }
                        //生命周期分类
                        if(infoObj.get("lifeCycleClassification")!=null){
                            StringBuffer bf=new StringBuffer();
                            if(infoObj.get("lifeCycleClassification").toString().indexOf("1")>-1){//死亡殡葬
                                bf.append("402881ef5ba2bf61015ba4277775003a,");
                            }
                            if(infoObj.get("lifeCycleClassification").toString().indexOf("2")>-1){//社会保障
                                bf.append("402881ef5ba2bf61015ba424d66e0032,");
                            }
                            if(infoObj.get("lifeCycleClassification").toString().indexOf("3")>-1){//婚姻家庭
                                bf.append("402881ef5ba2bf61015ba4255b330034");
                            }
                            if(bf!=null){
                                dbSxService.setLifeCycleClassification(bf.toString());
                            }
                        }
                        //主题分类，目前取字典的自然人，可根据需要改
                        if(infoObj.get("subjectClassification")!=null){
                            StringBuffer bf=new StringBuffer();
                            if(infoObj.get("subjectClassification").toString().indexOf("1")>-1){//个人准营准办
                                bf.append("402881e55f941b97015f941d90380000,");
                            }
                            if(infoObj.get("subjectClassification").toString().indexOf("2")>-1){//婚姻登记
                                bf.append("402881f95bf033d0015bf041154c0003,");
                            }
                            if(infoObj.get("subjectClassification").toString().indexOf("3")>-1){//设立变更
                                bf.append("402881ef5ba2bf61015ba38555ae0018");
                            }
                            if(bf!=null){
                                dbSxService.setSubjectClassification(bf.toString());
                            }
                        }
                        //承诺时限说明
                        if(map.get("serviceExtend")!=null && map.get("serviceExtend")!="") {
                            Map<String, Object> infoObjExtend = JSONObject.parseObject(map.get("serviceExtend").toString());
                            dbSxService.setPromiseTimeDesc(infoObjExtend.get("promiseTimeDesc")==null?"":infoObjExtend.get("promiseTimeDesc").toString());
                        }
                        if(oldService!=null){
                            dbSxService.setId(oldService.getId());
                            dbSxService.setModifyDate(new Date());
                            dbSxServiceMapper.updateByPrimaryKeyWithBLOBs(dbSxService);
                        }else{
                            dbSxServiceMapper.insertSelective(dbSxService);
                        }
                    }
                    //扩展信息
                    if(map.get("serviceExtend")!=null && map.get("serviceExtend")!=""){
                        Map<String,Object> infoObj=JSONObject.parseObject(map.get("serviceExtend").toString());
                        DbSxServiceExtendWithBLOBs extend= JSONObject.parseObject(JSONObject.toJSONString(infoObj), DbSxServiceExtendWithBLOBs.class);
                        DbSxServiceExtendWithBLOBs dbSxServiceExtend=new DbSxServiceExtendWithBLOBs();
                        BeanUtil.copyProperties(extend,dbSxServiceExtend);
                        //扩展信息》收件凭证》送达相关要求
                        String recipientVoucherRequireme=infoObj.get("recipientVoucherRequireme")==null?"":infoObj.get("recipientVoucherRequireme").toString();
                        dbSxServiceExtend.setRecipientVoucherRequirement(recipientVoucherRequireme);


                        //面向自然人地方特色主题分类
                        //String naturalCharacteristic=infoObj.get("naturalCharacteristic")==null?"":infoObj.get("naturalCharacteristic").toString();

                        //面向法人地方特色主题分类
                        String legalCharacteristicName=infoObj.get("legalCharacteristicName")==null?"":infoObj.get("legalCharacteristicName").toString();
                        dbSxServiceExtend.setLegalCharacteristic(legalCharacteristicName);


                        //是否支持预约
                        appointFlag= infoObj.get("appointmentFlag")==null?"":infoObj.get("appointmentFlag").toString();
                        if(StrUtil.isNotEmpty(appointFlag)){
                            Short appointmentFlag=Short.parseShort(appointFlag);
                            oldService.setAppointmentFlag(appointmentFlag);
                            if(oldService!=null){
                                DbSxServiceWithBLOBs dbSxService=new DbSxServiceWithBLOBs();
                                BeanUtil.copyProperties(oldService,dbSxService);
                                dbSxService.setModifyDate(new Date());
                                dbSxServiceMapper.updateByPrimaryKeyWithBLOBs(dbSxService);
                            }
                        }

                        //承诺时限类型
                        if(StrUtil.isNotEmpty(dbSxServiceExtend.getPromiseLimitType())){
                            if(dbSxServiceExtend.getPromiseLimitType().equals("0")){//工作日
                                dbSxServiceExtend.setPromiseLimitType("W");
                            }else if(dbSxServiceExtend.getPromiseLimitType().equals("1")){//自然日
                                dbSxServiceExtend.setPromiseLimitType("H");
                            }
                        }
                        //法定时限类型
                        if(StrUtil.isNotEmpty(dbSxServiceExtend.getLegalLimitType())){
                            if(dbSxServiceExtend.getLegalLimitType().equals("0")){//工作日
                                dbSxServiceExtend.setLegalLimitType("W");
                            }else if(dbSxServiceExtend.getLegalLimitType().equals("1")){//自然日
                                dbSxServiceExtend.setLegalLimitType("H");
                            }
                        }
                        dbSxServiceExtend.setExtendOid(infoObj.get("oid").toString());
                        extendOid=infoObj.get("oid").toString();
                        dbSxServiceExtend.setServiceOid(service.getOid());
                        //通办范围处理
                        dbSxServiceExtend.setHandleScope(SxptBaseStaticParameter.HANDLE_SCOPE_MAP.get(handleScope));
                        //判断是否存在
                        DbSxServiceExtendWithBLOBs ext= dbSxServiceExtendMapper.getSxServiceExtendByOid(dbSxServiceExtend.getExtendOid());
                        //塞入结果信息
                        if(infoResult.get("oid")!=null && infoResult.get("oid")!=""){
                            dbSxServiceExtend.setResultOid(infoResult.get("oid").toString());
                        }
                        if(infoResult.get("resultName")!=null && infoResult.get("resultName")!=""){
                            dbSxServiceExtend.setResultName(infoResult.get("resultName").toString());
                        }
                        if(infoResult.get("resultSampleType")!=null && infoResult.get("resultSampleType")!=""){
                            dbSxServiceExtend.setResultSampleType(Short.valueOf(infoResult.get("resultSampleType").toString()) );
                        }
                        if(infoResult.get("resultSampleAddr")!=null && infoResult.get("resultSampleAddr")!=""){
                            JSONArray array=JSONArray.parseArray(infoResult.get("resultSampleAddr").toString());
                            JSONObject sampleAdr= JSONObject.parseObject(array.get(0).toString());
                            dbSxServiceExtend.setResultSampleAddr(sampleAdr.get("oid").toString());
                            //判断附件是否一样
                           /* if(ext!=null && ext.getResultSampleAddr()!=null &&  ext.getResultSampleAddr().equals(sampleAdr.get("oid").toString())){
                                dbSxServiceExtend.setResultSampleAddr(sampleAdr.get("oid").toString());
                            }else{
                                //调用附件保存方法
                                this.saveSysAtta(sampleAdr,service.getCreateDate());
                                dbSxServiceExtend.setResultSampleAddr(sampleAdr.get("oid").toString());
                            }*/
                        }else{
                            dbSxServiceExtend.setResultSampleAddr(null);
                        }
                        //流程信息
                        if(map.get("serviceFlow")!=null && map.get("serviceFlow")!=""){
                            JSONObject flowAdr= JSONObject.parseObject(JSONArray.toJSON(map.get("serviceFlow")).toString());
                            JSONArray array=JSONArray.parseArray(flowAdr.get("handleFlow").toString());
                            JSONObject flowAtta= array.getJSONObject(0);
                            dbSxServiceExtend.setHandleFlow(flowAtta.get("oid").toString());
                            dbSxServiceExtend.setHandleFlowInstruction(flowAdr.get("handleFlowInstruction").toString());
                            //判断是否相等
                            /*if(ext!=null && ext.getHandleFlow()!=null&& ext.getHandleFlow().equals(flowAtta.get("oid").toString())){
                                dbSxServiceExtend.setHandleFlow(flowAtta.get("oid").toString());
                            }else{
                                //调用附件保存方法
                                this.saveSysAtta(flowAtta,service.getCreateDate());
                                dbSxServiceExtend.setHandleFlow(flowAtta.get("oid").toString());
                            }*/
                        }else{
                            dbSxServiceExtend.setHandleFlow(null);
                        }
                        //收费信息设置
                        if(map.get("serviceChargeInfo")!=null){
                            JSONObject chargeJson= JSONObject.parseObject(map.get("serviceChargeInfo").toString());
                            JSONArray array=JSONArray.parseArray(chargeJson.get("chargeEvidence").toString());
                            if(array!=null){
                                JSONObject objCharge=array.getJSONObject(0);
                                dbSxServiceExtend.setChargeEvidence(objCharge.get("oid")==null?"":objCharge.get("oid").toString());
                            }
                        }

                        dbSxServiceExtend.setChargeAccord(chargeAccord);
                        dbSxServiceExtend.setChargeStandard(chargeStandard);
                        if(ext!=null){
                            dbSxServiceExtend.setId(ext.getId());
                            dbSxServiceExtendMapper.updateByPrimaryKeyWithBLOBs(dbSxServiceExtend);
                        }else{
                            dbSxServiceExtendMapper.insertSelective(dbSxServiceExtend);
                        }
                    }
                    //办理地址
                    if(map.get("serviceAddress")!=null && map.get("serviceAddress")!=""){
                        List listAddress=JSONArray.parseArray(map.get("serviceAddress").toString());
                        for(Object acceptObj:listAddress){
                            JSONObject obj=JSONObject.parseObject(acceptObj.toString());
                            //根据oid查询信息
                            DbSxServiceLocation condition= dbSxServiceLocationMapper.getDbSxServiceLocationByOid(obj.get("oid").toString());
                            //转为对象
                            DbSxServiceLocation accept=JSONObject.parseObject(acceptObj.toString(),DbSxServiceLocation.class);
                            accept.setExtendOid(extendOid);
                            accept.setDelFlag((short) 0);
                            accept.setServiceLocationOid(obj.get("oid").toString());
                            //存在更新，不存在插入
                            if(condition!=null){
                                accept.setId(condition.getId());
                                dbSxServiceLocationMapper.updateByPrimaryKeySelective(accept);
                            }else{
                                dbSxServiceLocationMapper.insert(accept);
                            }
                        }
                    }
                    //受理条件
                    if(map.get("serviceAccept")!=null && map.get("serviceAccept")!=""){
                        List listAccept=JSONArray.parseArray(map.get("serviceAccept").toString());
                        for(Object acceptObj:listAccept){
                            JSONObject obj=JSONObject.parseObject(acceptObj.toString());
                            DbSxAcceptCondition condition= dbSxAcceptConditionMapper.getSxAcceptConditionByOid(obj.get("oid").toString());
                            if(condition!=null){
                                condition.setModifyDate(service.getModifyDate());
                                condition.setConditionText(obj.get("conditionText").toString());
                                dbSxAcceptConditionMapper.updateByPrimaryKeySelective(condition);
                            }else{
                                DbSxAcceptCondition accept=new DbSxAcceptCondition();
                                accept.setServiceOid(service.getOid());
                                accept.setDelFlag((short) 0);
                                accept.setCreateDate(service.getCreateDate());
                                accept.setOrganOid(service.getOrganOid());
                                accept.setConditionOid(obj.get("oid").toString());
                                accept.setConditionText(obj.get("conditionText").toString());
                                accept.setModifyDate(service.getModifyDate());
                                dbSxAcceptConditionMapper.insert(accept);
                            }
                        }
                    }
                    //收费信息
                    if(map.get("serviceCharge")!=null&& map.get("serviceCharge")!=""){
                        List listCharge=JSONArray.parseArray(map.get("serviceCharge").toString());
                        for(Object acceptObj:listCharge){
                            JSONObject obj=JSONObject.parseObject(acceptObj.toString());
                            //根据oid查询信息
                            DbSxServiceChargeWithBLOBs condition= dbSxServiceChargeMapper.getSxServiceChargeByOid(obj.get("oid").toString());
                            //转为对象
                            DbSxServiceChargeWithBLOBs accept=JSONObject.parseObject(acceptObj.toString(),DbSxServiceChargeWithBLOBs.class);
                            accept.setServiceOid(service.getOid());
                            accept.setDelFlag((short) 0);
                            accept.setCreateDate(service.getCreateDate());
                            accept.setChargeOid(obj.get("oid").toString());
                            accept.setChargeTypeOid(SxptBaseStaticParameter.CHARGE_TYPE_MAP.get(accept.getChargeTypeOid()));
                            //存在更新，不存在插入
                            if(condition!=null){
                                accept.setId(condition.getId());
                                dbSxServiceChargeMapper.updateByPrimaryKeyWithBLOBs(accept);
                            }else{
                                dbSxServiceChargeMapper.insert(accept);
                            }
                        }
                    }
                    //申请材料
                    if(map.get("serviceMaterial")!=null && map.get("serviceMaterial")!=""){
                        List listMaterial=JSONArray.parseArray(map.get("serviceMaterial").toString());
                        for(Object acceptObj:listMaterial){
                            JSONObject obj=JSONObject.parseObject(acceptObj.toString());
                            //根据oid查询信息
                            DbSxServiceMaterialWithBLOBs condition= dbSxServiceMaterialMapper.getSxServiceMaterialByOid(obj.get("oid").toString());
                            //转为对象
                            DbSxServiceMaterialWithBLOBs accept=JSONObject.parseObject(acceptObj.toString(),DbSxServiceMaterialWithBLOBs.class);
                            accept.setServiceOid(service.getOid());
                            accept.setDelFlag((short) 0);
                            accept.setCreateDate(service.getCreateDate());
                            accept.setMaterialOid(obj.get("oid").toString());

                            //处理附件信息
                            //材料样本
                            if(obj.get("materialSampleAddr")!=null && obj.get("materialSampleAddr")!=""){
                                JSONArray array=JSONArray.parseArray(obj.get("materialSampleAddr").toString());
                                if(array!=null){
                                    JSONObject materialSampleAddrJson=array.getJSONObject(0);
                                    //如果原来的附件和现在的附件不一样则新增
                                    accept.setMaterialSampleAddr(materialSampleAddrJson.get("oid").toString());
                                    /*if(condition!=null && condition.getMaterialSampleAddr()!=null && condition.getMaterialSampleAddr().equals(materialSampleAddrJson.get("oid").toString())){
                                        accept.setMaterialSampleAddr(materialSampleAddrJson.get("oid").toString());
                                    }else{
                                        //调用附件保存方法
                                        this.saveSysAtta(materialSampleAddrJson,service.getCreateDate());
                                        accept.setMaterialSampleAddr(materialSampleAddrJson.get("oid").toString());
                                        accept.setMaterialSampleName(materialSampleAddrJson.get("filename").toString());
                                    }*/
                                }
                            }
                            //电子空表
                            if(obj.get("materialEmptyAddr")!=null){
                                JSONArray array=JSONArray.parseArray(obj.get("materialEmptyAddr").toString());
                                if(array!=null){
                                    JSONObject materialEmptyAddrJson=array.getJSONObject(0);
                                    //如果原来的附件和现在的附件不一样则新增
                                    accept.setMaterialEmptyAddr(materialEmptyAddrJson.get("oid").toString());
                                    /*if(condition!=null && condition.getMaterialEmptyAddr()!=null && condition.getMaterialEmptyAddr().equals(materialEmptyAddrJson.get("oid").toString())){
                                        accept.setMaterialEmptyAddr(materialEmptyAddrJson.get("oid").toString());
                                    }else{
                                        //调用附件保存方法
                                        this.saveSysAtta(materialEmptyAddrJson,service.getCreateDate());
                                        accept.setMaterialEmptyAddr(materialEmptyAddrJson.get("oid").toString());
                                    }*/
                                }
                            }
                            //电子表单
                            if(obj.get("electronicFormAddr")!=null && obj.get("electronicFormAddr")!=""){
                                JSONArray array=JSONArray.parseArray(obj.get("electronicFormAddr").toString());
                                if(array!=null){
                                    JSONObject electronicFormAddrJson=array.getJSONObject(0);
                                    //如果原来的附件和现在的附件不一样则新增
                                    accept.setElectronicFormAddr(electronicFormAddrJson.get("oid").toString());
                                    /*if(condition!=null && condition.getElectronicFormAddr()!=null && condition.getElectronicFormAddr().equals(electronicFormAddrJson.get("oid").toString())){
                                        accept.setElectronicFormAddr(electronicFormAddrJson.get("oid").toString());
                                    }else{
                                        //调用附件保存方法
                                        this.saveSysAtta(electronicFormAddrJson,service.getCreateDate());
                                        accept.setElectronicFormAddr(electronicFormAddrJson.get("oid").toString());
                                    }*/
                                }
                            }
                            if(null!=accept.getMaterialSource() ||StrUtil.isNotEmpty(accept.getMaterialSource().toString())){
                                if(accept.getMaterialSource().toString().equals("3")){
                                    accept.setMaterialSource((short) 2);
                                }
                            }
                            //存在更新，不存在插入
                            if(condition!=null){
                                accept.setId(condition.getId());
                                dbSxServiceMaterialMapper.updateByPrimaryKeyWithBLOBs(accept);
                            }else{
                                dbSxServiceMaterialMapper.insert(accept);
                            }
                        }
                    }
                    //常见问题
                    if(map.get("serviceQuestion")!=null && map.get("serviceQuestion")!=""){
                        List listQuestion=JSONArray.parseArray(map.get("serviceQuestion").toString());
                        for(Object acceptObj:listQuestion){
                            JSONObject obj=JSONObject.parseObject(acceptObj.toString());
                            //根据oid查询信息
                            DbSxServiceQuestionWithBLOBs condition= dbSxServiceQuestionMapper.getSxServiceQuestionByOid(obj.get("oid").toString());
                            //转为对象
                            DbSxServiceQuestionWithBLOBs accept=JSONObject.parseObject(acceptObj.toString(),DbSxServiceQuestionWithBLOBs.class);
                            accept.setServiceOid(service.getOid());
                            accept.setDelFlag((short) 0);
                            accept.setCreateDate(service.getCreateDate());
                            accept.setQuestionOid(obj.get("oid").toString());
                            accept.setModifyDate(service.getModifyDate());
                            //存在更新，不存在插入
                            if(condition!=null){
                                accept.setId(condition.getId());
                                dbSxServiceQuestionMapper.updateByPrimaryKeyWithBLOBs(accept);
                            }else{
                                dbSxServiceQuestionMapper.insert(accept);
                            }
                        }
                    }
                    //办理环节
                    if(map.get("serviceLink")!=null && map.get("serviceLink")!=""){
                        List listlink=JSONArray.parseArray(map.get("serviceLink").toString());
                        for(Object acceptObj:listlink){
                            JSONObject obj=JSONObject.parseObject(acceptObj.toString());
                            //根据oid查询信息
                            DbSxServiceLinkWithBLOBs condition= dbSxServiceLinkMapper.getSxServiceLinkByOid(obj.get("oid").toString());
                            //转为对象
                            DbSxServiceLinkWithBLOBs accept=JSONObject.parseObject(acceptObj.toString(),DbSxServiceLinkWithBLOBs.class);
                            accept.setServiceOid(service.getOid());
                            accept.setDelFlag((short) 0);
                            accept.setCreateDate(service.getCreateDate());
                            if(obj.get("transactLinkOid")!=null && obj.get("transactLinkOid")!=""){
                                accept.setLinkOid(SxptBaseStaticParameter.LINK_MAP.get(obj.get("transactLinkOid").toString()));
                            }
                            accept.setServiceLinkOid(obj.get("oid").toString());
                            accept.setModifyDate(service.getModifyDate());
                            //存在更新，不存在插入
                            if(condition!=null){
                                accept.setId(condition.getId());
                                dbSxServiceLinkMapper.updateByPrimaryKeyWithBLOBs(accept);
                            }else{
                                dbSxServiceLinkMapper.insert(accept);
                            }

                            modelMap.put("flag","success");
                        }
                    }
                }
            }
        }
        return  modelMap;
    }

    /**
     * 保存附件
     * @param objJSON
     * @param createDate
     * @return
     */
    private String saveSysAtta(JSONObject objJSON,Date createDate){
        DbSxSysAtta sysAtta=new DbSxSysAtta();
        sysAtta.setOid(objJSON.get("oid").toString());
        sysAtta.setName(objJSON.get("filename").toString());
        sysAtta.setOriginName(objJSON.get("filename").toString());
        sysAtta.setUploadDate(createDate);
        sysAtta.setIsDelete((short) 0);
        sysAtta.setExtensionName(objJSON.get("filename").toString().substring(objJSON.get("filename").toString().lastIndexOf(".")+1));
        dbSxSysAttaMapper.insert(sysAtta);
        return null;

    }
}
