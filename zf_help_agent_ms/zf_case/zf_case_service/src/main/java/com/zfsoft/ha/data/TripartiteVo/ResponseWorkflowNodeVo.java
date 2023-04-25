package com.zfsoft.ha.data.TripartiteVo;

import lombok.Data;
import lombok.ToString;

/**
 * @Description //18.5获取工作流明细响应类
 * @Author: Wangyh
 * @Date: 2022/12/7 11:33
 */
@Data
@ToString
public class ResponseWorkflowNodeVo {
    /**
     * 工作流ID
     */
    private String workflowId;
    /**
     * 环节id
     */
    private String processId;
    /**
     * 环节名称
     */
    private String processName;

    /**
     * 环节code
     */
    private String processCode;
    /**
     * 环节描述
     */
    private String processDetail;
    /**
     * 后置环节ID，多个逗号隔开
     */
    private String postId;
    /**
     * 后置环节名称，多个逗号隔开
     */
    private String postName;
    /**
     * 前置环节ID，多个逗号隔开
     */
    private String preId;

    /**
     * 前置环节名称，多个逗号隔开
     */
    private String preName;
    /**
     * 标签ID
     */
    private String tagId;
    /**
     * 标签名称
     */
    private String tagName;
    /**
     * 标签状态(颜色)
     */
    private String tagExtValue;
    /**
     * 是否自动
     */
    private String isAuto;

    /**
     * 警报时长
     */
    private String errorTime;
    /**
     * 预警时长
     */
    private String warningTime;
    /**
     * 环节横坐标
     */
    private String nmX;
    /**
     * 环节纵坐标
     */
    private String nmY;
}
