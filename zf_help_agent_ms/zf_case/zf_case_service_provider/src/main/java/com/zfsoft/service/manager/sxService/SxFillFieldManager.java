package com.zfsoft.service.manager.sxService;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.platform.utils.validate.ResultInfoException;
import com.zfsoft.service.dbaccess.dao.sxService.DbSxFillFieldMapper;
import com.zfsoft.service.dbaccess.dao.sxService.DbSxFormInfoMapper;
import com.zfsoft.service.dbaccess.dao.sxService.DbSxOptionFieldMapper;
import com.zfsoft.service.dbaccess.data.sxService.*;
import com.zfsoft.service.sxService.data.SxFillField;
import com.zfsoft.service.sxService.data.SxOptionField;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.shardingsphere.transaction.annotation.ShardingTransactionType;
import org.apache.shardingsphere.transaction.core.TransactionType;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @ClassName SxFillFieldManager
 * @Description 事项表单字段manager
 * @Author xiayj
 * @Date 2021/7/15 16:16
 **/
@Service
@Slf4j
public class SxFillFieldManager {

    @Resource
    private DbSxFillFieldMapper dbSxFillFieldMapper;

    @Resource
    private SxFillLabelManager sxFillLabelManager;

    @Resource
    private SxFieldTypeManager sxFieldTypeManager;

    @Resource
    private SxServiceManager sxServiceManager;

    @Resource
    private DbSxOptionFieldMapper dbSxOptionFieldMapper;

    @Resource
    private DbSxFormInfoMapper dbSxFormInfoMapper;

    public PageResult<SxFillField> querySxFillFieldPage(String contentOid, String fieldName, String fieldCode, String labelOid, Integer pageNumber, Integer pageSize){
        //分页参数
        if (null == pageNumber || pageNumber <= 0) {
            pageNumber = 0;
        }
        if (null == pageSize || pageSize <= 0) {
            pageSize = 10;
        }
        PageHelper.startPage(pageNumber,pageSize);
        DbSxFillFieldExample example = new DbSxFillFieldExample();
        DbSxFillFieldExample.Criteria criteria = example.createCriteria();
        if(StringUtils.isNotEmpty(contentOid)){
            criteria.andServiceOidEqualTo(contentOid);
        }
        if(StringUtils.isNotEmpty(fieldName)){
            criteria.andFieldNameLike("%"+fieldName.trim() + "%");
        }
        if(StringUtils.isNotEmpty(fieldCode)){
            criteria.andFieldCodeLike("%"+fieldCode.trim()+"%");
        }
        if(StringUtils.isNotEmpty(labelOid)){
            criteria.andLabelOidEqualTo(labelOid);
        }
        criteria.andDelFlagEqualTo(0);
        Page<DbSxFillField> dbSxFillFields = (Page<DbSxFillField>) dbSxFillFieldMapper.selectByExample(example);
        PageResult<SxFillField> pageResult = new PageResult<>(dbSxFillFields.getPageNum(),dbSxFillFields.getPageSize(),dbSxFillFields.getTotal());
        List<SxFillField> collect = dbSxFillFields.stream().map(dbSxFillField -> {
            SxFillField sxFillField = new SxFillField();
            BeanUtils.copyProperties(dbSxFillField, sxFillField);
            sxFillField.setFieldTypeName(sxFieldTypeManager.getSxFieldTypeByFieldTypeOid(sxFillField.getFieldTypeOid()).getFieldTypeName());
            sxFillField.setServiceName(sxServiceManager.getSxServiceByOid(sxFillField.getServiceOid()).getServiceName());
            sxFillField.setLabelName(sxFillLabelManager.getSxFillLabelByOid(sxFillField.getLabelOid()).getLabelName());
            return sxFillField;
        }).collect(Collectors.toList());
        pageResult.setData(collect);
        return pageResult;
    }


