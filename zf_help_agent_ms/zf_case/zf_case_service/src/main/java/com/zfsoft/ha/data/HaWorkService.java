package com.zfsoft.ha.data;

import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 帮代办服务表
 * @author kangax
 * @version 1.0
 * @date 2022/7/20 上午10:45
 */
@Data
@ToString
public class HaWorkService {

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
     *队列表主键
     */
    private Long workQueueId;

    /**
     *服务类型，1-咨询，2-材料准备，3-收件，4-一键推送
     */
    private String serviceType;

    /**
     *服务内容，填写咨询过程
     */
    private String serviceMemo;

    /**
     *服务事项编号，材料准备或收件时，对应t_sx_service主键
     */
    private String sxServiceId;

    /**
     *办件编号，收件时，对应t_ql_case主键
     */
    private Long qlCaseId;


    /**
     * 办件编号（流水号）
     */
    private String caseNumber;

    /**
     *推送说明，一键推送时，说明内容
     */
    private String pushMemo;

    /**
     *创建人
     */
    private String createBy;

    /**
     *创建时间
     */
    private Date createDate;

    /**
     *更新人
     */
    private String updateBy;

    /**
     *更新时间
     */
    private Date updateDate;
    /**
     * 转派id
     */
    private Long turnRecordId;
    /**
     * 转派id
     */
    private String serviceStatus;

    /**
     * OPERATOR_NAME 经办人名称
     */
    private String operatorName;


    /**
     * OPERATOR_CARD_NO 经办人身份证
     */
    private String operatorCardNo;


    /**
     * OPERATOR_PHONE 经办人手机号
     */

    private String operatorPhone;

    /**
     * 推送时间
     */
    private Date pushDate;


}
