package com.zfsoft.ha.data;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@ApiModel(value = "帮代办工作人员排班",description = "")
public class HaWorkUserSchedule {
    /**
     * 新增用户，验证规则组
     */
    public interface INSERT_GROUP{};

    /**
     * 更新用户，验证规则组
     */
    public interface UPDATE_GROUP{};
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_ha_work_user_schedule.ID
     *
     * @mbg.generated
     */
    /** 主键 */
    @NotNull(message = "id不能为空",groups = {HaWorkUser.UPDATE_GROUP.class})
    private Long id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_ha_work_user_schedule.WORK_USER_ID
     *
     * @mbg.generated
     */
    private Long workUserId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_ha_work_user_schedule.WORK_DATE
     *
     * @mbg.generated
     */
    private Date workDate;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_ha_work_user_schedule.DAY_OF_WEEK
     *
     * @mbg.generated
     */
    private String dayOfWeek;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_ha_work_user_schedule.YEAR_MONTH
     *
     * @mbg.generated
     */
    private String yearMonth;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_ha_work_user_schedule.AM_OR_PM
     *
     * @mbg.generated
     */
    private String amOrPm;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_ha_work_user_schedule.SCHEDULE_STATUS
     *
     * @mbg.generated
     */
    private String scheduleStatus;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_ha_work_user_schedule.CREATE_BY
     *
     * @mbg.generated
     */
    private String createBy;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_ha_work_user_schedule.CREATE_DATE
     *
     * @mbg.generated
     */
    private Date createDate;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_ha_work_user_schedule.UPDATE_BY
     *
     * @mbg.generated
     */
    private String updateBy;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_ha_work_user_schedule.UPDATE_DATE
     *
     * @mbg.generated
     */
    private Date updateDate;


}