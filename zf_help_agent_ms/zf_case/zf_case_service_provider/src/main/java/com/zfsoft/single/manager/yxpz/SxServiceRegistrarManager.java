package com.zfsoft.single.manager.yxpz;

import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Lists;
import com.zfsoft.cases.feign.SysDistrictFeignService;
import com.zfsoft.cases.feign.SysOrganFeginService;
import com.zfsoft.cases.feign.SysUserFeginService;
import com.zfsoft.cases.util.GenDataTreeUtil;
import com.zfsoft.microservice.platform.data.sys.SysDistrict;
import com.zfsoft.microservice.platform.data.sys.SysOrgan;
import com.zfsoft.microservice.platform.data.sys.SysUser;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.security.data.CurrentLoginUser;
import com.zfsoft.platform.common.security.data.CurrentLoginUserHolder;
import com.zfsoft.service.common.data.TreeSelect;
import com.zfsoft.service.sxService.data.SxService;
import com.zfsoft.service.sxService.service.SxServiceService;
import com.zfsoft.single.data.yxpz.SxServiceRegistrar;
import com.zfsoft.single.dbaccess.dao.DbSxServiceRegistrarMapper;
import com.zfsoft.single.dbaccess.data.DbSxServiceRegistrar;
import com.zfsoft.single.dbaccess.data.DbSxServiceRegistrarExample;
import com.zfsoft.single.util.BeanUtils;
import com.zfsoft.single.util.SysCode;
import com.zfsoft.single.util.UUIDUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author: dongxl
 * @create: 2020-10-26
 * @description: 授权登记人服务层
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@CacheConfig(cacheNames = {"registerService"})
public class SxServiceRegistrarManager{

    private final DbSxServiceRegistrarMapper dbSxServiceRegistrarMapper;

    private final SysDistrictFeignService sysDistrictFeginService;
    private final SysOrganFeginService sysOrganFeginService;
    private final SxServiceService sxServiceFeginService;
    private final SysUserFeginService sysUserFeginService;

    @Transactional(rollbackFor = Exception.class)
    public void saveOrUpdate(SxServiceRegistrar sxServiceRegistrar) {
        //根据事项id查询所有的授权登记信息
        Assert.hasLength(sxServiceRegistrar.getServiceOid(), "事项主键不能为空！");
        DbSxServiceRegistrarExample example = new DbSxServiceRegistrarExample();
        example.createCriteria()
                .andServiceOidEqualTo(sxServiceRegistrar.getServiceOid())
                .andDelFlagEqualTo(SysCode.DELETE_STATUS.NO);
        example.setOrderByClause("MODIFY_DATE desc");
        List<DbSxServiceRegistrar> dbSxServiceRegistrar = this.dbSxServiceRegistrarMapper.selectByExample(example);
        Map<String,DbSxServiceRegistrar> registrarMap = new HashMap<>();
        if(null != dbSxServiceRegistrar && dbSxServiceRegistrar.size()>0){
            for(DbSxServiceRegistrar registrar : dbSxServiceRegistrar){
                registrarMap.put(registrar.getUserOid(),registrar);
            }
        }
        /*if(!CollectionUtils.isEmpty(dbSxServiceRegistrar)){
            for(DbSxServiceRegistrar sx: dbSxServiceRegistrar){
                sx.setDelFlag(SysCode.DELETE_STATUS.YES);
                this.dbSxServiceRegistrarMapper.updateByPrimaryKey(sx);
            }
        }*/
        //重新生成授权登记信息
        if (StringUtils.isNotEmpty(sxServiceRegistrar.getUserOids())) {
            List<String> result = Arrays.asList(sxServiceRegistrar.getUserOids().split(";"));
            Stream.iterate(0, i -> i + 1).limit(Optional.ofNullable(result).orElseGet(Lists::newArrayList).size())
                    .filter(flag -> sxServiceRegistrar.getId()==null || sxServiceRegistrar.getId()==0L )
                    .forEach(ind -> {
                        if(null == registrarMap.get(result.get(ind))){
                            DbSxServiceRegistrar dbsxreg=new DbSxServiceRegistrar();
                            BeanUtils.copyProperties(sxServiceRegistrar, dbsxreg,"userOids");
                            dbsxreg.setRegistrarOid(UUIDUtil.randomUUID());
                            dbsxreg.setCreateDate(new Date());
                            dbsxreg.setDelFlag(SysCode.DELETE_STATUS.NO);
                            dbsxreg.setModifyDate(new Date());
                            dbsxreg.setCreateBy(CurrentLoginUserHolder.getCurrentLoginUser().getUserOid().toString());
                            // dbsxreg.setServiceOid(sxServiceRegistrar.getServiceOid());
                            dbsxreg.setUserOid(result.get(ind));
                            this.dbSxServiceRegistrarMapper.insert(dbsxreg);
                        }else{
                            registrarMap.remove(result.get(ind));
                        }
                    });
        }
        if(null != registrarMap && registrarMap.size()>0){
            DbSxServiceRegistrar registrar = null;
            for(Map.Entry<String,DbSxServiceRegistrar> strar : registrarMap.entrySet()){
                registrar = strar.getValue();
                registrar.setDelFlag(SysCode.DELETE_STATUS.YES);
                this.dbSxServiceRegistrarMapper.updateByPrimaryKey(registrar);
            }
        }
    }