    /***
     * @param fieldOid
     * @description: 查询字段信息
     * @return: com.zfsoft.formConfig.data.SxFillField
     * @author: xiayj
     * @date: 2021/7/15
     */
    public SxFillField getSxFillFieldByOid(String fieldOid){
        DbSxFillField dbSxFillField = this.getDbSxFillFieldByOid(fieldOid);
        if(dbSxFillField==null){
            throw new ResultInfoException("找不到对应表单字段");
        }
        SxFillField sxFillField = new SxFillField();
        BeanUtils.copyProperties(dbSxFillField,sxFillField);
        sxFillField.setFieldTypeName(sxFieldTypeManager.getSxFieldTypeByFieldTypeOid(sxFillField.getFieldTypeOid()).getFieldTypeName());
        sxFillField.setServiceName(sxServiceManager.getSxServiceByOid(sxFillField.getServiceOid()).getServiceName());
        sxFillField.setLabelName(sxFillLabelManager.getSxFillLabelByOid(sxFillField.getLabelOid()).getLabelName());
        sxFillField.setTypeLabelTree(Arrays.asList(dbSxFillField.getFieldTypeOid(),dbSxFillField.getLabelOid()));
        return sxFillField;
    }

    /***
     * @param sxFillField
     * @description: 保存字段信息
     * @return: void
     * @author: xiayj
     * @date: 2021/7/15
     */
    public void saveOrUpdateSxFillField(SxFillField sxFillField){
        if(sxFillField.getTypeLabelTree()!=null){
            sxFillField.setFieldTypeOid(sxFillField.getTypeLabelTree().get(0));
            sxFillField.setLabelOid(sxFillField.getTypeLabelTree().get(1));
        }
        DbSxFillField dbSxFillField;
        List<DbSxFillField> dbSxFillFields = this.queryDbSxFillFieldByName(
                sxFillField.getServiceOid(),sxFillField.getFieldTypeOid(),sxFillField.getFieldName());
        List<DbSxFillField> dbSxFillFields1 = this.queryDbSxFillFieldByCode(
                sxFillField.getServiceOid(),sxFillField.getFieldTypeOid(), sxFillField.getFieldCode());
        // 表单信息
        DbSxFormInfo dbSxFormInfo = null;
        DbSxFormInfoExample formInfoExample = new DbSxFormInfoExample();
        DbSxFormInfoExample.Criteria criteria = formInfoExample.createCriteria();
        criteria.andFieldTypeOidEqualTo(sxFillField.getFieldTypeOid());
        criteria.andServiceOidEqualTo(sxFillField.getServiceOid());
        criteria.andDelFlagEqualTo(0);
        List<DbSxFormInfo> dbSxFormInfos = dbSxFormInfoMapper.selectByExample(formInfoExample);
        if(null != dbSxFormInfos && dbSxFormInfos.size() != 0) {
            dbSxFormInfo = dbSxFormInfos.get(0);
        }
        if(StringUtils.isEmpty(sxFillField.getFieldOid())){
            if(!dbSxFillFields.isEmpty()){
                throw new ResultInfoException("同分类下字段名称不能重复");
            }
            if(!dbSxFillFields1.isEmpty()){
                throw new ResultInfoException("同分类下字段编码不能重复");
            }
            dbSxFillField = new DbSxFillField();
            BeanUtils.copyProperties(sxFillField,dbSxFillField);
            if(null != dbSxFormInfo) {
                dbSxFillField.setFormOid(dbSxFormInfo.getFormOid());
            }
            dbSxFillField.setCreateDate(new Date());
            dbSxFillField.setModifyDate(new Date());
            dbSxFillField.setDelFlag(0);
            dbSxFillField.setFieldOid(IdUtil.simpleUUID());
            dbSxFillFieldMapper.insert(dbSxFillField);
        }else {
            if(!dbSxFillFields.isEmpty()){
                if(!sxFillField.getFieldOid().equals(dbSxFillFields.get(0).getFieldOid())){
                    throw new ResultInfoException("同分类下字段名称不能重复");
                }
            }
            if(!dbSxFillFields1.isEmpty()){
                if(!sxFillField.getFieldOid().equals(dbSxFillFields1.get(0).getFieldOid())){
                    throw new ResultInfoException("同分类下字段编码不能重复");
                }
            }
            dbSxFillField = this.getDbSxFillFieldByOid(sxFillField.getFieldOid());
            if(dbSxFillField==null){
                throw new ResultInfoException("找不到对应表单字段");
            }
            BeanUtils.copyProperties(sxFillField,dbSxFillField);
            if(null != dbSxFormInfo) {
                dbSxFillField.setFormOid(dbSxFormInfo.getFormOid());
            }
            dbSxFillField.setModifyDate(new Date());
            dbSxFillFieldMapper.updateByPrimaryKeySelective(dbSxFillField);
        }
    }

