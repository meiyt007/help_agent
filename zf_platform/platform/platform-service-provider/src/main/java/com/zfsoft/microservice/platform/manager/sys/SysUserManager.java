package com.zfsoft.microservice.platform.manager.sys;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zfsoft.microservice.platform.data.sys.*;
import com.zfsoft.microservice.platform.data.vo.SysUserVo;
import com.zfsoft.microservice.platform.dbaccess.dao.sys.DbSysDistrictMapper;
import com.zfsoft.microservice.platform.dbaccess.dao.sys.DbSysUserMapper;
import com.zfsoft.microservice.platform.dbaccess.data.sys.DbSysDistrict;
import com.zfsoft.microservice.platform.dbaccess.data.sys.DbSysUser;
import com.zfsoft.microservice.platform.dbaccess.data.sys.DbSysUserExample;
import com.zfsoft.microservice.platform.feign.settings.SysDictFeignService;
import com.zfsoft.microservice.platform.util.GenDataTreeUtil;
import com.zfsoft.microservice.platform.util.IdCardUtil;
import com.zfsoft.microservice.settings.data.SysDict;
import com.zfsoft.platform.common.data.BaseStaticParameter;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.platform.common.security.data.CurrentLoginUserHolder;
import com.zfsoft.platform.utils.util.Assert;
import com.zfsoft.platform.utils.validate.ParamValid;
import com.zfsoft.platform.utils.validate.ParamValidException;
import com.zfsoft.platform.utils.validate.ResultInfoException;
import com.zfsoft.platform.utils.validate.ValidGroups;
import com.zfsoft.platform.utils.web.CommonUtil;
import com.zfsoft.platform.utils.web.TreeSelect;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户service实现
 *
 * @author zxx
 * @date 2020/9/15 3:51 下午
 */
@Service
@CacheConfig(cacheNames = "sys:user")
public class SysUserManager {

    @Resource
    private DbSysUserMapper dbSysUserMapper;

    @Resource
    private DbSysDistrictMapper dbSysDistrictMapper;

    @Autowired
    private SysLoginManager sysLoginManager;

    @Autowired
    private SysDictFeignService sysDictFeignService;

    @Autowired
    private SysLoginRoleManager sysLoginRoleManager;

    @Autowired
    private SysRoleManager sysRoleManager;

    @Autowired
    private SysDistrictManager sysDistrictManager;

    @Autowired
    private SysOrganManager sysOrganManager;

    /**
     * @description:  根据主键查询对应的实体类
     * @param id 主键
     * @author: wuxx
     * @Date: 2020/10/28 9:54
     **/
    @Cacheable( key = "'getSysUserById:'+#id", unless = "#result == null")
    public SysUser getSysUserById(Long id) {
        if (null == id) {
            return null;
        }
        DbSysUser dbSysUser = dbSysUserMapper.selectByPrimaryKey(id);
        SysUser sysUser = new SysUser();
        BeanUtils.copyProperties(dbSysUser, sysUser);
        if(dbSysUser!=null){
            sysUser.setDataAuthority(null==sysUser.getDataAuthority()?1:sysUser.getDataAuthority());
            if(dbSysUser.getType()!=null){
                SysDict dict = sysDictFeignService.getSysDictByDictOid(dbSysUser.getType()).getData();
                sysUser.setTypeName(null!=dict?dict.getName():null);
            }
        }
        return sysUser;
    }

    /**
     * @description:  根据业务主键查询对应的实体类
     * @param userOid 业务主键
     * @author: wuxx
     * @Date: 2020/10/28 9:54
     **/
    @Cacheable( key = "'getSysUserByUserOid:'+#userOid", unless = "#result == null")
    public SysUser getSysUserByUserOid(String userOid) {
        if (null == userOid) {
            return null;
        }
        DbSysUser dbSysUser = dbSysUserMapper.selectByUserOid(userOid);
        SysUser sysUser = new SysUser();
        if(dbSysUser!=null){
            BeanUtils.copyProperties(dbSysUser, sysUser);
            sysUser.setDataAuthority(null==sysUser.getDataAuthority()?1:sysUser.getDataAuthority());
            if(dbSysUser.getType()!=null){
                SysDict dict = sysDictFeignService.getSysDictByDictOid(dbSysUser.getType()).getData();
                sysUser.setTypeName(null!=dict?dict.getName():null);
            }
        }
        return sysUser;
    }

