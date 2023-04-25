package com.zfsoft.service.sxSituation.data.vo;

import lombok.Data;
import lombok.ToString;
/**
 * @author wangwg
 * @description 事项材料和精细化材料统一临时表
 * @date 2020/11/27
 * @copyright 上海卓繁信息技术股份有限公司版权所有
 */
@Data
@ToString
public class ServiceMaterialVo {

    //事项材料id
    private String materialOid;
    private String materialName;
    private Integer materialType;
    private Integer materialMustFlag;
    private Integer materialSource;
    private String materialSimpleAddr;
    private String materialServiceOid;
    private Integer materialFormat;
    private Long paperNumber;
    private Integer materialFlag;

    /**
     * 材料目录主键
     */
    private String materialCatalogOid;

    /**
     * 百度自定义识别模板id，多个以英文半角逗号隔开
     */
    private String baiduTemplateIds;

    private String materialSampleAddr;

    /**
     * 空表attaOid
     */
    private String materialEmptyAddr;
    /**
     * 预览空表地址
     */
    private String materialEmptyAddrYl;
    /**
     * 预览样表地址
     */
    private String materialSimpleAddrYl;


    /**
     * 审核类型
     */
    private String  auditType;


    /***
     * 根据细化材料样本生成的样本oid
     */
    private String  materialSampleOid;
    /***
     * 根据细化材料样本生成的样本地址
     */
    private String  materialSampleUrl;

    //自制材料类型
    private Short madeMaterialType;

    // 是否有审查元素 0-无 1-有
    private Integer reviewPointsFlag;

    //备注
    private String remark;

    /**
     * 来源渠道选择其他时展示 (不存在)
     */
    private String otherMaterialSource;
    /**
     * 材料顺序
     */
    private Long materialSort;

    /**
     * 空表附件名称
     */
    private String emptyOriginName;
    /**
     * 样表
     */
    private String simpleOriginName;
}