    /***
     * @param fieldOid
     * @description: 删除数据
     * @return: void
     * @author: xiayj
     * @date: 2021/7/15
     */
    public void deleteSxFillField(String fieldOid){
        if(StringUtils.isEmpty(fieldOid)){
            throw new ResultInfoException("找不到删除主键");
        }
        String[] split = fieldOid.split(",");
        DbSxFillFieldExample example = new DbSxFillFieldExample();
        DbSxFillFieldExample.Criteria criteria = example.createCriteria();
        criteria.andFieldOidIn(Arrays.asList(split));
        criteria.andDelFlagEqualTo(0);
        List<DbSxFillField> dbSxFillFields = dbSxFillFieldMapper.selectByExample(example);
        for(DbSxFillField dbSxFillField:dbSxFillFields){
            dbSxFillField.setDelFlag(1);
            dbSxFillFieldMapper.updateByPrimaryKeySelective(dbSxFillField);
        }

    }

    /***
     * @param serviceOid
     * @description: 查询事项对应所有字段
     * @return: java.util.List<com.zfsoft.formConfig.data.SxFillField>
     * @author: xiayj
     * @date: 2021/7/28
     */
    public List<SxFillField> queryFieldList(String serviceOid, String fieldTypeOid, String formOid){
        DbSxFillFieldExample example = new DbSxFillFieldExample();
        DbSxFillFieldExample.Criteria criteria = example.createCriteria();
        criteria.andDelFlagEqualTo(0);
        criteria.andServiceOidEqualTo(serviceOid);
        if(StringUtils.isNotEmpty(fieldTypeOid)){
            criteria.andFieldTypeOidEqualTo(fieldTypeOid);
        }
        if(StringUtils.isNotEmpty(formOid)) {
            criteria.andFormOidEqualTo(formOid);
        }
        List<DbSxFillField> dbSxFillFields = dbSxFillFieldMapper.selectByExample(example);
        return dbSxFillFields.stream().map(dbSxFillField -> {
            SxFillField sxFillField = new SxFillField();
            BeanUtils.copyProperties(dbSxFillField,sxFillField);
            sxFillField.setFieldTypeName(sxFieldTypeManager.getSxFieldTypeByFieldTypeOid(sxFillField.getFieldTypeOid()).getFieldTypeName());
            sxFillField.setServiceName(sxServiceManager.getSxServiceByOid(sxFillField.getServiceOid()).getServiceName());
            sxFillField.setLabelName(sxFillLabelManager.getSxFillLabelByOid(sxFillField.getLabelOid()).getLabelName());
            return sxFillField;
        }).collect(Collectors.toList());
    }

    /***
     * @param serviceOid
     * @description: 查询事项对应所有字段
     * @return: java.util.List<com.zfsoft.formConfig.data.SxFillField>
     * @author: xiayj
     * @date: 2021/7/28
     */
    public List<SxFillField> queryBasicFieldList(String serviceOid, String fieldTypeOid, String formOid){
        DbSxFillFieldExample example = new DbSxFillFieldExample();
        DbSxFillFieldExample.Criteria criteria = example.createCriteria();
        criteria.andDelFlagEqualTo(0);
        criteria.andServiceOidEqualTo(serviceOid);
        if(StringUtils.isNotEmpty(fieldTypeOid)){
            criteria.andFieldTypeOidEqualTo(fieldTypeOid);
        }
        if(StringUtils.isNotEmpty(formOid)) {
            criteria.andFormOidEqualTo(formOid);
        }
        List<DbSxFillField> dbSxFillFields = dbSxFillFieldMapper.selectByExample(example);
        return dbSxFillFields.stream().map(dbSxFillField -> {
            SxFillField sxFillField = new SxFillField();
            BeanUtils.copyProperties(dbSxFillField,sxFillField);
            return sxFillField;
        }).collect(Collectors.toList());
    }

