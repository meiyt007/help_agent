package com.zfsoft.service.dto;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import lombok.Data;
import lombok.ToString;

/**
 * 要素内容详情 内容层级标题dto
 * @author : yuy
 * @date :  2021/1/25
 */
@Data
@ToString
public class FactorConDto {

    //层级还是内容 层级:1 内容2
    private String type;

    //层级/内容 oid
    private String oid;

    //层级名称/内容titel
    private String name;

    //层级上级内容OID/内容上级层级OId
    private String preOid;

    private String amount;//层级设置允许多少条数据

    //隐藏 Y  显示N
    private String isNone;//是否隐藏(如果层级只允许添加一条数据,并且已有数据,则为隐藏)

    private String lableOid;

    private String preConOid;//数据为内容时，存上级内容OID

    //层级等级
    private String level;

    private String tableStatus;
    //是否免交 Y 免交 N不免交
    private String isMianJiao;

    /**
     * 层级下所有要素内容
     */
    private JSONObject contents;

    /**
     * 层级下所有要素内容数组
     */
    private JSONArray contentsArray;

    public FactorConDto(){}

    public FactorConDto(String type, String oid, String name, String preOid, String amount, String level, String tableStatus, String isMianJiao){
        this.type = type;
        this.oid = oid;
        this.name = name;
        this.preOid = preOid;
        this.amount = amount;
        this.level = level;
        this.isNone = "N";
        this.isMianJiao=isMianJiao;
        if(tableStatus == null || tableStatus == ""){
            this.tableStatus = "N";
        }else{
            this.tableStatus = tableStatus;
        }
    }

    public FactorConDto(String type, String oid, String name, String preOid, String amount, String level, String tableStatus, String isMianJiao, JSONObject contents){
        this.type = type;
        this.oid = oid;
        this.name = name;
        this.preOid = preOid;
        this.amount = amount;
        this.level = level;
        this.isNone = "N";
        this.isMianJiao=isMianJiao;
        if(tableStatus == null || tableStatus == ""){
            this.tableStatus = "N";
        }else{
            this.tableStatus = tableStatus;
        }
        this.contents = contents;
    }

    public FactorConDto(String type, String oid, String name, String preOid, String amount,
                        String level, String tableStatus, String isMianJiao, JSONObject contents, JSONArray contentsArray){
        this.type = type;
        this.oid = oid;
        this.name = name;
        this.preOid = preOid;
        this.amount = amount;
        this.level = level;
        this.isNone = "N";
        this.isMianJiao=isMianJiao;
        if(tableStatus == null || tableStatus == ""){
            this.tableStatus = "N";
        }else{
            this.tableStatus = tableStatus;
        }
        this.contents = contents;
        this.contentsArray = contentsArray;
    }

    public FactorConDto(String type, String oid, String name, String preOid, String amount, String level){
        this.type = type;
        this.oid = oid;
        this.name = name;
        this.preOid = preOid;
        this.amount = amount;
        this.level = level;
        this.isNone = "N";
    }

}
