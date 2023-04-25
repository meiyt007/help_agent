package com.zfsoft.ha.data;

import lombok.Data;
import lombok.ToString;

import java.util.Date;

/**
 * @Description //消息表
 * @Author: Wangyh
 * @Date: 2022/7/19 9:36
 */
@Data
@ToString
public class HaMessage {
    /**
     * 新增用户，验证规则组
     */
    public interface INSERT_GROUP{};

    /**
     * 更新用户，验证规则组
     */
    public interface UPDATE_GROUP{};

    /**
     * 主键
     */
    private Long id;
    /**
     * 标题
     */
    private String title;
    /**
     * 内容
     */
    private String content;
    /**
     * 链接地址
     */
    private String url;
    /**
     * 发送人;系统发送时，设置为-1
     */
    private Long sendUserId;
    /**
     * 发送时间
     */
    private Date sendTime;
    /**
     * 接收人
     */
    private Long receiveUserId;
    /**
     * 读取状态;1-未读取，2-已读取
     */
    private String readStatus;
    /**
     * 读取时间
     */
    private Date readTime;
    /**
     * 删除状态;1-未删除，2-已删除
     */
    private String deleteStatus;
    /**
     * 创建人
     */
    private String createBy;
    /**
     * 创建时间
     */
    private Date createDate;
    /**
     * 更新人
     */
    private String updateBy;
    /**
     * 更新时间
     */
    private Date updateDate;

    /**
     * 消息接收人
     */
    private String receiver;

    /**
     * 消息来源
     */
    private String sender;

}
