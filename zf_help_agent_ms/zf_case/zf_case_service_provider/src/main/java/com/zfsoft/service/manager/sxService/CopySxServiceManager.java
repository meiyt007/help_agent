package com.zfsoft.service.manager.sxService;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.zfsoft.service.dbaccess.dao.sxService.*;
import com.zfsoft.service.dbaccess.dao.sxSituation.*;
import com.zfsoft.service.dbaccess.data.sxService.*;
import com.zfsoft.service.dbaccess.data.sxSituation.*;
import com.zfsoft.microservice.form.data.vo.FormPhysicalDataModelVo;
import com.zfsoft.platform.common.security.data.CurrentLoginUser;
import com.zfsoft.platform.common.security.data.CurrentLoginUserHolder;
import com.zfsoft.service.common.SxptBaseStaticParameter;
import com.zfsoft.service.dbaccess.dao.sxService.DbSxServicePrecheckMapper;
import com.zfsoft.service.dbaccess.data.sxService.DbSxServicePrecheck;
import com.zfsoft.service.dbaccess.data.sxService.DbSxServicePrecheckExample;
import com.zfsoft.service.feign.FormTableFeignService;
import com.zfsoft.single.data.yxpz.vo.CopyVo;
import com.zfsoft.service.sxService.data.SxService;
import com.zfsoft.single.service.yxpz.FormFieldRelConfigService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName CopySxServiceManager
 * @Description: 复制事项管理类
 * @Author chenyq
 * @Date 2022/03/22
 **/
@Service
@Slf4j
public class CopySxServiceManager {
    /**
     * 事项mapper
     */
    @Resource
    private DbSxServiceMapper dbSxServiceMapper;
    @Resource
    private DbSxServiceExtendMapper dbSxServiceExtendMapper;
    @Resource
    private DbSxAcceptConditionMapper dbSxAcceptConditionMapper;
    @Resource
    private DbSxServiceQuestionMapper dbSxServiceQuestionMapper;
    @Resource
    private DbSxServiceChargeMapper dbSxServiceChargeMapper;
    @Resource
    private DbSxServicePrecheckMapper dbSxServicePrecheckMapper;
    @Resource
    private DbSxServiceLinkMapper dbSxServiceLinkMapper;
    @Resource
    private DbSxServiceMaterialMapper dbSxServiceMaterialMapper;
    @Resource
    private DbSxServiceLocationMapper dbSxServiceLocationMapper;
    @Resource
    private DbRefinedMaterialMapper dbRefinedMaterialMapper;
    @Resource
    private DbServiceOptionRelMapper dbServiceOptionRelMapper;
    @Resource
    private DbSxServiceOptionValMapper dbSxServiceOptionValMapper;
    @Resource
    private DbSxServiceOptionTitleMapper dbSxServiceOptionTitleMapper;
    @Resource
    private DbSxServiceSituationMapper dbSxServiceSituationMapper;
    @Resource
    private DbSxServiceSituationOptionMapper dbSxServiceSituationOptionMapper;
    @Resource
    private DbSxFillFieldMapper dbSxFillFieldMapper;
    @Resource
    private DbSxFillLabelMapper dbSxFillLabelMapper;
    @Resource
    private DbSxServiceMateOptRelMapper dbSxServiceMateOptRelMapper;
    @Resource
    private FormFieldRelConfigService formFieldRelConfigFeignService;
    @Resource
    private DbSxFormInfoMapper dbSxFormInfoMapper ;
    @Resource
    private FormTableFeignService formTableFeignService;
    @Resource
    private DbReviewPointsMapper dbReviewPointsMapper;


