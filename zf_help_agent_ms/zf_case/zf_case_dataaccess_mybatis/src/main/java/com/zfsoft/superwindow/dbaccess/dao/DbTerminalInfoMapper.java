package com.zfsoft.superwindow.dbaccess.dao;

import com.zfsoft.superwindow.dbaccess.data.DbTerminalInfo;
import com.zfsoft.superwindow.dbaccess.data.DbTerminalInfoExample;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
@Mapper
public interface DbTerminalInfoMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_ahs_terminal_info
     *
     * @mbg.generated
     */
    long countByExample(DbTerminalInfoExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_ahs_terminal_info
     *
     * @mbg.generated
     */
    int deleteByExample(DbTerminalInfoExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_ahs_terminal_info
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_ahs_terminal_info
     *
     * @mbg.generated
     */
    int insert(DbTerminalInfo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_ahs_terminal_info
     *
     * @mbg.generated
     */
    int insertSelective(DbTerminalInfo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_ahs_terminal_info
     *
     * @mbg.generated
     */
    List<DbTerminalInfo> selectByExample(DbTerminalInfoExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_ahs_terminal_info
     *
     * @mbg.generated
     */
    DbTerminalInfo selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_ahs_terminal_info
     *
     * @mbg.generated
     */
    int updateByExampleSelective(@Param("record") DbTerminalInfo record, @Param("example") DbTerminalInfoExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_ahs_terminal_info
     *
     * @mbg.generated
     */
    int updateByExample(@Param("record") DbTerminalInfo record, @Param("example") DbTerminalInfoExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_ahs_terminal_info
     *
     * @mbg.generated
     */
    int updateByPrimaryKeySelective(DbTerminalInfo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_ahs_terminal_info
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(DbTerminalInfo record);
}
