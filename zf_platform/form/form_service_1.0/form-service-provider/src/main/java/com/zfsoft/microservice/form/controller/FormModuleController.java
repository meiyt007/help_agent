package com.zfsoft.microservice.form.controller;

import com.zfsoft.microservice.form.data.FormModule;
import com.zfsoft.microservice.form.manager.FormModuleManager;
import com.zfsoft.microservice.form.service.FormModuleService;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.data.BaseStaticParameter;
import com.zfsoft.platform.common.data.PageResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @ClassName FormModuleController
 * @Description 模块管理的实现类
 * @Author wuxx
 * @Date 2021-04-2 10:30
 * @Version V1.0
 **/
@RestController
@Slf4j
public class FormModuleController implements FormModuleService {

    @Resource
    private FormModuleManager formModuleManager;

    /**
     * @description:  查询接模块管理的信息列表
     * @param authorizeKey 授权key
     * @param moduleName 模块名称
     * @author: wuxx
     * @Date: 2021/4/2 10:14
     **/
    public ApiResultSet<PageResult<FormModule>> queryFormModuleWithPage(String authorizeKey,String moduleName,Integer pageNum,
                                                                              Integer pageSize){
        FormModule formModule = new FormModule();
        formModule.setModuleName(moduleName);
        formModule.setAuthorizeKey(authorizeKey);
        PageResult<FormModule> pageResult = formModuleManager.queryFormModuleWithPage(formModule,pageNum,pageSize);
        return new ApiResultSet<>(pageResult);
    }

    /**
     * @description:  初始化接模块管理的信息
     * @param id 接模块管理实体类主键
     * @author: wuxx
     * @Date: 2021/4/2 10:14
     **/
    @Override
    public ApiResultSet initFormModule(Long id){
        FormModule  formModule = formModuleManager.getFormModuleById(id);
        return new ApiResultSet<>(formModule);
    }

    /**
     * @description:  接模块管理的新增或者修改
     * @param formModule 接模块管理实体类
     * @author: wuxx
     * @Date: 2021/4/2 10:14
     **/
    public ApiResultSet<FormModule> saveFormModule(@RequestBody FormModule formModule){
        formModuleManager.saveFormModule(formModule);
        return  new ApiResultSet<>(formModule);
    }

    /**
     * @description:  接模块管理的信息的删除
     * @param id 接模块管理实体类主键
     * @author: wuxx
     * @Date: 2021/4/2 10:14
     **/
    public ApiResultSet<Integer> deleteFormModuleById(Long id){
        formModuleManager.deleteFormModuleById(id);
        return new ApiResultSet<>();
    }

    /**
     * @description:  获取接模块管理的信息
     * @param id 接模块管理实体类主键
     * @author: wuxx
     * @Date: 2021/4/2 10:14
     **/
    public ApiResultSet<FormModule> getFormModuleById(Long id){
        FormModule formModule = formModuleManager.getFormModuleById(id);
        return new ApiResultSet<>(formModule);
    }

    /**
     * @description:  获取接模块管理的信息
     * @param moduleOid 业务主键
     * @author: wanglei
     * @Date: 2020/10/29 11:40
     **/
    public ApiResultSet<FormModule> getFormModuleByModuleOid(String moduleOid){
        FormModule formModule = formModuleManager.getFormModuleByModuleOid(moduleOid);
        return new ApiResultSet<>(formModule);
    }

    /**
     * @description:  接模块管理的信息的启禁用
     * @param id 接模块管理实体类主键
     * @author: wuxx
     * @Date: 2021/4/2 10:14
     **/
    public ApiResultSet<Integer> ableFormModuleById(@PathVariable("id")Long id){
        int rows = formModuleManager.ableFormModuleById(id);
        ApiResultSet<Integer> apiResultSet = new ApiResultSet<>();
        apiResultSet.setData(rows);
        return apiResultSet;
    }

    @Override
    public ApiResultSet<List<FormModule>> queryFormModuleList(String authorizeKey) {
        FormModule formModule = new FormModule();
        formModule.setAuthorizeKey(authorizeKey);
        formModule.setIsAble(BaseStaticParameter.Y);
        formModule.setIsDelete(BaseStaticParameter.N);
        List<FormModule> formModuleList = formModuleManager.queryFormModuleList(formModule);
        return  new ApiResultSet<>(formModuleList);
    }

}
