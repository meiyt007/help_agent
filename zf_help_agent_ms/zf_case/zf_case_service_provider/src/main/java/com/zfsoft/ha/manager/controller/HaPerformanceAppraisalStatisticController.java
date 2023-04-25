package com.zfsoft.ha.manager.controller;

import cn.hutool.core.util.StrUtil;
import com.zfsoft.ha.data.HaAkeyReviewRecords;
import com.zfsoft.ha.data.vo.HaPerformanceAppraisalStatisticVo;
import com.zfsoft.ha.manager.HaPerformanceAppraisalStatisticService;
import com.zfsoft.ha.managers.*;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.platform.utils.excel.ExportExcelUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description 后台老师统计帮办人员绩效考核数据控制层
 * @author dingsn
 * @date 2022/11/04  15:03
 */
@RestController
@Slf4j
public class HaPerformanceAppraisalStatisticController implements HaPerformanceAppraisalStatisticService {
    /**
     * 加分项目维护管理实现业务层
     */
    @Resource
    private HaPlusProjectManager haPlusProjectManager;
    /**
     * 绩效加分时长统计管理业务实现层
     */
    @Resource
    private HaPerformancePlustimeRecordManager haPerformancePlustimeRecordManager;
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
    public ApiResultSet<PageResult> queryHaPerformanceAppraisalStatisticVoRecordPageResult(HaPerformanceAppraisalStatisticVo haPerformanceAppraisalStatisticVo, String beginTime, String endTime, Integer pageNum, Integer pageSize) throws Exception {
        log.info("进入获取全部帮办人绩效时长记录统计的数据，参数haPerformanceAppraisalStatisticVo:  {},选择开始时间:{}, 选择结束时间：{}", haPerformanceAppraisalStatisticVo, beginTime, endTime);
        PageResult<HaPerformanceAppraisalStatisticVo> resultSet = haPerformanceRegulartimeRecordManager.queryHaPerformanceAppraisalStatisticVoRecordPageResult(haPerformanceAppraisalStatisticVo, beginTime, endTime, pageNum, pageSize);
        log.info("分页查询全部帮办人绩效时长记列表结果={}", resultSet);
        return ApiResultSet.ok("请求成功", resultSet);

    }

