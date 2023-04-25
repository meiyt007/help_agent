package com.zfsoft.superwindow.data.clzs;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * @author: liangss
 * @create: 2020-11-03 15:40:29
 * @description: 材料目录
 */
@Data
public class MaterialCatalog {

    /* 主键  */
    @NotNull(message = "id不能为空")
    private Long id;

    /*  材料目录业务主键 */
    private String materialCatalogOid;

    /* 所属材料父级主键 */
    private String materialParentOid;

    /* 目录名称  */
    private String catalogName;
    /* 目录编码  */
    private String catalogCode;

    /* 子项名称  */
    private String subitemName;

    /* 材料类别oid */
    private String materialCategoryOid;

    /* 材料类别名称  */
    private String materialCategoryName;

    /* 宽度  */
    private String width;

    /* 高度	  */
    private String height;

    /* 存在内部矩形  */
    private String isInnerRectangle;

    /* 是否多页 */
    private String isMultiPage;

    /* 角类型 */
    private String angleType;

    /* 宽高类型 */
    private String widthHeightType;
    /* 印章数量 */
    private Integer sealsNumber;

    /* 百度模板id */
    private String baiduTemplateId;

    /* 材料识别类型oid  */
    private String materialIdentificationTypeOid;

    /* 材料识别类型  */
    private String materialIdentificationType;

    /*  备注 */
    private String note;

    /*  创建时间 */
    private Date createDate;

    /*  修改时间  */
    private Date modifyDate;

    /* 逻辑删除状态 0 未删除 1 删除  */
    private Integer delFlag;

    //子项列表
    private List<MaterialCatalog> subList;

    //状态
    private String stateDesc;

    //父级目录code
    private String bigCataCode;
    //父级目录name
    private String bigCataName;

    private FaModelTemplate template;

    private List<FaModelTemplateBlock> faModelTemplateBlockList;

    private List faModelRuleValidationList;

    private List isNotNull;

    private List<MaterialCatalogElement>  materialCatalogElementList;

    //卡证目录元素
    private String cardCatalogueOid;

    //textin模板id
    private String textinTemplateId;

    //卡证类型 0百度 1textIn
    private  String cardCatalogueType;


}
