package com.zfsoft.ha.dbaccess.dao;

import com.zfsoft.ha.dbaccess.data.DbHaPerformanceRegulartimeRecord;
import com.zfsoft.ha.dbaccess.data.example.DbHaPerformanceRegulartimeRecordExample;
import com.zfsoft.ha.dbaccess.data.vo.DbHaPerformanceAppraisalStatisticVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
@Mapper
public interface DbHaPerformanceRegulartimeRecordMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_ha_performance_regulartime_record
     *
     * @mbg.generated
     */
    long countByExample(DbHaPerformanceRegulartimeRecordExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_ha_performance_regulartime_record
     *
     * @mbg.generated
     */
    int deleteByExample(DbHaPerformanceRegulartimeRecordExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_ha_performance_regulartime_record
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_ha_performance_regulartime_record
     *
     * @mbg.generated
     */
    int insert(DbHaPerformanceRegulartimeRecord record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_ha_performance_regulartime_record
     *
     * @mbg.generated
     */
    int insertSelective(DbHaPerformanceRegulartimeRecord record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_ha_performance_regulartime_record
     *
     * @mbg.generated
     */
    List<DbHaPerformanceRegulartimeRecord> selectByExample(DbHaPerformanceRegulartimeRecordExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_ha_performance_regulartime_record
     *
     * @mbg.generated
     */
    DbHaPerformanceRegulartimeRecord selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_ha_performance_regulartime_record
     *
     * @mbg.generated
     */
    int updateByExampleSelective(@Param("record") DbHaPerformanceRegulartimeRecord record, @Param("example") DbHaPerformanceRegulartimeRecordExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_ha_performance_regulartime_record
     *
     * @mbg.generated
     */
    int updateByExample(@Param("record") DbHaPerformanceRegulartimeRecord record, @Param("example") DbHaPerformanceRegulartimeRecordExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_ha_performance_regulartime_record
     *
     * @mbg.generated
     */
    int updateByPrimaryKeySelective(DbHaPerformanceRegulartimeRecord record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_ha_performance_regulartime_record
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(DbHaPerformanceRegulartimeRecord record);

    /**
     * 根据常规时长对象查询绩效统计的常规时长记录信息
     * @param dbHaPerformanceRegulartimeRecord
     * @return
     */
    DbHaPerformanceRegulartimeRecord selectPerformanceRegulartimeRecordByHaPerformanceRegulartimeRecord(DbHaPerformanceRegulartimeRecord dbHaPerformanceRegulartimeRecord);

    /**
     * 根据条件查询帮办人全部时长绩效统计信息
     * @param haPerformanceAppraisalStatisticVo
     * @return
     */
    List<DbHaPerformanceAppraisalStatisticVo> queryDbHaPerformanceAppraisalStatisticVoPageList(DbHaPerformanceAppraisalStatisticVo haPerformanceAppraisalStatisticVo);

    List<DbHaPerformanceAppraisalStatisticVo> queryDbHaPerformanceAppraisalStatisticVoPageList2(DbHaPerformanceAppraisalStatisticVo haPerformanceAppraisalStatisticVo);

    DbHaPerformanceAppraisalStatisticVo queryDbHaPerformanceAppraisalStatisticVoByWorkUserOid(DbHaPerformanceAppraisalStatisticVo dbHaPerformanceAppraisalStatisticVo);
    /**
     * 根据条件查询组长未审核状态下的总数
     * @param haPerformanceAppraisalStatisticVo
     * @return
     */
    List<DbHaPerformanceAppraisalStatisticVo> queryGroupLeaderPageListByAuditStatus(DbHaPerformanceAppraisalStatisticVo haPerformanceAppraisalStatisticVo);

}