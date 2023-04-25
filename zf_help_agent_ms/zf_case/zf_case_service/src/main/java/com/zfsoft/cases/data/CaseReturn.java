package com.zfsoft.cases.data;

import lombok.Data;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 办件退件表(CaseReturn)实体类
 * @author liangss
 * @date  2020-10-22
 */
@Data
@ToString
public class CaseReturn {
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
    private String returnOid;

    /**
     * 办件主键
     */
    private String caseOid;
    /**
     * 退件状态
     */
    private String returnStatus;
    /**
     * 告知状态
     */
    private String informStatus;
    /**
     * 是否发送短信
     */
    private String isCms;
    /**
     * 创建时间
     */
    private Date createDate;
    /**
     * 修改时间
     */
    private Date modifyDate;
    /**
     * 创建人
     */
    private String createUser;

    private String returnRemark;

    private String informRemark;

    private String informCms;

    private String pickerCardType;

    private String receiveCardCode;

    private String receiveName;

    private String receivePhone;

    private String imgInfo;

    private String signInfo;

    private String materialInfo;

    private String returnStep;
    /**
     * 是否显示签名
     */
    private String displaySign;

    private Integer delFlag;

    //申请人手机
    private String applyPhone;

    /**
     * 办件编号
     */
    private String regNumber;

}