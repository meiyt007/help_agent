package com.zfsoft.superwindow.controller.sign;

import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.superwindow.data.wgpj.CaseSignRecord;
import com.zfsoft.superwindow.manager.sign.CaseSignRecordManager;
import com.zfsoft.superwindow.service.sign.CaseSignRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 办件签名记录表控制层
 *
 * @author wangwg
 * @date 2021-08-16
 */
@Slf4j
@RestController
public class CaseSignRecordController implements CaseSignRecordService {

    @Resource
    private CaseSignRecordManager caseSignRecordManager;


    @Override
    public CaseSignRecord queryById(Long id) {
        return null;
    }

    @Override
    public ApiResultSet<Map<String, String>> saveSignRecord(CaseSignRecord CaseSignRecord) {
        Map<String, String> map = caseSignRecordManager.saveSignRecord(CaseSignRecord);
        return new ApiResultSet(map);
    }

    @Override
    public ApiResultSet querySignImgPath(String caseOid) {
        String path = caseSignRecordManager.querySignImgPath(caseOid);
        return new ApiResultSet(path);
    }
}
