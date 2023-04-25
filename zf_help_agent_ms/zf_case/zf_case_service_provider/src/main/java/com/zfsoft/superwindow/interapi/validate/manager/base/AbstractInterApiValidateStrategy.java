package com.zfsoft.superwindow.interapi.validate.manager.base;

import com.zfsoft.superwindow.service.interapi.service.InterApiValidateStrategy;
import org.springframework.stereotype.Service;

/**
 * @Author: qiaol
 * @Description: 通用接口策略抽象类
 * @Date: 2022/5/16 17:44
 */
@Service
public abstract class AbstractInterApiValidateStrategy implements InterApiValidateStrategy {

   public String getApiCode() {
       return null;
   }

    public void register() {
        InterApiValidateStrategyContext.registerStrategy(getApiCode(), null);
    }
}
