package com.zfsoft.ha.data.responseData;

import lombok.Data;
import lombok.ToString;

/**
 * @Description //提供万行的，事项材料响应类
 * @Author: Wangyh
 * @Date: 2022/9/26 19:20
 */
@Data
@ToString
public class ServiceMaterialsRespVo {
    /**
     * 材料编号
     */
    private String materialOid;
    /**
     * 材料名称
     */
    private String materialName;
    /**
     * 材料类型，0-原件，1-复印件，2-原件和复印件
     */
    private Integer materialType;
    /**
     * 材料形式，1-纸质、2-电子版、3-证照、4-容缺补正、5-告知承诺
     */
    private Integer materialFormat;
    /**
     * 来源渠道：0-申请人自备、1-政府部门核发、2-其它
     */
    private Integer materialSource;
    /**
     * 来源渠道选择其他时展示 (不存在)
     */
    private String otherMaterialSource;
    /**
     *事项编号
     */
    private String materialServiceOid;
    /**
     * 纸质材料份数
     */
    private Long paperNumber;
    /**
     * 样本地址
     */
    private String materialSampleAddr;
    /**
     * 空表地址
     */
    private String materialEmptyAddr;
    /**
     * 预览空表地址
     */
    private String materialEmptyAddrYl;
    /**
     * 样本预览地址
     */
    private String materialSimpleAddrYl;
    /**
     * 审核类型
     */
    private String auditType;
    /**
     * 样表编号
     */
    private String materialSampleOid;
    /**
     * 根据细化材料样本生成的样本地址
     */
    private String materialSampleUrl;
    /**
     * 自制材料提交方式：0-智能制作材料，1-免于提交材料，2-自备材料',
     */
    private Short madeMaterialType;
    /**
     * 备注
     */
    private String remark;

}
