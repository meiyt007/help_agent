package com.zfsoft.service.manager.sxService;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zfsoft.service.dbaccess.dao.sxService.DbSxFieldTypeMapper;
import com.zfsoft.service.dbaccess.dao.sxService.DbSxFillLabelMapper;
import com.zfsoft.service.dbaccess.data.sxService.DbSxFieldType;
import com.zfsoft.service.dbaccess.data.sxService.DbSxFieldTypeExample;
import com.zfsoft.service.dbaccess.data.sxService.DbSxFillLabel;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.platform.utils.validate.ParamValid;
import com.zfsoft.platform.utils.validate.ResultInfoException;
import com.zfsoft.platform.utils.validate.ValidGroups;
import com.zfsoft.service.sxService.data.SxFieldType;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.shardingsphere.transaction.annotation.ShardingTransactionType;
import org.apache.shardingsphere.transaction.core.TransactionType;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName SxFieldTypeManagerImpl
 * @Description: 类别管理接口实现类
 * @Author wuxx
 * @Date 2021/4/5
 **/
@Service
@Slf4j
@CacheConfig(cacheNames = "dataChange:fieldType")
public class SxFieldTypeManager {

    @Resource
    private DbSxFieldTypeMapper dbSxFieldTypeMapper;

    @Resource
    private DbSxFillLabelMapper dbSxFillLabelMapper;
    /**
     * @description:  保存字段类别信息
     * @param sxFieldType 字段类别对象
     * @author: wuxx
     * @Date: 2021/7/14
     **/
    @Transactional(rollbackFor = Exception.class)
    @ParamValid
    @ShardingTransactionType(TransactionType.LOCAL)
    @CacheEvict(allEntries = true)
    public int saveSxFieldType(@ValidGroups(groups = {SxFieldType.INSERT_GROUP.class})SxFieldType sxFieldType) {
        if(null == sxFieldType){
            throw new ResultInfoException("字段类别对象不能为空！");
        }
        if(StringUtils.isEmpty(sxFieldType.getFieldTypeName())){
            throw new ResultInfoException("类别名称不能为空！");
        }
        List<DbSxFieldType> dbSxFieldTypes = this.queryDbSxFieldTypeByName(sxFieldType.getFieldTypeName());
        if (null == sxFieldType.getId()) {
            if(!dbSxFieldTypes.isEmpty()){
                throw new ResultInfoException("字段类别名称已存在!");
            }
            sxFieldType.setId(null);
            sxFieldType.setFieldTypeOid(IdUtil.simpleUUID());
            sxFieldType.setCreateDate(new Date());
        } else {
            if(!dbSxFieldTypes.isEmpty()){
                DbSxFieldType dbSxFieldType = dbSxFieldTypes.get(0);
                if(!dbSxFieldType.getId().equals(sxFieldType.getId())){
                    throw new ResultInfoException("字段类别名称已存在!");
                }
            }
            // 字典oid不为空
            SxFieldType cur = this.getSxFieldTypeById(sxFieldType.getId());
            if (cur == null) {
                throw new ResultInfoException("字段类别对象未查询到相应的信息!");
            }
        }
        // 设置区划信息的状态
        if (null == sxFieldType.getIsDelete()) {
            sxFieldType.setIsDelete(0);
        }
        if (null == sxFieldType.getIsAble()) {
            sxFieldType.setIsAble(1);
        }
        DbSxFieldType dbSxFieldType = new DbSxFieldType();
        BeanUtils.copyProperties(sxFieldType,dbSxFieldType);
        int index=0;
        if (null == sxFieldType.getId()) {
            index = dbSxFieldTypeMapper.insert(dbSxFieldType);
        }else {
            index = dbSxFieldTypeMapper.updateByPrimaryKeySelective(dbSxFieldType);
        }
        sxFieldType.setId(dbSxFieldType.getId());
        return index;
    }


    /**
     * @description: 分页查询列表
     * @param sxFieldType
     * @param pageNumber
     * @param pageSize
     * @author: wuxx
     * @Date: 2021/7/14 13:29
     **/
    public PageResult<SxFieldType> querySxFieldTypeWithPage(SxFieldType sxFieldType, Integer pageNumber, Integer pageSize) {
        //分页参数
        if (null == pageNumber || pageNumber <= 1) {
            pageNumber = 1;
        }
        if (null == pageSize || pageSize <= 0) {
            pageSize = 10;
        }
        PageHelper.startPage(pageNumber,pageSize);
        DbSxFieldTypeExample dbSxFieldTypeExample = new DbSxFieldTypeExample();
        DbSxFieldTypeExample.Criteria criteria = dbSxFieldTypeExample.createCriteria();
        dbSxFieldTypeExample.setOrderByClause(" sort asc ");
        if(null!=sxFieldType){
            if(StrUtil.isNotEmpty(sxFieldType.getFieldTypeName())){
                criteria.andFieldTypeNameLike("%"+sxFieldType.getFieldTypeName().trim()+"%");
            }
            if(null!=sxFieldType.getIsAble()){
                criteria.andIsAbleEqualTo(sxFieldType.getIsAble());
            }
        }
        criteria.andIsDeleteEqualTo(0);
        Page<DbSxFieldType> dbSxFieldTypes = (Page<DbSxFieldType>)dbSxFieldTypeMapper.selectByExample(dbSxFieldTypeExample);
        PageResult<SxFieldType> pageResult = new PageResult<>(dbSxFieldTypes.getPageNum(),dbSxFieldTypes.getPageSize(),dbSxFieldTypes.getTotal());
        List<SxFieldType> sxFieldTypeList = dbSxFieldTypes.stream().map(dbSxFieldType -> {
            SxFieldType dict = new SxFieldType();
            BeanUtils.copyProperties(dbSxFieldType,dict);
            return dict;
        }).collect(Collectors.toList());
        pageResult.setData(sxFieldTypeList);
        return pageResult;
    }

