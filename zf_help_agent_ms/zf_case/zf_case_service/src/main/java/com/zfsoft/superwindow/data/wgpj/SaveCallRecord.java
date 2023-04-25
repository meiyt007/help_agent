package com.zfsoft.superwindow.data.wgpj;

import lombok.Data;

import java.util.Date;

/**
 * @author sky
 * @version 1.0
 * @description: TODO
 * @date 2021/8/18 10:51
 */
@Data
public class SaveCallRecord {
    /**
     * 主键
     */
    private Long id;
    /**
     * 业务主键
     */
    private String oid;
    /**
     * 叫号信息
     */
    private String callInfo;
    /**
     * 叫号号码
     */
    private String callNum;
    /**
     * 开始时间
     */
    private Date startTime;
    /**
     * 结束时间
     */
    private Date endTime;
    /**
     * 时长秒值
     */
    private Long timeLength;
    /**
     * 登陆人主键
     */
    private String createBy;
    /**
     * 创建此记录时间
     */
    private Date createTime;
    /**
     * 虚拟业务记录表主键
     */
    private String virtualBusinessOid;
    /**
     * 是否删除 0 -- 否 1 -- 是
     */
    private Integer isDelete;
    /**
     * 鍔炰簨浜哄憳鍚嶅瓧
     */
    private String caseUserName;

    //证件号
    private String  cardNo;

    //时长字符串。
    private String  timeLengthString;

}
