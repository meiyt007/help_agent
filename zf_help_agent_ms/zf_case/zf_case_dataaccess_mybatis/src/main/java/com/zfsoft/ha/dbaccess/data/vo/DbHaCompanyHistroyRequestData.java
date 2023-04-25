package com.zfsoft.ha.dbaccess.data.vo;

import lombok.Data;

/**
 * @author zhaobf
 * @version 1.0
 * @date 2022/11/16 9:08
 */
@Data
public class DbHaCompanyHistroyRequestData {

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
