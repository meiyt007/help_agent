package com.zfsoft.microservice.platform.manager.sys;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zfsoft.microservice.platform.data.sys.SysApp;
import com.zfsoft.microservice.platform.dbaccess.dao.sys.DbSysAppMapper;
import com.zfsoft.microservice.platform.dbaccess.dao.sys.DbSysRoleMapper;
import com.zfsoft.microservice.platform.dbaccess.data.sys.DbSysApp;
import com.zfsoft.microservice.platform.dbaccess.data.sys.DbSysAppExample;
import com.zfsoft.platform.common.data.BaseStaticParameter;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.platform.utils.validate.ParamValid;
import com.zfsoft.platform.utils.validate.ResultInfoException;
import com.zfsoft.platform.utils.validate.ValidGroups;
import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.transaction.annotation.ShardingTransactionType;
import org.apache.shardingsphere.transaction.core.TransactionType;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName SysAppServiceImpl
 * @Description: 应用接口实现类
 * @Author wuxx
 * @Date 2020/9/7
 **/
@Service
@Slf4j
@CacheConfig(cacheNames = "sys:app")
public class SysAppManager{

    @Resource
    private DbSysRoleMapper dbSysRoleMapper;

    @Resource
    private DbSysAppMapper dbSysAppMapper;


    @Transactional
    @ParamValid
    @ShardingTransactionType(TransactionType.LOCAL)
    @CacheEvict(allEntries = true)
    public int saveSysApp(@ValidGroups(groups = {SysApp.INSERT_GROUP.class})SysApp sysApp) {
        if (sysApp == null) {
            throw new ResultInfoException("应用信息不正确!");
        }
        if (null == sysApp.getId()) {
            sysApp.setId(null);
            //生成业务主键
            sysApp.setAppOid(IdUtil.simpleUUID());
        } else {
            // 应用oid不为空
            SysApp curDict = getSysAppById(sysApp.getId());
            if (curDict == null) {
                throw new ResultInfoException("应用编号未查询到相应的应用信息!");
            }
        }
        if (null==sysApp.getCreateDate()){
            sysApp.setCreateDate(new Date());
        }
        // 设置应用信息的状态
        if (sysApp.getIsDelete() == null) {
            sysApp.setIsDelete(BaseStaticParameter.N);
        }
        if (sysApp.getIsAble() == null) {
            sysApp.setIsAble(BaseStaticParameter.Y);
        }
        sysApp.setModifyDate(new Date());
        DbSysApp dbSysApp = new DbSysApp();
        BeanUtils.copyProperties(sysApp,dbSysApp);
        if (null == sysApp.getId()) {
            return dbSysAppMapper.insert(dbSysApp);
        }else {
            return dbSysAppMapper.updateByPrimaryKeySelective(dbSysApp);
        }
    }


    /**
     * 删除应用
     *
     * @param oid 主键id
     * @return int 1表示成功，0表示失败
     * @author ningzz
     * @Date 2020/11/12
     **/
    @Transactional
    @ShardingTransactionType(TransactionType.LOCAL)
    @CacheEvict(allEntries = true)
    public int deleteSysAppById(Long oid) {
        DbSysApp dbSysApp = dbSysAppMapper.selectByPrimaryKey(oid);
        if(dbSysApp == null){
            throw new ResultInfoException("应用信息为空！");
        }
        if (dbSysApp.getAppOid() != null) {
            int i = dbSysRoleMapper.selectRoleByAppOid(dbSysApp.getAppOid());
            if (i > 0){
                // 应用下存在未删除的角色时禁止删除应用
                return BaseStaticParameter.N;
            }
        }
        dbSysAppMapper.deleteByOid(oid);
        // 同时删除该应用下的角色
        // dbSysRoleMapper.deleteSysRoleByAppOid(dbSysApp.getAppOid());
        return BaseStaticParameter.Y;
    }


