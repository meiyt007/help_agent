package com.zfsoft.microservice.form.controller;

import cn.hutool.core.util.StrUtil;
import com.zfsoft.microservice.form.data.FormMain;
import com.zfsoft.microservice.form.data.FormObject;
import com.zfsoft.microservice.form.manager.FormObjectManager;
import com.zfsoft.microservice.form.service.FormObjectService;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.data.BaseStaticParameter;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.platform.utils.validate.ResultInfoException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @ClassName FormObjectController1
 * @Description 存储对象的实现类
 * @Author wuxx
 * @Date 2021-04-13 15:30
 * @Version V1.0
 **/
@RestController
@Slf4j
public class FormObjectController implements FormObjectService {

    @Resource
    private FormObjectManager formObjectManager;

    /**
     * @description:  查询存储对象的信息列表
     * @param authorizeKey 授权key
     * @param objectName 对象名称
     * @author: wuxx
     * @Date: 2021-04-13 15:30
     **/
    @Override
    public ApiResultSet<PageResult<FormObject>> queryFormObjectWithPage(String authorizeKey, String objectName, String objectCode,String saveType,Integer pageNum,
                                                                                      Integer pageSize){
        FormObject formObject = new FormObject();
        formObject.setObjectName(objectName);
        formObject.setObjectCode(objectCode);
        formObject.setAuthorizeKey(authorizeKey);
        formObject.setSaveType(saveType);
        PageResult<FormObject> pageResult = formObjectManager.queryFormObjectWithPage(formObject,pageNum,pageSize);
        return new ApiResultSet<>(pageResult);
    }

    /**
     * @description: 初始化存储对象的信息
     * @param id 存储对象实体类主键
     * @author: wuxx
     * @Date: 2021-04-13 15:30
     **/
    @Override
    public ApiResultSet initFormObject(Long id){
        FormObject formObject = formObjectManager.getFormObjectById(id);
        return new ApiResultSet<>(formObject);
    }

    /**
     * @description:  存储对象的新增或者修改
     * @param formObject 存储对象实体类
     * @author: wuxx
     * @Date: 2021-04-13 15:30
     **/
    @Override
    public ApiResultSet<FormObject> saveFormObject(@RequestBody FormObject formObject){
        formObjectManager.saveFormObject(formObject);
        return  new ApiResultSet<>(formObject);
    }

    @Override
    public ApiResultSet<FormMain> saveObjectFormObject(FormObject formObject) {
        FormMain formMain = formObjectManager.saveObjectFormObject(formObject);
        return new ApiResultSet<>(formMain);
    }

    /**
     * @description:  存储对象的信息的删除
     * @param id 存储对象实体类主键
     * @author: wuxx
     * @Date: 2021-04-13 15:30
     **/
    @Override
    public ApiResultSet<Integer>  deleteFormObjectById(Long id){
        formObjectManager.deleteFormObjectById(id);
        return new ApiResultSet<>();
    }

    /**
     * @description:  获取存储对象的信息
     * @param id 存储对象实体类主键
     * @author: wuxx
     * @Date: 2021-04-13 15:30
     **/
    @Override
    public ApiResultSet<FormObject>  getFormObjectById(Long id){
        FormObject formObject = formObjectManager.getFormObjectById(id);
        return new ApiResultSet<>(formObject);
    }

    /**
     * @description:  获取存储对象的信息
     * @param objectOid 业务主键
     * @author: wuxx
     * @Date: 2021-04-13 15:30
     **/
    @Override
    public ApiResultSet<FormObject> getFormObjectByObjectOid(@PathVariable("objectOid")String objectOid){
        FormObject formObject = formObjectManager.getFormObjectByObjectOid(objectOid);
        return new ApiResultSet<>(formObject);
    }

    /**
     * @description:  存储对象的信息的启禁用
     * @param id 存储对象实体类主键
     * @author: wuxx
     * @Date: 2021-04-13 15:30
     **/
    @Override
    public ApiResultSet<Integer> ableFormObjectById(@PathVariable("id")Long id){
        int rows = formObjectManager.ableFormObjectById(id);
        ApiResultSet<Integer> apiResultSet = new ApiResultSet<>();
        apiResultSet.setData(rows);
        return apiResultSet;
    }

    @Override
    public ApiResultSet<List<FormObject>> queryFormObjectList(String authorizeKey,String moduleOid) {
        FormObject formObject = new FormObject();
        formObject.setAuthorizeKey(authorizeKey);
        formObject.setModuleOid(moduleOid);
        formObject.setIsAble(BaseStaticParameter.Y);
        formObject.setIsDelete(BaseStaticParameter.N);
        List<FormObject> formObjectList = formObjectManager.queryFormObjectList(formObject);
        return new ApiResultSet<>(formObjectList);
    }

    @Override
    public ApiResultSet<List<FormObject>> queryDataSourceFormObjectList(String authorizeKey, String moduleOid,String saveType) {
        FormObject formObject = new FormObject();
        formObject.setAuthorizeKey(authorizeKey);
        formObject.setModuleOid(moduleOid);
        formObject.setIsAble(BaseStaticParameter.Y);
        formObject.setIsDelete(BaseStaticParameter.N);
        formObject.setSaveType(saveType);
        List<FormObject> formObjects = formObjectManager.queryFormObjectList(formObject);
        return new ApiResultSet<>(formObjects);
    }

    @Override
    public ApiResultSet<Boolean> checkReportDataExist(String objectOid) {
        if(StrUtil.isBlank(objectOid)){
            throw new ResultInfoException("参数objectOid不能为空！");
        }
        Boolean aBoolean = formObjectManager.checkReportDataExist(objectOid);
        return new ApiResultSet<>(aBoolean);
    }

    @Override
    public ApiResultSet<List<FormObject>> getFormObjectByFormCodes(String formCodes) {
        List<FormObject> formObjectByFormCodes = formObjectManager.getFormObjectByFormCodes(formCodes);
        return new ApiResultSet<>(formObjectByFormCodes);
    }


}