    public PageResult<SysUser> querySysUserWithPage(SysUser sysUser, Integer pageNum, Integer pageSize) {
        DbSysUserExample dbSysUserExample = buildDbSysUserExample(sysUser);
        PageHelper.startPage(pageNum, pageSize);
        Page<DbSysUser> dbSysUserPage = (Page<DbSysUser>) dbSysUserMapper.selectSysUserPageByExample(dbSysUserExample);
        PageResult<SysUser> pageResult = new PageResult<>(dbSysUserPage.getPageNum(), dbSysUserPage.getPageSize(), dbSysUserPage.getTotal());
        List<SysUser> sysUserList = dbSysUserPage.stream().map(dbSysUser -> {
            SysUser user = new SysUser();
            BeanUtils.copyProperties(dbSysUser, user);
            user.setPassword(null);
            user.setMobile(CommonUtil.mobileEncode(user.getMobile()));
            user.setCardNo(CommonUtil.idCardEncode(user.getCardNo()));
            user.setEmail(CommonUtil.emailEncode(user.getEmail()));
            user.setTelphone(CommonUtil.phoneEncode(user.getTelphone()));
            return user;
        }).collect(Collectors.toList());
        pageResult.setData(sysUserList);
        return pageResult;
    }

    /**
     * 根据岗位oid分页查询用户信息
     *
     * @param pageNum  页码
     * @param pageSize 每页数量
     * @return ApiResultSet
     * @author wuxx
     * @date 2021/1/22 10:15
     */
    public PageResult<SysUser> queryUserPostPage(SysUser sysUser, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        DbSysUserExample dbSysUserExample = new DbSysUserExample();
        DbSysUserExample.Criteria criteria = dbSysUserExample.createCriteria();
        if (sysUser != null) {
            if (StrUtil.isNotEmpty(sysUser.getDistrictOid())) {
                criteria.andDistrictOidEqualTo(sysUser.getDistrictOid());
            }
            if (StrUtil.isNotEmpty(sysUser.getOrganOid())) {
                criteria.andOrganOidEqualTo(sysUser.getOrganOid());
            }
            if (StrUtil.isNotEmpty(sysUser.getName())) {
                criteria.andNameLike(StrUtil.format("%{}%", sysUser.getName().trim()));
            }
            if (StrUtil.isNotEmpty(sysUser.getPostOids())) {
                criteria.andPostOidsLike(StrUtil.format("%{}%", sysUser.getPostOids().trim()));
            }
        }
        criteria.andIsDeleteEqualTo(BaseStaticParameter.N);
        criteria.andIsAbleEqualTo(BaseStaticParameter.Y);
        dbSysUserExample.setOrderByClause("sort asc");
        Page<DbSysUser> dbSysUserPage = (Page<DbSysUser>) dbSysUserMapper.selectByExample(dbSysUserExample);
        PageResult<SysUser> pageResult = new PageResult<>(dbSysUserPage.getPageNum(), dbSysUserPage.getPageSize(), dbSysUserPage.getTotal());
        List<SysUser> sysUserList = dbSysUserPage.stream().map(dbSysUser -> {
            SysUser user = new SysUser();
            BeanUtils.copyProperties(dbSysUser, user);
            user.setPassword(null);
            user.setMobile(CommonUtil.mobileEncode(user.getMobile()));
            user.setCardNo(CommonUtil.idCardEncode(user.getCardNo()));
            user.setEmail(CommonUtil.emailEncode(user.getEmail()));
            user.setTelphone(CommonUtil.phoneEncode(user.getTelphone()));
            //所属区划
            if(StrUtil.isNotEmpty(user.getDistrictOid())){
                SysDistrict district = sysDistrictManager.getSysDistrictByDistrictOid(user.getDistrictOid());
                user.setDistrictName(null!=district?district.getName():"");
            }
            //所属机构
            if(StrUtil.isNotEmpty(user.getOrganOid())){
                SysOrgan organ = sysOrganManager.getSysOrganByOrganOid(user.getOrganOid());
                user.setOrganName(null!=organ?organ.getName():"");
            }
            return user;
        }).collect(Collectors.toList());
        pageResult.setData(sysUserList);
        return pageResult;
    }

