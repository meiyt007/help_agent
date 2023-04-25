package com.zfsoft.ha.dbaccess.dao;

import com.zfsoft.ha.dbaccess.data.DbHaEvalResultItem;
import com.zfsoft.ha.dbaccess.data.example.DbHaEvalResultItemExample;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @description: 评价结果评价项信息表
 * @author: kangax
 * @date: 2022-08-12 15:13 
 */
@Mapper
public interface DbHaEvalResultItemMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_ha_eval_result_item
     *
     * @mbg.generated
     */
    long countByExample(DbHaEvalResultItemExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_ha_eval_result_item
     *
     * @mbg.generated
     */
    int deleteByExample(DbHaEvalResultItemExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_ha_eval_result_item
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_ha_eval_result_item
     *
     * @mbg.generated
     */
    int insert(DbHaEvalResultItem record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_ha_eval_result_item
     *
     * @mbg.generated
     */
    int insertSelective(DbHaEvalResultItem record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_ha_eval_result_item
     *
     * @mbg.generated
     */
    List<DbHaEvalResultItem> selectByExample(DbHaEvalResultItemExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_ha_eval_result_item
     *
     * @mbg.generated
     */
    DbHaEvalResultItem selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_ha_eval_result_item
     *
     * @mbg.generated
     */
    int updateByExampleSelective(@Param("record") DbHaEvalResultItem record, @Param("example") DbHaEvalResultItemExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_ha_eval_result_item
     *
     * @mbg.generated
     */
    int updateByExample(@Param("record") DbHaEvalResultItem record, @Param("example") DbHaEvalResultItemExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_ha_eval_result_item
     *
     * @mbg.generated
     */
    int updateByPrimaryKeySelective(DbHaEvalResultItem record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_ha_eval_result_item
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(DbHaEvalResultItem record);
}