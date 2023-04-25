package com.zfsoft.microservice.form.controller;

import com.zfsoft.microservice.form.data.FormMember;
import com.zfsoft.microservice.form.manager.FormMemberManager;
import com.zfsoft.microservice.form.service.FormMemberService;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.platform.common.security.data.CurrentLoginUserHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName FormMemberController
 * @Description 成员管理的实现类
 * @Author wuxx
 * @Date 2021-04-8 10:30
 * @Version V1.0
 **/
@RestController
@Slf4j
public class FormMemberController implements FormMemberService {

    @Resource
    private FormMemberManager formMemberManager;

    /**
     * @description:  查询成员管理的信息列表
     * @param authorizeKey 授权key
     * @param userName 成员名称
     * @author: wuxx
     * @Date: 2020/9/12 10:14
     **/
    public ApiResultSet queryFormMemberWithPage(String authorizeKey,String userName,Integer pageNum,
                                                                              Integer pageSize){
        FormMember formMember = new FormMember();
        formMember.setUserName(userName);
        formMember.setAuthorizeKey(authorizeKey);
        PageResult<FormMember> pageResult = formMemberManager.queryFormMemberWithPage(formMember,pageNum,pageSize);
        String currentUserOid = CurrentLoginUserHolder.getCurrentLoginUser().getUserOid();
        Map<String, Object> map = new HashMap<>();
        map.put("currentUserOid",currentUserOid);
        map.put("pageResult",pageResult);
        //当前登录人
        boolean isAdminUser = CurrentLoginUserHolder.getIsAdminUser();
        //管理员查看所有
        if(isAdminUser){
            map.put("isAdminUser",true);
        }
        return new ApiResultSet<>(map);
    }

    /**
     * @description:  初始化成员管理的信息
     * @param id 成员管理实体类主键
     * @author: wuxx
     * @Date: 2020/9/12 10:14
     **/
    @Override
    public ApiResultSet initFormMember(Long id){
        FormMember  formMember = formMemberManager.getFormMemberById(id);
        return new ApiResultSet<>(formMember);
    }

    /**
     * @description:  成员管理的新增或者修改
     * @param formMember 成员管理实体类
     * @author: wuxx
     * @Date: 2020/9/12 10:14
     **/
    public ApiResultSet<FormMember> saveFormMember(@RequestBody FormMember formMember){
        formMemberManager.saveFormMember(formMember);
        return new ApiResultSet<>(formMember);
    }

    /**
     * @description:  成员管理的信息的删除
     * @param id 成员管理实体类主键
     * @author: wuxx
     * @Date: 2020/9/12 10:14
     **/
    public ApiResultSet<Integer> deleteFormMemberById(Long id){
        formMemberManager.deleteFormMemberById(id);
        return new ApiResultSet<>();
    }

    /**
     * @description:  获取成员管理的信息
     * @param id 成员管理实体类主键
     * @author: wuxx
     * @Date: 2020/9/12 10:14
     **/
    public ApiResultSet<FormMember> getFormMemberById(Long id){
        FormMember formMember = formMemberManager.getFormMemberById(id);
        return new ApiResultSet<>(formMember);
    }

    /**
     * @description:  获取成员管理的信息
     * @param memberOid 业务主键
     * @author: wanglei
     * @Date: 2020/10/29 11:40
     **/
    public ApiResultSet<FormMember> getFormMemberByMemberOid(@PathVariable("memberOid")String memberOid){
        FormMember formMember = formMemberManager.getFormMemberByMemberOid(memberOid);
        return new ApiResultSet<>(formMember);
    }

    @Override
    public ApiResultSet<List<FormMember>> queryFormMemberList(String authorizeKey) {
        FormMember formMember = new FormMember();
        formMember.setAuthorizeKey(authorizeKey);
        List<FormMember> formMemberList = formMemberManager.queryFormMemberList(formMember);
        return new ApiResultSet<>(formMemberList);
    }

}
