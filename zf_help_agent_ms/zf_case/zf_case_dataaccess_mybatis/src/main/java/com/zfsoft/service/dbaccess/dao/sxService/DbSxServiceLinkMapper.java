package com.zfsoft.service.dbaccess.dao.sxService;

import com.zfsoft.service.dbaccess.data.sxService.DbSxServiceLink;
import com.zfsoft.service.dbaccess.data.sxService.DbSxServiceLinkExample;
import com.zfsoft.service.dbaccess.data.sxService.DbSxServiceLinkWithBLOBs;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
@Mapper
public interface DbSxServiceLinkMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_sx_service_link
     *
     * @mbggenerated
     */
    int countByExample(DbSxServiceLinkExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_sx_service_link
     *
     * @mbggenerated
     */
    int deleteByExample(DbSxServiceLinkExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_sx_service_link
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_sx_service_link
     *
     * @mbggenerated
     */
    int insert(DbSxServiceLinkWithBLOBs record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_sx_service_link
     *
     * @mbggenerated
     */
    int insertSelective(DbSxServiceLinkWithBLOBs record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_sx_service_link
     *
     * @mbggenerated
     */
    List<DbSxServiceLinkWithBLOBs> selectByExampleWithBLOBs(DbSxServiceLinkExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_sx_service_link
     *
     * @mbggenerated
     */
    List<DbSxServiceLink> selectByExample(DbSxServiceLinkExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_sx_service_link
     *
     * @mbggenerated
     */
    DbSxServiceLinkWithBLOBs selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_sx_service_link
     *
     * @mbggenerated
     */
    int updateByExampleSelective(@Param("record") DbSxServiceLinkWithBLOBs record, @Param("example") DbSxServiceLinkExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_sx_service_link
     *
     * @mbggenerated
     */
    int updateByExampleWithBLOBs(@Param("record") DbSxServiceLinkWithBLOBs record, @Param("example") DbSxServiceLinkExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_sx_service_link
     *
     * @mbggenerated
     */
    int updateByExample(@Param("record") DbSxServiceLink record, @Param("example") DbSxServiceLinkExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_sx_service_link
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(DbSxServiceLinkWithBLOBs record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_sx_service_link
     *
     * @mbggenerated
     */
    int updateByPrimaryKeyWithBLOBs(DbSxServiceLinkWithBLOBs record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_sx_service_link
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(DbSxServiceLink record);

    /**
     * 根据业务主健获取对象
     * @param oid
     * @return
     */
    DbSxServiceLinkWithBLOBs getSxServiceLinkByOid(String oid);

    List<DbSxServiceLinkWithBLOBs> queryDbSxServiceLinkList(DbSxServiceLinkWithBLOBs dbSxServiceLinkWithBLOBs);
}
