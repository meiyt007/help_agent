package com.zfsoft.single.data.clzs.dto;

import com.zfsoft.cases.data.QlCaseMaterial;
import com.zfsoft.single.data.clzs.ClassifyRec;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: liangss
 * @create: 2021-06-12
 * @description: 材料分类使用表
 */
@Data
public class ClassifyRecVo {

    //办件id
    private String caseOid;

    //附件id
    private String attaOid;

    //分类器id
    private String classifierId;

    private List<ClassifyRec> classifyRecList=new ArrayList<>();

    private List<QlCaseMaterial> caseMaterials=new ArrayList<>();

    private List<ClassifyRec> unClassifyRecList=new ArrayList<>();
}
