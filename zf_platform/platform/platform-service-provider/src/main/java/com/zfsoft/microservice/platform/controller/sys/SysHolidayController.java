package com.zfsoft.microservice.platform.controller.sys;

import com.zfsoft.microservice.platform.data.sys.SysHoliday;
import com.zfsoft.microservice.platform.manager.sys.SysHolidayManager;
import com.zfsoft.microservice.platform.service.sys.SysHolidayService;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.data.BaseStaticParameter;
import com.zfsoft.platform.common.data.PageResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @ClassName SysHolidayController
 * @Description 节假日管理的实现类
 * @Author wuxx
 * @Date 2020-10-20 10:30
 * @Version V1.0
 **/
@RestController
@Slf4j
public class SysHolidayController implements SysHolidayService{

    @Resource
    private SysHolidayManager sysHolidayManager;
    /**
     * @param oid 节假日主键
     * @description: 初始化节假日管理的信息
     * @author: wuxx
     * @Date: 2020-10-20 10:14
     **/
    @RequestMapping(value = {"/init","/init/{oid}"}, method = {RequestMethod.GET})
    public ApiResultSet initSysHoliday(@PathVariable(value="oid",required=false) Long oid) {
        if (null != oid) {
            SysHoliday sysHoliday = sysHolidayManager.getSysHolidayById(oid);
            return new ApiResultSet<>(sysHoliday);
        }
        return new ApiResultSet<>();
    }

    /**
     * @param sysHoliday 节假日实体类
     * @description: 节假日的新增或者修改
     * @author: wuxx
     * @Date: 2020-10-20 10:14
     **/
    public ApiResultSet<SysHoliday> saveSysHoliday(@RequestBody SysHoliday sysHoliday) {
        sysHolidayManager.saveSysHoliday(sysHoliday);
        ApiResultSet<SysHoliday> apiResultSet = new ApiResultSet<>();
        apiResultSet.setData(sysHoliday);
        return apiResultSet;
    }

    /**
     * @param oid 节假日实体类主键
     * @description: 节假日的信息的删除
     * @author: wuxx
     * @Date: 2020-10-20 10:14
     **/
    public ApiResultSet<Integer> deleteSysHolidayById(@PathVariable("oid") Long oid) {
        int rows = sysHolidayManager.deleteSysHolidayById(oid);
        ApiResultSet<Integer> apiResultSet = new ApiResultSet<Integer>();
        apiResultSet.setData(rows);
        return apiResultSet;
    }

    /**
     * @param oid 节假日实体类主键
     * @description: 获取节假日的信息
     * @author: wuxx
     * @Date: 2020-10-20 10:14
     **/
    public ApiResultSet<SysHoliday> getSysHolidayById(@PathVariable("oid") Long oid) {
        SysHoliday sysHoliday = sysHolidayManager.getSysHolidayById(oid);
        ApiResultSet<SysHoliday> apiResultSet = new ApiResultSet<SysHoliday>();
        apiResultSet.setData(sysHoliday);
        return apiResultSet;
    }

    /**
     * @param holidayDate 节假日时间
     * @param holidayType 节假日类型
     * @description: 查询节假日的信息列表
     * @author: wuxx
     * @Date: 2020-10-20 10:14
     **/
    public ApiResultSet querySysHolidayWithPage(String holidayDate,String holidayType, Integer pageNum, Integer pageSize) {
        SysHoliday sysHoliday = new SysHoliday();
        sysHoliday.setHolidayDate(holidayDate);
        sysHoliday.setHolidayType(holidayType);
        PageResult<SysHoliday> pageResult = sysHolidayManager.querySysHolidayWithPage(sysHoliday, pageNum, pageSize);
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("pageResult",pageResult);
        resultMap.put("sysHoliday", BaseStaticParameter.HOLIDAY_DATE_MAP);
        return new ApiResultSet<>(resultMap);
    }

    /**
     * @description: 根据节假日类型获取节假日集合
     * @author: wuxx
     * @Date: 2021/2/1 15:16
     **/
    @Override
    public ApiResultSet<Set<String>> querySysHolidaySetByHolidayType(String holidayType) {
        Set<String> holidayTypeSet = sysHolidayManager.querySysHolidaySetByHolidayType(holidayType);
        return new ApiResultSet<>(holidayTypeSet);
    }

    /**
     * @description: 根据年份和节假日类型获取节假日列表
     * @param year 年份 默认当年
     * @param holidayType 节假日类型
     * @author: wuxx
     * @Date: 2021/5/17 9:51
     **/
    @Override
    public ApiResultSet<List<SysHoliday>> querySysHolidayListByYear(String year, String holidayType) {
        List<SysHoliday> sysHolidayList = sysHolidayManager.querySysHolidayListByYear(year, holidayType);
        return new ApiResultSet<>(sysHolidayList);
    }

}
