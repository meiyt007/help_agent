package com.zfsoft.ha.manager;

import com.zfsoft.ha.data.vo.HaPerformanceAppraisalStatisticVo;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.platform.utils.feign.ProcessFeignCalledResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @Description 后台老师统计帮办人员绩效考核数据接口
 * @author dingsn
 * @date 2022/11/04  10:48
 */
@RequestMapping("/performanceAppraisalStatistic")
public interface HaPerformanceAppraisalStatisticService {
    /**
     * @description: 查询帮代办人员的所有的绩效数据
     * @param haPerformanceAppraisalStatisticVo 绩效考核统计实体类（包括加分时长绩效和常规时长绩效）请求参数
     * @param beginTime 开始时间
     * @param endTime 结束时间
     * @param pageNum
     * @param pageSize
     * @author: dingsn
     * @date: 2022/11/01  14:47
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/queryHaPerformanceAppraisalStatisticVoRecordPageResult", method = {RequestMethod.POST})
    ApiResultSet<PageResult> queryHaPerformanceAppraisalStatisticVoRecordPageResult(HaPerformanceAppraisalStatisticVo haPerformanceAppraisalStatisticVo,
                                                                                    @RequestParam(value = "beginTime", required = false) String beginTime,
                                                                                    @RequestParam(value = "endTime", required = false) String endTime,
                                                                                    @RequestParam(value = "pageNum", required = false) Integer pageNum,
                                                                                    @RequestParam(value = "pageSize", required = false) Integer pageSize) throws Exception;
    /**
     * 导出统计绩效信息
     *
     * @param beginTime     开始时间
     * @param endTime     结束时间
     * @param pageNum 开始页
     * @param pageSize   每页大小
     * @author: dingsn
     * @date: 2022/11/07  17:47
     * @return
     */
   /* @RequestMapping(value = "/listExp", method = {RequestMethod.GET})
    void listExp(HttpServletResponse response, @RequestParam(value = "beginTime",required = false) String beginTime,
                @RequestParam(value = "endTime",required = false) String endTime,
                @RequestParam(value = "pageNum",required = false) Integer pageNum,
                @RequestParam(value = "pageSize",required = false) Integer pageSize) throws Exception;
*/
    /**
     * 根据时间区间去查待审核状态下的组长的信息（包括组长姓名、组长手机号、所属分组、几条没审）
     * @param beginTime
     * @param endTime
     * @param pageNum
     * @param pageSize
     * @author: dingsn
     * @date: 2022/11/09  12:25
     * @throws Exception
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/queryGroupLeaderPageListByAuditStatus", method = {RequestMethod.POST})
    ApiResultSet<PageResult> queryGroupLeaderPageListByAuditStatus(@RequestParam(value = "beginTime", required = false) String beginTime,
                                                                                    @RequestParam(value = "endTime", required = false) String endTime,
                                                                                    @RequestParam(value = "pageNum", required = false) Integer pageNum,
                                                                                    @RequestParam(value = "pageSize", required = false) Integer pageSize) throws Exception;

    /**
     * @description:  根据帮办组长id催审组长审核加分时长
     * @param id 主键
     * @author: dingsn
     * @Date: 2022/11/09
     **/
    @ProcessFeignCalledResult
    @RequestMapping(value = "/akeyReviewByGroupLeaderId", method = {RequestMethod.GET})
    ApiResultSet akeyReviewByGroupLeaderId(@RequestParam("id") Long id) throws Exception;

    /**
     * 一键催审(多个帮办组长id)催审组长审核加分时长
     * @param ids
     * @return
     * @throws Exception
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/batchAkeyReviewByGroupLeaderIds", method = {RequestMethod.GET})
    ApiResultSet batchAkeyReviewByGroupLeaderIds(@RequestParam(value = "ids") List<Long> ids) throws Exception;



}
