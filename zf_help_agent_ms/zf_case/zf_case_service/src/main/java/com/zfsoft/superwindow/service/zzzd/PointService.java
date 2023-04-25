package com.zfsoft.superwindow.service.zzzd;

import com.alibaba.fastjson.JSONArray;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.platform.utils.feign.ProcessFeignCalledResult;
import com.zfsoft.superwindow.data.zzzd.PointInfo;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Author ChangSheng
 * @Date 9:10 2022/5/28
 * @Description 点位管理
 **/
@RequestMapping("/point")
public interface PointService {
    /**
     * 查看列表分页
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/page", method = {RequestMethod.POST})
    ApiResultSet<PageResult<PointInfo>> queryPointInfoPage(PointInfo pointInfo,
                                                           @RequestParam(value = "pageNum") Integer pageNum,
                                                           @RequestParam(value = "pageSize") Integer pageSize);

    /**
     * 查看单个详细
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/getOne", method = {RequestMethod.POST})
    ApiResultSet<PointInfo> getPointInfoById(@RequestParam("id") String id);

    /**
     * 新增
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/save", method = {RequestMethod.POST})
    ApiResultSet<PointInfo> savePointInfo(@RequestBody PointInfo terminalInfo);


    /**
     * 逻辑删除
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/delete", method = {RequestMethod.POST})
    ApiResultSet<Integer> deletePointInfoById(@RequestParam("id") String id);

    /**
     * 根据地址获取坐标
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/getCoordinate", method = {RequestMethod.POST})
    ApiResultSet<String> getCoordinate(@RequestParam("address") String address);

    /**
     * 获取点位map -- 对不起啦，这是给终端管理用的
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/getMap", method = {RequestMethod.POST})
    ApiResultSet<JSONArray> getCoordinateMap();
}
