package com.zfsoft.ha.data;

import lombok.Data;
import lombok.ToString;

import java.util.Date;
/**
 * @Description 加分时长催办发送短信记录实体类
 * @author dingsn
 * @date 2022/10/27  14:59
 */
@Data
@ToString
public class HaAkeyReviewRecords {
    private Long id;

    private Long groupLeaderId;

    private String groupLeaderName;

    private String phone;

    private String message;

    private Date akeyReviewDate;

    private String deleteStatus;

    private String createBy;

    private Date createDate;

    private String updateBy;

    private Date updateDate;
}
