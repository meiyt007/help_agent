package com.zfsoft.cases.manager;

import com.zfsoft.cases.data.QlCaseApplay;
import com.zfsoft.cases.dbaccess.dao.QlCaseApplayMapper;
import com.zfsoft.cases.dbaccess.data.DbQlCaseApplay;
import com.zfsoft.cases.util.StringUtil;
import com.zfsoft.ha.data.HaUserResource;
import com.zfsoft.ha.data.responseData.HaQlCaseAppayResponseData;
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
import java.util.*;
import java.util.stream.Collectors;

/**
 * @（#）: QlCaseApplayManager
 * @description: 办件申请信息实现类
 * @author: wangwg
 * @date: 2020/10/24
 * @version: 1.0
 * @Copyright: 上海卓繁信息技术股份有限公司版权所有
 */
@Service
@Slf4j
@CacheConfig(cacheNames = "case:apply")
public class QlCaseApplayManager {

    @Resource
    private QlCaseApplayMapper qlCaseApplayMapper;

    @Transactional(rollbackFor=Exception.class)
    @ParamValid
    @ShardingTransactionType(TransactionType.LOCAL)
    @CacheEvict(allEntries = true)
    public Map<String, Integer> saveQlCaseApplay(QlCaseApplay qlCaseApplay) {
        Map<String, Integer> map = new HashMap<String, Integer>();
        if(!"0".equals(qlCaseApplay.getCaseOid())) {
            if(qlCaseApplay.getCaseOid() !=null){
                DbQlCaseApplay applay = qlCaseApplayMapper.queryQlCaseApplayByCaseOid(qlCaseApplay.getCaseOid());
                if(applay !=null){
                    qlCaseApplay.setId(applay.getId());
                    qlCaseApplay.setCaseOid(applay.getCaseOid());
                }
            }
            if (null == qlCaseApplay.getId()) {
                qlCaseApplay.setId(null);
                qlCaseApplay.setCreateDate(new Date());
                qlCaseApplay.setModifyDate(new Date());
            }
            DbQlCaseApplay dbQlCaseApplay=new DbQlCaseApplay();
            BeanUtils.copyProperties(qlCaseApplay,dbQlCaseApplay);
            int index=0;
            if (null == qlCaseApplay.getId()) {
                index = qlCaseApplayMapper.insert(dbQlCaseApplay);
            }else {
                dbQlCaseApplay.setModifyDate(new Date());
                index = qlCaseApplayMapper.update(dbQlCaseApplay);
            }
            //保存成功
            if (index >= 0) {
                map.put("applyOid", dbQlCaseApplay.getId().intValue());
            }
        }
        return map;
    }

    /**
     * 根据办件业务主键查询办件申请信息
     *
     * @author wangwg
     * @date 2020/10/26
     * @param caseOid
     * @return com.zfsoft.data.QlCaseApplay
     **/
    //@Cacheable(key = "'queryQlCaseApplayByCaseOid:caseOid=' + #caseOid", unless = "#result == null")
    public QlCaseApplay queryQlCaseApplayByCaseOid(String caseOid) {
        if(StringUtil.isEmpty(caseOid)){
            throw new ResultInfoException("办件业务编码为空！");
        }
        DbQlCaseApplay dbQlCaseApplay = qlCaseApplayMapper.queryQlCaseApplayByCaseOid(caseOid);
        QlCaseApplay qlCaseApplay = null;
        if(dbQlCaseApplay !=null){
            qlCaseApplay = new QlCaseApplay();
           BeanUtils.copyProperties(dbQlCaseApplay,qlCaseApplay);
        }
        return qlCaseApplay;
    }

    @Transactional(rollbackFor=Exception.class)
    @ParamValid
    @ShardingTransactionType(TransactionType.LOCAL)
    public void savePostResultInfoForBrowers(QlCaseApplay qlCaseApplay) {
        if(!"0".equals(qlCaseApplay.getCaseOid())) {
            if(qlCaseApplay.getCaseOid() != null){
                DbQlCaseApplay applay = qlCaseApplayMapper.queryQlCaseApplayByCaseOid(qlCaseApplay.getCaseOid());
                if(applay != null){
                    qlCaseApplay.setId(applay.getId());
                    qlCaseApplay.setCaseOid(applay.getCaseOid());
                }
            }
            if (null == qlCaseApplay.getId()) {
                qlCaseApplay.setId(null);
                qlCaseApplay.setCreateDate(new Date());
            }
            DbQlCaseApplay dbQlCaseApplay = new DbQlCaseApplay();
            BeanUtils.copyProperties(qlCaseApplay,dbQlCaseApplay);
            if (null == qlCaseApplay.getId()) {
                qlCaseApplayMapper.insert(dbQlCaseApplay);
            }else {
                dbQlCaseApplay.setModifyDate(new Date());
                qlCaseApplayMapper.update(dbQlCaseApplay);
            }
        }
    }

    /**
     * 根据对象筛选
     * @param applyUserName 申请人姓名
     * @param credentialNumber 身份证号码
     * @author: zhaobf
     * @Date: 2022/8/4 11:30
     */
    public List<QlCaseApplay> queryQlCaseApplayByApplyUserNameAndCredentialNumber(String applyUserName,String credentialNumber){
        List<DbQlCaseApplay> dbQlCaseApplays = qlCaseApplayMapper.queryQlCaseApplayByApplyUserNameAndCredentialNumber(applyUserName,credentialNumber);
        List<QlCaseApplay> qlCaseApplays = dbQlCaseApplays.stream().map(dbHaUserResource -> {
            QlCaseApplay qlcase = new QlCaseApplay();
            BeanUtils.copyProperties(dbHaUserResource, qlcase);
            return qlcase;
        }).collect(Collectors.toList());
        return qlCaseApplays;
    }


    /**
     * 根据办件业务主键查询办件申请信息
     *
     * @author zhaobf
     * @date 2022/11/17
     * @param name
     * @return com.zfsoft.data.QlCaseApplay
     **/
    public List<HaQlCaseAppayResponseData> selectByName(String name) {
        if(StringUtil.isEmpty(name)){
            throw new ResultInfoException("name不能为空！");
        }
        List<DbQlCaseApplay> dbQlCaseApplay = qlCaseApplayMapper.selectByName(name);

        return com.zfsoft.single.util.BeanUtils.copyListProperties(dbQlCaseApplay,HaQlCaseAppayResponseData::new);
    }
}
