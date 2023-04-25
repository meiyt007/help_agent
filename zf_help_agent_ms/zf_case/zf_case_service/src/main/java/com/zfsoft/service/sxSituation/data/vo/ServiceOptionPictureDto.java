package com.zfsoft.service.sxSituation.data.vo;

import lombok.Data;
import lombok.ToString;

/**
 * 选项关系图
 * xldong
 */

@Data
@ToString
public class ServiceOptionPictureDto  {

    /**
     * 主键
     */
    private String oid;
    /**
     * 名称
     */
    private String name;
    /**
     * x 坐标
     */
    private double x;
    /**
     * y 坐标
     */
    private double y;
    /**
     * 类型 0-标题 1 - 值
     */
    private String type;
    /**
     * 唯一标识
     */
    private String uniqueness;


}