    //包括基本信息（材料信息、办事地点、常见问题、受理条件、办理环节）
    // 情形信息（清醒配置、选项配置、选项材料配置）
    // 表单信息（字段标签配置、字段配置）
    @Transactional
    public void doCopy(SxService sxServiceNew) {
        CurrentLoginUser sysUser= CurrentLoginUserHolder.getCurrentLoginUser();
        String serviceOid = IdUtil.simpleUUID();
        Map map = new HashMap(16);
        DbSxServiceWithBLOBs sxServiceOld = dbSxServiceMapper.getSxServiceByOid(sxServiceNew.getServiceOid());//此时还是老的serviceOid
        sxServiceNew.setServiceOid(serviceOid);//更新为新的serviceOid
        //基本信息
        String extendOidOld = baseInfo(sxServiceOld,sxServiceNew,sysUser);
        //材料信息
        map = materialInfo(sxServiceOld.getServiceOid(), serviceOid,sysUser,map);
        //办事地点
        locationInfo(extendOidOld,serviceOid);
        //常见问题
        questionInfo(sxServiceOld.getServiceOid(), serviceOid,sysUser);
        //受理条件
        acceptInfo(sxServiceOld.getServiceOid(), serviceOid,sysUser);
        //收费设置
        chargeInfo(sxServiceOld.getServiceOid(), serviceOid,sysUser);
        //办理环节
        linkInfo(sxServiceOld.getServiceOid(), serviceOid,sysUser);
        //前置核验
        precheckInfo(sxServiceOld.getServiceOid(), serviceOid);
        //情形信息
        situationInfo(sxServiceOld.getServiceOid(), serviceOid,map);
        //表单复制
        map = formInfo(sxServiceOld.getServiceOid(), serviceOid,map);
        //表单详情复制
        sxFormInfo(sxServiceOld.getServiceOid(), serviceOid,map);

    }
    //基本信息
    private String baseInfo(DbSxService sxServiceOld,SxService sxServiceNew,CurrentLoginUser sysUser){
        //基本信息
        DbSxServiceWithBLOBs dbSxServiceCopy = new DbSxServiceWithBLOBs();
        BeanUtils.copyProperties(sxServiceOld,dbSxServiceCopy);
        dbSxServiceCopy.setServiceOid(sxServiceNew.getServiceOid());
        dbSxServiceCopy.setServiceName(sxServiceNew.getServiceName());
        dbSxServiceCopy.setImplementCode(sxServiceNew.getImplementCode());

        dbSxServiceCopy.setCreateDate(new Date());
        dbSxServiceCopy.setModifyDate(new Date());
        dbSxServiceCopy.setCreateUser(sysUser.getUserOid());
        dbSxServiceCopy.setDistrictOid(sysUser.getDistrictOid());
        dbSxServiceCopy.setOrganOid(sysUser.getOrganOid());
        dbSxServiceCopy.setId(null);
        dbSxServiceMapper.insertSelective(dbSxServiceCopy);
        //扩展信息
        DbSxServiceExtendWithBLOBs sxServiceExtend
                = dbSxServiceExtendMapper.getSxServiceExtendByServiceOid(sxServiceOld.getServiceOid());
        DbSxServiceExtendWithBLOBs dbSxServiceExtendWithBLOBsCopy = new DbSxServiceExtendWithBLOBs();
        BeanUtils.copyProperties(sxServiceExtend,dbSxServiceExtendWithBLOBsCopy);
        dbSxServiceExtendWithBLOBsCopy.setServiceOid(sxServiceNew.getServiceOid());
        dbSxServiceExtendWithBLOBsCopy.setId(null);
        dbSxServiceExtendWithBLOBsCopy.setExtendOid(sxServiceNew.getServiceOid());
        dbSxServiceExtendMapper.insert(dbSxServiceExtendWithBLOBsCopy);
        //返回老的扩展信息主键
        return sxServiceExtend.getExtendOid();
    }
    //材料信息
    private Map materialInfo(String serviceOidOld,String serviceOidNew,CurrentLoginUser sysUser,Map map){
        DbSxServiceMaterialExample dbSxServiceMaterialExample = new DbSxServiceMaterialExample();
        DbSxServiceMaterialExample.Criteria criteria = dbSxServiceMaterialExample.createCriteria();
        if (StrUtil.isNotEmpty(serviceOidOld)) {
            criteria.andServiceOidEqualTo(serviceOidOld);
        }
        criteria.andDelFlagEqualTo((short) 0);
        List<DbSxServiceMaterial> dbSxServiceMaterials = dbSxServiceMaterialMapper.selectByExample(dbSxServiceMaterialExample);
        dbSxServiceMaterials.forEach(dbSxServiceMaterial -> {
            DbSxServiceMaterialWithBLOBs dbSxServiceMaterialCopy = new DbSxServiceMaterialWithBLOBs();
            BeanUtils.copyProperties(dbSxServiceMaterial,dbSxServiceMaterialCopy);
            dbSxServiceMaterialCopy.setServiceOid(serviceOidNew);
            dbSxServiceMaterialCopy.setCreateUser(sysUser.getUserOid());
            dbSxServiceMaterialCopy.setCreateDate(new Date());
            dbSxServiceMaterialCopy.setModifyDate(new Date());
            dbSxServiceMaterialCopy.setId(null);
            String oid = IdUtil.simpleUUID();
            dbSxServiceMaterialCopy.setMaterialOid(oid);
            dbSxServiceMaterialMapper.insert(dbSxServiceMaterialCopy);
            map.put(dbSxServiceMaterial.getMaterialOid(),dbSxServiceMaterialCopy.getMaterialOid());
            //有颗粒化材料
            DbRefinedMaterialExample dbRefinedMaterialExample=new DbRefinedMaterialExample();
            DbRefinedMaterialExample.Criteria criteriaRe=dbRefinedMaterialExample.createCriteria();
            criteriaRe.andMaterialOidEqualTo(dbSxServiceMaterial.getMaterialOid());
            criteriaRe.andDeleteStatusEqualTo((short) 0);
            dbRefinedMaterialExample.setOrderByClause(" SERIAL_NUMBER");
            List<DbRefinedMaterial> dbRefinedMaterials = dbRefinedMaterialMapper.selectByExample(dbRefinedMaterialExample);
            dbRefinedMaterials.forEach(dbRefinedMaterial -> {
                DbRefinedMaterial dbRefinedMaterialCopy = new DbRefinedMaterial();
                BeanUtils.copyProperties(dbRefinedMaterial,dbRefinedMaterialCopy);
                dbRefinedMaterialCopy.setOid(IdUtil.simpleUUID());
                dbRefinedMaterialCopy.setModifyDate(new Date());
                dbRefinedMaterialCopy.setMaterialOid(oid);
                dbRefinedMaterialCopy.setId(null);
                dbRefinedMaterialCopy.setServiceOid(serviceOidNew);
                dbRefinedMaterialCopy.setCreateDate(new Date());
                map.put(dbRefinedMaterial.getOid(),dbRefinedMaterialCopy.getOid());
                dbRefinedMaterialMapper.insertSelective(dbRefinedMaterialCopy);
                //复制审查要点
                DbReviewPointsExample dbReviewPointsExample=new DbReviewPointsExample();
                DbReviewPointsExample.Criteria criteriaRpe=dbReviewPointsExample.createCriteria();
                criteriaRpe.andRefinedMaterialOidEqualTo(dbRefinedMaterial.getOid());
                criteriaRpe.andDeleteStatusEqualTo((short) 0);
                List<DbReviewPoints> dbReviewPointss=dbReviewPointsMapper.selectByExample(dbReviewPointsExample);
                dbReviewPointss.forEach(dbReviewPoints -> {
                    DbReviewPoints reviewPointsCopy = new DbReviewPoints();
                    BeanUtils.copyProperties(dbReviewPoints, reviewPointsCopy);
                    reviewPointsCopy.setDeleteStatus((short)0);
                    reviewPointsCopy.setOid(IdUtil.simpleUUID());
                    reviewPointsCopy.setId(null);
                    reviewPointsCopy.setCreateDate(new Date());
                    reviewPointsCopy.setModifyDate(new Date());
                    reviewPointsCopy.setRefinedMaterialOid(dbRefinedMaterialCopy.getOid());
                    dbReviewPointsMapper.insert(reviewPointsCopy);
                });
            });
        });
        return map;
    }
    //办事地点
    private void locationInfo(String extendOidOld,String extendOidNew){
        DbSxServiceLocationExample dbSxServiceLocationExample = new DbSxServiceLocationExample();
        DbSxServiceLocationExample.Criteria criteria = dbSxServiceLocationExample.createCriteria();
        criteria.andExtendOidEqualTo(extendOidOld);
        criteria.andDelFlagEqualTo(Short.valueOf("0"));
        List<DbSxServiceLocation> dbSxServiceLocations
                = dbSxServiceLocationMapper.selectByExampleWithBLOBs(dbSxServiceLocationExample);
        dbSxServiceLocations.forEach(dbSxServiceLocation -> {
            DbSxServiceLocation dbSxServiceLocationCopy = new DbSxServiceLocation();
            BeanUtils.copyProperties(dbSxServiceLocation,dbSxServiceLocationCopy);
            dbSxServiceLocationCopy.setExtendOid(extendOidNew);
            dbSxServiceLocationCopy.setModifyDate(new Date());
            dbSxServiceLocationCopy.setId(null);
            dbSxServiceLocationCopy.setServiceLocationOid(IdUtil.simpleUUID());
            dbSxServiceLocationMapper.insertSelective(dbSxServiceLocationCopy);
        });
    }
    //常见问题
    private void questionInfo(String serviceOidOld,String serviceOidNew,CurrentLoginUser sysUser){

        DbSxServiceQuestionExample sxServiceQuestionExample = new DbSxServiceQuestionExample();
        DbSxServiceQuestionExample.Criteria criteria = sxServiceQuestionExample.createCriteria();
        criteria.andDelFlagEqualTo((short)0);
        criteria.andServiceOidEqualTo(serviceOidOld);
        List<DbSxServiceQuestionWithBLOBs> dbSxServiceQuestions
                = dbSxServiceQuestionMapper.selectByExampleWithBLOBs(sxServiceQuestionExample);
        dbSxServiceQuestions.forEach(dbSxServiceQuestion -> {
            DbSxServiceQuestionWithBLOBs dbSxServiceQuestionCopy = new DbSxServiceQuestionWithBLOBs();
            BeanUtils.copyProperties(dbSxServiceQuestion,dbSxServiceQuestionCopy);
            dbSxServiceQuestionCopy.setServiceOid(serviceOidNew);
            dbSxServiceQuestionCopy.setId(null);
            dbSxServiceQuestionCopy.setQuestionOid(IdUtil.simpleUUID());
            dbSxServiceQuestionCopy.setCreateUser(sysUser.getUserOid());
            dbSxServiceQuestionCopy.setCreateDate(new Date());
            dbSxServiceQuestionCopy.setModifyDate(new Date());
            dbSxServiceQuestionMapper.insert(dbSxServiceQuestionCopy);
        });
    }
    //受理条件
    private void acceptInfo(String serviceOidOld,String serviceOidNew,CurrentLoginUser sysUser){
        DbSxAcceptConditionExample sxAcceptConditionExample = new DbSxAcceptConditionExample();
        DbSxAcceptConditionExample.Criteria criteria = sxAcceptConditionExample.createCriteria();
        criteria.andDelFlagEqualTo((short)0);
        criteria.andServiceOidEqualTo(serviceOidOld);
        List<DbSxAcceptCondition> dbSxAcceptConditions
                = dbSxAcceptConditionMapper.selectByExample(sxAcceptConditionExample);
        dbSxAcceptConditions.forEach(dbSxAcceptCondition -> {
            DbSxAcceptCondition dbSxAcceptConditionCopy = new DbSxAcceptCondition();
            BeanUtils.copyProperties(dbSxAcceptCondition,dbSxAcceptConditionCopy);
            dbSxAcceptConditionCopy.setServiceOid(serviceOidNew);
            dbSxAcceptConditionCopy.setId(null);
            dbSxAcceptConditionCopy.setConditionOid(IdUtil.simpleUUID());
            dbSxAcceptConditionCopy.setCreateUser(sysUser.getUserOid());
            dbSxAcceptConditionCopy.setCreateDate(new Date());
            dbSxAcceptConditionCopy.setModifyDate(new Date());
            dbSxAcceptConditionMapper.insertSelective(dbSxAcceptConditionCopy);
        });
    }
    //收费设置
    private void chargeInfo(String serviceOidOld,String serviceOidNew,CurrentLoginUser sysUser){
        DbSxServiceChargeExample dbSxServiceChargeExample = new DbSxServiceChargeExample();
        DbSxServiceChargeExample.Criteria criteria = dbSxServiceChargeExample.createCriteria();
        criteria.andServiceOidEqualTo(serviceOidOld);
        criteria.andDelFlagEqualTo(SxptBaseStaticParameter.ZERO);
        List<DbSxServiceChargeWithBLOBs> dbSxServiceCharges = dbSxServiceChargeMapper.selectByExampleWithBLOBs(dbSxServiceChargeExample);
        dbSxServiceCharges.forEach(db -> {
            DbSxServiceChargeWithBLOBs dbSxServiceChargeWithBLOBsCopy = new DbSxServiceChargeWithBLOBs();
            BeanUtils.copyProperties(db,dbSxServiceChargeWithBLOBsCopy);
            dbSxServiceChargeWithBLOBsCopy.setServiceOid(serviceOidNew);
            dbSxServiceChargeWithBLOBsCopy.setId(null);
            dbSxServiceChargeWithBLOBsCopy.setChargeOid(IdUtil.simpleUUID());
            dbSxServiceChargeWithBLOBsCopy.setCreateUser(sysUser.getUserOid());
            dbSxServiceChargeWithBLOBsCopy.setCreateDate(new Date());
            dbSxServiceChargeWithBLOBsCopy.setModifyDate(new Date());
            dbSxServiceChargeMapper.insert(dbSxServiceChargeWithBLOBsCopy);
        });
    }
    //办理环节
    private void linkInfo(String serviceOidOld,String serviceOidNew,CurrentLoginUser sysUser){
        DbSxServiceLinkExample sxAcceptConditionExample = new DbSxServiceLinkExample();
        DbSxServiceLinkExample.Criteria criteria = sxAcceptConditionExample.createCriteria();
        criteria.andDelFlagEqualTo((short)0);
        criteria.andServiceOidEqualTo(serviceOidOld);
        List<DbSxServiceLinkWithBLOBs> dbSxServiceLinks
                = dbSxServiceLinkMapper.selectByExampleWithBLOBs(sxAcceptConditionExample);
        dbSxServiceLinks.forEach(db -> {
            DbSxServiceLinkWithBLOBs dbSxServiceLinkWithBLOBsCopy = new DbSxServiceLinkWithBLOBs();
            BeanUtils.copyProperties(db,dbSxServiceLinkWithBLOBsCopy);
            dbSxServiceLinkWithBLOBsCopy.setServiceOid(serviceOidNew);
            dbSxServiceLinkWithBLOBsCopy.setId(null);
            dbSxServiceLinkWithBLOBsCopy.setLinkOid(IdUtil.simpleUUID());
            dbSxServiceLinkWithBLOBsCopy.setCreateUser(sysUser.getUserOid());
            dbSxServiceLinkWithBLOBsCopy.setCreateDate(new Date());
            dbSxServiceLinkWithBLOBsCopy.setModifyDate(new Date());
            dbSxServiceLinkMapper.insert(dbSxServiceLinkWithBLOBsCopy);
        });
    }
    //前置核验
    private void precheckInfo(String serviceOidOld,String serviceOidNew){
        DbSxServicePrecheckExample dbSxServicePrecheckExample = new DbSxServicePrecheckExample();
        DbSxServicePrecheckExample.Criteria criteria = dbSxServicePrecheckExample.createCriteria();
        criteria.andServiceOidEqualTo(serviceOidOld);
        criteria.andDelFlagEqualTo(0);
        List<DbSxServicePrecheck> dbSxServicePrechecks = dbSxServicePrecheckMapper.selectByExample(dbSxServicePrecheckExample);
        dbSxServicePrechecks.forEach(dbSxServicePrecheck -> {
            DbSxServicePrecheck dbSxServicePrecheckCopy = new DbSxServicePrecheck();
            BeanUtils.copyProperties(dbSxServicePrecheck,dbSxServicePrecheckCopy);
            dbSxServicePrecheckCopy.setId(null);
            dbSxServicePrecheckCopy.setServiceOid(serviceOidNew);
            dbSxServicePrecheckCopy.setCreateDate(new Date());
            dbSxServicePrecheckCopy.setDelFlag(0);
            dbSxServicePrecheckCopy.setPrecheckOid(IdUtil.simpleUUID());
            dbSxServicePrecheckMapper.insertSelective(dbSxServicePrecheckCopy);
        });
    }
    //情形信息
    private void situationInfo(String serviceOidOld,String serviceOidNew,Map map){
        //标题列表
        DbSxServiceOptionTitleExample dbSxServiceOptionTitleExample = new DbSxServiceOptionTitleExample();
        DbSxServiceOptionTitleExample.Criteria criteriaTitle = dbSxServiceOptionTitleExample.createCriteria();
        criteriaTitle.andServiceOidEqualTo(serviceOidOld);
        criteriaTitle.andDeleteStatusEqualTo((short)0);
        dbSxServiceOptionTitleExample.setOrderByClause("  sort");
        List<DbSxServiceOptionTitle> dbSxServiceOptionTitle = dbSxServiceOptionTitleMapper.selectByExample(dbSxServiceOptionTitleExample);
        dbSxServiceOptionTitle.forEach(db -> {
            //复制标题
            DbSxServiceOptionTitle dbSxServiceOptionTitleCopy = new DbSxServiceOptionTitle();
            BeanUtils.copyProperties(db,dbSxServiceOptionTitleCopy);
            dbSxServiceOptionTitleCopy.setServiceOid(serviceOidNew);
            dbSxServiceOptionTitleCopy.setId(null);
            String titleOidNew = IdUtil.simpleUUID();
            dbSxServiceOptionTitleCopy.setOid(titleOidNew);
            dbSxServiceOptionTitleCopy.setCreateDate(new Date());
            dbSxServiceOptionTitleCopy.setModifyDate(new Date());
            dbSxServiceOptionTitleMapper.insertSelective(dbSxServiceOptionTitleCopy);
            //原标题oid和现标题oid
            map.put(db.getOid(),titleOidNew);
            //查询对应的选项值
            DbSxServiceOptionValExample dbSxServiceOptionValExample = new DbSxServiceOptionValExample();
            DbSxServiceOptionValExample.Criteria criteria = dbSxServiceOptionValExample.createCriteria();
            criteria.andTitleOidEqualTo(db.getOid());
            criteria.andDeleteStatusEqualTo("0");
            dbSxServiceOptionValExample.setOrderByClause(" sort ");
            List<DbSxServiceOptionVal> dbSxServiceOptionVals = dbSxServiceOptionValMapper.selectByExample(dbSxServiceOptionValExample);
            dbSxServiceOptionVals.forEach(dbVal -> {
                //复制选项值
                DbSxServiceOptionVal dbSxServiceOptionValCopy = new DbSxServiceOptionVal();
                BeanUtils.copyProperties(dbVal,dbSxServiceOptionValCopy);
                dbSxServiceOptionValCopy.setTitleOid(titleOidNew);
                String valOid = IdUtil.simpleUUID();
                dbSxServiceOptionValCopy.setOid(valOid);
                dbSxServiceOptionValCopy.setCreateDate(new Date());
                dbSxServiceOptionValCopy.setModifyDate(new Date());
                dbSxServiceOptionValMapper.insertSelective(dbSxServiceOptionValCopy);
                //原选项值oid和现选项值oid
                map.put(dbVal.getOid(),valOid);
                //复制情形选项与材料之间的关系
                List<DbSxServiceMateOptRel> dbSxServiceMateOptRels
                        = dbSxServiceMateOptRelMapper.selectMateOptRelsByOptionValOid(dbVal.getOid());
                dbSxServiceMateOptRels.forEach(dbSxServiceMateOptRel -> {
                    DbSxServiceMateOptRel sxServiceMateOptRel = new DbSxServiceMateOptRel();
                    BeanUtils.copyProperties(dbSxServiceMateOptRel,sxServiceMateOptRel);
                    if (StringUtils.isNotEmpty(dbSxServiceMateOptRel.getSxMaterialOid())) {
                        String sxMaterialOid = dbSxServiceMateOptRel.getSxMaterialOid();//事项材料主键
                        sxServiceMateOptRel.setSxMaterialOid(map.get(sxMaterialOid).toString());
                    }
                    if (StringUtils.isNotEmpty(dbSxServiceMateOptRel.getMaterialOid())) {
                        String materialOid = dbSxServiceMateOptRel.getMaterialOid();//事项材料主键
                        sxServiceMateOptRel.setMaterialOid(map.get(materialOid).toString());
                    }
                    sxServiceMateOptRel.setOid(IdUtil.simpleUUID());
                    sxServiceMateOptRel.setCreateDate(new Date());
                    sxServiceMateOptRel.setModifyDate(new Date());
                    sxServiceMateOptRel.setOptionOid(valOid);
                    dbSxServiceMateOptRelMapper.insertSelective(sxServiceMateOptRel);
                });
            });
        });

        //情形查询
        DbSxServiceSituationExample dbSxServiceSituationExample = new DbSxServiceSituationExample();
        DbSxServiceSituationExample.Criteria criteria = dbSxServiceSituationExample.createCriteria();
        criteria.andServiceOidEqualTo(serviceOidOld);
        criteria.andDeleteStatusEqualTo((short)0);
        dbSxServiceSituationExample.setOrderByClause(" sort asc");
        List<DbSxServiceSituation> dbSxServiceSituations
                = dbSxServiceSituationMapper.selectByExample(dbSxServiceSituationExample);
        dbSxServiceSituations.forEach(db -> {
            //复制情形
            DbSxServiceSituation dbSxServiceSituationCopy = new DbSxServiceSituation();
            BeanUtils.copyProperties(db,dbSxServiceSituationCopy);
            dbSxServiceSituationCopy.setServiceOid(serviceOidNew);
            dbSxServiceSituationCopy.setId(null);
            String situOidNew = IdUtil.simpleUUID();
            dbSxServiceSituationCopy.setOid(situOidNew);
            dbSxServiceSituationCopy.setCreateDate(new Date());
            dbSxServiceSituationCopy.setModifyDate(new Date());
            dbSxServiceSituationMapper.insertSelective(dbSxServiceSituationCopy);
            //查询情形与选项关系
            DbSxServiceSituationOptionExample dbSxServiceSituationOptionExample = new DbSxServiceSituationOptionExample();
            DbSxServiceSituationOptionExample.Criteria criteriaSituOp = dbSxServiceSituationOptionExample.createCriteria();
            criteriaSituOp.andSituationOidEqualTo(db.getOid());
            criteriaSituOp.andDeleteStatusEqualTo((short)0);
            List<DbSxServiceSituationOption> dbSxServiceSituationOptions
                    = dbSxServiceSituationOptionMapper.selectByExample(dbSxServiceSituationOptionExample);
            dbSxServiceSituationOptions.forEach(dbSituationOption -> {
                //复制情形与选项关系
                DbSxServiceSituationOption dbSituationOptionCopy = new DbSxServiceSituationOption();
                BeanUtils.copyProperties(dbSituationOption,dbSituationOptionCopy);
                dbSituationOptionCopy.setSituationOid(situOidNew);
                dbSituationOptionCopy.setId(null);
                dbSituationOptionCopy.setOptionOid(map.get(dbSituationOption.getOptionOid()).toString());
                dbSituationOptionCopy.setCreateDate(new Date());
                dbSituationOptionCopy.setModifyDate(new Date());
                dbSxServiceSituationOptionMapper.insertSelective(dbSituationOptionCopy);
            });
        });

        //标题与选项关系
        DbServiceOptionRelExample dbServiceOptionRelExample = new DbServiceOptionRelExample();
        DbServiceOptionRelExample.Criteria criteriaOpRel = dbServiceOptionRelExample.createCriteria();
        criteriaOpRel.andServiceOidEqualTo(serviceOidOld);
        criteriaOpRel.andDeleteStatusEqualTo((short)0);
        List<DbServiceOptionRelWithBLOBs> dbServiceOptionRels
                = dbServiceOptionRelMapper.selectByExampleWithBLOBs(dbServiceOptionRelExample);
        dbServiceOptionRels.forEach(dbRels -> {
            //复制标题与选项关系
            DbServiceOptionRelWithBLOBs dbServiceOptionRelCopy = new DbServiceOptionRelWithBLOBs();
            BeanUtils.copyProperties(dbRels,dbServiceOptionRelCopy);
            dbServiceOptionRelCopy.setServiceOid(serviceOidNew);
            dbServiceOptionRelCopy.setId(null);
            dbServiceOptionRelCopy.setOid(IdUtil.simpleUUID());
            dbServiceOptionRelCopy.setModifyDate(new Date());
            String[] titleSplit = dbRels.getTitleOids().split(",");
            StringBuffer titleOids = new StringBuffer();
            //获取原标题oid和现标题oid，整理成现标题oid字符串
            for(int i=0;i<titleSplit.length;i++){
                titleOids.append(map.get(titleSplit[i])).append(",");
            }
            dbServiceOptionRelCopy.setTitleOids(titleOids.toString());
            String[] valueSplit = dbRels.getValueOids().split(",");
            StringBuffer valueOids = new StringBuffer();
            //获取原选项值oid和现选项值oid，整理成现选项值oid字符串
            for(int i=0;i<valueSplit.length;i++){
                valueOids.append(map.get(valueSplit[i])).append(",");
            }
            dbServiceOptionRelCopy.setValueOids(valueOids.toString());
            dbServiceOptionRelMapper.insert(dbServiceOptionRelCopy);
        });
    }
    //表单复制
    private Map formInfo(String serviceOidOld,String serviceOidNew,Map map){
        Map serviceMap = new HashMap(16);
        serviceMap.put("serviceOidOld",serviceOidOld);
        serviceMap.put("serviceOidNew",serviceOidNew);
        serviceMap.putAll(map);

        //查询标签，并拷贝
        DbSxFillLabelExample example = new DbSxFillLabelExample();
        DbSxFillLabelExample.Criteria criteria = example.createCriteria();
        criteria.andServiceOidEqualTo(serviceOidOld);
        criteria.andDelFlagEqualTo(0);
        List<DbSxFillLabel> dbSxFillLabels = dbSxFillLabelMapper.selectByExample(example);
        dbSxFillLabels.forEach(dbSxFillLabel -> {
            DbSxFillLabel dbSxFillLabelCopy = new DbSxFillLabel();
            BeanUtils.copyProperties(dbSxFillLabel,dbSxFillLabelCopy);
            dbSxFillLabelCopy.setServiceOid(serviceOidNew);
            dbSxFillLabelCopy.setId(null);
            String lableOid = IdUtil.simpleUUID();
            dbSxFillLabelCopy.setLabelOid(lableOid);
            dbSxFillLabelCopy.setModifyDate(new Date());
            dbSxFillLabelCopy.setCreateDate(new Date());
            dbSxFillLabelMapper.insertSelective(dbSxFillLabelCopy);
            //标签值复制
            map.put(dbSxFillLabel.getLabelOid(),dbSxFillLabelCopy.getLabelOid());
            serviceMap.put(dbSxFillLabel.getLabelOid(),dbSxFillLabelCopy.getLabelOid());
            //查询字段，并拷贝
            DbSxFillFieldExample dbExample = new DbSxFillFieldExample();
            DbSxFillFieldExample.Criteria criteriaFile = dbExample.createCriteria();
            criteriaFile.andLabelOidEqualTo(dbSxFillLabel.getLabelOid());
            criteriaFile.andServiceOidEqualTo(serviceOidOld);
            criteriaFile.andDelFlagEqualTo(0);
            List<DbSxFillField> dbSxFillFields = dbSxFillFieldMapper.selectByExample(dbExample);
            dbSxFillFields.forEach(dbSxFillField -> {
                DbSxFillField dbSxFillFieldCopy = new DbSxFillField();
                BeanUtils.copyProperties(dbSxFillField,dbSxFillFieldCopy);
                dbSxFillFieldCopy.setServiceOid(serviceOidNew);
                dbSxFillFieldCopy.setId(null);
                dbSxFillFieldCopy.setLabelOid(lableOid);
                dbSxFillFieldCopy.setFieldOid(IdUtil.simpleUUID());
                dbSxFillFieldCopy.setModifyDate(new Date());
                dbSxFillFieldCopy.setCreateDate(new Date());
                dbSxFillFieldMapper.insertSelective(dbSxFillFieldCopy);
                map.put(dbSxFillField.getFieldOid(),dbSxFillFieldCopy.getFieldOid());
                serviceMap.put(dbSxFillField.getFieldOid(),dbSxFillFieldCopy.getFieldOid());
            });
        });
        //字段值填充配置
        CopyVo copyVo = new CopyVo();
        copyVo.setMap(serviceMap);
        formFieldRelConfigFeignService.copyFormFieldRelConfig(copyVo);
        return map;
    }
    //表单详情复制
    private void sxFormInfo(String serviceOidOld,String serviceOidNew,Map map){
        DbSxFormInfoExample dbSxFormInfoExample = new DbSxFormInfoExample();
        DbSxFormInfoExample.Criteria criteria = dbSxFormInfoExample.createCriteria();
        if (StrUtil.isNotEmpty(serviceOidOld)) {
            criteria.andServiceOidEqualTo(serviceOidOld);
        }
        criteria.andIsAbleEqualTo(1);
        criteria.andDelFlagEqualTo(0);
        List<DbSxFormInfo> dbSxFormInfos = dbSxFormInfoMapper.selectByExample(dbSxFormInfoExample);
        dbSxFormInfos.forEach(dbSxFormInfo -> {
            DbSxFormInfo dbSxFormInfoCopy = new DbSxFormInfo();
            BeanUtils.copyProperties(dbSxFormInfo,dbSxFormInfoCopy);
            dbSxFormInfoCopy.setServiceOid(serviceOidNew);
            dbSxFormInfoCopy.setModifyDate(new Date());
            dbSxFormInfoCopy.setCreateDate(new Date());
            dbSxFormInfoCopy.setId(null);
            String oid = IdUtil.simpleUUID();
            dbSxFormInfoCopy.setFormOid(oid);
            /**
             * 以下字段是由表单接口返回提供
             */
            FormPhysicalDataModelVo formObj = formTableFeignService.copyInFormPhysicalDataModel (dbSxFormInfo.getFormCode(),dbSxFormInfo.getRelationObjectBusinessId()).getData();
                    dbSxFormInfoCopy.setDesignOid(formObj.getDesignOid());
            dbSxFormInfoCopy.setFormCode(formObj.getFormCode());
            dbSxFormInfoCopy.setFormMainOid(formObj.getFormMainOid());
            //此处数据需要通过map来重新整合
            if (StringUtils.isNotEmpty(formObj.getRelationObjectBusinessId())) {
                StringBuffer newRelation = new StringBuffer();
                String[] oneArray = formObj.getRelationObjectBusinessId().split(",");
                for (String s : oneArray) {
                    String[] twoArray = s.split(":");
                    String s1 = twoArray[0];
                    if(null==map.get(s1)){
                        newRelation.append(s1).append(":").append(twoArray[1]).append(",");
                    }else{
                        String newId =  map.get(s1)+":"+twoArray[1];
                        newRelation.append(newId).append(",");
                    }
                }
                int i = newRelation.toString().lastIndexOf(",");
                if(i>0){
                    String relation = newRelation.substring(0,newRelation.length()-1);
                    dbSxFormInfoCopy.setRelationObjectBusinessId(relation);
                }else{
                    dbSxFormInfoCopy.setRelationObjectBusinessId(newRelation.toString());
                }
            }
            dbSxFormInfoMapper.insert(dbSxFormInfoCopy);
        });
    }

