package com.zfsoft.ha.wandaResponse;

import lombok.Data;
import lombok.ToString;

/**
 * @Description //万达常见问题对象
 * @Author: Wangyh
 * @Date: 2022/8/25 13:33
 */
@Data
@ToString
public class IssueVO {
    /**
     * 问答id
     */
    private String issueID;
    /**
     * 更新人
     */
    private String updateUser;
    /**
     *
     */
    private String commitID;
    /**
     * 重复次数
     */
    private String trigger;
    /**
     * 回答文本
     */
    private String feedback;
    /**
     * 回答时间
     */
    private String feedbackTime;
    /**
     * 问题类型
     */
    private String issueType;
    /**
     * 事项id
     */
    private String itemID;
    /**
     * 部门名称
     */
    private String organName;
    /**
     * 全局事项名称
     */
    private String itemName;
    /**
     * 情形id(万达的情形就是事项id)
     */
    private String statusID;
    /**
     * 创建时间
     */
    private String createTime;

    /**
     *
     */
    private String organCode;
    /**
     * 全局情形名称
     */
    private String statusName;
    /**
     * 问题文本
     */
    private String issueInfo;
    /**
     * 创建人
     */
    private String createUser;
    /**
     * 状态
     */
    private String state;
}
