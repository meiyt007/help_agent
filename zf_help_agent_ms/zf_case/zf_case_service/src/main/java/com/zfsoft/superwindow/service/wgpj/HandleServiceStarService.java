package com.zfsoft.superwindow.service.wgpj;

import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.utils.feign.ProcessFeignCalledResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping("/starService")
public interface HandleServiceStarService {

    /**
     * 查询推送评价星级
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/getPushStar", method = {RequestMethod.GET})
    ApiResultSet getPushStar();

    /**
     * 根据虚拟ID查询评价星级
     * @param virtualBusinessOid
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/getStarAndScore", method = {RequestMethod.GET})
    ApiResultSet getStarAndScoreByOid(@RequestParam(value = "virtualBusinessOid", required = false) String virtualBusinessOid);
}
