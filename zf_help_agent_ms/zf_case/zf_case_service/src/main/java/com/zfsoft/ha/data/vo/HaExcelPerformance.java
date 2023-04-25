package com.zfsoft.ha.data.vo;

import lombok.Data;

import java.util.Date;

/**
 * 导入电话绩效时的实体类
 * @author zhaobf
 * @version 1.0
 * @date 2023/3/21 15:30
 */
@Data
public class HaExcelPerformance {
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
}
