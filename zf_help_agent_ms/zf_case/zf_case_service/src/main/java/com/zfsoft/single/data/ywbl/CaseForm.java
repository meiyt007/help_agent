package com.zfsoft.single.data.ywbl;

import lombok.*;

import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CaseForm {

    private Long id;


    private String oid;

    private String regOid;


    private String formDataId;


    private Integer sort;

    private String delFlag;

    private Date modifyDate;

    private String serFormOid;

    private String comboDireOid;
    /**
     * 一件事保存多个表单使用
     */
    private List<CaseForm> comboCaseFormList;

}