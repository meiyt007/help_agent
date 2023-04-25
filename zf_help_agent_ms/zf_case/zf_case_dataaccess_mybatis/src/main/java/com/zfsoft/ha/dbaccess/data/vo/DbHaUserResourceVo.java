package com.zfsoft.ha.dbaccess.data.vo;

import lombok.Data;
import lombok.ToString;

import java.util.Date;

@Data
@ToString
public class DbHaUserResourceVo {
    private Long id;


    private Long workUserId;


    private String type;


    private String name;


    private Long parentId;

    private String resourceInfo;


    private String deleteStatus;


    private String createBy;


    private Date createDate;


    private String updateBy;


    private Date updateDate;

    private String workUserName;

    private String fastdfsUploadUrl;
}
