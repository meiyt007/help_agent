package com.zfsoft.cases.manager;

import com.zfsoft.cases.data.SysDict;
import com.zfsoft.cases.dbaccess.dao.SysDictMapper;
import com.zfsoft.cases.dbaccess.data.DbSysDict;
import com.zfsoft.platform.utils.validate.ParamValid;
import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.transaction.annotation.ShardingTransactionType;
import org.apache.shardingsphere.transaction.core.TransactionType;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @（#）: SysDictManager
 * @description: 字典信息实现类
 * @author: wangwg
 * @date: 2020/10/24
 * @version: 1.0
 * @Copyright: 上海卓繁信息技术股份有限公司版权所有
 */
@Service
@Slf4j
@CacheConfig(cacheNames = "case:sysdict")
public class SysDictManager {

    @Resource
    private SysDictMapper sysDictMapper;


    @Transactional(rollbackFor = Exception.class)
    @ParamValid
    @ShardingTransactionType(TransactionType.LOCAL)
    @CacheEvict(allEntries = true)
    public String saveSysDict(SysDict sysDict) {
        if (null == sysDict.getId()) {
            sysDict.setId(null);
        }
        sysDict.setCreateDate(new Date());
        DbSysDict dbSysDict = new DbSysDict();
        BeanUtils.copyProperties(sysDict, dbSysDict);
        int index = 0;
        if (null == sysDict.getId()) {
            index = sysDictMapper.insert(dbSysDict);
        } else {
            index = sysDictMapper.update(dbSysDict);
        }
        return sysDict.getCode();
    }
}
