package com.zfsoft.service.manager.sxService;

import cn.hutool.core.util.StrUtil;
import com.zfsoft.service.dbaccess.dao.sxService.DbSxServiceLinkMapper;
import com.zfsoft.service.dbaccess.dao.sxService.DbSxServiceLinkSpecialMapper;
import com.zfsoft.service.dbaccess.data.sxService.*;
import com.zfsoft.platform.utils.validate.ResultInfoException;
import com.zfsoft.service.common.SxptBaseStaticParameter;
import com.zfsoft.service.sxService.data.SxServiceLink;
import com.zfsoft.service.sxService.data.SxServiceLinkSpecial;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.text.MessageFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @ClassName SxServiceLinkServiceImpl
 * @Description: 实施清单-环节信息 实现类
 * @Author wangxl
 * @Date 2020/10/26
 **/
@Service
@Slf4j
@CacheConfig(cacheNames = "service:sxServiceLink")
public class SxServiceLinkManager {

        @Resource
        private DbSxServiceLinkMapper dbSxServiceLinkMapper;
        @Resource
        private DbSxServiceLinkSpecialMapper dbSxServiceLinkSpecialMapper;

    //@Cacheable(key = "'getSxServiceLinkByOid:oid=' + #oid", unless = "#result == null")
        public SxServiceLink getSxServiceLinkByOid(String oid){
            DbSxServiceLinkWithBLOBs dbSxServiceLinkWithBLOBs = dbSxServiceLinkMapper.getSxServiceLinkByOid(oid);
            if(null == dbSxServiceLinkWithBLOBs){
                throw new ResultInfoException("实施清单环节信息为空！");
            }
            SxServiceLink sxServiceLink = new SxServiceLink();
            BeanUtils.copyProperties(dbSxServiceLinkWithBLOBs,sxServiceLink);
            DbSxServiceLinkSpecialExample dbSxServiceLinkSpecialExample = new DbSxServiceLinkSpecialExample();
            DbSxServiceLinkSpecialExample.Criteria specialCriteria = dbSxServiceLinkSpecialExample.createCriteria();
            specialCriteria.andServiceLinkOidEqualTo(sxServiceLink.getLinkOid());
            specialCriteria.andDelFlagEqualTo(SxptBaseStaticParameter.ZERO);
            List<DbSxServiceLinkSpecial> dbSxServiceLinkSpecials = dbSxServiceLinkSpecialMapper.selectByExample(dbSxServiceLinkSpecialExample);
            List<SxServiceLinkSpecial> serviceLinkSpecialList = dbSxServiceLinkSpecials.stream().map(sxServiceLinkSpecial -> {
                SxServiceLinkSpecial serviceLinkSpecial = new SxServiceLinkSpecial();
                BeanUtils.copyProperties(sxServiceLinkSpecial,serviceLinkSpecial);
                return serviceLinkSpecial;
            }).collect(Collectors.toList());
            if(null != serviceLinkSpecialList && serviceLinkSpecialList.size()>0){
                sxServiceLink.setServiceLinkSpecials(serviceLinkSpecialList);
            }
            return sxServiceLink;
        }

    //@Cacheable(key = "'getSxServiceLinkByServiceOid:serviceOid=' + #serviceOid", unless = "#result == null")
        public List<SxServiceLink> getSxServiceLinkByServiceOid(String serviceOid) {
            DbSxServiceLinkExample dbSxServiceLinkExample = new DbSxServiceLinkExample();
            DbSxServiceLinkExample.Criteria criteria = dbSxServiceLinkExample.createCriteria();
            if(StrUtil.isNotEmpty(serviceOid)){
                criteria.andServiceOidEqualTo(serviceOid);
            }
            criteria.andDelFlagEqualTo(SxptBaseStaticParameter.ZERO);
            criteria.andLinkOidIsNotNull();
            dbSxServiceLinkExample.setOrderByClause(" link_sort");
            DbSxServiceLinkSpecialExample dbSxServiceLinkSpecialExample = new DbSxServiceLinkSpecialExample();
            DbSxServiceLinkSpecialExample.Criteria specialCriteria = dbSxServiceLinkSpecialExample.createCriteria();
            List<DbSxServiceLink> dbSxServiceLinks = dbSxServiceLinkMapper.selectByExample(dbSxServiceLinkExample);
            List<SxServiceLink> sxServiceLinkList = dbSxServiceLinks.stream().map(sxServiceLink -> {
                SxServiceLink serviceLink = new SxServiceLink();
                BeanUtils.copyProperties(sxServiceLink,serviceLink);
                if(StrUtil.isNotEmpty(serviceLink.getLinkOid())){
                    specialCriteria.andServiceLinkOidEqualTo(serviceLink.getLinkOid());
                    specialCriteria.andDelFlagEqualTo(SxptBaseStaticParameter.ZERO);
                    List<DbSxServiceLinkSpecial> dbSxServiceLinkSpecials = dbSxServiceLinkSpecialMapper.selectByExample(dbSxServiceLinkSpecialExample);
                    List<SxServiceLinkSpecial> serviceLinkSpecialList = dbSxServiceLinkSpecials.stream().map(sxServiceLinkSpecial -> {
                        SxServiceLinkSpecial serviceLinkSpecial = new SxServiceLinkSpecial();
                        BeanUtils.copyProperties(sxServiceLinkSpecial,serviceLinkSpecial);
                        return serviceLinkSpecial;
                    }).collect(Collectors.toList());
                    serviceLink.setServiceLinkSpecials(serviceLinkSpecialList);//收费分段参数配置
                }
                return serviceLink;
            }).collect(Collectors.toList());
            return sxServiceLinkList;
        }

