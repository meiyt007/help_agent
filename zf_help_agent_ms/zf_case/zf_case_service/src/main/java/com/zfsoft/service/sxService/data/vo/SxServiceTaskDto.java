package com.zfsoft.service.sxService.data.vo;

import lombok.Data;
import lombok.ToString;

/*
 * @Description:目录清单待办任务
 * @Author: wangxl
 * @Date: 2021/2/23
 **/
@Data
@ToString
public class SxServiceTaskDto {

    //taskInfo.id
    private String id;
    /**
     * 任务定义key
     */
    private String taskDefinitionKey;
    /**
     * 名称
     */
    private String name;
    /**
     * 实例化主键
     */
    private String processInstanceId;
    /**
     * 进程id
     */
    private String executionId;
    /**
     * 定义唯一标识
     */
    private String processDefinitionId;
//    private String linkOid;
    private String linkName;
    private String actionName;
    private String typeCode;
//    private String rejectTaskId;
//    private String fallbackTaskId;
    private String auditOid;

    private String contentStatus; //审核状态
    private String indexOid;
    private String dirOid;
    private String contentOid;
    private String lableTypeOid;
    private String  districtOid;
    private String organOid;
    private String remark;
    private Integer contentVersion;
    private String factorValue1; // 列表值1
    private String factorValue2; // 列表值2
    private String factorValue3; // 列表值3
    private String factorValue4; // 列表值4
    private String factorValue5; // 列表值5
    private String factorValue6; // 列表值5
    private Integer isXiafang;
    private Integer isCityLevel;

    public void setFactorValue(String factorValue, String number){
        if("1".equals(number)){
            this.factorValue1 = factorValue;
        }else if("2".equals(number)){
            this.factorValue2 = factorValue;
        }else if("3".equals(number)){
            this.factorValue3 = factorValue;
        }else if("4".equals(number)){
            this.factorValue4 = factorValue;
        }else if("5".equals(number)){
            this.factorValue5 = factorValue;
        }else if("6".equals(number)){
            this.factorValue6 = factorValue;
        }
    }

    public String getFactorValue(String number){
        String factorValue = null;
        if("1".equals(number)){
            factorValue = this.factorValue1;
        }else if("2".equals(number)){
            factorValue = this.factorValue2;
        }else if("3".equals(number)){
            factorValue = this.factorValue3;
        }else if("4".equals(number)){
            factorValue = this.factorValue4;
        }else if("5".equals(number)){
            factorValue = this.factorValue5;
        }else if("6".equals(number)){
            factorValue = this.factorValue6;
        }
        return factorValue;
    }
}
