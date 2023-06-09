package com.zfsoft.service.dbaccess.dao.sxService;

import com.zfsoft.service.dbaccess.data.sxService.DbSxServiceLocation;
import com.zfsoft.service.dbaccess.data.sxService.DbSxServiceLocationExample;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
@Mapper
public interface DbSxServiceLocationMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_sx_service_location
     *
     * @mbggenerated
     */
    int countByExample(DbSxServiceLocationExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_sx_service_location
     *
     * @mbggenerated
     */
    int deleteByExample(DbSxServiceLocationExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_sx_service_location
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_sx_service_location
     *
     * @mbggenerated
     */
    int insert(DbSxServiceLocation record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_sx_service_location
     *
     * @mbggenerated
     */
    int insertSelective(DbSxServiceLocation record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_sx_service_location
     *
     * @mbggenerated
     */
    List<DbSxServiceLocation> selectByExampleWithBLOBs(DbSxServiceLocationExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_sx_service_location
     *
     * @mbggenerated
     */
    List<DbSxServiceLocation> selectByExample(DbSxServiceLocationExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_sx_service_location
     *
     * @mbggenerated
     */
    DbSxServiceLocation selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_sx_service_location
     *
     * @mbggenerated
     */
    int updateByExampleSelective(@Param("record") DbSxServiceLocation record, @Param("example") DbSxServiceLocationExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_sx_service_location
     *
     * @mbggenerated
     */
    int updateByExampleWithBLOBs(@Param("record") DbSxServiceLocation record, @Param("example") DbSxServiceLocationExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_sx_service_location
     *
     * @mbggenerated
     */
    int updateByExample(@Param("record") DbSxServiceLocation record, @Param("example") DbSxServiceLocationExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_sx_service_location
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(DbSxServiceLocation record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_sx_service_location
     *
     * @mbggenerated
     */
    int updateByPrimaryKeyWithBLOBs(DbSxServiceLocation record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_sx_service_location
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(DbSxServiceLocation record);

    DbSxServiceLocation getDbSxServiceLocationByOid(String oid);

    List<DbSxServiceLocation> queryDbSxServiceLocationList(DbSxServiceLocation dbSxServiceLocation);
}
