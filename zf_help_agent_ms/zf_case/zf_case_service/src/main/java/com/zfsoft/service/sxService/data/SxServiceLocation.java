package com.zfsoft.service.sxService.data;

import lombok.Data;
import lombok.ToString;

import java.util.Date;

/**
 * 实施清单-办理地址
 */
@Data
@ToString
public class SxServiceLocation {
    /**
     *主健
     */
    private Long id;

    /**
     * 业务主键
     */
    private String serviceLocationOid;

    /**
     * 所属事项扩展表
     */
    private String extendOid;

    /**
     * 所属办理地点
     */
    private String handleLocationOid;

    /**
     * 办理地点名称
     */
    private String locationName;

    /**
     * 办理地点地址
     */
    private String locationAddr;

    /**
     * 办理时间
     */
    private String acceptDate;

    /**
     * 删除状态(0否、1是)
     */
    private Short delFlag;

    /**
     * 修改时间
     */
    private Date modifyDate;

    /**
     * 乘车路线
     */
    private String busRoute;

    /**
     * 咨询电话
     */
    private String phone;

}