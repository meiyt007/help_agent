package com.zfsoft.superwindow.data.clzs;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @author: liangss
 * @create: 2020-11-03 15:40:29
 * @description: 材料目录元素
 */
@Data
public class MaterialCatalogElement {

    /* 主键  */
    @NotNull(message = "id不能为空")
    private Long id;

    /*  材料目录元素业务主键 */
    private String materialCatalogElementOid;

    /* 关联目录主键 */
    private String materialCatalogOid;

    /* 关联目录父级主键 */
    private String materialCatalogParentOid;

    /* 关联目录名称*/
    private String materialCatalogName;

    /* 元素编码  */
    private String elementCode;

    /* 元素名称 */
    private String elementName;

    /*  创建时间 */
    private Date createDate;

    /*  修改时间  */
    private Date modifyDate;

    /* 逻辑删除状态 0 未删除 1 删除  */
    private Integer delFlag;
   /* 目录元素oid*/
    private String cardCatalogueElementOid;
    /*目录元素编码*/
    private String cardCatalogueElementCode;
   /* 目录元素名称*/
    private String cardCatalogueElementName;
}
