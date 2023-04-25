package com.zfsoft.service.dto;

import lombok.Data;
import lombok.ToString;

/*
 * @Description:事项类型待办数量
 * @Author: yuy
 * @Date: 2021/4/19
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