        public List<DbSxServiceLinkWithBLOBs> queryList(SxServiceLink sxServiceLink) {
            DbSxServiceLinkWithBLOBs dbSxServiceLinkWithBLOBs = new DbSxServiceLinkWithBLOBs();
            BeanUtils.copyProperties(sxServiceLink, dbSxServiceLinkWithBLOBs);
            List<DbSxServiceLinkWithBLOBs> dbSxServiceLinkWithBLOBsList = dbSxServiceLinkMapper.queryDbSxServiceLinkList(dbSxServiceLinkWithBLOBs);
            return dbSxServiceLinkWithBLOBsList;
        }

        public void saveOrUpdateSxServiceLink(SxServiceLink sxServiceLink) {
            if (null != sxServiceLink.getId()) {
                DbSxServiceLinkWithBLOBs dbSxServiceLinkWithBLOBs = dbSxServiceLinkMapper.selectByPrimaryKey(sxServiceLink.getId());
                Assert.notNull(sxServiceLink, MessageFormat.format("更新对象不存在！对象id为{0}", sxServiceLink.getId()));
                BeanUtils.copyProperties(sxServiceLink, dbSxServiceLinkWithBLOBs);
                dbSxServiceLinkWithBLOBs.setModifyDate(new Date());
                dbSxServiceLinkMapper.updateByPrimaryKeySelective(dbSxServiceLinkWithBLOBs);
            } else {
                DbSxServiceLinkWithBLOBs dbSxServiceLinkWithBLOBs = new DbSxServiceLinkWithBLOBs();
                BeanUtils.copyProperties(sxServiceLink, dbSxServiceLinkWithBLOBs);
                dbSxServiceLinkWithBLOBs.setCreateDate(new Date());
                dbSxServiceLinkWithBLOBs.setModifyDate(new Date());
                dbSxServiceLinkWithBLOBs.setDelFlag((short)0);
                dbSxServiceLinkWithBLOBs.setServiceLinkOid(UUID.randomUUID().toString().replaceAll("-", ""));
                dbSxServiceLinkMapper.insert(dbSxServiceLinkWithBLOBs);
            }
        }

        @Transactional(rollbackFor = Exception.class)
        public void delete(String id) {
            Assert.hasLength(id, "删除主键不能为空！");
            DbSxServiceLinkWithBLOBs dbSxServiceLinkWithBLOBs = dbSxServiceLinkMapper.selectByPrimaryKey(Long.valueOf(id));
            Assert.notNull(dbSxServiceLinkWithBLOBs, MessageFormat.format("删除对象不存在！对象id为{0}", id));
            dbSxServiceLinkWithBLOBs.setDelFlag((short)1);
            dbSxServiceLinkWithBLOBs.setModifyDate(new Date());
            this.dbSxServiceLinkMapper.updateByPrimaryKeySelective(dbSxServiceLinkWithBLOBs);
        }

        public DbSxServiceLinkWithBLOBs getDetail(String id) {
            Assert.hasLength(id, "查询主键不能为空！");
            DbSxServiceLinkWithBLOBs dbSxServiceLinkWithBLOBs = dbSxServiceLinkMapper.selectByPrimaryKey(Long.valueOf(id));
            Assert.notNull(dbSxServiceLinkWithBLOBs, MessageFormat.format("查询对象不存在！对象id为{0}", id));
            return dbSxServiceLinkWithBLOBs;
        }
}
