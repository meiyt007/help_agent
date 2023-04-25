package com.zfsoft.ha.managers;

import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zfsoft.cases.util.DateUtil;
import com.zfsoft.cases.util.StringUtil;
import com.zfsoft.ha.constant.Constants;
import com.zfsoft.ha.data.HaLoginUserInfo;
import com.zfsoft.ha.data.HaWorkTurnRecord;
import com.zfsoft.ha.data.responseData.HaGetWorkQueueByTurnIdResponseData;
import com.zfsoft.ha.data.responseData.HaTurnIsAcceptResponseData;
import com.zfsoft.ha.data.vo.HaWorkQueueVo;
import com.zfsoft.ha.data.vo.HaWorkTurnRecordVo;
import com.zfsoft.ha.dbaccess.dao.DbHaWorkQueueMapper;
import com.zfsoft.ha.dbaccess.dao.DbHaWorkServiceMapper;
import com.zfsoft.ha.dbaccess.dao.DbHaWorkTurnRecordMapper;
import com.zfsoft.ha.dbaccess.dao.DbHaWorkUserMapper;
import com.zfsoft.ha.dbaccess.data.DbHaWorkQueue;
import com.zfsoft.ha.dbaccess.data.DbHaWorkTurnRecord;
import com.zfsoft.ha.dbaccess.data.DbHaWorkUser;
import com.zfsoft.ha.dbaccess.data.example.DbHaWorkTurnRecordExample;
import com.zfsoft.ha.dbaccess.data.vo.DbHaWorkQueueVo;
import com.zfsoft.ha.dbaccess.data.vo.DbHaWorkServiceVo;
import com.zfsoft.ha.dbaccess.data.vo.DbHaWorkTurnRecordVo;
import com.zfsoft.ha.util.HaLoginUserHolder;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.superwindow.service.easyquickcase.data.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.zfsoft.ha.constant.RedisConstants.TURN_TIMEOUT;

/**
 * @description: 办事转派记录manager
 * @author: kangax
 * @date: 2022-08-03 14:15
 **/
@Service
@Slf4j
public class HaWorkTurnRecordManager {

    /**
     * 办事转派记录mapper
     */
    @Resource
    private DbHaWorkTurnRecordMapper dbHaWorkTurnRecordMapper;

    /**
     * 帮代办人员管理 mapper
     */
    @Resource
    private DbHaWorkUserMapper dbHaWorkUserMapper;

    /**
     * 办事队列mapper
     */
    @Resource
    private DbHaWorkQueueMapper dbHaWorkQueueMapper;
    /**
     * 办事服务
     */
    @Resource
    private DbHaWorkServiceMapper dbHaWorkServiceMapper;

