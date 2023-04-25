package com.zfsoft.microservice.platform.manager.sys;

import cn.hutool.core.util.IdUtil;
import com.zfsoft.microservice.platform.data.sys.SysPermissionLink;
import com.zfsoft.microservice.platform.dbaccess.dao.sys.DbSysPermissionLinkMapper;
import com.zfsoft.microservice.platform.dbaccess.data.sys.DbSysPermissionLink;
import com.zfsoft.microservice.platform.dbaccess.data.sys.DbSysPermissionLinkExample;
import com.zfsoft.platform.utils.validate.ParamValidException;
import org.apache.commons.lang3.StringUtils;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 权限链接service实现
 * @author zxx
 * @date 2020/9/10 3:19 下午
 */
@Service
@CacheConfig(cacheNames = "sys:permissionLink")
public class SysPermissionLinkManager {

    @Resource
    private DbSysPermissionLinkMapper dbSysPermissionLinkMapper;



    @Transactional
    @ShardingTransactionType(TransactionType.LOCAL)
    @CacheEvict(cacheNames = {"sys:permission","sys:permissionLink"},allEntries = true)
    public int saveSysPermissionLinks(List<SysPermissionLink> linkList) {
//        if(linkList==null||linkList.size()==0){
//            throw new ParamValidException("请先新增权限链接后再确认");
//        }
        Set<String> parentOids=new HashSet<>();
        linkList.forEach(link->{
            if(link.getPermissionOid() == null){
                throw new ParamValidException("上级权限无效,无法保存");
            }
            parentOids.add(link.getPermissionOid());
        });
        if(parentOids.size()>1){
            throw new ParamValidException("上级权限无效,无法保存");
        }
        parentOids.forEach(parentOid->{
            this.deleteByPermissionOid(parentOid);
        });
        linkList.forEach(link->{
            if(StringUtils.isNotEmpty(link.getPerLink())) {
                this.saveSysPermissionLink(link);
            }
        });
        return 0;
    }

    public int saveSysPermissionLink(SysPermissionLink sysPermissionLink) {
        if(null==sysPermissionLink.getPermissionOid()){
            throw new ParamValidException("上级权限无效");
        }
        sysPermissionLink.setId(null);
        sysPermissionLink.setModifyDate(new Date());
        sysPermissionLink.setPermissionLinkOid(IdUtil.simpleUUID());
        DbSysPermissionLink dbSysPermissionLink=new DbSysPermissionLink();
        BeanUtils.copyProperties(sysPermissionLink,dbSysPermissionLink);
        if (null==dbSysPermissionLink.getId()){
            return dbSysPermissionLinkMapper.insert(dbSysPermissionLink);
        }else {
            return dbSysPermissionLinkMapper.updateByPrimaryKey(dbSysPermissionLink);
        }
    }

    @Cacheable( key = "'getSysPermissionLinkById:'+#oid", unless = "#result == null")
    public SysPermissionLink getSysPermissionLinkById(Long oid) {
        if(null==oid){
            return null;
        }
        DbSysPermissionLink dbSysPermissionLink = dbSysPermissionLinkMapper.selectByPrimaryKey(oid);
        if(dbSysPermissionLink == null){
            return null;
        }
        SysPermissionLink sysPermissionLink = new SysPermissionLink();
        BeanUtils.copyProperties(dbSysPermissionLink,sysPermissionLink);
        return sysPermissionLink;
    }

    @Cacheable( key = "'querySysPermissionLinkByPermissionOid:'+#permissionOid", unless = "#result == null")
    public List<SysPermissionLink> querySysPermissionLinkByPermissionOid(List<String> permissionOidList) {
        DbSysPermissionLinkExample dbSysPermissionLinkExample=new DbSysPermissionLinkExample();
        DbSysPermissionLinkExample.Criteria criteria = dbSysPermissionLinkExample.createCriteria();
        criteria.andPermissionOidIn(permissionOidList);
        List<DbSysPermissionLink> dbSysPermissionLinks = dbSysPermissionLinkMapper.selectByExample(dbSysPermissionLinkExample);
        List<SysPermissionLink> sysPermissionLinkList=dbSysPermissionLinks.stream().map(dbSysPermissionLink -> {
            SysPermissionLink sysPermissionLink = new SysPermissionLink();
            BeanUtils.copyProperties(dbSysPermissionLink,sysPermissionLink);
            return sysPermissionLink;
        }).collect(Collectors.toList());
        return sysPermissionLinkList;
    }

    public int deleteByPermissionOid(String permissionOid) {
        if(permissionOid != null){
            dbSysPermissionLinkMapper.deleteByPermissionOid(permissionOid);
        }
        return 0;
    }
}
