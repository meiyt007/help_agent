package com.zfsoft.superwindow.service.sy;

import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.superwindow.data.sy.AfterAdvisoryry;

/**
 * @author: kkfan
 * @create: 2020-10-31 11:57:01
 * @description:    后援接口
 */
public interface AfterAdvisoryService {
    /**
     * 分页查询后援列表
     * @param afterAdvisoryry
     * @param pageNum
     * @param pageSize
     * @return
     */
    ApiResultSet queryPageList(AfterAdvisoryry afterAdvisoryry,
                               Integer pageNum,
                               Integer pageSize);

    /**
     * 查询忙碌状态
     * @return  0 -- 空闲   1 -- 忙碌
     */
    ApiResultSet queryReplyState();


    /**
     * 改变忙碌状态   0 -- 否   1 -- 是
     * @param replyState
     * @return
     */
    ApiResultSet changeReplyState(String id, Integer replyState);

    /**
     * 检查是否被呼叫
     * @param replyOrganOid
     * @param replyUserOid
     * @return
     */
    ApiResultSet checkIsCall(String replyOrganOid, String replyUserOid);

    /**
     * 保存/更新操作
     * @param afterAdvisoryry
     * @return
     */
    ApiResultSet saveOrUpdate(AfterAdvisoryry afterAdvisoryry);

    /**
     * 删除操作  支持批量删除 id,id
     * @param ids
     * @return
     */
    ApiResultSet delete(String ids);

    /**
     * 获取数据详情
     * @param id
     * @return
     */
    ApiResultSet getOne(String id);

    /**
     * 获取机构下的空闲用户
     * @param organOid
     * @return
     */
    ApiResultSet getFreeUserTreeByOrgan(String organOid);
}
