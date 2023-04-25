package com.zfsoft.superwindow.service.front;

import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.utils.feign.ProcessFeignCalledResult;
import com.zfsoft.superwindow.data.front.WorkerHonor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@RequestMapping("/honor")
public interface WorkerHonorService {

    /**
     * 获取所有的荣誉
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = {"/honorList"}, method = {RequestMethod.GET})
    List initPrintSetting();

    /**
     * 获取员工荣誉
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = {"/getPersonalHonors"}, method = {RequestMethod.GET})
    List selectByUserOid();

    /**
     * 获取员工荣誉
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = {"/getOneHonorList"}, method = {RequestMethod.GET})
    List queryByUserOid(String userOid);

    /**
     * 保存/更新员工荣誉
     * @param workerHonor
     * @return
     */
    @ProcessFeignCalledResult
    @PostMapping(value = "/saveOrUpdateWorkerHonor")
    ApiResultSet<WorkerHonor> saveWorkerHonor(@RequestBody WorkerHonor workerHonor);

    /**
     * 删除员工荣誉
     * @param id
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = {"/deleteWorkerHonorById"}, method = {RequestMethod.GET})
    ApiResultSet<Integer> deleteWorkerHonorById(Long id);

    /**
     * 获取所有员工荣誉信息列表
     * @return
     */
    @RequestMapping(value = {"/honorAll"}, method = {RequestMethod.GET})
    List queryALL();
}
