package com.zfsoft.service.sxService.service;

import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.utils.feign.ProcessFeignCalledResult;
import com.zfsoft.service.sxService.data.ReviewPoints;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @ClassName reviewPointsService
 * @Description 审查要点信息服务定义接口
 * @Author liangss
 * @Date 2021-07-12
 * @Version V1.0
 **/

@RequestMapping("/affair/reviewPoints")
public interface ReviewPointsService {


    /**
     * 根据事项材料id获取审查要点列表
     * @param refinedMaterialOid
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping( value = "/getReviewPointsListByRefinedMaterialOid",method = {RequestMethod.GET})
    ApiResultSet<List<ReviewPoints>> getReviewPointsListByRefinedMaterialOid(@RequestParam("refinedMaterialOid") String refinedMaterialOid);


   
}
