package com.zfsoft.service.sxService.data;

import lombok.Data;
import lombok.ToString;

import java.util.Date;

/**
 * 实施清单-常见问题
 */
@Data
@ToString
public class SxServiceQuestion {
    /**
     * 主健
     */
    private Long id;

    /**
     * 业务主键
     */
    private String questionOid;

    /**
     * 所属事项
     */
    private String serviceOid;

    /**
     * 主题词
     */
    private String keyWord;

    /**
     * 问题标题
     */
    private String title;

    /**
     * 删除状态(0否、1是)
     */
    private Short delFlag;
    /**
     * 问题描述
     */
    private String description;

    /**
     * 问题解答
     */
    private String answer;

    /**
     * 创建时间
     */
    private Date createDate;

    /**
     * 创建人
     */
    private String createUser;

    /**
     * 排序号
     */
    private Long sort;

    /**
     * 启用状态(0否、1是)
     */
    private Short enabledFlag;

}