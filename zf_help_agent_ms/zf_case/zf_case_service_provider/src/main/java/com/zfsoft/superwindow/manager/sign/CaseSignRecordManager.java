package com.zfsoft.superwindow.manager.sign;

import com.zfsoft.platform.utils.validate.ResultInfoException;
import com.zfsoft.superwindow.data.wgpj.CaseSignRecord;
import com.zfsoft.superwindow.dbaccess.dao.DbCaseSignRecordMapper;
import com.zfsoft.superwindow.dbaccess.data.DbCaseSignRecord;
import com.zfsoft.superwindow.util.BeanUtils;
import com.zfsoft.superwindow.util.UUIDUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 办件签名记录表服务实现类
 *
 * @author wangwg
 * @date 2021-08-16
 */
@Service
public class CaseSignRecordManager {
    @Resource
    private DbCaseSignRecordMapper dbCaseSignRecordMapper;


    public Map<String,String> saveSignRecord(CaseSignRecord caseSignRecord) {
        Map<String,String> map = new HashMap<String,String>();
        DbCaseSignRecord dbCaseSignRecord= null;
        if(caseSignRecord.getCaseOid() == null) {
            throw new ResultInfoException("办件信息为空！");
        }else{
            dbCaseSignRecord = dbCaseSignRecordMapper.getSignRecordByCaseOid(caseSignRecord.getCaseOid());
        }
        if(caseSignRecord.getSignOid() == null) {
            caseSignRecord.setSignOid(UUIDUtil.randomUUID());
        }
        if(caseSignRecord.getCreateDate() == null) {
            caseSignRecord.setCreateDate(new Date());
        }
        if (dbCaseSignRecord ==null) {
            dbCaseSignRecord = new DbCaseSignRecord();
        }else{
            caseSignRecord.setId(dbCaseSignRecord.getId());
            caseSignRecord.setCreateDate(dbCaseSignRecord.getCreateDate());
        }
        BeanUtils.copyProperties(caseSignRecord,dbCaseSignRecord);
        if(dbCaseSignRecord.getId() ==null){
           dbCaseSignRecordMapper.insert(dbCaseSignRecord);
        }else{
           dbCaseSignRecordMapper.update(dbCaseSignRecord);
        }
        map.put("signOid",caseSignRecord.getSignOid());
        map.put("signUrl",caseSignRecord.getSignUrl());
        return  map;
    }

    public String querySignImgPath(String caseOid) {
        String url = "";
        DbCaseSignRecord dbCaseSignRecord = dbCaseSignRecordMapper.querySignImgPath(caseOid);
        if (dbCaseSignRecord != null) {
            url = dbCaseSignRecord.getSignUrl();
        }
        return url;
    }
}
