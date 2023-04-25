package com.zfsoft.ha.front.controller;

import com.zfsoft.ha.front.HaCaISig;
import com.zfsoft.ha.util.CalSig;
import com.zfsoft.platform.common.data.ApiResultSet;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description //视频咨询秘钥
 * @Author: Wangyh
 * @Date: 2023/3/8 10:50
 */
@RestController
@Slf4j
public class HaCalSigController implements HaCaISig  {
    @Override
    public ApiResultSet<String> getSig(String identifier,String room, int expire) throws Exception {
        String sig = CalSig.genSign(identifier,room,expire);
        return ApiResultSet.ok("获取动态秘钥成功",sig);
    }
}
