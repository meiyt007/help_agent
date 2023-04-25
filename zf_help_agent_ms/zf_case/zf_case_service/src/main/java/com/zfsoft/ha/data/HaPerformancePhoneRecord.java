package com.zfsoft.ha.data;

import lombok.Data;

import java.util.Date;

@Data
public class HaPerformancePhoneRecord {
    /**
     * 主键
     */
    private Long id;

    /**
     * 来电号码
     */
    private String phone;
    /**
     * 姓名
     */
    private String name;
    /**
     * 日期
     */
    private Date callTime;
    /**
     * 时长
     */
    private String duration;
    /**
     * 评价
     */
    private String appraise;
    /**
     * 通话备注
     */
    private String callNotes;
    /**
     * 意向结果
     */
    private String intentResults;
    /**
     * 呼叫结果
     */
    private String callResults;
    /**
     * 工号
     */
    private String workNumber;




    /**
     *
     */
    private String deleteStatus;

    /**
     *
     */
    private String createBy;

    /**
     *
     */
    private Date createDate;

    /**
     *
     */
    private String updateBy;

    /**
     *
     */
    private Date updateDate;

}