    /**
     * @description:  删除字段类别信息
     * @param id 字段类别对象主键
     * @author: wuxx
     * @Date: 2021/7/14 13:18
     **/
    @Transactional(rollbackFor = Exception.class)
    @ShardingTransactionType(TransactionType.LOCAL)
    @CacheEvict(allEntries = true)
    public int deleteSxFieldTypeById(Long id) {
        List<DbSxFillLabel> dbSxFillLabels = null;
        int index =0;
        DbSxFieldType dbSxFieldType = dbSxFieldTypeMapper.selectByPrimaryKey(id);
        if(dbSxFieldType == null){
            throw new ResultInfoException("字段类别对象信息为空！");
        }else{
             dbSxFillLabels =  dbSxFillLabelMapper.getSxFillLabelsByTypeOid(dbSxFieldType.getFieldTypeOid());
            if(dbSxFillLabels.size() > 0){
                 throw new ResultInfoException("存在关联字段标签！");
             }else{
                 index = dbSxFieldTypeMapper.deleteByFieldTypeOid(dbSxFieldType.getFieldTypeOid());
                 if(index==0){
                     throw new ResultInfoException("字段类别对象删除失败，请稍后再试！");
                 }
             }
        }
        return index;
    }

    /**
     * @description:  启禁用字段类别信息
     * @param id 字段类别对象主键
     * @author: wuxx
     * @Date: 2021/7/14 13:18
     **/
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(allEntries = true)
    public int ableSxFieldTypeById(Long id) {
        List<DbSxFillLabel> dbSxFillLabels = null;
        int index =0;
        DbSxFieldType dbSxFieldType = dbSxFieldTypeMapper.selectByPrimaryKey(id);
        if(dbSxFieldType == null){
            throw new ResultInfoException("字段类别对象信息为空！");
        }else{
            dbSxFillLabels =  dbSxFillLabelMapper.getSxFillLabelsByTypeOid(dbSxFieldType.getFieldTypeOid());
            if(dbSxFillLabels.size() > 0){
                throw new ResultInfoException("存在关联字段标签！");
            }else{
                Integer isAble = dbSxFieldType.getIsAble();
                if(0 == isAble){
                    dbSxFieldType.setIsAble(1);
                }else {
                    dbSxFieldType.setIsAble(0);
                }
                index = dbSxFieldTypeMapper.updateByPrimaryKeySelective(dbSxFieldType);
                if(index==0){
                    throw new ResultInfoException("字段类别对象启禁用失败，请稍后再试！");
                }
            }
        }
        return index;
    }

    //@Cacheable(key = "'getSxFieldTypeById:'+#id", unless = "#result == null")
    public SxFieldType getSxFieldTypeById(Long id) {
        DbSxFieldType dbSxFieldType = dbSxFieldTypeMapper.selectByPrimaryKey(id);
        if(dbSxFieldType == null){return null;}
        SxFieldType sxFieldType = new SxFieldType();
        BeanUtils.copyProperties(dbSxFieldType,sxFieldType);
        return sxFieldType;
    }

    /**
     * @description: 根据业务主键获取对象
     * @param fieldTypeOid 业务主键
     * @author: wuxx
     * @Date: 2021/7/14 13:29
     **/
    //@Cacheable(key = "'getSxFieldTypeByFieldTypeOid:'+#fieldTypeOid", unless = "#result == null")
    public SxFieldType getSxFieldTypeByFieldTypeOid(String fieldTypeOid) {
        DbSxFieldType dbSxFieldType = dbSxFieldTypeMapper.selectByFieldTypeOid(fieldTypeOid);
        if(dbSxFieldType == null){return null;}
        SxFieldType sxFieldType = new SxFieldType();
        BeanUtils.copyProperties(dbSxFieldType,sxFieldType);
        return sxFieldType;
    }


