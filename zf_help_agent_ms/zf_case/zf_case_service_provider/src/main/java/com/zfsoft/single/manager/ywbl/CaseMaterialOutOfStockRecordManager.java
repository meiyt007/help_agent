package com.zfsoft.single.manager.ywbl;

import com.zfsoft.single.data.ywbl.CaseMaterialOutOfStockRecord;
import com.zfsoft.single.dbaccess.dao.ywbl.DbCaseMaterialOutOfStockRecordMapper;
import com.zfsoft.single.dbaccess.data.ywbl.DbCaseMaterialOutOfStockRecord;
import com.zfsoft.single.dbaccess.data.ywbl.DbCaseMaterialOutOfStockRecordExample;
import com.zfsoft.single.util.BeanUtils;
import com.zfsoft.single.util.StringUtils;
import com.zfsoft.single.util.SysCode;
import com.zfsoft.single.util.UUIDUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.text.MessageFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


/**
 * @author: dongxl
 * @create: 2020-11-6
 * @description: 办件材料出库
 */
@Slf4j
@Service
public class CaseMaterialOutOfStockRecordManager {

    @Resource
    private DbCaseMaterialOutOfStockRecordMapper dbCaseMaterialOutOfStockRecordMapper;


    /***
    * @Description:  保存更新修改出库记录
    * @Author:liangss
    * @Date:2021/9/8
    * @Param: [caseMaterialOutOfStockRecord]
    */
    @Transactional(rollbackFor = Exception.class)
    public void saveOrUpdate(CaseMaterialOutOfStockRecord caseMaterialOutOfStockRecord) {
        if(caseMaterialOutOfStockRecord!=null){
            if(caseMaterialOutOfStockRecord.getId()!=null){
                DbCaseMaterialOutOfStockRecord dbCaseMaterialOutOfStockRecord=dbCaseMaterialOutOfStockRecordMapper.selectByPrimaryKey(caseMaterialOutOfStockRecord.getId());
                Assert.notNull(dbCaseMaterialOutOfStockRecord, MessageFormat.format("更新对象不存在！对象id为{0}", caseMaterialOutOfStockRecord.getId()));
                org.springframework.beans.BeanUtils.copyProperties(caseMaterialOutOfStockRecord, dbCaseMaterialOutOfStockRecord);
                dbCaseMaterialOutOfStockRecord.setModifyDate(new Date());
                dbCaseMaterialOutOfStockRecord.setModifyDate(new Date());
                this.dbCaseMaterialOutOfStockRecordMapper.updateByPrimaryKeySelective(dbCaseMaterialOutOfStockRecord);
            }else{
                DbCaseMaterialOutOfStockRecord dbCaseMaterialOutOfStockRecord=new DbCaseMaterialOutOfStockRecord();
                BeanUtils.copyProperties(caseMaterialOutOfStockRecord,dbCaseMaterialOutOfStockRecord);
                dbCaseMaterialOutOfStockRecord.setOid(UUIDUtil.randomUUID());
                dbCaseMaterialOutOfStockRecord.setDelFlag(0);
                dbCaseMaterialOutOfStockRecord.setCreateDate(new Date());
                dbCaseMaterialOutOfStockRecord.setModifyDate(new Date());
                this.dbCaseMaterialOutOfStockRecordMapper.insertSelective(dbCaseMaterialOutOfStockRecord);
            }
        }
    }



    /***
    * @Description:根据id查询出库数据记录
    * @Author:liangss
    * @Date:2021/9/8
    * @Param: [id]
    */
    public CaseMaterialOutOfStockRecord getCaseMaterialOutOfStockRecordById(Long id){
        DbCaseMaterialOutOfStockRecord dbCaseMaterialOutOfStockRecord=dbCaseMaterialOutOfStockRecordMapper.selectByPrimaryKey(id);
        if(dbCaseMaterialOutOfStockRecord!=null){
            CaseMaterialOutOfStockRecord cm=new CaseMaterialOutOfStockRecord();
            BeanUtils.copyProperties(dbCaseMaterialOutOfStockRecord,cm);
            return cm;
        }
        return null;
    }


    public List<CaseMaterialOutOfStockRecord> queryCaseMaterialOutOfStockRecordList(CaseMaterialOutOfStockRecord caseMaterialOutOfStockRecord){
        DbCaseMaterialOutOfStockRecordExample dbCaseMaterialOutOfStockRecordExample=new DbCaseMaterialOutOfStockRecordExample();
        DbCaseMaterialOutOfStockRecordExample.Criteria criteria=dbCaseMaterialOutOfStockRecordExample.createCriteria();

        if(null!=caseMaterialOutOfStockRecord){
            if(StringUtils.isNotEmpty(caseMaterialOutOfStockRecord.getOutOid())){
                criteria.andOutOidEqualTo(caseMaterialOutOfStockRecord.getOutOid());
            }
            if(StringUtils.isNotEmpty(caseMaterialOutOfStockRecord.getRegOid())){
                criteria.andRegOidEqualTo(caseMaterialOutOfStockRecord.getRegOid());
            }
        }
        criteria.andDelFlagEqualTo(SysCode.STATUS.NO);
        dbCaseMaterialOutOfStockRecordExample.setOrderByClause(" CREATE_DATE desc");

        List<DbCaseMaterialOutOfStockRecord> caseMaterialOutOfStockRecordLists=dbCaseMaterialOutOfStockRecordMapper.selectByExample(dbCaseMaterialOutOfStockRecordExample);

        List<CaseMaterialOutOfStockRecord> caseMaterialOutOfStockRecordList=caseMaterialOutOfStockRecordLists.stream().map(dbCaseMaterialOutOfStockRecord -> {
            CaseMaterialOutOfStockRecord caseMaterialOutOfStockRecord1 = new CaseMaterialOutOfStockRecord();
            org.springframework.beans.BeanUtils.copyProperties(dbCaseMaterialOutOfStockRecord, caseMaterialOutOfStockRecord1);
            return caseMaterialOutOfStockRecord1;
        }).collect(Collectors.toList());
        return caseMaterialOutOfStockRecordList;
    }

}
