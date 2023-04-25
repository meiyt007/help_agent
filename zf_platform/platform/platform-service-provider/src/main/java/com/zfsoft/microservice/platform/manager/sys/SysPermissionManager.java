package com.zfsoft.microservice.platform.manager.sys;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.pinyin.PinyinUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Maps;
import com.zfsoft.microservice.platform.data.sys.SysApp;
import com.zfsoft.microservice.platform.data.sys.SysPermission;
import com.zfsoft.microservice.platform.data.sys.SysPermissionLink;
import com.zfsoft.microservice.platform.data.vo.MetaVo;
import com.zfsoft.microservice.platform.data.vo.RouterVo;
import com.zfsoft.microservice.platform.dbaccess.dao.sys.DbSysPermissionMapper;
import com.zfsoft.microservice.platform.dbaccess.dao.sys.DbSysRoleMapper;
import com.zfsoft.microservice.platform.dbaccess.dao.sys.DbSysRolePermissionMapper;
import com.zfsoft.microservice.platform.dbaccess.data.sys.DbSysPermission;
import com.zfsoft.microservice.platform.dbaccess.data.sys.DbSysPermissionExample;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.data.BaseStaticParameter;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.platform.utils.validate.ParamValid;
import com.zfsoft.platform.utils.validate.ParamValidException;
import com.zfsoft.platform.utils.validate.ValidGroups;
import org.apache.commons.lang.StringUtils;
import org.apache.shardingsphere.transaction.annotation.ShardingTransactionType;
import org.apache.shardingsphere.transaction.core.TransactionType;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 权限service实现
 *
 * @author zxx
 * @date 2020/9/10 1:09 下午
 */
@Service
@CacheConfig(cacheNames = "sys:permission")
public class SysPermissionManager {

    @Resource
    private DbSysPermissionMapper dbSysPermissionMapper;

    @Resource
    private SysPermissionLinkManager sysPermissionLinkManager;

    @Resource
    private DbSysRolePermissionMapper dbSysRolePermissionMapper;

    @Autowired
    private SysAppManager sysAppManager;

    @Resource
    private DbSysRoleMapper dbSysRoleMapper;

    /**
     * 菜单设置外链接时候的url前置
     **/
    @Value("${zfsoft.out-link-prefix:#{null}}")
    private String outLinkPrefix;


    @ParamValid
    @Transactional
    @ShardingTransactionType(TransactionType.LOCAL)
    @CacheEvict(allEntries = true)
    public SysPermission saveSysPermission(@ValidGroups(groups = {SysPermission.INSERT_GROUP.class}) SysPermission sysPermission) {
        //应用oid判断
        if (null == sysPermission.getParentOid() && null == sysPermission.getAppOid()) {
            throw new ParamValidException("上级权限无效！");
        }
        if (null != sysPermission.getAppOid()) {
            SysApp app = sysAppManager.getSysAppByAppOid(sysPermission.getAppOid());
            if (app == null) {
                throw new ParamValidException("上级权限无效！");
            }
        }
        //
        if (null != sysPermission.getParentOid()) {
            SysPermission permissionParent = this.getSysPermissionByPermissionOid(sysPermission.getParentOid());
            if (permissionParent == null) {
                throw new ParamValidException("上级权限无效！");
            }
            if (permissionParent.getIsAble() == BaseStaticParameter.N) {
                throw new ParamValidException("上级权限被禁用，暂不能操作！");
            }
            // 父等级加一
            sysPermission.setPerLevel(permissionParent.getPerLevel() + 1);
            sysPermission.setAppOid(permissionParent.getAppOid());
        } else {
            sysPermission.setParentOid(null);
            sysPermission.setPerLevel(1);
        }
        //code生成
        if (StrUtil.isEmpty(sysPermission.getCode()) && StrUtil.isNotEmpty(sysPermission.getName())) {
            StringBuffer sb = new StringBuffer("");
            sb.append(PinyinUtil.getFirstLetter(sysPermission.getName(), ""));
            sb.append(System.currentTimeMillis());
            sysPermission.setCode(sb.toString());
        }
        sysPermission.setIsDelete(BaseStaticParameter.N);
        DbSysPermission dbSysPermission = new DbSysPermission();
        BeanUtils.copyProperties(sysPermission, dbSysPermission);
        dbSysPermission.setModifyDate(new Date());
        if (null == dbSysPermission.getId()) {
            dbSysPermission.setIsAble(BaseStaticParameter.Y);
            dbSysPermission.setPermissionOid(IdUtil.simpleUUID());
            dbSysPermissionMapper.insert(dbSysPermission);
        } else {
            dbSysPermissionMapper.updateByPrimaryKey(dbSysPermission);
        }
        //删除权限链接
//        if (null != sysPermission.getOid()) {
//            sysPermissionLinkService.deleteByPermissionOid(sysPermission.getOid());
//        }
//        //保存权限链接
//        if (sysPermission.getChildrenLink() != null)
//            sysPermission.getChildrenLink().forEach(sysPermissionLink -> {
//                sysPermissionLink.setPermissionOid(dbSysPermission.getOid());
//                sysPermissionLinkService.saveSysPermissionLink(sysPermissionLink);
//            });
        BeanUtils.copyProperties(dbSysPermission,sysPermission );
        return sysPermission;
    }

