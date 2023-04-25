package com.zfsoft.microservice.platform.manager.sys;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zfsoft.microservice.platform.data.sys.SysLogin;
import com.zfsoft.microservice.platform.data.vo.SysLoginRoleVo;
import com.zfsoft.microservice.platform.data.vo.SysLoginVo;
import com.zfsoft.microservice.platform.dbaccess.dao.sys.DbSysLoginMapper;
import com.zfsoft.microservice.platform.dbaccess.data.sys.DbSysLogin;
import com.zfsoft.microservice.platform.dbaccess.data.sys.DbSysLoginExample;
import com.zfsoft.microservice.platform.dbaccess.vo.DbSysLoginVo;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.data.BaseStaticParameter;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.platform.common.security.data.CurrentLoginUserHolder;
import com.zfsoft.platform.utils.validate.ParamValid;
import com.zfsoft.platform.utils.validate.ParamValidException;
import com.zfsoft.platform.utils.validate.ValidGroups;
import org.apache.shardingsphere.transaction.annotation.ShardingTransactionType;
import org.apache.shardingsphere.transaction.core.TransactionType;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName SysLoginServiceImpl
 * @Description
 * @Author
 * @Date2020-09-11 17:19
 * @Version V1.0
 **/
@Service
@CacheConfig(cacheNames = "sys:login")
public class SysLoginManager {

    @Resource
    private DbSysLoginMapper dbSysLoginMapper;

    @Autowired
    private SysLoginRoleManager sysLoginRoleManager;

    @Cacheable( key = "'getSysLoginByAccount:'+#account", unless="#result == null")
    public SysLogin getSysLoginByAccount(String account) {
        if (StringUtils.isEmpty(account)){
            throw null;
        }
        SysLogin ret = null;
        DbSysLoginExample dbSysLoginExample = new DbSysLoginExample();
        DbSysLoginExample.Criteria criteria = dbSysLoginExample.createCriteria();
        criteria.andAccountEqualTo(account);
        criteria.andIsDeleteEqualTo(BaseStaticParameter.N);
        List<DbSysLogin> dbSysLoginList = dbSysLoginMapper.selectByExample(dbSysLoginExample);
        if (dbSysLoginList == null || dbSysLoginList.size() == 0){
            return null;
        }
        DbSysLogin dbSysLogin = dbSysLoginList.get(0);
        if(!dbSysLogin.getAccount().equals(account)){
            return null;
        }
        ret = new SysLogin();
        BeanUtils.copyProperties(dbSysLogin, ret);
        return ret;
    }


    @Cacheable( key = "'getSysLoginById:'+#oid")
    public SysLogin getSysLoginById(Long oid) {
        if (null == oid) {
            return null;
        }
        DbSysLogin dbSysLogin = dbSysLoginMapper.selectByPrimaryKey(oid);
        SysLogin sysLogin = new SysLogin();
        BeanUtils.copyProperties(dbSysLogin, sysLogin);
        return sysLogin;
    }

    @Cacheable( key = "'getSysLoginVoById:'+#id")
    public SysLoginVo getSysLoginVoById(Long id) {
        if (null == id) {
            return null;
        }
        DbSysLoginVo sysLoginVo = dbSysLoginMapper.selectDbSysLoginVoByPrimaryKey(id);
        SysLoginVo sysLogin = new SysLoginVo();
        BeanUtils.copyProperties(sysLoginVo, sysLogin);
        List<SysLoginRoleVo> roleList = sysLoginRoleManager.querySysLoginRoleVoByLoginOid(sysLogin.getLoginOid());
        sysLogin.setSysLoginRoleList(roleList);
        return sysLogin;
    }


