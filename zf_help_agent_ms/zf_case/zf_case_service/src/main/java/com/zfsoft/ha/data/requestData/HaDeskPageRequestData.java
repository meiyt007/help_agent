package com.zfsoft.ha.data.requestData;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @author zhaobf
 * @version 1.0
 * @date 2022/12/26 11:21
 */
@Data
public class HaDeskPageRequestData {

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
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date beginTime;
    /**
     * 筛查结束时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date endTime;

    private Long workUserId;
}
