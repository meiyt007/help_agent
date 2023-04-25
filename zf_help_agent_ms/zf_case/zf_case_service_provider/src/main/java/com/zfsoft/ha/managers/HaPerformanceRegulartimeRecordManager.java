package com.zfsoft.ha.managers;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zfsoft.cases.util.DateUtil;
import com.zfsoft.cases.util.StringUtil;
import com.zfsoft.ha.constant.Constants;
import com.zfsoft.ha.data.HaLoginUserInfo;
import com.zfsoft.ha.data.HaPerformanceRegulartimeRecord;
import com.zfsoft.ha.data.HaWorkUser;
import com.zfsoft.ha.data.vo.HaPerformanceAppraisalStatisticVo;
import com.zfsoft.ha.dbaccess.dao.DbHaAkeyReviewRecordsMapper;
import com.zfsoft.ha.dbaccess.dao.DbHaPerformancePhoneRecordMapper;
import com.zfsoft.ha.dbaccess.dao.DbHaPerformanceRegulartimeRecordMapper;
import com.zfsoft.ha.dbaccess.dao.DbHaWorkUserMapper;
import com.zfsoft.ha.dbaccess.data.DbHaAkeyReviewRecords;
import com.zfsoft.ha.dbaccess.data.DbHaPerformanceRegulartimeRecord;
import com.zfsoft.ha.dbaccess.data.DbHaWorkUser;
import com.zfsoft.ha.dbaccess.data.example.DbHaPerformanceRegulartimeRecordExample;
import com.zfsoft.ha.dbaccess.data.vo.DbHaPerformanceAppraisalStatisticVo;
import com.zfsoft.ha.dbaccess.data.vo.DbHaWorkUserVo;
import com.zfsoft.ha.util.HaLoginUserHolder;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.platform.common.security.data.CurrentLoginUser;
import com.zfsoft.platform.common.security.data.CurrentLoginUserHolder;
import com.zfsoft.platform.utils.validate.ParamValid;
import com.zfsoft.single.util.CalculateWorkDaysUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.transaction.annotation.ShardingTransactionType;
import org.apache.shardingsphere.transaction.core.TransactionType;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.zfsoft.ha.constant.Constants.GROUP_DEPUTY_LEADER;
import static com.zfsoft.ha.constant.Constants.GROUP_LEADER;

/**
 * @Description 绩效常规时长统计管理业务实现层
 * @author dingsn
 * @date 2022/10/31  11:21
 */
@Service
@Slf4j
public class HaPerformanceRegulartimeRecordManager {
    @Resource
    private DbHaPerformanceRegulartimeRecordMapper dbHaPerformanceRegulartimeRecordMapper;
    /**
     * 催办发送短信记录
     */
    @Resource
    private DbHaAkeyReviewRecordsMapper dbHaAkeyReviewRecordsMapper;
    /**
     * 用户db层接口
     */
    @Resource
    private DbHaWorkUserMapper dbHaWorkUserMapper;

