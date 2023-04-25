package com.zfsoft.microservice.platform.manager.sys;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zfsoft.microservice.platform.data.sys.*;
import com.zfsoft.microservice.platform.dbaccess.dao.sys.DbSysAppMapper;
import com.zfsoft.microservice.platform.dbaccess.dao.sys.DbSysPermissionMapper;
import com.zfsoft.microservice.platform.dbaccess.dao.sys.DbSysRoleMapper;
import com.zfsoft.microservice.platform.dbaccess.data.sys.DbSysApp;
import com.zfsoft.microservice.platform.dbaccess.data.sys.DbSysRole;
import com.zfsoft.microservice.platform.dbaccess.data.sys.DbSysRoleExample;
import com.zfsoft.microservice.platform.util.GenDataTreeUtil;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.data.BaseStaticParameter;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.platform.utils.validate.ParamValid;
import com.zfsoft.platform.utils.validate.ParamValidException;
import com.zfsoft.platform.utils.validate.ResultInfoException;
import com.zfsoft.platform.utils.validate.ValidGroups;
import com.zfsoft.platform.utils.web.TreeSelect;
import org.apache.ibatis.cache.NullCacheKey;
import org.apache.shardingsphere.transaction.annotation.ShardingTransactionType;
import org.apache.shardingsphere.transaction.core.TransactionType;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 角色service实现
 *
 * @author zxx
 * @date 2020/9/12 10:58 上午
 */
@CacheConfig(cacheNames = "sys:role")
@Service
public class SysRoleManager {

    @Resource
    private DbSysRoleMapper dbSysRoleMapper;

    @Resource
    private DbSysAppMapper dbSysAppMapper;

    @Resource
    private DbSysPermissionMapper dbSysPermissionMapper;

    @Autowired
    private SysRolePermissionManager sysRolePermissionManager;

    @Autowired
    private SysLoginRoleManager sysLoginRoleManager;

    @Autowired
    private SysLoginManager sysLoginManager;

    @Autowired
    private SysAppManager sysAppManager;

    @Cacheable( key = "'getSysRoleById:'+#oid", unless = "#result == null")
    public SysRole getSysRoleById(Long oid) {
        if (null == oid) {
            return null;
        }
        DbSysRole dbSysRole = dbSysRoleMapper.selectByPrimaryKey(oid);
        SysRole sysRole = new SysRole();
        BeanUtils.copyProperties(dbSysRole, sysRole);
        if(StrUtil.isNotEmpty(sysRole.getAppOid())){
            SysApp app = sysAppManager.getSysAppByAppOid(sysRole.getAppOid());
            sysRole.setAppName(null!=app?app.getName():null);
        }
        return sysRole;
    }

    @Cacheable( key = "'getSysRoleByRoleOid:'+#roleOid", unless = "#result == null")
    public SysRole getSysRoleByRoleOid(String roleOid) {
        if (null == roleOid) {
            return null;
        }
        DbSysRole dbSysRole = dbSysRoleMapper.selectByRoleOid(roleOid);
        SysRole sysRole = new SysRole();
        BeanUtils.copyProperties(dbSysRole, sysRole);
        if(StrUtil.isNotEmpty(sysRole.getAppOid())){
            SysApp app = sysAppManager.getSysAppByAppOid(sysRole.getAppOid());
            sysRole.setAppName(null!=app?app.getName():null);
        }
        return sysRole;
    }

    public PageResult<SysRole> querySysRoleWithPage(SysRole sysRole, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        DbSysRoleExample dbSysRoleExample = buildDbSysRoleExample(sysRole);
        Page<DbSysRole> dbSysRolePage = (Page<DbSysRole>) dbSysRoleMapper.selectSysRolePageByExample(dbSysRoleExample);
        PageResult<SysRole> pageResult = new PageResult<>(dbSysRolePage.getPageNum(), dbSysRolePage.getPageSize(), dbSysRolePage.getTotal());
        List<SysRole> sysRoleList = dbSysRolePage.stream().map(dbSysRole -> {
            SysRole role = new SysRole();
            BeanUtils.copyProperties(dbSysRole, role);
            if(StrUtil.isNotEmpty(role.getAppOid())){
                SysApp app = sysAppManager.getSysAppByAppOid(role.getAppOid());
                role.setAppName(null!=app?app.getName():null);
            }
            return role;
        }).collect(Collectors.toList());
        pageResult.setData(sysRoleList);
        return pageResult;
    }


    public List<SysRole> querySysRole(SysRole sysRole) {
        DbSysRoleExample dbSysRoleExample = buildDbSysRoleExampleNoAble(sysRole);
        List<DbSysRole> dbSysRoles = dbSysRoleMapper.selectByExample(dbSysRoleExample);
        List<SysRole> sysRoleList = dbSysRoles.stream().map(dbSysRole -> {
            SysRole role = new SysRole();
            BeanUtils.copyProperties(dbSysRole, role);
            return role;
        }).collect(Collectors.toList());
        return sysRoleList;
    }

