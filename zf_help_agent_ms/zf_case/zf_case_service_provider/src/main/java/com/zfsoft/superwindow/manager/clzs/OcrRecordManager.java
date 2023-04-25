package com.zfsoft.superwindow.manager.clzs;

import com.zfsoft.superwindow.data.clzs.OcrRecord;
import com.zfsoft.superwindow.dbaccess.dao.DbOcrRecordMapper;
import com.zfsoft.superwindow.dbaccess.data.DbOcrRecord;
import com.zfsoft.superwindow.util.SysCode;
import com.zfsoft.superwindow.util.UUIDUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.text.MessageFormat;
import java.util.Date;


/**
 * @ClassName OcrRecordManager
 * @Description: 营业执照识别
 * @Author liangss
 * @Date 2020-12-14 15:46:29
 **/
@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class OcrRecordManager {

    @Resource
    private DbOcrRecordMapper dbOcrRecordMapper;
    /**
     * 保存更新信息
    * @param ocrRecord
     */
    public void saveOrUpdate(OcrRecord ocrRecord) {
        if (null!=ocrRecord.getId()) {
            DbOcrRecord dbOcrRecord=this.dbOcrRecordMapper.selectByPrimaryKey(ocrRecord.getId());
            Assert.notNull(dbOcrRecord, MessageFormat.format("更新对象不存在！对象id为{0}", dbOcrRecord.getId()));
            BeanUtils.copyProperties(ocrRecord, dbOcrRecord);
            dbOcrRecord.setModifyDate(new Date());
            this.dbOcrRecordMapper.updateByPrimaryKeySelective(dbOcrRecord);
        } else {
            DbOcrRecord dbOcrRecord=new DbOcrRecord();
            BeanUtils.copyProperties(ocrRecord, dbOcrRecord);
            dbOcrRecord.setDelFlag(SysCode.DELETE_STATUS.NO);
            dbOcrRecord.setCreateDate(new Date());
            dbOcrRecord.setModifyDate(new Date());
            dbOcrRecord.setOcrRecordOid(UUIDUtil.randomUUID());
            this.dbOcrRecordMapper.insert(dbOcrRecord);
        }
    }

}
