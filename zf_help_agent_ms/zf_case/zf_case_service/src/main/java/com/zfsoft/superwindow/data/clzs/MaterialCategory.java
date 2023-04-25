package com.zfsoft.superwindow.data.clzs;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @author: liangss
 * @create: 2020-11-03 10:40:29
 * @description: 材料类别
 */
@Data
public class MaterialCategory {

    /* 主键  */
    @NotNull(message = "id不能为空")
    private Long id;

    /*  材料类别业务主键 */
    private String materialCategoryOid;

    /*  类别名称 */
    private String categoryName;

    /*  类别编码 */
    private String categoryCode;

    /*  备注 */
    private String note;

    /*  创建时间 */
    private Integer sort;

    /*  创建时间 */
    private Date createDate;

    /*  修改时间  */
    private Date modifyDate;

    /* 逻辑删除状态 0 未删除 1 删除  */
    private Integer delFlag;
}
