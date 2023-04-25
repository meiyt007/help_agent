package com.zfsoft.ha.data;

import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @author zhaobf
 * @version 1.0
 * @date 2022/9/5 13:56
 */
@ToString
@Data
public class HaServiceCommonProblem {
    /**
     * 新增，验证规则组
     */
    public interface INSERT_GROUP{};

    /**
     * 更新，验证规则组
     */
    public interface UPDATE_GROUP{};
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_ha_service_common_problem.ID
     *
     * @mbg.generated
     */
    /**
     * 主键
     */
    @NotNull(message = "id不能为空",groups = {HaQuestion.INSERT_GROUP.class})
    private Long id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_ha_service_common_problem.SERVICE_OID
     *
     * @mbg.generated
     */
    private String serviceOid;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_ha_service_common_problem.VALUE_OIDS
     *
     * @mbg.generated
     */
    private String valueOids;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_ha_service_common_problem.TITLE_OIDS
     *
     * @mbg.generated
     */
    private String titleOids;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_ha_service_common_problem.QUESTION
     *
     * @mbg.generated
     */
    private String question;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_ha_service_common_problem.ANSWER
     *
     * @mbg.generated
     */
    private String answer;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_ha_service_common_problem.MODIFY_DATE
     *
     * @mbg.generated
     */
    private Date modifyDate;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_ha_service_common_problem.DELETE_STATUS
     *
     * @mbg.generated
     */
    private Short deleteStatus;
}