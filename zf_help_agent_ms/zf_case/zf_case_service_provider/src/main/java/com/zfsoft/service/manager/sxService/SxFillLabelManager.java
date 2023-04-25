package com.zfsoft.service.manager.sxService;

import cn.hutool.core.util.IdUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zfsoft.service.dbaccess.dao.sxService.DbSxFillFieldMapper;
import com.zfsoft.service.dbaccess.dao.sxService.DbSxFillLabelMapper;
import com.zfsoft.service.dbaccess.data.sxService.DbSxFillField;
import com.zfsoft.service.dbaccess.data.sxService.DbSxFillFieldExample;
import com.zfsoft.service.dbaccess.data.sxService.DbSxFillLabel;
import com.zfsoft.service.dbaccess.data.sxService.DbSxFillLabelExample;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.platform.utils.validate.ResultInfoException;
import com.zfsoft.service.sxService.data.FieldTree;
import com.zfsoft.service.sxService.data.SxFieldType;
import com.zfsoft.service.sxService.data.SxFillField;
import com.zfsoft.service.sxService.data.SxFillLabel;
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
 * @ClassName SxFillLabelManager
 * @Description 事项表单字段标签manager
 * @Author xiayj
 * @Date 2021/7/14 16:17
 **/
@Service
@Slf4j
public class SxFillLabelManager {

    @Resource
    private DbSxFillLabelMapper dbSxFillLabelMapper;

    @Resource
    private SxFieldTypeManager sxFieldTypeManager;

    @Resource
    private SxServiceManager sxServiceManager;

    @Resource
    private DbSxFillFieldMapper dbSxFillFieldMapper;


    /***
     * @param pageNumber
     * @param pageSize
     * @description: 表单标签分页
     * @return: com.zfsoft.platform.common.data.PageResult<com.zfsoft.formConfig.data.SxFillLabel>
     * @author: xiayj
     * @date: 2021/7/15
     */
    public PageResult<SxFillLabel> querySxFillLabelPage(String contentOid, String labelName, String labelCode, String typeOid, Integer pageNumber, Integer pageSize){
        //分页参数
        if (null == pageNumber || pageNumber <= 0) {
            pageNumber = 0;
        }
        if (null == pageSize || pageSize <= 0) {
            pageSize = 10;
        }
        PageHelper.startPage(pageNumber,pageSize);
        DbSxFillLabelExample example = new DbSxFillLabelExample();
        DbSxFillLabelExample.Criteria criteria = example.createCriteria();
        criteria.andDelFlagEqualTo(0);
        if(StringUtils.isNotEmpty(contentOid)){
            criteria.andServiceOidEqualTo(contentOid);
        }
        if(StringUtils.isNotEmpty(labelName)){
            criteria.andLabelNameLike("%"+labelName.trim()+"%");
        }
        if(StringUtils.isNotEmpty(labelCode)){
            criteria.andLabelCodeLike("%"+labelCode.trim()+"%");
        }
        if(StringUtils.isNotEmpty(typeOid)){
            criteria.andTypeOidEqualTo(typeOid);
        }
        Page<DbSxFillLabel> dbSxFillLabels = (Page<DbSxFillLabel>) dbSxFillLabelMapper.selectByExample(example);
        PageResult<SxFillLabel> pageResult = new PageResult<>(dbSxFillLabels.getPageNum(),dbSxFillLabels.getPageSize(),dbSxFillLabels.getTotal());
        List<SxFillLabel> collect = dbSxFillLabels.stream().map(dbSxFillLabel -> {
            SxFillLabel label = new SxFillLabel();
            BeanUtils.copyProperties(dbSxFillLabel, label);
            label.setTypeName(sxFieldTypeManager.getSxFieldTypeByFieldTypeOid(label.getTypeOid()).getFieldTypeName());
            label.setServiceName(sxServiceManager.getSxServiceByOid(label.getServiceOid()).getServiceName());
            return label;
        }).collect(Collectors.toList());
        pageResult.setData(collect);
        return pageResult;
    }

