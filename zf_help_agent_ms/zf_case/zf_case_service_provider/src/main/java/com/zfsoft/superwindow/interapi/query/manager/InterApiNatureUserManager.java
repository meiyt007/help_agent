package com.zfsoft.superwindow.interapi.query.manager;

import com.zfsoft.superwindow.data.sso.NatureUser;
import com.zfsoft.superwindow.interapi.query.manager.base.AbstractInterApiQueryStrategy;
import com.zfsoft.superwindow.manager.sso.NatureUserManager;
import com.zfsoft.superwindow.service.easyquickcase.data.ResponseData;
import com.zfsoft.superwindow.service.interapi.data.ApiReqParams;
import com.zfsoft.superwindow.service.interapi.service.InterApiQueryStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: qiaol
 * @Description:
 * @Date: 2022/6/18 16:49
 */
@Service("interApiNatureUserManager")
@Slf4j
public class InterApiNatureUserManager extends AbstractInterApiQueryStrategy implements InterApiQueryStrategy {

    @Autowired
    private NatureUserManager natureUserManager;

    public InterApiNatureUserManager() {
        register();
    }

    @Override
    public ResponseData<NatureUser> query(ApiReqParams params) {
        NatureUser natureUser = natureUserManager.getUserByCardNo(params.getUniqueCode());
        ResponseData<NatureUser> res = new ResponseData<>();
        res.success(natureUser);
        return res;
    }

    @Override
    public String getApiCode() {
        return "natureUser";
    }
}
