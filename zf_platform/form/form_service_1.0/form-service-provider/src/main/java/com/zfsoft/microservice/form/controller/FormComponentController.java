package com.zfsoft.microservice.form.controller;

import com.zfsoft.microservice.form.data.FormComponent;
import com.zfsoft.microservice.form.manager.FormComponentManager;
import com.zfsoft.microservice.form.service.FormComponentService;
import com.zfsoft.platform.common.data.ApiResultSet;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @ClassName FormComponentController
 * @Description 组件管理的实现类
 * @Author wuxx
 * @Date 2021-04-12 10:30
 * @Version V1.0
 **/
@RestController
@Slf4j
public class FormComponentController implements FormComponentService {

    @Resource
    private FormComponentManager formComponentManager;


    /**
     * @description:  保存子组件
     * @param formComponent 组件实体类
     * @author: wuxx
     * @Date: 2021/4/12 14:57
     **/
    @Override
    public ApiResultSet<FormComponent> saveFormComponent(@RequestBody FormComponent formComponent){
        formComponentManager.saveFormComponent(formComponent);
        return new ApiResultSet<>(formComponent);
    }

    /**
     * @description:  获取组件的信息
     * @param id 组件实体类主键
     * @author: wuxx
     * @Date: 2021/04/12 10:14
     **/
    @Override
    public ApiResultSet<FormComponent> getFormComponentById(Long id) {
        FormComponent formComponent = formComponentManager.getFormComponentById(id);
        return  new ApiResultSet<>(formComponent);
    }

    /**
     * @description:  接组件管理的信息的删除
     * @param id 接组件管理实体类主键
     * @author: wuxx
     * @Date: 2021/4/12 10:14
     **/
    public ApiResultSet<Integer> deleteFormComponentById(Long id){
        formComponentManager.deleteFormComponentById(id);
        return new ApiResultSet<>();
    }

    /**
     * @description:  获取接组件管理的信息
     * @param componentOid 业务主键
     * @author: wanglei
     * @Date: 2020/10/29 11:40
     **/
    public ApiResultSet<FormComponent> getFormComponentByComponentOid(@PathVariable("componentOid")String componentOid){
        FormComponent formComponent = formComponentManager.getFormComponentByComponentOid(componentOid);
        return new ApiResultSet<>(formComponent);
    }

    /**
     * @description:  根据授权key查询设计器中的授权组件列表
     * @param authorizeKey 授权key
     * @author: wuxx
     * @Date: 2021/04/25 14:14
     **/
    @Override
    public ApiResultSet<List<FormComponent>> getFormComponentByAuthorizeKey(String authorizeKey){
        List<FormComponent> formComponentList = formComponentManager.getFormComponentListByAuthorizeKey(authorizeKey);
        return new ApiResultSet<>(formComponentList);
    }

}
