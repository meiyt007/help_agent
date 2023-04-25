package com.zfsoft.ha.data.requestData;

import lombok.Data;
import lombok.ToString;

/**
 * @Description //发送消息接口请求实体类
 * @Author: Wangyh
 * @Date: 2023/3/15 17:05
 */
@Data
@ToString
public class HaMessageRequestData {
    /**
     *房间主键
     */
    private  Long roomOid;
    /**
     * 房间出去记录主键
     */
    private  Long accessOid;
    /**
     * 发送人姓名 用户名称
     */
    private  String userName;
    /**
     * 内容类型;1-文字、2-文件，3-信息确认，4-信息反馈
     */
    private  String contentType;
    /**
     * 文件类型 当内容类型为2时。该字段有值
     */
    private  String fileType;
    /**
     * 确认类型 1-基本信息 2- 申请表
     */
    private  String sureType;
    /**
     * 聊天内容,如果是文件则存的是fastdfs地址
     */
    private  String sendContent	;
    /**
     * 视频咨询的用户编号
     */
    private  Long videoNum;
    /**
     * 信息状态 0-没问题 1-有问题 存到redis中，key为 checkCode:id 且只有在内容类型为4的时候，该字段有值
     */
    private  String informationStatus;
    /**
     * 回复消息编号，当消息为信息反馈时，必须传反馈信息的Oid
     */
    private  String receiveMessageOid;

    /**
     * 确认状态 1-待确认，2-信息无误，3-信息有误
     */
    private  String checkCode;
}
