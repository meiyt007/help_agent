package com.zfsoft.superwindow.service.sso;

import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.superwindow.data.sso.NatureUser;
import org.springframework.web.bind.annotation.*;

/**
 * @Author: qiaol
 * @Description: 统一身份认证-自然人
 * @Date: 2022/5/31 21:50
 */
@RequestMapping("/sso/nature")
public interface NatureUserService {

    /**
     *  登录
     * @param user
     * @return token
     */
    @PostMapping("/login")
    ApiResultSet<String> login(@RequestBody NatureUser user);

    /**
     * @description 根据token获取自然人信息
     * @param token
     * @author meiyt
     * @date 2022/6/7
     * @return
     **/
    @GetMapping("/getNatureUserByToken")
    ApiResultSet<NatureUser> getNatureUserByToken(@RequestHeader("access_token") String token);

    /**
     *  根据身份证号获取用户信息
     * @param cardNo
     * @return
     */
    @GetMapping("/getNatureUserByCardNo/{cardNo}")
    ApiResultSet<NatureUser> getNatureUserByCardNo(@PathVariable("cardNo") String cardNo);

}