    /**
     * 启禁用应用
     *
     * @param oid 主键id
     * @return int 1表示成功，0表示失败
     * @author ningzz
     * @Date 2020/11/12
     **/
    @Transactional
    @CacheEvict(allEntries = true)
    public int ableSysAppById(Long oid) {
        DbSysApp dbSysApp = dbSysAppMapper.selectByPrimaryKey(oid);
        if(dbSysApp == null){
            throw new ResultInfoException("应用信息为空！");
        }
        Integer isAble = dbSysApp.getIsAble();
        if(BaseStaticParameter.N==isAble){
            dbSysApp.setIsAble(BaseStaticParameter.Y);
        }else {
            // 判断应用下是否存在启用状态角色
            if (dbSysApp.getAppOid() != null) {
                int i = dbSysRoleMapper.selectAbleRoleByAppOid(dbSysApp.getAppOid());
                if (i > 0){
                    return BaseStaticParameter.N;
                }
            }
            dbSysApp.setIsAble(BaseStaticParameter.N);
        }
        dbSysAppMapper.updateByPrimaryKeySelective(dbSysApp);
        return BaseStaticParameter.Y;
    }

    @Cacheable(key = "'getSysAppById:'+#id", unless = "#result == null")
    public SysApp getSysAppById(Long id) {
        DbSysApp dbSysApp = dbSysAppMapper.selectByPrimaryKey(id);
        if(dbSysApp == null)
            throw new ResultInfoException("应用信息为空！");
        SysApp sysApp = new SysApp();
        BeanUtils.copyProperties(dbSysApp,sysApp);
        return sysApp;
    }

    @Cacheable(key = "'getSysAppByAppOid:'+#appOid", unless = "#result == null")
    public SysApp getSysAppByAppOid(String appOid) {
        DbSysApp dbSysApp = dbSysAppMapper.selectDbSysAppByAppOid(appOid);
        if(dbSysApp == null)
            throw new ResultInfoException("应用信息为空！");
        SysApp sysApp = new SysApp();
        BeanUtils.copyProperties(dbSysApp,sysApp);
        return sysApp;
    }

    public PageResult<SysApp> querySysAppWithPage(SysApp sysApp, Integer pageNumber, Integer pageSize) {
        //分页参数
        if (null == pageNumber || pageNumber <=1) {
            pageNumber = 1;
        }
        if (null == pageSize || pageSize <= 0) {
            pageSize = 10;
        }
        PageHelper.startPage(pageNumber,pageSize);
        DbSysAppExample dbSysAppExample = new DbSysAppExample();
        DbSysAppExample.Criteria criteria = dbSysAppExample.createCriteria();
        if(null!=sysApp){
            if(StrUtil.isNotEmpty(sysApp.getName())){
                criteria.andNameLike("%"+sysApp.getName().trim()+"%");
            }
            if(null!= sysApp.getParentOid()){
                criteria.andParentOidEqualTo(sysApp.getParentOid());
            }
        }
        criteria.andIsDeleteEqualTo(BaseStaticParameter.N);
        dbSysAppExample.setOrderByClause(" sort asc ");
        Page<DbSysApp> dbSysApps = (Page<DbSysApp>)dbSysAppMapper.selectByExample(dbSysAppExample);
        PageResult<SysApp> pageResult = new PageResult<>(dbSysApps.getPageNum(),dbSysApps.getPageSize(),dbSysApps.getTotal());
        List<SysApp> sysAppList = dbSysApps.stream().map(dbSysApp -> {
            SysApp app = new SysApp();
            BeanUtils.copyProperties(dbSysApp,app);
            return app;
        }).collect(Collectors.toList());
        pageResult.setData(sysAppList);
        return pageResult;
    }


    public List<SysApp> querySysApp(SysApp sysApp) {
        DbSysAppExample dbSysAppExample = new DbSysAppExample();
        DbSysAppExample.Criteria criteria = dbSysAppExample.createCriteria();
        if(null!=sysApp){
            if(StrUtil.isNotEmpty(sysApp.getName())){
                criteria.andNameLike("%"+sysApp.getName()+"%");
            }
            if(null!= sysApp.getParentOid()){
                criteria.andParentOidEqualTo(sysApp.getParentOid());
            }
        }
        criteria.andIsDeleteEqualTo(BaseStaticParameter.N);
        criteria.andIsAbleEqualTo(BaseStaticParameter.Y);
        List<DbSysApp> dbSysApps = dbSysAppMapper.selectByExample(dbSysAppExample);
        List<SysApp> sysAppList = dbSysApps.stream().map(dbSysApp -> {
            SysApp app = new SysApp();
            BeanUtils.copyProperties(dbSysApp, app);
            return app;
        }).collect(Collectors.toList());
        return sysAppList;

    }

}
