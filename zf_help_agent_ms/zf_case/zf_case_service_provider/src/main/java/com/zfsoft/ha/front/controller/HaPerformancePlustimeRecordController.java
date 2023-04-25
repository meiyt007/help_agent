package com.zfsoft.ha.front.controller;

import com.alibaba.fastjson.JSONObject;
import com.zfsoft.ha.constant.Constants;
import com.zfsoft.ha.data.*;
import com.zfsoft.ha.data.vo.HaPerformanceAppraisalStatisticVo;
import com.zfsoft.ha.data.vo.HaPerformancePlustimeRecordVo;
import com.zfsoft.ha.front.HaPerformancePlusTimeRecordService;
import com.zfsoft.ha.managers.*;
import com.zfsoft.ha.util.HaLoginUserHolder;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.data.PageResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 帮代办人员绩效加分时长手动填报管理相关接口
 *
 * @author dingsn
 * @date 2022/11/01 15:30
 */
@Slf4j
@RestController

public class HaPerformancePlustimeRecordController implements HaPerformancePlusTimeRecordService {
    /**
     * redis
     */
    @Autowired
    private RedisTemplate redisTemplate;

    @Resource
    private HaPerformancePlustimeRecordManager haPerformancePlustimeRecordManager;

    @Resource
    private HaPerformancePhoneRecordManager haPerformancePhoneRecordManager;

    /**
     * 加分项目维护管理实现业务层
     */
    @Resource
    private HaPlusProjectManager haPlusProjectManager;

    /**
     * 帮办人员用户表实现manager
     */
    @Resource
    private HaWorkUserServiceManager haWorkUserServiceManager;
    /**
     * 帮代办人员分组表实现层
     */
    @Resource
    private HaWorkGroupServiceManager haWorkGroupServiceManager;


    /**
     * 绩效常规时长统计管理业务实现层
     */
    @Resource
    private HaPerformanceRegulartimeRecordManager haPerformanceRegulartimeRecordManager;

    @Override
    public ApiResultSet<PageResult> queryHaPerformancePlustimeRecordPageResult(HaPerformancePlustimeRecordVo haPerformancePlustimeRecordVo,String plusProjectOid, String auditStatus, Integer pageNum, Integer pageSize) throws Exception {
        log.info("进入获取全部绩效加分时长记录统计的数据，参数plusProjectOid=  {},auditStatus={}", plusProjectOid, auditStatus);
        PageResult<HaPerformancePlustimeRecordVo> resultSet = haPerformancePlustimeRecordManager.queryHaPerformancePlustimeRecordPageResult(haPerformancePlustimeRecordVo, pageNum, pageSize);
        log.info("分页查询绩效加分时长记录列表数据总数={}", resultSet.getTotal());
        return ApiResultSet.ok("请求成功=={}", resultSet);
    }

    @Override
    public ApiResultSet saveHaPerformancePlustimeRecord(HaPerformancePlustimeRecord haPerformancePlustimeRecord) throws Exception {
        log.info("新增或者修改绩效加分时长记录信息=={}", haPerformancePlustimeRecord);
        ApiResultSet apiResultSet = new ApiResultSet<>();
        if (haPerformancePlustimeRecord.getPlusProjectOid() == null || "".equals(haPerformancePlustimeRecord.getPlusProjectOid())) {
            apiResultSet.setCode(ApiResultSet.PARAM_VALID_ERROR);
            apiResultSet.setMessage("保存时所属加分项目不能为空！");
            apiResultSet.setTime(String.valueOf(new Date()));
            return apiResultSet;
        }
        if (haPerformancePlustimeRecord.getGroupLeaderId() == null || "".equals(haPerformancePlustimeRecord.getGroupLeaderId())) {
            apiResultSet.setCode(ApiResultSet.PARAM_VALID_ERROR);
            apiResultSet.setMessage("保存时选择的审核组长不能为空！");
            apiResultSet.setTime(String.valueOf(new Date()));
            return apiResultSet;
        }
        JSONObject result = haPerformancePlustimeRecordManager.saveHaPerformancePlustimeRecord(haPerformancePlustimeRecord);
        int index = (int) result.get("index");
        log.debug("index结果集，index:{}", index);
        if (index == 1) {
            //说明有新增或修改
            apiResultSet.setCode(ApiResultSet.SUCCESS);
            apiResultSet.setMessage("新增或修改成功");
            apiResultSet.setTime(String.valueOf(new Date()));
            log.info("新增或修改绩效加分时长信息成功");
            //发短信给审核的组长，让他去审核
            Long groupLeaderId = haPerformancePlustimeRecord.getGroupLeaderId();
            HaWorkUser groupLeaderInfo = haWorkUserServiceManager.getHelpWorkUserById(groupLeaderId);
            String phone = groupLeaderInfo.getPhone();//获取组长手机号
            //掉用短信接口发送短信


        } else {
            apiResultSet.setCode(ApiResultSet.UNKNOWN_ERROR);
            apiResultSet.setMessage("新增或修改失败！");
            log.error("新增或修改绩效加分时长信息失败");
        }
        return apiResultSet;
    }

