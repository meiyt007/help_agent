package com.zfsoft.ha.front.controller;

import com.zfsoft.ha.data.HaLoginUserInfo;
import com.zfsoft.ha.data.HaMessage;
import com.zfsoft.ha.data.vo.HaMessageVo;
import com.zfsoft.ha.front.MessageService;
import com.zfsoft.ha.managers.HaMessageManager;
import com.zfsoft.ha.util.HaLoginUserHolder;
import com.zfsoft.platform.common.data.ApiResultSet;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Description //消息,控制层
 * @Author: Wangyh
 * @Date: 2022/7/19 9:41
 */
@RestController
@Slf4j
public class MessageController implements MessageService {
    /**
     * 消息接口实现层
     */
    @Resource
    private HaMessageManager haMessageManager;

    /**
     * 获取未读消息的数量
     * @return ApiResultSet 获取未读消息的数量
     * @Author: Wangyh
     * @Date: 2022/7/19 9:41
     */
    @Override
    public ApiResultSet getNoReadNum() {
        log.info("获取未读消息的数量");
        try {
            //获取当前用户信息
            HaLoginUserInfo haLoginUserInfo = HaLoginUserHolder.getCurrentHaLoginUserInfo();
            Map<String, Object> map = new HashMap<>();
            long noReadNum = haMessageManager.getNoReadNum(haLoginUserInfo);
            map.put("noReadNum", noReadNum);
            return ApiResultSet.ok("获取未读消息的数量成功", map);
        } catch (Exception e) {
            log.error("获取未读消息的数量方法失败：", e);
            return new ApiResultSet<>(500, "获取未读消息的数量方法失败", e.getMessage());
        }
    }

    /**
     * 获取消息列表
     * @param title     消息标题，模糊查询
     * @param beginDate 消息开始时间
     * @param endDate   消息结束时间
     * @return ApiResultSet 获取消息列表详细信息
     * @Author: Wangyh
     * @Date: 2022/7/19 9:41
     */
    @Override
    public ApiResultSet queryMessageList(String title, String beginDate, String endDate) {
        log.info("获取消息列表，title:{},beginDate:{},endDate:{}", title, beginDate, endDate);
        try {
            //获取当前用户信息
            HaLoginUserInfo haLoginUserInfo = HaLoginUserHolder.getCurrentHaLoginUserInfo();
            List<HaMessageVo> haMessageList = haMessageManager.queryMessageList(haLoginUserInfo,title, beginDate, endDate);
            log.debug("messageList:{}", haMessageList);
            return ApiResultSet.ok("获取消息列表成功", haMessageList);
        } catch (Exception e) {
            log.error("获取未读消息的数量方法失败：", e);
            return new ApiResultSet<>(500, "获取未读消息的数量方法失败", e.getMessage());
        }
    }

    /**
     * 获取消息详细信息
     * @param id 主键
     * @return ApiResultSet 获取消息详细信息
     * @Author: Wangyh
     * @Date: 2022/7/19 9:41
     */
    @Override
    public ApiResultSet getMessageInfo(Long id) {
        log.info("获取消息详细信息，id:{}", id);
        try {
            HaMessageVo haMessageVo = haMessageManager.getMessageInfo(id);
            if(haMessageVo != null){
                return ApiResultSet.ok("获取消息详细信息成功", haMessageVo);
            }else{
                return new ApiResultSet<>(500, "未查询到该条信息");
            }

        } catch (Exception e) {
            log.error("获取消息详细信息方法错误：", e);
            return new ApiResultSet<>(500, "获取消息详细信息方法错误", e.getMessage());
        }
    }

    /**
     * 读取消息
     * @param id 主键
     * @return ApiResultSet 读取消息详细信息
     * @Author: Wangyh
     * @Date: 2022/7/19 9:41
     */
    @Override
    public ApiResultSet readMessage(Long id) {
        log.info("读取消息，id:{}", id);
        try {
            HaMessageVo haMessageVo = haMessageManager.getMessageInfo(id);
            if(haMessageVo != null){
                return ApiResultSet.ok("读取消息成功", haMessageVo);
            }else{
                return new ApiResultSet<>(500, "未查询到该条信息");
            }

        } catch (Exception e) {
            log.error("读取消息方法错误：", e);
            return new ApiResultSet<>(500, "读取消息方法错误", e.getMessage());
        }

    }

    /**
     * 批量删除消息
     * @param ids 主键集合
     * @return ApiResultSet 批量删除消息详细信息
     * @Author: Wangyh
     * @Date: 2022/7/19 9:41
     */
    @Override
    public ApiResultSet deleteMessage(String ids) {
        log.info("批量删除消息，ids:{}", ids);
            try {
                haMessageManager.batchMessageid(ids);
                return ApiResultSet.ok("批量删除成功");
            } catch (Exception e) {
                log.error("批量删除消息方法错误：", e);
                return new ApiResultSet<>(500, "批量删除消息方法错误", e.getMessage());
            }

    }
}
