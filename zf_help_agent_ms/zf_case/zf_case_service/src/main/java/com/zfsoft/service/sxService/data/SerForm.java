package com.zfsoft.service.sxService.data;

import lombok.Data;
import lombok.ToString;

/**
 * 实施清单 - 表单配置
 */
@Data
@ToString
public class SerForm {
    /**
     * 主键
     */
    private Long id;

    /**
     * 业务主键
     */
    private String oid;

    /**
     * 所属事项
     */
    private String serviceOid;

    /**
     * 表单编码
     */
    private String formCode;

    /**
     * 表单名称
     */
    private String formName;

    /**
     * 表单说明
     */
    private String formText;

    /**
     * 排序号
     */
    private Long sort;

    /**
     * 删除状态(0否、1是)
     */
    private Short delFlag;

    /**
     * 是否必填(0必填、1非必填、2容缺后补)
     */
    private Short isFormFlag;
}