package com.zfsoft.microservice.platform.dbaccess.dao.sys;

import com.zfsoft.microservice.platform.dbaccess.data.sys.DbSysPermissionLink;
import com.zfsoft.microservice.platform.dbaccess.data.sys.DbSysPermissionLinkExample;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
@Mapper
public interface DbSysPermissionLinkMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_sys_permission_link
     *
     * @mbggenerated
     */
    int countByExample(DbSysPermissionLinkExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_sys_permission_link
     *
     * @mbggenerated
     */
    int deleteByExample(DbSysPermissionLinkExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_sys_permission_link
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_sys_permission_link
     *
     * @mbggenerated
     */
    int insert(DbSysPermissionLink record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_sys_permission_link
     *
     * @mbggenerated
     */
    int insertSelective(DbSysPermissionLink record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_sys_permission_link
     *
     * @mbggenerated
     */
    List<DbSysPermissionLink> selectByExample(DbSysPermissionLinkExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_sys_permission_link
     *
     * @mbggenerated
     */
    DbSysPermissionLink selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_sys_permission_link
     *
     * @mbggenerated
     */
    int updateByExampleSelective(@Param("record") DbSysPermissionLink record, @Param("example") DbSysPermissionLinkExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_sys_permission_link
     *
     * @mbggenerated
     */
    int updateByExample(@Param("record") DbSysPermissionLink record, @Param("example") DbSysPermissionLinkExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_sys_permission_link
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(DbSysPermissionLink record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_sys_permission_link
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(DbSysPermissionLink record);

    /**
     * 根据权限删除权限链接
     *
     * @param permissionOid 权限主键
     * @return
     * @author zxx
     * @date 2020/9/10 4:26 下午
     */
    @Delete({
            "delete from T_SYS_PERMISSION_LINK",
            "where PERMISSION_OID = #{permissionOid,jdbcType=VARCHAR}"
    })
    int deleteByPermissionOid(String permissionOid);
}