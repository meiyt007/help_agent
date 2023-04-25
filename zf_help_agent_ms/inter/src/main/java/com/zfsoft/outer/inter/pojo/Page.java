package com.zfsoft.outer.inter.pojo;


import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author: kkfan
 * @create: 2022-04-07 15:58:43
 * @description: 分页参数
 */
@Data
public class Page {

    @NotBlank(message = "页码不能为空！")
    private Integer pageNum;

    @NotBlank(message = "每页大小不能为空！")
    private Integer pageSize;
}
