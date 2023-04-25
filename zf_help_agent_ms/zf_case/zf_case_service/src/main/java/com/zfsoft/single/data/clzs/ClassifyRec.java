package com.zfsoft.single.data.clzs;


import com.zfsoft.cases.data.QlCaseMaterial;
import com.zfsoft.cases.data.QlSysAtta;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Map;

/**
 * @author: liangss
 * @create: 2021-06-16
 * @description: 材料分类表
 */
@Data
public class ClassifyRec {

    /* 主键  */
    @NotNull(message = "id不能为空")
    private Long id;

    private String oid;

    /**
     * 材料id
     */
    private String caseMaterialOid;


    private String caseFileAttaRecOid;



    //办件id
    private String caseOid;

    //附件id
    private String attaOid;

    //分类器id
    private String classifierId;

    //分类状态 Y：已有分类   N：未能分类',
    private String classifierStatus;


    private Short deleteStatus;

    /*  创建时间 */
    private Date createDate;

    /*  修改时间  */
    private Date modifyDate;

    /* 逻辑删除状态 0 未删除 1 删除  */
    private Integer delFlag;

    private QlSysAtta qlSysAtta;


    //材料oid
    private String materialOid;

    /**
     * 材料目录主键
     */
    private String materialCatalogOid;

    /**
     * 百度自定义识别模板id，多个以英文半角逗号隔开
     */
    private String baiduTemplateIds;

    //精细化材料oid
    private String refinedMaterialOid;


    private String baseValue;

    private String serviceOid;
    private String serviceName;

    private String beforeAttaOid;
    private String fastdfsNginxUrl;
    private  Map<String, QlCaseMaterial> qlCaseMaterialMap;
    private  Map<String,Map<String ,String>> fileListMap;
    private String fileList;


}
