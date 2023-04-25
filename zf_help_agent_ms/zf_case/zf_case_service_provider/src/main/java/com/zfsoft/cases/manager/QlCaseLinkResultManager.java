package com.zfsoft.cases.manager;

import com.zfsoft.cases.data.QlCaseLinkResult;
import com.zfsoft.cases.dbaccess.dao.QlCaseLinkResultMapper;
import com.zfsoft.cases.dbaccess.data.DbQlCaseLinkResult;
import com.zfsoft.cases.feign.SysUserFeginService;
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
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @（#）: QlCaseLinkResultManager
 * @description: 办件审批环节信息实现类
 * @author: wangwg
 * @date: 2020/10/24
 * @version: 1.0
 * @Copyright: 上海卓繁信息技术股份有限公司版权所有
 */
@Service
@Slf4j
@CacheConfig(cacheNames = "case:link")
public class QlCaseLinkResultManager {

    @Resource
    private QlCaseLinkResultMapper qlCaseLinkResultMapper;

    @Resource
    private SysUserFeginService sysUserFeginService;

    @Transactional(rollbackFor = Exception.class)
    @ParamValid
    @ShardingTransactionType(TransactionType.LOCAL)
    @CacheEvict(allEntries = true)
    public Map<String, String> saveQlCaseLinkResult(QlCaseLinkResult qlCaseLinkResult) {
        Map<String, String> map=new HashMap<String, String>();
        if(!"0".equals(qlCaseLinkResult.getCaseOid())) {
            if (null == qlCaseLinkResult.getId()) {
                qlCaseLinkResult.setId(null);
                qlCaseLinkResult.setCreateDate(new Date());
                qlCaseLinkResult.setModifyDate(new Date());
            }
            if (null == qlCaseLinkResult.getLinkResultOid()) {
                qlCaseLinkResult.setLinkResultOid(UUID.randomUUID().toString().replaceAll("-", ""));
            }
            DbQlCaseLinkResult dbQlCaseLinkResult = new DbQlCaseLinkResult();
            BeanUtils.copyProperties(qlCaseLinkResult, dbQlCaseLinkResult);

            if(StringUtil.isNotEmpty(dbQlCaseLinkResult.getPersonName())&&isSpecialChar(dbQlCaseLinkResult.getPersonName())){
                if(StringUtil.isNotEmpty(dbQlCaseLinkResult.getPersonOid())){
                    dbQlCaseLinkResult.setPersonName(sysUserFeginService.getSysUserByUserOid(dbQlCaseLinkResult.getPersonOid()).getData().getName());
                }
            }
            int index = 0;
            if (null == qlCaseLinkResult.getId()) {
                index = qlCaseLinkResultMapper.insert(dbQlCaseLinkResult);
            } else {
                dbQlCaseLinkResult.setModifyDate(new Date());
                index = qlCaseLinkResultMapper.update(dbQlCaseLinkResult);
            }
            //保存成功
            if (index > 0) {
                map.put("caseOid",qlCaseLinkResult.getCaseOid());
                map.put("linkResultOid",qlCaseLinkResult.getLinkResultOid());
                map.put("personName",qlCaseLinkResult.getPersonName());
                map.put("accpectTime",new SimpleDateFormat("yyyy-MM-dd").format(qlCaseLinkResult.getFinalDate()));
            }
        }
        return map;
    }

    @Cacheable(key = "'queryQlCaseLinkResultListByCaseOid:caseOid=' + #caseOid", unless = "#result == null")
    public List<QlCaseLinkResult> queryQlCaseLinkResultListByCaseOid(String caseOid) {
        if(StringUtil.isEmpty(caseOid)){
            throw new ResultInfoException("办件业务编码为空！");
        }
        List<DbQlCaseLinkResult> dbQlCaseLinkResults = qlCaseLinkResultMapper.queryQlCaseLinkResultListByCaseOid(caseOid);
        List<QlCaseLinkResult> qlCaseLinkResultList = dbQlCaseLinkResults.stream().map(dbQlCaseLinkResult -> {
            QlCaseLinkResult linkResult = new QlCaseLinkResult();
            BeanUtils.copyProperties(dbQlCaseLinkResult,linkResult);
            return linkResult;
        }).collect(Collectors.toList());
        return qlCaseLinkResultList;
    }

    @Cacheable(key = "'querySlQlCaseLinkResultByCaseOid:caseOid=' + #caseOid", unless = "#result == null")
    public QlCaseLinkResult querySlQlCaseLinkResultByCaseOid(String caseOid) {
        if(StringUtil.isEmpty(caseOid)){
            throw new ResultInfoException("办件业务编码为空！");
        }
        QlCaseLinkResult linkResult =null;
        DbQlCaseLinkResult dbResult = qlCaseLinkResultMapper.querySlQlCaseLinkResultByCaseOid(caseOid);
        if(dbResult !=null){
            linkResult = new QlCaseLinkResult();
            BeanUtils.copyProperties(dbResult,linkResult);
        }
        return linkResult;
    }

    @Transactional(rollbackFor = Exception.class)
    @ParamValid
    @ShardingTransactionType(TransactionType.LOCAL)
    @CacheEvict(allEntries = true)
    public int deleteQlCaseLinkResult(Long id) {
        int index = qlCaseLinkResultMapper.deleteById(id);
        if(index==0){
            throw new ResultInfoException("参数删除失败，请稍后再试！");
        }
        return index;
    }

    public static boolean isSpecialChar(String str) {
        String regEx = "[?？]|\n|\r|\t";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.find();
    }

    @Cacheable(key = "'queryBjQlCaseLinkResultByCaseOid:caseOid=' + #caseOid", unless = "#result == null")
    public QlCaseLinkResult queryBjQlCaseLinkResultByCaseOid(String caseOid) {
        QlCaseLinkResult linkResult =null;
        DbQlCaseLinkResult dbResult = qlCaseLinkResultMapper.queryBjQlCaseLinkResultByCaseOid(caseOid);
        if(dbResult !=null){
            linkResult = new QlCaseLinkResult();
            BeanUtils.copyProperties(dbResult,linkResult);
        }
        return linkResult;
    }
}
