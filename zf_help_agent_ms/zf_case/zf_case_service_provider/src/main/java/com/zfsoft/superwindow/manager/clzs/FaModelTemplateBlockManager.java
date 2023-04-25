package com.zfsoft.superwindow.manager.clzs;


import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.superwindow.data.clzs.FaModelTemplateBlock;
import com.zfsoft.superwindow.dbaccess.dao.DbFaModelTemplateBlockMapper;
import com.zfsoft.superwindow.dbaccess.data.DbFaModelTemplateBlock;
import com.zfsoft.superwindow.dbaccess.data.DbFaModelTemplateBlockExample;
import com.zfsoft.superwindow.util.*;
import com.zfsoft.superwindow.util.fa.PictureCutRuleNew;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.text.MessageFormat;
import java.util.*;
import java.util.stream.Collectors;


/**
 * @ClassName FaModelTemplateBlockManager
 * @Description: 识别模板区块实现类
 * @Author liangss
 * @Date 2020-11-07 16:55:29
 **/
@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@SpringBootApplication
@ComponentScan(basePackages = {"com.zfsoft.rest.service.ocr"})
public class FaModelTemplateBlockManager {

    @Resource
    private DbFaModelTemplateBlockMapper dbFaModelTemplateBlockMapper;

    //材料目录
    @Resource
    private  MaterialCatalogManager materialCatalogManager;
    //模板
    @Resource
    private  FaModelTemplateManager faModelTemplateManager;

    /**
     * 分页查询列表
     * @param faModelTemplateBlock
     * @param pageNumber
     * @param pageSize
     * @return
     */
    public PageResult<FaModelTemplateBlock> queryFaModelTemplateBlockWithPage
    (FaModelTemplateBlock faModelTemplateBlock, Integer pageNumber, Integer pageSize) {
        //分页参数
        if (null == pageNumber || pageNumber <= 0) {
            pageNumber = 0;
        }
        if (null == pageSize || pageSize <= 0) {
            pageSize = 10;
        }
        PageHelper.startPage(pageNumber,pageSize);
        DbFaModelTemplateBlockExample dbFaModelTemplateBlockExample=new DbFaModelTemplateBlockExample();
        DbFaModelTemplateBlockExample.Criteria criteria=dbFaModelTemplateBlockExample.createCriteria();
        if(null!=faModelTemplateBlock){
            if(null!=faModelTemplateBlock.getFaModelTemplateOid()){
                criteria.andFaModelTemplateOidEqualTo(faModelTemplateBlock.getFaModelTemplateOid());
            }
            if(null!=faModelTemplateBlock.getBlockCode()){
                criteria.andBlockCodeLike("%"+faModelTemplateBlock.getBlockCode().trim()+"%");
            }
            if(null!=faModelTemplateBlock.getBlockName()){
                criteria.andBlockNameLike("%"+faModelTemplateBlock.getBlockName().trim()+"%");
            }
        }
        criteria.andDelFlagEqualTo(SysCode.DELETE_STATUS.NO);
        Page<DbFaModelTemplateBlock> dbFaModelTemplateBlocks= (Page<DbFaModelTemplateBlock>)dbFaModelTemplateBlockMapper.selectByExample(dbFaModelTemplateBlockExample);
        PageResult<FaModelTemplateBlock> pageResult = new PageResult<>(dbFaModelTemplateBlocks.getPageNum(),dbFaModelTemplateBlocks.getPageSize(),dbFaModelTemplateBlocks.getTotal());
        List<FaModelTemplateBlock> faModelTemplateBlockList= dbFaModelTemplateBlocks.stream().map(dbFaModelTemplateBlock -> {
            FaModelTemplateBlock faModelTemplateBlock1 = new FaModelTemplateBlock();
            BeanUtils.copyProperties(dbFaModelTemplateBlock,faModelTemplateBlock1);
            return faModelTemplateBlock1;
        }).collect(Collectors.toList());
        pageResult.setData(faModelTemplateBlockList);
        return pageResult;
    }


