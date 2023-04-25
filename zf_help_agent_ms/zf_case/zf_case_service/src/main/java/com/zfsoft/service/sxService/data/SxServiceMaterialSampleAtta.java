package com.zfsoft.service.sxService.data;

import lombok.Data;
import lombok.ToString;

/**
 * 实施清单-申请材料
 * @author WangKe
 */
@Data
@ToString
public class SxServiceMaterialSampleAtta {

    /**
     * 材料样本编号
     */
    private String materialSampleOid;

    /**
     * 材料样本名称
     */
    private String materialSampleName;

    /**
     * 材料样本路径
     */
    private String materialSampleAddr;
}
