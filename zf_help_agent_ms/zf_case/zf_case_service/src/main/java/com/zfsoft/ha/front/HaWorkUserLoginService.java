package com.zfsoft.ha.front;

import com.zfsoft.ha.data.HaLoginUserInfo;
import com.zfsoft.ha.data.HaWorkUser;
import com.zfsoft.ha.data.responseData.HaWorkUserResponseData;
import com.zfsoft.ha.data.vo.HaWorkUserVo;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.utils.feign.ProcessFeignCalledResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @description: 帮代办人员登录相关接口
 * @author: kangax
 * @date: 2022-07-28 10:01
 */
@RequestMapping("/ha/")
public interface HaWorkUserLoginService {

    /**
     * 获取帮代办人员登录验证码
     *
     * @param request  请求request
     * @param response 请求response
     */
    @GetMapping(value = "login/getLoginCheckCode", produces = "image/jpeg")
    void getLoginCode(HttpServletRequest request, HttpServletResponse response);


    /**
     * @param haType
     * @param account   登录用户名
     * @param pcode  密码
     * @param checkCode 验证码
     * @description: 帮代办人员登录
     * @author: wangyh
     * @Date: 2022年7月25日13:04:17
     **/
    @ProcessFeignCalledResult
    @RequestMapping(value = "login/loginByAccount", method = {RequestMethod.POST})
    ApiResultSet<HaWorkUserResponseData> loginByAccount(HttpServletRequest request,
                                                        @RequestParam(value = "account", required = true) String account,
                                                        @RequestParam(value = "pcode", required = true) String pcode,
                                                        @RequestParam(value = "checkCode", required = false) String checkCode,
                                                        @RequestParam(value = "haType", required = true) String haType);

    /**
     * @description: 获取登录人员信息
     * @return: HaLoginUserInfo 登录人员信息实体
     * @author: kangax
     * @date: 2022-07-28 10:50
     */
    @ProcessFeignCalledResult
    @RequestMapping(value = "/getUserInfo", method = {RequestMethod.GET})
    ApiResultSet<HaWorkUserVo> getLoginInfo();

    /**
     * @param  // token  在header中
     * @description: 退出登录
     * @author: wangyh
     * @Date: 2022-08-2 10:50
     * @return ApiResultSet  获取退出登录详细信息
     **/
    @ProcessFeignCalledResult
    @RequestMapping(value = "/logout", method = {RequestMethod.GET})
    ApiResultSet logout() throws Exception;

    /**
     * @param  // token  在header中
     * @description: 在线状态
     * @author: wangyh
     * @Date: 2022-08-2 10:50
     * @return ApiResultSet  获取在线状态详细信息
     **/
    @ProcessFeignCalledResult
    @RequestMapping(value = "/login/online", method = {RequestMethod.GET})
    ApiResultSet online() throws Exception;
}
