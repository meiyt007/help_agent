package com.zfsoft.superwindow.service.easyquickcase.data;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @description 电子签章
 * @author meiyt
 * @date 2022/5/23
 **/
@Data
@ApiModel(description = "电子签章接口调用信息")
public class ElectronicSignatureDto {

    @ApiModelProperty(value = "签署人信息集合", required = true)
    private List<SignerDto> signerDTOList;

    @ApiModelProperty(value = "材料主键")
    private String materialOid;

    @ApiModelProperty(value = "办件编号", required = true)
    private String caseOid;

    @ApiModelProperty(value = "签署文件集合")
    private List<Map<String, String>> fileList;

    @ApiModelProperty(value = "调用类型")
    private String callType;

    @ApiModelProperty(value = "接口id")
    private String interId;

    @ApiModelProperty(value = "页面回跳地址")
    private String redirectUrl;
}
