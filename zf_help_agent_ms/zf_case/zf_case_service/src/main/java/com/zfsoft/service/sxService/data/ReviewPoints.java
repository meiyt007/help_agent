package com.zfsoft.service.sxService.data;

import lombok.Data;
import lombok.ToString;

import java.util.Date;

/**
 * 审查要点
 */
@Data
@ToString
public class ReviewPoints {
    //主键
    private Long id;
    //业务主键
    private String oid;
    //细化材料关联主键
    private String refinedMaterialOid;
    //审核要点
    private String reviewPoints;
     //序号
    private Long serialNumber;
    //创建时间
    private Date createDate;
    //修改时间
    private Date modifyDate;
    //是否删除(0否1是)
    private Short deleteStatus;


}
