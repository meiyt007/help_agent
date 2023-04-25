package com.zfsoft.service.dbaccess.dao.sxSituation;

import com.zfsoft.service.dbaccess.data.sxSituation.DbSxServiceOptionVal;
import com.zfsoft.service.dbaccess.data.sxSituation.DbSxServiceOptionValExample;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
@Mapper
public interface DbSxServiceOptionValMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_sx_service_option_val
     *
     * @mbggenerated
     */
    int countByExample(DbSxServiceOptionValExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_sx_service_option_val
     *
     * @mbggenerated
     */
    int deleteByExample(DbSxServiceOptionValExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_sx_service_option_val
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(String oid);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_sx_service_option_val
     *
     * @mbggenerated
     */
    int insert(DbSxServiceOptionVal record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_sx_service_option_val
     *
     * @mbggenerated
     */
    int insertSelective(DbSxServiceOptionVal record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_sx_service_option_val
     *
     * @mbggenerated
     */
    List<DbSxServiceOptionVal> selectByExample(DbSxServiceOptionValExample example);

    /**
     * 根据业务主键查询未被删除的数据
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_sx_service_option_val
     *
     * @mbggenerated
     */
    DbSxServiceOptionVal selectByOid(String oid);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_sx_service_option_val
     *
     * @mbggenerated
     */
    int updateByExampleSelective(@Param("record") DbSxServiceOptionVal record, @Param("example") DbSxServiceOptionValExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_sx_service_option_val
     *
     * @mbggenerated
     */
    int updateByExample(@Param("record") DbSxServiceOptionVal record, @Param("example") DbSxServiceOptionValExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_sx_service_option_val
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(DbSxServiceOptionVal record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_sx_service_option_val
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(DbSxServiceOptionVal record);

    // 根据titleOid查询
    List<DbSxServiceOptionVal> getSxServiceOptionValByTitleOid(String titleOid);
}