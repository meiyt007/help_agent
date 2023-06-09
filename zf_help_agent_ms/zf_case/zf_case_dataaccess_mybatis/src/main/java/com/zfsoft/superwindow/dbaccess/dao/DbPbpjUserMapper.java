package com.zfsoft.superwindow.dbaccess.dao;

import com.zfsoft.superwindow.dbaccess.data.DbPbpjUser;
import com.zfsoft.superwindow.dbaccess.data.DbPbpjUserExample;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
@Mapper
public interface DbPbpjUserMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_pbpj_user
     *
     * @mbggenerated
     */
    int countByExample(DbPbpjUserExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_pbpj_user
     *
     * @mbggenerated
     */
    int deleteByExample(DbPbpjUserExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_pbpj_user
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_pbpj_user
     *
     * @mbggenerated
     */
    int insert(DbPbpjUser record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_pbpj_user
     *
     * @mbggenerated
     */
    int insertSelective(DbPbpjUser record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_pbpj_user
     *
     * @mbggenerated
     */
    List<DbPbpjUser> selectByExample(DbPbpjUserExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_pbpj_user
     *
     * @mbggenerated
     */
    DbPbpjUser selectByPrimaryKey(Long id);
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_pbpj_user
     *
     * @mbggenerated
     */
    DbPbpjUser selectByUserOid(String UserOid);
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_pbpj_user
     *
     * @mbggenerated
     */
    int updateByExampleSelective(@Param("record") DbPbpjUser record, @Param("example") DbPbpjUserExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_pbpj_user
     *
     * @mbggenerated
     */
    int updateByExample(@Param("record") DbPbpjUser record, @Param("example") DbPbpjUserExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_pbpj_user
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(DbPbpjUser record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_pbpj_user
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(DbPbpjUser record);
}