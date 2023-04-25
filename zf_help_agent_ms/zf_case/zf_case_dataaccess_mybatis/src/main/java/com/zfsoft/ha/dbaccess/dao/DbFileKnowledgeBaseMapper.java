package com.zfsoft.ha.dbaccess.dao;

import com.zfsoft.ha.dbaccess.data.DbFileKnowledgeBase;
import com.zfsoft.ha.dbaccess.data.example.DbFileKnowledgeBaseExample;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
@Mapper
public interface DbFileKnowledgeBaseMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_ha_file_knowledge_base
     *
     * @mbg.generated
     */
    long countByExample(DbFileKnowledgeBaseExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_ha_file_knowledge_base
     *
     * @mbg.generated
     */
    int deleteByExample(DbFileKnowledgeBaseExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_ha_file_knowledge_base
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_ha_file_knowledge_base
     *
     * @mbg.generated
     */
    int insert(DbFileKnowledgeBase record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_ha_file_knowledge_base
     *
     * @mbg.generated
     */
    int insertSelective(DbFileKnowledgeBase record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_ha_file_knowledge_base
     *
     * @mbg.generated
     */
    List<DbFileKnowledgeBase> selectByExample(DbFileKnowledgeBaseExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_ha_file_knowledge_base
     *
     * @mbg.generated
     */
    DbFileKnowledgeBase selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_ha_file_knowledge_base
     *
     * @mbg.generated
     */
    int updateByExampleSelective(@Param("record") DbFileKnowledgeBase record, @Param("example") DbFileKnowledgeBaseExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_ha_file_knowledge_base
     *
     * @mbg.generated
     */
    int updateByExample(@Param("record") DbFileKnowledgeBase record, @Param("example") DbFileKnowledgeBaseExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_ha_file_knowledge_base
     *
     * @mbg.generated
     */
    int updateByPrimaryKeySelective(DbFileKnowledgeBase record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_ha_file_knowledge_base
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(DbFileKnowledgeBase record);
}