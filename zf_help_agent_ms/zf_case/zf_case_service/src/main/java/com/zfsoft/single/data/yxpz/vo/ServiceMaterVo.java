package com.zfsoft.single.data.yxpz.vo;

import com.zfsoft.superwindow.data.clzs.MaterialCatalog;
import lombok.Data;

import java.util.List;

/**
 * @author: liangss
 * @create: 2020-11-07 16:40:29
 * @description: 规则验证表
 */
@Data
public class ServiceMaterVo {
    private Long id;
    private String materialOid;
    private String serviceOid;
    private String materialType;
    private String materialFormat;
    private String materialSampleName;
    private String electronicFormName;
    private String materialSource;
    private String materialTypeName;
    private String paperNumber;
    private String materialSpecification;
    private String  materialName;
    private String electronicFlag;
    private String delFlag;
    private String mustFlag;
    private String createDate;

    private String createUser;
    private String materialSort;
    private String remark;
    private String examinePoint;
    private String materialCatalogOid;


    private String oid;
    private String rawMaterialOid;
    private String optionOid;
    private Short needStatus;
    private Short needElectronicStatus;
    private Short needFillStatus;
    private Short businessLicenseStatus;
    private Short ifElectronic;
    private String materialSampleAddr;
    private String materialEmptyAddr;
    private Short deleteStatus;
    private String fillRequirement;


    private List<MaterialCatalog> materialCatalogList;
    private  MaterialCatalog  materialCatalog;
}
