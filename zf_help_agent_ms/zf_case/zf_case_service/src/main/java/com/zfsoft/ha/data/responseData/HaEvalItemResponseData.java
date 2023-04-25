package com.zfsoft.ha.data.responseData;

import lombok.Data;
import lombok.ToString;

/**
 * @description: 评价项响应类
 * @author: kangax
 * @date: 2022-08-12 14:43
 **/
@Data
@ToString
public class HaEvalItemResponseData {
    /**
     * 评价项编号
     */
    private Long id;

    /**
     * 评价项名称
     */
    private String name;

    /**
     * 评价项内容
     */
    private String memo;
}