    public List<SxFillField> queryFieldListNew(String serviceOid, String fieldTypeOid, String formOid){
        DbSxFillFieldExample example = new DbSxFillFieldExample();
        DbSxFillFieldExample.Criteria criteria = example.createCriteria();
        criteria.andDelFlagEqualTo(0);
        criteria.andServiceOidEqualTo(serviceOid);
        if(StringUtils.isNotEmpty(fieldTypeOid)){
            criteria.andFieldTypeOidEqualTo(fieldTypeOid);
        }
        if(StringUtils.isNotEmpty(formOid)) {
            criteria.andFormOidEqualTo(formOid);
        }
        List<DbSxFillField> dbSxFillFields = dbSxFillFieldMapper.selectByExample(example);
        return dbSxFillFields.stream().map(dbSxFillField -> {
            SxFillField sxFillField = new SxFillField();
            BeanUtils.copyProperties(dbSxFillField,sxFillField);
            return sxFillField;
        }).collect(Collectors.toList());
    }

    public List<SxFillField> selectFieldListByServiceOidAndTypeOid(String serviceOid, String fieldTypeOid){
        DbSxFillFieldExample example = new DbSxFillFieldExample();
        DbSxFillFieldExample.Criteria criteria = example.createCriteria();
        criteria.andDelFlagEqualTo(0);
        criteria.andServiceOidEqualTo(serviceOid);
        if(StringUtils.isNotEmpty(fieldTypeOid)){
            criteria.andFieldTypeOidEqualTo(fieldTypeOid);
        }
        List<DbSxFillField> dbSxFillFields = dbSxFillFieldMapper.selectByExample(example);
        return dbSxFillFields.stream().map(dbSxFillField -> {
            SxFillField sxFillField = new SxFillField();
            BeanUtils.copyProperties(dbSxFillField,sxFillField);
            return sxFillField;
        }).collect(Collectors.toList());
    }

    /***
     * @param fieldOid
     * @description: 查询field
     * @return: com.zfsoft.dbaccess.data.formConfig.DbSxFillField
     * @author: xiayj
     * @date: 2021/7/15
     */
    public DbSxFillField getDbSxFillFieldByOid(String fieldOid){
        DbSxFillFieldExample example = new DbSxFillFieldExample();
        DbSxFillFieldExample.Criteria criteria = example.createCriteria();
        criteria.andDelFlagEqualTo(0);
        criteria.andFieldOidEqualTo(fieldOid);
        List<DbSxFillField> dbSxFillFields = dbSxFillFieldMapper.selectByExample(example);
        if(dbSxFillFields.isEmpty()){
            return null;
        }
        return dbSxFillFields.get(0);
    }

    /***
     * @param name
     * @description: 根据名字查询字段
     * @return: java.util.List<com.zfsoft.dbaccess.data.formConfig.DbSxFillField>
     * @author: xiayj
     * @date: 2021/7/27
     */
    private List<DbSxFillField> queryDbSxFillFieldByName(String serviceOid,String fieldTypeOid,String name){
        DbSxFillFieldExample example = new DbSxFillFieldExample();
        DbSxFillFieldExample.Criteria criteria = example.createCriteria();
        criteria.andDelFlagEqualTo(0);
        criteria.andServiceOidEqualTo(serviceOid);
        criteria.andFieldTypeOidEqualTo(fieldTypeOid);
        criteria.andFieldNameEqualTo(name);
        return dbSxFillFieldMapper.selectByExample(example);
    }

