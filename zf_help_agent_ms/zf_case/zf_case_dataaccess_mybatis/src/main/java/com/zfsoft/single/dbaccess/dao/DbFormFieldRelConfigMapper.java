package com.zfsoft.single.dbaccess.dao;

import com.zfsoft.single.dbaccess.data.DbFormFieldRelConfig;
import com.zfsoft.single.dbaccess.data.DbFormFieldRelConfigExample;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
@Mapper
public interface DbFormFieldRelConfigMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_form_field_rel_config
     *
     * @mbggenerated
     */
    int countByExample(DbFormFieldRelConfigExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_form_field_rel_config
     *
     * @mbggenerated
     */
    int deleteByExample(DbFormFieldRelConfigExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_form_field_rel_config
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_form_field_rel_config
     *
     * @mbggenerated
     */
    int insert(DbFormFieldRelConfig record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_form_field_rel_config
     *
     * @mbggenerated
     */
    int insertSelective(DbFormFieldRelConfig record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_form_field_rel_config
     *
     * @mbggenerated
     */
    List<DbFormFieldRelConfig> selectByExample(DbFormFieldRelConfigExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_form_field_rel_config
     *
     * @mbggenerated
     */
    DbFormFieldRelConfig selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_form_field_rel_config
     *
     * @mbggenerated
     */
    int updateByExampleSelective(@Param("record") DbFormFieldRelConfig record, @Param("example") DbFormFieldRelConfigExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_form_field_rel_config
     *
     * @mbggenerated
     */
    int updateByExample(@Param("record") DbFormFieldRelConfig record, @Param("example") DbFormFieldRelConfigExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_form_field_rel_config
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(DbFormFieldRelConfig record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_form_field_rel_config
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(DbFormFieldRelConfig record);

    /**
     * 根据业务主键查询信息
     * @param oid
     * @return
     */
    DbFormFieldRelConfig selectOneByOid(String oid);
    List<DbFormFieldRelConfig> selectByServiceOid(DbFormFieldRelConfig record);

}
