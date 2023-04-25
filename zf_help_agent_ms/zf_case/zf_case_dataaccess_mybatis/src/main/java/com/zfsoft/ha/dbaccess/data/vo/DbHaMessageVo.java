package com.zfsoft.ha.dbaccess.data.vo;

import lombok.Data;
import lombok.ToString;

import java.util.Date;

/**
 * 帮代办消息vo
 *
 * @author yupeng
 * @version 1.0
 * @date 2022年08月12日 15:25:27
 */
@Data
@ToString
public class DbHaMessageVo {


    /**
     * 消息标题
     */
    private String title;

    /**
     * 消息内容
     */
    private String content;

    /**
     * 链接地址
     */
    private String url;

    /**
     * 消息接收人
     */
    private String receiver;

    /**
     * 消息来源
     */
    private String sender;

    /**
     * 消息发送时间
     */
    private Date sendTime;

    /**
     * 消息读取时间
     */
    private Date readTime;

    /**
     * 读取状态
     */
    private String readStatus;
}
