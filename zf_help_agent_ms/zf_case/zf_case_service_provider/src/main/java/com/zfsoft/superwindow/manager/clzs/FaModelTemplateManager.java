package com.zfsoft.superwindow.manager.clzs;


import cn.hutool.core.date.DateUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.superwindow.data.clzs.FaModelTemplate;
import com.zfsoft.superwindow.data.clzs.FaModelTemplateBlock;
import com.zfsoft.superwindow.dbaccess.dao.DbFaModelTemplateMapper;
import com.zfsoft.superwindow.dbaccess.data.DbFaModelTemplate;
import com.zfsoft.superwindow.dbaccess.data.DbFaModelTemplateExample;
import com.zfsoft.superwindow.dbaccess.data.DbMaterialCatalog;
import com.zfsoft.superwindow.util.BeanUtils;
import com.zfsoft.superwindow.util.SysCode;
import com.zfsoft.superwindow.util.UUIDUtil;
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
 * @ClassName FaModelTemplateManager
 * @Description: 识别模板实现类
 * @Author liangss
 * @Date 2020-11-07 16:55:29
 **/
@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FaModelTemplateManager {

    @Resource
    private DbFaModelTemplateMapper dbFaModelTemplateMapper;

    //材料目录
    @Resource
    private  MaterialCatalogManager materialCatalogManager;

    @Resource
    private  FaModelTemplateBlockManager faModelTemplateBlockManager;

    /**
     * 分页查询列表
     * @param faModelTemplate
     * @param pageNumber
     * @param pageSize
     * @return
     */
    public PageResult<FaModelTemplate> queryFaModelTemplateWithPage
            (FaModelTemplate faModelTemplate, Integer pageNumber, Integer pageSize) {
        //分页参数
        if (null == pageNumber || pageNumber <= 0) {
            pageNumber = 0;
        }
        if (null == pageSize || pageSize <= 0) {
            pageSize = 10;
        }
        PageHelper.startPage(pageNumber,pageSize);
        DbFaModelTemplateExample dbFaModelTemplateExample=new DbFaModelTemplateExample();
        DbFaModelTemplateExample.Criteria criteria=dbFaModelTemplateExample.createCriteria();
        if(null!=faModelTemplate){

            if(null!=faModelTemplate.getMaterialCatalogOid()){
                criteria.andMaterialCatalogOidEqualTo(faModelTemplate.getMaterialCatalogOid());
            }
            if(null!=faModelTemplate.getTemplateCode()){
                criteria.andTemplateCodeLike("%"+faModelTemplate.getTemplateCode().trim()+"%");
            }
            if(null!=faModelTemplate.getTemplateName()){
                criteria.andTemplateNameLike("%"+faModelTemplate.getTemplateName().trim()+"%");
            }

        }
        criteria.andDelFlagEqualTo(SysCode.DELETE_STATUS.NO);
        dbFaModelTemplateExample.setOrderByClause(" MODIFY_DATE DESC ");
        Page<DbFaModelTemplate> dbFaModelTemplates= (Page<DbFaModelTemplate>)dbFaModelTemplateMapper.selectByExample(dbFaModelTemplateExample);
        PageResult<FaModelTemplate> pageResult = new PageResult<>(dbFaModelTemplates.getPageNum(),dbFaModelTemplates.getPageSize(),dbFaModelTemplates.getTotal());
        List<FaModelTemplate> faModelTemplateList = dbFaModelTemplates.stream().map(dbFaModelTemplate -> {
            FaModelTemplate faModelTemplate1 = new FaModelTemplate();
            BeanUtils.copyProperties(dbFaModelTemplate,faModelTemplate1);
            return faModelTemplate1;
        }).collect(Collectors.toList());
        pageResult.setData(faModelTemplateList);
        return pageResult;
    }


    /**
     * 查询模板列表根据条件
     * @param faModelTemplate
     * @param type
     * @return
     */
    public List<FaModelTemplate> queryFaModelTemplateList
            (FaModelTemplate faModelTemplate, String type) {
        DbFaModelTemplateExample dbFaModelTemplateExample=new DbFaModelTemplateExample();
        DbFaModelTemplateExample.Criteria criteria=dbFaModelTemplateExample.createCriteria();
        if(null!=faModelTemplate){
            if(null!=faModelTemplate.getMaterialCatalogOid()){
                criteria.andMaterialCatalogOidEqualTo(faModelTemplate.getMaterialCatalogOid());
            }
            if(null!=faModelTemplate.getTemplateCode()){
                criteria.andTemplateCodeLike("%"+faModelTemplate.getTemplateCode().trim()+"%");
            }
            if(null!=faModelTemplate.getTemplateName()){
                criteria.andTemplateNameLike("%"+faModelTemplate.getTemplateName().trim()+"%");
            }
        }
        if(type.equals("YFB")){//查询已发布的列表
            criteria.andVersionIsNotNull();
        }
        criteria.andTemplateStatusEqualTo(SysCode.DELETE_STATUS.YES);
        criteria.andDelFlagEqualTo(SysCode.DELETE_STATUS.NO);
        dbFaModelTemplateExample.setOrderByClause(" MODIFY_DATE DESC ");
        List<DbFaModelTemplate>  dbFaModelTemplateList=this.dbFaModelTemplateMapper.selectByExample(dbFaModelTemplateExample);
        return BeanUtils.copyListProperties(dbFaModelTemplateList, FaModelTemplate::new);

    }
    public void updateFaModelTemplateStatusByCataOid(String cataOid) {
        DbFaModelTemplateExample dbFaModelTemplateExample=new DbFaModelTemplateExample();
        DbFaModelTemplateExample.Criteria criteria=dbFaModelTemplateExample.createCriteria();
        if(null!=cataOid){
            criteria.andMaterialCatalogOidEqualTo(cataOid);
        }
        criteria.andTemplateStatusEqualTo(SysCode.DELETE_STATUS.YES);//是否启用
        criteria.andDelFlagEqualTo(SysCode.DELETE_STATUS.NO);
        List<DbFaModelTemplate>  dbFaModelTemplateList=this.dbFaModelTemplateMapper.selectByExample(dbFaModelTemplateExample);
        if(dbFaModelTemplateList.size()>0){
            for (DbFaModelTemplate dbFaModelTemplate:dbFaModelTemplateList) {
                dbFaModelTemplate.setTemplateStatus(SysCode.DELETE_STATUS.NO);//禁用
                this.dbFaModelTemplateMapper.updateByPrimaryKeySelective(dbFaModelTemplate);
            }
        }
    }


    /**
     * 根据主键查询信息
     * @param id
     * @return
     */
    public FaModelTemplate getOne(String id) {
        Assert.hasLength(id, "主键不能为空！");
        DbFaModelTemplate dbFaModelTemplate=this.dbFaModelTemplateMapper.selectByPrimaryKey(Long.valueOf(id));
        FaModelTemplate faModelTemplate=new FaModelTemplate();
        BeanUtils.copyProperties(dbFaModelTemplate,faModelTemplate);
        return faModelTemplate;
    }


    /**
     * 根据业务主键查询模板
     * @param faModelTemplateOid
     * @return
     */
    public FaModelTemplate getFaModelTemplateByOid(String faModelTemplateOid) {
        DbFaModelTemplateExample dbFaModelTemplateExample=new DbFaModelTemplateExample();
        DbFaModelTemplateExample.Criteria criteria=dbFaModelTemplateExample.createCriteria();
        if(null!=faModelTemplateOid){
            criteria.andFaModelTemplateOidEqualTo(faModelTemplateOid);
        }
        criteria.andDelFlagEqualTo(SysCode.DELETE_STATUS.NO);
        criteria.andTemplateStatusEqualTo(SysCode.DELETE_STATUS.YES);
        List<DbFaModelTemplate>  dbFaModelTemplateList=this.dbFaModelTemplateMapper.selectByExample(dbFaModelTemplateExample);
        DbFaModelTemplate dbFaModelTemplate=CollectionUtils.isEmpty(dbFaModelTemplateList) ? null : dbFaModelTemplateList.get(0);
        FaModelTemplate  faModelTemplate = null;
        if(null!=dbFaModelTemplate){
            faModelTemplate=new FaModelTemplate();
            BeanUtils.copyProperties(dbFaModelTemplate, faModelTemplate);
        }
        return  faModelTemplate;
    }


    public FaModelTemplate getFaModelTemplateAllByOid(String faModelTemplateOid) {
        DbFaModelTemplateExample dbFaModelTemplateExample=new DbFaModelTemplateExample();
        DbFaModelTemplateExample.Criteria criteria=dbFaModelTemplateExample.createCriteria();
        if(null!=faModelTemplateOid){
            criteria.andFaModelTemplateOidEqualTo(faModelTemplateOid);
        }
    /*    criteria.andDelFlagEqualTo(SysCode.DELETE_STATUS.NO);
        criteria.andTemplateStatusEqualTo(SysCode.DELETE_STATUS.YES);*/
        List<DbFaModelTemplate>  dbFaModelTemplateList=this.dbFaModelTemplateMapper.selectByExample(dbFaModelTemplateExample);
        DbFaModelTemplate dbFaModelTemplate=CollectionUtils.isEmpty(dbFaModelTemplateList) ? null : dbFaModelTemplateList.get(0);
        FaModelTemplate  faModelTemplate = null;
        if(null!=dbFaModelTemplate){
            faModelTemplate=new FaModelTemplate();
            BeanUtils.copyProperties(dbFaModelTemplate, faModelTemplate);
        }
        return  faModelTemplate;
    }

    /**
     * 根据条件对象查询
     * @param faModelTemplate
     * @return
     */
    public DbFaModelTemplate getFaModelTemplateByFaModelTemplate(FaModelTemplate faModelTemplate) {
        DbFaModelTemplateExample dbFaModelTemplateExample=new DbFaModelTemplateExample();
        DbFaModelTemplateExample.Criteria criteria=dbFaModelTemplateExample.createCriteria();
        if(null!=faModelTemplate){

            if(null!=faModelTemplate.getMaterialCatalogOid()){
                criteria.andMaterialCatalogOidEqualTo(faModelTemplate.getMaterialCatalogOid());
            }
            if(null!=faModelTemplate.getTemplateCode()){
                criteria.andTemplateCodeLike("%"+faModelTemplate.getTemplateCode().trim()+"%");
            }
            if(null!=faModelTemplate.getTemplateName()){
                criteria.andTemplateNameLike("%"+faModelTemplate.getTemplateName().trim()+"%");
            }
        }
        criteria.andTemplateStatusEqualTo(SysCode.ABLE_STATUS.YES);//查询已发布的
        criteria.andDelFlagEqualTo(SysCode.DELETE_STATUS.NO);
        List<DbFaModelTemplate>  dbFaModelTemplateList=this.dbFaModelTemplateMapper.selectByExample(dbFaModelTemplateExample);
        return CollectionUtils.isEmpty(dbFaModelTemplateList) ? null : dbFaModelTemplateList.get(0);

    }

    /**
     * 根据所属目录id获取当前正在编制中的模板（未生成版本号）
     * @param materialCatalogOid
     * @return
     */
    public FaModelTemplate getCompilingFaModelTemplateByCataOid(String materialCatalogOid) {
        DbFaModelTemplateExample dbFaModelTemplateExample=new DbFaModelTemplateExample();
        DbFaModelTemplateExample.Criteria criteria=dbFaModelTemplateExample.createCriteria();
            if(null!=materialCatalogOid){
                criteria.andMaterialCatalogOidEqualTo(materialCatalogOid);
            }
            criteria.andVersionIsNull();
        criteria.andDelFlagEqualTo(SysCode.DELETE_STATUS.NO);
        criteria.andTemplateStatusEqualTo(SysCode.ABLE_STATUS.ZC);
        List<DbFaModelTemplate>  dbFaModelTemplateList=this.dbFaModelTemplateMapper.selectByExample(dbFaModelTemplateExample);
        DbFaModelTemplate dbFaModelTemplate=CollectionUtils.isEmpty(dbFaModelTemplateList) ? null : dbFaModelTemplateList.get(0);
        FaModelTemplate  faModelTemplate = new FaModelTemplate();
        if(null!=dbFaModelTemplate){
            BeanUtils.copyProperties(dbFaModelTemplate, faModelTemplate);
        }else{
            faModelTemplate=null;
        }
        return  faModelTemplate;
    }

    /**
     * 根据所属目录id获取当前已经发布的模板
     * @param materialCatalogOid
     * @return
     */
    public FaModelTemplate getPulishedFaModelTemplateByCataOid(String materialCatalogOid) {
        DbFaModelTemplateExample dbFaModelTemplateExample=new DbFaModelTemplateExample();
        DbFaModelTemplateExample.Criteria criteria=dbFaModelTemplateExample.createCriteria();
        if(null!=materialCatalogOid){
            criteria.andMaterialCatalogOidEqualTo(materialCatalogOid);
        }
        criteria.andDelFlagEqualTo(SysCode.DELETE_STATUS.NO);
        criteria.andTemplateStatusEqualTo(SysCode.DELETE_STATUS.YES);
        List<DbFaModelTemplate>  dbFaModelTemplateList=this.dbFaModelTemplateMapper.selectByExample(dbFaModelTemplateExample);
        DbFaModelTemplate dbFaModelTemplate=CollectionUtils.isEmpty(dbFaModelTemplateList) ? null : dbFaModelTemplateList.get(0);
        FaModelTemplate  faModelTemplate = null;
        if(null!=dbFaModelTemplate){
            faModelTemplate=new FaModelTemplate();
            BeanUtils.copyProperties(dbFaModelTemplate, faModelTemplate);
        }
        return  faModelTemplate;
    }


    /**
     * 删除信息
     * @param id
     */
    public void del(String id) {
        Assert.hasLength(id, "主键不能为空！");
        DbFaModelTemplate dbFaModelTemplate=this.dbFaModelTemplateMapper.selectByPrimaryKey(Long.valueOf(id));
        Assert.notNull(dbFaModelTemplate, MessageFormat.format("操作对象不存在！对象id为{0}", id));
        dbFaModelTemplate.setDelFlag(SysCode.DELETE_STATUS.YES);
        this.dbFaModelTemplateMapper.updateByPrimaryKeySelective(dbFaModelTemplate);

    }


    public void able(String id,String materialCatalogOid,String ableFlag) {
        Assert.hasLength(id, "主键不能为空！");
        DbFaModelTemplateExample dbFaModelTemplateExample=new DbFaModelTemplateExample();
        DbFaModelTemplateExample.Criteria criteria=dbFaModelTemplateExample.createCriteria();
        if(null!=materialCatalogOid){
            criteria.andMaterialCatalogOidEqualTo(materialCatalogOid);
        }
        criteria.andDelFlagEqualTo(SysCode.DELETE_STATUS.NO);
        criteria.andTemplateStatusNotEqualTo(SysCode.ABLE_STATUS.ZC);
        //criteria.andTemplateStatusEqualTo(SysCode.ABLE_STATUS.YES);
        //把所有发布的改为禁用
        List<DbFaModelTemplate>  dbFaModelTemplateList=this.dbFaModelTemplateMapper.selectByExample(dbFaModelTemplateExample);
        for(DbFaModelTemplate db:dbFaModelTemplateList){
            db.setTemplateStatus(SysCode.ABLE_STATUS.NO);
            this.dbFaModelTemplateMapper.updateByPrimaryKeySelective(db);
        }

        DbFaModelTemplate dbFaModelTemplate=this.dbFaModelTemplateMapper.selectByPrimaryKey(Long.valueOf(id));
        if(ableFlag.equals("Y")){
            dbFaModelTemplate.setTemplateStatus(SysCode.ABLE_STATUS.YES);
            dbFaModelTemplate.setModifyDate(new Date());
            this.dbFaModelTemplateMapper.updateByPrimaryKeySelective(dbFaModelTemplate);
        }
    }

    public void saveOrUpdate(FaModelTemplate faModelTemplate) throws Exception {
        DbFaModelTemplate dbFaModelTemplate;
        if (null != faModelTemplate.getId()) {
            dbFaModelTemplate = this.dbFaModelTemplateMapper.selectByPrimaryKey(faModelTemplate.getId());
            Assert.notNull(dbFaModelTemplate, MessageFormat.format("更新对象不存在！对象id为{0}", dbFaModelTemplate.getId()));
            BeanUtils.copyProperties(faModelTemplate, dbFaModelTemplate);
            dbFaModelTemplate.setModifyDate(new Date());
            this.dbFaModelTemplateMapper.updateByPrimaryKeySelective(dbFaModelTemplate);
        } else {
            dbFaModelTemplate = new DbFaModelTemplate();

            BeanUtils.copyProperties(faModelTemplate, dbFaModelTemplate);
            dbFaModelTemplate.setDelFlag(SysCode.DELETE_STATUS.NO);
            dbFaModelTemplate.setCreateDate(new Date());
            dbFaModelTemplate.setModifyDate(new Date());
            dbFaModelTemplate.setFaModelTemplateOid(UUIDUtil.randomUUID());
            //生成编码
            String  templateCode   = createTemplateCode(faModelTemplate.getTemplateCode());
            dbFaModelTemplate.setTemplateCode(templateCode);
            this.dbFaModelTemplateMapper.insert(dbFaModelTemplate);
        }

    }


    public String saveOrUpdateFaModelTemplate(FaModelTemplate faModelTemplate) throws Exception {
        DbFaModelTemplate dbFaModelTemplate;
        if (null != faModelTemplate.getId()) {
            dbFaModelTemplate = this.dbFaModelTemplateMapper.selectByPrimaryKey(faModelTemplate.getId());
            Assert.notNull(dbFaModelTemplate, MessageFormat.format("更新对象不存在！对象id为{0}", dbFaModelTemplate.getId()));
            BeanUtils.copyProperties(faModelTemplate, dbFaModelTemplate);
            dbFaModelTemplate.setModifyDate(new Date());
            this.dbFaModelTemplateMapper.updateByPrimaryKeySelective(dbFaModelTemplate);
        } else {
            dbFaModelTemplate = new DbFaModelTemplate();

            BeanUtils.copyProperties(faModelTemplate, dbFaModelTemplate);
            dbFaModelTemplate.setDelFlag(SysCode.DELETE_STATUS.NO);
            dbFaModelTemplate.setCreateDate(new Date());
            dbFaModelTemplate.setModifyDate(new Date());
            dbFaModelTemplate.setFaModelTemplateOid(UUIDUtil.randomUUID());
            this.dbFaModelTemplateMapper.insert(dbFaModelTemplate);
        }
        return  dbFaModelTemplate.getFaModelTemplateOid();
    }

    /**
     * 根据目录编号统计当前模板数量
     * @param cataCode
     * @return
     */
    public Long countByCataCode(String cataCode) {
        DbFaModelTemplateExample dbFaModelTemplateExample=new DbFaModelTemplateExample();
        DbFaModelTemplateExample.Criteria criteria=dbFaModelTemplateExample.createCriteria();
        criteria.andTemplateCodeLike("%"+cataCode+"%");
        criteria.andDelFlagEqualTo(SysCode.DELETE_STATUS.NO);
        return Long.valueOf(this.dbFaModelTemplateMapper.countByExample(dbFaModelTemplateExample));
    }

    /**
     * 根据目录编号获取当前最大版本号的模板
     * @param cataCode
     * @return
     */
    public DbFaModelTemplate getMaxVersionByCataCode(String cataCode) {
        DbFaModelTemplateExample dbFaModelTemplateExample=new DbFaModelTemplateExample();
        DbFaModelTemplateExample.Criteria criteria=dbFaModelTemplateExample.createCriteria();
        criteria.andTemplateCodeLike("%"+cataCode+"%");
        criteria.andVersionIsNotNull();
        criteria.andDelFlagEqualTo(SysCode.DELETE_STATUS.NO);
        dbFaModelTemplateExample.setOrderByClause(" CREATE_DATE DESC ");
        List<DbFaModelTemplate>  dbFaModelTemplateList=this.dbFaModelTemplateMapper.selectByExample(dbFaModelTemplateExample);
        return CollectionUtils.isEmpty(dbFaModelTemplateList) ? null : dbFaModelTemplateList.get(0);
    }


    /**
     * 根据主键查询模板信息
     * @param faModelTemplateOid
     * @return
     */
    public DbFaModelTemplate getDbFaModelTemplateByOid(String faModelTemplateOid) {
        DbFaModelTemplateExample dbFaModelTemplateExample=new DbFaModelTemplateExample();
        DbFaModelTemplateExample.Criteria criteria=dbFaModelTemplateExample.createCriteria();
        if(null!=faModelTemplateOid){
            criteria.andFaModelTemplateOidEqualTo(faModelTemplateOid);
        }
        criteria.andDelFlagEqualTo(SysCode.DELETE_STATUS.NO);
        /*criteria.andTemplateStatusEqualTo(SysCode.ABLE_STATUS.YES);*/
        List<DbFaModelTemplate>  dbFaModelTemplateList=this.dbFaModelTemplateMapper.selectByExample(dbFaModelTemplateExample);
        DbFaModelTemplate dbFaModelTemplate=CollectionUtils.isEmpty(dbFaModelTemplateList) ? null : dbFaModelTemplateList.get(0);
        return  dbFaModelTemplate;
    }

    public String  saveOrUpdateDbFaModelTemplate(DbFaModelTemplate faModelTemplate) throws Exception {
        DbFaModelTemplate dbFaModelTemplate;
        if (null != faModelTemplate.getId()) {
            dbFaModelTemplate = this.dbFaModelTemplateMapper.selectByPrimaryKey(faModelTemplate.getId());
            Assert.notNull(dbFaModelTemplate, MessageFormat.format("更新对象不存在！对象id为{0}", dbFaModelTemplate.getId()));
            BeanUtils.copyProperties(faModelTemplate, dbFaModelTemplate);
            dbFaModelTemplate.setModifyDate(new Date());
            this.dbFaModelTemplateMapper.updateByPrimaryKeySelective(dbFaModelTemplate);
        } else {
            dbFaModelTemplate = new DbFaModelTemplate();
            BeanUtils.copyProperties(faModelTemplate, dbFaModelTemplate);
            dbFaModelTemplate.setDelFlag(SysCode.DELETE_STATUS.NO);
            dbFaModelTemplate.setCreateDate(new Date());
            dbFaModelTemplate.setModifyDate(new Date());
            dbFaModelTemplate.setFaModelTemplateOid(UUIDUtil.randomUUID());
            this.dbFaModelTemplateMapper.insert(dbFaModelTemplate);
        }
        return  dbFaModelTemplate.getFaModelTemplateOid();
    }


    public String cloneFaModelTemplate(String id,String faModelTemplateOid) throws Exception{
        DbFaModelTemplate t=this.getDbFaModelTemplateByOid(faModelTemplateOid);
        DbMaterialCatalog cata=this.materialCatalogManager.getMaterialCatalogOid(t.getMaterialCatalogOid());
        t.setId(null);
        t.setVersion(null);
        t.setCreateDate(new Date());
        String templateCode=this.createTemplateCode(cata.getCatalogCode());
        t.setTemplateCode(templateCode);
        t.setTemplateName(cata.getCatalogName() + DateUtil.format(new Date(), "yyyyMMddHHmm"));
        t.setTemplateStatus(SysCode.ABLE_STATUS.ZC);
        t.setFeatureBlockImgPath(null);
        String newFaModelTemplateOid=this.saveOrUpdateDbFaModelTemplate(t);

        //faModelTemplateBlockManager
        List<FaModelTemplateBlock> l =this.faModelTemplateBlockManager.queryFaModelTemplateBlockListByTemplateOid(faModelTemplateOid);
        for (FaModelTemplateBlock b : l) {
            b.setFaModelTemplateOid(newFaModelTemplateOid);
            b.setId(null);
            b.setCreateDate(new Date());
            b.setModifyDate(new Date());
            this.faModelTemplateBlockManager.saveOrUpdate(b);
        }
        return null;
    }


    /**
     * 生成模板编号
     * 生成规则：材料目录编码（9位）+ 模板版本号（2位）
     */
    public String createTemplateCode(String cataCode) throws Exception{
        Long ret=countByCataCode(cataCode);
        ret += 1;
        int smallCataCodeLen = 9;
        if(cataCode.length() < smallCataCodeLen) {
            cataCode += "01";
        }
        cataCode = cataCode + "00";
        cataCode = cataCode.substring(0,cataCode.length() - (ret + "").length());
        return cataCode + ret;
    }

    /**
     * 版本号生成规则
     * 初始版本V1.00
     * 更换底图-大的版本号加1 如：V2.00
     * 普通修改-小版本号加1 如：V2.01
     */
    public String createTemplateVersion(String cataCode,String baseImgFileOid) throws Exception{
        DbFaModelTemplate t = getMaxVersionByCataCode(cataCode);
        String version = "V1.00";
        if(t != null){
            version = t.getVersion();
            if(null!=t.getBaseImgFileOid()
                    && !t.getBaseImgFileOid().equals(baseImgFileOid)){
                String bigVer = version.substring(1,3);
                if(version.length() == 6){
                    bigVer = version.substring(1,3);
                }else{
                    bigVer = version.substring(1,2);
                }
                int bv = Integer.parseInt(bigVer) + 1;
                version = "V" + bv + ".00";
            }else{
                String smallVer = null;
                if(version.length() == 6){
                    smallVer = version.substring(4);
                }else{
                    smallVer = version.substring(3);
                }

                int sv = Integer.parseInt(smallVer);
                sv += 1;
                version = version.substring(0,version.length() - (sv + "").length()) + sv;
            }
        }
        return version;
    }


    public FaModelTemplate queryLicenseResultTemplateByOid(String materialCatalogOid) {
        DbFaModelTemplateExample dbFaModelTemplateExample=new DbFaModelTemplateExample();
        DbFaModelTemplateExample.Criteria criteria=dbFaModelTemplateExample.createCriteria();
        if(null!=materialCatalogOid){
            criteria.andMaterialCatalogOidEqualTo(materialCatalogOid);
        }
        criteria.andDelFlagEqualTo(SysCode.DELETE_STATUS.NO);
        criteria.andTemplateStatusEqualTo(2);
        List<DbFaModelTemplate>  dbFaModelTemplateList=this.dbFaModelTemplateMapper.selectByExample(dbFaModelTemplateExample);
        DbFaModelTemplate dbFaModelTemplate=CollectionUtils.isEmpty(dbFaModelTemplateList) ? null : dbFaModelTemplateList.get(0);
        FaModelTemplate  faModelTemplate = null;
        if(null!=dbFaModelTemplate){
            faModelTemplate=new FaModelTemplate();
            BeanUtils.copyProperties(dbFaModelTemplate, faModelTemplate);
        }
        return  faModelTemplate;
    }
}
