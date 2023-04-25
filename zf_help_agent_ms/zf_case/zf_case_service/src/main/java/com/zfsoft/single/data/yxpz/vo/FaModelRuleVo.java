package com.zfsoft.single.data.yxpz.vo;
import lombok.Data;

import java.util.List;

/**
 * @author: liangss
 * @create: 2020-11-07 16:40:29
 * @description: 事项验证套餐验证
 */
@Data
public class FaModelRuleVo {
    private Long id;
    private String serviceOid;
    private String districtOid;
    private String districtName;
    private String organOid;
    private String organName;
    private String implementOrganProperty;
    private String implementOrganPropertyName;
    private String basicCode;
    private String implementCode;
    private String serviceName;

    private List<ServiceMaterVo> sxServiceMaterials;
}