    /**
     * @description: 根据转派记录编号查询转派状态时候接收
     * @param turnRecordId 办事转派记录编号
     * @return: HaTurnIsAcceptResponseData
     * @author: kangax
     * @date: 2022-08-03 14:25
     */
    public HaTurnIsAcceptResponseData turnIsAccept(Long turnRecordId) throws ServiceException {
        //获取当前用户信息
        HaLoginUserInfo currentHaLoginUserInfo = HaLoginUserHolder.getCurrentHaLoginUserInfo();
        HaTurnIsAcceptResponseData haTurnIsAcceptResponseData = new HaTurnIsAcceptResponseData();
        //根据转派记录编号查询转派记录
        DbHaWorkTurnRecord dbHaWorkTurnRecord = dbHaWorkTurnRecordMapper.selectByPrimaryKey(turnRecordId);
        if (dbHaWorkTurnRecord == null) {
            throw new ServiceException("根据转派记录编号查询转派记录为空");
        }
        //判断当前转派记录是否属于当前操作用户
        if (!dbHaWorkTurnRecord.getOldServiceWorkUserId().equals(currentHaLoginUserInfo.getId())) {
            throw new ServiceException("检测到当前转派服务不属于当前用户，操作失败！");
        }
        //获取当前转派工作人员信息
        DbHaWorkUser dbHaWorkUser = dbHaWorkUserMapper.queryById(dbHaWorkTurnRecord.getServiceWorkUserId());
//        if (dbHaWorkUser == null) {
//            throw new ServiceException("获取当前转派人员信息为空");
//        }
        //获取转派状态
        String turnStatus = dbHaWorkTurnRecord.getTurnStatus();
        //处理当30秒时间转派人员未处理则进行退回操作
        //获取转派时间
        Date turnTime = dbHaWorkTurnRecord.getTurnTime();
        //转派时间戳+30秒
        long turnTimeStamp = turnTime.getTime() + TURN_TIMEOUT;
        //获取当前时间时间戳
        long nowTimeStamp = new Date().getTime();
        if (nowTimeStamp > turnTimeStamp && Constants.TURN_STATUS_WAit_ACCEPT.equals(turnStatus)) {
            //如果大于30秒，退回转派件，更新转派
            dbHaWorkTurnRecord.setTurnStatus(Constants.TURN_STATUS_BACK);
            dbHaWorkTurnRecord.setServiceWorkUserId(dbHaWorkTurnRecord.getOldServiceWorkUserId());
            dbHaWorkTurnRecord.setRollbackMemo("超时未处理");
            dbHaWorkTurnRecord.setHandleTime(new Date());
            dbHaWorkTurnRecordMapper.updateByPrimaryKeySelective(dbHaWorkTurnRecord);
            //根据ID查询原服务队列
            DbHaWorkQueue dbHaWorkQueueById = dbHaWorkQueueMapper.selectByPrimaryKey(dbHaWorkTurnRecord.getWorkQueueId());
            //设置办件状态为待服务
            dbHaWorkQueueById.setServiceStatus(Constants.WAIT);
            dbHaWorkQueueById.setDetectsServiceTime(new Date());
            dbHaWorkQueueMapper.updateByPrimaryKeySelective(dbHaWorkQueueById);
        }
        //封装处理时间
        haTurnIsAcceptResponseData.setHandleTime(dbHaWorkTurnRecord.getHandleTime());
        //封装接收状态参数
        haTurnIsAcceptResponseData.setTurnIsVerify(dbHaWorkTurnRecord.getTurnStatus());
        if (Constants.TURN_STATUS_ACCEPT.equals(dbHaWorkTurnRecord.getTurnStatus())) {
            //接收
            //工作人员编号
            haTurnIsAcceptResponseData.setWorkUserId(dbHaWorkTurnRecord.getServiceWorkUserId());
            //工作人员姓名
            haTurnIsAcceptResponseData.setName(dbHaWorkUser.getName());
            //工号
            haTurnIsAcceptResponseData.setWorkNumber(dbHaWorkUser.getWorkNumber());
            //服务位置
            haTurnIsAcceptResponseData.setServicePosition(dbHaWorkUser.getServicePostion());
        } else if (Constants.TURN_STATUS_BACK.equals(dbHaWorkTurnRecord.getTurnStatus())) {
            //退回
            //退回原因
            haTurnIsAcceptResponseData.setRollbackMemo(dbHaWorkTurnRecord.getRollbackMemo());
        }
        return haTurnIsAcceptResponseData;
    }

    public HaWorkTurnRecord getById(Long turnRecordId) throws ServiceException {
        HaWorkTurnRecord haMassesNucleic = new HaWorkTurnRecord();
        DbHaWorkTurnRecord dbHaWorkTurnRecord = dbHaWorkTurnRecordMapper.selectByPrimaryKey(turnRecordId);
        BeanUtils.copyProperties(dbHaWorkTurnRecord, haMassesNucleic);
        return haMassesNucleic;
    }

    public  List<HaWorkTurnRecord> queryTurnByExample(HaWorkTurnRecord haWorkTurnRecord) throws ServiceException {
        DbHaWorkTurnRecordExample example = new DbHaWorkTurnRecordExample();
        DbHaWorkTurnRecordExample.Criteria criteria = example.createCriteria();
        if (null != haWorkTurnRecord) {
            if (haWorkTurnRecord.getWorkQueueId() != null) {
                criteria.andWorkQueueIdEqualTo(haWorkTurnRecord.getWorkQueueId());
            }
            if (StrUtil.isNotEmpty(haWorkTurnRecord.getServiceWorkUserId())) {
                criteria.andServiceWorkUserIdEqualTo(haWorkTurnRecord.getServiceWorkUserId());
            }
            if (StrUtil.isNotEmpty(haWorkTurnRecord.getTurnStatus())) {
                criteria.andTurnStatusEqualTo(haWorkTurnRecord.getTurnStatus());
            }
            example.setOrderByClause("CREATE_DATE desc");
        }
        List<DbHaWorkTurnRecord> dbHaMassesNucleics = dbHaWorkTurnRecordMapper.selectByExample(example);
        List<HaWorkTurnRecord> haMassesNucleicList = dbHaMassesNucleics.stream().map(dbHaMassesNucleic1 -> {
            HaWorkTurnRecord haMassesNucleic1 = new HaWorkTurnRecord();
            com.zfsoft.platform.utils.bean.BeanUtils.copyProperties(dbHaMassesNucleic1, haMassesNucleic1);
            return haMassesNucleic1;
        }).collect(Collectors.toList());
        return haMassesNucleicList;
    }