    @Resource
    private DbHaPerformancePhoneRecordMapper dbHaPerformancePhoneRecordMapper;
    /**
     * @description 新增或修改绩效常规时长统计数据信息
     * @param haPerformanceRegulartimeRecord
     * @author: dingsn
     * @return
     * @throws Exception
     * @Date: 2022/10/31
     */
    @Transactional
    @ParamValid
    @ShardingTransactionType(TransactionType.LOCAL)
    public JSONObject saveOrUpdatePerformanceRegulartimeRecord(HaPerformanceRegulartimeRecord haPerformanceRegulartimeRecord) throws Exception {
        JSONObject result = new JSONObject();
        //获取当前用户登录信息
        HaLoginUserInfo currentHaLoginUserInfo = HaLoginUserHolder.getCurrentHaLoginUserInfo();
        int index = 0;
        //判断是修改还是新增
        if (haPerformanceRegulartimeRecord.getId() != null) {
            //修改
            DbHaPerformanceRegulartimeRecord dbHaPerformanceRegulartimeRecord = dbHaPerformanceRegulartimeRecordMapper.selectByPrimaryKey(haPerformanceRegulartimeRecord.getId());
            BeanUtils.copyProperties(haPerformanceRegulartimeRecord, dbHaPerformanceRegulartimeRecord);
            dbHaPerformanceRegulartimeRecord.setUpdateBy(currentHaLoginUserInfo==null?"系统管理员":currentHaLoginUserInfo.getName()); //获取当前登录用户名称
            dbHaPerformanceRegulartimeRecord.setUpdateDate(new Date()); //获取当前时间
            DbHaPerformanceRegulartimeRecordExample dbHaPerformanceRegulartimeRecordExample = new DbHaPerformanceRegulartimeRecordExample();
            DbHaPerformanceRegulartimeRecordExample.Criteria criteria = dbHaPerformanceRegulartimeRecordExample.createCriteria();
            criteria.andIdEqualTo(haPerformanceRegulartimeRecord.getId());
            index = dbHaPerformanceRegulartimeRecordMapper.updateByExampleSelective(dbHaPerformanceRegulartimeRecord, dbHaPerformanceRegulartimeRecordExample);
        } else {
            //新增
            DbHaPerformanceRegulartimeRecord dbHaPerformanceRegulartimeRecord = new DbHaPerformanceRegulartimeRecord();
            BeanUtils.copyProperties(haPerformanceRegulartimeRecord, dbHaPerformanceRegulartimeRecord);
            dbHaPerformanceRegulartimeRecord.setDeleteStatus(Constants.DELETE_STATUS_NO);
            dbHaPerformanceRegulartimeRecord.setCreateBy(currentHaLoginUserInfo==null?"系统管理员":currentHaLoginUserInfo.getName());
            dbHaPerformanceRegulartimeRecord.setCreateDate(new Date());
            index = dbHaPerformanceRegulartimeRecordMapper.insert(dbHaPerformanceRegulartimeRecord);
        }
        result.put("index", index);
        return result;
    }

    /**
     * 根据常规时长对象查询绩效统计的常规时长记录信息
     * @param haPerformanceRegulartimeRecord
     * @return
     * @throws Exception
     * @author: dingsn
     */
    public HaPerformanceRegulartimeRecord selectPerformanceRegulartimeRecordByHaPerformanceRegulartimeRecord(HaPerformanceRegulartimeRecord haPerformanceRegulartimeRecord) throws Exception {
        log.info("根据常规时长对象查询绩效统计的常规时长记录信息,HaPerformanceRegulartimeRecord={} ", haPerformanceRegulartimeRecord);
        DbHaPerformanceRegulartimeRecord dbHaPerformanceRegulartimeRecord = new DbHaPerformanceRegulartimeRecord();
        BeanUtils.copyProperties(haPerformanceRegulartimeRecord, dbHaPerformanceRegulartimeRecord);
        DbHaPerformanceRegulartimeRecord d = dbHaPerformanceRegulartimeRecordMapper.selectPerformanceRegulartimeRecordByHaPerformanceRegulartimeRecord(dbHaPerformanceRegulartimeRecord);
        HaPerformanceRegulartimeRecord record = new HaPerformanceRegulartimeRecord();
        BeanUtils.copyProperties(d, record);
        return record;
    }

