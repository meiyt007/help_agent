package com.zfsoft.superwindow.controller.yxpz;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.superwindow.data.yxpz.HumanEvidenceDeviceMgt;
import com.zfsoft.superwindow.manager.yxpz.HumanEvidenceDeviceMgtManager;
import com.zfsoft.superwindow.service.yxpz.HumanEvidenceDeviceMgtService;
import com.zfsoft.superwindow.util.IpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author: kkfan
 * @create: 2020-10-23 14:42:24
 * @description: 人证核验管理控制层
 */
@Slf4j
@RestController
public class HumanEvidenceDeviceMgtController implements HumanEvidenceDeviceMgtService {
    @Resource
    private HumanEvidenceDeviceMgtManager humanEvidenceDeviceMgtManager;

    @Override
    public ApiResultSet pageList(HumanEvidenceDeviceMgt humanEvidenceDeviceMgt,
                                 @RequestParam(value = "pageNum", required = false, defaultValue = "1") Integer pageNum,
                                 @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<HumanEvidenceDeviceMgt> humanEvidenceDeviceMgtList = this.humanEvidenceDeviceMgtManager.queryList(humanEvidenceDeviceMgt);
        PageResult<HumanEvidenceDeviceMgt> pageResult = new PageResult<>(
                ((Page) humanEvidenceDeviceMgtList).getPageNum(),
                ((Page) humanEvidenceDeviceMgtList).getPageSize(),
                ((Page) humanEvidenceDeviceMgtList).getTotal()
        );
        pageResult.setData(humanEvidenceDeviceMgtList);
        log.info("获取人证核验列表调用成功结果为：{}", JSON.toJSONString(pageResult));
        return new ApiResultSet(pageResult);
    }


    @Override
    public ApiResultSet saveOrUpdate(@RequestBody @Validated HumanEvidenceDeviceMgt humanEvidenceDeviceMgt) {
        HumanEvidenceDeviceMgt resObj = this.humanEvidenceDeviceMgtManager.saveOrUpdate(humanEvidenceDeviceMgt);
        log.info("受理辖区信息新增/更新成功：{}", JSON.toJSONString(resObj));
        return new ApiResultSet(resObj);
    }

    @Override
    public ApiResultSet delete(String ids) {
        this.humanEvidenceDeviceMgtManager.delete(ids);
        log.info("删除成功：{}", ids);
        return new ApiResultSet(ids);
    }


    @Override
    public ApiResultSet getOne(String id) {
        HumanEvidenceDeviceMgt humanEvidenceDeviceMgt = this.humanEvidenceDeviceMgtManager.getOne(id);
        log.info("详情获取成功：{}", JSON.toJSONString(humanEvidenceDeviceMgtManager));
        return new ApiResultSet(humanEvidenceDeviceMgt);
    }


    @Override
    public ApiResultSet validDeviceId(String deviceId, String oid) {
        Boolean flag = this.humanEvidenceDeviceMgtManager.validDeviceId(deviceId, oid);
        return new ApiResultSet(flag);
    }


    @Override
    public ApiResultSet getDeviceIdByMac(HttpServletRequest request) {
        String ipAddress = IpUtil.getIpAddr(request);
        String deviceId = this.humanEvidenceDeviceMgtManager.getDeviceIdByIpAddress(ipAddress);
        return new ApiResultSet(deviceId);
    }

}
