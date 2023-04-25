package com.zfsoft.microservice.platform.controller.sys;

import com.zfsoft.microservice.platform.data.sys.SysMessage;
import com.zfsoft.microservice.platform.data.sys.SysMessageSended;
import com.zfsoft.microservice.platform.data.sys.SysUser;
import com.zfsoft.microservice.platform.dbaccess.data.sys.DbSysMessage;
import com.zfsoft.microservice.platform.manager.sys.SysMessageManager;
import com.zfsoft.microservice.platform.manager.sys.SysMessageSendedManager;
import com.zfsoft.microservice.platform.manager.sys.SysUserManager;
import com.zfsoft.microservice.platform.service.sys.SysMessageService;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.platform.common.security.data.CurrentLoginUser;
import com.zfsoft.platform.common.security.data.CurrentLoginUserHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @ClassName SysMessageController
 * @Description 消息管理的实现类
 * @Author wuxx
 * @Date 2020-10-23 14:30
 * @Version V1.0
 **/
@RestController
@Slf4j
public class SysMessageController implements SysMessageService{

    @Resource
    private SysMessageManager sysMessageManager;

    @Resource
    private SysMessageSendedManager sysMessageSendedManager;
    @Autowired
    private SysUserManager sysUserManager;
    /**
     * @param sysMessage 消息实体类
     * @description: 发送消息
     * @author: wuxx
     * @Date: 2020/10/23 14:14
     **/
    @Override
    public ApiResultSet<SysMessage> sendSysMessage(SysMessage sysMessage) {
        CurrentLoginUser currentLoginUser = CurrentLoginUserHolder.getCurrentLoginUser();
        SysUser user = sysUserManager.getSysUserByUserOid(currentLoginUser.getUserOid());
        sysMessage.setUserName(null!=user?user.getName():"");
        sysMessageManager.sendSysMessage(sysMessage,currentLoginUser.getUserOid());
        return new ApiResultSet<>(sysMessage);
    }

    /**
     * @param id 消息实体类主键
     * @description: 查看消息并修改查看状态
     * @author: wuxx
     * @Date: 2020/10/23 14:14
     **/
    @Override
    public ApiResultSet readSysMessage(Long id) {
        sysMessageManager.readSysMessage(id);
        return new ApiResultSet<>();
    }


    /**
     * @param oid 消息实体类主键
     * @description: 消息的信息的删除
     * @author: wuxx
     * @Date: 2020/10/23 14:14
     **/
    @Override
    public ApiResultSet<Integer> deleteSysMessageById(Long oid) {
        sysMessageManager.deleteSysMessageById(oid);
        return new ApiResultSet<>();
    }

    /**
     * @param oid 消息实体类主键
     * @description: 获取消息的信息
     * @author: wuxx
     * @Date: 2020/10/23 14:14
     **/
    @Override
    public ApiResultSet<SysMessage> getSysMessageById(Long oid) {
        SysMessage sysMessage = sysMessageManager.getSysMessageById(oid);
        return new ApiResultSet<>(sysMessage);
    }

    /**
     * 分页查询消息信息列表
     *
     * @param title     标题
     * @param readStatus  读取状态 0未读 1已读
     * @param pageNum 开始页
     * @param pageSize   每页大小
     * @return
     */
    @Override
    public ApiResultSet querySysMessageWithPage(String title, Integer readStatus,Integer pageNum,Integer pageSize) {
        String userOid = CurrentLoginUserHolder.getCurrentLoginUser().getUserOid();
        //性能测试使用
        //PageResult<DbSysMessage> pageResult = sysMessageManager.queryDbSysMessageWithPage(title,readStatus,userOid, pageNum, pageSize);
        PageResult<SysMessage> pageResult = sysMessageManager.querySysMessageWithPage(title,readStatus,userOid, pageNum, pageSize);
        return new ApiResultSet<>(pageResult);
    }

    /**
     * 分页查询已发送消息信息列表
     * @param title     标题
     * @param userName     接受人
     * @param beginDate     开始时间
     * @param endDate  接受时间
     * @param pageNum 开始页
     * @param pageSize   每页大小
     * @return
     */
    @Override
    public ApiResultSet querySysMessageSendedPage(String title, String userName, String beginDate, String endDate, Integer pageNum, Integer pageSize) {
        String sendUserOid = CurrentLoginUserHolder.getCurrentLoginUser().getUserOid();
        PageResult<SysMessageSended> sendedPageResult = sysMessageSendedManager.querySysMessageSendedPage(title, userName,sendUserOid, beginDate, endDate, pageNum, pageSize);
        return new ApiResultSet<>(sendedPageResult);
    }


    /**
     * 删除指定Id的已发送消息信息
     * @param id 消息id
     * @return
     */
    @Override
    public ApiResultSet<Integer> deleteSysMessageSendedById(Long id) {
        sysMessageSendedManager.deleteSysMessageSendedById(id);
        return new ApiResultSet<>();
    }

    /**
     * @description:  根据消息Id获取已发送消息
     * @param id 主键
     * @author: wuxx
     * @Date: 2020/10/23 13:38
     **/
    @Override
    public ApiResultSet<SysMessageSended> getSysMessageSendedById(Long id) {
        SysMessageSended messageSended= sysMessageSendedManager.getSysMessageSendedById(id);
        return new ApiResultSet<>(messageSended);
    }

    @Override
    public ApiResultSet<Integer> getCountOfUnReadMessagebyUserOid() {
        String userOid = CurrentLoginUserHolder.getCurrentLoginUser().getUserOid();
        int count = sysMessageManager.getCountOfUnReadMessagebyUserOid(userOid);
        ApiResultSet apiResultSet = new ApiResultSet<>();
        apiResultSet.setData(count);
        return apiResultSet;
    }

}
