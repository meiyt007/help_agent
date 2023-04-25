package com.zfsoft.service.sxService.data;

import lombok.Data;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * 细化材料
 */
@Data
@ToString
public class RefinedMaterial {

    //主键
    private Long id;
    //业务主键
    private String oid;
    //事项材料业务主健
    private String materialOid;
    //细化材料名称
    private String refinedMaterialName;
    //是否为必要材料 0:否 1:是
    private Short needStatus;
    //材料来源
    private String materialSource;
    //审查要点业务主健
    private String reviewPointsOid;
    //材料类型 :1:图片 2:其他
    private Short materialType;
    //材料样本
    private String materialSampleOid;
    //材料样本路径
    private String materialSampleAddr;
    //序号（必填、不能重复
    private Long serialNumber;
    //百度模板id
    private String baiduTemplateIds;
    //目录id
    private String materialCatalogOid;
    //创建时间
    private Date createDate;
    //修改时间
    private Date modifyDate;
    //是否删除(0否1是)
    private Short deleteStatus;

    //材料名称
    private String materialName;

    private String serviceOid;

    //证照类型oid
    private String licenceOid;
    //证照类型名称
    private String licenceName;


    //材料和附件关联oid
    private List qlCaseMaterialAttaList=new ArrayList<>();

    private  List<ReviewPoints>  reviewPointsList;

    /**
     *审核结果状态
     */
    private String  resultStatus;

    private String materialSampleOriginName;

    //一件事目录oid
    private String comboDirectoryOid;


    private List materialCatalogList;


}
