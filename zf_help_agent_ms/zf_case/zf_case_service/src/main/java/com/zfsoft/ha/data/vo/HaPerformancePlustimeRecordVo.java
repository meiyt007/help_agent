package com.zfsoft.ha.data.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @Description 绩效加分项目时长记录表实体类
 * @author dingsn
 * @date 2022/11/01  16:30
 */
@ApiModel(description = "内层数据响应封装")
@Data
@ToString
public class HaPerformancePlustimeRecordVo {

    /** 主键 */
    @ApiModelProperty(value = "id", required = false)
    private Long id;

    /** 帮办人员id */
    @ApiModelProperty(value = "workUserOid", required = false)
    private Long workUserOid;

    /** 帮办人员姓名 */
    @ApiModelProperty(value = "workUserName", required = false)
    private String workUserName;

    /** 加分项目id */
    @ApiModelProperty(value = "plusProjectOid", required = false)
    private Long plusProjectOid;
    /*加分项目名称*/
    private String plusProjectName;

    /**
     * 加分时长;单位：分钟
     */
    @ApiModelProperty(value = "plusDuration", required = false)
    private Integer plusDuration;

    /**
     * 上传附件attaOid
     */
    @ApiModelProperty(value = "attaOid", required = false)
    private String attaOid;
    /**
     * 预览地址:  fastdfs上传url
     */
    @ApiModelProperty(value = "fastdfsUploadUrl", required = false)
    private String fastdfsUploadUrl;
    /**
     * 预览地址:  fastdfs上传url带有http
     */
    @ApiModelProperty(value = "fastdfsNginxUrl", required = false)
    private String fastdfsNginxUrl;

    /** 加分情况说明 */
    @ApiModelProperty(value = "bonusNotes", required = false)
    private String bonusNotes;

    /** 审核状态;0-待审核，1-审核通过，2-审核不通过 */
    @ApiModelProperty(value = "auditStatus", required = false)
    private String auditStatus;
    /**
     *审核原因理由
     */
    @ApiModelProperty(value = "auditReason", required = false)
    private String auditReason;

    /**
     *帮办人员组长id
     */
    @ApiModelProperty(value = "groupLeaderId", required = false)
    private Long groupLeaderId;

    /**
     *帮办人员组长名字
     */
    @ApiModelProperty(value = "groupLeaderName", required = false)
    private String groupLeaderName;

    /** 删除状态;1-未删除，2-已删除 */
    @ApiModelProperty(value = "deleteStatus", required = false)
    private String deleteStatus;

    /** 创建人 */
    @ApiModelProperty(value = "createBy", required = false)
    private String createBy;

    /** 创建时间 */
    @ApiModelProperty(value = "createDate", required = false)
    private Date createDate;

    /** 更新人 */
    @ApiModelProperty(value = "updateBy", required = false)
    private String updateBy;

    /** 更新时间 */
    @ApiModelProperty(value = "updateDate", required = false)
    private Date updateDate;
    /**
     * 组长审核时间
     */
    private Date auditDate;
    /** 分组职务;1-组长，2-副组长，3-组员 */
    private String groupPost;
    /**
     * 当前登录人id
     */
    private Long currentHaLoginUserId;
}
