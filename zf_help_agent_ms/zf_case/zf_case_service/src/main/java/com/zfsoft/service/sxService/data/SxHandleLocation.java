package com.zfsoft.service.sxService.data;

import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 办理地点信息
 */
@Data
@ToString
public class SxHandleLocation {
    /**
     * 主健
     */
    private Long id;

    /**
     * 业务主键
     */
    private String handleLocationOid;

    /**
     * 所属区划
     */
    private String districtOid;

    /**
     * 所属机构
     */
    private String organOid;

    /**
     * 办理地点名称
     */
    private String locationName;

    /**
     * 办理地点地址
     */
    private String locationAddr;

    /**
     * 办理时间起
     */
    private String acceptDateStart;

    /**
     * 办理时间止
     */
    private String acceptDateEnd;

    /**
     * 监督电话
     */
    private String superviseTel;

    /**
     * 咨询电话
     */
    private String consultTel;

    /**
     * 启用状态(0否、1是)
     */
    private Short enabledFlag;

    /**
     * 删除状态(0否、1是)
     */
    private Short delFlag;

    /**
     * 是否支持电话预约(0否、1是
     */
    private Short appointmentFlag;

    /**
     * 地点分类(0 大厅 1办公室)
     */
    private Short locationType;

    /**
     * 是否公用(0否 1是)
     */
    private Short commonFlag;

    /**
     * 经度
     */
    private BigDecimal longitude;

    /**
     * 纬度
     */
    private BigDecimal latitude;

    /**
     * 办理时间
     */
    private String acceptDate;

    /**
     * 修改时间
     */
    private Date modifyDate;

    /**
     * 地点随机码
     */
    private String locationIid;

    /**
     * 联系人
     */
    private String linkPerson;

    /**
     * 乘车路线
     */
    private String busRoute;
}