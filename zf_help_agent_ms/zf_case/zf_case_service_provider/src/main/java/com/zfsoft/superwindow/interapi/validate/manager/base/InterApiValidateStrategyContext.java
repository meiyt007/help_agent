package com.zfsoft.superwindow.interapi.validate.manager.base;

import com.zfsoft.superwindow.service.interapi.service.InterApiValidateStrategy;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: qiaol
 * @Description: 通用接口-策略上下文获取
 * @Date: 2022/5/16 17:46
 */
public class InterApiValidateStrategyContext {

    private static final Map<String, InterApiValidateStrategy> REGISTER_MAP = new HashMap<>(128);

    public static void registerStrategy(String apiCode, InterApiValidateStrategy apiStrategy) {
        REGISTER_MAP.putIfAbsent(apiCode, apiStrategy);
    }

    public static InterApiValidateStrategy getStrategy(String apiCode) {
        return REGISTER_MAP.get(apiCode);
    }
}
