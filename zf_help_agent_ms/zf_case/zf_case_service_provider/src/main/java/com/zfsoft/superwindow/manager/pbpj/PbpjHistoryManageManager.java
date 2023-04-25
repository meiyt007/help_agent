package com.zfsoft.superwindow.manager.pbpj;

import com.zfsoft.platform.utils.validate.ParamValid;
import com.zfsoft.superwindow.data.yxpz.PbpjHistoryManage;
import com.zfsoft.superwindow.dbaccess.dao.DbPbpjHistoryManageMapper;
import com.zfsoft.superwindow.dbaccess.data.DbPbpjHistoryManage;
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
 * @（#）: PbpjHistoryManageManager
 * @description: 平板评价历史接口实现类
 * @author: wangwg
 * @date: 2020/12/02
 * @version: 1.0
 * @Copyright: 上海卓繁信息技术股份有限公司版权所有
 */
@Service
@Slf4j
@CacheConfig(cacheNames = "manage:pbpjhistory")
public class PbpjHistoryManageManager {


    @Resource
    private DbPbpjHistoryManageMapper dbPbpjHistoryManageMapper;

    @Transactional(rollbackFor=Exception.class)
    @ParamValid
    @ShardingTransactionType(TransactionType.LOCAL)
    @CacheEvict(allEntries = true)
    public int savePbpjHistoryManage(PbpjHistoryManage history) {
        if (null == history.getId()) {
            history.setId(null);
        }
        history.setDateTime(new Date());
        history.setStatus(1);
        history.setIsDelete(0);
        DbPbpjHistoryManage dbPbpjHistoryManage = new DbPbpjHistoryManage();
        BeanUtils.copyProperties(history,dbPbpjHistoryManage);
        int index=0;
        if (null == history.getId()) {
            index = dbPbpjHistoryManageMapper.insert(dbPbpjHistoryManage);
        }else {
            index = dbPbpjHistoryManageMapper.update(dbPbpjHistoryManage);
        }
        return index;
    }
}