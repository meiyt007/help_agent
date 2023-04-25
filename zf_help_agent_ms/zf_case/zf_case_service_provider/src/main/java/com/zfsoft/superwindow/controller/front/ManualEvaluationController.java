package com.zfsoft.superwindow.controller.front;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zfsoft.microservice.settings.data.SysConfig;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.superwindow.data.front.ManualEvaluation;
import com.zfsoft.superwindow.dbaccess.data.DbManualEvaluation;
import com.zfsoft.superwindow.feign.settings.SysConfigFeignService;
import com.zfsoft.superwindow.manager.front.ManualEvaluationManager;
import com.zfsoft.superwindow.service.front.ManualEvaluationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@RestController
public class ManualEvaluationController implements ManualEvaluationService {

    @Resource
    private ManualEvaluationManager manualEvaluationManager;

    @Resource
    private SysConfigFeignService sysConfigFeignService;

    @Override
    public ApiResultSet<DbManualEvaluation> selectByVirtualBusinessNum(String virtualBusinessNum) {
        DbManualEvaluation dbManualEvaluation = manualEvaluationManager.queryById(virtualBusinessNum);
        ApiResultSet<DbManualEvaluation> apiResultSet = new ApiResultSet<>();
        apiResultSet.setData(dbManualEvaluation);
        return apiResultSet;
    }

    @Override
    public ApiResultSet saveManualEvaluation(ManualEvaluation manualEvaluation) {
        ApiResultSet apiResultSet = manualEvaluationManager.saveManualEvaluation(manualEvaluation);
        return apiResultSet;
    }

    @Override
    public ApiResultSet updateManualEvaluation(ManualEvaluation manualEvaluation) {
        manualEvaluationManager.updateManualEvaluation(manualEvaluation);
        ApiResultSet<DbManualEvaluation> apiResultSet = new ApiResultSet<>();
        return apiResultSet;
    }

    @Override
    public ApiResultSet pushManualEvaluation(String virtualBusinessNum) {
        String body = manualEvaluationManager.pushManualEvaluation(virtualBusinessNum);
        ApiResultSet<String> apiResultSet = new ApiResultSet<>();
        apiResultSet.setData(body);
        return apiResultSet;
    }

    @Override
    public ApiResultSet getSatisfaction() {
        List list = manualEvaluationManager.getSatisfaction();
        ApiResultSet apiResultSet = new ApiResultSet<>();
        apiResultSet.setData(list);
        return apiResultSet;
    }

    @Override
    public ApiResultSet getEvaluateAndPush(String virtualBusinessNum) {
        ApiResultSet<SysConfig> config = sysConfigFeignService.getSysConfigByCode("XJTS");
        String type ="3";
        if(config.getData() !=null){
            type = config.getData().getValue();
        }
        String body = null;
        List<DbManualEvaluation> dbManualEvaluationList = this.manualEvaluationManager.queryByVirtualBusinessNum(virtualBusinessNum);
        if (dbManualEvaluationList.size() > 0) {
            for (DbManualEvaluation dbManualEvaluation : dbManualEvaluationList) {
                if (dbManualEvaluation.getEvaluateFlag() == 0) {
                    if (dbManualEvaluation.getServiceEvaluation() != null && dbManualEvaluation.getServiceEvaluation() != "") {
                        if (Integer.parseInt(dbManualEvaluation.getServiceEvaluation()) > Integer.parseInt(type)) {
                            this.manualEvaluationManager.frontPushManualEvaluation(dbManualEvaluation);
                        }
                    }
                }
            }
        }
        ApiResultSet<String> apiResultSet = new ApiResultSet<>();
        apiResultSet.setData(body);
        return apiResultSet;
    }

    @Override
    public ApiResultSet queryPageList(ManualEvaluation manualEvaluation, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<DbManualEvaluation> dbManualEvaluationList = this.manualEvaluationManager.queryList(manualEvaluation);
        PageResult<DbManualEvaluation> pageResult = new PageResult<>(
                ((Page) dbManualEvaluationList).getPageNum(),
                ((Page) dbManualEvaluationList).getPageSize(),
                ((Page) dbManualEvaluationList).getTotal()
        );
        pageResult.setData(dbManualEvaluationList);
        log.info("获取好差评列表调用成功结果为：{}", JSON.toJSONString(pageResult));
        return new ApiResultSet(pageResult);
    }
}
