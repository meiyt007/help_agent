package com.zfsoft.superwindow.controller.sso;

import cn.hutool.core.util.StrUtil;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.utils.validate.ResultInfoException;
import com.zfsoft.superwindow.data.sso.NatureUser;
import com.zfsoft.superwindow.manager.sso.NatureUserManager;
import com.zfsoft.superwindow.service.sso.NatureUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @Author: qiaol
 * @Description: 统一身份认证-自然人
 * @Date: 2022/5/31 21:55
 */
@Slf4j
@RestController
public class NatureUserController implements NatureUserService {

    @Resource
    private NatureUserManager natureUserManager;

    @Override
    public ApiResultSet<String> login(NatureUser user) {
        if(null == user) {
            throw new ResultInfoException("自然人信息不能为空");
        }
        String token = natureUserManager.login(user);
        return new ApiResultSet<>(token);
    }

    @Override
    public ApiResultSet<NatureUser> getNatureUserByToken(String token) {
        if(StrUtil.isEmpty(token)) {
            throw new ResultInfoException("token信息不能为空");
        }
        NatureUser natureUser = natureUserManager.getNatureUserByToken(token);
        return new ApiResultSet<>(natureUser);
    }

    @Override
    public ApiResultSet<NatureUser> getNatureUserByCardNo(String cardNo) {

        NatureUser natureUser = natureUserManager.getUserByCardNo(cardNo);
        return new ApiResultSet<>(natureUser);
    }
}
