package com.zfsoft.service.manager.sxService;

import cn.hutool.core.util.StrUtil;
import com.zfsoft.service.dbaccess.dao.sxService.DbSxAcceptConditionMapper;
import com.zfsoft.service.dbaccess.data.sxService.DbSxAcceptCondition;
import com.zfsoft.service.dbaccess.data.sxService.DbSxAcceptConditionExample;
import com.zfsoft.platform.utils.validate.ResultInfoException;
import com.zfsoft.service.sxService.data.SxAcceptCondition;
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
 * @ClassName SxAcceptConditionServiceImpl
 * @Description: 实施清单-受理条件 实现类
 * @Author wangxl
 * @Date 2020/10/26
 **/
@Service
@Slf4j
@CacheConfig(cacheNames = "service:sxAcceptCondition")
public class SxAcceptConditionManager {

        @Resource
        private DbSxAcceptConditionMapper dbSxAcceptConditionMapper;

        //@Cacheable(key = "'getSxAcceptConditionByServiceOid:serviceOid=' + #serviceOid", unless = "#result == null")
        public List<SxAcceptCondition> getSxAcceptConditionByServiceOid(String serviceOid) {
            DbSxAcceptConditionExample dbSxAcceptConditionExample = new DbSxAcceptConditionExample();
            DbSxAcceptConditionExample.Criteria criteria = dbSxAcceptConditionExample.createCriteria();
            if(StrUtil.isNotEmpty(serviceOid)){
                criteria.andServiceOidEqualTo(serviceOid);
            }
            criteria.andDelFlagEqualTo((short)0);
            List<DbSxAcceptCondition> dbSxAcceptConditions = dbSxAcceptConditionMapper.selectByExample(dbSxAcceptConditionExample);
            List<SxAcceptCondition> sxAcceptConditionList = dbSxAcceptConditions.stream().map(sxAcceptCondition -> {
                SxAcceptCondition acceptCondition = new SxAcceptCondition();
                BeanUtils.copyProperties(sxAcceptCondition,acceptCondition);
                return acceptCondition;
            }).collect(Collectors.toList());
            return sxAcceptConditionList;
        }

        //@Cacheable(key = "'getSxAcceptConditionByOid:oid=' + #oid", unless = "#result == null")
        public SxAcceptCondition getSxAcceptConditionByOid(String oid){
             DbSxAcceptCondition dbSxAcceptCondition= dbSxAcceptConditionMapper.getSxAcceptConditionByOid(oid);
             if(null  == dbSxAcceptCondition){
                 throw  new ResultInfoException("实施清单受理条件为空！");
             }
             SxAcceptCondition sxAcceptCondition = new SxAcceptCondition();
             BeanUtils.copyProperties(dbSxAcceptCondition,sxAcceptCondition);
             return sxAcceptCondition;
        }

        public List<DbSxAcceptCondition> queryList(SxAcceptCondition sxAcceptCondition) {
            DbSxAcceptCondition dbSxAcceptCondition = new DbSxAcceptCondition();
            BeanUtils.copyProperties(sxAcceptCondition, dbSxAcceptCondition);
            List<DbSxAcceptCondition> dbSxAcceptConditionList = dbSxAcceptConditionMapper.queryDbSxAcceptConditionList(dbSxAcceptCondition);
            return dbSxAcceptConditionList;
        }

        public void saveOrUpdateSxAcceptCondition(SxAcceptCondition sxAcceptCondition) {
            if (null != sxAcceptCondition.getId()) {
                DbSxAcceptCondition dbSxAcceptCondition = dbSxAcceptConditionMapper.selectByPrimaryKey(sxAcceptCondition.getId());
                Assert.notNull(sxAcceptCondition, MessageFormat.format("更新对象不存在！对象id为{0}", sxAcceptCondition.getId()));
                BeanUtils.copyProperties(sxAcceptCondition, dbSxAcceptCondition);
                dbSxAcceptCondition.setModifyDate(new Date());
                dbSxAcceptConditionMapper.updateByPrimaryKeySelective(dbSxAcceptCondition);
            } else {
                DbSxAcceptCondition dbSxAcceptCondition = new DbSxAcceptCondition();
                BeanUtils.copyProperties(sxAcceptCondition, dbSxAcceptCondition);
                dbSxAcceptCondition.setCreateDate(new Date());
                dbSxAcceptCondition.setModifyDate(new Date());
                dbSxAcceptCondition.setDelFlag((short)0);
                dbSxAcceptCondition.setConditionOid(UUID.randomUUID().toString().replaceAll("-", ""));
                this.dbSxAcceptConditionMapper.insert(dbSxAcceptCondition);
            }
        }

        @Transactional(rollbackFor = Exception.class)
        public void delete(String id) {
            Assert.hasLength(id, "删除主键不能为空！");
            DbSxAcceptCondition dbSxAcceptCondition = this.dbSxAcceptConditionMapper.selectByPrimaryKey(Long.valueOf(id));
            Assert.notNull(dbSxAcceptCondition, MessageFormat.format("删除对象不存在！对象id为{0}", id));
            dbSxAcceptCondition.setDelFlag((short)1);
            dbSxAcceptCondition.setModifyDate(new Date());
            this.dbSxAcceptConditionMapper.updateByPrimaryKeySelective(dbSxAcceptCondition);
        }

        public DbSxAcceptCondition getDetail(String id) {
            Assert.hasLength(id, "查询主键不能为空！");
            DbSxAcceptCondition dbSxAcceptCondition = this.dbSxAcceptConditionMapper.selectByPrimaryKey(Long.valueOf(id));
            Assert.notNull(dbSxAcceptCondition, MessageFormat.format("查询对象不存在！对象id为{0}", id));
            return dbSxAcceptCondition;
        }
}
