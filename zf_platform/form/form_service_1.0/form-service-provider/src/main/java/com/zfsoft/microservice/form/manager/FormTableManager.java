package com.zfsoft.microservice.form.manager;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zfsoft.microservice.form.data.FormDataSource;
import com.zfsoft.microservice.form.data.FormTable;
import com.zfsoft.microservice.form.data.vo.CreateFormTableVo;
import com.zfsoft.microservice.form.data.vo.FormTableDto;
import com.zfsoft.microservice.form.dbaccess.dao.DbFormTableMapper;
import com.zfsoft.microservice.form.dbaccess.data.DbFormTable;
import com.zfsoft.microservice.form.dbaccess.data.DbFormTableExample;
import com.zfsoft.microservice.form.util.DBHelper;
import com.zfsoft.microservice.form.util.MongoDBUtil;
import com.zfsoft.microservice.form.util.dataSourceUtil.OperationTable;
import com.zfsoft.microservice.form.util.dataSourceUtil.OperatorTableFactory;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @ClassName FormTableManager
 * @Description: 创建表接口实现类
 * @Author wuxx
 * @Date 2021/9/8 15:00
 **/
@Service
@Slf4j
@CacheConfig(cacheNames = "form:table")
public class FormTableManager {

    @Resource
    private DbFormTableMapper dbFormTableMapper;

    @Resource
    private FormDataSourceManager formDataSourceManager;

    /**
     * @description:  保存创建表信息
     * @param formTable 创建表对象
     * @author: wuxx
     * @Date: 2021/9/8 15:00
     **/
    @Transactional
    @ParamValid
    @ShardingTransactionType(TransactionType.LOCAL)
    @CacheEvict(allEntries = true)
    public FormTable saveFormTable(@ValidGroups(groups = {FormTable.INSERT_GROUP.class}) FormTable formTable) {
        if(null == formTable){
            throw new ResultInfoException("创建表对象不能为空！");
        }
        if (null == formTable.getId()) {
            formTable.setId(null);
            formTable.setTableOid(IdUtil.simpleUUID());
        } else {
            // 对象不为空
            FormTable curDict = this.getFormTableById(formTable.getId());
            if (curDict == null) {
                throw new ResultInfoException("创建表对象未查询到相应的信息!");
            }
        }
        DbFormTable dbFormTable = new DbFormTable();
        BeanUtils.copyProperties(formTable,dbFormTable);
        int index=0;
        if (null == formTable.getId()) {
            index = dbFormTableMapper.insert(dbFormTable);
        }else {
            index = dbFormTableMapper.updateByPrimaryKeySelective(dbFormTable);
        }
        formTable.setId(dbFormTable.getId());
        return formTable;
    }