    /***
     * @param labelOid
     * @description: 获取label
     * @return: com.zfsoft.formConfig.data.SxFillLabel
     * @author: xiayj
     * @date: 2021/7/15
     */
    public SxFillLabel getSxFillLabelByOid(String labelOid){
        DbSxFillLabel dbSxFillLabel = this.queryDbSxFillLabelByOid(labelOid);
        if(dbSxFillLabel==null){
            throw new ResultInfoException("找不到对应标签数据");
        }
        SxFillLabel sxFillLabel = new SxFillLabel();
        BeanUtils.copyProperties(dbSxFillLabel,sxFillLabel);
        sxFillLabel.setTypeName(sxFieldTypeManager.getSxFieldTypeByFieldTypeOid(sxFillLabel.getTypeOid()).getFieldTypeName());
        sxFillLabel.setServiceName(sxServiceManager.getSxServiceByOid(sxFillLabel.getServiceOid()).getServiceName());
        return sxFillLabel;
    }

    /***
     * @param sxFillLabel
     * @description: 保存label
     * @return: void
     * @author: xiayj
     * @date: 2021/7/15
     */
    @Transactional(rollbackFor = Exception.class)
    @ShardingTransactionType(TransactionType.LOCAL)
    public void saveOrUpdateSxFillLabel(SxFillLabel sxFillLabel){
        if(StringUtils.isEmpty(sxFillLabel.getServiceOid())){
            throw new ResultInfoException("所属事项不能为空");
        }
        if(StringUtils.isEmpty(sxFillLabel.getTypeOid())){
            throw new ResultInfoException("所属分类不能为空");
        }
        if(StringUtils.isEmpty(sxFillLabel.getLabelName())){
            throw new ResultInfoException("标签名称不能为空");
        }
        if(StringUtils.isEmpty(sxFillLabel.getLabelCode())){
            throw new ResultInfoException("标签编码不能为空");
        }
        DbSxFillLabel dbSxFillLabel;
        List<DbSxFillLabel> dbSxFillLabels = this.queryLabelByName(
                sxFillLabel.getServiceOid(),sxFillLabel.getTypeOid(),sxFillLabel.getLabelName());
        if(StringUtils.isEmpty(sxFillLabel.getLabelOid())){
            if(!dbSxFillLabels.isEmpty()){
                throw new ResultInfoException("对应标签名称已存在！");
            }
            dbSxFillLabel = new DbSxFillLabel();
            BeanUtils.copyProperties(sxFillLabel,dbSxFillLabel);
            dbSxFillLabel.setDelFlag(0);
            dbSxFillLabel.setCreateDate(new Date());
            dbSxFillLabel.setModifyDate(new Date());
            dbSxFillLabel.setLabelOid(IdUtil.simpleUUID());
            dbSxFillLabelMapper.insert(dbSxFillLabel);
        }else {
            if(!dbSxFillLabels.isEmpty()){
                if(!dbSxFillLabels.get(0).getLabelOid().equals(sxFillLabel.getLabelOid())){
                    throw new ResultInfoException("对应标签名称已存在！");
                }
            }
            dbSxFillLabel = this.queryDbSxFillLabelByOid(sxFillLabel.getLabelOid());
            if(dbSxFillLabel==null){
                throw new ResultInfoException("找不到对应标签数据");
            }
            //如果修改了类别，要对应修改字段类别
            if(!sxFillLabel.getTypeOid().equals(dbSxFillLabel.getTypeOid())){
                dbSxFillFieldMapper.updateTypeByLabelOid(sxFillLabel.getTypeOid(),dbSxFillLabel.getLabelOid());
            }
            BeanUtils.copyProperties(sxFillLabel,dbSxFillLabel);
            dbSxFillLabel.setModifyDate(new Date());
            dbSxFillLabelMapper.updateByPrimaryKeySelective(dbSxFillLabel);
        }
    }

    /***
     * @param labelOid
     * @description:删除label
     * @return: void
     * @author: xiayj
     * @date: 2021/7/14
     */
    public void deleteSxFillLabel(String labelOid){
        if(StringUtils.isEmpty(labelOid)){
            throw new ResultInfoException("找不到删除主键");
        }
        List<DbSxFillField> dbSxFillFields = dbSxFillFieldMapper.queryDbSxFillFieldByLableOid(labelOid);
        if(!dbSxFillFields.isEmpty()){
            throw new ResultInfoException("存在关联的表单字段，无法删除");
        }
        String[] split = labelOid.split(",");
        DbSxFillLabelExample example = new DbSxFillLabelExample();
        DbSxFillLabelExample.Criteria criteria = example.createCriteria();
        criteria.andLabelOidIn(Arrays.asList(split));
        criteria.andDelFlagEqualTo(0);
        List<DbSxFillLabel> dbSxFillLabels = dbSxFillLabelMapper.selectByExample(example);
        for(DbSxFillLabel dbSxFillLabel:dbSxFillLabels){
            dbSxFillLabel.setDelFlag(1);
            dbSxFillLabelMapper.updateByPrimaryKeySelective(dbSxFillLabel);
        }
    }

