package com.zfsoft.superwindow.interapi.query.manager.base;

import com.zfsoft.superwindow.service.interapi.service.InterApiQueryStrategy;
import org.springframework.stereotype.Service;

/**
 * @Author: qiaol
 * @Description: 通用接口策略抽象类
 * @Date: 2022/5/16 17:44
 */
@Service
public abstract class AbstractInterApiQueryStrategy implements InterApiQueryStrategy {

   public String getApiCode() {
       return null;
   }

    public void register() {
        InterApiQueryStrategyContext.registerStrategy(getApiCode(), this);
    }
}
