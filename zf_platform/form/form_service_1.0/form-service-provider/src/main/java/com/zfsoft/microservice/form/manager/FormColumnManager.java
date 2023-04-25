package com.zfsoft.microservice.form.manager;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zfsoft.microservice.form.data.FormColumn;
import com.zfsoft.microservice.form.data.FormDataSource;
import com.zfsoft.microservice.form.data.FormObjectExtand;
import com.zfsoft.microservice.form.dbaccess.dao.DbFormColumnMapper;
import com.zfsoft.microservice.form.dbaccess.data.DbFormColumn;
import com.zfsoft.microservice.form.dbaccess.data.DbFormColumnExample;
import com.zfsoft.microservice.form.util.DBHelper;
import com.zfsoft.platform.common.data.BaseStaticParameter;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.platform.utils.validate.ParamValid;
import com.zfsoft.platform.utils.validate.ResultInfoException;
import com.zfsoft.platform.utils.validate.ValidGroups;
import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.transaction.annotation.ShardingTransactionType;
import org.apache.shardingsphere.transaction.core.TransactionType;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @ClassName FormColumnManager
 * @Description: 表结构接口实现类
 * @Author wuxx
 * @Date 2021/4/13 15:00
 **/
@Service
@Slf4j
@CacheConfig(cacheNames = "form:column")
public class FormColumnManager {

    @Resource
    private DbFormColumnMapper dbFormColumnMapper;

    @Resource
    private FormDataSourceManager formDataSourceManager;

    @Resource
    @Lazy
    private FormObjectExtandManager formObjectExtandManager;

    /**
     * @description:  保存表结构信息
     * @param formColumn 表结构对象
     * @author: wuxx
     * @Date: 2021/4/13 15:00
     **/
    @Transactional
    @ParamValid
    @ShardingTransactionType(TransactionType.LOCAL)
    @CacheEvict(allEntries = true)
    public int saveFormColumn(@ValidGroups(groups = {FormColumn.INSERT_GROUP.class}) FormColumn formColumn) {
        if(null == formColumn){
            throw new ResultInfoException("表结构对象不能为空！");
        }
        if (null == formColumn.getId()) {
            formColumn.setId(null);
            formColumn.setColumnOid(IdUtil.simpleUUID());
        } else {
            // 对象不为空
            FormColumn curDict = this.getFormColumnById(formColumn.getId());
            if (curDict == null) {
                throw new ResultInfoException("表结构对象未查询到相应的信息!");
            }
        }
        DbFormColumn dbFormColumn = new DbFormColumn();
        BeanUtils.copyProperties(formColumn,dbFormColumn);
        int index=0;
        if (null == formColumn.getId()) {
            index = dbFormColumnMapper.insert(dbFormColumn);
        }else {
            index = dbFormColumnMapper.updateByPrimaryKeySelective(dbFormColumn);
        }
        formColumn.setId(dbFormColumn.getId());
        return index;
    }

    @Transactional
    @ParamValid
    @ShardingTransactionType(TransactionType.LOCAL)
    @CacheEvict(allEntries = true)
    public int updateFormColumn(@ValidGroups(groups = {FormColumn.UPDATE_GROUP.class}) FormColumn formColumn) {
        DbFormColumn dbFormColumn = new DbFormColumn();
        BeanUtils.copyProperties(formColumn,dbFormColumn);
        int index = dbFormColumnMapper.updateByPrimaryKeySelective(dbFormColumn);
        return index;
    }
    /**
     * @description: 根据主键获取对象信息
     * @param id 主键id
     * @author: wuxx
     * @Date: 2021/4/13 15:00
     **/
    @Cacheable(key = "'getFormColumnById:'+#id", unless = "#result == null")
    public FormColumn getFormColumnById(Long id) {
        DbFormColumn dbFormColumn = dbFormColumnMapper.selectByPrimaryKey(id);
        if(dbFormColumn == null)
            return null;
        FormColumn formColumn = new FormColumn();
        BeanUtils.copyProperties(dbFormColumn,formColumn);
        return formColumn;
    }

    /**
     * @description: 根据对象主键删除表结构
     * @param objectOid 对象主键
     * @author: wuxx
     * @Date: 2021/4/15 14:18
     **/
    public int deleteFormColumnByObjectOid(String objectOid) {
        if(StrUtil.isBlank(objectOid)){
            return 0;
        }
        DbFormColumnExample dbFormColumnExample = new DbFormColumnExample();
        DbFormColumnExample.Criteria criteria = dbFormColumnExample.createCriteria();
        criteria.andObjectOidEqualTo(objectOid);
        int delete = dbFormColumnMapper.deleteByExample(dbFormColumnExample);
        return delete;
    }

