package com.zfsoft.microservice.platform.manager.sys;

import cn.hutool.core.util.StrUtil;
import com.zfsoft.microservice.platform.data.sys.SysLoginRole;
import com.zfsoft.microservice.platform.data.vo.SysLoginRoleVo;
import com.zfsoft.microservice.platform.dbaccess.dao.sys.DbSysLoginRoleMapper;
import com.zfsoft.microservice.platform.dbaccess.data.sys.DbSysLoginRole;
import com.zfsoft.microservice.platform.dbaccess.data.sys.DbSysLoginRoleExample;
import com.zfsoft.microservice.platform.dbaccess.vo.DBSysLoginRoleVo;
import com.zfsoft.platform.utils.validate.ParamValidException;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 登录角色serivce实现
 * @author zxx
 * @date 2020/9/15 9:52 上午
 */
@Service
@CacheConfig(cacheNames = "sys:loginRole")
public class SysLoginRoleManager{

    @Resource
    private DbSysLoginRoleMapper dbSysLoginRoleMapper;

    @Cacheable( key = "'getSysLoginRoleById:'+#oid", unless = "#result == null")
    public SysLoginRole getSysLoginRoleById(Long oid) {
        if(null==oid){
            return null;
        }
        DbSysLoginRole dbSysLoginRole = dbSysLoginRoleMapper.selectByPrimaryKey(oid);
        SysLoginRole sysLoginRole=new SysLoginRole();
        BeanUtils.copyProperties(dbSysLoginRole,sysLoginRole);
        return sysLoginRole;
    }

    public List<SysLoginRole> querySysLoginRole(SysLoginRole sysLoginRole) {
        DbSysLoginRoleExample dbSysLoginRoleExample = buildDbSysLoginRoleExample(sysLoginRole);
        List<DbSysLoginRole> dbSysLoginRoles = dbSysLoginRoleMapper.selectByExample(dbSysLoginRoleExample);
        List<SysLoginRole> sysPermissionLinkList=dbSysLoginRoles.stream().map(dbSysLoginRole -> {
            SysLoginRole sysPermissionLink = new SysLoginRole();
            BeanUtils.copyProperties(dbSysLoginRole,sysPermissionLink);
            return sysPermissionLink;
        }).collect(Collectors.toList());
        return sysPermissionLinkList;
    }

    /**
     * @description: 根据登录oid获取未删除未禁用的角色role列表
     * @param loginOid 登录oid
     * @author: wuxx
     * @Date: 2020/11/16 10:31
     **/
    public List<SysLoginRole> querySysLoginRoleByLoginOid(String loginOid) {
        List<DbSysLoginRole> dbSysLoginRoles = dbSysLoginRoleMapper.querySysLoginRoleByLoginOid(loginOid);
        List<SysLoginRole> sysLoginRoleList =dbSysLoginRoles.stream().map(dbSysLoginRole -> {
            SysLoginRole role = new SysLoginRole();
            BeanUtils.copyProperties(dbSysLoginRole,role);
            return role;
        }).collect(Collectors.toList());
        return sysLoginRoleList;
    }

    /**
     * @description: 根据登录oid获取未删除未禁用的角色role列表
     * @param seclectRole
     * @author: wuxx
     * @Date: 2021/1/11 17:31
     **/
    public List<SysLoginRole> querySeclectSysLoginRole(SysLoginRole seclectRole) {
        DbSysLoginRoleExample dbSysLoginRoleExample=new DbSysLoginRoleExample();
        DbSysLoginRoleExample.Criteria criteria = dbSysLoginRoleExample.createCriteria();
        criteria.andAppOidEqualTo(seclectRole.getAppOid());
        criteria.andLoginOidEqualTo(seclectRole.getLoginOid());
        criteria.andRoleOidEqualTo(seclectRole.getRoleOid());
        List<DbSysLoginRole> dbSysLoginRoles = dbSysLoginRoleMapper.selectByExample(dbSysLoginRoleExample);
        List<SysLoginRole> sysLoginRoleList =dbSysLoginRoles.stream().map(dbSysLoginRole -> {
            SysLoginRole role = new SysLoginRole();
            BeanUtils.copyProperties(dbSysLoginRole,role);
            return role;
        }).collect(Collectors.toList());
        return sysLoginRoleList;
    }

