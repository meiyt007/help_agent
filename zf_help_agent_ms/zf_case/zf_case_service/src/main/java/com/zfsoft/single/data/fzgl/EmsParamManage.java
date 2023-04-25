package com.zfsoft.single.data.fzgl;
import lombok.*;

import java.util.Date;

/**
 *  ems发送快递参数
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class EmsParamManage {

    private String oid;

    private String addresseeName;

    private String addresseePhone;

    private String addresseeDetailAddress;

    private String addresseeAddress;

    private String addresseePostCode;

    private String addresseeTel;

    private String sendePerson;

    private String senderMailCode;

    private String senderCall;

    private String senderPhone;

    private String senderAddress;

    private String senderDetailAddress;

    private String kdCode;

}
