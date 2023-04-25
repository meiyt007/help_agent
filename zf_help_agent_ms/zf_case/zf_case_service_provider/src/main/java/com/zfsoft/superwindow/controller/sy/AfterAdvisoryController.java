package com.zfsoft.superwindow.controller.sy;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zfsoft.cases.feign.SysOrganFeginService;
import com.zfsoft.cases.feign.SysUserFeginService;
import com.zfsoft.microservice.platform.data.sys.SysOrgan;
import com.zfsoft.microservice.platform.data.sys.SysUser;
import com.zfsoft.microservice.platform.service.sys.SysUserService;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.platform.common.security.data.CurrentLoginUser;
import com.zfsoft.platform.common.security.data.CurrentLoginUserHolder;
import com.zfsoft.superwindow.data.sy.AfterAdvisoryry;
import com.zfsoft.superwindow.manager.sy.AfterAdvisoryManager;
import com.zfsoft.superwindow.service.sy.AfterAdvisoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: kkfan
 * @create: 2020-10-31 11:52:14
 * @description: 后援接口控制层
 */
@Slf4j
@RestController
@RequestMapping(value = "/afterAdvisory")
public class AfterAdvisoryController implements AfterAdvisoryService {
    @Resource
    private AfterAdvisoryManager afterAdvisoryManager;
    @Resource
    private SysOrganFeginService sysOrganFeginService;

    @Resource
    private SysUserFeginService sysUserFeginService;

    @Override
    @PostMapping(value = "/pageList")
    public ApiResultSet queryPageList(AfterAdvisoryry afterAdvisoryry,
                                      @RequestParam(value = "pageNum", required = false, defaultValue = "1") Integer pageNum,
                                      @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize) {
        Map<String, Object> resultMap = new HashMap<>();
        ApiResultSet apiResultSet = new ApiResultSet<>();
        PageHelper.startPage(pageNum, pageSize);
        List<AfterAdvisoryry> afterAdvisoryryList = this.afterAdvisoryManager.queryList(afterAdvisoryry);
        if(afterAdvisoryryList.size()>0){
            for(AfterAdvisoryry newAfterAdvisoryry : afterAdvisoryryList){
                //援助人名称
                ApiResultSet<SysUser> replyUser = sysUserFeginService.getSysUserByUserOid(newAfterAdvisoryry.getReplyUserOid());
                if(null!=replyUser){
                    //log.info("获取用户接口结果为：{}", JSON.toJSONString(replyUser));
                    newAfterAdvisoryry.setReplyUserName(replyUser.getData().getName());
                    newAfterAdvisoryry.setReplyOrganName(replyUser.getData().getOrganName());
                }
                //援助组织名称
                ApiResultSet<SysOrgan> replyOrgan=sysOrganFeginService.getSysOrganByOrganOid(newAfterAdvisoryry.getReplyOrganOid());
                if(null!=replyOrgan){
                    newAfterAdvisoryry.setReplyOrganName(replyOrgan.getData().getName());
                }
                //咨询人名称
                ApiResultSet<SysUser> advisoryUser = sysUserFeginService.getSysUserByUserOid(newAfterAdvisoryry.getAdvisoryUserOid());
                if(null!=advisoryUser){
                    newAfterAdvisoryry.setAdvisoryUserName(advisoryUser.getData().getName());
                }
            }

            }
        PageResult<AfterAdvisoryry> pageResult = new PageResult<>(((Page) afterAdvisoryryList).getPageNum(), ((Page) afterAdvisoryryList).getPageSize(), ((Page) afterAdvisoryryList).getTotal());
        pageResult.setData(afterAdvisoryryList);

        ApiResultSet<SysUser> sysUser = null;
        int advistatus=0;
        String organOid="";
        CurrentLoginUser loginUser = CurrentLoginUserHolder.getCurrentLoginUser();
        if(null!=loginUser){
            sysUser= sysUserFeginService.getSysUserByUserOid(loginUser.getUserOid());
            if(null!=sysUser){
                SysUser user=sysUser.getData();
                log.info("获取用户接口结果为：{}", JSON.toJSONString(user));
                if(null!=user.getAdviStatus()){
                    advistatus= user.getAdviStatus();
                }
               // advistatus=user.getAdviStatus();
                organOid=sysUser.getData().getOrganOid();
            }
        }
       /* apiResultSet.setData(pageResult);*/
       /* log.info("获取后援列表调用成功结果为：{}", JSON.toJSONString(pageResult));*/
        resultMap.put("pageResult", pageResult);
        resultMap.put("sysUser", sysUser);
        resultMap.put("advistatus", advistatus);
        resultMap.put("organOid", organOid);
        apiResultSet.setData(resultMap);
        return apiResultSet;
    }
    @Override
    @PostMapping(value = "/queryReplyState")
    public ApiResultSet queryReplyState() {
        Integer replyState = this.afterAdvisoryManager.queryReplyState();
        log.info("获取援助状态, replyState：", replyState);
        return new ApiResultSet(replyState);
    }
    @Override
    @PostMapping(value = "/changeReplyState")
    public ApiResultSet changeReplyState(String id, Integer replyState) {
        this.afterAdvisoryManager.changeReplyState(id, replyState);
        log.info("保存/更新后援咨询（呼叫）信息成功, replyState：", replyState);
        return new ApiResultSet();
    }
    @Override
    @PostMapping(value = "/checkIsCall")
    public ApiResultSet checkIsCall(String replyOrganOid,String replyUserOid) {
        Boolean isCall = this.afterAdvisoryManager.checkIsCall(replyOrganOid, replyUserOid);
        log.info("校验是否已呼叫咨询接口调用程成功：{}", isCall);
        return new ApiResultSet(isCall);
    }
    @Override
    @PostMapping(value = "/saveOrUpdate")
    public ApiResultSet saveOrUpdate(@RequestBody AfterAdvisoryry afterAdvisoryry) {
        this.afterAdvisoryManager.saveOrUpdate(afterAdvisoryry);
        log.info("保存/更新后援咨询（呼叫）信息成功：{}", JSON.toJSONString(afterAdvisoryry));
        return new ApiResultSet(afterAdvisoryry);
    }
    @Override
    @PostMapping(value = "/delete")
    public ApiResultSet delete(String ids) {
        this.afterAdvisoryManager.delete(ids);
        log.info("删除成功：{}", ids);
        return new ApiResultSet(ids);
    }
    @Override
    @PostMapping(value = "/getOne")
    public ApiResultSet getOne(String id) {
        AfterAdvisoryry afterAdvisoryry = this.afterAdvisoryManager.getOne(id);
        log.info("详情获取成功：{}", JSON.toJSONString(afterAdvisoryry));
        return new ApiResultSet(afterAdvisoryry);
    }

