package com.zfsoft.service.sxService.data;

import lombok.Data;
import lombok.ToString;

/**
 * 实施清单-申请材料
 * @author WangKe
 */
@Data
@ToString
public class SxServiceMaterialEmptyAtta {

    /**
     * 材料空表编号
     */
    private String materialEmptyOid;

    /**
     * 材料空表路径
     */
    private String materialEmptyAddr;

    /**
     * 材料空表名称
     */
    private String materialEmptyName;
}
