package com.zfsoft.single.data.clzs;


import com.zfsoft.cases.data.QlCaseMaterial;
import lombok.Data;



/**
 * @author: liangss
 * @create: 2021-06-17
 * @description: 材料分类查询表
 */
@Data
public class MaterialClassifyRecVo {
    private String oid;
    /**
     * 材料id
     */
    private String caseMaterialOid;

    private QlCaseMaterial qlCaseMaterial;



}