    @Override
    @GetMapping(value = "/getFreeUserTreeByOrgan")
    public ApiResultSet getFreeUserTreeByOrgan(String organOid) {
       ApiResultSet res= afterAdvisoryManager.getFreeUserTreeByOrgan(organOid);
        return res;
    }

    //设置设置当前登录人状态     1 -- 忙碌   0 -- 空闲
    @PostMapping(value = "/setReplyState")
    public ApiResultSet setReplyState(String advStatus) {
        ApiResultSet apiResultSet = new ApiResultSet<>();
        CurrentLoginUser loginUser = CurrentLoginUserHolder.getCurrentLoginUser();
        String message="";
        if(null!=loginUser){
            ApiResultSet<SysUser> sysUser = sysUserFeginService.getSysUserByUserOid(loginUser.getUserOid());
            SysUser user=sysUser.getData();
            if(null!=sysUser){
                log.info("用户详情：{}", JSON.toJSONString(sysUser));
                int newAdvistatus=user.getAdviStatus();
                //int newAdvistatus=1;
                boolean b = false;
                if(0==newAdvistatus){//空闲
                    //查询是否有未处理的咨询
                    //回复状态（0-等待，1-接受，2-拒绝）
                    Long count=this.afterAdvisoryManager.queryAfterAdvisoryCount(user.getOrganOid(),newAdvistatus,user.getUserOid());
                    if(count>0){
                        message="您有未处理的咨询，不能设为【空闲】状态！";
                    }else{
                        user.setAdviStatus(1);
                        b = true;
                    }
                }else{//忙碌
                    user.setAdviStatus(0);
                    b = true;
                }
                log.info("用户信息详情：{}", JSON.toJSONString(user));
                log.info("newAdvistatus详情：{}", JSON.toJSONString(user.getAdviStatus()));
                ApiResultSet ss= sysUserFeginService.updateSysUser(user);
                log.info("返回信息：{}", JSON.toJSONString(ss));
               /* sysUserFeginService.updateSysUser(user);*/
            }

        }
        apiResultSet.setMessage(message);
        return  apiResultSet;

    }



}