    /***
     * @description: 查询字段分类和标签树状关联数据
     * @return: java.util.List<com.zfsoft.dataChange.data.FieldTree>
     * @author: xiayj
     * @date: 2021/7/16
     */
    public List<FieldTree> querySxFieldTypeAndLabelTree(String serviceOid){
        SxFieldType type = new SxFieldType();
        type.setIsAble(1);
        List<SxFieldType> sxFieldTypes = sxFieldTypeManager.querySxFieldTypeList(type);
        List<FieldTree> sxFillLabels = this.querySxFillLabel(serviceOid);
        Map<String, List<FieldTree>> collect = sxFillLabels.stream().collect(Collectors.groupingBy(FieldTree::getParentOid));
        Iterator<SxFieldType> iterator = sxFieldTypes.iterator();
        List<FieldTree> result = new ArrayList<>();
        while (iterator.hasNext()){
            SxFieldType sxFieldType = iterator.next();
            List<FieldTree> fieldTrees = collect.get(sxFieldType.getFieldTypeOid());
            if(fieldTrees==null){
                iterator.remove();
                continue;
            }
            FieldTree fieldTree = new FieldTree();
            fieldTree.setLabel(sxFieldType.getFieldTypeName());
            fieldTree.setValue(sxFieldType.getFieldTypeOid());
            fieldTree.setChildren(fieldTrees);
            result.add(fieldTree);
        }
        return result;
    }

    /***
     *
     * 根据事项查询标签列表
     * @param serviceOid
     * @return: java.util.List<com.zfsoft.formConfig.data.SxFillLabel>
     * @author: xiayj
     * @date: 2021/8/4
     */
    public List<SxFillLabel> querySxFillLabelList(String serviceOid,String typeOid){
        DbSxFillLabelExample example = new DbSxFillLabelExample();
        DbSxFillLabelExample.Criteria criteria = example.createCriteria();
        criteria.andServiceOidEqualTo(serviceOid);
        if(StringUtils.isNotEmpty(typeOid)){
            criteria.andTypeOidEqualTo(typeOid);
        }
        criteria.andDelFlagEqualTo(0);
        return dbSxFillLabelMapper.selectByExample(example).stream().map(dbSxFillLabel -> {
            SxFillLabel label = new SxFillLabel();
            BeanUtils.copyProperties(dbSxFillLabel, label);
            return label;
        }).collect(Collectors.toList());
    }

    private DbSxFillLabel queryDbSxFillLabelByOid(String oid){
        DbSxFillLabelExample example = new DbSxFillLabelExample();
        DbSxFillLabelExample.Criteria criteria = example.createCriteria();
        criteria.andLabelOidEqualTo(oid);
        List<DbSxFillLabel> dbSxFillLabels = dbSxFillLabelMapper.selectByExample(example);
        if(dbSxFillLabels.isEmpty()){
            return null;
        }
        return dbSxFillLabels.get(0);
    }

    private List<FieldTree> querySxFillLabel(String serviceOid){
        DbSxFillLabelExample example = new DbSxFillLabelExample();
        DbSxFillLabelExample.Criteria criteria = example.createCriteria();
        criteria.andDelFlagEqualTo(0);
        criteria.andServiceOidEqualTo(serviceOid);
        return dbSxFillLabelMapper.selectByExample(example).stream().map(dbSxFillLabel -> {
            FieldTree fieldTree = new FieldTree();
            fieldTree.setLabel(dbSxFillLabel.getLabelName());
            fieldTree.setValue(dbSxFillLabel.getLabelOid());
            fieldTree.setParentOid(dbSxFillLabel.getTypeOid());
            return fieldTree;
        }).collect(Collectors.toList());
    }

    /***
     * @param name
     * @description: 根据名字查询label
     * @return: java.util.List<com.zfsoft.dbaccess.data.formConfig.DbSxFillLabel>
     * @author: xiayj
     * @date: 2021/7/27
     */
    private List<DbSxFillLabel> queryLabelByName(String serviceOid,String fieldTypeOid,String name){
        DbSxFillLabelExample example = new DbSxFillLabelExample();
        DbSxFillLabelExample.Criteria criteria = example.createCriteria();
        criteria.andDelFlagEqualTo(0);
        criteria.andServiceOidEqualTo(serviceOid);
        criteria.andLabelNameEqualTo(name);
        criteria.andTypeOidEqualTo(fieldTypeOid);
        return dbSxFillLabelMapper.selectByExample(example);
    }



