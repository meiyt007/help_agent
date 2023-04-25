package com.zfsoft.ha.front.controller;

import com.zfsoft.ha.data.HaLoginUserInfo;
import com.zfsoft.ha.data.HaOnline;
import com.zfsoft.ha.data.requestData.HaOnlineRequestData;
import com.zfsoft.ha.front.HaOnlineService;
import com.zfsoft.ha.managers.HaOnlineManager;
import com.zfsoft.ha.util.HaLoginUserHolder;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.platform.common.security.data.CurrentLoginUser;
import com.zfsoft.platform.common.security.data.CurrentLoginUserHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @Description //在线时长表控制层
 * @Author: Wangyh
 * @Date: 2022/8/12 11:08
 */
@RestController
@Slf4j
public class HaOnlineController implements HaOnlineService {
    /**
     * 用户在线时长表manager层
     */
    @Resource
    private HaOnlineManager haOnlineManager;


    /**
     * @description:  查询在线时长表分页信息列表
     * @param haOnlineRequestData 在线时长表请求封装实体类
     * @param pageNum 页码
     * @param pageSize 当前页展示数量
     * @author: wangyh
     * @Date: 2022/8/12
     **/
    @Override
    public ApiResultSet<PageResult<HaOnline>> queryOnlineServiceWithPage(HaOnlineRequestData haOnlineRequestData, Integer pageNum, Integer pageSize) throws Exception {

        log.info("查询在线时长表分页信息列表，haOnlineRequestData:{},pageNum:{},pageSize:{}",haOnlineRequestData,pageNum,pageSize);

        ApiResultSet<PageResult<HaOnline>> apiResultSet = new ApiResultSet<>();
        PageResult<HaOnline> pageResult=null;
        if(haOnlineRequestData != null && haOnlineRequestData.getWorkUserId() !=null && !haOnlineRequestData.getWorkUserId().equals("") ){
            pageResult = haOnlineManager.queryOnlineServiceWithPage(haOnlineRequestData,pageNum,pageSize);
            log.debug("pageResult结果集：pageResult:{}",pageResult);

        }else{
            return new ApiResultSet<>(500,"没有当前用户信息");
        }
        return apiResultSet.ok("接口调用成功",pageResult);
    }
}