    @ParamValid
    @Transactional
    @ShardingTransactionType(TransactionType.LOCAL)
    @CacheEvict(allEntries = true)
    public int saveSysLogin(@ValidGroups(groups = {SysLogin.INSERT_GROUP.class}) SysLogin sysLogin) {
//        if(null!=sysLogin.getOid()){
//            DbSysLogin dbSysLogin = dbSysLoginMapper.selectByPrimaryKey(sysLogin.getOid());
//            if (dbSysLogin == null) {
//                throw  new ParamValidException("登录信息验证有误！");
//            }
//        }
        sysLogin.setIsDelete(BaseStaticParameter.N);
        // 登录信息保存时，重新验证登录信息
        SysLogin sysLoginByAccount = this.getSysLoginByAccount(sysLogin.getAccount());
        if (null == sysLogin.getId()) {
            if (null != sysLoginByAccount) {
                throw new ParamValidException("账号已存在！");
            }
        } else {
            if (sysLoginByAccount != null && sysLoginByAccount.getId() != sysLogin.getId()) {
                throw new ParamValidException("账号已存在！");
            }
        }

        // 当启禁用状态为空或在不正确时，设置为启用状态
        if (null!=sysLogin.getIsAble()
                || !BaseStaticParameter.ABLE_MAP.containsKey(sysLogin.getIsAble())) {
            sysLogin.setIsAble(BaseStaticParameter.Y);
        }
        if (null!=sysLogin.getLockStatus() && sysLogin.getFailNum() == null) {
            sysLogin.setLockStatus(BaseStaticParameter.N);
            sysLogin.setFailNum(0);
        }

        if (sysLoginByAccount != null && sysLogin.getPassword().equals(sysLoginByAccount.getPassword())) {
            sysLogin.setPassword(sysLogin.getPassword());
        } else {
            sysLogin.setPassword(SecureUtil.md5(sysLogin.getPassword()));
        }
        DbSysLogin dbSysLogin = new DbSysLogin();
        BeanUtils.copyProperties(sysLogin, dbSysLogin);
        if (null == sysLogin.getId()) {
            dbSysLogin.setLoginOid(IdUtil.simpleUUID());
            dbSysLoginMapper.insert(dbSysLogin);
        } else {
            dbSysLoginMapper.updateByPrimaryKey(dbSysLogin);
            sysLoginRoleManager.deleteByLoginOid(dbSysLogin.getLoginOid());
        }
        if (sysLogin.getSysLoginRoleList() != null && sysLogin.getSysLoginRoleList().size() > 0) {
            sysLogin.getSysLoginRoleList().forEach(sysLoginRole -> {
                sysLoginRole.setLoginOid(dbSysLogin.getLoginOid());
                sysLoginRoleManager.saveSysLoginRole(sysLoginRole);
            });
        }
        return 0;
    }

    @CacheEvict(allEntries = true)
    public int updatePassword(String oldPassword, String password, String confirmPassword, Long id, String userOid) {
        if (StrUtil.isEmpty(password) || StrUtil.isEmpty(confirmPassword)) {
            throw new ParamValidException("密码信息不正确！");
        }
        if (!password.equals(confirmPassword)) {
            throw new ParamValidException("两次输入的密码不一致！");
        }
        if(id == null && userOid == null){
            throw new ParamValidException("登录信息异常！");
        }
        DbSysLogin dbSysLogin = null;
        if (id != null){
            dbSysLogin = dbSysLoginMapper.selectByPrimaryKey(id);
        }else {
            dbSysLogin = dbSysLoginMapper.selectByUserOid(userOid);
        }
        if (null == dbSysLogin) {
            throw new ParamValidException("未查询到登录信息！");
        }
        if (StrUtil.isNotEmpty(oldPassword) && !SecureUtil.md5(oldPassword).equals(dbSysLogin.getPassword())) {
            throw new ParamValidException("旧密码不正确！");
        }
        dbSysLogin.setPassword(SecureUtil.md5(password));
        dbSysLogin.setPasswordDate(new DateTime());
        return dbSysLoginMapper.updateByPrimaryKey(dbSysLogin);
    }

