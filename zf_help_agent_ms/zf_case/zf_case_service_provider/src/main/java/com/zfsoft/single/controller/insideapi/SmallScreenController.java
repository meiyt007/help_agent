package com.zfsoft.single.controller.insideapi;

import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.single.service.insideapi.smallScreen.ISmallScreenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


@RestController
@Slf4j
@CacheConfig(cacheNames = {"smallScreen"})
public class SmallScreenController implements ISmallScreenService {

    @Resource
    private RedisTemplate redisTemplate;

    @Override
    public ApiResultSet<Boolean> pushCallBackInfo(String userOid, String returnFlag) {
        boolean flag = false;
        String userId = (String) redisTemplate.opsForValue().get("smallScreen:userOid="+userOid);
        if (userId != null) {
            redisTemplate.delete("smallScreen:userOid="+userOid);
        }
        //1 无误
        if ("1".equals(returnFlag)) {
            //1信息确认成功 2 信息评价成功
            redisTemplate.opsForValue().set("smallScreen:userOid="+userOid, "1");
            flag = true;
        }
        // 0有误
        if ("0".equals(returnFlag)) {
            //0确认信息有误
            redisTemplate.opsForValue().set("smallScreen:userOid="+userOid, "0");
        }
        ApiResultSet<Boolean> apiResultSet = new ApiResultSet<Boolean>();
        apiResultSet.setData(flag);
        return apiResultSet;
    }

    @Override
    public ApiResultSet<String> getCallBackInfo(String userOid) {
        String userId = (String) redisTemplate.opsForValue().get("smallScreen:userOid="+userOid);
        if (userId != null) {
            redisTemplate.delete("smallScreen:userOid="+userOid);
        }
        ApiResultSet<String> apiResultSet = new ApiResultSet<String>();
        apiResultSet.setData(userId);
        return apiResultSet;
    }


}