    public List<SysUser> querySysUser(SysUser sysUser) {
        DbSysUserExample dbSysUserExample = buildDbSysUserExample(sysUser);
        List<DbSysUser> dbSysUserList = dbSysUserMapper.selectByExample(dbSysUserExample);
        List<SysUser> sysUserList = dbSysUserList.stream().map(dbSysUser -> {
            SysUser user = new SysUser();
            user.setDataAuthority(null==user.getDataAuthority()?1:user.getDataAuthority());
            BeanUtils.copyProperties(dbSysUser, user);
            return user;
        }).collect(Collectors.toList());
        return sysUserList;
    }

    public List<SysUser> querySysUserNoLogin(SysUser sysUser) {
        DbSysUserExample dbSysUserExample = new DbSysUserExample();
        DbSysUserExample.Criteria criteria = dbSysUserExample.createCriteria();
        criteria.andUserOidEqualTo(sysUser.getUserOid());
        criteria.andMobileEqualTo(sysUser.getMobile());
        criteria.andCardNoEqualTo(sysUser.getCardNo());
        criteria.andIsDeleteEqualTo(BaseStaticParameter.N);
        List<DbSysUser> dbSysUserList = dbSysUserMapper.selectByExample(dbSysUserExample);
        List<SysUser> sysUserList = dbSysUserList.stream().map(dbSysUser -> {
            SysUser user = new SysUser();
            BeanUtils.copyProperties(dbSysUser, user);
            return user;
        }).collect(Collectors.toList());
        return sysUserList;
    }

    /**
     * 启禁用用户
     *
     * @param id
     * @return int 1表示成功，0表示失败
     * @author ningzz
     * @Date 2020/11/12
     **/
    @Transactional
    @ShardingTransactionType(TransactionType.LOCAL)
    @CacheEvict(key = "'getSysUserById:'+#id")
    public int ableSysUser(Long id) {
        if (null == id) {
            throw new ResultInfoException("未查询到用户信息！");
        }
        DbSysUser dbSysUser = dbSysUserMapper.selectByPrimaryKey(id);
        if (null == dbSysUser) {
            throw new ResultInfoException("未查询到用户信息！");
        }
        SysLogin sysLogin = sysLoginManager.getSysLoginByUserOid(dbSysUser.getUserOid());
        if (sysLogin != null && BaseStaticParameter.N==sysLogin.getIsDelete() && BaseStaticParameter.Y==sysLogin.getIsAble()){
            // 禁用失败，该用户的登录账户为启用状态
            return BaseStaticParameter.N;
        }
        if (BaseStaticParameter.Y == dbSysUser.getIsAble()) {
            dbSysUser.setIsAble(BaseStaticParameter.N);
            sysLogin.setIsAble(BaseStaticParameter.N);
        } else {
            dbSysUser.setIsAble(BaseStaticParameter.Y);
            sysLogin.setIsAble(BaseStaticParameter.Y);
        }
        dbSysUser.setModifyDate(new Date());
        dbSysUserMapper.updateByPrimaryKey(dbSysUser);
        sysLoginManager.updateByPrimaryKey(sysLogin);
        return BaseStaticParameter.Y;
    }


    /**
     * 删除用户
     *
     * @param id
     * @return int 1表示成功，0表示失败
     * @author ningzz
     * @Date 2020/11/12
     **/
    @Transactional
    @ShardingTransactionType(TransactionType.LOCAL)
    @CacheEvict(key = "'getSysUserById:'+#id")
    public int deleteSysUser(Long id) {
        if (null == id) {
            throw new ResultInfoException("未查询到用户信息！");
        }
        DbSysUser dbSysUser = dbSysUserMapper.selectByPrimaryKey(id);
        if (null == dbSysUser) {
            throw new ResultInfoException("未查询到用户信息！");
        }
        SysLogin sysLogin = sysLoginManager.getSysLoginByUserOid(dbSysUser.getUserOid());
        if (sysLogin != null && BaseStaticParameter.N==sysLogin.getIsDelete() && BaseStaticParameter.Y==sysLogin.getIsAble()){
            // 删除失败，该用户的登录账户为启用状态
            return BaseStaticParameter.N;
        }
        dbSysUser.setIsDelete(BaseStaticParameter.Y);
        dbSysUser.setModifyDate(new Date());
        dbSysUserMapper.updateByPrimaryKey(dbSysUser);
        sysLoginManager.deleteSysLogin(sysLogin.getId());
        return BaseStaticParameter.Y;
    }