    @Override
    public ApiResultSet getHaPerformancePlustimeRecordById(Long id) throws Exception {
        log.info("根据id查询绩效加分时长记录,id={}", id);
        HaPerformancePlustimeRecord haPerformancePlustimeRecord = haPerformancePlustimeRecordManager.selectHaPerformancePlustimeRecordByid(id);
        return ApiResultSet.ok("绩效加分时长记录接口查询调用成功", haPerformancePlustimeRecord);
    }

    @Override
    public ApiResultSet deleteHaPerformancePlustimeRecordById(Long id) throws Exception {
        log.info("根据id删除绩效加分时长记录信息, id={}", id);
        ApiResultSet apiResultSet = new ApiResultSet();
        Integer index = haPerformancePlustimeRecordManager.deleteHaPerformancePlustimeRecordById(id);
        if (1 == index) {
            apiResultSet.setCode(ApiResultSet.SUCCESS);
            apiResultSet.setMessage("绩效加分时长记录信息已成功删除！");
            apiResultSet.setTime(String.valueOf(new Date()));
            return apiResultSet;
        } else {
            apiResultSet.setMessage("绩效加分时长记录信息删除失败");
            apiResultSet.setCode(ApiResultSet.UNKNOWN_ERROR);
            return apiResultSet;
        }
    }

    @Override
    public ApiResultSet<List<HaWorkUser>> queryGroupLeaderInfoList() throws Exception {
        log.info("查询当前登录的帮办人员所属组长集合");
        HaLoginUserInfo currentHaLoginUserInfo = HaLoginUserHolder.getCurrentHaLoginUserInfo();
        Long groupId = currentHaLoginUserInfo.getGroupId();
        HaWorkUser haWorkUser = new HaWorkUser();
        haWorkUser.setGroupId(groupId);
        haWorkUser.setDeleteStatus(Constants.DELETE_STATUS_NO);
        haWorkUser.setGroupPost("1");
        List<HaWorkUser> list = haWorkUserServiceManager.queryAllWorkUserByHaWorkUser(haWorkUser);
        return ApiResultSet.ok("查询当前登录的帮办人员所属组长集合成功", list);
    }

    /**
     * @description: 根据id查询HaPlusProject对象集合
     * @return
     */
    @Override
    public ApiResultSet<List<HaPlusProject>> queryHaPlusProjectList() throws Exception {
        log.info("前端查询HaPlusProject对象集合");
        List<HaPlusProject> haPlusProjectList = haPlusProjectManager.queryPlusProjectList();
        return ApiResultSet.ok("前端查询加分项信息对象列表成功", haPlusProjectList);
    }

