package com.zfsoft.ha.manager;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.utils.feign.ProcessFeignCalledResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 事项授权或帮代办用户授权接口
 *
 * @author yupeng
 * @version 1.0
 * @date 2022年08月01日 15:37:52
 */
@RequestMapping("/serviceRegistrar")
public interface HaServiceRegistrarService {

    /**
     * 查询事项授相关的帮代办人员
     *
     * @param serviceOid 事项oid
     * @return ApiResultSet 接口查询结果集
     * @author yupeng
     * @date 2022年08月01 16:54:34
     */
    @ProcessFeignCalledResult
    @GetMapping(value = "/getAllHelpWorkUserList")
    ApiResultSet getAllHelpWorkUserList(String serviceOid);

    /**
     * 保存或修改事项授权给帮代办人员
     *
     * @param jsonObject 授权信息
     * @return 保存结果信息
     * @author yupeng
     * @date 2022年08月03 14:42:20
     */
    @ProcessFeignCalledResult
    @PostMapping(value = "/saveOrUpdateServiceRegistrar")
    ApiResultSet saveOrUpdateServiceRegistrar(@RequestBody JSONObject jsonObject);


    /**
     * 查询所有的分组
     *
     * @return 分组集合
     * @author yupeng
     * @date 2022年08月05 09:28:12
     */
    @ProcessFeignCalledResult
    @GetMapping(value = "/queryAllGroup")
    ApiResultSet queryAllGroup();


    /**
     * 查询授权帮代办人员列表
     *
     * @param groupName    分组名
     * @param postName     分组职务
     * @param workUserName 帮代办人员姓名
     * @param pageNum      分页参数
     * @param pageSize     分页参数
     * @return 分页列表
     * @author yupeng
     * @date 2022年08月05 10:21:19
     */
    @ProcessFeignCalledResult
    @PostMapping(value = "/queryWorkUserRegistrarPage")
    ApiResultSet queryWorkUserRegistrarPage(String groupName, String postName, String workUserName, Integer pageNum, Integer pageSize);


    /**
     * 保存或修改帮代办人员授权
     *
     * @param jsonArray 授权信息
     * @return 保存结果信息
     * @author yupeng
     * @date 2022年08月05 17:24:19
     */
    @ProcessFeignCalledResult
    @PostMapping(value = "/saveOrUpdateWorkUserRegistrar")
    ApiResultSet saveOrUpdateWorkUserRegistrar(@RequestBody JSONArray jsonArray);


    /**
     * 查询帮代办授权中的事项列表
     *
     * @param serviceName   事项名称
     * @param implementCode 实施编码
     * @param workUserId    帮代办表主键id
     * @param pageNum       分页参数
     * @param pageSize      分页参数
     * @return 事项结果集
     * @author yupeng
     * @date 2022年08月08 10:11:54
     */
    @ProcessFeignCalledResult
    @PostMapping(value = "/queryServiceInfoPage")
    ApiResultSet queryServiceInfoPage(String serviceName, String implementCode, String workUserId, Integer pageNum, Integer pageSize);


    /**
     * 查询授权事项tree
     *
     * @param workUserId 帮代办用户id
     * @return 授权事项tree
     * @author yupeng
     * @date 2022年08月10 14:36:14
     */
    @ProcessFeignCalledResult
    @GetMapping(value = "/queryServiceTree")
    ApiResultSet queryServiceTree(String workUserId);

    /**
     * 批量保存或修改帮代办授权
     *
     * @param jsonObject 授权信息
     * @return 保存结果
     * @author yupeng
     * @date 2022年08月11 15:07:01
     */
    @ProcessFeignCalledResult
    @PostMapping(value = "/bathSaveOrUpdateWorkUserRegistrar")
    ApiResultSet bathSaveOrUpdateWorkUserRegistrar(@RequestBody JSONObject jsonObject);

}
