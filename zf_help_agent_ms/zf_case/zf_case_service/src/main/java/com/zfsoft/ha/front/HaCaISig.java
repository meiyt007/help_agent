package com.zfsoft.ha.front;

import com.zfsoft.platform.common.data.ApiResultSet;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @Description //视频咨询秘钥
 * @Author: Wangyh
 * @Date: 2023/3/8 10:05
 */
@RequestMapping("/ha/caSig")
public interface HaCaISig {
    /**
     * @param identifier 用户身份标识
     * @param room     房间号
     * @param expire 过期时间,单位秒,默认 1天
     * @return string 签名字符串
     * @throws Exception
     * @throws \Exception
     */
    @RequestMapping(value = "/getSig", method = {RequestMethod.GET})
    ApiResultSet<String> getSig( String identifier,String room, int expire)throws Exception ;

}
