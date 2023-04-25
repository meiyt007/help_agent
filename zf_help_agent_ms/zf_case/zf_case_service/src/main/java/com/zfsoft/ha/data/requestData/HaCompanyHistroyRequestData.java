package com.zfsoft.ha.data.requestData;


import lombok.Data;

@Data
public class HaCompanyHistroyRequestData {
    //证件类型
    private String credentialType;
    //证件号码
    private String credentialNumber;
    //申请人姓名or申请单位名称
    private String applyUserName;
    //服务状态
    private String serviceStatus;

    //分页参数
    private Integer pageNum;
    private Integer pageSize;
}
