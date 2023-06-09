package com.zfsoft.microservice.form.dbaccess.dao;

import com.zfsoft.microservice.form.dbaccess.data.DbFormTemplate;
import com.zfsoft.microservice.form.dbaccess.data.DbFormTemplateExample;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
@Mapper
public interface DbFormTemplateMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_form_template
     *
     * @mbggenerated
     */
    int countByExample(DbFormTemplateExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_form_template
     *
     * @mbggenerated
     */
    int deleteByExample(DbFormTemplateExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_form_template
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_form_template
     *
     * @mbggenerated
     */
    int insert(DbFormTemplate record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_form_template
     *
     * @mbggenerated
     */
    int insertSelective(DbFormTemplate record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_form_template
     *
     * @mbggenerated
     */
    List<DbFormTemplate> selectByExampleWithBLOBs(DbFormTemplateExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_form_template
     *
     * @mbggenerated
     */
    List<DbFormTemplate> selectByExample(DbFormTemplateExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_form_template
     *
     * @mbggenerated
     */
    DbFormTemplate selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_form_template
     *
     * @mbggenerated
     */
    int updateByExampleSelective(@Param("record") DbFormTemplate record, @Param("example") DbFormTemplateExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_form_template
     *
     * @mbggenerated
     */
    int updateByExampleWithBLOBs(@Param("record") DbFormTemplate record, @Param("example") DbFormTemplateExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_form_template
     *
     * @mbggenerated
     */
    int updateByExample(@Param("record") DbFormTemplate record, @Param("example") DbFormTemplateExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_form_template
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(DbFormTemplate record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_form_template
     *
     * @mbggenerated
     */
    int updateByPrimaryKeyWithBLOBs(DbFormTemplate record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_form_template
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(DbFormTemplate record);


    /**
     * @description:  逻辑删除对象
     * @author: wuxx
     * @Date: 2021/04/19 10:46
     **/
    @Update({"update t_form_template set IS_DELETE='1' where TEMPLATE_OID = #{templateOid,jdbcType=VARCHAR}"
    })
    int deleteByForeignKey(String formMainOid);

    /**
     * @description: 根据业务主键查询对象
     * @param templateOid 业务主键
     * @author: wuxx
     * @Date: 2021/4/19 10:46
     **/
    DbFormTemplate selectByForeignKey(String templateOid);
}
