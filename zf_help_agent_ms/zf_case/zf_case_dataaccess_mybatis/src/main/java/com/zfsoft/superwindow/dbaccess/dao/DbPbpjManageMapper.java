package com.zfsoft.superwindow.dbaccess.dao;

import com.zfsoft.superwindow.dbaccess.data.DbPbpjManage;
import com.zfsoft.superwindow.dbaccess.data.DbPbpjManageExample;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
@Mapper
public interface DbPbpjManageMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_pbpj_manage
     *
     * @mbggenerated
     */
    int countByExample(DbPbpjManageExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_pbpj_manage
     *
     * @mbggenerated
     */
    int deleteByExample(DbPbpjManageExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_pbpj_manage
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_pbpj_manage
     *
     * @mbggenerated
     */
    int insert(DbPbpjManage record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_pbpj_manage
     *
     * @mbggenerated
     */
    int insertSelective(DbPbpjManage record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_pbpj_manage
     *
     * @mbggenerated
     */
    List<DbPbpjManage> selectByExample(DbPbpjManageExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_pbpj_manage
     *
     * @mbggenerated
     */
    DbPbpjManage selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_pbpj_manage
     *
     * @mbggenerated
     */
    int updateByExampleSelective(@Param("record") DbPbpjManage record, @Param("example") DbPbpjManageExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_pbpj_manage
     *
     * @mbggenerated
     */
    int updateByExample(@Param("record") DbPbpjManage record, @Param("example") DbPbpjManageExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_pbpj_manage
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(DbPbpjManage record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_pbpj_manage
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(DbPbpjManage record);

    /**
     * 根据账号平板评价管理信息
     * @param userCode
     * @return
     */
    DbPbpjManage getPbpjManageByUserCode(String userCode);

    /**
     * 根据设备编号查找设备信息
     * @param runCode
     * @return
     */
    DbPbpjManage getPbpjManageByRunCode(String runCode);
}