    @Cacheable( key = "'getSysPermissionById:'+#oid", unless = "#result == null")
    public SysPermission getSysPermissionById(Long oid) {
        if (null == oid) {
            return null;
        }
        DbSysPermission dbSysPermission = dbSysPermissionMapper.selectByPrimaryKey(oid);
        if (dbSysPermission == null) {
            return null;
        }
        List<String> permissionOidList = new ArrayList<>();
        permissionOidList.add(dbSysPermission.getPermissionOid());
        List<SysPermissionLink> sysPermissionLinkList = sysPermissionLinkManager.querySysPermissionLinkByPermissionOid(permissionOidList);
        SysPermission sysPermission = new SysPermission();
        BeanUtils.copyProperties(dbSysPermission, sysPermission);
        sysPermission.setChildrenLink(sysPermissionLinkList);
        if(sysPermission.getAppOid()!=null){
            SysApp sysApp = sysAppManager.getSysAppByAppOid(dbSysPermission.getAppOid());
            sysPermission.setAppName(sysApp.getName());
        }
        if(sysPermission.getParentOid()!=null){
            DbSysPermission parent = dbSysPermissionMapper.selectByPermissionOid(sysPermission.getParentOid());
            sysPermission.setParentName(parent.getName());
        }
        return sysPermission;
    }

    @Cacheable( key = "'getSysPermissionByPermissionOid:'+#permissionOid", unless = "#result == null")
    public SysPermission getSysPermissionByPermissionOid(String permissionOid) {
        if (null == permissionOid) {
            return null;
        }
        DbSysPermission dbSysPermission = dbSysPermissionMapper.selectByPermissionOid(permissionOid);
        if (dbSysPermission == null) {
            return null;
        }
        List<String> permissionOidList = new ArrayList<>();
        permissionOidList.add(dbSysPermission.getPermissionOid());
        List<SysPermissionLink> sysPermissionLinkList = sysPermissionLinkManager.querySysPermissionLinkByPermissionOid(permissionOidList);
        SysPermission sysPermission = new SysPermission();
        BeanUtils.copyProperties(dbSysPermission, sysPermission);
        sysPermission.setChildrenLink(sysPermissionLinkList);
        if(sysPermission.getAppOid()!=null){
            SysApp sysApp = sysAppManager.getSysAppByAppOid(dbSysPermission.getAppOid());
            sysPermission.setAppName(sysApp.getName());
        }
        if(sysPermission.getParentOid()!=null){
            DbSysPermission parent = dbSysPermissionMapper.selectByPermissionOid(sysPermission.getParentOid());
            sysPermission.setParentName(parent.getName());
        }
        return sysPermission;
    }


