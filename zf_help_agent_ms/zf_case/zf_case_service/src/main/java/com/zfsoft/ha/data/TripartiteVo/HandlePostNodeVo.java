package com.zfsoft.ha.data.TripartiteVo;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @Description //触发流程操作
 * @Author: Wangyh
 * @Date: 2022/12/7 10:45
 */
@Slf4j
@Data
public class HandlePostNodeVo {
    /**
     * 办件ID
     */
    private String applyID;
    /**
     * 综窗系统中必填
     */
    private String workflowProcID;
    /**
     * 环节编码
     */
    private String processCode;
    /**
     * 环节名称
     */
    private String processName;
    /**
     * 环节描述
     */
    private String processDetail;
    /**
     * 默认不填（true），当为TRUE时，根据NM_AUTO_PROCESS和ST_POST_IDS，自动推进流
     * 程；当为FALSE时，强制不执行自动
     */
    private String autoProcess;
    /**
     *环节的开始时间，为空时，使用上一个环节的结束时间/没有上一个环节的时候，使用
     * endTime
     */
    private String startTime;
    /**
     * 环节的结束时间，为空时，使用接口触发的时间
     */
    private String endTime;
    /**
     * 短信id,传入根据id查询短信发送
     */
    private String nodeDetailId;
    /**
     * 子办件id，传了则仅更新子办件环节
     */
    private String zapplyID;

}
