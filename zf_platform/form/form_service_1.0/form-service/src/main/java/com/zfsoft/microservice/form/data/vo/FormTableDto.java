package com.zfsoft.microservice.form.data.vo;

import lombok.Data;

/**
 * @ClassName FormColumnVo
 * @Description:
 * @Author wuxx
 * @Date 2021/8/6
 **/
@Data
public class FormTableDto {

    //表名称
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

    /**
     * 是否创建索引 默认false 不创建  true创建
     */
    private Boolean indexFlag;

    /**
     * 是否为空 默认为null   true不为null
     */
    private Boolean isNotNull;

    /**
     * 删除  默认为null不删除   true删除
     */
    private Boolean delFlag;

    /**
     * 修改字段前的字段名称
     */
    private String preColumnName;
}
