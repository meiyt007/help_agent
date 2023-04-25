package com.zfsoft.microservice.form.dbaccess.dao;

import com.zfsoft.microservice.form.dbaccess.data.DbFormDesign;
import com.zfsoft.microservice.form.dbaccess.data.DbFormDesignExample;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
@Mapper
public interface DbFormDesignMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_form_design
     *
     * @mbggenerated
     */
    int countByExample(DbFormDesignExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_form_design
     *
     * @mbggenerated
     */
    int deleteByExample(DbFormDesignExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_form_design
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_form_design
     *
     * @mbggenerated
     */
    int insert(DbFormDesign record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_form_design
     *
     * @mbggenerated
     */
    int insertSelective(DbFormDesign record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_form_design
     *
     * @mbggenerated
     */
    List<DbFormDesign> selectByExampleWithBLOBs(DbFormDesignExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_form_design
     *
     * @mbggenerated
     */
    List<DbFormDesign> selectByExample(DbFormDesignExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_form_design
     *
     * @mbggenerated
     */
    DbFormDesign selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_form_design
     *
     * @mbggenerated
     */
    int updateByExampleSelective(@Param("record") DbFormDesign record, @Param("example") DbFormDesignExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_form_design
     *
     * @mbggenerated
     */
    int updateByExampleWithBLOBs(@Param("record") DbFormDesign record, @Param("example") DbFormDesignExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_form_design
     *
     * @mbggenerated
     */
    int updateByExample(@Param("record") DbFormDesign record, @Param("example") DbFormDesignExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_form_design
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(DbFormDesign record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_form_design
     *
     * @mbggenerated
     */
    int updateByPrimaryKeyWithBLOBs(DbFormDesign record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_form_design
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(DbFormDesign record);

    /**
     * @description:  逻辑删除对象
     * @author: wuxx
     * @Date: 2021/04/16 10:46
     **/
    @Update({"update t_form_design set IS_DELETE='1' where DESIGN_OID = #{designOid,jdbcType=VARCHAR}"
    })
    int deleteByForeignKey(String designOid);

    /**
     * @description: 根据业务主键查询对象
     * @param designOid 业务主键
     * @author: wuxx
     * @Date: 2021/4/16 10:46
     **/
    DbFormDesign selectByForeignKey(String designOid);

    /**
     * @description:  逻辑删除对象
     * @author: wuxx
     * @Date: 2021/04/16 10:46
     **/
    @Update({"update t_form_design set IS_DELETE='1' where FORM_MAIN_OID = #{formMainOid,jdbcType=VARCHAR}"
    })
    int deleteAllByFormMainOid(String formMainOid);

    /**
     * @description: 查询第一个
     * @param example 对象
     * @author: wuxx
     * @Date: 2021/6/17 14:25
     **/
    DbFormDesign selectTopByExampleWithBLOBs(DbFormDesignExample example);

}