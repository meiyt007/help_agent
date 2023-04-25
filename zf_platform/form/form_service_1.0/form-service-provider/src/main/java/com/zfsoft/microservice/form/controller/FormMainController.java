package com.zfsoft.microservice.form.controller;

import cn.hutool.core.util.StrUtil;
import com.zfsoft.microservice.form.data.FormMain;
import com.zfsoft.microservice.form.manager.FormMainManager;
import com.zfsoft.microservice.form.service.FormMainService;
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
 * @ClassName FormMainController
 * @Description 表单设计主表的实现类
 * @Author wuxx
 * @Date 2021-04-16 13:30
 * @Version V1.0
 **/
@RestController
@Slf4j
public class FormMainController implements FormMainService {

    @Resource
    private FormMainManager formMainManager;

    /**
     * @description:  查询表单设计主表的信息列表
     * @param authorizeKey 授权key
     * @param formName 对象名称
     * @author: wuxx
     * @Date: 2021-04-16 13:30
     **/
    @Override
    public ApiResultSet<PageResult<FormMain>> queryFormMainWithPage(String authorizeKey, String formName, String formCode, Integer formStatus ,String objectOid,Integer isAble,
                                                                    Integer pageNum, Integer pageSize){
        FormMain formMain = new FormMain();
        formMain.setIsAble(isAble);
        formMain.setFormName(formName);
        formMain.setFormStatus(formStatus);
        formMain.setAuthorizeKey(authorizeKey);
        formMain.setFormCode(formCode);
        formMain.setObjectOid(objectOid);
        PageResult<FormMain> pageResult = formMainManager.queryFormMainWithPage(formMain,pageNum,pageSize);
        return new ApiResultSet<>(pageResult);
    }

    /**
     * @description: 初始化表单设计主表的信息
     * @param id 表单设计主表实体类主键
     * @author: wuxx
     * @Date: 2021-04-16 13:30
     **/
    @Override
    public ApiResultSet initFormMain(Long id){
        FormMain formMain = formMainManager.getFormMainById(id);
        return new ApiResultSet<>(formMain);
    }

    /**
     * @description:  表单设计主表的新增或者修改
     * @param formMain 表单设计主表实体类
     * @author: wuxx
     * @Date: 2021-04-16 13:30
     **/
    @Override
    public ApiResultSet<FormMain> saveFormMain(@RequestBody FormMain formMain){
        formMainManager.saveFormMain(formMain);
        return  new ApiResultSet<>(formMain);
    }

    /**
     * @description:  表单设计主表的信息的删除
     * @param id 表单设计主表实体类主键
     * @author: wuxx
     * @Date: 2021-04-16 13:30
     **/
    @Override
    public ApiResultSet<Integer>  deleteFormMainById(Long id){
        formMainManager.deleteFormMainById(id);
        return new ApiResultSet<>();
    }

    /**
     * @description:  获取表单设计主表的信息
     * @param id 表单设计主表实体类主键
     * @author: wuxx
     * @Date: 2021-04-16 13:30
     **/
    @Override
    public ApiResultSet<FormMain>  getFormMainById(Long id){
        FormMain formMain = formMainManager.getFormMainById(id);
        return new ApiResultSet<>(formMain);
    }

    /**
     * @description:  获取表单设计主表的信息
     * @param formMainOid 业务主键
     * @author: wuxx
     * @Date: 2021-04-16 13:30
     **/
    @Override
    public ApiResultSet<FormMain>  getFormMainByFormMainOid(@PathVariable("formMainOid")String formMainOid){
        FormMain formMain = formMainManager.getFormMainByFormMainOid(formMainOid);
        return new ApiResultSet<>(formMain);
    }

    /**
     * @description:  表单设计主表的信息的启禁用
     * @param id 表单设计主表实体类主键
     * @author: wuxx
     * @Date: 2021-04-16 13:30
     **/
    @Override
    public ApiResultSet<Integer> ableFormMainById(@PathVariable("id")Long id){
        int rows = formMainManager.ableFormMainById(id);
        ApiResultSet<Integer> apiResultSet = new ApiResultSet<>();
        apiResultSet.setData(rows);
        return apiResultSet;
    }

    @Override
    public ApiResultSet<Integer> deployFormMain(String formMainOid) {
        int rows = formMainManager.deployFormMain(formMainOid);
        ApiResultSet<Integer> apiResultSet = new ApiResultSet<>();
        apiResultSet.setData(rows);
        return apiResultSet;
    }

    @Override
    public ApiResultSet<Integer> deployByFormCode(String formCode) {
        if(StrUtil.isBlank(formCode)){
            throw new ResultInfoException("参数formCode不能为空！");
        }
        FormMain formMain = formMainManager.getFormMainByFormMainCode(formCode);
        if(null == formMain){
            throw new ResultInfoException("未查询到设计对象信息!");
        }
        int rows = formMainManager.deployFormMain(formMain.getFormMainOid());
        ApiResultSet<Integer> apiResultSet = new ApiResultSet<>();
        apiResultSet.setData(rows);
        return apiResultSet;
    }

    /**
     * @description:  表单的复制
     * @param formMainOid 表单的业务主键
     * @author: wuxx
     * @Date: 2021/05/6 10:14
     **/
    @Override
    public ApiResultSet<Integer> copyFormMain(String formMainOid) {
        int rows = formMainManager.copyFormMain(formMainOid);
        ApiResultSet<Integer> apiResultSet = new ApiResultSet<>();
        apiResultSet.setData(rows);
        return apiResultSet;
    }

    /**
     * @description:  表单设计主表的查询
     * @param authorizeKey 授权key
     * @param formStatus 表单状态 0修订 1发布 2变更
     * @author: wuxx
     * @Date: 2021-04-16 13:30
     **/
    @Override
    public ApiResultSet<List<FormMain>> queryFormMainList(String authorizeKey,Integer formStatus) {
        FormMain formMain = new FormMain();
        formMain.setAuthorizeKey(authorizeKey);
        formMain.setFormStatus(formStatus);
        formMain.setIsAble(BaseStaticParameter.Y);
        formMain.setIsDelete(BaseStaticParameter.N);
        List<FormMain> formMainList = formMainManager.queryFormMainList(formMain);
        return new ApiResultSet<>(formMainList);
    }


}
