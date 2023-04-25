package com.zfsoft.microservice.form.dbaccess.dao;

import com.zfsoft.microservice.form.dbaccess.data.DbFormMain;
import com.zfsoft.microservice.form.dbaccess.data.DbFormMainExample;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
@Mapper
public interface DbFormMainMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_form_main
     *
     * @mbggenerated
     */
    int countByExample(DbFormMainExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_form_main
     *
     * @mbggenerated
     */
    int deleteByExample(DbFormMainExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_form_main
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_form_main
     *
     * @mbggenerated
     */
    int insert(DbFormMain record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_form_main
     *
     * @mbggenerated
     */
    int insertSelective(DbFormMain record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_form_main
     *
     * @mbggenerated
     */
    List<DbFormMain> selectByExample(DbFormMainExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_form_main
     *
     * @mbggenerated
     */
    DbFormMain selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_form_main
     *
     * @mbggenerated
     */
    int updateByExampleSelective(@Param("record") DbFormMain record, @Param("example") DbFormMainExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_form_main
     *
     * @mbggenerated
     */
    int updateByExample(@Param("record") DbFormMain record, @Param("example") DbFormMainExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_form_main
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(DbFormMain record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_form_main
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(DbFormMain record);

    /**
     * @description:  逻辑删除对象
     * @author: wuxx
     * @Date: 2021/04/16 10:46
     **/
    @Update({"update t_form_main set IS_DELETE='1' where FORM_MAIN_OID = #{formMainOid,jdbcType=VARCHAR}"
    })
    int deleteByForeignKey(String formMainOid);

    /**
     * @description: 根据业务主键查询对象
     * @param formMainOid 业务主键
     * @author: wuxx
     * @Date: 2021/4/16 10:46
     **/
    DbFormMain selectByForeignKey(String formMainOid);
}