    public List<FieldTree> querySxFieldTypeAndLabelTreeNew(String serviceOid){
        SxFieldType type = new SxFieldType();
        type.setIsAble(1);
        List<SxFieldType> sxFieldTypes = sxFieldTypeManager.querySxFieldTypeList(type);
        List<FieldTree> sxFillLabels = this.querySxFillLabel(serviceOid);
        Map<String, List<FieldTree>> collect = sxFillLabels.stream().collect(Collectors.groupingBy(FieldTree::getParentOid));
        Iterator<SxFieldType> iterator = sxFieldTypes.iterator();
        List<FieldTree> result = new ArrayList<>();
        while (iterator.hasNext()){
            SxFieldType sxFieldType = iterator.next();
            List<FieldTree> fieldTrees = collect.get(sxFieldType.getFieldTypeOid());
            if(fieldTrees==null){
                iterator.remove();
                continue;
            }
            FieldTree fieldTree = new FieldTree();
            fieldTree.setLabel(sxFieldType.getFieldTypeName());
            fieldTree.setValue(sxFieldType.getFieldTypeOid());
            fieldTree.setChildren(fieldTrees);
            result.add(fieldTree);
        }
        return result;
    }



    public List<FieldTree> querySxFieldTypeAndLabelAndSxFillFieldTree(String serviceOid){
        SxFieldType type = new SxFieldType();
        type.setIsAble(1);
        List<SxFieldType> sxFieldTypes = sxFieldTypeManager.querySxFieldTypeList(type);
        List<FieldTree> sxFillLabels = this.querySxFillLabel(serviceOid);
        Map<String, List<FieldTree>> collect = sxFillLabels.stream().collect(Collectors.groupingBy(FieldTree::getParentOid));
        Iterator<SxFieldType> iterator = sxFieldTypes.iterator();
        List<FieldTree> result = new ArrayList<>();
        while (iterator.hasNext()){
            SxFieldType sxFieldType = iterator.next();
            List<FieldTree> fieldTrees = collect.get(sxFieldType.getFieldTypeOid());
            if(fieldTrees==null){
                iterator.remove();
                continue;
            }else{
                for(FieldTree fieldTree1:fieldTrees){
                    String fieldTypeOid=sxFieldType.getFieldTypeOid();
                    String labelOid=fieldTree1.getValue();
                    String labelName=fieldTree1.getLabel();
                    List<SxFillField> sxFillFieldList=this.queryFieldListByOids(serviceOid,fieldTypeOid,labelOid);
                    List<FieldTree> childList=new ArrayList<>();
                    for(SxFillField sxFillField:sxFillFieldList){
                        FieldTree childfieldTree = new FieldTree();
                        String fieldCode=sxFillField.getFieldCode();
                        fieldCode=this.changeToJavaFiled(fieldCode);
                        childfieldTree.setValue(sxFillField.getFieldOid()+";"+fieldCode+";"+sxFillField.getFieldName());
                        childfieldTree.setLabel(sxFillField.getFieldName());
                        childfieldTree.setParentOid(labelOid);
                        childList.add(childfieldTree);
                    }
                    fieldTree1.setValue(labelOid+";"+labelName);
                    fieldTree1.setChildren(childList);
                }
            }
            FieldTree fieldTree = new FieldTree();
            fieldTree.setLabel(sxFieldType.getFieldTypeName());
            fieldTree.setValue(sxFieldType.getFieldTypeOid()+";"+sxFieldType.getFieldTypeName());
            fieldTree.setChildren(fieldTrees);

            result.add(fieldTree);
        }
        return result;
    }
    /**
     * @description:  驼峰命名转换
     * @param field 字段名称
     * @author: wuxx
     * @Date: 2021/4/14 15:55
     **/
    public String changeToJavaFiled(String field) {
        if (!field.contains("_")){
            return field.toLowerCase();
        }
        String[] fields = field.toLowerCase().split("_");
        StringBuilder sbuilder = new StringBuilder(fields[0]);
        for (int i = 1; i < fields.length; i++) {
            char[] cs = fields[i].toCharArray();
            cs[0] -= 32;
            sbuilder.append(String.valueOf(cs));
        }
        return sbuilder.toString();
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



}
