package com.zfsoft.single.manager.yxpz;


import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.single.data.yxpz.FaModelRuleValidation;
import com.zfsoft.single.dbaccess.dao.DbFaModelRuleValidationMapper;
import com.zfsoft.single.dbaccess.data.DbFaModelRuleValidation;
import com.zfsoft.single.dbaccess.data.DbFaModelRuleValidationExample;
import com.zfsoft.single.util.BeanUtils;
import com.zfsoft.single.util.StringUtils;
import com.zfsoft.single.util.SysCode;
import com.zfsoft.single.util.UUIDUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.text.MessageFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


/**
 * @ClassName FaModelRuleValidationManager
 * @Description: 规则验证实现类
 * @Author liangss
 * @Date 2020-11-07 16:55:29
 **/
@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FaModelRuleValidationManager {

    @Resource
    private DbFaModelRuleValidationMapper dbFaModelRuleValidationMapper;

    /**
     * 分页查询列表
     * @param faModelRuleValidation
     * @param pageNumber
     * @param pageSize
     * @return
     */
    public PageResult<FaModelRuleValidation> queryFaModelRuleValidationWithPage
    (FaModelRuleValidation faModelRuleValidation, Integer pageNumber, Integer pageSize) {
        //分页参数
        if (null == pageNumber || pageNumber <= 0) {
            pageNumber = 0;
        }
        if (null == pageSize || pageSize <= 0) {
            pageSize = 10;
        }
        PageHelper.startPage(pageNumber,pageSize);
        DbFaModelRuleValidationExample dbFaModelRuleValidationExample=new DbFaModelRuleValidationExample();
        DbFaModelRuleValidationExample.Criteria criteria=dbFaModelRuleValidationExample.createCriteria();
        if(null!=faModelRuleValidation){
            if(null!=faModelRuleValidation.getServiceOid()){
                criteria.andServiceOidEqualTo(faModelRuleValidation.getServiceOid());
            }
            if(null!=faModelRuleValidation.getSxMaterialOid()){
                criteria.andSxMaterialOidEqualTo(faModelRuleValidation.getSxMaterialOid());
            }
            if(null!=faModelRuleValidation.getCatalogOid()){
                criteria.andCatalogOidEqualTo(faModelRuleValidation.getCatalogOid());
            }
            if(null!=faModelRuleValidation.getTemplateMetadataOid()){
                criteria.andTemplateMetadataOidEqualTo(faModelRuleValidation.getTemplateMetadataOid());
            }
            if(null!=faModelRuleValidation.getThanContent()){
                criteria.andThanContentEqualTo("%"+faModelRuleValidation.getThanContent().trim()+"%");
            }
        }
        criteria.andDelFlagEqualTo(SysCode.DELETE_STATUS.NO);
        Page<DbFaModelRuleValidation> dbFaModelRuleValidations= (Page<DbFaModelRuleValidation>)dbFaModelRuleValidationMapper.selectByExample(dbFaModelRuleValidationExample);
        PageResult<FaModelRuleValidation> pageResult = new PageResult<>(dbFaModelRuleValidations.getPageNum(),dbFaModelRuleValidations.getPageSize(),dbFaModelRuleValidations.getTotal());
        List<FaModelRuleValidation> faModelRuleValidationList = dbFaModelRuleValidations.stream().map(dbFaModelRuleValidation -> {
            FaModelRuleValidation faModelRuleValidation1 = new FaModelRuleValidation();
            BeanUtils.copyProperties(dbFaModelRuleValidation,faModelRuleValidation1);
            return faModelRuleValidation1;
        }).collect(Collectors.toList());
        pageResult.setData(faModelRuleValidationList);
        return pageResult;
    }







    public List<FaModelRuleValidation> queryFaModelRuleValidationList
            (FaModelRuleValidation faModelRuleValidation) {
        DbFaModelRuleValidationExample dbFaModelRuleValidationExample=new DbFaModelRuleValidationExample();
        DbFaModelRuleValidationExample.Criteria criteria=dbFaModelRuleValidationExample.createCriteria();
        if(null!=faModelRuleValidation){
            if(null!=faModelRuleValidation.getServiceOid()){
                criteria.andServiceOidEqualTo(faModelRuleValidation.getServiceOid());
            }
            if(null!=faModelRuleValidation.getComboDirectoryOid()){
                criteria.andComboDirectoryOidEqualTo(faModelRuleValidation.getComboDirectoryOid());
            }
            if(null!=faModelRuleValidation.getSxMaterialOid()){
                criteria.andSxMaterialOidEqualTo(faModelRuleValidation.getSxMaterialOid());
            }
            if(null!=faModelRuleValidation.getCatalogOid()){
                criteria.andCatalogOidEqualTo(faModelRuleValidation.getCatalogOid());
            }
            if(null!=faModelRuleValidation.getTemplateMetadataOid()){
                criteria.andTemplateMetadataOidEqualTo(faModelRuleValidation.getTemplateMetadataOid());
            }
            if(null!=faModelRuleValidation.getTemplateMetadataCode()){
                criteria.andTemplateMetadataCodeEqualTo(faModelRuleValidation.getTemplateMetadataCode()) ;
            }
            if(null!=faModelRuleValidation.getThanContent()){
                criteria.andThanContentEqualTo("%"+faModelRuleValidation.getThanContent().trim()+"%");
            }
            if(StringUtils.isNotEmpty(faModelRuleValidation.getThanTemplateMetadataOid())){
                criteria.andThanTemplateMetadataOidEqualTo(faModelRuleValidation.getThanTemplateMetadataOid());
            }
            if(StringUtils.isNotEmpty(faModelRuleValidation.getThanTemplateMetadataCode())){
                criteria.andThanTemplateMetadataCodeEqualTo(faModelRuleValidation.getThanTemplateMetadataCode());
            }

            if(null!=faModelRuleValidation.getRuleType()){
                criteria.andRuleTypeEqualTo(faModelRuleValidation.getRuleType());
            }else{
                criteria.andRuleTypeIsNull();
            }
        }
        criteria.andDelFlagEqualTo(SysCode.DELETE_STATUS.NO);
        dbFaModelRuleValidationExample.setOrderByClause(" REFINED_MATERIAL_OID DESC ");
        List<DbFaModelRuleValidation>  dbFaModelRuleValidationList=this.dbFaModelRuleValidationMapper.selectByExample(dbFaModelRuleValidationExample);
        return BeanUtils.copyListProperties(dbFaModelRuleValidationList, FaModelRuleValidation::new);
    }

    public List<FaModelRuleValidation> queryFaModelRuleValidationListByServiceOid(String  serviceOid) {
        DbFaModelRuleValidationExample dbFaModelRuleValidationExample=new DbFaModelRuleValidationExample();
        DbFaModelRuleValidationExample.Criteria criteria=dbFaModelRuleValidationExample.createCriteria();
            if(StringUtils.isNotEmpty(serviceOid)){
                criteria.andServiceOidEqualTo(serviceOid);
            }
        criteria.andDelFlagEqualTo(SysCode.DELETE_STATUS.NO);
        dbFaModelRuleValidationExample.setOrderByClause(" REFINED_MATERIAL_OID DESC ");
        List<DbFaModelRuleValidation>  dbFaModelRuleValidationList=this.dbFaModelRuleValidationMapper.selectByExample(dbFaModelRuleValidationExample);
        return BeanUtils.copyListProperties(dbFaModelRuleValidationList, FaModelRuleValidation::new);
    }


    public List<FaModelRuleValidation> queryFaModelRuleValidationListByServiceOidAndmaterialOid(String  serviceOid,String materialOid) {
        DbFaModelRuleValidationExample dbFaModelRuleValidationExample=new DbFaModelRuleValidationExample();
        DbFaModelRuleValidationExample.Criteria criteria=dbFaModelRuleValidationExample.createCriteria();
        if(StringUtils.isNotEmpty(serviceOid)){
            criteria.andServiceOidEqualTo(serviceOid);
        }

        if(StringUtils.isNotEmpty(materialOid)){
            criteria.andSxMaterialOidEqualTo(materialOid);
        }

        criteria.andDelFlagEqualTo(SysCode.DELETE_STATUS.NO);
        dbFaModelRuleValidationExample.setOrderByClause(" REFINED_MATERIAL_OID DESC ");
        List<DbFaModelRuleValidation>  dbFaModelRuleValidationList=this.dbFaModelRuleValidationMapper.selectByExample(dbFaModelRuleValidationExample);
        return BeanUtils.copyListProperties(dbFaModelRuleValidationList, FaModelRuleValidation::new);
    }


    public List<FaModelRuleValidation> queryFaModelRuleValidationListByServiceOidAndRuleType(String  serviceOid,String ruleType,String materialOid) {
        DbFaModelRuleValidationExample dbFaModelRuleValidationExample=new DbFaModelRuleValidationExample();
        DbFaModelRuleValidationExample.Criteria criteria=dbFaModelRuleValidationExample.createCriteria();
            if(StringUtils.isNotEmpty(serviceOid)){
                criteria.andServiceOidEqualTo(serviceOid);
            }
            if(StringUtils.isNotEmpty(ruleType)){
                criteria.andRuleTypeEqualTo(ruleType);
            }else{
                criteria.andRuleTypeIsNull();
            }
            if(StringUtils.isNotEmpty(materialOid)){
                criteria.andSxMaterialOidEqualTo(materialOid);
            }

        criteria.andDelFlagEqualTo(SysCode.DELETE_STATUS.NO);
        dbFaModelRuleValidationExample.setOrderByClause(" REFINED_MATERIAL_OID DESC ");
        List<DbFaModelRuleValidation>  dbFaModelRuleValidationList=this.dbFaModelRuleValidationMapper.selectByExample(dbFaModelRuleValidationExample);
        return BeanUtils.copyListProperties(dbFaModelRuleValidationList, FaModelRuleValidation::new);
    }



    /**
     * 根据主键查询信息
     * @param id
     * @return
     */
    public FaModelRuleValidation getOne(String id) {
        Assert.hasLength(id, "主键不能为空！");
        DbFaModelRuleValidation dbFaModelRuleValidation=this.dbFaModelRuleValidationMapper.selectByPrimaryKey(Long.valueOf(id));
        FaModelRuleValidation faModelRuleValidation=new FaModelRuleValidation();
        BeanUtils.copyProperties(dbFaModelRuleValidation,faModelRuleValidation);
        return faModelRuleValidation;
    }


    /**
     * 根据条件对象查询
     * @param faModelRuleValidation
     * @return
     */
    public DbFaModelRuleValidation getFaModelRuleValidationByFaModelRuleValidation(FaModelRuleValidation faModelRuleValidation) {
        DbFaModelRuleValidationExample dbFaModelRuleValidationExample=new DbFaModelRuleValidationExample();
        DbFaModelRuleValidationExample.Criteria criteria=dbFaModelRuleValidationExample.createCriteria();
        if(null!=faModelRuleValidation){
            if(null!=faModelRuleValidation.getServiceOid()){
                criteria.andServiceOidEqualTo(faModelRuleValidation.getServiceOid());
            }
            if(null!=faModelRuleValidation.getSxMaterialOid()){
                criteria.andSxMaterialOidEqualTo(faModelRuleValidation.getSxMaterialOid());
            }
            if(null!=faModelRuleValidation.getCatalogOid()){
                criteria.andCatalogOidEqualTo(faModelRuleValidation.getCatalogOid());
            }
            if(null!=faModelRuleValidation.getTemplateMetadataOid()){
                criteria.andTemplateMetadataOidEqualTo(faModelRuleValidation.getTemplateMetadataOid());
            }
            if(null!=faModelRuleValidation.getThanContent()){
                criteria.andThanContentEqualTo("%"+faModelRuleValidation.getThanContent().trim()+"%");
            }
        }
        criteria.andDelFlagEqualTo(SysCode.DELETE_STATUS.NO);
        dbFaModelRuleValidationExample.setOrderByClause(" REFINED_MATERIAL_OID DESC ");
        List<DbFaModelRuleValidation>  dbFaModelRuleValidationList=this.dbFaModelRuleValidationMapper.selectByExample(dbFaModelRuleValidationExample);
        return CollectionUtils.isEmpty(dbFaModelRuleValidationList) ? null : dbFaModelRuleValidationList.get(0);

    }

    /**
     * 删除信息
     * @param id
     */
    public void del(String id) {
        Assert.hasLength(id, "主键不能为空！");
        DbFaModelRuleValidation dbFaModelRuleValidation=this.dbFaModelRuleValidationMapper.selectByPrimaryKey(Long.valueOf(id));
        Assert.notNull(dbFaModelRuleValidation, MessageFormat.format("操作对象不存在！对象id为{0}", id));
        dbFaModelRuleValidation.setDelFlag(dbFaModelRuleValidation.getDelFlag().equals(SysCode.DELETE_STATUS.YES) ? SysCode.DELETE_STATUS.NO : SysCode.DELETE_STATUS.YES);
        this.dbFaModelRuleValidationMapper.updateByPrimaryKeySelective(dbFaModelRuleValidation);

    }

    /**
     * 更新/保存信息
     * @param faModelRuleValidation
     * @throws Exception
     */
    public void saveOrUpdate(FaModelRuleValidation faModelRuleValidation){
        DbFaModelRuleValidation dbFaModelRuleValidation;
        if (null != faModelRuleValidation.getId()) {
            dbFaModelRuleValidation = this.dbFaModelRuleValidationMapper.selectByPrimaryKey(faModelRuleValidation.getId());
            Assert.notNull(dbFaModelRuleValidation, MessageFormat.format("更新对象不存在！对象id为{0}", dbFaModelRuleValidation.getId()));
            BeanUtils.copyProperties(faModelRuleValidation, dbFaModelRuleValidation);
            dbFaModelRuleValidation.setModifyDate(new Date());
            this.dbFaModelRuleValidationMapper.updateByPrimaryKeySelective(dbFaModelRuleValidation);
        } else {
            dbFaModelRuleValidation = new DbFaModelRuleValidation();
            BeanUtils.copyProperties(faModelRuleValidation, dbFaModelRuleValidation);
            dbFaModelRuleValidation.setDelFlag(SysCode.DELETE_STATUS.NO);
            dbFaModelRuleValidation.setCreateDate(new Date());
            dbFaModelRuleValidation.setModifyDate(new Date());
            dbFaModelRuleValidation.setFaModelRuleValidationOid(UUIDUtil.randomUUID());
            this.dbFaModelRuleValidationMapper.insert(dbFaModelRuleValidation);
        }

    }


    public void saveOrUpdateList( List<FaModelRuleValidation> faModelRuleValidationList) {
        for(FaModelRuleValidation faModelRuleValidation:faModelRuleValidationList){
            DbFaModelRuleValidation dbFaModelRuleValidation;
            if (null != faModelRuleValidation.getId()) {
                dbFaModelRuleValidation = this.dbFaModelRuleValidationMapper.selectByPrimaryKey(faModelRuleValidation.getId());
                Assert.notNull(dbFaModelRuleValidation, MessageFormat.format("更新对象不存在！对象id为{0}", dbFaModelRuleValidation.getId()));
                BeanUtils.copyProperties(faModelRuleValidation, dbFaModelRuleValidation);
                dbFaModelRuleValidation.setModifyDate(new Date());
                this.dbFaModelRuleValidationMapper.updateByPrimaryKey(dbFaModelRuleValidation);
            } else {
                dbFaModelRuleValidation = new DbFaModelRuleValidation();
                BeanUtils.copyProperties(faModelRuleValidation, dbFaModelRuleValidation);
                dbFaModelRuleValidation.setDelFlag(SysCode.DELETE_STATUS.NO);
                dbFaModelRuleValidation.setCreateDate(new Date());
                dbFaModelRuleValidation.setModifyDate(new Date());
                dbFaModelRuleValidation.setFaModelRuleValidationOid(UUIDUtil.randomUUID());
                this.dbFaModelRuleValidationMapper.insert(dbFaModelRuleValidation);
            }
        }
    }



    public void saveOrUpdateFaModelRuleValidationList( List<FaModelRuleValidation> faModelRuleValidationList) {
        for(FaModelRuleValidation faModelRuleValidation:faModelRuleValidationList){
            String[] elementLabelTree=faModelRuleValidation.getElementLabelTree();
            String[] typeLabelTree=faModelRuleValidation.getTypeLabelTree();
            String[] licenseTree=faModelRuleValidation.getLicenseTree();
            if(elementLabelTree.length>0 ) {
                String materials = elementLabelTree[0];
                String materialCatalogs = elementLabelTree[1];
                String materialElements = elementLabelTree[2];

                String[] materialss = materials.split(";");
                String materialOid = materialss[0];
                String materialName = materialss[1];

                String[] materialCatalogss = materialCatalogs.split(";");
                String refinedMaterialOid = materialCatalogss[0];
                String refinedMaterialName = materialCatalogss[1];
                String materialCatalogOid = materialCatalogss[2];
                String materialCatalogName = materialCatalogss[3];

                String[] materialElementss = materialElements.split(";");
                String elementOid = materialElementss[0];
                String elementCode = materialElementss[1];
                String elementName = materialElementss[2];

                faModelRuleValidation.setSxMaterialOid(materialOid);
                faModelRuleValidation.setSxMaterialName(materialName);
                faModelRuleValidation.setRefinedMaterialOid(refinedMaterialOid);
                faModelRuleValidation.setRefinedMaterialName(refinedMaterialName);
                faModelRuleValidation.setCatalogOid(materialCatalogOid);
                faModelRuleValidation.setCatalogName(materialCatalogName);
                faModelRuleValidation.setTemplateMetadataOid(elementOid);
                faModelRuleValidation.setTemplateMetadataCode(elementCode);
                faModelRuleValidation.setTemplateMetadataName(elementName);

                if(faModelRuleValidation.getRuleType().equals("DZBD")&&typeLabelTree.length>0){
                    String typeLabelTreeOne = typeLabelTree[0];
                    String typeLabelTreeTwo = typeLabelTree[1];
                    String typeLabelTreeThree = typeLabelTree[2];
                    String[] typeLabelTreeOnes = typeLabelTreeOne.split(";");
                    String[] typeLabelTreeTwos = typeLabelTreeTwo.split(";");
                    String[] typeLabelTreeThrees = typeLabelTreeThree.split(";");
                    String thanRefinedMaterialOid = typeLabelTreeOnes[0];
                    String thanRefinedMaterialName = typeLabelTreeOnes[1];
                    String thanCatalogOid = typeLabelTreeTwos[0];
                    String thanCatalogName = typeLabelTreeTwos[1];
                    String thanTemplateMetadataOid = typeLabelTreeThrees[0];
                    String thanTemplateMetadataCode = typeLabelTreeThrees[1];
                    String thanTemplateMetadataName = typeLabelTreeThrees[2];
                    faModelRuleValidation.setThanRefinedMaterialOid(thanRefinedMaterialOid);
                    faModelRuleValidation.setThanRefinedMaterialName(thanRefinedMaterialName);
                    faModelRuleValidation.setThanCatalogOid(thanCatalogOid);
                    faModelRuleValidation.setThanCatalogName(thanCatalogName);
                    faModelRuleValidation.setThanTemplateMetadataOid(thanTemplateMetadataOid);
                    faModelRuleValidation.setThanTemplateMetadataCode(thanTemplateMetadataCode);
                    faModelRuleValidation.setThanTemplateMetadataName(thanTemplateMetadataName);
                }else if(faModelRuleValidation.getRuleType().equals("JCBD")){
                    String basicForms=faModelRuleValidation.getBasicForms();
                    String[] typeLabelTreeOnes = basicForms.split(";");
                    String thanTemplateMetadataOid = typeLabelTreeOnes[0];
                    String thanTemplateMetadataCode = typeLabelTreeOnes[1];
                    String thanTemplateMetadataName = typeLabelTreeOnes[2];
                    faModelRuleValidation.setThanTemplateMetadataOid(thanTemplateMetadataOid);
                    faModelRuleValidation.setThanTemplateMetadataCode(thanTemplateMetadataCode);
                    faModelRuleValidation.setThanTemplateMetadataName(thanTemplateMetadataName);
                }else if(faModelRuleValidation.getRuleType().equals("DZZZ")&&licenseTree.length>0){
                    String licenseTreeOne = licenseTree[0];
                    String licenseTreeTwo = licenseTree[1];
                    String[] licenseTreeOnes = licenseTreeOne.split(";");
                    String[] licenseTreeTwos = licenseTreeTwo.split(";");
                    String thanRefinedMaterialOid = licenseTreeOnes[0];
                    String thanRefinedMaterialName = licenseTreeOnes[1];
                    String thanCatalogOid = licenseTreeOnes[2];
                    String thanCatalogName = licenseTreeOnes[3];

                    String thanTemplateMetadataOid = licenseTreeTwos[0];
                    String thanTemplateMetadataCode = licenseTreeTwos[1];
                    String thanTemplateMetadataName = licenseTreeTwos[2];
                    faModelRuleValidation.setThanCatalogOid(thanCatalogOid);
                    faModelRuleValidation.setThanCatalogName(thanCatalogName);
                    faModelRuleValidation.setThanRefinedMaterialOid(thanRefinedMaterialOid);
                    faModelRuleValidation.setThanRefinedMaterialName(thanRefinedMaterialName);
                    faModelRuleValidation.setThanTemplateMetadataOid(thanTemplateMetadataOid);
                    faModelRuleValidation.setThanTemplateMetadataCode(thanTemplateMetadataCode);
                    faModelRuleValidation.setThanTemplateMetadataName(thanTemplateMetadataName);
                }

                DbFaModelRuleValidation dbFaModelRuleValidation;
                if (null != faModelRuleValidation.getId()) {
                    dbFaModelRuleValidation = this.dbFaModelRuleValidationMapper.selectByPrimaryKey(faModelRuleValidation.getId());
                    Assert.notNull(dbFaModelRuleValidation, MessageFormat.format("更新对象不存在！对象id为{0}", dbFaModelRuleValidation.getId()));
                    BeanUtils.copyProperties(faModelRuleValidation, dbFaModelRuleValidation);
                    dbFaModelRuleValidation.setModifyDate(new Date());
                    this.dbFaModelRuleValidationMapper.updateByPrimaryKey(dbFaModelRuleValidation);
                } else {
                    dbFaModelRuleValidation = new DbFaModelRuleValidation();
                    BeanUtils.copyProperties(faModelRuleValidation, dbFaModelRuleValidation);
                    dbFaModelRuleValidation.setDelFlag(SysCode.DELETE_STATUS.NO);
                    dbFaModelRuleValidation.setCreateDate(new Date());
                    dbFaModelRuleValidation.setModifyDate(new Date());
                    dbFaModelRuleValidation.setFaModelRuleValidationOid(UUIDUtil.randomUUID());
                    this.dbFaModelRuleValidationMapper.insert(dbFaModelRuleValidation);
                }
            }


        }
    }




    public List<FaModelRuleValidation> queryFaModelRuleValidationListByRuleType
            (FaModelRuleValidation faModelRuleValidation) {
        DbFaModelRuleValidationExample dbFaModelRuleValidationExample=new DbFaModelRuleValidationExample();
        DbFaModelRuleValidationExample.Criteria criteria=dbFaModelRuleValidationExample.createCriteria();
        if(null!=faModelRuleValidation){
            if(null!=faModelRuleValidation.getServiceOid()){
                criteria.andServiceOidEqualTo(faModelRuleValidation.getServiceOid());
            }
            if(null!=faModelRuleValidation.getComboDirectoryOid()){
                criteria.andComboDirectoryOidEqualTo(faModelRuleValidation.getComboDirectoryOid());
            }
            if(null!=faModelRuleValidation.getSxMaterialOid()){
                criteria.andSxMaterialOidEqualTo(faModelRuleValidation.getSxMaterialOid());
            }
            if(null!=faModelRuleValidation.getCatalogOid()){
                criteria.andCatalogOidEqualTo(faModelRuleValidation.getCatalogOid());
            }
            if(null!=faModelRuleValidation.getTemplateMetadataOid()){
                criteria.andTemplateMetadataOidEqualTo(faModelRuleValidation.getTemplateMetadataOid());
            }
            if(null!=faModelRuleValidation.getTemplateMetadataCode()){
                criteria.andTemplateMetadataCodeEqualTo(faModelRuleValidation.getTemplateMetadataCode()) ;
            }
            if(null!=faModelRuleValidation.getThanContent()){
                criteria.andThanContentEqualTo("%"+faModelRuleValidation.getThanContent().trim()+"%");
            }
            if(null!=faModelRuleValidation.getRuleType()){
                criteria.andRuleTypeEqualTo(faModelRuleValidation.getRuleType());
            }
        }
        criteria.andDelFlagEqualTo(SysCode.DELETE_STATUS.NO);
        dbFaModelRuleValidationExample.setOrderByClause(" REFINED_MATERIAL_OID DESC ");
        List<DbFaModelRuleValidation>  dbFaModelRuleValidationList=this.dbFaModelRuleValidationMapper.selectByExample(dbFaModelRuleValidationExample);
        List<FaModelRuleValidation> faModelRuleValidationList = dbFaModelRuleValidationList.stream().map(dbFaModelRuleValidation -> {
            FaModelRuleValidation faModelRuleValidation1 = new FaModelRuleValidation();
            BeanUtils.copyProperties(dbFaModelRuleValidation,faModelRuleValidation1);
            String refinedMaterialOid = dbFaModelRuleValidation.getRefinedMaterialOid();
            String refinedMaterialName = dbFaModelRuleValidation.getRefinedMaterialName();
            String materialOid = dbFaModelRuleValidation.getSxMaterialOid();
            String materialName = dbFaModelRuleValidation.getSxMaterialName();
            String materials = materialOid+";"+materialName;
            String materialCatalogOid = dbFaModelRuleValidation.getCatalogOid();
            String materialCatalogName = dbFaModelRuleValidation.getCatalogName();
            String materialCatalogss =refinedMaterialOid+";"+refinedMaterialName+";"+materialCatalogOid+";"+ materialCatalogName;
            String elementOid = dbFaModelRuleValidation.getTemplateMetadataOid();
            String elementCode = dbFaModelRuleValidation.getTemplateMetadataCode();
            String elementName = dbFaModelRuleValidation.getTemplateMetadataName();
            String materialElementss = elementOid+";"+elementCode+";"+elementName;
            String[] elementLabelTree=new   String[3];
            elementLabelTree[0]=materials;
            elementLabelTree[1]=materialCatalogss;
            elementLabelTree[2]=materialElementss;
            faModelRuleValidation1.setElementLabelTree(elementLabelTree);
            String thanTemplateMetadataOid = dbFaModelRuleValidation.getThanTemplateMetadataOid();
            String thanTemplateMetadataCode = dbFaModelRuleValidation.getThanTemplateMetadataCode();
            String thanTemplateMetadataName = dbFaModelRuleValidation.getThanTemplateMetadataName();
            if(dbFaModelRuleValidation.getRuleType().equals("DZBD")){
                String thanRefinedMaterialOid =  dbFaModelRuleValidation.getThanRefinedMaterialOid();
                String thanRefinedMaterialName = dbFaModelRuleValidation.getThanRefinedMaterialName();
                String thanCatalogOid = dbFaModelRuleValidation.getThanCatalogOid();
                String thanCatalogName = dbFaModelRuleValidation.getThanCatalogName();
                String typeLabelTreeOne =thanRefinedMaterialOid+";"+thanRefinedMaterialName ;
                String typeLabelTreeTwo =thanCatalogOid+";"+ thanCatalogName;
                String typeLabelTreeThree =thanTemplateMetadataOid+";" +thanTemplateMetadataCode+";"+thanTemplateMetadataName;
                String[] typeLabelTree =new   String[3];
                typeLabelTree[0]=typeLabelTreeOne;
                typeLabelTree[1]=typeLabelTreeTwo;
                typeLabelTree[2]=typeLabelTreeThree;
                faModelRuleValidation1.setTypeLabelTree(typeLabelTree);
            }else  if(dbFaModelRuleValidation.getRuleType().equals("JCBD")) {
                String basicForms =thanTemplateMetadataOid+";" +thanTemplateMetadataCode+";"+thanTemplateMetadataName;
                faModelRuleValidation1.setBasicForms(basicForms);
            }else  if(dbFaModelRuleValidation.getRuleType().equals("DZZZ")) {
                String[] licenseTree=new   String[2];
                String thanCatalogOid = dbFaModelRuleValidation.getThanCatalogOid();
                String thanCatalogName = dbFaModelRuleValidation.getThanCatalogName();
                String thanRefinedMaterialOid =  dbFaModelRuleValidation.getThanRefinedMaterialOid();
                String thanRefinedMaterialName = dbFaModelRuleValidation.getThanRefinedMaterialName();
                String typeLabelTreeTwo =thanRefinedMaterialOid+";"+thanRefinedMaterialName+";"+thanCatalogOid+";"+ thanCatalogName;
                String typeLabelTreeThree =thanTemplateMetadataOid+";" +thanTemplateMetadataCode+";"+thanTemplateMetadataName;
                licenseTree[0]=typeLabelTreeTwo;
                licenseTree[1]=typeLabelTreeThree;
                faModelRuleValidation1.setLicenseTree(licenseTree);
            }

            return faModelRuleValidation1;
        }).collect(Collectors.toList());
        return faModelRuleValidationList;
       /* return com.zfsoft.util.BeanUtils.copyListProperties(dbFaModelRuleValidationList, FaModelRuleValidation::new);*/
    }






}