    @ParamValid
    @Transactional
    @ShardingTransactionType(TransactionType.LOCAL)
    @CacheEvict(allEntries = true)
    public int saveSysUser(@ValidGroups(groups = {SysUser.INSERT_GROUP.class})SysUser sysUser) {
        if (sysUser == null) {
            throw new ParamValidException("用户信息不正确！");
        }
        if (!IdCardUtil.checkIdCard(sysUser.getCardNo())){
            throw new ParamValidException("身份证信息不正确！");
        }
        if (null == sysUser.getId()) {
            sysUser.setCreateDate(new Date());
            sysUser.setUserOid(IdUtil.simpleUUID());
        } else {
            DbSysUser dbSysUser = dbSysUserMapper.selectByPrimaryKey(sysUser.getId());
            if (dbSysUser == null) {
                throw new ParamValidException("用户编号未查询到用户信息！");
            }
            sysUser.setHeadImageAttaOid(dbSysUser.getHeadImageAttaOid());
        }
        if (null ==sysUser.getIsAble() || !BaseStaticParameter.ABLE_MAP.containsKey(sysUser.getIsAble())) {
            sysUser.setIsAble(BaseStaticParameter.Y);
        }

        sysUser.setIsDelete(BaseStaticParameter.N);
        sysUser.setModifyDate(new Date());
        sysUser.setDataAuthority(null==sysUser.getDataAuthority()?1:sysUser.getDataAuthority());
        DbSysUser dbSysUser = new DbSysUser();
        BeanUtils.copyProperties(sysUser, dbSysUser);
        if (null == sysUser.getId()) {
            dbSysUserMapper.insert(dbSysUser);
        } else {
            dbSysUserMapper.updateByPrimaryKey(dbSysUser);
        }
        //新增用户时设置用户密码
        if (null == sysUser.getId()) {
            SysLogin sysLogin=new SysLogin();
            sysLogin.setAccount(sysUser.getAccount());
            sysLogin.setUserOid(dbSysUser.getUserOid());
            sysLogin.setName(sysUser.getName());
            sysLogin.setPassword(sysUser.getPassword());
            sysLogin.setFailNum(0);
            sysLogin.setLockStatus(BaseStaticParameter.N);
            sysLogin.setPasswordDate(new Date());
            sysLoginManager.saveSysLogin(sysLogin);
        }
        return 0;
    }


    @Transactional
    @CacheEvict(allEntries = true)
    public int updateSysUser(SysUser sysUser) {
        if (sysUser == null) {
            throw new ParamValidException("用户信息不正确！");
        }
        if (null == sysUser.getUserOid()) {
            throw new ParamValidException("用户业务主键不能为空！");
        } else {
            DbSysUser dbSysUser = dbSysUserMapper.selectByUserOid(sysUser.getUserOid());
            if (dbSysUser == null) {
                throw new ParamValidException("用户编号未查询到用户信息！");
            }
            sysUser.setId(dbSysUser.getId());
        }
        DbSysUser dbSysUser = new DbSysUser();
        BeanUtils.copyProperties(sysUser, dbSysUser);
        return dbSysUserMapper.updateByPrimaryKeySelective(dbSysUser);
    }

    public List<SysLoginRole> getUserRoleByUserOid(String userOid) {
        if(userOid==null){
            return null;
        }
        SysLogin login = sysLoginManager.getSysLoginByUserOid(userOid);
        if(login==null){
            return null;
        }
        return sysLoginRoleManager.querySysLoginRoleByLoginOid(login.getLoginOid());
    }


    @Transactional
    @ShardingTransactionType(TransactionType.LOCAL)
    @CacheEvict(cacheNames = {"sys:permission","sys:role","sys:user"},allEntries = true)
    public void saveUserRole(String userOid, List<String> roleOids) {
        if(userOid==null){
            throw new ParamValidException("用户信息无效！");
        }
        SysLogin login = sysLoginManager.getSysLoginByUserOid(userOid);
        if(login==null){
            throw new ParamValidException("用户信息无效！");
        }
        if(roleOids==null||roleOids.size()==0){
            throw new ParamValidException("无效的角色信息！");
        }
        sysLoginRoleManager.deleteByLoginOid(login.getLoginOid());
        List<SysRole> sysRoles = sysRoleManager.querySysRole(null);
        roleOids.forEach(roleOid->{
            List<SysRole> roles = sysRoles.stream().filter(sysRole -> {
                return sysRole.getRoleOid().equals(roleOid);
            }).collect(Collectors.toList());
            if(roles==null||roles.size()==0){
                throw new ParamValidException("无效的角色信息！");
            }
            SysLoginRole sysLoginRole=new SysLoginRole();
            sysLoginRole.setLoginOid(login.getLoginOid());
            sysLoginRole.setRoleOid(roleOid);
            sysLoginRole.setLoginRoleOid(IdUtil.simpleUUID());
            sysLoginRole.setAppOid(roles.get(0).getAppOid());
            sysLoginRoleManager.saveSysLoginRole(sysLoginRole);
        });
    }


