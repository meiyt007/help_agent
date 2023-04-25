package com.zfsoft.superwindow.manager.wgpj;

import com.zfsoft.platform.common.security.data.CurrentLoginUser;
import com.zfsoft.platform.common.security.data.CurrentLoginUserHolder;
import com.zfsoft.superwindow.data.wgpj.SaveCallRecord;
import com.zfsoft.superwindow.dbaccess.dao.DbSaveCallRecordMapper;
import com.zfsoft.superwindow.dbaccess.data.DbSaveCallRecord;
import com.zfsoft.superwindow.util.BeanUtils;
import com.zfsoft.superwindow.util.StringUtils;
import com.zfsoft.superwindow.util.UUIDUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author sky
 * @version 1.0
 * @description: TODO
 * @date 2021/8/18 10:58
 */
@Slf4j
@Service
public class CallRecordManager {

    @Resource
    private DbSaveCallRecordMapper dbSaveCallRecordMapper;

    public List<DbSaveCallRecord> selectCallNums() {
        CurrentLoginUser currentLoginUser = CurrentLoginUserHolder.getCurrentLoginUser();
        Date now = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String time =  sdf.format(now);
        return dbSaveCallRecordMapper.selectCallNums(time,"");
    }

    public SaveCallRecord querySaveCallRecordByOid(String oid) {
        DbSaveCallRecord  dbSaveCallRecord  = dbSaveCallRecordMapper.queryByOid(oid);
        SaveCallRecord saveCallRecord = new SaveCallRecord();
        BeanUtils.copyProperties(dbSaveCallRecord,saveCallRecord);
        return saveCallRecord;
    }

    public String SaveCallRecord(SaveCallRecord saveCallRecord) {
        if(StringUtils.isNotEmpty(saveCallRecord.getOid())){
            DbSaveCallRecord  dbSaveCallRecord  = dbSaveCallRecordMapper.queryByOid(saveCallRecord.getOid());
            dbSaveCallRecord.setEndTime(new Date());
            if(StringUtils.isNotEmpty(saveCallRecord.getTimeLengthString())){
                String emptyString = saveCallRecord.getTimeLengthString().replaceAll(" ","");
                dbSaveCallRecord.setTimeLength(getSeconds(emptyString));
            }
            //Date startTime = dbSaveCallRecord.getStartTime();
            //long  timeLentgh = new Date().getTime() - startTime.getTime();
            //dbSaveCallRecord.setTimeLength(timeLentgh/1000);
            dbSaveCallRecord.setVirtualBusinessOid(saveCallRecord.getVirtualBusinessOid());
            dbSaveCallRecordMapper.update(dbSaveCallRecord);
            return  dbSaveCallRecord.getOid();
        }else{
            String oid =  UUIDUtil.randomUUID();
            DbSaveCallRecord  dbSaveCallRecord  = new DbSaveCallRecord();
            BeanUtils.copyProperties(saveCallRecord,dbSaveCallRecord);
            dbSaveCallRecord.setOid(oid);
            Date time = new Date();
            dbSaveCallRecord.setCreateTime(time);
            dbSaveCallRecord.setStartTime(time);
            CurrentLoginUser currentLoginUser = CurrentLoginUserHolder.getCurrentLoginUser();
            //dbSaveCallRecord.setCaseUserName(currentLoginUser==null?"":currentLoginUser.getUserName());
            dbSaveCallRecord.setCreateBy(currentLoginUser==null?"":currentLoginUser.getUserOid());
            dbSaveCallRecord.setIsDelete(0);
            dbSaveCallRecord.setTimeLength(0L);
            dbSaveCallRecordMapper.insert(dbSaveCallRecord);
            return oid;
        }
    }

    private Long getSeconds(String time) {
        Long seconds = Long.parseLong(time.substring(0,2)) * 3600
                + Long.parseLong(time.substring(3,5)) * 60
                + Long.parseLong(time.substring(6));
        return seconds;
    }

}