    /**
     * 删除扩展列
     * @param extandOid
     * @return
     */
    public int deleteFormColumnByExtandOid(String extandOid) {
        if(StrUtil.isBlank(extandOid)){
            return 0;
        }
        DbFormColumnExample dbFormColumnExample = new DbFormColumnExample();
        DbFormColumnExample.Criteria criteria = dbFormColumnExample.createCriteria();
        criteria.andExtandOidEqualTo(extandOid);
        int delete = dbFormColumnMapper.deleteByExample(dbFormColumnExample);
        return delete;
    }

    /**
     * @description: 根据业务主键获取对象信息
     * @param columnOid 业务主键id
     * @author: wuxx
     * @Date: 2021/4/13 15:00
     **/
    @Cacheable(key = "'getFormColumnByColumnOid:'+#columnOid", unless = "#result == null")
    public FormColumn getFormColumnByColumnOid(String columnOid) {
        DbFormColumn dbFormColumn = dbFormColumnMapper.selectByForeignKey(columnOid);
        if(dbFormColumn == null)
            return null;
        FormColumn formColumn = new FormColumn();
        BeanUtils.copyProperties(dbFormColumn,formColumn);
        return formColumn;
    }

    /**
     * @description: 分页查询表结构的列表
     * @author: wuxx
     * @Date: 2021/4/13 15:00
     **/
    public PageResult<FormColumn> queryFormColumnWithPage(FormColumn formColumn, Integer pageNumber, Integer pageSize) {
        //分页参数
        if (null == pageNumber || pageNumber <= 1) {
            pageNumber = 1;
        }
        if (null == pageSize || pageSize <= 0) {
            pageSize = 10;
        }
        PageHelper.startPage(pageNumber,pageSize);
        DbFormColumnExample dbFormColumnExample = new DbFormColumnExample();
        DbFormColumnExample.Criteria criteria = dbFormColumnExample.createCriteria();
        if(null!=formColumn){
            if(StrUtil.isNotEmpty(formColumn.getObjectOid())){
                criteria.andObjectOidEqualTo(formColumn.getObjectOid());
            }
            if(StrUtil.isNotEmpty(formColumn.getDatasourceOid())){
                criteria.andDatasourceOidEqualTo(formColumn.getDatasourceOid());
            }
        }
        Page<DbFormColumn> dbFormColumns = (Page<DbFormColumn>)dbFormColumnMapper.selectByExample(dbFormColumnExample);
        PageResult<FormColumn> pageResult = new PageResult<>(dbFormColumns.getPageNum(),dbFormColumns.getPageSize(),dbFormColumns.getTotal());
        List<FormColumn> formColumnList = dbFormColumns.stream().map(dbFormColumn -> {
            FormColumn object = new FormColumn();
            BeanUtils.copyProperties(dbFormColumn,object);
            if(null == object.getDataType()){
                object.setDataType(BaseStaticParameter.N);
            }
            return object;
        }).collect(Collectors.toList());
        pageResult.setData(formColumnList);
        return pageResult;
    }

    /**
     * @description: 根据表结构对象查询集合
     * @param formColumn 表结构对象
     * @author: wuxx
     * @Date: 2021/4/13 15:00
     **/
    public List<FormColumn> queryFormColumnList(FormColumn formColumn) {
        DbFormColumnExample dbFormColumnExample = new DbFormColumnExample();
        DbFormColumnExample.Criteria criteria = dbFormColumnExample.createCriteria();
        if(null!=formColumn){
            if(StrUtil.isNotEmpty(formColumn.getObjectOid())){
                criteria.andObjectOidEqualTo(formColumn.getObjectOid());
            }
            if(StrUtil.isNotEmpty(formColumn.getDatasourceOid())){
                criteria.andDatasourceOidEqualTo(formColumn.getDatasourceOid());
            }
            if(StrUtil.isNotEmpty(formColumn.getObjectProperty())){
                criteria.andObjectPropertyEqualTo(formColumn.getObjectProperty());
            }
            if(StrUtil.isNotBlank(formColumn.getExtandOid())){
                criteria.andExtandOidEqualTo(formColumn.getExtandOid());
            }else{
                criteria.andExtandOidIsNull();
            }
        }
        List<DbFormColumn> dbFormColumnList = dbFormColumnMapper.selectByExample(dbFormColumnExample);
        List<FormColumn> formColumnList = dbFormColumnList.stream().map(dbFormColumn -> {
            FormColumn object = new FormColumn();
            BeanUtils.copyProperties(dbFormColumn,object);
            if(null == object.getDataType()){
                object.setDataType(BaseStaticParameter.N);
            }
            return object;
        }).collect(Collectors.toList());
        return formColumnList;
    }