    public Map comboRefinedInfo(String materialOidOld,String materialOidNew,String comboDirectoryOid){
        Map map = new HashMap(16);
        //有颗粒化材料
        DbRefinedMaterialExample dbRefinedMaterialExample=new DbRefinedMaterialExample();
        DbRefinedMaterialExample.Criteria criteriaRe=dbRefinedMaterialExample.createCriteria();
        criteriaRe.andMaterialOidEqualTo(materialOidOld);
        criteriaRe.andDeleteStatusEqualTo((short) 0);
        List<DbRefinedMaterial> dbRefinedMaterials = dbRefinedMaterialMapper.selectByExample(dbRefinedMaterialExample);
        dbRefinedMaterials.forEach(dbRefinedMaterial -> {
            DbRefinedMaterial dbRefinedMaterialCopy = new DbRefinedMaterial();
            BeanUtils.copyProperties(dbRefinedMaterial,dbRefinedMaterialCopy);
            String oid = IdUtil.simpleUUID();
            dbRefinedMaterialCopy.setOid(oid);
            dbRefinedMaterialCopy.setComboDirectoryOid(comboDirectoryOid);
            dbRefinedMaterialCopy.setMaterialOid(materialOidNew);
            dbRefinedMaterialCopy.setId(null);
            dbRefinedMaterialCopy.setCreateDate(new Date());
            dbRefinedMaterialMapper.insertSelective(dbRefinedMaterialCopy);
            map.put(dbRefinedMaterial.getOid(),oid);
            //复制审查要点
            DbReviewPointsExample dbReviewPointsExample=new DbReviewPointsExample();
            DbReviewPointsExample.Criteria criteriaRpe=dbReviewPointsExample.createCriteria();
            criteriaRpe.andRefinedMaterialOidEqualTo(dbRefinedMaterial.getOid());
            criteriaRpe.andDeleteStatusEqualTo((short) 0);
            List<DbReviewPoints> dbReviewPointss=dbReviewPointsMapper.selectByExample(dbReviewPointsExample);
            dbReviewPointss.forEach(dbReviewPoints -> {
                DbReviewPoints reviewPointsCopy = new DbReviewPoints();
                BeanUtils.copyProperties(dbReviewPoints, reviewPointsCopy);
                reviewPointsCopy.setDeleteStatus((short)0);
                reviewPointsCopy.setOid(IdUtil.simpleUUID());
                reviewPointsCopy.setId(null);
                reviewPointsCopy.setCreateDate(new Date());
                reviewPointsCopy.setModifyDate(new Date());
                reviewPointsCopy.setRefinedMaterialOid(dbRefinedMaterialCopy.getOid());
                dbReviewPointsMapper.insert(reviewPointsCopy);
            });
        });
        return map;
    }
}
