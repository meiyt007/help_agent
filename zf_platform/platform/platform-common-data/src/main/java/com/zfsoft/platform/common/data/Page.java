package com.zfsoft.platform.common.data;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author: kkfan
 * @create: 2022-04-07 15:58:43
 * @description: 分页参数
 */
@Data
@ApiModel("分页参数")
public class Page {

    @ApiModelProperty(value = "页码")
    @NotBlank(message = "页码不能为空！")
    private Integer pageNum;

    @ApiModelProperty(value = "每页大小")
    @NotBlank(message = "每页大小不能为空！")
    private Integer pageSize;
}
