package com.zfsoft.single.manager.ywbl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zfsoft.cases.data.QlCase;
import com.zfsoft.cases.data.QlCaseApplay;
import com.zfsoft.cases.data.QlCaseMaterial;
import com.zfsoft.cases.data.QlCaseMaterialAtta;
import com.zfsoft.cases.service.QlCaseApplayService;
import com.zfsoft.cases.service.QlCaseMaterialAttaService;
import com.zfsoft.cases.service.QlCaseMaterialService;
import com.zfsoft.cases.service.QlCaseService;
import com.zfsoft.cases.util.SendSmsUtil;
import com.zfsoft.single.data.ywbl.vo.CaseMaterialVo;
import com.zfsoft.single.dbaccess.dao.ywbl.DbQlCaseCorrectionMapper;
import com.zfsoft.single.dbaccess.dao.ywbl.DbQlCorrectionMaterialMapper;
import com.zfsoft.single.dbaccess.data.ywbl.DbQlCaseCorrection;
import com.zfsoft.single.dbaccess.data.ywbl.DbQlCaseCorrectionExample;
import com.zfsoft.single.dbaccess.data.ywbl.DbQlCorrectionMaterial;
import com.zfsoft.single.feign.settings.*;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.platform.common.security.data.CurrentLoginUser;
import com.zfsoft.platform.common.security.data.CurrentLoginUserHolder;
import com.zfsoft.single.data.ywbl.CaseRqhb;
import com.zfsoft.single.data.ywbl.QlCaseCorrection;
import com.zfsoft.single.util.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;


/**
 * @author: dongxl
 * @create: 2020-11-17
 * @description: 办件补齐补正
 */
@Slf4j
@Service
public class QlCaseCorrectionManager {

    @Resource
    private DbQlCaseCorrectionMapper dbQlCaseCorrectionMapper;
    @Resource
    private QlCaseMaterialService qlCaseMaterialServiceFeginService;
    @Resource
    private CaseRqhbManager caseRqhbManager;
    @Resource
    private QlCaseMaterialAttaService qlCaseMaterialAttaServiceFeginService;
    @Resource
    private DbQlCorrectionMaterialMapper dbQlCorrectionMaterialMapper;
    @Resource
    private QlCaseApplayService qlCaseApplayServiceFeginService;
    @Resource
    private QlCaseService qlCaseServiceFeginService;

    /**
     * 保存补齐补正信息
     * dongxl
     * @param vo
     */
    @Transactional(rollbackFor = Exception.class)
    public void saveOrUpdate(CaseMaterialVo vo) {
        if(vo!=null){
            if(vo.getId()!=null){
                DbQlCaseCorrection caseCorrection=dbQlCaseCorrectionMapper.selectByPrimaryKey(vo.getId());
                if(caseCorrection !=null){
                    caseCorrection.setCorrectionUserOid(CurrentLoginUserHolder.getCurrentLoginUser().getUserOid());
                    caseCorrection.setCorrectionStatus(String.valueOf(SysCode.STATUS.YES));//已补正
                    caseCorrection.setMemo(vo.getMemo());
                    caseCorrection.setPatchOpinion(vo.getPatchOpinion());
                    caseCorrection.setPatchResult(vo.getPatchResult());
                    caseCorrection.setModifyDate(new Date());
                    caseCorrection.setPatchTime(new Date());
                    this.dbQlCaseCorrectionMapper.updateByPrimaryKeySelective(caseCorrection);
                }
            }
            if(vo.getMaterialCollect()!=null&&vo.getMaterialCollect().size()>0){
                List materialType= vo.getMaterialCollect();
                List materials=new ArrayList<QlCaseMaterial>();
                for(Object indType:materialType){
                    JSONObject obj= (JSONObject) JSONObject.toJSON(indType);
                    String caseMaterialOid= obj.get("materialOid").toString();
                    //根据材料编号查询材料信息
                    ApiResultSet<QlCaseMaterial> caseMaterialRes=qlCaseMaterialServiceFeginService.queryMaterialByCaseMaterialOid(caseMaterialOid);
                    if(caseMaterialRes.getData()!=null){
                        QlCaseMaterial caseMaterial=caseMaterialRes.getData();
                        caseMaterial.setCollectionType(obj.get("collectionType").toString());
                        if(!obj.get("collectionType").toString().equals("4")){
                            caseMaterial.setCollectionFlag(1);
                            caseMaterial.setCollectionDate(new Date());
                        }
                        //判断是否是证照收取
                        if(obj.get("collectionType").toString().equals("5")){
                            if(vo.getElemLicense()!=null&&vo.getElemLicense().size()>0){
                                for(Object elem:vo.getElemLicense()){
                                    JSONObject objElem= (JSONObject) JSONObject.toJSON(elem);
                                    if(objElem.get("materialOid").equals(caseMaterialOid)){
                                        caseMaterial.setElemLicenseOid(objElem.get("attaOid")==null?"":objElem.get("attaOid").toString());
                                        caseMaterial.setElemNumber(objElem.get("originName")==null?"":objElem.get("originName").toString());
                                        break;
                                    }
                                }
                            }
                        }
                        materials.add(caseMaterial);
                        //删除材料下面的附件
                      ApiResultSet attaResule=  qlCaseMaterialAttaServiceFeginService.deleteQlCaseMaterialAttaByCaseMaterialOid(caseMaterial.getCaseMaterialOid());
                    }
                }
                ApiResultSet dataReturn= qlCaseMaterialServiceFeginService.saveQlCaseMaterial(materials);
                if(dataReturn.getCode()==200){
                    if(vo.getMaterialAtta()!=null&&vo.getMaterialAtta().size()>0){
                        List materialAttas=new ArrayList<QlCaseMaterialAtta>();
                        List materialAtta= vo.getMaterialAtta();
                        for(Object attaObj:materialAtta){
                            JSONObject obj= (JSONObject) JSONObject.toJSON(attaObj);
                            String caseMaterialOid= obj.get("materialOid").toString();
                            String attaOid= obj.get("attaOid").toString();
                            QlCaseMaterialAtta atta =new QlCaseMaterialAtta();
                            atta.setAttaOid(attaOid);
                            atta.setCaseMaterialOid(caseMaterialOid);
                            materialAttas.add(atta);
                        }
                        qlCaseMaterialAttaServiceFeginService.saveQlCaseMaterialAttaList(materialAttas);
                    }
                }
            }
        }
    }

