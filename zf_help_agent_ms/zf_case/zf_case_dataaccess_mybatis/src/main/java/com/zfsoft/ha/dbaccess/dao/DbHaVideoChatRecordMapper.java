package com.zfsoft.ha.dbaccess.dao;

import com.zfsoft.ha.dbaccess.data.DbHaVideoChatRecord;
import com.zfsoft.ha.dbaccess.data.example.DbHaVideoChatRecordExample;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Date;
import java.util.List;
@Mapper
public interface DbHaVideoChatRecordMapper {
    /**
     * 是否有新的消息
     * @param videoLatestTime
     * @param roomId
     * @return
     */
    @Select({"select count(*) from t_ha_video_chat_record where  CREATE_DATE > #{videoLatestTime} and ROOM_OID= #{roomId}"})
    int isHaveNewMessesLatestTime(Date videoLatestTime, Long roomId);

    /**
     * 是否有新的消息
     */
    @Select({"select count(*) from t_ha_video_chat_record where  CREATE_DATE > FROM_UNIXTIME(UNIX_TIMESTAMP(CAST(SYSDATE()AS DATE)))"})
    int isHaveNewMesses();
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_ha_video_chat_record
     *
     * @mbg.generated
     */
    long countByExample(DbHaVideoChatRecordExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_ha_video_chat_record
     *
     * @mbg.generated
     */
    int deleteByExample(DbHaVideoChatRecordExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_ha_video_chat_record
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_ha_video_chat_record
     *
     * @mbg.generated
     */
    int insert(DbHaVideoChatRecord record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_ha_video_chat_record
     *
     * @mbg.generated
     */
    int insertSelective(DbHaVideoChatRecord record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_ha_video_chat_record
     *
     * @mbg.generated
     */
    List<DbHaVideoChatRecord> selectByExample(DbHaVideoChatRecordExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_ha_video_chat_record
     *
     * @mbg.generated
     */
    DbHaVideoChatRecord selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_ha_video_chat_record
     *
     * @mbg.generated
     */
    int updateByExampleSelective(@Param("record") DbHaVideoChatRecord record, @Param("example") DbHaVideoChatRecordExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_ha_video_chat_record
     *
     * @mbg.generated
     */
    int updateByExample(@Param("record") DbHaVideoChatRecord record, @Param("example") DbHaVideoChatRecordExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_ha_video_chat_record
     *
     * @mbg.generated
     */
    int updateByPrimaryKeySelective(DbHaVideoChatRecord record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_ha_video_chat_record
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(DbHaVideoChatRecord record);
}
