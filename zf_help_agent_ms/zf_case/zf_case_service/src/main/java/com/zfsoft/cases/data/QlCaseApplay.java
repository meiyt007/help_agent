package com.zfsoft.cases.data;

import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 办件申请信息(QlCaseApplay)实体类
 *
 * @author wangwg
 * @date  2020-10-22
 */
@Data
@ToString
public class QlCaseApplay {
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
     * 申请数量
     */
    @NotNull(message = "申请数量不能为空",groups = {INSERT_GROUP.class})
    private Integer applyNumber;
    /**
     * 申请人名称
     */
    @NotNull(message = "申请人名称不能为空",groups = {INSERT_GROUP.class})
    private String applyUserName;
    /**
     * 申请人类型
     */
    @NotNull(message = "申请人类型不能为空",groups = {INSERT_GROUP.class})
    private String applyUserType;
    /**
     * 证件类型
     */
    @NotNull(message = "证件类型不能为空",groups = {INSERT_GROUP.class})
    private String credentialType;
    /**
     * 证件号码
     */
    @NotNull(message = "证件号码不能为空",groups = {INSERT_GROUP.class})
    private String credentialNumber;
    /**
     * 申请人地址
     */
    private String applyUserAddress;
    /**
     * 申请人电话
     */
    @NotNull(message = "申请人电话不能为空",groups = {INSERT_GROUP.class})
    private String applyUserTel;
    /**
     * 申请人手机
     */
    @NotNull(message = "申请人手机不能为空",groups = {INSERT_GROUP.class})
    private String applyUserPhone;
    /**
     * 邮政编码
     */
    private String applyPostCode;
    /**
     * 法定代表人
     */
    @NotNull(message = "法定代表人不能为空",groups = {INSERT_GROUP.class})
    private String legalPersonName;
    /**
     * 收件人姓名
     */
    @NotNull(message = "收件人姓名不能为空",groups = {INSERT_GROUP.class})
    private String addresseeName;
    /**
     * 收件人邮编
     */
    @NotNull(message = "收件人邮编不能为空",groups = {INSERT_GROUP.class})
    private String addresseePostCode;
    /**
     * 收件人手机
     */
    @NotNull(message = "收件人手机不能为空",groups = {INSERT_GROUP.class})
    private String addresseePhone;
    /**
     * 收件人电话
     */
    private String addresseeTel;
    /**
     * 收件人地址
     */
    @NotNull(message = "收件人地址不能为空",groups = {INSERT_GROUP.class})
    private String addresseeAddress;

    /**
     * 收件人地址选择
     */
    private String chooseAddress;
    /**
     * 收件人详细地址
     */
    @NotNull(message = "收件人详细地址不能为空",groups = {INSERT_GROUP.class})
    private String addresseeDetailAddress;

    /**
     * 联系人名称
     */
    @NotNull(message = "联系人名称不能为空",groups = {INSERT_GROUP.class})
    private String contactUserName;

    /**
     * 联系人身份证号码
     */
    @NotNull(message = "联系人身份证号码不能为空",groups = {INSERT_GROUP.class})
    private String contactCredentialNumber;

    /**
     * 联系人手机
     */
    @NotNull(message = "联系人手机不能为空",groups = {INSERT_GROUP.class})
    private String contactUserPhone;

    /**
     * 联系人电话
     */
    @NotNull(message = "联系人电话不能为空",groups = {INSERT_GROUP.class})
    private String contactUserTel;

    /**
     * 联系人邮件
     */
    private String contactEmail;
    /**
     * 联系人备注
     */
    private String contactRemark;
    /**
     * 所属业务管辖地
     */
    @NotNull(message = "所属业务管辖地人不能为空",groups = {INSERT_GROUP.class})
    private String bussVenueDistrictOid;
    /**
     * 受理具体地点
     */
    @NotNull(message = "受理具体地点不能为空",groups = {INSERT_GROUP.class})
    private String specificLocation;
    /**
     * 创建时间
     */
    private Date createDate;

    /**
     * 修改时间
     */
    @NotNull(message = "修改时间不能为空",groups = {QlCase.INSERT_GROUP.class})
    private Date modifyDate;

    /**
     *  申请数量限制
     */
    private String applyNumberLimit;

    /**
     *  互联网用户ID
     */
    private String netUserId;

}