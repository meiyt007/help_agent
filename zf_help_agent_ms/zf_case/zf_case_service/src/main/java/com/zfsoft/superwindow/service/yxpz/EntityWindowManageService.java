package com.zfsoft.superwindow.service.yxpz;

import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.utils.feign.ProcessFeignCalledResult;
import com.zfsoft.superwindow.data.yxpz.EntityWindowManage;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author: kkfan
 * @create: 2020-10-26 10:33:56
 * @description:    实体窗口管理控制层接口
 */
@RequestMapping(value = "/entityWindowManage")
public interface EntityWindowManageService {
    /**
     * 查询实体窗口列表
     * @param entityWindowManage
     * @param pageNum
     * @param pageSize
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/pageList")
    ApiResultSet queryPageListWindow(EntityWindowManage entityWindowManage,
                                     @RequestParam(value = "pageNum", required = false, defaultValue = "1") Integer pageNum,
                                     @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize);

    /**
     * 保存/更新实体窗口
     * @param entityWindowManage
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/saveOrUpdate")
    ApiResultSet saveOrUpdateWindow(@RequestBody @Validated EntityWindowManage entityWindowManage);

    /**
     * 逻辑删除实体窗口信息 支持批量删除
     * @param ids
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/delete")
    ApiResultSet deleteWindow(@RequestParam(value = "ids") String ids);

    /**
     * 获取实体窗口信息详情
     * @param id
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/getOne")
    ApiResultSet getOneWindow(@RequestParam(value = "id") String id);

    @ProcessFeignCalledResult
    @RequestMapping(value = "/queryEntityWindowByWindoNo")
    ApiResultSet queryEntityWindowByWindoNumber(@RequestParam(value = "windoNo") String windoNo);


    @ProcessFeignCalledResult
    @RequestMapping(value = "/querAllConfigEntityWindow")
    ApiResultSet querAllConfigEntityWindow();


    @ProcessFeignCalledResult
    @RequestMapping(value = "/queryEntityWindowByUserOid")
    ApiResultSet queryWindowByUserOid(@RequestParam(value = "userOid") String userOid);

    @ProcessFeignCalledResult
    @RequestMapping(value = "/getntityWindowByUserOid")
    ApiResultSet<EntityWindowManage> getntityWindowByUserOid(@RequestParam(value = "userOid") String userOid);

}
