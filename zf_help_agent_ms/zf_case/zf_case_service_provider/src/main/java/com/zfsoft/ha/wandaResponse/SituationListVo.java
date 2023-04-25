package com.zfsoft.ha.wandaResponse;

import lombok.Data;
import lombok.ToString;

/**
 * @Description //事项情形列表-万达
 * @Author: Wangyh
 * @Date: 2022/8/23 13:17
 */
@Data
@ToString
public class SituationListVo {
    /**
     *情形唯一编码
     */
    private String statusId;
    /**
     *情形名称
     */
    private String statusName;
    /**
     *情形编码
     */
    private String statusNo;
    /**
     *事项id
     */
    private String itemID;
    /**
     *事项编码
     */
    private String itemNo;
    /**
     *事项名称
     */
    private String itemName;
    /**
     *对应事项库id
     */
    private String databaseID;
    /**
     * 当前情形commitID
     */
    private String commitID;
    /**
     * 排序
     */
    private String order;
}