    /**
     * @description: 获取全部的办事队列
     * @author: kangax
     * @date: 2022-08-17 14:20
     */
    public PageResult<HaWorkTurnRecordVo> queryWorkTrunRecordListWithPage(Long workUserId,Long verifyWorkUserId, String name,Integer pageNum, Integer pageSize,String turnStatus) {

        //分页参数
        if (null == pageNum || pageNum <= 0) {
            pageNum = 0;
        }
        if (null == pageSize || pageSize <= 0) {
            pageSize = 10;
        }
        PageHelper.startPage(pageNum, pageSize);


        //查询
        Page<DbHaWorkTurnRecordVo> dbHaWorkQueueVoList = (Page<DbHaWorkTurnRecordVo>) dbHaWorkTurnRecordMapper.queryWorkTurnRecord(workUserId,verifyWorkUserId,name,turnStatus);
        PageResult<HaWorkTurnRecordVo> pageResult = new PageResult<>(dbHaWorkQueueVoList.getPageNum(), dbHaWorkQueueVoList.getPageSize(), dbHaWorkQueueVoList.getTotal());
        List<HaWorkTurnRecordVo> haWorkQueueVos = dbHaWorkQueueVoList.stream().map(dbHaWorkQueueVo1 -> {
            HaWorkTurnRecordVo haWorkQueueVo1 = new HaWorkTurnRecordVo();
            BeanUtils.copyProperties(dbHaWorkQueueVo1, haWorkQueueVo1);
            return haWorkQueueVo1;
        }).collect(Collectors.toList());
        pageResult.setData(haWorkQueueVos);
        return pageResult;
    }

    /**
     * @description: 获取全部的办事队列
     * @author: kangax
     * @date: 2022-08-17 14:20
     */
    public PageResult<HaWorkTurnRecordVo> queryWorkTrunServiceListWithPage(HaWorkTurnRecordVo turnRecordVo, String beginTime, String endTime, Integer pageNum, Integer pageSize)throws Exception {

        //分页参数
        if (null == pageNum || pageNum <= 0) {
            pageNum = 0;
        }
        if (null == pageSize || pageSize <= 0) {
            pageSize = 10;
        }
        PageHelper.startPage(pageNum, pageSize);
        DbHaWorkTurnRecordVo dbTurnRecordVo = new DbHaWorkTurnRecordVo();
        com.zfsoft.platform.utils.bean.BeanUtils.copyProperties(turnRecordVo, dbTurnRecordVo);
        //来访时间
        dbTurnRecordVo.setBeginADay(StringUtil.isEmpty(beginTime)?null: DateUtil.datetimeFormat.parse(beginTime));
        //来访结束时间
        dbTurnRecordVo.setEndADay(StringUtil.isEmpty(endTime)?null:DateUtil.datetimeFormat.parse(endTime));
        //查询
        Page<DbHaWorkTurnRecordVo> dbHaWorkQueueVoList = (Page<DbHaWorkTurnRecordVo>) dbHaWorkTurnRecordMapper.queryWorkService(dbTurnRecordVo);
        PageResult<HaWorkTurnRecordVo> pageResult = new PageResult<>(dbHaWorkQueueVoList.getPageNum(), dbHaWorkQueueVoList.getPageSize(), dbHaWorkQueueVoList.getTotal());
        List<HaWorkTurnRecordVo> haWorkQueueVos = dbHaWorkQueueVoList.stream().map(dbHaWorkQueueVo1 -> {
            HaWorkTurnRecordVo haWorkQueueVo1 = new HaWorkTurnRecordVo();
            BeanUtils.copyProperties(dbHaWorkQueueVo1, haWorkQueueVo1);
            return haWorkQueueVo1;
        }).collect(Collectors.toList());
        pageResult.setData(haWorkQueueVos);
        return pageResult;
    }
    /**
     * @param turnRecordId 转派记录编号
     * @return HaGetWorkQueueByTurnIdResponseData 根据转派编号获取办事队列详细信息
     * @description: 根据转派编号获取办事队列信息
     * @author: kangax
     * @date: 2022-08-04 10:27
     */
    public HaGetWorkQueueByTurnIdResponseData getMessesByTurn(Long turnRecordId) throws ServiceException {
        //根据转派编号获取办事队列信息响应类
        HaGetWorkQueueByTurnIdResponseData responseData = new HaGetWorkQueueByTurnIdResponseData();
        //根据转派记录编号获取转派记录
        DbHaWorkTurnRecord dbHaWorkTurnRecord = dbHaWorkTurnRecordMapper.selectByPrimaryKey(turnRecordId);
        if (dbHaWorkTurnRecord == null) {
            throw new ServiceException("根据转派记录编号获取转派记录为空");
        }
        DbHaWorkQueue dbHaWorkQueue = dbHaWorkQueueMapper.selectByPrimaryKey(dbHaWorkTurnRecord.getWorkQueueId());
        if (dbHaWorkQueue == null) {
            throw new ServiceException("根据办事队列编号获取办事队列为空");
        }
        DbHaWorkUser dbHaWorkUser = dbHaWorkUserMapper.queryById(dbHaWorkTurnRecord.getOldServiceWorkUserId());
        if (dbHaWorkUser == null) {
            throw new ServiceException("根据帮代办人员编号获取帮代办信息为空");
        }
        //封装响应参数
        responseData.setTurnRecordId(turnRecordId);
        responseData.setQueueId(dbHaWorkTurnRecord.getWorkQueueId());
        responseData.setOldServiceWorkUserId(dbHaWorkTurnRecord.getOldServiceWorkUserId());
        responseData.setOldServiceWorkUserName(dbHaWorkUser.getName());
        responseData.setTurnStatus(dbHaWorkTurnRecord.getTurnStatus());
        responseData.setTurnMemo(dbHaWorkTurnRecord.getTurnMemo());
        responseData.setName(dbHaWorkQueue.getName());
        responseData.setCardNo(dbHaWorkQueue.getCardNo());
        responseData.setPhone(dbHaWorkQueue.getPhone());
        responseData.setCompanyName(dbHaWorkQueue.getCompanyName());
        responseData.setCompanyCode(dbHaWorkQueue.getCompanyCode());
        return responseData;
    }

