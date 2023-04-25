package com.zfsoft.service.sxService.data;

import lombok.Data;
import lombok.ToString;

import java.util.Date;

@Data
@ToString
public class SxOptionField {

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
     * 选项标题主键
     *
     * @mbg.generated
     */
    private String titleOid;

    /**
     * 选项值主键
     *
     * @mbg.generated
     */
    private String valOid;

    /**
     * 字段主键
     *
     * @mbg.generated
     */
    private String fieldOid;

    /**
     * 事项主键
     *
     * @mbg.generated
     */
    private String serviceOid;

    /**
     * 是否删除，0-未删除，1-已删除
     *
     * @mbg.generated
     */
    private Integer delFlag;

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

    /**
     * 类型oid
     *
     * @mbg.generated
     */
    private String fieldTypeOid;

    private String fieldOids;
}