    /**
     * 查询扩展对象列
     *
     * @date 2021-07-28 10:59
     * @param formColumn
     * @return
     */
    public List<FormColumn> queryFormObjectExtandColumnList(FormColumn formColumn) {
        DbFormColumnExample dbFormColumnExample = new DbFormColumnExample();
        DbFormColumnExample.Criteria criteria = dbFormColumnExample.createCriteria();
        if(null!=formColumn){
            if(StrUtil.isNotEmpty(formColumn.getObjectOid())){
                criteria.andObjectOidEqualTo(formColumn.getObjectOid());
            }
            if(StrUtil.isNotEmpty(formColumn.getDatasourceOid())){
                criteria.andDatasourceOidEqualTo(formColumn.getDatasourceOid());
            }
            criteria.andExtandOidIsNotNull();
            if(StrUtil.isNotBlank(formColumn.getExtandOid())){
                criteria.andExtandOidEqualTo(formColumn.getExtandOid());
            }
        }
        List<DbFormColumn> dbFormColumnList = dbFormColumnMapper.selectByExample(dbFormColumnExample);
        List<FormColumn> formColumnList = dbFormColumnList.stream().map(dbFormColumn -> {
            FormColumn object = new FormColumn();
            BeanUtils.copyProperties(dbFormColumn,object);
            if(null == object.getDataType()){
                object.setDataType(BaseStaticParameter.N);
            }
            return object;
        }).collect(Collectors.toList());
        return formColumnList;
    }

    /**
     * @description:  根据对象主键查询表结构字符串包括关联
     * @param objectOid 对象主键
     * @author: wuxx
     * @Date: 2021/12/3 16:14
     **/
    public List<String> queryFormColumnStrByObjectOid(String objectOid) {
        List<String> columnStrList = new ArrayList<>();
        FormColumn formColumn = new FormColumn();
        formColumn.setObjectOid(objectOid);
        List<FormColumn> formColumnList = this.queryFormColumnList(formColumn);
        List<String> formColumnStrList = formColumnList.stream().map(FormColumn::getObjectProperty).collect(Collectors.toList());
        List<FormObjectExtand> formObjectExtands = formObjectExtandManager.queryFormObjectExtandList(objectOid, null);
        List<String> formObjectExtandsStrList = formObjectExtands.stream().map(FormObjectExtand::getVariableName).collect(Collectors.toList());
        columnStrList.addAll(formColumnStrList);
        columnStrList.addAll(formObjectExtandsStrList);
        return columnStrList;
    }

    /**
     * @description: 根据数据库主键获取数据表结构
     * @param datasourceOid 数据库主键id
     * @param isNotChangeFiledName 是否数据库字段名和表单字段名 是否不用转换  1不用转换  其他需要转换  默认需要转换
     * @author: wuxx
     * @Date: 2021/4/13 15:00
     **/
    public List<FormColumn> getDatasourceColumn(String datasourceOid,String objectForm,Integer isNotChangeFiledName) {
        FormDataSource dataSource = formDataSourceManager.getFormDataSourceByDataSourceOid(datasourceOid);
        if (null==dataSource){
            return null;
        }
        DBHelper dBHelper = new DBHelper(dataSource);
        List<Map<String, Object>> mapList = dBHelper.getColumnsByTableName(objectForm,true);
        List<FormColumn> formColumnList = new ArrayList<>();
        if(null!=mapList && mapList.size()>0){
            for (Map<String, Object> columnMap : mapList){
                String columnName = columnMap.get("columnName").toString();
                String objectProperty = dBHelper.changeToJavaFiled(columnName);
                if (isNotChangeFiledName != null && isNotChangeFiledName == 1){
                    objectProperty = columnName;
                }
                FormColumn column = (FormColumn) dBHelper.mapToObject(columnMap, FormColumn.class);
                column.setObjectProperty(objectProperty);
                column.setDataType(0);
                formColumnList.add(column);
            }
        }
        //dBHelper.closeConnection(connection);
        return formColumnList;
    }

}
