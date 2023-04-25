package com.zfsoft.service.sxService.data;

import lombok.Data;

import java.util.Map;

@Data
public class ZcFormInfo extends SxFormInfo {

    private String caseOid;

    private String formNames;

    private Map<String,Object> formVals;

}
