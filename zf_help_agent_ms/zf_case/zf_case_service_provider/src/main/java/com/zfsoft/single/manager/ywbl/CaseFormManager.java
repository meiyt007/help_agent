package com.zfsoft.single.manager.ywbl;

import cn.hutool.core.util.StrUtil;
import com.zfsoft.single.data.ywbl.CaseForm;
import com.zfsoft.single.dbaccess.dao.ywbl.DbCaseFormMapper;
import com.zfsoft.single.dbaccess.data.ywbl.DbCaseForm;
import com.zfsoft.single.dbaccess.data.ywbl.DbCaseFormExample;
import com.zfsoft.single.util.BeanUtils;
import com.zfsoft.single.util.SysCode;
import com.zfsoft.single.util.UUIDUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.Date;
import java.util.List;


/**
 * @author: dongxl
 * @create: 2020-12-01
 * @description: 办件电子表单
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CaseFormManager {

    private final DbCaseFormMapper dbCaseFormMapper;

    /**
     * 办件表单记录保存
     * dongxl
     * @param caseForm
     */
    @Transactional(rollbackFor = Exception.class)
    public void saveOrUpdate(CaseForm caseForm) {
        Assert.notNull(caseForm, "办件表单信息不能为空！");
        if(caseForm!=null&&caseForm.getId()!=null){
            DbCaseForm caseFormOld= this.dbCaseFormMapper.selectByPrimaryKey(caseForm.getId());
            caseForm.setOid(caseFormOld.getOid());
            BeanUtils.copyProperties(caseForm,caseFormOld);
            this.dbCaseFormMapper.updateByPrimaryKeySelective(caseFormOld);
        }else {
            caseForm.setOid(UUIDUtil.randomUUID());
            caseForm.setDelFlag(String.valueOf(SysCode.STATUS.NO));
            caseForm.setModifyDate(new Date());
            DbCaseForm caseAudit=new DbCaseForm();
            BeanUtils.copyProperties(caseForm,caseAudit);
            this.dbCaseFormMapper.insertSelective(caseAudit);
        }
    }

    public CaseForm caseFormByCaseOid(String caseOid,String sxSerFormOid) {
        Assert.hasLength(caseOid, "办件编号不能为空！");
        DbCaseFormExample dbCaseFormExample = new DbCaseFormExample();
        DbCaseFormExample.Criteria criteria = dbCaseFormExample.createCriteria();
        if (StrUtil.isNotEmpty(caseOid)) {
            criteria.andRegOidEqualTo(caseOid.trim());
        }
        if (StrUtil.isNotEmpty(sxSerFormOid)) {
            criteria.andSerFormOidEqualTo(sxSerFormOid.trim());
        }
        List<DbCaseForm> dbCaseForm = dbCaseFormMapper.selectByExample(dbCaseFormExample);
        if (!CollectionUtils.isEmpty(dbCaseForm)) {
            CaseForm caseform=new CaseForm();
            BeanUtils.copyProperties(dbCaseForm.get(0),caseform);
            return caseform;
        }
        return null;
    }

    public List<CaseForm> caseFormByCase(CaseForm caseForm) {
        DbCaseFormExample dbCaseFormExample = new DbCaseFormExample();
        DbCaseFormExample.Criteria criteria = dbCaseFormExample.createCriteria();
        if(caseForm!=null){
            if (StrUtil.isNotEmpty(caseForm.getRegOid())) {
                criteria.andRegOidEqualTo(caseForm.getRegOid().trim());
            }
            if (StrUtil.isNotEmpty(caseForm.getComboDireOid())) {
                criteria.andComboDireOidEqualTo(caseForm.getComboDireOid().trim());
            }
        }
        criteria.andDelFlagEqualTo(String.valueOf(SysCode.STATUS.NO));
        List<DbCaseForm> dbCaseForm = dbCaseFormMapper.selectByExample(dbCaseFormExample);
        if (!CollectionUtils.isEmpty(dbCaseForm)) {
            return BeanUtils.copyListProperties(dbCaseForm,CaseForm::new);
        }
        return Collections.emptyList();
    }
}
