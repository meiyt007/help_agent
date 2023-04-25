package com.zfsoft.service.manager.sxService;

import cn.hutool.core.bean.BeanUtil;
import com.zfsoft.service.dbaccess.dao.sxConditionalRules.DbSxConditionalRulesMapper;
import com.zfsoft.service.dbaccess.data.sxConditionalRules.DbSxConditionalRules;
import com.zfsoft.service.dbaccess.data.sxConditionalRules.DbSxConditionalRulesExample;
import com.zfsoft.service.sxConditionalRules.data.SxConditionalRules;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.text.MessageFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @ClassName SxServiceLinkServiceImpl
 * @Description: 条件配置实现类
 * @Author WangKe
 * @Date 2022/05/24
 **/
@Service
@Slf4j
@CacheConfig(cacheNames = "rules:conditionalRules")
public class SxConditionalRulesManager {

    @Resource
    private DbSxConditionalRulesMapper dbSxConditionalRulesMapper;

    public void savaOrUpdateConditionalRules(List<SxConditionalRules> rulesList){
        if(rulesList.size() > 0){
            // 根据事项和规则类型删除
            Set<String> ruleTypeSet = rulesList.stream().map(a -> a.getRuleType()).collect(Collectors.toSet());
            List<String> ruleTypeList = new ArrayList<>();
            ruleTypeList.addAll(ruleTypeSet);
            DbSxConditionalRulesExample example = new DbSxConditionalRulesExample();
            DbSxConditionalRulesExample.Criteria criteria = example.createCriteria();
            criteria.andServiceOidEqualTo(rulesList.get(0).getServiceOid());
            criteria.andRuleTypeIn(ruleTypeList);
            dbSxConditionalRulesMapper.deleteByExample(example);
            for (SxConditionalRules sxConditionalRules : rulesList){
                DbSxConditionalRules newDbRules = new DbSxConditionalRules();
                BeanUtil.copyProperties(sxConditionalRules, newDbRules);
                newDbRules.setDelFlag((short) 0);
                newDbRules.setCreateDate(new Date());
                newDbRules.setModifyDate(new Date());
                newDbRules.setRulesOid(UUID.randomUUID().toString().replace("-", ""));
                dbSxConditionalRulesMapper.insert(newDbRules);
            }
        }
    }

    public List<SxConditionalRules> queryAllSxConditionalRules(SxConditionalRules cond){
        DbSxConditionalRulesExample example = new DbSxConditionalRulesExample();
        DbSxConditionalRulesExample.Criteria criteria = example.createCriteria();
        criteria.andServiceOidEqualTo(cond.getServiceOid());
        List<String> list = new ArrayList<>();
        // 当ruleType为2时，则为秒批规则
        if("2".equals(cond.getRuleType())){
            list.add("2");
            list.add("3");
            criteria.andRuleTypeIn(list);
        }else {
            criteria.andRuleTypeEqualTo(cond.getRuleType());
        }
        List<DbSxConditionalRules> dbSxConditionalRules = dbSxConditionalRulesMapper.selectByExample(example);
        List<SxConditionalRules> rulesList = new ArrayList<>();
        for(DbSxConditionalRules dbSxConditionalRules1 : dbSxConditionalRules){
            SxConditionalRules sxConditionalRules = new SxConditionalRules();
            BeanUtil.copyProperties(dbSxConditionalRules1, sxConditionalRules);
            rulesList.add(sxConditionalRules);
        }
        return rulesList;
    }

    public void delete(String id){
        Assert.hasLength(id, "删除主键不能为空！");
        int i = this.dbSxConditionalRulesMapper.deleteByPrimaryKey(Long.valueOf(id));
        if(i < 0){
            MessageFormat.format("删除对象不存在！对象id为{0}", id);
        }
    }

    public SxConditionalRules selectByRules(SxConditionalRules rules){
        DbSxConditionalRules dbSxConditionalRules = new DbSxConditionalRules();
        BeanUtil.copyProperties(rules, dbSxConditionalRules);
        DbSxConditionalRules dbSxConditionalRules1 = dbSxConditionalRulesMapper.selectByRules(dbSxConditionalRules);
        SxConditionalRules rules1 = new SxConditionalRules();
        BeanUtil.copyProperties(dbSxConditionalRules1, rules1);
        return rules1;
    }
    public  List<SxConditionalRules> selectByserviceOid(String serviceOid){
        List<DbSxConditionalRules> dbSxConditionalRules = dbSxConditionalRulesMapper.selectByserviceOid(serviceOid);
        List<SxConditionalRules> rulesList = new ArrayList<>();
        for(DbSxConditionalRules dbSxConditionalRules1 : dbSxConditionalRules){
            SxConditionalRules sxConditionalRules = new SxConditionalRules();
            BeanUtil.copyProperties(dbSxConditionalRules1, sxConditionalRules);
            rulesList.add(sxConditionalRules);
        }
        return rulesList;
    }
}