    public List<TreeSelect> queryDistrictAndOrganAndUserTreeSelect(String oid, String identity) {
        List<TreeSelect> treeSelectList=new ArrayList<>();
        switch (identity){
            case "DISTRICT":
                if((StrUtil.isEmpty(oid))){
                    List<SysDistrict> sysDistricts = sysDistrictManager.queryTopDistrictByOid(null);
                    treeSelectList= GenDataTreeUtil.buildSysDistrictTreeSelect(sysDistricts);
                }else{
                    List<SysDistrict> sysDistricts = sysDistrictManager.queryDistrictChildrenByParentOid(oid);
                    //List<SysOrgan> sysOrgans = sysOrganManager.queryOrganChildrenByDistrictOid(oid);
                    List<SysOrgan> sysOrgans = sysOrganManager.queryOrganTreeByDistrictOid(oid);
                    treeSelectList= GenDataTreeUtil.buildDistrictAndOrganTreeSelect(sysDistricts,sysOrgans);
                }
                break;
            case "ORGAN":
                List<SysOrgan> sysOrgans = sysOrganManager.queryOrganChildrenByParentOid(oid);
                SysUser sysUser=new SysUser();
                sysUser.setOrganOid(oid);
                sysUser.setIsAble(BaseStaticParameter.Y);
                List<SysUser> sysUsers = this.querySysUser(sysUser);
                treeSelectList= GenDataTreeUtil.buildOrganAndUserTreeSelect(sysOrgans,sysUsers,false);
                break;
            default:
                break;
        }
        return treeSelectList;
    }

    /**
     * @description: 查询所有区划的组织机构、用户信息Tree
     * @author: wuxx
     * @Date: 2021/1/26 10:40
     **/
    @Cacheable( key = "'queryAllDistrictOrganUserTree'", unless = "#result == null")
    public List<TreeSelect> queryAllDistrictOrganUserTree() {
        List<SysDistrict> sysDistrictList = sysDistrictManager.queryDistrictSimpleTreeList(null);
        List<SysOrgan> organList = sysOrganManager.queryOrganTreeList(null);
        List<SysUser> userList = this.querySysUser(null);
        List<TreeSelect> treeSelects = GenDataTreeUtil.buildDistrictAndOrganAndUserTreeSelect(sysDistrictList, organList, userList, false);
        treeSelects = GenDataTreeUtil.buildDisabledLastTree(treeSelects,GenDataTreeUtil.TYPE_USER);
        return treeSelects;
    }

    /**
     * @description: 分页查询区划的组织机构、用户信息Tree
     * @author: wuxx
     * @Date: 2021/3/25 10:40
     **/
    @Cacheable( key = "'queryPageDistrictOrganUserTree'", unless = "#result == null")
    public List<TreeSelect> queryPageDistrictOrganUserTree() {
        List<SysDistrict> sysDistrictList = sysDistrictManager.queryDistrictSimpleTreeList(null);
        List<SysOrgan> organList = sysOrganManager.queryOrganTreeList(null);
        List<SysUser> userAllList = new ArrayList<>();
        for(SysOrgan  sysOrgan : organList){
            SysUser user = new SysUser();
            user.setOrganOid(sysOrgan.getOrganOid());
            List<SysUser> userList = this.querySysUserWithPage(user,1,10).getData();
            userAllList.addAll(userList);
        }
        List<TreeSelect> treeSelects = GenDataTreeUtil.buildDistrictAndOrganAndUserTreeSelect(sysDistrictList, organList, userAllList, false);
        treeSelects = GenDataTreeUtil.buildDisabledLastTree(treeSelects,GenDataTreeUtil.TYPE_USER);
        return treeSelects;
    }

