package com.zfsoft.microservice.form.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.zfsoft.microservice.form.data.*;
import com.zfsoft.microservice.form.data.vo.*;
import com.zfsoft.microservice.form.manager.*;
import com.zfsoft.microservice.form.service.FormTableService;
import com.zfsoft.microservice.form.util.DBHelper;
import com.zfsoft.microservice.form.util.MongoDBUtil;
import com.zfsoft.microservice.form.util.dataSourceUtil.OperationTable;
import com.zfsoft.microservice.form.util.dataSourceUtil.OperatorTableFactory;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.data.Builder;
import com.zfsoft.platform.utils.validate.ResultInfoException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @description: 创建表的实现类
 * @author: wuxx
 * @Date: 2021/8/4 10:14
 **/
@RestController
@Slf4j
@RefreshScope
public class FormTableController implements FormTableService {

    @Resource
    private FormDataSourceManager formDataSourceManager;

    @Resource
    private FormColumnManager formColumnManager;

    @Resource
    private FormObjectManager formObjectManager;

    @Resource
    private FormMainManager formMainManager;

    @Resource
    private FormTableManager formTableManager;

    @Resource
    private FormObjectExtandManager formObjectExtandManager;

    @Override
    public ApiResultSet createTableByFormTableDto(FormTableDtoParams formTableDtoParams) {
        if (StrUtil.isBlank(formTableDtoParams.getFormTableDtoJson())) {
            throw new ResultInfoException("参数formTableDtoJson不能为空!");
        }
        if (StrUtil.isBlank(formTableDtoParams.getDatasourceOid())) {
            throw new ResultInfoException("参数datasourceOid不能为空!");
        }
        String tableName = formTableDtoParams.getTableName();
        if (StrUtil.isBlank(tableName)) {
            throw new ResultInfoException("参数tableName不能为空!");
        }
        FormDataSource source = formDataSourceManager.getFormDataSourceByDataSourceOid(formTableDtoParams.getDatasourceOid());
        if (null == source) {
            throw new ResultInfoException("参数datasourceOid不正确!");
        }
        try {
            JSONArray array = JSONUtil.parseArray(formTableDtoParams.getFormTableDtoJson());
            List<FormTableDto> formColumnVoList = array.toList(FormTableDto.class);
            List<String> tableSqlList = null;
            DBHelper dbHelper = new DBHelper(source);
            //查询表是否有记录
            List<Map<String, Object>> mapList = dbHelper.getColumnsByTableName(tableName, false);
            OperationTable operationTable = OperatorTableFactory.getOperation(source.getType());
            String formCode = formTableDtoParams.getFormCode();
            String objectOid = null;
            if(StrUtil.isNotBlank(formTableDtoParams.getObjectOid())){
                objectOid=formTableDtoParams.getObjectOid();
            }
            if (StrUtil.isNotEmpty(formCode) && StrUtil.isNotBlank(formCode)) {
                FormMain mainCode = formMainManager.getFormMainByFormMainCode(formCode);
                if (null != mainCode && StrUtil.isNotEmpty(mainCode.getObjectOid())) {
                    objectOid = mainCode.getObjectOid();
                }
            }

            if (StrUtil.isNotEmpty(objectOid) && StrUtil.isNotBlank(objectOid)) {
                FormObject object = formObjectManager.getFormObjectByObjectOid(objectOid);
                String objectForm = null != object ? object.getObjectForm() : "";
                //修改
                if (null != objectForm && objectForm.equals(tableName)) {
                    List<FormTableDto> dataSourceList = new ArrayList<>();
                    if (null != mapList && mapList.size() > 0) {
                        for (Map<String, Object> columnMap : mapList) {
                            FormTableDto formColumnVo = new FormTableDto();
                            String columnName = columnMap.get("columnName").toString();
                            formColumnVo.setColumnName(columnName);
                            dataSourceList.add(formColumnVo);
                        }
                        tableSqlList = operationTable.updateOrDelFormTableSql(tableName, formColumnVoList, dataSourceList);
                    } else {
                        tableSqlList = operationTable.createFormTableSql(tableName, formColumnVoList, formTableDtoParams.getIdIsVarchar());
                    }
                } else {
                    //更换数据库表名后，再判断库中是否存在
                    if (null != mapList && mapList.size() > 0) {
                        throw new ResultInfoException("数据库表结构，" + tableName + "已经存在!");
                    } else {
                        //不存在
                        tableSqlList = operationTable.createFormTableSql(tableName, formColumnVoList, formTableDtoParams.getIdIsVarchar());
                    }
                }
            } else {
                //objectOid 为空则为新建
                if (null != mapList && mapList.size() > 0) {
                    throw new ResultInfoException("数据库表" + tableName + "已经存在!");
                }
                tableSqlList = operationTable.createFormTableSql(tableName, formColumnVoList, formTableDtoParams.getIdIsVarchar());
            }
            if (null != tableSqlList) {
                Map<String, Object> map = null;
                //mongdb
                if ("mongodb".equals(source.getType().toLowerCase())) {
                    //throw new ResultInfoException("mongodb数据库暂不支持生成文档!");
                    MongoDBUtil mongoDBUtil = new MongoDBUtil(source.getHost(), source.getPort(),
                            source.getDatasourceName(), source.getUsername(), source.getPassword());
                    map = mongoDBUtil.executeBatchByList(tableName, tableSqlList);
                } else {
                    map = dbHelper.executeBatchBySql(tableSqlList);
                }
                Boolean success = (Boolean) map.get("success");
                if (!success) {
                    String message = map.get("message").toString();
                    if (!message.contains("ORA-00942")) {
                        throw new ResultInfoException(message);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new ResultInfoException(e.getMessage());
        }
        return new ApiResultSet<>(true);
    }

    @Override
    public ApiResultSet createTable(String sql, String datasourceOid, String tableName) {
        if (StrUtil.isBlank(sql)) {
            throw new ResultInfoException("参数sql不能为空!");
        }
        if (StrUtil.isBlank(datasourceOid)) {
            throw new ResultInfoException("参数datasourceOid不能为空!");
        }
        if (StrUtil.isBlank(tableName)) {
            throw new ResultInfoException("参数tableName不能为空!");
        }
        FormDataSource source = formDataSourceManager.getFormDataSourceByDataSourceOid(datasourceOid);
        if (null == source) {
            throw new ResultInfoException("参数datasourceOid不正确!");
        }
        DBHelper dbHelper = new DBHelper(source);
        String dropSql = "DROP TABLE " + tableName + ";";
        dbHelper.createTableByDbSql(dropSql);
        Map<String, Object> map = dbHelper.createTableByDbSql(sql);
        return new ApiResultSet<>(map);
    }

    @Override
    public ApiResultSet updateTable(String sql, String datasourceOid) {
        if (StrUtil.isBlank(sql)) {
            throw new ResultInfoException("参数sql不能为空!");
        }
        if (StrUtil.isBlank(datasourceOid)) {
            throw new ResultInfoException("参数datasourceOid不能为空!");
        }
        FormDataSource source = formDataSourceManager.getFormDataSourceByDataSourceOid(datasourceOid);
        if (null == source) {
            throw new ResultInfoException("参数datasourceOid不正确!");
        }
        DBHelper dbHelper = new DBHelper(source);
        Map<String, Object> map = dbHelper.executeByDbSql(sql);
        return new ApiResultSet<>(map);
    }

    @Override
    public ApiResultSet selectTable(String sql, String datasourceOid, String objectOid) {
        if (StrUtil.isBlank(sql)) {
            throw new ResultInfoException("参数sql不能为空!");
        }
        if (StrUtil.isBlank(datasourceOid)) {
            throw new ResultInfoException("参数datasourceOid不能为空!");
        }
        FormDataSource source = formDataSourceManager.getFormDataSourceByDataSourceOid(datasourceOid);
        if (null == source) {
            throw new ResultInfoException("参数datasourceOid不正确!");
        }
        Map<String, Object> selectDataMap = new LinkedHashMap<>();
        List<FormColumn> formColumnList = null;
        if (StrUtil.isEmpty(objectOid)) {
            FormColumn formColumn = new FormColumn();
            formColumn.setObjectOid(objectOid);
            formColumnList = formColumnManager.queryFormColumnList(formColumn);
            for (FormColumn column : formColumnList) {
                //数据库字段名称
                String columnName = column.getColumnName();
                selectDataMap.put(columnName, null);
            }
        }
        DBHelper dbHelper = new DBHelper(source);
        List<Map<String, Object>> mapList = dbHelper.selectDbSql(sql, selectDataMap);
        List<Map<String, Object>> resultList = new ArrayList<>();
        if (mapList != null && StrUtil.isNotEmpty(objectOid) && null != formColumnList) {
            for (Map<String, Object> resultMap : mapList) {
                Map<String, Object> jsonMap = new HashMap<>();
                for (FormColumn column : formColumnList) {
                    //数据库字段名称
                    String columnName = column.getColumnName();
                    //实体类属性名称
                    String objectProperty = column.getObjectProperty();
                    Object data = resultMap.get(columnName);
                    jsonMap.put(objectProperty, data);
                }
                resultList.add(jsonMap);
            }
        } else {
            resultList = mapList;
        }
        return new ApiResultSet<>(resultList);
    }

    @Override
    public ApiResultSet deleteTable(String sql, String datasourceOid) {
        if (StrUtil.isBlank(sql)) {
            throw new ResultInfoException("参数sql不能为空!");
        }
        if (StrUtil.isBlank(datasourceOid)) {
            throw new ResultInfoException("参数datasourceOid不能为空!");
        }
        FormDataSource source = formDataSourceManager.getFormDataSourceByDataSourceOid(datasourceOid);
        if (null == source) {
            throw new ResultInfoException("参数datasourceOid不正确!");
        }
        DBHelper dbHelper = new DBHelper(source);
        Map<String, Object> map = dbHelper.executeByDbSql(sql);
        return new ApiResultSet<>(map);
    }

    @Override
    public ApiResultSet<List<String>> getDataSourceTypeList(String datasourceOid) {
        if (StrUtil.isBlank(datasourceOid)) {
            throw new ResultInfoException("参数datasourceOid不能为空!");
        }
        FormDataSource source = formDataSourceManager.getFormDataSourceByDataSourceOid(datasourceOid);
        if (null == source) {
            throw new ResultInfoException("参数datasourceOid不正确!");
        }
        List<String> list = FormStaticParameter.DATA_SOURCE_TYPE_MAP.get(source.getType());
        return new ApiResultSet<>(list);
    }

    @Override
    public ApiResultSet<List<String>> getDataSourceTypeByNameList(String datasourceTypeName) {
        List<String> list = FormStaticParameter.DATA_SOURCE_TYPE_MAP.get(datasourceTypeName);
        return new ApiResultSet<>(list);
    }

    @Override
    public ApiResultSet<FormTable> saveFormTable(FormTable formTable) {
        FormTable table = formTableManager.saveFormTable(formTable);
        return new ApiResultSet<>(table);
    }

    @Override
    public ApiResultSet saveFormTableVo(CreateFormTableVo createFormTableVo) {
        formTableManager.saveFormTableVo(createFormTableVo);
        return new ApiResultSet<>(true);
    }

    @Override
    public ApiResultSet<FormTable> getFormTableById(Long id) {
        FormTable table = formTableManager.getFormTableById(id);
        return new ApiResultSet<>(table);
    }

    @Override
    public ApiResultSet<FormTable> getFormTableByTableOid(String tableOid) {
        FormTable table = formTableManager.getFormTableByTableOid(tableOid);
        return new ApiResultSet<>(table);
    }

    @Override
    public ApiResultSet<List<FormTable>> queryFormTableList(String tableName, String datasourceOid) {
        if (StrUtil.isBlank(tableName)) {
            throw new ResultInfoException("参数tableName不能为空!");
        }
        if (StrUtil.isBlank(datasourceOid)) {
            throw new ResultInfoException("参数datasourceOid不能为空!");
        }
        FormTable formTable = new FormTable();
        formTable.setTableName(tableName);
        formTable.setDatasourceOid(datasourceOid);
        List<FormTable> formTableList = formTableManager.queryFormTableList(formTable);
        if (null == formTableList || formTableList.size() == 0) {
            FormDataSource source = formDataSourceManager.getFormDataSourceByDataSourceOid(datasourceOid);
            if (null == source) {
                throw new ResultInfoException("参数datasourceOid不正确!");
            }
            DBHelper dbHelper = new DBHelper(source);
            //查询表是否有记录
            List<Map<String, Object>> mapList = dbHelper.getColumnsByTableName(tableName, false);
            if (null != mapList && mapList.size() > 0) {
                for (Map<String, Object> map : mapList) {
                    String columnName = map.get("columnName").toString();
                    if ("ID".equals(columnName.toUpperCase()) || "MODIFY_DATE".equals(columnName.toUpperCase())) {
                        //不操作ID和MODIFY_DATE
                        continue;
                    }
                    FormTable table = new FormTable();
                    table.setColumnName(columnName);
                    if (null != map.get("columnType")) {
                        String columnType = map.get("columnType").toString().toUpperCase();
                        if (columnType.contains(" ")) {
                            columnType = columnType.substring(0, columnType.indexOf(" "));
                        }
                        if (columnType.contains("(")) {
                            columnType = columnType.substring(0, columnType.indexOf("("));
                        }
                        table.setColumnType(columnType);
                    }
                    table.setDemo(null == map.get("demo") ? "" : map.get("demo").toString());
                    table.setIndexFlag(0);
                    table.setIsNotNull((Integer) map.getOrDefault("notNull", 0));
                    table.setMaxLength(null == map.get("maxLenth") ? "0" : map.get("maxLenth").toString());
                    formTableList.add(table);
                }
            }
        }
        return new ApiResultSet<>(formTableList);
    }

    @Override
    public ApiResultSet createFormTableByFormPhysicalDataModel(FormPhysicalDataModel formPhysicalDataModel) {
        if (formPhysicalDataModel.getIsSave() == null){
            throw new ResultInfoException("主表信息需要传是否保存！");
        }
        //判断是否有重复字段
        judgeRepeatField(formPhysicalDataModel);

        String relationObjectBusinessId = "";
        List<FormPhysicalDataModel> children = formPhysicalDataModel.getChildren();
        //从表创建
        if (children != null && children.size() > 0) {
            for (FormPhysicalDataModel child : children) {
                if (child.getColumnList() != null && child.getColumnList().size() == 0){
                    throw new ResultInfoException("除了逻辑主表，其他所有表的栏位list 不能为空串！");
                }
                FormTableDtoParams formTableDtoParams = Builder.of(FormTableDtoParams::new)
                        .with(FormTableDtoParams::setDatasourceOid, child.getDatasourceOid())
                        .with(FormTableDtoParams::setFormTableDtoJson, JSON.toJSONString(child.getColumnList()))
                        .with(FormTableDtoParams::setTableName, child.getObjectForm())
                        .with(FormTableDtoParams::setIdIsVarchar, child.getIdIsVarchar())
                        .with(FormTableDtoParams::setObjectOid, child.getObjectOid())
                        .build();
                this.createTableByFormTableDto(formTableDtoParams);
            }
        }
        //主表创建
        if (formPhysicalDataModel.getIsSave() == 1 && formPhysicalDataModel.getColumnList() != null
                && formPhysicalDataModel.getColumnList().size() == 0){
            throw new ResultInfoException("除了逻辑主表，其他所有表的栏位list 不能为空串！");
        }
        FormTableDtoParams formTableDtoParams = Builder.of(FormTableDtoParams::new)
                .with(FormTableDtoParams::setDatasourceOid, formPhysicalDataModel.getDatasourceOid())
                .with(FormTableDtoParams::setFormTableDtoJson, JSON.toJSONString(formPhysicalDataModel.getColumnList()))
                .with(FormTableDtoParams::setTableName, formPhysicalDataModel.getObjectForm())
                .with(FormTableDtoParams::setIdIsVarchar, formPhysicalDataModel.getIdIsVarchar())
                .with(FormTableDtoParams::setFormCode, formPhysicalDataModel.getFormCode())
                .with(FormTableDtoParams::setObjectOid, formPhysicalDataModel.getObjectOid())
                .build();
        this.createTableByFormTableDto(formTableDtoParams);

        //从存储对象创建
        List<FormObjectExtand> extands = new ArrayList<>();
        if (children != null && children.size() > 0) {
            for (FormPhysicalDataModel child : children) {
                List<FormColumn> datasourceColumn = formColumnManager.getDatasourceColumn(child.getDatasourceOid(), child.getObjectForm(),formPhysicalDataModel.getIsNotChangeFiledName());

                //从存储对象创建  设置栏位的数据类型
                Map<String, FormColumn> collect = null;
                if (!StringUtils.isEmpty(child.getObjectOid())){
                    List<FormColumn> originalFormColumns = formColumnManager.queryFormColumnList(Builder.of(FormColumn::new).with(FormColumn::setObjectOid, child.getObjectOid()).build());
                    collect = originalFormColumns.stream().collect(Collectors.toMap(FormColumn::getColumnName, formColumn -> formColumn));
                }
                List<FormColumn> columnList = child.getColumnList();
                Map<String, FormColumn> pushCollect = columnList.stream().collect(Collectors.toMap(FormColumn::getColumnName, formColumn -> formColumn));
                for (FormColumn column : datasourceColumn){
                    if (pushCollect.get(column.getColumnName()) != null){
                        column.setDataType(pushCollect.get(column.getColumnName()).getDataType());
                    }else if (collect != null && collect.get(column.getColumnName()) != null){
                        column.setDataType(collect.get(column.getColumnName()).getDataType());
                    }
                }

                FormObject formObject =new FormObject();
                BeanUtils.copyProperties(child,formObject);
                formObject.setColumnList(datasourceColumn);
                formObject.setAuthorizeKey(formPhysicalDataModel.getAuthorizeKey());
                FormObject object = formObjectManager.saveFormObject(formObject);
                relationObjectBusinessId += child.getRelationObjectBusinessId()+":"+object.getObjectOid()+",";
                FormObjectExtand extand = new FormObjectExtand();
                BeanUtils.copyProperties(child.getFormObjectExtand(), extand);
                extand.setSecondaryObjectOid(object.getObjectOid());
                extands.add(extand);
            }
        }
        //主存储对象创建
        List<FormColumn> datasourceColumn = formColumnManager.getDatasourceColumn(formPhysicalDataModel.getDatasourceOid(), formPhysicalDataModel.getObjectForm(),formPhysicalDataModel.getIsNotChangeFiledName());

        //主存储对象创建  设置栏位的数据类型
        Map<String, FormColumn> collect = null;
        if (!StringUtils.isEmpty(formPhysicalDataModel.getObjectOid())){
            List<FormColumn> originalFormColumns = formColumnManager.queryFormColumnList(Builder.of(FormColumn::new).with(FormColumn::setObjectOid, formPhysicalDataModel.getObjectOid()).build());
            collect = originalFormColumns.stream().collect(Collectors.toMap(FormColumn::getColumnName, formColumn -> formColumn));
        }
        List<FormColumn> columnList = formPhysicalDataModel.getColumnList();
        Map<String, FormColumn> pushCollect = columnList.stream().collect(Collectors.toMap(FormColumn::getColumnName, formColumn -> formColumn));
        for (FormColumn column : datasourceColumn){
            if (pushCollect.get(column.getColumnName()) != null){
                column.setDataType(pushCollect.get(column.getColumnName()).getDataType());
            }else if (collect != null && collect.get(column.getColumnName()) != null){
                column.setDataType(collect.get(column.getColumnName()).getDataType());
            }
        }

        FormObject formObject =new FormObject();
        BeanUtils.copyProperties(formPhysicalDataModel,formObject);
        formObject.setColumnList(datasourceColumn);
        formObject.setExtandList(extands);
        FormMain formMain = formObjectManager.saveObjectFormObject(formObject);

        relationObjectBusinessId += formPhysicalDataModel.getRelationObjectBusinessId()+":"+formMain.getObjectOid();

        FormPhysicalDataModelVo vo = new FormPhysicalDataModelVo();
        BeanUtils.copyProperties(formMain,vo);
        vo.setRelationObjectBusinessId(relationObjectBusinessId);
        return new ApiResultSet(vo);
    }

    //判断是否有重复字段
    private void judgeRepeatField(FormPhysicalDataModel formPhysicalDataModel) {
        if (formPhysicalDataModel != null){
            List<FormColumn> columnList = formPhysicalDataModel.getColumnList();
            if (columnList != null && columnList.size()>0){
                Set<String> set = new HashSet<>();
                for (FormColumn formColumn:columnList){
                    if (!set.add(formColumn.getColumnName().toLowerCase())){
                        throw new ResultInfoException("表:"+formPhysicalDataModel.getObjectForm()+",存在字段重复。");
                    }
                }
            }
            List<FormPhysicalDataModel> children = formPhysicalDataModel.getChildren();
            if (children != null && children.size()>0){
                for (FormPhysicalDataModel formPhysicalDataModel1 : children){
                    judgeRepeatField(formPhysicalDataModel1);
                }
            }
        }
    }

    @Override
    public ApiResultSet getFormPhysicalDataModelByFormCode(String formCode) {
        if (StrUtil.isBlank(formCode)) {
            throw new ResultInfoException("参数formCode不能为空!");
        }
        FormMain formMain = formMainManager.getFormMainByFormMainCode(formCode);
        if (formMain != null) {
            FormObject formObject = formObjectManager.getFormObjectByObjectOid(formMain.getObjectOid());
            //List<FormColumn> datasourceColumn = formColumnManager.getDatasourceColumn(formObject.getDatasourceOid(), formObject.getObjectForm());
            FormColumn queryFormColumn = new FormColumn();
            queryFormColumn.setObjectOid(formMain.getObjectOid());
            List<FormColumn> datasourceColumn = formColumnManager.queryFormColumnList(queryFormColumn);
            datasourceColumn = removeDefaultIdAndModifyDate(datasourceColumn);
            FormPhysicalDataModel formPhysicalDataModel = new FormPhysicalDataModel();
            BeanUtils.copyProperties(formObject, formPhysicalDataModel);
            formPhysicalDataModel.setColumnList(datasourceColumn);
            formPhysicalDataModel.setFormCode(formCode);
            List<FormObjectExtand> formObjectExtands = formObjectExtandManager.queryFormObjectExtandList(formObject.getObjectOid(), null);
            if (formObjectExtands != null && formObjectExtands.size() > 0) {
                List<FormPhysicalDataModel> children = new ArrayList<>();
                for (FormObjectExtand extand : formObjectExtands) {
                    FormObject object = formObjectManager.getFormObjectByObjectOid(extand.getSecondaryObjectOid());
                    //List<FormColumn> column = formColumnManager.getDatasourceColumn(formObject.getDatasourceOid(), formObject.getObjectForm());
                    queryFormColumn.setObjectOid(extand.getSecondaryObjectOid());
                    List<FormColumn> column = formColumnManager.queryFormColumnList(queryFormColumn);
                    column = removeDefaultIdAndModifyDate(column);
                    FormPhysicalDataModel physicalDataModel = new FormPhysicalDataModel();
                    BeanUtils.copyProperties(object, physicalDataModel);
                    physicalDataModel.setColumnList(column);
                    physicalDataModel.setFormObjectExtand(extand);
                    children.add(physicalDataModel);
                }
                formPhysicalDataModel.setChildren(children);
            }
            return new ApiResultSet(formPhysicalDataModel);
        }
        return new ApiResultSet();
    }

    /**
     * 默认生成的ID和MODIFY_DATE字段不必传给页面
     * @param datasourceColumn
     * @return
     */
    private List<FormColumn> removeDefaultIdAndModifyDate(List<FormColumn> datasourceColumn) {
        List<FormColumn> newDatasourceColumn = new ArrayList<>();
        for (FormColumn formColumn : datasourceColumn){
            if (!("ID".equalsIgnoreCase(formColumn.getColumnName()) || "MODIFY_DATE".equalsIgnoreCase(formColumn.getColumnName()))){
                newDatasourceColumn.add(formColumn);
            }
        }
        return newDatasourceColumn;
    }

}
