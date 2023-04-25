package com.zfsoft.microservice.form.dbaccess.dao;

import com.zfsoft.microservice.form.dbaccess.data.DbFormReportLog;
import com.zfsoft.microservice.form.dbaccess.data.DbFormReportLogExample;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
@Mapper
public interface DbFormReportLogMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_form_report_log
     *
     * @mbggenerated
     */
    int countByExample(DbFormReportLogExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_form_report_log
     *
     * @mbggenerated
     */
    int deleteByExample(DbFormReportLogExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_form_report_log
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_form_report_log
     *
     * @mbggenerated
     */
    int insert(DbFormReportLog record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_form_report_log
     *
     * @mbggenerated
     */
    int insertSelective(DbFormReportLog record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_form_report_log
     *
     * @mbggenerated
     */
    List<DbFormReportLog> selectByExampleWithBLOBs(DbFormReportLogExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_form_report_log
     *
     * @mbggenerated
     */
    List<DbFormReportLog> selectByExample(DbFormReportLogExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_form_report_log
     *
     * @mbggenerated
     */
    DbFormReportLog selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_form_report_log
     *
     * @mbggenerated
     */
    int updateByExampleSelective(@Param("record") DbFormReportLog record, @Param("example") DbFormReportLogExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_form_report_log
     *
     * @mbggenerated
     */
    int updateByExampleWithBLOBs(@Param("record") DbFormReportLog record, @Param("example") DbFormReportLogExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_form_report_log
     *
     * @mbggenerated
     */
    int updateByExample(@Param("record") DbFormReportLog record, @Param("example") DbFormReportLogExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_form_report_log
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(DbFormReportLog record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_form_report_log
     *
     * @mbggenerated
     */
    int updateByPrimaryKeyWithBLOBs(DbFormReportLog record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_form_report_log
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(DbFormReportLog record);
}
