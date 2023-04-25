package com.zfsoft.superwindow.manager.front;

import com.zfsoft.cases.feign.SysUserFeginService;
import com.zfsoft.microservice.platform.data.sys.SysUser;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.security.data.CurrentLoginUser;
import com.zfsoft.platform.common.security.data.CurrentLoginUserHolder;
import com.zfsoft.superwindow.data.front.VirtualBusinessRecord;
import com.zfsoft.superwindow.dbaccess.dao.DbVirtualBusinessRecordMapper;
import com.zfsoft.superwindow.dbaccess.data.DbVirtualBusinessRecord;
import com.zfsoft.superwindow.dbaccess.data.DbVirtualBusinessRecordManual;
import com.zfsoft.superwindow.util.BeanUtils;
import com.zfsoft.superwindow.util.SysCode;
import com.zfsoft.superwindow.util.UUIDUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class VirtualBusinessRecordManager {

    @Resource
    private DbVirtualBusinessRecordMapper dbVirtualBusinessRecordMapper;

    @Resource
    private SysUserFeginService sysUserFeginService;

    public List<DbVirtualBusinessRecordManual> queryList(VirtualBusinessRecord virtualBusinessRecord) {
        DbVirtualBusinessRecordManual dbVirtualBusinessRecordManual = new DbVirtualBusinessRecordManual();
        BeanUtils.copyProperties(virtualBusinessRecord, dbVirtualBusinessRecordManual);
        List<DbVirtualBusinessRecordManual> dbVirtualBusinessRecordManualList = this.dbVirtualBusinessRecordMapper.queryAll(dbVirtualBusinessRecordManual);
        return dbVirtualBusinessRecordManualList;
    }

    public String saveVirtualBusinessRecord(VirtualBusinessRecord virtualBusinessRecord) {
        if(virtualBusinessRecord.getOid() == null){//新增
            CurrentLoginUser user = CurrentLoginUserHolder.getCurrentLoginUser();
            ApiResultSet<SysUser> sysUser =  sysUserFeginService.getSysUserByUserOid(user.getUserOid());
            DbVirtualBusinessRecord dbVirtualBusinessRecord = new DbVirtualBusinessRecord();
            BeanUtils.copyProperties(virtualBusinessRecord, dbVirtualBusinessRecord);
            dbVirtualBusinessRecord.setCreateTime(new Date());
            dbVirtualBusinessRecord.setIsDelete(SysCode.DELETE_STATUS.NO);
            dbVirtualBusinessRecord.setCreateBy(user.getUserOid());
            //dbVirtualBusinessRecord.setCreateName(user.getUserName());
            dbVirtualBusinessRecord.setCreateName(sysUser.getData().getName());
            String oid =  UUIDUtil.randomUUID();
            dbVirtualBusinessRecord.setOid(oid);
            this.dbVirtualBusinessRecordMapper.insert(dbVirtualBusinessRecord);
            return  oid;
        }else{
            String oid = virtualBusinessRecord.getOid();
            DbVirtualBusinessRecord dbVirtualBusinessRecord = dbVirtualBusinessRecordMapper.queryByOid(oid);
            if(StringUtils.isNotEmpty(virtualBusinessRecord.getHandleOid())){
                dbVirtualBusinessRecord.setHandleOid(virtualBusinessRecord.getHandleOid());
            }
            if(StringUtils.isNotEmpty(virtualBusinessRecord.getPhone())){
                dbVirtualBusinessRecord.setPhone(virtualBusinessRecord.getPhone());
            }
            if(StringUtils.isNotEmpty(virtualBusinessRecord.getCaseUserName())){
                dbVirtualBusinessRecord.setCaseUserName(virtualBusinessRecord.getCaseUserName());
            }
            if (StringUtils.isNotEmpty(virtualBusinessRecord.getAudioAddress())) {
                dbVirtualBusinessRecord.setAudioAddress(virtualBusinessRecord.getAudioAddress());
            }
            dbVirtualBusinessRecordMapper.update(dbVirtualBusinessRecord);
            return  oid;
        }
    }

    public DbVirtualBusinessRecord getVirtualBusinessRecord(String virtualBusinessNum) {
        DbVirtualBusinessRecord dbVirtualBusinessRecord = new DbVirtualBusinessRecord();
        dbVirtualBusinessRecord = this.dbVirtualBusinessRecordMapper.queryByOid(virtualBusinessNum);
        return dbVirtualBusinessRecord;
    }
}
