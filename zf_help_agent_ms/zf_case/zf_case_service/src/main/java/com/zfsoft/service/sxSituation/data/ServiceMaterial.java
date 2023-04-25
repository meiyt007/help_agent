package com.zfsoft.service.sxSituation.data;

import com.zfsoft.service.sxSys.data.SxSysAtta;
import lombok.Data;
import lombok.ToString;

/**
 * @author wangns
 * @description 事项颗粒材料
 * @date 2020/11/3 9:34
 * @copyright 上海卓繁信息技术股份有限公司版权所有
 */
@Data
@ToString
public class ServiceMaterial {

    /**
     * 主键
     */
    private Long id;

    /**
     * 业务主键
     */
    private String oid;

    /**
     * 事项基本信息表业务主健
     */
    private String serviceOid;

    /**
     * 原材料表业务主健
     */
    private String rawMaterialOid;

    /**
     * 选项值主键
     */
    private String optionOid;

    /**
     * 材料名称
     */
    private String materialName;

    /**
     * 材料类型 :1:申请表 2:证照 3:批文
     */
    private Short materialType;

    /**
     * 是否为必要材料 0:否 1:是
     */
    private Short needStatus;

    /**
     * 是否需要电子化  0:否 1:是
     */
    private Short needElectronicStatus;

    /**
     * 是否需要填写 1:是  0:否
     */
    private Short needFillStatus;

    /**
     * 是否为营业执照标识 1:是 0:否
     */
    private Short businessLicenseStatus;

    /**
     * 材料来源
     */
    private String materialSource;

    /**
     * 是否引用电子证照  1:是  0:否
     */
    private Short ifElectronic;

    /**
     * 纸质材料份数
     */
    private String paperNumber;

    /**
     * 材料样本路径
     */
    private String materialSampleAddr;

    /**
     * 材料空表路径
     */
    private String materialEmptyAddr;

    /**
     * 审查要点
     */
    private String examinePoint;

    /**
     * 是否删除(0否1是)
     */
    private Short deleteStatus;

    /**
     * 材料规格
     */
    private String materialSpecification;

    /**
     * 填报要求
     */
    private String fillRequirement;

    /**
     * 材料形式
     */
    private Short materialFormat;

    /**
     * 颗粒材料附件数据
     */
    private SxSysAtta sxSysAtta;

    /**
     * 百度自定义识别模板id，多个以英文半角逗号隔开
     */
    private String baiduTemplateIds;

    /**
     * 材料目录主键
     */
    private String materialCatalogOid;




    /**
     * 预览样表地址
     */
    private String materialSampleAddrYl;

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

    /**
     *  材料名称 - 万达
     */
    private String materialNameWd;
}
