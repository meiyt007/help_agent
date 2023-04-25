package com.zfsoft.microservice.platform.service.sys;

import com.zfsoft.microservice.platform.data.sys.SysMessage;
import com.zfsoft.microservice.platform.data.sys.SysMessageSended;
import com.zfsoft.platform.common.data.ApiResultSet;
import org.springframework.web.bind.annotation.*;

/**
 * @ClassName SysMessageService
 * @Description 消息组件服务定义接口
 * @Author wuxx
 * @Date 2020-10-23 13:33
 * @Version V1.0
 **/
@RequestMapping("/security/message")
public interface SysMessageService {
    /**
     * 增加一个新消息
     * @param sysMessage 新消息
     * @return
     */
    @RequestMapping(value = "/send", method = {RequestMethod.POST})
    ApiResultSet<SysMessage> sendSysMessage(@RequestBody SysMessage sysMessage);

    /**
     * 删除指定Id的消息信息
     *
     * @param oid 消息id
     * @return
     */
    @RequestMapping(value = "/delete/{oid}", method = {RequestMethod.POST})
    ApiResultSet<Integer> deleteSysMessageById(@PathVariable("oid") Long oid);

    /**
     * @param id 消息实体类主键
     * @description: 查看消息并修改查看状态
     * @author: wuxx
     * @Date: 2020/10/23 14:14
     **/
    @RequestMapping(value = "/read/{id}", method = {RequestMethod.POST})
    ApiResultSet<Integer> readSysMessage(@PathVariable("id") Long id);

    /**
     * @description:  根据消息Id获取消息信息
     * @param id 主键
     * @author: wuxx
     * @Date: 2020/10/23 13:38
     **/
    @RequestMapping(value = "/getOne/{id}", method = {RequestMethod.GET})
    ApiResultSet<SysMessage> getSysMessageById(@PathVariable("id") Long id);

    /**
     * 分页查询消息信息列表
     *
     * @param title     标题
     * @param readStatus  读取状态 0未读 1已读
     * @param pageNum 开始页
     * @param pageSize   每页大小
     * @return
     */
    @RequestMapping(value = "/page", method = {RequestMethod.GET})
    ApiResultSet querySysMessageWithPage(@RequestParam(value = "title", required = false) String title,
                                         @RequestParam(value = "readStatus", required = false) Integer readStatus,
                                         @RequestParam("pageNum") Integer pageNum,
                                         @RequestParam("pageSize") Integer pageSize);
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
    @RequestMapping(value = "/sendPage", method = {RequestMethod.GET})
    ApiResultSet querySysMessageSendedPage(@RequestParam(value = "title", required = false) String title,
                                         @RequestParam(value = "userName", required = false) String userName,
                                         @RequestParam(value = "beginDate", required = false) String beginDate,
                                         @RequestParam(value = "endDate", required = false) String endDate,
                                           @RequestParam("pageNum") Integer pageNum,
                                           @RequestParam("pageSize") Integer pageSize);

    /**
     * 删除指定Id的已发送消息信息
     * @param id 消息id
     * @return
     */
    @RequestMapping(value = "/deleteSended/{id}", method = {RequestMethod.POST})
    ApiResultSet<Integer> deleteSysMessageSendedById(@PathVariable("id") Long id);


    /**
     * @description:  根据消息Id获取已发送消息
     * @param id 主键
     * @author: wuxx
     * @Date: 2020/10/23 13:38
     **/
    @RequestMapping(value = "/getOneSended/{id}", method = {RequestMethod.GET})
    ApiResultSet<SysMessageSended> getSysMessageSendedById(@PathVariable("id") Long id);

    /**
     * @description:  根据用户oid获取未读消息
     * @author: wuxx
     * @Date: 2020/10/28 13:38
     **/
    @RequestMapping(value = "/getCountOfUnReadMessagebyUserOid", method = {RequestMethod.GET})
    ApiResultSet<Integer> getCountOfUnReadMessagebyUserOid();

}
