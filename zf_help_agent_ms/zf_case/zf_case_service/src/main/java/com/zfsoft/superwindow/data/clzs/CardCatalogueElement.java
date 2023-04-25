package com.zfsoft.superwindow.data.clzs;

import lombok.Data;

import java.util.Date;
@Data
public class CardCatalogueElement {
    //id
    private Long id;
    //业务主键
    private String oid;
   //卡证目录主键
    private String cardCatalogueOid;
    //卡证目录编码
    private String cardCatalogueElementCode;
    //卡证目录名称
    private String cardCatalogueElementName;
    //删除标识
    private Integer deleteFlag;
    //创建时间
    private Date createDate;
    //修改时间
    private Date modifyDate;

}