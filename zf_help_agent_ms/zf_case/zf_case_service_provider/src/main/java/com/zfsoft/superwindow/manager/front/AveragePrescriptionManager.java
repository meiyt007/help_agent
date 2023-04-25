package com.zfsoft.superwindow.manager.front;

import com.zfsoft.cases.feign.SysUserFeginService;
import com.zfsoft.microservice.platform.data.sys.SysUser;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.security.data.CurrentLoginUserHolder;
import com.zfsoft.superwindow.data.front.AveragePrescription;
import com.zfsoft.superwindow.dbaccess.dao.DbAveragePrescriptionMapper;
import com.zfsoft.superwindow.dbaccess.data.DbAveragePrescription;
import com.zfsoft.superwindow.util.SysCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @（#）: AveragePrescriptionManager
 * @description: 平均受理时效接口实现类
 * @author: wb
 * @date: 2021/07/17
 * @version: 1.0
 * @Copyright: 上海卓繁信息技术股份有限公司版权所有
 */
@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AveragePrescriptionManager {
    @Resource
    private DbAveragePrescriptionMapper averagePrescriptionMapper;

    @Resource
    private SysUserFeginService sysUserFeginService;

    public int getAveragePrescription() {
        int averageNum = 0;
        int totalNum = 0;
        List<DbAveragePrescription> averagePrescriptionList = new ArrayList<>();
        if (null != CurrentLoginUserHolder.getCurrentLoginUser()) {
            String userOid = CurrentLoginUserHolder.getCurrentLoginUser().getUserOid().toString();
            ApiResultSet<SysUser> resultSet = sysUserFeginService.getSysUserByUserOid(userOid);
            if (null != resultSet) {
                averagePrescriptionList = averagePrescriptionMapper.queryByUserOid(userOid);
            }
        }
        if (averagePrescriptionList.size() > 0) {
            for (DbAveragePrescription dbAveragePrescription : averagePrescriptionList) {
                totalNum += dbAveragePrescription.getProcessingTime();
            }
        }
        if (totalNum >= 0 && averagePrescriptionList.size() > 0) {
            averageNum = Math.round((float)totalNum/(float) (60 * averagePrescriptionList.size()));
        }
        return averageNum;
    }

    @Transactional(rollbackFor=Exception.class)
    public void addAveragePrescription(AveragePrescription averagePrescription) {
        DbAveragePrescription dbAveragePrescription = new DbAveragePrescription();
        if (null != CurrentLoginUserHolder.getCurrentLoginUser()) {
            String userOid = CurrentLoginUserHolder.getCurrentLoginUser().getUserOid().toString();
            ApiResultSet<SysUser> resultSet = sysUserFeginService.getSysUserByUserOid(userOid);
            if (null != resultSet) {
                dbAveragePrescription.setUserOid(userOid);
                dbAveragePrescription.setProcessingTime(getSeconds(averagePrescription.getProcessingTime()));
                dbAveragePrescription.setModifyDate(new Date());
                dbAveragePrescription.setCreateDate(new Date());
                dbAveragePrescription.setDelFlag(SysCode.ABLE_STATUS.NO);
            }
        }
        averagePrescriptionMapper.insert(dbAveragePrescription);
    }

    private Long getSeconds(String time) {
         Long seconds = Long.parseLong(time.substring(0,2)) * 3600
                + Long.parseLong(time.substring(3,5)) * 60
                + Long.parseLong(time.substring(6));
        return seconds;
    }
}
