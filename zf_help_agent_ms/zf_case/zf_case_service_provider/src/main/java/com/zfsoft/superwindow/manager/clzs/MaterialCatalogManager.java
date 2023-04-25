package com.zfsoft.superwindow.manager.clzs;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.superwindow.data.clzs.FaModelTemplate;
import com.zfsoft.superwindow.data.clzs.MaterialCatalog;
import com.zfsoft.superwindow.dbaccess.dao.DbCardCatalogueMapper;
import com.zfsoft.superwindow.dbaccess.dao.DbMaterialCatalogMapper;
import com.zfsoft.superwindow.dbaccess.data.*;
import com.zfsoft.superwindow.util.BeanUtils;
import com.zfsoft.superwindow.util.SysCode;
import com.zfsoft.superwindow.util.UUIDUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


/**
 * @ClassName MaterialCatalogManager
 * @Description: 材料目录实现类
 * @Author liangss
 * @Date 2020-11-03 15:46:29
 **/
@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MaterialCatalogManager {
    @Resource
    private DbMaterialCatalogMapper dbMaterialCatalogMapper;
    @Resource
    private MaterialCategoryManager materialCategoryManager;

    @Resource
    private FaModelTemplateManager faModelTemplateManager;

    @Resource
    private DbCardCatalogueMapper dbCardCatalogueMapper;

    /**
     * 分页查询列表
     * @param materialCatalog
     * @param pageNumber
     * @param pageSize
     * @return
     */
    public PageResult<MaterialCatalog> queryMaterialCatalogWithPage
            (MaterialCatalog materialCatalog, Integer pageNumber, Integer pageSize) {
        //分页参数
        if (null == pageNumber || pageNumber <= 0) {
            pageNumber = 0;
        }
        if (null == pageSize || pageSize <= 0) {
            pageSize = 10;
        }
        PageHelper.startPage(pageNumber,pageSize);
        DbMaterialCatalogExample dbMaterialCatalogExample=new DbMaterialCatalogExample();
        dbMaterialCatalogExample.setOrderByClause(" MODIFY_DATE DESC ");
        DbMaterialCatalogExample.Criteria criteria=dbMaterialCatalogExample.createCriteria();
        if(null!=materialCatalog){
            if(null!=materialCatalog.getCatalogName()){
                criteria.andCatalogNameLike("%"+materialCatalog.getCatalogName().trim()+"%");
            }
            if(null!=materialCatalog.getCatalogCode()){
                criteria.andCatalogCodeLike("%"+materialCatalog.getCatalogCode().trim()+"%");
            }
            if(null!=materialCatalog.getMaterialParentOid()){
                criteria.andMaterialParentOidLike(materialCatalog.getMaterialParentOid());
            }
        }
        criteria.andMaterialParentOidIsNull();
        criteria.andCatalogNameIsNotNull();
        criteria.andDelFlagEqualTo(SysCode.DELETE_STATUS.NO);
        Page<DbMaterialCatalog> dbMaterialCatalogs = (Page<DbMaterialCatalog>)dbMaterialCatalogMapper.selectByExample(dbMaterialCatalogExample);
        PageResult<MaterialCatalog> pageResult = new PageResult<>(dbMaterialCatalogs.getPageNum(),dbMaterialCatalogs.getPageSize(),dbMaterialCatalogs.getTotal());
        List<MaterialCatalog> materialCatalogList = dbMaterialCatalogs.stream().map(dbMaterialCatalog -> {
            MaterialCatalog materialCategory1 = new MaterialCatalog();
            BeanUtils.copyProperties(dbMaterialCatalog,materialCategory1);
            //查询子项
            List<MaterialCatalog> subList=this.queryList(materialCategory1.getMaterialCatalogOid());
            materialCategory1.setSubList(subList);
            return materialCategory1;
        }).collect(Collectors.toList());
        pageResult.setData(materialCatalogList);
        return pageResult;
    }


    public PageResult<MaterialCatalog> queryFaModelMaterialCatalogPaginationForTemplateSet
            (MaterialCatalog materialCatalog, Integer pageNumber, Integer pageSize) {
        //分页参数
        if (null == pageNumber || pageNumber <= 0) {
            pageNumber = 0;
        }
        if (null == pageSize || pageSize <= 0) {
            pageSize = 10;
        }
        PageHelper.startPage(pageNumber,pageSize);
        DbMaterialCatalogExample dbMaterialCatalogExample=new DbMaterialCatalogExample();
        dbMaterialCatalogExample.setOrderByClause(" CREATE_DATE DESC ");
        DbMaterialCatalogExample.Criteria criteria=dbMaterialCatalogExample.createCriteria();
        if(null!=materialCatalog){
            if(null!=materialCatalog.getCatalogName()){
                criteria.andCatalogNameLike("%"+materialCatalog.getCatalogName().trim()+"%");
            }
            if(null!=materialCatalog.getCatalogCode()){
                criteria.andCatalogCodeLike("%"+materialCatalog.getCatalogCode().trim()+"%");
            }
            if(null!=materialCatalog.getMaterialParentOid()){
                criteria.andMaterialParentOidLike(materialCatalog.getMaterialParentOid());
            }else{
               /* criteria.andMaterialParentOidInMaterialParent();*/
            }
        }
        criteria.andMaterialParentOidIsNotNull();
        criteria.andCatalogNameIsNotNull();
        criteria.andDelFlagEqualTo(SysCode.DELETE_STATUS.NO);
        Page<DbMaterialCatalog> dbMaterialCatalogs = (Page<DbMaterialCatalog>)dbMaterialCatalogMapper.selectByExample(dbMaterialCatalogExample);
        PageResult<MaterialCatalog> pageResult = new PageResult<>(dbMaterialCatalogs.getPageNum(),dbMaterialCatalogs.getPageSize(),dbMaterialCatalogs.getTotal());
        List<MaterialCatalog> materialCatalogList = dbMaterialCatalogs.stream().map(dbMaterialCatalog -> {
            MaterialCatalog materialCategory1 = new MaterialCatalog();
            BeanUtils.copyProperties(dbMaterialCatalog,materialCategory1);

            if(null!=materialCategory1.getMaterialParentOid()){
                DbMaterialCatalog mc=this.getMaterialCatalogOid(materialCategory1.getMaterialParentOid());
                materialCategory1.setBigCataName(mc.getCatalogName());
                materialCategory1.setBigCataCode(mc.getCatalogCode());
            }else{
                materialCategory1.setBigCataName(materialCategory1.getCatalogName());
                materialCategory1.setBigCataCode(materialCategory1.getCatalogCode());
            }
            FaModelTemplate fmt=new FaModelTemplate();
            fmt.setMaterialCatalogOid(materialCategory1.getMaterialCatalogOid());
            DbFaModelTemplate dbFaModelTemplate=this.faModelTemplateManager.getFaModelTemplateByFaModelTemplate(fmt);
            if (dbFaModelTemplate != null) {
                materialCategory1.setStateDesc("已发布");
            } else {
                materialCategory1.setStateDesc("未发布");
            }
            return materialCategory1;
        }).collect(Collectors.toList());
        pageResult.setData(materialCatalogList);
        return pageResult;
    }
    public List<MaterialCatalog> queryListMaterialCatalog(MaterialCatalog materialCatalog, Integer pageNumber, Integer pageSize) {
        DbMaterialCatalogExample dbMaterialCatalogExample=new DbMaterialCatalogExample();
        DbMaterialCatalogExample.Criteria criteria=dbMaterialCatalogExample.createCriteria();
        if(null!=materialCatalog){
            if(null!=materialCatalog.getCatalogName()){
                criteria.andCatalogNameLike("%"+materialCatalog.getCatalogName().trim()+"%");
            }
            if(null!=materialCatalog.getCatalogCode()){
                criteria.andCatalogCodeLike("%"+materialCatalog.getCatalogCode().trim()+"%");
            }
            if(null!=materialCatalog.getMaterialParentOid()){
                criteria.andMaterialParentOidLike(materialCatalog.getMaterialParentOid());
            }
        }
        criteria.andCatalogNameIsNotNull();
        criteria.andMaterialParentOidIsNotNull();
        criteria.andDelFlagEqualTo(SysCode.DELETE_STATUS.NO);
        List<DbMaterialCatalog>  dbMaterialCatalogList=this.dbMaterialCatalogMapper.selectByExample(dbMaterialCatalogExample);
        return BeanUtils.copyListProperties(dbMaterialCatalogList, MaterialCatalog::new);
    }

    /**
     * 根据材料类别查询材料目录的数量
     * @param materialCategoryOid
     * @return
     */
    public Long queryMaterialCatalogCountBymaterialCategoryOid(String materialCategoryOid) {
        DbMaterialCatalogExample dbMaterialCatalogExample=new DbMaterialCatalogExample();
        dbMaterialCatalogExample.createCriteria()
                .andDelFlagEqualTo(SysCode.DELETE_STATUS.NO)
                .andCatalogNameIsNotNull()
                .andMaterialCategoryOidEqualTo(materialCategoryOid);
        return Long.valueOf(this.dbMaterialCatalogMapper.countByExample(dbMaterialCatalogExample));
    }

    /**
     * 根据父级id查询子项
     * @param materialParentOid
     * @return
     */
    public List<MaterialCatalog> queryList(String materialParentOid) {
        DbMaterialCatalogExample dbMaterialCatalogExample=new DbMaterialCatalogExample();
        dbMaterialCatalogExample.createCriteria()
                .andDelFlagEqualTo(SysCode.DELETE_STATUS.NO)
                .andCatalogNameIsNotNull()
                .andMaterialParentOidEqualTo(materialParentOid);
        List<DbMaterialCatalog>  dbMaterialCatalogList=this.dbMaterialCatalogMapper.selectByExample(dbMaterialCatalogExample);
        return BeanUtils.copyListProperties(dbMaterialCatalogList, MaterialCatalog::new);
    }


    /**
     * 根据父级oid查询列表
     * @param materialParentOid
     * @return
     */
    public List<DbMaterialCatalog> queryDbMaterialCatalogList(String materialParentOid) {
        DbMaterialCatalogExample dbMaterialCatalogExample=new DbMaterialCatalogExample();
        dbMaterialCatalogExample.createCriteria()
                .andDelFlagEqualTo(SysCode.DELETE_STATUS.NO)
                .andCatalogNameIsNotNull()
                .andMaterialCatalogOidEqualTo(materialParentOid);
        List<DbMaterialCatalog>  dbMaterialCatalogList=this.dbMaterialCatalogMapper.selectByExample(dbMaterialCatalogExample);
        return dbMaterialCatalogList;
    }

    /**
     * 查询列表
     * @param materialCatalog
     * @return
     */
    public List<MaterialCatalog> queryList(MaterialCatalog materialCatalog) {
        DbMaterialCatalogExample dbMaterialCatalogExample=new DbMaterialCatalogExample();
        DbMaterialCatalogExample.Criteria criteria=dbMaterialCatalogExample.createCriteria();
        if(null!=materialCatalog){
            if(null!=materialCatalog.getCatalogName()){
                criteria.andCatalogNameLike("%"+materialCatalog.getCatalogName().trim()+"%");
            }
            if(null!=materialCatalog.getMaterialParentOid()){
                criteria.andMaterialParentOidEqualTo(materialCatalog.getMaterialParentOid());
            }
        }
        criteria.andDelFlagEqualTo(SysCode.DELETE_STATUS.NO);
        criteria.andCatalogNameIsNotNull();
        List<DbMaterialCatalog>  dbMaterialCatalogList=this.dbMaterialCatalogMapper.selectByExample(dbMaterialCatalogExample);
        return BeanUtils.copyListProperties(dbMaterialCatalogList, MaterialCatalog::new);
    }

    /**
     * 保存更新信息
     * @param materialCatalog
     */
    public void saveOrUpdate(MaterialCatalog materialCatalog) {
        if (null!=materialCatalog.getId()) {
            DbMaterialCatalog dbMaterialCatalog=this.dbMaterialCatalogMapper.selectByPrimaryKey(materialCatalog.getId());
            Assert.notNull(dbMaterialCatalog, MessageFormat.format("更新对象不存在！对象id为{0}", dbMaterialCatalog.getId()));
            BeanUtils.copyProperties(materialCatalog, dbMaterialCatalog);
            dbMaterialCatalog.setModifyDate(new Date());
            this.dbMaterialCatalogMapper.updateByPrimaryKeySelective(dbMaterialCatalog);
        } else {
            DbMaterialCatalog dbMaterialCatalog=new DbMaterialCatalog();
            BeanUtils.copyProperties(materialCatalog, dbMaterialCatalog);
            dbMaterialCatalog.setDelFlag(SysCode.DELETE_STATUS.NO);
            dbMaterialCatalog.setCreateDate(new Date());
            dbMaterialCatalog.setModifyDate(new Date());
            dbMaterialCatalog.setMaterialCategoryOid(UUIDUtil.randomUUID());
            this.dbMaterialCatalogMapper.insert(dbMaterialCatalog);
        }
    }
    public void saveOrUpdateMaterialCatalogAndSubitem(MaterialCatalog materialCatalog) throws Exception {
        DbMaterialCatalog dbMaterialCatalog;
        if (null!=materialCatalog.getId()) {
            dbMaterialCatalog =this.dbMaterialCatalogMapper.selectByPrimaryKey(materialCatalog.getId());
            Assert.notNull(dbMaterialCatalog, MessageFormat.format("更新对象不存在！对象id为{0}", dbMaterialCatalog.getId()));
            BeanUtils.copyProperties(materialCatalog, dbMaterialCatalog);
            dbMaterialCatalog.setModifyDate(new Date());
            this.dbMaterialCatalogMapper.updateByPrimaryKeySelective(dbMaterialCatalog);
        } else {
             dbMaterialCatalog=new DbMaterialCatalog();
            //生成编码
            createCataCode(materialCatalog,null,null);
            BeanUtils.copyProperties(materialCatalog, dbMaterialCatalog);
            dbMaterialCatalog.setDelFlag(SysCode.DELETE_STATUS.NO);
            dbMaterialCatalog.setCreateDate(new Date());
            dbMaterialCatalog.setModifyDate(new Date());
            dbMaterialCatalog.setMaterialCatalogOid(UUIDUtil.randomUUID());
            this.dbMaterialCatalogMapper.insert(dbMaterialCatalog);
        }
      List<MaterialCatalog> subList=materialCatalog.getSubList();
        for (MaterialCatalog mal:subList) {
            if (null!=mal.getId()) {
                DbMaterialCatalog dbMaterialCatalogChildren=this.dbMaterialCatalogMapper.selectByPrimaryKey(mal.getId());
                Assert.notNull(dbMaterialCatalogChildren, MessageFormat.format("更新对象不存在！对象id为{0}", mal.getId()));
                BeanUtils.copyProperties(mal, dbMaterialCatalogChildren);
                dbMaterialCatalogChildren.setModifyDate(new Date());
                String materialIdentificationTypeOid=mal.getMaterialIdentificationTypeOid();
                String materialIdentificationType="";
                String cardCatalogueType="";
                if(StringUtils.isNotEmpty(materialIdentificationTypeOid)){
                    DbCardCatalogue dbCardCatalogue = dbCardCatalogueMapper.getCardCatalogueByOid(materialIdentificationTypeOid);
                    if(null!=dbCardCatalogue){
                        materialIdentificationType= dbCardCatalogue.getCardCatalogueCode();
                        cardCatalogueType=dbCardCatalogue.getType();
                    }
                }
                dbMaterialCatalogChildren.setCardCatalogueType(cardCatalogueType);
                dbMaterialCatalogChildren.setMaterialIdentificationType(materialIdentificationType);
                this.dbMaterialCatalogMapper.updateByPrimaryKeySelective(dbMaterialCatalogChildren);
            } else {
                DbMaterialCatalog dbMaterialCatalogChildren=new DbMaterialCatalog();
                //生成编码
                createCataCode(mal,dbMaterialCatalog.getCatalogCode(),dbMaterialCatalog.getMaterialCatalogOid());
                BeanUtils.copyProperties(mal, dbMaterialCatalogChildren);
                dbMaterialCatalogChildren.setDelFlag(SysCode.DELETE_STATUS.NO);
                dbMaterialCatalogChildren.setCreateDate(new Date());
                dbMaterialCatalogChildren.setModifyDate(new Date());
                dbMaterialCatalogChildren.setMaterialCatalogOid(UUIDUtil.randomUUID());
                dbMaterialCatalogChildren.setMaterialCategoryOid(dbMaterialCatalog.getMaterialCategoryOid());
                dbMaterialCatalogChildren.setMaterialCategoryName(dbMaterialCatalog.getMaterialCategoryName());
                dbMaterialCatalogChildren.setMaterialParentOid(dbMaterialCatalog.getMaterialCatalogOid());
                String materialIdentificationTypeOid=mal.getMaterialIdentificationTypeOid();
                String materialIdentificationType="";
                String cardCatalogueType="";
                if(StringUtils.isNotEmpty(materialIdentificationTypeOid)){
                    DbCardCatalogue dbCardCatalogue = dbCardCatalogueMapper.getCardCatalogueByOid(materialIdentificationTypeOid);
                    if(null!=dbCardCatalogue){
                        materialIdentificationType= dbCardCatalogue.getCardCatalogueCode();
                        cardCatalogueType=dbCardCatalogue.getType();
                    }
                }
                dbMaterialCatalogChildren.setCardCatalogueType(cardCatalogueType);
                dbMaterialCatalogChildren.setMaterialIdentificationType(materialIdentificationType);
                this.dbMaterialCatalogMapper.insert(dbMaterialCatalogChildren);
            }

        }

    }


    //生成编码/* String parentOid = materialCatalog.getMaterialParentOid();*/
    private void createCataCode(MaterialCatalog materialCatalog,String parentCode,String parentOid) throws Exception {
        MaterialCatalog queryObj=new MaterialCatalog();
        if(null!=parentOid){//子项
            String parentCataCode = parentCode;
            queryObj.setMaterialParentOid(parentOid);
            Long num=queryMaterialCategoryCount(queryObj);
            String cataCode = "00" + (num + 1);
            cataCode = parentCataCode + cataCode.substring(cataCode.length() - 2);
            materialCatalog.setCatalogCode(cataCode);
        }else{
            //所属类别oid
            String typeOid =materialCatalog.getMaterialCategoryOid();
            queryObj.setMaterialCategoryOid(typeOid);
            DbMaterialCategory type=this.materialCategoryManager.getMaterialCategoryOid(typeOid);
            Long num=queryMaterialCategoryCount(queryObj);
            String cataCode = "00000" + (num + 1);
            cataCode = type.getCategoryCode() + cataCode.substring(cataCode.length() - 5);
            materialCatalog.setCatalogCode(cataCode);
        }
    }


    public Long queryMaterialCategoryCount(MaterialCatalog materialCatalog) {
        DbMaterialCatalogExample dbMaterialCatalogExample=new DbMaterialCatalogExample();
        DbMaterialCatalogExample.Criteria criteria=dbMaterialCatalogExample.createCriteria();
        if(null!=materialCatalog){
            if(null!=materialCatalog.getMaterialCategoryOid()){
                criteria.andMaterialCategoryOidEqualTo(materialCatalog.getMaterialCategoryOid());
            }
            if(null!=materialCatalog.getCatalogName()){
                criteria.andCatalogNameLike("%"+materialCatalog.getCatalogName().trim()+"%");
            }
            if(null!=materialCatalog.getMaterialParentOid()){
                criteria.andMaterialParentOidEqualTo(materialCatalog.getMaterialParentOid());
            }
        }
        criteria.andCatalogNameIsNotNull();
        criteria.andDelFlagEqualTo(SysCode.DELETE_STATUS.NO);

        return Long.valueOf(this.dbMaterialCatalogMapper.countByExample(dbMaterialCatalogExample));
    }


    /**
     * 根据主键查询信息
     * @param id
     * @return
     */
    public MaterialCatalog getOne(String id) {
        Assert.hasLength(id, "主键不能为空！");
        DbMaterialCatalog dbMaterialCatalog=this.dbMaterialCatalogMapper.selectByPrimaryKey(Long.valueOf(id));
        MaterialCatalog materialCatalog=new MaterialCatalog();
        BeanUtils.copyProperties(dbMaterialCatalog,materialCatalog);
        return materialCatalog;
    }

    /**
     * 根据业务oid查询材料目录信息
     * @param materialCatalogOid
     * @return
     */
    public DbMaterialCatalog getMaterialCatalogOid(String materialCatalogOid) {
        Assert.hasLength(materialCatalogOid, "业务主键不能为空！");
        DbMaterialCatalogExample dbMaterialCatalogExample=new DbMaterialCatalogExample();
        DbMaterialCatalogExample.Criteria criteria=dbMaterialCatalogExample.createCriteria();
        criteria.andMaterialCatalogOidEqualTo(materialCatalogOid);
        criteria.andDelFlagEqualTo(SysCode.DELETE_STATUS.NO);
        List<DbMaterialCatalog>  dbMaterialCatalogList=this.dbMaterialCatalogMapper.selectByExample(dbMaterialCatalogExample);
        return CollectionUtils.isEmpty(dbMaterialCatalogList) ? null : dbMaterialCatalogList.get(0);

    }

    /**
     * 根据主键查询目录以及下级目录
     * @param id
     * @return
     */
    public MaterialCatalog getMaterialCatalogAndSubitem(String id) {
        Assert.hasLength(id, "主键不能为空！");
        DbMaterialCatalog dbMaterialCatalog=this.dbMaterialCatalogMapper.selectByPrimaryKey(Long.valueOf(id));
        log.info("dbMaterialCatalog详情获取成功：{}", JSON.toJSONString(dbMaterialCatalog));
        MaterialCatalog materialCategory1 = new MaterialCatalog();
        BeanUtils.copyProperties(dbMaterialCatalog,materialCategory1);
        log.info("materialCatalog详情获取成功：{}", JSON.toJSONString(materialCategory1));
        //查询子项
        List<MaterialCatalog> subList=this.queryList(materialCategory1.getMaterialCatalogOid());
        materialCategory1.setSubList(subList);
        return materialCategory1;
    }

    /**
     * 根据主键删除登记信息
     * @param id
     */
    public void del(String id) {
        Assert.hasLength(id, "主键不能为空！");
        DbMaterialCatalog dbMaterialCatalog=this.dbMaterialCatalogMapper.selectByPrimaryKey(Long.valueOf(id));
        Assert.notNull(dbMaterialCatalog, MessageFormat.format("操作对象不存在！对象id为{0}", id));
        dbMaterialCatalog.setDelFlag(dbMaterialCatalog.getDelFlag().equals(SysCode.DELETE_STATUS.YES) ? SysCode.DELETE_STATUS.NO : SysCode.DELETE_STATUS.YES);
        this.dbMaterialCatalogMapper.updateByPrimaryKeySelective(dbMaterialCatalog);
        //查询子项删除子项信息
        List<DbMaterialCatalog> subList=this.queryDbMaterialCatalogList(id);
        for (DbMaterialCatalog dbmcl:subList
             ) {
            dbmcl.setDelFlag(SysCode.DELETE_STATUS.YES);
            this.dbMaterialCatalogMapper.updateByPrimaryKeySelective(dbmcl);

        }
    }


    /**
     * Title: 查询所有一级目录
     * Description: TODO
     *
     * @param
     * @return
     * @author chenjm
     * @date 2020-11-10 9:04
     */
    public List<MaterialCatalog> queryListNoParent() {
        DbMaterialCatalogExample dbMaterialCatalogExample=new DbMaterialCatalogExample();
        dbMaterialCatalogExample.createCriteria()
                .andDelFlagEqualTo(SysCode.DELETE_STATUS.NO)
                .andCatalogNameIsNotNull()
                .andMaterialParentOidIsNull();
        List<DbMaterialCatalog>  dbMaterialCatalogList=this.dbMaterialCatalogMapper.selectByExample(dbMaterialCatalogExample);
        return BeanUtils.copyListProperties(dbMaterialCatalogList, MaterialCatalog::new);
    }



    public List<MaterialCatalog> queryCatalogListByCataOids(String bigCataOid, String smallCataOid) {
        DbMaterialCatalogExample dbMaterialCatalogExample=new DbMaterialCatalogExample();
        DbMaterialCatalogExample.Criteria criteria=dbMaterialCatalogExample.createCriteria();
            if(StringUtils.isNotEmpty(bigCataOid)){
                criteria.andMaterialParentOidEqualTo(bigCataOid);
            }
            if(StringUtils.isNotEmpty(smallCataOid)){
                criteria.andMaterialCatalogOidNotEqualTo(smallCataOid);
            }
        criteria.andDelFlagEqualTo(SysCode.DELETE_STATUS.NO);
        criteria.andCatalogNameIsNotNull();
        List<DbMaterialCatalog>  dbMaterialCatalogList=this.dbMaterialCatalogMapper.selectByExample(dbMaterialCatalogExample);
        return BeanUtils.copyListProperties(dbMaterialCatalogList, MaterialCatalog::new);
    }

    /**
     * 根据业务主键查询目录
     * @param materialCatalogOid
     * @return
     */
    public MaterialCatalog getMaterialCatalogByOid(String materialCatalogOid) {
        DbMaterialCatalog dbMaterialCatalog=this.dbMaterialCatalogMapper.getMaterialCatalogByOid(materialCatalogOid);
        MaterialCatalog materialCatalog=new MaterialCatalog();
        if(null != dbMaterialCatalog) {
            BeanUtils.copyProperties(dbMaterialCatalog,materialCatalog);
        }
        return materialCatalog;

    }

    /**
     * @description 根据名称精确查询
     * @param materialCatalog
     * @return java.util.List<com.zfsoft.data.clzs.MaterialCatalog>
     * @author chenjm
     * @date 2021/4/7 14:51
     **/
    public List<MaterialCatalog> queryListByName(MaterialCatalog materialCatalog) {
        DbMaterialCatalogExample dbMaterialCatalogExample=new DbMaterialCatalogExample();
        DbMaterialCatalogExample.Criteria criteria=dbMaterialCatalogExample.createCriteria();
        if(null!=materialCatalog){
            if(null!=materialCatalog.getCatalogName()){
                criteria.andCatalogNameEqualTo(materialCatalog.getCatalogName().trim());
            }
            if(null!=materialCatalog.getMaterialParentOid()){
                criteria.andMaterialParentOidEqualTo(materialCatalog.getMaterialParentOid());
            }
        }
        criteria.andDelFlagEqualTo(SysCode.DELETE_STATUS.NO);
        criteria.andCatalogNameIsNotNull();
        List<DbMaterialCatalog>  dbMaterialCatalogList=this.dbMaterialCatalogMapper.selectByExample(dbMaterialCatalogExample);
        return BeanUtils.copyListProperties(dbMaterialCatalogList, MaterialCatalog::new);
    }


    /**
     * 查询二级列表
     * @return
     */
    public List<MaterialCatalog> querySubitemMaterialCatalogList(){
        List<DbMaterialCatalog>  dbMaterialCatalogList=this.dbMaterialCatalogMapper.querySubitemMaterialCatalogList();
        List<MaterialCatalog> materialCatalogList = new ArrayList<>();
        materialCatalogList = dbMaterialCatalogList.stream().map(dbMaterialCatalog -> {
            String materialParentOid=dbMaterialCatalog.getMaterialCategoryName();
            DbMaterialCatalog dbParentMaterialCatalog=this.dbMaterialCatalogMapper.getMaterialCatalogByOid(materialParentOid);
            MaterialCatalog materialCatalog = new MaterialCatalog();
            BeanUtils.copyProperties(dbMaterialCatalog, materialCatalog);
            if(null!=dbParentMaterialCatalog){
                materialCatalog.setBigCataCode(dbParentMaterialCatalog.getCatalogCode());
                materialCatalog.setBigCataName(dbParentMaterialCatalog.getCatalogName());
            }
            return materialCatalog;
        }).collect(Collectors.toList());
        return materialCatalogList;
    }



    public List<MaterialCatalog> queryAllCatalogList(){
        List<DbMaterialCatalog>  dbMaterialCatalogList=this.dbMaterialCatalogMapper.queryAllCatalogList();
        List<MaterialCatalog> materialCatalogList = new ArrayList<>();
        materialCatalogList = dbMaterialCatalogList.stream().map(dbMaterialCatalog -> {
            MaterialCatalog materialCatalog = new MaterialCatalog();
            BeanUtils.copyProperties(dbMaterialCatalog, materialCatalog);
           /* String materialParentOid=dbMaterialCatalog.getMaterialCategoryName();
            DbMaterialCatalog dbParentMaterialCatalog=this.dbMaterialCatalogMapper.getMaterialCatalogByOid(materialParentOid);
            if(null!=dbParentMaterialCatalog){
                materialCatalog.setBigCataCode(dbParentMaterialCatalog.getCatalogCode());
                materialCatalog.setBigCataName(dbParentMaterialCatalog.getCatalogName());
            }*/
            return materialCatalog;
        }).collect(Collectors.toList());
        return materialCatalogList;
    }



    public List<MaterialCatalog> queryParentListByName(MaterialCatalog materialCatalog) {
        DbMaterialCatalogExample dbMaterialCatalogExample=new DbMaterialCatalogExample();
        DbMaterialCatalogExample.Criteria criteria=dbMaterialCatalogExample.createCriteria();
        if(null!=materialCatalog){
            if(null!=materialCatalog.getCatalogName()){
                criteria.andCatalogNameEqualTo(materialCatalog.getCatalogName().trim());
            }
            if(null!=materialCatalog.getMaterialParentOid()){
                criteria.andMaterialParentOidEqualTo(materialCatalog.getMaterialParentOid());
            }
        }
        criteria.andMaterialParentOidIsNull();
        criteria.andDelFlagEqualTo(SysCode.DELETE_STATUS.NO);
        criteria.andCatalogNameIsNotNull();
        List<DbMaterialCatalog>  dbMaterialCatalogList=this.dbMaterialCatalogMapper.selectByExample(dbMaterialCatalogExample);
        return BeanUtils.copyListProperties(dbMaterialCatalogList, MaterialCatalog::new);
    }

    public List<MaterialCatalog> queryAllCatalogByCatalogOidsList(List<String> materialCatalogOids){
        DbMaterialCatalogExample dbMaterialCatalogExample=new DbMaterialCatalogExample();
        DbMaterialCatalogExample.Criteria criteria=dbMaterialCatalogExample.createCriteria();
        if(materialCatalogOids!=null && materialCatalogOids.size()>0){
            criteria.andMaterialCatalogOidIn(materialCatalogOids);
        }
        criteria.andDelFlagEqualTo(SysCode.DELETE_STATUS.NO);
        List<DbMaterialCatalog>  dbMaterialCatalogList=this.dbMaterialCatalogMapper.selectByExample(dbMaterialCatalogExample);
        return BeanUtils.copyListProperties(dbMaterialCatalogList, MaterialCatalog::new);
    }




}
