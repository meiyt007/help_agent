package com.zfsoft.ha.data;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @Description 绩效加分项目时长记录表实体类
 * @author dingsn
 * @date 2022/11/01  14:30
 */
@Data
@ToString
public class HaPerformancePlustimeRecord {
    /**
     * 新增，验证规则组
     */
    public interface INSERT_GROUP{};

    /** 主键 */
    @NotNull(message = "id不能为空",groups = {HaPerformancePlustimeRecord.INSERT_GROUP.class})
    private Long id;

    /** 帮办人员id */
    private Long workUserOid;

    /** 帮办人员姓名 */
    private String workUserName;

    /** 加分项目id */
    private Long plusProjectOid;

    /**
     * 加分时长;单位：分钟
     */
    private Integer plusDuration;

    /**
     * 上传附件attaOid
     */
    private String attaOid;
    /**
     * 预览地址:  fastdfs上传url
     */
    private String fastdfsUploadUrl;
    /**
     * 预览地址:  fastdfs上传url带有http
     */
    private String fastdfsNginxUrl;

    /** 加分情况说明 */
    private String bonusNotes;

    /** 审核状态;0-待审核，1-审核通过，2-审核不通过 */
    private String auditStatus;
    /**
     *审核原因理由
     */
    private String auditReason;

    /**
     *帮办人员组长id
     */
    private Long groupLeaderId;

    /**
     *帮办人员组长名字
     */
    private String groupLeaderName;

    /** 删除状态;1-未删除，2-已删除 */
    private String deleteStatus;

    /** 创建人 */
    private String createBy;

    /** 创建时间 */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createDate;

    /** 更新人 */
    private String updateBy;

    /** 更新时间 */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateDate;

    /** 组长审核时间 */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date auditDate;
}