    @CacheEvict( key = "'getSysRoleById:'+#oid")
    public ApiResultSet ableSysRole(Long oid) {
        ApiResultSet apiResultSet = new ApiResultSet(ApiResultSet.DIRTY_DATA_TITLE);
        DbSysRole dbSysRole = dbSysRoleMapper.selectByPrimaryKey(oid);
        if (dbSysRole != null) {
            if (BaseStaticParameter.Y == dbSysRole.getIsAble()) {
                //禁用之前判断当前角色是否有用户角色已经存在
                if(sysLoginRoleManager.checkUserByRoleOid(dbSysRole.getRoleOid(),BaseStaticParameter.YES)){
                    throw new ResultInfoException("禁用失败，该角色存在授权用户信息！");
                }
                dbSysRole.setIsAble(BaseStaticParameter.N);
            } else {
                // 启用之前判断角色是否存在禁用的应用
                DbSysApp dbSysApp = dbSysAppMapper.selectDbSysAppByAppOid(dbSysRole.getAppOid());
                if (dbSysApp != null && BaseStaticParameter.N==dbSysApp.getIsAble()){
                    throw new ResultInfoException("启用失败，该角色所属应用被禁用！");
                }
                // 启用之前判断角色是否存在禁用的权限
                if (dbSysPermissionMapper.selectNotAblePermissionByRoleOid(dbSysRole.getRoleOid()) > 0){
                    throw new ResultInfoException("启用失败，该角色菜单权限被禁用");
                }
                dbSysRole.setIsAble(BaseStaticParameter.Y);
            }
            dbSysRoleMapper.updateByPrimaryKey(dbSysRole);
            apiResultSet.setCode(ApiResultSet.SUCCESS);
            return apiResultSet;
        } else {
            // 否则给予提示
            throw new ResultInfoException("没有此角色信息，操作失败！");
        }
    }


    @Transactional
    @ShardingTransactionType(TransactionType.LOCAL)
    @CacheEvict( key = "'getSysRoleById:'+#oid")
    public void deleteSysRole(Long oid) {
        DbSysRole dbSysRole = dbSysRoleMapper.selectByPrimaryKey(oid);
        if (dbSysRole != null) {
            //删除之前判断当前角色是否有用户角色已经存在
            if(sysLoginRoleManager.checkUserByRoleOid(dbSysRole.getRoleOid(),null)){
                throw new ResultInfoException("当前角色信息存在授权用户信息，无法删除！");
            }
            //查询用户判断
            sysRolePermissionManager.deleteByRoleOid(dbSysRole.getRoleOid());
            dbSysRole.setIsDelete(BaseStaticParameter.Y);
            dbSysRoleMapper.updateByPrimaryKey(dbSysRole);
        } else {
            // 否则给予提示
            throw new ParamValidException("没有此角色信息，操作失败！");
        }
    }


    @ParamValid
    @Transactional
    @ShardingTransactionType(TransactionType.LOCAL)
    @CacheEvict(cacheNames = {"sys:role","sys:permission"},allEntries = true)
    public int saveSysRole(@ValidGroups(groups = {SysRole.INSERT_GROUP.class})SysRole sysRole) {
        if (sysRole == null) {
            throw new ParamValidException("角色信息不正确!");
        }
        if (null==sysRole.getId()) {
            sysRole.setId(null);
            sysRole.setRoleOid(IdUtil.simpleUUID());
        } else {
            DbSysRole dbSysRole = dbSysRoleMapper.selectByPrimaryKey(sysRole.getId());
            // 验证是否存在角色信息
            if (dbSysRole == null) {
                throw new ParamValidException("角色信息编号未查询到相应的角色信息");
            }
        }
        // 当启禁用状态为空或在不正确时，设置为启用状态
        if (null == sysRole.getIsAble() || !BaseStaticParameter.ABLE_MAP.containsKey(sysRole.getIsAble())) {
            sysRole.setIsAble(BaseStaticParameter.Y);
        }
        sysRole.setIsDelete(BaseStaticParameter.N);
        DbSysRole dbSysRole = new DbSysRole();
        BeanUtils.copyProperties(sysRole, dbSysRole);
        if (null == sysRole.getId()) {
            dbSysRoleMapper.insert(dbSysRole);
        } else {
            dbSysRoleMapper.updateByPrimaryKey(dbSysRole);
        }
        if(sysRole.getPermissionIds()!=null&&sysRole.getPermissionIds().length>0){
            //System.out.println(sysRole.getPermissionIds().length);
            sysRolePermissionManager.deleteByRoleOid(dbSysRole.getRoleOid());
            for (String permissionId : sysRole.getPermissionIds()) {
                SysRolePermission sysRolePermission=new SysRolePermission();
                sysRolePermission.setPermissionOid(permissionId);
                sysRolePermission.setRoleOid(dbSysRole.getRoleOid());
                sysRolePermission.setModifyDate(new Date());
                sysRolePermissionManager.saveSysRolePermission(sysRolePermission);
            }
        }
        return 0;
    }