    /***
     * @param fieldTypeOid
     * @param code
     * @description: 根据编码查询字段
     * @return: java.util.List<com.zfsoft.dbaccess.data.formConfig.DbSxFillField>
     * @author: xiayj
     * @date: 2021/7/27
     */
    private List<DbSxFillField> queryDbSxFillFieldByCode(String serviceOid,String fieldTypeOid,String code){
        DbSxFillFieldExample example = new DbSxFillFieldExample();
        DbSxFillFieldExample.Criteria criteria = example.createCriteria();
        criteria.andDelFlagEqualTo(0);
        criteria.andServiceOidEqualTo(serviceOid);
        criteria.andFieldTypeOidEqualTo(fieldTypeOid);
        criteria.andFieldCodeEqualTo(code);
        return dbSxFillFieldMapper.selectByExample(example);
    }

    /***
     * 批量更新字段信息集合
     * @param sxFillFields
     * @author lushuai
     * @date 2021/8/27 16:09
     */
    @Transactional(rollbackFor = Exception.class)
    @ShardingTransactionType(TransactionType.LOCAL)
    public void batchUpdateFillField(List<SxFillField> sxFillFields) {
        if (sxFillFields.size() <= 0) {
            return;
        }
        for (SxFillField sxFillField : sxFillFields) {
            if (sxFillField.getId() == null) {continue;}

            DbSxFillField dbSxFillField = new DbSxFillField();
            BeanUtils.copyProperties(sxFillField, dbSxFillField);
            dbSxFillFieldMapper.updateByPrimaryKeySelective(dbSxFillField);
        }
    }

    public List<SxFillField> queryFieldListByOids(String serviceOid, String fieldTypeOid, String labelOid) {
        DbSxFillFieldExample example = new DbSxFillFieldExample();
        DbSxFillFieldExample.Criteria criteria = example.createCriteria();
        criteria.andDelFlagEqualTo(0);
        criteria.andServiceOidEqualTo(serviceOid);
        if(StringUtils.isNotEmpty(fieldTypeOid)){
            criteria.andFieldTypeOidEqualTo(fieldTypeOid);
        }
        if(StringUtils.isNotEmpty(labelOid)){
            criteria.andLabelOidEqualTo(labelOid);
        }
        List<DbSxFillField> dbSxFillFields = dbSxFillFieldMapper.selectByExample(example);
        return dbSxFillFields.stream().map(dbSxFillField -> {
            SxFillField sxFillField = new SxFillField();
            BeanUtils.copyProperties(dbSxFillField,sxFillField);
            return sxFillField;
        }).collect(Collectors.toList());
    }

    /**
     * 查询分类下面的标签和字段信息
     * @param serviceOid
     * @param fieldTypeOid
     * @param labelOid 为空的时候查询分类下面不是动态表格的所有标签
     * @return
     */
    public List<SxFillField> queryFieldListInfo(String serviceOid, String fieldTypeOid, String labelOid) {
        Map<String,String> map=new HashMap<>();
        map.put("serviceOid",serviceOid);
        if(StrUtil.isNotEmpty(fieldTypeOid)){
            map.put("fieldTypeOid",fieldTypeOid);
        }
        if(StrUtil.isNotEmpty(labelOid)){
            map.put("labelOid",labelOid);
        }
        List<DbSxFillField> dbSxFillFields = dbSxFillFieldMapper.queryFieldListInfo(map);
        return dbSxFillFields.stream().map(dbSxFillField -> {
            SxFillField sxFillField = new SxFillField();
            BeanUtils.copyProperties(dbSxFillField,sxFillField);
            return sxFillField;
        }).collect(Collectors.toList());
    }

    /**
     * 批量保存更新信息
     * @param list
     */
    public void saveOrUpdateField(List<SxFillField> list, String formOid) {
        if(list!=null && list.size()>0){
            for(SxFillField field:list){
                if(field.getFieldOid()!=null){
                    DbSxFillField oldField=this.getDbSxFillFieldByOid(field.getFieldOid());
                    if(oldField!=null){
                        oldField.setDataType(field.getDataType());
                        oldField.setFormOid(formOid);
                        oldField.setColumnLenght(field.getColumnLenght());
                        oldField.setColumnType(field.getColumnType());
                        dbSxFillFieldMapper.updateByPrimaryKey(oldField);
                    }
                }
            }
        }


    }