    public SxServiceRegistrar getOneByServiceOid(String id) {
        Assert.hasLength(id, "主键不能为空！");
        DbSxServiceRegistrarExample example = new DbSxServiceRegistrarExample();
        example.createCriteria()
                .andServiceOidEqualTo(id)
                .andDelFlagEqualTo(SysCode.DELETE_STATUS.NO);
        example.setOrderByClause("MODIFY_DATE desc");
        List<DbSxServiceRegistrar> dbSxServiceRegistrar = this.dbSxServiceRegistrarMapper.selectByExample(example);
        if(!CollectionUtils.isEmpty(dbSxServiceRegistrar)){
            SxServiceRegistrar ss=new SxServiceRegistrar();
            ss.setUserOids(Optional.ofNullable(dbSxServiceRegistrar).orElseGet(Lists::newArrayList).stream().map(DbSxServiceRegistrar::getUserOid).collect(Collectors.joining(";")));
            return ss;
        }
        return null;
    }

    public List<SysUser> getSysUserRegistrarList(String id){
        List<SysUser> sysUsers = new ArrayList<>();
        DbSxServiceRegistrarExample example = new DbSxServiceRegistrarExample();
        example.createCriteria()
                .andServiceOidEqualTo(id)
                .andDelFlagEqualTo(SysCode.DELETE_STATUS.NO);
        example.setOrderByClause("MODIFY_DATE desc");
        List<DbSxServiceRegistrar> dbSxServiceRegistrar = this.dbSxServiceRegistrarMapper.selectByExample(example);
        SysUser sysUser = null;
        for(DbSxServiceRegistrar registrar : dbSxServiceRegistrar){
            if(StrUtil.isNotEmpty(registrar.getUserOid())){
                sysUser = sysUserFeginService.getSysUserByUserOid(registrar.getUserOid()).getData();
                if(null != sysUser){
                    sysUsers.add(sysUser);
                }
            }

        }
        return sysUsers;
    }