    /**
     * @description: 登录页面修改密码
     * @author: wuxx
     * @Date: 2021/4/13 9:48
     **/
    @CacheEvict(allEntries = true)
    public int updatePasswordLogin(String oldPassword, String password, String confirmPassword, Long id, String userOid) {
        if (StrUtil.isEmpty(password) || StrUtil.isEmpty(confirmPassword)) {
            throw new ParamValidException("密码信息不正确！");
        }
        if (!password.equals(confirmPassword)) {
            throw new ParamValidException("两次输入的密码不一致！");
        }
        if(id == null && userOid == null){
            throw new ParamValidException("登录信息异常！");
        }
        DbSysLogin dbSysLogin = null;
        if (id != null){
            dbSysLogin = dbSysLoginMapper.selectByPrimaryKey(id);
        }else {
            dbSysLogin = dbSysLoginMapper.selectByUserOid(userOid);
        }
        if (null == dbSysLogin) {
            throw new ParamValidException("未查询到登录信息！");
        }
        if (StrUtil.isNotEmpty(oldPassword) && !oldPassword.equals(dbSysLogin.getPassword())) {
            throw new ParamValidException("旧密码不正确！");
        }
        dbSysLogin.setPassword(SecureUtil.md5(password));
        dbSysLogin.setPasswordDate(new DateTime());
        return dbSysLoginMapper.updateByPrimaryKey(dbSysLogin);
    }

    @CacheEvict(allEntries = true)
    public int ableSysLogin(Long oid) {
        if (null == oid) {
            throw new ParamValidException("未查询到用户信息！");
        }
        DbSysLogin dbSysLogin = dbSysLoginMapper.selectByPrimaryKey(oid);
        if (null == dbSysLogin) {
            throw new ParamValidException("未查询到用户信息！");
        }
        if (BaseStaticParameter.Y == dbSysLogin.getIsAble()) {
            dbSysLogin.setIsAble(BaseStaticParameter.N);
        } else {
            dbSysLogin.setIsAble(BaseStaticParameter.Y);
        }
        dbSysLogin.setModifyDate(new Date());
        dbSysLoginMapper.updateByPrimaryKey(dbSysLogin);
        return 0;
    }

    @CacheEvict(allEntries = true)
    public int deleteSysLogin(Long oid) {
        if (null == oid) {
            throw new ParamValidException("未查询到用户信息！");
        }
        DbSysLogin dbSysLogin = dbSysLoginMapper.selectByPrimaryKey(oid);
        if (null == dbSysLogin) {
            throw new ParamValidException("未查询到用户信息！");
        }
        dbSysLogin.setIsDelete(BaseStaticParameter.Y);
        dbSysLogin.setModifyDate(new Date());
        dbSysLoginMapper.updateByPrimaryKey(dbSysLogin);
        sysLoginRoleManager.deleteByLoginOid(dbSysLogin.getLoginOid());
        return 0;
    }

    /**
     * 解锁登录列表
     * @author zhongxx
     * @date 2021-03-11 11:43
     * @return
     */
    @CacheEvict(allEntries = true)
    public ApiResultSet delockSysLoginList() {
        DbSysLoginExample dbSysLoginExample = new DbSysLoginExample();
        DbSysLoginExample.Criteria criteria = dbSysLoginExample.createCriteria();
        criteria.andLockStatusEqualTo(BaseStaticParameter.Y);
        criteria.andIsDeleteEqualTo(BaseStaticParameter.N);
        List<DbSysLogin> dbSysLogins = dbSysLoginMapper.selectByExample(dbSysLoginExample);
        dbSysLogins.forEach(dbSysLogin -> {
            SysLogin sysLogin = new SysLogin();
            BeanUtils.copyProperties(dbSysLogin, sysLogin);
            if(sysLogin!=null){
                sysLogin.setFailNum(0);
                sysLogin.setLockStatus(BaseStaticParameter.N);
                sysLogin.setLockDate(null);
                this.updateByPrimaryKey(sysLogin);
            }
        });
        return new ApiResultSet();
    }


    public PageResult<SysLogin> querySysLoginWithPage(SysLogin sysLogin, Integer pageNum, Integer pageSize) {
        //分页参数
        if (null == pageNum || pageNum <= 1) {
            pageNum = 1;
        }
        if (null == pageSize || pageSize <= 0) {
            pageSize = 10;
        }
        PageHelper.startPage(pageNum, pageSize);
        DbSysLoginExample dbSysLoginExample = buildDbSysLoginExample(sysLogin);
        Page<DbSysLogin> dbSysLogins = (Page<DbSysLogin>) dbSysLoginMapper.selectByExample(dbSysLoginExample);
        PageResult<SysLogin> pageResult = new PageResult<>(dbSysLogins.getPageNum(), dbSysLogins.getPageSize(), dbSysLogins.getTotal());
        List<SysLogin> sysLoginList = dbSysLogins.stream().map(dbSysLogin -> {
            SysLogin login = new SysLogin();
            BeanUtils.copyProperties(dbSysLogin, login);
            return login;
        }).collect(Collectors.toList());
        pageResult.setData(sysLoginList);
        return pageResult;
    }


