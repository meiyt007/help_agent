package com.zfsoft.cases.data;

import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 办件信息表(QlCaseExt)实体类
 *
 * @author wangwg
 * @date  2020-10-22
 */
@Data
@ToString
public class QlCaseExt {
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
     * 办件主键
     */
    @NotNull(message = "办件主键不能为空",groups = {INSERT_GROUP.class})
    private String caseOid;
    /**
     * 投资项目编号
     */
    private String investProjectCode;
    /**
     * 投资项目名称
     */
    private String investProjecName;
    /**
     * 是否需要制证（否0、是1）
     */
    @NotNull(message = "是否需要制证不能为空",groups = {INSERT_GROUP.class})
    private Integer needLincese;
    /**
     * 所属单位
     */
    @NotNull(message = "所属单位不能为空",groups = {INSERT_GROUP.class})
    private String belongsystem;
    /**
     * 查询密码
     */
    @NotNull(message = "查询密码不能为空",groups = {INSERT_GROUP.class})
    private String projpwd;
    /**
     * 办件来源二级类型
     */
    @NotNull(message = "办件来源不能为空",groups = {INSERT_GROUP.class})
    private String sourceType;
    /**
     * 寄送日期
     */
    private Date sendDate;
    /**
     * 业务办理项编码
     */
    private String taskHandleItem;
    /**
     * 地方实施编码
     */
    private String localTaskCode;
    /**
     * 受理文书编号
     */
    private String acceptDocNo;
    /**
     * 办件摘要
     */
    private String projectAbstract;
    /**
     * 跨地区办理目标部门
     */
    private String targeOrgName;
    /**
     * 是否已经补齐补正（否0、是1）
     */
    private Integer isBqbz;
    @NotNull(message = "是否已经补齐补正不能为空",groups = {INSERT_GROUP.class})
    /**
     * 事项性质
     */
    private String serviceCharacter;
    /**
     * 送达方式
     */
    @NotNull(message = "送达方式不能为空",groups = {INSERT_GROUP.class})
    private String resultDeliveryWay;
    /**
     * 送达状态（否0、是1）
     */
    @NotNull(message = "送达状态不能为空",groups = {INSERT_GROUP.class})
    private Integer deliverFlag;

    /**
     * 是否是代理人（否0、是1）
     */
    @NotNull(message = "是否是代理人不能为空",groups = {INSERT_GROUP.class})
    private String proxyFlag;

    /**
     * 创建时间
     */
    private Date createDate;

    /**
     * 修改时间
     */
    @NotNull(message = "修改时间不能为空",groups = {QlCase.INSERT_GROUP.class})
    private Date modifyDate;

    // 纸质材料提交方式:1-邮寄，2-窗口提交
    private int paperSubmitType;
}