    public PageResult<SysPermission> querySysPermissionWithPage(SysPermission sysPermission, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        DbSysPermissionExample dbSysPermissionExample = buildDbSysPermissionExample(sysPermission);
        if(sysPermission!=null){
            DbSysPermissionExample.Criteria criteria = dbSysPermissionExample.getOredCriteria().get(0);
            if(null==sysPermission.getParentOid()){
                criteria.andParentOidIsNull();
            }else{
                criteria.andParentOidEqualTo(sysPermission.getParentOid());
            }
        }
        Page<DbSysPermission> dbSysPermissionPage = (Page<DbSysPermission>) dbSysPermissionMapper.selectByExample(dbSysPermissionExample);
        PageResult<SysPermission> pageResult = new PageResult<>(dbSysPermissionPage.getPageNum(), dbSysPermissionPage.getPageSize(), dbSysPermissionPage.getTotal());
        List<SysPermission> sysPermissionList = dbSysPermissionPage.stream().map(dbSysPermission -> {
            SysPermission permission = new SysPermission();
            BeanUtils.copyProperties(dbSysPermission, permission);
            return permission;
        }).collect(Collectors.toList());
        pageResult.setData(sysPermissionList);
        return pageResult;
    }


    public List<SysPermission> querySysPermission(SysPermission sysPermission) {
        DbSysPermissionExample dbSysPermissionExample = buildDbSysPermissionExample(sysPermission);
        List<DbSysPermission> dbSysPermissions = dbSysPermissionMapper.selectByExample(dbSysPermissionExample);
        List<SysPermission> sysPermissionList = dbSysPermissions.stream().map(dbSysPermission -> {
            SysPermission permission = new SysPermission();
            BeanUtils.copyProperties(dbSysPermission, permission);
            return permission;
        }).collect(Collectors.toList());
        return sysPermissionList;
    }

    /**
     * 启禁用权限
     *
     * @param id
     * @return ApiResultSet
     * @author ningzz
     * @Date 2020/11/13
     **/
    @CacheEvict( key = "'getSysPermissionById:'+#id")
    public ApiResultSet ableSysPermission(Long id) {
        if (null == id) {
            throw new ParamValidException("无效权限！");
        }
        DbSysPermission dbSysPermission = dbSysPermissionMapper.selectByPrimaryKey(id);
        if (dbSysPermission == null) {
            throw new ParamValidException("无效权限！");
        }
        String dbSysPermissionPermissionOid = dbSysPermission.getPermissionOid();
        ApiResultSet apiResultSet = new ApiResultSet(ApiResultSet.DIRTY_DATA_TITLE);
        SysPermission sysPermission = new SysPermission();
        if (BaseStaticParameter.Y == dbSysPermission.getIsAble()) {
            // 验证是否存在启用状态下级权限
            sysPermission.setParentOid(dbSysPermissionPermissionOid);
            sysPermission.setIsAble(BaseStaticParameter.Y);
            DbSysPermissionExample dbSysPermissionExampleChild = buildDbSysPermissionExample(sysPermission);
            List<DbSysPermission> dbSysPermissionsChild = dbSysPermissionMapper.selectByExample(dbSysPermissionExampleChild);
            if (dbSysPermissionsChild.size() > 0) {
                apiResultSet.setMessage("禁用失败，该权限存在启用状态的下级权限！");
                return apiResultSet;
            }
            // 验证该权限下是否存在启用状态角色
            if (dbSysPermissionPermissionOid != null){
                if (dbSysRoleMapper.selectAbleRoleByPermissionOid(dbSysPermissionPermissionOid) > 0){
                    apiResultSet.setMessage("禁用失败，该权限存在启用状态的角色！");
                    return apiResultSet;
                }
            }
            dbSysPermission.setIsAble(BaseStaticParameter.N);
        } else {
            // 验证是否存在禁用状态上级权限
            if (dbSysPermission.getParentOid() != null){
                sysPermission.setPermissionOid(dbSysPermission.getParentOid());
                sysPermission.setIsAble(BaseStaticParameter.N);
                DbSysPermissionExample dbSysPermissionExampleParent = buildDbSysPermissionExample(sysPermission);
                List<DbSysPermission> dbSysPermissionsParents = dbSysPermissionMapper.selectByExample(dbSysPermissionExampleParent);
                if (dbSysPermissionsParents.size() > 0){
                    apiResultSet.setMessage("启用失败，该权限存在禁用状态的上级权限！");
                    return apiResultSet;
                }
            }
            dbSysPermission.setIsAble(BaseStaticParameter.Y);
        }
        dbSysPermission.setModifyDate(new Date());
        dbSysPermissionMapper.updateByPrimaryKey(dbSysPermission);
        // dbSysRolePermissionMapper.deleteByPermissionOid(dbSysPermission.getPermissionOid());
        apiResultSet.setCode(ApiResultSet.SUCCESS);
        return apiResultSet;
    }