    /**
     * @description: 查询是否有新的转派等待服务
     * @return: boolean
     * @author: zhaobf
     * @date: 2022-08-03 15:38
     */
    public int isHaveNewMesses(Long workUserId, Date beginTime) throws Exception {
        int count = dbHaWorkTurnRecordMapper.selectCountByVerifyIdAndBeginTime(workUserId, beginTime);
        return count;
    }

    /**
     * @description: 查询用户组的转派等待服务信息
     * @return: List<QlCaseApplay>
     * @author: zhaobf
     * @date: 2022-08-04 15:38
     */
    public List<HaWorkTurnRecord>  queryByBeginTime(Long workUserId, Date beginTime) throws Exception {
        List<DbHaWorkTurnRecord> dbQlCaseApplays = dbHaWorkTurnRecordMapper.queryByBeginTime(workUserId, beginTime);
        List<HaWorkTurnRecord> haUserResourceList = dbQlCaseApplays.stream().map(dbQlCaseApplay -> {
            HaWorkTurnRecord qlCaseApplay = new HaWorkTurnRecord();
            BeanUtils.copyProperties(dbQlCaseApplay, qlCaseApplay);
            return qlCaseApplay;
        }).collect(Collectors.toList());
        return haUserResourceList;
    }

    /**
     * @description: 组长查询当天组内的的未审核转派服务信息
     * @return: List<QlCaseApplay>
     * @author: zhaobf
     * @date: 2022-08-04 15:38
     */
    public List<HaWorkTurnRecord>  queryByVerifyWorkUserIdAndBeginTime(Long verifyWorkUserId, Date beginTime) throws Exception {
        List<DbHaWorkTurnRecord> dbQlCaseApplays = dbHaWorkTurnRecordMapper.queryByVerifyWorkUserIdAndBeginTime(verifyWorkUserId, beginTime);
        List<HaWorkTurnRecord> haUserResourceList = dbQlCaseApplays.stream().map(dbQlCaseApplay -> {
            HaWorkTurnRecord qlCaseApplay = new HaWorkTurnRecord();
            BeanUtils.copyProperties(dbQlCaseApplay, qlCaseApplay);
            return qlCaseApplay;
        }).collect(Collectors.toList());
        return haUserResourceList;
    }

    public int  getTurnCount(Long userId ) throws ServiceException {
        return  dbHaWorkTurnRecordMapper.getTurnCount(userId);
    }
}
