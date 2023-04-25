package com.zfsoft.microservice.form.dbaccess.dao;

import com.zfsoft.microservice.form.dbaccess.data.DbFormTable;
import com.zfsoft.microservice.form.dbaccess.data.DbFormTableExample;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
@Mapper
public interface DbFormTableMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_form_table
     *
     * @mbggenerated
     */
    int countByExample(DbFormTableExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_form_table
     *
     * @mbggenerated
     */
    int deleteByExample(DbFormTableExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_form_table
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_form_table
     *
     * @mbggenerated
     */
    int insert(DbFormTable record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_form_table
     *
     * @mbggenerated
     */
    int insertSelective(DbFormTable record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_form_table
     *
     * @mbggenerated
     */
    List<DbFormTable> selectByExample(DbFormTableExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_form_table
     *
     * @mbggenerated
     */
    DbFormTable selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_form_table
     *
     * @mbggenerated
     */
    int updateByExampleSelective(@Param("record") DbFormTable record, @Param("example") DbFormTableExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_form_table
     *
     * @mbggenerated
     */
    int updateByExample(@Param("record") DbFormTable record, @Param("example") DbFormTableExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_form_table
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(DbFormTable record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_form_table
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(DbFormTable record);


    /**
     * @description: 根据业务主键查询对象列表
     * @param tableOid 业务主键
     * @author: wuxx
     * @Date: 2021/9/8 10:46
     **/
    DbFormTable selectByForeignKey(String tableOid);
}