package com.zfsoft.ocr.controller;


import com.zfsoft.ocr.data.pojo.ocr.OcrCustomTemplateRequest;
import com.zfsoft.ocr.data.pojo.ocr.OcrCustomTemplateResponse;
import com.zfsoft.ocr.manager.ocr.OcrCustomTemplateRestManager;
import com.zfsoft.ocr.service.ocr.OcrCustomTemplateRestService;
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
public class OcrCustomTemplateRestController implements OcrCustomTemplateRestService {
    @Resource
    private OcrCustomTemplateRestManager ocrCustomTemplateRestManager;

    
    @Override
    public OcrCustomTemplateResponse customTemplate(OcrCustomTemplateRequest ocrCustomTemplateRequest) {
        OcrCustomTemplateResponse ocrCustomTemplateResponse=ocrCustomTemplateRestManager.customTemplate(ocrCustomTemplateRequest);
        return ocrCustomTemplateResponse;
    }

    @Override
    public String getcs(String comboDireOid) throws Exception {
        ocrCustomTemplateRestManager.cs();
        return "cs测试";
    }
}