    /**
     * 补正列表查询
     * dongxl
     * @return
     */
    public List<QlCaseCorrection> queryPage(QlCaseCorrection qlCaseCorrection) {
        Map<String,Object> map = new HashMap<String,Object>();
        CurrentLoginUser loginUser = CurrentLoginUserHolder.getCurrentLoginUser();

        if(null!=qlCaseCorrection){
            if(StrUtil.isNotEmpty(qlCaseCorrection.getNoticeFlag())){
                map.put("noticeFlag",qlCaseCorrection.getNoticeFlag());
            }
            if(StrUtil.isNotEmpty(qlCaseCorrection.getCorrectionStatus())){
                if (qlCaseCorrection.getCorrectionStatus().equals("1")){//待补正
                    map.put("correctionStatus","0");
                }else if (qlCaseCorrection.getCorrectionStatus().equals("2")){//已补正
                    map.put("correctionStatus","1");
                }else if (qlCaseCorrection.getCorrectionStatus().equals("3")){//终止
                    map.put("correctionStatus","2");
                }
            }
            if(StrUtil.isNotEmpty(qlCaseCorrection.getCaseOid())){
                map.put("caseOid",qlCaseCorrection.getCaseOid());
            }else {
                if(StrUtil.isNotEmpty(qlCaseCorrection.getStartDate())){
                    map.put("startDate",DateUtil.startDateFormat(qlCaseCorrection.getStartDate()));
                }
                if(StrUtil.isNotEmpty(qlCaseCorrection.getEndDate())){
                    map.put("endDate", DateUtil.endDateFormat(qlCaseCorrection.getEndDate()));
                }
            }
            if(StrUtil.isNotEmpty(qlCaseCorrection.getApplyUserName())){
                map.put("applyUserName",qlCaseCorrection.getApplyUserName());
            }
            if(StrUtil.isNotEmpty(qlCaseCorrection.getCaseNumber())){
                map.put("caseNumber",qlCaseCorrection.getCaseNumber());
            }
            if(!CurrentLoginUserHolder.getIsAdminUser()){
                if(qlCaseCorrection.getServiceOids()!=null &&qlCaseCorrection.getServiceOids().size()>0){
                    map.put("serviceOids",qlCaseCorrection.getServiceOids());
                }else{
                    return new ArrayList<QlCaseCorrection>();
                }
            }

        }
        List<DbQlCaseCorrection> dbQlCaseCorrection=dbQlCaseCorrectionMapper.selectQlCaseCorrectionList(map);
        return BeanUtils.copyListProperties(dbQlCaseCorrection, QlCaseCorrection::new);
    }

