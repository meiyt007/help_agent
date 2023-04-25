package com.zfsoft.service.controller.sxService;

import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.service.manager.sxService.SxOptionFieldValManager;
import com.zfsoft.service.sxService.data.SxOptionFieldVal;
import com.zfsoft.service.sxService.service.SxOptionFieldValService;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@RestController
public class SxOptionFieldValController implements SxOptionFieldValService {

    @Resource
    private SxOptionFieldValManager sxOptionFieldValManager;

    @Override
    public ApiResultSet<Map<String, Object>> initOptionFieldValInfo(String serviceOid, String oid) {
        ApiResultSet<Map<String, Object>> apiResultSet = new ApiResultSet<>();
        Map<String, Object> result = sxOptionFieldValManager.initOptionFieldValInfo(serviceOid, oid);
        apiResultSet.setData(result);
        return apiResultSet;
    }

    @Override
    public ApiResultSet getFieldFillValList(String serviceOid) {
        ApiResultSet apiResultSet = new ApiResultSet();
        List<SxOptionFieldVal> sxOptionFieldVals = sxOptionFieldValManager.getFieldFillValList(serviceOid);
        apiResultSet.setData(sxOptionFieldVals);
        return apiResultSet;
    }

    @Override
    public ApiResultSet<SxOptionFieldVal> saveSxOptionFieldValInfo(SxOptionFieldVal sxOptionFieldVal) {
        ApiResultSet apiResultSet = new ApiResultSet();
        sxOptionFieldValManager.saveSxOptionFieldValInfo(sxOptionFieldVal);
        apiResultSet.setData(sxOptionFieldVal);
        return apiResultSet;
    }

    @Override
    public ApiResultSet delSxOptionFieldVal(String oid) {
        int i= sxOptionFieldValManager.delSxOptionFieldVal(oid);
        ApiResultSet apiResultSet = new ApiResultSet();
        apiResultSet.setData(i);
        return apiResultSet;
    }
}
