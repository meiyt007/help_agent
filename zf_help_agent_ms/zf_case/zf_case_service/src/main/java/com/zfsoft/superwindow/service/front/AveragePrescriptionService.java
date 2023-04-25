package com.zfsoft.superwindow.service.front;

import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.utils.feign.ProcessFeignCalledResult;
import com.zfsoft.superwindow.data.front.AveragePrescription;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @ClassName AveragePrescriptionService
 * @Description 平均受理时效定义接口
 * @Author wb
 * @Date 2021/7/17
 * @Version V1.0
 */
@RequestMapping("/front")
public interface AveragePrescriptionService {

    /**
     * 获取平均受理时效
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = {"/getAveragePrescription"}, method = {RequestMethod.GET})
    int getAverageTime();

    /**
     * 插入受理时间
     * @param averagePrescription
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = {"/addAveragePrescription"}, method = {RequestMethod.POST})
    ApiResultSet insertAveragePrescription(@RequestBody AveragePrescription averagePrescription);
}
