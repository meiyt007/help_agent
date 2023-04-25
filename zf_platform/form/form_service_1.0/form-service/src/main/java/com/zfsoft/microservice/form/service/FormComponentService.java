package com.zfsoft.microservice.form.service;

import com.zfsoft.microservice.form.data.FormComponent;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.utils.feign.ProcessFeignCalledResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @ClassName FormComponentService
 * @Description 组件件服务定义接口
 * @Author wuxx
 * @Date 2021-04-22 10:10
 * @Version V1.0
 **/
@RequestMapping("/manager/security/component")
public interface FormComponentService {

    /**
     * 删除指定Id的组件信息
     * @param id 组件id
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping( value = "/delete/{id}",method = {RequestMethod.DELETE})
    ApiResultSet<Integer>  deleteFormComponentById(@PathVariable("id") Long id);

    /**
     * @description:  保存子组件
     * @param formComponent 组件实体类
     * @author: wuxx
     * @Date: 2021/4/12 14:57
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/saveFormComponent",method = {RequestMethod.POST})
    ApiResultSet<FormComponent> saveFormComponent(@RequestBody FormComponent formComponent);

    /**
     * @description:  获取组件的信息
     * @param id 组件实体类主键
     * @author: wuxx
     * @Date: 2021/04/12 10:14
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/getOne/{id}",method = {RequestMethod.GET})
    ApiResultSet<FormComponent> getFormComponentById(@PathVariable("id") Long id);

    /**
     * @description:  获取组件的信息
     * @param componentOid 组件实体类业务主键
     * @author: wuxx
     * @Date: 2021/04/12 10:14
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/getFormComponentByComponentOid/{componentOid}",method = {RequestMethod.GET})
    ApiResultSet<FormComponent> getFormComponentByComponentOid(@PathVariable("componentOid") String componentOid);

    /**
     * @description:  根据授权key查询设计器中的授权组件
     * @param authorizeKey 授权key
     * @author: wuxx
     * @Date: 2021/04/21 10:14
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/getFormComponentByAuthorizeKey",method = {RequestMethod.GET})
    ApiResultSet<List<FormComponent>> getFormComponentByAuthorizeKey(@RequestParam("authorizeKey") String authorizeKey);

}
