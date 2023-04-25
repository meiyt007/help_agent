package com.zfsoft.superwindow.service.easyquickcase.data;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class SignerDto {

    @ApiModelProperty(value = "签署人姓名", required = true)
    private String name;

    @ApiModelProperty(value = "签署人手机", required = true)
    private String mobile;

    @ApiModelProperty(value = "签署人企业名称")
    private String orgName;

    @ApiModelProperty(value = "签署人企业统一社会信用代码")
    private String orgUscc;
}
