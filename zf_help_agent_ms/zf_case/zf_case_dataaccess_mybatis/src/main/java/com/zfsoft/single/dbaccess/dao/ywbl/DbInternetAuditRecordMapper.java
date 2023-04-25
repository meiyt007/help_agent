package com.zfsoft.single.dbaccess.dao.ywbl;

import com.zfsoft.single.dbaccess.data.ywbl.DbInternetAuditRecord;
import com.zfsoft.single.dbaccess.data.ywbl.DbInternetAuditRecordExample;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
@Mapper
public interface DbInternetAuditRecordMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_internet_audit_record
     *
     * @mbggenerated
     */
    int countByExample(DbInternetAuditRecordExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_internet_audit_record
     *
     * @mbggenerated
     */
    int deleteByExample(DbInternetAuditRecordExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_internet_audit_record
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_internet_audit_record
     *
     * @mbggenerated
     */
    int insert(DbInternetAuditRecord record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_internet_audit_record
     *
     * @mbggenerated
     */
    int insertSelective(DbInternetAuditRecord record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_internet_audit_record
     *
     * @mbggenerated
     */
    List<DbInternetAuditRecord> selectByExample(DbInternetAuditRecordExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_internet_audit_record
     *
     * @mbggenerated
     */
    DbInternetAuditRecord selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_internet_audit_record
     *
     * @mbggenerated
     */
    int updateByExampleSelective(@Param("record") DbInternetAuditRecord record, @Param("example") DbInternetAuditRecordExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_internet_audit_record
     *
     * @mbggenerated
     */
    int updateByExample(@Param("record") DbInternetAuditRecord record, @Param("example") DbInternetAuditRecordExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_internet_audit_record
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(DbInternetAuditRecord record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_internet_audit_record
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(DbInternetAuditRecord record);
}