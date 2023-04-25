package com.zfsoft.microservice.platform.data.sys;


import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @description:  上班时间设置表
 * @author: wuxx
 * @Date: 2021/5/10 11:51
 **/
@Data
@ToString
public class SysWorkTime {

    /**
     * 新增区划，验证规则组
     */
    public interface INSERT_GROUP{};

    /**
     * 更新区划，验证规则组
     */
    public interface UPDATE_GROUP{};

    /* 主键 */
    @NotNull(message = "id不能为空",groups = {UPDATE_GROUP.class})
    private Long id;

    /* 业务主键 */
    @NotNull(message = "业务主键不能为空",groups = { UPDATE_GROUP.class})
    private String workTimeOid;

    /* 班次 */
    @NotNull(message = "班次不能为空",groups = {INSERT_GROUP.class})
    private String timeNum;

    /* 上午上班时间 */
    private Date morningWorkOnTime;

    /* 上午下班时间 */
    private Date morningWorkOffTime;

    /* 下午上班时间 */
    private Date afternoonWorkOnTime;

    /* 下午下班时间 */
    private Date afternoonWorkOffTime;

    /* 启禁用状态 */
    private Integer isAble;

    /* 删除状态 */
    private Integer isDelete;

    /* 创建时间 */
    private Date createDate;
}
