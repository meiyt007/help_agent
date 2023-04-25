package com.zfsoft.service.controller.sxService;

import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.service.manager.sxService.ReviewPointsManager;
import com.zfsoft.service.sxService.data.ReviewPoints;
import com.zfsoft.service.sxService.service.ReviewPointsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 *
 * @ClassName RefinedMaterialController
 * @Description 审查要点控制类
 * @Author liangss
 * @Date 2021-07-12
 * @Version V1.1
 **/
@RestController
@Slf4j
public class ReviewPointsController implements ReviewPointsService {

    @Resource
    private ReviewPointsManager reviewPointsManager;


    @Override
    public ApiResultSet<List<ReviewPoints>> getReviewPointsListByRefinedMaterialOid(String refinedMaterialOid) {
        List<ReviewPoints> reviewPointsList=reviewPointsManager.getReviewPointsListByRefinedMaterialOid(refinedMaterialOid);
        ApiResultSet<List<ReviewPoints>> apiResultSet = new  ApiResultSet<List<ReviewPoints>>();
        apiResultSet.setData(reviewPointsList);
        return apiResultSet;
    }


}