    /**
     * 删除权限
     *
     * @param id
     * @return ApiResultSet
     * @author ningzz
     * @Date 2020/11/13
     **/
    @CacheEvict( key = "'getSysPermissionById:'+#id")
    public ApiResultSet deleteSysPermission(Long id) {
        if (null == id) {
            throw new ParamValidException("无效权限！");
        }
        DbSysPermission dbSysPermission = dbSysPermissionMapper.selectByPrimaryKey(id);
        if (dbSysPermission == null) {
            throw new ParamValidException("无效权限！");
        }
        ApiResultSet apiResultSet = new ApiResultSet(ApiResultSet.DIRTY_DATA_TITLE);
        String dbSysPermissionPermissionOid = dbSysPermission.getPermissionOid();
        SysPermission sysPermission = new SysPermission();
        // 验证是否存在启用状态下级权限
        sysPermission.setParentOid(dbSysPermissionPermissionOid);
        sysPermission.setIsAble(BaseStaticParameter.Y);
        DbSysPermissionExample dbSysPermissionExampleChild = buildDbSysPermissionExample(sysPermission);
        List<DbSysPermission> dbSysPermissionsChild = dbSysPermissionMapper.selectByExample(dbSysPermissionExampleChild);
        if (dbSysPermissionsChild.size() > 0) {
            apiResultSet.setMessage("删除失败，该权限存在启用状态的下级权限！");
            return apiResultSet;
        }
        // 验证该权限下是否存在未删除的角色
        if (dbSysPermissionPermissionOid != null){
            if (dbSysRoleMapper.selectRoleByPermissionOid(dbSysPermissionPermissionOid) > 0){
                apiResultSet.setMessage("删除失败，该权限存在未删除的角色！");
                return apiResultSet;
            }
        }
        dbSysPermission.setIsDelete(BaseStaticParameter.Y);
        dbSysPermission.setModifyDate(new Date());
        dbSysPermissionMapper.updateByPrimaryKey(dbSysPermission);
        // 将下级权限一并删除
        sysPermission.setIsAble(null);
        DbSysPermissionExample dbSysPermissionExampleChild2 = buildDbSysPermissionExample(sysPermission);
        List<DbSysPermission> dbSysPermissionsChild2s = dbSysPermissionMapper.selectByExample(dbSysPermissionExampleChild2);
        if (dbSysPermissionsChild2s.size() > 0) {
            for (DbSysPermission dbSysPermissionsChild2:dbSysPermissionsChild2s) {
                dbSysPermissionsChild2.setIsDelete(BaseStaticParameter.Y);
                dbSysPermissionMapper.updateByPrimaryKey(dbSysPermissionsChild2);
            }
        }
        apiResultSet.setCode(ApiResultSet.SUCCESS);
        return apiResultSet;
    }

