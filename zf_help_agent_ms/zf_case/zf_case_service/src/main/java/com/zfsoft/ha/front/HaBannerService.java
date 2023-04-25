package com.zfsoft.ha.front;

import com.zfsoft.ha.data.HaWorkUser;
import com.zfsoft.platform.common.data.ApiResultSet;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Description //banner接口
 * @Author: Wangyh
 * @Date: 2022/7/29 10:15
 */
@RequestMapping("/ha/banner")
public interface HaBannerService {

    /**
     * @param //Token在header中
     * @description: 获取banner列表
     * @author: Wangyh
     * @Date: 2022/7/18 21:10
     * @return ApiResultSet 获取banner列表详情
     **/
    @RequestMapping(value = "/list", method = {RequestMethod.GET})
    ApiResultSet queryMessageList() throws Exception;

}
