package com.zfsoft.microservice.platform.dbaccess.dao.sys;

import com.zfsoft.microservice.platform.dbaccess.data.sys.DbSysRole;
import com.zfsoft.microservice.platform.dbaccess.data.sys.DbSysRoleExample;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;
@Mapper
public interface DbSysRoleMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_sys_role
     *
     * @mbggenerated
     */
    int countByExample(DbSysRoleExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_sys_role
     *
     * @mbggenerated
     */
    int deleteByExample(DbSysRoleExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_sys_role
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_sys_role
     *
     * @mbggenerated
     */
    int insert(DbSysRole record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_sys_role
     *
     * @mbggenerated
     */
    int insertSelective(DbSysRole record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_sys_role
     *
     * @mbggenerated
     */
    List<DbSysRole> selectByExample(DbSysRoleExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_sys_role
     *
     * @mbggenerated
     */
    DbSysRole selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_sys_role
     *
     * @mbggenerated
     */
    int updateByExampleSelective(@Param("record") DbSysRole record, @Param("example") DbSysRoleExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_sys_role
     *
     * @mbggenerated
     */
    int updateByExample(@Param("record") DbSysRole record, @Param("example") DbSysRoleExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_sys_role
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(DbSysRole record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_sys_role
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(DbSysRole record);


    /**
     * 分页查询角色
     * @author zxx
     * @date 2020/9/22 7:49 下午
     * @param dbSysRoleExample
     * @return
     */
    List<DbSysRole> selectSysRolePageByExample(DbSysRoleExample dbSysRoleExample);

    @Select("select role.* from t_sys_login_role lr INNER JOIN t_sys_role role on lr.ROLE_OID=role.ROLE_OID where lr.LOGIN_OID=#{loginOid}")
    List<DbSysRole> selectSysRoleByLoginOid(@Param("loginOid") String loginOid);


    /**
     * @description:  根据业务主键查询实体类
     * @param roleOid 业务主键
     * @author: wuxx
     * @Date: 2020/10/29 9:17
     **/
    DbSysRole selectByRoleOid(String roleOid);

    /**
     * 根据APP_OID查询该应用下是否有未删除且启用状态角色
     *
     * @param appOid
     * @return int
     * @author ningzz
     * @Date 2020/11/12
     **/
    @Select("select count(*) from t_sys_role where IS_DELETE=0 and IS_ABLE=1 and APP_OID=#{appOid}")
    int selectAbleRoleByAppOid(String appOid);

    /**
     * 根据APP_OID查询该应用下是否有未删除的角色
     *
     * @param appOid
     * @return int
     * @author ningzz
     * @Date 2020/11/12
     **/
    @Select("select count(*) from t_sys_role where IS_DELETE=0 and APP_OID=#{appOid}")
    int selectRoleByAppOid(String appOid);


    /**
     * 根据APP_OID逻辑删除角色
     *
     * @param appOid
     * @return int
     * @author ningzz
     * @Date 2020/11/12
     **/
    @Update("update t_sys_role set IS_DELETE=1 where IS_DELETE=0 and APP_OID=#{appOid}")
    int deleteSysRoleByAppOid(String appOid);


    /**
     * 根据PERMISSION_OID查询该权限下是否有未删除且启用状态角色
     *
     * @param permissionOid
     * @return int
     * @author ningzz
     * @Date 2020/11/13
     **/
    @Select("select count(*) from t_sys_role tsr where tsr.IS_DELETE=0 and tsr.IS_ABLE=1 and tsr.ROLE_OID in " +
            "(select tsrp.ROLE_OID from t_sys_role_permission tsrp where tsrp.PERMISSION_OID=#{permissionOid})")
    int selectAbleRoleByPermissionOid(String permissionOid);

    /**
     * 根据PERMISSION_OID查询该权限下是否有未删除的角色
     *
     * @param permissionOid
     * @return int
     * @author ningzz
     * @Date 2020/11/13
     **/
    @Select("select count(*) from t_sys_role tsr where tsr.IS_DELETE=0 and tsr.ROLE_OID in " +
            "(select tsrp.ROLE_OID from t_sys_role_permission tsrp where tsrp.PERMISSION_OID=#{permissionOid})")
    int selectRoleByPermissionOid(String permissionOid);
}