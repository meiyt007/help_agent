package com.zfsoft.ha.data.TripartiteVo;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @Description //获取办件编码
 * @Author: Wangyh
 * @Date: 2022/9/15 10:34
 */
@Slf4j
@Data
public class getWdCserIdVo {
    /**
     * 办件编号
     */
    private String caseOid;

    /**
     * 所属事项编号
     */
    private String serviceOid;
}
