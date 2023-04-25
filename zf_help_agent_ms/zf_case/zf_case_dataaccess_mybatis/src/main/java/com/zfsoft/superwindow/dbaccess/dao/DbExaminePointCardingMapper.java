package com.zfsoft.superwindow.dbaccess.dao;

import com.zfsoft.superwindow.dbaccess.data.DbExaminePointCarding;
import com.zfsoft.superwindow.dbaccess.data.DbExaminePointCardingExample;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
@Mapper
public interface DbExaminePointCardingMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_examine_point_carding
     *
     * @mbggenerated
     */
    int countByExample(DbExaminePointCardingExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_examine_point_carding
     *
     * @mbggenerated
     */
    int deleteByExample(DbExaminePointCardingExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_examine_point_carding
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_examine_point_carding
     *
     * @mbggenerated
     */
    int insert(DbExaminePointCarding record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_examine_point_carding
     *
     * @mbggenerated
     */
    int insertSelective(DbExaminePointCarding record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_examine_point_carding
     *
     * @mbggenerated
     */
    List<DbExaminePointCarding> selectByExample(DbExaminePointCardingExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_examine_point_carding
     *
     * @mbggenerated
     */
    DbExaminePointCarding selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_examine_point_carding
     *
     * @mbggenerated
     */
    int updateByExampleSelective(@Param("record") DbExaminePointCarding record, @Param("example") DbExaminePointCardingExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_examine_point_carding
     *
     * @mbggenerated
     */
    int updateByExample(@Param("record") DbExaminePointCarding record, @Param("example") DbExaminePointCardingExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_examine_point_carding
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(DbExaminePointCarding record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_examine_point_carding
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(DbExaminePointCarding record);
}