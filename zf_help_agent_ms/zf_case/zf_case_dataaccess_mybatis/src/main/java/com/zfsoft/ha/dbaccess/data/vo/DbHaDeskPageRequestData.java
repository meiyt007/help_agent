package com.zfsoft.ha.dbaccess.data.vo;

import lombok.Data;

import java.util.Date;

/**
 * @author zhaobf
 * @version 1.0
 * @date 2022/12/26 11:21
 */
@Data
public class DbHaDeskPageRequestData {

    /**
     * 经办人姓名
     */
    private String name;
    /**
     * 经办人手机号
     */
    private String phone;

    /**
     * 企业名称
     */
    private String companyName;
    /**
     * 企业统一社会信用代码
     */
    private String companyCode;

    /**
     * 状态 0未预约，1预约中，2已预约，3已发起一桌联办
     */
    private String deskStatus;


    /**
     * 筛查开始时间
     */
    private Date beginTime;
    /**
     * 筛查结束时间
     */
    private Date endTime;

    private Long workUserId;
}