    /**
     * @description:  保存创建表信息createFormTableVo
     * @param createFormTableVo 创建表对象
     * @author: wuxx
     * @Date: 2021/9/8 15:00
     **/
    @Transactional
    @CacheEvict(allEntries = true)
    public void saveFormTableVo(CreateFormTableVo createFormTableVo) {
        if(null == createFormTableVo){
            throw new ResultInfoException("创建表信息不能为空！");
        }
        List<FormTable> formTableList = createFormTableVo.getFormTableList();
        if(null!=formTableList && formTableList.size()==0){
            throw new ResultInfoException("创建表结构不能为空！");
        }
        String tableName = createFormTableVo.getTableName();
        String datasourceOid = createFormTableVo.getDatasourceOid();
        if(StrUtil.isBlank(tableName)){
            throw new ResultInfoException("参数tableName不能为空!");
        }
        if(StrUtil.isBlank(datasourceOid)){
            throw new ResultInfoException("参数datasourceOid不能为空!");
        }
        FormDataSource source = formDataSourceManager.getFormDataSourceByDataSourceOid(datasourceOid);
        if(null==source){
            throw new ResultInfoException("参数datasourceOid不正确!");
        }
        //先删除所有的表
        this.deleteFormTable(tableName,createFormTableVo.getDatasourceOid());
        List<FormTableDto> formTableDtos = new ArrayList<>();
        for (FormTable table : formTableList){
            //保存数据到表
            table.setDatasourceOid(datasourceOid);
            table.setTableName(tableName);
            table.setCreateDate(new Date());
            table.setId(null);
            this.saveFormTable(table);

            FormTableDto formTableDto = new FormTableDto();
            formTableDto.setTableName(tableName);
            formTableDto.setColumnName(table.getColumnName());
            formTableDto.setColumnType(table.getColumnType());
            formTableDto.setDemo(null==table.getDemo()?"":table.getDemo());
            formTableDto.setMaxLenth(table.getMaxLength());
            formTableDto.setIndexFlag(null!=table.getIndexFlag() && BaseStaticParameter.Y==table.getIndexFlag().intValue());
            formTableDto.setIsNotNull(null!=table.getIsNotNull() && BaseStaticParameter.Y==table.getIsNotNull().intValue());
            formTableDtos.add(formTableDto);
        }
        DBHelper dbHelper = new DBHelper(source);
        OperationTable operationTable = OperatorTableFactory.getOperation(source.getType());
        List<String> tableSqlList = null;
        //查询表是否有记录
        List<Map<String, Object>> mapList = dbHelper.getColumnsByTableName(tableName,false);
        //是否是新建 默认为0新建   1修改
        if(null!=createFormTableVo.getIsCreate() && BaseStaticParameter.Y == createFormTableVo.getIsCreate().intValue()){
            //修改
            List<FormTableDto> dataSourceList = new ArrayList<>();
            if(null!=mapList && mapList.size()>0){
                for (Map<String, Object> columnMap : mapList){
                    FormTableDto formColumnVo = new FormTableDto();
                    String columnName = columnMap.get("columnName").toString();
                    formColumnVo.setColumnName(columnName);
                    dataSourceList.add(formColumnVo);
                }
                tableSqlList = operationTable.updateOrDelFormTableSql(tableName, formTableDtos, dataSourceList);
            }else{
                tableSqlList = operationTable.createFormTableSql(tableName, formTableDtos,createFormTableVo.getIdIsVarchar());
            }
        }else {
            if(null!=mapList && mapList.size()>0){
                throw new ResultInfoException("数据库表"+tableName+"已经存在!");
            }
            //新建
            tableSqlList = operationTable.createFormTableSql(tableName, formTableDtos,createFormTableVo.getIdIsVarchar());
        }
        //System.err.println(JSONUtil.toJsonStr(tableSqlList));
        if(null!=tableSqlList){
            Map<String, Object> map = null;
            //mongdb
            if("mongodb".equals(source.getType().toLowerCase())){
                //throw new ResultInfoException("mongodb数据库暂不支持生成文档!");
                MongoDBUtil mongoDBUtil = new MongoDBUtil(source.getHost(),source.getPort(),
                        source.getDatasourceName(),source.getUsername(),source.getPassword());
                map =  mongoDBUtil.executeBatchByList(tableName,tableSqlList);
            }else{
                map = dbHelper.executeBatchBySql(tableSqlList);
            }
            Boolean success = (Boolean) map.get("success");
            if(!success){
                String message = map.get("message").toString();
                if(!message.contains("ORA-00942")){
                    throw new ResultInfoException(message);
                }
            }
        }
    }

    @Transactional
    @ParamValid
    @ShardingTransactionType(TransactionType.LOCAL)
    @CacheEvict(allEntries = true)
    public int updateFormTable(@ValidGroups(groups = {FormTable.UPDATE_GROUP.class}) FormTable formTable) {
        DbFormTable dbFormTable = new DbFormTable();
        BeanUtils.copyProperties(formTable,dbFormTable);
        int index = dbFormTableMapper.updateByPrimaryKeySelective(dbFormTable);
        return index;
    }
    /**
     * @description: 根据主键获取对象信息
     * @param id 主键id
     * @author: wuxx
     * @Date: 2021/9/8 15:00
     **/
    @Cacheable(key = "'getFormTableById:'+#id", unless = "#result == null")
    public FormTable getFormTableById(Long id) {
        DbFormTable dbFormTable = dbFormTableMapper.selectByPrimaryKey(id);
        if(dbFormTable == null)
            return null;
        FormTable formTable = new FormTable();
        BeanUtils.copyProperties(dbFormTable,formTable);
        return formTable;
    }

