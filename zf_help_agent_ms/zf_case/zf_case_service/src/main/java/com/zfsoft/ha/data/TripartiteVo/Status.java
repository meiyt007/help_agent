package com.zfsoft.ha.data.TripartiteVo;

import lombok.Data;
import lombok.ToString;

/**
 * @Description //类名称
 * @Author: Wangyh
 * @Date: 2022/9/16 16:38
 */
@Data
@ToString
public class Status {
    /**
     * 非必填，情形ID
     */
    private String statusId;
    /**
     * 情形编码33位编码
     */
    private String statusNo;
    /**
     * 必填，情形名称
     */
    private String statusName;
}