    public List<QlCaseCorrection> getListByCaseOid(String caseOid) {
        Assert.hasLength(caseOid, "办件主键不能为空！");
        DbQlCaseCorrectionExample dbQlCaseCorrectionExample = new DbQlCaseCorrectionExample();
        DbQlCaseCorrectionExample.Criteria criteria = dbQlCaseCorrectionExample.createCriteria();
        if (StrUtil.isNotEmpty(caseOid)) {
            criteria.andCaseOidEqualTo(caseOid);
        }
        List<DbQlCaseCorrection> dbCaseCorr = dbQlCaseCorrectionMapper.selectByExample(dbQlCaseCorrectionExample);
        if (!CollectionUtils.isEmpty(dbCaseCorr)) {
            return BeanUtils.copyListProperties(dbCaseCorr, QlCaseCorrection::new);
        }
        return null;
    }

    public QlCaseCorrection getQlCorrectByCaseOid(String caseOid) {
        Assert.hasLength(caseOid, "办件主键不能为空！");
        DbQlCaseCorrectionExample dbQlCaseCorrectionExample = new DbQlCaseCorrectionExample();
        DbQlCaseCorrectionExample.Criteria criteria = dbQlCaseCorrectionExample.createCriteria();
        if (StrUtil.isNotEmpty(caseOid)) {
            criteria.andCaseOidEqualTo(caseOid);
        }
        criteria.andCorrectionStatusEqualTo("0");//待补正状态
        List<DbQlCaseCorrection> dbCaseCorr = dbQlCaseCorrectionMapper.selectByExample(dbQlCaseCorrectionExample);
        if (!CollectionUtils.isEmpty(dbCaseCorr)) {
            QlCaseCorrection correction=new QlCaseCorrection();
            BeanUtils.copyProperties(dbCaseCorr.get(0),correction);
            return correction;
        }
        return null;
    }

    public QlCaseCorrection getOneByid(Long id) {
        Assert.hasLength(String.valueOf(id), "主键不能为空！");
        DbQlCaseCorrection caseCorrection=dbQlCaseCorrectionMapper.selectByPrimaryKey(id);
        QlCaseCorrection caseCo=new QlCaseCorrection();
        BeanUtils.copyProperties(caseCorrection,caseCo);
        return caseCo;
    }

