package com.zfsoft.ha.data.TripartiteVo;

import lombok.Data;
import lombok.ToString;

/**
 * @Description //提交办件接口响应类
 * @Author: Wangyh
 * @Date: 2022/9/20 17:26
 */
@Data
@ToString
public class ResponseSubmitVo {
    /**
     * 结果物名称
     */
    private String stPrintName;
    /**
     * 结果物接口路径
     */
    private String stPrintPath;
}
