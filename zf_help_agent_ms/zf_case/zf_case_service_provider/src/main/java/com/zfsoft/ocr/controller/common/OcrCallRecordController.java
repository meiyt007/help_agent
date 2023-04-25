package com.zfsoft.ocr.controller.common;


import com.zfsoft.ocr.data.pojo.common.OcrCallRecord;
import com.zfsoft.ocr.manager.common.OcrCallRecordManager;
import com.zfsoft.ocr.service.common.OcrCallRecordService;
import com.zfsoft.platform.common.data.ApiResultSet;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


/***
* @Description:  ocr自定义模板识别
* @Author:liangss
* @Date:2021/10/27 
* @Param: 
*/
@RestController
@Slf4j
public class OcrCallRecordController implements OcrCallRecordService {
    @Resource
    private OcrCallRecordManager ocrCallRecordManager;


    @Override
    public String saveOrUpdateOcrCallRecord(OcrCallRecord ocrCallRecord) {
        return null;
    }

    @Override
    public ApiResultSet queryDbOcrCallRecordByMd5(String md5) {
        return null;
    }

    @Override
    public ApiResultSet queryDbOcrCallRecordByMd5AndInterUrl(String md5, String interUrl) {
        OcrCallRecord ocrCallRecord=  ocrCallRecordManager.queryDbOcrCallRecordByMd5AndInterUrl(md5,interUrl);
        ApiResultSet<OcrCallRecord> apiResultSet = new ApiResultSet<>();
        apiResultSet.setData(ocrCallRecord);
        return apiResultSet;
    }
}