    @RequestMapping( value = "/listExp",method = {RequestMethod.GET})
    public void listExp(HttpServletResponse response, String beginTime, String endTime
            , Integer pageNum, Integer pageSize) throws Exception {
        log.info("绩效统计导出；请求参数：beginTime={}，endTime={}", beginTime, endTime);
        HaPerformanceAppraisalStatisticVo haPerformanceAppraisalStatisticVo = new HaPerformanceAppraisalStatisticVo();
        PageResult<HaPerformanceAppraisalStatisticVo> pageResult = haPerformanceRegulartimeRecordManager.queryHaPerformanceAppraisalStatisticVoRecordPageResult(haPerformanceAppraisalStatisticVo, beginTime, endTime, pageNum, pageSize);
        List<HaPerformanceAppraisalStatisticVo> list = pageResult.getData();
        String title = "黄浦区行政服务中心聘用人员绩效考核表";
        String[] rowsName = new String[] { "序号", "帮办人员姓名", "服务时长（分钟）", "加分项目时长（分钟）", "云客服通话时长（分钟）","季度工作量小计", "管理系数", "季度累计积分",
                "工作日天数" ,"日工作量", "工作效能百分比（含管理系数）" , "服务人数（总数）" };
        List<Object[]> dataList = new ArrayList<Object[]>();
        Object[] objs = null;
        for (int i = 0; i < list.size(); i++) {
            HaPerformanceAppraisalStatisticVo haPerformanceAppraisalStatisticVoN = list.get(i);
            objs = new Object[rowsName.length];
            objs[0] = i + 1;
            if (!StrUtil.isEmpty(haPerformanceAppraisalStatisticVoN.getWorkUserName())) {//帮办人员姓名
                objs[1] = haPerformanceAppraisalStatisticVoN.getWorkUserName();
            } else {
                objs[1] = "-";
            }
            if (haPerformanceAppraisalStatisticVoN.getServiceDuration() != null) {//服务时长
                objs[2] = haPerformanceAppraisalStatisticVoN.getServiceDuration();
            } else {
                objs[2] = "-";
            }
            if (haPerformanceAppraisalStatisticVoN.getPlusDuration() != null) {//加分项目时长
                objs[3] = haPerformanceAppraisalStatisticVoN.getPlusDuration();
            } else {
                objs[3] = "-";
            }
            if (haPerformanceAppraisalStatisticVoN.getPhoneDuration() != null) {//加分项目时长
                objs[4] = haPerformanceAppraisalStatisticVoN.getPhoneDuration();
            } else {
                objs[4] = "-";
            }
            if (haPerformanceAppraisalStatisticVoN.getJiduSum() != null) {//季度工作量小计
                objs[5] = haPerformanceAppraisalStatisticVoN.getJiduSum();
            } else {
                objs[5] = "-";
            }
            if (!StrUtil.isEmpty(haPerformanceAppraisalStatisticVoN.getManageFactor())) {//管理系数
                objs[6] = haPerformanceAppraisalStatisticVoN.getManageFactor();
            } else {
                objs[6] = "-";
            }

            objs[7] = haPerformanceAppraisalStatisticVoN.getLeijiSum();

            if (haPerformanceAppraisalStatisticVoN.getFdWorkCount() != null) {//工作日天数
                objs[8] = haPerformanceAppraisalStatisticVoN.getFdWorkCount();
            } else {
                objs[8] = "-";
            }
            objs[9] = haPerformanceAppraisalStatisticVoN.getDayWorkSum();
            objs[10] = haPerformanceAppraisalStatisticVoN.getGzxnPercent();
            //服务人数
            objs[11] = haPerformanceAppraisalStatisticVoN.getServicePeopleNum();

            dataList.add(objs);
        }
        ExportExcelUtil ex = new ExportExcelUtil();
        ex.export(response, title, rowsName, dataList);

    }

    @Override
    public ApiResultSet<PageResult> queryGroupLeaderPageListByAuditStatus(String beginTime, String endTime,
                                                                          Integer pageNum, Integer pageSize) throws Exception {
        log.info("进入获取全部组长未审核加分时长总数的数据，参数选择开始时间:{}, 选择结束时间：{}", beginTime, endTime);
        PageResult<HaPerformanceAppraisalStatisticVo> resultSet = haPerformanceRegulartimeRecordManager.queryGroupLeaderPageListByAuditStatus(beginTime, endTime, pageNum, pageSize);
        log.info("分页查询全部组长未审核加分时长总数列表结果={}", resultSet);
        return ApiResultSet.ok("请求成功", resultSet);
    }

    @Override
    public ApiResultSet akeyReviewByGroupLeaderId(Long id) throws Exception {
        log.info("根据帮办组长id催审组长审核加分时长,组长id={}", id);
        Integer index = haPerformanceRegulartimeRecordManager.akeyReviewByGroupLeaderId(id);
        if (1 == index) {
            return ApiResultSet.ok("催审组长审核已成功");
        } else {
            return new ApiResultSet(500, "催审组长审核失败");
        }
    }

    @Override
    public ApiResultSet batchAkeyReviewByGroupLeaderIds(List<Long> ids) throws Exception {
        log.info("一键催审(多个帮办组长id)催审组长审核加分时长, 组长ids={}", ids);
        ApiResultSet<HaAkeyReviewRecords> apiResultSet = new ApiResultSet<>();
        haPerformanceRegulartimeRecordManager.batchAkeyReviewByGroupLeaderIds(ids);
        apiResultSet.setMessage("批量一键催办成功");
        return null;
    }
}
