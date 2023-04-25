package com.zfsoft.service.controller.sxService;

import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.service.manager.sxService.SerFormManager;
import com.zfsoft.service.sxService.data.SerForm;
import com.zfsoft.service.sxService.service.SerFormService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 *
 * @ClassName SerFormController
 * @Description 实施清单 - 表单配置
 * @Author wangxl
 * @Date 2020-10-26
 * @Version V1.0
 **/
@RestController
@Slf4j
public class SerFormController implements SerFormService {
    @Resource
    private SerFormManager serFormManager;

    @Override
    public ApiResultSet<SerForm> getSerFormByServiceOid(String serviceOid) {
        SerForm serForm = serFormManager.getSerFormByService(serviceOid);
        ApiResultSet<SerForm> apiResultSet = new ApiResultSet<SerForm>();
        apiResultSet.setData(serForm);
        return apiResultSet;
    }
}
