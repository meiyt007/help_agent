package com.zfsoft.superwindow.dbaccess.dao.itfr;

import com.zfsoft.superwindow.dbaccess.data.itfr.DbEntityBasicFormField;
import com.zfsoft.superwindow.dbaccess.data.itfr.DbEntityBasicFormFieldExample;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DbEntityBasicFormFieldMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_basic_form_field
     *
     * @mbggenerated
     */
    int countByExample(DbEntityBasicFormFieldExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_basic_form_field
     *
     * @mbggenerated
     */
    int deleteByExample(DbEntityBasicFormFieldExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_basic_form_field
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_basic_form_field
     *
     * @mbggenerated
     */
    int insert(DbEntityBasicFormField record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_basic_form_field
     *
     * @mbggenerated
     */
    int insertSelective(DbEntityBasicFormField record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_basic_form_field
     *
     * @mbggenerated
     */
    List<DbEntityBasicFormField> selectByExample(DbEntityBasicFormFieldExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_basic_form_field
     *
     * @mbggenerated
     */
    DbEntityBasicFormField selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_basic_form_field
     *
     * @mbggenerated
     */
    int updateByExampleSelective(@Param("record") DbEntityBasicFormField record, @Param("example") DbEntityBasicFormFieldExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_basic_form_field
     *
     * @mbggenerated
     */
    int updateByExample(@Param("record") DbEntityBasicFormField record, @Param("example") DbEntityBasicFormFieldExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_basic_form_field
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(DbEntityBasicFormField record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_basic_form_field
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(DbEntityBasicFormField record);

    DbEntityBasicFormField selectByOid(String oid);
}