    /**
     * 查询列表
     * @param faModelTemplateBlock
     * @return
     */
    public List<FaModelTemplateBlock> queryFaModelTemplateBlockList(FaModelTemplateBlock faModelTemplateBlock) {
        DbFaModelTemplateBlockExample dbFaModelTemplateBlockExample=new DbFaModelTemplateBlockExample();
        DbFaModelTemplateBlockExample.Criteria criteria=dbFaModelTemplateBlockExample.createCriteria();
        if(null!=faModelTemplateBlock){
            if(null!=faModelTemplateBlock.getFaModelTemplateOid()){
                criteria.andFaModelTemplateOidEqualTo(faModelTemplateBlock.getFaModelTemplateOid());
            }
            if(null!=faModelTemplateBlock.getMaterialCatalogElementOid()){
                criteria.andMaterialCatalogElementOidEqualTo(faModelTemplateBlock.getMaterialCatalogElementOid());
            }
            if(null!=faModelTemplateBlock.getBlockCode()){
                criteria.andBlockCodeLike("%"+faModelTemplateBlock.getBlockCode().trim()+"%");
            }
            if(null!=faModelTemplateBlock.getBlockName()){
                criteria.andBlockNameLike("%"+faModelTemplateBlock.getBlockName().trim()+"%");
            }
        }
        criteria.andDelFlagEqualTo(SysCode.DELETE_STATUS.NO);
        List<DbFaModelTemplateBlock>  dbFaModelTemplateBlockList=this.dbFaModelTemplateBlockMapper.selectByBlock(dbFaModelTemplateBlockExample);
        return BeanUtils.copyListProperties(dbFaModelTemplateBlockList, FaModelTemplateBlock::new);
    }


    /**
     * 根据模板id查询区块列表
     * @param templateOid
     * @return
     */
    public List<FaModelTemplateBlock> queryFaModelTemplateBlockListByTemplateOid(String templateOid) {
        DbFaModelTemplateBlockExample dbFaModelTemplateBlockExample=new DbFaModelTemplateBlockExample();
        DbFaModelTemplateBlockExample.Criteria criteria=dbFaModelTemplateBlockExample.createCriteria();
        if(StringUtils.isNotEmpty(templateOid)){
            criteria.andFaModelTemplateOidEqualTo(templateOid);
        }
        criteria.andDelFlagEqualTo(SysCode.DELETE_STATUS.NO);
        List<DbFaModelTemplateBlock>  dbFaModelTemplateBlockList=this.dbFaModelTemplateBlockMapper.selectByExample(dbFaModelTemplateBlockExample);
        return BeanUtils.copyListProperties(dbFaModelTemplateBlockList, FaModelTemplateBlock::new);
    }




    /**
     * 根据主键查询信息
     * @param id
     * @return
     */
    public FaModelTemplateBlock getOne(String id) {
        Assert.hasLength(id, "主键不能为空！");
        DbFaModelTemplateBlock dbFaModelTemplateBlock=this.dbFaModelTemplateBlockMapper.selectByPrimaryKey(Long.valueOf(id));
        FaModelTemplateBlock faModelTemplateBlock=new FaModelTemplateBlock();
        BeanUtils.copyProperties(dbFaModelTemplateBlock,faModelTemplateBlock);
        return faModelTemplateBlock;
    }


    /**
     * 根据条件对象查询
     * @param faModelTemplateBlock
     * @return
     */
    public DbFaModelTemplateBlock getFaModelTemplateBlockByFaModelTemplateBlock(FaModelTemplateBlock faModelTemplateBlock) {
        DbFaModelTemplateBlockExample dbFaModelTemplateBlockExample=new DbFaModelTemplateBlockExample();
        DbFaModelTemplateBlockExample.Criteria criteria=dbFaModelTemplateBlockExample.createCriteria();
        if(null!=faModelTemplateBlock){
            if(null!=faModelTemplateBlock.getFaModelTemplateOid()){
                criteria.andFaModelTemplateOidEqualTo(faModelTemplateBlock.getFaModelTemplateOid());
            }
            if(null!=faModelTemplateBlock.getBlockCode()){
                criteria.andBlockCodeLike("%"+faModelTemplateBlock.getBlockCode().trim()+"%");
            }
            if(null!=faModelTemplateBlock.getBlockName()){
                criteria.andBlockNameLike("%"+faModelTemplateBlock.getBlockName().trim()+"%");
            }
        }
        criteria.andDelFlagEqualTo(SysCode.DELETE_STATUS.NO);
        List<DbFaModelTemplateBlock>  dbFaModelTemplateBlockList=this.dbFaModelTemplateBlockMapper.selectByExample(dbFaModelTemplateBlockExample);
        return CollectionUtils.isEmpty(dbFaModelTemplateBlockList) ? null : dbFaModelTemplateBlockList.get(0);

    }

    public DbFaModelTemplateBlock getFaModelTemplateBlockByOid(String faModelTemplateBlockOid) {
        DbFaModelTemplateBlockExample dbFaModelTemplateBlockExample=new DbFaModelTemplateBlockExample();
        DbFaModelTemplateBlockExample.Criteria criteria=dbFaModelTemplateBlockExample.createCriteria();
        if(null!=faModelTemplateBlockOid){
            criteria.andFaModelTemplateBlockOidEqualTo(faModelTemplateBlockOid);
        }
        criteria.andDelFlagEqualTo(SysCode.DELETE_STATUS.NO);
        List<DbFaModelTemplateBlock>  dbFaModelTemplateBlockList=this.dbFaModelTemplateBlockMapper.selectByExample(dbFaModelTemplateBlockExample);
        return CollectionUtils.isEmpty(dbFaModelTemplateBlockList) ? null : dbFaModelTemplateBlockList.get(0);

    }

