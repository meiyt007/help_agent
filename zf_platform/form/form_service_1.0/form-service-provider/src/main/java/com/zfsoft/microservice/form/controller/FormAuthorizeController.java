package com.zfsoft.microservice.form.controller;

import cn.hutool.core.util.StrUtil;
import com.zfsoft.microservice.form.data.FormAuthorize;
import com.zfsoft.microservice.form.data.FormStaticParameter;
import com.zfsoft.microservice.form.manager.FormAuthorizeManager;
import com.zfsoft.microservice.form.manager.FormMemberManager;
import com.zfsoft.microservice.form.service.FormAuthorizeService;
import com.zfsoft.microservice.form.util.GenDataTreeUtil;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.platform.common.security.data.CurrentLoginUserHolder;
import com.zfsoft.platform.utils.web.TreeSelect;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName FormAuthorizeController1
 * @Description 接入系统授权管理的实现类
 * @Author wuxx
 * @Date 2021-03-11 10:30
 * @Version V1.0
 **/
@RestController
@Slf4j
public class FormAuthorizeController implements FormAuthorizeService {

    @Resource
    private FormAuthorizeManager formAuthorizeManager;
    @Resource
    private FormMemberManager formMemberManager;

    /**
     * @description:  查询接入系统授权管理的信息列表
     * @param authorizeKey 授权key
     * @param systemName 接入系统名称
     * @author: wuxx
     * @Date: 2020/9/12 10:14
     **/
    public ApiResultSet<PageResult<FormAuthorize>> queryFormAuthorizeWithPage(String authorizeKey,String systemName,Integer pageNum,
                                                                              Integer pageSize){
        List<String> authorizeKeyList = new ArrayList<>();
        FormAuthorize formAuthorize = new FormAuthorize();
        if(StrUtil.isNotBlank(authorizeKey)){
            authorizeKeyList.add(authorizeKey);
        }else{
            try {
                //当前登录人
                boolean isAdminUser = CurrentLoginUserHolder.getIsAdminUser();
                //管理员查看所有
                if(isAdminUser){
                    formAuthorize.setAuthorizeKey(FormStaticParameter.DEFAULT_AUTHORIZEKEY);
                }else {
                    if(null!=CurrentLoginUserHolder.getCurrentLoginUser()){
                        //非管理员查看所属的成员
                        List<String> keyList = formMemberManager.queryAuthorizeKeyListByUserOid(CurrentLoginUserHolder.getCurrentLoginUser().getUserOid());
                        authorizeKeyList.addAll(keyList);
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        formAuthorize.setAuthorizeKeyList(authorizeKeyList);
        formAuthorize.setSystemName(systemName);
        PageResult<FormAuthorize> pageResult = formAuthorizeManager.queryFormAuthorizeWithPage(formAuthorize,pageNum,pageSize);
        return new ApiResultSet<>(pageResult);
    }

    /**
     * @description:  初始化接入系统授权管理的信息
     * @param id 接入系统授权管理实体类主键
     * @author: wuxx
     * @Date: 2020/9/12 10:14
     **/
    @Override
    public ApiResultSet initFormAuthorize(Long id){
        FormAuthorize  formAuthorize = formAuthorizeManager.getFormAuthorizeById(id);
        return new ApiResultSet<>(formAuthorize);
    }

    /**
     * @description:  接入系统授权管理的新增或者修改
     * @param formAuthorize 接入系统授权管理实体类
     * @author: wuxx
     * @Date: 2020/9/12 10:14
     **/
    public ApiResultSet<FormAuthorize> saveFormAuthorize(@RequestBody FormAuthorize formAuthorize){
        formAuthorizeManager.saveFormAuthorize(formAuthorize);
        return  new ApiResultSet<>(formAuthorize);
    }

    /**
     * @description:  接入系统授权管理的信息的删除
     * @param id 接入系统授权管理实体类主键
     * @author: wuxx
     * @Date: 2020/9/12 10:14
     **/
    public ApiResultSet<Integer> deleteFormAuthorizeById(Long id){
        formAuthorizeManager.deleteFormAuthorizeById(id);
        return new ApiResultSet<>();
    }

    /**
     * @description:  获取接入系统授权管理的信息
     * @param id 接入系统授权管理实体类主键
     * @author: wuxx
     * @Date: 2020/9/12 10:14
     **/
    public ApiResultSet<FormAuthorize> getFormAuthorizeById(Long id){
        FormAuthorize formAuthorize = formAuthorizeManager.getFormAuthorizeById(id);
        return new ApiResultSet<>(formAuthorize);
    }

    /**
     * @description:  获取接入系统授权管理的信息
     * @param authorizeKey 授权key
     * @author: wanglei
     * @Date: 2020/10/29 11:40
     **/
    public ApiResultSet<FormAuthorize> getFormAuthorizeByAuthorizeKey(@PathVariable("authorizeKey")String authorizeKey){
        FormAuthorize formAuthorize = formAuthorizeManager.getFormAuthorizeByAuthorizeKey(authorizeKey);
        return new ApiResultSet<>(formAuthorize);
    }

    /**
     * @description:  接入系统授权管理的信息的启禁用
     * @param id 接入系统授权管理实体类主键
     * @author: wuxx
     * @Date: 2020/9/12 10:14
     **/
    public ApiResultSet<Integer> ableFormAuthorizeById(@PathVariable("id")Long id){
        int rows = formAuthorizeManager.ableFormAuthorizeById(id);
        ApiResultSet<Integer> apiResultSet = new ApiResultSet<>();
        apiResultSet.setData(rows);
        return apiResultSet;
    }

    @Override
    public ApiResultSet<List<FormAuthorize>> queryFormAuthorizeList(String authorizeKey) {
        FormAuthorize formAuthorize = new FormAuthorize();
        formAuthorize.setAuthorizeKey(authorizeKey);
        List<FormAuthorize> formAuthorizeList = formAuthorizeManager.queryFormAuthorizeList(formAuthorize);
        return  new ApiResultSet<>(formAuthorizeList);
    }

    /**
     * @description: 根据授权key查询表单接入管理的tree
     * @author: wuxx
     * @Date: 2021/4/7 13:16
     **/
    @Override
    public ApiResultSet<List<TreeSelect>> queryAuthorizeTree(String userOid) {
        String authorizeKey = null;
        List<String> authorizeKeyList = new ArrayList<>();
        FormAuthorize formAuthorize = new FormAuthorize();
        Map<String, Object> map = new HashMap();
        map.put("isAdminUser",false);
        //当设置了用户oid
        //userOid = FormStaticParameter.DEFAULT_AUTHORIZEKEY;
        if(StrUtil.isNotBlank(userOid)){
            map.put("userOid",userOid);
            List<String> keyList = formMemberManager.queryAuthorizeKeyListByUserOid(userOid);
            authorizeKeyList.addAll(keyList);
            /*SysUser sysUser = sysUserFeignService.getSysUserByUserOid(userOid).getData();
            SysDictFeignService sysDictFeignService = SpringContextHelper.getBean(SysDictFeignService.class);
            SysDict sysDict = sysDictFeignService.getSysDictByDictOid(sysUser.getType()).getData();
            String userCode = null!=sysDict?sysDict.getCode():null;
            //管理员
            if(null!=userCode && userCode.equals("GR-D1487902326132")){
                map.put("isAdminUser",true);
                authorizeKey = FormStaticParameter.DEFAULT_AUTHORIZEKEY;
                formAuthorize.setAuthorizeKey(authorizeKey);
            }else{
                List<String> keyList = formMemberManager.queryAuthorizeKeyListByUserOid(userOid);
                authorizeKeyList.addAll(keyList);
            }*/
        }else{
            try {
                //当前登录人
                boolean isAdminUser = CurrentLoginUserHolder.getIsAdminUser();
                map.put("isAdminUser",isAdminUser);
                //管理员查看所有
                if(isAdminUser){
                    authorizeKey = FormStaticParameter.DEFAULT_AUTHORIZEKEY;
                    formAuthorize.setAuthorizeKey(authorizeKey);
                }else {
                    //非管理员查看所属的成员
                    if(null!=CurrentLoginUserHolder.getCurrentLoginUser()){
                        List<String> keyList = formMemberManager.queryAuthorizeKeyListByUserOid(CurrentLoginUserHolder.getCurrentLoginUser().getUserOid());
                        authorizeKeyList.addAll(keyList);
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        formAuthorize.setAuthorizeKeyList(authorizeKeyList);
        List<FormAuthorize> formAuthorizeList = formAuthorizeManager.queryFormAuthorizeList(formAuthorize);
        List<TreeSelect> treeSelectList = GenDataTreeUtil.buildFormAuthorizeTreeSelect(formAuthorizeList);
        map.put("treeSelectList",treeSelectList);
        map.put("authorizeKey",authorizeKey);
        return new ApiResultSet(map);
    }

}
