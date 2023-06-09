package com.zfsoft.microservice.platform.dbaccess.dao.sys;

import com.zfsoft.microservice.platform.dbaccess.data.sys.DbSysRolePermission;
import com.zfsoft.microservice.platform.dbaccess.data.sys.DbSysRolePermissionExample;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
@Mapper
public interface DbSysRolePermissionMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_sys_role_permission
     *
     * @mbggenerated
     */
    int countByExample(DbSysRolePermissionExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_sys_role_permission
     *
     * @mbggenerated
     */
    int deleteByExample(DbSysRolePermissionExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_sys_role_permission
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_sys_role_permission
     *
     * @mbggenerated
     */
    int insert(DbSysRolePermission record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_sys_role_permission
     *
     * @mbggenerated
     */
    int insertSelective(DbSysRolePermission record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_sys_role_permission
     *
     * @mbggenerated
     */
    List<DbSysRolePermission> selectByExample(DbSysRolePermissionExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_sys_role_permission
     *
     * @mbggenerated
     */
    DbSysRolePermission selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_sys_role_permission
     *
     * @mbggenerated
     */
    int updateByExampleSelective(@Param("record") DbSysRolePermission record, @Param("example") DbSysRolePermissionExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_sys_role_permission
     *
     * @mbggenerated
     */
    int updateByExample(@Param("record") DbSysRolePermission record, @Param("example") DbSysRolePermissionExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_sys_role_permission
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(DbSysRolePermission record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_sys_role_permission
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(DbSysRolePermission record);

    /**
     * 根据角色删除角色权限
     * @author zxx
     * @date 2020/9/12 11:21 上午
     * @param roleOid 角色主键
     * @return
     */
    @Delete({
            "delete from T_SYS_ROLE_PERMISSION",
            "where ROLE_OID = #{roleOid,jdbcType=VARCHAR}"
    })
    int deleteByRoleOid(String roleOid);

    /**
     * 根据权限删除角色权限
     *
     * @param permissionOid
     * @return int
     * @author ningzz
     * @Date 2020/11/16
     **/
    @Delete(" delete from T_SYS_ROLE_PERMISSION where PERMISSION_OID = #{permissionOid,jdbcType=VARCHAR}")
    int deleteByPermissionOid(String permissionOid);
}