    //@Cacheable( key = "'getRoutersPermission:'+#loginOid+#appOid", unless = "#result == null")
    public List<SysPermission> getRoutersPermission(String loginOid, String appOid) {
        List<DbSysPermission> dbSysPermissionList = dbSysPermissionMapper.selectByLoginOidAndAppOidAndFunctionType(loginOid, appOid, BaseStaticParameter.L);
        //已经存在的权限oid
        List<String> permissionOids = new ArrayList<>();
        List<Long> ids = new ArrayList<>();//用来临时存储SysPermission的id
        List<SysPermission> sysPermissionList = dbSysPermissionList.stream().filter(permission->{
            boolean flag = !ids.contains(permission.getId());
            ids.add(permission.getId());
            return flag;
        }).map(dbSysPermission -> {
            SysPermission sysPermission = com.zfsoft.microservice.platform.util.BeanUtils.copyProperties(dbSysPermission, SysPermission::new, (s, t) -> {
                t.setIsRouter(StringUtils.isNotEmpty(s.getHyperLink()));
            });
            permissionOids.add(sysPermission.getPermissionOid());
            return sysPermission;
        }).collect(Collectors.toList());

        //获取父类的权限
        Map<String, SysPermission> sysPermissionMap = new HashMap<>();
        List<String> permissionOidList = sysPermissionList.stream().map(SysPermission::getParentOid).filter(org.apache.commons.lang3.StringUtils::isNotBlank).collect(Collectors.toList());
        this.getParent(permissionOidList, sysPermissionMap);
        for (Map.Entry<String, SysPermission> entry:sysPermissionMap.entrySet()) {
            //System.out.println(!permissionOids.contains(entry.getKey()));
            if(!permissionOids.contains(entry.getKey())){
                sysPermissionList.add(entry.getValue());
            }
        }
        List<SysPermission> permissionList = sysPermissionList.stream().filter(sysPermission -> {
            return sysPermission.getParentOid() == null;
        }).map(sysPermission -> {
            sysPermission.setChildren(getChildren(sysPermission, sysPermissionList));
            return sysPermission;
        }).sorted((sysPermission, permission1) -> {
            return sysPermission.getSort() - permission1.getSort();
        }).collect(Collectors.toList());
        return permissionList;
    }

    private void getParent(List<String> permissionOidList,Map<String, SysPermission> sysPermissionMap){
        if (!CollectionUtils.isEmpty(permissionOidList)){
            List<DbSysPermission> dbSysPermissionList = dbSysPermissionMapper.selectByPermissionOidList(permissionOidList);
            if(!CollectionUtils.isEmpty(dbSysPermissionList)){
                List<SysPermission> sysPermissionList = dbSysPermissionList.stream().map(dbSysPermission -> {
                    SysPermission sysPermission = new SysPermission();
                    BeanUtils.copyProperties(dbSysPermission, sysPermission);
                    return sysPermission;
                }).collect(Collectors.toList());

                sysPermissionList.forEach(sysPermission -> {
                    sysPermissionMap.put(sysPermission.getPermissionOid(), sysPermission);

                });
                List<String> permissionOids = sysPermissionList.stream().map(SysPermission::getParentOid).filter(org.apache.commons.lang3.StringUtils::isNotBlank).collect(Collectors.toList());
                this.getParent(permissionOids, sysPermissionMap);
            }
        }
    }

    /**
     * @description:  递归查找父类
     * @param permission 权限
     * @param sysPermissionMap 父类权限实体类
     * @author: wuxx
     * @Date: 2020/9/27 10:10
     **/
    private Map<String, SysPermission> getParent(SysPermission permission,Map<String, SysPermission> sysPermissionMap){
        if (null!=permission.getParentOid()){
            DbSysPermission dbSysPermission = dbSysPermissionMapper.selectByPermissionOid(permission.getParentOid());
            if(null!=dbSysPermission){
                SysPermission sysPermission = new SysPermission();
                BeanUtils.copyProperties(dbSysPermission, sysPermission);
                sysPermissionMap.put(permission.getParentOid(),sysPermission);
                return getParent(sysPermission,sysPermissionMap);
            }
        }
        return sysPermissionMap;
    }


    /**
     * 递归查找子类
     *
     * @param root
     * @param all
     * @return
     */
    private List<SysPermission> getChildren(SysPermission root, List<SysPermission> all) {
        List<SysPermission> children = all.stream().filter(permission -> {
            return  root.getPermissionOid().equals(permission.getParentOid());
        }).map(sysPermission -> {
            sysPermission.setChildren(getChildren(sysPermission, all));
            return sysPermission;
        }).sorted((sysPermission, permission) -> {
            return sysPermission.getSort() - permission.getSort();
        }).collect(Collectors.toList());
        return children;
    }

