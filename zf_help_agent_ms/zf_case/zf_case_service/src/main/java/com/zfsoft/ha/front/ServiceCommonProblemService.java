package com.zfsoft.ha.front;


import com.zfsoft.ha.data.vo.HaServiceCommonProblemVo;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.utils.feign.ProcessFeignCalledResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author zhaobf
 * @version 1.0
 * @date 2022/9/5 14:32
 */
@RequestMapping("/ha/service/commonProblem")
public interface ServiceCommonProblemService {

    /**
     * 根据事项oid查询事项常见问题
     * @param serviceOid 事项oid
     * @date 2022/9/5 15:14
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/queryComPro", method = {RequestMethod.GET})
    ApiResultSet<List<HaServiceCommonProblemVo>> queryComPro(@RequestParam(value = "serviceOid", required = false) String serviceOid,
                                                             @RequestParam(value = "optionValOids", required = false) String optionValOids);


}
