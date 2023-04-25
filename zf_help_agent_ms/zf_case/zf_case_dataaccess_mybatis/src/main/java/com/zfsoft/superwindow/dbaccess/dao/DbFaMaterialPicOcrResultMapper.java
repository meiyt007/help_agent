package com.zfsoft.superwindow.dbaccess.dao;

import com.zfsoft.superwindow.dbaccess.data.DbFaMaterialPicOcrResult;
import com.zfsoft.superwindow.dbaccess.data.DbFaMaterialPicOcrResultExample;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
@Mapper
public interface DbFaMaterialPicOcrResultMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_fa_material_pic_ocr_result
     *
     * @mbggenerated
     */
    int countByExample(DbFaMaterialPicOcrResultExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_fa_material_pic_ocr_result
     *
     * @mbggenerated
     */
    int deleteByExample(DbFaMaterialPicOcrResultExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_fa_material_pic_ocr_result
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_fa_material_pic_ocr_result
     *
     * @mbggenerated
     */
    int insert(DbFaMaterialPicOcrResult record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_fa_material_pic_ocr_result
     *
     * @mbggenerated
     */
    int insertSelective(DbFaMaterialPicOcrResult record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_fa_material_pic_ocr_result
     *
     * @mbggenerated
     */
    List<DbFaMaterialPicOcrResult> selectByExample(DbFaMaterialPicOcrResultExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_fa_material_pic_ocr_result
     *
     * @mbggenerated
     */
    DbFaMaterialPicOcrResult selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_fa_material_pic_ocr_result
     *
     * @mbggenerated
     */
    int updateByExampleSelective(@Param("record") DbFaMaterialPicOcrResult record, @Param("example") DbFaMaterialPicOcrResultExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_fa_material_pic_ocr_result
     *
     * @mbggenerated
     */
    int updateByExample(@Param("record") DbFaMaterialPicOcrResult record, @Param("example") DbFaMaterialPicOcrResultExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_fa_material_pic_ocr_result
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(DbFaMaterialPicOcrResult record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_fa_material_pic_ocr_result
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(DbFaMaterialPicOcrResult record);
}