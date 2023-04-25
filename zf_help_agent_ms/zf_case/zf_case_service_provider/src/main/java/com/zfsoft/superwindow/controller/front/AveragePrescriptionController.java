package com.zfsoft.superwindow.controller.front;

import com.alibaba.fastjson.JSON;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.superwindow.data.front.AveragePrescription;
import com.zfsoft.superwindow.manager.front.AveragePrescriptionManager;
import com.zfsoft.superwindow.service.front.AveragePrescriptionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @ClassName AveragePrescriptionController
 * @Description 平均受理时效的实现类
 * @Author wb
 * @Date 2021-07-17 10:30
 * @Version V1.0
 */
@RestController
@Slf4j
public class AveragePrescriptionController implements AveragePrescriptionService {

    @Resource
    private AveragePrescriptionManager averagePrescriptionManager;

    @Override
    public int getAverageTime() {
        int num = averagePrescriptionManager.getAveragePrescription();
        return num;
    }

    @Override
    public ApiResultSet insertAveragePrescription(AveragePrescription averagePrescription) {
        averagePrescriptionManager.addAveragePrescription(averagePrescription);
        log.info("平均受理时效新增/更新成功：{}", JSON.toJSONString(averagePrescription));
        return new ApiResultSet(averagePrescription);
    }
}
