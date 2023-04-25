package com.zfsoft.ha.dbaccess.dao;

import com.zfsoft.ha.dbaccess.data.DbHaVideoCloudRecord;
import com.zfsoft.ha.dbaccess.data.example.DbHaVideoCloudRecordExample;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
@Mapper
public interface DbHaVideoCloudRecordMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_ha_video_cloud_record
     *
     * @mbg.generated
     */
    long countByExample(DbHaVideoCloudRecordExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_ha_video_cloud_record
     *
     * @mbg.generated
     */
    int deleteByExample(DbHaVideoCloudRecordExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_ha_video_cloud_record
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_ha_video_cloud_record
     *
     * @mbg.generated
     */
    int insert(DbHaVideoCloudRecord record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_ha_video_cloud_record
     *
     * @mbg.generated
     */
    int insertSelective(DbHaVideoCloudRecord record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_ha_video_cloud_record
     *
     * @mbg.generated
     */
    List<DbHaVideoCloudRecord> selectByExample(DbHaVideoCloudRecordExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_ha_video_cloud_record
     *
     * @mbg.generated
     */
    DbHaVideoCloudRecord selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_ha_video_cloud_record
     *
     * @mbg.generated
     */
    int updateByExampleSelective(@Param("record") DbHaVideoCloudRecord record, @Param("example") DbHaVideoCloudRecordExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_ha_video_cloud_record
     *
     * @mbg.generated
     */
    int updateByExample(@Param("record") DbHaVideoCloudRecord record, @Param("example") DbHaVideoCloudRecordExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_ha_video_cloud_record
     *
     * @mbg.generated
     */
    int updateByPrimaryKeySelective(DbHaVideoCloudRecord record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_ha_video_cloud_record
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(DbHaVideoCloudRecord record);

    DbHaVideoCloudRecord selectByRoomIdAndRoomNumber(Long roomId, Long roomNumber);

}