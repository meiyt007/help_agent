package com.zfsoft.ha.data.TripartiteVo;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @Description //数据推送（增加材料,提交办件,材料添加附件并关联派生）
 * @Author: Wangyh
 * @Date: 2022/9/16 10:19
 */
@Slf4j
@Data
public class PushDataVo {
    /**
     * 办件编号
     */
    private String caseOid;
    /**
     * 事项code-区域code，用于表单访问  获取办件编号接口返回
     */
    private String itemAreaCode;
    /**
     * 办件id  获取办件编号接口返回
     */
    private String applyId;
    /**
     * 办件编码   获取办件编号接口返回
     */
    private String applyNo;
    /**
     * 流程id   获取办件编号接口返回
     */
    private String processId;
    /**
     * 单事项：单部门；主题事项：综合   获取办件编号接口返回
     */
    private String type;
    /**
     * 事项id
     */
    private String itemId;
    /**
     * 事项名称
     */
    private String itemName;
    /**
     * 事项code
     */
    private String itemCode;

    /**
     * 情形id
     */
    private String statusId;
}