    public int saveOrUpdateOptionField(SxOptionField sxOptionField) {
        if(null == sxOptionField) {
            throw new ResultInfoException("保存对象不能为空");
        }
        DbSxOptionFieldExample dbSxOptionFieldExample = new DbSxOptionFieldExample();
        DbSxOptionFieldExample.Criteria criteria = dbSxOptionFieldExample.createCriteria();
        criteria.andValOidEqualTo(sxOptionField.getValOid());
        criteria.andFieldTypeOidEqualTo(sxOptionField.getFieldTypeOid());
        criteria.andDelFlagEqualTo(0);
        List<DbSxOptionField> dbSxOptionFields = dbSxOptionFieldMapper.selectByExample(dbSxOptionFieldExample);
        for(DbSxOptionField dbSxOptionField : dbSxOptionFields) {
            dbSxOptionField.setDelFlag(1);
            dbSxOptionField.setModifyDate(new Date());
            dbSxOptionFieldMapper.updateByPrimaryKeySelective(dbSxOptionField);
        }
        String[] arr = sxOptionField.getFieldOids().split(",");
        for(String fieldOid : arr) {
            if(StrUtil.isEmpty(fieldOid)) {
                continue;
            }
            DbSxOptionField dbSxOptionField = new DbSxOptionField();
            dbSxOptionField.setOid(IdUtil.simpleUUID());
            dbSxOptionField.setServiceOid(sxOptionField.getServiceOid());
            dbSxOptionField.setFieldOid(fieldOid);
            dbSxOptionField.setValOid(sxOptionField.getValOid());
            dbSxOptionField.setTitleOid(sxOptionField.getTitleOid());
            dbSxOptionField.setFieldTypeOid(sxOptionField.getFieldTypeOid());
            dbSxOptionField.setDelFlag(0);
            dbSxOptionField.setCreateDate(new Date());
            dbSxOptionFieldMapper.insert(dbSxOptionField);
        }
        return 1;
    }

    public List<SxOptionField> queryOptionFieldListByValOid(String valOid) {
        if(StrUtil.isEmpty(valOid)) {
            throw new ResultInfoException("选项不能为空");
        }
        DbSxOptionFieldExample dbSxOptionFieldExample = new DbSxOptionFieldExample();
        DbSxOptionFieldExample.Criteria criteria = dbSxOptionFieldExample.createCriteria();
        criteria.andValOidEqualTo(valOid);
        criteria.andDelFlagEqualTo(0);
        List<DbSxOptionField> dbSxOptionFields = dbSxOptionFieldMapper.selectByExample(dbSxOptionFieldExample);
        List<SxOptionField> sxOptionFieldList = dbSxOptionFields.stream().map(dbSxOptionField -> {
            SxOptionField sxOptionField = new SxOptionField();
            BeanUtils.copyProperties(dbSxOptionField, sxOptionField);
            return sxOptionField;
        }).collect(Collectors.toList());
        return sxOptionFieldList;
    }

    public List<SxOptionField> queryOptionFieldListByServiceOid(String serviceOid) {
        if(StrUtil.isEmpty(serviceOid)) {
            throw new ResultInfoException("事项主键不能为空");
        }
        DbSxOptionFieldExample dbSxOptionFieldExample = new DbSxOptionFieldExample();
        DbSxOptionFieldExample.Criteria criteria = dbSxOptionFieldExample.createCriteria();
        criteria.andServiceOidEqualTo(serviceOid);
        criteria.andDelFlagEqualTo(0);
        List<DbSxOptionField> dbSxOptionFields = dbSxOptionFieldMapper.selectByExample(dbSxOptionFieldExample);
        List<SxOptionField> sxOptionFieldList = dbSxOptionFields.stream().map(dbSxOptionField -> {
            SxOptionField sxOptionField = new SxOptionField();
            BeanUtils.copyProperties(dbSxOptionField, sxOptionField);
            return sxOptionField;
        }).collect(Collectors.toList());
        return sxOptionFieldList;
    }
}
