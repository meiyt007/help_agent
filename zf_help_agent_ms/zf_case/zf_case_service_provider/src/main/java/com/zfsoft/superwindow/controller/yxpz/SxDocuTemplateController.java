package com.zfsoft.superwindow.controller.yxpz;

import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.superwindow.data.yxpz.SxDocuTemplate;
import com.zfsoft.superwindow.service.yxpz.SxDocuTemplateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @ClassName SxDocuTemplateController
 * @Description 参数配置管理的实现类
 * @Author liangxm
 * @Date 2020-09-12 10:30
 * @Version V1.0
 **/
@RestController
@Slf4j
public class SxDocuTemplateController implements SxDocuTemplateService {

    @Resource
    private com.zfsoft.superwindow.manager.yxpz.SxDocuTemplateManager SxDocuTemplateManager;



    /**
     * @description:  参数配置的新增或者修改
     * @param SxDocuTemplate 参数配置实体类
     * @author: liangxm
     * @Date: 2020/9/12 10:14
     **/
    @Override
    public ApiResultSet<SxDocuTemplate> saveSxDocuTemplate(@RequestBody SxDocuTemplate SxDocuTemplate){
        SxDocuTemplateManager.saveSxDocuTemplate(SxDocuTemplate);
        return new ApiResultSet<>(SxDocuTemplate);
    }

    @Override
    public ApiResultSet<PageResult<SxDocuTemplate>> querySxDocuTemplateWithPage(String docuTemplateName,Integer pageNum, Integer pageSize) {
        SxDocuTemplate SxDocuTemplate = new SxDocuTemplate();
        SxDocuTemplate.setDocuTemplateName(docuTemplateName);
//        sysConfig.setName(name);
//        sysConfig.setCode(code);
//        sysConfig.setParentOid(parentOid);
        PageResult<SxDocuTemplate> pageResult = SxDocuTemplateManager.querySxDocuTemplateWithPage(SxDocuTemplate,pageNum,pageSize);
        ApiResultSet<PageResult<SxDocuTemplate>> apiResultSet = new ApiResultSet<>();
        apiResultSet.setData(pageResult);
        return apiResultSet;
    }

    @Override
    public ApiResultSet initSxDocuTemplate(String id) {
        SxDocuTemplate SxDocuTemplate = SxDocuTemplateManager.getSxDocuTemplateById(Long.valueOf(id));
        return new ApiResultSet<SxDocuTemplate>(SxDocuTemplate);
    }

    @Override
    public ApiResultSet<Integer> deleteSxDocuTemplateById(String id) {
        SxDocuTemplateManager.deleteSxDocuTemplateById(Long.valueOf(id));
        return  new ApiResultSet();
    }

    @Override
    public ApiResultSet<Integer> ableSxDocuTemplateById(String id) {
        SxDocuTemplateManager.ableSxDocuTemplateById(Long.valueOf(id));
        return new ApiResultSet();
    }

    @Override
    public ApiResultSet<SxDocuTemplate> getSxDocuTemplateById(String id) {
        SxDocuTemplate SxDocuTemplate = SxDocuTemplateManager.getSxDocuTemplateById(Long.valueOf(id));
        return new ApiResultSet<SxDocuTemplate>(SxDocuTemplate);
    }


}
