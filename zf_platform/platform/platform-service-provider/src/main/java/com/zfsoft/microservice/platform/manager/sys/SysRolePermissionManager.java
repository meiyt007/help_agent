package com.zfsoft.microservice.platform.manager.sys;

import com.zfsoft.microservice.platform.data.sys.SysRolePermission;
import com.zfsoft.microservice.platform.dbaccess.dao.sys.DbSysRolePermissionMapper;
import com.zfsoft.microservice.platform.dbaccess.data.sys.DbSysRolePermission;
import com.zfsoft.microservice.platform.dbaccess.data.sys.DbSysRolePermissionExample;
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
 * 角色权限service实现
 *
 * @author zxx
 * @date 2020/9/12 10:59 上午
 */
@CacheConfig(cacheNames = "sys:rolePermission")
@Service
public class SysRolePermissionManager{

    @Resource
    private DbSysRolePermissionMapper dbSysRolePermissionMapper;

    @CacheEvict(allEntries = true)
    public int saveSysRolePermission(SysRolePermission sysRolePermission) {
        if (null == sysRolePermission.getRoleOid()) {
            throw new ParamValidException("角色信息验证无效");
        }
        if (null == sysRolePermission.getPermissionOid()) {
            throw new ParamValidException("权限信息验证无效");
        }
        sysRolePermission.setModifyDate(new Date());
        DbSysRolePermission dbSysRolePermission = new DbSysRolePermission();
        BeanUtils.copyProperties(sysRolePermission, dbSysRolePermission);
        if (null == dbSysRolePermission.getId()) {
            return dbSysRolePermissionMapper.insert(dbSysRolePermission);
        } else {
            return dbSysRolePermissionMapper.updateByPrimaryKey(dbSysRolePermission);
        }
    }

    @Cacheable( key = "'getSysRolePermissionById:'+#oid", unless = "#result == null")
    public SysRolePermission getSysRolePermissionById(Long oid) {
        if (null == oid) {
            return null;
        }
        DbSysRolePermission dbSysRolePermission = dbSysRolePermissionMapper.selectByPrimaryKey(oid);
        if (dbSysRolePermission == null) {
            return null;
        }
        SysRolePermission sysRolePermission = new SysRolePermission();
        BeanUtils.copyProperties(dbSysRolePermission, sysRolePermission);
        return sysRolePermission;
    }

    @Cacheable( key = "'querySysRolePermissionByRoleOid:'+#roleOid", unless = "#result == null")
    public List<SysRolePermission> querySysRolePermissionByRoleOid(String roleOid) {
        DbSysRolePermissionExample dbSysRolePermissionExample = new DbSysRolePermissionExample();
        DbSysRolePermissionExample.Criteria criteria = dbSysRolePermissionExample.createCriteria();
        criteria.andRoleOidEqualTo(roleOid);
        List<DbSysRolePermission> dbSysRolePermissions = dbSysRolePermissionMapper.selectByExample(dbSysRolePermissionExample);
        List<SysRolePermission> collect = dbSysRolePermissions.stream().map(dbSysRolePermission -> {
            SysRolePermission sysRolePermission = new SysRolePermission();
            BeanUtils.copyProperties(dbSysRolePermission, sysRolePermission);
            return sysRolePermission;
        }).collect(Collectors.toList());
        return collect;
    }

    @CacheEvict(allEntries = true)
    public int deleteByRoleOid(String roleOid) {
        if (null != roleOid) {
            dbSysRolePermissionMapper.deleteByRoleOid(roleOid);
        }
        return 0;
    }


    public List<String> queryPermissionByRoleOid(String roleOid) {
        List<SysRolePermission> sysRolePermissions = this.querySysRolePermissionByRoleOid(roleOid);
        List<String> longList = sysRolePermissions.stream().map(sysRolePermission -> {
            return sysRolePermission.getPermissionOid();
        }).collect(Collectors.toList());
        return longList;
    }

}
