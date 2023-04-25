package com.zfsoft.microservice.form.controller;

import com.zfsoft.microservice.form.data.FormColumn;
import com.zfsoft.microservice.form.data.FormStaticParameter;
import com.zfsoft.microservice.form.manager.FormColumnManager;
import com.zfsoft.microservice.form.service.FormColumnService;
import com.zfsoft.platform.common.data.ApiResultSet;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @ClassName FormColumnController
 * @Description 表结构的实现类
 * @Author wuxx
 * @Date 2021-04-13 15:30
 * @Version V1.0
 **/
@RestController
@Slf4j
public class FormColumnController implements FormColumnService {

    @Resource
    private FormColumnManager formColumnManager;

    /**
     * @description: 初始化表结构的信息
     * @param id 表结构实体类主键
     * @author: wuxx
     * @Date: 2021-04-13 15:30
     **/
    @Override
    public ApiResultSet initFormColumn(Long id){
        FormColumn formColumn = formColumnManager.getFormColumnById(id);
        return new ApiResultSet<>(formColumn);
    }

    /**
     * @description:  表结构的新增或者修改
     * @param formColumn 表结构实体类
     * @author: wuxx
     * @Date: 2021-04-13 15:30
     **/
    @Override
    public ApiResultSet<FormColumn> saveFormColumn(@RequestBody FormColumn formColumn){
        formColumnManager.saveFormColumn(formColumn);
        return  new ApiResultSet<>(formColumn);
    }

    /**
     * @description:  获取表结构的信息
     * @param id 表结构实体类主键
     * @author: wuxx
     * @Date: 2021-04-13 15:30
     **/
    @Override
    public ApiResultSet<FormColumn>  getFormColumnById(Long id){
        FormColumn formColumn = formColumnManager.getFormColumnById(id);
        return new ApiResultSet<>(formColumn);
    }

    /**
     * @description:  获取表结构的信息
     * @param columnOid 业务主键
     * @author: wuxx
     * @Date: 2021-04-13 15:30
     **/
    @Override
    public ApiResultSet<FormColumn>  getFormColumnByColumnOid(@PathVariable("columnOid")String columnOid){
        FormColumn formColumn = formColumnManager.getFormColumnByColumnOid(columnOid);
        return new ApiResultSet<>(formColumn);
    }

    /**
     * @description:  根据对象主键查询表结构列表
     * @param objectOid 对象主键
     * @author: wuxx
     * @Date: 2021/04/13 16:14
     **/
    @Override
    public ApiResultSet<List<FormColumn>> queryFormColumnListByObjectOid(String objectOid,String datasourceOid) {
        FormColumn formColumn = new FormColumn();
        formColumn.setObjectOid(objectOid);
        formColumn.setDatasourceOid(datasourceOid);
        List<FormColumn> formColumnList = formColumnManager.queryFormColumnList(formColumn);
        return new ApiResultSet<>(formColumnList);
    }


    /**
     * @description:  根据对象主键查询表结构字符串包括关联
     * @param objectOid 对象主键
     * @author: wuxx
     * @Date: 2021/12/3 16:14
     **/
    @Override
    public ApiResultSet<List<String>> queryFormColumnStrByObjectOid(String objectOid) {
        List<String> formColumnStrList = formColumnManager.queryFormColumnStrByObjectOid(objectOid);
        return new ApiResultSet<>(formColumnStrList);
    }

    @Override
    public ApiResultSet<List<FormColumn>> getDatasourceColumn(String datasourceOid,String objectForm,Integer isNotChangeFiledName) {
        List<FormColumn> formColumnList = formColumnManager.getDatasourceColumn(datasourceOid, objectForm,isNotChangeFiledName);
        return new ApiResultSet<>(formColumnList);
    }

    @Override
    public ApiResultSet<List<Map<String, Object>>> getObjectFieldSaveTypeList() {
        List<Map<String, Object>> list = FormStaticParameter.OBJECT_FIELD_SAVE_TYPE_LIST;
        return new ApiResultSet<>(list);
    }


}
