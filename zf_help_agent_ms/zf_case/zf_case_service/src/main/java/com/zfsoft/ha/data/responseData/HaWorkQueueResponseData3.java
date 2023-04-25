package com.zfsoft.ha.data.responseData;

import lombok.Data;
import lombok.ToString;

import java.util.Date;

/**
 * @author zhaobf
 * @version 1.0
 * @date 2022/8/10 13:07
 */
@Data
@ToString
public class HaWorkQueueResponseData3 {
    private String name;  //姓名

    private String companyName;

    private String serviceType;

    private String pushType;

    private String serviceMemo;

    private String sxServiceId;

    private String sxServiceName;

    private String qlCaseId;

    private String serviceStatus;

    private String caseNumber;

    private Date pushDate;
}
