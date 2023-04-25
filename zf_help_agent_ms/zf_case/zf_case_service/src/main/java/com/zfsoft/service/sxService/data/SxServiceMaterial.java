package com.zfsoft.service.sxService.data;

import lombok.Data;
import lombok.ToString;

import java.util.Date;
import java.util.List;

/**
 * 实施清单-申请材料
 */
@Data
@ToString
public class SxServiceMaterial {
    /**
     * 主健
     */
    private Long id;

    /**
     * 业务主键
     */
    private String materialOid;

    /**
     * 所属事项
     */
    private String serviceOid;
    /**
     * 材料名称
     */
    private String materialName;

    /**
     * 材料类型
     */
    private Short materialType;

    /**
     * 材料形式
     */
    private Short materialFormat;

    /**
     * 材料样本名称
     */
    private String materialSampleName;

    /**
     * 材料样本路径
     */
    private String materialSampleAddr;

    /**
     * 电子表单名称
     */
    private String electronicFormName;

    /**
     * 电子表单路径
     */
    private String electronicFormAddr;

    /**
     * 材料空表路径
     */
    private String materialEmptyAddr;

    /**
     * 材料样表集合
     * add by WangKe 2022-06-29
     */
    private List<SxServiceMaterialSampleAtta> sxServiceMaterialSampleAttaList;

    /**
     * 材料电子表单集合
     * add by WangKe 2022-06-29
     */
    private List<SxServiceMaterialElectronicAtta> sxServiceMaterialElectronicAttaList;

    /**
     * 材料空表集合
     * add by WangKe 2022-06-29
     */
    private List<SxServiceMaterialEmptyAtta> sxServiceMaterialEmptyAttaList;

    /**
     * 来源渠道 0申请人自备 1政府部门核发 2其它
     */
    private Short materialSource;
    /**
     * 其他来源渠道
     */
    private String otherMaterialSource;

    /**
     * 材料类型名称
     */
    private String materialTypeName;

    /**
     * 纸质材料份数
     */
    private Long paperNumber;

    /**
     * 材料规格
     */
    private String materialSpecification;
    /**
     * 填报须知
     */
    private String makeNotice;
    /**
     * 受理标准
     */
    private String acceptStandard;

    /**
     * 是否需要电子材料(0否、1是)
     */
    private Short electronicFlag;

    /**
     * 是否引用电子证照  1:是  0:否
     */
    private Short ifElectronic;

    /**
     * 删除状态(0否、1是)
     */
    private Short delFlag;

    /**
     * 是否必须(0必要、1非必要 2容缺)
     */
    private Short mustFlag;

    /**
     * 创建时间
     */
    private Date createDate;

    /**
     * 创建人
     */
    private String createUser;

    /**
     * 常用样表次数
     */
    private Long commonNumber;
    /**
     * 材料依据
     */
    private String materialBasis;

    /**
     * 材料要求
     */
    private String materialRequirement;

    /**
     * 修改时间
     */
    private Date modifyDate;

    /**
     * 材料顺序
     */
    private Long materialSort;

    /**
     * 是否需要(0否、1是)
     */
    private Short reportFlag;

    /**
     * 表单编码
     */
    private String reportformCode;

    /**
     * 备注
     */
    private String remark;

    /**
     * 审查要点
     */
    private String examinePoint;

    /**
     * 审查要素底图路径
     */
    private String examinePointCardingAddr;

    /**
     * 材料目录主键
     */
    private String materialCatalogOid;

    /**
     * 所属事项名称
     */
    private String serviceName;


    /**
     * 百度自定义识别模板id，多个以英文半角逗号隔开
     */
    private String baiduTemplateIds;

    /**
     * 百度自定义识别模板id 用于一件事和一业一证查询时使用
     */
    private String comboBaiduTemplateIds;

    /**
     * 预览样表地址
     */
    private String materialSampleAddrYl;

    /**
     * 审核类型
     */
    private String  auditType;

    //细化材料列表
    private List<RefinedMaterial> refinedMaterialList;

    //2021.7.30  添加
    /**
     * 材料样本，原始文件名  ORIGIN_NAME
     */
    private String materialSampleOriginName;

    /**
     * 电子表单名称   原始文件名  ORIGIN_NAME
     */
    private String electronicFormOriginName;

    /**
     * 材料空表路径   原始文件名  ORIGIN_NAME
     */
    private String materialEmptyOriginName;


    /***
     * 根据细化材料样本生成的样本oid
     */
    private String  materialSampleOid;
    /***
     * 根据细化材料样本生成的样本地址
     */
    private String  materialSampleUrl;

    // 材料对应的表单模板名称
    private String templateName;

    //0：智能制作材料，1：免于提交材料，2：自备材料',
    private Short madeMaterialType;

    /**
     * 提示内容
     */
    private String memo;

    /**
     * 签名角色
     */
    private Short roleType;

    /**
     * 如果是多角色是否配置过
     */

    private String signFlag;

    /**
     * 原件数量,默认为1
     */
    private String nmOriginal;
    /**
     * 复印件数量,默认为1
     */
    private String nmCopy;
    /**
     * 取值范围 0为首次提交 2为补充材料
     */
    private String stuffStatus;
    /**
     * 是否是主题事项 0否 1是
     */
    private String isScene;
    /**
     * 万达材料名称
     */
    private String materialNameWd;
}
