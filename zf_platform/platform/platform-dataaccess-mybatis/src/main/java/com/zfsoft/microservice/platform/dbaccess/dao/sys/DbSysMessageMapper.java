package com.zfsoft.microservice.platform.dbaccess.dao.sys;

import com.zfsoft.microservice.platform.dbaccess.data.sys.DbSysMessage;
import com.zfsoft.microservice.platform.dbaccess.data.sys.DbSysMessageExample;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.util.List;
@Mapper
public interface DbSysMessageMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_sys_message
     *
     * @mbggenerated
     */
    int countByExample(DbSysMessageExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_sys_message
     *
     * @mbggenerated
     */
    int deleteByExample(DbSysMessageExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_sys_message
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_sys_message
     *
     * @mbggenerated
     */
    int insert(DbSysMessage record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_sys_message
     *
     * @mbggenerated
     */
    int insertSelective(DbSysMessage record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_sys_message
     *
     * @mbggenerated
     */
    List<DbSysMessage> selectByExample(DbSysMessageExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_sys_message
     *
     * @mbggenerated
     */
    DbSysMessage selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_sys_message
     *
     * @mbggenerated
     */
    int updateByExampleSelective(@Param("record") DbSysMessage record, @Param("example") DbSysMessageExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_sys_message
     *
     * @mbggenerated
     */
    int updateByExample(@Param("record") DbSysMessage record, @Param("example") DbSysMessageExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_sys_message
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(DbSysMessage record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_sys_message
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(DbSysMessage record);

    /**
     * @description:  逻辑删除
     * @author: wuxx
     * @Date: 2020/10/23 14:23
     **/
    @Update({"update t_sys_message set IS_DELETE='1' where ID = #{id,jdbcType=BIGINT} "
    })
    int deleteByOid(Long id);
}