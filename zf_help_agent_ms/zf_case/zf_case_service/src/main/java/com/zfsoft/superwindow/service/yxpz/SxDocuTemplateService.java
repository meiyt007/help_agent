package com.zfsoft.superwindow.service.yxpz;

import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.platform.utils.feign.ProcessFeignCalledResult;
import com.zfsoft.superwindow.data.yxpz.SxDocuTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @ClassName SxDocuTemplateService
 * @Description 文书模板服务定义接口
 * @Author liangxm
 * @Date 2020-11-1 11:33
 * @Version V1.0
 **/
@RequestMapping("/manage/sxDocuTemplate")
public interface SxDocuTemplateService {


    /**
     * @description:  文书模板的新增或者修改
     * @param SxDocuTemplate 文书模板实体类
     * @author: liangxm
     * @Date: 2020/11/01 10:14
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/save",method = {RequestMethod.POST})
    ApiResultSet<SxDocuTemplate> saveSxDocuTemplate(@RequestBody SxDocuTemplate SxDocuTemplate);


    /**
     * @description:  查询文书模板的信息列表
   
     * @author: liangxm
     * @Date: 2020/11/01 10:14
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/page",method = {RequestMethod.GET})
    ApiResultSet<PageResult<SxDocuTemplate>> querySxDocuTemplateWithPage(
            @RequestParam(value = "docuTemplateName") String docuTemplateName,
            @RequestParam(value = "pageNum") Integer pageNum,
            @RequestParam(value = "pageSize") Integer pageSize);

    /**
     * @description:  初始化文书模板的信息
     * @param id 文书模板实体类主键
     * @author: liangxm
     * @Date: 2020/11/01 10:14
     **/
    @ProcessFeignCalledResult
    @RequestMapping(value = {"/init"},method = {RequestMethod.GET})
    ApiResultSet initSxDocuTemplate(String id);

    /**
     * 删除指定Id的文书模板信息
     * @param id 文书模板id
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping( value = "/delete/{id}",method = {RequestMethod.POST})
    ApiResultSet<Integer>  deleteSxDocuTemplateById(String id);

    /**
     * 删除指定Id的文书模板信息
     * @param id 文书模板id
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping( value = "/able/{id}",method = {RequestMethod.POST})
    ApiResultSet<Integer>  ableSxDocuTemplateById(String id);

    

    /**
     * @description:  获取文书模板的信息
     * @param id 文书模板实体类主键
     * @author: liangxm
     * @Date: 2020/11/01 10:14
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/getOne/{id}",method = {RequestMethod.GET})
    ApiResultSet<SxDocuTemplate>  getSxDocuTemplateById(String id);

}
