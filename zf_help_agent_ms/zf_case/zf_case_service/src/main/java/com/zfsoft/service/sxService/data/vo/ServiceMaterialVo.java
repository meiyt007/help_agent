package com.zfsoft.service.sxService.data.vo;

import lombok.Data;
import lombok.ToString;

/*
 * @Description:实施清单-申请材料
 * @Author: wangxl
 * @Date: 2021/4/1
 **/
@Data
@ToString
public class ServiceMaterialVo {

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
    private String valOid;

    /**
     * 材料样本名称
     */
//    private String materialSampleName;

    /**
     * 材料样本路径
     */
//    private String materialSampleAddr;

    /**
     * 电子表单名称
     */
//    private String electronicFormName;

    /**
     * 电子表单路径
     */
//    private String electronicFormAddr;

    /**
     * 材料空表路径
     */
//    private String materialEmptyAddr;

    /**
     * 来源渠道 0申请人自备 1政府部门核发 2其它
     */
    private Short materialSource;
    /**
     * 其他来源渠道
     */
//    private String otherMaterialSource;

    /**
     * 材料类型名称
     */
//    private String materialTypeName;

    /**
     * 纸质材料份数
     */
    private Long paperNumber;

    /**
     * 材料规格
     */
//    private String materialSpecification;
    /**
     * 填报须知
     */
//    private String makeNotice;
    /**
     * 受理标准
     */
//    private String acceptStandard;

    /**
     * 是否需要电子材料(0否、1是)
     */
//    private Short electronicFlag;

    /**
     * 删除状态(0否、1是)
     */
//    private Short delFlag;

    /**
     * 是否必须(0否、1是)
     */
    private Short mustFlag;

    /**
     * 创建时间
     */
//    private Date createDate;

    /**
     * 创建人
     */
//    private String createUser;

    /**
     * 常用样表次数
     */
//    private Long commonNumber;
    /**
     * 材料依据
     */
//    private String materialBasis;

    /**
     * 材料要求
     */
//    private String materialRequirement;

    /**
     * 修改时间
     */
//    private Date modifyDate;

    /**
     * 材料顺序
     */
//    private Long materialSort;

    /**
     * 是否需要(0否、1是)
     */
//    private Short reportFlag;

    /**
     * 表单编码
     */
//    private String reportformCode;

    /**
     * 备注
     */
//    private String remark;

    /**
     * 审查要点
     */
//    private String examinePoint;

    /**
     * 审查要素底图路径
     */
//    private String examinePointCardingAddr;

    /**
     * 材料目录主键
     */
//    private String materialCatalogOid;

    /**
     * 所属事项名称
     */
    private String serviceName;

}
