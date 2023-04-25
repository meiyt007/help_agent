package com.zfsoft.ha.data.vo;

import lombok.Data;

import java.util.Date;

@Data
public class HaDeskDepVo {
    private Long id;

    private Long deskId;

    private Long groupId;

    private String groupName;

    private Long deskQueueId;

    private String confirmFlag;

    private Long conWorkUserId;

    private Date conDate;

    private String desc;

    private String deleteStatus;

    private String createBy;

    private Date createDate;

    private String updateBy;

    private Date updateDate;

    private HaDeskVo HaDeskVo;
}