package com.zfsoft.superwindow.controller.clzs;

import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.superwindow.data.clzs.OcrRecord;
import com.zfsoft.superwindow.manager.clzs.OcrRecordManager;
import com.zfsoft.superwindow.service.clzs.OcrRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author: wangwg
 * @create: 2021-01-11
 * @description: 材材料识别记录表控制层
 */
@Slf4j
@RestController
public class OcrRecordController implements OcrRecordService {

    @Resource
    private OcrRecordManager ocrRecordManager;

    @Override
    public ApiResultSet saveOrUpdate(OcrRecord ocrRecord){
        ocrRecordManager.saveOrUpdate(ocrRecord);
        return null;
    }
}
