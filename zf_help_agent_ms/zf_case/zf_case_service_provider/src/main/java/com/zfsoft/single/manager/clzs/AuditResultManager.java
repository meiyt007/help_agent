package com.zfsoft.single.manager.clzs;


import com.zfsoft.single.data.clzs.AuditResult;
import com.zfsoft.single.dbaccess.dao.DbAuditResultMapper;
import com.zfsoft.single.dbaccess.data.DbAuditResult;
import com.zfsoft.single.dbaccess.data.DbAuditResultExample;
import com.zfsoft.single.util.StringUtils;
import com.zfsoft.single.util.SysCode;
import com.zfsoft.single.util.UUIDUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.text.MessageFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


/**
 * @ClassName AuditResultManager
 * @Description: 审核结果实现类
 * @Author liangss
 * @Date 2021-07-20
 **/
@Service
@Slf4j
public class AuditResultManager {
    @Resource
    private DbAuditResultMapper dbAuditResultMapper;

    public void saveOrUpdateAuditResult(AuditResult auditResult) {
        if (null!=auditResult.getId()) {
            DbAuditResult dbAuditResult=this.dbAuditResultMapper.selectByPrimaryKey(auditResult.getId());
            Assert.notNull(dbAuditResult, MessageFormat.format("更新对象不存在！对象id为{0}", dbAuditResult.getId()));
            BeanUtils.copyProperties(auditResult, dbAuditResult);
            dbAuditResult.setModifyDate(new Date());
            this.dbAuditResultMapper.updateByPrimaryKeySelective(dbAuditResult);
        } else {
            DbAuditResult dbAuditResult=new DbAuditResult();
            BeanUtils.copyProperties(auditResult, dbAuditResult);
            dbAuditResult.setDeleteStatus(0);
            dbAuditResult.setCreateDate(new Date());
            dbAuditResult.setModifyDate(new Date());
            dbAuditResult.setOid(UUIDUtil.randomUUID());
            this.dbAuditResultMapper.insertSelective(dbAuditResult);
        }
    }


    public List<AuditResult> queryAuditResultList(AuditResult auditResult){
        DbAuditResultExample dbAuditResultExample=new DbAuditResultExample();
        DbAuditResultExample.Criteria criteria=dbAuditResultExample.createCriteria();
        if(null!=auditResult){
            if(StringUtils.isNotEmpty(auditResult.getCaseOid())){
                criteria.andCaseOidEqualTo(auditResult.getCaseOid());
            }
        }
        criteria.andDeleteStatusEqualTo(SysCode.STATUS.NO);
        dbAuditResultExample.setOrderByClause(" CREATE_DATE desc");
        List<DbAuditResult> dbAuditResultList=dbAuditResultMapper.selectByExample(dbAuditResultExample);

        List<AuditResult> auditResultList=dbAuditResultList.stream().map(dbAuditResult -> {
            AuditResult auditResult1 = new AuditResult();
            BeanUtils.copyProperties(dbAuditResult, auditResult1);
            return auditResult1;
        }).collect(Collectors.toList());
        return auditResultList;


    }

    public void updateByCaseOid(String caseOid){
        dbAuditResultMapper.updateByCaseOid(caseOid);
    }


    public void updateByCaseOidAndMateriaOid(String caseOid,String materialOid){
        dbAuditResultMapper.updateByCaseOidAndMateriaOid(caseOid,materialOid);
    }


    public void updateIsIgnoreByOid(String oid){
        dbAuditResultMapper.updateIsIgnoreByOid(oid);
    }

}