    /**
     * 构建前端路由所需要的菜单
     *
     * @param menus 菜单列表
     * @return 路由列表
     */
    public List<RouterVo> buildMenus(List<SysPermission> menus) {
        List<RouterVo> routers = new LinkedList<RouterVo>();
        for (SysPermission menu : menus) {
            RouterVo router = new RouterVo();
            router.setHidden(false);
            router.setName(getRouteName(menu));
            router.setPath(getRouterPath(menu));
            router.setComponent(getComponent(menu));
            router.setHyperLink(menu.getHyperLink());
            router.setExistRouter(StringUtils.isNotEmpty(menu.getHyperLink()));
            String outLink = menu.getOutLink();
            // 20210710 modify by kkfan
//            if(StrUtil.isNotEmpty(outLink) && !outLink.contains(BaseStaticParameter.HTTP) && !outLink.contains(BaseStaticParameter.HTTPS)){
//                if(StrUtil.isNotEmpty(outLinkPrefix)){
//                    outLink = outLinkPrefix+menu.getOutLink();
//                }
//            }
            router.setMeta(new MetaVo(menu.getName(), menu.getIconName(),outLink));
            List<SysPermission> cMenus = menu.getChildren();
            //&& 1 == menu.getPerLevel() 去除1级菜单的限制  by wuxx 20210311
            if (!cMenus.isEmpty() && cMenus.size() > 0 && "L".equals(menu.getFunctionType())) {
                router.setAlwaysShow(true);
                router.setRedirect("noRedirect");
                router.setChildren(buildMenus(cMenus));
            } else if (isMeunFrame(menu)) {
                List<RouterVo> childrenList = new ArrayList<RouterVo>();
                RouterVo children = new RouterVo();
                children.setPath(menu.getRouter());
                children.setComponent(menu.getHyperLink());
                children.setHyperLink(menu.getHyperLink());
                children.setName(StrUtil.upperFirst(menu.getRouter()));
                children.setMeta(new MetaVo(menu.getName(), menu.getIconName()));
                children.setExistRouter(StringUtils.isNotEmpty(menu.getHyperLink()));
                childrenList.add(children);
                router.setChildren(childrenList);
            }
            routers.add(router);
        }
        return routers;
    }

    @Cacheable( key = "'getPermissionStrList:'+#loginOid+#appOid", unless = "#result == null")
    public List<String> getPermissionStrList(String loginOid, String appOid) {
        Set<String> permissonSet = new HashSet<>();
        List<DbSysPermission> dbSysPermissionList = dbSysPermissionMapper.selectByLoginOidAndAppOidAndFunctionType(loginOid, appOid,null);
        
        List<String> permissionOidList = dbSysPermissionList.stream().map(DbSysPermission::getPermissionOid).collect(Collectors.toList());
        List<SysPermissionLink> permissionLinkList = sysPermissionLinkManager.querySysPermissionLinkByPermissionOid(permissionOidList);

        for (DbSysPermission permission:dbSysPermissionList){
            if(StrUtil.isNotEmpty(permission.getHyperLink())){
                permissonSet.add(permission.getHyperLink());
            }
            if(StrUtil.isNotEmpty(permission.getStr())){
                permissonSet.add(permission.getStr());
            }
            if(null!=permissionLinkList && permissionLinkList.size()>0){
                for (SysPermissionLink permissionLink:permissionLinkList){
                    if(StrUtil.isNotEmpty(permissionLink.getPerLink())){
                        permissonSet.add(permissionLink.getPerLink());
                    }
                }
            }
        }
        if(permissonSet.size()>0){
            return new ArrayList<>(permissonSet);
        }
        return new ArrayList<>();
    }

    /**
     * 获取路由名称
     *
     * @param menu 菜单信息
     * @return 路由名称
     */
    public String getRouteName(SysPermission menu) {
        String routerName = StrUtil.upperFirst(menu.getRouter());
        // 非外链并且是一级目录（类型为目录）
        if (isMeunFrame(menu)) {
            routerName = "";
        }
        return routerName;
    }

