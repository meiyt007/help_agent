package com.zfsoft.superwindow.service.easyquickcase.data;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(description = "办件打印办件凭证")
public class CasePrintCertificateDto {

    /**
     * 办件主表主键
     */
    @ApiModelProperty(value = "办件主表主键")
    private String caseOid;

    /**
     * 办件编号
     */
    @ApiModelProperty(value = "办件编号")
    private String caseNumber;


    /**
     * 终端编号
     */
    @ApiModelProperty(value = "终端编号")
    private String terminalCode;


    /**
     * 办件人姓名
     */
    @ApiModelProperty(value = "办件人姓名")
    private String applyUserName;

    /**
     * 办件人手机号
     */
    @ApiModelProperty(value = "办件人手机号")
    private String applyUserPhone;

    /**
     * 事项主键
     */
    @ApiModelProperty(value = "事项主键")
    private String serviceOid;

    /**
     * 事项编号
     */
    @ApiModelProperty(value = "事项编号")
    private String basicCode;

    /**
     * 事项名称
     */
    @ApiModelProperty(value = "事项名称")
    private String serviceName;

    /**
     * 办理单位唯一标识
     */
    @ApiModelProperty(value = "办理单位唯一标识")
    private String organOid;

    /**
     * 办理单位名称
     */
    @ApiModelProperty(value = "办理单位名称")
    private String organName;

    /**
     * 办理单位联系电话
     */
    @ApiModelProperty(value = "办理单位联系电话")
    private String organTelphone;


    /**
     * 是否开启工作时间  Y:是, N:否
     */
    @ApiModelProperty(value = "是否开启工作时间  Y:是, N:否")
    private String openWorkTime;

    /**
     * 是否开启文件柜 Y:是, N:否
     */
    @ApiModelProperty(value = "是否开启文件柜 Y:是, N:否")
    private String openFileCabinet;

    /**
     * 材料名称列表
     */
    @ApiModelProperty(value = "材料名称列表")
    private List<String> materialNameList;

    /**
     * 办理单位点位唯一标识
     */
    @ApiModelProperty(value = "办理单位点位唯一标识")
    private String pointOid;
}
