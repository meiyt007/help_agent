package com.zfsoft.ha.dbaccess.dao;

import com.zfsoft.ha.dbaccess.data.DbHaServiceCommonProblem;
import com.zfsoft.ha.dbaccess.data.example.DbHaServiceCommonProblemExample;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface DbHaServiceCommonProblemMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_ha_service_common_problem
     *
     * @mbg.generated
     */
    long countByExample(DbHaServiceCommonProblemExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_ha_service_common_problem
     *
     * @mbg.generated
     */
    int deleteByExample(DbHaServiceCommonProblemExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_ha_service_common_problem
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_ha_service_common_problem
     *
     * @mbg.generated
     */
    int insert(DbHaServiceCommonProblem record);
    /**
     * 修改数据
     *
     * @param record 实例对象
     * @return 影响行数
     */
    int update(DbHaServiceCommonProblem record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_ha_service_common_problem
     *
     * @mbg.generated
     */
    int insertSelective(DbHaServiceCommonProblem record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_ha_service_common_problem
     *
     * @mbg.generated
     */
    List<DbHaServiceCommonProblem> selectByExample(DbHaServiceCommonProblemExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_ha_service_common_problem
     *
     * @mbg.generated
     */
    DbHaServiceCommonProblem selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_ha_service_common_problem
     *
     * @mbg.generated
     */
    int updateByExampleSelective(@Param("record") DbHaServiceCommonProblem record, @Param("example") DbHaServiceCommonProblemExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_ha_service_common_problem
     *
     * @mbg.generated
     */
    int updateByExample(@Param("record") DbHaServiceCommonProblem record, @Param("example") DbHaServiceCommonProblemExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_ha_service_common_problem
     *
     * @mbg.generated
     */
    int updateByPrimaryKeySelective(DbHaServiceCommonProblem record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_ha_service_common_problem
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(DbHaServiceCommonProblem record);
}