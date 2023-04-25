package com.zfsoft.microservice.form.service;

import com.zfsoft.microservice.form.data.FormMember;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.utils.feign.ProcessFeignCalledResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @ClassName FormMemberService
 * @Description 成员组件服务定义接口
 * @Author wuxx
 * @Date 2021-03-10 16:10
 * @Version V1.0
 **/
@RequestMapping("/manager/security/member")
public interface FormMemberService {

    /**
     * @description:  查询成员的信息列表
     * @param userName 成员名称
     * @param authorizeKey 成员授权key
     * @author: wuxx
     * @Date: 2021/04/8 10:14
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/page",method = {RequestMethod.GET})
    ApiResultSet queryFormMemberWithPage(@RequestParam(value = "authorizeKey") String authorizeKey,
                                            @RequestParam(value = "userName", required = false) String userName,
                                            @RequestParam(value = "pageNum", required = false) Integer pageNum,
                                            @RequestParam(value = "pageSize", required = false) Integer pageSize);

    /**
     * @description:  初始化成员的信息
     * @param id 成员实体类主键
     * @author: wuxx
     * @Date: 2021/04/8 10:14
     **/
    @ProcessFeignCalledResult
    @RequestMapping(value = {"/init"},method = {RequestMethod.GET})
    ApiResultSet initFormMember(@RequestParam(value = "id") Long id);

    /**
     * 删除指定Id的成员信息
     * @param id 成员id
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping( value = "/delete/{id}",method = {RequestMethod.DELETE})
    ApiResultSet<Integer>  deleteFormMemberById(@PathVariable("id") Long id);

    /**
     * @description:  成员的新增或者修改
     * @param formMember 成员实体类
     * @author: wuxx
     * @Date: 2021/04/8 10:14
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/save",method = {RequestMethod.POST})
    ApiResultSet<FormMember> saveFormMember(@RequestBody FormMember formMember);

    /**
     * @description:  获取成员的信息
     * @param id 成员实体类主键
     * @author: wuxx
     * @Date: 2021/04/8 10:14
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/getOne/{id}",method = {RequestMethod.GET})
    ApiResultSet<FormMember> getFormMemberById(@PathVariable("id") Long id);

    /**
     * @description:  获取成员的信息
     * @param memberOid 成员业务主键
     * @author: wuxx
     * @Date: 2021/04/8 10:14
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/getFormMemberByMemberOid/{memberOid}",method = {RequestMethod.GET})
    ApiResultSet<FormMember> getFormMemberByMemberOid(@PathVariable("memberOid") String memberOid);

    /**
     * @description:  根据授权key查询成员列表
     * @param authorizeKey 授权key
     * @author: wuxx
     * @Date: 2021/04/8 10:14
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/queryFormMemberList",method = {RequestMethod.GET})
    ApiResultSet<List<FormMember>> queryFormMemberList(@RequestParam("authorizeKey") String authorizeKey);

}
