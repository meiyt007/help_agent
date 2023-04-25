package com.zfsoft.single.manager.ywbl;

import cn.hutool.core.util.StrUtil;
import com.zfsoft.platform.common.security.data.CurrentLoginUserHolder;
import com.zfsoft.single.data.ywbl.InternetAuditRecord;
import com.zfsoft.single.dbaccess.dao.ywbl.DbInternetAuditRecordMapper;
import com.zfsoft.single.dbaccess.data.ywbl.DbInternetAuditRecord;
import com.zfsoft.single.dbaccess.data.ywbl.DbInternetAuditRecordExample;
import com.zfsoft.single.util.BeanUtils;
import com.zfsoft.single.util.UUIDUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;


/**
 * @author: dongxl
 * @create: 2020-11-9
 * @description: 办件预审
 */
@Slf4j
@Service
public class InternetAuditRecordManager {

    @Resource
    private DbInternetAuditRecordMapper dbInternetAuditRecordMapper;

    /**
     * 办件预审记录保存
     * dongxl
     * @param internetAuditRecord
     */
    @Transactional(rollbackFor = Exception.class)
    public void saveOrUpdate(InternetAuditRecord internetAuditRecord) {
        if(internetAuditRecord!=null){
            DbInternetAuditRecord caserecord=  this.getOneByCaseOid(internetAuditRecord.getCaseOid());
            if(caserecord!=null){
                caserecord.setAuditResult(internetAuditRecord.getAuditResult());
                caserecord.setResultDesc(internetAuditRecord.getResultDesc());
                caserecord.setModifyDate(new Date());
                caserecord.setCreateUser(CurrentLoginUserHolder.getCurrentLoginUser().getUserOid());
                this.dbInternetAuditRecordMapper.updateByPrimaryKeySelective(caserecord);
            }else{
                DbInternetAuditRecord caseAudit=new DbInternetAuditRecord();
                BeanUtils.copyProperties(internetAuditRecord,caseAudit);
                caseAudit.setRecordOid(UUIDUtil.randomUUID());
                caseAudit.setModifyDate(new Date());
                caseAudit.setCreateDate(new Date());
                caseAudit.setCreateUser(CurrentLoginUserHolder.getCurrentLoginUser().getUserOid());
                this.dbInternetAuditRecordMapper.insertSelective(caseAudit);
            }
        }
    }

    /**
     * dongxl
     * 根据办件主键获取记录
     * @param caseOid
     * @return
     */
    public DbInternetAuditRecord getOneByCaseOid(String caseOid) {
        Assert.hasLength(caseOid, "办件主键不能为空！");
        DbInternetAuditRecordExample dbInternetAuditRecordExample = new DbInternetAuditRecordExample();
        DbInternetAuditRecordExample.Criteria criteria = dbInternetAuditRecordExample.createCriteria();
        if (StrUtil.isNotEmpty(caseOid)) {
            criteria.andCaseOidEqualTo(caseOid);
        }
        List<DbInternetAuditRecord> dbCaseRecord = dbInternetAuditRecordMapper.selectByExample(dbInternetAuditRecordExample);
        if (!CollectionUtils.isEmpty(dbCaseRecord)) {
            return dbCaseRecord.get(0);
        }
        return null;
    }
}
