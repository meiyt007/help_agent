package com.zfsoft.superwindow.dbaccess.dao;

import com.zfsoft.superwindow.dbaccess.data.DbPointInfo;
import com.zfsoft.superwindow.dbaccess.data.DbPointInfoExample;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
@Mapper
public interface DbPointInfoMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_ahs_point_info
     *
     * @mbg.generated
     */
    long countByExample(DbPointInfoExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_ahs_point_info
     *
     * @mbg.generated
     */
    int deleteByExample(DbPointInfoExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_ahs_point_info
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_ahs_point_info
     *
     * @mbg.generated
     */
    int insert(DbPointInfo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_ahs_point_info
     *
     * @mbg.generated
     */
    int insertSelective(DbPointInfo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_ahs_point_info
     *
     * @mbg.generated
     */
    List<DbPointInfo> selectByExample(DbPointInfoExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_ahs_point_info
     *
     * @mbg.generated
     */
    DbPointInfo selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_ahs_point_info
     *
     * @mbg.generated
     */
    int updateByExampleSelective(@Param("record") DbPointInfo record, @Param("example") DbPointInfoExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_ahs_point_info
     *
     * @mbg.generated
     */
    int updateByExample(@Param("record") DbPointInfo record, @Param("example") DbPointInfoExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_ahs_point_info
     *
     * @mbg.generated
     */
    int updateByPrimaryKeySelective(DbPointInfo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_ahs_point_info
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(DbPointInfo record);
}