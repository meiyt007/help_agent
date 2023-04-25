package com.zfsoft.ha.manager.controller;

import com.zfsoft.ha.data.HaMessage;
import com.zfsoft.ha.dbaccess.data.vo.DbHaMessageVo;
import com.zfsoft.ha.manager.HaMessageManagementService;
import com.zfsoft.ha.managers.HaMessageManager;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.data.PageResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;

/**
 * 帮代办消息Controller
 *
 * @author yupeng
 * @version 1.0
 * @date 2022年08月12日 15:11:44
 */
@Slf4j
@Controller
public class HaMessageManagementController implements HaMessageManagementService {

    /**
     * 帮代办消息manager
     */
    @Resource
    private HaMessageManager haMessageManager;

    /**
     * 查询消息列表
     *
     * @param receiver 消息发送者
     * @param sender   消息接收者
     * @param title    消息标题
     * @param pageNum  分页参数
     * @param pageSize 分页参数
     * @return 返回消息列表
     * @author yupeng
     * @date 2022年08月12 15:09:50
     */
    @Override
    public ApiResultSet queryMessagePage(String receiver, String sender, String title, Integer pageNum, Integer pageSize) {
        log.info("查询帮代办信息列表，messageReceiver：{}，messageSource：{}，messageTitle：{}，pageNum：{}，pageSize：{}", receiver, sender, title, pageNum, pageSize);
        PageResult<HaMessage> messagePageResult = haMessageManager.queryMessagePage(receiver, sender, title, pageNum, pageSize);
        return ApiResultSet.ok(messagePageResult);
    }
}