    /**
     * @description: 根据字段类别对象查询集合
     * @param sxFieldType 字段类别对象
     * @author: wuxx
     * @Date: 2021/3/11 10:57
     **/
    public List<SxFieldType> querySxFieldTypeList(SxFieldType sxFieldType) {
        DbSxFieldTypeExample dbSxFieldTypeExample = new DbSxFieldTypeExample();
        DbSxFieldTypeExample.Criteria criteria = dbSxFieldTypeExample.createCriteria();
        if(null!=sxFieldType){
            if(StrUtil.isNotEmpty(sxFieldType.getFieldTypeName())){
                criteria.andFieldTypeNameLike("%"+sxFieldType.getFieldTypeName().trim()+"%");
            }
            if(null!=sxFieldType.getIsAble()){
                criteria.andIsAbleEqualTo(sxFieldType.getIsAble());
            }
            if(StrUtil.isNotEmpty(sxFieldType.getParentOid())){
                criteria.andParentOidEqualTo(sxFieldType.getParentOid());
            }
        }
        criteria.andIsDeleteEqualTo(0);
        List<DbSxFieldType> dbSxFieldTypeList = dbSxFieldTypeMapper.selectByExample(dbSxFieldTypeExample);
        List<SxFieldType> sxFieldTypeList = dbSxFieldTypeList.stream().map(dbSxFieldType -> {
            SxFieldType object = new SxFieldType();
            BeanUtils.copyProperties(dbSxFieldType,object);
            return object;
        }).collect(Collectors.toList());
        return sxFieldTypeList;
    }

    /**
     * @description: 根据字段类别对象查询集合
     * @param sxFieldType 字段类别对象
     * @author: wuxx
     * @Date: 2021/3/11 10:57
     **/
    public List<SxFieldType> queryTopSxFieldTypeList(SxFieldType sxFieldType) {
        DbSxFieldTypeExample dbSxFieldTypeExample = new DbSxFieldTypeExample();
        DbSxFieldTypeExample.Criteria criteria = dbSxFieldTypeExample.createCriteria();
        if(null!=sxFieldType){
            if(StrUtil.isNotEmpty(sxFieldType.getFieldTypeName())){
                criteria.andFieldTypeNameLike("%"+sxFieldType.getFieldTypeName().trim()+"%");
            }
            if(null!=sxFieldType.getIsAble()){
                criteria.andIsAbleEqualTo(sxFieldType.getIsAble());
            }
            if(StrUtil.isNotEmpty(sxFieldType.getParentOid())){
                criteria.andParentOidEqualTo(sxFieldType.getParentOid());
            }
        }
        criteria.andIsDeleteEqualTo(0);
        List<DbSxFieldType> dbSxFieldTypeList = dbSxFieldTypeMapper.selectByExample(dbSxFieldTypeExample);
        List<SxFieldType> sxFieldTypeList = dbSxFieldTypeList.stream().map(dbSxFieldType -> {
            SxFieldType object = new SxFieldType();
            BeanUtils.copyProperties(dbSxFieldType,object);
            //查询子类
            return object;
        }).collect(Collectors.toList());
        return sxFieldTypeList;
    }


    /***
     * @param name
     * @description: 根据名字查询字段
     * @return: java.util.List<com.zfsoft.dbaccess.data.dataChange.DbSxFieldType>
     * @author: xiayj
     * @date: 2021/7/27
     */
    private List<DbSxFieldType> queryDbSxFieldTypeByName(String name){
        DbSxFieldTypeExample example = new DbSxFieldTypeExample();
        DbSxFieldTypeExample.Criteria criteria = example.createCriteria();
        criteria.andIsDeleteEqualTo(0);
        criteria.andFieldTypeNameEqualTo(name);
        return dbSxFieldTypeMapper.selectByExample(example);
    }

    /**
     * 查询事项下面所有可用的分类
     * @param serviceOid
     * @return
     */
    public List<SxFieldType> querySxFieldTypeListByServiceOid(String serviceOid) {
        List<DbSxFieldType> dbSxFieldTypeList = dbSxFieldTypeMapper.querySxFieldTypeListByServiceOid(serviceOid);
        List<SxFieldType> sxFieldTypeList = dbSxFieldTypeList.stream().map(dbSxFieldType -> {
            SxFieldType object = new SxFieldType();
            BeanUtils.copyProperties(dbSxFieldType,object);
            //查询子类
            return object;
        }).collect(Collectors.toList());
        return sxFieldTypeList;
    }

    /**
     * 查询一件事所有分类
     * @param thingOid
     * @return
     */
    public List<SxFieldType> queryComboFieldTypeListByThingOid(String thingOid) {
        List<DbSxFieldType> dbSxFieldTypeList = dbSxFieldTypeMapper.queryComboFieldTypeListByThingOid(thingOid);
        List<SxFieldType> sxFieldTypeList = dbSxFieldTypeList.stream().map(dbSxFieldType -> {
            SxFieldType object = new SxFieldType();
            BeanUtils.copyProperties(dbSxFieldType,object);
            return object;
        }).collect(Collectors.toList());
        return sxFieldTypeList;
    }



}
