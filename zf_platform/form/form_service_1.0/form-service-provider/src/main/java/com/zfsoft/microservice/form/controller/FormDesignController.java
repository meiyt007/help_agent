package com.zfsoft.microservice.form.controller;

import com.zfsoft.microservice.form.data.FormDesign;
import com.zfsoft.microservice.form.manager.FormDesignManager;
import com.zfsoft.microservice.form.service.FormDesignService;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.data.BaseStaticParameter;
import com.zfsoft.platform.common.data.PageResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @ClassName FormDesignController
 * @Description 表单设计历史设计表的实现类
 * @Author wuxx
 * @Date 2021-04-16 13:30
 * @Version V1.0
 **/
@RestController
@Slf4j
public class FormDesignController implements FormDesignService {

    @Resource
    private FormDesignManager formDesignManager;

    /**
     * @description:  查询表单设计历史设计表的信息列表
     * @param formMainOid 设计主键
     * @param moduleOid 模块主键
     * @author: wuxx
     * @Date: 2021-04-16 13:30
     **/
    @Override
    public ApiResultSet<PageResult<FormDesign>> queryFormDesignWithPage(String formMainOid, String moduleOid, Integer pageNum,
                                                                                      Integer pageSize){
        FormDesign formDesign = new FormDesign();
        formDesign.setFormMainOid(formMainOid);
        formDesign.setModuleOid(moduleOid);
        PageResult<FormDesign> pageResult = formDesignManager.queryFormDesignWithPage(formDesign,pageNum,pageSize);
        return new ApiResultSet<>(pageResult);
    }

    /**
     * @description: 初始化表单设计历史设计表的信息
     * @param id 表单设计历史设计表实体类主键
     * @author: wuxx
     * @Date: 2021-04-16 13:30
     **/
    @Override
    public ApiResultSet initFormDesign(Long id){
        FormDesign formDesign = formDesignManager.getFormDesignById(id);
        return new ApiResultSet<>(formDesign);
    }

    /**
     * @description:  表单设计历史设计表的信息的删除
     * @param id 表单设计历史设计表实体类主键
     * @author: wuxx
     * @Date: 2021-04-16 13:30
     **/
    @Override
    public ApiResultSet<Integer>  deleteFormDesignById(Long id){
        formDesignManager.deleteFormDesignById(id);
        return new ApiResultSet<>();
    }

    /**
     * @description:  获取表单设计历史设计表的信息
     * @param id 表单设计历史设计表实体类主键
     * @author: wuxx
     * @Date: 2021-04-16 13:30
     **/
    @Override
    public ApiResultSet<FormDesign>  getFormDesignById(Long id){
        FormDesign formDesign = formDesignManager.getFormDesignById(id);
        return new ApiResultSet<>(formDesign);
    }

    /**
     * @description:  获取表单设计历史设计表的信息
     * @param designOid 业务主键
     * @author: wuxx
     * @Date: 2021-04-16 13:30
     **/
    @Override
    public ApiResultSet<FormDesign>  getFormDesignByDesignOid(String designOid){
        FormDesign formDesign = formDesignManager.getFormDesignByDesignOid(designOid);
        return new ApiResultSet<>(formDesign);
    }

    @Override
    public ApiResultSet<FormDesign> getFormDesignByFormMainOid(String formMainOid) {
        FormDesign formDesign = formDesignManager.getFormDesignByFormMainOid(formMainOid);
        return new ApiResultSet<>(formDesign);
    }

    /**
     * @description:  表单设计历史设计表的查询
     * @param formMainOid 表单主键
     * @author: wuxx
     * @Date: 2021-04-16 13:30
     **/
    @Override
    public ApiResultSet<List<FormDesign>> queryFormDesignList(String formMainOid) {
        FormDesign formDesign = new FormDesign();
        formDesign.setIsDelete(BaseStaticParameter.N);
        formDesign.setFormMainOid(formMainOid);
        List<FormDesign> formDesignList = formDesignManager.queryFormDesignList(formDesign);
        return new ApiResultSet<>(formDesignList);
    }


}
