package com.zfsoft.single.data.yxpz;

import lombok.Data;
import lombok.ToString;


/**
 * @author wangns
 * @description 事项表单关系表
 * @date 2020/11/6 16:37
 * @copyright 上海卓繁信息技术股份有限公司版权所有
 */
@Data
@ToString
public class SxSerForm {

    /* 主键  */
    private Long id;

    /* 业务主健  */
    private String oid;

    /*  所属事项 */
    private String serviceOid;
    /*所属事项名称*/
    private String serviceName;

    /* 授权id  */
    private String authorizeKey;

    /* 表单模板id  */
    private String designOid;

    private String formMainOid;

    /* 表单编码  */
    private String formCode;

    /* 表单名称  */
    private String formName;

    /* 表单说明  */
    private String formText;

    /* 表单地址  */
    private String formAddr;
    /*  表单来源（1事项平台，0综窗） */
    private Short formSource;

    /* 是否必填(0必填、1非必填、2容缺后补)  */
    private Short isFormFlag;

    /* 表单启用/禁用（1启用，0禁用） */
    private Short formUseStatus;
    private String formUseStatusName;

    /* 是否删除*/
    private Short delFlag;

    /*表单类型 （1电子表单，0自定义表单）*/
    private Short formType;
    private String formTypeName;

    /* 排序号*/
    private Long sort;

    /*套餐目录表主键*/
    private String comboDirectoryOid;
    /**
     * 表单样本
     */
    private String simpleAtta;

    private String attaName;

}
