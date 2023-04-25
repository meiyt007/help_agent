package com.zfsoft.ha.data.responseData;

import lombok.Data;
import lombok.ToString;

/**
 * @Description //提供万行的，根据选项获取的填报须知响应类
 * @Author: Wangyh
 * @Date: 2022/9/26 19:31
 */
@Data
@ToString
public class FillNoticeRespVo {
    /**
     * 事项编号
     */
    private String serviceOid;
    /**
     * 问题
     */
    private String question;
    /**
     * 答案
     */
    private String answer;
}
