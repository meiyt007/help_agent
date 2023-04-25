package com.zfsoft.superwindow.interapi.query.manager;

import com.zfsoft.superwindow.data.sso.LegalUser;
import com.zfsoft.superwindow.interapi.query.manager.base.AbstractInterApiQueryStrategy;
import com.zfsoft.superwindow.manager.sso.LegalUserManager;
import com.zfsoft.superwindow.service.easyquickcase.data.ResponseData;
import com.zfsoft.superwindow.service.interapi.data.ApiReqParams;
import com.zfsoft.superwindow.service.interapi.service.InterApiQueryStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Author: qiaol
 * @Description:
 * @Date: 2022/6/18 16:49
 */
@Service("interApiLegalUserManager")
@Slf4j
public class InterApiLegalUserManager extends AbstractInterApiQueryStrategy implements InterApiQueryStrategy {

    @Resource
    private LegalUserManager legalUserManager;

    public InterApiLegalUserManager() {
        register();
    }

    @Override
    public ResponseData<LegalUser> query(ApiReqParams params) {
        LegalUser legalUser = legalUserManager.getLegalUserByCorporateCode(params.getUniqueCode());
        ResponseData<LegalUser> res = new ResponseData<>();
        res.success(legalUser);
        return res;
    }

    @Override
    public String getApiCode() {
        return "legalUser";
    }
}
