package com.zfsoft.single.data.clzs;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @author: liangss
 * @create: 2020-11-07 16:40:29
 * @description: 规则验证表
 */
@Data
public class FaModelRuleValidation {

    /* 主键  */
    @NotNull(message = "id不能为空")
    private Long id;
    /*  规则验证表主键 */
    private String faModelRuleValidationOid;
    //事项oid
    private String serviceOid;
    //套餐oid
    private String comboDirectoryOid;
    private String sxMaterialOid;
    //材料名称
    private String sxMaterialName;
    private String catalogOid;
    //目录名称
    private String catalogName;
    private String templateMetadataOid;
    private String templateMetadataCode;
    private String templateMetadataName;
    private String validateType;
    private String thanContent;
    private String contentDown;
    private String contentUp;
    private String contentDateFormat;
    private String contentDateValue;
    private String thanSxMaterialOid;
    private String thanCatalogOid;
    /*比对材料名称*/
    private String thanSxMaterialName;
    /*比对目录名称*/
    private String thanCatalogName;
    /*模板区块主键*/
    private String thanTemplateMetadataOid;
   /* 模板区块编号 */
    private String thanTemplateMetadataCode;
   /* 模板区块名称*/
    private String thanTemplateMetadataName;
    private String similar;
    private String memo;

    private Integer sort;

    /*  创建时间 */
    private Date createDate;

    /*  修改时间  */
    private Date modifyDate;

    /* 逻辑删除状态 0 未删除 1 删除  */
    private Integer delFlag;
}