    /**
     * 获取路由地址
     *
     * @param menu 菜单信息
     * @return 路由地址
     */
    public String getRouterPath(SysPermission menu) {
        String routerPath = menu.getRouter();
        // 非外链并且是一级目录（类型为目录）
        if (menu.getParentOid() == null && 1 == menu.getPerLevel()
                && BaseStaticParameter.N == menu.getIsFrame()) {
            routerPath = "/" + menu.getRouter();
        }
        // 非外链并且是一级目录（类型为菜单）
        else if (isMeunFrame(menu)) {
            routerPath = "/";
        }
        return routerPath;
    }

    /**
     * 获取组件信息
     *
     * @param menu 菜单信息
     * @return 组件信息
     */
    public String getComponent(SysPermission menu) {
        String component = BaseStaticParameter.LAYOUT;
        if (StrUtil.isNotEmpty(menu.getHyperLink()) && !isMeunFrame(menu)) {
            component = menu.getHyperLink();
            if(isInnerMeunFrame(menu)){
                //二级菜单的下级菜单
                component = "ParentView";
            }
        }
        return component;
    }

    /**
     * 是否为菜单内部跳转
     *
     * @param menu 菜单信息
     * @return 结果
     */
    public boolean isMeunFrame(SysPermission menu) {
        return menu.getParentOid() == null && 2 == menu.getPerLevel()
                && BaseStaticParameter.N == menu.getIsFrame() && "L".equals(menu.getFunctionType());
    }

    /**
     * 是否为菜单内部跳转的下级菜单
     *
     * @param menu 菜单信息
     * @return 结果
     */
    public boolean isInnerMeunFrame(SysPermission menu) {
        if(StrUtil.isBlank(menu.getHyperLink())){
            return false;
        }
        boolean flag = StrUtil.isNotBlank(menu.getParentOid()) && "L".equals(menu.getFunctionType()) && 2 == menu.getPerLevel()
                && BaseStaticParameter.N == menu.getIsFrame();
        if(flag){
            SysPermission permission = new SysPermission();
            permission.setParentOid(menu.getPermissionOid());
            permission.setFunctionType("L");
            List<SysPermission> sysPermissionList = this.querySysPermission(permission);
            if(null!=sysPermissionList && sysPermissionList.size()>0){
                return true;
            }
        }
        return false;
    }
    /**
     * 构建权限查询
     *
     * @param sysPermission 权限
     * @return DbSysPermissionExample
     * @author zxx
     * @date 2020/9/11 4:12 下午
     */
    private DbSysPermissionExample buildDbSysPermissionExample(SysPermission sysPermission) {
        DbSysPermissionExample dbSysPermissionExample = new DbSysPermissionExample();
        DbSysPermissionExample.Criteria criteria = dbSysPermissionExample.createCriteria();
        if (sysPermission != null) {
            if (null != sysPermission.getAppOid()) {
                criteria.andAppOidEqualTo(sysPermission.getAppOid());
            }
            if (null != sysPermission.getPermissionOid()){
                criteria.andPermissionOidEqualTo(sysPermission.getPermissionOid());
            }
            if (null != sysPermission.getParentOid()) {
                criteria.andParentOidEqualTo(sysPermission.getParentOid());
            }
            if (StrUtil.isNotBlank(sysPermission.getName())) {
                criteria.andNameLike(StrUtil.format("{}%", sysPermission.getName().trim()));
            }
            if (null!=sysPermission.getIsAble()) {
                criteria.andIsAbleEqualTo(sysPermission.getIsAble());
            }
            if (StrUtil.isNotBlank(sysPermission.getFunctionType())) {
                criteria.andFunctionTypeEqualTo(sysPermission.getFunctionType());
            }
        }

        criteria.andIsDeleteEqualTo(BaseStaticParameter.N);
        dbSysPermissionExample.setOrderByClause("sort asc");
        return dbSysPermissionExample;
    }
}
