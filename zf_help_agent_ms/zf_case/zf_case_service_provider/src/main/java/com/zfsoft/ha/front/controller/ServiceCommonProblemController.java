package com.zfsoft.ha.front.controller;

import com.zfsoft.ha.data.HaServiceCommonProblem;
import com.zfsoft.ha.data.HaUserResource;
import com.zfsoft.ha.data.vo.HaServiceCommonProblemVo;
import com.zfsoft.ha.front.ServiceCommonProblemService;
import com.zfsoft.ha.manager.HaServiceCommonProblemService;
import com.zfsoft.ha.managers.HaServiceCommonProblemManager;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.data.PageResult;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchTask;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author zhaobf
 * @version 1.0
 * @date 2022/9/5 14:37
 */
@Slf4j
@RestController
public class ServiceCommonProblemController implements ServiceCommonProblemService {

    @Resource
    private HaServiceCommonProblemManager haServiceCommonProblemManager;


    @Override
    public ApiResultSet<List<HaServiceCommonProblemVo>> queryComPro(String serviceOid,String optionValOids ) {
        log.info("事项常见问题：进入事项常见问题查询controller查询参数 serviceOid: {}", serviceOid);
        HaServiceCommonProblemVo haServiceCommonProblem = new HaServiceCommonProblemVo();
        haServiceCommonProblem.setServiceOid(serviceOid);
        String[] valueOids = optionValOids.split(",");
        //set去重
        Set<HaServiceCommonProblemVo> setResult = new HashSet<>();
        for (String valueOid : valueOids) {
            haServiceCommonProblem.setOptionVal(valueOid);
            setResult.addAll( haServiceCommonProblemManager.queryInfo(haServiceCommonProblem));
        }
        List<HaServiceCommonProblemVo> listResult = new ArrayList<>(setResult);
        ApiResultSet<List<HaServiceCommonProblemVo>> apiResultSet = new ApiResultSet<>();
        apiResultSet.setData(listResult);
        return apiResultSet;
    }


}
