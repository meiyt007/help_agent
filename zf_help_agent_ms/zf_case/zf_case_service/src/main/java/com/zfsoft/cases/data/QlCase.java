package com.zfsoft.cases.data;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 办件表(QlCase)实体类
 *
 * @author wangwg
 * @date  2020-10-22
 */
@Data
@ToString
public class QlCase {
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
    private String caseOid;
    /**
     * 所属事项
     */
    @NotNull(message = "所属不能为空",groups = {INSERT_GROUP.class, UPDATE_GROUP.class})
    private String serviceOid;
    /**
     * 办件编号
     */
    @NotNull(message = "办件编号不能为空",groups = {INSERT_GROUP.class})
    @Length(min = 1,max = 25,message = "办件编号长度为1-25",groups = {INSERT_GROUP.class, UPDATE_GROUP.class})
    private String caseNumber;
    /**
     * 所属机构
     */
    @NotNull(message = "所属机构不能为空",groups = {INSERT_GROUP.class})
    private String organOid;
    /**
     * 办件状态0暂存1待预审2办理中3办结，5或-1 异常办结，4已作废，7不予受理9预审不通过
     */
    @NotNull(message = "办件状态不能为空",groups = {INSERT_GROUP.class, UPDATE_GROUP.class})
    private Integer caseStatus;
    /**
     * 是否删除（否0、是1）
     */
    @NotNull(message = "是否删除不能为空",groups = {INSERT_GROUP.class, UPDATE_GROUP.class})
    private Integer delFlag;
    /**
     * 受理日期
     */
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
    private Date acceptanceDate;
    /**
     * 创建时间
     */
    @NotNull(message = "创建时间不能为空",groups = {INSERT_GROUP.class})
    private Date createDate;
    /**
     * 创建人主键
     */
    @NotNull(message = "创建人不能为空",groups = {INSERT_GROUP.class})
    private String createUserOid;
    /**
     * 办结时间
     */
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
    private Date concludeDate;
    /**
     * 办件类型
     */
    @NotNull(message = "办件类型不能为空",groups = {INSERT_GROUP.class})
    private String caseType;
    /**
     * 应办结时间
     */
    @NotNull(message = "应办结时间不能为空",groups = {INSERT_GROUP.class})
    private Date shouldConcludeDate;
    /**
     * 业务项名称/实施清单名称
     */
    @NotNull(message = "业务项名称/实施清单名称不能为空",groups = {INSERT_GROUP.class, UPDATE_GROUP.class})
    @Length(min = 1,max = 500,message = "业务项名称/实施清单名称长度为1-500",groups = {INSERT_GROUP.class, UPDATE_GROUP.class})
    private String serviceName;
    /**
     * 事项类型
     */
    @NotNull(message = "事项类型不能为空",groups = {INSERT_GROUP.class})
    private String serviceType;
    /**
     * 事项类型名称
     */
    @NotNull(message = "事项类型名称不能为空",groups = {INSERT_GROUP.class})
    private String serviceTypeName;
    /**
     * 项目名称
     */
    @NotNull(message = "项目名称不能为空",groups = {INSERT_GROUP.class})
    private String projectName;
    /**
     * 套餐办件编号
     */
    private String packageCaseOid;
    /**
     * 所属应用(1综窗、2跨省通办、5运行平台)
     */
    @NotNull(message = "所属应用不能为空",groups = {INSERT_GROUP.class})
    private Integer sourceApp;

    /**
     * 修改时间
     */
    @NotNull(message = "修改时间不能为空",groups = {INSERT_GROUP.class})
    private Date modifyDate;

    private  QlCaseApplay  applay;

    /**
     * 是否超时标识
     */
    private String overTime;
    /**
     * 容缺补正到期时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date rqbzDueDate;
    /**
     * 办件来源
     */
    private Integer sourceType;
    /**
     * 给平板评价使用的办件状态
     */
    private String pbCaseStatus;

    /**
     * 申请人类型
     */
    private String applyUserType;

    /**
     * 申请人名称
     */
    private String applyUserName;

    /**
     * 申请人证件号码
     */
    private String credentialNumber;

    /**
     * 申请人手机号
     */
    private String applyUserPhone;

    /**
     * 通讯地址
     */
    private String applyUserAddress;

    /**
     * 收件人详细地址
     */
    private String addresseeDetailAddress;

    /**
     * 证件类型
     */
    private String credentialType;

    /**
     * 排队号
     */
    private String queueNum;
    /**
     * 表单保存的ID
     */
    private String formOids;

    /**
     * 备注
     */
    private String remarks;

    /**
     * 终端主键
     */
    private String terminalOid;

    /**
     * 终端编号
     */
    private String terminalCode;

    // 是否已预填
    private int prepFill;

    /**
     * 材料类型 1:材料准备,2:收件
     */
    private String  materialType;
    /**
     *万达-发证方式：自取，物流，无结果物
     */
    private String  certWay;
    /**
     * 万达-配送公司，取值为EMS、顺丰速递、其他
     */
    private String  expressCompany;
}
