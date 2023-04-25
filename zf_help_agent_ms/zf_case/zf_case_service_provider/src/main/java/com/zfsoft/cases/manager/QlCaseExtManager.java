package com.zfsoft.cases.manager;

import com.zfsoft.cases.data.QlCaseExt;
import com.zfsoft.cases.dbaccess.dao.QlCaseExtMapper;
import com.zfsoft.cases.dbaccess.data.DbQlCaseExt;
import com.zfsoft.cases.util.StringUtil;
import com.zfsoft.platform.utils.validate.ParamValid;
import com.zfsoft.platform.utils.validate.ResultInfoException;
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
import java.util.HashMap;
import java.util.Map;

/**
 * @（#）: QlCaseExtManager
 * @description: 办件扩展信息实现类
 * @author: wangwg
 * @date: 2020/10/24
 * @version: 1.0
 * @Copyright: 上海卓繁信息技术股份有限公司版权所有
 */
@Service
@Slf4j
@CacheConfig(cacheNames = "case:ext")
public class QlCaseExtManager {

    @Resource
    private QlCaseExtMapper qlCaseExtMapper;

    @Transactional(rollbackFor = Exception.class)
    @ParamValid
    @ShardingTransactionType(TransactionType.LOCAL)
    @CacheEvict(allEntries = true)
    public Map<String, Integer> saveQlCaseExt(QlCaseExt qlCaseExt) {
        Map<String, Integer> map = new HashMap<String, Integer>();
        if(!"0".equals(qlCaseExt.getCaseOid())) {
            if (null == qlCaseExt.getId()) {
                qlCaseExt.setId(null);
                qlCaseExt.setCreateDate(new Date());
                qlCaseExt.setModifyDate(new Date());
            }
            if (null == qlCaseExt.getDeliverFlag()) {
                qlCaseExt.setDeliverFlag(0);
            }
            DbQlCaseExt dbQlCaseExt = new DbQlCaseExt();
            BeanUtils.copyProperties(qlCaseExt, dbQlCaseExt);
            int index = 0;
            if (null == qlCaseExt.getId()) {
                index = qlCaseExtMapper.insert(dbQlCaseExt);
            } else {
                dbQlCaseExt.setModifyDate(new Date());
                index = qlCaseExtMapper.update(dbQlCaseExt);
            }
            //保存成功
            if (index >= 0) {
                map.put("extOid", dbQlCaseExt.getId().intValue());
            }
        }
        return map;
    }

    @Cacheable(key = "'queryQlCaseExtByCaseOid:caseOid=' + #caseOid", unless = "#result == null")
    public QlCaseExt queryQlCaseExtByCaseOid(String caseOid) {
        if(StringUtil.isEmpty(caseOid)){
            throw new ResultInfoException("办件业务编码为空！");
        }
        DbQlCaseExt dbQlCaseExt = qlCaseExtMapper.queryQlCaseExtByCaseOid(caseOid);
        QlCaseExt qlCaseExt = new QlCaseExt();
        if(dbQlCaseExt !=null){
            BeanUtils.copyProperties(dbQlCaseExt,qlCaseExt);
        }
        return qlCaseExt;
    }
}