    /**
     * 保存告知信息
     * dongxl
     * @param qlCaseCorrection
     */
    @Transactional(rollbackFor = Exception.class)
    public void saveOrUpdateNotice(QlCaseCorrection qlCaseCorrection) {
        if(qlCaseCorrection!=null){
            if(qlCaseCorrection.getId()!=null){
                DbQlCaseCorrection caseCorrection=dbQlCaseCorrectionMapper.selectByPrimaryKey(qlCaseCorrection.getId());
                Assert.notNull(caseCorrection, MessageFormat.format("更新对象不存在！对象id为{0}", caseCorrection.getId()));
                caseCorrection.setMessageFlag(qlCaseCorrection.getMessageFlag());
                caseCorrection.setPhoneFlag(qlCaseCorrection.getPhoneFlag());
                caseCorrection.setNoticeFlag(String.valueOf(SysCode.STATUS.YES));
                this.dbQlCaseCorrectionMapper.updateByPrimaryKeySelective(caseCorrection);
                if(qlCaseCorrection.getMessageFlag().equals(String.valueOf(SysCode.STATUS.YES))){
                    //发送短信给申请人
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                    String dateOfIssue = DateUtil.getNowDate(df.format(caseCorrection.getDueDate()));
                    String messageContent= caseCorrection.getApplyUserName()+"您好，您的办件"+caseCorrection.getCaseNumber()+"需要来综合大厅进行补正，请于"+dateOfIssue+"前到综合大厅来进行办理。";
                    SendSmsUtil.sendSms(caseCorrection.getUserPhone(), messageContent);
                }
            }
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void saveOrUpdateRqbz(CaseMaterialVo vo) {
        if(vo!=null){
            if(vo.getMaterialCollect()!=null&&vo.getMaterialCollect().size()>0){
               List materialType= vo.getMaterialCollect();
                List materials=new ArrayList<QlCaseMaterial>();
                for(Object indType:materialType){
                    JSONObject obj= (JSONObject) JSONObject.toJSON(indType);
                    String caseMaterialOid= obj.get("materialOid").toString();
                    //根据材料编号查询材料信息
                    ApiResultSet<QlCaseMaterial> caseMaterialRes=qlCaseMaterialServiceFeginService.queryMaterialByCaseMaterialOid(caseMaterialOid);
                    if(caseMaterialRes.getData()!=null){
                        QlCaseMaterial caseMaterial=caseMaterialRes.getData();
                        caseMaterial.setCollectionType(obj.get("collectionType").toString());
                        if(!obj.get("collectionType").toString().equals("4")){
                            caseMaterial.setCollectionFlag(1);
                            caseMaterial.setCollectionDate(new Date());
                        }
                        //判断是否是证照收取
                        if(obj.get("collectionType").toString().equals("5")){
                            if(vo.getElemLicense()!=null&&vo.getElemLicense().size()>0){
                                for(Object elem:vo.getElemLicense()){
                                    JSONObject objElem= (JSONObject) JSONObject.toJSON(elem);
                                    if(objElem.get("materialOid").equals(caseMaterialOid)){
                                        caseMaterial.setElemLicenseOid(objElem.get("attaOid")==null?"":objElem.get("attaOid").toString());
                                        caseMaterial.setElemNumber(objElem.get("originName")==null?"":objElem.get("originName").toString());
                                        break;
                                    }
                                }
                            }
                        }
                        materials.add(caseMaterial);
                    }
                    //保存容缺后补信息记录
                    CaseRqhb rqhb=new CaseRqhb();
                    rqhb.setCaseOid(vo.getCaseOid());
                    rqhb.setCaseMaterialOid(caseMaterialOid);
                    rqhb.setRqhbOid(UUIDUtil.randomUUID());
                    rqhb.setModifyDate(new Date());
                    rqhb.setCreateDate(new Date());
                    rqhb.setCreateUser(CurrentLoginUserHolder.getCurrentLoginUser().getUserOid());
                    caseRqhbManager.saveOrUpdate(rqhb);
                }
               ApiResultSet dataReturn= qlCaseMaterialServiceFeginService.saveQlCaseMaterial(materials);
               if(dataReturn.getData() !=null){
                   if(vo.getMaterialAtta()!=null&&vo.getMaterialAtta().size()>0){
                       List materialAttas=new ArrayList<QlCaseMaterialAtta>();
                       List materialAtta= vo.getMaterialAtta();
                       for(Object attaObj:materialAtta){
                           JSONObject obj= (JSONObject) JSONObject.toJSON(attaObj);
                           String caseMaterialOid= obj.get("materialOid").toString();
                           String attaOid= obj.get("attaOid").toString();
                           QlCaseMaterialAtta atta =new QlCaseMaterialAtta();
                           atta.setAttaOid(attaOid);
                           atta.setCaseMaterialOid(caseMaterialOid);
                           materialAttas.add(atta);
                       }
                       qlCaseMaterialAttaServiceFeginService.saveQlCaseMaterialAttaList(materialAttas);
                   }
               }
            }
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void saveOrUpdateGzBqbz(QlCaseCorrection correction) {
        CurrentLoginUser loginUser = CurrentLoginUserHolder.getCurrentLoginUser();
        if(correction!=null){
            DbQlCaseCorrection dbQlCaseCorrection=null;
            ApiResultSet<QlCase> resultSet = qlCaseServiceFeginService.queryQlCaseByCaseOid(correction.getCaseOid());
            QlCase qlCase = resultSet.getData();
            //查询办件是否存在没有补正的信息
            DbQlCaseCorrectionExample dbQlCaseCorrectionExample = new DbQlCaseCorrectionExample();
            DbQlCaseCorrectionExample.Criteria criteria = dbQlCaseCorrectionExample.createCriteria();
            criteria.andCaseOidEqualTo(correction.getCaseOid());
            criteria.andCorrectionStatusEqualTo("0");
            List<DbQlCaseCorrection> caseCorrections=dbQlCaseCorrectionMapper.selectByExample(dbQlCaseCorrectionExample);
            if(caseCorrections!=null&&caseCorrections.size()>0){
                dbQlCaseCorrection=caseCorrections.get(0);
                dbQlCaseCorrection.setApplyProjectName(correction.getApplyProjectName());
                dbQlCaseCorrection.setApplyUserName(correction.getApplyUserName());
                dbQlCaseCorrection.setUserPhone(correction.getUserPhone());
                dbQlCaseCorrection.setDueDate(correction.getDueDate());
                if(qlCase !=null){
                    dbQlCaseCorrection.setOrganOid(qlCase.getOrganOid());
                }
                dbQlCaseCorrectionMapper.updateByPrimaryKeySelective(dbQlCaseCorrection);
            }else{
                dbQlCaseCorrection=new DbQlCaseCorrection();
                correction.setCorrectionReason("告知承诺补正!!!");
                correction.setCorrectionOid(UUIDUtil.randomUUID());
                correction.setCorrectionStatus("0");
                correction.setCreateDate(new Date());
                correction.setCreateUser(loginUser.getUserOid());
                correction.setNoticeFlag("0");
                BeanUtils.copyProperties(correction,dbQlCaseCorrection);
                if(qlCase !=null){
                    dbQlCaseCorrection.setOrganOid(qlCase.getOrganOid());
                }
                dbQlCaseCorrectionMapper.insertSelective(dbQlCaseCorrection);
                //保存补正办件材料信息
               ApiResultSet<List<QlCaseMaterial>> list=qlCaseMaterialServiceFeginService.queryQlCaseMaterialByCaseOid(correction.getCaseOid());
               if(list!=null&&list.getData()!=null&&list.getData().size()>0){
                   DbQlCorrectionMaterial correctionMaterial=null;
                   for (QlCaseMaterial material:list.getData()){
                       correctionMaterial=new DbQlCorrectionMaterial();
                       correctionMaterial.setCorrectionOid(dbQlCaseCorrection.getCorrectionOid());
                       correctionMaterial.setCaseMaterialOid(material.getCaseMaterialOid());
                       correctionMaterial.setCreateDate(new Date());
                       correctionMaterial.setCorrectionMaterialOid(UUIDUtil.randomUUID());
                       dbQlCorrectionMaterialMapper.insertSelective(correctionMaterial);
                   }
               }
            }
        }
    }

    @Transactional(rollbackFor = Exception.class)
   public Map<String,String> saveStopCorrection(Long id){
        if(id!=null){
           DbQlCaseCorrection qlCase= dbQlCaseCorrectionMapper.selectByPrimaryKey(id);
           if(qlCase!=null){
               qlCase.setCorrectionStatus("2");//补证过期未受理
              dbQlCaseCorrectionMapper.updateByPrimaryKeySelective(qlCase);
              //中止办件
               qlCaseServiceFeginService.updateQlCase(qlCase.getCaseOid(),5);
              //查询申请人证件号
             ApiResultSet<QlCaseApplay> applay=  qlCaseApplayServiceFeginService.queryQlCaseApplayByCaseOid(qlCase.getCaseOid());
             if(applay!=null && applay.getData()!=null){
                 Map map=new HashMap();
                 map.put("cardNumber",applay.getData().getCredentialNumber());
                 map.put("cardType",applay.getData().getCredentialType());
                 return map;
             }
           }
        }
        return null;
   }

    /**
     * 查询所有已经过期的补正
     * dongxl
     * @return
     */
    public List<QlCaseCorrection> getDueTimeCorrection() {

        DbQlCaseCorrectionExample dbQlCaseCorrectionExample = new DbQlCaseCorrectionExample();
        DbQlCaseCorrectionExample.Criteria criteria = dbQlCaseCorrectionExample.createCriteria();
        criteria.andCorrectionStatusEqualTo("0");
        criteria.andDueDateIsNotNull();
        criteria.andDueDateLessThanOrEqualTo(new Date());
        List<DbQlCaseCorrection> dbQlCaseCorrection=dbQlCaseCorrectionMapper.selectByExample(dbQlCaseCorrectionExample);
        return BeanUtils.copyListProperties(dbQlCaseCorrection, QlCaseCorrection::new);
    }

    /**
     * 手动办结的时候不予通过终止补正操作
     * @param caseOid
     */
    @Transactional(rollbackFor = Exception.class)
    public void saveBjCorrection(String caseOid){
        DbQlCaseCorrectionExample dbQlCaseCorrectionExample = new DbQlCaseCorrectionExample();
        DbQlCaseCorrectionExample.Criteria criteria = dbQlCaseCorrectionExample.createCriteria();
        criteria.andCorrectionStatusEqualTo("0");
        criteria.andCaseOidEqualTo(caseOid);
        List<DbQlCaseCorrection> dbQlCaseCorrection=dbQlCaseCorrectionMapper.selectByExample(dbQlCaseCorrectionExample);
        if(dbQlCaseCorrection!=null&&dbQlCaseCorrection.size()>0){
            for(DbQlCaseCorrection qlcase:dbQlCaseCorrection){
                if(qlcase!=null) {
                    qlcase.setCorrectionStatus("3");//作废
                    dbQlCaseCorrectionMapper.updateByPrimaryKeySelective(qlcase);
                }
            }
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public Map<String,String> updateCorrection(QlCaseCorrection qlCaseCorrection){
            if(qlCaseCorrection!=null){
                DbQlCaseCorrection dbQlCaseCorrection=new DbQlCaseCorrection();
                BeanUtils.copyProperties(qlCaseCorrection,dbQlCaseCorrection);
                dbQlCaseCorrectionMapper.updateByPrimaryKeySelective(dbQlCaseCorrection);
        }
        return null;
    }

    public List<QlCaseCorrection> bqbzCaseList(QlCaseCorrection qlCaseCorrection){
        /*DbQlCaseCorrectionExample dbQlCaseCorrectionExample = new DbQlCaseCorrectionExample();
        DbQlCaseCorrectionExample.Criteria criteria = dbQlCaseCorrectionExample.createCriteria();
        if(qlCaseCorrection!=null){
            if(StringUtils.isNotEmpty(qlCaseCorrection.getCorrectionStatus())){
                criteria.andCorrectionStatusEqualTo(qlCaseCorrection.getCorrectionStatus());
            }
            if(StringUtils.isNotEmpty(qlCaseCorrection.getServiceOids())){

            }
        }
        List<DbQlCaseCorrection> dbQlCaseCorrection=dbQlCaseCorrectionMapper.selectByExample(dbQlCaseCorrectionExample);
        List<QlCaseCorrection> list=dbQlCaseCorrection.stream().map(dbcorrect->{
            QlCaseCorrection caseBz=new QlCaseCorrection();
            BeanUtils.copyProperties(dbcorrect,caseBz);
            return caseBz;
        }).collect(Collectors.toList());*/
        Map<String,Object> map = new HashMap<String,Object>();
        if(qlCaseCorrection!=null){
            if(StringUtils.isNotEmpty(qlCaseCorrection.getCorrectionStatus())){
                map.put("correctionStatus",qlCaseCorrection.getCorrectionStatus());
            }
            if(StringUtils.isNotEmpty(qlCaseCorrection.getServiceOids())){
                map.put("serviceOids",qlCaseCorrection.getServiceOids());

            }
        }
        List<DbQlCaseCorrection> dbQlCaseCorrection=dbQlCaseCorrectionMapper.selectQlCaseCorrectionList(map);
        return BeanUtils.copyListProperties(dbQlCaseCorrection, QlCaseCorrection::new);
    }

    public PageResult<QlCaseCorrection> bqbzCasePage(QlCaseCorrection qlCaseCorrection, Integer pageNum, Integer pageSize){
        Map<String,Object> map = new HashMap<String,Object>();
        if(qlCaseCorrection!=null){
            if(StringUtils.isNotEmpty(qlCaseCorrection.getCorrectionStatus())){
                map.put("correctionStatus",qlCaseCorrection.getCorrectionStatus());
            }
            if(StringUtils.isNotEmpty(qlCaseCorrection.getServiceOids())){
                map.put("serviceOids",qlCaseCorrection.getServiceOids());

            }
        }
        //分页参数
        if (null == pageNum || pageNum <= 0) {
            pageNum = 0;
        }
        if (null == pageSize || pageSize <= 0) {
            pageSize = 10;
        }
        PageHelper.startPage(pageNum, pageSize);
        Page<DbQlCaseCorrection> dbDbQlCases = (Page<DbQlCaseCorrection>)dbQlCaseCorrectionMapper.selectQlCaseCorrectionList(map);
        PageResult<QlCaseCorrection> pageResult = new PageResult<>(dbDbQlCases.getPageNum(), dbDbQlCases.getPageSize(), dbDbQlCases.getTotal());
        List<QlCaseCorrection> qlCaseList = dbDbQlCases.stream().map(dbQlCase -> {
            QlCaseCorrection qlcase = new QlCaseCorrection();
            org.springframework.beans.BeanUtils.copyProperties(dbQlCase, qlcase);
            return qlcase;
        }).collect(Collectors.toList());
        pageResult.setData(qlCaseList);
        return pageResult;
    }

}
