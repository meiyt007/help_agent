package com.zfsoft.superwindow.controller.front;

import com.zfsoft.cases.feign.SysDictFeignService;
import com.zfsoft.microservice.settings.data.SysDict;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.superwindow.data.front.WorkerHonor;
import com.zfsoft.superwindow.dbaccess.data.DbWorkerHonor;
import com.zfsoft.superwindow.manager.front.WorkerHonorManager;
import com.zfsoft.superwindow.service.front.WorkerHonorService;
import com.zfsoft.superwindow.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.*;

@Slf4j
@RestController
public class WorkerHonorController implements WorkerHonorService {
    @Resource
    private SysDictFeignService sysDictFeignService;

    @Resource
    private WorkerHonorManager workerHonorManager;

    /**
     * 获取所有的荣誉
     *
     * @return
     */
    @Override
    public List initPrintSetting() {
        List list = new ArrayList();
        ApiResultSet<List<SysDict>> apiResultSet = sysDictFeignService.querySysDictListByParentCode("RYFL");
        if (null != apiResultSet.getData()) {
            List<SysDict> sysDictList = apiResultSet.getData();
            if (sysDictList.size() > 0) {
                for (SysDict sysDict : sysDictList) {
                    Map<String, String> modelMap = new HashMap<>();
                    modelMap.put("code", sysDict.getCode());
                    modelMap.put("name", sysDict.getName());
                    list.add(modelMap);
                }
            }
        }
        return list;
    }

    /**
     * 获取单个员工的荣誉
     * @return
     */
    @Override
    public List selectByUserOid() {
        List list = new ArrayList<>();
        List<String> codeList = new ArrayList<>();
        DbWorkerHonor dbWorkerHonor = this.workerHonorManager.selectByUserOid();
        if(dbWorkerHonor !=null){
            if (!StringUtils.isEmpty(dbWorkerHonor.getHonorOid())) {
                codeList = Arrays.asList(dbWorkerHonor.getHonorOid().split(","));
            }
        }
        ApiResultSet<List<SysDict>> apiResultSet = sysDictFeignService.querySysDictListByParentCode("RYFL");
        if (null != apiResultSet.getData()) {
            List<SysDict> sysDictList = apiResultSet.getData();
            if (sysDictList.size() > 0) {
                for (SysDict sysDict : sysDictList) {
                    if (codeList.size() > 0) {
                        for (String str : codeList) {
                            if (sysDict.getCode().equals(str)) {
                                Map<String, String> modelMap = new HashMap<>();
                                modelMap.put("code", sysDict.getCode());
                                modelMap.put("name", sysDict.getName());
                                list.add(modelMap);
                            }
                        }
                    }
                }
            }
        }
        return list;
    }

    @Override
    public List queryByUserOid(String userOid) {
        List list = new ArrayList<>();
        List<String> codeList = new ArrayList<>();
        DbWorkerHonor dbWorkerHonor = this.workerHonorManager.queryByUserOid(userOid);
        if (dbWorkerHonor != null) {
            if (!StringUtils.isEmpty(dbWorkerHonor.getHonorOid())) {
                codeList = Arrays.asList(dbWorkerHonor.getHonorOid().split(","));
            }
            ApiResultSet<List<SysDict>> apiResultSet = sysDictFeignService.querySysDictListByParentCode("RYFL");
            if (null != apiResultSet.getData()) {
                List<SysDict> sysDictList = apiResultSet.getData();
                if (sysDictList.size() > 0) {
                    for (SysDict sysDict : sysDictList) {
                        if (codeList.size() > 0) {
                            for (String str : codeList) {
                                if (sysDict.getCode().equals(str)) {
                                    Map<String, String> modelMap = new HashMap<>();
                                    modelMap.put("code", sysDict.getCode());
                                    modelMap.put("name", sysDict.getName());
                                    list.add(modelMap);
                                }
                            }
                        }
                    }
                }
            }
        }
        return list;
    }

    /**
     * 新增或修改员工荣誉信息
     * @param workerHonor
     * @return
     */
    @Override
    public ApiResultSet<WorkerHonor> saveWorkerHonor(WorkerHonor workerHonor) {
        workerHonorManager.saveWorkerHonor(workerHonor);
        ApiResultSet<WorkerHonor> apiResultSet = new ApiResultSet<>();
        apiResultSet.setData(workerHonor);
        return apiResultSet;
    }

    /**
     * 根据主键删除员工荣誉信息
     * @param id
     * @return
     */
    @Override
    public ApiResultSet<Integer> deleteWorkerHonorById(Long id) {
        this.workerHonorManager.deleteWorkerHonor(id);
        log.info("删除成功：{}", id);
        return null;
    }

    /**
     * 获取所有员工荣誉信息列表
     *
     * @return
     */
    @Override
    public List queryALL() {
        List<DbWorkerHonor> list = this.workerHonorManager.queryAll();
        return list;
    }

}