    public List<SysUser> querySysUserByRoleOid(String roleOid) {
        if(roleOid==null){
            return null;
        }
        List<DbSysUser> dbSysUserList = dbSysUserMapper.selectSysUserByRoleOid(roleOid);
        List<SysUser> sysUserList = dbSysUserList.stream().map(dbSysUser -> {
            SysUser sysUser = new SysUser();
            BeanUtils.copyProperties(dbSysUser, sysUser);
            return sysUser;
        }).collect(Collectors.toList());
        return sysUserList;
    }

    /**
     * 获取角色下的用户信息Tree
     * @author wuxx
     * @date 2021/03/22 4:56 下午
     * @param roleOid
     * @return
     */
    public List<String> queryRoleUserTree(String roleOid) {
        List<String> list = new ArrayList<>();
        // oids.push('USER-'+user.userOid)
        // oids.push('DISTRICT-'+user.districtOid)
        // oids.push('ORGAN-'+user.organOid)
        List<SysUser> sysUserList = this.querySysUserByRoleOid(roleOid);
        for (SysUser sysUser : sysUserList){
            list.add("USER-"+sysUser.getUserOid());
            //循环获取区划的父子主键
            this.getDistrictOidsList(sysUser.getDistrictOid(),list);
            //循环获取组织机构的父子主键
            this.getOrganOidsList(sysUser.getOrganOid(),list);
        }
        return list;
    }

    /**
     * @description: 迭代循环获取区划的父子主键
     * @param districtOid 区划主键
     * @param list 集合
     * @author: wuxx
     * @Date: 2021/3/22 15:05
     **/
    private List<String> getDistrictOidsList(String districtOid ,List<String> list){
        if(StrUtil.isBlank(districtOid)){
            return list;
        }
        SysDistrict district = sysDistrictManager.getSysDistrictByDistrictOid(districtOid);
        if(null==district){
            return list;
        }
        list.add("DISTRICT-"+districtOid);
        if(StrUtil.isBlank(district.getParentOid())){
            return list;
        }
        this.getDistrictOidsList(district.getParentOid(),list);
        return list;
    }

    /**
     * @description: 迭代循环获取主键的父子主键
     * @param organOid 组织机构主键
     * @param list 集合
     * @author: wuxx
     * @Date: 2021/3/22 15:05
     **/
    private List<String> getOrganOidsList(String organOid ,List<String> list){
        if(StrUtil.isBlank(organOid)){
            return list;
        }
        SysOrgan organ = sysOrganManager.getSysOrganByOrganOid(organOid);
        if(null==organ){
            return list;
        }
        list.add("ORGAN-"+organOid);
        if(StrUtil.isBlank(organ.getParentOid())){
            return list;
        }
        this.getDistrictOidsList(organ.getParentOid(),list);
        return list;
    }

    @CacheEvict(key = "'getSysUserByUserOid:'+#userOid")
    public int updateUserAvatar(String userOid, String imageOid) {
        if(userOid==null){
            throw new ParamValidException("无效的用户信息！");
        }
        if(imageOid==null){
            throw new ParamValidException("无效的头像信息！");
        }
        DbSysUser dbSysUser = dbSysUserMapper.selectByUserOid(userOid);
        if(dbSysUser==null){
            throw new ParamValidException("无效的用户信息！");
        }
        dbSysUser.setHeadImageAttaOid(imageOid);
        dbSysUser.setModifyDate(new Date());
        dbSysUserMapper.updateByPrimaryKeySelective(dbSysUser);
        return 0;
    }

    /**
     * @description:  更新用户的皮肤
     * @param userOid
     * @param skinClassname
     * @author: wuxx
     * @Date: 2020/10/31 14:23
     **/
    @CacheEvict(key = "'getSysUserByUserOid:'+#userOid")
    public int updateUserSkinClassname(String userOid, String skinClassname) {
        if(userOid==null){
            throw new ParamValidException("无效的用户信息！");
        }
        if(StrUtil.isEmpty(skinClassname)){
            throw new ParamValidException("无效的皮肤信息！");
        }
        DbSysUser dbSysUser = dbSysUserMapper.selectByUserOid(userOid);
        if(dbSysUser==null){
            throw new ParamValidException("无效的用户信息！");
        }
        dbSysUser.setSkinClassname("#"+skinClassname);
        dbSysUser.setModifyDate(new Date());
        dbSysUserMapper.updateByPrimaryKey(dbSysUser);
        return 0;
    }

