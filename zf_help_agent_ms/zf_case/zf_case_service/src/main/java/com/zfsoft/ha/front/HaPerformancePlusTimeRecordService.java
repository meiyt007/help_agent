package com.zfsoft.ha.front;

import com.zfsoft.ha.data.HaPerformancePlustimeRecord;
import com.zfsoft.ha.data.HaPerformanceRegulartimeRecord;
import com.zfsoft.ha.data.HaPlusProject;
import com.zfsoft.ha.data.HaWorkUser;
import com.zfsoft.ha.data.vo.HaPerformanceAppraisalStatisticVo;
import com.zfsoft.ha.data.vo.HaPerformancePlustimeRecordVo;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.platform.utils.feign.ProcessFeignCalledResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @Description 绩效加分项目时长记录管理接口
 * @author dingsn
 * @date 2022/11/01  14:15
 */
@RequestMapping("/ha/performancePlusTimeRecord")
public interface HaPerformancePlusTimeRecordService {
    /**
     * @description: 查询当前帮代办人员的所有的绩效加分时长记录数据
     * @author: dingsn
     * @date: 2022/11/01  14:47
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/queryHaPerformancePlustimeRecordPageResult", method = {RequestMethod.POST})
    ApiResultSet<PageResult> queryHaPerformancePlustimeRecordPageResult(HaPerformancePlustimeRecordVo haPerformancePlustimeRecordVo,
                                                                        @RequestParam(value = "plusProjectOid", required = false) String plusProjectOid,
                                                                        @RequestParam(value = "auditStatus", required = false) String auditStatus,
                                                                        @RequestParam(value = "pageNum", required = false) Integer pageNum,
                                                                        @RequestParam(value = "pageSize", required = false) Integer pageSize) throws Exception;

    /**
     * @description: 新增或者修改绩效加分时长信息
     * @param haPerformancePlustimeRecord 绩效加分时长记录实体类
     * @author: dingsn
     * @Date: 2022/10/27
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/saveHaPerformancePlustimeRecord",method = {RequestMethod.POST})
    ApiResultSet saveHaPerformancePlustimeRecord(HaPerformancePlustimeRecord haPerformancePlustimeRecord) throws Exception;

    /**
     * @description:  根据id查询绩效加分时长记录信息
     * @param id
     * @author: dingsn
     * @Date: 2022/10/27
     */
    @ProcessFeignCalledResult
    @RequestMapping( value = "/getHaPerformancePlustimeRecordById",method = {RequestMethod.GET})
    ApiResultSet getHaPerformancePlustimeRecordById(@RequestParam("id") Long id) throws Exception;

    /**
     * @description:  删除绩效加分时长记录信息
     * @param id 主键
     * @author: dingsn
     * @Date: 2022/10/27
     **/
    @ProcessFeignCalledResult
    @RequestMapping(value = "/deleteHaPerformancePlustimeRecordById", method = {RequestMethod.POST})
    ApiResultSet deleteHaPerformancePlustimeRecordById(@RequestParam("id") Long id) throws Exception;

    /**
     * @description: 查询当前帮办人所属的组长列表
     * @author: dingsn
     * @Date: 2022/11/02
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/queryGroupLeaderInfoList",method = {RequestMethod.POST})
    ApiResultSet<List<HaWorkUser>> queryGroupLeaderInfoList() throws Exception;

    /**
     * @description:  查询HaPlusProject对象list集合
     * @author: dingsn
     * @Date: 2022/11/03
     **/
    @RequestMapping( value = "/queryHaPlusProjectList",method = {RequestMethod.GET})
    ApiResultSet<List<HaPlusProject>> queryHaPlusProjectList() throws Exception;

    /**
     * @description: 组长根据选择的加分时长记录去修改该条记录审核状况
     * @param haPerformancePlustimeRecord 绩效加分时长记录实体类
     * @author: dingsn
     * @Date: 2022/11/03
     **/
    @ProcessFeignCalledResult
    @RequestMapping( value = "/groupLeaderAuditHaPerformancePlustimeRecord",method = {RequestMethod.POST})
    ApiResultSet groupLeaderAuditHaPerformancePlustimeRecord(HaPerformancePlustimeRecord haPerformancePlustimeRecord) throws Exception;

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
    * Description: 获取帮办人的所有绩效记录
    * @param haPerforRegRec 绩效记录信息
    * @param beginTime 开始时间
    * @param endTime 结束时间
    * @param pageNum 页码
    * @param pageSize 篇幅
    * @author zhaobf
    * date: 2023/2/21 10:15
    * @copyright 版权由上海卓繁信息技术股份有限公司拥有
    */
    @PostMapping(value = "/queryHaPerRegRecordPageResult")
    ApiResultSet<PageResult> queryHaPerRegRecordPageResult(HaPerformanceRegulartimeRecord haPerforRegRec, String beginTime, String endTime, Integer pageNum, Integer pageSize) throws Exception;

    @PostMapping(value = "/ImportPhoneExcel")
    ApiResultSet<Map<String, String>> ImportPhoneExcel(HttpServletRequest request, MultipartFile[] files);
}
