package com.zfsoft.single.data.clzs;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @author: liangss
 * @create: 2020-12-14 14:40:29
 * @description: 样表信息
 */
@Data
public class StampRecord {

    /* 主键  */
    @NotNull(message = "id不能为空")
    private Long id;

    private String ahsStampRecordOid;
    private String attaOid;

   /* 办件主键*/
    private String caseOid;

   /* 办件记录主键*/
    private String caseFileRecOid;

  /*  所属目录子项主键*/
    private String cataOid;

    /** 印章数量   */
    private Integer amount;

    /* 是否验证通过 0-通过 1-未通过*/
    private Integer passFlag;


    /*  创建时间 */
    private Date createDate;

    /*  修改时间  */
    private Date modifyDate;

    /* 逻辑删除状态 0 未删除 1 删除  */
    private Integer delFlag;



}
