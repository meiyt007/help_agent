package com.zfsoft.ha.manager;

import com.zfsoft.ha.data.HaVisit;
import com.zfsoft.ha.data.vo.HaUserResourceVo;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.platform.utils.feign.ProcessFeignCalledResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author zhaobf
 * @version 1.0
 * @date 2022/8/11 19:45
 */
@RequestMapping("/help/haVisit")
public interface HaVisitService {
    /**
     *
     * 获取分页访问列表
     * @param name 姓名
     * @param visitType 类型 1 个人2 法人
     * @param pageNumber 页码
     * @param pageSize 每页数量
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/queryHaVisitByNameAndVisitType", method = {RequestMethod.GET})
    ApiResultSet<PageResult<HaVisit>> queryHaVisitByNameAndVisitType(@RequestParam("name") String name, @RequestParam("visitType") String visitType,@RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber, @RequestParam(value = "pageSize", required = false, defaultValue = "10")  Integer pageSize);

    /**
     * 获取来访人员信息
     * @param workQueueId 办事队列id
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/getVisitMassageByWorkQueueId", method = {RequestMethod.GET})
    ApiResultSet getVisitMassageByWorkQueueId(@RequestParam("workQueueId") String workQueueId);
}