    private DbSysUserExample buildDbSysUserExample(SysUser sysUser) {
        DbSysUserExample dbSysUserExample = new DbSysUserExample();
        String loginDistrictOid = CurrentLoginUserHolder.getCurrentLoginUser().getDistrictOid();
        //根据区划id去区划表中获取他的path
        DbSysDistrict parent = dbSysDistrictMapper.selectByDistrictOid(loginDistrictOid);
        String path = parent.getPath();
        if(StrUtil.isNotEmpty(loginDistrictOid)){
            dbSysUserExample.setLoginDistrictOid(path+"%");
        }
        DbSysUserExample.Criteria criteria = dbSysUserExample.createCriteria();
        if (sysUser != null) {
            if (StrUtil.isNotEmpty(sysUser.getDistrictOid())) {
                criteria.andDistrictOidEqualTo(sysUser.getDistrictOid());
            }
            if (StrUtil.isNotEmpty(sysUser.getOrganOid())) {
                criteria.andOrganOidEqualTo(sysUser.getOrganOid());
            }
            if (StrUtil.isNotEmpty(sysUser.getCardNo())) {
                criteria.andCardNoEqualTo(sysUser.getCardNo());
            }
            if (StrUtil.isNotEmpty(sysUser.getMobile())) {
                criteria.andMobileEqualTo(sysUser.getMobile());
            }
            if (StrUtil.isNotEmpty(sysUser.getName())) {
                criteria.andNameLike(StrUtil.format("%{}%", sysUser.getName().trim()));
            }
            if (null!=sysUser.getIsAble()) {
                criteria.andIsAbleEqualTo(sysUser.getIsAble());
            }
            if (StrUtil.isNotEmpty(sysUser.getPostOids())) {
                criteria.andPostOidsLike(StrUtil.format("%{}%", sysUser.getPostOids().trim()));
            }
            if (StrUtil.isNotEmpty(sysUser.getType())) {
                if(BaseStaticParameter.PERSONEL_LIST.contains(sysUser.getType())){
                    SysDict sysDictByCode = sysDictFeignService.getSysDictByCode(sysUser.getType()).getData();
                    if(null!=sysDictByCode){
                        criteria.andTypeEqualTo(sysDictByCode.getDictOid());
                    }
                }
            }
        }
        criteria.andIsDeleteEqualTo(BaseStaticParameter.N);
        dbSysUserExample.setOrderByClause("sort asc");
        return dbSysUserExample;
    }

    /**
     * @description: 根据业务组织机构主键获取用户信息
     * @param organOid 组织机构主键
     * @author: wuxx
     * @Date: 2020/12/9 17:34
     **/
    public List<SysUser> getSysUserListByOrganOid(String organOid) {
        DbSysUserExample dbSysUserExample = new DbSysUserExample();
        DbSysUserExample.Criteria criteria = dbSysUserExample.createCriteria();
        criteria.andOrganOidEqualTo(organOid);
        criteria.andIsDeleteEqualTo(BaseStaticParameter.N);
        List<DbSysUser> dbSysUsers= dbSysUserMapper.selectByExample(dbSysUserExample);
        List<SysUser> sysUserList = dbSysUsers.stream().map(dbSysUser -> {
            SysUser user = new SysUser();
            BeanUtils.copyProperties(dbSysUser,user);
            return user;
        }).collect(Collectors.toList());
        return sysUserList;
    }

    /**
     * @description: 根据岗位oid获取用户信息
     * @param postOid 岗位oid
     * @author: wuxx
     * @Date: 2021/1/22 10:34
     **/
    public List<SysUser> getSysUserListByPostOid(String postOid) {
        if (StrUtil.isNotEmpty(postOid)){
            DbSysUserExample dbSysUserExample = new DbSysUserExample();
            DbSysUserExample.Criteria criteria = dbSysUserExample.createCriteria();
            criteria.andPostOidsLike(StrUtil.format("%{}%", postOid.trim()));
            criteria.andIsDeleteEqualTo(BaseStaticParameter.N);
            List<DbSysUser> dbSysUsers= dbSysUserMapper.selectByExample(dbSysUserExample);
            List<SysUser> sysUserList = dbSysUsers.stream().map(dbSysUser -> {
                SysUser user = new SysUser();
                BeanUtils.copyProperties(dbSysUser,user);
                return user;
            }).collect(Collectors.toList());
            return sysUserList;
        }else {
            return null;
        }
    }

