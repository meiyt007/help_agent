package com.zfsoft.single.data.formTemplate;

import lombok.*;

@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TermlateDataDto {

    // 模板附件主键
    private String attaOid;

    // 模板附件网页地址
    private String attaUrl;

    // 模板附件名称
    private String templateName;

    // 模板盖章状态0未盖章1已盖章
    private Integer sealStatus;

    // 材料主键
    private String materialOid;

    // 材料名称
    private String materialName;
}