    public int saveRoleUser(SysRole sysRole) {
        if(sysRole.getId()==null){
            throw new ParamValidException("角色信息无效！");
        }
       /* if(sysRole.getUserOids()==null||sysRole.getUserOids().size()==0){
            throw new ParamValidException("没有有效的用户信息！");
        }*/
        sysLoginRoleManager.deleteByRoleOid(sysRole.getRoleOid());
        //List<SysRole> sysRoles = sysRoleService.querySysRole(null);
        List<String> userOids = sysRole.getUserOids();
        userOids = removeDuplicate(userOids);
        userOids.forEach(userOid->{
            SysLogin login = sysLoginManager.getSysLoginByUserOid(userOid);
            SysLoginRole sysLoginRole=new SysLoginRole();
            sysLoginRole.setLoginOid(login.getLoginOid());
            sysLoginRole.setRoleOid(sysRole.getRoleOid());
            sysLoginRole.setAppOid(sysRole.getAppOid());
            sysLoginRole.setLoginRoleOid(IdUtil.simpleUUID());
            sysLoginRoleManager.saveSysLoginRole(sysLoginRole);
        });
        return 0;
    }


    /**
     * @description: list去重
     * @author: wuxx
     * @Date: 2021/1/12 9:22
     **/
    public static List removeDuplicate(List list) {
        HashSet h = new HashSet(list);
        list.clear();
        list.addAll(h);
        return list;
    }

    @Cacheable(key = "'getSysRolesByLoginId:'+#oid", unless = "#result == null")
    public List<SysRole> getSysRolesByLoginId(String oid) {
        List<DbSysRole> dbSysRoles = dbSysRoleMapper.selectSysRoleByLoginOid(oid);
        List<SysRole> sysRoleList = dbSysRoles.stream().map(dbSysRole -> {
            SysRole role = new SysRole();
            BeanUtils.copyProperties(dbSysRole, role);
            return role;
        }).collect(Collectors.toList());
        return sysRoleList;
    }

    /**
     * 构建查询条件
     *
     * @param sysRole 角色
     * @return
     * @author zxx
     * @date 2020/9/12 11:51 上午
     */
    protected DbSysRoleExample buildDbSysRoleExample(SysRole sysRole) {
        DbSysRoleExample dbSysRoleExample = new DbSysRoleExample();
        DbSysRoleExample.Criteria criteria = dbSysRoleExample.createCriteria();
        if (sysRole != null) {
            if(StrUtil.isNotEmpty(sysRole.getAppOid())){
                criteria.andAppOidEqualTo(sysRole.getAppOid());
            }
            if(StrUtil.isNotEmpty(sysRole.getName())){
                //criteria.andNameLike(StrUtil.format("{}%",sysRole.getName()));
                criteria.andNameLike("%"+sysRole.getName().trim()+"%");
            }
            if(null!=sysRole.getIsAble()){
                criteria.andIsAbleEqualTo(sysRole.getIsAble());
            }
        }
        criteria.andIsDeleteEqualTo(BaseStaticParameter.N);
        dbSysRoleExample.setOrderByClause("sort asc");
        return dbSysRoleExample;
    }

   /**
    * @description:   构建查询条件 不包含已经禁用的
    * @param sysRole
    * @author: wuxx
    * @Date: 2020/11/13 13:19
    **/
    protected DbSysRoleExample buildDbSysRoleExampleNoAble(SysRole sysRole) {
        DbSysRoleExample dbSysRoleExample = new DbSysRoleExample();
        DbSysRoleExample.Criteria criteria = dbSysRoleExample.createCriteria();
        if (sysRole != null) {
            if(StrUtil.isNotEmpty(sysRole.getAppOid())){
                criteria.andAppOidEqualTo(sysRole.getAppOid());
            }
            if(StrUtil.isNotEmpty(sysRole.getName())){
                //criteria.andNameLike(StrUtil.format("{}%",sysRole.getName()));
                criteria.andNameLike("%"+sysRole.getName().trim()+"%");
            }
            if(null!=sysRole.getIsAble()){
                criteria.andIsAbleEqualTo(sysRole.getIsAble());
            }
        }
        criteria.andIsDeleteEqualTo(BaseStaticParameter.N);
        criteria.andIsAbleEqualTo(BaseStaticParameter.Y);
        dbSysRoleExample.setOrderByClause("sort asc");
        return dbSysRoleExample;
    }

    @Cacheable( key = "'queryAppRoleTree'", unless = "#result == null")
    public List<TreeSelect> queryAppRoleTree() {
        List<SysApp> sysApps = sysAppManager.querySysApp(null);
        List<SysRole> sysRoles = this.querySysRole(null);
        List<TreeSelect> treeSelects = GenDataTreeUtil.buildAppAndRoleTreeSelect(sysApps, sysRoles,true);
        treeSelects = GenDataTreeUtil.buildDisabledLastTree(treeSelects,GenDataTreeUtil.TYPE_ROLE);
        return treeSelects;
    }
}
