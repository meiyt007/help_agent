package com.zfsoft.service.dbaccess.dao.sxService;

import com.zfsoft.service.dbaccess.data.sxService.DbSxMaterialFormTemp;
import com.zfsoft.service.dbaccess.data.sxService.DbSxMaterialFormTempExample;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;
@Mapper
public interface DbSxMaterialFormTempMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_sx_material_form_template
     *
     * @mbg.generated
     */
    long countByExample(DbSxMaterialFormTempExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_sx_material_form_template
     *
     * @mbg.generated
     */
    int deleteByExample(DbSxMaterialFormTempExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_sx_material_form_template
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_sx_material_form_template
     *
     * @mbg.generated
     */
    int insert(DbSxMaterialFormTemp record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_sx_material_form_template
     *
     * @mbg.generated
     */
    int insertSelective(DbSxMaterialFormTemp record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_sx_material_form_template
     *
     * @mbg.generated
     */
    List<DbSxMaterialFormTemp> selectByExample(DbSxMaterialFormTempExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_sx_material_form_template
     *
     * @mbg.generated
     */
    DbSxMaterialFormTemp selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_sx_material_form_template
     *
     * @mbg.generated
     */
    int updateByExampleSelective(@Param("record") DbSxMaterialFormTemp record, @Param("example") DbSxMaterialFormTempExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_sx_material_form_template
     *
     * @mbg.generated
     */
    int updateByExample(@Param("record") DbSxMaterialFormTemp record, @Param("example") DbSxMaterialFormTempExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_sx_material_form_template
     *
     * @mbg.generated
     */
    int updateByPrimaryKeySelective(DbSxMaterialFormTemp record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_sx_material_form_template
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(DbSxMaterialFormTemp record);

    // 查询材料表单数据
    DbSxMaterialFormTemp getOneMaterialFormTemplate(Map<String, String> params);
}