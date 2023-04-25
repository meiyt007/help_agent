package com.zfsoft.ha.managers;

import com.zfsoft.cases.util.DateUtil;
import com.zfsoft.cases.util.StringUtil;
import com.zfsoft.ha.constant.Constants;
import com.zfsoft.ha.data.HaLoginUserInfo;
import com.zfsoft.ha.data.HaWorkService;
import com.zfsoft.ha.data.HaWorkTurnRecord;
import com.zfsoft.ha.data.requestData.HaWorkServiceRequestData;
import com.zfsoft.ha.data.vo.HaWorkServiceVo;
import com.zfsoft.ha.dbaccess.dao.DbHaWorkQueueMapper;
import com.zfsoft.ha.dbaccess.dao.DbHaWorkServiceMapper;
import com.zfsoft.ha.dbaccess.data.DbHaWorkQueue;
import com.zfsoft.ha.dbaccess.data.DbHaWorkService;
import com.zfsoft.ha.dbaccess.data.vo.DbHaWorkServiceVo;
import com.zfsoft.ha.util.HaLoginUserHolder;
import com.zfsoft.platform.utils.bean.BeanUtils;
import com.zfsoft.superwindow.service.easyquickcase.data.ServiceException;
import com.zfsoft.superwindow.util.BASE64Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 帮代办服务业务逻辑层
 *
 * @author kangax
 * @version 1.0
 * @date 2022/7/21 下午11:44
 */
@Service
@Slf4j
public class HaWorkServiceManager {
    /**
     * 帮代办服务表mapper
     */
    @Resource
    private DbHaWorkServiceMapper dbHaWorkServiceMapper;

    /**
     * 办事队列mapper
     */
    @Resource
    private DbHaWorkQueueMapper dbHaWorkQueueMapper;

    /**
     * 转派服务service
     */
    @Resource
    private HaWorkTurnRecordManager haWorkTurnRecordManager;
    /**
     * @description: 完成服务保存服务记录
     * @params： [ haWorkServiceRequestData 完成服务请求参数]
     * @return: int
     * @author: kangax
     * @date: 2022-07-27 18:55
     */
    @Transactional(rollbackFor = ServiceException.class)
    public Long completeService(HaWorkServiceRequestData haWorkServiceRequestData) throws ServiceException {
        //获取当前用户信息
        HaLoginUserInfo currentHaLoginUserInfo = HaLoginUserHolder.getCurrentHaLoginUserInfo();
        //获取当前办事队列信息
        DbHaWorkQueue dbHaWorkQueue = dbHaWorkQueueMapper.selectByPrimaryKey(haWorkServiceRequestData.getWorkQueueId());
        if (dbHaWorkQueue == null) {
            throw new ServiceException("当前办事队列为空");
        }
        //判断当前操作用是否正确
        if (!dbHaWorkQueue.getServiceWorkUserId().equals(currentHaLoginUserInfo.getId())) {
            throw new ServiceException("当前办事队列不属于当前操作用户");
        }
        String memo = BASE64Utils.base64Decode(haWorkServiceRequestData.getServiceMemo());
        haWorkServiceRequestData.setServiceMemo(memo);
        DbHaWorkService dbHaWorkService = new DbHaWorkService();
        BeanUtils.copyProperties(haWorkServiceRequestData, dbHaWorkService);
        //查询该办事队列是否有转派记录
        HaWorkTurnRecord haWorkTurnRecord = new HaWorkTurnRecord();
        haWorkTurnRecord.setTurnStatus(Constants.TURN_STATUS_ACCEPT);
        haWorkTurnRecord.setServiceWorkUserId(currentHaLoginUserInfo.getId().toString());
        haWorkTurnRecord.setWorkQueueId(dbHaWorkQueue.getId());
        List<HaWorkTurnRecord> haWorkTurnRecords = haWorkTurnRecordManager.queryTurnByExample(haWorkTurnRecord);
        if (haWorkTurnRecords!=null&&haWorkTurnRecords.size()>0){
            dbHaWorkService.setTurnRecordId(haWorkTurnRecords.get(0).getId());
        }
        dbHaWorkService.setCreateDate(new Date());
        //设置创建人
        dbHaWorkService.setCreateBy(currentHaLoginUserInfo.getName());
        dbHaWorkService.setWorkUserId(currentHaLoginUserInfo.getId());
        dbHaWorkService.setOperatorName(dbHaWorkQueue.getName());
        dbHaWorkService.setOperatorCardNo(dbHaWorkQueue.getCardNo());
        dbHaWorkService.setOperatorPhone(dbHaWorkQueue.getPhone());
        if("4".equals(dbHaWorkService.getServiceType())){
            dbHaWorkService.setPushDate(new Date());
        }
        dbHaWorkServiceMapper.insertSelective(dbHaWorkService);
        return dbHaWorkService.getId();
    }

    /**
     * 根据多个办事id，获取帮代办服务记录
     * @param workQueueIds
     * @return
     * @throws ServiceException
     */
    public List<HaWorkServiceVo> selectByHaWorkService(List<Long> workQueueIds) throws ServiceException {
        List<DbHaWorkServiceVo> dbHaWorkServices = dbHaWorkServiceMapper.selectByWorkQueueIds(workQueueIds);
        List<HaWorkServiceVo> haQuestionList = dbHaWorkServices.stream().map(dbHaWorkServiceVo1 -> {
            HaWorkServiceVo HaWorkServiceVo1 = new HaWorkServiceVo();
            BeanUtils.copyProperties(dbHaWorkServiceVo1, HaWorkServiceVo1);
            return HaWorkServiceVo1;
        }).collect(Collectors.toList());
        return haQuestionList;
    }

    /**
     * 更新workService
     * @param haWorkServiceId
     * @param serviceStatus
     * @throws ServiceException
     */
    public void updateService(Long haWorkServiceId,String serviceStatus) throws ServiceException {
        //获取当前用户信息
        HaLoginUserInfo currentHaLoginUserInfo = HaLoginUserHolder.getCurrentHaLoginUserInfo();
        DbHaWorkService dbHaWorkService = dbHaWorkServiceMapper.selectByPrimaryKey(haWorkServiceId);
        if(StringUtil.isNotEmpty(serviceStatus)){
            dbHaWorkService.setServiceStatus(serviceStatus);
        }
        dbHaWorkService.setUpdateBy(currentHaLoginUserInfo.getName());
        dbHaWorkService.setUpdateDate(DateUtil.getDate());
        dbHaWorkServiceMapper.updateByPrimaryKeySelective(dbHaWorkService);
    }

    public HaWorkService selectByCaseOid(String caseOid){
        HaWorkService haWorkService = new HaWorkService();
        DbHaWorkService dbHaWorkService = dbHaWorkServiceMapper.selectByCaseOid(caseOid);
        BeanUtils.copyProperties(dbHaWorkService,haWorkService);
        return haWorkService;
    }

    public int getTempServiceNum(Long userID){
        return dbHaWorkServiceMapper.getTempServiceNum(userID);
    }
}
