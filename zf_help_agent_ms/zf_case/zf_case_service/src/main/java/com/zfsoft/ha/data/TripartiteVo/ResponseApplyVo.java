package com.zfsoft.ha.data.TripartiteVo;

import lombok.Data;
import lombok.ToString;

/**
 * @Description //获取办件编码对象
 * @Author: Wangyh
 * @Date: 2022/9/19 17:05
 */
@Data
@ToString
public class ResponseApplyVo {
    /**
     * 办件id
     */
    private String applyId;
    /**
     *  办件编码
     */
    private String itemAreaCode;
    /**
     * 情形id
     */
    private String statusId;
    /**
     * 流程id
     */
    private String processId;
    /**
     *  办件编码
     */
    private String applyNo;
    /**
     *  情形名称
     */
    private String statusName;
    /**
     * 单事项：单部门；主题事项：综合
     */
    private String type;
    /**
     *  情形编码
     */
    private String statusCode;
}
