package com.zfsoft.service.sxService.data;

import lombok.Data;
import lombok.ToString;

/**
 * 实施清单-申请材料
 * @author WangKe
 */
@Data
@ToString
public class SxServiceMaterialElectronicAtta {

    /**
     * 电子表单编号
     */
    private String electronicFormOid;

    /**
     * 电子表单名称
     */
    private String electronicFormName;

    /**
     * 电子表单路径
     */
    private String electronicFormAddr;
}
