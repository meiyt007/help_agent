package com.zfsoft.superwindow.data.yxpz;

import lombok.Data;
import lombok.ToString;

import java.util.Date;

/**
 * @author wangwg
 * @description 一件事平板评价选项
 * @date 2021-02-25
 * @copyright 上海卓繁信息技术股份有限公司版权所有
 */
@Data
@ToString
public class EvaluationOption {

    /**
     * 主键
     */
    private Long id;

    /**
     * 评价选项id
     */
    private String optionOid;

    /**
     * 评价标准id
     */
    private String standardOid;

    /**
     * 评价选项内容
     */
    private String optionContent;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 创建时间
     */
    private Date createDate;

    /**
     * 是否有效
     */
    private Integer delFlag;
}
