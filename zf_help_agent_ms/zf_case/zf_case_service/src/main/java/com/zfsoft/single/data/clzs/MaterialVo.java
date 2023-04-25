package com.zfsoft.single.data.clzs;

import lombok.*;

/**
 * @author Administrator
 * 办件材料辅助类
 * @date 2020/11/20
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MaterialVo {
    private Long id;
    private String materialOid;
    private String serviceOid;
    private String materialCatalogOid;
    private String serviceName;
    private String materialName;

}
