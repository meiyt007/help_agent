package com.zfsoft.ocr.manager.common;


import cn.hutool.core.collection.CollUtil;
import com.zfsoft.ocr.data.pojo.common.OcrCallRecord;
import com.zfsoft.ocr.dbaccess.dao.DbOcrCallRecordMapper;
import com.zfsoft.ocr.dbaccess.data.DbOcrCallRecord;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.text.MessageFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;


/***
 * @Description:接口调用记录
 * @Author:liangss
 * @Date:2021/12/31
 * @Param:
 */
@Service
@Slf4j
@CacheConfig(cacheNames = "ocr:ocrCallRecord")
public class OcrCallRecordManager {

    @Resource
    private DbOcrCallRecordMapper dbOcrCallRecordMapper;


    @Caching(evict = {@CacheEvict(key = "'queryDbOcrCallRecordByMd5AndInterUrl:md5=' + #md5+',interUrl=' + #interUrl"),
            @CacheEvict(key = "'queryDbOcrCallRecordByMd5:md5=' + #md5")})
    public void saveOrUpdateOcrCallRecord(OcrCallRecord ocrCallRecord) {
        if (null != ocrCallRecord.getId()) {
            DbOcrCallRecord dbOcrCallRecord = this.dbOcrCallRecordMapper.selectByPrimaryKey(ocrCallRecord.getId());
            Assert.notNull(dbOcrCallRecord, MessageFormat.format("更新对象不存在！对象id为{0}", dbOcrCallRecord.getId()));
            BeanUtils.copyProperties(ocrCallRecord, dbOcrCallRecord);
            ocrCallRecord.setModifyDate(new Date());
            this.dbOcrCallRecordMapper.updateByPrimaryKeySelective(dbOcrCallRecord);
        } else {
            DbOcrCallRecord dbOcrCallRecord = new DbOcrCallRecord();
            BeanUtils.copyProperties(ocrCallRecord, dbOcrCallRecord);
            dbOcrCallRecord.setDeleteFlag(0);
            dbOcrCallRecord.setCreateDate(new Date());
            dbOcrCallRecord.setModifyDate(new Date());
            dbOcrCallRecord.setOid(UUID.randomUUID().toString().replaceAll("-", ""));
            this.dbOcrCallRecordMapper.insertSelective(dbOcrCallRecord);
        }
    }


    @Cacheable(key = "'queryDbOcrCallRecordByMd5:md5=' + #md5", unless = "#result == null")
    public OcrCallRecord queryDbOcrCallRecordByMd5(String md5) {
        Assert.hasLength(md5, "MD5不能为空！");
        DbOcrCallRecord dbOcrCallRecord = this.dbOcrCallRecordMapper.queryDbOcrCallRecordByMd5(md5);
        OcrCallRecord ocrCallRecord = new OcrCallRecord();
        if (null != dbOcrCallRecord) {
            BeanUtils.copyProperties(dbOcrCallRecord, ocrCallRecord);
        }
        return ocrCallRecord;
    }

    @Cacheable(key = "'queryDbOcrCallRecordByMd5AndInterUrl:md5=' + #md5+',interUrl=' + #interUrl", unless = "#result == null")
    public OcrCallRecord queryDbOcrCallRecordByMd5AndInterUrl(String md5, String interUrl) {
        Assert.hasLength(md5, "MD5不能为空！");
        Assert.hasLength(interUrl, "接口地址不能为空！");
        List<DbOcrCallRecord> dbOcrCallRecordList = this.dbOcrCallRecordMapper.queryDbOcrCallRecordByMd5AndInterUrl(md5, interUrl);
        OcrCallRecord ocrCallRecord = new OcrCallRecord();
        if (CollUtil.isNotEmpty(dbOcrCallRecordList)) {
            BeanUtils.copyProperties(dbOcrCallRecordList.get(0), ocrCallRecord);
        }
        return ocrCallRecord;
    }


}
