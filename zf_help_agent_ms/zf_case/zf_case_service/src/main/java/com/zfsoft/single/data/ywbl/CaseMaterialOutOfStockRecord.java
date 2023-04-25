package com.zfsoft.single.data.ywbl;
import lombok.*;

import java.util.Date;

/**
 * @author: liangss
 * @create: 2021-09-08 15:40:29
 * @description: 材料出库记录
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CaseMaterialOutOfStockRecord {

    private Long id;
    private String oid;
    private String regOid;
    private String outOid;
    private String receiverName;
    private String receiverPhone;
    private String idCard;
    private String outAttaOid;
    private String sdAttaOid;
    private String kdCompany;
    private String kdCode;
    private String senderUserName;
    private String senderUserPhone;
    private Integer outType;
    private Integer delFlag;
    private Date createDate;
    private Date modifyDate;

    private String operatorName;
    private String operatorPhone;

    private String materialsFlowType;//1 打印材料出库流转单 2 材料出库 3 材料签收



}
