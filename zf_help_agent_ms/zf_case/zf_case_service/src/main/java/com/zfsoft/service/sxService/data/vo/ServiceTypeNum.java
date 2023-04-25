package com.zfsoft.service.sxService.data.vo;

import lombok.Data;
import lombok.ToString;

/**
 * @Description:事项类型待办数量
 *
 * @Author: wangwg
 * @Date: 2021/8/26
 **/
@Data
@ToString
public class ServiceTypeNum {

    /**
     * 事项类型主键
     */
    private String oid;
    /**
     * 名称
     */
    private String name;
    /**
     * 待办数量
     */
    private Integer number;

}
