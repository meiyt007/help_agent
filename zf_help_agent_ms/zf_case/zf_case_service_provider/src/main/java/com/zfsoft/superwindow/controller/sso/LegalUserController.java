package com.zfsoft.superwindow.controller.sso;

import cn.hutool.core.util.StrUtil;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.utils.validate.ResultInfoException;
import com.zfsoft.superwindow.data.sso.LegalUser;
import com.zfsoft.superwindow.manager.sso.LegalUserManager;
import com.zfsoft.superwindow.service.sso.LegalUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @Author: qiaol
 * @Description: 统一身份认证-法人
 * @Date: 2022/5/31 21:55
 */
@Slf4j
@RestController
public class LegalUserController implements LegalUserService {

    @Resource
    private LegalUserManager legalUserManager;

    @Override
    public ApiResultSet<String> login(LegalUser user) {
        if(null == user) {
            throw new ResultInfoException("法人信息不能为空");
        }
        String token = legalUserManager.login(user);
        return new ApiResultSet<>(token);
    }

    @Override
    public ApiResultSet<LegalUser> getLegalUserByToken(String token) {
        if(StrUtil.isEmpty(token)) {
            throw new ResultInfoException("token信息不能为空");
        }
        LegalUser legalUser = legalUserManager.getLegalUserByToken(token);
        return new ApiResultSet<>(legalUser);
    }

    @Override
    public ApiResultSet<LegalUser> getLegalUserByCorporateCode(String corporateCode) {
        LegalUser legalUser = legalUserManager.getLegalUserByCorporateCode(corporateCode);
        return new ApiResultSet<>(legalUser);
    }

}
