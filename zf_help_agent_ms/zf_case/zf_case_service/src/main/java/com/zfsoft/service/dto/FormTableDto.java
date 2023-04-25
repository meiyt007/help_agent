package com.zfsoft.service.dto;

import lombok.Data;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

/*
 * 表dto
 *
 * @Author: yuy
 * @Date: 2021/8/5
 **/
@Data
@ToString
public class FormTableDto {

    //表面
    private String tableName;

    //列名
    private String columnName;
    /**
     * 字段类型
     */
    private String columnType;
    /**
     * 最大长度
     */
    private String maxLenth;
    /**
     * 备注
     */
    private String demo;

    List<FormTableDto> columnList = new ArrayList<>();

}
