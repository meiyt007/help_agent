package com.zfsoft.superwindow.service.sso;

import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.superwindow.data.sso.LegalUser;
import org.springframework.web.bind.annotation.*;

/**
 * @Author: qiaol
 * @Description: 统一身份认证-法人
 * @Date: 2022/5/31 21:50
 */
@RequestMapping("/sso/legal")
public interface LegalUserService {

    /**
     *  登录
     * @param user
     * @return token
     */
    @PostMapping("/login")
    ApiResultSet<String> login(@RequestBody LegalUser user);

    /**
     * @description 根据token获取法人信息
     * @param token
     * @author meiyt
     * @date 2022/6/7
     * @return
     **/
    @GetMapping("/getLegalUserByToken")
    ApiResultSet<LegalUser> getLegalUserByToken(@RequestHeader("access_token") String token);

    /**
     *  根据身份证号获取用户信息
     * @param corporateCode
     * @return
     */
    @GetMapping("/getLegalUserByCorporateCode/{corporateCode}")
    ApiResultSet<LegalUser> getLegalUserByCorporateCode(@PathVariable("corporateCode") String corporateCode);
}
