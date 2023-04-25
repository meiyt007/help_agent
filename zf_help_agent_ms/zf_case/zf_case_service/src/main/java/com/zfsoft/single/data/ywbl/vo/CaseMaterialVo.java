package com.zfsoft.single.data.ywbl.vo;

import com.zfsoft.cases.data.QlCaseMaterial;
import lombok.*;

import java.util.List;

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
public class CaseMaterialVo {
    private Long id;
    private String caseOid;
    private List materialAtta;//材料附件信息
    private List materialCollect;//材料采集类型
    private List elemLicense;//电子证照
    private String memo;
    private String patchOpinion;
    private String patchResult;
    private String elecBillOid;//证照配置oid
    private QlCaseMaterial qlCaseMaterial;
}
