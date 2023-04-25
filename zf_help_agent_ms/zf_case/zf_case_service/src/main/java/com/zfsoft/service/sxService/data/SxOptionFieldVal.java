package com.zfsoft.service.sxService.data;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class SxOptionFieldVal {

    /**
     * 主键
     *
     * @mbg.generated
     */
    private Long id;

    /**
     * 业务主键
     *
     * @mbg.generated
     */
    private String oid;

    /**
     * 事项主键
     *
     * @mbg.generated
     */
    private String serviceOid;

    /**
     * 表单字段主键
     *
     * @mbg.generated
     */
    private String fieldOid;

    /**
     * 字段类型
     *
     * @mbg.generated
     */
    private String fieldTypeOid;

    /**
     * 字段填充值
     *
     * @mbg.generated
     */
    private String valInfo;

    /**
     * 选项主键
     *
     * @mbg.generated
     */
    private String valOids;

    /**
     * 选项标签主键
     *
     * @mbg.generated
     */
    private String labelOid;

    /**
     * 是否删除
     *
     * @mbg.generated
     */
    private Integer deleteFlag;

    /**
     * 创建时间
     *
     * @mbg.generated
     */
    private Date createDate;

    /**
     * 修改时间
     *
     * @mbg.generated
     */
    private Date modifyDate;

    // 标签类型
    private String controlType;

    // 情形名称
    private List<String> optionNames;

    // 字段名称
    private String fieldName;
}
