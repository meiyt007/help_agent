package com.zfsoft.microservice.platform.service.sys;

import com.zfsoft.microservice.platform.data.sys.SysHoliday;
import com.zfsoft.platform.common.data.ApiResultSet;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Set;

/**
 * @ClassName SysHolidayService
 * @Description 节假日组件服务定义接口
 * @Author wuxx
 * @Date 2020-10-20 11:33
 * @Version V1.0
 **/

@RequestMapping("/security/holiday")
public interface SysHolidayService {

    /**
     * 增加一个新节假日
     *
     * @param sysHoliday 新节假日
     * @return
     */
    @RequestMapping(value = "/save", method = {RequestMethod.POST})
    ApiResultSet saveSysHoliday(SysHoliday sysHoliday);

    /**
     * @param oid 节假日实体类主键
     * @description: 获取节假日的信息
     * @author: wuxx
     * @Date: 2020-10-20 10:14
     **/
    @RequestMapping(value = "/getOne/{oid}", method = {RequestMethod.GET})
    ApiResultSet<SysHoliday> getSysHolidayById(@PathVariable("oid") Long oid);


    /**
     * @param oid 节假日实体类主键
     * @description: 节假日的信息的删除
     * @author: wuxx
     * @Date: 2020-10-20 10:14
     **/
    @RequestMapping(value = "/delete/{oid}", method = {RequestMethod.POST})
    ApiResultSet<Integer> deleteSysHolidayById(@PathVariable("oid") Long oid);

    /**
     * @param holidayDate 节假日时间
     * @param holidayType 节假日类型
     * @description: 查询节假日的信息列表
     * @author: wuxx
     * @Date: 2020-10-20 10:14
     **/
    @RequestMapping(value = "/page", method = {RequestMethod.GET})
    ApiResultSet querySysHolidayWithPage(@RequestParam(value="holidayDate",required=false)String holidayDate,
                                         @RequestParam(value="holidayType",required=false)String holidayType,
                                         @RequestParam(value="pageNum",required=false)Integer pageNum,
                                         @RequestParam(value="pageSize",required=false)Integer pageSize);

    /**
     * @description: 根据节假日类型获取节假日集合
     * @author: wuxx
     * @Date: 2021/2/1 15:16
     **/
    @RequestMapping(value = "/querySysHolidayMapByHolidayType", method = {RequestMethod.GET})
    ApiResultSet<Set<String>> querySysHolidaySetByHolidayType(@RequestParam("holidayType") String holidayType);

    /**
     * @description: 根据年份和节假日类型获取节假日列表
     * @param year 年份 默认当年
     * @param holidayType 节假日类型
     * @author: wuxx
     * @Date: 2021/5/17 9:51
     **/
    @RequestMapping(value = "/querySysHolidayListByYear", method = {RequestMethod.GET})
    ApiResultSet<List<SysHoliday>> querySysHolidayListByYear(@RequestParam(value = "year",required = false) String year,
                                                         @RequestParam(value = "holidayType",required = false) String holidayType);

}
