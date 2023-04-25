package com.zfsoft.superwindow.data.clzs;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @author: liangss
 * @create: 2021-01-25
 * @description: 目录关联
 */
@Data
public class MaterialCatalogRelation {
    /* 主键  */
    @NotNull(message = "id不能为空")
    private Long id;

    /**
     * 业务关联oid
     */
    private String materialCatalogRelationOid;
    /**
     * 目录oid
     */
    private String materialCatalogOid;

    /**
     * 关联oid
     */
    private String relationOid;


    private Date createDate;


    private Date modifyDate;


    private Integer delFlag;


}