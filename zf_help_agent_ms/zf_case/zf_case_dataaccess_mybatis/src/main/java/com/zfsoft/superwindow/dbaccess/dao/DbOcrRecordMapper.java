package com.zfsoft.superwindow.dbaccess.dao;

import com.zfsoft.superwindow.dbaccess.data.DbOcrRecord;
import com.zfsoft.superwindow.dbaccess.data.DbOcrRecordExample;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
@Mapper
public interface DbOcrRecordMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_ahs_ocr_record
     *
     * @mbggenerated
     */
    int countByExample(DbOcrRecordExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_ahs_ocr_record
     *
     * @mbggenerated
     */
    int deleteByExample(DbOcrRecordExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_ahs_ocr_record
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_ahs_ocr_record
     *
     * @mbggenerated
     */
    int insert(DbOcrRecord record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_ahs_ocr_record
     *
     * @mbggenerated
     */
    int insertSelective(DbOcrRecord record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_ahs_ocr_record
     *
     * @mbggenerated
     */
    List<DbOcrRecord> selectByExampleWithBLOBs(DbOcrRecordExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_ahs_ocr_record
     *
     * @mbggenerated
     */
    List<DbOcrRecord> selectByExample(DbOcrRecordExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_ahs_ocr_record
     *
     * @mbggenerated
     */
    DbOcrRecord selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_ahs_ocr_record
     *
     * @mbggenerated
     */
    int updateByExampleSelective(@Param("record") DbOcrRecord record, @Param("example") DbOcrRecordExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_ahs_ocr_record
     *
     * @mbggenerated
     */
    int updateByExampleWithBLOBs(@Param("record") DbOcrRecord record, @Param("example") DbOcrRecordExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_ahs_ocr_record
     *
     * @mbggenerated
     */
    int updateByExample(@Param("record") DbOcrRecord record, @Param("example") DbOcrRecordExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_ahs_ocr_record
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(DbOcrRecord record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_ahs_ocr_record
     *
     * @mbggenerated
     */
    int updateByPrimaryKeyWithBLOBs(DbOcrRecord record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_ahs_ocr_record
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(DbOcrRecord record);
}