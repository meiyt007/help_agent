package com.zfsoft.service.sxSituation.data;

import com.zfsoft.service.sxService.data.SxServiceMaterial;
import lombok.Data;
import lombok.ToString;

/**
 * @author wangns
 * @description 细化材料选项值关系表
 * @date 2020/11/2 17:05
 * @copyright 上海卓繁信息技术股份有限公司版权所有
 */
@Data
@ToString
public class SxServiceMateOptRel {

    /**
     * 主键
     */
    private Long id;

    /**
     * 业务主键
     */
    private String oid;

    /**
     * 细化材料业务主键
     */
    private String materialOid;

    /**
     * 选项值业务主键
     */
    private String optionOid;

    /**
     * 事项材料业务主键
     */
    private String sxMaterialOid;

    /**
     * 是否删除(0否1是)
     */
    private String deleteStatus;

    /**
     * 事项颗粒化材料
     */
    private ServiceMaterial serviceMaterial;

    /**
     * 事项材料
     */
    private SxServiceMaterial sxServiceMaterial;


}
