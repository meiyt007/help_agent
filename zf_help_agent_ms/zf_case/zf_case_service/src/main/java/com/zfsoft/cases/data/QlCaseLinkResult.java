package com.zfsoft.cases.data;

import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 办件审批环节信息(QlCaseLinkResult)实体类
 *
 * @author wangwg
 * @date  2020-10-22
 */
@Data
@ToString
public class QlCaseLinkResult {

    /**
     * 新增，验证规则组
     */
    public interface INSERT_GROUP{};

    /**
     * 更新，验证规则组
     */
    public interface UPDATE_GROUP{};

    /**
     * 主键
     */
    @NotNull(message = "id不能为空",groups = {INSERT_GROUP.class})
    private Long id;
    /**
     * 业务主键
     */
    @NotNull(message = "业务主键不能为空",groups = {INSERT_GROUP.class})
    private String linkResultOid;
    /**
     * 办件主键
     */
    @NotNull(message = "办件主键不能为空",groups = {INSERT_GROUP.class})
    private String caseOid;
    /**
     * 办理环节操作人主键
     */
    @NotNull(message = "办理环节操作人主键不能为空",groups = {INSERT_GROUP.class})
    private String personOid;
    /**
     * 办理环节操作人姓名
     */
    @NotNull(message = "办理环节操作人姓名不能为空",groups = {INSERT_GROUP.class})
    private String personName;
    /**
     * 办理环节编码
     */
    @NotNull(message = "办理环节编码不能为空",groups = {INSERT_GROUP.class})
    private String linkCode;
    /**
     * 办理环节名称
     */
    @NotNull(message = "办理环节名称不能为空",groups = {INSERT_GROUP.class})
    private String linkName;
    /**
     * 是否审批通过（否0、是1）
     */
    @NotNull(message = "是否审批通过不能为空",groups = {INSERT_GROUP.class})
    private Integer finalStatus;
    /**
     * 意见
     */
    @NotNull(message = "意见不能为空",groups = {INSERT_GROUP.class})
    private String finalOpinion;
    /**
     * 意见说明
     */
    @NotNull(message = "意见说明为空",groups = {INSERT_GROUP.class})
    private String finalOpinionDesc;
    /**
     * 审批完成时间
     */
    @NotNull(message = "审批完成时间为空",groups = {INSERT_GROUP.class})
    private Date finalDate;
    /**
     * 创建时间
     */
    @NotNull(message = "创建时间为空",groups = {INSERT_GROUP.class})
    private Date createDate;

    /**
     * 修改时间
     */
    @NotNull(message = "修改时间不能为空",groups = {QlCase.INSERT_GROUP.class})
    private Date modifyDate;

    private String attaOid;

}