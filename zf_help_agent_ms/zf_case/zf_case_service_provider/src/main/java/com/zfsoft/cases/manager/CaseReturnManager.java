package com.zfsoft.cases.manager;

import com.zfsoft.cases.data.CaseReturn;
import com.zfsoft.cases.dbaccess.dao.DbCaseReturnMapper;
import com.zfsoft.cases.dbaccess.data.DbCaseReturn;
import com.zfsoft.cases.util.SendSmsUtil;
import com.zfsoft.cases.util.StringUtil;
import com.zfsoft.platform.utils.validate.ParamValid;
import com.zfsoft.platform.utils.validate.ResultInfoException;
import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.transaction.annotation.ShardingTransactionType;
import org.apache.shardingsphere.transaction.core.TransactionType;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @（#）: CaseReturnManager
 * @description: 办件退件基本信息实现类
 * @author: liangss
 * @date: 2021/01/15
 * @version: 1.0
 * @Copyright: 上海卓繁信息技术股份有限公司版权所有
 */
@Slf4j
@Service
public class CaseReturnManager {

    private static final int DEFAULT_LENGTH = 4;

    @Resource
    private DbCaseReturnMapper dbCaseReturnMapper;



    @Transactional(rollbackFor = Exception.class)
    @ParamValid
    @ShardingTransactionType(TransactionType.LOCAL)
   /* @CacheEvict(allEntries = true)*/
    public Map<String, String> saveCaseReturn(CaseReturn caseReturn) {
        Map<String, String> map = new HashMap<String, String>();
        if (null == caseReturn.getId()) {
            caseReturn.setId(null);
            caseReturn.setCreateDate(new Date());
            caseReturn.setModifyDate(new Date());
        }
        if(StringUtil.isEmpty(caseReturn.getReturnOid())){
           caseReturn.setReturnOid(UUID.randomUUID().toString().replaceAll("-", ""));
           caseReturn.setDelFlag(0);
           caseReturn.setReturnStep("0");
        }
        caseReturn.setModifyDate(new Date());
        DbCaseReturn dbCaseReturn = new DbCaseReturn();
        BeanUtils.copyProperties(caseReturn, dbCaseReturn);

        int index = 0;
        if (null == caseReturn.getId()) {
            index = dbCaseReturnMapper.insert(dbCaseReturn);
        } else {
            dbCaseReturn.setModifyDate(new Date());
            index = dbCaseReturnMapper.updateByPrimaryKey(dbCaseReturn);
        }
        //保存成功
        if (index >= 0) {
            map.put("caseOid", caseReturn.getCaseOid());
            map.put("returnOid", caseReturn.getReturnOid());
        }
        return map;
    }


    public Map<String, String> outReturnFile(CaseReturn caseReturn) {
        Map<String, String> map = new HashMap<String, String>();
        if (null == caseReturn.getId()) {
            caseReturn.setId(null);
        }
        caseReturn.setCreateDate(new Date());
        if(StringUtil.isEmpty(caseReturn.getReturnOid())){
            //log.info(  UUID.randomUUID().toString().replaceAll("-", ""));
            caseReturn.setReturnOid(UUID.randomUUID().toString().replaceAll("-", ""));
            caseReturn.setDelFlag(0);
        }
        caseReturn.setReturnStatus("3");
        caseReturn.setReturnStep("3");
        caseReturn.setModifyDate(new Date());
        if(caseReturn.getIsCms().equals("Y")){
            if(caseReturn.getApplyPhone().equals(caseReturn.getReceivePhone())){
                SendSmsUtil.sendSms(caseReturn.getApplyPhone(), "您申请的办件编号为"+caseReturn.getRegNumber()+"的办件，审核未通过，现已成功办理退件");
            }else{
                  SendSmsUtil.sendSms(caseReturn.getApplyPhone(), "您申请的办件编号为"+caseReturn.getRegNumber()+"的办件，审核未通过，现已成功办理退件");
                  SendSmsUtil.sendSms(caseReturn.getReceivePhone(), "您申请的办件编号为"+caseReturn.getRegNumber()+"的办件，审核未通过，现已成功办理退件");
            }
        }
        DbCaseReturn  dbCaseReturn = new DbCaseReturn();
        BeanUtils.copyProperties(caseReturn, dbCaseReturn);

        int index = 0;
        if (null == caseReturn.getId()) {
            index = dbCaseReturnMapper.insert(dbCaseReturn);
        } else {
            index = dbCaseReturnMapper.updateByPrimaryKey(dbCaseReturn);
        }
        //保存成功
        if (index >= 0) {
            map.put("caseOid", caseReturn.getCaseOid());
            map.put("returnOid", caseReturn.getReturnOid());
        }
        return map;
    }


    public Map<String, String> saveCaseReturnInform(CaseReturn caseReturn) {
        Map<String, String> map = new HashMap<String, String>();
        if (null == caseReturn.getId()) {
            caseReturn.setId(null);
        }
        if(StringUtil.isEmpty(caseReturn.getReturnOid())){
            caseReturn.setReturnOid(UUID.randomUUID().toString().replaceAll("-", ""));
            caseReturn.setDelFlag(0);
        }
        caseReturn.setModifyDate(new Date());
        DbCaseReturn  dbCaseReturn = new DbCaseReturn();
        BeanUtils.copyProperties(caseReturn, dbCaseReturn);

        int index = 0;
        if (null == caseReturn.getId()) {
            index = dbCaseReturnMapper.insert(dbCaseReturn);
        } else {
            index = dbCaseReturnMapper.updateByPrimaryKey(dbCaseReturn);
        }
        //保存成功
        if (index >= 0) {
            map.put("caseOid", caseReturn.getCaseOid());
            map.put("returnOid", caseReturn.getReturnOid());
        }
        return map;
    }

    /**
     * 根据办件oid查询退件信息
     * @param caseOid
     * @return
     */
   //@Cacheable(key = "'queryCaseReturnByCaseOid:caseOid=' + #caseOid", unless = "#result == null")
  public CaseReturn queryCaseReturnByCaseOid(String caseOid) {
        if (StringUtil.isEmpty(caseOid)) {
            throw new ResultInfoException("办件Oid为空！");
        }
        DbCaseReturn  dbCaseReturn=dbCaseReturnMapper.queryCaseReturnByCaseOid(caseOid);
        CaseReturn caseReturn  = new CaseReturn ();
        if(dbCaseReturn !=null){
            BeanUtils.copyProperties(dbCaseReturn, caseReturn);
        }
      return caseReturn;
    }


}
