package com.zfsoft.superwindow.service.easyquickcase.data;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * @description 签章流程记录
 * @author meiyt
 * @date 2022/5/25
 **/
@Data
public class SignatureFlowRecordSuper implements Serializable {

    /**
     * 新增验证规则组
     */
    public interface INSERT_GROUP{};

    /**
     * 更新验证规则组
     */
    public interface UPDATE_GROUP{};

    /**
     * 主键
     *
     * @mbg.generated
     */
    private Long id;

    /**
     * 业务主键
     *
     * @mbg.generated
     */
    private String oid;

    /**
     * 办件编号
     *
     * @mbg.generated
     */
    @NotNull(message = "办件编号不能为空",groups = {INSERT_GROUP.class})
    private String caseOid;

    /**
     * 材料主键
     *
     * @mbg.generated
     */
    @NotNull(message = "材料主键不能为空",groups = {INSERT_GROUP.class})
    private String materialOid;

    /**
     * 签章文件下载地址
     *
     * @mbg.generated
     */
    @NotNull(message = "文件下载地址不能为空",groups = {UPDATE_GROUP.class})
    private String downloadUrl;

    /**
     * 签章文件key
     *
     * @mbg.generated
     */
    private String fileKey;

    /**
     * 签章流程ID
     *
     * @mbg.generated
     */
    @NotNull(message = "流程ID不能为空",groups = {INSERT_GROUP.class})
    private String flowId;

    /**
     * 流程状态：0-未签署，1-已签署，2-签署中，3-拒签，4-过期作废
     *
     * @mbg.generated
     */
    @NotNull(message = "状态值不能为空",groups = {UPDATE_GROUP.class})
    private Integer status;

    /**
     * 是否删除
     *
     * @mbg.generated
     */
    private Integer delFlag;

    /**
     * 创建时间
     *
     * @mbg.generated
     */
    private Date createDate;

    /**
     * 修改时间
     *
     * @mbg.generated
     */
    private Date modifyDate;
}