    /**
     * 删除信息
     * @param id
     */
    public void del(String id) {
        Assert.hasLength(id, "主键不能为空！");
        DbFaModelTemplateBlock dbFaModelTemplateBlock=this.dbFaModelTemplateBlockMapper.selectByPrimaryKey(Long.valueOf(id));
        Assert.notNull(dbFaModelTemplateBlock, MessageFormat.format("操作对象不存在！对象id为{0}", id));
        dbFaModelTemplateBlock.setDelFlag(SysCode.DELETE_STATUS.YES);
        this.dbFaModelTemplateBlockMapper.updateByPrimaryKeySelective(dbFaModelTemplateBlock);
    }

    /**
     * 根据ids删除区块
     * @param ids
     */
    @Transactional(rollbackFor = Exception.class)
    public void delByBlockIds(String ids) {
        Assert.hasLength(ids, "删除主键不能为空！");
        Optional.ofNullable(Arrays.asList(ids.split(",")))
                .orElseGet(Lists::newArrayList)
                .stream()
                .forEach(id -> {
                    DbFaModelTemplateBlock dbFaModelTemplateBlock=this.getFaModelTemplateBlockByOid(id);
                    Assert.notNull(dbFaModelTemplateBlock, MessageFormat.format("删除对象不存在！对象id为{0}", id));
                    dbFaModelTemplateBlock.setDelFlag(SysCode.DELETE_STATUS.YES);
                    dbFaModelTemplateBlock.setModifyDate(new Date());
                    this.dbFaModelTemplateBlockMapper.updateByPrimaryKeySelective(dbFaModelTemplateBlock);
                });
    }

    public void saveOrUpdate(FaModelTemplateBlock faModelTemplateBlock) throws Exception {
        DbFaModelTemplateBlock dbFaModelTemplateBlock;
        if (null != faModelTemplateBlock.getId()) {
            dbFaModelTemplateBlock = this.dbFaModelTemplateBlockMapper.selectByPrimaryKey(faModelTemplateBlock.getId());
            Assert.notNull(dbFaModelTemplateBlock, MessageFormat.format("更新对象不存在！对象id为{0}", dbFaModelTemplateBlock.getId()));
            BeanUtils.copyProperties(faModelTemplateBlock, dbFaModelTemplateBlock);
            dbFaModelTemplateBlock.setModifyDate(new Date());
            this.dbFaModelTemplateBlockMapper.updateByPrimaryKeySelective(dbFaModelTemplateBlock);
        } else {
            dbFaModelTemplateBlock = new DbFaModelTemplateBlock();

            BeanUtils.copyProperties(faModelTemplateBlock, dbFaModelTemplateBlock);
            dbFaModelTemplateBlock.setDelFlag(SysCode.DELETE_STATUS.NO);
            dbFaModelTemplateBlock.setCreateDate(new Date());
            dbFaModelTemplateBlock.setModifyDate(new Date());
            dbFaModelTemplateBlock.setFaModelTemplateBlockOid(UUIDUtil.randomUUID());
            this.dbFaModelTemplateBlockMapper.insert(dbFaModelTemplateBlock);
        }

    }

    public List<PictureCutRuleNew> queryCataMetadataBlockByTemplateOid(String templateOid) throws Exception {
        //List<Object> listObj = faModelTemplateBlockDao.queryCataMetadataBlockByTemplateOid(templateOid);
        List<Object> listObj = null;
        List<PictureCutRuleNew> list = new ArrayList<PictureCutRuleNew>();
        for (int i = 0; i < listObj.size(); i++) {
            JSONObject jo = JsonUtil.objToJSONObject(listObj.get(i));
            PictureCutRuleNew pcr = (PictureCutRuleNew) JSONObject.toJavaObject(jo, PictureCutRuleNew.class);
            list.add(pcr);
        }
        return list;
    }


    public FaModelTemplateBlock getFeatureFaModelTemplateBlockByTemplateOidNew(String faModelTemplateOid){
        Assert.hasLength(faModelTemplateOid, "主键不能为空！");
        DbFaModelTemplateBlock dbFaModelTemplateBlock=this.dbFaModelTemplateBlockMapper.getFeatureFaModelTemplateBlockByTemplateOid(faModelTemplateOid);
        FaModelTemplateBlock faModelTemplateBlock=new FaModelTemplateBlock();
        BeanUtils.copyProperties(dbFaModelTemplateBlock,faModelTemplateBlock);
        return faModelTemplateBlock;
    }

}
