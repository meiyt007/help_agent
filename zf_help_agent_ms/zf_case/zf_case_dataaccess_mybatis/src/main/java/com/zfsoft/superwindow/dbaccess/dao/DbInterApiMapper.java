package com.zfsoft.superwindow.dbaccess.dao;

import com.zfsoft.superwindow.dbaccess.data.DbInterApi;
import com.zfsoft.superwindow.dbaccess.data.DbInterApiExample;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
@Mapper
public interface DbInterApiMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_inter_api
     *
     * @mbg.generated
     */
    long countByExample(DbInterApiExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_inter_api
     *
     * @mbg.generated
     */
    int deleteByExample(DbInterApiExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_inter_api
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_inter_api
     *
     * @mbg.generated
     */
    int insert(DbInterApi record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_inter_api
     *
     * @mbg.generated
     */
    int insertSelective(DbInterApi record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_inter_api
     *
     * @mbg.generated
     */
    List<DbInterApi> selectByExample(DbInterApiExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_inter_api
     *
     * @mbg.generated
     */
    DbInterApi selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_inter_api
     *
     * @mbg.generated
     */
    int updateByExampleSelective(@Param("record") DbInterApi record, @Param("example") DbInterApiExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_inter_api
     *
     * @mbg.generated
     */
    int updateByExample(@Param("record") DbInterApi record, @Param("example") DbInterApiExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_inter_api
     *
     * @mbg.generated
     */
    int updateByPrimaryKeySelective(DbInterApi record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_inter_api
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(DbInterApi record);
}