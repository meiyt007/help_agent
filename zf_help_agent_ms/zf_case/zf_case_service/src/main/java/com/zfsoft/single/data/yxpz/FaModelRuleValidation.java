package com.zfsoft.single.data.yxpz;

import com.zfsoft.service.sxService.data.ReviewPoints;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

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
    //事项oid
    private String sxMaterialOid;
    //材料名称
    private String sxMaterialName;
    //目录id
    private String catalogOid;
    //目录名称
    private String catalogName;
    //目录元素oid
    private String templateMetadataOid;
    //目录元素code
    private String templateMetadataCode;
    //目录元素名称
    private String templateMetadataName;
    private String validateType;
    private String thanContent;
    private String contentDown;
    private String contentUp;
    private String contentDateFormat;
    private String contentDateValue;
    /*比对材料oid   */
    private String thanSxMaterialOid;
    /*比对目录id  /表单标签oid */
    private String thanCatalogOid;
    /*比对材料名称   */
    private String thanSxMaterialName;
    /*比对目录名称*/
    private String thanCatalogName;
    /*比对目录元素oid  表单字段oid */
    private String thanTemplateMetadataOid;
   /* 比对目录元素code   */
    private String thanTemplateMetadataCode;
   /* 比对目录元素名称   */
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


    /**
     * 精细化材料oid
     */
    private String refinedMaterialOid;
    /**
     * 精细化材料名称
     */
    private String refinedMaterialName;

    private String auditContent;

    //比对精细化材料oid   /表单分类oid
    private String thanRefinedMaterialOid;
    //比对精细化材料名称   /表单分类名称
    private String thanRefinedMaterialName;



    //小于时间
    private String contentBeforeDateValue;
   //大于时间
    private String contentAfterDateValue;
    //距离今天时间小于天数
    private String biforeNowDayValue;

    //距离今天时间大于天数
    private String afterNowDayValue;

    private List<ReviewPoints> reviewPointsList;


    //不超过年龄
    private String noMoreThanAge;

    //不小于年龄
    private String noLessThanAge;


    private String auditPointStatus;

    private String reviewPointOid;

    //规则类型  DZBD-电子表单
    private  String ruleType;


    private String[] elementLabelTree;
    private String[] typeLabelTree;
    private String[] licenseTree;
    private String basicForms;
}
