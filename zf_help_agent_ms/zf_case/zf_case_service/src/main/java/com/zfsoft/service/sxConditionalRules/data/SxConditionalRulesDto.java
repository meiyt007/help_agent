package com.zfsoft.service.sxConditionalRules.data;

import lombok.Data;

import java.util.List;

@Data
public class SxConditionalRulesDto {

    // 条件预检集合
    private List<SxConditionalRules> tableData;

    // 秒批通过
    private String mpPass;

    // 秒批不通过
    private String mpNoPass;
}
