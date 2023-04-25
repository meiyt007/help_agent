package com.zfsoft.superwindow.controller.clzs;

import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.single.manager.clzs.StampRecordManager;
import com.zfsoft.superwindow.data.clzs.StampRecord;
import com.zfsoft.superwindow.service.clzs.StampRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author: wangwg
 * @create: 2021-01-11
 * @description: 样表信息控制层
 */
@Slf4j
@RestController
public class StampRecordController implements StampRecordService {

    @Resource
    private StampRecordManager stampRecordManager;

    @Override
    public ApiResultSet saveOrUpdate(StampRecord stampRecord) {
        stampRecordManager.saveOrUpdate(stampRecord);
        return null;
    }

    @Override
    public ApiResultSet getStampRecordByCataOidAndAttaOid(String cataOid, String attaOid) {
        StampRecord stampRecord = stampRecordManager.getStampRecordByCataOidAndAttaOid(cataOid, attaOid);
        ApiResultSet<StampRecord> apiResultSet = new ApiResultSet<>();
        apiResultSet.setData(stampRecord);
        return apiResultSet;
    }
}
