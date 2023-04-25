package com.zfsoft.service.dbaccess.dao.sxService;

import com.zfsoft.service.dbaccess.data.sxService.DbSxServiceLinkSpecial;
import com.zfsoft.service.dbaccess.data.sxService.DbSxServiceLinkSpecialExample;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
@Mapper
public interface DbSxServiceLinkSpecialMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_sx_service_link_special
     *
     * @mbggenerated
     */
    int countByExample(DbSxServiceLinkSpecialExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_sx_service_link_special
     *
     * @mbggenerated
     */
    int deleteByExample(DbSxServiceLinkSpecialExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_sx_service_link_special
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_sx_service_link_special
     *
     * @mbggenerated
     */
    int insert(DbSxServiceLinkSpecial record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_sx_service_link_special
     *
     * @mbggenerated
     */
    int insertSelective(DbSxServiceLinkSpecial record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_sx_service_link_special
     *
     * @mbggenerated
     */
    List<DbSxServiceLinkSpecial> selectByExample(DbSxServiceLinkSpecialExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_sx_service_link_special
     *
     * @mbggenerated
     */
    DbSxServiceLinkSpecial selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_sx_service_link_special
     *
     * @mbggenerated
     */
    int updateByExampleSelective(@Param("record") DbSxServiceLinkSpecial record, @Param("example") DbSxServiceLinkSpecialExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_sx_service_link_special
     *
     * @mbggenerated
     */
    int updateByExample(@Param("record") DbSxServiceLinkSpecial record, @Param("example") DbSxServiceLinkSpecialExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_sx_service_link_special
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(DbSxServiceLinkSpecial record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_sx_service_link_special
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(DbSxServiceLinkSpecial record);
}