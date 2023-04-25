package com.zfsoft.ocr.controller.textInOcr;


import com.zfsoft.ocr.data.pojo.textInOcr.TextInOcrRequest;
import com.zfsoft.ocr.manager.common.OcrCallRecordManager;
import com.zfsoft.ocr.manager.textInOcr.TextInCommonUseRestManager;
import com.zfsoft.ocr.service.textInOcr.TextInOcrCommonUseRestService;
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
public class TextInOcrCommonUseRestController implements TextInOcrCommonUseRestService {
    @Resource
    private TextInCommonUseRestManager textInCommonUseRestManager;

    @Resource
    private OcrCallRecordManager ocrCallRecordManager;

    @Override
    public String customCommonUseOcr(TextInOcrRequest textInOcrRequest) {

        //OcrCallRecord ocrCallRecordqz=ocrCallRecordManager.queryDbOcrCallRecordByMd5()


        String result=textInCommonUseRestManager.customCommonUseOcr(textInOcrRequest);

        return result;
    }

  /*  @Resource
    private OcrCustomTemplateRestManager ocrCustomTemplateRestManager;

    
    @Override
    public OcrCustomTemplateResponse customTemplate(OcrCustomTemplateRequest ocrCustomTemplateRequest) {
        OcrCustomTemplateResponse ocrCustomTemplateResponse=ocrCustomTemplateRestManager.customTemplate(ocrCustomTemplateRequest);
        return ocrCustomTemplateResponse;
    }

    @Override
    public String getcs(String comboDireOid) {
        return "cs测试";
    }*/
}
