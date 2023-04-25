package com.zfsoft.microservice.form.service;

import com.zfsoft.microservice.form.data.FormAtta;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.utils.feign.ProcessFeignCalledResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @ClassName FormAttaService
 * @Description 表单附件组件服务定义接口
 * @Author wuxx
 * @Date 2021-05-26 11:33
 * @Version V1.0
 **/
@RequestMapping("/manager/security/formAtta")
public interface FormAttaService {
    /**
     * @description:  增加一个新附件
     * @param formAtta 附件
     * @author: wuxx
     * @Date: 2021/5/26 13:48
     **/
    @RequestMapping(value = "/save", method = {RequestMethod.POST})
    ApiResultSet<FormAtta> saveFormAtta(@RequestBody FormAtta formAtta);

    /**
     * @param attaOid 附件实体类业务主键
     * @description: 获取附件的信息
     * @author: wuxx
     * @Date: 2021/5/26 10:14
     **/
    @RequestMapping(value = "/getFormAttaByAttaOid/{attaOid}", method = {RequestMethod.GET})
    ApiResultSet<FormAtta> getFormAttaByAttaOid(@PathVariable("attaOid") String attaOid);

    /**
     * @description:  根据id 集合 获取 atta信息
     * @param attaOidList id主键集合
     * @author: wuxx
     * @Date: 2020/10/28 17:28
     **/
    @RequestMapping( value = "/getAttaListByOidList",method = {RequestMethod.POST})
    ApiResultSet getAttaListByOidList(@RequestBody List<String> attaOidList);

    /**
     * 删除指定Id的附件信息
     * @param id
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping( value = "/delete/{id}",method = {RequestMethod.DELETE})
    ApiResultSet<Integer>  deleteFormAttaById(@PathVariable("id") Long id);

    /**
     * @description:  获取附件
     * @param id 主键
     * @author: wuxx
     * @Date: 2021/04/2 10:14
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/getOne/{id}",method = {RequestMethod.GET})
    ApiResultSet<FormAtta> getFormAttaById(@PathVariable("id") Long id);
}
