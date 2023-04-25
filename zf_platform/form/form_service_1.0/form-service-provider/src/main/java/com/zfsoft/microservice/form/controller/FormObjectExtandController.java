package com.zfsoft.microservice.form.controller;

import com.zfsoft.microservice.form.data.FormObjectExtand;
import com.zfsoft.microservice.form.manager.FormObjectExtandManager;
import com.zfsoft.microservice.form.service.FormObjectExtandService;
import com.zfsoft.platform.common.data.ApiResultSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 存储对象扩展信息控制类实现
 */
@RestController
public class FormObjectExtandController implements FormObjectExtandService {

    @Autowired
    private FormObjectExtandManager formObjectExtandManager;

    @Override
    public ApiResultSet<List<FormObjectExtand>> queryFormObjectExtandList(String objectOid) {
        List<FormObjectExtand> formObjectExtands = formObjectExtandManager.queryFormObjectExtandList(objectOid,null);
        return new ApiResultSet<>(formObjectExtands);
    }
}
