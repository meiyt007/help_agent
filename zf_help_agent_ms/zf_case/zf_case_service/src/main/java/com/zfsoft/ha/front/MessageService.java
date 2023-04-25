package com.zfsoft.ha.front;

import com.zfsoft.ha.data.HaWorkUser;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.utils.feign.ProcessFeignCalledResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Description //消息接口
 * @Author: Wangyh
 * @Date: 2022年7月25日13:29:22
 */
@RequestMapping("/ha/message")
public interface MessageService {


    /**
     * @param //token在header中
     * @description: 获取未读消息的数量
     * @author: Wangyh
     * @Date: 2022/7/18 21:10
     **/
    @RequestMapping(value = "/getNoReadNum", method = {RequestMethod.GET})
    ApiResultSet<HaWorkUser> getNoReadNum();


    /**
     * @param title           消息标题，模糊查询
     * @param beginDate       消息开始时间
     * @param endDate         消息结束时间
     * @param //Token在header中
     * @description: 获取消息列表
     * @author: Wangyh
     * @Date: 2022/7/18 21:10
     **/
    @RequestMapping(value = "/queryMessageList", method = {RequestMethod.POST})
    ApiResultSet<HaWorkUser> queryMessageList(
           @RequestParam(value = "title", required = false) String title,
           @RequestParam(value = "beginDate", required = false) String beginDate,
           @RequestParam(value = "endDate", required = false) String endDate
           );

    /**
     * @param id              消息标题，消息编号
     * @param //Token在header中
     * @description: 获取消息详细信息
     * @author: Wangyh
     * @Date: 2022/7/18 21:10
     **/
    @RequestMapping(value = "/getMessageInfo", method = {RequestMethod.GET})
    ApiResultSet<HaWorkUser> getMessageInfo(
            @RequestParam(value = "id", required = true) Long id);

    /**
     * @param id 消息标题，消息编号
     * @param //Token在header中
     * @description: 读取消息
     * @author: Wangyh
     * @Date: 2022/7/18 21:10
     **/
    @RequestMapping(value = "/readMessage", method = {RequestMethod.GET})
    ApiResultSet<HaWorkUser> readMessage(
                                            @RequestParam(value = "id", required = true) Long id);

    /**
     * @param ids 主键
     * @description: 批量删除消息
     * @author: wangyh
     * @Date: 2022/7/15
     **/
    @ProcessFeignCalledResult
    @RequestMapping(value = "/deleteMessage", method = {RequestMethod.POST})
    ApiResultSet deleteMessage(@RequestParam(value = "ids", required = true) String ids);
}