    @Transactional(rollbackFor = Exception.class)
    public void saveOrUpdatePersonReg(SxServiceRegistrar sxServiceRegistrar) {
        //根据人员id查询所有授权的事项主键
       // Assert.hasLength(sxServiceRegistrar.getUserOid(), "人员主键不能为空！");
        DbSxServiceRegistrarExample example = new DbSxServiceRegistrarExample();
        example.createCriteria()
                .andUserOidEqualTo(sxServiceRegistrar.getUserOid())
                .andDelFlagEqualTo(SysCode.DELETE_STATUS.NO);
        example.setOrderByClause("MODIFY_DATE desc");
        List<DbSxServiceRegistrar> dbSxServiceRegistrar = this.dbSxServiceRegistrarMapper.selectByExample(example);
        if(!CollectionUtils.isEmpty(dbSxServiceRegistrar)){
            for(DbSxServiceRegistrar sx: dbSxServiceRegistrar){
                sx.setDelFlag(SysCode.DELETE_STATUS.YES);
                this.dbSxServiceRegistrarMapper.updateByPrimaryKey(sx);
            }
        }
        //重新生成授权登记信息
        if (StringUtils.isNotEmpty(sxServiceRegistrar.getUserOids())) {
            List<String> result = Arrays.asList(sxServiceRegistrar.getUserOids().split(";"));
            Stream.iterate(0, i -> i + 1).limit(Optional.ofNullable(result).orElseGet(Lists::newArrayList).size())
                    .forEach(ind -> {
                        ApiResultSet<HashMap> sxservice=this.sxServiceFeginService.viewSxService(result.get(ind));
                        HashMap sx=sxservice.getData();
                        DbSxServiceRegistrar dbsxreg=new DbSxServiceRegistrar();
                        BeanUtils.copyProperties(sxServiceRegistrar, dbsxreg,"userOids");
                        dbsxreg.setRegistrarOid(UUIDUtil.randomUUID());
                        dbsxreg.setCreateDate(new Date());
                        dbsxreg.setDelFlag(SysCode.DELETE_STATUS.NO);
                        dbsxreg.setModifyDate(new Date());
                        dbsxreg.setCreateBy(CurrentLoginUserHolder.getCurrentLoginUser().getUserOid().toString());
                        dbsxreg.setServiceOid(result.get(ind));
                        if(sx!=null){
                            SxService sxService= (SxService) sx.get("sxService");
                            if(sxService!=null){
                                dbsxreg.setOrganName(sxService.getOrganName()==null? null:sxService.getOrganName());
                                dbsxreg.setOrganOid(sxService.getOrganOid()==null? null:sxService.getOrganOid());
                                dbsxreg.setServiceName(sxService.getServiceName()==null? null:sxService.getServiceName());
                                dbsxreg.setServiceType(sxService.getServiceTypeOid()==null? null:sxService.getServiceTypeOid());
                                dbsxreg.setServiceTypeName(sxService.getServiceTypeName()==null? null:sxService.getServiceTypeName());
                                dbsxreg.setServiceObject(sxService.getServiceObject()==null? null:sxService.getServiceObject());
                            }

                        }
                        this.dbSxServiceRegistrarMapper.insertSelective(dbsxreg);
                    });
        }
    }

    /**
     * 根据用户主键查询授权信息
     * @param id
     * @return
     */
    public SxServiceRegistrar getOneByUserOid(String id) {
        Assert.hasLength(id, "用户主键不能为空！");
        DbSxServiceRegistrarExample example = new DbSxServiceRegistrarExample();
        example.createCriteria()
                .andUserOidEqualTo(id)
                .andDelFlagEqualTo(SysCode.DELETE_STATUS.NO);
        example.setOrderByClause("MODIFY_DATE desc");
        List<DbSxServiceRegistrar> dbSxServiceRegistrar = this.dbSxServiceRegistrarMapper.selectByExample(example);
        if(!CollectionUtils.isEmpty(dbSxServiceRegistrar)){
            SxServiceRegistrar ss=new SxServiceRegistrar();
            String userOids = "";
            for (DbSxServiceRegistrar dssr: dbSxServiceRegistrar) {
                String serviceOid = dssr.getServiceOid();
                ApiResultSet<HashMap> apiResultSet = sxServiceFeginService.viewSxService(serviceOid);
                HashMap data = apiResultSet.getData();
                if (data !=null) {
                    userOids = serviceOid + ";" + userOids;
                }
            }
            ss.setUserOids(userOids);
            //ss.setUserOids(Optional.ofNullable(dbSxServiceRegistrar).orElseGet(Lists::newArrayList).stream().map(DbSxServiceRegistrar::getServiceOid).collect(Collectors.joining(";")));
            return ss;
        }
        return null;
    }

