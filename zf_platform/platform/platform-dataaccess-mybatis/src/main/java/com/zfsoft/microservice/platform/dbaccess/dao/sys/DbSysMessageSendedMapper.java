package com.zfsoft.microservice.platform.dbaccess.dao.sys;

import com.zfsoft.microservice.platform.dbaccess.data.sys.DbSysMessageSended;
import com.zfsoft.microservice.platform.dbaccess.data.sys.DbSysMessageSendedExample;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
@Mapper
public interface DbSysMessageSendedMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_sys_message_sended
     *
     * @mbggenerated
     */
    int countByExample(DbSysMessageSendedExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_sys_message_sended
     *
     * @mbggenerated
     */
    int deleteByExample(DbSysMessageSendedExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_sys_message_sended
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_sys_message_sended
     *
     * @mbggenerated
     */
    int insert(DbSysMessageSended record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_sys_message_sended
     *
     * @mbggenerated
     */
    int insertSelective(DbSysMessageSended record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_sys_message_sended
     *
     * @mbggenerated
     */
    List<DbSysMessageSended> selectByExample(DbSysMessageSendedExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_sys_message_sended
     *
     * @mbggenerated
     */
    DbSysMessageSended selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_sys_message_sended
     *
     * @mbggenerated
     */
    int updateByExampleSelective(@Param("record") DbSysMessageSended record, @Param("example") DbSysMessageSendedExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_sys_message_sended
     *
     * @mbggenerated
     */
    int updateByExample(@Param("record") DbSysMessageSended record, @Param("example") DbSysMessageSendedExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_sys_message_sended
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(DbSysMessageSended record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_sys_message_sended
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(DbSysMessageSended record);
}