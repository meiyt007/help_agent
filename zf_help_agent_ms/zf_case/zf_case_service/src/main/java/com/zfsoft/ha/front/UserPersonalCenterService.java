package com.zfsoft.ha.front;


import com.zfsoft.ha.data.HaWorkUser;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.utils.feign.ProcessFeignCalledResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;


/**个人中心
 * @author zhaobf
 * @version 1.0
 * @date 2022-07-22 9:52
 */
@RequestMapping("/ha/user")
public interface UserPersonalCenterService {
    /**
     * 更新密码
     * @param oldPass 旧密码
     * @param newPass 新密码
     * @param newPassConfirm 再次输入新密码
     * @param request 用于token验证
     * @return
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/updatePass", method = {RequestMethod.POST})
    ApiResultSet<Map<String,String>> updatePass(
            @RequestParam(value = "oldPass", required = true) String oldPass,
            @RequestParam(value = "newPass", required = true) String newPass,
            @RequestParam(value = "newPassConfirm", required = true) String newPassConfirm,
            HttpServletRequest request);

    /**
     * 更新user账户信息
     * @return 用于token验证
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/updateInfo", method = {RequestMethod.POST})
    ApiResultSet<Map<String,String> > updateInfo(HaWorkUser haWorkUser);

}