    /**
     * 根据区划机构事项树
     * @return
     */
    public List<TreeSelect> queryServiceTree() {
        CurrentLoginUser currentLoginUser = CurrentLoginUserHolder.getCurrentLoginUser();
        if (null != currentLoginUser) {
            ApiResultSet resultSet = this.sysDistrictFeginService.querySysDistrictListByParentOid(null);
            List<SysDistrict> sysDistrictList= (List<SysDistrict>) resultSet.getData();
            ApiResultSet<List<SysOrgan>> sysOrganResultSet=this.sysOrganFeginService.querySysOrganListByParentOid(null);
            List<SysOrgan> sysOrganList=sysOrganResultSet.getData();
            ApiResultSet<List<SxService>> sxResult=this.sxServiceFeginService.getSxServicList(null);
            List<SxService> sxServiceList=null;
            if(sxResult!=null&&sxResult.getCode()==200){
              sxServiceList=  sxResult.getData();
            }
            if(sxServiceList!=null){
                List<TreeSelect> treeSelects = GenDataTreeUtil.buildDistrictAndOrganAndServiceTreeSelect(sysDistrictList, sysOrganList, sxServiceList);
                return treeSelects;
            }
        }
        return null;
    }
    /**
     * 根据区划机构事项树
     * @return
     */
    public List<TreeSelect> queryServiceTreeForUser() {
        CurrentLoginUser currentLoginUser = CurrentLoginUserHolder.getCurrentLoginUser();
        if (null != currentLoginUser) {
            ApiResultSet resultSet = this.sysDistrictFeginService.querySysDistrictListByParentOid(null);
            List<SysDistrict> sysDistrictList= (List<SysDistrict>) resultSet.getData();
            ApiResultSet<List<SysOrgan>> sysOrganResultSet=this.sysOrganFeginService.querySysOrganListByParentOid(null);
            List<SysOrgan> sysOrganList=sysOrganResultSet.getData();
            List<SysUser> userList = new ArrayList<>();
            sysOrganList.forEach(sysOrgan -> {
                userList.addAll(this.sysUserFeginService.getSysUserListByOrganOid(sysOrgan.getOrganOid()).getData()) ;
            });
            if(userList!=null){
                List<TreeSelect> treeSelects = GenDataTreeUtil.buildDistrictAndOrganAndUserTreeSelect(sysDistrictList, sysOrganList, userList);
                return treeSelects;
            }
        }
        return null;
    }
    public List<SxServiceRegistrar> listSxServiceByUserOid(String userOid) {
        List<DbSxServiceRegistrar> dbSxServiceRegistrars = dbSxServiceRegistrarMapper.selectSxServiceRegistrarListByUserOid(userOid);
        List<SxServiceRegistrar> sxServiceRegistrarList = dbSxServiceRegistrars.stream().map(dbSxServiceRegistrar -> {
            SxServiceRegistrar sxServiceRegistrar = new SxServiceRegistrar();
            org.springframework.beans.BeanUtils.copyProperties(dbSxServiceRegistrar,sxServiceRegistrar);
            return sxServiceRegistrar;
        }).collect(Collectors.toList());
        return sxServiceRegistrarList;
    }

    /**
     * 根据事项OID取消授权
     * @param serviceOid
     */
    public int delSxServiceRegistrarByServiceOid(String serviceOid) {
        Assert.hasLength(serviceOid, "事项主键不能为空！");
        if(StrUtil.isNotEmpty(serviceOid)){
            int index = this.dbSxServiceRegistrarMapper.delSxServiceRegistrarByServiceOid(serviceOid);
            return index;
        }
        return 0;
    }

    /*根据用户OID取消授权
     * @Description:
     * @Author: wangxl
     * @Date: 2020/12/30 17:06
     * @param userOid:
     * @return: int
     **/
    public int delSxServiceRegistrarByUserOid(String userOid) {
        Assert.hasLength(userOid, "人员主键不能为空！");
        if(StrUtil.isNotEmpty(userOid)){
            int index = this.dbSxServiceRegistrarMapper.delSxServiceRegistrarByUserOid(userOid);
            return index;
        }
        return 0;
    }

    @Cacheable(key = "'serviceOidsByUserOid:userOid='+ #userOid", unless = "#result == null")
    public List sxServiceOidsListByUserOid(String userOid){
        List oids=dbSxServiceRegistrarMapper.sxServiceOidsListByUserOid(userOid);
        return  oids;
    }

    /**
     *  查询事项是否被授权
     * @param serviceOid
     * @return
     */
    public boolean getServiceRegistrarByServiceOid(String serviceOid) {
        List<String> serviceOids = dbSxServiceRegistrarMapper.getServiceRegistrarByServiceOid(serviceOid);
        if (serviceOids !=null && serviceOids.size()>0) {
            return false;
        } else {
            return true;
        }

    }
}
