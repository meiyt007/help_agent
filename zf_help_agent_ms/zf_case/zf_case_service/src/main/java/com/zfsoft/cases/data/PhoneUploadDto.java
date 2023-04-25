package com.zfsoft.cases.data;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author hut
 * @date 2022/6/7
 */
@lombok.Data
public class PhoneUploadDto {
    /**
     * 材料名称
     */
    @ApiModelProperty(value = "材料名称")
    private String materialName;

    /**
     * 过期时间毫秒值
     */
    @ApiModelProperty(value = "过期时间毫秒值")
    private long expireDate;

    /**
     * 扫描状态：1：已扫描，未提交； 2：已提交；
     */
    @ApiModelProperty(value = "扫描状态：1：已扫描，未提交； 2：已提交；")
    private String scanStatus;


    /**
     * 附件主键拼接字符串以逗号隔开
     */
    @ApiModelProperty(value = "附件主键拼接字符串以逗号隔开")
    private String attaOids;

    @ApiModelProperty(value = "附件地址拼接字符串以逗号隔开")
    private String fastDFSPaths;
}
