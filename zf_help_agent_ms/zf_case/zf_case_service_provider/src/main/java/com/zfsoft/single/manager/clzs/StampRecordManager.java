package com.zfsoft.single.manager.clzs;

import com.zfsoft.single.util.BeanUtils;
import com.zfsoft.single.util.SysCode;
import com.zfsoft.single.util.UUIDUtil;
import com.zfsoft.superwindow.data.clzs.StampRecord;
import com.zfsoft.superwindow.dbaccess.dao.DbStampRecordMapper;
import com.zfsoft.superwindow.dbaccess.data.DbStampRecord;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.text.MessageFormat;
import java.util.Date;
import java.util.List;


/**
 * @ClassName StampRecordManager
 * @Description: 印章实现类
 * @Author liangss
 * @Date 2020-12-14 15:46:29
 **/
@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class StampRecordManager {

    @Resource
    private DbStampRecordMapper dbStampRecordMapper;
    /**
     * 保存更新信息
     * @param stampRecord
     */
    public void saveOrUpdate(StampRecord stampRecord) {
        if (null!=stampRecord.getId()) {
            DbStampRecord dbStampRecord=this.dbStampRecordMapper.selectByPrimaryKey(stampRecord.getId());
            Assert.notNull(dbStampRecord, MessageFormat.format("更新对象不存在！对象id为{0}", dbStampRecord.getId()));
            BeanUtils.copyProperties(stampRecord, dbStampRecord);
            dbStampRecord.setModifyDate(new Date());
            this.dbStampRecordMapper.updateByPrimaryKeySelective(dbStampRecord);
        } else {
            DbStampRecord dbStampRecord=new DbStampRecord();
            BeanUtils.copyProperties(stampRecord, dbStampRecord);
            dbStampRecord.setDelFlag(SysCode.DELETE_STATUS.NO);
            dbStampRecord.setCreateDate(new Date());
            dbStampRecord.setModifyDate(new Date());
            dbStampRecord.setAhsStampRecordOid(UUIDUtil.randomUUID());
            this.dbStampRecordMapper.insert(dbStampRecord);
        }
    }


    public StampRecord getStampRecordByCataOidAndAttaOid(String cataOid, String attaOid){
        DbStampRecord dbStampRecord=this.dbStampRecordMapper.getCaseFileAttaRecByOidAndCataOid(attaOid,cataOid);
        StampRecord stampRecord=new StampRecord();
        BeanUtils.copyProperties(dbStampRecord,stampRecord);
        return stampRecord;
    }

    public List<StampRecord> getCaseFileAttaRecListByOidAndCataOid(String cataOid, String attaOid){

        return null;
    }
}