    /**
     * @description: 根据对象删除创建表
     * @author: wuxx
     * @Date: 2021/9/8 14:18
     **/
    public int deleteFormTable(String tableName,String datasourceOid) {
        if(StrUtil.isBlank(tableName) || StrUtil.isBlank(datasourceOid)){
            return 0;
        }
        DbFormTableExample dbFormTableExample = new DbFormTableExample();
        DbFormTableExample.Criteria criteria = dbFormTableExample.createCriteria();
        criteria.andTableNameEqualTo(tableName);
        criteria.andDatasourceOidEqualTo(datasourceOid);
        int delete = dbFormTableMapper.deleteByExample(dbFormTableExample);
        return delete;
    }


    /**
     * @description: 根据业务主键获取对象信息
     * @param tableOid 业务主键id
     * @author: wuxx
     * @Date: 2021/9/8 15:00
     **/
    @Cacheable(key = "'getFormTableByTableOid:'+#tableOid", unless = "#result == null")
    public FormTable getFormTableByTableOid(String tableOid) {
        DbFormTable dbFormTable = dbFormTableMapper.selectByForeignKey(tableOid);
        if(dbFormTable == null)
            return null;
        FormTable formTable = new FormTable();
        BeanUtils.copyProperties(dbFormTable,formTable);
        return formTable;
    }

    /**
     * @description: 分页查询创建表的列表
     * @author: wuxx
     * @Date: 2021/9/8 15:00
     **/
    public PageResult<FormTable> queryFormTableWithPage(FormTable formTable, Integer pageNumber, Integer pageSize) {
        //分页参数
        if (null == pageNumber || pageNumber <= 1) {
            pageNumber = 1;
        }
        if (null == pageSize || pageSize <= 0) {
            pageSize = 10;
        }
        PageHelper.startPage(pageNumber,pageSize);
        DbFormTableExample dbFormTableExample = new DbFormTableExample();
        DbFormTableExample.Criteria criteria = dbFormTableExample.createCriteria();
        if(null!=formTable){
            if(StrUtil.isNotEmpty(formTable.getTableOid())){
                criteria.andTableOidEqualTo(formTable.getTableOid());
            }
            if(StrUtil.isNotEmpty(formTable.getDatasourceOid())){
                criteria.andDatasourceOidEqualTo(formTable.getDatasourceOid());
            }
        }
        Page<DbFormTable> dbFormTables = (Page<DbFormTable>)dbFormTableMapper.selectByExample(dbFormTableExample);
        PageResult<FormTable> pageResult = new PageResult<>(dbFormTables.getPageNum(),dbFormTables.getPageSize(),dbFormTables.getTotal());
        List<FormTable> formTableList = dbFormTables.stream().map(dbFormTable -> {
            FormTable object = new FormTable();
            BeanUtils.copyProperties(dbFormTable,object);
            return object;
        }).collect(Collectors.toList());
        pageResult.setData(formTableList);
        return pageResult;
    }

    /**
     * @description: 根据创建表对象查询集合
     * @param formTable 创建表对象
     * @author: wuxx
     * @Date: 2021/9/8 15:00
     **/
    public List<FormTable> queryFormTableList(FormTable formTable) {
        DbFormTableExample dbFormTableExample = new DbFormTableExample();
        DbFormTableExample.Criteria criteria = dbFormTableExample.createCriteria();
        if(null!=formTable){
            if(StrUtil.isNotEmpty(formTable.getTableOid())){
                criteria.andTableOidEqualTo(formTable.getTableOid());
            }
            if(StrUtil.isNotEmpty(formTable.getTableName())){
                criteria.andTableNameEqualTo(formTable.getTableName());
            }
            if(StrUtil.isNotEmpty(formTable.getDatasourceOid())){
                criteria.andDatasourceOidEqualTo(formTable.getDatasourceOid());
            }
        }
        List<DbFormTable> dbFormTableList = dbFormTableMapper.selectByExample(dbFormTableExample);
        List<FormTable> formTableList = dbFormTableList.stream().map(dbFormTable -> {
            FormTable object = new FormTable();
            BeanUtils.copyProperties(dbFormTable,object);
            return object;
        }).collect(Collectors.toList());
        return formTableList;
    }


}
