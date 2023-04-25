package com.zfsoft.superwindow.dbaccess.dao;

import com.zfsoft.superwindow.dbaccess.data.DbKnowledgeBase;
import com.zfsoft.superwindow.dbaccess.data.DbKnowledgeBaseExample;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DbKnowledgeBaseMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_knowledge_base
     *
     * @mbggenerated
     */
    int countByExample(DbKnowledgeBaseExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_knowledge_base
     *
     * @mbggenerated
     */
    int deleteByExample(DbKnowledgeBaseExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_knowledge_base
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_knowledge_base
     *
     * @mbggenerated
     */
    int insert(DbKnowledgeBase record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_knowledge_base
     *
     * @mbggenerated
     */
    int insertSelective(DbKnowledgeBase record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_knowledge_base
     *
     * @mbggenerated
     */
    List<DbKnowledgeBase> selectByExample(DbKnowledgeBaseExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_knowledge_base
     *
     * @mbggenerated
     */
    DbKnowledgeBase selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_knowledge_base
     *
     * @mbggenerated
     */
    int updateByExampleSelective(@Param("record") DbKnowledgeBase record, @Param("example") DbKnowledgeBaseExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_knowledge_base
     *
     * @mbggenerated
     */
    int updateByExample(@Param("record") DbKnowledgeBase record, @Param("example") DbKnowledgeBaseExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_knowledge_base
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(DbKnowledgeBase record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_knowledge_base
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(DbKnowledgeBase record);
}