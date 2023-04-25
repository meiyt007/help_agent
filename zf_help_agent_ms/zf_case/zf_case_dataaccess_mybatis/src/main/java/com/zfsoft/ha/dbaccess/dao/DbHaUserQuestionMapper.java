package com.zfsoft.ha.dbaccess.dao;

import com.zfsoft.ha.dbaccess.data.DbHaUserQuestion;
import com.zfsoft.ha.dbaccess.data.example.DbHaUserQuestionExample;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface DbHaUserQuestionMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_ha_user_question
     *
     * @mbg.generated
     */
    long countByExample(DbHaUserQuestionExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_ha_user_question
     *
     * @mbg.generated
     */
    int deleteByExample(DbHaUserQuestionExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_ha_user_question
     *
     * @mbg.generated
     */
    int insert(DbHaUserQuestion record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_ha_user_question
     *
     * @mbg.generated
     */
    int insertSelective(DbHaUserQuestion record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_ha_user_question
     *
     * @mbg.generated
     */
    List<DbHaUserQuestion> selectByExample(DbHaUserQuestionExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_ha_user_question
     *
     * @mbg.generated
     */
    int updateByExampleSelective(@Param("record") DbHaUserQuestion record, @Param("example") DbHaUserQuestionExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_ha_user_question
     *
     * @mbg.generated
     */
    int updateByExample(@Param("record") DbHaUserQuestion record, @Param("example") DbHaUserQuestionExample example);

    /**
     * 根据主键修改
     *
     * @param dbHaUserQuestion
     * @return
     */
    int updateByPrimaryKeySelective(DbHaUserQuestion dbHaUserQuestion);

    /**
     * 根据主键查询对象
     *
     * @param id
     * @return
     */
    DbHaUserQuestion selectByPrimaryKey(Long id);

    /**
     * 根据主键删除逻辑删除对象信息
     *
     * @param id 主键
     * @param deleteStatus 删除状态设置为2 已删除
     */
    @Update({"update t_ha_user_question set DELETE_STATUS=#{deleteStatus} where ID =#{id}"})
    int deleteByUserQuestionId(String deleteStatus,Long id);

    /*
     * 联合查询用户常见问题及工作人员信息
     * */
    List<DbHaUserQuestion> selectUserQuestionAndWorkPersonInfo(DbHaUserQuestion dbHaUserQuestion);

    /**
     * 查询用户常见问题list 参数不为空时为模糊查询
     * @param question  用户常见问题
     * @param workUserId 用户主键
     * @return
     */
    List<DbHaUserQuestion> queryHaQuestionListByLikeQuestion(Long workUserId,String question);

    /**
     * 获取所有问题
     * @return
     */
    List<DbHaUserQuestion> queryHaQuestionList();
}