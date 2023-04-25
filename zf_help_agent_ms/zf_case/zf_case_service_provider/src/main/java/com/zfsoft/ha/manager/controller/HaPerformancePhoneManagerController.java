package com.zfsoft.ha.manager.controller;

import com.zfsoft.ha.data.HaPerformancePhoneRecord;
import com.zfsoft.ha.data.requestData.HaPerPhoneRequestData;
import com.zfsoft.ha.manager.HaPerformancePhoneManagerService;
import com.zfsoft.ha.managers.HaPerformancePhoneRecordManager;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.data.PageResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @Description 帮代办人员电话绩效表控制层
 * @Author: zhaobf
 * @Date: 2023/3/20 13:58
 */
@RestController
@Slf4j
public class HaPerformancePhoneManagerController implements HaPerformancePhoneManagerService {
    /**
     * 帮代办人员电话绩效表实现层
     */
    @Resource
    private HaPerformancePhoneRecordManager haPerformancePhoneRecordManager;
    /**
     * @description:  查询帮代办人员电话绩效分页信息列表
     * @author: zhaobf
     * @Date: 2022年7月26日14:04:40
     * @return  ApiResultSet<PageResult<HaWorkGroup>> 获取帮代办人员电话绩效分页信息列表详情
     **/
    @Override
    public ApiResultSet<PageResult<HaPerformancePhoneRecord>> queryPerPhoneWithPage(HaPerPhoneRequestData haPerPhoneRequestData) throws Exception {
        log.info("查询帮代办人员电话绩效分页信息列表， haPerPhoneRequestData:{}",haPerPhoneRequestData);
        PageResult<HaPerformancePhoneRecord> pageResult = haPerformancePhoneRecordManager.queryPerPhoneWithPage(haPerPhoneRequestData);
        log.debug("pageResult结果集：pageResult:{}",pageResult);
        ApiResultSet<PageResult<HaPerformancePhoneRecord>> apiResultSet = new ApiResultSet<>();
        apiResultSet.setData(pageResult);
        return apiResultSet;
    }

    /**
     * @description:  删除帮代办人员电话绩效信息
     * @param id 主键
     * @author: zhaobf
     * @Date: 2023/3/20
     * @return  ApiResultSet 获取删除帮代办人员电话绩效信息标识
     **/
    @Override
    public ApiResultSet deletePerPhoneById(Long id) throws Exception {
        log.info("删除帮代办人员电话绩效信息， id:{}",id);
        Integer index = haPerformancePhoneRecordManager.deletePerPhoneById(id);
        if (index == 1) {
            return ApiResultSet.ok("帮代办人员电话绩效信息删除成功");
        } else {
            return new ApiResultSet(500, "帮代办人员电话绩效信息删除失败");
        }
    }

    /**
     * @description: 新增或者修改帮代办人员电话绩效信息
     * @param phoneRecord 帮代办人员电话绩效实体类
     * @author: zhaobf
     * @Date: 2023/3/20
     * @return  ApiResultSet 获取新增或者修改帮代办人员电话绩效信息标识
     **/
    @Override
    public ApiResultSet saveOrUpdatePerPhone(HaPerformancePhoneRecord phoneRecord) throws Exception {
        log.info("新增或者修改帮代办人员电话绩效信息， haWorkGroup:{}",phoneRecord);
        Map<String, Object> map = haPerformancePhoneRecordManager.saveOrUpdatePerPhone(phoneRecord);
        int index = (int) map.get("index");
        log.debug("index结果集，index:{}", index);
        if (index == 1) {
            return ApiResultSet.ok("新增成功");
        } else {
            return ApiResultSet.ok("修改成功");
        }
    }

    /**
     * @description:  根据id查询帮代办人员电话绩效信息
     * @param id
     * @author: zhaobf
     * @Date: 2023/3/20
     * @return  ApiResultSet 获取根据id查询帮代办人员电话绩效信息
     */
    @Override
    public ApiResultSet getPerPhoneById(Long id) throws Exception {
        log.info("根据id查询帮代办人员电话绩效信息， id:{}",id);
        HaPerformancePhoneRecord haWorkGroup = haPerformancePhoneRecordManager.selectPerPhoneById(id);
        return ApiResultSet.ok("接口调用成功", haWorkGroup);
    }

    @Override
    public ApiResultSet ImportPhoneExcel(HttpServletRequest request, MultipartFile[] files)  {
        log.info("进入导入电话绩效");
        Map<String, String> resultSet = haPerformancePhoneRecordManager.ImportPhoneExcel(request,files);
        log.info("进入导入电话绩效结果={}", resultSet);
        return ApiResultSet.ok("请求成功", resultSet);
    }


}
