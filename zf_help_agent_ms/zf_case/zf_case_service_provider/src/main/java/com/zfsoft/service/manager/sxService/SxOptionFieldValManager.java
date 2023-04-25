package com.zfsoft.service.manager.sxService;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.zfsoft.platform.utils.validate.ResultInfoException;
import com.zfsoft.service.dbaccess.dao.sxService.DbSxOptionFieldValMapper;
import com.zfsoft.service.dbaccess.dao.sxSituation.DbSxServiceOptionTitleMapper;
import com.zfsoft.service.dbaccess.data.sxService.DbSxOptionFieldVal;
import com.zfsoft.service.dbaccess.data.sxService.DbSxOptionFieldValExample;
import com.zfsoft.service.dbaccess.data.sxSituation.DbSxServiceOptionTitle;
import com.zfsoft.service.dbaccess.data.sxSituation.DbSxServiceOptionTitleExample;
import com.zfsoft.service.manager.sxSituation.SxServiceOptionValManager;
import com.zfsoft.service.sxService.data.FieldTree;
import com.zfsoft.service.sxService.data.SxFillField;
import com.zfsoft.service.sxService.data.SxOptionFieldVal;
import com.zfsoft.service.sxSituation.data.SxServiceOptionTitle;
import com.zfsoft.service.sxSituation.data.SxServiceOptionVal;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SxOptionFieldValManager {

    @Resource
    private DbSxServiceOptionTitleMapper dbSxServiceOptionTitleMapper;

    @Resource
    private SxServiceOptionValManager sxServiceOptionValManager;

    @Resource
    private DbSxOptionFieldValMapper dbSxOptionFieldValMapper;

    @Resource
    private SxFillLabelManager sxFillLabelManager;

    @Resource
    private SxFillFieldManager sxFillFieldManager;

    public Map<String, Object> initOptionFieldValInfo(String serviceOid, String oid) {
        Map<String, Object> resultMap = new HashMap<>();
        if (StrUtil.isEmpty(serviceOid)) {
            throw new ResultInfoException("事项主键不能为空");
        }
        DbSxServiceOptionTitleExample optionTitleExample = new DbSxServiceOptionTitleExample();
        DbSxServiceOptionTitleExample.Criteria criteria_title = optionTitleExample.createCriteria();
        criteria_title.andServiceOidEqualTo(serviceOid);
        criteria_title.andDeleteStatusEqualTo((short) 0);
        List<DbSxServiceOptionTitle> optionTitles = dbSxServiceOptionTitleMapper.selectByExample(optionTitleExample);
        List<SxServiceOptionTitle> sxServiceOptionTitles = optionTitles.stream().map(dbSxServiceOptionTitle -> {
            SxServiceOptionTitle sxServiceOptionTitle = new SxServiceOptionTitle();
            BeanUtils.copyProperties(dbSxServiceOptionTitle, sxServiceOptionTitle);
            List<SxServiceOptionVal> sxServiceOptionVals = sxServiceOptionValManager.getSxServiceOptionValsByTitleOid(sxServiceOptionTitle.getOid());
            sxServiceOptionTitle.setSxServiceOptionVals(sxServiceOptionVals);
            return sxServiceOptionTitle;
        }).collect(Collectors.toList());
        //
        List<FieldTree> fieldTrees = sxFillLabelManager.querySxFieldTypeAndLabelTreeNew(serviceOid);
        if (StrUtil.isNotEmpty(oid)) {
            DbSxOptionFieldVal dbSxOptionFieldVal = dbSxOptionFieldValMapper.getDbSxOptionFieldValByOid(oid);
            if (null != dbSxOptionFieldVal) {
                SxOptionFieldVal sxOptionFieldVal = new SxOptionFieldVal();
                BeanUtils.copyProperties(dbSxOptionFieldVal, sxOptionFieldVal);
                resultMap.put("fieldValRel", sxOptionFieldVal);
                List<SxFillField> sxFillFields = sxFillFieldManager.selectFieldListByServiceOidAndTypeOid(serviceOid, sxOptionFieldVal.getFieldTypeOid());
                resultMap.put("fieldList", sxFillFields);
                if (StrUtil.isNotEmpty(sxOptionFieldVal.getValOids())) {
                    String[] arr = sxOptionFieldVal.getValOids().split(",");
                    for (String valOid : arr) {
                        SxServiceOptionVal optionVal = sxServiceOptionValManager.getSxServiceOptionValByOid(valOid);
                        for (SxServiceOptionTitle title : sxServiceOptionTitles) {
                            if (optionVal.getTitleOid().equals(title.getOid())) {
                                title.setIsSelected(1);
                            }
                        }
                    }
                }
            }
        }
        resultMap.put("optionTitleList", sxServiceOptionTitles);
        resultMap.put("labelList", fieldTrees);
        return resultMap;
    }

    public List<SxOptionFieldVal> getFieldFillValList(String serviceOid) {
        if (StrUtil.isEmpty(serviceOid)) {
            throw new ResultInfoException("事项主键不能为空");
        }
        DbSxOptionFieldValExample dbSxOptionFieldValExample = new DbSxOptionFieldValExample();
        DbSxOptionFieldValExample.Criteria criteria = dbSxOptionFieldValExample.createCriteria();
        criteria.andServiceOidEqualTo(serviceOid);
        criteria.andDeleteFlagEqualTo(0);
        List<DbSxOptionFieldVal> dbSxOptionFieldVals = dbSxOptionFieldValMapper.selectByExample(dbSxOptionFieldValExample);
        List<SxOptionFieldVal> sxOptionFieldVals = dbSxOptionFieldVals.stream().map(dbSxOptionFieldVal -> {
            SxOptionFieldVal sxOptionFieldVal = new SxOptionFieldVal();
            BeanUtils.copyProperties(dbSxOptionFieldVal, sxOptionFieldVal);
            SxFillField sxFillField = sxFillFieldManager.getSxFillFieldByOid(sxOptionFieldVal.getFieldOid());
            if (null != sxFillField) {
                String fillInfo = sxOptionFieldVal.getValInfo();
                String fieldName = "";
                if (StrUtil.isNotEmpty(fillInfo)) {
                    for (String str : fillInfo.split(",")) {
                        fieldName += str.split(":")[0] + ";";
                    }
                }
                sxOptionFieldVal.setFieldName(sxFillField.getFieldName() + "【" + fieldName + "】");
            }
            String optionValOids = sxOptionFieldVal.getValOids();
            List<String> optionValNames = new ArrayList<>();
            if (StrUtil.isNotEmpty(optionValOids)) {
                for (String oid : optionValOids.split(",")) {
                    SxServiceOptionVal optionVal = sxServiceOptionValManager.getSxServiceOptionValByOid(oid);
                    if (null != optionVal) {
                        optionValNames.add(optionVal.getName());
                    }
                }
                sxOptionFieldVal.setOptionNames(optionValNames);
            }
            return sxOptionFieldVal;
        }).collect(Collectors.toList());
        return sxOptionFieldVals;
    }

    /**
     * 获取基础的字段值列表
     * @param serviceOid
     * @return
     */
    public List<SxOptionFieldVal> getFieldValList(String serviceOid) {
        if (StrUtil.isEmpty(serviceOid)) {
            return new ArrayList<>();
        }
        DbSxOptionFieldValExample dbSxOptionFieldValExample = new DbSxOptionFieldValExample();
        DbSxOptionFieldValExample.Criteria criteria = dbSxOptionFieldValExample.createCriteria();
        criteria.andServiceOidEqualTo(serviceOid);
        criteria.andDeleteFlagEqualTo(0);
        List<DbSxOptionFieldVal> dbSxOptionFieldVals = dbSxOptionFieldValMapper.selectByExample(dbSxOptionFieldValExample);
        List<SxOptionFieldVal> sxOptionFieldVals = dbSxOptionFieldVals.stream().map(dbSxOptionFieldVal -> {
            SxOptionFieldVal sxOptionFieldVal = new SxOptionFieldVal();
            BeanUtils.copyProperties(dbSxOptionFieldVal, sxOptionFieldVal);
            return sxOptionFieldVal;
        }).collect(Collectors.toList());
        return sxOptionFieldVals;
    }

    public List<SxOptionFieldVal> queryFieldFillList(String serviceOid, List<String> fieldOidList) {
        DbSxOptionFieldValExample fieldValExample = new DbSxOptionFieldValExample();
        DbSxOptionFieldValExample.Criteria criteria = fieldValExample.createCriteria();
        criteria.andServiceOidEqualTo(serviceOid);
//        criteria.andFieldOidEqualTo(fieldOid);
        if(fieldOidList!=null&&fieldOidList.size()>0){
            criteria.andFieldOidIn(fieldOidList);
        }
        criteria.andDeleteFlagEqualTo(0);
        List<DbSxOptionFieldVal> dbSxOptionFieldVals = dbSxOptionFieldValMapper.selectByExample(fieldValExample);
        List<SxOptionFieldVal> sxOptionFieldVals = dbSxOptionFieldVals.stream().map(dbSxOptionFieldVal -> {
            SxOptionFieldVal sxOptionFieldVal = new SxOptionFieldVal();
            BeanUtils.copyProperties(dbSxOptionFieldVal, sxOptionFieldVal);
            return sxOptionFieldVal;
        }).collect(Collectors.toList());
        return sxOptionFieldVals;
    }

    public void saveSxOptionFieldValInfo(SxOptionFieldVal sxOptionFieldVal) {
        if (null == sxOptionFieldVal) {
            throw new ResultInfoException("字段填充信息不能为空");
        }
        if (null == sxOptionFieldVal.getId() || null == sxOptionFieldVal.getOid()) {
            DbSxOptionFieldVal dbSxOptionFieldVal = new DbSxOptionFieldVal();
            BeanUtils.copyProperties(sxOptionFieldVal, dbSxOptionFieldVal);
            dbSxOptionFieldVal.setOid(IdUtil.simpleUUID());
            dbSxOptionFieldVal.setDeleteFlag(0);
            dbSxOptionFieldVal.setCreateDate(new Date());
            dbSxOptionFieldValMapper.insert(dbSxOptionFieldVal);
        } else {
            DbSxOptionFieldVal oldInfo = dbSxOptionFieldValMapper.getDbSxOptionFieldValByOid(sxOptionFieldVal.getOid());
            if (null != oldInfo) {
                BeanUtils.copyProperties(sxOptionFieldVal, oldInfo);
                oldInfo.setModifyDate(new Date());
                dbSxOptionFieldValMapper.updateByPrimaryKeySelective(oldInfo);
            }
        }
    }

    public int delSxOptionFieldVal(String oid) {
        if (StrUtil.isEmpty(oid)) {
            throw new ResultInfoException("信息主键不能为空");
        }
        DbSxOptionFieldVal dbSxOptionFieldVal = dbSxOptionFieldValMapper.getDbSxOptionFieldValByOid(oid);
        if (null == dbSxOptionFieldVal) {
            throw new ResultInfoException("删除失败，字段填充信息为空");
        }
        dbSxOptionFieldVal.setDeleteFlag(1);
        dbSxOptionFieldVal.setModifyDate(new Date());
        return dbSxOptionFieldValMapper.updateByPrimaryKeySelective(dbSxOptionFieldVal);
    }
}
