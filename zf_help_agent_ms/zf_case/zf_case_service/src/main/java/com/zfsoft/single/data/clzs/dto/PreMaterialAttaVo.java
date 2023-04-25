package com.zfsoft.single.data.clzs.dto;

import com.zfsoft.cases.data.QlCaseMaterialAtta;
import lombok.Data;

import java.util.List;

/**
 * 预审结果材料列表参数vo
 ** @author liangss
 *  * @date 2021年9月18日
 *  * @Copyright 版权由上海卓繁信息技术股份有限公司拥有
 *
 */

@Data
public class PreMaterialAttaVo {
    private String caseOid;
    private String serviceOid;
    private List<QlCaseMaterialAtta> qlCaseMaterialAttaList;

}