    /**
     * @description: 根据岗位oids获取用户信息
     * @param postOids 岗位oid
     * @author: wuxx
     * @Date: 2021/1/22 10:34
     **/
    public List<SysUser> getSysUserListByPostOids(String postOids) {
        if (StrUtil.isNotEmpty(postOids)){
            List<SysUser> sysUserAllList = new ArrayList<>();
            String[] splitOids = postOids.split(",");
            for(String postOid:splitOids){
                DbSysUserExample dbSysUserExample = new DbSysUserExample();
                DbSysUserExample.Criteria criteria = dbSysUserExample.createCriteria();
                criteria.andPostOidsLike(StrUtil.format("%{}%", postOid.trim()));
                criteria.andIsDeleteEqualTo(BaseStaticParameter.N);
                List<DbSysUser> dbSysUsers= dbSysUserMapper.selectByExample(dbSysUserExample);
                List<SysUser> sysUserList = dbSysUsers.stream().map(dbSysUser -> {
                    SysUser user = new SysUser();
                    BeanUtils.copyProperties(dbSysUser,user);
                    return user;
                }).collect(Collectors.toList());
                sysUserAllList.addAll(sysUserList);
            }
            return sysUserAllList;
        }else {
            return null;
        }
    }

    /**
     * @description: 根据角色oids获取用户信息
     * @param roleOids 角色oid
     * @author: wuxx
     * @Date: 2022/3/14 10:34
     **/
    public List<SysUser> getSysUserListByRoleOids(String roleOids) {
        if (StrUtil.isNotEmpty(roleOids)){
            List<SysUser> sysUserAllList = new ArrayList<>();
            String[] splitOids = roleOids.split(",");
            for(String roleOid:splitOids){
                List<DbSysUser> dbSysUsers = dbSysUserMapper.selectSysUserByRoleOid(roleOid);
                List<SysUser> sysUserList = dbSysUsers.stream().map(dbSysUser -> {
                    SysUser user = new SysUser();
                    BeanUtils.copyProperties(dbSysUser,user);
                    return user;
                }).collect(Collectors.toList());
                sysUserAllList.addAll(sysUserList);
            }
            return sysUserAllList;
        }else {
            return null;
        }
    }

    /**
     * @description: 根据机构oids获取用户信息
     * @param organOids 机构oid
     * @author: wuxx
     * @Date: 2022/3/14 10:34
     **/
    public List<SysUser> getSysUserListByOrganOids(String organOids) {
        if (StrUtil.isNotEmpty(organOids)){
            List<SysUser> sysUserAllList = new ArrayList<>();
            String[] splitOids = organOids.split(",");
            for(String organOid:splitOids){
                List<DbSysUser> dbSysUsers = dbSysUserMapper.selectSysUserByOrganOid(organOid);
                List<SysUser> sysUserList = dbSysUsers.stream().map(dbSysUser -> {
                    SysUser user = new SysUser();
                    BeanUtils.copyProperties(dbSysUser,user);
                    return user;
                }).collect(Collectors.toList());
                sysUserAllList.addAll(sysUserList);
            }
            return sysUserAllList;
        }else {
            return null;
        }
    }

    /**
     * @description: 获取所有的用户vo
     * @author: wuxx
     * @Date: 2021/6/3 17:35
     **/
    public List<SysUserVo> getAllUserVoList() {
        List<DbSysUser> dbSysUsers = dbSysUserMapper.selectAllSysUserVoList();
        List<SysUserVo> sysUserList = dbSysUsers.stream().map(dbSysUser -> {
            SysUserVo user = new SysUserVo();
            BeanUtils.copyProperties(dbSysUser,user);
            return user;
        }).collect(Collectors.toList());
        return sysUserList;
    }

    @CacheEvict(allEntries = true)
    public void changeStatisticTypes(String statisticTypes) {
        Assert.notNull(statisticTypes, "统计类型信息不能为空！");
        DbSysUser dbSysUser = dbSysUserMapper.selectByUserOid(CurrentLoginUserHolder.getCurrentLoginUser().getUserOid());
        Assert.notNull(dbSysUser, "修改对象不存在！id = " + CurrentLoginUserHolder.getCurrentLoginUser().getUserOid());
        dbSysUser.setStatisticTypes(statisticTypes);
        dbSysUser.setModifyDate(new Date());
        dbSysUserMapper.updateByPrimaryKey(dbSysUser);
    }
}
