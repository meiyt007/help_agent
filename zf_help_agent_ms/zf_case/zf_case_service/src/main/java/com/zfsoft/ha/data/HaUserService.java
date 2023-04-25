package com.zfsoft.ha.data;

import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 用户事项权限表
 * @author zhaobf
 * @version 1.0
 * @date 2022/8/5 10:55
 */
@Data
@ToString
public class HaUserService {
    /**
     * 新增，验证规则组
     */
    public interface INSERT_GROUP{};

    /**
     * 更新，验证规则组
     */
    public interface UPDATE_GROUP{};
    /**
     * 主键id
     */
    @NotNull(message = "id不能为空",groups = {INSERT_GROUP.class})
    private Long id;

    /**
     * 工作人员编号;对应t_ha_help_work_user表主键
     */
    @NotNull(message = "workUserId不能为空",groups = {INSERT_GROUP.class})
    private Long workUserId;

    /**
     * 事项编号
     */
    private String serviceId;

    /**
     * 服务类型;CLZB-材料准备，SJ-收件
     */
    private String serviceType;

    /**
     * 服务状态;1-有权限，2-无权限
     */
    private String serviceStatus;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 创建时间
     */
    private Date createDate;

    /**
     * 更新人
     */
    private String updateBy;

    /**
     * 更新时间
     */
    private Date updateDate;
}
