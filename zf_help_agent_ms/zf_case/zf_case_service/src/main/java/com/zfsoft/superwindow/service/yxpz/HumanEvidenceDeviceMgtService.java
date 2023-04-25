package com.zfsoft.superwindow.service.yxpz;

import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.superwindow.data.yxpz.HumanEvidenceDeviceMgt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * @author: kkfan
 * @create: 2020-10-23 14:43:01
 * @description: 人证核验接口服务层
 */
@RequestMapping(value = "/humanEvidenceDeviceMgt")
public interface HumanEvidenceDeviceMgtService {

    /**
     * 分页查询人证核验列表
     * @param humanEvidenceDeviceMgt
     * @param pageNumber
     * @param pageSize
     * @return
     */
    @PostMapping(value = "/pageList")
    ApiResultSet pageList(HumanEvidenceDeviceMgt humanEvidenceDeviceMgt,
                          Integer pageNumber,
                          Integer pageSize);

    /**
     * 保存/更新人证核验信息
     * @param humanEvidenceDeviceMgt
     * @return
     */
    @PostMapping(value = "/saveOrUpdate")
    ApiResultSet saveOrUpdate(HumanEvidenceDeviceMgt humanEvidenceDeviceMgt);

    /**
     * 根据id删除 支持批量删除  id使用,隔开
     * @param ids
     * @return
     */
    @PostMapping(value = "/delete")
    ApiResultSet delete(String ids);

    /**
     * 根据id获取人证核验信息详情
     * @param id
     * @return
     */
    @PostMapping(value = "/getOne")
    ApiResultSet getOne(String id);


    /**
     * 验证设备编号是否唯一
     * @param deviceId  设备id
     * @param oid   数据id
     * @return
     */
    @PostMapping(value = "/validDeviceId")
    ApiResultSet validDeviceId(String deviceId, String oid);

    /**
     * 根据当前机器的IP地址找到窗口相同的IP地址，从而得到窗口对应的机器设备编号
     * @param request
     * @return
     */
    @PostMapping(value = "/getDeviceIdByMac")
    ApiResultSet getDeviceIdByMac(HttpServletRequest request);

}
