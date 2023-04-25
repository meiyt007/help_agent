package com.zfsoft.ha.manager.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zfsoft.cases.feign.SysOrganFeginService;
import com.zfsoft.cases.util.StringUtil;
import com.zfsoft.ha.data.HaWorkGroup;
import com.zfsoft.ha.data.vo.HaServiceRegistrarVo;
import com.zfsoft.ha.data.vo.HaServiceVo;
import com.zfsoft.ha.data.vo.HaWorkUserRegistrarVo;
import com.zfsoft.ha.manager.HaServiceRegistrarService;
import com.zfsoft.ha.managers.HaServiceRegistrarManger;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.service.manager.sxService.SxServiceManager;
import com.zfsoft.service.sxService.data.SxService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 事项授权或帮代办用户授权controller
 *
 * @author yupeng
 * @version 1.0
 * @date 2022年08月01日 15:37:52
 */
@RestController
@Slf4j
public class HaServiceRegistrarController implements HaServiceRegistrarService {


    /**
     * 事项授权或帮代办人员授权manger
     */
    @Resource
    private HaServiceRegistrarManger haServiceRegistrarManger;

    /**
     * 实施清单实现manger
     */
    @Resource
    private SxServiceManager sxServiceManager;

    /**
     * 查询事项授相关的帮代办人员
     *
     * @return ApiResultSet 接口查询结果集
     * @author yupeng
     * @date 2022年08月01 16:54:34
     */
    @Override
    public ApiResultSet getAllHelpWorkUserList(String serviceOid) {
        log.info("查询事项授权帮代办人员信息serviceOid：{}", serviceOid);
        return haServiceRegistrarManger.getAllHelpWorkUserList(serviceOid);
    }

    /**
     * 保存或修改事项授权给帮代办人员
     *
     * @param jsonObject 授权信息
     * @return 保存结果信息
     * @author yupeng
     * @date 2022年08月03 14:42:20
     */
    @Override
    public ApiResultSet saveOrUpdateServiceRegistrar(JSONObject jsonObject) {
        log.info("保存或修改事项授权给帮代办人员：{}", jsonObject);
        String result = haServiceRegistrarManger.saveOrUpdateServiceRegistrar(jsonObject);
        if (StringUtil.isEmpty(result)) {
            return ApiResultSet.ok();
        } else {
            return new ApiResultSet(ApiResultSet.PARAM_VALID_ERROR, result);
        }
    }


    /**
     * 查询所有的分组
     *
     * @return 分组集合
     * @author yupeng
     * @date 2022年08月05 09:28:12
     */
    @Override
    public ApiResultSet queryAllGroup() {
        log.info("查询所有的分组");
        List<HaWorkGroup> groupList = haServiceRegistrarManger.getAllGroup();
        return ApiResultSet.ok(groupList);
    }


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
    @Override
    public ApiResultSet queryWorkUserRegistrarPage(String groupName, String postName, String workUserName, Integer pageNum, Integer pageSize) {
        log.info("查询授权帮代办人员列表，groupName：{}，postName：{}，account：{}，pageNum：{}，pageSize：{}", groupName, postName, workUserName, pageNum, pageSize);
        PageResult<HaWorkUserRegistrarVo> WorkUserRegistrarPage = haServiceRegistrarManger.queryWorkUserRegistrarPage(groupName, postName, workUserName, pageNum, pageSize);
        return ApiResultSet.ok(WorkUserRegistrarPage);
    }


    /**
     * 保存或修改帮代办人员授权
     *
     * @param jsonArray 授权信息
     * @return 保存结果信息
     * @author yupeng
     * @date 2022年08月05 17:24:19
     */
    @Override
    public ApiResultSet saveOrUpdateWorkUserRegistrar(JSONArray jsonArray) {
        log.info("保存帮代办人员授权信:{}", jsonArray);
        Integer result = haServiceRegistrarManger.saveOrUpdateWorkUserRegistrar(jsonArray);
        if (result == null) {
            return ApiResultSet.ok();
        } else {
            return new ApiResultSet(ApiResultSet.PARAM_VALID_ERROR, "保存失败，请重新操作");
        }
    }


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
     * *@date 2022年08月08 10:11:54
     */
    @Override
    public ApiResultSet queryServiceInfoPage(String serviceName, String implementCode, String workUserId, Integer pageNum, Integer pageSize) {
        log.info("查询帮代办授权事项列表,serviceName：{}，implementCode：{}，workUserId：{}，pageNum：{}，pageSize：{}", serviceName, implementCode, workUserId, pageNum, pageSize);
        SxService sxService = new SxService();
        sxService.setServiceStatus(Short.valueOf("3"));
        sxService.setImplementCode(implementCode);
        sxService.setServiceName(serviceName);
        PageResult<SxService> sxServicePage = sxServiceManager.querySxServiceWithPage(sxService, pageNum, pageSize);
        PageResult<HaServiceVo> HaServiceVoPage = haServiceRegistrarManger.queryServiceInfoPage(sxServicePage, workUserId);
        return ApiResultSet.ok(HaServiceVoPage);
    }

    /**
     * 查询授权事项tree
     *
     * @param workUserId 帮代办用户id
     * @return 授权事项tree
     * @author yupeng
     * @date 2022年08月10 14:36:14
     */
    public ApiResultSet queryServiceTree(String workUserId) {
        log.info("查询授权事项tree，workUserId：{}", workUserId);
        SxService sxService = new SxService();
        sxService.setServiceStatus(Short.valueOf("3"));
        List<SxService> sxServiceList = sxServiceManager.getSxServicList(sxService);
        HaServiceRegistrarVo haServiceRegistrarVo = haServiceRegistrarManger.queryServiceTree(sxServiceList, workUserId);
        return new ApiResultSet<>(haServiceRegistrarVo);
    }

    /**
     * 批量保存或修改帮代办授权
     *
     * @param jsonObject 授权信息
     * @return 保存结果
     * @author yupeng
     * @date 2022年08月11 15:07:01
     */
    @Override
    public ApiResultSet bathSaveOrUpdateWorkUserRegistrar(JSONObject jsonObject) {
        Integer result = haServiceRegistrarManger.bathSaveOrUpdateWorkUserRegistrar(jsonObject);
        if (result == null) {
            return ApiResultSet.ok();
        } else {
            return new ApiResultSet(ApiResultSet.PARAM_VALID_ERROR, "保存失败，请重新操作");
        }
    }

}