    public PageResult<HaPerformanceAppraisalStatisticVo> queryHaPerformanceAppraisalStatisticVoRecordPageResult(HaPerformanceAppraisalStatisticVo haPerformanceAppraisalStatisticVo, String beginTime, String endTime, Integer pageNum, Integer pageSize) throws Exception {
        //获取当前用户登录信息
//        CurrentLoginUser loginUser = CurrentLoginUserHolder.getCurrentLoginUser();
        //分页参数
        if (null == pageNum || pageNum <= 0) {
            pageNum = 0;
        }
        if (null == pageSize || pageSize <= 0) {
            pageSize = 10;
        }
        PageHelper.startPage(pageNum, pageSize);
        DbHaPerformanceAppraisalStatisticVo dbHaPerformanceAppraisalStatisticVo = new DbHaPerformanceAppraisalStatisticVo();
        BeanUtils.copyProperties(haPerformanceAppraisalStatisticVo, dbHaPerformanceAppraisalStatisticVo);
        if (beginTime.contains("(中国标准时间)")) {
            //开始查询时间
            dbHaPerformanceAppraisalStatisticVo.setBeginTime(StringUtil.isEmpty(beginTime) ? null : DateUtil.getFormatByStringTime(DateUtil.date_sdf.format(DateUtil.sdgl2.parse(beginTime)) + " 00:00:00"));
        } else {
            //开始查询时间
            dbHaPerformanceAppraisalStatisticVo.setBeginTime(StringUtil.isEmpty(beginTime) ? null : DateUtil.date_sdf.parse(beginTime + ""));
        }
        if (endTime.contains("(中国标准时间)")) {
            //结束查询时间
            dbHaPerformanceAppraisalStatisticVo.setEndTime(StringUtil.isEmpty(endTime) ? null : DateUtil.getFormatByStringTime(DateUtil.date_sdf.format(DateUtil.sdgl2.parse(endTime)) + " 23:59:59"));
        } else {
            //结束查询时间
            dbHaPerformanceAppraisalStatisticVo.setEndTime(StringUtil.isEmpty(endTime) ? null : DateUtil.sdf2.parse(String.valueOf(DateUtil.date_sdf.parse(endTime)).replace("00:00:00","23:59:59")));//.replace(" 00:00:00"," 23:59:59")
        }
        dbHaPerformanceAppraisalStatisticVo.setAuditStatus(Constants.ONE);
        //1查询帮办服务绩效，每个帮办用户返回一条信息
        Page<DbHaPerformanceAppraisalStatisticVo> dbHaPerformanceAppraisalStatisticVoList = (Page<DbHaPerformanceAppraisalStatisticVo>) dbHaPerformanceRegulartimeRecordMapper.queryDbHaPerformanceAppraisalStatisticVoPageList(dbHaPerformanceAppraisalStatisticVo);
        PageResult<HaPerformanceAppraisalStatisticVo> pageResult = new PageResult<>(dbHaPerformanceAppraisalStatisticVoList.getPageNum(), dbHaPerformanceAppraisalStatisticVoList.getPageSize(), dbHaPerformanceAppraisalStatisticVoList.getTotal());
        List<HaPerformanceAppraisalStatisticVo> dbHaPerformanceAppraisalStatisticVos = dbHaPerformanceAppraisalStatisticVoList.stream().map(dbHaPerformanceAppraisalStatisticVo1 -> {
            HaPerformanceAppraisalStatisticVo resultVo = new HaPerformanceAppraisalStatisticVo();
            BeanUtils.copyProperties(dbHaPerformanceAppraisalStatisticVo1, resultVo);
            //1.1查询帮办绩效加分，封装到实体类种
            DbHaPerformanceAppraisalStatisticVo vo = new DbHaPerformanceAppraisalStatisticVo();
            vo.setWorkUserOid(dbHaPerformanceAppraisalStatisticVo1.getWorkUserOid());
            try {
                if (beginTime.contains("(中国标准时间)")) {
                    //开始查询时间
                    vo.setBeginTime(StringUtil.isEmpty(beginTime) ? null : DateUtil.getFormatByStringTime(DateUtil.date_sdf.format(DateUtil.sdgl2.parse(beginTime)) + " 00:00:00"));

                } else {
                    //开始查询时间
                    vo.setBeginTime(StringUtil.isEmpty(beginTime)?null: DateUtil.date_sdf.parse(beginTime));
                }
                if (endTime.contains("(中国标准时间)")) {
                    //结束查询时间
                    vo.setEndTime(StringUtil.isEmpty(endTime) ? null : DateUtil.getFormatByStringTime(DateUtil.date_sdf.format(DateUtil.sdgl2.parse(endTime)) + " 23:59:59"));

                } else {
                    //结束查询时间
                    vo.setEndTime(StringUtil.isEmpty(endTime)?null:DateUtil.sdf2.parse(String.valueOf(DateUtil.date_sdf.parse(endTime)).replace(" 00:00:00"," 23:59:59")));
                }
                DbHaPerformanceAppraisalStatisticVo dbVo2 = dbHaPerformanceRegulartimeRecordMapper.queryDbHaPerformanceAppraisalStatisticVoByWorkUserOid(vo);
                Integer jiduSum = 0;
                int plusDuration = 0;
                if (dbVo2 != null) {
                    plusDuration = dbVo2.getPlusDuration() != null ? dbVo2.getPlusDuration() : 0;
                    resultVo.setPlusDuration(plusDuration);
                    resultVo.setPlusProjectOid(dbVo2.getPlusProjectOid());
                    resultVo.setPlusProjectName(dbVo2.getPlusProjectName());

                }else{
                    resultVo.setPlusDuration(plusDuration);
                }
                if(resultVo.getServicePeopleNum() == null){
                    resultVo.setServicePeopleNum("0");
                }
                if(resultVo.getManageFactor() == null){
                    resultVo.setManageFactor("0");
                }
                //1.2 查询帮办电话时长
                int phoneDuration = 0;
                DbHaWorkUser dbHaWorkUser = dbHaWorkUserMapper.queryById(vo.getWorkUserOid());
                if(StringUtil.isNotEmpty(dbHaWorkUser.getCloudServiceNumber())){
                    DbHaPerformanceAppraisalStatisticVo phoneVo = dbHaPerformancePhoneRecordMapper.getPhoneDuration(dbHaWorkUser.getCloudServiceNumber(),beginTime,endTime);
                    //秒转化为分钟，向上取整，5分1秒转为6分
                    if(phoneVo==null){
                        resultVo.setPhoneDuration(phoneDuration);
                    }else{
                        phoneDuration = phoneVo.getPhoneDuration() / 60 + (phoneVo.getPhoneDuration() % 60 != 0 ? 1 : 0);

                        resultVo.setPhoneDuration(phoneDuration);
                    }

                }else{
                    resultVo.setPhoneDuration(phoneDuration);
                }
                //1.3 封装其他绩效参数
                int serviceDuration = resultVo.getServiceDuration() != null ? resultVo.getServiceDuration() : 0;
                resultVo.setServiceDuration(serviceDuration);
                //加分时长+服务时长
                jiduSum = plusDuration + serviceDuration + phoneDuration;
                resultVo.setJiduSum(jiduSum);//季度工作量小计
                //季度累计积分
                double leijiSum = jiduSum * (resultVo.getManageFactor()!=null?Double.parseDouble(resultVo.getManageFactor().trim()):1);
                resultVo.setLeijiSum(new BigDecimal(leijiSum).setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue());
                //工作日天数
                try {
                    Date bDate;//开始时间
                    Date eDate;//结束时间
                    if (beginTime.contains("(中国标准时间)")) {
                        bDate = DateUtil.sdgl2.parse(beginTime);
                    } else {
                        bDate = DateUtil.date_sdf.parse(beginTime);
                    }
                    if (endTime.contains("(中国标准时间)")) {
                        eDate = DateUtil.sdgl2.parse(endTime);
                    } else {
                        eDate = DateUtil.date_sdf.parse(endTime);
                    }
                    int dayCount = CalculateWorkDaysUtil.addDates(DateUtil.date_sdf.format(bDate), DateUtil.date_sdf.format(eDate));
                    int fdWorkCount = CalculateWorkDaysUtil.FDGZR(DateUtil.date_sdf.format(bDate),dayCount);
                    resultVo.setFdWorkCount(fdWorkCount);
                    //日工作量
                    double dayWorkSum = leijiSum/fdWorkCount;
                    resultVo.setDayWorkSum(new BigDecimal(dayWorkSum).setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue());
                    //工作效能百分比
                    double gzxnPercent = dayWorkSum/(5.5*60);
                    resultVo.setGzxnPercent(new BigDecimal(gzxnPercent).setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue());
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } catch (ParseException e) {
                e.printStackTrace();
            }
            return resultVo;
        }).collect(Collectors.toList());
        pageResult.setData(dbHaPerformanceAppraisalStatisticVos);

        return pageResult;
    }

    public PageResult<HaPerformanceAppraisalStatisticVo> queryHaPerformanceAppraisalStatisticVoRecordPageResultByPost(HaPerformanceAppraisalStatisticVo haPerformanceAppraisalStatisticVo, String beginTime, String endTime, Integer pageNum, Integer pageSize) throws Exception {
        //获取当前用户登录信息
        HaLoginUserInfo currentHaLoginUserInfo = HaLoginUserHolder.getCurrentHaLoginUserInfo();
        //分页参数
        if (null == pageNum || pageNum <= 0) {
            pageNum = 0;
        }
        if (null == pageSize || pageSize <= 0) {
            pageSize = 10;
        }

        DbHaPerformanceAppraisalStatisticVo dbHaPerformanceAppraisalStatisticVo = new DbHaPerformanceAppraisalStatisticVo();
        BeanUtils.copyProperties(haPerformanceAppraisalStatisticVo, dbHaPerformanceAppraisalStatisticVo);
        if (beginTime.contains("(中国标准时间)")) {
            //开始查询时间
            dbHaPerformanceAppraisalStatisticVo.setBeginTime(StringUtil.isEmpty(beginTime) ? null : DateUtil.getFormatByStringTime(DateUtil.date_sdf.format(DateUtil.sdgl2.parse(beginTime)) + " 00:00:00"));
        } else {
            //开始查询时间
            dbHaPerformanceAppraisalStatisticVo.setBeginTime(StringUtil.isEmpty(beginTime) ? null : DateUtil.date_sdf.parse(beginTime + ""));
        }
        if (endTime.contains("(中国标准时间)")) {
            //结束查询时间
            dbHaPerformanceAppraisalStatisticVo.setEndTime(StringUtil.isEmpty(endTime) ? null : DateUtil.getFormatByStringTime(DateUtil.date_sdf.format(DateUtil.sdgl2.parse(endTime)) + " 23:59:59"));
        } else {
            //结束查询时间
            dbHaPerformanceAppraisalStatisticVo.setEndTime(StringUtil.isEmpty(endTime) ? null : DateUtil.sdf2.parse(String.valueOf(DateUtil.date_sdf.parse(endTime)).replace("00:00:00","23:59:59")));//.replace(" 00:00:00"," 23:59:59")
        }
        dbHaPerformanceAppraisalStatisticVo.setAuditStatus(Constants.ONE);


        List<String> ids = new ArrayList<String>();
        if(GROUP_LEADER.equals(currentHaLoginUserInfo.getGroupPost())||
                GROUP_DEPUTY_LEADER.equals(currentHaLoginUserInfo.getGroupPost())){
            List<DbHaWorkUserVo> DbHaWorkUserList = dbHaWorkUserMapper.queryNameAndGroup("",currentHaLoginUserInfo.getGroupId());
            ids.addAll(DbHaWorkUserList.stream().map(DbHaWorkUserVo::getId).map(String::valueOf).collect(Collectors.toList()));

        }else{
            ids.add(currentHaLoginUserInfo.getId().toString());
        }
        dbHaPerformanceAppraisalStatisticVo.setIds(ids);
        dbHaPerformanceAppraisalStatisticVo.setGroupSplitId(currentHaLoginUserInfo.getGroupSplitId());
        //查询
        PageHelper.startPage(pageNum, pageSize);
        Page<DbHaPerformanceAppraisalStatisticVo> dbHaPerformanceAppraisalStatisticVoList = (Page<DbHaPerformanceAppraisalStatisticVo>)  dbHaPerformanceRegulartimeRecordMapper.queryDbHaPerformanceAppraisalStatisticVoPageList2(dbHaPerformanceAppraisalStatisticVo);
        PageResult<HaPerformanceAppraisalStatisticVo> pageResult = new PageResult<>(dbHaPerformanceAppraisalStatisticVoList.getPageNum(), dbHaPerformanceAppraisalStatisticVoList.getPageSize(), dbHaPerformanceAppraisalStatisticVoList.getTotal());
        List<HaPerformanceAppraisalStatisticVo> dbHaPerformanceAppraisalStatisticVos = dbHaPerformanceAppraisalStatisticVoList.stream().map(dbHaPerformanceAppraisalStatisticVo1 -> {
            HaPerformanceAppraisalStatisticVo resultVo = new HaPerformanceAppraisalStatisticVo();
            BeanUtils.copyProperties(dbHaPerformanceAppraisalStatisticVo1, resultVo);
            DbHaPerformanceAppraisalStatisticVo vo = new DbHaPerformanceAppraisalStatisticVo();
            vo.setWorkUserOid(dbHaPerformanceAppraisalStatisticVo1.getWorkUserOid());
            try {
                if (beginTime.contains("(中国标准时间)")) {
                    //开始查询时间
                    vo.setBeginTime(StringUtil.isEmpty(beginTime) ? null : DateUtil.getFormatByStringTime(DateUtil.date_sdf.format(DateUtil.sdgl2.parse(beginTime)) + " 00:00:00"));

                } else {
                    //开始查询时间
                    vo.setBeginTime(StringUtil.isEmpty(beginTime)?null: DateUtil.date_sdf.parse(beginTime));
                }
                if (endTime.contains("(中国标准时间)")) {
                    //结束查询时间
                    vo.setEndTime(StringUtil.isEmpty(endTime) ? null : DateUtil.getFormatByStringTime(DateUtil.date_sdf.format(DateUtil.sdgl2.parse(endTime)) + " 23:59:59"));

                } else {
                    //结束查询时间
                    vo.setEndTime(StringUtil.isEmpty(endTime)?null:DateUtil.sdf2.parse(String.valueOf(DateUtil.date_sdf.parse(endTime)).replace(" 00:00:00"," 23:59:59")));
                }
                DbHaPerformanceAppraisalStatisticVo dbVo2 = dbHaPerformanceRegulartimeRecordMapper.queryDbHaPerformanceAppraisalStatisticVoByWorkUserOid(vo);
                Integer jiduSum = 0;
                int plusDuration = 0;
                if (dbVo2 != null) {
                    plusDuration = dbVo2.getPlusDuration() != null ? dbVo2.getPlusDuration() : 0;
                    resultVo.setPlusDuration(plusDuration);
                    resultVo.setPlusProjectOid(dbVo2.getPlusProjectOid());
                    resultVo.setPlusProjectName(dbVo2.getPlusProjectName());

                }else{
                    resultVo.setPlusDuration(plusDuration);
                }
                if(resultVo.getServicePeopleNum() == null){
                    resultVo.setServicePeopleNum("0");
                }
                if(resultVo.getManageFactor() == null){
                    resultVo.setManageFactor("0");
                }
                //1.2 查询帮办电话时长
                int phoneDuration = 0;
                DbHaWorkUser dbHaWorkUser = dbHaWorkUserMapper.queryById(vo.getWorkUserOid());
                if(StringUtil.isNotEmpty(dbHaWorkUser.getCloudServiceNumber())){
                    DbHaPerformanceAppraisalStatisticVo phoneVo = dbHaPerformancePhoneRecordMapper.getPhoneDuration(dbHaWorkUser.getCloudServiceNumber(),beginTime,endTime);
                    //秒转化为分钟，向上取整，5分1秒转为6分
                    phoneDuration = phoneVo.getPhoneDuration() / 60 + (phoneVo.getPhoneDuration() % 60 != 0 ? 1 : 0);

                    resultVo.setPhoneDuration(phoneDuration);
                }else{
                    resultVo.setPhoneDuration(phoneDuration);
                }
                //1.3 封装其他绩效参数
                int serviceDuration = resultVo.getServiceDuration() != null ? resultVo.getServiceDuration() : 0;
                resultVo.setServiceDuration(serviceDuration);
                //加分时长+服务时长
                jiduSum = plusDuration + serviceDuration + phoneDuration;
                resultVo.setJiduSum(jiduSum);//季度工作量小计
                //季度累计积分
                double leijiSum = jiduSum * (resultVo.getManageFactor()!=null?Double.parseDouble(resultVo.getManageFactor().trim()):1);
                resultVo.setLeijiSum(new BigDecimal(leijiSum).setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue());
                //工作日天数
                try {
                    Date bDate;//开始时间
                    Date eDate;//结束时间
                    if (beginTime.contains("(中国标准时间)")) {
                        bDate = DateUtil.sdgl2.parse(beginTime);
                    } else {
                        bDate = DateUtil.date_sdf.parse(beginTime);
                    }
                    if (endTime.contains("(中国标准时间)")) {
                        eDate = DateUtil.sdgl2.parse(endTime);
                    } else {
                        eDate = DateUtil.date_sdf.parse(endTime);
                    }
                    int dayCount = CalculateWorkDaysUtil.addDates(DateUtil.date_sdf.format(bDate), DateUtil.date_sdf.format(eDate));
                    int fdWorkCount = CalculateWorkDaysUtil.FDGZR(DateUtil.date_sdf.format(bDate),dayCount);
                    resultVo.setFdWorkCount(fdWorkCount);
                    //日工作量
                    double dayWorkSum = leijiSum/fdWorkCount;
                    resultVo.setDayWorkSum(new BigDecimal(dayWorkSum).setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue());
                    //工作效能百分比
                    double gzxnPercent = dayWorkSum/(5.5*60);
                    resultVo.setGzxnPercent(new BigDecimal(gzxnPercent).setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue());
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } catch (ParseException e) {
                e.printStackTrace();
            }
            return resultVo;
        }).sorted(Comparator.comparing(HaPerformanceAppraisalStatisticVo::getLeijiSum).reversed()).collect(Collectors.toList());
        pageResult.setData(dbHaPerformanceAppraisalStatisticVos);

        return pageResult;
    }

    public PageResult<HaPerformanceRegulartimeRecord> queryHaPerRegRecordPage(HaPerformanceRegulartimeRecord haPerforRegRec, String beginTime, String endTime, Integer pageNum, Integer pageSize) throws Exception {
        //获取当前用户登录信息
//        CurrentLoginUser loginUser = CurrentLoginUserHolder.getCurrentLoginUser();
        //分页参数
        if (null == pageNum || pageNum <= 0) {
            pageNum = 0;
        }
        if (null == pageSize || pageSize <= 0) {
            pageSize = 10;
        }
        PageHelper.startPage(pageNum, pageSize);
        DbHaPerformanceRegulartimeRecordExample example = new DbHaPerformanceRegulartimeRecordExample();
        DbHaPerformanceRegulartimeRecordExample.Criteria criteria = example.createCriteria();
        if (beginTime!=null) {
            criteria.andCreateDateGreaterThanOrEqualTo(DateUtil.getTodayStartTime(DateUtil.datetimeFormat.parse(beginTime)));
        }
        if (endTime!=null) {
            criteria.andCreateDateLessThanOrEqualTo(DateUtil.getTodayEndTime(DateUtil.datetimeFormat.parse(endTime)));
        }
        if (haPerforRegRec != null) {
            if (haPerforRegRec.getWorkUserOid()!= null) {
                criteria.andWorkUserOidEqualTo(haPerforRegRec.getWorkUserOid());
            }
        }
        example.setOrderByClause("CREATE_DATE desc");

        //查询
        Page<DbHaPerformanceRegulartimeRecord> dbHaPerRegRecordList = (Page<DbHaPerformanceRegulartimeRecord>) dbHaPerformanceRegulartimeRecordMapper.selectByExample(example);
        PageResult<HaPerformanceRegulartimeRecord> pageResult = new PageResult<>(dbHaPerRegRecordList.getPageNum(), dbHaPerRegRecordList.getPageSize(), dbHaPerRegRecordList.getTotal());
        List<HaPerformanceRegulartimeRecord> haPerformanceRegulartimeRecords = com.zfsoft.single.util.BeanUtils.copyListProperties(dbHaPerRegRecordList, HaPerformanceRegulartimeRecord::new);
        pageResult.setData(haPerformanceRegulartimeRecords);

        return pageResult;
    }

    /**
     * 根据时间区间去查待审核状态下的组长的信息（包括组长姓名、组长手机号、所属分组、几条没审）
     * @param beginTime
     * @param endTime
     * @param pageNum
     * @param pageSize
     * @author: dingsn
     * @throws Exception
     */
    public PageResult<HaPerformanceAppraisalStatisticVo> queryGroupLeaderPageListByAuditStatus(String beginTime, String endTime, Integer pageNum, Integer pageSize)  throws Exception {
        //分页参数
        if (null == pageNum || pageNum <= 0) {
            pageNum = 0;
        }
        if (null == pageSize || pageSize <= 0) {
            pageSize = 10;
        }
        PageHelper.startPage(pageNum, pageSize);
        DbHaPerformanceAppraisalStatisticVo dbHaPerformanceAppraisalStatisticVo = new DbHaPerformanceAppraisalStatisticVo();
        //开始查询时间
        dbHaPerformanceAppraisalStatisticVo.setBeginTime(StringUtil.isEmpty(beginTime)?null: DateUtil.datetimeFormat.parse(beginTime));
        //结束查询时间
        dbHaPerformanceAppraisalStatisticVo.setEndTime(StringUtil.isEmpty(endTime)?null:DateUtil.datetimeFormat.parse(endTime.replace(" 00:00:00"," 23:59:59")));
        //查询
        Page<DbHaPerformanceAppraisalStatisticVo> dbHaPerformanceAppraisalStatisticVoList = (Page<DbHaPerformanceAppraisalStatisticVo>) dbHaPerformanceRegulartimeRecordMapper.queryGroupLeaderPageListByAuditStatus(dbHaPerformanceAppraisalStatisticVo);
        PageResult<HaPerformanceAppraisalStatisticVo> pageResult = new PageResult<>(dbHaPerformanceAppraisalStatisticVoList.getPageNum(), dbHaPerformanceAppraisalStatisticVoList.getPageSize(), dbHaPerformanceAppraisalStatisticVoList.getTotal());
        List<HaPerformanceAppraisalStatisticVo> dbHaPerformanceAppraisalStatisticVos = dbHaPerformanceAppraisalStatisticVoList.stream().map(dbHaPerformanceAppraisalStatisticVo1 -> {
            HaPerformanceAppraisalStatisticVo haPerformanceAppraisalStatisticVo1 = new HaPerformanceAppraisalStatisticVo();
            BeanUtils.copyProperties(dbHaPerformanceAppraisalStatisticVo1, haPerformanceAppraisalStatisticVo1);
            return haPerformanceAppraisalStatisticVo1;
        }).collect(Collectors.toList());
        pageResult.setData(dbHaPerformanceAppraisalStatisticVos);

        return pageResult;
    }

    public Integer akeyReviewByGroupLeaderId(Long id) throws Exception {
        DbHaWorkUser DbHaWorkUser = dbHaWorkUserMapper.queryById(id);
        HaWorkUser haWorkUser = new HaWorkUser();
        if (DbHaWorkUser != null) {
            BeanUtils.copyProperties(DbHaWorkUser, haWorkUser);
        }
        DbHaPerformanceAppraisalStatisticVo dbHaPerformanceAppraisalStatisticVo = new DbHaPerformanceAppraisalStatisticVo();
        dbHaPerformanceAppraisalStatisticVo.setGroupLeaderId(haWorkUser.getId());
        List<DbHaPerformanceAppraisalStatisticVo> dbHaPerformanceAppraisalStatisticVos = dbHaPerformanceRegulartimeRecordMapper.queryGroupLeaderPageListByAuditStatus(dbHaPerformanceAppraisalStatisticVo);
        if (dbHaPerformanceAppraisalStatisticVos == null) {
            return 0;
        }
        //发送短信开始
        StringBuffer message = new StringBuffer("尊敬的" + haWorkUser.getName());
        message.append("您好，您有");
        message.append(dbHaPerformanceAppraisalStatisticVos.get(0).getNoAuditStatusSum() == null ? 0 : dbHaPerformanceAppraisalStatisticVos.get(0).getNoAuditStatusSum());
        message.append( "条待审核的组员加分时长记录，请及时登录帮办系统做审核处理");

        //发送短信结束
        CurrentLoginUser loginUser = CurrentLoginUserHolder.getCurrentLoginUser();
        DbHaAkeyReviewRecords dbHaAkeyReviewRecords = new DbHaAkeyReviewRecords();
        dbHaAkeyReviewRecords.setGroupLeaderId(haWorkUser.getId());
        dbHaAkeyReviewRecords.setGroupLeaderName(haWorkUser.getName());
        dbHaAkeyReviewRecords.setPhone(haWorkUser.getPhone());
        dbHaAkeyReviewRecords.setMessage(message.toString());
        dbHaAkeyReviewRecords.setAkeyReviewDate(new Date());
        dbHaAkeyReviewRecords.setDeleteStatus(Constants.DELETE_STATUS_NO);
        dbHaAkeyReviewRecords.setCreateBy(loginUser.getUserName());
        dbHaAkeyReviewRecords.setCreateDate(new Date());
        int index = dbHaAkeyReviewRecordsMapper.insert(dbHaAkeyReviewRecords);
        return index;
    }

    public void batchAkeyReviewByGroupLeaderIds(List<Long> ids) throws Exception {
        ids.forEach(id -> {
            try {
                akeyReviewByGroupLeaderId(id);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
