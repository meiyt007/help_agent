package com.zfsoft.superwindow.data.clzs;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class CardCatalogue {
    //id
    private Long id;
    //业务主键
    private String oid;
    //目录编码
    private String cardCatalogueCode;
    //目录名称
    private String cardCatalogueName;
    //删除标识
    private Integer deleteFlag;
    //创建时间
    private Date createDate;
    //修改时间
    private Date modifyDate;

    //类型
    private String type;

    private List<CardCatalogueElement> subList;

}