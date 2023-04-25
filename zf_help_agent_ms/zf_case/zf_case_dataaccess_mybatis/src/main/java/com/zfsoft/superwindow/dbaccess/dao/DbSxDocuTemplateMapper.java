package com.zfsoft.superwindow.dbaccess.dao;

import com.zfsoft.superwindow.dbaccess.data.DbSxDocuTemplate;
import com.zfsoft.superwindow.dbaccess.data.DbSxDocuTemplateExample;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DbSxDocuTemplateMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_sx_docu_template
     *
     * @mbggenerated
     */
    int countByExample(DbSxDocuTemplateExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_sx_docu_template
     *
     * @mbggenerated
     */
    int deleteByExample(DbSxDocuTemplateExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_sx_docu_template
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_sx_docu_template
     *
     * @mbggenerated
     */
    int insert(DbSxDocuTemplate record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_sx_docu_template
     *
     * @mbggenerated
     */
    int insertSelective(DbSxDocuTemplate record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_sx_docu_template
     *
     * @mbggenerated
     */
    List<DbSxDocuTemplate> selectByExample(DbSxDocuTemplateExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_sx_docu_template
     *
     * @mbggenerated
     */
    DbSxDocuTemplate selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_sx_docu_template
     *
     * @mbggenerated
     */
    int updateByExampleSelective(@Param("record") DbSxDocuTemplate record, @Param("example") DbSxDocuTemplateExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_sx_docu_template
     *
     * @mbggenerated
     */
    int updateByExample(@Param("record") DbSxDocuTemplate record, @Param("example") DbSxDocuTemplateExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_sx_docu_template
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(DbSxDocuTemplate record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_sx_docu_template
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(DbSxDocuTemplate record);
}