    @Override
    public ApiResultSet groupLeaderAuditHaPerformancePlustimeRecord(HaPerformancePlustimeRecord haPerformancePlustimeRecord) throws Exception {
        log.info("组长根据选择的加分时长记录去修改该条记录审核状况=={}", haPerformancePlustimeRecord);
        ApiResultSet apiResultSet = new ApiResultSet<>();
        if (haPerformancePlustimeRecord.getId() == null || "".equals(haPerformancePlustimeRecord.getId())) {
            apiResultSet.setCode(ApiResultSet.PARAM_VALID_ERROR);
            apiResultSet.setMessage("加分时长记录id不能为空！");
            apiResultSet.setTime(String.valueOf(new Date()));
            return apiResultSet;
        }
        if ("0".equals(haPerformancePlustimeRecord.getAuditStatus())) {//只有不是待审核时才可以操作
            apiResultSet.setCode(ApiResultSet.PARAM_VALID_ERROR);
            apiResultSet.setMessage("加分审核状态不能是待审核");
            apiResultSet.setTime(String.valueOf(new Date()));
        }
        JSONObject result = haPerformancePlustimeRecordManager.saveHaPerformancePlustimeRecord(haPerformancePlustimeRecord);
        int index = (int) result.get("index");
        log.debug("index结果集，index:{}", index);
        if (index == 1) {
            //说明有新增或修改
            apiResultSet.setCode(ApiResultSet.SUCCESS);
            apiResultSet.setMessage("组长根据选择的加分时长记录去修改该条记录审核状况成功");
            apiResultSet.setTime(String.valueOf(new Date()));
            log.info("组长根据选择的加分时长记录去修改该条记录审核状况信息成功");
        } else {
            apiResultSet.setCode(ApiResultSet.UNKNOWN_ERROR);
            apiResultSet.setMessage("组长根据选择的加分时长记录去修改该条记录审核状况失败！");
            log.error("组长根据选择的加分时长记录去修改该条记录审核状况信息失败");
        }
        return apiResultSet;
    }

    @Override
    public ApiResultSet<PageResult> queryHaPerformanceAppraisalStatisticVoRecordPageResult(HaPerformanceAppraisalStatisticVo haPerformanceAppraisalStatisticVo, String beginTime, String endTime, Integer pageNum, Integer pageSize) throws Exception {
        log.info("进入获取全部帮办人绩效时长记录统计的数据，参数haPerformanceAppraisalStatisticVo:  {},选择开始时间:{}, 选择结束时间：{}", haPerformanceAppraisalStatisticVo, beginTime, endTime);
        PageResult<HaPerformanceAppraisalStatisticVo> resultSet = haPerformanceRegulartimeRecordManager.queryHaPerformanceAppraisalStatisticVoRecordPageResultByPost(haPerformanceAppraisalStatisticVo, beginTime, endTime, pageNum, pageSize);
        log.info("分页查询全部帮办人绩效时长记列表结果={}", resultSet);
        return ApiResultSet.ok("请求成功", resultSet);

    }

    @Override
    public ApiResultSet<PageResult> queryHaPerRegRecordPageResult(HaPerformanceRegulartimeRecord haPerforRegRec, String beginTime, String endTime, Integer pageNum, Integer pageSize) throws Exception {
        log.info("进入获取选择的帮办人绩效时长记录统计的数据，haPerforRegRec:  {},选择开始时间:{}, 选择结束时间：{}", haPerforRegRec, beginTime, endTime);
        PageResult<HaPerformanceRegulartimeRecord> resultSet = haPerformanceRegulartimeRecordManager.queryHaPerRegRecordPage(haPerforRegRec, beginTime, endTime, pageNum, pageSize);
        log.info("分页查询全部帮办人绩效时长记列表结果={}", resultSet);
        return ApiResultSet.ok("请求成功", resultSet);

    }


    @Override
    public ApiResultSet ImportPhoneExcel(HttpServletRequest request, MultipartFile[] files)  {
        log.info("进入导入电话绩效");
        Map<String, String> resultSet = haPerformancePhoneRecordManager.ImportPhoneExcel(request,files);
        log.info("进入导入电话绩效结果={}", resultSet);
        return ApiResultSet.ok("请求成功", resultSet);
    }
}