    public PageResult<SysLoginVo> querySysLoginVoWithPage(SysLoginVo sysLoginVo, Integer pageNum, Integer pageSize) {
        //分页参数
        if (null == pageNum || pageNum <= 1) {
            pageNum = 1;
        }
        if (null == pageSize || pageSize <= 0) {
            pageSize = 10;
        }
        PageHelper.startPage(pageNum, pageSize);
        DbSysLoginVo dbSysLoginVo = new DbSysLoginVo();
        BeanUtils.copyProperties(sysLoginVo, dbSysLoginVo);
        if(null!=dbSysLoginVo && StrUtil.isNotEmpty(dbSysLoginVo.getUserName())){
            dbSysLoginVo.setUserName("%"+dbSysLoginVo.getUserName().replace(" ","")+"%");
        }
        if(StrUtil.isEmpty(dbSysLoginVo.getAccount())){
            dbSysLoginVo.setAccount(null);
        }
        if(StrUtil.isEmpty(dbSysLoginVo.getOrganOid())){
            dbSysLoginVo.setOrganOid(null);
        }
        String loginDistrictOid = CurrentLoginUserHolder.getCurrentLoginUser().getDistrictOid();
        if(StrUtil.isNotEmpty(loginDistrictOid)){
            dbSysLoginVo.setLoginDistrictOid("%"+loginDistrictOid+"%");
        }
        Page<DbSysLoginVo> dbDbSysLoginVos = (Page<DbSysLoginVo>) dbSysLoginMapper.selectByDbSysLoginVo(dbSysLoginVo);
        PageResult<SysLoginVo> pageResult = new PageResult<>(dbDbSysLoginVos.getPageNum(), dbDbSysLoginVos.getPageSize(), dbDbSysLoginVos.getTotal());
        List<SysLoginVo> sysLoginList = dbDbSysLoginVos.stream().map(dbSysLogin -> {
            SysLoginVo login = new SysLoginVo();
            BeanUtils.copyProperties(dbSysLogin, login);
            return login;
        }).collect(Collectors.toList());
        pageResult.setData(sysLoginList);
        return pageResult;
    }

    @Cacheable( key = "'getSysLoginByUserOid:'+#userOid", unless = "#result == null")
    public SysLogin getSysLoginByUserOid(String userOid) {
        if (null == userOid) {
            return null;
        }
        DbSysLogin dbSysLogin = dbSysLoginMapper.selectByUserOid(userOid);
        if (null == dbSysLogin) {
            return null;
        }
        SysLogin sysLogin = new SysLogin();
        BeanUtils.copyProperties(dbSysLogin, sysLogin);
        return sysLogin;
    }

    @CacheEvict(allEntries = true)
    public void updateByPrimaryKey(SysLogin sysLogin) {
        if (sysLogin != null && sysLogin.getId() != null) {
            DbSysLogin dbSysLogin=new DbSysLogin();
            BeanUtils.copyProperties(sysLogin,dbSysLogin);
            dbSysLogin.setModifyDate(new Date());
            dbSysLoginMapper.updateByPrimaryKey(dbSysLogin);
        }
    }

    private DbSysLoginExample buildDbSysLoginExample(SysLogin sysLogin) {
        DbSysLoginExample dbSysLoginExample = new DbSysLoginExample();
        DbSysLoginExample.Criteria criteria = dbSysLoginExample.createCriteria();
        if (sysLogin != null) {
            if (StrUtil.isNotBlank(sysLogin.getAccount())) {
                criteria.andAccountEqualTo(sysLogin.getAccount().trim());
            }
            if (StrUtil.isNotBlank(sysLogin.getName())) {
                criteria.andNameLike(StrUtil.format("%{}%", sysLogin.getName().trim()));
            }
            if (null!=sysLogin.getIsAble()) {
                criteria.andIsAbleEqualTo(sysLogin.getIsAble());
            }
        }
        criteria.andIsDeleteEqualTo(BaseStaticParameter.N);
        return dbSysLoginExample;
    }
}
