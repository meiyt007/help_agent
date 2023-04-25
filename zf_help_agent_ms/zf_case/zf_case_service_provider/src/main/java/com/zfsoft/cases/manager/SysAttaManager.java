package com.zfsoft.cases.manager;

import com.zfsoft.cases.data.QlSysAtta;
import com.zfsoft.cases.dbaccess.dao.SysAttaMapper;
import com.zfsoft.cases.dbaccess.data.DbQlSysAtta;
import com.zfsoft.cases.util.StringUtil;
import com.zfsoft.platform.utils.validate.ParamValid;
import com.zfsoft.platform.utils.validate.ResultInfoException;
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
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @（#）: SysAttaManager
 * @description: 上传附件信息实现类
 * @author: wangwg
 * @date: 2020/10/24
 * @version: 1.0
 * @Copyright: 上海卓繁信息技术股份有限公司版权所有
 */
@Service
@Slf4j
@CacheConfig(cacheNames = "case:sysatta")
public class SysAttaManager {

    @Resource
    private SysAttaMapper sysAttaMapper;

    @Transactional(rollbackFor = Exception.class)
    @ParamValid
    @ShardingTransactionType(TransactionType.LOCAL)
    @CacheEvict(allEntries = true)
    public QlSysAtta saveSysAtta(QlSysAtta sysAtta) {
        if (null == sysAtta.getId()) {
            sysAtta.setId(null);
            sysAtta.setCreateDate(new Date());
            sysAtta.setModifyDate(new Date());
        }
        if(null == sysAtta.getAttaOid()){
            sysAtta.setAttaOid(UUID.randomUUID().toString().replaceAll("-", ""));
        }
        sysAtta.setUploadDate(new Date());
        sysAtta.setDelFlag(0);
        DbQlSysAtta DbQlSysAtta = new DbQlSysAtta();
        BeanUtils.copyProperties(sysAtta, DbQlSysAtta);
        int index = 0;
        if (null == sysAtta.getId()) {
            index = sysAttaMapper.insert(DbQlSysAtta);
        } else {
            DbQlSysAtta.setModifyDate(new Date());
            index = sysAttaMapper.update(DbQlSysAtta);
        }
        return sysAtta;
    }

    public QlSysAtta querySysAttaByOid(String attaOid) {
        if (StringUtil.isEmpty(attaOid)) {
            throw new ResultInfoException("附件业务主键为空！");
        }
        DbQlSysAtta DbQlSysAtta = sysAttaMapper.querySysAttaByOid(attaOid);
        QlSysAtta sysAtta = null;
        if(DbQlSysAtta !=null){
            sysAtta = new QlSysAtta();
            BeanUtils.copyProperties(DbQlSysAtta, sysAtta);
        }
        return sysAtta;
    }

    public List<QlSysAtta> queryAll(DbQlSysAtta dbQlSysAtta){
        List<DbQlSysAtta> dbQlSysAttas = sysAttaMapper.queryAll(dbQlSysAtta);
        List<QlSysAtta> qlSysAttas = dbQlSysAttas.stream().map(dbQlSysAtta1 -> {
            QlSysAtta qlSysAtta = new QlSysAtta();
            if (dbQlSysAtta1 != null) {
                BeanUtils.copyProperties(dbQlSysAtta1, qlSysAtta);
            }
            return qlSysAtta;
        }).collect(Collectors.toList());
        return qlSysAttas;
    }

}
