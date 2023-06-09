package com.zfsoft.microservice.form.dbaccess.dao;

import com.zfsoft.microservice.form.dbaccess.data.DbFormModule;
import com.zfsoft.microservice.form.dbaccess.data.DbFormModuleExample;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface DbFormModuleMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_form_module
     *
     * @mbggenerated
     */
    int countByExample(DbFormModuleExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_form_module
     *
     * @mbggenerated
     */
    int deleteByExample(DbFormModuleExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_form_module
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_form_module
     *
     * @mbggenerated
     */
    int insert(DbFormModule record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_form_module
     *
     * @mbggenerated
     */
    int insertSelective(DbFormModule record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_form_module
     *
     * @mbggenerated
     */
    List<DbFormModule> selectByExample(DbFormModuleExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_form_module
     *
     * @mbggenerated
     */
    DbFormModule selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_form_module
     *
     * @mbggenerated
     */
    int updateByExampleSelective(@Param("record") DbFormModule record, @Param("example") DbFormModuleExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_form_module
     *
     * @mbggenerated
     */
    int updateByExample(@Param("record") DbFormModule record, @Param("example") DbFormModuleExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_form_module
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(DbFormModule record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_form_module
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(DbFormModule record);

    /**
     * @description:  逻辑删除对象
     * @author: wuxx
     * @Date: 2021/04/2 11:23
     **/
    @Update({"update t_form_module set IS_DELETE='1' where MODULE_OID = #{moduleOid,jdbcType=VARCHAR}"
    })
    int deleteByModuleOid(String moduleOid);

    /**
     * @description: 根据授权可以获取授权对象
     * @param moduleOid 业务主键
     * @author: wuxx
     * @Date: 2021/4/2 10:46
     **/
    DbFormModule selectByModuleOid(String moduleOid);
}