    @Cacheable( key = "'querySysLoginRoleVoByLoginOid:'+#loginOid", unless = "#result == null")
    public List<SysLoginRoleVo> querySysLoginRoleVoByLoginOid(String loginOid) {
        List<DBSysLoginRoleVo> dbSysLoginRoleVos = dbSysLoginRoleMapper.selectByDBSysLoginRoleVo(loginOid);
        List<SysLoginRoleVo> sysLoginRoleVoList =dbSysLoginRoleVos.stream().map(dbSysLoginRoleVo -> {
            SysLoginRoleVo roleVo = new SysLoginRoleVo();
            BeanUtils.copyProperties(dbSysLoginRoleVo,roleVo);
            return roleVo;
        }).collect(Collectors.toList());
        return sysLoginRoleVoList;
    }

    @CacheEvict(allEntries = true)
    public void deleteSysLoginRole(Long oid) {
        if(null!=oid){
            dbSysLoginRoleMapper.deleteByPrimaryKey(oid);
        }
    }

    @CacheEvict(allEntries = true)
    public int saveSysLoginRole(SysLoginRole sysLoginRole) {
        if(null==sysLoginRole){
            throw new ParamValidException("角色信息验证有误！");
        }
        if(null==sysLoginRole.getRoleOid()){
            throw new ParamValidException("角色信息验证有误！");
        }
        if(null==sysLoginRole.getLoginOid()){
            throw new ParamValidException("角色信息验证有误！");
        }
        DbSysLoginRole dbSysLoginRole=new DbSysLoginRole();
        BeanUtils.copyProperties(sysLoginRole,dbSysLoginRole);
        dbSysLoginRole.setModifyDate(new Date());
        if(null==dbSysLoginRole.getId()){
            dbSysLoginRoleMapper.insert(dbSysLoginRole);
        }else{
            dbSysLoginRoleMapper.updateByPrimaryKey(dbSysLoginRole);
        }
        return 0;
    }

    @CacheEvict(allEntries = true)
    public void deleteByLoginOid(String loginOid) {
        if(null!=loginOid){
            dbSysLoginRoleMapper.deleteByLoginOid(loginOid);
        }
    }

    @CacheEvict(allEntries = true)
    public void deleteByRoleOid(String roleOid) {
        if(StrUtil.isNotEmpty(roleOid)){
            dbSysLoginRoleMapper.deleteByRoleOid(roleOid);
        }
    }

    /**
     * @description:  判断当前角色是否有用户角色已经存在
     * @param roleOid 角色oid
     * @author: wuxx
     * @Date: 2020/11/12 17:13
     **/
    public boolean checkUserByRoleOid(String roleOid,String ableFlag) {
        if(null!=roleOid){
            int index = dbSysLoginRoleMapper.checkUserByRoleOid(roleOid,ableFlag);
            if(index>0){
                return true;
            }
        }
        return false;
    }

    /**
     * 构建查询条件
     * @author zxx
     * @date 2020/9/15 9:59 上午
     * @param sysLoginRole 登录角色
     * @return DbSysLoginRoleExample
     */
    private DbSysLoginRoleExample buildDbSysLoginRoleExample(SysLoginRole sysLoginRole){
        DbSysLoginRoleExample dbSysLoginRoleExample=new DbSysLoginRoleExample();
        DbSysLoginRoleExample.Criteria criteria = dbSysLoginRoleExample.createCriteria();
        if (sysLoginRole != null) {
            if (null != sysLoginRole.getAppOid()) {
                criteria.andAppOidEqualTo(sysLoginRole.getAppOid());
            }
            if (null != sysLoginRole.getLoginOid()) {
                criteria.andLoginOidEqualTo(sysLoginRole.getLoginOid());
            }
            if (null != sysLoginRole.getRoleOid()) {
                criteria.andRoleOidEqualTo(sysLoginRole.getRoleOid());
            }
        }
        return dbSysLoginRoleExample;
    }
}
