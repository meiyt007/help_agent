package com.zfsoft.single.manager.api;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zfsoft.cases.data.QlSysAtta;
import com.zfsoft.cases.service.SysAttaService;
import com.zfsoft.single.data.api.SignatureFlowRecord;
import com.zfsoft.single.dbaccess.dao.DbSignatureFlowRecordMapper;
import com.zfsoft.single.dbaccess.data.DbSignatureFlowRecord;
import com.zfsoft.single.dbaccess.data.DbSignatureFlowRecordExample;
import com.zfsoft.platform.utils.data.SysAttaTemp;
import com.zfsoft.platform.utils.fileUtil.UploadUtil;
import com.zfsoft.platform.utils.validate.ResultInfoException;
import com.zfsoft.platform.utils.validate.ValidGroups;
import com.zfsoft.single.util.BeanUtils;
import com.zfsoft.single.util.FileUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ApiManager {

    @Resource
    private DbSignatureFlowRecordMapper dbSignatureFlowRecordMapper;

    @Resource
    private SysAttaService qlSysAttaFeignService;

    public SignatureFlowRecord getSignatureFlowRecord(String caseOid, String materialOid) {
        if(StrUtil.isEmpty(caseOid) || StrUtil.isEmpty(materialOid)) {
            throw new ResultInfoException("办件主键或材料主键不能为空");
        }
        DbSignatureFlowRecordExample dbSignatureFlowRecordExample = new DbSignatureFlowRecordExample();
        DbSignatureFlowRecordExample.Criteria criteria = dbSignatureFlowRecordExample.createCriteria();
        criteria.andCaseOidEqualTo(caseOid);
        criteria.andMaterialOidEqualTo(materialOid);
        criteria.andDelFlagEqualTo(0);
        List<DbSignatureFlowRecord> dbSignatureFlowRecords = dbSignatureFlowRecordMapper.selectByExample(dbSignatureFlowRecordExample);
        if(null != dbSignatureFlowRecords && dbSignatureFlowRecords.size() != 0) {
            DbSignatureFlowRecord dbSignatureFlowRecord = dbSignatureFlowRecords.get(0);
            SignatureFlowRecord signatureFlowRecord = new SignatureFlowRecord();
            BeanUtils.copyProperties(dbSignatureFlowRecord, signatureFlowRecord);
            return signatureFlowRecord;
        }
        return null;
    }

    public String saveOrUpdateSignatureFlow(
            @ValidGroups(groups = {SignatureFlowRecord.INSERT_GROUP.class})SignatureFlowRecord signatureFlowRecord) {
        if(null == signatureFlowRecord) {
            throw new ResultInfoException("保存信息不能为空");
        }
        Map<String, String> params = new HashMap<>();
        params.put("caseOid", signatureFlowRecord.getCaseOid());
        params.put("materialOid", signatureFlowRecord.getMaterialOid());
        DbSignatureFlowRecord oldInfo = dbSignatureFlowRecordMapper.getDbSignatureFlowRecordByParams(params);
        if(null != oldInfo) {
            oldInfo.setFlowId(signatureFlowRecord.getFlowId());
            oldInfo.setStatus(signatureFlowRecord.getStatus());
            oldInfo.setModifyDate(new Date());
            dbSignatureFlowRecordMapper.updateByPrimaryKeySelective(oldInfo);
            return oldInfo.getOid();
        }
        DbSignatureFlowRecord dbSignatureFlowRecord = new DbSignatureFlowRecord();
        BeanUtils.copyProperties(signatureFlowRecord, dbSignatureFlowRecord);
        dbSignatureFlowRecord.setOid(IdUtil.simpleUUID());
        dbSignatureFlowRecord.setDelFlag(0);
        dbSignatureFlowRecord.setCreateDate(new Date());
        dbSignatureFlowRecordMapper.insert(dbSignatureFlowRecord);
        return dbSignatureFlowRecord.getOid();
    }

    public List<SignatureFlowRecord> getSignatureFlowRecordByCaseOid(String caseOid) {
        if(StrUtil.isEmpty(caseOid)) {
            throw new ResultInfoException("办件编号不能为空");
        }
        DbSignatureFlowRecordExample flowRecordExample = new DbSignatureFlowRecordExample();
        DbSignatureFlowRecordExample.Criteria criteria = flowRecordExample.createCriteria();
        criteria.andCaseOidEqualTo(caseOid);
        criteria.andDelFlagEqualTo(0);
        List<DbSignatureFlowRecord> dbSignatureFlowRecordList = dbSignatureFlowRecordMapper.selectByExample(flowRecordExample);
        List<SignatureFlowRecord> signatureFlowRecords = dbSignatureFlowRecordList.stream().map(dbSignatureFlowRecord -> {
            SignatureFlowRecord signatureFlowRecord = new SignatureFlowRecord();
            BeanUtils.copyProperties(dbSignatureFlowRecord, signatureFlowRecord);
            return signatureFlowRecord;
        }).collect(Collectors.toList());
        return signatureFlowRecords;
    }

    public void electronicSignatureCallBack(HttpServletRequest request) {
        StringBuilder result = new StringBuilder();
        try {
            String line;
            BufferedReader reader = request.getReader();
            while (null != (line = reader.readLine())) {
                result.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("回调结果=====》" + result.toString());
        if(StrUtil.isEmpty(result.toString())) {
            return;
        }
        JSONObject jsonObject = JSON.parseObject(result.toString());
        // 签署流程 ID
        String flowId = jsonObject.getString("flowId");
        // 流程状态
        String status = jsonObject.getString("status");
        //
        DbSignatureFlowRecord record = dbSignatureFlowRecordMapper.getDbSignatureFlowRecordByFlowId(flowId);
        if(null == record) {
            System.out.println("====>流程信息不存在：flowId=" + flowId );
            return;
        }
        if(null == status) {
            record.setStatus(0);// 未签署
        } else if("2".equals(status)) {
            record.setStatus(1);// 已签署
        } else if("5".equals(status)) {
            record.setStatus(0);// 未签署
        } else if("6".equals(status)) {
            record.setStatus(2);// 签署中
        } else if("7".equals(status)) {
            record.setStatus(3);// 拒签
        } else {
            record.setStatus(2);// 签署中
        }
        String finishDocUrlBeans = jsonObject.getString("finishDocUrlBeans");
        if(StrUtil.isNotEmpty(finishDocUrlBeans)) {
            JSONArray jsonArray = JSONArray.parseArray(finishDocUrlBeans);
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject object = (JSONObject)jsonArray.get(i);
                // 签署流程结束的文档下载地址
                String downloadDocUrl = object.getString("downloadDocUrl");
                MultipartFile multipartFile = FileUtils.transFile(downloadDocUrl);
                if(null != multipartFile) {
                    try {
                        HttpServletRequest request1 = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
                        UploadUtil uploadUtil = new UploadUtil(request1);
                        String filePath = uploadUtil.uploadFile(multipartFile);
                        SysAttaTemp tempAtta = uploadUtil.getSysAttaFile(filePath, "00000000000000000000000000000010");
                        QlSysAtta qlSysAtta = new QlSysAtta();
                        qlSysAtta.setFastdfsNginxUrl(tempAtta.getFastdfsNginxUrl());
                        qlSysAtta.setName(tempAtta.getName());
                        qlSysAtta.setOriginName(tempAtta.getOriginName());
                        //保存附件信息
                        qlSysAttaFeignService.saveSysAtta(qlSysAtta);
                        record.setDownloadUrl(qlSysAtta.getFastdfsNginxUrl());
                        record.setAttaOids(qlSysAtta.getAttaOid());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if(null != record) {
                    //record.setDownloadUrl(downloadDocUrl);
                    //record.setFileKey(docFileKey);
                    record.setModifyDate(new Date());
                    dbSignatureFlowRecordMapper.updateByPrimaryKeySelective(record);
                }
            }
        }
    }

}
