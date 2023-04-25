package com.zfsoft.superwindow.interapi.query.manager.base;

import com.zfsoft.superwindow.service.interapi.service.InterApiQueryStrategy;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: qiaol
 * @Description: 通用接口-策略上下文获取
 * @Date: 2022/5/16 17:46
 */
@Slf4j
public class InterApiQueryStrategyContext {

    private static final Map<String, InterApiQueryStrategy> REGISTER_MAP = new HashMap<>(128);

    public static void registerStrategy(String apiCode, InterApiQueryStrategy apiStrategy) {
        if (REGISTER_MAP.containsKey(apiCode)) {
            log.error(String.format("接口【%s】编码重复,【%s】注册失败！！！", apiCode, apiStrategy.getClass()));
        }
        REGISTER_MAP.putIfAbsent(apiCode, apiStrategy);
    }

    public static InterApiQueryStrategy getStrategy(String apiCode) {
        return REGISTER_MAP.get(apiCode);
    }
}
