package com.zfsoft.ha.data;

import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.util.Date;
/**
 * @Description 核酸信息表
 * @Author:zhaobf
 * @Date: 2022-07-26 18:46
 */
@Data
@ToString
public class HaMassesNucleic {
    /**
     * 新增，验证规则组
     */
    public interface INSERT_GROUP{};

    /**
     * 更新，验证规则组
     */
    public interface UPDATE_GROUP{};
    /**
     * 核酸信息id
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_ha_masses_nucleic.ID
     *
     * @mbg.generated
     */
    @NotNull(message = "id不能为空",groups = {INSERT_GROUP.class})
    private Long id;

    /**
     * 队列编号
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_ha_masses_nucleic.QUEUE_ID
     *
     * @mbg.generated
     */
    private Long queueId;

    /**
     * 姓名
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_ha_masses_nucleic.NAME
     *
     * @mbg.generated
     */
    private String name;

    /**
     * 身份证号码
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_ha_masses_nucleic.CARD_NO
     *
     * @mbg.generated
     */
    private String cardNo;

    /**
     * 核酸采集时间
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_ha_masses_nucleic.NUCLEIC_COLLECTION_TIME
     *
     * @mbg.generated
     */
    private Date nucleicCollectionTime;

    /**
     * 核酸采集点名册
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_ha_masses_nucleic.NUCLEIC_COLLECTION_POINT
     *
     * @mbg.generated
     */
    private String nucleicCollectionPoint;

    /**
     * 核酸报告时间
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_ha_masses_nucleic.NUCLEIC_TESTING_TIME
     *
     * @mbg.generated
     */
    private Date nucleicTestingTime;

    /**
     * 核酸结果
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_ha_masses_nucleic.NUCLEIC_RESULT
     *
     * @mbg.generated
     */
    private String nucleicResult;

    /**
     * 核酸结果代码;1-阴性，2-阳性，3-其他
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_ha_masses_nucleic.NUCLEIC_RESULT_CODE
     *
     * @mbg.generated
     */
    private String nucleicResultCode;

    /**
     * 创建人
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_ha_masses_nucleic.CREATE_BY
     *
     * @mbg.generated
     */
    private String createBy;

    /**
     * 创建时间
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_ha_masses_nucleic.CREATE_DATE
     *
     * @mbg.generated
     */
    private Date createDate;

    /**
     * 更新人
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_ha_masses_nucleic.UPDATE_BY
     *
     * @mbg.generated
     */
    private String updateBy;

    /**
     * 更新时间
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_ha_masses_nucleic.UPDATE_DATE
     *
     * @mbg.generated
     */
    private Date updateDate;

    /**
     * 核酸检测编号
     */
    private String nucleicCode;
}